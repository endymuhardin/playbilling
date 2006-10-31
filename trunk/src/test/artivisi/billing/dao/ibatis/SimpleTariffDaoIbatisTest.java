/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.billing.dao.ibatis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


import com.artivisi.billing.SimpleTariff;
import com.artivisi.billing.TimeUnit;
import com.artivisi.billing.dao.SimpleTariffDao;
import com.artivisi.billing.dao.ibatis.SimpleTariffDatabaseInit;
import com.artivisi.common.dao.Sequencer;
import com.artivisi.common.dao.ibatis.SequencerDatabaseInit;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleTariffDaoIbatisTest extends TestCase {
    private ApplicationContext ctx;
    private DataSource dataSource;
    private Sequencer sequencer;
    private SequencerDatabaseInit seqInit;
    
    private SimpleTariffDao dao;
    private SimpleTariffDatabaseInit dbInit;
    
    private List getSampleData (){
        SimpleTariff simple = new SimpleTariff();
        simple.setTimeUnit(TimeUnit.MINUTE); // time unit is MINUTE
        simple.setGracePeriod(30); // grace period 30 minute
        simple.setGracePeriodCharge(new BigDecimal(5000)); // charge 5000 for first 15 minute        
        
        simple.setChargeUnit(15); // charge every 15 minute
        simple.setRate(new BigDecimal(2000));
        
        SimpleTariff simple1 = new SimpleTariff();
        simple1.setTimeUnit(TimeUnit.SECOND); // time unit is SECOND
        simple1.setGracePeriod(3600); // grace period 3600 second
        simple1.setGracePeriodCharge(new BigDecimal(6000)); // charge 5000 for first hour        
        
        simple1.setChargeUnit(30); // charge every 30 second
        simple1.setRate(new BigDecimal(100)); // 100 per 30 second
        
        SimpleTariff simple2 = new SimpleTariff();
        simple2.setTimeUnit(TimeUnit.MINUTE); // time unit is MINUTE
        // no grace period
        // no charge for grace period        
        
        simple2.setChargeUnit(1); // charge every 1 minute
        simple2.setRate(new BigDecimal(100)); // 100 per minute
        
        List samples = new ArrayList();
        samples.add(simple);
        samples.add(simple1);
        samples.add(simple2);
        
        return samples;
    }
        
    public SimpleTariffDaoIbatisTest() {
        String[] configLocation = {"conf/ctx-datasource.xml", "conf/ctx-billing.xml"};
        ctx = new FileSystemXmlApplicationContext(configLocation);
        dataSource = (DataSource) ctx.getBean("dataSource");
        dao = (SimpleTariffDao) ctx.getBean("simpleTariffDao");
        dbInit = (SimpleTariffDatabaseInit) ctx.getBean("simpleTariffDatabaseCreator");        
        seqInit = (SequencerDatabaseInit) ctx.getBean("sequencerDatabaseCreator");
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        seqInit.createDatabase();
        dbInit.createDatabase();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        seqInit.dropDatabase();
        dbInit.dropDatabase();
    }

    public void testCreateAndGetById() {
        List samples = getSampleData();
        dao.create((SimpleTariff) samples.get(0));
        
        SimpleTariff s = (SimpleTariff) samples.get(0);
        SimpleTariff sx = dao.getById(new Integer(0));
        
        assertNotNull(sx);
        
        compareEquality(s, sx);
    }
    
    private void compareEquality(SimpleTariff s, SimpleTariff sx) {
        assertEquals(s.getName(), sx.getName());
        assertEquals(s.getChargeUnit(), sx.getChargeUnit());
        assertEquals(s.getGracePeriod(), sx.getGracePeriod());
        assertEquals(s.getGracePeriodCharge(), sx.getGracePeriodCharge());
        assertEquals(s.getRate(), sx.getRate());
        assertEquals(s.getTimeUnit(), sx.getTimeUnit());
    }

    public void testUpdate() {
        SimpleTariff st = (SimpleTariff) getSampleData().get(0);
        SimpleTariff st2 = (SimpleTariff) getSampleData().get(1);
        
        dao.create(st);
        SimpleTariff stx = dao.getById(new Integer(0));
        compareEquality(st, stx);
        
        st2.setId(new Integer(0));
        dao.update(st2);
        SimpleTariff stx2 = dao.getById(new Integer(0));
        compareEquality(st2, stx2);
    }

    public void testDelete() {
        Iterator samples = getSampleData().iterator();
        while(samples.hasNext()) {
            dao.create((SimpleTariff) samples.next());
        }
                
        SimpleTariff st = dao.getById(new Integer(0));
        assertNotNull(st);
        dao.delete(st);
        SimpleTariff sts = dao.getById(new Integer(0));
        assertNull(sts);
    }

    public void testGetAll() {
        Iterator samples = getSampleData().iterator();
        while(samples.hasNext()) {
            dao.create((SimpleTariff) samples.next());
        }
        
        Iterator result = dao.getAll().iterator();
        List sampleData = getSampleData();
        int i = 0;
        while(result.hasNext()) {            
            compareEquality((SimpleTariff)sampleData.get(i++), (SimpleTariff)result.next());
        }
    }

}
