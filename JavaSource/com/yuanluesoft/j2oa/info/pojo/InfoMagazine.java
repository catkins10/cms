package com.yuanluesoft.j2oa.info.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 信息采编:刊物(info_magazine)
 * @author linchuan
 *
 */
public class InfoMagazine extends WorkflowData {
	private long defineId; //刊物配置ID
	private String name; //刊物名称
	private int sn; //期数
	private int snTotal; //总期数
	private long typesetPersonId; //排版人ID
	private String typesetPerson; //排版人
	private Timestamp typesetTime; //排版时间
	private Date generateDate; //生成日期
	private long issuePersonId; //定版人ID
	private String issuePerson; //定版人
	private Timestamp issueTime; //定版时间
	private Set htmlBodies; //正文
	private Set useInfos; //使用的稿件
	
	/**
	 * 获取正文
	 */
	public String getHtmlBody() {
		try {
			if(htmlBodies==null || htmlBodies.isEmpty()) {
				return null;
			}
			return ((InfoMagazineBody)htmlBodies.iterator().next()).getBody();
		}
		catch (Exception e) {
			return null;
		}
	}
	/**
	 * @return the defineId
	 */
	public long getDefineId() {
		return defineId;
	}
	/**
	 * @param defineId the defineId to set
	 */
	public void setDefineId(long defineId) {
		this.defineId = defineId;
	}
	/**
	 * @return the issuePerson
	 */
	public String getIssuePerson() {
		return issuePerson;
	}
	/**
	 * @param issuePerson the issuePerson to set
	 */
	public void setIssuePerson(String issuePerson) {
		this.issuePerson = issuePerson;
	}
	/**
	 * @return the issuePersonId
	 */
	public long getIssuePersonId() {
		return issuePersonId;
	}
	/**
	 * @param issuePersonId the issuePersonId to set
	 */
	public void setIssuePersonId(long issuePersonId) {
		this.issuePersonId = issuePersonId;
	}
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
	public int getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(int sn) {
		this.sn = sn;
	}
	/**
	 * @return the snTotal
	 */
	public int getSnTotal() {
		return snTotal;
	}
	/**
	 * @param snTotal the snTotal to set
	 */
	public void setSnTotal(int snTotal) {
		this.snTotal = snTotal;
	}
	/**
	 * @return the typesetPerson
	 */
	public String getTypesetPerson() {
		return typesetPerson;
	}
	/**
	 * @param typesetPerson the typesetPerson to set
	 */
	public void setTypesetPerson(String typesetPerson) {
		this.typesetPerson = typesetPerson;
	}
	/**
	 * @return the typesetPersonId
	 */
	public long getTypesetPersonId() {
		return typesetPersonId;
	}
	/**
	 * @param typesetPersonId the typesetPersonId to set
	 */
	public void setTypesetPersonId(long typesetPersonId) {
		this.typesetPersonId = typesetPersonId;
	}
	/**
	 * @return the typesetTime
	 */
	public Timestamp getTypesetTime() {
		return typesetTime;
	}
	/**
	 * @param typesetTime the typesetTime to set
	 */
	public void setTypesetTime(Timestamp typesetTime) {
		this.typesetTime = typesetTime;
	}
	/**
	 * @return the useInfos
	 */
	public Set getUseInfos() {
		return useInfos;
	}
	/**
	 * @param useInfos the useInfos to set
	 */
	public void setUseInfos(Set useInfos) {
		this.useInfos = useInfos;
	}
	/**
	 * @return the htmlBodies
	 */
	public Set getHtmlBodies() {
		return htmlBodies;
	}
	/**
	 * @param htmlBodies the htmlBodies to set
	 */
	public void setHtmlBodies(Set htmlBodies) {
		this.htmlBodies = htmlBodies;
	}
	/**
	 * @return the generateDate
	 */
	public Date getGenerateDate() {
		return generateDate;
	}
	/**
	 * @param generateDate the generateDate to set
	 */
	public void setGenerateDate(Date generateDate) {
		this.generateDate = generateDate;
	}

}