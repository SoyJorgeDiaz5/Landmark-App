package com.co.soyjorgediaz5.landmarkapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(GoogleSignIn.getLastSignedInAccount(this));
    }

    private void initView() {
        findViewById(R.id.button_signIn).setOnClickListener(this);
        //Configure Sign In client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //initialize client to connect
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(View v) {
        signIn();
    }

    private void signIn() {
        Intent signinIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signinIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle signin result
        switch (requestCode){
            case RC_SIGN_IN:
                handleSigInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }

    private void handleSigInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            updateUI(completedTask.getResult(ApiException.class));
        } catch (ApiException e) {
            error(this.getClass().getName(), "signInResult: failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            startActivity(new Intent(this, MainActivity.class));
        }


    }

    private void error(String name, String msg) {
        Log.w(name, msg);
        Toast.makeText(this, msg,Toast.LENGTH_LONG).show();
    }
}
