package com.yuanluesoft.j2oa.info.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 拟采用稿件:报送情况(info_send_higher)
 * @author linchuan
 *
 */
public class InfoSendHigher extends Record {
	private long infoId; //稿件ID
	private int level; //报送级别,1/县办,2/市办,3/省办,4/国办
	private Timestamp sendTime; //报送时间
	private long senderId; //报送人ID
	private String sender; //报送人
	private Timestamp useTime; //采用时间
	private String useMagazine; //采用刊物名称
	private long useRegisterId; //采用登记人ID
	private String useRegister; //采用登记人
	
	/**
	 * @return the infoId
	 */
	public long getInfoId() {
		return infoId;
	}
	/**
	 * @param infoId the infoId to set
	 */
	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * @return the senderId
	 */
	public long getSenderId() {
		return senderId;
	}
	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}
	/**
	 * @return the sendTime
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the useMagazine
	 */
	public String getUseMagazine() {
		return useMagazine;
	}
	/**
	 * @param useMagazine the useMagazine to set
	 */
	public void setUseMagazine(String useMagazine) {
		this.useMagazine = useMagazine;
	}
	/**
	 * @return the useRegister
	 */
	public String getUseRegister() {
		return useRegister;
	}
	/**
	 * @param useRegister the useRegister to set
	 */
	public void setUseRegister(String useRegister) {
		this.useRegister = useRegister;
	}
	/**
	 * @return the useRegisterId
	 */
	public long getUseRegisterId() {
		return useRegisterId;
	}
	/**
	 * @param useRegisterId the useRegisterId to set
	 */
	public void setUseRegisterId(long useRegisterId) {
		this.useRegisterId = useRegisterId;
	}
	/**
	 * @return the useTime
	 */
	public Timestamp getUseTime() {
		return useTime;
	}
	/**
	 * @param useTime the useTime to set
	 */
	public void setUseTime(Timestamp useTime) {
		this.useTime = useTime;
	}
}