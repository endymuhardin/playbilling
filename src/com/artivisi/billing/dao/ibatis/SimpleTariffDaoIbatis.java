/*
 * Created on Jun 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.dao.ibatis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.artivisi.billing.SimpleTariff;
import com.artivisi.billing.TimeUnit;
import com.artivisi.billing.dao.SimpleTariffDao;
import com.artivisi.common.dao.Sequencer;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleTariffDaoIbatis extends SqlMapClientDaoSupport implements SimpleTariffDao {
    private Sequencer sequencer;

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SimpleTariffDao#create(com.artivisi.billing.SimpleTariff)
     */
    public Integer create(SimpleTariff tariff) {
        if (tariff == null) return null;
        Integer newId = sequencer.getNext(getClass().getName());
        tariff.setId(newId);
        
        getSqlMapClientTemplate().update("insertSimpleTariff", tariff);
        return newId;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SimpleTariffDao#update(com.artivisi.billing.SimpleTariff)
     */
    public void update(SimpleTariff tariff) {    	
        getSqlMapClientTemplate().update("updateSimpleTariff", tariff);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SimpleTariffDao#delete(com.artivisi.billing.SimpleTariff)
     */
    public void delete(SimpleTariff tariff) {
        getSqlMapClientTemplate().delete("deleteSimpleTariff", tariff);
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SimpleTariffDao#getById(java.lang.Integer)
     */
    public SimpleTariff getById(Integer id) {
        if (id == null) return null;
        return toSimpleTariff((Map) getSqlMapClientTemplate().queryForObject("getSimpleTariffById", id));
    }
    
    private SimpleTariff toSimpleTariff(Map map){ 
        if (map == null) return null;
        
        SimpleTariff st = new SimpleTariff();
        st.setId((Integer) map.get("ID"));
        st.setChargeUnit(((Integer) map.get("CHARGE_UNIT")).longValue());
        st.setGracePeriod(((Integer) map.get("GRACE_PERIOD")).longValue());
        st.setGracePeriodCharge((BigDecimal) map.get("GRACE_PERIOD_CHARGE"));
        st.setName((String) map.get("NAME"));
        st.setRate((BigDecimal) map.get("RATE"));
        st.setTimeUnit(TimeUnit.getInstance((String) map.get("TIME_UNIT")));
        
        return st;
    }

    /* (non-Javadoc)
     * @see com.artivisi.billing.dao.SimpleTariffDao#getAll()
     */
    public List getAll() {
        Iterator result = getSqlMapClientTemplate().queryForList("getAllSimpleTariff", null).iterator();
        List allTariff = new ArrayList();
        while (result.hasNext()) {
            allTariff.add(toSimpleTariff((Map) result.next()));
        }
        return allTariff;
    }

    /**
     * @param sequencer The sequencer to set.
     */
    public void setSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }
}
