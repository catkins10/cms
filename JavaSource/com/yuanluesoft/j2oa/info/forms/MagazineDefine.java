package com.yuanluesoft.j2oa.info.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 * 
 * @author linchuan
 *
 */
public class MagazineDefine extends ActionForm {
	private String name; //名称
	private int hasBrief; //有无简讯
	private int sn; //当前期号
	private int snYear; //编号年度
	private int snTotal; //当前总期号
	private int resetSnByYear; //每年重排期号
	private String columns; //栏目列表,用逗号分隔
	private Set visitors; //分发范围、编辑、主编
	
	//扩展属性
	private RecordVisitorList issueRanges = new RecordVisitorList(); //分发范围
	private RecordVisitorList editors = new RecordVisitorList(); //编辑
	private RecordVisitorList chiefEditors = new RecordVisitorList(); //主编
	
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
	 * @return the chiefEditors
	 */
	public RecordVisitorList getChiefEditors() {
		return chiefEditors;
	}
	/**
	 * @param chiefEditors the chiefEditors to set
	 */
	public void setChiefEditors(RecordVisitorList chiefEditors) {
		this.chiefEditors = chiefEditors;
	}
	/**
	 * @return the issueRanges
	 */
	public RecordVisitorList getIssueRanges() {
		return issueRanges;
	}
	/**
	 * @param issueRanges the issueRanges to set
	 */
	public void setIssueRanges(RecordVisitorList issueRanges) {
		this.issueRanges = issueRanges;
	}
	/**
	 * @return the editors
	 */
	public RecordVisitorList getEditors() {
		return editors;
	}
	/**
	 * @param editors the editors to set
	 */
	public void setEditors(RecordVisitorList editors) {
		this.editors = editors;
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