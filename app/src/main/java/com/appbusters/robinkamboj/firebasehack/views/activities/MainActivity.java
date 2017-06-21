package com.appbusters.robinkamboj.firebasehack.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.firebasehack.R;
import com.appbusters.robinkamboj.firebasehack.views.fragments.CommonFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    boolean doubleBackToExitPressedOnce = false;
    private String from;
    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.conditional)
    LinearLayout conditional;
    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.status_select)
    TextView status_select;
    @BindView(R.id.photos_select)
    TextView photos_select;
    @BindView(R.id.videos_select)
    TextView videos_select;

    @OnClick(R.id.status_select)
    public void setStatus_select(){
        Intent i = new Intent(this, SelectStatusActivity.class);
        i.putExtra("page", 1);
        startActivity(i);
    }

    @OnClick(R.id.photos_select)
    public void setPhotos_select(){
        Intent i = new Intent(this, SelectPhotoActivity.class);
        i.putExtra("page", 1);
        startActivity(i);
    }

    @OnClick(R.id.videos_select)
    public void setVideos_select(){
        Intent i = new Intent(this, SubmitAssignmentActivity.class);
        i.putExtra("page", 1);
        startActivity(i);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:{
                    from = getString(R.string.title_home);
                    header.setText(R.string.title_home);
                    addFragment(from);
                    showConditional();
                    return true;
                }
                case R.id.navigation_follow:{
                    hideConditional();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this, TeacherListActivity.class));
                        }
                    }, 200);
                    return true;
                }
                case R.id.navigation_explore:{
                    header.setText(R.string.title_explore);
                    from = getString(R.string.title_explore);
                    addFragment(from);
                    showConditional();
                    return true;
                }
                case R.id.navigation_chat:{
                    header.setText(R.string.chat);
                    from = getString(R.string.chat);
                    addFragment(from);
                    showConditional();
                    return true;
                }
                case R.id.navigation_profile:{
                    hideConditional();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                            i.putExtra("fromHome", 1);
                            startActivity(i);
                        }
                    }, 200);
                    return true;
                }
            }
            return false;
        }

    };

    private void hideConditional(){
        if(conditional.getVisibility()== View.VISIBLE){
            conditional.setVisibility(View.INVISIBLE);
        }
    }
    private void showConditional(){
        if(conditional.getVisibility()== View.INVISIBLE){
            conditional.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        from = getString(R.string.title_home);
        addFragment(getResources().getString(R.string.title_home));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void addFragment(String from){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        CommonFragment commonFragment = new CommonFragment();
        Bundle args = new Bundle();
        args.putString("from", from);
        commonFragment.setArguments(args);

        fragmentTransaction.replace(R.id.fragment_container_new_main, commonFragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
//            finish();
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.click_back, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
