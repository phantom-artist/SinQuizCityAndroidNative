package com.phantomartist.sinquizcity;

/**
 * Denotes a General Application Exception occurred.
 */
public class GeneralAppException extends RuntimeException {

    private static final long serialVersionUID = 1117263487465566L;

    public GeneralAppException(Exception e) {
        super(e);
    }
}
