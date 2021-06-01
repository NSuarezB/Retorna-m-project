package com.example.retornam20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.retornam20.dialog.DatePickerFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NouPrestecActivity extends AppCompatActivity {

    public static final String LOG_TAG_PRESTEC = "NOU_PRESTEC";
    Timestamp devolucioTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nou_prestec);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        String objecteId = extras.getString("objecte");
        Log.d(LOG_TAG_PRESTEC, "Objecte seleccionat: " + objecteId);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference objectes = db.collection("objectes");
        CollectionReference prestecs = db.collection("prestecs");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        objectes.document(objecteId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(LOG_TAG_PRESTEC, "DocumentSnapshot data: " + document.getData());

                    TextView nomObjecte = findViewById(R.id.nomObjecteTextView);
                    nomObjecte.setText(document.get("nom").toString());
                } else {
                    Log.d(LOG_TAG_PRESTEC, "No such document");
                }
            } else {
                Log.d(LOG_TAG_PRESTEC, "get failed with ", task.getException());
            }
        });

        /////////
        // EVENTS
        /////////

        /**
         * 
         * Guardar els nous prestecs creats a la base de dades:Firebase
         * 
         */
        EditText devolucioPicker = findViewById(R.id.editTextDate);
        devolucioPicker.setOnClickListener(v -> {
            DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                devolucioPicker.setText(selectedDate);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                devolucioTimestamp = new Timestamp(calendar.getTime());
            });

            newFragment.show(this.getSupportFragmentManager(), "datePicker");
        });

        Button crearPrestecView = findViewById(R.id.button4);
        crearPrestecView.setOnClickListener(v -> {
            EditText prestat = findViewById(R.id.editTextTextPersonName5);

            Map<String, Object> prestec = new HashMap<>();
            prestec.put("objecte", objecteId);
            prestec.put("usuari", user.getUid());
            prestec.put("usuariPrestat", prestat.getText().toString());
            prestec.put("dataLimit", devolucioTimestamp);
            prestec.put("dataPrestec", Timestamp.now());
            
            prestecs.add(prestec)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(LOG_TAG_PRESTEC, "DocumentSnapshot added with ID: " + documentReference.getId());

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Log.w(LOG_TAG_PRESTEC, "Error adding document", e));
        });
    }
}