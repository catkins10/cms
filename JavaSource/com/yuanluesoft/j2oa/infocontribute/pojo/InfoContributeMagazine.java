package com.yuanluesoft.j2oa.infocontribute.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 刊物发布(info_contribute_magazine)
 * @author linchuan
 *
 */
public class InfoContributeMagazine extends Record {
	private String name; //名称
	private Timestamp issueTime; //发布时间
	private long sn; //期数
	private long snTotal; //总期数
	private Set visitors; //访问者
	
	/**
	 * @return the issueTime
	 */
	public Timestamp getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(Timestamp issueTime) {
		this.issueTime = issueTime;
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
	/**
	 * @return the sn
	 */
	public long getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(long sn) {
		this.sn = sn;
	}
	/**
	 * @return the snTotal
	 */
	public long getSnTotal() {
		return snTotal;
	}
	/**
	 * @param snTotal the snTotal to set
	 */
	public void setSnTotal(long snTotal) {
		this.snTotal = snTotal;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
}