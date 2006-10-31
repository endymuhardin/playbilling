/*
 * Created on May 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Mayong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Session {
    private Integer id = new Integer(-1);
    private Member member;
    private String ipAddress;
    private Date startTime = new Date();
    private Date lastVisit = new Date();
    private Date endTime;
    private long expirationLimit = 60;
    private BigDecimal charge = new BigDecimal(0);
    private SessionStatus status = SessionStatus.OPEN;
    
    /**
     * calculate lastVisit minus startTime
     * @return lastVisit minus startTime in milliseconds
     * */
    public long getDuration() {
        if ((startTime == null) || (lastVisit == null) ) return 0;
        return lastVisit.getTime() - startTime.getTime();        
    }
    
    /**
     * calculate lastVisit minus startTime
     * @return lastVisit minus startTime in milliseconds
     * */
    public String getStringDuration() {
        return TimeUnit.secondsToString(getDuration()/1000);
    }
    
    public void start(){
        startTime = new Date();
        lastVisit = new Date();
        status = SessionStatus.OPEN;
    }
    
    public void stop(){
        endTime = new Date();
        lastVisit = endTime;
        status = SessionStatus.CLOSED;
    }
    
    public void refresh() {
        if (isExpired()) {
            endTime = new Date(lastVisit.getTime() + (expirationLimit*1000));
            status = SessionStatus.CLOSED;
            return;
        }
        lastVisit = new Date();        
    }
    
    public boolean isExpired() {
        return ((new Date().getTime() - lastVisit.getTime())/1000) > expirationLimit ;
    }
    
    /**
     * @return Returns the charge.
     */
    public BigDecimal getCharge() {
        return charge;
    }
    /**
     * @param charge The charge to set.
     */
    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }
    /**
     * @return Returns the endTime.
     */
    public Date getEndTime() {
        return endTime;
    }
    
    /**
     * @return Returns the member.
     */
    public Member getMember() {
        return member;
    }
    /**
     * @param member The member to set.
     */
    public void setMember(Member member) {
        this.member = member;
    }
    /**
     * @return Returns the startTime.
     */
    public Date getStartTime() {
        return startTime;
    }        
    /**
     * @return Returns the ipAddress.
     */
    public String getIpAddress() {
        return ipAddress;
    }
    /**
     * @param ipAddress The ipAddress to set.
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    /**
     * @param endTime The endTime to set.
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    /**
     * @param startTime The startTime to set.
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    /**
     * @return Returns the status.
     */
    public SessionStatus getStatus() {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(SessionStatus status) {
        this.status = status;
    }
    /**
     * return session expiration limit (in seconds)
     * @return Returns the expirationLimit.
     */
    public long getExpirationLimit() {
        return expirationLimit;
    }
    /**
     * set session expiration limit (in seconds)
     * @param expirationLimit The expirationLimit to set.
     */
    public void setExpirationLimit(long expirationLimit) {
        this.expirationLimit = expirationLimit;
    }
    /**
     * @return Returns the lastVisit.
     */
    public Date getLastVisit() {
        return lastVisit;
    }
    /**
     * @param lastVisit The lastVisit to set.
     */
    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }
    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
