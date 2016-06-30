/*
 * Created on 2005-2-13
 *
 */
package com.yuanluesoft.jeaf.taglib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.jasper.runtime.JspRuntimeLibrary;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Tab;
import com.yuanluesoft.jeaf.form.model.TabList;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class TabTag extends BodyTagSupport {
	private String name = null; //BEAN名称
	private String property = null; //属性
	private String scope = null; //范围
	//动态属性名
	private String scopeProperty = null; 
	private String nameProperty = null;
	private String propertyProperty = null;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		//动态属性名
		if(nameProperty!=null || propertyProperty!=null) {
			if(nameProperty==null) {
				nameProperty = Constants.BEAN_KEY;
			}
			property = (String)TagUtils.getInstance().lookup(pageContext, nameProperty, propertyProperty, scopeProperty);
		}
		if(property==null && name==null) {
			property = "tabs";
		}
		if(name==null) {
			name = Constants.BEAN_KEY;
		}
		TabList tabList = (TabList)TagUtils.getInstance().lookup(pageContext, name, property, scope);
		List tabLists = (List)pageContext.getAttribute("tabLists");
		if(tabLists==null) {
			tabLists = new ArrayList();
			pageContext.setAttribute("tabLists", tabLists);
		}
		tabLists.add(tabList);
		pageContext.setAttribute("currentTabList", tabList); //设为当前TAB列表
		JspWriter out = pageContext.getOut();
		if(tabList!=null && tabList.size()>1) { //没有TAB或者只有一个TAB时,不显示TAB栏
			try	{
				out.println("<script>new TabList([");
				for(int i = 0; i < tabList.size(); i++) {
					Tab tab = (Tab)tabList.get(i);
					out.println((i==0 ? "" : ",") + "{id:\"" + tab.getId() + "\"" + (tab.isSelected() ? ",selected:true" : "") + ",name:\"" + tab.getName() + "\"}");
				}
				out.println("]);</script>");
			}
			catch (Exception e)	{
				Logger.exception(e);
				throw new JspTagException("Tag Error:" + e.toString());
			}
		}
		//输出引用JSP页面的TAB
		for(Iterator iterator = (tabList==null ? null : tabList.iterator()); iterator!=null && iterator.hasNext();) {
			Tab tab = (Tab)iterator.next();
			if(tab.getJspFile()==null) {
				continue;
			}
			try {
				out.println("<div id=\"tab" + tab.getId() + "\"" + (tab.isSelected() || tabList.size()==1 ? "" : " style=\"display:none\"") + ">");
				for(Iterator iteratorAttribute = tab.getAttributes()==null ? null : tab.getAttributes().keySet().iterator(); iteratorAttribute!=null && iteratorAttribute.hasNext();) {
					String attributeName = (String)iteratorAttribute.next();
					pageContext.getRequest().setAttribute(attributeName, tab.getAttribute(attributeName));
				}
				String jspFile = tab.getJspFile();
				if("SUBFORM".equals(jspFile)) {
					ActionForm form = (ActionForm)TagUtils.getInstance().lookup(pageContext, Constants.BEAN_KEY, null, scope);
					jspFile = form.getSubForm();
				}
				JspRuntimeLibrary.include(pageContext.getRequest(), pageContext.getResponse(), jspFile, pageContext.getOut(), false);
				out.println("</div>");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		//让JSP继续处理以下页面的内容
		return EVAL_BODY_INCLUDE;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {
		List tabLists = (List)pageContext.getAttribute("tabLists");
		if(tabLists.size()==1) {
			pageContext.removeAttribute("tabLists");
		}
		else {
			tabLists.remove(tabLists.size()-1); //删除
			pageContext.setAttribute("currentTabList", tabLists.get(tabLists.size()-1)); //设为当前TAB列表
		}
		return super.doEndTag();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	public void release() {
		super.release();
		name = null; //BEAN名称
		property = null; //属性
		scope = null; //范围
		//动态属性名
		scopeProperty = null; 
		nameProperty = null;
		propertyProperty = null;
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
}