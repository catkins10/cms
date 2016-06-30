package com.yuanluesoft.j2oa.infocontribute.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 投稿:采用情况(info_contribute_use)
 * @author linchuan
 *
 */
public class InfoContributeUse extends Record {
	private long infoId; //稿件ID
	private long useId; //采用情况ID
	private int level; //报送级别,1/县办,2/市办,3/省办,4/国办
	private Timestamp sendTime; //报送时间
	private Timestamp useTime; //采用时间
	private long magazineId; //采用刊物ID
	private String magazine; //采用刊物名称
	private InfoContribute info; //投稿
	
	/**
	 * @return the infoId
	 */
	public long getInfoId() {
		return infoId;
	}
	/**
	 * @param infoId the infoId to set
	 */
	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	/**
	 * @return the magazine
	 */
	public String getMagazine() {
		return magazine;
	}
	/**
	 * @param magazine the magazine to set
	 */
	public void setMagazine(String magazine) {
		this.magazine = magazine;
	}
	/**
	 * @return the magazineId
	 */
	public long getMagazineId() {
		return magazineId;
	}
	/**
	 * @param magazineId the magazineId to set
	 */
	public void setMagazineId(long magazineId) {
		this.magazineId = magazineId;
	}
	/**
	 * @return the sendTime
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the useTime
	 */
	public Timestamp getUseTime() {
		return useTime;
	}
	/**
	 * @param useTime the useTime to set
	 */
	public void setUseTime(Timestamp useTime) {
		this.useTime = useTime;
	}
	/**
	 * @return the useId
	 */
	public long getUseId() {
		return useId;
	}
	/**
	 * @param useId the useId to set
	 */
	public void setUseId(long useId) {
		this.useId = useId;
	}
	/**
	 * @return the info
	 */
	public InfoContribute getInfo() {
		return info;
	}
	/**
	 * @param info the info to set
	 */
	public void setInfo(InfoContribute info) {
		this.info = info;
	}
}