/*
 * Created on Jun 23, 2005
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
public class MemberExpiredException extends RuntimeException {
    private Throwable cause;
    
    public MemberExpiredException(String message) {
        super(message);
    }
    
    public MemberExpiredException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }
}
