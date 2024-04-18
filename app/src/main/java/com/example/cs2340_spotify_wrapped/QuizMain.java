package com.example.cs2340_spotify_wrapped;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class QuizMain extends AppCompatActivity {

    private TextView questionText, questionAttemptedText;
    private ImageView quizImageView;
    private Button option1Button, option2Button, option3Button;
    private List<QuizModal> questionsList;
    private QuizModal currentQuestion;
    private int totalQuestions;
    private int questionsAttempted;
    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_question);

        questionText = findViewById(R.id.QUESTION);
        questionAttemptedText = findViewById(R.id.QUESTIONATTEMPTED);
        quizImageView = findViewById(R.id.quiz_image);
        option1Button = findViewById(R.id.OPTION1);
        option2Button = findViewById(R.id.OPTION2);
        option3Button = findViewById(R.id.OPTION3);

        loadQuestions();
        setQuestionScreen();

        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option1Button.getText().toString());
            }
        });

        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option2Button.getText().toString());
            }
        });

        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(option3Button.getText().toString());
            }
        });
    }

    private void loadQuestions() {
        questionsList = new ArrayList<>();
        // Updated questions focusing only on identifying album covers
        questionsList.add(new QuizModal("Identify the album cover", "÷ (Divide)", "x (Multiply)", "No.6 Collaborations Project", "÷ (Divide)", "https://example.com/divide.jpg"));
        questionsList.add(new QuizModal("Identify the album cover", "25", "21", "19", "25", "https://example.com/adele25.jpg"));
        questionsList.add(new QuizModal("Identify the album cover", "Reputation", "1989", "Red", "1989", "https://example.com/taylor1989.jpg"));
        questionsList.add(new QuizModal("Identify the album cover", "Purpose", "Changes", "Justice", "Purpose", "https://example.com/bieberpurpose.jpg"));
        questionsList.add(new QuizModal("Identify the album cover", "Starboy", "Beauty Behind the Madness", "After Hours", "Starboy", "https://example.com/starboy.jpg"));
        totalQuestions = questionsList.size();
    }

    private void setQuestionScreen() {
        if (questionsAttempted < totalQuestions) {
            currentQuestion = questionsList.get(questionsAttempted);
            Glide.with(this)
                    .load(currentQuestion.getImageUrl())
                    .into(quizImageView);
            questionText.setText(currentQuestion.getQuestion());
            option1Button.setText(currentQuestion.getOption1());
            option2Button.setText(currentQuestion.getOption2());
            option3Button.setText(currentQuestion.getOption3());
            questionAttemptedText.setText("Questions Attempted: " + questionsAttempted + "/" + totalQuestions);
            questionsAttempted++;
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            correctAnswers++;  // Increment correct answers count
            // Display some feedback for correct answer
        } else {
            // Display some feedback for wrong answer
        }
        if (questionsAttempted == totalQuestions) {
            finishQuiz();
        } else {
            setQuestionScreen();
        }
    }


    private void finishQuiz() {
        Intent resultIntent = new Intent(QuizMain.this, QuizResult.class);
        resultIntent.putExtra("SCORE", correctAnswers);  // Assuming you have a correctAnswers variable to track the score
        resultIntent.putExtra("TOTAL_QUESTIONS", totalQuestions);
        startActivity(resultIntent);
        finish();  // End this activity
    }
}
