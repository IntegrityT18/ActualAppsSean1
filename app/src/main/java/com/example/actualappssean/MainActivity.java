package com.example.actualappssean;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;


public class MainActivity extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Login");
        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progressbar);


        showHidePassword();


        TextView textViewRegister = findViewById(R.id.textView_register);
        textViewRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(MainActivity.this, activity_register.class);
                startActivity(registerActivity);
            }
        });
        auth = FirebaseAuth.getInstance();
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPassword = editTextLoginEmail.getText().toString();
                if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(MainActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("An email address is required!");
                    editTextLoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword){
                    Toast.makeText(MainActivity.this, "Please enter a valid password (6+ characters)", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("A valid password is required!");
                    editTextLoginPwd.requestFocus();
                } else if(textPassword.length() < 6) {
                    Toast.makeText(MainActivity.this, "Please enter password longer than 6 characters", Toast.LENGTH_SHORT).show();

                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPassword);
                }
            }
        });
    }


    private void loginUser(String textEmail, String textPassword) {
        auth.signInWithEmailAndPassword((textEmail, textPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent userProfileActivity = new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(userProfileActivity);
                }else{
                    try{
                        throw task.getException();
                    }catch(Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showHidePassword() {
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.eyeopen);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.eyeclose);
                } else {
                    editTextLoginPwd.setTransformationMethod((HideReturnsTransformationMethod.getInstance()));
                    imageViewShowHidePwd.setImageResource(R.drawable.eyeopen);
                }
            }
        });
    }
}
