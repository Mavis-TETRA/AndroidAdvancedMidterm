package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebase.Adapter.MessageAdapter;
import com.example.firebase.Model.Chat;
import com.example.firebase.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView image_account;
    TextView username;
    ImageView return_main;
    EditText text_send;
    ImageButton btn_send;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    RecyclerView get_messages;
    List<Chat> mChat;
    MessageAdapter messageAdapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        image_account = findViewById(R.id.image_account);
        username = findViewById(R.id.name_account);
        return_main = findViewById(R.id.return_mainpage);
        text_send = findViewById(R.id.text_send);
        btn_send = findViewById(R.id.btn_send);

        get_messages = findViewById(R.id.get_messages);
        get_messages.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        get_messages.setLayoutManager(linearLayoutManager);

        return_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent = getIntent();
        String userid = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        btn_send.setOnClickListener(view -> confSendMessage(userid));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
//                image_account.setImageResource(R.mipmap.ic_launcher);
                if (user.getImageURL().equals("default")) {
                    image_account.setImageResource(R.mipmap.ic_launcher);
                }else {
//                    Toast.makeText(SigninActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(image_account);
                }
                ReadMessage(firebaseUser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void confSendMessage(String userid) {
        String msg = text_send.getText().toString();
        if (!msg.equals("")) {
            sendMesssge(firebaseUser.getUid(), userid, msg);
        }else {
            Toast.makeText(MessageActivity.this, "Bạn nên nhập gì đó trước khi gửi để tôn trọng họ nhé....", Toast.LENGTH_SHORT).show();
        }
        text_send.setText("");
    }

    private void sendMesssge(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void ReadMessage (final String myid, final String userid, final String imageurl) {
        mChat = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot snapshot_chat : snapshot.getChildren()) {
                    Chat chat1 = snapshot_chat.getValue(Chat.class);
//                    User user = datasnapshot.getValue(User.class);
                    assert chat1 != null;
                    if (chat1.getReceiver().equals(myid) && chat1.getSender().equals(userid) ||  chat1.getReceiver().equals(userid) && chat1.getSender().equals(myid)) {
                        mChat.add(chat1);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageurl);
                    get_messages.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}