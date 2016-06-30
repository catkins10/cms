package com.yuanluesoft.bidding.project.pojo;

import java.sql.Timestamp;

/**
 * 标书上传(bidding_project_documents)
 * @author linchuan
 *
 */
public class BiddingProjectDocument extends BiddingProjectComponent {
	private String path; //标书文件路径,金润标书服务器上的存放目录
	private Timestamp uploadTime; //上传时间
	private long uploadPersonId; //上传人ID
	private String uploadPersonName; //上传人
	
	public String getFilePath() {
		return path.replaceAll("\\x5c","\\\\\\\\");
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the uploadPersonId
	 */
	public long getUploadPersonId() {
		return uploadPersonId;
	}
	/**
	 * @param uploadPersonId the uploadPersonId to set
	 */
	public void setUploadPersonId(long uploadPersonId) {
		this.uploadPersonId = uploadPersonId;
	}
	/**
	 * @return the uploadPersonName
	 */
	public String getUploadPersonName() {
		return uploadPersonName;
	}
	/**
	 * @param uploadPersonName the uploadPersonName to set
	 */
	public void setUploadPersonName(String uploadPersonName) {
		this.uploadPersonName = uploadPersonName;
	}
	/**
	 * @return the uploadTime
	 */
	public Timestamp getUploadTime() {
		return uploadTime;
	}
	/**
	 * @param uploadTime the uploadTime to set
	 */
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
}
