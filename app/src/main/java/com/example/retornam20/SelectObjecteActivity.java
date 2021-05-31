package com.example.retornam20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retornam20.adapter.ObjectesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectObjecteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_objecte);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference objectes = db.collection("objectes");

        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            finish();
            return;
        }

        RecyclerView mRecyclerView = findViewById(R.id.llistaObjectes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        List<Map<String, Object>> listObjects = new ArrayList<>();
        objectes.whereEqualTo("propietari", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("SELECT_OBJECTE", document.getId() + " => " + document.getData());

                    Map<String, Object> data = document.getData();
                    data.put("id", document.getId());
                    listObjects.add(data);
                }

                ObjectesAdapter mAdapter = new ObjectesAdapter(listObjects);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Log.d("SELECT_OBJECTE", "Error getting documents: ", task.getException());
            }
        });

        Button nouObjecte = findViewById(R.id.button5);
        nouObjecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NouObjecteActivity.class);
                startActivity(i);
            }
        });
    }
}


