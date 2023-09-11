package com.example.actualappssean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome, textViewFirstName, textViewLastName, textViewEmail, textViewRegisterDate;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Home");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        findViews();
        signOut();

        //show profile details if the user is not null
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null) {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }else{
            Toast.makeText(UserProfileActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        FirebaseUserMetadata setData = firebaseUser.getMetadata();
        long registerTimeStamp = setData.getCreationTimestamp();
        String dataPattern = "E, dd MMMM yyyy hh:mm a z";
        SimpleDateFormat sdf = new SimpleDateFormat(dataPattern);
        String register = sdf.format(new Date(registerTimeStamp));
        String registerDate = getResources().getString(R.string.user_since, register);
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        textViewEmail.setText(email);
        textViewFirstName.setText(name);
        textViewWelcome.setText(name);

        String welcome = getResources().getString(R.string.welcome_user);
        textViewWelcome.setText(welcome);
        progressBar.setVisibility(View.GONE);
    }

    private void signOut() {
        Button buttonSignOut = findViewById(R.id.button_sign_out);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(UserProfileActivity.this, "Sign out", Toast.LENGTH_SHORT).show();
                Intent mainActivity = new Intent(UserProfileActivity.this, MainActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                finish();
            }
        });
    }

    private void findViews() {
        textViewWelcome = findViewById(R.id.textView_welcome);
        textViewFirstName = findViewById(R.id.textView_show_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewRegisterDate = findViewById(R.id.textView_register);
        progressBar = findViewById(R.id.progressbar);
    }


}