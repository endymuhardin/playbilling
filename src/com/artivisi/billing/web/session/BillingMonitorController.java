/*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.artivisi.billing.BillingService;
import com.artivisi.billing.InsufficientCreditException;
import com.artivisi.billing.MemberExpiredException;
import com.artivisi.billing.Session;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BillingMonitorController implements Controller {

    private static final Logger log = Logger.getLogger(LoginController.class.getName());
    private BillingService billingService;    
    private String loginPage;
    private String monitorView;
	private String logoutPage;
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ipAddress = req.getRemoteAddr();
        log.info(ipAddress+" refreshing session");
        
        // check session        
        Session sess =  billingService.getOpenSession(ipAddress);
        log.debug("Session : "+sess);
        
        // session is null or expired, redirect to login
        if (sess == null || sess.isExpired()) {
            log.warn("Invalid session request from "+ipAddress+", may be session hijack");
            res.sendRedirect(loginPage+"?error=Invalid session, please relogin"); 
            return null;
        }
        
        log.debug("Open Session for "+ipAddress+" owned by "+sess.getMember().getUsername());
        
        // session is good, refresh and display
        try {
        	billingService.refreshSession(sess);
        } catch (InsufficientCreditException ex) {
        	log.info("Insufficient member credit for request from "+ipAddress);
            res.sendRedirect(logoutPage); 
            return null;
        } catch (MemberExpiredException ex) {        	
            res.sendRedirect(logoutPage); 
            return null;
        }
               
        log.info("Session refreshed for "+ipAddress);
        
        Map vars = new HashMap();        
        vars.put("session", sess);
        
        return new ModelAndView(monitorView, "vars", vars);
    }

    /**
     * @param billingService The billingService to set.
     */
    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }
    /**
     * @param loginPage The loginPage to set.
     */
    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
    /**
     * @param monitorView The monitorView to set.
     */
    public void setMonitorView(String monitorView) {
        this.monitorView = monitorView;
    }
	/**
	 * @param logoutPage The logoutPage to set.
	 */
	public void setLogoutPage(String logoutPage) {
		this.logoutPage = logoutPage;
	}
}
