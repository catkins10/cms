package com.yuanluesoft.j2oa.info.forms;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Magazine extends WorkflowForm {
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
	
	//扩展属性
	private List magazineColumns; //栏目(MagazineColumn)列表
	private List toTypesetInfos; //待排版稿件（非简讯）列表,编辑时添加稿件用
	private List toTypesetBriefs; //待排版稿件（简讯）列表,编辑时添加稿件用
	private String useInfoIds; //使用的稿件ID
	private String useInfoSubjects; //使用的稿件标题
	private String currentColumn; //当前栏目,用来保存使用的稿件
	private String htmlBody; //HTML正文
	private String recordListChange; //记录列表变化情况
	private RecordVisitorList issueRanges = new RecordVisitorList(); //分发范围
	
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
	 * @return the magazineColumns
	 */
	public List getMagazineColumns() {
		return magazineColumns;
	}
	/**
	 * @param magazineColumns the magazineColumns to set
	 */
	public void setMagazineColumns(List magazineColumns) {
		this.magazineColumns = magazineColumns;
	}
	/**
	 * @return the useInfoIds
	 */
	public String getUseInfoIds() {
		return useInfoIds;
	}
	/**
	 * @param useInfoIds the useInfoIds to set
	 */
	public void setUseInfoIds(String useInfoIds) {
		this.useInfoIds = useInfoIds;
	}
	/**
	 * @return the useInfoSubjects
	 */
	public String getUseInfoSubjects() {
		return useInfoSubjects;
	}
	/**
	 * @param useInfoSubjects the useInfoSubjects to set
	 */
	public void setUseInfoSubjects(String useInfoSubjects) {
		this.useInfoSubjects = useInfoSubjects;
	}
	/**
	 * @return the currentColumn
	 */
	public String getCurrentColumn() {
		return currentColumn;
	}
	/**
	 * @param currentColumn the currentColumn to set
	 */
	public void setCurrentColumn(String currentColumn) {
		this.currentColumn = currentColumn;
	}
	/**
	 * @return the htmlBody
	 */
	public String getHtmlBody() {
		return htmlBody;
	}
	/**
	 * @param htmlBody the htmlBody to set
	 */
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
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
	/**
	 * @return the toTypesetBriefs
	 */
	public List getToTypesetBriefs() {
		return toTypesetBriefs;
	}
	/**
	 * @param toTypesetBriefs the toTypesetBriefs to set
	 */
	public void setToTypesetBriefs(List toTypesetBriefs) {
		this.toTypesetBriefs = toTypesetBriefs;
	}
	/**
	 * @return the toTypesetInfos
	 */
	public List getToTypesetInfos() {
		return toTypesetInfos;
	}
	/**
	 * @param toTypesetInfos the toTypesetInfos to set
	 */
	public void setToTypesetInfos(List toTypesetInfos) {
		this.toTypesetInfos = toTypesetInfos;
	}
	/**
	 * @return the recordListChange
	 */
	public String getRecordListChange() {
		return recordListChange;
	}
	/**
	 * @param recordListChange the recordListChange to set
	 */
	public void setRecordListChange(String recordListChange) {
		this.recordListChange = recordListChange;
	}
	/**
	 * @return the issueRanges
	 */
	public RecordVisitorList getIssueRanges() {
		return issueRanges;
	}
	/**
	 * @param issueRanges the issueRanges to set
	 */
	public void setIssueRanges(RecordVisitorList issueRanges) {
		this.issueRanges = issueRanges;
	}
}