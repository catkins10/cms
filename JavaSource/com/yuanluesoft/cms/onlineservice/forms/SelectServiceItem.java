package com.yuanluesoft.cms.onlineservice.forms;


/**
 * 
 * @author linchuan
 *
 */
public class SelectServiceItem extends SelectDirectory {
	private long filterDirectoryId; //过滤掉属于该目录的事项
	private boolean acceptSupportOnly; //是否必须支持受理
	private boolean complaintSupportOnly; //是否必须支持投诉
	private boolean consultSupportOnly; //是否必须支持咨询
	private boolean querySupportOnly; //是否必须支持状态查询

	/**
	 * @return the filterDirectoryId
	 */
	public long getFilterDirectoryId() {
		return filterDirectoryId;
	}

	/**
	 * @param filterDirectoryId the filterDirectoryId to set
	 */
	public void setFilterDirectoryId(long filterDirectoryId) {
		this.filterDirectoryId = filterDirectoryId;
	}

	/**
	 * @return the acceptSupportOnly
	 */
	public boolean isAcceptSupportOnly() {
		return acceptSupportOnly;
	}

	/**
	 * @param acceptSupportOnly the acceptSupportOnly to set
	 */
	public void setAcceptSupportOnly(boolean acceptSupportOnly) {
		this.acceptSupportOnly = acceptSupportOnly;
	}

	/**
	 * @return the complaintSupportOnly
	 */
	public boolean isComplaintSupportOnly() {
		return complaintSupportOnly;
	}

	/**
	 * @param complaintSupportOnly the complaintSupportOnly to set
	 */
	public void setComplaintSupportOnly(boolean complaintSupportOnly) {
		this.complaintSupportOnly = complaintSupportOnly;
	}

	/**
	 * @return the consultSupportOnly
	 */
	public boolean isConsultSupportOnly() {
		return consultSupportOnly;
	}

	/**
	 * @param consultSupportOnly the consultSupportOnly to set
	 */
	public void setConsultSupportOnly(boolean consultSupportOnly) {
		this.consultSupportOnly = consultSupportOnly;
	}

	/**
	 * @return the querySupportOnly
	 */
	public boolean isQuerySupportOnly() {
		return querySupportOnly;
	}

	/**
	 * @param querySupportOnly the querySupportOnly to set
	 */
	public void setQuerySupportOnly(boolean querySupportOnly) {
		this.querySupportOnly = querySupportOnly;
	}
}