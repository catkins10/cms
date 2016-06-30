package com.yuanluesoft.cms.interview.pojo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 在线访谈:访谈视频(interview_video)
 * @author linchuan
 *
 */
public class InterviewVideo extends Record {
	private long interviewSubjectId; //主题ID
	private String videoUrl; //视频URL
	private String subject; //标题
	private Timestamp created; //上传时间
	private long creatorId; //创建人ID
	private String creator; //创建人
	
	/**
	 * 获取视频文件名称
	 * @return
	 */
	public String getVideoFileName() {
		String name = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
		try {
			return URLDecoder.decode(name, "utf-8");
		}
		catch (UnsupportedEncodingException e) {
			return name;
		}
	}
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the interviewSubjectId
	 */
	public long getInterviewSubjectId() {
		return interviewSubjectId;
	}
	/**
	 * @param interviewSubjectId the interviewSubjectId to set
	 */
	public void setInterviewSubjectId(long interviewSubjectId) {
		this.interviewSubjectId = interviewSubjectId;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the videoUrl
	 */
	public String getVideoUrl() {
		return videoUrl;
	}
	/**
	 * @param videoUrl the videoUrl to set
	 */
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
}