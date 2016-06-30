package com.yuanluesoft.j2oa.info.forms;


/**
 * 
 * @author linchuan
 *
 */
public class InfoFilter extends InfoReceive {
	private String[] magazineIds; //预选刊型ID列表
	private String level; //采用级别,普通 优先
	private int isBrief; //是否简讯
	private int isVerified; //是否核实
	private int isCircular; //是否通报

	/**
	 * @return the isBrief
	 */
	public int getIsBrief() {
		return isBrief;
	}

	/**
	 * @param isBrief the isBrief to set
	 */
	public void setIsBrief(int isBrief) {
		this.isBrief = isBrief;
	}

	/**
	 * @return the isCircular
	 */
	public int getIsCircular() {
		return isCircular;
	}

	/**
	 * @param isCircular the isCircular to set
	 */
	public void setIsCircular(int isCircular) {
		this.isCircular = isCircular;
	}

	/**
	 * @return the isVerified
	 */
	public int getIsVerified() {
		return isVerified;
	}

	/**
	 * @param isVerified the isVerified to set
	 */
	public void setIsVerified(int isVerified) {
		this.isVerified = isVerified;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the magazineIds
	 */
	public String[] getMagazineIds() {
		return magazineIds;
	}

	/**
	 * @param magazineIds the magazineIds to set
	 */
	public void setMagazineIds(String[] magazineIds) {
		this.magazineIds = magazineIds;
	}
}