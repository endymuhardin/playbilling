/*
 * Created on Jun 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.artivisi.billing;

import java.math.BigDecimal;

/**
 * @author endy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleTariff implements Tariff {
    private Integer id = new Integer(-1);
    private String name;
    private BigDecimal rate = new BigDecimal(0);
    private long chargeUnit = 0;
    private TimeUnit timeUnit = TimeUnit.MINUTE;
    private long gracePeriod = 0;
    private BigDecimal gracePeriodCharge = new BigDecimal(0);
    
    public void charge(Session sess) throws InsufficientCreditException {
        BigDecimal result = gracePeriodCharge;
        if (chargeUnit < 1) {
        	throw new IllegalStateException("minimum value for chargeUnit is 1");
        }
        
        long chargedDuration = getTimeUnit(sess) - gracePeriod;
        if (chargedDuration > 0) {            
            while(chargedDuration > 0) {
                result = result.add(rate);
                chargedDuration -= chargeUnit;                
            }            
        }        
        
        BigDecimal oldSessionCharge = sess.getCharge();
        BigDecimal delta = result.subtract(oldSessionCharge);
        
        sess.setCharge(result);
        Member member = sess.getMember(); 
        if (member.getType().equals(MemberType.PREPAID)) {
        	BigDecimal oldCredit = member.getCredit();
        	BigDecimal newCredit = oldCredit.subtract(delta);
        	if (newCredit.compareTo(new BigDecimal(0)) < 0) {
        		throw new InsufficientCreditException("negative credit");        		
        	}
        	member.setCredit(newCredit);
        }
    }
    
    private long getTimeUnit(Session sess) {
        if (timeUnit == null) throw new IllegalStateException("Invalid time unit");
        
        long sessSecond = sess.getDuration()/1000;
        if (timeUnit.equals(TimeUnit.SECOND)) {
            return sessSecond;
        } else if(timeUnit.equals(TimeUnit.MINUTE)) {
            return sessSecond/60;
        } else if(timeUnit.equals(TimeUnit.MINUTE)) {
            return sessSecond/3600;
        } else {
            throw new IllegalStateException("Invalid time unit");
        }        
    }   
    /**
     * @return Returns the gracePeriod.
     */
    public long getGracePeriod() {
        return gracePeriod;
    }
    /**
     * @param gracePeriod The gracePeriod to set.
     */
    public void setGracePeriod(long gracePeriod) {
    	if (gracePeriod < 0) {
    		gracePeriod = 0;
    	}
        this.gracePeriod = gracePeriod;
    }
    /**
     * @return Returns the gracePeriodCharge.
     */
    public BigDecimal getGracePeriodCharge() {
        return gracePeriodCharge;
    }
    /**
     * @param gracePeriodCharge The gracePeriodCharge to set.
     */
    public void setGracePeriodCharge(BigDecimal gracePeriodCharge) {
    	if (gracePeriodCharge.compareTo(new BigDecimal(0))<0) {
    		gracePeriodCharge = new BigDecimal(0);
    	}
        this.gracePeriodCharge = gracePeriodCharge;
    }
    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the rate.
     */
    public BigDecimal getRate() {
        return rate;
    }
    /**
     * @param rate The rate to set.
     */
    public void setRate(BigDecimal rate) {
    	if (rate.compareTo(new BigDecimal(0)) < 0) {
    		rate = new BigDecimal(0);
    	}
        this.rate = rate;
    }
    /**
     * @return Returns the timeUnit.
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
    /**
     * @param timeUnit The timeUnit to set.
     */
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
    /**
     * @return Returns the chargeUnit.
     */
    public long getChargeUnit() {
        return chargeUnit;
    }
    /**
     * @param chargeUnit The chargeUnit to set.
     */
    public void setChargeUnit(long chargeUnit) {
    	if (chargeUnit < 1) {
    		chargeUnit = 1;
    	}
        this.chargeUnit = chargeUnit;
    }
}
