package com.yuanluesoft.jeaf.report.graph.jfreechart;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.report.graph.GraphReportService;
import com.yuanluesoft.jeaf.report.graph.model.ChartData;
import com.yuanluesoft.jeaf.util.Mime;

/**
 * 
 * @author linchuan
 *
 */
public class GraphReportServiceImpl implements GraphReportService {
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.report.graph.GraphReportService#writeChart(java.util.List, int, int, java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void writeChart(List chartDataList, int chartWidth, int chartHeight, String chartMode, HttpServletResponse response) throws ServiceException {
		JFreeChart chart = null;
		if("barChart".equals(chartMode)) { //柱形图
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for(Iterator iterator=chartDataList==null ? null : chartDataList.iterator(); iterator!=null && iterator.hasNext();) {
				ChartData chartData = (ChartData)iterator.next();
				dataset.addValue(chartData.getValue(), "", chartData.getName());
			}
			chart = ChartFactory.createBarChart3D(
								"", // 图表标题
								"", // 目录轴的显示标签
								"", // 数值轴的显示标签
								dataset, // 数据集
								PlotOrientation.VERTICAL, // 图表方向：水平、垂直
								false, 	// 是否显示图例(对于简单的柱状图必须是false)
								false, 	// 是否生成工具
								false 	// 是否生成URL链接
			);
			CategoryPlot plot = (CategoryPlot)chart.getPlot();
			CategoryAxis axis = plot.getDomainAxis();    
			axis.setLabelFont(new Font("宋体",Font.PLAIN,12));
			axis.setTickLabelFont(new Font("宋体",Font.PLAIN,12));// 轴数值 
			axis.setMaximumCategoryLabelLines(4); // 这边的4代表最多显示4行字 
			BarRenderer customBarRenderer =  (BarRenderer)plot.getRenderer();
			customBarRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值 
			customBarRenderer.setBaseItemLabelsVisible(true); 
			customBarRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER)); 
			customBarRenderer.setSeriesPaint(0, new Color(73, 138, 243)); //设置颜色
			customBarRenderer.setItemLabelAnchorOffset(10D); // 设置柱形图上的文字偏离值
			//customBarRenderer.setItemLabelsVisible(true); 
		}
		else if("lineChart".equals(chartMode)) { //折线图
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for(Iterator iterator=chartDataList==null ? null : chartDataList.iterator(); iterator!=null && iterator.hasNext();) {
				ChartData chartData = (ChartData)iterator.next();
				dataset.addValue(chartData.getValue(), "", chartData.getName());
			}
			chart = ChartFactory.createLineChart3D(
								"", // 图表标题
								"", // 目录轴的显示标签
								"", // 数值轴的显示标签
								dataset, // 数据集
								PlotOrientation.VERTICAL, // 图表方向：水平、垂直
								false, 	// 是否显示图例(对于简单的柱状图必须是false)
								false, 	// 是否生成工具
								false 	// 是否生成URL链接
			);
			CategoryPlot plot = (CategoryPlot)chart.getPlot();
			CategoryAxis axis = plot.getDomainAxis();    
			axis.setLabelFont(new Font("宋体",Font.PLAIN,12));
			axis.setTickLabelFont(new Font("宋体",Font.PLAIN,12));// 轴数值 
			axis.setMaximumCategoryLabelLines(4); // 这边的4代表最多显示4行字 
			LineRenderer3D lineRenderer =  (LineRenderer3D)plot.getRenderer();
			lineRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值 
			lineRenderer.setBaseItemLabelsVisible(true); 
			lineRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER)); 
			lineRenderer.setSeriesPaint(0, new Color(73, 138, 243)); //设置颜色
			//lineRenderer.setSeriesStroke(0, new BasicStroke(2.0F)); //改变宽度
			//lineRenderer.setItemLabelAnchorOffset(10D); // 设置柱形图上的文字偏离值
			//customBarRenderer.setItemLabelsVisible(true); 
		}
		else if("pieChart".equals(chartMode)) { //柱形图
			DefaultPieDataset dataset = new DefaultPieDataset();
			for(Iterator iterator=chartDataList==null ? null : chartDataList.iterator(); iterator!=null && iterator.hasNext();) {
				ChartData chartData = (ChartData)iterator.next();
				dataset.setValue(chartData.getName(), chartData.getValue());
			}
			chart = ChartFactory.createPieChart("", dataset, false, false, false);
			//输出饼图
			PiePlot plot = (PiePlot)chart.getPlot();
	        //设置扇区标签显示格式：关键字：值(百分比)   
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}：{1}({2})"));   
	        //设置扇区标签颜色
			plot.setLabelBackgroundPaint(new Color(220, 220, 220));   
			plot.setLabelFont((new Font("宋体", Font.PLAIN, 12)));
		}
		//生成图片
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		try {
			response.setContentType(Mime.MIME_PNG);
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, chartWidth, chartHeight, null);
		}
		catch(SocketException e) {
			
		}
		catch(IOException e) {
			
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
}