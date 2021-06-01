package com.example.retornam20;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retornam20.adapter.DeixatsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class DeixatsContentFragment extends Fragment {

    private static final String TAG = "TAG";

    public static DeixatsContentFragment newInstance() {
        return new DeixatsContentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_deixats, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference objectes = db.collection("prestecs");

        if (user == null) {
            return layout;
        }


        RecyclerView mRecyclerView = layout.findViewById(R.id.llistaDeixats);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        List<Map<String, Object>> listPrestecs = new ArrayList<>();
        objectes.whereEqualTo("usuari", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("HISTORY_PRESTEC", document.getId() + " => " + document.getData());

                            Map<String, Object> data = document.getData();
                            data.put("id", document.getId());

                            if (document.getData().get("dataRetornat") == null) {
                                listPrestecs.add(data);
                            }

                            Log.d(TAG, "AQUI ES DONDE TENEMOS QUE AÑADIR LOS DATOS???");
                        }

                        DeixatsAdapter mAdapter = new DeixatsAdapter(listPrestecs);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Log.d("SELECT_PRESTEC", "Error getting documents: ", task.getException());
                    }
                });

        return layout;
    }


}