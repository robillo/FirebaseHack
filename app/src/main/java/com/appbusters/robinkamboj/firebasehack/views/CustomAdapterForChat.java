package com.appbusters.robinkamboj.firebasehack.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.appbusters.robinkamboj.firebasehack.R;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;


/**
 * Created by VASU on 1/2/2017.
 */
public class CustomAdapterForChat extends ArrayAdapter<com.appbusters.robinkamboj.firebasehack.ChatMessage> implements View.OnClickListener,Filterable {

    private ArrayList<com.appbusters.robinkamboj.firebasehack.ChatMessage> originalList;
    private ArrayList<com.appbusters.robinkamboj.firebasehack.ChatMessage> usersList;

    public CustomAdapterForChat(Context context, int textViewResourceId, ArrayList<com.appbusters.robinkamboj.firebasehack.ChatMessage> usersList ) {
        super(context, textViewResourceId, usersList);
        this.usersList = new ArrayList<com.appbusters.robinkamboj.firebasehack.ChatMessage>();
        this.usersList.addAll(usersList);
        this.originalList = new ArrayList<com.appbusters.robinkamboj.firebasehack.ChatMessage>();
        this.originalList.addAll(usersList);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        com.appbusters.robinkamboj.firebasehack.ChatMessage message = usersList.get(position) ;
        return message.getType();
    }

    private class ViewHolder {
        public EmojiTextView message ;
        public TextView date;
      //  public String name ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

      //  position = usersList.size() - position - 1;

        final com.appbusters.robinkamboj.firebasehack.ChatMessage users = usersList.get(position);

        int type = getItemViewType(position) ;

        if (convertView == null) {

            if(type == 0){
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.chat_user_receive_item, null);
            }else if(type == 1){
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.chat_user_send_item, null);
            }

            holder = new ViewHolder();

            holder.message = (EmojiTextView) convertView.findViewById(R.id.textview_message);
            holder.date = (TextView) convertView.findViewById(R.id.textview_time);

            convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

        holder.message.setText(users.getMessage());
        holder.date.setText(users.getDate());

        return convertView;
    }


}
