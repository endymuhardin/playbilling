/*
 * Created on Jun 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.dao.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MemberDatabaseInit extends SqlMapClientDaoSupport {
    
    public void createDatabase() {
        getSqlMapClientTemplate().update("createDatabaseMember", null);
        
    }
    
    public void dropDatabase() {
        getSqlMapClientTemplate().update("dropDatabaseMember", null);        
    }

}
