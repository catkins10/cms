package com.yuanluesoft.portal.container.internal;

/**
 * 
 * @author linchuan
 *
 */
public class PortletPreference {
	/** The preference name. */
    private String name;
    /** The preference values. */
    private String[] values;
    /** Flag indicating if this preference is read-only. */
    private boolean readOnly = false;
    
	public PortletPreference(String name, String[] values, boolean readOnly) {
		super();
		this.name = name;
		this.values = values;
		this.readOnly = readOnly;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
    public Object clone() {
    	return new PortletPreference(this.name, (this.values==null ? null : (String[]) this.values.clone()), this.readOnly);
    }
    
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}
	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	/**
	 * @return the values
	 */
	public String[] getValues() {
		return values;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(String[] values) {
		this.values = values;
	}
}