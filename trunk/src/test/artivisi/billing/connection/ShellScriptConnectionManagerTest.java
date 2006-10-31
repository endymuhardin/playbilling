/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.billing.connection;

import com.artivisi.billing.connection.ShellScriptConnectionManager;

import junit.framework.TestCase;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShellScriptConnectionManagerTest extends TestCase {

    public void testOpen() {
        ShellScriptConnectionManager conn = new ShellScriptConnectionManager();
        conn.setOpenCommand("src/test/artivisi/billing/connection/open-success.sh");        
        assertTrue(conn.open("192.168.1.2"));        
        conn.setOpenCommand("src/test/artivisi/billing/connection/open-fail.sh");
        assertFalse(conn.open("192.168.1.2"));
    }

    public void testClose() {
        ShellScriptConnectionManager conn = new ShellScriptConnectionManager();        
        conn.setCloseCommand("src/test/artivisi/billing/connection/close-success.sh");
        assertTrue(conn.close("192.168.1.2"));
        conn.setCloseCommand("src/test/artivisi/billing/connection/close-fail.sh");
        assertFalse(conn.close("192.168.1.2"));
    }

}
