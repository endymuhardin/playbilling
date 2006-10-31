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
public class SessionStatus {
    public static final SessionStatus OPEN = new SessionStatus("open");
    public static final SessionStatus CLOSED = new SessionStatus("closed");
    public static final SessionStatus PAID = new SessionStatus("paid");
    public static final SessionStatus CANCELLED = new SessionStatus("cancelled");
    
    private String name;
    
    private SessionStatus(String name) {
        this.name = name;
    }
    
    public static SessionStatus getInstance(String value) {
        if ((value == null) ||(value.equals(""))) throw new IllegalArgumentException("Must be one of: second, minute, or hour");
        
        String trimmed = value.trim();
        if(trimmed.equalsIgnoreCase("open")){
            return OPEN;
        } else if(trimmed.equalsIgnoreCase("closed")){
            return CLOSED;
        } else if(trimmed.equalsIgnoreCase("paid")){
            return PAID;
        } else if(trimmed.equalsIgnoreCase("cancelled")){
            return CANCELLED;
        } else {
            throw new IllegalArgumentException("Must be one of: open, closed, paid, cancelled");
        }
    }
    
    public String getValue() {
        return name;
    }
}
