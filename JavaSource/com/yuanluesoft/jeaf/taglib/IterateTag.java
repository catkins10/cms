/*
 * Created on 2004-12-29
 *
 */
package com.yuanluesoft.jeaf.taglib;

import java.util.Collection;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class IterateTag extends org.apache.struts.taglib.logic.IterateTag {
	private DatabaseService databaseService = null;
	private String hql = null;
	//位移
	protected String scopeOffset = null;
	protected String nameOffset = null;
	protected String propertyOffset = null;
	//动态列表名称
	protected String scopeList = null;
	protected String nameList = null;
	protected String propertyList = null;
	
	private String sizeId; //输出记录数
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		try {
			if(name==null) {
				name = Constants.BEAN_KEY;
			}
			if(propertyOffset!=null || nameOffset!=null) {
				if(nameOffset==null) {
					nameOffset = Constants.BEAN_KEY;
				}
				offset = "" + TagUtils.getInstance().lookup(pageContext, nameOffset, propertyOffset, scopeOffset);
			}
			if(hql!=null) { //从数据库中获取记录集合
				if(databaseService==null) {
					WebApplicationContext webApplicationContext =	WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
					databaseService = (DatabaseService)webApplicationContext.getBean("databaseService");
				}
				//替换时间
				hql = hql.replaceAll("\\x7BNOW\\x7D", "'" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + "'");
				//替换用户ID
				hql = hql.replaceAll("\\x7BUSERID\\x7D",  "" + TagUtils.getInstance().lookup(pageContext, "SessionInfo", "userId", "session"));
				collection = databaseService.findRecordsByHql(hql, 0, Integer.parseInt(length));
			}
			else {
				if(nameList!=null || propertyList!=null) { //动态列表属性
					if(nameList==null) {
						nameList = Constants.BEAN_KEY;
					}
					property = "" + TagUtils.getInstance().lookup(pageContext, nameList, propertyList, scopeList);
				}
				try {
					collection = TagUtils.getInstance().lookup(pageContext, name, property, scope);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(collection == null) {
				return SKIP_BODY;
			}
			if(sizeId!=null && !sizeId.equals("")) {
				if(collection instanceof Collection) {
					pageContext.setAttribute(sizeId, new Integer(((Collection)collection).size()));
				}
			}
			return super.doStartTag();
		}
		finally {
			collection = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		scopeOffset = null;
		nameOffset = null;
		propertyOffset = null;
		scopeList = null;
		nameList = null;
		propertyList = null;
		hql = null;
		sizeId = null; //输出记录数
		super.release();
	}
	
	/**
	 * @return Returns the nameOffset.
	 */
	public String getNameOffset() {
		return nameOffset;
	}
	/**
	 * @param nameOffset The nameOffset to set.
	 */
	public void setNameOffset(String nameOffset) {
		this.nameOffset = nameOffset;
	}
	/**
	 * @return Returns the propertyOffset.
	 */
	public String getPropertyOffset() {
		return propertyOffset;
	}
	/**
	 * @param propertyOffset The propertyOffset to set.
	 */
	public void setPropertyOffset(String propertyOffset) {
		this.propertyOffset = propertyOffset;
	}
	/**
	 * @return Returns the scopeOffset.
	 */
	public String getScopeOffset() {
		return scopeOffset;
	}
	/**
	 * @param scopeOffset The scopeOffset to set.
	 */
	public void setScopeOffset(String scopeOffset) {
		this.scopeOffset = scopeOffset;
	}
	
	/**
	 * @return Returns the nameList.
	 */
	public String getNameList() {
		return nameList;
	}
	/**
	 * @param nameList The nameList to set.
	 */
	public void setNameList(String nameList) {
		this.nameList = nameList;
	}
	/**
	 * @return Returns the propertyList.
	 */
	public String getPropertyList() {
		return propertyList;
	}
	/**
	 * @param propertyList The propertyList to set.
	 */
	public void setPropertyList(String propertyList) {
		this.propertyList = propertyList;
	}
	/**
	 * @return Returns the scopeList.
	 */
	public String getScopeList() {
		return scopeList;
	}
	/**
	 * @param scopeList The scopeList to set.
	 */
	public void setScopeList(String scopeList) {
		this.scopeList = scopeList;
	}
	/**
	 * @return Returns the hql.
	 */
	public String getHql() {
		return hql;
	}
	/**
	 * @param hql The hql to set.
	 */
	public void setHql(String hql) {
		this.hql = hql;
	}

	/**
	 * @return the sizeId
	 */
	public String getSizeId() {
		return sizeId;
	}

	/**
	 * @param sizeId the sizeId to set
	 */
	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}
}
