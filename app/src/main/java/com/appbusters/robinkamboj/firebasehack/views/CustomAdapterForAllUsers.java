package com.appbusters.robinkamboj.firebasehack.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.appbusters.robinkamboj.firebasehack.R;

import java.util.ArrayList;


/**
 * Created by VASU on 1/2/2017.
 */
public class CustomAdapterForAllUsers extends ArrayAdapter<com.appbusters.robinkamboj.firebasehack.UserDetails> implements View.OnClickListener,Filterable {



    private ArrayList<com.appbusters.robinkamboj.firebasehack.UserDetails> originalList;
    private ArrayList<com.appbusters.robinkamboj.firebasehack.UserDetails> usersList;

    public CustomAdapterForAllUsers(Context context, int textViewResourceId,
                                    ArrayList<com.appbusters.robinkamboj.firebasehack.UserDetails> usersList) {
        super(context, textViewResourceId, usersList);
        this.usersList = new ArrayList<com.appbusters.robinkamboj.firebasehack.UserDetails>();
        this.usersList.addAll(usersList);
        this.originalList = new ArrayList<com.appbusters.robinkamboj.firebasehack.UserDetails>();
        this.originalList.addAll(usersList);
    }

    @Override
    public void onClick(View v) {

    }


    private class ViewHolder {
        public TextView name ;
        public TextView user_type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyData" , getContext().MODE_PRIVATE);

        String user_name = sharedPreferences.getString("Name" , "") ;

        //  position = usersList.size() - position - 1;

        final com.appbusters.robinkamboj.firebasehack.UserDetails users = usersList.get(position);

        final String description ;

        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_for_all_users, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.user_type = (TextView) convertView.findViewById(R.id.personType);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(users.getName());
        holder.user_type.setText(users.getUser_type());

        return convertView;

    }
}
