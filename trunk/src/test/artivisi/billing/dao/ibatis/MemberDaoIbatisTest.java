/*
 * Created on Jun 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.billing.dao.ibatis;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.artivisi.billing.Member;
import com.artivisi.billing.MemberStatus;
import com.artivisi.billing.MemberType;
import com.artivisi.billing.SimpleTariff;
import com.artivisi.billing.TimeUnit;
import com.artivisi.billing.dao.MemberDao;
import com.artivisi.billing.dao.SimpleTariffDao;
import com.artivisi.billing.dao.ibatis.MemberDatabaseInit;
import com.artivisi.billing.dao.ibatis.SimpleTariffDatabaseInit;
import com.artivisi.common.dao.Sequencer;
import com.artivisi.common.dao.ibatis.SequencerDatabaseInit;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MemberDaoIbatisTest extends TestCase {
    private ApplicationContext ctx;
    private DataSource dataSource;
    private Sequencer sequencer;
    private SequencerDatabaseInit seqInit;
    
    private MemberDao dao;
    private MemberDatabaseInit dbInit;
    
    private SimpleTariffDao tariffDao;
    private SimpleTariffDatabaseInit tariffInit;
    
    public MemberDaoIbatisTest() {
        String[] configLocation = {"conf/ctx-datasource.xml", "conf/ctx-billing.xml"};
        ctx = new FileSystemXmlApplicationContext(configLocation);
        dataSource = (DataSource) ctx.getBean("dataSource");
        dao = (MemberDao) ctx.getBean("memberDao");
        dbInit = (MemberDatabaseInit) ctx.getBean("memberDatabaseCreator");
        seqInit = (SequencerDatabaseInit) ctx.getBean("sequencerDatabaseCreator");
        tariffDao = (SimpleTariffDao) ctx.getBean("simpleTariffDao");
        tariffInit = (SimpleTariffDatabaseInit) ctx.getBean("simpleTariffDatabaseCreator");
    }
    
    private Member getSampleMember() {
        Member sample = new Member();
               
        sample.setUsername("khalisa");
        sample.setFullname("Khalisa Alayya");
        sample.setEmail("khalisa@yahoo.com");
        sample.setPassword("abcd");
        sample.setAddress("cililitan");
        sample.setSessionExpirationLimit(100);
        sample.setAutoconnect(true);
        sample.setCredit(new BigDecimal(123));
        sample.setStatus(MemberStatus.INACTIVE);
        sample.setType(MemberType.POSTPAID);
        sample.setDateExpired(new DateTime(2006,1,1,0,0,0,0).toDate());
        return sample;
    }
    
    private SimpleTariff getSampleTariff() {
        SimpleTariff simple = new SimpleTariff();
        simple.setTimeUnit(TimeUnit.MINUTE); // time unit is MINUTE
        simple.setGracePeriod(30); // grace period 30 minute
        simple.setGracePeriodCharge(new BigDecimal(5000)); // charge 5000 for first 15 minute        
        
        simple.setChargeUnit(15); // charge every 15 minute
        simple.setRate(new BigDecimal(2000));
        return simple;
    }
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        seqInit.createDatabase();
        tariffInit.createDatabase();	
        dbInit.createDatabase();
        	
		tariffDao.create(getSampleTariff());
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        dbInit.dropDatabase();
        seqInit.dropDatabase();
        tariffInit.dropDatabase();
    }

    public void testCreateAndGetById() throws Exception {
        Member member = getSampleMember();
        dao.create(member);
        
        String sqlTestMember = "SELECT * FROM billing_member";
        ResultSet rsMember = dataSource.getConnection().createStatement().executeQuery(sqlTestMember);
                
        Member mx = dao.getById(new Integer(0));
        assertNotNull(mx);
        assertTrue(mx.isAutoconnect());
        assertEquals(new BigDecimal(123), mx.getCredit());
        assertEquals(MemberStatus.INACTIVE, mx.getStatus());
        assertEquals(MemberType.POSTPAID, mx.getType());
        assertNull(mx.getTariff());
        
        assertEquals("khalisa", mx.getUsername());
        assertEquals("Khalisa Alayya", mx.getFullname());
        assertEquals("khalisa@yahoo.com", mx.getEmail());
        assertEquals("abcd", mx.getPassword());
        assertEquals("cililitan", mx.getAddress());
        assertEquals(100, mx.getSessionExpirationLimit());
        assertEquals(new DateTime(2006,1,1,0,0,0,0).toDate(), mx.getDateExpired());
    }
    
    public void testGetByUsername(){
        Member member = getSampleMember();
        dao.create(member);
        
        boolean isExist = dao.isUsernameExist("khalisa");
        assertTrue(isExist);
        
        isExist = dao.isUsernameExist("endy");
        assertFalse(isExist);
        
    }
    
    public void testUpdate() {
        Member member = getSampleMember();
        dao.create(member);
        Member mx = dao.getById(new Integer(0));
        assertNotNull(mx);
        
        mx.setAutoconnect(false);
        mx.setStatus(MemberStatus.LOCKED);
        mx.setType(MemberType.PREPAID);
        mx.setCredit(new BigDecimal(456));
        mx.setTariff(tariffDao.getById(new Integer(0)));
        
        mx.setEmail("khalisa@gmail.com");
        mx.setPassword("asdf");
        mx.setFullname("Khalisa");
        mx.setUsername("khal");
        mx.setSessionExpirationLimit(101);
        mx.setDateExpired(new DateTime(2006,12,31,0,0,0,0).toDate());
        
        dao.update(mx);
        
        Member m = dao.getById(new Integer(0));
        assertNotNull(m);
        assertFalse(m.isAutoconnect());
        assertEquals(new BigDecimal(456), m.getCredit());
        assertEquals(MemberStatus.LOCKED, m.getStatus());
        assertEquals(MemberType.PREPAID, m.getType());
        assertNotNull(m.getTariff());
        
        assertEquals("khalisa", m.getUsername());
        assertEquals("Khalisa", m.getFullname());
        assertEquals("khalisa@gmail.com", m.getEmail());
        assertEquals("asdf", m.getPassword());
        assertEquals(101, mx.getSessionExpirationLimit());
        assertEquals(new DateTime(2006,12,31,0,0,0,0).toDate(), mx.getDateExpired());
    }

    public void testDelete() {
        Member member = getSampleMember();
        dao.create(member);
        Member mx = dao.getById(new Integer(0));
        assertNotNull(mx);
        
        dao.delete(mx);
        
        Member my = dao.getById(new Integer(0));
        assertNull(my);
       
    }

    public void testGetAll() {
        Member member = getSampleMember();
        dao.create(member);
        member.setUsername("endy");
        dao.create(member);
        
        List result = dao.getAll();
        assertEquals(2, result.size());
        
        Member mx = (Member) result.get(0);
        assertNotNull(mx);
        assertTrue(mx.isAutoconnect());
        assertEquals(new BigDecimal(123), mx.getCredit());
        assertEquals(MemberStatus.INACTIVE, mx.getStatus());
        assertEquals(MemberType.POSTPAID, mx.getType());
        assertNull(mx.getTariff());
        
        assertEquals("khalisa", mx.getUsername());
        assertEquals("Khalisa Alayya", mx.getFullname());
        assertEquals("khalisa@yahoo.com", mx.getEmail());
        assertEquals("abcd", mx.getPassword());
        assertEquals("cililitan", mx.getAddress());
        assertEquals(100, mx.getSessionExpirationLimit());
        assertEquals(new DateTime(2006,1,1,0,0,0,0).toDate(), mx.getDateExpired());
    }
    
    public void testSearch() {
        Member member = getSampleMember();
        dao.create(member);
        
        Member sample = new Member();
        sample.setUsername("%khal%");
        
        List result = dao.search(sample);
        assertEquals(1, result.size());
        Member mx = (Member) result.iterator().next();
        assertNotNull(mx);
        assertTrue(mx.isAutoconnect());
        assertEquals(new BigDecimal(123), mx.getCredit());
        assertEquals(MemberStatus.INACTIVE, mx.getStatus());
        
        sample = new Member();
        sample.setFullname("%hali%");
        
        result = dao.search(sample);
        assertEquals(1, result.size());
        
        sample = new Member();
        sample.setEmail("%yahoo%");
        
        result = dao.search(sample);
        assertEquals(1, result.size());
                
        assertEquals("khalisa", mx.getUsername());
        assertEquals("Khalisa Alayya", mx.getFullname());
        assertEquals("khalisa@yahoo.com", mx.getEmail());
        assertEquals("abcd", mx.getPassword());   
        assertEquals(100, mx.getSessionExpirationLimit());  
        assertEquals(new DateTime(2006,1,1,0,0,0,0).toDate(), mx.getDateExpired());
    }
    
    public void testGetByUsernameAndPassword(){
        Member member = getSampleMember();
        dao.create(member);
        
        String username = "tester";
        String password = "qwer";
        
        Member mustNull = dao.getByUsernameAndPassword(username, password);
        assertNull(mustNull);
        
        Member mx = dao.getByUsernameAndPassword("khalisa", "abcd");
        assertNotNull(mx);
        assertTrue(mx.isAutoconnect());
        assertEquals(new BigDecimal(123), mx.getCredit());
        assertEquals(MemberStatus.INACTIVE, mx.getStatus());
                
        assertEquals("khalisa", mx.getUsername());
        assertEquals("Khalisa Alayya", mx.getFullname());
        assertEquals("khalisa@yahoo.com", mx.getEmail());
        assertEquals("abcd", mx.getPassword());
        assertEquals(100, mx.getSessionExpirationLimit());
        assertEquals(new DateTime(2006,1,1,0,0,0,0).toDate(), mx.getDateExpired());
    }
}
