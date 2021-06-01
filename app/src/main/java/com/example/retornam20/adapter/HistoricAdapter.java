package com.example.retornam20.adapter;

import android.content.Context;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class HistoricAdapter extends RecyclerView.Adapter<HistoricAdapter.ViewHolder> {

    /**
     * Carrega una referencia a una foto de la Firestore Storage a un ImageView
     *
     * @param viewHolder ObjectesAdapter.ViewHolder suport de la vista on esta el ImageView
     * @param imageRef   String referencia de la imatge a la Firestore (images/image:XXXX)
     */
    final static long ONE_MEGABYTE = 1024 * 1024;
    private static final String TAG_HISTORIC_PRESTECS = "HISTORY_PRESTECS";
    private final List<Map<String, Object>> localDataSet;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public HistoricAdapter(List<Map<String, Object>> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @NotNull
    @Override
    public HistoricAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_select_prestec, parent, false);

        return new HistoricAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoricAdapter.ViewHolder viewHolder, final int position) {
        Map<String, Object> prestec = localDataSet.get(position);

        String objecteId = (String) prestec.get("objecte");
        String prestatId = (String) prestec.get("usuari");
        String id = (String) prestec.get("id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        viewHolder.setCurrentId(id);
        viewHolder.setDataPrestat((Timestamp) prestec.get("dataPrestec"));
        viewHolder.setDataDevolucio((Timestamp) prestec.get("dataLimit"));

        if (objecteId != null) {
            db.collection("objectes")
                    .document(objecteId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG_HISTORIC_PRESTECS, "Dades obtingudes: " + document.getData());

                                String imageRef = (String) document.get("foto");
                                if (imageRef != null) {
                                    carregaFotoAlItem(viewHolder, imageRef);
                                }

                                viewHolder.setNom((String) document.get("nom"));
                            } else {
                                Log.d(TAG_HISTORIC_PRESTECS, "no s'ha trobat el objecte: " + objecteId);
                            }
                        } else {
                            Log.d(TAG_HISTORIC_PRESTECS, "obtenir el objecte, ha fallta ", task.getException());
                        }
                    });
        } else {
            Log.d(TAG_HISTORIC_PRESTECS, "no hi ha objecte!!");
        }

        if (prestatId != null) {
            db.collection("usuaris")
                    .document(prestatId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG_HISTORIC_PRESTECS, "Dades obtingudes: " + document.getData());

                                String imageRef = (String) document.get("foto");
                                if (imageRef != null) {
                                    carregaFotoAlItem(viewHolder, imageRef);
                                }

                                viewHolder.setNomPersona((String) document.get("nom"));
                            } else {
                                Log.d(TAG_HISTORIC_PRESTECS, "no s'ha trobat el usuari: " + prestatId);
                            }
                        } else {
                            Log.d(TAG_HISTORIC_PRESTECS, "obtenir el usuari, ha fallta ", task.getException());
                        }
                    });
        } else {
            Log.d(TAG_HISTORIC_PRESTECS, "no hi ha prestatari!!");
        }
    }

    private void carregaFotoAlItem(HistoricAdapter.ViewHolder viewHolder, String imageRef) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child(imageRef);


        photoReference.getBytes(10 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                viewHolder.getImageView().setImageBitmap(decoded);

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nomObjecte;
        private final TextView nomPersona;
        private final TextView dataPrestec;
        private final TextView dataDevolucio;
        private final Context context;
        private String currentId;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            context = view.getContext();
            imageView = view.findViewById(R.id.imageView5);

            nomObjecte = view.findViewById(R.id.textView8);
            nomPersona = view.findViewById(R.id.textViewPersonaPRestada);
            dataPrestec = view.findViewById(R.id.textViewDataPrestec);
            dataDevolucio = view.findViewById(R.id.textViewDataDevolucio);

            Button btn = view.findViewById(R.id.button6);
            btn.setVisibility(View.GONE);
        }

        public void setCurrentId(String id) {
            currentId = id;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setNom(String nom) {
            nomObjecte.setText(nom);
        }

        public void setNomPersona(String email) {
            nomPersona.setText(email);
        }

        public void setDataPrestat(Timestamp data) {
            if (data == null) {
                dataPrestec.setText("--");
                return;
            }

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            dataPrestec.setText(format.format(data.toDate()));
        }

        public void setDataDevolucio(Timestamp data) {
            if (data == null) {
                dataDevolucio.setText("--");
                return;
            }

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            dataDevolucio.setText(format.format(data.toDate()));
        }
    }
}
