package com.example.cs2340_spotify_wrapped;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizMain extends AppCompatActivity {

    private TextView questionText, questionAttemptedText;
    private ImageView quizImageView;
    private Button option1Button, option2Button, option3Button;
    private List<QuizModel> questionsList;
    private QuizModel currentQuestion;
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
        for (int i = 0; i < 5 ; ++i) {
            questionsList.add(getRandomQuestion());
        }
        totalQuestions = questionsList.size();
    }

    private QuizModel getRandomQuestion() {
        Random rand = new Random();
        String[] options = new String[3]; // 3 random out of top 5
        int[] indices = new int[3];
        int correct = rand.nextInt(3);
        boolean[] include = new boolean[5];
        for (int i=0, count = 0; count<3; i = (i + 1)%5) {
            boolean y = rand.nextInt(2) > 0;
            if (y && !include[i]) {
                indices[count] = i;
                include[i] = true;
                count++;
            }
        }

        int mode = rand.nextInt(2);
        String question = "";
        String imageUrl = ImageLoader.getImageURL(mode, indices[correct]);
        if (imageUrl.equals("")) {
            correct = (correct + 1) % 3;
            imageUrl = ImageLoader.getImageURL(mode, indices[correct]);
        } // this is a lazy way to try to make sure the answer has an image that can be used
        if (imageUrl.equals("")) {
            correct = (correct + 1) % 3;
            imageUrl = ImageLoader.getImageURL(mode, indices[correct]);
        }
        for (int i=0; i<3; ++i) {
            try {
                switch (mode) {
                    case 0:
                        System.out.println(WrapperLoader.currWrapperData.artists.getJSONArray("items").getJSONObject(indices[i]));
                        question = "Identify the artist picture";
                        options[i] = WrapperLoader.currWrapperData.artists.getJSONArray("items").getJSONObject(indices[i]).getString("name");
                        break;
                    case 1:
                        question = "Identify the album cover";
                        options[i] = WrapperLoader.currWrapperData.tracks.getJSONArray("items").getJSONObject(indices[i]).getString("name");
                        break;
                }
            } catch (Exception e) {
                System.out.println("failed to load quiz");
            }
        }

        QuizModel ret = new QuizModel(question, options[0], options[1], options[2], options[correct], imageUrl);
        return ret;
    }

    private void setQuestionScreen() {
        if (questionsAttempted < totalQuestions) {
            currentQuestion = questionsList.get(questionsAttempted);
            if (!currentQuestion.getImageUrl().equals("")) {
                Picasso.get()
                        .load(currentQuestion.getImageUrl())
                        .into(quizImageView);
            } else {
                quizImageView.setImageResource(0);
            }
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
            correctAnswers++;
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
        resultIntent.putExtra("SCORE", correctAnswers);
        resultIntent.putExtra("TOTAL_QUESTIONS", totalQuestions);
        startActivity(resultIntent);
        finish();
    }
}
