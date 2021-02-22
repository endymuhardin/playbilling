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
public class TimeUnit {
    public static final TimeUnit SECOND = new TimeUnit("second");
    public static final TimeUnit MINUTE = new TimeUnit("minute");
    public static final TimeUnit HOUR = new TimeUnit("hour");
    
    
    private String value;
    
    private TimeUnit(String name) {
        this.value = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public static TimeUnit getInstance(String name) {
        if ((name == null) ||(name.equals(""))) throw new IllegalArgumentException("Must be one of: second, minute, or hour");
        
        String trimmed = name.trim();
        if(trimmed.equalsIgnoreCase("second")){
            return SECOND;
        } else if(trimmed.equalsIgnoreCase("minute")){
            return MINUTE;
        } else if(trimmed.equalsIgnoreCase("hour")){
            return HOUR;
        } else {
            throw new IllegalArgumentException("Must be one of: second, minute, or hour");
        }
    }
    
    /**
     * @param value The value to set.
     */
    public void setValue(String name) {
        this.value = name;
    }
    
    public static String secondsToString(long seconds) {        
        long hours = seconds/3600;
        long minutes = (seconds-(hours*3600))/60;
        long secs = seconds - (3600*hours) - (60*minutes);
        return hours +" : "+minutes+" : "+secs;
    }
}
