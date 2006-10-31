/*
 * Created on Jun 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.billing.dao.ibatis;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BillingDaoTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(BillingDaoTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for test.artivisi.billing.dao.ibatis");
        //$JUnit-BEGIN$
        suite.addTestSuite(SessionDaoIbatisTest.class);
        suite.addTestSuite(MemberDaoIbatisTest.class);
        suite.addTestSuite(SimpleTariffDaoIbatisTest.class);
        //$JUnit-END$
        return suite;
    }
}
