/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.artivisi.billing.connection.ConnectionManager;
import com.artivisi.billing.dao.MemberDao;
import com.artivisi.billing.dao.SessionDao;
import com.artivisi.billing.dao.SimpleTariffDao;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DefaultBillingService implements BillingService {
    private static final Logger log = Logger.getLogger(DefaultBillingService.class);
    private MemberDao memberDao;
    private long sessionExpirationLimit;
    private ConnectionManager connectionManager;
    private SessionDao sessionDao;
    private SimpleTariffDao tariffDao;

    /**
     * @param tariffDao The tariffDao to set.
     */
    public void setTariffDao(SimpleTariffDao tariffDao) {
        this.tariffDao = tariffDao;
    }
    /**
     * @param sessionDao The sessionDao to set.
     */
    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }
    /**
     * @param connectionManager The connectionManager to set.
     */
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    /**
     * @param memberDao The memberDao to set.
     */
    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#create(com.artivisi.billing.Member)
     */
    public void createMember(Member member) {        
        memberDao.create(member);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#delete(com.artivisi.billing.Member)
     */
    public void deleteMember(Member member) {
        memberDao.delete(member);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#update(com.artivisi.billing.Member)
     */
    public void updateMember(Member member) {
        memberDao.update(member);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#search(com.artivisi.billing.Member)
     */
    public List searchMember(Member member) {
        return memberDao.search(member);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getAllMember()
     */
    public List getAllMember() {
        return memberDao.getAll();
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getMemberById(java.lang.Integer)
     */
    public Member getMemberById(Integer id) {
        return memberDao.getById(id);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getMemberByUsernameAndPassword(java.lang.String, java.lang.String)
     */
    public Member getMemberByUsernameAndPassword(String username,
            String password) {
        return memberDao.getByUsernameAndPassword(username, password);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#setSessionExpirationLimit(long)
     */
    public void setSessionExpirationLimit(long second) {       
        this.sessionExpirationLimit = second;
    }
    
    private void charge(Session sess) throws InsufficientCreditException {
    	Member member = sess.getMember();
    	SimpleTariff tariff = member.getTariff();
        tariff.charge(sess);
        sessionDao.update(sess);
        memberDao.update(member);
    }
    
    private boolean hasSufficientCredit(Session sess, BigDecimal charge) {
    	Member member = sess.getMember();
    	if (member.getType().equals(MemberType.POSTPAID)) {
    		return true;
    	}
    	
    	BigDecimal currentCredit = member.getCredit();
    	if (currentCredit.compareTo(charge) > 0) {
    		return true;
    	}
    	
    	return false;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#connect(com.artivisi.billing.Member, java.lang.String)
     */
    public Session connect(Member m, String ipAddress) throws AlreadyConnectedException, InsufficientCreditException {
        // check member status, only active member allowed
        if (!m.getStatus().equals(MemberStatus.ACTIVE)) {
            log.warn("Connect attempt from non-active member "+m.getUsername()+" from "+ipAddress);            
            throw new IllegalStateException("Inactive member try to connect");
        }
        
        if (m.isExpired()) {
            log.warn("Connect attempt from expired member "+m.getUsername()+" from "+ipAddress);            
            throw new MemberExpiredException("Expired member try to connect");
        }
        
        // check if member has unclosed session, if any, throw exception
        List openSessionsForMember = getOpenSession(m);
        if (openSessionsForMember.size()>0) {
            Session s = (Session) openSessionsForMember.get(0);
            log.warn("Duplicate connection : "+m.getUsername()+" is already has open session at "+s.getIpAddress());
            throw new MemberAlreadyConnectedException("Member "+m.getUsername()+" is already has open session at "+s.getIpAddress());
        }
        
        // check if ip address has open connection, if any, throw exception
        Session openSessionsForHost = getOpenSession(ipAddress);
        if (openSessionsForHost!=null) {            
            log.warn("Duplicate connection : "+openSessionsForHost.getMember().getUsername()+" is already has open session at "+openSessionsForHost.getIpAddress());
            throw new WorkstationAlreadyConnectedException("Member "+openSessionsForHost.getMember().getUsername()+" is already has open session at "+openSessionsForHost.getIpAddress());
        }
        
        Session session = m.openSession();
        m.getTariff().charge(session);
        session.setIpAddress(ipAddress);
        
        if (sessionExpirationLimit > 0) {
            session.setExpirationLimit(sessionExpirationLimit);
        }
        
        // if Member.sessionExpirationLimit is set, override default setting
        if (m.getSessionExpirationLimit() > 0) {        
            session.setExpirationLimit(m.getSessionExpirationLimit());
        }
        
        sessionDao.create(session);        
        memberDao.update(m);
        
        connectionManager.open(ipAddress);
        return session;
    }
    
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#refreshSession(com.artivisi.billing.Session)
     */
    public void refreshSession(Session sess) throws InsufficientCreditException{
        if (sess == null) return; 
        if (sess.getMember().isExpired()) {
            log.warn("Membership is expired : "+sess.getMember().getUsername());   
            log.info("Closing session for expired member");
            charge(sess);
        	disconnect(sess);
            throw new MemberExpiredException("Expired member try to connect");
        }
        sess.refresh();
        try {
        	charge(sess);
        } catch (InsufficientCreditException ex) {
        	log.info("Closing session with insufficient credit, reset member credit to 0");
        	sess.getMember().setCredit(new BigDecimal(0));
        	memberDao.update(sess.getMember());
        	disconnect(sess);
        	throw new InsufficientCreditException("Closing session with insufficient credit, reset member credit to 0");
        }
    }
    
    public void cleanupSessions(){
        Iterator sessions = getOpenSessions().iterator();
        log.debug("There are "+getOpenSessions().size()+" open sessions");
        while(sessions.hasNext()) {
            Session sess = (Session) sessions.next();  
            log.debug("Checking: "+sess.getMember().getUsername()+" on "+sess.getIpAddress());
            log.debug("Last Visit: "+sess.getLastVisit()+", expire on "+sess.getExpirationLimit());
            
            try {
            	charge(sess);
            } catch (InsufficientCreditException ex) {
            	log.info("Closing session with insufficient credit, reset member credit to 0");
            	sess.getMember().setCredit(new BigDecimal(0));
            	memberDao.update(sess.getMember());
            	disconnect(sess);
            }
            
            if (sess.isExpired()) {
                log.debug("Session expired: "+sess.getMember().getUsername()+" on "+sess.getIpAddress());
                disconnect(sess);
            }
            
            if (sess.getMember().isExpired()) {
                log.warn("Membership is expired : "+sess.getMember().getUsername());   
                log.info("Closing session for expired member");
            	disconnect(sess);
                throw new MemberExpiredException("Expired member try to connect");
            }
        }
    }
    
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#disconnect(com.artivisi.billing.Session)
     */
    public void disconnect(Session sess) {
        if (sess == null) return;        
        sess.stop();
        sessionDao.update(sess);
        connectionManager.close(sess.getIpAddress());
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getOpenSession(java.lang.String)
     */
    public Session getOpenSession(String ipAddress) {
    	log.debug("IP Address : "+ipAddress);
        if (!StringUtils.hasText(ipAddress)) return null;
        return sessionDao.getByStatusForIpAddress(SessionStatus.OPEN, ipAddress);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getOpenSession()
     */
    public List getOpenSessions() {
        return sessionDao.getByStatus(SessionStatus.OPEN);
    }
    
    

    

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#update(com.artivisi.billing.Session)
     */
    public void updateSession(Session sess) {
        if (sess == null) return;
        sessionDao.update(sess);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getSessions(com.artivisi.billing.Member, java.util.Date, java.util.Date)
     */
    public List getSessions(Member m, Date start, Date end) {
        return sessionDao.getByIntervalForMember(start, end, m);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getBillingForDate(java.util.Date, java.util.Date)
     */
    public List getSessionsForDate(Date start, Date end) {
        return sessionDao.getByInterval(start, end);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getUnpaidSessions(com.artivisi.billing.Member)
     */
    public List getUnpaidSessions(Member m) {
        return sessionDao.getByStatusForMember(SessionStatus.CLOSED, m);
    }
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getOpenSession(com.artivisi.billing.Member)
     */
    public List getOpenSession(Member member) {
        return sessionDao.getByStatusForMember(SessionStatus.OPEN, member);
    }
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#createTariff(com.artivisi.billing.SimpleTariff)
     */
    public Integer createTariff(SimpleTariff tariff) {
        if (tariff == null) return null;
        if (!tariff.getId().equals(new Integer(-1))) return null;
        
        return tariffDao.create(tariff);
    }
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#updateTariff(com.artivisi.billing.SimpleTariff)
     */
    public void updateTariff(SimpleTariff tariff) {
        tariffDao.update(tariff);
    }
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#deleteTariff(com.artivisi.billing.SimpleTariff)
     */
    public void deleteTariff(SimpleTariff tariff) {
        tariffDao.delete(tariff);
    }
    
    public SimpleTariff getTariffById(Integer id) {
        return tariffDao.getById(id);
    }
    
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getAllTariff()
     */
    public List getAllTariff() {        
        return tariffDao.getAll();
    }
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#isMemberExist(java.lang.String)
     */
    public boolean isMemberExist(String username) {        
        return memberDao.isUsernameExist(username);
    }
    /* (non-Javadoc)
     * @see com.artivisi.billing.BillingService#getSessionById(java.lang.Integer)
     */
    public Session getSessionById(Integer id) {        
        return sessionDao.getById(id);
    }

}
