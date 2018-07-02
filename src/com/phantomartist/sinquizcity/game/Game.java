package com.phantomartist.sinquizcity.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import android.util.Log;

import com.phantomartist.sinquizcity.MainActivity;
import com.phantomartist.sinquizcity.R;
import com.phantomartist.sinquizcity.util.Bars;
import com.phantomartist.sinquizcity.util.Questions;
import com.phantomartist.sinquizcity.util.UserPreferences;

/**
 * Wrapper for the SinQuizCity game logic.
 * 
 * Instantiates and iterates over a set of questions in a random order.
 * Models interactions for correct and wrong answers.
 */
public class Game implements Iterator<QuestionAndAnswers> {

    private static Game game; // Instance of the Game
    
    private Iterator<QuestionAndAnswers> questionIterator;
    private Random rand;
    private String[] correct;
    private String[] wrong;
    private QuestionAndAnswers currentQuestion;
    
    /**
     * Get current instance of game.
     * (Creates new instance if none defined).
     * 
     * @return Game the game.
     */
    public static Game getInstance() {
        if (game == null) {
            game = newGame();
        }
        return game;
    }
    
    /**
     * Force a new game.
     * 
     * @return Game game instance.
     */
    public static Game newGame() {
        game = new Game();
        return game;
    }
    
    /**
     * Returns a pseudo-random correct/wrong response String based on flag passed.
     * 
     * @param isCorrect If true, returns a random 'correct' response, otherwise returns a random 'wrong' response. 
     * @return String the response.
     */
    public String getRandomResponse(boolean isCorrect) {
        
        if (isCorrect) {
            int random = rand.nextInt(correct.length);
            return correct[random];
        }

        int random = rand.nextInt(wrong.length);
        return wrong[random];
    }

    /**
     * Determine if a game is in-progress.
     * 
     * @return boolean inProgress.
     */
    public static boolean isInProgress() {

        boolean isInProgress = false;
        if (game != null) {
            isInProgress = game.hasNext();
        }
        return isInProgress;
    }
    
    /**
     * Return true if the question iterator has more questions.
     */
    @Override
    public boolean hasNext() {

        return questionIterator.hasNext();
    }

    /**
     * Return the next question in the list.
     */
    @Override
    public QuestionAndAnswers next() {

        currentQuestion = questionIterator.next();
        return currentQuestion;
    }

    /**
     * Return current question (if resuming game).
     * 
     * @return QuestionAndAnswers current question.
     */
    public QuestionAndAnswers current() {
        return currentQuestion;
    }
    
    /**
     * Remove not supported by this game.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove questions!");
    }
    
    /**
     * Constructor
     */
    private Game() {
        initGame();
    }
    
    /**
     * Initializes a new game.
     */
    private void initGame() {

        Log.d(getClass().getName(), "Creating new game\n." + new Date().toString() + " - reading questions...");

        ArrayList<QuestionAndAnswers> questions = Questions.getInstance().getShuffledQuestions();
        questionIterator = questions.iterator();
        
        Log.d(getClass().getName(), new Date().toString() + " - " + questions.size() + " questions read and shuffled.");
        
        rand = new Random();
        correct = MainActivity.getAppContext().getResources().getStringArray(R.array.correct_responses);
        wrong = MainActivity.getAppContext().getResources().getStringArray(R.array.wrong_responses);
        UserPreferences.saveData(R.string.bar_key, Bars.getInstance().getFirstBar().getName());
    }
}
