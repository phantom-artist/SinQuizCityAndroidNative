package com.phantomartist.sinquizcity.util;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.phantomartist.sinquizcity.R;
import com.phantomartist.sinquizcity.game.Bar;

/**
 * Reads the res/xml/bar.xml
 */
public class Bars extends BaseParser {

    private static Bars instance;
    private ArrayList<Bar> bars;
	
    /**
     * Get singleton instance of Bar.
     * 
     * @param context
     * @param resourceID
     * @return Bars
     */
    public static Bars getInstance() {
		
        if (instance == null) {
	    instance = new Bars(R.xml.bars, "bar");
	}
        return instance;
    }
	
    /**
     * Returns the questions list.
     * 
     * @return ArrayList of questions.
     */
    public ArrayList<Bar> getBars() {
		
        return bars;
    }
	
    /**
     * Returns the bar you've reached based on points.
     * 
     * @param points The points (must be >= 0)
     * @return Bar the bar
     */
    public Bar getBarByPoints(int points) {
	
        if (points < 0) throw new IllegalArgumentException("Points must be greater than zero");
		
	for (int i = (bars.size() - 1); i >= 0; i--) {
            Bar bar = bars.get(i);
            if (points >= bar.getPoints()) {
                return bar;
            }
        }
        return getFirstBar();
    }
	
    /**
     * Returns the bar object based on the bar name.
     * 
     * @param barName the property name
     * @return Bar the bar
     */
    public Bar getBarByName(String barName) {
		
        for (Bar bar : bars) {
            if (bar.getName().equals(barName)) {
	        return bar;
	    }
	}
        return null; // Should not get here!
    }
	
    /**
     * Returns the first bar in the list.
     * 
     * @return Bar the first bar
     */
    public Bar getFirstBar() {
		
        return bars.get(0);
    }
	
    /**
     * Returns true if this is the last bar in the list.
     * 
     * @param barName The bar name
     * @return boolean true if last bar
     */
    public boolean isLastBar(String barName) {
		
        Bar bar = bars.get(bars.size() - 1);
	return bar.getName().equals(barName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseObject() throws XmlPullParserException, IOException {
		
        Log.v("parseObject()", ">>>");
		
        Bar bar = new Bar();

        // Keep reading until we reach end tag
	while (!isEndObject()) {

	    String value = null;
            if (XmlPullParser.TEXT == eventType) {
                value = parser.getText();
            }

            if (value != null) {
                processValue(bar, value);
            }

            // Increment parser
            eventType = parser.next();
            updateCurrentTag();
       }

       Log.v("parseObject()", "<<< " + bar);

       bars.add(bar);
    }
	
    /**
     * Process the value based on currentTag.
     * 
     * @param bar The bar
     * @param value The value
     */
    private void processValue(Bar bar, String value) {

        Log.v("processValue()", ">>>");

        if (Bar.PROPERTY_TAG.equals(currentTag)) {
	
            bar.setProperty(value);
            Log.v("processValue()", "property=" + bar.getProperty());
			
        } else if (Bar.NAME_TAG.equals(currentTag)) {
	
            bar.setName(value);
            Log.v("processValue()", "name=" + bar.getName());

        } else if (Bar.INFO_TAG.equals(currentTag)) {

            bar.setInfo(value);
            Log.v("processValue()", "info=" + bar.getInfo());

        } else if (Bar.POINTS_TAG.equals(currentTag)) {

            bar.setPoints(Integer.parseInt(value));
            Log.v("processValue()", "points=" + bar.getPoints());

        } else {
	    Log.v("processValue()", "Unknown tag/value - " + currentTag + "/" + value);
        }
		
        Log.v("processValue()", "<<<");
    }
	
    /**
     * Get instance of Bars
     */
    private Bars(int resourceID, String objectTag) {
        super(resourceID, objectTag);
        bars = new ArrayList<Bar>();
        parseObjectList();
    }
}
