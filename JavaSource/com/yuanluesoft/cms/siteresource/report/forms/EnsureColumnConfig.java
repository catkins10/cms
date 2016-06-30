package com.yuanluesoft.cms.siteresource.report.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class EnsureColumnConfig extends ActionForm {
	private long siteId; //站点ID
	private String columnIds; //栏目ID,0/全部栏目,1/其他应保障栏目,2/政府信息公开
	private String columnNames; //栏目名称
	private int mode; //计分方式,0/按信息量,1/按维护栏目数
	private int score; //分数
	private int captureScore; //信息抓取分数,非集群单位时有效
	
	//扩展属性
	private int columnType; //栏目类型,0/全部栏目,1/其他应保障栏目,2/政府信息公开,3/自定义
	
	/**
	 * @return the captureScore
	 */
	public int getCaptureScore() {
		return captureScore;
	}
	/**
	 * @param captureScore the captureScore to set
	 */
	public void setCaptureScore(int captureScore) {
		this.captureScore = captureScore;
	}
	/**
	 * @return the columnIds
	 */
	public String getColumnIds() {
		return columnIds;
	}
	/**
	 * @param columnIds the columnIds to set
	 */
	public void setColumnIds(String columnIds) {
		this.columnIds = columnIds;
	}
	/**
	 * @return the columnNames
	 */
	public String getColumnNames() {
		return columnNames;
	}
	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the columnType
	 */
	public int getColumnType() {
		return columnType;
	}
	/**
	 * @param columnType the columnType to set
	 */
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
}