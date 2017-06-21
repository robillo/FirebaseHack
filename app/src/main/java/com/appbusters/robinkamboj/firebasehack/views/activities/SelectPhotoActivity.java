package com.appbusters.robinkamboj.firebasehack.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.firebasehack.Models.Photo;
import com.appbusters.robinkamboj.firebasehack.R;
import com.appbusters.robinkamboj.firebasehack.Utils.AppPreferencesHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPhotoActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.upload)
    ImageView upload;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.etCaption)
    EditText etCaption;

    private static final String TAG = "PHOTO_POST";
    private static final int PICK_IMAGE_REQUEST = 250;
    private Uri filePath;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReferenceUser, mDatabaseReferenceAllPosts, mDatabaseReferenceTag;
    private StorageReference mStorageReference;
    ProgressDialog progressDialog;
    Long size;
    String bucket, encoding, lang;
    Uri downloadUrl;

    private int from = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getName();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");
//        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();

    }

    @OnClick(R.id.tvSubmit)
    public void postImage() {
        uploadToFirebase();
    }

    @OnClick(R.id.upload)
    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Photo"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                upload.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No File Chosen", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void uploadToFirebase() {
        if (filePath != null) {
            if (etCaption.length() >= 1) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                mDatabaseReferenceTag = FirebaseDatabase.getInstance().getReference("posts");
                mDatabaseReferenceAllPosts = FirebaseDatabase.getInstance().getReference("allPosts");

                final String postID = mDatabaseReferenceTag.push().getKey();
                getName();
                StorageReference riversRef = mStorageReference.child(postID);
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Log.d(TAG, "onSuccess: " + taskSnapshot.getMetadata().getSizeBytes());
                                Log.d(TAG, "onSuccess: " + taskSnapshot.getDownloadUrl());

//                            progressDialog.dismiss();
                                Toast.makeText(SelectPhotoActivity.this, "Post Updated!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
//                                progressDialog.dismiss();
                                Toast.makeText(SelectPhotoActivity.this, "Post Update Failed!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: " + exception.getMessage());

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });


                Photo photo = null;
                if (name.getText() != "") {
                    photo = new Photo("photo",mFirebaseUser.getUid(), name.getText().toString(),
                            -System.currentTimeMillis(), etCaption.getText().toString(), postID);
                }

                mDatabaseReferenceAllPosts.child(postID).setValue(photo);

                mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();
                if(AppPreferencesHelper.getUserType()!=null) {
                    mDatabaseReferenceUser.child(AppPreferencesHelper.getUserType().toLowerCase()+"s").child(mFirebaseUser.getUid()).child("userPosts").child(postID).setValue(photo);
                }
                finish();
            } else {
                Toast.makeText(this, "Enter Caption", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "uploadToFirebase: No file chosen!");
        }

    }


    public void getName() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").getValue() != null) {
                    Log.d(TAG, "onDataChange: NAMEE" + (dataSnapshot.child("name").getValue()));
                    name.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//        return null;
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        if(from == 1){
//            startActivity(new Intent(this, NewExploreyActivity.class));
//        }
//        else if(from == 2){
//            startActivity(new Intent(this, NewHomeyActivity.class));
//        }
        finish();
    }

}
