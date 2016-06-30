package com.yuanluesoft.chd.evaluation.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评价体系目录:发电企业主要荣誉(chd_evaluation_plant_honor)
 * @author linchuan
 *
 */
public class ChdEvaluationPlantHonor extends Record {
	private long plantId; //发电企业ID
	private int honorYear; //年度
	private String honor; //荣誉名称
	private String honorAwarding; //授予单位
	private Date honorDate; //授予时间
	/**
	 * @return the honor
	 */
	public String getHonor() {
		return honor;
	}
	/**
	 * @param honor the honor to set
	 */
	public void setHonor(String honor) {
		this.honor = honor;
	}
	/**
	 * @return the honorAwarding
	 */
	public String getHonorAwarding() {
		return honorAwarding;
	}
	/**
	 * @param honorAwarding the honorAwarding to set
	 */
	public void setHonorAwarding(String honorAwarding) {
		this.honorAwarding = honorAwarding;
	}
	/**
	 * @return the honorDate
	 */
	public Date getHonorDate() {
		return honorDate;
	}
	/**
	 * @param honorDate the honorDate to set
	 */
	public void setHonorDate(Date honorDate) {
		this.honorDate = honorDate;
	}
	/**
	 * @return the honorYear
	 */
	public int getHonorYear() {
		return honorYear;
	}
	/**
	 * @param honorYear the honorYear to set
	 */
	public void setHonorYear(int honorYear) {
		this.honorYear = honorYear;
	}
	/**
	 * @return the plantId
	 */
	public long getPlantId() {
		return plantId;
	}
	/**
	 * @param plantId the plantId to set
	 */
	public void setPlantId(long plantId) {
		this.plantId = plantId;
	}
}