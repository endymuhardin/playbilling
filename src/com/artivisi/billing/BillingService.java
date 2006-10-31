/*
 * Created on Jun 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing;

import java.util.Date;
import java.util.List;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface BillingService {
    // user management
    public void createMember(Member member);
    public void deleteMember(Member member);
    public void updateMember(Member member);
    public List searchMember(Member member);
    public boolean isMemberExist(String username);
    public List getAllMember();
    public Member getMemberById(Integer id);
    public Member getMemberByUsernameAndPassword(String username, String password);    
    
    // tariff management
    public Integer createTariff(SimpleTariff tariff);
    public void updateTariff(SimpleTariff tariff);
    public void deleteTariff(SimpleTariff tariff);
    public List getAllTariff();
    
    // connection management
    
    /**
     * set default session expiration limit.
     * this setting can be overridden for each member
     * @param second session expiration limit in second
     * @see com.artivisi.billing.Member#setCredit(long sessionExpirationLimit)
     * */
    public void setSessionExpirationLimit(long second);
    
    /**
     * do session initialization activities.
     * <ol>
     * <li>issue command to allow member connection</li>
     * <li>create user session, set expiration limit</li>
     * <li>start user session, session status = open</li>
     * <li>store user session in database</li>
     * </ol>
     * @throws InsufficientCreditException
     * */
    public Session connect(Member m, String ipAddress) throws AlreadyConnectedException, InsufficientCreditException ;
    
    /**
     * retrieve current open session from this ipAddress
     * @param ipAddress connecting client
     * @return Session object associated with this ipAddress having status OPEN, null if no OPEN session found 
     * */
    public Session getOpenSession(String ipAddress);
    
    /**
     * retrieve current open session from this member
     * @param member connecting client
     * @return Session object associated with this ipAddress having status OPEN, null if no OPEN session found 
     * */
    public List getOpenSession(Member member);
    
    /**
     * retrieve all current open session
     * @return List of Session object having status OPEN, empty list if no OPEN session found 
     * */
    public List getOpenSessions();
    
    /**
     * refresh session to keep it from expire
     * @param session to be refreshed
     * @throws InsufficientCreditException
     * */
    public void refreshSession(Session sess) throws InsufficientCreditException;
    
    public void cleanupSessions();
    
    /**
     * do session cleanup activities. 
     * <ol>
     * <li>issue command to disallow member connection</li>
     * <li>stop user session, session status = closed</li>
     * </ol>
     * */
    public void disconnect(Session sess);
    
    // session status management
    
    /**
     * modify session properties, and save changes.
     * Usually used to: 
     * <ul>
     * <li>set session status : paid</li>
     * <li>set session status : cancelled</li>
     * </ul> 
     * */
    public void updateSession(Session sess);
    
    // reporting    
    public List getSessions(Member m, Date start, Date end);
    
    /**
     * retrieve usage report for all member
     * @param start 
     * @param end
     * @return map with Member object as key and Integer totalValuePerDay as value
     * */
    public List getSessionsForDate(Date start, Date end);
    
    /**
     * convenience method to generate member billing. 
     * @param m member to be processed
     * @return all session object with status == CLOSED
     * */
    public List getUnpaidSessions(Member m);
    /**
     * @param intId
     * @return
     */
    public SimpleTariff getTariffById(Integer intId);
    /**
     * @param integer
     * @return
     */
    public Session getSessionById(Integer integer);
}
