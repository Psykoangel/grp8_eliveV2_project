package fr.group8.elive.exceptions;

import fr.group8.elive.utils.NfcExceptionType;

/**
 * Created by psyko on 28/01/16.
 */
public class NfcException extends Exception {
    private NfcExceptionType exceptionType;

    public NfcException(String detailMessage, NfcExceptionType exceptionType) {
        super(detailMessage);
        this.exceptionType = exceptionType;
    }


    public String getExceptionType() {
        return exceptionType.toString();
    }
}
