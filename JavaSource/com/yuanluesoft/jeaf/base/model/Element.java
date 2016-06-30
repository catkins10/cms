/*
 * Created on 2005-1-14
 *
 */
package com.yuanluesoft.jeaf.base.model;


/**
 * 
 * @author linchuan
 *
 */
public class Element extends CloneableObject {
	protected String id;
	protected String name;
	
	public Element() {
        
    }
	
    /**
     * @param id
     * @param name
     */
    public Element(String id, String name) {
        this.id = id;
        this.name = name;
    }
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
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
}
