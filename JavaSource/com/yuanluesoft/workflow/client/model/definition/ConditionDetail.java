/*
 * Created on 2005-1-18
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import com.yuanluesoft.jeaf.base.model.CloneableObject;
import com.yuanluesoft.workflow.client.model.resource.Application;
import com.yuanluesoft.workflow.client.model.resource.DataField;
import com.yuanluesoft.workflow.client.model.resource.FormalParameter;

/**
 * 
 * @author linchuan
 *
 */
public class ConditionDetail extends CloneableObject {
	private String mode; //连接模式,AND/OR
	private DataField dataField; //字段
	private Application application; //过程
	private FormalParameter formalParameter; //过程返回参数
	private String expression; //比较方式
	private String compareValue1;
	private String compareValue2;
	
	/**
	 * 获取显示标题
	 * @return
	 */
	public String getTitle() {
		String type = null;
		if(dataField != null) {
			type = dataField.getType();
		}
		else if(formalParameter!=null) {
			type = formalParameter.getType();
		}
		else {
			return "条件已失效";
		}
		String title = "OR".equals(mode) ? "或 " : "与 ";
		//title += "[" + dataField.getName() + "]";
		title += dataField==null ? (formalParameter.getDescription()==null ? formalParameter.getId():formalParameter.getDescription()) : dataField.getName();
		if("equal".equals(expression)) {
			title += "等于";
		}
		else if("not equal".equals(expression)) {
			title += "不等于";
		}
		else if("less than".equals(expression)) {
			title += "DATETIME".equals(type) ? "早于":"小于";
		}
		else if("greater than".equals(expression)) {
			title += "DATETIME".equals(type) ? "晚于":"大于";
		}
		else if("between".equals(expression)) {
			title += "介于";
		}
		else if("not between".equals(expression)) {
			title += "不介于";
		}
		else if("contain".equals(expression)) {
			title += "包含";
		}
		else if("not contain".equals(expression)) {
			title += "不包含";
		}
		else if("member".equals(expression)) {
			title += "属于";
		}
		else if("not member".equals(expression)) {
			title += "不属于";
		}
		if(compareValue1==null) {
			title += "空";
		}
		else {
			if("BOOLEAN".equals(type)) {
				if(dataField!=null) {
					if(compareValue1.equals("true")) {
						title += (dataField.getTrueTitle()==null ? "true":dataField.getTrueTitle());
					}
					else {
						title += (dataField.getFalseTitle()==null ? "false":dataField.getFalseTitle());
					}
				}
				else if(formalParameter!=null) {
					if(compareValue1.equals("true")) {
						title += (formalParameter.getTrueTitle()==null ? "true":formalParameter.getTrueTitle());
					}
					else {
						title += (formalParameter.getFalseTitle()==null ? "false":formalParameter.getFalseTitle());
					}
				}
				else {
					title += compareValue1;
				}
			}
			else {
				title += compareValue1;
			}
		}
		if("between".equals(expression) || "not between".equals(expression)) {
			title += "和";
			if(compareValue2==null) {
				title += "空";
			}
			else {
				title += compareValue2;
			}
			title += "之间";
		}
		return title;
	}
	/**
	 * 获取字段ID
	 * @return
	 */
	public String getField() {
		return dataField==null ? (formalParameter==null ? null:formalParameter.getId()):dataField.getId();
	}
	/**
	 * 获取过程ID
	 * @return
	 */
	public String getApplicationId() {
		return application==null ? null:application.getId();
	}
	/**
	 * @return Returns the compareValue1.
	 */
	public String getCompareValue1() {
		return compareValue1;
	}
	/**
	 * @param compareValue1 The compareValue1 to set.
	 */
	public void setCompareValue1(String compareValue1) {
		this.compareValue1 = compareValue1;
	}
	/**
	 * @return Returns the compareValue2.
	 */
	public String getCompareValue2() {
		return compareValue2;
	}
	/**
	 * @param compareValue2 The compareValue2 to set.
	 */
	public void setCompareValue2(String compareValue2) {
		this.compareValue2 = compareValue2;
	}
	/**
	 * @return Returns the dataField.
	 */
	public DataField getDataField() {
		return dataField;
	}
	/**
	 * @param dataField The dataField to set.
	 */
	public void setDataField(DataField dataField) {
		this.dataField = dataField;
	}
	/**
	 * @return Returns the expression.
	 */
	public String getExpression() {
		return expression;
	}
	/**
	 * @param expression The expression to set.
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	/**
	 * @return Returns the application.
	 */
	public Application getApplication() {
		return application;
	}
	/**
	 * @param application The application to set.
	 */
	public void setApplication(Application application) {
		this.application = application;
	}
	/**
	 * @return Returns the formalParameter.
	 */
	public FormalParameter getFormalParameter() {
		return formalParameter;
	}
	/**
	 * @param formalParameter The formalParameter to set.
	 */
	public void setFormalParameter(FormalParameter formalParameter) {
		this.formalParameter = formalParameter;
	}
}
