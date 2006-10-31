/*
 * Created on Jun 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.artivisi.billing.BillingService;
import com.artivisi.billing.Member;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoginController implements Controller {
    private static final Logger log = Logger.getLogger(LoginController.class);
    private String loginView;
    private BillingService billingService;
    private String memberPage;

    /**
     * @param memberPage The memberPage to set.
     */
    public void setMemberPage(String memberPage) {
        this.memberPage = memberPage;
    }
    /**
     * @param billingService The billingService to set.
     */
    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }
    /**
     * @param loginView The loginView to set.
     */
    public void setLoginView(String loginView) {
        this.loginView = loginView;
    }
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
//      serve login page for first visit
        if (!StringUtils.hasText(req.getParameter("login"))) {
            String err = req.getParameter("error");            
            return new ModelAndView(loginView, "error", err);            
        }
        
        // request come from login form, process authentication
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        if ((!StringUtils.hasText(username)) || (!StringUtils.hasText(password))) {
            String err = "Invalid username["+username+"] or password["+password+"]";   
            return new ModelAndView(loginView, "error", err);
        }
        Member member = billingService.getMemberByUsernameAndPassword(username, password);
        
        if (member == null) {
            String err = "Unknown username or password";            
            log.warn(err);
            return new ModelAndView(loginView, "error", err);
        }
        
        req.getSession().setAttribute("member", member);
        
        // all success, lets go to billing monitor
        res.sendRedirect(memberPage);
        return null;
    }

}
