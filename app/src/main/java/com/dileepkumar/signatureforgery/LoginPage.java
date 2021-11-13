package com.dileepkumar.signatureforgery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

import java.util.Objects;

public class LoginPage extends AppCompatActivity {

    TextView tvNotRegister, tvLoginErrorLog;
    EditText etLoginEmail, etLoginPassword;

    LoadingDialog loadingDialog;

    private FirebaseAuth mAuth;
    private final String TAG = "SignInUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        // Remove Dark Theme From Android
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        tvNotRegister = findViewById(R.id.tvNotRegister);
        tvLoginErrorLog = findViewById(R.id.tvLoginErrorLog);
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);

        tvLoginErrorLog.setVisibility(View.GONE);

        loadingDialog = new LoadingDialog(this);

        setTvNotRegister();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    public void setTvNotRegister() {
        tvNotRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationPage.class);
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    public void btnSignIn(View view) {
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            tvLoginErrorLog.setVisibility(View.VISIBLE);
            tvLoginErrorLog.setText("Please Fill In The All The Fields");
            tvLoginErrorLog.setTextColor(Color.RED);
        } else {
            tvLoginErrorLog.setVisibility(View.GONE);
            loadingDialog.startLoadingDialog();
            tryToLogin(email, password);
        }
    }

    private void tryToLogin(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        tvLoginErrorLog.setVisibility(View.GONE);
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        goToMainPage();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginPage.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        tvLoginErrorLog.setVisibility(View.VISIBLE);
                        tvLoginErrorLog.setText(
                                Objects.requireNonNull(task.getException()).getMessage());
                        tvLoginErrorLog.setTextColor(Color.RED);
                        loadingDialog.dismissDialog();
                    }
                });

    }

    private void goToMainPage() {
        loadingDialog.dismissDialog();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}