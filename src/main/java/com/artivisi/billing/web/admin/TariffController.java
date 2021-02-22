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

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.artivisi.billing.BillingService;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TariffController implements Controller {

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
		Map params = new HashMap();
		
		String err = req.getParameter("err");
        params.put("errors", err);
        
        params.put("tableSorter", tableSorter);
		params.put("screen", pageView);
		
		List allTariff = billingService.getAllTariff();
		params.put("allTariff", allTariff);
		
		return new ModelAndView(mainView, "vars", params);
	}

}
