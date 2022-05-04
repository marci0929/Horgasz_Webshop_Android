package com.example.kotprog_horgasz_webshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Regisztracio extends AppCompatActivity {

    private static final String LOG_TAG = Regisztracio.class.getName();
    private static final String PREF_KEY = Regisztracio.class.getPackage().toString();
    private static final int SECRET_KEY = 153;
    private FirebaseAuth fireBaseAuth;
    private SharedPreferences preferences;

    EditText username;
    EditText email;
    EditText password;
    EditText passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisztracio);
        int secret_key = getIntent().getIntExtra("KULCS", 0);

        if(secret_key != 153){
            finish();
        }

        username = findViewById(R.id.usernameText);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        passwordConfirm = findViewById(R.id.passwordConfirmText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String uname = preferences.getString("username", "");
        String pass = preferences.getString("password", "");

        username.setText(uname);
        password.setText(pass);
        passwordConfirm.setText(pass);

        fireBaseAuth = FirebaseAuth.getInstance();
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void makeRegistration(View view) {
        String m_userName = username.getText().toString();
        String m_email = email.getText().toString();
        String m_password = password.getText().toString();
        String m_passwordConfirm = passwordConfirm.getText().toString();

        if (!m_password.equals(m_passwordConfirm)) {
            Log.e(LOG_TAG, "Nem egyeznek a jelszavak!");
            return;
        }

        Log.i(LOG_TAG, "Regisztrált: " + m_userName + ", e-mail: " + m_email);

        fireBaseAuth.createUserWithEmailAndPassword(m_email, m_password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d(LOG_TAG, "Felhasználó létrehozva!");
                redirectToLogin();
            } else {
                Log.d(LOG_TAG, "Nem sikerült a felhasználót létrehozni!"+ task.getException().getMessage());
                Toast.makeText(Regisztracio.this, "Nem sikerült a felhasználót létrehozni: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}