/*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.billing;

import java.math.BigDecimal;

import junit.framework.TestCase;

import com.artivisi.billing.Member;
import com.artivisi.billing.MemberType;
import com.artivisi.billing.Session;
import com.artivisi.billing.SimpleTariff;
import com.artivisi.billing.TimeUnit;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleTariffTest extends TestCase {

    public void testChargePerMinute() throws Exception {
        Session sess = new MockSession();
        
        /* 5000 for first 30 minute, 
         * 2000 for every 15 minute afterward 
         * */
        SimpleTariff simple = new SimpleTariff();
        simple.setTimeUnit(TimeUnit.MINUTE); // time unit is MINUTE
        simple.setGracePeriod(30); // grace period 30 minute
        simple.setGracePeriodCharge(new BigDecimal(5000)); // charge 5000 for first 15 minute        
        
        simple.setChargeUnit(15); // charge every 15 minute
        simple.setRate(new BigDecimal(2000));
        
        simple.charge(sess);
        
        assertEquals(new BigDecimal(11000), sess.getCharge());
    }
    
    public void testChargePerSecond() throws Exception{
        Session sess = new MockSession();
        
        /* 6000 for first hour
         * 100 per 30 second afterward
         * should bill 6300
         * */
        SimpleTariff simple = new SimpleTariff();
        simple.setTimeUnit(TimeUnit.SECOND); // time unit is SECOND
        simple.setGracePeriod(3600); // grace period 3600 second
        simple.setGracePeriodCharge(new BigDecimal(6000)); // charge 5000 for first hour        
        
        simple.setChargeUnit(30); // charge every 30 second
        simple.setRate(new BigDecimal(100)); // 100 per 30 second
        
        simple.charge(sess);
        
        assertEquals(new BigDecimal(6300), sess.getCharge());
    }
    
    public void testChargePerHour() throws Exception{
        Session sess = new MockSession();
        
        /* Simply 100 per minute
         * Should charge 6100
         * */
        SimpleTariff simple = new SimpleTariff();
        simple.setTimeUnit(TimeUnit.MINUTE); // time unit is MINUTE
        // no grace period
        // no charge for grace period        
        
        simple.setChargeUnit(1); // charge every 1 minute
        simple.setRate(new BigDecimal(100)); // 100 per minute
        
        simple.charge(sess);
        
        assertEquals(new BigDecimal(6100), sess.getCharge());
    }
    
    public void testMemberCreditPrepaid() throws Exception{
    	Session sess = new MockSession();
        
        /* Simply 100 per minute
         * Should charge 6100
         * */
        SimpleTariff simple = new SimpleTariff();
        simple.setTimeUnit(TimeUnit.MINUTE); // time unit is MINUTE
        // no grace period
        // no charge for grace period        
        
        simple.setChargeUnit(1); // charge every 1 minute
        simple.setRate(new BigDecimal(100)); // 100 per minute
        
        simple.charge(sess);
        
        assertEquals(new BigDecimal(6100), sess.getCharge());
        assertEquals(new BigDecimal(3900), sess.getMember().getCredit());
    }
    
    public void testMemberCreditPostpaid() throws Exception{
    	Session sess = new MockSession();
        
        /* Simply 100 per minute
         * Should charge 6100
         * */
        SimpleTariff simple = new SimpleTariff();
        simple.setTimeUnit(TimeUnit.MINUTE); // time unit is MINUTE
        // no grace period
        // no charge for grace period        
        
        simple.setChargeUnit(1); // charge every 1 minute
        simple.setRate(new BigDecimal(100)); // 100 per minute
        
        // change type to postpaid
        sess.getMember().setType(MemberType.POSTPAID);
        
        simple.charge(sess);
        
        assertEquals(new BigDecimal(6100), sess.getCharge());
        assertEquals(new BigDecimal(10000), sess.getMember().getCredit());
    }
    
    public void testConvertDuration() {
        long duration = new MockSession().getDuration()/1000;
        System.out.println("Duration : "+duration);
        long hours = duration/3600;
        assertEquals(1, hours);
        long minutes = (duration-(hours*3600))/60;
        assertEquals(1, minutes);
        long seconds = duration - (3600*hours) - (60*minutes);
        assertEquals(20, seconds);
    }
    
    class MockSession extends Session {  
    	public MockSession() {
    		Member m = new Member();
			m.setType(MemberType.PREPAID);			
			m.setCredit(new BigDecimal(10000));	
			setMember(m);
    	}
         
        public long getDuration() {
            // always return 1 hour 1 minutes 20 seconds
            return (3600 + 60 + 20)*1000;
        }
    }
}
