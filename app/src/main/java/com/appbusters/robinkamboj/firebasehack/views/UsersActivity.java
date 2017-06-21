package com.appbusters.robinkamboj.firebasehack.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.firebasehack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {


    ListView usersList;

    DatabaseReference databaseUsers  ;

    ArrayList<com.appbusters.robinkamboj.firebasehack.UserDetails> usersList1 ;

    SharedPreferences shared ;

    String UserId ;

    boolean flag = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Toast.makeText(this, "All users here", Toast.LENGTH_SHORT).show();

        shared = getSharedPreferences("MyData" , MODE_PRIVATE) ;

        UserId = shared.getString("UserId" , "") ;

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        usersList = (ListView)findViewById(R.id.listViewUsers);

        usersList1 = new ArrayList<>();

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(UsersActivity.this, ChatActivity.class);
                i.putExtra("ChatWith" , usersList1.get(position).getName());
                i.putExtra("userID" , usersList1.get(position).getUser_id());
                i.putExtra("userType" , usersList1.get(position).getUser_type());
                flag = false ;
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                com.appbusters.robinkamboj.firebasehack.UserDetails users ;
                usersList1.clear();

                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    users = artistSnapshot.getValue(com.appbusters.robinkamboj.firebasehack.UserDetails.class);
                    //adding artist to the list
                    if (!users.getUser_id().equals(UserId)) {
                        usersList1.add(users);
                    }
                }

                CustomAdapterForAllUsers adapter = new CustomAdapterForAllUsers(UsersActivity.this ,
                                        R.layout.list_item_for_all_users , usersList1);
                usersList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
