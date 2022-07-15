package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btn_signin;
    TextView btn_regisster;
    ImageView btn_google, btn_facebook;
    EditText inputemail, inputpassword;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputemail = findViewById(R.id.sign_email);
        inputpassword = findViewById(R.id.sign_pass);
        btn_regisster = findViewById(R.id.register);
        btn_signin = findViewById(R.id.sign_in);
        btn_facebook = findViewById(R.id.btn_facebook);
        btn_google = findViewById(R.id.btn_google);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Toast.makeText(MainActivity.this, "Successfull Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        mAuth= FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(MainActivity.this);

        btn_regisster.setOnClickListener(view -> register());
        btn_signin.setOnClickListener(view -> login());
    }

    private void login() {
        String email = inputemail.getText().toString();
        String password = inputpassword.getText().toString();

        mLoadingBar.setTitle("Login");
        mLoadingBar.setMessage("Đang Đăng nhập...");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Successfull Login", Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                    Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else  {
                    Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void register() {
        Intent intent = new Intent(MainActivity.this,Register.class);
        startActivity(intent);
    }
}