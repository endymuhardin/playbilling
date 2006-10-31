/*
 * Created on Jun 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing;

/**
 * @author Anton Raharja
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InsufficientCreditException extends Exception {
	private Throwable cause;
    
    public InsufficientCreditException(String message) {
        super(message);
    }
    
    public InsufficientCreditException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }
}
