package com.appbusters.robinkamboj.firebasehack.views.fragments;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.appbusters.robinkamboj.firebasehack.Models.Photo;
import com.appbusters.robinkamboj.firebasehack.Models.Status;
import com.appbusters.robinkamboj.firebasehack.R;
import com.appbusters.robinkamboj.firebasehack.views.PhotoItemAdapter;
import com.appbusters.robinkamboj.firebasehack.views.StatusItemAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommonFragment extends Fragment {

    @BindView(R.id.swiperefresh_common)
    SwipeRefreshLayout mPullToRefresh;
    @BindView(R.id.alternate_layout_common)
    LinearLayout alternateLayout;

    private FirebaseAnalytics mFirebaseAnalytics;
    private MultiTypeAdapter mAdapter;

    private static FirebaseUser mFirebaseUser;
    private HashMap<String, String> userStatus;
    private static DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencePosts, mDatabaseReferenceGuru;
    private StorageReference mStorageReference;
    private ArrayList<String> mSelectedSubInterests;
    private ArrayList<Status> statusYouraList, statusHomeaList;
    private StorageReference mStorageReferenceDP;
    private HashMap mAllInterests;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUsersDatabase;
    private ArrayList<String> mSelectedGurus;

    private static final String TAG = "ALLSTATUS", STATUS = "status";
    private static final int PICK_IMAGE_REQUEST = 250;
    private Uri filePath;
    Items items;
    private Animation slide_down;
    private Animation slide_up;
    private int CHECK = 1;
    private String intentDBReference = null;
    private HashMap<String, Long> isDone;
    private HashMap<String, Status> statusHashMapStore;
    private HashMap<String, Long> statusHashMap;
    private String from = "HOME";
    private HashMap<String, Boolean> isStatusDone, isStatusDoneGuru;

    @BindView(R.id.recyclerview_common)
    RecyclerView mRvHome;

    private int lang = 2;
    private int sort = 0;
    private String sortBy = "timestamp";
    boolean bool = true;

    public CommonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_common, container, false);

        ButterKnife.bind(getActivity());
        setHasOptionsMenu(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        if (sort == 0) {
            sortBy = "timestamp";
        } else {
            sortBy = "likes";
        }

        String from = getArguments().getString("from");

        mRvHome = (RecyclerView) v.findViewById(R.id.recyclerview_common);
        mPullToRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh_common);
        alternateLayout = (LinearLayout) v.findViewById(R.id.alternate_layout_common);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("students");
        mStorageReferenceDP = FirebaseStorage.getInstance().getReference("profile").child("user").child("dp");

//        fetchDP();

//        avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        mRvHome.setVisibility(View.VISIBLE);

        String uid = mFirebaseUser.getUid();

        Log.e(TAG, uid);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRvHome.setLayoutManager(mLayoutManager);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Status.class, new StatusItemAdapter());
        mAdapter.register(Photo.class, new PhotoItemAdapter());
        mRvHome.setAdapter(mAdapter);

        items = new Items();

        isDone = new HashMap<>();
        mAllInterests = new HashMap();

        Log.e("FROM", from);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("students");
        Log.e(TAG, uid);
        Log.e(TAG, mDatabaseReference.toString());
        bool = false;

        fetchExplorePostsFromFirebase(sortBy);
        alternateLayout.setVisibility(View.INVISIBLE);
        refresh();

        return v;
    }

    private void fetchExplorePostsFromFirebase(String sortOption) {
        mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("allPosts");

        mDatabaseReferencePosts.orderByChild(sortOption).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");

                final StorageReference mStorageReferenceVideo = FirebaseStorage.getInstance().getReference("posts").child("videos");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "onDataChange: DATA " + postSnapshot);
                    Log.d(TAG, "onDataChange: DATA " + postSnapshot.child("type"));

                    if (!isDone.containsKey(postSnapshot.getKey())) {
                        if (postSnapshot.child("type").getValue().equals("status")) {
                            Log.d(TAG, "onDataChange: DATA troo");
                            Status statusSnap = postSnapshot.getValue(Status.class);
                            statusSnap.setPostUid(postSnapshot.getKey());
                            isDone.put(postSnapshot.getKey(), (long) 1);
                            items.add(statusSnap);

                        } else if (postSnapshot.child("type").getValue().equals("photo")) {
                            Photo photoSnap = postSnapshot.getValue(Photo.class);
                            photoSnap.setPostUid(postSnapshot.getKey());
                            photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));
                            isDone.put(postSnapshot.getKey(), (long) 1);
                            items.add(photoSnap);
                        }

                    }
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        Log.d(TAG, "fetchExplorePostsFromFirebase: " + items.size());
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                Handler handler1 = new Handler();
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        //CALL DATA HERE
                        fetchExplorePostsFromFirebase(sortBy);
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }


}
