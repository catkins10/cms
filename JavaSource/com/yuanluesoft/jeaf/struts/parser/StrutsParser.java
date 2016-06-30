package com.yuanluesoft.jeaf.struts.parser;

import org.apache.struts.config.ModuleConfig;

import com.yuanluesoft.jeaf.exception.ParseException;

/**
 * 
 * @author linchuan
 *
 */
public interface StrutsParser {

	/**
	 * 保存Struts配置
	 * @param moduleConfig
	 * @param defineFileName
	 * @throws ParseException
	 */
	public void saveStrutsModuleConfig(ModuleConfig moduleConfig, String defineFileName) throws ParseException;
}