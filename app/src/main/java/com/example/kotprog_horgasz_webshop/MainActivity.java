package com.example.kotprog_horgasz_webshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int REQUEST_CODE = 111;
    private static final int KULCS = 153;

    EditText emailAddress;
    EditText password;

    private FirebaseAuth fireBaseAuth;
    private GoogleSignInClient googleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        emailAddress = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);

        fireBaseAuth = FirebaseAuth.getInstance();

        //nem működik a requestIdToken(getString(R.string.default_web_client_id)) módszer, így hardkódolva lett
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("633488002143-m89gn2ki93v6ukgnlhudeiabkpfu0te0.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignIn = GoogleSignIn.getClient(this, gso);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(LOG_TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fireBaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {

                Log.d(LOG_TAG, "signInWithCredential:success");
                redirectToWebshop();
            } else {
                // If sign in fails, display a message to the user.
                Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
            }
        });
    }

    public void login(View view) {
        String email = emailAddress.getText().toString();
        String pass = password.getText().toString();

        fireBaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d(LOG_TAG, "Sikeres login!");
                redirectToWebshop();
            } else {
                Log.d(LOG_TAG, "Nem sikerült bejelentkezni!");
            }
        });
    }

    public void register(View view) {
        Intent intent = new Intent(this, Regisztracio.class);
        intent.putExtra("KULCS", KULCS);
        startActivity(intent);
    }

    public void loginAnonymus(View view) {
        fireBaseAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d(LOG_TAG, "Anonym bejelentkezés sikerült!");
                redirectToWebshop();
            } else {
                Log.d(LOG_TAG, "Anonym bejelentkezés nem sikerült!"+ task.getException().getMessage());
            }
        });
    }

    private void redirectToWebshop() {
        Intent intent = new Intent(this, WebshopActivity.class);
        startActivity(intent);
    }

    public void googleLogin(View view) {
        Intent signIn = googleSignIn.getSignInIntent();
        startActivityForResult(signIn, REQUEST_CODE);
    }
}