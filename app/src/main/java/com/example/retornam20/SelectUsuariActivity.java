package com.example.retornam20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retornam20.adapter.UsuarisAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectUsuariActivity extends AppCompatActivity {

    public static final String TAG = "SELECT_AMIC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_usuari);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e(TAG, "Cal passar el objecte!!");
            finish();
            return;
        }
        String objecteId = extras.getString("objecte");
        Log.d(TAG, "Objecte seleccionat: " + objecteId);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference amics = db.collection("amics");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e(TAG, "Sense sessio iniciada!!");
            finish();
            return;
        }

        RecyclerView mRecyclerView = findViewById(R.id.llistaAmics);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        List<Map<String, Object>> listAmics = new ArrayList<>();
        UsuarisAdapter mAdapter = new UsuarisAdapter(listAmics, objecteId);

        Log.d(TAG, "Llista amics buida?" + listAmics);
        amics.whereEqualTo("usuari", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "successful amics 1" + user.getUid());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());

                            db.collection("usuaris")
                                    .whereEqualTo("id", document.getData().get("amic"))
                                    .limit(1)
                                    .get()
                                    .addOnCompleteListener(taskAmic -> {
                                        Log.d(TAG, "Estoy dentro y funciono he conseguido estar en el addOnCompleteListener");
                                        if (taskAmic.isSuccessful()) {
                                            if (taskAmic.getResult() == null || taskAmic.getResult().isEmpty()) {
                                                return;
                                            }

                                            DocumentSnapshot documentUsuariAmic = taskAmic.getResult().getDocuments().get(0);
                                            Log.d(TAG, "Que contiene el DocumentSnapshot? :" + documentUsuariAmic);
                                            Map<String, Object> data = documentUsuariAmic.getData();
                                            Log.d(TAG, "Que contiene data para que no salga?" + data);
                                            // data.put("id", documentUsuariAmic.getId());
                                            Log.d(TAG, "Segunda vez que vemos el data data.put: " + data);
                                            listAmics.add(data);

                                            mAdapter.notifyDataSetChanged();
                                        } else {
                                            Log.d(TAG, "Error obtenint amic: ", task.getException());
                                        }
                                    });
                        }

                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Log.d(TAG, "Error obtenint amics: ", task.getException());
                    }
                });

        Button nouAmic = findViewById(R.id.addNouAmicBtn);
        nouAmic.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), NouAmicActivity.class);
            startActivityForResult(i, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String amicId = data.getStringExtra("amic");
                String proba = "Holiii";

                Intent i = new Intent(getApplicationContext(), NouPrestecActivity.class);

                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    i.putExtra("objecte", extras.getString("objecte"));
                }

                i.putExtra("amic", amicId);


                startActivity(i);
            }
        }
    }
}


