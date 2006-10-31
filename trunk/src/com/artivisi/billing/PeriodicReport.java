/*
 * Created on Jun 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PeriodicReport {
    private Logger log = Logger.getLogger(PeriodicReport.class);
    private List entries = new ArrayList();
    private Map memberPool = new HashMap();
    
    private Date startPeriod;
    private Date endPeriod;
    
    public PeriodicReport(Date startPeriod, Date endPeriod) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }
    
    public void addSession(Session sess) {
        Member m = sess.getMember();
        Member memberInPool = (Member) memberPool.get(m.getId());
        if (memberInPool == null) {
            memberPool.put(m.getId(), m);
            entries.add(m);
            memberInPool = m;            
        }
        
        memberInPool.getSessions().add(sess);
    }
    
    public long totalDuration(){
        long seconds = 0;
        Iterator iter = entries.iterator();
        log.debug("Entries in periodic report: "+entries.size());
        
        while(iter.hasNext()) {            
            Member m = (Member) iter.next();
            log.debug("Accessing member: "+m);
            seconds += m.totalDuration();
        }
        return seconds;
    }
    
    public String totalDurationString(){
        return TimeUnit.secondsToString(totalDuration());
    }
    
    public BigDecimal totalCharge() {
        BigDecimal result = new BigDecimal(0);
        
        Iterator i = entries.iterator();
        log.debug("Entries in periodic report: "+entries.size());
        while(i.hasNext()) {
            Member m = (Member) i.next();
            result = result.add(m.totalCharge());
        }
        
        return result;
    }
    
    /**
     * @return Returns the entries.
     */
    public List getEntries() {        
        return entries;
    }
}
