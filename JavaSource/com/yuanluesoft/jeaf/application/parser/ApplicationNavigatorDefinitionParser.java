package com.yuanluesoft.jeaf.application.parser;

import com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition;
import com.yuanluesoft.jeaf.exception.ParseException;

/**
 * 
 * @author linchuan
 *
 */
public interface ApplicationNavigatorDefinitionParser {

	/**
	 * 根据定义文件解析导航定义
	 * @param applicationName
	 * @return
	 * @throws ApplicationDefineParseException
	 */
	public ApplicationNavigatorDefinition parse(final String defineFileName) throws ParseException;
	
	/**
	 * 保存应用导航定义
	 * @param navigatorDefinition
	 * @param defineFileName
	 * @throws ParseException
	 */
	public void saveApplicationNavigatorDefinition(ApplicationNavigatorDefinition navigatorDefinition, String defineFileName) throws ParseException;
}