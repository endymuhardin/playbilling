/*
 * Created on Jun 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.dao.ibatis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.artivisi.common.dao.ibatis.SequencerDatabaseInit;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BillingDatabaseInit {
    private SequencerDatabaseInit seqInit;
    private MemberDatabaseInit memberInit;
    private SessionDatabaseInit sessInit;
    private SimpleTariffDatabaseInit tariffInit;
    
    public void createDatabase() {
        seqInit.createDatabase();
        memberInit.createDatabase();
        sessInit.createDatabase();
        tariffInit.createDatabase();
    }
    
    public static void main(String[] args) {
        String[] configLocation = {"WEB-INF/conf/ctx-commons.xml", 
                				   "WEB-INF/conf/ctx-datasource.xml", 
                				   "WEB-INF/conf/ctx-billing.xml"};
        ApplicationContext ctx = new FileSystemXmlApplicationContext(configLocation);
        
        BillingDatabaseInit init = new BillingDatabaseInit();
        
        init.setSequencerInit((SequencerDatabaseInit) ctx.getBean("sequencerDatabaseCreator"));        
        init.setMemberInit((MemberDatabaseInit) ctx.getBean("memberDatabaseCreator"));
        init.setTariffInit((SimpleTariffDatabaseInit) ctx.getBean("simpleTariffDatabaseCreator"));
        init.setSessionInit((SessionDatabaseInit) ctx.getBean("sessionDatabaseCreator"));
        
        init.createDatabase();
    }
    
    /**
     * @param memberInit The memberInit to set.
     */
    public void setMemberInit(MemberDatabaseInit memberInit) {
        this.memberInit = memberInit;
    }
    /**
     * @param seqInit The seqInit to set.
     */
    public void setSequencerInit(SequencerDatabaseInit seqInit) {
        this.seqInit = seqInit;
    }
    /**
     * @param sessInit The sessInit to set.
     */
    public void setSessionInit(SessionDatabaseInit sessInit) {
        this.sessInit = sessInit;
    }
    /**
     * @param tariffInit The tariffInit to set.
     */
    public void setTariffInit(SimpleTariffDatabaseInit tariffInit) {
        this.tariffInit = tariffInit;
    }
    
}
