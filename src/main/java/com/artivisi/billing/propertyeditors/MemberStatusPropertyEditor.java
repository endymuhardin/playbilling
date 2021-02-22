/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.propertyeditors;

import java.beans.PropertyEditorSupport;

import com.artivisi.billing.MemberStatus;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MemberStatusPropertyEditor extends PropertyEditorSupport {
    
    /* (non-Javadoc)
     * @see java.beans.PropertyEditor#getAsText()
     */
    public String getAsText() {        
        return (getValue() == null ? "" : ((MemberStatus)getValue()).getValue());
    }
    /* (non-Javadoc)
     * @see java.beans.PropertyEditor#setAsText(java.lang.String)
     */
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(MemberStatus.getInstance(text));
    }
}
