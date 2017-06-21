package com.appbusters.robinkamboj.firebasehack.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.firebasehack.Models.Status;
import com.appbusters.robinkamboj.firebasehack.R;
import com.appbusters.robinkamboj.firebasehack.Utils.AppPreferencesHelper;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectStatusActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String TAG = "SELECT";
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tvSubmit)
    TextView submit;
    @BindView(R.id.etStatus)
    EditText etStatus;

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReferenceTag, mDatabaseReferenceUser, mDatabaseReferenceAllPosts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_status);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getName();
    }

    @OnClick(R.id.tvSubmit)
    public void postStatus() {
        if (etStatus.getText().length() >= 1) {
            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            Status status;
            Log.d(TAG, "postStatus: NAMEE " + name);
            mDatabaseReferenceAllPosts = FirebaseDatabase.getInstance().getReference("allPosts");
            mDatabaseReferenceTag = FirebaseDatabase.getInstance().getReference("posts");
            String postID = mDatabaseReferenceTag.push().getKey();

            status = new Status("status", mFirebaseUser.getUid(), name.getText().toString(), -System.currentTimeMillis(),
                    etStatus.getText().toString(),postID);
            mDatabaseReferenceAllPosts.child(postID).setValue(status);

            mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();
            if(AppPreferencesHelper.getUserType()!=null) {
                mDatabaseReferenceUser.child(AppPreferencesHelper.getUserType().toLowerCase()+"s").child(mFirebaseUser.getUid()).child("userPosts").child(postID).setValue(status);
            }
            Toast.makeText(this, "Status updated!", Toast.LENGTH_SHORT).show();
            finish();
        }

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

    @Override
    public void onBackPressed() {
        finish();
    }
}
