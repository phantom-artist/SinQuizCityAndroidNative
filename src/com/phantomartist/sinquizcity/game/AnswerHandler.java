package com.phantomartist.sinquizcity.game;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.phantomartist.sinquizcity.R;
import com.phantomartist.sinquizcity.activities.GameDisplay;
import com.phantomartist.sinquizcity.util.UserPreferences;

/**
 * Handles user response.
 */
public class AnswerHandler implements OnItemClickListener {

	private GameDisplay display;
	private QuestionAndAnswers question;
	private String correctResponse;
	private String wrongResponse;
	
	/**
	 * Constructor
	 * 
	 * @param question Question
	 * @param correctResponse String to display
	 * @param wrongResponse String to display
	 */
	public AnswerHandler(GameDisplay display, QuestionAndAnswers question, String correctResponse, String wrongResponse) {
		this.display = display;
		this.question = question;
		this.correctResponse = correctResponse;
		this.wrongResponse = wrongResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		
		Log.d(getClass().getName(), "onItemClicked() - position=" + position);
		
		QuestionAndAnswers.Answer answer = 
			(QuestionAndAnswers.Answer)adapter.getItemAtPosition(position);
		
		// Right or Wrong?
		if (question.isCorrectAnswer(answer)) {
			handleResponse(view, Color.GREEN, correctResponse, true);
		} else {
			handleResponse(view, Color.RED, wrongResponse, false);
		}
	}
	
	/**
	 * Handle the user's response by configuring screen.
	 * 
	 * @param view The item view to change
	 * @param color The color to set
	 * @param basicResponse The basic response
	 * @param wasCorrect true if the correct response was selected
	 * 
	 * @return Toast the response to the user.
	 */
	private void handleResponse(View view, int color, String basicResponse, boolean wasCorrect) {
		
		view.setBackgroundColor(color);
		final Toast toast = Toast.makeText(display, getResponse(basicResponse, wasCorrect), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		
		display.updateDisplay(wasCorrect);
		
		// Set the 'next question' delay...
		long delay = 600;
		if (question.hasTrivia() || !wasCorrect) {
			delay = 1500;
		}
		
		// Pause, then display next question
		Handler handler = new Handler(); 
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				toast.cancel();
				display.updateBar();
				display.displayNextQuestion();
			}
		}, delay);
	}
	
	/**
	 * Formats a response to the user based on whether they selected the right answer or not.
	 * 
	 * @param response The response
	 * @param isCorrect determines whether to tab on the correct answer or not.
	 * 
	 * @return String the complete response.
	 */
	private String getResponse(String response, boolean isCorrect) {
		
		StringBuilder formattedResponse = new StringBuilder(response);
		
		boolean showAnswers = UserPreferences.getBooleanData(R.string.show_answers_key);

		if (!isCorrect && showAnswers) {
			formattedResponse.append("\n\n").append("Answer is: ").append(question.getCorrectAnswer().getAnswer());
		}
		
		if ((isCorrect || showAnswers) && question.getTrivia() != null) {
			formattedResponse.append("\n\n").append(question.getTrivia());
		}
		
		return formattedResponse.toString();
	}
}
