/*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.dao;

import java.util.List;

import com.artivisi.billing.SimpleTariff;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface SimpleTariffDao {
    public Integer create(SimpleTariff tariff);
    public void update(SimpleTariff tariff);
    public void delete(SimpleTariff tariff);
    public SimpleTariff getById(Integer id);
    public List getAll();
}
