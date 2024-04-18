package com.example.cs2340_spotify_wrapped;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuizResult extends AppCompatActivity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_result);

        prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);

        TextView scoreTextView = findViewById(R.id.score);
        TextView commentTextView = findViewById(R.id.comment);
        Button playAgainButton = findViewById(R.id.play_again_btn);
        ImageButton homeButton = findViewById(R.id.home_btn);

        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);
        int totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0);

        scoreTextView.setText(score + "/" + totalQuestions);

        if (score == totalQuestions) {
            commentTextView.setText("Perfect! You're a real music expert!");
        } else if (score >= totalQuestions / 2) {
            commentTextView.setText("Well done! You know your stuff.");
        } else {
            commentTextView.setText("Keep practicing and you'll get there!");
        }

        saveHighestScore(score);

        playAgainButton.setOnClickListener(v -> {
            Intent restartIntent = new Intent(QuizResult.this, QuizMain.class);
            startActivity(restartIntent);
            finish();
        });

        homeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(QuizResult.this, QuizHome.class);
            startActivity(homeIntent);
            finish();
        });
    }

    private void saveHighestScore(int score) {
        int highScore = prefs.getInt("high_score", 0);
        if (score > highScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("high_score", score);
            editor.apply();
        }
    }
}
