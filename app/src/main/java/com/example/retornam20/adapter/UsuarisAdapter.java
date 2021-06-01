package com.example.retornam20.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retornam20.NouPrestecActivity;
import com.example.retornam20.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class UsuarisAdapter extends RecyclerView.Adapter<UsuarisAdapter.ViewHolder> {

    private final List<Map<String, Object>> localDataSet;
    private final String objecteId;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private String currentId;
        private String objecteId;
        private final Context context;

        public ViewHolder(View view, String objecte) {
            super(view);
            // Define click listener for the ViewHolder's View

            context = view.getContext();
            objecteId = objecte;
            textView = view.findViewById(R.id.titolObjecte);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("SELECT_USUARI", "TOUCH EVENT: " + getAdapterPosition() + " USUARI: " + currentId + " OBJECT: " + objecteId);

                    Context context = v.getContext();
                    Intent i = new Intent(context, NouPrestecActivity.class);
                    i.putExtra("usuari", currentId);
                    i.putExtra("objecte", objecteId);
                    context.startActivity(i);
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }

        public void setCurrentId(String id) {
            currentId = id;
        }
    }

    /**
     * Inicialitza la llista d'usuaris
     *
     * @param dataSet List<Map<String, Object>> conté les converses en format id => document
     */
    public UsuarisAdapter(List<Map<String, Object>> dataSet, String objecte) {
        localDataSet = dataSet;
        objecteId = objecte;
    }

    @NonNull
    @NotNull
    @Override
    public UsuarisAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_select_objecte, parent, false);

        return new UsuarisAdapter.ViewHolder(view, objecteId);
    }

    @Override
    public void onBindViewHolder(@NotNull UsuarisAdapter.ViewHolder viewHolder, final int position) {

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
}
