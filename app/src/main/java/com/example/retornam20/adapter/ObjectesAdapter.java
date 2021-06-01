package com.example.retornam20.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retornam20.R;
import com.example.retornam20.SelectUsuariActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ObjectesAdapter extends RecyclerView.Adapter<ObjectesAdapter.ViewHolder> {

    private final List<Map<String, Object>> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        private String currentId;
        private final Context context;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            context = view.getContext();
            textView = view.findViewById(R.id.titolObjecte);
            imageView = view.findViewById(R.id.imageView5);
            Button btn = view.findViewById(R.id.button6);
            btn.setVisibility(View.GONE);
            Button btn2 = view.findViewById(R.id.button8);
            btn2.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("SELECT_OBJECTE", "TOUCH EVENT: " + getAdapterPosition() + " OBJECT: " + currentId);

                    Context context = v.getContext();
                    Intent i = new Intent(context, SelectUsuariActivity.class);
                    i.putExtra("objecte", currentId);
                    context.startActivity(i);
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setCurrentId(String id) {
            currentId = id;
        }
    }

    /**
     * Inicialitza la llista d'objectes interna de l'adapter
     *
     * @param dataSet List<Map<String, Object>> conté els objectes nen format id => document
     */
    public ObjectesAdapter(List<Map<String, Object>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @NotNull
    @Override
    public ObjectesAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_select_objecte, parent, false);

        return new ObjectesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ObjectesAdapter.ViewHolder viewHolder, final int position) {

        Map<String, Object> objecte = localDataSet.get(position);
        String nom = (String) objecte.get("nom");
        String id = (String) objecte.get("id");
        String imageRef = (String) objecte.get("foto");

        // Si tenim foto:
        if (imageRef != null) {
            carregaFotoAlItem(viewHolder, imageRef);
        }

        viewHolder.getTextView().setText(nom);
        viewHolder.setCurrentId(id);
    }

    /**
    * Carrega una referencia a una foto de la Firestore Storage a un ImageView
    *
    * @param viewHolder ObjectesAdapter.ViewHolder suport de la vista on esta el ImageView
    * @param imageRef String referencia de la imatge a la Firestore (images/image:XXXX)
    */
    private void carregaFotoAlItem(ObjectesAdapter.ViewHolder viewHolder, String imageRef) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child(imageRef);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(10*ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    viewHolder.getImageView().setImageBitmap(bmp);

                }
        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(viewHolder.context, "No Such file or Path found: " + imageRef, Toast.LENGTH_LONG).show();

    Log.d("CARREGA_FOTO", exception.getMessage());            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
