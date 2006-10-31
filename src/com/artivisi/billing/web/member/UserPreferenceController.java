/*
 * Created on Jun 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.member;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
public class UserPreferenceController implements Controller {

    private String loginPage;
    private String preferenceView;
    private String mainView;
    private BillingService billingService;

    /**
     * @param mainView The mainView to set.
     */
    public void setMainView(String mainView) {
        this.mainView = mainView;
    }
    /**
     * @param billingService The billingService to set.
     */
    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }
    /**
     * @param preferenceView The preferenceView to set.
     */
    public void setPreferenceView(String preferenceView) {
        this.preferenceView = preferenceView;
    }
    /**
     * @param loginPage The loginPage to set.
     */
    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession sess = req.getSession(false);
        
        if (sess == null || (sess.getAttribute("member")==null)) {
            res.sendRedirect(loginPage);
            return null;
        }
        
        Member member = (Member) sess.getAttribute("member");
        Map vars = new HashMap();
        vars.put("member", member);
        vars.put("screen", preferenceView);
        
        if (StringUtils.hasText(req.getParameter("save"))) {
            String fullname = req.getParameter("fullname");
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirm-password");
            String email = req.getParameter("email");
            String address = req.getParameter("address");
            
            member.setFullname(fullname);
            member.setEmail(email);
            member.setAddress(address);
            
            vars.put("error", "Your data is updated");
            
            if (StringUtils.hasText(password) && StringUtils.hasText((confirmPassword))) {
                if (password.equals(confirmPassword)) {
                    member.setPassword(password);
                    vars.put("error", "All data is updated");
                } else {
                    vars.put("error", "Password confirmation mismatch, not updated");
                }
            }
            
            billingService.updateMember(member);
        }
        
        return new ModelAndView(mainView, "vars", vars);
    }

}
