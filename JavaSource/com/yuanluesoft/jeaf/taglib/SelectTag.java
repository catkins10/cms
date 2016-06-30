/*
 * Created on 2005-9-14
 *
 */
package com.yuanluesoft.jeaf.taglib;

import java.util.Collection;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.logic.IterateTag;

import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SelectTag extends IterateTag {
	//选择条件
	protected String value = null;
	//动态选择条件
	protected String scopeValue = null;
	protected String nameValue = null;
	protected String propertyValue = null;
	//对象中用来比较的属性
	protected String select = null;
	protected String isLastId = null;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		if(name==null) {
			name = Constants.BEAN_KEY;
		}
        // Acquire the collection we are going to iterate over
        Collection collection = (Collection)this.collection;
        try {
        	if (collection == null) {
        		collection = (Collection)TagUtils.getInstance().lookup(pageContext, name, property, scope);
        	}
        	if (collection == null) {
        		return SKIP_BODY;
        	}
        	Object valueCompare = value;
        	if(valueCompare==null) {
        		valueCompare = TagUtils.getInstance().lookup(pageContext, nameValue, propertyValue, scopeValue);
        	}
        	collection = ListUtils.getSubListByProperty(collection, select, valueCompare);
        	if (collection == null) {
        		return SKIP_BODY;
        	}
        	iterator = collection.iterator();

        	// Calculate the starting offset
        	if (offset == null) {
        		offsetValue = 0;
        	} else {
        		try {
        			offsetValue = Integer.parseInt(offset);
        		} catch (NumberFormatException e) {
        			Integer offsetObject = (Integer) TagUtils.getInstance().lookup(pageContext, offset, null);
        			if (offsetObject == null) {
        				offsetValue = 0;
        			} else {
        				offsetValue = offsetObject.intValue();
        			}
        		}
        	}
        	if (offsetValue < 0) {
        		offsetValue = 0;
        	}

        	// Calculate the rendering length
        	if (length == null) {
        		lengthValue = 0;
        	} else {
        		try {
        			lengthValue = Integer.parseInt(length);
        		} catch (NumberFormatException e) {
        			Integer lengthObject = (Integer) TagUtils.getInstance().lookup(pageContext, length, null);
        			if (lengthObject == null) {
        				lengthValue = 0;
        			} else {
        				lengthValue = lengthObject.intValue();
        			}
        		}
        	}
        	if (lengthValue < 0) {
        		lengthValue = 0;
        	}
        	lengthCount = 0;

        	// Skip the leading elements up to the starting offset
        	for (int i = 0; i < offsetValue; i++) {
        		if (iterator.hasNext()) {
        			iterator.next();
        		}
        	}

        	// Store the first value and evaluate, or skip the body if none
        	if (iterator.hasNext()) {
        		Object element = iterator.next();
        		if (element == null) {
        			pageContext.removeAttribute(id);
        		} else {
        			pageContext.setAttribute(id, element);
        		}
        		lengthCount++;
        		started = true;
        		if (indexId != null) {
        			pageContext.setAttribute(indexId, new Integer(getIndex()));
        		}
        		if(isLastId!=null) {
        			pageContext.setAttribute(isLastId, new Boolean(!iterator.hasNext()));
        		}
        		return (EVAL_BODY_AGAIN);
        	} else {
        		return (SKIP_BODY);
        	}
        }
        finally {
        	collection = null;
        }
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspException {
		// TODO Auto-generated method stub
		int ret = super.doAfterBody();
		if(isLastId!=null) {
			pageContext.setAttribute(isLastId, new Boolean(!iterator.hasNext()));
		}
		return ret;
	}
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		value = null;
		//动态选择条件
		scopeValue = null;
		nameValue = null;
		propertyValue = null;
		//对象中用来比较的属性
		select = null;
		super.release();
	}
	
	/**
	 * @return Returns the nameValue.
	 */
	public String getNameValue() {
		return nameValue;
	}
	/**
	 * @param nameValue The nameValue to set.
	 */
	public void setNameValue(String nameValue) {
		this.nameValue = nameValue;
	}
	/**
	 * @return Returns the propertyValue.
	 */
	public String getPropertyValue() {
		return propertyValue;
	}
	/**
	 * @param propertyValue The propertyValue to set.
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	/**
	 * @return Returns the scopeValue.
	 */
	public String getScopeValue() {
		return scopeValue;
	}
	/**
	 * @param scopeValue The scopeValue to set.
	 */
	public void setScopeValue(String scopeValue) {
		this.scopeValue = scopeValue;
	}
	/**
	 * @return Returns the select.
	 */
	public String getSelect() {
		return select;
	}
	/**
	 * @param select The select to set.
	 */
	public void setSelect(String select) {
		this.select = select;
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
	/**
	 * @return Returns the isLastId.
	 */
	public String getIsLastId() {
		return isLastId;
	}
	/**
	 * @param isLastId The isLastId to set.
	 */
	public void setIsLastId(String isLastId) {
		this.isLastId = isLastId;
	}
}
