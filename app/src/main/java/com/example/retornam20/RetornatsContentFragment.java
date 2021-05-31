package com.example.retornam20;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

public class RetornatsContentFragment extends Fragment {

    private static final String TEXT = "text";

    public static RetornatsContentFragment newInstance() {
        RetornatsContentFragment frag = new RetornatsContentFragment();

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_retornats, container, false);

        if (getArguments() != null) {
            ((TextView) layout.findViewById(R.id.text)).setText(getArguments().getString(TEXT));
        }

        /*FirebaseAuth auth = FirebaseAuth.getInstance();
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
                        }

                        ObjectesLlistaAdapter mAdapter = new ObjectesLlistaAdapter(listObjects);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Log.d("SELECT_OBJECTE", "Error getting documents: ", task.getException());
                    }
                });*/

        return layout;
    }
}