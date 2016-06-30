package com.yuanluesoft.jeaf.report.excel.model;

import java.util.HashMap;
import java.util.Map;

import com.yuanluesoft.jeaf.util.StringUtils;


/**
 * Excel数据,报表可以扩展ExcelReportData,添加自己的数据属性
 * @author linchuan
 *
 */
public class ExcelReportData {
	private String referenceRowNumbers; //参考行号
	private boolean fixedRowHeight; //是否固定行高
	private String rowIndex; //行号
	private Map propertyMap = new HashMap();
	
	public ExcelReportData() {
		super();
	}

	public ExcelReportData(String referenceRowNumbers, boolean fixedRowHeight, String rowIndex) {
		super();
		this.referenceRowNumbers = referenceRowNumbers;
		this.fixedRowHeight = fixedRowHeight;
		this.rowIndex = rowIndex;
	}

	/**
	 * 获取中文行号
	 * @return
	 */
	public String getChineseRowIndex() {
		try {
			return StringUtils.getChineseNumber(Integer.parseInt(rowIndex), false);
		}
		catch(Exception e) {
			return rowIndex;
		}
	}
	
	/**
	 * 设置属性值
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setPropertyValue(String propertyName, Object propertyValue) {
		propertyMap.put(propertyName, propertyValue);
	}
	
	/**
	 * 获取属性值
	 * @param propertyName
	 * @return
	 */
	public Object getPropertyValue(String propertyName) {
		return propertyMap.get(propertyName);
	}
	
	/**
	 * 获取属性(数字类型)值
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	public double getPropertyNumberValue(String propertyName, double defaultValue) {
		try {
			return ((Number)getPropertyValue(propertyName)).doubleValue();
		}
		catch(Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 设置属性(数字类型)值
	 * @param propertyName
	 * @param value
	 */
	public void setPropertyNumberValue(String propertyName, double value) {
		setPropertyValue(propertyName, new Double(value));
	}
	
	/**
	 * 增加属性(数字类型)值
	 * @param propertyName
	 * @param addValue
	 * @return
	 */
	public double addPropertyNumberValue(String propertyName, double addValue) {
		double value = getPropertyNumberValue(propertyName, 0);
		value += addValue;
		setPropertyValue(propertyName, new Double(value));
		return value;
	}
	
	/**
	 * @return the fixedRowHeight
	 */
	public boolean isFixedRowHeight() {
		return fixedRowHeight;
	}

	/**
	 * @param fixedRowHeight the fixedRowHeight to set
	 */
	public void setFixedRowHeight(boolean fixedRowHeight) {
		this.fixedRowHeight = fixedRowHeight;
	}

	/**
	 * @return the referenceRowNumbers
	 */
	public String getReferenceRowNumbers() {
		return referenceRowNumbers;
	}

	/**
	 * @param referenceRowNumbers the referenceRowNumbers to set
	 */
	public void setReferenceRowNumbers(String referenceRowNumbers) {
		this.referenceRowNumbers = referenceRowNumbers;
	}

	/**
	 * @return the propertyMap
	 */
	public Map getPropertyMap() {
		return propertyMap;
	}

	/**
	 * @param propertyMap the propertyMap to set
	 */
	public void setPropertyMap(Map propertyMap) {
		this.propertyMap = propertyMap;
	}

	/**
	 * @return the rowIndex
	 */
	public String getRowIndex() {
		return rowIndex;
	}

	/**
	 * @param rowIndex the rowIndex to set
	 */
	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}
}