package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class Register extends AppCompatActivity {
    Button btn_register;
    TextView btn_signin;
    EditText inputusername, inputemail, inputpassword;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputusername = findViewById(R.id.sign_name);
        inputemail = findViewById(R.id.sign_email);
        inputpassword = findViewById(R.id.sign_pass);
        btn_register = findViewById(R.id.register);
        btn_signin = findViewById(R.id.sign_in);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(Register.this);

        btn_signin.setOnClickListener(view -> login());
        btn_register.setOnClickListener(view -> register());
    }

    private void register() {
        String username = inputusername.getText().toString();
        String email = inputemail.getText().toString();
        String password = inputpassword.getText().toString();

        mLoadingBar.setTitle("Registeration");
        mLoadingBar.setMessage("Xin Chờ Máy Chủ Xử Lí");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Register.this, "Successfull Registeration", Toast.LENGTH_SHORT).show();
                                mLoadingBar.dismiss();
                                Intent intent = new Intent(Register.this, SigninActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(Register.this, "You can't register woth this email or password ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else  {
                    Toast.makeText(Register.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void login() {
        Intent intent = new Intent(Register.this,MainActivity.class);
        startActivity(intent);
    }
}