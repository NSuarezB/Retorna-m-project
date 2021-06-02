package com.example.retornam20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NouAmicActivity extends AppCompatActivity {
    public static final String CREAR_AMIC = "CREAR_OBJECTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nou_amic);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference amics = db.collection("amics");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        Button crearAmicBtn = findViewById(R.id.button4);
        crearAmicBtn.setOnClickListener(v -> {
            EditText emailUsuari = findViewById(R.id.amicMailTextView);

            db.collection("usuaris")
                    .whereEqualTo("email", emailUsuari.getText().toString())
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() == null || task.getResult().isEmpty()) {
                                emailUsuari.setError("L'usuari no està registrat a la plataforma");
                                return;
                            }

                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            Log.d(CREAR_AMIC, document.getId() + " => " + document.getData());
                            String amicId = document.getId();

                            Map<String, Object> amic = new HashMap<>();
                            amic.put("usuari", user.getUid());
                            amic.put("amic", amicId);

                            amics.add(amic)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(CREAR_AMIC, "DocumentSnapshot added with ID: " + documentReference.getId());

                                        // Retornar a seleccionar amic
                                        Intent data = new Intent();
                                        data.putExtra("amic", amicId);

                                        setResult(RESULT_OK, data);

                                        finish();
                                    })
                                    .addOnFailureListener(e -> Log.w(CREAR_AMIC, "Error adding document", e));

                        } else {
                            Log.d(CREAR_AMIC, "Error obtenint amic: ", task.getException());
                        }
                    });
        });

    }

}