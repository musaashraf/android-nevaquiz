package com.nevadiatechnology.nevaquiz.Model;

public class WrongQ {

    private String question;
    private String selectedAnswer;
    private String actualAnswer;

    public WrongQ() {
    }

    public WrongQ(String question, String selectedAnswer, String actualAnswer) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.actualAnswer = actualAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public String getActualAnswer() {
        return actualAnswer;
    }

    public void setActualAnswer(String actualAnswer) {
        this.actualAnswer = actualAnswer;
    }

}
