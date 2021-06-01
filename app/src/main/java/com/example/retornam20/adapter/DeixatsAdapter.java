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

import com.example.retornam20.MainActivity;
import com.example.retornam20.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DeixatsAdapter extends RecyclerView.Adapter<DeixatsAdapter.ViewHolder> {

    private final List<Map<String, Object>> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private String currentId;
        private final Context context;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            context = view.getContext();
            textView = view.findViewById(R.id.textView8);
            imageView = view.findViewById(R.id.imageView5);

            Button btn = view.findViewById(R.id.button6);
            btn.setOnClickListener(t -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("objectes").document(currentId)
                        .update("retornat", Timestamp.now())
                        .addOnSuccessListener(aVoid -> {
                            Log.d("LLISTA_OBJECTES", "prestec successfully retornat!");

                            Intent i = new Intent(context, MainActivity.class);
                            context.startActivity(i);
                        })
                        .addOnFailureListener(e -> Log.w("LLISTA_OBJECTES", "Error retornant prestec", e));
            });
        }

        public TextView getTextView() {
            return textView;
        }

        public void setCurrentId(String id) {
            currentId = id;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public DeixatsAdapter(List<Map<String, Object>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @NotNull
    @Override
    public DeixatsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_select_prestec, parent, false);

        return new DeixatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeixatsAdapter.ViewHolder viewHolder, final int position) {
        Map<String, Object> prestec = localDataSet.get(position);

        String objecteId = (String) prestec.get("objectes");
        String id = (String) prestec.get("id");
        String nomObject = (String) prestec.get("objecte");

        viewHolder.setCurrentId(id);

        viewHolder.getTextView().setText(nomObject);
        Log.d(TAG,"Esto es para saber donde me encuentro: "+ nomObject);
        viewHolder.setCurrentId(id);

        if (objecteId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference objectes = db.collection("objectes");

            objectes.document(objecteId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("DEIXATS_OBJECTE", "Dades obtingudes: " + document.getData());

                                String imageRef = (String) document.get("foto");
                                if (imageRef != null) {
                                    //carregaFotoAlItem(viewHolder, imageRef);
                                }
                            } else {
                                Log.d("DEIXATS_OBJECTE", "no s'ha trobat el objecte");
                            }
                        } else {
                            Log.d("DEIXATS_OBJECTE", "obtenir el objecte, ha fallta ", task.getException());
                        }



                    });
        }



    }

    /**
    * Carrega una referencia a una foto de la Firestore Storage a un ImageView
    *
    * @param viewHolder ObjectesAdapter.ViewHolder suport de la vista on esta el ImageView
    * @param imageRef String referencia de la imatge a la Firestore (images/image:XXXX)
    */
    private void carregaFotoAlItem(DeixatsAdapter.ViewHolder viewHolder, String imageRef) {
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
                Toast.makeText(viewHolder.context, "No s'ha trobat la imatge de la db: " + imageRef, Toast.LENGTH_LONG).show();
Log.d("CARREGA_FOTO", exception.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
