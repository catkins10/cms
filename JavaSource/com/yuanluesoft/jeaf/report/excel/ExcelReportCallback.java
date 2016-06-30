package com.yuanluesoft.jeaf.report.excel;

import com.yuanluesoft.jeaf.report.excel.model.ExcelReport;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReportData;

/**
 * 
 * @author linchuan
 *
 */
public interface ExcelReportCallback {

	/**
	 * 根据Excel页名称获取报表
	 * @param sheetName
	 * @return
	 */
	public ExcelReport getExcelReport(String sheetName);
	
	/**
	 * 根据Excel现有的一行数据,获取需要输出的数据
	 * @param rowNumner
	 * @param rowContents
	 * @return
	 */
	public ExcelReportData getExcelReportData(int rowNumner, String[] rowContents);
}