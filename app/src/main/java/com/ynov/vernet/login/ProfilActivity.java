package com.ynov.vernet.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfilActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    TextView textViewFirstName, textViewLastName, textViewPhone;

    private static final String TAG = "ProfilActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String userId = fAuth.getCurrentUser().getUid();

        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewLastName = findViewById(R.id.textViewLastName);
        textViewPhone = findViewById(R.id.textViewPhone);

        // Display information of logged user
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (value, error) -> {
            textViewFirstName.setText(value.getString("firstName"));
            textViewLastName.setText(value.getString("lastName"));
            textViewPhone.setText(value.getString("phone"));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}