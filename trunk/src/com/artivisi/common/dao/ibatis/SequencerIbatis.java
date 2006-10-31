/*
 * Created on Jun 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.common.dao.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.artivisi.common.dao.Sequencer;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SequencerIbatis extends SqlMapClientDaoSupport implements Sequencer {

    /* (non-Javadoc)
     * @see com.artivisi.common.dao.Sequencer#getNext(java.lang.String)
     */
    public Integer getNext(String name) {
        Integer next = (Integer) getSqlMapClientTemplate().queryForObject("getNextValue", name);
        if (next == null) {
            getSqlMapClientTemplate().insert("createSequencer", name);
            next = (Integer) getSqlMapClientTemplate().queryForObject("getNextValue", name);
        }
        getSqlMapClientTemplate().update("increment", name);
        return next;
    }

}
