package com.yuanluesoft.jeaf.view.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.calendarview.model.CalendarView;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.util.HighLightCallback;
import com.yuanluesoft.jeaf.view.util.ViewUtils;

/**
 * 视图字段输出标签
 * @author linchuan
 *
 */
public class ViewFieldTag extends BodyTagSupport {
	//视图包属性
	private String nameViewPackage = null;
	private String propertyViewPackage = null;
	private String scopeViewPackage = null;
	
	private String nameColumn = null; //列
	private String nameRecord = null; //记录
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
		//获取视图包
		ViewPackage viewPackage = (ViewPackage)TagUtils.getInstance().lookup(pageContext, (nameViewPackage==null ? Constants.BEAN_KEY : nameViewPackage), propertyViewPackage, scopeViewPackage);
		//获取列
		Column column = (Column)TagUtils.getInstance().lookup(pageContext, nameColumn, null, null);
		//获取记录
		Object record = TagUtils.getInstance().lookup(pageContext, nameRecord, null, null);
		//输出
		String output = null;
		if(Column.COLUMN_TYPE_SELECT.equals(column.getType())) { //选择列
			try {
				output = "<input onclick=\"selectRecord('" + propertyViewPackage + "', checked, value)\" name=\"" + propertyViewPackage + ".select\" type=\"checkbox\" class=\"checkbox\" value=\"" + PropertyUtils.getProperty(record, (column.getName()==null ? "id" : column.getName())) + "\">";
			}
			catch (Exception e) {
				
			}
		}
		else {
			if(Column.COLUMN_TYPE_ROWNUM.equals(column.getType())) { //行号
				output = "" + viewPackage.getRowNum();
			}
			else { //其他字段
				output = ViewUtils.getViewFieldValue(viewPackage.getView(), column, record, true, (HttpServletRequest)pageContext.getRequest());
				if(column.isHideZero() && "0".equals(output)) { //隐藏"0"
					output = "";
				}
				String title = null;
				if(column.getMaxCharCount()>0) { //有字数限制
					String slice = StringUtils.slice(output, column.getMaxCharCount(), column.getEllipsis());
					if(output.length()>500 || slice.length()==output.length()) {
						output = slice;
					}
					else {
						title = output;
						output = slice;
					}
				}
				if((viewPackage.getView() instanceof CalendarView) && !output.startsWith("<span")) { //日历视图
					title = output;
				}

				//加亮搜索条件
				if(viewPackage.getSearchConditionList()!=null) {
					Field fieldDefine = FieldUtils.getRecordField(record.getClass().getName(), column.getName(), (HttpServletRequest)pageContext.getRequest());
					if(fieldDefine!=null && "string,html".indexOf(fieldDefine.getType())!=-1) {
						final StringBuffer buffer = new StringBuffer(); 
						boolean changed = ViewUtils.writeHighLightKeyWords(viewPackage.getSearchConditionList(), column.getName(), output, new HighLightCallback() {
							public void write(String text, boolean highLight) {
								buffer.append(highLight ? "<span class=\"highlight\">" + text + "</span>" : text);
							}
						});
						if(changed) {
							output = buffer.toString();
						}
					}
				}
				
				if(title!=null) {
					output = "<span title=\"" + title + "\">" + output + "</span>";
				}
			}
			if(column.getLink()!=null) {
				output = "<a" + (column.getLink().getTarget()==null ? "" : " target=\"" + column.getLink().getTarget() + "\"") + " href=\"" + StringUtils.fillParameters(column.getLink().getUrl(), true, false, false, "utf-8", record, (HttpServletRequest)pageContext.getRequest(), null) + "\">" + output + "</a>";
			}
		}
		TagUtils.getInstance().write(pageContext, output);
		return SKIP_BODY;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	public void release() {
		super.release();
		nameViewPackage = null;
		propertyViewPackage = null;
		scopeViewPackage = null;
		nameColumn = null; //列
		nameRecord = null; //记录
	}

	/**
	 * @return the nameColumn
	 */
	public String getNameColumn() {
		return nameColumn;
	}
	/**
	 * @param nameColumn the nameColumn to set
	 */
	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}
	/**
	 * @return the nameRecord
	 */
	public String getNameRecord() {
		return nameRecord;
	}
	/**
	 * @param nameRecord the nameRecord to set
	 */
	public void setNameRecord(String nameRecord) {
		this.nameRecord = nameRecord;
	}
	/**
	 * @return the nameViewPackage
	 */
	public String getNameViewPackage() {
		return nameViewPackage;
	}
	/**
	 * @param nameViewPackage the nameViewPackage to set
	 */
	public void setNameViewPackage(String nameViewPackage) {
		this.nameViewPackage = nameViewPackage;
	}
	/**
	 * @return the propertyViewPackage
	 */
	public String getPropertyViewPackage() {
		return propertyViewPackage;
	}
	/**
	 * @param propertyViewPackage the propertyViewPackage to set
	 */
	public void setPropertyViewPackage(String propertyViewPackage) {
		this.propertyViewPackage = propertyViewPackage;
	}
	/**
	 * @return the scopeViewPackage
	 */
	public String getScopeViewPackage() {
		return scopeViewPackage;
	}
	/**
	 * @param scopeViewPackage the scopeViewPackage to set
	 */
	public void setScopeViewPackage(String scopeViewPackage) {
		this.scopeViewPackage = scopeViewPackage;
	}
}
