package com.example.firebase.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebase.MessageActivity;
import com.example.firebase.Model.User;
import com.example.firebase.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder>{

    Context activity;
    List<User> mListUser;

    public UserAdapter(Context activity, List<User> mListUser) {

        this.activity = activity;
        this.mListUser = mListUser;
    }

//    @SuppressLint("NotifyDataSetChanged")
//    public void setData(List<User> list) {
//        this.mListUser = list;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = mListUser.get(position);
        holder.title_users.setText(user.getUsername());
        holder.email_users.setText(user.getEmail());
//        holder.image_users.setImageResource(R.mipmap.ic_launcher);
        if ((user.getImageURL()).equals("default")) {
            holder.image_users.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(activity).load(user.getImageURL()).into(holder.image_users);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListUser != null) {
            return mListUser.size();
        }
        return 0;
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        private ImageView image_users;
        private TextView title_users, email_users;



        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            image_users = itemView.findViewById(R.id.image_users);
            title_users = itemView.findViewById(R.id.title_users);
            email_users = itemView.findViewById(R.id.email_users);


        }
    }
}
