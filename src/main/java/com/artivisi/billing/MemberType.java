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
public class MemberType {
    public static final MemberType PREPAID = new MemberType("prepaid");
    public static final MemberType POSTPAID = new MemberType("postpaid");
    
    private String value;
    
    private MemberType(String value) {
        this.value = value;
    }
    
    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    
    public static MemberType getInstance(String value){
        if ((value == null) ||(value.equals(""))) throw new IllegalArgumentException("Must be one of: postpaid, prepaid");
        
        String trimmed = value.trim();
        if(trimmed.equalsIgnoreCase("prepaid")){
            return PREPAID;
        } else if(trimmed.equalsIgnoreCase("postpaid")){
            return POSTPAID;
        } else {
            throw new IllegalArgumentException("Must be one of: postpaid, prepaid");
        }
    }
}
