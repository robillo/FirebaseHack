package com.appbusters.robinkamboj.firebasehack.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.firebasehack.Models.Assignment;
import com.appbusters.robinkamboj.firebasehack.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubmitAssignmentActivity extends AppCompatActivity {

    @BindView(R.id.spinnerSelect)
    Spinner spinnerSelectTeacher;
    List<String> teacherList;

    @BindView(R.id.upload)
    ImageView upload;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tvSubmit)
    TextView submit;
    private String teacherUid;
    //    Button next;
//    @BindView(R.id.grid)
//    GridLayout grid;
//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerView;
    @BindView(R.id.etCaption)
    EditText etCaption;
    private static final String TAG = "PHOTO_POST";
    private static final int PICK_DOC_REQUEST = 250;
    private Uri filePath;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReferenceTag, mDatabaseReferenceUser, mDatabaseReferenceAllPosts;
    private StorageReference mStorageReference;
    ProgressDialog progressDialog;
    Long size;
    String bucket, encoding, lang;
    Uri downloadUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_assignment);
        ButterKnife.bind(this);
        teacherList = new ArrayList<>();
        teacherList.add("Teacher A");
        teacherList.add("Teacher B");
        teacherList.add("Teacher C");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, teacherList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectTeacher.setAdapter(dataAdapter);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getName();

        mStorageReference = FirebaseStorage.getInstance().getReference("assignments").child("docs");
    }

    @OnClick(R.id.tvSubmit)
    public void submitAssignment() {
        uploadToFirebase();
    }

    @OnClick(R.id.upload)
    public void setUpload(){
        showFileChooser();
    }

    public void getName() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("students").child(mFirebaseUser.getUid());
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
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Upload Document"), PICK_DOC_REQUEST);
    }


    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DOC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Glide.with(this).load(R.drawable.tick).into(upload);
            filePath = data.getData();

//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
//                upload.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
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

                mDatabaseReferenceAllPosts = FirebaseDatabase.getInstance().getReference("allAssignments");
                mDatabaseReferenceTag = FirebaseDatabase.getInstance().getReference("posts");

                final String postID = mDatabaseReferenceTag.push().getKey();
                getName();
                StorageReference riversRef = mStorageReference.child(postID);
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Log.d(TAG, "onSuccess: " + taskSnapshot.getMetadata().getSizeBytes());
                                Log.d(TAG, "onSuccess: " + taskSnapshot.getDownloadUrl());

                                size = taskSnapshot.getMetadata().getSizeBytes();
                                lang = taskSnapshot.getMetadata().getContentLanguage();
                                encoding = taskSnapshot.getMetadata().getContentEncoding();
                                bucket = taskSnapshot.getMetadata().getBucket();
                                downloadUrl = taskSnapshot.getDownloadUrl();

                                Toast.makeText(SubmitAssignmentActivity.this, "Assignment Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
//                                progressDialog.dismiss();
                                Toast.makeText(SubmitAssignmentActivity.this, "Assignment File", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: " + exception.getMessage());

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });


                Assignment assignment = null;
                if (name.getText() != "") {
                        assignment = new Assignment(etCaption.getText().toString(),filePath.toString(),mFirebaseUser.getUid(),-System.currentTimeMillis(),postID);
                }
                if(teacherUid!=null){
                    //TODO
                }
                mDatabaseReferenceAllPosts.child(postID).setValue(assignment);
                mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();
                mDatabaseReferenceUser.child("students").child(mFirebaseUser.getUid()).child("studentAssignments").child(postID).setValue(assignment);

                Toast.makeText(this, "Assignment Uploaded Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Write an Assignment Topic", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No File Chosen", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "uploadToFirebase: No file chosen!");
        }
    }

}