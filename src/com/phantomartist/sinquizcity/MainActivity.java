package com.phantomartist.sinquizcity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.phantomartist.sinquizcity.activities.GameDisplay;
import com.phantomartist.sinquizcity.game.Game;
import com.phantomartist.sinquizcity.util.DialogBuilder;
import com.phantomartist.sinquizcity.util.Strings;
import com.phantomartist.sinquizcity.util.UserPreferences;

/**
 * Entry point to the game
 */
public class MainActivity extends Activity {
	
    private static MainActivity main;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = this;
        setupUIOptions();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Utility method for getting at application context for non-Activity-based classes
     * that need access to application resources.
     * 
     * This should be used rather than passing local Activity references for classes that 
     * have no other access to global system resources.
     * 
     * http://android-developers.blogspot.co.uk/2009/01/avoiding-memory-leaks.html
     * 
     * @return Context The global application context. 
     */
    public static Context getAppContext() {
    	return main.getApplicationContext();
    }
    
    /**
     * Called when "Memorize/I'm Back/Continue Game" is clicked.
     * Either continues an existing game or starts a fresh one
     * and collects user information.
     *
     * @param view The view that relates to the button. 
     */
    public void runGame(View view) {
    	
        // What action to perform? (Based on Button txt)
        Button btn = (Button) findViewById(R.id.user_btn);
        
        // If 'continue' then just go back to game...
        if (getString(R.string.user_btn_continue).equals(btn.getText())) {
            
            goToGame(false);
            
        } else {
            
            // Figure some stuff out
            EditText userPrompt = (EditText) findViewById(R.id.user_prompt);
            String userName = userPrompt.getText().toString();
            
            if (Strings.isValidString(userName)) {
                
                // Do we have a new user name?
                if (isNewUser(userName)) {
                    UserPreferences.resetUserPreferences();
                }
                
                Spinner vegasVisits = (Spinner) findViewById(R.id.vegas_visits);
                int numVisits = vegasVisits.getSelectedItemPosition();
                
                UserPreferences.saveData(R.string.user_name_key, userName);
                UserPreferences.saveData(R.string.vegas_visits_key, String.valueOf(numVisits));
                
                Spannable greetingResponse = Strings.getFormattedGreetingResponse(numVisits, userName);
                
                TextView responseView = (TextView)findViewById(R.id.response_view);
                responseView.setText(greetingResponse, BufferType.SPANNABLE);
                
                final Toast toast = Toast.makeText(this, R.string.loading_questions, Toast.LENGTH_SHORT);
                toast.show();
                
                goToGame(true);
                
            } else {
                
                // Alert user there is a problem
                AlertDialog alert = DialogBuilder.generateMsg(this, R.string.error_missing_username, true);
                alert.show();
            }
        }
    }

    /**
     * Called when "Reveal Answers?" is clicked.
     * 
     * @param view
     */
    public void onShowAnswersClick(View view) {
        
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        UserPreferences.saveData(R.string.show_answers_key, String.valueOf(checked));
    }
    
    /**
     * Setup some UI options
     */
    private void setupUIOptions() {
    	
    	// Have we played before?
    	boolean hasPlayedBefore = false;
    	
    	String userName = UserPreferences.getStringData(R.string.user_name_key, null);
    	if (Strings.isValidString(userName)) {
    		hasPlayedBefore = true;
    		EditText userPrompt = (EditText)findViewById(R.id.user_prompt);
    		userPrompt.setText(userName);
    	}
    	
    	Spinner spinner = (Spinner) findViewById(R.id.vegas_visits);
    	
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence> adapter = 
    			ArrayAdapter.createFromResource(this, R.array.vegas_visits, android.R.layout.simple_spinner_item);
    	
    	// Specify the layout to use when the list of choices appears
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    	// Apply the adapter to the spinner
    	spinner.setAdapter(adapter);

    	// Are we 'revealing answers'?
    	CheckBox reveal = (CheckBox) findViewById(R.id.chk_show_answers);
    	reveal.setChecked(UserPreferences.getBooleanData(R.string.show_answers_key));
    	
        // Did we just navigate back from a current game?
    	boolean isInProgress = Game.isInProgress();
    	
    	if (hasPlayedBefore || isInProgress) {
    		String visits = UserPreferences.getStringData(R.string.vegas_visits_key, "0");
    		int position = adapter.getPosition(visits);
    		spinner.setSelection(position, true);

    		Button btn = (Button) findViewById(R.id.user_btn);
    		if (isInProgress) {
    		    btn.setText(getString(R.string.user_btn_continue));
    		} else {
    		    btn.setText(getString(R.string.user_btn_return));
    		}
    	}
    	
    	if (!isInProgress) {
    	    UserPreferences.resetScore();
    	}
    }
    
    /**
     * Checks if this is a new user. (Ignores case).
     * 
     * @param userName The username
     * @return boolean true if userName does not match existing stored user. 
     */
    private boolean isNewUser(String userName) {
    	String existingUser = UserPreferences.getStringData(R.string.user_name_key, null);
    
    	return !(userName.equalsIgnoreCase(existingUser));
    }
    
    /**
     * Goes to the game screen (with optional pause).
     */
    private void goToGame(boolean addPause) {
        
        final Intent intent = new Intent(this, GameDisplay.class);
        
        if (addPause) {
            Handler handler = new Handler(); 
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TextView responseView = (TextView)findViewById(R.id.response_view);
                    responseView.setText("", BufferType.SPANNABLE);
                    startActivity(intent);
                }
            }, 1000);
        } else {
            startActivity(intent);
        }
    }
}
