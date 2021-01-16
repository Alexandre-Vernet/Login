package com.ynov.vernet.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPsw;
    TextView textViewRegister;
    ProgressBar progressBar;

    private FirebaseAuth fAuth;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        // If user is already logged
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPsw = findViewById(R.id.editTextPsw);
        textViewRegister = findViewById(R.id.textViewRegister);
        progressBar = findViewById(R.id.progressBar);

        // If user has already connected
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String data = sharedPref.getString("email", null);
        editTextEmail.setText(data);


        findViewById(R.id.btnLogin).setOnClickListener(v -> {

            // Get login field
            String email = editTextEmail.getText().toString();
            String password = editTextPsw.getText().toString();
            progressBar.setVisibility(View.VISIBLE);

            // Enter a valid email & psw
            if (email.isEmpty()) {
                editTextEmail.setError("Texbox cannot be empty !");
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            if (password.isEmpty()) {
                editTextPsw.setError("Texbox cannot be empty !");
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            if (password.length() < 6) {
                editTextPsw.setError("Password must contain 6 characters");
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }

            fAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Save login in memory
                            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("email", email);
                            editor.apply();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        });

        // Register
        textViewRegister.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });
    }
}