package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebase.Adapter.UserAdapter;
import com.example.firebase.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SigninActivity extends AppCompatActivity {
    Button btn_logout;
    TextView name_account;
    RecyclerView lst_users;
    CircleImageView image_account;
    UserAdapter userAdapter;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    List<User> mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        image_account = findViewById(R.id.image_account);
        name_account = findViewById(R.id.name_account);
        lst_users = findViewById(R.id.lst_users);

        btn_logout = findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
//        Set Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        lst_users.setLayoutManager(linearLayoutManager);

        getListUsers();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                name_account.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    image_account.setImageResource(R.mipmap.ic_launcher);
                }else {
//                    Toast.makeText(SigninActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    Glide.with(SigninActivity.this).load(user.getImageURL()).into(image_account);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn_logout.setOnClickListener(view -> logout());
    }

    private void getListUsers() {
        mUser = new ArrayList<>();

        final FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Log.e("tag", datasnapshot.toString());
                    User user = datasnapshot.getValue(User.class);

                    assert firebaseUser1 != null;
                    assert user != null;
                    if (!user.getId().equals(firebaseUser1.getUid())) {
                        mUser.add(user);

                    }
                }
                userAdapter = new UserAdapter(SigninActivity.this, mUser);
                lst_users.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}