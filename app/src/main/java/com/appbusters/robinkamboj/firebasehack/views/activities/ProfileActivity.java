package com.appbusters.robinkamboj.firebasehack.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.firebasehack.R;
import com.appbusters.robinkamboj.firebasehack.Utils.AppPreferencesHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CardView hidenshowCardMain, hidenshowCardUnwanted;
    private LinearLayout hidenshowMain, hidenshowUnwanted;
    private RadioGroup rgUserType, rgSpec;
    private static final String TAG = "PROFILE";
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mGurusDatabase;
    private FirebaseUser mFirebaseUser;
    private StorageReference mStorageReferenceDP,mStorageReferenceCover, mStorageReferenceGovID, mStorageReferenceSpecID;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth firebaseAuth;
    private static final int RESULT_LOAD_GOV_ID = 8010;
    Uri govPath = null, specPath = null;
    private int code = 0;
    private String pushGender = null, pushID = null, pushName = null, pushUserType =null, pushSpec = null;

    @BindView(R.id.phone)
    EditText ephone;
    @BindView(R.id.college)
    EditText ecollege;
    @BindView(R.id.govID)
    ImageView govID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        code = getIntent().getIntExtra("fromHome", 0);

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        }

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);
        }

        hidenshowCardMain = (CardView) findViewById(R.id.hidenshowcardmain);
        hidenshowCardUnwanted = (CardView) findViewById(R.id.hidenshowcardunwanted);
        hidenshowMain = (LinearLayout) findViewById(R.id.hidenshowmain);
        rgUserType = (RadioGroup) findViewById(R.id.rgusertype);
        rgSpec = (RadioGroup) findViewById(R.id.rg_spec);
        pushUserType = "STUDENT";

        Bundle params = new Bundle();
        params.putString("fields", "id,email,name,gender,cover,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();

                                if(data.has("name")){
                                    String temp = data.getString("name");
                                    Log.e("Login" + "name", temp);
                                    ((TextView) findViewById(R.id.name)).setText(temp);
                                    pushName = temp;
                                    Log.e("pushname", pushName);
                                }
                                if(data.has("gender")){
                                    String temp = data.getString("gender");
                                    Log.e("Login" + "Gender", temp);
                                    ((TextView) findViewById(R.id.gender)).setText(temp);
                                    pushGender = temp;
                                    Log.e("pushgender", pushGender);
                                }
                                if(data.has("id")){
                                    String id = data.getString("id");
                                    Log.e("Login" + "Id", id);
                                    String temp = "FACEBOOK ID: " + id;
                                    ((TextView) findViewById(R.id.id)).setText(temp);

                                    String profilePicUrl = "http://graph.facebook.com/" + id + "/picture?type=large";

                                    pushID = id;
                                    Log.e("pushid", pushID);

                                    Glide.with(ProfileActivity.this)
                                            .load(profilePicUrl)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into((CircleImageView) findViewById(R.id.profile_image));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("students");
        DatabaseReference mTempDatabase = FirebaseDatabase.getInstance().getReference();
        mTempDatabase.child("teachers").child(mFirebaseUser.getUid()).push();
        mGurusDatabase = FirebaseDatabase.getInstance().getReference("teachers").child("pending");

        Log.e("UID", mFirebaseUser.getUid());

        mStorageReferenceGovID = FirebaseStorage.getInstance().getReference("teachers").child("pending").child("govid");
        mStorageReferenceSpecID = FirebaseStorage.getInstance().getReference("teachers").child("pending").child("specid");

        if(code==1){        //FROM HOME
            setNotClickable();
            rgUserType.setClickable(false);
            ((RadioButton) findViewById(R.id.standard)).setClickable(false);
            ((RadioButton) findViewById(R.id.guru)).setClickable(false);
            if(AppPreferencesHelper.getUserType()!=null){
                if(AppPreferencesHelper.getUserType().equals("TEACHER")){
                    ((RadioButton) findViewById(R.id.standard)).setChecked(false);
                    ((RadioButton) findViewById(R.id.guru)).setChecked(true);
                }
                else {
                    ((RadioButton) findViewById(R.id.standard)).setChecked(true);
                    ((RadioButton) findViewById(R.id.guru)).setChecked(false);
                }
            }
        }

        rgUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.standard:{
                        hideGuruStuff();
                        pushUserType = "STUDENT";
                        break;
                    }
                    case R.id.guru:{
                        showGuruStuff();
                        pushUserType = "TEACHER";
                        break;
                    }
                }
            }
        });

        rgSpec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rbastrology:{
                        pushSpec = "PHYSICS";
                        break;
                    }
                    case R.id.rbyoga:{
                        pushSpec = "CHEMISTRY";
                        break;
                    }
                    case R.id.rbmotivation:{
                        pushSpec = "MATHS";
                        break;
                    }
                    case R.id.rbpandit:{
                        pushSpec = "C++";
                        break;
                    }
                }
            }
        });
    }


    @OnClick(R.id.govID)
    public void setGovID(){
        if((ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
            }

            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);
            }
        }
        else {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_GOV_ID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_GOV_ID && resultCode == RESULT_OK && null != data){
            govPath = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(govPath,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            int columnIndex = cursor != null ? cursor.getColumnIndex(filePathColumn[0]) : 0;
            String picturePath = cursor != null ? cursor.getString(columnIndex) : null;
            if (cursor != null) {
                cursor.close();
            }

            Log.e("PICTURE PATH GOV", "BEFORE");
            if (picturePath != null) {
                Log.e("PICTURE PATH GOV", "file://" + picturePath);
                Glide.with(this)
                        .load(picturePath)
                        .centerCrop()
                        .into(govID);
            }
            Log.e("PICTURE PATH GOV", "AFTER");


            if(govPath!=null){
//                Toast.makeText(getApplicationContext(), R.string.shortly, Toast.LENGTH_SHORT).show();
                StorageReference riversRef = mStorageReferenceGovID.child(mFirebaseUser.getUid());
                riversRef.putFile(govPath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileActivity.this, "Image Uploaded..Changes will be reflected.", Toast.LENGTH_SHORT).show();
//                                fetchData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileActivity.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });
                mGurusDatabase.child(mFirebaseUser.getUid()).child("govID").setValue(govPath.toString());
            }
        }
    }
    
    public void setNotClickable(){
        hidenshowCardMain.setClickable(false);
        hidenshowCardUnwanted.setClickable(false);
        hidenshowMain.setClickable(false);
        (findViewById(R.id.save)).setVisibility(View.GONE);
    }

    public void showGuruStuff(){
        if(hidenshowCardMain.getVisibility()== View.GONE){
            hidenshowCardMain.setVisibility(View.VISIBLE);
            hidenshowMain.setVisibility(View.VISIBLE);
            hidenshowCardUnwanted.setVisibility(View.VISIBLE);
        }
    }

    public void hideGuruStuff(){
        if(hidenshowCardMain.getVisibility()==View.VISIBLE){
            hidenshowCardMain.setVisibility(View.GONE);
            hidenshowMain.setVisibility(View.GONE);
            hidenshowCardUnwanted.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.save)
    public void save(){
        if (pushUserType.equals("STUDENT")){
            mUsersDatabase.child(mFirebaseUser.getUid()).child("name").setValue(pushName);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("id").setValue(pushID);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("gender").setValue(pushGender);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("userType").setValue(pushUserType);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("phone").setValue(ephone.getText().toString());
            mUsersDatabase.child(mFirebaseUser.getUid()).child("college").setValue(ecollege.getText().toString());
            mUsersDatabase.child(mFirebaseUser.getUid()).child("uid").setValue(mFirebaseUser.getUid());
            AppPreferencesHelper.setIsProfileSet("TRUE");
            AppPreferencesHelper.setUserType("TEACHER");
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(getApplicationContext(), "Profile Successfully Updated.", Toast.LENGTH_SHORT).show();
        }
        else if (pushUserType.equals("TEACHER")){
            mGurusDatabase.child(mFirebaseUser.getUid()).child("name").setValue(pushName);
            mGurusDatabase.child(mFirebaseUser.getUid()).child("fbId").setValue(pushID);
            mGurusDatabase.child(mFirebaseUser.getUid()).child("gender").setValue(pushGender);
            mGurusDatabase.child(mFirebaseUser.getUid()).child("phone").setValue(ephone.getText().toString());
            mGurusDatabase.child(mFirebaseUser.getUid()).child("college").setValue(ecollege.getText().toString());
            mGurusDatabase.child(mFirebaseUser.getUid()).child("subject").setValue(pushSpec);
            mGurusDatabase.child(mFirebaseUser.getUid()).child("govID").setValue(govPath.toString());
            mGurusDatabase.child(mFirebaseUser.getUid()).child("uid").setValue(mFirebaseUser.getUid());
            AppPreferencesHelper.setIsProfileSet("TRUE");
            AppPreferencesHelper.setUserType("TEACHER");
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(getApplicationContext(), "Profile Successfully Updated.", Toast.LENGTH_SHORT).show();
        }
    }
}
