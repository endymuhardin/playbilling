/*
 * Created on Jun 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.billing;

import java.util.Date;

import org.joda.time.DateTime;

import com.artivisi.billing.Member;

import junit.framework.TestCase;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MemberTest extends TestCase {

    public void testIsExpired() {
        Member m = new Member();
        Date expire = new DateTime(2004,1,1,0,0,0,0).toDate();
        m.setDateExpired(expire);
        assertTrue(m.isExpired());
        
        expire = new DateTime(2008,1,1,0,0,0,0).toDate();
        m.setDateExpired(expire);
        assertFalse(m.isExpired());
        
    }

}
