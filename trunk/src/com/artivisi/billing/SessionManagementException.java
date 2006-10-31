/*
 * Created on Jun 8, 2005
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
public class SessionManagementException extends RuntimeException {
    private Throwable cause;
    
    
    /**
     * @param msg exception description
     * @param cause root cause of this exception.
     */
    public SessionManagementException(String msg, Throwable cause) {
        this(msg);
        this.cause = cause;
    }


    /**
     * @param msg exception description
     */
    public SessionManagementException(String msg) {
        super(msg);
    }
}
