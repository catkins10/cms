package com.yuanluesoft.jeaf.business.parser;

import java.util.List;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ParseException;

/**
 * 业务逻辑配置解析器
 * @author linchuan
 *
 */
public interface BusinessDefineParser {

	/**
	 * 解析业务逻辑配置
	 * @param applicationName
	 * @param defineFileName
	 * @return
	 * @throws ParseException
	 */
	public List parse(final String applicationName, final String defineFileName) throws ParseException;
	
	/**
	 * 解析字段
	 * @param xmlField
	 * @return
	 * @throws ParseException
	 */
	public Field parseFeild(Element xmlField) throws ParseException;
	
	/**
	 * 保存业务逻辑配置
	 * @param businessObjects
	 * @param defineFileName
	 * @throws ParseException
	 */
	public void saveBusinessDefine(List businessObjects, final String defineFileName) throws ParseException;
	
	/**
	 * 生成字段XML
	 * @param field
	 * @param parentElement
	 * @return
	 * @throws ParseException
	 */
	public Element generateFeildXML(Field field, Element parentElement) throws ParseException;
}