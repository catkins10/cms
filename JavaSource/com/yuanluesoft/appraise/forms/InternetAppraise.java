package com.yuanluesoft.appraise.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 网上评议
 * @author linchuan
 *
 */
public class InternetAppraise extends ActionForm {
	private long taskId; //评议任务ID
	private String taskName; //评议任务名称
	private int isSpecial; //是否专题评议
	private int appraiserType; //评议员类型,0/基础,1/管理服务对象
	private long areaId; //地区ID
	private String area; //地区名称
	private int appraiseYear; //评议年度
	private int appraiseMonth; //评议月份
	private int voteTimes; //投票人次
	private long creatorId; //发起人ID
	private String creator; //发起人
	private Timestamp created; //发起时间
	private Timestamp endTime; //截止时间
	private Set shortMessages; //短信列表
	private Set unitAppraises; //单位评议列表
	private int voteTotal; //本期投票数
	private int smsVoteTotal; //本期短信投票数
	private int internetVoteTotal; //本期网络投票数
	private double scoreComprehensive; //本期综合得分
	private int yearVoteTotal; //累计投票数,截止到本期
	private int yearSmsVoteTotal; //累计短信投票数,截止到本期
	private int yearInternetVoteTotal; //累计网络投票数,截止到本期
	private double yearScoreComprehensive; //累计综合得分,截止到本期
	private Set optionVotes; //选项统计列表

	//扩展属性
	private String appraiserNumber; //评议员手机号码
	private String appraiseCode; //评议验证码
	
	/**
	 * @return the appraiseMonth
	 */
	public int getAppraiseMonth() {
		return appraiseMonth;
	}
	/**
	 * @param appraiseMonth the appraiseMonth to set
	 */
	public void setAppraiseMonth(int appraiseMonth) {
		this.appraiseMonth = appraiseMonth;
	}
	/**
	 * @return the appraiserType
	 */
	public int getAppraiserType() {
		return appraiserType;
	}
	/**
	 * @param appraiserType the appraiserType to set
	 */
	public void setAppraiserType(int appraiserType) {
		this.appraiserType = appraiserType;
	}
	/**
	 * @return the appraiseYear
	 */
	public int getAppraiseYear() {
		return appraiseYear;
	}
	/**
	 * @param appraiseYear the appraiseYear to set
	 */
	public void setAppraiseYear(int appraiseYear) {
		this.appraiseYear = appraiseYear;
	}
	/**
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
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
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the shortMessages
	 */
	public Set getShortMessages() {
		return shortMessages;
	}
	/**
	 * @param shortMessages the shortMessages to set
	 */
	public void setShortMessages(Set shortMessages) {
		this.shortMessages = shortMessages;
	}
	/**
	 * @return the taskId
	 */
	public long getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}
	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	/**
	 * @return the unitAppraises
	 */
	public Set getUnitAppraises() {
		return unitAppraises;
	}
	/**
	 * @param unitAppraises the unitAppraises to set
	 */
	public void setUnitAppraises(Set unitAppraises) {
		this.unitAppraises = unitAppraises;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the appraiseCode
	 */
	public String getAppraiseCode() {
		return appraiseCode;
	}
	/**
	 * @param appraiseCode the appraiseCode to set
	 */
	public void setAppraiseCode(String appraiseCode) {
		this.appraiseCode = appraiseCode;
	}
	/**
	 * @return the appraiserNumber
	 */
	public String getAppraiserNumber() {
		return appraiserNumber;
	}
	/**
	 * @param appraiserNumber the appraiserNumber to set
	 */
	public void setAppraiserNumber(String appraiserNumber) {
		this.appraiserNumber = appraiserNumber;
	}
	/**
	 * @return the internetVoteTotal
	 */
	public int getInternetVoteTotal() {
		return internetVoteTotal;
	}
	/**
	 * @param internetVoteTotal the internetVoteTotal to set
	 */
	public void setInternetVoteTotal(int internetVoteTotal) {
		this.internetVoteTotal = internetVoteTotal;
	}
	/**
	 * @return the optionVotes
	 */
	public Set getOptionVotes() {
		return optionVotes;
	}
	/**
	 * @param optionVotes the optionVotes to set
	 */
	public void setOptionVotes(Set optionVotes) {
		this.optionVotes = optionVotes;
	}
	/**
	 * @return the smsVoteTotal
	 */
	public int getSmsVoteTotal() {
		return smsVoteTotal;
	}
	/**
	 * @param smsVoteTotal the smsVoteTotal to set
	 */
	public void setSmsVoteTotal(int smsVoteTotal) {
		this.smsVoteTotal = smsVoteTotal;
	}
	/**
	 * @return the voteTotal
	 */
	public int getVoteTotal() {
		return voteTotal;
	}
	/**
	 * @param voteTotal the voteTotal to set
	 */
	public void setVoteTotal(int voteTotal) {
		this.voteTotal = voteTotal;
	}
	/**
	 * @return the isSpecial
	 */
	public int getIsSpecial() {
		return isSpecial;
	}
	/**
	 * @param isSpecial the isSpecial to set
	 */
	public void setIsSpecial(int isSpecial) {
		this.isSpecial = isSpecial;
	}
	/**
	 * @return the scoreComprehensive
	 */
	public double getScoreComprehensive() {
		return scoreComprehensive;
	}
	/**
	 * @param scoreComprehensive the scoreComprehensive to set
	 */
	public void setScoreComprehensive(double scoreComprehensive) {
		this.scoreComprehensive = scoreComprehensive;
	}
	/**
	 * @return the voteTimes
	 */
	public int getVoteTimes() {
		return voteTimes;
	}
	/**
	 * @param voteTimes the voteTimes to set
	 */
	public void setVoteTimes(int voteTimes) {
		this.voteTimes = voteTimes;
	}
	/**
	 * @return the yearInternetVoteTotal
	 */
	public int getYearInternetVoteTotal() {
		return yearInternetVoteTotal;
	}
	/**
	 * @param yearInternetVoteTotal the yearInternetVoteTotal to set
	 */
	public void setYearInternetVoteTotal(int yearInternetVoteTotal) {
		this.yearInternetVoteTotal = yearInternetVoteTotal;
	}
	/**
	 * @return the yearScoreComprehensive
	 */
	public double getYearScoreComprehensive() {
		return yearScoreComprehensive;
	}
	/**
	 * @param yearScoreComprehensive the yearScoreComprehensive to set
	 */
	public void setYearScoreComprehensive(double yearScoreComprehensive) {
		this.yearScoreComprehensive = yearScoreComprehensive;
	}
	/**
	 * @return the yearSmsVoteTotal
	 */
	public int getYearSmsVoteTotal() {
		return yearSmsVoteTotal;
	}
	/**
	 * @param yearSmsVoteTotal the yearSmsVoteTotal to set
	 */
	public void setYearSmsVoteTotal(int yearSmsVoteTotal) {
		this.yearSmsVoteTotal = yearSmsVoteTotal;
	}
	/**
	 * @return the yearVoteTotal
	 */
	public int getYearVoteTotal() {
		return yearVoteTotal;
	}
	/**
	 * @param yearVoteTotal the yearVoteTotal to set
	 */
	public void setYearVoteTotal(int yearVoteTotal) {
		this.yearVoteTotal = yearVoteTotal;
	}
}