/*
 * Created on Jun 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.dao;

import java.util.Date;
import java.util.List;

import com.artivisi.billing.Member;
import com.artivisi.billing.Session;
import com.artivisi.billing.SessionStatus;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface SessionDao {
    public Integer create(Session sess);
    public void update(Session sess);
    public void delete(Session sess);
    public Session getById(Integer id);
    public List getAll();
    public List getByInterval(Date start, Date end);
    public List getByIntervalForMember(Date start, Date end, Member member);
    public List getByStatus(SessionStatus status);
    public List getByStatusForMember(SessionStatus status, Member member);
    public Session getByStatusForIpAddress(SessionStatus status, String ipAddress);
}
