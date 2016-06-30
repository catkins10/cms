/*
 * Created on 2004-12-23
 *
 */
package com.yuanluesoft.jeaf.taglib;

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 输出数字
 * @author LinChuan
 * 
 */
public class WriteNumberTag extends BodyTagSupport {
	private String scope = null; 
	private String name = null;
	private String property = null;
	private String scopePlus = null; 
	private String namePlus = null;
	private String propertyPlus = null;
	private int plus = 0;
	private boolean chinese; //是否中文数字
	private boolean capital; //是否大写,如：壹,弍
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		if(name==null) {
			name = Constants.BEAN_KEY;
		}
		Object value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
		if(value == null) {
			return SKIP_BODY; // Nothing to output
		}
		Number valuePlus = null;
		if(namePlus!=null) {
			valuePlus = (Number)TagUtils.getInstance().lookup(pageContext, namePlus, propertyPlus, scopePlus);
		}
		if(value instanceof Collection) { //集合
			//转换为集合元素个数
			value = new Integer(((Collection)value).size());
		}
		if(value instanceof Integer) {
			int intValue = (((Integer)value).intValue() + plus + (valuePlus==null ? 0 : valuePlus.intValue()));
			TagUtils.getInstance().write(pageContext, chinese ? StringUtils.getChineseNumber(intValue, capital) : ("" + intValue));
		}
		else if(value instanceof Number) {
			double doubleValue = (((Number)value).doubleValue() + plus + (valuePlus==null ? 0 : valuePlus.doubleValue()));
			TagUtils.getInstance().write(pageContext, ("" + doubleValue));
		}
		return SKIP_BODY;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		scope = null; 
		name = null;
		property = null;
		scopePlus = null; 
		namePlus = null;
		propertyPlus = null;
		plus = 0;
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
	 * @return Returns the namePlus.
	 */
	public String getNamePlus() {
		return namePlus;
	}
	/**
	 * @param namePlus The namePlus to set.
	 */
	public void setNamePlus(String namePlus) {
		this.namePlus = namePlus;
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
	 * @return Returns the propertyPlus.
	 */
	public String getPropertyPlus() {
		return propertyPlus;
	}
	/**
	 * @param propertyPlus The propertyPlus to set.
	 */
	public void setPropertyPlus(String propertyPlus) {
		this.propertyPlus = propertyPlus;
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
	 * @return Returns the scopePlus.
	 */
	public String getScopePlus() {
		return scopePlus;
	}
	/**
	 * @param scopePlus The scopePlus to set.
	 */
	public void setScopePlus(String scopePlus) {
		this.scopePlus = scopePlus;
	}
	
	/**
	 * @return Returns the plus.
	 */
	public int getPlus() {
		return plus;
	}
	/**
	 * @param plus The plus to set.
	 */
	public void setPlus(int plus) {
		this.plus = plus;
	}

	/**
	 * @return the capital
	 */
	public boolean isCapital() {
		return capital;
	}

	/**
	 * @param capital the capital to set
	 */
	public void setCapital(boolean capital) {
		this.capital = capital;
	}

	/**
	 * @return the chinese
	 */
	public boolean isChinese() {
		return chinese;
	}

	/**
	 * @param chinese the chinese to set
	 */
	public void setChinese(boolean chinese) {
		this.chinese = chinese;
	}
}