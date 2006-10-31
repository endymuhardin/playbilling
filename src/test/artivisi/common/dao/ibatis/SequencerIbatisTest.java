/*
 * Created on Jun 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.common.dao.ibatis;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


import com.artivisi.common.dao.Sequencer;
import com.artivisi.common.dao.ibatis.SequencerDatabaseInit;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SequencerIbatisTest extends TestCase {
    private Sequencer sequencer;
    private ApplicationContext ctx;
    private SequencerDatabaseInit dbInit;
    
    public SequencerIbatisTest() {
        String[] configLocation = {"conf/ctx-commons.xml", "conf/ctx-datasource.xml"};
        ctx = new FileSystemXmlApplicationContext(configLocation);    
        sequencer =  (Sequencer) ctx.getBean("sequencer");
        dbInit = (SequencerDatabaseInit) ctx.getBean("sequencerDatabaseCreator");
    }
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        dbInit.createDatabase();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        dbInit.dropDatabase();
    }

    public void testGetNext() {
        Integer id = sequencer.getNext("test");
    	assertEquals(new Integer(0), id);
    	id = sequencer.getNext("test");
    	assertEquals(new Integer(1), id);
    	id = sequencer.getNext("test");
    	assertEquals(new Integer(2), id);
    	id = sequencer.getNext("test");
    	assertEquals(new Integer(3), id);
    }

}
