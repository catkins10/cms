/*
 * Created on 2004-12-23
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
public class EqualTag extends BodyTagSupport {
	protected String scope = null;
	protected String name = null;
	protected String property = null;
	protected String scopeCompare = null;
	protected String nameCompare = null;
	protected String propertyCompare = null;
	protected String value = null;
		
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
		Object obj = null;
		try {
			obj = TagUtils.getInstance().lookup(pageContext, name, property, scope);
		}
		catch(Exception e) {
			name = ((ActionConfig)TagUtils.getInstance().lookup(pageContext,"org.apache.struts.action.mapping.instance", scope)).getName();
			if(name!=null) {
				obj = TagUtils.getInstance().lookup(pageContext, name, property, scope);
			}
		}
		if(value!=null) {
			return (obj + "").equals(value) ? EVAL_BODY_INCLUDE:SKIP_BODY;
		}
		else {
		    if(nameCompare==null) {
		        nameCompare = Constants.BEAN_KEY;
			}
			Object objCompare = TagUtils.getInstance().lookup(pageContext, nameCompare, propertyCompare, scopeCompare);
			return (obj + "").equals(objCompare + "") ? EVAL_BODY_INCLUDE : SKIP_BODY;
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
		value = null;
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
	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
