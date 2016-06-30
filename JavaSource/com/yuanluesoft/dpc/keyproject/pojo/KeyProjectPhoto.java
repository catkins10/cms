package com.yuanluesoft.dpc.keyproject.pojo;

import java.sql.Date;

/**
 * 项目进展实景(keyproject_photo)
 * @author linchuan
 *
 */
public class KeyProjectPhoto extends KeyProjectComponent {
	private int photoYear; //年份
	private int photoMonth; //月份
	private String photoSubject; //图片标题
	private Date shotTime; //拍摄时间
	
	/**
	 * @return the photoMonth
	 */
	public int getPhotoMonth() {
		return photoMonth;
	}
	/**
	 * @param photoMonth the photoMonth to set
	 */
	public void setPhotoMonth(int photoMonth) {
		this.photoMonth = photoMonth;
	}
	/**
	 * @return the photoSubject
	 */
	public String getPhotoSubject() {
		return photoSubject;
	}
	/**
	 * @param photoSubject the photoSubject to set
	 */
	public void setPhotoSubject(String photoSubject) {
		this.photoSubject = photoSubject;
	}
	/**
	 * @return the photoYear
	 */
	public int getPhotoYear() {
		return photoYear;
	}
	/**
	 * @param photoYear the photoYear to set
	 */
	public void setPhotoYear(int photoYear) {
		this.photoYear = photoYear;
	}
	/**
	 * @return the shotTime
	 */
	public Date getShotTime() {
		return shotTime;
	}
	/**
	 * @param shotTime the shotTime to set
	 */
	public void setShotTime(Date shotTime) {
		this.shotTime = shotTime;
	}
}