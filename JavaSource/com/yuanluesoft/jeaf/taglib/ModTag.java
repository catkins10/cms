/*
 * Created on 2004-12-23
 *
 */
package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;

/**
 *
 * @author LinChuan
 * 整除取余数,余数与value比较
 *
 */
public class ModTag extends BodyTagSupport {
	protected String scope = null;
	protected String name = null;
	protected String property = null;
	protected int mod;
	protected int value;
	
	/**
	 * Perform the test required for this particular tag, and either evaluate
	 * or skip the body of this tag.
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	public int doStartTag() throws JspException {
		Object obj = TagUtils.getInstance().lookup(pageContext, name, property, scope);
		return (((Integer)obj).intValue() % mod)==value ? EVAL_BODY_INCLUDE:SKIP_BODY;
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
	}
	
	/**
	 * @return Returns the mod.
	 */
	public int getMod() {
		return mod;
	}
	/**
	 * @param mod The mod to set.
	 */
	public void setMod(int mod) {
		this.mod = mod;
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
