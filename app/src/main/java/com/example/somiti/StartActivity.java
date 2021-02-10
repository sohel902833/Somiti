package com.example.somiti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {


    private  Button loginButton;
    private  EditText emailEdittext,passwordEdittext;

    String email,password;

    private  FirebaseAuth mAuth;
    private  ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.setTitle("Login");

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("We Are Checking Your Account");
        progressDialog.setCanceledOnTouchOutside(false);


        mAuth=FirebaseAuth.getInstance();

        loginButton=findViewById(R.id.start_LoginButtonid);
        emailEdittext=findViewById(R.id.start_EmailEdittextid);
        passwordEdittext=findViewById(R.id.start_PasswordEdittextid);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEdittext.getText().toString();
                password = passwordEdittext.getText().toString();

                if (email.isEmpty()) {
                    emailEdittext.setError("Please enter Your Email");
                    emailEdittext.requestFocus();
                    return;
                } else if (password.isEmpty()) {
                    passwordEdittext.setError("Please Enter Password");
                    passwordEdittext.requestFocus();
                    return;
                } else {
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(StartActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(StartActivity.this, MainActivity.class));
                            }
                        }
                    });
                }
            }
        });
    }
}