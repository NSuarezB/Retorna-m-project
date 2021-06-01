package com.example.retornam20;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retornam20.adapter.ObjectesLlistaAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class ObjectesContentFragment extends Fragment {

    private static final String TEXT = "text";

    public static ObjectesContentFragment newInstance() {
        ObjectesContentFragment frag = new ObjectesContentFragment();

        return frag;
    }


    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_objectes, container, false);

        if (getArguments() != null) {
            ((TextView) layout.findViewById(R.id.text)).setText(getArguments().getString(TEXT));
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference objectes = db.collection("objectes");

        RecyclerView mRecyclerView = layout.findViewById(R.id.llistaObjectes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
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
                            Log.d(TAG, "Esto es el objetesFragment despues de añadir data a la listObject AAAAAIIIXX");
                        }

                        ObjectesLlistaAdapter mAdapter = new ObjectesLlistaAdapter(listObjects);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Log.d("SELECT_OBJECTE", "Error getting documents: ", task.getException());
                    }
                });

        return layout;
    }
}