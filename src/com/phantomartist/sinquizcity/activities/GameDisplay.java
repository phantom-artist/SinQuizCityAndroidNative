package com.phantomartist.sinquizcity.activities;

import java.util.ArrayList;
import java.util.Collections;

import com.phantomartist.sinquizcity.GeneralAppException;
import com.phantomartist.sinquizcity.R;
import com.phantomartist.sinquizcity.game.Answer;
import com.phantomartist.sinquizcity.game.AnswerHandler;
import com.phantomartist.sinquizcity.game.Bar;
import com.phantomartist.sinquizcity.game.Game;
import com.phantomartist.sinquizcity.game.QuestionAndAnswers;
import com.phantomartist.sinquizcity.util.Bars;
import com.phantomartist.sinquizcity.util.DialogBuilder;
import com.phantomartist.sinquizcity.util.Icon;
import com.phantomartist.sinquizcity.util.UserPreferences;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Represents the game View
 */
public class GameDisplay extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_game);

        // Show the Up button in the action bar.
        setupActionBar();

        // Continue game, or new game?
        boolean isInProgress = Game.isInProgress();
        
        if (isInProgress) {
            resumeGame();
        } else {
            newGame();
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.run_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Resume an existing game.
     */
    public void resumeGame() {
        
        try {
            setupHeaderInfo();
            
            if (Game.getInstance().hasNext()) {
                
                QuestionAndAnswers question = Game.getInstance().current();
                AnswerHandler handler = 
                    new AnswerHandler(this, question, Game.getInstance().getRandomResponse(true), Game.getInstance().getRandomResponse(false));
        
                display(question, handler);
            
            } else {
                
                endGame();
            }
        } catch (GeneralAppException e) {
            Log.e("runGame()", "GeneralException - " + e);
            Dialog alert = DialogBuilder.generateMsg(this, R.string.system_oops, false);
            alert.show();
        } catch (Exception e) {
            Log.e("runGame()", "Exception - " + e);
            Dialog alert = DialogBuilder.generateMsg(this, R.string.system_oops, false);
            alert.show();
        }
    }
    
    /**
     * Start a new game.
     */
    public void newGame() {
        
        try {
            Game.newGame();
            
            setupHeaderInfo();
            
            Dialog firstBar = DialogBuilder.generateNextBarMsg(this, Bars.getInstance().getFirstBar());
            firstBar.show();
            
            displayNextQuestion();
            
        } catch (GeneralAppException e) {
            Log.e("runGame()", "GeneralException - " + e);
            Dialog alert = DialogBuilder.generateMsg(this, R.string.system_oops, false);
            alert.show();
        } catch (Exception e) {
            Log.e("runGame()", "Exception - " + e);
            Dialog alert = DialogBuilder.generateMsg(this, R.string.system_oops, false);
            alert.show();
        }
    }
    
    /**
     * Perform the next activity in the game sequence 
     * (Either display next question or end game).
     */
    public void displayNextQuestion() {
        
        if (Game.getInstance().hasNext()) {
        
            QuestionAndAnswers question = Game.getInstance().next();
            AnswerHandler handler = 
                new AnswerHandler(this, question, Game.getInstance().getRandomResponse(true), Game.getInstance().getRandomResponse(false));
    
            display(question, handler);
        
        } else {
            
            endGame();
        }
    }
    
    /**
     * Update the game based on result of a correct/incorrect answer.
     * 
     * @param wasCorrectAnswer true if correct, false otherwise.
     */
    public void updateDisplay(boolean wasCorrectAnswer) {
        if (wasCorrectAnswer) {
            updateXP();
        } else {
            updateDrinks();
            resetRun();
        }
        
        updateRun(wasCorrectAnswer);
        
        // Figure out which icon to display
        updateIcon(wasCorrectAnswer);
        
        // Set 'last answer' for next question
        UserPreferences.saveData(R.string.last_answer_key, String.valueOf(wasCorrectAnswer));
    }
    
    /**
     * Update the bar, via pop-up, if the user has accumulated enough points. 
     */
    public void updateBar() {
        
        // Have we reached next bar?
        Bars bars = Bars.getInstance();
        int points = UserPreferences.getIntData(R.string.points_key, "0");
        String currentBar = UserPreferences.getStringData(R.string.bar_key, bars.getFirstBar().getName());
        
        Bar bar = bars.getBarByPoints(points);
        if (!bar.hasSameBarName(currentBar)) {
            UserPreferences.saveData(R.string.bar_key, bar.getName());
            AlertDialog alert = DialogBuilder.generateNextBarMsg(this, bar);
            alert.show();
        }
    }
    
    /**
     * Reset displayed scores.
     */
    public void resetScore() {
        TextView pointsLabel = (TextView) findViewById(R.id.points);
        pointsLabel.setText(String.valueOf("0"));
        TextView drinksLabel = (TextView) findViewById(R.id.drinks);
        drinksLabel.setText(String.valueOf("0"));
        UserPreferences.resetScore();
    }
    
    /**
     * Update the 'run' statistics.
     * 
     * @param wasCorrect If true, update the run by 1, otherwise reset to 0.
     */
    public void updateRun(boolean wasCorrect) {
        
        int run = 0;
        if (wasCorrect) {
            run = UserPreferences.getIntData(R.string.run_key, "0");
            run++;
        } 
        UserPreferences.saveData(R.string.run_key, String.valueOf(run));
    }
    
    /**
     * Reset the current run of correct answers.
     */
    public void resetRun() {
        UserPreferences.saveData(R.string.run_key, "0");
    }
    
    /**
     * Setup Game header info
     */
    private void setupHeaderInfo() {
        
        // Display header info
        TextView userLabel = (TextView) findViewById(R.id.user_label);
        userLabel.setText(UserPreferences.getStringData(R.string.user_name_key, "Who?"));
        
        // Display experience points
        TextView points = (TextView) findViewById(R.id.points);
        points.setText(UserPreferences.getStringData(R.string.points_key, "0"));
        
        TextView drinks = (TextView) findViewById(R.id.drinks);
        drinks.setText(UserPreferences.getStringData(R.string.drinks_key, "0"));
        
        updateIcon();
    }
    
    /**
     * Update user fails (drinks) score store + screen.
     */
    private void updateDrinks() {
        
        TextView drinksLabel = (TextView) findViewById(R.id.drinks);
        int drinks = Integer.parseInt(drinksLabel.getText().toString());
        drinks++;
        UserPreferences.saveData(R.string.drinks_key, String.valueOf(drinks));
        
        drinksLabel.setText(String.valueOf(drinks));
    }
    
    /**
     * Update user experience points to store + screen.
     */
    private void updateXP() {
        
        TextView pointsLabel = (TextView) findViewById(R.id.points);
        int points = Integer.parseInt(pointsLabel.getText().toString());
        points++;
        UserPreferences.saveData(R.string.points_key, String.valueOf(points));
        
        pointsLabel.setText(String.valueOf(points));
    }
    
    /**
     * Updates the user 'state' icon displayed on-screen.
     */
    private void updateIcon(boolean isCurrentAnswerCorrect) {
        
        TextView userIcon = (TextView) findViewById(R.id.user_label);
        boolean previousAnswerWasCorrect = UserPreferences.getBooleanData(R.string.last_answer_key);
        
        // Figure out what to display as the icon
        if (previousAnswerWasCorrect && !isCurrentAnswerCorrect) {
            userIcon.setCompoundDrawablesWithIntrinsicBounds(0, 0, Icon.getNeutralIcon().getIconID(), 0);
            showIconText(Icon.getNeutralIcon());
        } else {
            updateIcon();
        }
    }
    
    /**
     * Update icon.
     */
    private void updateIcon() {
        
        Icon icon = Icon.getIcon(
            UserPreferences.getIntData(R.string.points_key, "0"), UserPreferences.getIntData(R.string.drinks_key, "0"));
        
        TextView userIconView = (TextView) findViewById(R.id.user_label);
        userIconView.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon.getIconID(), 0);
        
        int currentIconID = UserPreferences.getIntData(R.string.icon_key, "0");
        if (icon.getIconID() != currentIconID) {
            showIconText(icon);
        }
        UserPreferences.saveData(R.string.icon_key, String.valueOf(icon.getIconID()));
    }
    
    /**
     * Display icon text.
     * 
     * @param icon
     */
    private void showIconText(Icon icon) {
        final Toast toast = Toast.makeText(this, icon.getText(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 40); // Position toast near icon on-screen
        toast.show();
    }
    
    /**
     * Display a question and process user response.
     * 
     * @param question The question
     */
    private void display(QuestionAndAnswers question, AnswerHandler handler) {
        
        // Display question
        TextView questionLabel = (TextView) findViewById(R.id.question);
        questionLabel.setText(question.getQuestion());
        
        // Display answers
        ArrayList<Answer> answers = question.getAnswers();
        Collections.shuffle(answers);
        ListView answerList = (ListView) findViewById(R.id.answers);
        ArrayAdapter<Answer> adapter = 
            new ArrayAdapter<Answer>(
                    this, 
                    R.layout.answers_list_view, 
                    R.id.answers_list_view,
                    answers);

        answerList.setAdapter(adapter);
        answerList.setOnItemClickListener(handler);
    }
    
    /**
     * End the game.
     * Display relevant message to user depending on their ultimate progress through the 'crawl'.
     */
    private void endGame() {
        
        String barName = UserPreferences.getStringData(R.string.bar_key, "");
        
        int endMessage = R.string.end_game_incomplete;
        if (Bars.getInstance().isLastBar(barName)) {
            endMessage = R.string.end_game_complete;
        }
        Dialog dialog = DialogBuilder.generateNewGame(this, R.string.game_over, endMessage, barName);
        dialog.show();
    }
}
