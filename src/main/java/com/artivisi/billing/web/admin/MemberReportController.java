/*
 * Created on Jun 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class MemberReportController implements Controller {

	private String mainView;
	private String pageView;
	private String tableSorter;
	private BillingService billingService;
	private static final Logger log = Logger.getLogger(MemberReportController.class);
	
	
    /**
     * @param billingService The billingService to set.
     */
    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }
	public void setTableSorter(String tableSorter) {
		this.tableSorter = tableSorter;
	}
	
	public void setMainView(String mainView) {
		this.mainView = mainView;
	}
	public void setPageView(String pageView) {
		this.pageView = pageView;
	}
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		Map params = new HashMap();
		params.put("allSessions", new ArrayList());
		params.put("tableSorter", tableSorter);
		params.put("screen", pageView);
		
		List allMembers = billingService.getAllMember();
		params.put("allMembers", allMembers);
		
		if(StringUtils.hasText(req.getParameter("submit"))) {
		   String opt = req.getParameter("searchoption");
		   String id = req.getParameter("member_id");
		   
		   log.debug("Search Option: "+opt);
		   log.debug("Member ID: "+id);
		   
		   Member member = billingService.getMemberById(new Integer(id));
		   params.put("member", member);
		   
		   DateTime start = new DateTime();
		   DateTime end = new DateTime();
		   
		   if ("interval".equals(opt)) {
		       String startTime = req.getParameter("start");
		       String endTime = req.getParameter("end");
		       
		       if (!StringUtils.hasText(startTime) || !StringUtils.hasText(endTime)) {
		           params.put("error", "Untuk pilihan interval, field tanggal harus diisi");
		           return new ModelAndView(mainView, "vars", params);
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
		           params.put("error", "Untuk pilihan bulan, field bulan dan tahun harus diisi");
		           return new ModelAndView(mainView, "vars", params);
		       }
		       
		       int searchMonth = Integer.parseInt(month);
		       int searchYear = Integer.parseInt(year);
		       
		       start = new DateTime(searchYear, searchMonth, 1, 0, 0, 0, 0);
		       end = start.plus(Period.months(1));
		       
		   }
		   
		   log.debug("Converted start date: "+start);
	       log.debug("Converted end date: "+end);
		   List sessions = billingService.getSessions(member, start.toDate(), end.toDate());
	       params.put("allSessions", sessions);
	       
	       Iterator it = sessions.iterator();
	       while(it.hasNext()) {
	           member.getSessions().add(it.next());
	       }
		}
		
		return new ModelAndView(mainView, "vars", params);
	}
}

