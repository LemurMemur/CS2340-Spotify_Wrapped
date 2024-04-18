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
    private Button startButton;
    private ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_home);

        // Initialize SharedPreferences
        prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);

        // Initialize views
        highestScoreTextView = findViewById(R.id.highestScore);
        startButton = findViewById(R.id.start_btn);
        homeButton = findViewById(R.id.home_btn); // Initialize the ImageButton

        // Display the highest score
        updateHighScore();

        // Setup the button to start the quiz
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizHome.this, QuizMain.class);
                startActivity(intent);
            }
        });

        // Set the home button to navigate to Wrapper activity
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizHome.this, Wrapper.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the high score every time this activity resumes
        updateHighScore();
    }

    private void updateHighScore() {
        int highScore = prefs.getInt("high_score", 0); // Default to 0 if no high score is stored
        highestScoreTextView.setText("Highest Score: " + highScore);
    }
}
