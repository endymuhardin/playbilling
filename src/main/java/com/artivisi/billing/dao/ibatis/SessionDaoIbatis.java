/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.dao.ibatis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.artivisi.billing.Member;
import com.artivisi.billing.Session;
import com.artivisi.billing.SessionStatus;
import com.artivisi.billing.dao.MemberDao;
import com.artivisi.billing.dao.SessionDao;
import com.artivisi.common.dao.Sequencer;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SessionDaoIbatis extends SqlMapClientDaoSupport implements SessionDao {
	private static final Logger log = Logger.getLogger(SessionDaoIbatis.class);
    private MemberDao memberDao;
    private Sequencer sequencer;
    
    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#create(com.artivisi.billing.Session)
     */
    public Integer create(Session sess) {
        if (sess == null) return null;
        Integer newId = sequencer.getNext(getClass().getName());
        sess.setId(newId);
        getSqlMapClientTemplate().update("insertSession", sess);
        return newId;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#update(com.artivisi.billing.Session)
     */
    public void update(Session sess) {
        getSqlMapClientTemplate().update("updateSession", sess);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#delete(com.artivisi.billing.Session)
     */
    public void delete(Session sess) {
        getSqlMapClientTemplate().delete("deleteSession", sess);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#getById(java.lang.Integer)
     */
    public Session getById(Integer id) {
        if (id == null) return null;
        return toSession((Map) getSqlMapClientTemplate().queryForObject("getSessionById", id));        
    }
    
    private Session toSession(Map m) {
        if (m == null) return null;
        
        Session sess = new Session();
        
        sess.setId((Integer) m.get("ID"));
        sess.setCharge((BigDecimal) m.get("CHARGE"));
        sess.setEndTime((Date) m.get("END_TIME"));
        sess.setExpirationLimit(((Integer)m.get("EXPIRATION_LIMIT")).longValue());
        sess.setIpAddress((String) m.get("IP_ADDRESS"));
        sess.setLastVisit((Date) m.get("LAST_VISIT"));
        Member mem = memberDao.getById((Integer) m.get("MEMBER_ID"));
        sess.setMember(mem);
        sess.setStartTime((Date) m.get("START_TIME"));
        sess.setStatus(SessionStatus.getInstance((String) m.get("SESSION_STATUS")));
        
        return sess;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#getAll()
     */
    public List getAll() {
        Iterator result = getSqlMapClientTemplate().queryForList("getAllSession", null).iterator();
        List sessions = new ArrayList();
        while(result.hasNext()) {
            sessions.add(toSession((Map) result.next()));
        }
        return sessions;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#getByInterval(java.util.Date, java.util.Date)
     */
    public List getByInterval(Date start, Date end) {
        Map params = new HashMap();
        params.put("start", start);
        params.put("end", end);
        Iterator result = getSqlMapClientTemplate().queryForList("getSessionByInterval", params).iterator();
        List sessions = new ArrayList();
        while(result.hasNext()) {
            sessions.add(toSession((Map) result.next()));
        }
        return sessions;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#getByIntervalForMember(java.util.Date, java.util.Date, com.artivisi.billing.Member)
     */
    public List getByIntervalForMember(Date start, Date end, Member member) {
        Map params = new HashMap();
        params.put("start", start);
        params.put("end", end);
        params.put("member", member);
        Iterator result = getSqlMapClientTemplate().queryForList("getSessionByIntervalForMember", params).iterator();
        List sessions = new ArrayList();
        while(result.hasNext()) {
            sessions.add(toSession((Map) result.next()));
        }
        return sessions;
    }

    /**
     * @param memberDao The memberDao to set.
     */
    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    /**
     * @param sequencer The sequencer to set.
     */
    public void setSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#getByStatus(com.artivisi.billing.SessionStatus)
     */
    public List getByStatus(SessionStatus status) {
        Iterator result = getSqlMapClientTemplate().queryForList("getSessionByStatus", status).iterator();
        List sessions = new ArrayList();
        while(result.hasNext()) {
            sessions.add(toSession((Map) result.next()));
        }
        return sessions;
    }
    
    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#getByStatus(com.artivisi.billing.SessionStatus)
     */
    public Session getByStatusForIpAddress(SessionStatus status, String ipAddress) {
        Map params = new HashMap();
        params.put("status", status);
        params.put("ipAddress", ipAddress);
        log.debug("Status : "+status.getValue());
        log.debug("IP Address : "+ipAddress);
        Map result = (Map) getSqlMapClientTemplate().queryForObject("getSessionByStatusForIp", params);
        log.debug("Query Result"+result);
        return toSession(result);
    }
        
    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SessionDao#getByStatusForMember(com.artivisi.billing.SessionStatus, com.artivisi.billing.Member)
     */
    public List getByStatusForMember(SessionStatus status, Member member) {
        Map params = new HashMap();
        params.put("status", status);
        params.put("member", member);
        Iterator result = getSqlMapClientTemplate().queryForList("getSessionByStatusForMember", params).iterator();
        List sessions = new ArrayList();
        while(result.hasNext()) {
            sessions.add(toSession((Map) result.next()));
        }
        return sessions;
    }
}
