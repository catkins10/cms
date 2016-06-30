/*
 * Created on 2005-10-23
 *
 */
package com.yuanluesoft.jeaf.view.parser;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ParseException;

/**
 * 
 * @author linchuan
 *
 */
public interface ViewDefineParser {
	
	/**
	 * 解析视图定义
	 * @param applicationName
	 * @param defineFileName
	 * @return
	 * @throws ViewDefineParseException
	 */
	public abstract List parse(final String applicationName, final String defineFileName) throws ParseException;
	
	/**
	 * 生成视图定义XML文本
	 * @param views
	 * @return
	 * @throws ParseException
	 */
	public abstract void saveViewDefine(List views, String defineFileName) throws ParseException;
}