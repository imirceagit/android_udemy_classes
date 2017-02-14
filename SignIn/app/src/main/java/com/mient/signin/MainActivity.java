package com.mient.signin;

import android.content.Intent;
import android.net.Credentials;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private CallbackManager callbackManager;
    private TextView info;
    private ImageView profileImgView;
    private LoginButton loginButton;

    private LogedEntity logedEntity;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private AccessToken currentToken;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker mProfileTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        logedEntity = LogedEntity.getInstance();
        logedEntity.setActivity(this);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.info);
        profileImgView = (ImageView) findViewById(R.id.profile_img);
        loginButton = (LoginButton) findViewById(R.id.login_button);

//        loginButton.setReadPermissions("email", "public_profile");

        logedEntity.getToken();

        currentToken = AccessToken.getCurrentAccessToken();

        if(AccessToken.getCurrentAccessToken() != null && logedEntity.getAccessToken() != null && logedEntity.getAccessToken().equals(AccessToken.getCurrentAccessToken().getToken())){
            Log.v(TAG, "TOKEN EXIST");
            clearUserArea();

            Profile profile = Profile.getCurrentProfile();
            info.setText(message(profile));
            String profileImgUrl = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";


            Glide.with(MainActivity.this)
                    .load(profileImgUrl)
                    .into(profileImgView);
        }

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {

                Log.v(TAG, "OLD " + (oldAccessToken == null ? "" : oldAccessToken.getToken()) + " CURRENT " + (currentAccessToken == null ? "" :currentAccessToken.getToken()));

                if (currentAccessToken == null) {
                    FirebaseAuth.getInstance().signOut();
                    clearUserArea();
                }
            }
        };

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v(TAG, "===========================ON SUCCESS=======================");

                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        Log.v("facebook - profile", profile2.getFirstName());
                        info.setText(message(profile2));
                        mProfileTracker.stopTracking();
                    }
                };

                Profile profile = Profile.getCurrentProfile();
                info.setText("TEST " + message(profile));

                String userId = loginResult.getAccessToken().getUserId();
                String accessToken = loginResult.getAccessToken().getToken();

                logedEntity.setUid(userId);
                logedEntity.setAccessToken(accessToken);

                logedEntity.saveAccessToken();

                Log.v(TAG, "ON SUCCESS LG TOKEN " + logedEntity.getAccessToken());

                String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";


                Glide.with(MainActivity.this)
                        .load(profileImgUrl)
                        .into(profileImgView);

                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                mAuth.signInWithCredential(credential).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                info.setText("Login attempt failed.");
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        info.setText(message(profile));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private String message(Profile profile) {
        StringBuilder stringBuffer = new StringBuilder();
        if (profile != null) {
            stringBuffer.append("Welcome ").append(profile.getName());
        }
        return stringBuffer.toString();
    }

    private void clearUserArea() {
        info.setText("");
        profileImgView.setImageDrawable(null);
    }
}
