package com.appbusters.robinkamboj.firebasehack.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appbusters.robinkamboj.firebasehack.R;
import com.appbusters.robinkamboj.firebasehack.Utils.AppPreferencesHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
//    private MaterialDialog mDialog;

    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.phone_auth)
    TextView phone;

    Context context;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        context = getApplicationContext();
        mFirebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        if(AppPreferencesHelper.getIsProfileSet()!=null){
            startActivity(new Intent(this, MainActivity.class));
        }
        else if(AppPreferencesHelper.getIsLoggedIn()!=null){
            startActivity(new Intent(this, ProfileActivity.class));
        }


//        setupDialog();
        loginButton.setReadPermissions("email");
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(context, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
//                showDialog("", getString(R.string.wait));
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(context, "LOGIN CANCEL", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(context, "LOGIN ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("HANDLING", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("SUCCESS?   ", "NO");
                            Log.w("EXCEPTION", "signInWithCredential:failure", task.getException());
//                            dismissDialog();
                            Toast.makeText(context.getApplicationContext(), "auth failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("SUCCESS?   ", "YES");
                            Intent i = new Intent(context.getApplicationContext(), ProfileActivity.class);
                            i.putExtra("fromLogin", 0);
//                            dismissDialog();

                            AppPreferencesHelper.setIsLoggedIn("TRUE");
                            startActivity(i);
                            Toast.makeText(context.getApplicationContext(), "auth success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.phone_auth)
    public void startPhoneAuth(){
        startActivity(new Intent(this, PhoneAuthActivity.class));
    }

//    public void setupDialog() {
//        mDialog = new MaterialDialog.Builder(context)
//                .cancelable(false)
//                .progress(true, 0)
//                .build();
//    }
//
//    public void showDialog(String title, String content) {
//        mDialog.setTitle(title);
//        mDialog.setContent(content);
//        mDialog.show();
//    }
//
//    public void dismissDialog() {
//        if (mDialog.isShowing())
//            mDialog.dismiss();
//    }
}
