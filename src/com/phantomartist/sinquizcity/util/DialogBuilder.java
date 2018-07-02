package com.phantomartist.sinquizcity.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.phantomartist.sinquizcity.R;
import com.phantomartist.sinquizcity.activities.GameDisplay;
import com.phantomartist.sinquizcity.game.Bar;

/**
 * Basic error handling
 *
 */
public class DialogBuilder {

    /**
     * Generate a standard message. Requires Activity in order to 
     * correctly associate the resulting Dialog.
     * 
     * @param context The context
     * @param msgID the message resource
     * @param isErrorMsg true if this is an error message
     * @return AlertDialog object
     */
    public static AlertDialog generateMsg(Activity context, int msgID, boolean isErrorMsg) {
        
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Chain together various setter methods to set the dialog characteristics
        builder.setMessage(msgID);
               
        if (isErrorMsg) {
            builder.setTitle(R.string.user_oops);
        }

        // Set the OK button
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int id) {
                   // User clicked OK button - nowt to do...
               }
           });
        
        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        return dialog;
    }
    
    /**
     * Generates 'next bar reached' message. Requires Activity in order to 
     * correctly associate the resulting Dialog.
     * 
     * @param context The context
     * @param bar The bar name
     *
     * @return AlertDialog
     */
    public static AlertDialog generateNextBarMsg(Activity context, Bar bar) {
        
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (Bars.getInstance().getFirstBar().equals(bar)) {
            builder.setTitle(context.getString(R.string.first_bar));
        } else {
            builder.setTitle(context.getString(R.string.next_bar_reached));
        }
        
        StringBuilder message = new StringBuilder().
                append(bar.getName()).
                append(" at ").
                append(bar.getProperty()).
                append("\n\n").
                append(bar.getInfo());
        builder.setMessage(message.toString());

        // Set the OK button
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int id) {
                   // User clicked OK button - nowt to do...
               }
           });
        
        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * Generate 'New Game' dialog.
     * Requires Activity in order to correctly associate the resulting Dialog.
     * 
     * @param game
     * @param msgID
     * @param endGameMsgID
     * 
     * @return AlertDialog
     */
    public static AlertDialog generateNewGame(final GameDisplay game, int msgID, int endGameMsgID, String barName) {
        
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(game);

        // Chain together various setter methods to set the dialog characteristics
        StringBuilder message = new StringBuilder(game.getString(endGameMsgID, barName));
        builder.setTitle(message.toString());
        
        builder.setMessage(msgID);

        // Set the OK button
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int id) {
                   game.resetScore();
                   game.newGame();
               }
           });
        
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int id) { 
                    game.resetScore();
                    game.finish();
               }
           });
        
        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
