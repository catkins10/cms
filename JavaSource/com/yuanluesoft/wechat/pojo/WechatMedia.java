package com.yuanluesoft.wechat.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 微信:多媒体文件(wechat_media)
 * @author linchuan
 *
 */
public class WechatMedia extends Record {
	private String path; //文件路径
	private String mediaId; //多媒体文件ID,每个多媒体文件（media_id）会在上传、用户发送到微信服务器3天后自动删除
	private Timestamp uploadTime; //上传时间
	
	/**
	 * @return the mediaId
	 */
	public String getMediaId() {
		return mediaId;
	}
	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
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