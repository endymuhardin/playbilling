/*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.billing;

import com.artivisi.billing.TimeUnit;

import junit.framework.TestCase;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TimeUnitTest extends TestCase {

    public void testEquals() {
        assertTrue(TimeUnit.SECOND.equals(TimeUnit.SECOND));
        assertFalse(TimeUnit.HOUR.equals(TimeUnit.SECOND));
    }

}
