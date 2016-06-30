package com.yuanluesoft.j2oa.info.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 信息采编:刊物配置(info_magazine_define)
 * @author linchuan
 *
 */
public class InfoMagazineDefine extends Record {
	private String name; //名称
	private int hasBrief; //有无简讯
	private int sn; //当前期号
	private int snYear; //编号年度
	private int snTotal; //当前总期号
	private int resetSnByYear; //每年重排期号
	private String columns; //栏目列表,用逗号分隔
	private Set visitors; //分发范围、编辑、主编
	
	/**
	 * 获取下一个期号
	 * @return
	 */
	public int getNextSn() {
		return resetSnByYear==0 ? snTotal + 1 : (snYear==DateTimeUtils.getYear(DateTimeUtils.date()) ? sn + 1 : 1);
	}
	
	/**
	 * 获取刊物名称
	 * @return
	 */
	public String getMagazineName() {
		return name + (resetSnByYear==0 ? "（第" + (snTotal+1) + "期）" : "〔" + DateTimeUtils.getYear(DateTimeUtils.date()) + "〕" + getNextSn() + "期");
	}
	
	/**
	 * @return the columns
	 */
	public String getColumns() {
		return columns;
	}
	/**
	 * @param columns the columns to set
	 */
	public void setColumns(String columns) {
		this.columns = columns;
	}
	/**
	 * @return the hasBrief
	 */
	public int getHasBrief() {
		return hasBrief;
	}
	/**
	 * @param hasBrief the hasBrief to set
	 */
	public void setHasBrief(int hasBrief) {
		this.hasBrief = hasBrief;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the resetSnByYear
	 */
	public int getResetSnByYear() {
		return resetSnByYear;
	}
	/**
	 * @param resetSnByYear the resetSnByYear to set
	 */
	public void setResetSnByYear(int resetSnByYear) {
		this.resetSnByYear = resetSnByYear;
	}
	/**
	 * @return the sn
	 */
	public int getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(int sn) {
		this.sn = sn;
	}
	/**
	 * @return the snTotal
	 */
	public int getSnTotal() {
		return snTotal;
	}
	/**
	 * @param snTotal the snTotal to set
	 */
	public void setSnTotal(int snTotal) {
		this.snTotal = snTotal;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the snYear
	 */
	public int getSnYear() {
		return snYear;
	}
	/**
	 * @param snYear the snYear to set
	 */
	public void setSnYear(int snYear) {
		this.snYear = snYear;
	}
}