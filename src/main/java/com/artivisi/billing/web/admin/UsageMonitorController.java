/*
 * Created on Jun 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
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
public class UsageMonitorController implements Controller {
    private Logger log = Logger.getLogger(UsageMonitorController.class);
	private String mainView;
	private String pageView;
	private String tableSorter;
	private BillingService billingService;
	
	
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
	    String opt = req.getParameter("opt");
	    String id = req.getParameter("id");
	    Session sess = null;
	    try {
	        if (StringUtils.hasText(opt) && StringUtils.hasText(id)) {
	            sess = billingService.getSessionById(new Integer(id));
	            if (sess != null) {
	                billingService.disconnect(sess);
	            }
	        }
	    } catch (Exception err) {
	        log.error("Cannot close session: "+sess);
	        log.error(err);
	    }
	    
		Map params = new HashMap();
		params.put("tableSorter", tableSorter);
		params.put("screen", pageView);
		
		List openSessions = billingService.getOpenSessions();
		params.put("openSessions", openSessions);
		
		return new ModelAndView(mainView, "vars", params);
	}

}
