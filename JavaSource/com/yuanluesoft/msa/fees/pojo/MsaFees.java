package com.yuanluesoft.msa.fees.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 规费征缴：基本信息(msa_fees)
 * @author linchuan
 *
 */
public class MsaFees extends WorkflowData {
	private String name; //名称
	private long creatorId; //创建者ID
	private String creator; //创建者
	private Timestamp created; //创建时间
	private Set items; //规费项目列表
	
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
	 * @return the items
	 */
	public Set getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(Set items) {
		this.items = items;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}