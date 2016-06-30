/*
 * Created on 2004-12-19
 *
 */
package com.yuanluesoft.jeaf.form.parser;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ParseException;

/**
 * 
 * @author linchuan
 *
 */
public interface FormDefineParser {
	
	/**
	 * 从配置文件解析表单定义列表
	 * @param formClassName
	 * @return
	 * @throws FormDefineParseException
	 */
	public abstract List parse(final String applicationName, final String defineFileName) throws ParseException;
	
	/**
	 * 保存表单定义
	 * @param forms
	 * @param defineFileName
	 * @throws ParseException
	 */
	public abstract void saveFormDefine(List forms, String defineFileName) throws ParseException;
}