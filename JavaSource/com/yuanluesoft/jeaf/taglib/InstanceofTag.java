/*
 * Created on 2004-12-23
 *
 */
package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

/**
 * 
 * @author linchuan
 *
 */
public class InstanceofTag extends BodyTagSupport {
	protected String scope = null;
	protected String name = null;
	protected String property = null;
	protected String className = null; //类名称
	protected boolean fullName = false; //类名称是否完整
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
		String objectName = TagUtils.getInstance().lookup(pageContext, name, property, scope).getClass().getName();
		if(!fullName) {
			objectName = objectName.substring(objectName.lastIndexOf(".")+1);
		}
		return objectName.equals(className) ? EVAL_BODY_INCLUDE : SKIP_BODY;
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
		className = null;
	}
	
	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return Returns the fullName.
	 */
	public boolean isFullName() {
		return fullName;
	}
	/**
	 * @param fullName The fullName to set.
	 */
	public void setFullName(boolean fullName) {
		this.fullName = fullName;
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
}
