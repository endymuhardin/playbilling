/*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.member;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
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
public class UserBillingReportController implements Controller {
    private static final Logger log = Logger.getLogger(UserBillingReportController.class);
    private String loginPage;
    private String usageView;
    private BillingService billingService;
    private String mainView;

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
     * @param usageView The usageView to set.
     */
    public void setUsageView(String usagePage) {
        this.usageView = usagePage;
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
        vars.put("screen", usageView);
        
        if(StringUtils.hasText(req.getParameter("submit"))) {
           String opt = req.getParameter("searchoption");
 		   DateTime start = new DateTime();
 		   DateTime end = new DateTime();
 		   
 		   if ("interval".equals(opt)) {
 		       String startTime = req.getParameter("start");
 		       String endTime = req.getParameter("end");
 		       
 		       if (!StringUtils.hasText(startTime) || !StringUtils.hasText(endTime)) {
 		           vars.put("error", "Untuk pilihan interval, field tanggal harus diisi");
 		           return new ModelAndView(mainView, "vars", vars);
 		       }
 		       
 		       log.debug("Start Time: "+startTime);
 		       log.debug("End Time: "+endTime);
 		       
 		       DateTimeFormatter fmt = ISODateTimeFormat.yearMonthDay();
 		       start = fmt.parseDateTime(startTime);
 		       end = fmt.parseDateTime(endTime);
 		       
 		   } else if ("month".equals(opt)) {
 		       String month = req.getParameter("month");
 		       String year = req.getParameter("year");
 		       
 		       log.debug("Month : "+month);
 		       log.debug("Year : "+year);
 		       
 		       if (!StringUtils.hasText(month) || !StringUtils.hasText(year)) {
 		           vars.put("error", "Untuk pilihan bulan, field bulan dan tahun harus diisi");
 		           return new ModelAndView(mainView, "vars", vars);
 		       }
 		       
 		       int searchMonth = Integer.parseInt(month);
 		       int searchYear = Integer.parseInt(year);
 		       
 		       start = new DateTime(searchYear, searchMonth, 1, 0, 0, 0, 0);
 		       end = start.plus(Period.months(1));
 		       
 		   }
 		   
 		   log.debug("Converted start date: "+start);
 	       log.debug("Converted end date: "+end);
 		   List sessions = billingService.getSessions(member, start.toDate(), end.toDate());
 	      
 	       member.getSessions().clear();
 	       Iterator it = sessions.iterator();
 	       while(it.hasNext()) {
 	           member.getSessions().add(it.next());
 	       }
 		}
        
        return new ModelAndView(mainView, "vars", vars);        
    }

}
