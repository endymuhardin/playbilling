/*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.artivisi.billing.BillingService;
import com.artivisi.billing.Session;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LogoutController implements Controller {

    private static final Logger log = Logger.getLogger(LoginController.class.getName());
    private BillingService billingService;   
    private String loginPage;
    private String logoutPage;
    
    /**
     * @param logoutPage The logoutPage to set.
     */
    public void setLogoutPage(String logoutPage) {
        this.logoutPage = logoutPage;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ipAddress = req.getRemoteAddr();
        log.info(ipAddress+" wants to disconnect");
        
        // check session        
        Session sess =  billingService.getOpenSession(ipAddress);
        
        // session is null or expired, simply redirect to login
        if (sess == null || sess.isExpired()) {
            res.sendRedirect(loginPage);
            log.info("Invalid logout request from "+ipAddress);
            return null;
        }
        
        // session is good, close and disconnect
        billingService.disconnect(sess);       
        log.info("Session disconnected for "+ipAddress);
        return new ModelAndView(logoutPage, "session", sess);
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
}
