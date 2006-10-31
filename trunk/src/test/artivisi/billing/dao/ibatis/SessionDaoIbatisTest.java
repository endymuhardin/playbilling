/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.billing.dao.ibatis;

import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


import com.artivisi.billing.Member;
import com.artivisi.billing.MemberStatus;
import com.artivisi.billing.Session;
import com.artivisi.billing.SessionStatus;
import com.artivisi.billing.dao.MemberDao;
import com.artivisi.billing.dao.SessionDao;
import com.artivisi.billing.dao.ibatis.MemberDatabaseInit;
import com.artivisi.billing.dao.ibatis.SessionDatabaseInit;
import com.artivisi.billing.dao.ibatis.SimpleTariffDatabaseInit;
import com.artivisi.common.dao.Sequencer;
import com.artivisi.common.dao.ibatis.SequencerDatabaseInit;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SessionDaoIbatisTest extends TestCase {
    private ApplicationContext ctx;
    private DataSource dataSource;
    private Sequencer sequencer;
    private SequencerDatabaseInit seqInit;
    
    private SessionDao dao;
    private MemberDao memberDao;
    private SessionDatabaseInit dbInit;
    private MemberDatabaseInit memberInit;
    private SimpleTariffDatabaseInit tariffInit;
    
    public SessionDaoIbatisTest() {
        String[] configLocation = {"conf/ctx-datasource.xml", "conf/ctx-billing.xml"};
        ctx = new FileSystemXmlApplicationContext(configLocation);
        dataSource = (DataSource) ctx.getBean("dataSource");
        dao = (SessionDao) ctx.getBean("sessionDao");
        dbInit = (SessionDatabaseInit) ctx.getBean("sessionDatabaseCreator");        
        seqInit = (SequencerDatabaseInit) ctx.getBean("sequencerDatabaseCreator");
        memberInit = (MemberDatabaseInit) ctx.getBean("memberDatabaseCreator");
        tariffInit =  (SimpleTariffDatabaseInit) ctx.getBean("simpleTariffDatabaseCreator");
        memberDao = (MemberDao) ctx.getBean("memberDao");
    }
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        seqInit.createDatabase();
        tariffInit.createDatabase();
        memberInit.createDatabase();
        dbInit.createDatabase();
        
        // init member
        Member sample = new Member();
               
        sample.setUsername("khalisa");
        sample.setFullname("Khalisa Alayya");
        sample.setEmail("khalisa@yahoo.com");
        sample.setPassword("abcd");
        
        
        sample.setAutoconnect(true);
        sample.setCredit(new BigDecimal(123));
        sample.setStatus(MemberStatus.INACTIVE);
        memberDao.create(sample);
        
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        seqInit.dropDatabase();
        dbInit.dropDatabase();
        memberInit.dropDatabase();
        tariffInit.dropDatabase();
    }
    
    private Session getSample() {
        Session sess = new Session();
        sess.setIpAddress("192.168.0.1");
        sess.setMember(memberDao.getById(new Integer(0)));
        sess.start();
        sess.refresh();
        sess.stop();
        return sess;        
    }
    
    private Session getSample2() {
        Session sess = new Session();
        sess.setIpAddress("192.168.0.2");
        sess.setMember(memberDao.getById(new Integer(0)));
        sess.start();
        sess.refresh();
        sess.stop();
        return sess;        
    }
    
    private void compareAllProperties(Session sess1, Session sess2) {        
        assertEquals(sess1.getIpAddress(), sess2.getIpAddress());
        assertEquals(sess1.getCharge(), sess2.getCharge());
        assertEquals(sess1.getEndTime(), sess2.getEndTime());
        assertEquals(sess1.getExpirationLimit(), sess2.getExpirationLimit());
        assertEquals(sess1.getLastVisit(), sess2.getLastVisit());
        assertEquals(sess1.getMember().getId(), sess2.getMember().getId());
        assertEquals(sess1.getStartTime(), sess2.getStartTime());
        assertEquals(sess1.getStatus(), sess2.getStatus());
    }

    public void testCreateAndGetById() {
        Session sample = getSample();
        dao.create(sample);
        
        Session samplex = dao.getById(new Integer(0));
        assertNotNull(samplex);        
        compareAllProperties(sample, samplex);
        
        assertNull(dao.getById(new Integer(9999)));
    }
    
    public void testUpdate() {
        Session sample = getSample();
        dao.create(sample);        
        Session samplex = dao.getById(new Integer(0));
        
        Session sample2 = getSample2();
        sample2.setId(new Integer(0));
        dao.update(sample2);
        
        compareAllProperties(sample2, dao.getById(new Integer(0)));
    }

    public void testDelete() {
        Session sample = getSample();
        dao.create(sample);        
        Session samplex = dao.getById(new Integer(0));
        assertNotNull(samplex);
        dao.delete(samplex);
        
        Session samplez = dao.getById(new Integer(0));
        assertNull(samplez);
    }
    
    public void testGetAll() {   
        Session sample1 = getSample();
        Session sample2 = getSample2();
        dao.create(sample1);        
        dao.create(sample2);
        
        List all = dao.getAll();
        assertEquals(2, all.size());
        
        Session s1 = (Session) all.get(0);
        Session s2 = (Session) all.get(1);
        
        compareAllProperties(sample1, s1);
        compareAllProperties(sample2, s2);
    }
    
    public void testGetByStatus(){
        Session sample = getSample();
        dao.create(sample);
        Session sample2 = getSample2();
        dao.create(sample2);        
        
        assertEquals(2, dao.getByStatus(sample.getStatus()).size());
        
        sample.setStatus(SessionStatus.OPEN);
        dao.update(sample);
        sample2.setStatus(SessionStatus.OPEN);
        dao.update(sample2);
        
        assertEquals(0, dao.getByStatus(SessionStatus.CLOSED).size());
        assertEquals(2, dao.getByStatus(SessionStatus.OPEN).size());
        
    }
    
    public void testGetByStatusForIpAddress(){
        Session sample = getSample();
        dao.create(sample);        
        
        Session samplez = dao.getByStatusForIpAddress(SessionStatus.CLOSED, "192.168.0.1");
        assertNotNull(samplez);
        
        samplez.setStatus(SessionStatus.OPEN);
        dao.update(samplez);
        
        Session samplezz = dao.getByStatusForIpAddress(SessionStatus.OPEN, "192.168.0.1");
        assertNotNull(samplezz);
        
        Session samplec = dao.getByStatusForIpAddress(SessionStatus.CLOSED, "192.168.0.13");
        assertNull(samplec);
    }
    
    public void testGetByStatusForMember(){
        Session sample = getSample();
        dao.create(sample);        
        
        List samplez = dao.getByStatusForMember(SessionStatus.CLOSED, sample.getMember());
        assertTrue(samplez.size() == 1);
        Session xx = (Session) samplez.get(0);
        compareAllProperties(sample, xx);
        
        List samplec = dao.getByStatusForMember(SessionStatus.OPEN, sample.getMember());
        assertTrue(samplec.size() == 0);
    }

    

}
