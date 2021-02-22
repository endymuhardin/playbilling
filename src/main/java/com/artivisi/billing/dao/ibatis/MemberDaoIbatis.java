/*
 * Created on Jun 7, 2005
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
import com.artivisi.billing.MemberStatus;
import com.artivisi.billing.MemberType;
import com.artivisi.billing.SimpleTariff;
import com.artivisi.billing.dao.MemberDao;
import com.artivisi.billing.dao.SimpleTariffDao;
import com.artivisi.common.dao.Sequencer;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MemberDaoIbatis extends SqlMapClientDaoSupport implements MemberDao {
    private static final Logger log = Logger.getLogger(MemberDaoIbatis.class);
    private Sequencer sequencer;
    private SimpleTariffDao simpleTariffDao;
    
    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.MemberDao#create(com.artivisi.billing.Member)
     */
    public Integer create(Member m) {
        if (m == null) return null;
        Integer newId = sequencer.getNext(getClass().getName());
        m.setId(newId);
        getSqlMapClientTemplate().update("insertMember", m);        
        return newId;
    }
    
    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.MemberDao#delete(com.artivisi.billing.Member)
     */
    public void delete(Member m) {
        if (m == null) return ;
        getSqlMapClientTemplate().delete("deleteMember", m);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.MemberDao#getById(java.lang.Integer)
     */
    public Member getById(Integer id) {
        if (id == null) return null;
        Map m = (Map) getSqlMapClientTemplate().queryForObject("getMemberById", id);
        
        if (m == null) return null;
        
        return toMember(m);
    }
    
    private Member toMember(Map m){
        if (m == null) return null;
        Member member = new Member();
        
        member.setId((Integer) m.get("ID"));
        member.setUsername((String) m.get("USERNAME"));
        member.setFullname((String) m.get("FULLNAME"));
        member.setPassword((String) m.get("PASSWORD"));
        member.setEmail((String) m.get("EMAIL"));
        member.setAddress((String) m.get("ADDRESS"));
        member.setDateRegistered((Date) m.get("REGISTER_DATE"));
        member.setDateExpired((Date) m.get("EXPIRE_DATE"));
        member.setAutoconnect(((Boolean) m.get("AUTOCONNECT")).booleanValue());
        member.setSessionExpirationLimit(((Long)m.get("SESSION_EXPIRATION_LIMIT")).longValue());
        BigDecimal credit = (BigDecimal) m.get("CREDIT");
        
        if (credit == null) {
            member.setCredit(new BigDecimal(0));
        } else {
            member.setCredit(credit);
        }        
        
        member.setStatus(MemberStatus.getInstance((String) m.get("MEMBER_STATUS")));
        member.setType(MemberType.getInstance((String) m.get("MEMBER_TYPE")));
        
        SimpleTariff tariff = simpleTariffDao.getById((Integer)m.get("TARIFF_ID")); 
        log.debug("tariff: "+tariff);
        member.setTariff(tariff);
        return member;
    }

    

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.MemberDao#getAll()
     */
    public List getAll() {
        List result = new ArrayList();
        
        Iterator allMember = getSqlMapClientTemplate().queryForList("getAllMember", null).iterator();        
        while(allMember.hasNext()) {
            Map map = (Map) allMember.next();
            result.add(toMember(map));
        }
        return result;
    }
    /**
     * @param sequencer The sequencer to set.
     */
    public void setSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }
    

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.MemberDao#update(com.artivisi.billing.Member)
     */
    public void update(Member m) {
        if (m == null) return ;
        getSqlMapClientTemplate().update("updateMember", m);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.MemberDao#search(com.artivisi.billing.Member)
     */
    public List search(Member m) {
        Iterator sqlMapResult =  getSqlMapClientTemplate().queryForList("searchMemberByExample", m).iterator();
        List convertResult = new ArrayList();
        
        while(sqlMapResult.hasNext()) {
            Map result = (Map) sqlMapResult.next();
            convertResult.add(toMember(result));
        }
        
        return convertResult;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.MemberDao#getByUsernameAndPassword(java.lang.String, java.lang.String)
     */
    public Member getByUsernameAndPassword(String username, String password) {
        Map params = new HashMap();
        params.put("username", username);
        params.put("password", password);
        return toMember((Map) getSqlMapClientTemplate().queryForObject("getMemberByUsernameAndPassword", params));         
    }
    
    /**
     * @param simpleTariffDao The simpleTariffDao to set.
     */
    public void setSimpleTariffDao(SimpleTariffDao simpleTariffDao) {
        this.simpleTariffDao = simpleTariffDao;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.MemberDao#isUsernameExist(java.lang.String)
     */
    public boolean isUsernameExist(String username) {
        Map result = (Map) getSqlMapClientTemplate().queryForObject("getMemberByUser", username);
        return result != null ? true : false; 
    }
}
