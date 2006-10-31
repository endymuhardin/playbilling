/*
 * Created on Jun 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.admin;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.artivisi.billing.Member;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MemberValidator implements Validator {    
    /* (non-Javadoc)
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class cls) {        
        return cls.equals(Member.class);
    }

    /* (non-Javadoc)
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object obj, Errors err) {        
                
        Member m = (Member) obj;
        if (!StringUtils.hasText(m.getUsername())) {
            err.rejectValue("username", "member.username.required", "Username harus diisi");
        }            
        if (!StringUtils.hasText(m.getEmail())) {
            err.rejectValue("email", "member.email.required", "Email harus diisi");
        }
    }

}
