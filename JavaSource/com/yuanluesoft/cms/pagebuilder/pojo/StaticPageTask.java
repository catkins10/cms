package com.yuanluesoft.cms.pagebuilder.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 静态页面生成任务(cms_page_task)
 * @author linchuan
 *
 */
public class StaticPageTask extends Record {
	private Timestamp created; //创建时间
	private long startupTime; //启动时间
	private int startup = 0; //是否启动
	private float priority; //优先级

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
	 * @return the startupTime
	 */
	public long getStartupTime() {
		return startupTime;
	}

	/**
	 * @param startupTime the startupTime to set
	 */
	public void setStartupTime(long startupTime) {
		this.startupTime = startupTime;
	}

	/**
	 * @return the startup
	 */
	public int getStartup() {
		return startup;
	}

	/**
	 * @param startup the startup to set
	 */
	public void setStartup(int startup) {
		this.startup = startup;
	}

	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
}