/*
 * Created on Jun 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AlreadyConnectedException extends Exception {
    private Throwable cause;
    
    public AlreadyConnectedException(String message) {
        super(message);
    }
    
    public AlreadyConnectedException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }
}
