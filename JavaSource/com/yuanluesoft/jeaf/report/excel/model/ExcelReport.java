package com.yuanluesoft.jeaf.report.excel.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel数据集合,报表需要继承ExcelReport,添加自己的属性,用来填充报表表头
 * @author linchuan
 *
 */
public class ExcelReport {
	private String headRowNumbers; //表头行号列表,如:0,1
	private String referenceRowNumbers; //格式参考行号列表,如:2,3
	private int dataRowNumber; //数据输出的行号
	private Collection dataSet; //数据列表
	private Map propertyMap = new HashMap();
	
	public ExcelReport() {
		super();
	}

	public ExcelReport(String headRowNumbers, String referenceRowNumbers, int dataRowNumber) {
		super();
		this.headRowNumbers = headRowNumbers;
		this.referenceRowNumbers = referenceRowNumbers;
		this.dataRowNumber = dataRowNumber;
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
	 * 添加报表数据,index==-1时加入到最后
	 * @param index
	 * @param reportData
	 */
	public void addReportData(int index, ExcelReportData reportData) {
		if(dataSet==null) {
			dataSet = new ArrayList();
		}
		if(index==-1 || !(dataSet instanceof List)) {
			dataSet.add(reportData);
		}
		else {
			((List)dataSet).add(index, reportData);
		}
	}
	
	/**
	 * @return the dataSet
	 */
	public Collection getDataSet() {
		return dataSet;
	}
	/**
	 * @param dataSet the dataSet to set
	 */
	public void setDataSet(Collection dataSet) {
		this.dataSet = dataSet;
	}
	/**
	 * @return the headRowNumbers
	 */
	public String getHeadRowNumbers() {
		return headRowNumbers;
	}
	/**
	 * @param headRowNumbers the headRowNumbers to set
	 */
	public void setHeadRowNumbers(String headRowNumbers) {
		this.headRowNumbers = headRowNumbers;
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
	 * @return the dataRowNumber
	 */
	public int getDataRowNumber() {
		return dataRowNumber;
	}
	/**
	 * @param dataRowNumber the dataRowNumber to set
	 */
	public void setDataRowNumber(int dataRowNumber) {
		this.dataRowNumber = dataRowNumber;
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
}