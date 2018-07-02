package com.phantomartist.sinquizcity.util;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.phantomartist.sinquizcity.GeneralAppException;
import com.phantomartist.sinquizcity.MainActivity;

/**
 * Base parser for XML documents.
 */
public abstract class BaseParser {

    protected XmlResourceParser parser;
    protected int eventType = -1;
    protected String objectTag;
    protected String currentTag;
    
    /**
     * Constructor.
     * 
     * @param objectTag
     */
    protected BaseParser(int resourceID, String objectTag) {
        this.objectTag = objectTag;
        parser = MainActivity.getAppContext().getResources().getXml(resourceID);
    }
    
    /**
     * Parses the list of objects.
     */
    protected void parseObjectList() {
        
        Log.d("parseObjectList()", ">>>");
        
        try {
            while (eventType != XmlPullParser.END_DOCUMENT) {
                
                eventType = parser.next(); // Increment parser to next item
                updateCurrentTag(); // Update current tag name
                
                // If not an end-tag, process the data
                if (!(XmlPullParser.END_TAG == eventType)) {
                    
                    if (isStartObject()) {
                        
                        parseObject();
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(Questions.class.getName(), "Question XML parsing problem", e);
            throw new GeneralAppException(e);
        } catch (IOException e) {
            Log.e(Questions.class.getName(), "Can't read question XML!", e);
            throw new GeneralAppException(e);
        }
        
        Log.d("parseObjectList()", "<<<");
    }
    
    /**
     * Parses an individual object - implement in sub-class.
     * 
     * @throws XmlPullParserException if parser exception occurs.
     * @throws IOException if read exception occurs.
     */
    protected abstract void parseObject() throws XmlPullParserException, IOException;

    /**
     * Returns true if we are processing end tag.
     * 
     * @return boolean true if we are processing an object end tag
     */
    protected boolean isStartObject() {
        
        return objectTag.equals(currentTag) && XmlPullParser.START_TAG == eventType;
    }
    
    /**
     * Returns true if we are processing end tag.
     * 
     * @return boolean true if we are processing an object end tag
     */
    protected boolean isEndObject() {
        
        return objectTag.equals(currentTag) && XmlPullParser.END_TAG == eventType;
    }
    
    /**
     * Update currentTag if the parser is on a tag node.
     */
    protected void updateCurrentTag() {
        String tag = parser.getName();
        if (tag != null) {
            currentTag = tag;
        }
    }
}
