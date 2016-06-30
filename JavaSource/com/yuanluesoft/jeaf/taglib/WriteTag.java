/*
 * Created on 2004-8-23
 *
 */
package com.yuanluesoft.jeaf.taglib;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.struts.config.ActionConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class WriteTag extends org.apache.struts.taglib.bean.WriteTag {
	//动态属性名
	private String scopeProperty = null; 
	private String nameProperty = null;
	private String propertyProperty = null;
	//动态格式
	private String scopeFormat = null;
	private String nameFormat = null;
	private String propertyFormat = null;
	//最多显示字符数
	private int maxCharCount = 0;
	//动态最多显示字符数
	private String scopeMaxCharCount = null;
	private String nameMaxCharCount = null;
	private String propertyMaxCharCount = null;
	
	private String ellipsis; //显示省略符号,如"...","等"
	//动态省略符号
	private String scopeEllipsis = null;
	private String nameEllipsis = null;
	private String propertyEllipsis = null;
	
	//输出MAP中的对象
	private String mapKey = null;
	private String scopeMapKey = null;
	private String nameMapKey = null;
	private String propertyMapKey = null;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		if(name==null) {
			name = Constants.BEAN_KEY;
		}
		//动态属性名
		if(nameProperty!=null || propertyProperty!=null) {
			if(nameProperty==null) {
				nameProperty = Constants.BEAN_KEY;
			}
			property = (String)TagUtils.getInstance().lookup(pageContext, nameProperty, propertyProperty, scopeProperty);
		}
		// Look up the requested bean (if necessary)
		if (ignore) {
		    if (TagUtils.getInstance().lookup(pageContext, name, scope) == null) {
                return (SKIP_BODY); // Nothing to output
            }
		}
		Object value = null;
		String output = null;
		
		// Look up the requested property value
		try {
		    value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
		}
		catch(JspException e) {
		    value = null;
		}
		if(value==null) {
        	Object record = TagUtils.getInstance().lookup(pageContext, name, scope);
        	if(record!=null && (record instanceof Record)) {
        		value = ((Record)record).getExtendPropertyValue(this.property); //获取扩展属性
        	}
        	else if(record!=null && (record instanceof SqlResult)) { //SQL执行结果
        		value = ((SqlResult)record).get(property);
        	}
        }
		if(value == null) {
			try {
				name = ((ActionConfig)TagUtils.getInstance().lookup(pageContext,"org.apache.struts.action.mapping.instance", scope)).getName();
			    value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
		    }
		    catch(Exception e) {
		        value = null;
		    }
		    if (value == null) {
		        return SKIP_BODY; // Nothing to output
		    }
		}
		Object key = mapKey;
		if(nameMapKey!=null || propertyMapKey!=null) {
			if(nameMapKey==null) {
				nameMapKey = Constants.BEAN_KEY;
			}
			key = TagUtils.getInstance().lookup(pageContext, nameMapKey, propertyMapKey, scopeMapKey);
		}
		if(key!=null) {
			if(value instanceof Map) {
				value = ((Map)value).get(key);
			}
			else if(value instanceof SqlResult) {
				value = ((SqlResult)value).get("" + key);
			}
			if(value==null) {
				return SKIP_BODY;
			}
		}
		if(value instanceof Calendar) { //日历,转换为时间
	    	value = new Timestamp(((Calendar)value).getTimeInMillis());
	    }
		//动态省略符号
		if(nameEllipsis!=null || propertyEllipsis!=null) {
			if(nameEllipsis==null) {
				nameEllipsis = Constants.BEAN_KEY;
			}
			this.ellipsis = (String)TagUtils.getInstance().lookup(pageContext, nameEllipsis, propertyEllipsis, scopeEllipsis);
		}
		//动态最多显示字符数
		if(nameMaxCharCount!=null || propertyMaxCharCount!=null) {
			if(nameMaxCharCount==null) {
				nameMaxCharCount = Constants.BEAN_KEY;
			}
			this.maxCharCount = ((Number)TagUtils.getInstance().lookup(pageContext, nameMaxCharCount, propertyMaxCharCount, scopeMaxCharCount)).intValue();
		}
		//动态格式
		if(nameFormat!=null || propertyFormat!=null) {
			if(nameFormat==null) {
				nameFormat = Constants.BEAN_KEY;
			}
			this.formatStr = (String)TagUtils.getInstance().lookup(pageContext, nameFormat, propertyFormat, scopeFormat);
		}
		if(value==null) {
		    return SKIP_BODY; // Nothing to output
		}
	    // Convert value to the String with some formatting
	    output = formatValue(value);
		if(maxCharCount>0) {
			output = StringUtils.slice(output, maxCharCount, ellipsis);
		}
		// Print this property value to our output writer, suitably filtered
		if (filter) {
			TagUtils.getInstance().write(pageContext, TagUtils.getInstance().filter(output));
		}
		else {
			TagUtils.getInstance().write(pageContext, output);
		}
		// Continue processing this page
		return SKIP_BODY;
	}
    /**
     * Release all allocated resources.
     */
    public void release() {
    	super.release();
    	ellipsis = null;
    	//动态属性名
    	scopeProperty = null; 
    	nameProperty = null;
    	propertyProperty = null;
    	//动态格式
    	scopeFormat = null;
    	nameFormat = null;
    	propertyFormat = null;
    	//动态省略符号属性
    	scopeEllipsis = null;
    	nameEllipsis = null;
    	propertyEllipsis = null;
    	//输出MAP中的对象
    	mapKey = null;
    	scopeMapKey = null;
    	nameMapKey = null;
    	propertyMapKey = null;
    	maxCharCount = 0;
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
	
	/**
	 * @return Returns the nameFormat.
	 */
	public String getNameFormat() {
		return nameFormat;
	}
	/**
	 * @param nameFormat The nameFormat to set.
	 */
	public void setNameFormat(String nameFormat) {
		this.nameFormat = nameFormat;
	}
	/**
	 * @return Returns the propertyFormat.
	 */
	public String getPropertyFormat() {
		return propertyFormat;
	}
	/**
	 * @param propertyFormat The propertyFormat to set.
	 */
	public void setPropertyFormat(String propertyFormat) {
		this.propertyFormat = propertyFormat;
	}
	/**
	 * @return Returns the scopeFormat.
	 */
	public String getScopeFormat() {
		return scopeFormat;
	}
	/**
	 * @param scopeFormat The scopeFormat to set.
	 */
	public void setScopeFormat(String scopeFormat) {
		this.scopeFormat = scopeFormat;
	}
    /**
     * @return Returns the collectionEllipsis.
     */
    public String getEllipsis() {
        return ellipsis;
    }
    /**
     * @param collectionEllipsis The collectionEllipsis to set.
     */
    public void setEllipsis(String collectionEllipsis) {
        this.ellipsis = collectionEllipsis;
    }
    /**
     * @return Returns the nameEllipsis.
     */
    public String getNameEllipsis() {
        return nameEllipsis;
    }
    /**
     * @param nameEllipsis The nameEllipsis to set.
     */
    public void setNameEllipsis(String nameEllipsis) {
        this.nameEllipsis = nameEllipsis;
    }
    /**
     * @return Returns the propertyEllipsis.
     */
    public String getPropertyEllipsis() {
        return propertyEllipsis;
    }
    /**
     * @param propertyEllipsis The propertyEllipsis to set.
     */
    public void setPropertyEllipsis(String propertyEllipsis) {
        this.propertyEllipsis = propertyEllipsis;
    }
    /**
     * @return Returns the scopeEllipsis.
     */
    public String getScopeEllipsis() {
        return scopeEllipsis;
    }
    /**
     * @param scopeEllipsis The scopeEllipsis to set.
     */
    public void setScopeEllipsis(String scopeEllipsis) {
        this.scopeEllipsis = scopeEllipsis;
    }
	/**
	 * @return Returns the maxCharCount.
	 */
	public int getMaxCharCount() {
		return maxCharCount;
	}
	/**
	 * @param maxCharCount The maxCharCount to set.
	 */
	public void setMaxCharCount(int maxCharCount) {
		this.maxCharCount = maxCharCount;
	}
	/**
	 * @return the mapKey
	 */
	public String getMapKey() {
		return mapKey;
	}
	/**
	 * @param mapKey the mapKey to set
	 */
	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}
	/**
	 * @return the nameMapKey
	 */
	public String getNameMapKey() {
		return nameMapKey;
	}
	/**
	 * @param nameMapKey the nameMapKey to set
	 */
	public void setNameMapKey(String nameMapKey) {
		this.nameMapKey = nameMapKey;
	}
	/**
	 * @return the propertyMapKey
	 */
	public String getPropertyMapKey() {
		return propertyMapKey;
	}
	/**
	 * @param propertyMapKey the propertyMapKey to set
	 */
	public void setPropertyMapKey(String propertyMapKey) {
		this.propertyMapKey = propertyMapKey;
	}
	/**
	 * @return the scopeMapKey
	 */
	public String getScopeMapKey() {
		return scopeMapKey;
	}
	/**
	 * @param scopeMapKey the scopeMapKey to set
	 */
	public void setScopeMapKey(String scopeMapKey) {
		this.scopeMapKey = scopeMapKey;
	}
	/**
	 * @return the nameMaxCharCount
	 */
	public String getNameMaxCharCount() {
		return nameMaxCharCount;
	}
	/**
	 * @param nameMaxCharCount the nameMaxCharCount to set
	 */
	public void setNameMaxCharCount(String nameMaxCharCount) {
		this.nameMaxCharCount = nameMaxCharCount;
	}
	/**
	 * @return the propertyMaxCharCount
	 */
	public String getPropertyMaxCharCount() {
		return propertyMaxCharCount;
	}
	/**
	 * @param propertyMaxCharCount the propertyMaxCharCount to set
	 */
	public void setPropertyMaxCharCount(String propertyMaxCharCount) {
		this.propertyMaxCharCount = propertyMaxCharCount;
	}
	/**
	 * @return the scopeMaxCharCount
	 */
	public String getScopeMaxCharCount() {
		return scopeMaxCharCount;
	}
	/**
	 * @param scopeMaxCharCount the scopeMaxCharCount to set
	 */
	public void setScopeMaxCharCount(String scopeMaxCharCount) {
		this.scopeMaxCharCount = scopeMaxCharCount;
	}
}