/*
 * Created on 2006-8-31
 *
 */
package com.yuanluesoft.jeaf.view.model.search;

import com.yuanluesoft.jeaf.base.model.CloneableObject;


/**
 * 
 * @author linchuan
 *
 */
public class Condition extends CloneableObject {
	//条件连接方式
	public static final String CONDITION_LINK_MODE_OR = "or";
	public static final String CONDITION_LINK_MODE_AND = "and";
	//条件表达式
	public static final String CONDITION_EXPRESSION_EQUAL = "equal"; //等于
	public static final String CONDITION_EXPRESSION_NOT_EQUAL = "not equal"; //不等于
	public static final String CONDITION_EXPRESSION_LESS_THAN = "less than"; //小于
	public static final String CONDITION_EXPRESSION_LESS_THAN_OR_EQUAL = "less than or equal"; //小等于
	public static final String CONDITION_EXPRESSION_GREATER_THAN = "greater than"; //大于
	public static final String CONDITION_EXPRESSION_GREATER_THAN_OR_EQUAL = "greater than or equal"; //大等于
	public static final String CONDITION_EXPRESSION_BETWEEN = "between"; //介于
	public static final String CONDITION_EXPRESSION_NOT_BETWEEN = "not between"; //不介于
	public static final String CONDITION_EXPRESSION_CONTAIN = "contain"; //包含
	public static final String CONDITION_EXPRESSION_NOT_CONTAIN = "not contain"; //不包含
	public static final String CONDITION_EXPRESSION_MEMBER = "member"; //属于
	public static final String CONDITION_EXPRESSION_NOT_MEMBER = "not member"; //不属于
	public static final String CONDITION_EXPRESSION_KEY = "key"; //按关键字查询
	public static final String CONDITION_EXPRESSION_HQL = "hql"; //自定义HQL
	
    private String linkMode; //连接方式
    private String fieldName; //字段名称
    private String fieldType; //字段类型
    private String expression; //比较方式
    private String value1; //比较值1
    private String title1; //比较值1标题
    private String value2; //比较值2
    private String title2; //比较值2标题
    
	public Condition() {
		super();
	}
    
    public Condition(String linkMode, String fieldName, String fieldType, String expression, String value1, String value2) {
		super();
		this.linkMode = linkMode;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.expression = expression;
		this.value1 = value1;
		this.value2 = value2;
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
     * @return Returns the fieldName.
     */
    public String getFieldName() {
        return fieldName;
    }
    /**
     * @param fieldName The fieldName to set.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    /**
     * @return Returns the fieldType.
     */
    public String getFieldType() {
        return fieldType;
    }
    /**
     * @param fieldType The fieldType to set.
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    /**
     * @return Returns the linkMode.
     */
    public String getLinkMode() {
        return linkMode;
    }
    /**
     * @param linkMode The linkMode to set.
     */
    public void setLinkMode(String linkMode) {
        this.linkMode = linkMode;
    }
    /**
     * @return Returns the value1.
     */
    public String getValue1() {
        return value1;
    }
    /**
     * @param value1 The value1 to set.
     */
    public void setValue1(String value1) {
        this.value1 = value1;
    }
    /**
     * @return Returns the value2.
     */
    public String getValue2() {
        return value2;
    }
    /**
     * @param value2 The value2 to set.
     */
    public void setValue2(String value2) {
        this.value2 = value2;
    }
    /**
     * @return Returns the title1.
     */
    public String getTitle1() {
        return title1;
    }
    /**
     * @param title1 The title1 to set.
     */
    public void setTitle1(String title1) {
        this.title1 = title1;
    }
    /**
     * @return Returns the title2.
     */
    public String getTitle2() {
        return title2;
    }
    /**
     * @param title2 The title2 to set.
     */
    public void setTitle2(String title2) {
        this.title2 = title2;
    }
}