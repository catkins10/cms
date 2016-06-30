package com.yuanluesoft.portal.container.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.portal.container.service.PortletContainer;

/**
 * 
 * @author linchuan
 *
 */
public class PortletPreferencesImpl implements PortletPreferences {
	private PortletContainer portletContainer;
	private PortletWindow portletWindow;
	private PortletRequest request;
	
	//私有属性
	private Map preferences;
	
	public PortletPreferencesImpl(PortletContainer portletContainer, PortletWindow portletWindow, PortletRequest request) {
		super();
		this.portletContainer = portletContainer;
		this.portletWindow = portletWindow;
		this.request = request;
		try {
			this.preferences = portletContainer.getPortletPreferences(portletWindow, request);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		if(this.preferences==null) {
			this.preferences = new HashMap();
		}
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletPreferences#getMap()
	 */
	public Map getMap() {
		Map map = new HashMap();
        for(Iterator iterator = preferences.keySet().iterator(); iterator.hasNext();) {
        	String name = (String)iterator.next();
        	PortletPreference preference = (PortletPreference)preferences.get(name);
            map.put(preference.getName(), preference.getValues()!=null ? preference.getValues().clone() : null);
        }
        return Collections.unmodifiableMap(map);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletPreferences#getNames()
	 */
	public Enumeration getNames() {
		return new Vector(preferences.keySet()).elements();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletPreferences#getValue(java.lang.String, java.lang.String)
	 */
	public String getValue(String key, String defaultValue) {
		String[] values = getValues(key, new String[] { defaultValue });
        String value = null;
        if(values != null && values.length > 0) {
        	value = values[0];
        }
        if(value == null) {
        	value = defaultValue;
        }
        return value;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletPreferences#getValues(java.lang.String, java.lang.String[])
	 */
	public String[] getValues(String key, String[] defaultValues) {
		if (key == null) {
            throw new IllegalArgumentException("Preference key is null");
        }
        String[] values = null;
        PortletPreference preference = (PortletPreference)preferences.get(key);
        if(preference != null) {
            values = preference.getValues();
        }
        if(values == null) {
            values = defaultValues;
        }
        return values;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletPreferences#isReadOnly(java.lang.String)
	 */
	public boolean isReadOnly(String key) {
		if (key == null) {
            throw new IllegalArgumentException("Preference key is null");
        }
		PortletPreference preference = (PortletPreference)preferences.get(key);
        return (preference != null && preference.isReadOnly());
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletPreferences#reset(java.lang.String)
	 */
	public void reset(String key) throws ReadOnlyException {
		if(isReadOnly(key)) {
			throw new ReadOnlyException("preference is readonly");
        }
        // Try to reset preference to the default values.
        boolean resetDone = false;
		Map defaultPreferences = null;
		try {
			defaultPreferences = portletContainer.getDefaultPortletPreferences(portletWindow, request);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		for(Iterator iterator = (defaultPreferences==null ? null : defaultPreferences.keySet().iterator()) ; !resetDone && iterator!=null && iterator.hasNext();) {
			String name = (String)iterator.next();
        	if(key.equals(name)) {
        		preferences.put(key, defaultPreferences.get(name));
        		resetDone = true;
        	}
        }
        //Remove preference if default values are not defined (PLT.14.1).
        if(!resetDone) {
        	preferences.remove(key);
        }
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletPreferences#setValue(java.lang.String, java.lang.String)
	 */
	public void setValue(String key, String value) throws ReadOnlyException {
		setValues(key, new String[] {value});
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletPreferences#setValues(java.lang.String, java.lang.String[])
	 */
	public void setValues(String key, String[] values) throws ReadOnlyException {
		if (isReadOnly(key)) {
            throw new ReadOnlyException("preference is readonly");
        }
		PortletPreference preference = (PortletPreference)preferences.get(key);
		if (preference != null) {
			preference.setValues(values);
		}
		else {
			preference = new PortletPreference(key, values, false);
            preferences.put(key, preference);
		}
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletPreferences#store()
	 */
	public void store() throws IOException, ValidatorException {
		try {
			portletContainer.storePortletPreferences(portletWindow, request, this);
		} 
		catch (Exception e) {
			throw new IOException(e);
		}
	}
}