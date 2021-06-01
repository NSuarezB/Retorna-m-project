package com.example.retornam20;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    /**
     * Creació de comptes d'usuari,
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            finish();
        }

        Button registration = findViewById(R.id.button2);
        registration.setOnClickListener(v -> {

            EditText email = findViewById(R.id.editTextTextEmailAddress3);
            EditText pass = findViewById(R.id.editTextTextPassword);
            EditText nom = findViewById(R.id.editTextTextPersonName4);

            if (TextUtils.isEmpty(nom.getText().toString())) {
                nom.setError("Ha d'introduïr un nom");
                return;
            }

            if (TextUtils.isEmpty(email.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                email.setError("L'adreça electrònica inserida és incorrecte");
                return;
            }

            if (pass.getText().length() < 8) {
                pass.setError("La contraseya a de ser de 8 caracters o més");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                    .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("REGISTRATION", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference usuaris = db.collection("usuaris");

                                Map<String, Object> usuari = new HashMap<>();
                                usuari.put("id", user.getUid());
                                usuari.put("nom", nom.getText().toString());
                                usuari.put("data", Timestamp.now());
                                usuari.put("email", email.getText().toString());
                                usuaris.document(user.getUid()).set(usuari);

                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("REGISTRATION", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }

                        private void updateUI(FirebaseUser user) {
                            if (user != null) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });
        });


    }


}