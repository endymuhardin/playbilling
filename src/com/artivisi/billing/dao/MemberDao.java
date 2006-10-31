/*
 * Created on Jun 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing.dao;

import java.util.List;

import com.artivisi.billing.Member;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface MemberDao {
    public Integer create(Member m);
    public void update(Member m);
    public void delete(Member m);
    public Member getById(Integer id);
    public Member getByUsernameAndPassword(String username, String password);
    public List search(Member m);
    public List getAll();
    public boolean isUsernameExist(String username); 
}
