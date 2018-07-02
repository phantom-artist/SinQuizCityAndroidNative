package com.phantomartist.sinquizcity.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import com.phantomartist.sinquizcity.MainActivity;
import com.phantomartist.sinquizcity.R;

/**
 * Utility class to get reference to strings XML data.
 *
 */
public final class Strings {

	/**
	 * Return a formatted greeting based on number of visits and userName.
	 * 
	 * @param activity The activity context
	 * @param visits The number of visits the user has had
	 * @param userName The userName of the user
	 * @return String The greeting response
	 */
	public static Spannable getFormattedGreetingResponse(int visits, String userName) {

		Context appContext = MainActivity.getAppContext();
		Resources res = appContext.getResources();
		String[] responses = res.getStringArray(R.array.vegas_visits_response);
		
		String unformattedResponse = responses[visits];
		
		// Find the token to replace with userName
		int start = unformattedResponse.indexOf("%1$s");
		int end = start + userName.length();
		
		String formattedResponse = String.format(unformattedResponse, userName);
		
		// Format the userName with highlight color
		int color = appContext.getResources().getColor(R.color.highlight);
		Spannable spannable = Spannable.Factory.getInstance().newSpannable(formattedResponse);
		spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		return spannable;
	}
	
	/**
	 * Crude string checker.
	 * 
	 * @param toTest
	 * @return boolean 
	 */
	public static boolean isValidString(String toTest) {
		return !(toTest == null || "".equals(toTest));
	}
}
