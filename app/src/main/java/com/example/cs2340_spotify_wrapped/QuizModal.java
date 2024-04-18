package com.example.cs2340_spotify_wrapped;

public class QuizModal {
    private String question, option1, option2, option3, answer, imageUrl;

    // Constructor
    public QuizModal(String question, String option1, String option2, String option3, String answer, String imageUrl) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answer = answer;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getQuestion() { return question; }
    public String getOption1() { return option1; }
    public String getOption2() { return option2; }
    public String getOption3() { return option3; }
    public String getAnswer() { return answer; }
    public String getImageUrl() { return imageUrl; }
}
