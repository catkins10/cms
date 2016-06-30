package com.yuanluesoft.jeaf.report.graph;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 图像报表服务
 * @author linchuan
 *
 */
public interface GraphReportService {

	/**
	 * 输出图表
	 * @param chartDataList
	 * @param chartWidth
	 * @param chartHeight
	 * @param chartMode 模式: barChart/柱形图, lineChart/折线图, pieChart/柱形图
	 * @param response
	 * @throws ServiceException
	 */
	public void writeChart(List chartDataList, int chartWidth, int chartHeight, String chartMode, HttpServletResponse response) throws ServiceException;
}