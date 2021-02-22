/*
 * Created on Jun 9, 2005
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
public class MemberStatus {
    public static final MemberStatus INACTIVE = new MemberStatus("inactive");
    public static final MemberStatus ACTIVE= new MemberStatus("active");
    public static final MemberStatus LOCKED= new MemberStatus("locked");
    
    private String name;
    
    private MemberStatus(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return name;
    }
    
    public static MemberStatus getInstance(String value){
        if ((value == null) ||(value.equals(""))) throw new IllegalArgumentException("Must be one of: active, locked, or inactive");
        
        String trimmed = value.trim();
        if(trimmed.equalsIgnoreCase("inactive")){
            return INACTIVE;
        } else if(trimmed.equalsIgnoreCase("active")){
            return ACTIVE;
        } else if(trimmed.equalsIgnoreCase("locked")){
            return LOCKED;
        } else {
            throw new IllegalArgumentException("Must be one of: active, inactive, locked");
        }
    }
}
