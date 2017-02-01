package com.mient.mywallet.mywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private TextView authDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        authDetails = (TextView) findViewById(R.id.auth_details);

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        }else {
            authDetails.setText("UserId: " + AccessToken.getCurrentAccessToken().getUserId() + " Token: " + AccessToken.getCurrentAccessToken().getToken());
        }
    }

    private void goLoginScreen(){
        Intent intent = new Intent(this, FacebookLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}
