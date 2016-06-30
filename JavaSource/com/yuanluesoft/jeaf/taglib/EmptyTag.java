/*
 * Created on 2005-3-2
 *
 */
package com.yuanluesoft.jeaf.taglib;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class EmptyTag extends org.apache.struts.taglib.logic.EmptyTag {
	//动态属性O
	protected String scopeProperty = null;
	protected String nameProperty = null;
	protected String propertyProperty = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.taglib.logic.EmptyTag#condition(boolean)
	 */
	protected boolean condition(boolean desired) throws JspException {
	    //重设属性名称
		if(name==null) {
	    	name = Constants.BEAN_KEY;
		}
		if(nameProperty!=null) {
			this.property = (String)TagUtils.getInstance().lookup(pageContext, nameProperty, propertyProperty, scopeProperty);
		}
		if(this.name == null) {
            JspException e =
                    new JspException(messages.getMessage("empty.noNameAttribute"));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        Object value = null;
        try {
	        if(this.property == null) {
	            value = TagUtils.getInstance().lookup(pageContext, name, scope);
	        }
	        else {
	            value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
	        }
        }
		catch(JspException e) {
		    value = null;
		}
        if(value==null) {
        	Object record = TagUtils.getInstance().lookup(pageContext, name, scope);
        	if(record!=null && (record instanceof Record)) {
        		value = ((Record)record).getExtendPropertyValue(this.property); //获取扩展属性
        	}
        }
        boolean empty = true;

        if (value == null) {
            empty = true;

        } else if (value instanceof String) {
            String strValue = (String) value;
            empty = (strValue.length() < 1);

        } else if (value instanceof Collection) {
            Collection collValue = (Collection) value;
            empty = collValue.isEmpty();

        } else if (value instanceof Map) {
            Map mapValue = (Map) value;
            empty = mapValue.isEmpty();

        } else if (value.getClass().isArray()) {
            empty = Array.getLength(value) == 0;

        } else {
            empty = false;
        }

        return (empty == desired);
    }
	
    /**
     * Perform the test required for this particular tag, and either evaluate
     * or skip the body of this tag.
     *
     * @exception JspException if a JSP exception occurs
     */
    public int doStartTag() throws JspException {
        if(condition(true))
            return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);

    }
	
    public void release() {
        super.release();
        nameProperty = null;
        propertyProperty = null;
        scopeProperty = null;
    }

    /**
	 * @return Returns the nameProperty.
	 */
	public String getNameProperty() {
		return nameProperty;
	}
	/**
	 * @param nameProperty The nameProperty to set.
	 */
	public void setNameProperty(String nameProperty) {
		this.nameProperty = nameProperty;
	}
	/**
	 * @return Returns the propertyProperty.
	 */
	public String getPropertyProperty() {
		return propertyProperty;
	}
	/**
	 * @param propertyProperty The propertyProperty to set.
	 */
	public void setPropertyProperty(String propertyProperty) {
		this.propertyProperty = propertyProperty;
	}
	/**
	 * @return Returns the scopeProperty.
	 */
	public String getScopeProperty() {
		return scopeProperty;
	}
	/**
	 * @param scopeProperty The scopeProperty to set.
	 */
	public void setScopeProperty(String scopeProperty) {
		this.scopeProperty = scopeProperty;
	}
}
