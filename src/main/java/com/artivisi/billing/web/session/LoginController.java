/*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.session;



import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.artivisi.billing.BillingService;
import com.artivisi.billing.InsufficientCreditException;
import com.artivisi.billing.Member;
import com.artivisi.billing.MemberAlreadyConnectedException;
import com.artivisi.billing.MemberExpiredException;
import com.artivisi.billing.Session;
import com.artivisi.billing.SessionManagementException;
import com.artivisi.billing.WorkstationAlreadyConnectedException;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoginController implements Controller {

    private static final Logger log = Logger.getLogger(LoginController.class.getName());    
    private String loginView;
    private BillingService billingService;
    private String billingPage;
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String clientAddress = req.getRemoteAddr();
        log.info("Accepting request from "+clientAddress);
        
        // serve login page for first visit
        if (!StringUtils.hasText(req.getParameter("login"))) {
            String err = req.getParameter("error");            
            return new ModelAndView(loginView, "error", err);            
        }
        
        // request come from login form, process authentication
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        if ((!StringUtils.hasText(username)) || (!StringUtils.hasText(password))) {
            String err = "Invalid username["+username+"] or password["+password+"]";            
            log.info(err+" from "+clientAddress);
            return new ModelAndView(loginView, "error", err);
        }
        Member member = billingService.getMemberByUsernameAndPassword(username, password);
        
        if (member == null) {
            String err = "Unknown username or password";            
            log.warn(err);
            return new ModelAndView(loginView, "error", err);
        } 
        
        try {
            Session userSession = billingService.connect(member, clientAddress);
        } catch (SessionManagementException ex) {
            String err = "Fatal Error starting session : contact system administrator";            
            log.error(err);
            return new ModelAndView(loginView, "error", err);
        } catch (InsufficientCreditException ex) {
        	String err = "Insufficient member credit";            
            log.info(err);
            return new ModelAndView(loginView, "error", err);
        } catch (MemberExpiredException ex) {
        	String err = "Membership is expired";            
            log.info(err);
            return new ModelAndView(loginView, "error", err);
        } catch (WorkstationAlreadyConnectedException ex) {
        	res.sendRedirect(billingPage);
            return null;
        } catch (MemberAlreadyConnectedException ex) {
            Iterator openSessions = billingService.getOpenSession(member).iterator();
            while(openSessions.hasNext()) {
                Session sess = (Session) openSessions.next();
                billingService.disconnect(sess);
            }
            
            // now retry operation
            try {
                Session retrySession = billingService.connect(member, clientAddress);
            } catch (Exception err) {
                String err2 = "Cannot login due to internal error. Please contact administrator";            
                log.error(err2);
                return new ModelAndView(loginView, "error", err2);
            }
            
        	res.sendRedirect(billingPage);
            return null;
        }
        
        // all success, lets go to billing monitor
        res.sendRedirect(billingPage);
        return null;
    }

    /**
     * @param billingService The billingService to set.
     */
    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }
    /**
     * @param billingView The billingView to set.
     */
    public void setBillingPage(String billingPage) {
        this.billingPage = billingPage;
    }
    /**
     * @param loginView The loginView to set.
     */
    public void setLoginView(String loginView) {
        this.loginView = loginView;
    }
}
