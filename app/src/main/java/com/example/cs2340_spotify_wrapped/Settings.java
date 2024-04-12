package com.example.cs2340_spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {
    FirebaseAuth auth;
    Button button, changeProfile, changePassword, deleteUser, wrapperPage;
    TextView textView;
    FirebaseUser user;
    ImageButton home;
    ConstraintLayout activity_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout_btn);
//        textView = findViewById(R.id.user_details);
        changeProfile = findViewById(R.id.changeProfile);
        changePassword = findViewById(R.id.changePassword);
        deleteUser = findViewById(R.id.deleteUser);
//        wrapperPage = findViewById(R.id.wrapperPage);
        home = findViewById(R.id.home);

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserEdit.class);
                startActivity(intent);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChangePassword.class);
                startActivity(intent);
            }
        });
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DeleteUserActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                startActivity(intent);
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
        activity_settings = findViewById(R.id.activity_settings);
        AnimationDrawable animationdrawable = (AnimationDrawable) activity_settings.getBackground();
        animationdrawable.setEnterFadeDuration(1000);
        animationdrawable.setExitFadeDuration(3000);
        animationdrawable.start();
    }
}