/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.connection;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShellScriptConnectionManager implements ConnectionManager {

    private static final Logger log = Logger.getLogger(ShellScriptConnectionManager.class);
    private String openCommand;
    private String closeCommand;
    /* (non-Javadoc)
     * @see com.artivisi.billing.connection.ConnectionManager#open(java.lang.String)
     */
    public boolean open(String ipAddress) {
        try {
            if (openCommand == null) {
                ConnectionManagementException ce = new ConnectionManagementException("Open Script does not exist, please configure"); 
                log.fatal(ce);
                throw ce;
            }
            Process command = Runtime.getRuntime().exec(openCommand+" "+ipAddress);            
            int result = command.waitFor();
            log.debug("Open command exit value: "+result);
            return result==0?true:false;
        } catch (IOException e) {
            log.error(e);
            throw new ConnectionManagementException("fail opening connection", e);
        } catch (InterruptedException e) {
            log.error(e);
            throw new ConnectionManagementException("fail opening connection", e);
        }
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.connection.ConnectionManager#close(java.lang.String)
     */
    public boolean close(String ipAddress) {
        try {
            if (openCommand == null){
                ConnectionManagementException ce = new ConnectionManagementException("Close Script does not exist, please configure"); 
                log.fatal(ce);
                throw ce;
            }
            Process command = Runtime.getRuntime().exec(closeCommand+" "+ipAddress);
            int result = command.waitFor();
            log.debug("Close command exit value: "+result);
            return result==0?true:false;
        } catch (IOException e) {
            log.error(e);
            throw new ConnectionManagementException("fail closing connection", e);
        } catch (InterruptedException e) {
            log.error(e);
            throw new ConnectionManagementException("fail closing connection", e);
        }
    }

    /**
     * @param closeCommand The closeCommand to set.
     */
    public void setCloseCommand(String closeCommand) {
        this.closeCommand = closeCommand;
    }
    /**
     * @param openCommand The openCommand to set.
     */
    public void setOpenCommand(String openCommand) {
        this.openCommand = openCommand;
    }
}
