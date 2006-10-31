/*
 * Created on Jun 22, 2005
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
public class WorkstationAlreadyConnectedException extends
        AlreadyConnectedException {
    public WorkstationAlreadyConnectedException(String message) {
        super(message);
    }
    
    public WorkstationAlreadyConnectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
