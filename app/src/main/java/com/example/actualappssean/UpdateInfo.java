package com.example.actualappssean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateInfo extends AppCompatActivity {
    public EditText editTextUpdateEmail, editTextUpdatePwd, editTextOldEmail, editTextOldPwd;
    public ProgressBar progressBar;
    public FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        Button buttonUpdate = findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldTextEmail = editTextOldEmail.getText().toString();
                String oldTextPwd = editTextOldPwd.getText().toString();
                String newTextEmail = editTextUpdateEmail.getText().toString();
                String newTextPwd = editTextUpdatePwd.getText().toString();
                // Check to see if data is valid before registering
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(oldTextEmail, oldTextPwd);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            if (TextUtils.isEmpty(newTextEmail)) {
                                Toast.makeText(UpdateInfo.this, "Please enter a new email address", Toast.LENGTH_SHORT).show();
                                editTextUpdateEmail.setError("An email address is required!");
                                editTextUpdateEmail.requestFocus();
                            } else if (TextUtils.isEmpty(newTextPwd) || newTextPwd.length() < 6) {
                                Toast.makeText(UpdateInfo.this, "Please enter a new and valid password (6+ characters)", Toast.LENGTH_SHORT).show();
                                editTextUpdatePwd.setError("A valid password is required!");
                                editTextUpdatePwd.requestFocus();
                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                                updateInfo(newTextEmail, newTextPwd);
                                Toast.makeText(UpdateInfo.this, "Succesfully Updated!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(UpdateInfo.this, "User does not exist, please sign up", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        Button buttonLeave = findViewById(R.id.button_leave_page);
        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateInfo.this, "Returning to User Profile Page", Toast.LENGTH_SHORT).show();
                Intent mainActivity = new Intent(UpdateInfo.this, UserProfileActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
            }
        });
    }

    private void updateInfo(String updateEmail, String updatePwd) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(updateEmail);
        user.updatePassword(updatePwd);
    }
}