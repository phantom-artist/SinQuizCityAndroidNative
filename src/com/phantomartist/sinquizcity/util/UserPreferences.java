package com.phantomartist.sinquizcity.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.phantomartist.sinquizcity.MainActivity;
import com.phantomartist.sinquizcity.R;
import com.phantomartist.sinquizcity.intent.Messages;

/**
 * Save user preferences - uses Android SharedPreferences
 */
public class UserPreferences {

    public static void saveData(int key, String data) {
        
        SharedPreferences sharedPref = MainActivity.getAppContext().getSharedPreferences(Messages.BASE_PKG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MainActivity.getAppContext().getString(key), data);
        editor.commit();
    }
    
    /**
     * Gets String data from UserPreferences.
     * 
     * @param context The context
     * @param key The key
     * @param defaultValue The default value if nothing is found
     * 
     * @return String the data (or the default value null)
     */
    public static String getStringData(int key, String defaultValue) {
        
        SharedPreferences sharedPref = MainActivity.getAppContext().getSharedPreferences(Messages.BASE_PKG, Context.MODE_PRIVATE);
        
        return sharedPref.getString(MainActivity.getAppContext().getResources().getString(key), defaultValue);
    }
    
    /**
     * Gets int data from UserPreferences.
     * 
     * @param key The key
     * @param defaultValue The default value if nothing is found
     * 
     * @return String the data (or the default value null)
     */
    public static int getIntData(int key, String defaultValue) {
        
        return Integer.parseInt(getStringData(key, defaultValue));
    }
    
    /**
     * Gets boolean data from UserPreferences.
     * 
     * @param key The key
     * 
     * @return boolean the data
     */
    public static boolean getBooleanData(int key) {
        
        return "true".equalsIgnoreCase(getStringData(key, "false"));
    }
    
    /**
     * Clears out all stored preferences for the user.
     */
    public static void resetUserPreferences() {
        
        SharedPreferences sharedPref = 
            MainActivity.getAppContext().getSharedPreferences(Messages.BASE_PKG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }
    
    /**
     * Clears the score for the current user.
     */
    public static void resetScore() {
        
        Context appContext = MainActivity.getAppContext();
        SharedPreferences sharedPref = 
            appContext.getSharedPreferences(Messages.BASE_PKG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(appContext.getString(R.string.points_key), "0");
        editor.putString(appContext.getString(R.string.drinks_key), "0");
        editor.putString(appContext.getString(R.string.bar_key), Bars.getInstance().getFirstBar().getName());
        editor.commit();
    }
}
