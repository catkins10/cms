/*
 * Created on 2005-2-20
 *
 */
package com.yuanluesoft.jeaf.taglib;

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

/**
 * 
 * @author linchuan
 *
 */
public class SizeEqualTag extends BodyTagSupport {
	protected String scope = null;
	protected String name = null;
	protected String property = null;
	protected int value;
	
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
		Collection list = (Collection)TagUtils.getInstance().lookup(pageContext, name, property, scope);
		return (list==null ? 0 : list.size())==value ? EVAL_BODY_INCLUDE:SKIP_BODY;
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
	 * @return Returns the value.
	 */
	public int getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(int value) {
		this.value = value;
	}
}
