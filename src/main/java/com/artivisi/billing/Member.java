/*
 * Created on May 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mayong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Member {
    private Integer id = new Integer(-1);
    private String username;
    private String fullname;
    private String password;
    private String email;
    private String address;
    private Date dateRegistered = new Date();
    private Date dateExpired = new Date();
    private List sessions = new ArrayList();
    private boolean autoconnect = false;
    private BigDecimal credit = new BigDecimal(0);
    private long sessionExpirationLimit = 0;
    private MemberStatus status = MemberStatus.INACTIVE;
    private MemberType type = MemberType.PREPAID;
    private SimpleTariff tariff;
    
    public long totalDuration(){
        Iterator i = sessions.iterator();
        long totalSeconds = 0;
        while(i.hasNext()){
            Session ses = (Session) i.next();
            totalSeconds += ses.getDuration()/1000; 
        }
        return totalSeconds;
    }
    
    public String totalDurationString(){
        return TimeUnit.secondsToString(totalDuration());
    }
    
    public BigDecimal totalCharge(){
        Iterator i = sessions.iterator();
        BigDecimal totalCharge = new BigDecimal(0);
        while(i.hasNext()){
            Session ses = (Session) i.next();
            totalCharge = totalCharge.add(ses.getCharge()); 
        }
        
        return totalCharge;
    }
    
    /**
     * @return Returns the address.
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return Returns the fullname.
     */
    public String getFullname() {
        return fullname;
    }
    /**
     * @param fullname The fullname to set.
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * @return Returns the dateRegistered.
     */
    public Date getDateRegistered() {
        return dateRegistered;
    }
    /**
     * @param dateRegistered The dateRegistered to set.
     */
    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
    /**
     * @return Returns the sessions.
     */
    public List getSessions() {
        return sessions;
    }
    /**
     * @param sessions The sessions to set.
     */
    public void setSessions(List sessions) {
        this.sessions = sessions;
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
    /**
     * @param autoconnect The autoconnect to set.
     */
    public void setAutoconnect(boolean autoconnect) {
        this.autoconnect = autoconnect;
    }
    /**
     * @param credit The credit to set.
     */
    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }
    /**
     * @return Returns the autoconnect.
     */
    public boolean isAutoconnect() {
        return autoconnect;
    }
    /**
     * @return Returns the credit.
     */
    public BigDecimal getCredit() {         
        return credit;
    }
    /**
     * @return Returns the status.
     */
    public MemberStatus getStatus() {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(MemberStatus status) {
        this.status = status;
    }
    /**
     * @return Returns the tariff.
     */
    public SimpleTariff getTariff() {
        return tariff;
    }
    /**
     * @param tariff The tariff to set.
     */
    public void setTariff(SimpleTariff tariff) {
        this.tariff = tariff;
    }
    
    public Session openSession(){
        Session sess = new Session();
        sess.setMember(this);
        sess.start();
        return sess;
    }
    /**
     * @return Returns the type.
     */
    public MemberType getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(MemberType type) {
        this.type = type;
    }
    /**
     * @return Returns the sessionExpirationLimit.
     */
    public long getSessionExpirationLimit() {
        return sessionExpirationLimit;
    }
    /**
     * @param sessionExpirationLimit The sessionExpirationLimit to set.
     */
    public void setSessionExpirationLimit(long sessionExpirationLimit) {
        this.sessionExpirationLimit = sessionExpirationLimit;
    }
    /**
     * @return Returns the dateExpired.
     */
    public Date getDateExpired() {
        return dateExpired;
    }
    /**
     * @param dateExpired The dateExpired to set.
     */
    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
    }
    
    public boolean isExpired() {
        return new Date().getTime() > dateExpired.getTime();
    }
}
