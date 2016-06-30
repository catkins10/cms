package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryLink;

/**
 * 组织机构:组织机构引用(user_directory_link)
 * @author linchuan
 *
 */
public class OrgLink extends Org implements DirectoryLink {
	private long linkedDirectoryId; //引用的组织机构ID

	/**
	 * @return the linkedDirectoryId
	 */
	public long getLinkedDirectoryId() {
		return linkedDirectoryId;
	}

	/**
	 * @param linkedDirectoryId the linkedDirectoryId to set
	 */
	public void setLinkedDirectoryId(long linkedDirectoryId) {
		this.linkedDirectoryId = linkedDirectoryId;
	}
}