/*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.billing;

import com.artivisi.billing.Session;

import junit.framework.TestCase;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SessionTest extends TestCase {

    public void testCalculateDuration() throws Exception {
        Session sess = new Session();
        sess.start();
        Thread.sleep(5000);
        sess.stop();
        assertEquals(5, sess.getDuration()/1000);
    }

}
