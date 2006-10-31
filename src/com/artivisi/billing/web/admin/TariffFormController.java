/*
 * Created on Jun 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.artivisi.billing.BillingService;
import com.artivisi.billing.SimpleTariff;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TariffFormController extends SimpleFormController {
    private static final Logger log = Logger.getLogger(TariffFormController.class);
    private String nextPage;
    private String screen;
    private BillingService billingService;
    
    
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
     */
    protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, false));
    }
    /**
     * @param billingService The billingService to set.
     */
    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }
    
    /**
     * @param nextPage The nextPage to set.
     */
    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
    /**
     * @param screen The nextPage to set.
     */
    public void setScreen(String screen) {
        this.screen = screen;
    }
    
    
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest req)
            throws Exception {
        String id = req.getParameter("id");
        if (!StringUtils.hasText("id")) {
            return new SimpleTariff();
        }
        
        Integer intId = null;
        
        try {
            intId = new Integer(id); 
        } catch (NumberFormatException err) {
            return new SimpleTariff();
        }
        
        SimpleTariff tariff = billingService.getTariffById(intId);
        if (tariff == null) {
            tariff = new SimpleTariff();
        }
        return tariff;
    }
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map referenceData(HttpServletRequest req) throws Exception {        
        Map data = new HashMap();
        Map vars = new HashMap();
        vars.put("screen", screen);
        data.put("vars", vars);
        return data;
    }
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView onSubmit(HttpServletRequest req, HttpServletResponse res, Object obj, BindException err) throws Exception {
        String delete = req.getParameter("delete");
        SimpleTariff tar = (SimpleTariff) obj;
        if (StringUtils.hasText(delete)) {
          log.debug("Deleting object SimpleTariff: "+tar.toString());
          try {
              billingService.deleteTariff(tar);
          } catch (DataIntegrityViolationException ex) {
              log.debug(ex);              
              res.sendRedirect(nextPage+"?err=Tariff already used by member");
              return null;
          }
            
        } else {
            
            if (tar.getId().equals(new Integer(-1))) {  // new object, invoke create
                log.debug("Creating object SimpleTariff: "+tar.toString());
                try {
                    billingService.createTariff(tar);
                } catch (DataIntegrityViolationException ex) {
                    log.debug(ex);
                    res.sendRedirect(nextPage+"?err=Tariff already exists");
                    return null;
                }
                
            } else { // existing object, invoke update
                log.debug("Updating object SimpleTariff: "+tar.toString());
                billingService.updateTariff(tar);
            }
        }
        res.sendRedirect(nextPage);
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#handleInvalidSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleInvalidSubmit(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        res.sendRedirect(nextPage+"?err=Invalid Submission");
        return null;
    }
}
