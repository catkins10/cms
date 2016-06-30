package com.yuanluesoft.chd.evaluation.data.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class PrerequisitesData extends ActionForm {
	private long plantId; //发电企业ID
	private String plant; //发电企业名称
	private int declareYear; //年度
	private List prerequisitesDataList; //必备条件完成情况列表

	/**
	 * @return the declareYear
	 */
	public int getDeclareYear() {
		return declareYear;
	}

	/**
	 * @param declareYear the declareYear to set
	 */
	public void setDeclareYear(int declareYear) {
		this.declareYear = declareYear;
	}

	/**
	 * @return the plant
	 */
	public String getPlant() {
		return plant;
	}

	/**
	 * @param plant the plant to set
	 */
	public void setPlant(String plant) {
		this.plant = plant;
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

	/**
	 * @return the prerequisitesDataList
	 */
	public List getPrerequisitesDataList() {
		return prerequisitesDataList;
	}

	/**
	 * @param prerequisitesDataList the prerequisitesDataList to set
	 */
	public void setPrerequisitesDataList(List prerequisitesDataList) {
		this.prerequisitesDataList = prerequisitesDataList;
	}
}