package com.example.firebase.Adapter;

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
import com.example.firebase.Model.Chat;
import com.example.firebase.Model.User;
import com.example.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
        static final int MSG_TYPE_LEFT = 0;
        static final int MSG_TYPE_RIGHT = 1;
        Context activity;
        List<Chat> mChat;
        private String imageurl;
        FirebaseUser firebaseUser;

        public MessageAdapter(Context activity, List<Chat> mChat, String imageurl) {
                this.activity = activity;
                this.mChat = mChat;
                this.imageurl = imageurl;
        }


        @NonNull
        @Override
        public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == MSG_TYPE_RIGHT) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
                return new MessageViewHolder(view);
            } else  {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
                return new MessageViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            Chat chat = mChat.get(position);
            holder.show_msessage.setText(chat.getMessage());

            if (imageurl.equals("default")) {
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);

            }else {
                Glide.with(activity).load(imageurl).into(holder.profile_image);
            }
        }

        @Override
        public int getItemCount() {
            if (mChat != null) {
                return mChat.size();
            }
                return 0;
            }

        public class MessageViewHolder extends RecyclerView.ViewHolder {

            private ImageView profile_image;
            private TextView show_msessage;

            public MessageViewHolder(@NonNull View itemView) {
                super(itemView);

                profile_image = itemView.findViewById(R.id.profile_image);
                show_msessage = itemView.findViewById(R.id.show_msessage);

            }
        }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
