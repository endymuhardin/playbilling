/*
 * Created on Jun 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.web.admin;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.artivisi.billing.BillingService;
import com.artivisi.billing.Member;
import com.artivisi.billing.MemberStatus;
import com.artivisi.billing.MemberType;
import com.artivisi.billing.SimpleTariff;
import com.artivisi.billing.propertyeditors.MemberStatusPropertyEditor;
import com.artivisi.billing.propertyeditors.MemberTypePropertyEditor;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MemberFormController extends SimpleFormController {
    private static final Logger log = Logger.getLogger(MemberFormController.class);
    private String nextPage;
    private String screen;
    private BillingService billingService;
    
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
     * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
     */
    protected void initBinder(HttpServletRequest req, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(MemberStatus.class, new MemberStatusPropertyEditor());
        binder.registerCustomEditor(MemberType.class, new MemberTypePropertyEditor());
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map referenceData(HttpServletRequest arg0) throws Exception {
        Map data = new HashMap();
        Map vars = new HashMap();
        
        Map memberTypes = new LinkedHashMap();
        memberTypes.put(MemberType.PREPAID.getValue(), "Prepaid");
        memberTypes.put(MemberType.POSTPAID.getValue(), "Postpaid");
        
        Map memberStatus = new LinkedHashMap();
        memberStatus.put(MemberStatus.INACTIVE.getValue(), "Inactive");
        memberStatus.put(MemberStatus.ACTIVE.getValue(), "Active");
        memberStatus.put(MemberStatus.LOCKED.getValue(), "Locked");
        
        Map tariffOption = new LinkedHashMap();
        List allTariff = billingService.getAllTariff();
        for(Iterator i = allTariff.iterator(); i.hasNext(); ){
            SimpleTariff tariff = (SimpleTariff) i.next();
            tariffOption.put(tariff.getId(), tariff.getName());
        }
                
        vars.put("screen", screen);
        vars.put("memberTypes", memberTypes);
        vars.put("memberStatus", memberStatus);
        vars.put("allTariff", allTariff);
        
        data.put("vars", vars);
        return data;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest req)
            throws Exception {
        String id = req.getParameter("id");
        log.debug("Member id: "+id);
        if (!StringUtils.hasText("id")) {
            return new Member();
        }
        
        Integer intId = null;
        
        try {
            intId = new Integer(id); 
        } catch (NumberFormatException err) {
            return new Member();
        }
        
        Member member = billingService.getMemberById(intId);
        if (member == null) {
            member = new Member();
        }
        return member;
    }
    
    
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.BaseCommandController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException)
     */
    protected void onBindAndValidate(HttpServletRequest req, Object obj,  BindException err) throws Exception {
        Member member = (Member) obj;
        
        String confirmPassword = req.getParameter("password-confirm");
        if (!StringUtils.hasText(confirmPassword) || !confirmPassword.equals(member.getPassword())) {
            err.rejectValue("password", "member.password.mismatch", "Password dan konfirmasi tidak sama");
        }
        
        // we are creating new Member, check whether username is already exist
        if (member.getId().equals(new Integer(-1))) {
            if (billingService.isMemberExist(member.getUsername().trim())) {
                err.rejectValue("username", "member.username.exist", "Username sudah digunakan, silahkan pilih yang lain");
            }
        }
        
        SimpleTariff tariff = null;
        String tariffId = req.getParameter("tariff_id");
        log.debug("tariff_id: "+tariffId);
        if (!StringUtils.hasText(tariffId)) {
            err.rejectValue("tariff", "member.tariff.required", "Setiap member harus diberikan jenis tarif");
        }
        
        Integer intId = null;
        try {
            intId = new Integer(tariffId);
        } catch (Exception ex) {
            err.rejectValue("tariff", "member.tariff.required", "Invalid tariff id");
        }
        
        tariff = billingService.getTariffById(intId);
        log.debug("tarif yang dipilih : "+tariff);
        if (tariff == null) {                
            err.rejectValue("tariff", "member.tariff.required", "Tarif yang dipilih tidak terdaftar");
        }
        
        
        member.setTariff(tariff);
    }
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView onSubmit(HttpServletRequest req, HttpServletResponse res, Object obj, BindException err) throws Exception {
        Member member = (Member) obj;
        member.setUsername(member.getUsername().trim());
        
        try {
            String delete = req.getParameter("delete");         
           
            if (StringUtils.hasText(delete)) {
                log.debug("Deleting object Member: "+member.toString());
                billingService.deleteMember(member);
            } else {
	            
	            if (member.getId().equals(new Integer(-1))) {  // new object, invoke create
	                log.debug("Creating object Member: "+member.toString());
	                log.debug("Tariff reference: "+member.getTariff().getId());
	                billingService.createMember(member);
	            } else { // existing object, invoke update
	                log.debug("Updating object Member: "+member.toString());
	                billingService.updateMember(member);
	            }
            }
            res.sendRedirect(nextPage);
            return null;
        } catch (Exception ex) {
            log.debug(ex);
            res.sendRedirect(nextPage+"?err=Cannot create object");
            return null;
        }        
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

