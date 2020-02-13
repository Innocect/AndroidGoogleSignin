package com.dhobi.googlesignin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout prof_section;
    private Button Signout;
    private SignInButton Signin;
    private TextView Name;
    private GoogleApiClient googleApiclient;
    private static final int REQ_CODE =9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prof_section = findViewById(R.id.prof_section);
        Signout = findViewById(R.id.logout);
        Signin = findViewById(R.id.signin);
        Name = findViewById(R.id.name);
        Signin.setOnClickListener(this);
        Signout.setOnClickListener(this);
        prof_section.setVisibility(View.VISIBLE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiclient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signin:
                signin();
                break;
            case R.id.logout:
                signout();
                break;

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiclient);
        startActivityForResult(intent,REQ_CODE);
    }
    private void signout()
    {
        Auth.GoogleSignInApi.signOut(googleApiclient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateui(false);
            }
        });

    }
    private void handleResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            Name.setText(name);
            updateui(true);

        }
        else
        {
            updateui(false);
        }

    }
    private void updateui(boolean isLogin)
    {
        if(isLogin)
        {
            prof_section.setVisibility(View.VISIBLE);
            Signin.setVisibility(View.GONE);
        }
        else
        {
            prof_section.setVisibility(View.GONE);
            Signin.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result); // check here
        }
    }
}
