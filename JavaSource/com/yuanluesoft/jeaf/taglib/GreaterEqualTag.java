/*
 * Created on 2005-3-2
 *
 */
package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.config.ActionConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

/**
 * 
 * @author linchuan
 *
 */
public class GreaterEqualTag extends BodyTagSupport {
	protected String scope = null;
	protected String name = null;
	protected String property = null;
	protected String scopeCompare = null;
	protected String nameCompare = null;
	protected String propertyCompare = null;
	
	/**
	 * Perform the test required for this particular tag, and either evaluate
	 * or skip the body of this tag.
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	public int doStartTag() throws JspException {
		if(name==null) {
			name = Constants.BEAN_KEY;
		}
		if(nameCompare==null) {
			nameCompare = Constants.BEAN_KEY;
		}
		Object obj = null;
		try {
			obj = TagUtils.getInstance().lookup(pageContext, name, property, scope);
		}
		catch(Exception e) {
			name = ((ActionConfig)TagUtils.getInstance().lookup(pageContext,"org.apache.struts.action.mapping.instance", scope)).getName();
			obj = TagUtils.getInstance().lookup(pageContext, name, property, scope);
		}
		Object objCompare = null;
		try {
			objCompare = TagUtils.getInstance().lookup(pageContext, nameCompare, propertyCompare, scopeCompare);
		}
		catch(Exception e) {
			nameCompare = ((ActionConfig)TagUtils.getInstance().lookup(pageContext,"org.apache.struts.action.mapping.instance", scope)).getName();
			objCompare = TagUtils.getInstance().lookup(pageContext, nameCompare, propertyCompare, scopeCompare);
		}
		if((obj instanceof Integer)) {
			return ((Integer)obj).intValue() >= ((Integer)objCompare).intValue() ? EVAL_BODY_INCLUDE:SKIP_BODY;
		}
		else if((obj instanceof Long)) {
			return ((Long)obj).longValue() >= ((Long)objCompare).longValue() ? EVAL_BODY_INCLUDE:SKIP_BODY;
		}
		else if((obj instanceof Float)) {
			return ((Float)obj).floatValue() >= ((Float)objCompare).floatValue() ? EVAL_BODY_INCLUDE:SKIP_BODY;
		}
		else if((obj instanceof Double)) {
			return ((Double)obj).doubleValue() >= ((Double)objCompare).doubleValue() ? EVAL_BODY_INCLUDE:SKIP_BODY;
		}
		else {
			return (obj+"").compareTo(objCompare+"")>=0 ? EVAL_BODY_INCLUDE:SKIP_BODY;
		}
	}

	/**
	 * Evaluate the remainder of the current page normally.
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	/**
	 * Release all allocated resources.
	 */
	public void release() {
		super.release();
		name = null;
		property = null;
		scope = null;
		nameCompare = null;
		propertyCompare = null;
		scopeCompare = null;
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
	 * @return Returns the property.
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * @param property The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	/**
	 * @return Returns the scope.
	 */
	public String getScope() {
		return scope;
	}
	/**
	 * @param scope The scope to set.
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	/**
	 * @return Returns the nameCompare.
	 */
	public String getNameCompare() {
		return nameCompare;
	}
	/**
	 * @param nameCompare The nameCompare to set.
	 */
	public void setNameCompare(String nameCompare) {
		this.nameCompare = nameCompare;
	}
	/**
	 * @return Returns the propertyCompare.
	 */
	public String getPropertyCompare() {
		return propertyCompare;
	}
	/**
	 * @param propertyCompare The propertyCompare to set.
	 */
	public void setPropertyCompare(String propertyCompare) {
		this.propertyCompare = propertyCompare;
	}
	/**
	 * @return Returns the scopeCompare.
	 */
	public String getScopeCompare() {
		return scopeCompare;
	}
	/**
	 * @param scopeCompare The scopeCompare to set.
	 */
	public void setScopeCompare(String scopeCompare) {
		this.scopeCompare = scopeCompare;
	}
}
