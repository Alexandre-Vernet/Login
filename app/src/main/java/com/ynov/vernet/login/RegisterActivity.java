package com.ynov.vernet.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextLastName, editTextFirstName, editTextPhone, editTextEmail, editTextPsw;
    TextView textViewLogin;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // If user is already login
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }

        editTextLastName = findViewById(R.id.editTextLastName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPsw = findViewById(R.id.editTextPsw);
        progressBar = findViewById(R.id.progressBar);
        textViewLogin = findViewById(R.id.textViewLogin);

        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            // Get data from field
            String email = editTextEmail.getText().toString();
            String password = editTextPsw.getText().toString();

            String lastName = editTextLastName.getText().toString();
            String firstName = editTextFirstName.getText().toString();
            String phone = editTextPhone.getText().toString();

            // Enter a valid email & psw
            if (email.isEmpty()) {
                editTextEmail.setError("Texbox cannot be empty !");
                return;
            }
            if (password.isEmpty()) {
                editTextPsw.setError("Texbox cannot be empty !");
                return;
            }
            if (password.length() < 6) {
                editTextPsw.setError("Password must contain 6 characters");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);


            // Create user with Email & psw
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Create a new user with data
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("firstName", firstName);
                            user.put("lastName", lastName);
                            user.put("phone", phone);

                            // Store data
                            fStore.collection("users")
                                    .document(mAuth.getUid())
                                    .set(user)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Error adding document", e);
                                        }
                                    });

                            // Start MainActivity
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);

                        }
                    });
        });


        // Login
        textViewLogin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

    }
}