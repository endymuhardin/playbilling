/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.connection;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConnectionManagementException extends RuntimeException {
    private Throwable cause;
    
    /**
     * @param msg exception description
     * @param cause root cause of this exception.
     */
    public ConnectionManagementException(String msg, Throwable cause) {
        this(msg);
        this.cause = cause;
    }


    /**
     * @param msg exception description
     */
    public ConnectionManagementException(String msg) {
        super(msg);
    }
}
