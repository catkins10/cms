package com.yuanluesoft.dpc.keyproject.report.model;

import com.yuanluesoft.jeaf.report.excel.model.ExcelReport;

/**
 * 项目报表
 * @author linchuan
 *
 */
public class ProjectExcelReport extends ExcelReport {
	private int year; //年
	private int month; //月
	private char tenDay = '1'; //旬
	private String developmentArea; //开发区或开发区分类
	
	/**
	 * 获取上一年
	 * @return
	 */
	public int getLastYear() {
		return year - 1;
	}
	
	/**
	 * 获取上一月
	 * @return
	 */
	public int getLastMonth() {
		return month - 1;
	}
	
	/**
	 * 获取第二个月
	 * @return
	 */
	public int getSecondMonth() {
		return (month % 12) + 1;
	}
	
	/**
	 * 获取第三个月
	 * @return
	 */
	public int getThirdMonth() {
		return (month+1) % 12 + 1;
	}
	
	/**
	 * 获取第四个月
	 * @return
	 */
	public int getFourthMonth() {
		return (month+2) % 12 + 1;
	}
	
	/**
	 * 获取第五个月
	 * @return
	 */
	public int getFifthMonth() {
		return (month+3) % 12 + 1;
	}
	
	/**
	 * 获取旬名称
	 * @return
	 */
	public String getTenDayName() {
		return new String[]{"上旬", "中旬", "下旬"}[tenDay-'1'];
	}
	
	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
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
	 * @return the developmentArea
	 */
	public String getDevelopmentArea() {
		return developmentArea;
	}

	/**
	 * @param developmentArea the developmentArea to set
	 */
	public void setDevelopmentArea(String developmentArea) {
		this.developmentArea = developmentArea;
	}

	/**
	 * @return the tenDay
	 */
	public char getTenDay() {
		return tenDay;
	}

	/**
	 * @param tenDay the tenDay to set
	 */
	public void setTenDay(char tenDay) {
		this.tenDay = tenDay;
	}
}
