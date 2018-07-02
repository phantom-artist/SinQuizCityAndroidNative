package com.phantomartist.sinquizcity.game;

/**
 * Models an Answer
 */
public class Answer {
    
    private int answerID;
    private String answer;
    
    public Answer(int id, String answer) {
        this.answerID = id;
        this.answer = answer;
    }
    public int getAnswerID() {
        return answerID;
    }
    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public String toFullString() {
        return "Answer=[id=" + answerID + ", answer=" + answer + "]";
    }
    
    /**
     * Used by ArrayAdapter to display value.
     */
    @Override
    public String toString() {
        return answer;
    }
}