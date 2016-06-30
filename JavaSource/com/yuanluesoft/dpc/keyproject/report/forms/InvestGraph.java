package com.yuanluesoft.dpc.keyproject.report.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class InvestGraph extends ActionForm {
	private int year;
	private String graphBy; //industry、area、level、investmentSubject
	private String graphByTitle; //按行业、建设地、项目级别、投资主体
	private String graphType; //总体投资分析、年度计划投资分析以及投资完成分析
	private int width;
	private int height;
	
	/**
	 * @return the graphBy
	 */
	public String getGraphBy() {
		return graphBy;
	}
	/**
	 * @param graphBy the graphBy to set
	 */
	public void setGraphBy(String graphBy) {
		this.graphBy = graphBy;
	}
	/**
	 * @return the graphType
	 */
	public String getGraphType() {
		return graphType;
	}
	/**
	 * @param graphType the graphType to set
	 */
	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the graphByTitle
	 */
	public String getGraphByTitle() {
		return graphByTitle;
	}
	/**
	 * @param graphByTitle the graphByTitle to set
	 */
	public void setGraphByTitle(String graphByTitle) {
		this.graphByTitle = graphByTitle;
	}
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
}