package com.yuanluesoft.jeaf.database.parser;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ParseException;

/**
 * 
 * @author linchuan
 *
 */
public interface DatabaseDefineParser {
	
	/**
	 * 解析表
	 * @param defineFileName
	 * @return
	 * @throws ParseException
	 */
	public List parseTables(String defineFileName) throws ParseException;
}