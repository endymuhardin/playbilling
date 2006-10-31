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

import com.artivisi.billing.SimpleTariff;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TariffValidator implements Validator {

    /* (non-Javadoc)
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class cls) {        
        return cls.equals(SimpleTariff.class);
    }

    /* (non-Javadoc)
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object obj, Errors err) {        
        try {
            SimpleTariff t = (SimpleTariff) obj;
            if (!StringUtils.hasText(t.getName())) {
                err.rejectValue("name", "tariff.name.required", "Nama Tarif harus diisi");
            }            
          } catch (IllegalArgumentException ex) {              
              err.reject("tariff.field.malformat", "Field harus diisi dengan angka");
          }
    }

}
