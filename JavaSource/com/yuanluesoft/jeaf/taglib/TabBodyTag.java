/*
 * Created on 2005-12-15
 *
 */
package com.yuanluesoft.jeaf.taglib;

import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.form.model.Tab;
import com.yuanluesoft.jeaf.form.model.TabList;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * TAB主体
 * @author linchuan
 *
 */
public class TabBodyTag extends BodyTagSupport {
	private String tabId = null;
	//动态TABID
	private String scopeTabId = null; 
	private String nameTabId = null;
	private String propertyTabId = null;
	
	//私有属性
	private Tab tab;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		try {
			TabList tabList = (TabList)pageContext.getAttribute("currentTabList");
			JspWriter out = pageContext.getOut();
			if(tabList==null) {
				out.println("<div>");
			}
			else {
				//动态TABID
				if(nameTabId!=null || propertyTabId!=null) {
					if(nameTabId==null) {
						nameTabId = Constants.BEAN_KEY;
					}
					tabId = (String)TagUtils.getInstance().lookup(pageContext, nameTabId, propertyTabId, scopeTabId);
				}
				tab = (Tab)ListUtils.findObjectByProperty(tabList, "id", tabId);
				if(tab==null) {
					return SKIP_BODY;
				}
				for(Iterator iteratorAttribute = tab.getAttributes()==null ? null : tab.getAttributes().keySet().iterator(); iteratorAttribute!=null && iteratorAttribute.hasNext();) {
					String attributeName = (String)iteratorAttribute.next();
					pageContext.getRequest().setAttribute(attributeName, tab.getAttribute(attributeName));
				}
				out.println("<div id=\"tab" + tab.getId() + "\"" + (tab.isSelected() || tabList.size()==1 ? "" : " style=\"display:none\"") + ">");
			}
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new JspTagException("Tag Error:" + e.toString());
		}
	 	//让JSP继续处理以下页面的内容
		return EVAL_BODY_INCLUDE;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			out.println("</div>");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
		tabId = null;
		scopeTabId = null; 
		nameTabId = null;
		propertyTabId = null;
		tab = null;
	}
	
	/**
	 * @return the tabId
	 */
	public String getTabId() {
		return tabId;
	}
	
	/**
	 * @param tabId the tabId to set
	 */
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}

	/**
	 * @return the nameTabId
	 */
	public String getNameTabId() {
		return nameTabId;
	}

	/**
	 * @param nameTabId the nameTabId to set
	 */
	public void setNameTabId(String nameTabId) {
		this.nameTabId = nameTabId;
	}

	/**
	 * @return the propertyTabId
	 */
	public String getPropertyTabId() {
		return propertyTabId;
	}

	/**
	 * @param propertyTabId the propertyTabId to set
	 */
	public void setPropertyTabId(String propertyTabId) {
		this.propertyTabId = propertyTabId;
	}

	/**
	 * @return the scopeTabId
	 */
	public String getScopeTabId() {
		return scopeTabId;
	}

	/**
	 * @param scopeTabId the scopeTabId to set
	 */
	public void setScopeTabId(String scopeTabId) {
		this.scopeTabId = scopeTabId;
	}
}