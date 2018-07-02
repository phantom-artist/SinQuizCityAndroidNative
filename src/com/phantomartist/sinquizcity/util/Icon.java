package com.phantomartist.sinquizcity.util;

import com.phantomartist.sinquizcity.R;

/**
 * Calculates the user icon displayed 
 * on-screen based on current points.
 */
public enum Icon {
    
    // Good
    GRIN(R.drawable.grin, "Feelin' good!"),
    WANT(R.drawable.want, "Alright!"),
    BUSINESS(R.drawable.serious_business, "Getting serious!"),
    HOT(R.drawable.on_fire, "Hot streak!"),
    PIRATE(R.drawable.arr, "Arrrrgh!"),
    NINJA(R.drawable.ninja, "Ninja skills!"),
    FLAWLESS(R.drawable.evilish, "FLAWLESS VICTORY!"),
    
    // Neutral
    HUH(R.drawable.huh, "Hmm..."),
    
    // Bad
    TIPSY(R.drawable.drunk, "Tipsy!"),
    DRUNK(R.drawable.xd, "Drunk!"),
    WASTED(R.drawable.x_x, "Wasted!"),
    ILL(R.drawable.sick, "Oooh, ill!"),
    ZOMBIE(R.drawable.zombie, "ZOMBIE TIME!");
    
    
    private int iconID;
    private String text;
    
    /**
     * Get icon ID.
     * 
     * @return int
     */
    public int getIconID() {
        return iconID;
    }
    
    /**
     * Get text.
     * 
     * @return String
     */
    public String getText() {
        return text;
    }
    
    /**
     * Constructor.
     * 
     * @param iconID The ID
     * @param text The text
     */
    private Icon(int iconID, String text) {
        this.iconID = iconID;
        this.text = text;
    }
    
    /**
     * Get the neutral icon.
     * 
     * @return HUH icon
     */
    public static Icon getNeutralIcon() {
        return HUH;
    }
    
    /**
     * Determine which icon to display based on user's points.
     * 
     * @param points the points
     * @param drinks the drinks
     * 
     * @return Icon The icon ID to display.
     */
    public static Icon getIcon(int points, int drinks) {
        
        Icon icon = HUH; // Huh? - (default icon)
        
        // Otherwise, do a quick calculation and figure out what to display...
        int run = points - drinks;
                
        // If negative...
        if (run < 1) {
            
            if (run <= -30) {
                return ZOMBIE; // Zombie
            }
            
            if (run <= -20) {
                return ILL; // Sick
            }
            
            if (run <= -15) {
                return WASTED; // Cross-eyed
            }

            if (run <= -10) {
                return DRUNK; // More drunk
            }
            
            if (run <= -5) {
                return TIPSY; // Drunk
            }

            return HUH; // Huh?
            
        } else {

            // Flawless
            if (run == 100) {
                return FLAWLESS; // Devil
            }
            
            // Others
            if (run >= 90) {
                return NINJA; // Ninja
            }
            
            if (run >= 75) {
                return PIRATE; // Pirate
            }
                    
            if (run >= 50) {
                return HOT; // Hot streak
            }
            
            if (run >= 25) {
                return BUSINESS; // Serious
            }
            
            if (run >= 15) {
                return WANT; // Want
            }
            
            if (run >= 5) {
                return GRIN; // Grin
            }
        }
        
        return icon;
    }
}
