package com.example.cs2340_spotify_wrapped;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuizHome extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView highestScoreTextView;
    private Button startButton, resetButton;
    private ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_home);

        prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);

        highestScoreTextView = findViewById(R.id.highestScore);
        startButton = findViewById(R.id.start_btn);
        resetButton = findViewById(R.id.reset_btn);  // Initialize the reset button
        homeButton = findViewById(R.id.home_btn);

        updateHighScore();

        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(QuizHome.this, QuizMain.class);
            startActivity(intent);
        });

        resetButton.setOnClickListener(v -> resetHighScore());  // Set up click listener for reset

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(QuizHome.this, Wrapper.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHighScore();
    }

    private void updateHighScore() {
        int highScore = prefs.getInt("high_score", 0);
        highestScoreTextView.setText("Highest Score: " + highScore);
    }

    private void resetHighScore() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("high_score", 0);  // Reset the highest score to 0
        editor.apply();
        updateHighScore();  // Update the displayed score immediately
    }
}
