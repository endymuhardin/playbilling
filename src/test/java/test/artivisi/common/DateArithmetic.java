/*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package test.artivisi.common;

import org.joda.time.DateTime;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DateArithmetic {

    public static void main(String[] args) {
        DateTime seven = new DateTime(2005,6,8,7,50,0,0);
        DateTime eight = new DateTime(2005,6,8,8,0,0,0);
        
        long sevenMillis = seven.toDate().getTime();
        long eightMillis = eight.toDate().getTime();
        
        System.out.println("From seven to eight: "+((eightMillis-sevenMillis)/1000));
    }
}
