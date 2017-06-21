package com.appbusters.robinkamboj.firebasehack.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbusters.robinkamboj.firebasehack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.emoji.Emoji;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiClickedListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    static final String TAG = "ChatActivity";

    ImageButton sendButton;
    EmojiEditText messageArea;
    ViewGroup rootView;

    EmojiPopup emojiPopup ;

    com.appbusters.robinkamboj.firebasehack.UserChats userChats ;

    String senderName , chatWith;
    DatabaseReference databaseReference , databaseUsers , onlineUpdate, chatWithData , typingUpdate , userUpdate ;

    Intent i ;

    ArrayList<com.appbusters.robinkamboj.firebasehack.ChatMessage> chatList ;

    ListView chatListView ;

    CustomAdapterForChat adapter ;

    ImageView toolbar_profile  , emojiBtn ;
    TextView toolbar_name , toolbar_user_type ;

    String userId , chatWithUserId  , chatWithUserType ;

    SharedPreferences sharedPreferences ;

    boolean flag = true , multiple_select = false;

    String last_message , last_message_date  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_chat);

        sharedPreferences = getSharedPreferences("my_data" , MODE_PRIVATE);

        userId = sharedPreferences.getString("Id" , "") ;

        userUpdate = FirebaseDatabase.getInstance().getReference("UsersLastMessage");
        onlineUpdate = FirebaseDatabase.getInstance().getReference("Users");
        typingUpdate = FirebaseDatabase.getInstance().getReference("UsersTyping");
        databaseReference = FirebaseDatabase.getInstance().getReference("messages");
        chatWithData = FirebaseDatabase.getInstance().getReference();
        databaseUsers = FirebaseDatabase.getInstance().getReference("messages");

        emojiBtn = (ImageView) findViewById(R.id.emojiButton);
        rootView = (ViewGroup) findViewById(R.id.chat_activity_root_view);

        chatList = new ArrayList<>();

        sendButton = (ImageButton) findViewById(R.id.enter_chat1);
        messageArea = (EmojiEditText) findViewById(R.id.chat_edit_text1);
        chatListView = (ListView) findViewById(R.id.chat_list_view);

        toolbar_profile = (ImageView) findViewById(R.id.toolbar_profile);
        toolbar_name = (TextView) findViewById(R.id.toolbar_name);
        toolbar_user_type = (TextView) findViewById(R.id.toolbar_user_type);

        emojiBtn.setColorFilter(ContextCompat.getColor(this, R.color.emoji_icons), PorterDuff.Mode.SRC_IN);
        sendButton.setColorFilter(ContextCompat.getColor(this, R.color.emoji_icons), PorterDuff.Mode.SRC_IN);

        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);

        senderName = sharedPreferences.getString("Name", "");

        i = getIntent();

        chatWith = i.getStringExtra("ChatWith");
        chatWithUserId = i.getStringExtra("userID");
        chatWithUserType = i.getStringExtra("userType");

        toolbar_name.setText(chatWith);
        toolbar_user_type.setText(chatWithUserType);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!messageArea.getText().toString().equals("")) {
                    addData();
                }

            }
        });

        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chooseImage();
                emojiPopup.toggle();
            }
        });

        setUpEmojiPopup();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getChats();

    }

    public void addData() {

        String message = "";

        String child1 = senderName + "_" + chatWith ;
        String child2 = chatWith + "_" + senderName ;

        message = messageArea.getText().toString();

        if (!isNetworkAvailable(this)){
            userChats = new com.appbusters.robinkamboj.firebasehack.UserChats(message, senderName , new SimpleDateFormat("hh:mm aa").format(new Date()) );
        }else{
            userChats = new com.appbusters.robinkamboj.firebasehack.UserChats(message, senderName , new SimpleDateFormat("hh:mm aa").format(new Date()) );
        }


        databaseReference.child(child1).push().setValue(userChats);
        databaseReference.child(child2).push().setValue(userChats);

        scrollMyListViewToBottom();
        messageArea.setText("");

    }

    public void getChats(){

        final String child1 = senderName + "_" + chatWith ;
        final String child2 = chatWith + "_" + senderName ;

        final Query chatQuery = databaseUsers.child(senderName + "_" + chatWith);
        chatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chatList.clear();

                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {

                    com.appbusters.robinkamboj.firebasehack.ChatMessage chats = artistSnapshot.getValue(com.appbusters.robinkamboj.firebasehack.ChatMessage.class);

                    if (chats.getName().equals(senderName)){
                        chats.setType(1);
                    }
                    else {
                        chats.setType(0);
                    }
                    chatList.add(chats);

                    last_message = chats.getMessage();
                    last_message_date = chats.getDate();

                }

                adapter = new CustomAdapterForChat(ChatActivity.this , R.layout.chat_user_receive_item, chatList );
                chatListView.setAdapter(adapter);
                scrollMyListViewToBottom();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
                    @Override public void onEmojiBackspaceClicked(final View v) {
                        Log.d(TAG, "Clicked on Backspace");
                    }
                })
                .setOnEmojiClickedListener(new OnEmojiClickedListener() {
                    @Override public void onEmojiClicked(final Emoji emoji) {
                        Log.d(TAG, "Clicked on emoji");
                    }
                })
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override public void onEmojiPopupShown() {
                        emojiBtn.setImageResource(R.drawable.ic_keyboard);
                    }
                })
                .setOnSoftKeyboardOpenListener(new OnSoftKeyboardOpenListener() {
                    @Override public void onKeyboardOpen(final int keyBoardHeight) {
                        Log.d(TAG, "Opened soft keyboard");
                    }
                })
                .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override public void onEmojiPopupDismiss() {
                        emojiBtn.setImageResource(R.drawable.emoji_ios_category_people);
                    }
                })
                .setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
                    @Override public void onKeyboardClose() {
                        Log.d(TAG, "Closed soft keyboard");
                    }
                })
                .build(messageArea);
    }

    private void scrollMyListViewToBottom() {
        chatListView.post(new Runnable() {
            @Override
            public void run() {
                chatListView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        }
        flag = false ;
        multiple_select = false ;
        finish();
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
