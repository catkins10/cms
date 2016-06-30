/*
 * Created on 2005-4-7
 *
 */
package com.yuanluesoft.jeaf.view.viewcustomize.parser;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.view.viewcustomize.model.ViewCustom;

/**
 * 
 * @author linchuan
 *
 */
public interface ViewCustomParser {
	
	/**
	 * 解析字节类型的视图定制信息
	 * @param applicationName
	 * @return
	 * @throws ApplicationDefineParseException
	 */
	public ViewCustom parseViewCustom(String viewCustomText) throws ParseException;
	
	/**
	 * 生成字节类型的视图定制信息
	 * @param viewCustom
	 * @return
	 * @throws ParseException
	 */
	public String generateViewCustomXmlText(ViewCustom viewCustom) throws ParseException;
}