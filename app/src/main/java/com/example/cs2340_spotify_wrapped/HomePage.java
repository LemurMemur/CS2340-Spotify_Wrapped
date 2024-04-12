package com.example.cs2340_spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class HomePage extends AppCompatActivity {
    RelativeLayout home;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button loginButton = findViewById(R.id.login_btn);
        Button signupButton = findViewById(R.id.signUp_btn);

        //Remove Later
        Button spotify_api_button = findViewById(R.id.spotify_api_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, UserLogin.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, UserRegistration.class);
                startActivity(intent);
            }
        });

        //Remove Later

        spotify_api_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        home = findViewById(R.id.home);
        AnimationDrawable animationDrawable = (AnimationDrawable) home.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }
}
