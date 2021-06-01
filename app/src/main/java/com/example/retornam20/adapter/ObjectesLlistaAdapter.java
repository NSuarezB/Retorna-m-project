package com.example.retornam20.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retornam20.MainActivity;
import com.example.retornam20.R;
import com.example.retornam20.SelectUsuariActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ObjectesLlistaAdapter extends RecyclerView.Adapter<ObjectesLlistaAdapter.ViewHolder> {

    private final List<Map<String, Object>> localDataSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public ObjectesLlistaAdapter(List<Map<String, Object>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @NotNull
    @Override
    public ObjectesLlistaAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_select_objecte, parent, false);

        return new ObjectesLlistaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ObjectesLlistaAdapter.ViewHolder viewHolder, final int position) {
        Map<String, Object> objecte = localDataSet.get(position);
        String nom = (String) objecte.get("nom");
        String id = (String) objecte.get("id");

        viewHolder.getTextView().setText(nom);
        viewHolder.setCurrentId(id);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Context context;
        private String currentId;

        public ViewHolder(View view) {
            super(view);

            context = view.getContext();
            textView = view.findViewById(R.id.titolObjecte);

            /**
             * Eliminem la dada de la base de dades
             */
            Button btn = view.findViewById(R.id.button6);
            btn.setOnClickListener(t -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("objectes").document(currentId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d("LLISTA_OBJECTES", "DocumentSnapshot successfully deleted!");

                            Intent i = new Intent(context, MainActivity.class);
                            context.startActivity(i);
                        })
                        .addOnFailureListener(e -> Log.w("LLISTA_OBJECTES", "Error deleting document", e));
            });
            /**
             * Prestem el objecte
             */
            Button btn2 = view.findViewById(R.id.button8);
            btn2.setOnClickListener(t -> {
                Intent i = new Intent(context, SelectUsuariActivity.class);
                i.putExtra("objecte", currentId);
                context.startActivity(i);
            });
        }

        public TextView getTextView() {
            return textView;
        }

        public void setCurrentId(String id) {
            currentId = id;
        }
    }
}
