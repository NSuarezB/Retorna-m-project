package com.example.retornam20;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class NouObjecteActivity extends AppCompatActivity {
    Uri chosenImage;
    RatingBar valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nou_objecte);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        CollectionReference objectes = db.collection("objectes");

        valor = (RatingBar) findViewById(R.id.ratingBar);


        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            finish();
            return;
        }

        ImageView ivImage = (ImageView) findViewById(R.id.imageView4);
        ActivityResultLauncher<String> pickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            Log.d("CREAR_OBJECTE", result.toString());
                            ivImage.setImageURI(result);
                            chosenImage = result;
                        }
                    }
                }
        );
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerLauncher.launch("image/*");
            }
        });

        /**
         *
         *  Guarda els objectes creats a la base de dades:Firebase
         *
         */

        Button crearObjecteButton = (Button)findViewById(R.id.button4);
        crearObjecteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"BOTO TOCAT!!!!!!!!!!!");
                EditText descripcio = (EditText) findViewById(R.id.editTextTextPersonName5);
                EditText nom = (EditText) findViewById(R.id.editTextTextPersonName3);
                valor = (RatingBar) findViewById(R.id.ratingBar);

                Map<String, Object> objecte = new HashMap<>();
                objecte.put("descripcio", descripcio.getText().toString());
                objecte.put("nom", nom.getText().toString());
                objecte.put("valor", valor.getNumStars());
                objecte.put("propietari", user.getUid());
                objecte.put("dataCreacio", Timestamp.now());

                if (chosenImage != null) {
                    StorageReference storageImage = storage.getReference().child("images/" + chosenImage.getLastPathSegment());
                    UploadTask uploadTask = storageImage.putFile(chosenImage);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.d(TAG,"HA FALLADO TTTTTTTTTTTTTTTT");

                        }
                    });

                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                            Log.d(TAG,"CONSEGUIDO xxxxxxxxxxxdddddddddddddd");

                        }
                    });

                    objecte.put("foto", "images/" + chosenImage.getLastPathSegment());
                }

                objectes.add(objecte)
                        .addOnSuccessListener(documentReference -> {
                            Log.d("CREAR_OBJECTE", "DocumentSnapshot added with ID: " + documentReference.getId());
                            Intent data = new Intent();
                            data.putExtra("objecte", documentReference.getId());
                            setResult(RESULT_OK, data);
                        })
                        .addOnFailureListener(e -> Log.w("CREAR_OBJECTE", "Error adding document", e));
            }
        });

    }

}