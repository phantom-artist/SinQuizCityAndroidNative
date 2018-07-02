package com.phantomartist.sinquizcity.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.phantomartist.sinquizcity.R;
import com.phantomartist.sinquizcity.game.QuestionAndAnswers;

/**
 * Utility class for parsing questions.xml file in the app.
 * 
 */
public class Questions extends BaseParser {
    
    private ArrayList<QuestionAndAnswers> questions;    
    private static Questions instance; 
    
    /**
     * Get singleton instance of QuestionParser.
     * 
     * @param context
     * @param resourceID
     * @return
     */
    public static Questions getInstance() {
        if (instance == null) {
            instance = new Questions(R.xml.questions, "question");
        }
        return instance;
    }
    
    /**
     * Returns the questions list.
     * 
     * @return ArrayList of questions.
     */
    public ArrayList<QuestionAndAnswers> getShuffledQuestions() {
        Collections.shuffle(questions);
        return questions;
    }
    
    /**
     * Constructor.
     * 
     * @param context App Context.
     * @param resourceID Resource ID to parse.
     */
    private Questions(int resourceID, String objectTag) {
        super(resourceID, objectTag);
        questions = new ArrayList<QuestionAndAnswers>();
        parseObjectList();
    }
    
    @Override
    protected void parseObject() throws XmlPullParserException, IOException {
        
        Log.v("parseQuestion()", ">>>");
        
        QuestionAndAnswers question = new QuestionAndAnswers();

        // Keep reading until we reach end tag
        while (!isEndObject()) {

            String value = null;
            if (XmlPullParser.START_TAG == eventType) {                
                value = (parser.getAttributeCount() > 0) ? parser.getAttributeValue(0) : null;
            } else if (XmlPullParser.TEXT == eventType) {
                value = parser.getText();
            }
            
            if (value != null) {
                processValue(question, value);
            }

            // Increment parser
            eventType = parser.next();
            updateCurrentTag();
        }
        
        Log.v("parseQuestion()", "<<< " + question);
        
        questions.add(question);
    }
    
    /**
     * Process a value
     * 
     * @param question The question object
     * @param value The value to process
     * 
     * @throws XmlPullParserException, IOException if a parser exception occurs.
     */
    private void processValue(QuestionAndAnswers question, String value)
        throws XmlPullParserException, IOException {
        
        Log.v("processValue()", ">>>");
        
        if (QuestionAndAnswers.QUESTION_TAG.equals(currentTag)) {
            
            question.setQuestionID(Integer.parseInt(value));
            Log.v("processValue()", "Set questionID=" + question.getQuestionID());
            
        } else if (QuestionAndAnswers.ASK_TAG.equals(currentTag)) {
            
            question.setQuestion(value);
            Log.v("processValue()", "question=" + question.getQuestion());
            
        } else if (QuestionAndAnswers.ANSWER_TAG.equals(currentTag)) {
            
            int answerID = Integer.parseInt(value);
            parser.next(); // Increment token to get the text value of the answer
            String answer = parser.getText(); // Then get answer text
            question.addAnswer(answerID, answer);
            Log.v("processValue()", "Found answer=" + answerID + ", " + answer);
            
        } else if (QuestionAndAnswers.CORRECT_TAG.equals(currentTag)) {
            
            question.setCorrectID(Integer.parseInt(value));
            Log.v("processValue()", "correctID=" + question.getCorrectID());
            
        } else if (QuestionAndAnswers.TRIVIA_TAG.equals(currentTag)) {
            
            question.setTrivia(value);
            Log.v("processValue()", "trivia=" + question.getTrivia());
        
        } else {
            Log.v("processValue()", "Unknown tag/value - " + currentTag + "/" + value);
        }
        
        Log.v("processValue()", "<<<");
    }
}
