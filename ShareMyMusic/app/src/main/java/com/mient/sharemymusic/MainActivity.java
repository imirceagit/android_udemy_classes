package com.mient.sharemymusic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    private Button btnSignout, btnSave;
    private TextView info;
    private EditText displayName;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignout = (Button) findViewById(R.id.btn_signout);
        btnSave = (Button) findViewById(R.id.btn_save);
        info = (TextView) findViewById(R.id.info);
        displayName = (EditText) findViewById(R.id.username);

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        if (user != null) {
            Log.v("MAINACTIVITY", "OUT " + user.getEmail());
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null){
                    Log.v("MAINACTIVITY", "USER NULL ");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.v("MAINACTIVITY", "USER NOT NULL" + user.getEmail());
                    info.setText("USER " + user.getDisplayName() + " " + user.getEmail());
                }
            }
        };
//
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("MAINACTIVITY", "SIGN OUT");
                signOut();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String username = displayName.getText().toString();
        if (user != null) {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
            user.updateProfile(profileChangeRequest);
        }
        user = auth.getCurrentUser();
    }

    private void signOut() {
        auth.signOut();
        user = auth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
