package com.yuanluesoft.jeaf.util.callback;

import java.util.List;

/**
 * EXCEL导入回调
 * @author linchuan
 *
 */
public interface ExcelSheetImportCallback {
	
	/**
	 * 根据内容来判断是否表头
	 * @param colNames
	 * @return
	 */
	public boolean isHeader(List rowContents) throws Exception;
	
	/**
	 * 创建一个新记录
	 * @return
	 */
	public Object createNewRecord() throws Exception;
	
	/**
	 * 保存记录
	 * @param record
	 */
	public void saveRecrd(Object record) throws Exception;
	
	/**
	 * 根据列名称设置记录属性
	 * @param record
	 * @param columnName
	 * @param cellContent
	 */
	public void setRecordPropertyValue(Object record, String columnName, String cellContent) throws Exception;
}