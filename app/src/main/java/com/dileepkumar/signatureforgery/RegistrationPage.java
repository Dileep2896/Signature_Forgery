package com.dileepkumar.signatureforgery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class RegistrationPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText etRegisterEmail, etRegisterPassword, etRegisterRePassword, etRegisterUserName;
    TextView tvErrorLog;
    String email, password, re_password, username;

    private final String TAG = "UserRegistration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        mAuth = FirebaseAuth.getInstance();

        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterRePassword = findViewById(R.id.etRegisterRePassword);
        etRegisterUserName = findViewById(R.id.etRegisterUserName);
        tvErrorLog = findViewById(R.id.tvErrorLog);

        tvErrorLog.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(getBaseContext(), "User Is Already Logged In", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public void btnSignUP(View view) {
        email = etRegisterEmail.getText().toString();
        password = etRegisterPassword.getText().toString();
        re_password = etRegisterRePassword.getText().toString();
        username = etRegisterUserName.getText().toString();

        if (email.isEmpty() || password.isEmpty() || re_password.isEmpty() || username.isEmpty()) {
            tvErrorLog.setVisibility(View.VISIBLE);
            tvErrorLog.setText("Please Fill All The Fields");
            tvErrorLog.setTextColor(Color.RED);
        } else if (!password.equals(re_password)) {
            tvErrorLog.setVisibility(View.VISIBLE);
            tvErrorLog.setText("Password & Re-Password Is Not Same.");
            tvErrorLog.setTextColor(Color.RED);
        } else {
            tvErrorLog.setVisibility(View.GONE);
            createNewUser(email, password, username);
        }

    }

    public void createNewUser(String email, String password, String username) {
        Log.i(TAG, email + " - " + password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        tvErrorLog.setVisibility(View.GONE);
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username).build();
                        Objects.requireNonNull(user).updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        tvErrorLog.setVisibility(View.GONE);
                                        Log.d(TAG, "User profile updated.");
                                        goToMainPage();
                                    } else {
                                        tvErrorLog.setVisibility(View.VISIBLE);
                                        tvErrorLog.setText(Objects.requireNonNull(task1
                                                .getException()).getMessage());
                                        tvErrorLog.setTextColor(Color.RED);
                                    }
                                });
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegistrationPage.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        tvErrorLog.setVisibility(View.VISIBLE);
                        tvErrorLog.setText(Objects.requireNonNull(task.getException()).getMessage());
                        tvErrorLog.setTextColor(Color.RED);
                    }
                });
    }

    public void goToMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}