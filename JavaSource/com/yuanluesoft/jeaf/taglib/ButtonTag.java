package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ButtonTag extends BodyTagSupport {
	private String styleName = null; //样式名称,默认为"imgButton"
	private String name; //按钮名称
	private String onclick; //点击时执行的脚本
	private String width; //宽度
	
	//动态按钮名称
	private String scopeName = null; 
	private String nameName = null;
	private String propertyName = null;
	
	//动态点击时执行的脚本
	private String scopeOnclick = null; 
	private String nameOnclick = null;
	private String propertyOnclick = null;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		String styleName = (this.styleName==null ? "imgButton" : this.styleName);
		//动态按钮名称
		if(nameName!=null || propertyName!=null) {
			if(nameName==null) {
				nameName = Constants.BEAN_KEY;
			}
			name = (String)TagUtils.getInstance().lookup(pageContext, nameName, propertyName, scopeName);
		}
		//动态点击时执行的脚本
		if(nameOnclick!=null || propertyOnclick!=null) {
			if(nameOnclick==null) {
				nameOnclick = Constants.BEAN_KEY;
			}
			onclick = (String)TagUtils.getInstance().lookup(pageContext, nameOnclick, propertyOnclick, scopeOnclick);
		}
		String html = "<span onclick=\"" + onclick + "\" id=\"imageButton\" title=\"" + name + "\" onmouseover=\"for(var i=0; i<3; i++)childNodes[i].className=childNodes[i].className.replace('Normal', 'Over');\" onmouseout=\"for(var i=0; i<3; i++)childNodeschildNodes[i].className=childNodes[i].className.replace('Over', 'Normal');\">" +
					  "<span style=\"display:inline-block;\" class=\"" + styleName + "LeftNormal\">&nbsp;</span>" +
					  "<span style=\"display:inline-block;text-align:center;width:" + (width==null ? (name.length()*12 + 20) + "px" : width)  + "\" class=\"" + styleName + "MiddleNormal\">" + name + "</span>" +
					  "<span style=\"display:inline-block;\" class=\"" + styleName + "RightNormal\">&nbsp;</span>" +
					  "</span>";
		TagUtils.getInstance().write(this.pageContext, html);
		return (EVAL_BODY_AGAIN );
	}
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
		styleName = null; //选择按钮的样式类
		name = null; //选择按钮的样式
		onclick = null;
		width = null;
		
		//动态按钮名称
		scopeName = null; 
		nameName = null;
		propertyName = null;
		
		//动态点击时执行的脚本
		scopeOnclick = null; 
		nameOnclick = null;
		propertyOnclick = null;
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
	 * @return the onclick
	 */
	public String getOnclick() {
		return onclick;
	}
	/**
	 * @param onclick the onclick to set
	 */
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	/**
	 * @return the styleName
	 */
	public String getStyleName() {
		return styleName;
	}
	/**
	 * @param styleName the styleName to set
	 */
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * @return the nameName
	 */
	public String getNameName() {
		return nameName;
	}
	/**
	 * @param nameName the nameName to set
	 */
	public void setNameName(String nameName) {
		this.nameName = nameName;
	}
	/**
	 * @return the nameOnclick
	 */
	public String getNameOnclick() {
		return nameOnclick;
	}
	/**
	 * @param nameOnclick the nameOnclick to set
	 */
	public void setNameOnclick(String nameOnclick) {
		this.nameOnclick = nameOnclick;
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
	 * @return the propertyOnclick
	 */
	public String getPropertyOnclick() {
		return propertyOnclick;
	}
	/**
	 * @param propertyOnclick the propertyOnclick to set
	 */
	public void setPropertyOnclick(String propertyOnclick) {
		this.propertyOnclick = propertyOnclick;
	}
	/**
	 * @return the scopeName
	 */
	public String getScopeName() {
		return scopeName;
	}
	/**
	 * @param scopeName the scopeName to set
	 */
	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}
	/**
	 * @return the scopeOnclick
	 */
	public String getScopeOnclick() {
		return scopeOnclick;
	}
	/**
	 * @param scopeOnclick the scopeOnclick to set
	 */
	public void setScopeOnclick(String scopeOnclick) {
		this.scopeOnclick = scopeOnclick;
	}
}
