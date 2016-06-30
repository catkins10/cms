package com.yuanluesoft.jeaf.taglib;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

/**
 * 判断列表中是否包含某个值
 * @author linchuan
 *
 */
public class ContainsTag extends BodyTagSupport {
	protected String scope = null;
	protected String name = null;
	protected String property = null;
	//动态属性
	protected String scopeProperty = null;
	protected String nameProperty = null;
	protected String propertyProperty = null;
	//被比较的属性和属性值
	protected String propertyName = null;
	protected String propertyValue = null;
	
	/**
	 * Perform the test required for this particular tag, and either evaluate
	 * or skip the body of this tag.
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	public int doStartTag() throws JspException {
		try {
			if(name==null) {
				name = Constants.BEAN_KEY;
			}
			if(nameProperty!=null) {
				this.property = (String)TagUtils.getInstance().lookup(pageContext, nameProperty, propertyProperty, scopeProperty);
			}
			Collection list = (Collection)TagUtils.getInstance().lookup(pageContext, name, property, scope);
			//检查是否包含所需要的值
			for(Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
				if(propertyName==null) {
					if(obj.toString().equals(propertyValue)) {
						return EVAL_BODY_INCLUDE;
					}
				}
				else if(PropertyUtils.getProperty(obj, propertyName).toString().equals(propertyValue)) {
					return EVAL_BODY_INCLUDE;
				}
			}
		}
		catch(Exception e) {
			
		}
		return SKIP_BODY;
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
		scope = null;
		name = null;
		property = null;
		//动态属性
		scopeProperty = null;
		nameProperty = null;
		propertyProperty = null;
		//被比较的属性和属性值
		propertyName = null;
		propertyValue = null;
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
	 * @return the nameProperty
	 */
	public String getNameProperty() {
		return nameProperty;
	}

	/**
	 * @param nameProperty the nameProperty to set
	 */
	public void setNameProperty(String nameProperty) {
		this.nameProperty = nameProperty;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the propertyProperty
	 */
	public String getPropertyProperty() {
		return propertyProperty;
	}

	/**
	 * @param propertyProperty the propertyProperty to set
	 */
	public void setPropertyProperty(String propertyProperty) {
		this.propertyProperty = propertyProperty;
	}

	/**
	 * @return the propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @param propertyValue the propertyValue to set
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the scopeProperty
	 */
	public String getScopeProperty() {
		return scopeProperty;
	}

	/**
	 * @param scopeProperty the scopeProperty to set
	 */
	public void setScopeProperty(String scopeProperty) {
		this.scopeProperty = scopeProperty;
	}
}
