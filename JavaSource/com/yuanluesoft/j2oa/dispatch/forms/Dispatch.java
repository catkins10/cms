/*
 * Created on 2005-2-11
 *
 */
package com.yuanluesoft.j2oa.dispatch.forms;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.archives.administrative.model.FilingOption;
import com.yuanluesoft.j2oa.dispatch.pojo.DispatchFilingConfig;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author LinChuan
 * TODO 允许所有人读要求在privilege表中增加visitorId=0的记录
 * 
 */
public class Dispatch extends WorkflowForm {
	private String subject; //标题
	private String docType; //发文种类
	private String secureLevel; //秘密等级
	private String secureTerm; //保密期限
	private String priority; //紧急程度
	private String mainSend; //主送机关
	private String copySend; //抄送机关
	private String keyword; //主题词
	private String docMark; //机关代字
	private int markYear; //机关代字年份
	private int markSequence; //机关代字序号
	private String docWord; //发文字号
	private int printNumber; //打印份数
	private int pageCount; //页数
	private String queryLevel; //查询级别
	private String distributeRange; //分发范围
	private String draftDepartment; //起草部门
	private String draftPerson; //起草人
	private Timestamp draftDate; //起草时间
	private String signPerson; //签发人
	private Date signDate; //签发时间
	private Date generateDate; //生成时间
	private Date distributeDate; //印发日期
	private Timestamp filingTime; //归档时间
	private String remark; //附注
	private String publicType; //公开类型,主动公开/依申请公开/不公开
	private String publicReason; //不公开的理由
	private Set bodies; //正文
	
	private String handling; //办理单
	private String htmlBody; //HTML正文
	private String readerNames; //读者名称列表
	private RecordVisitorList interSendVisitors = new RecordVisitorList(); //内部分发
	
	//归档选项
	private DispatchFilingConfig filingConfig = new DispatchFilingConfig(); //归档配置
	private FilingOption filingOption = new FilingOption();
	private boolean forceFiling; //是否强制归档,比如用户还有操作需要执行
	
	/**
	 * @return Returns the copySend.
	 */
	public String getCopySend() {
		return copySend;
	}
	/**
	 * @param copySend The copySend to set.
	 */
	public void setCopySend(String copySend) {
		this.copySend = copySend;
	}
	/**
	 * @return Returns the distributeRange.
	 */
	public String getDistributeRange() {
		return distributeRange;
	}
	/**
	 * @param distributeRange The distributeRange to set.
	 */
	public void setDistributeRange(String distributeRange) {
		this.distributeRange = distributeRange;
	}
	/**
	 * @return Returns the docMark.
	 */
	public String getDocMark() {
		return docMark;
	}
	/**
	 * @param docMark The docMark to set.
	 */
	public void setDocMark(String docMark) {
		if("".equals(docMark)) {
			docMark = null;
		}
		this.docMark = docMark;
	}
	/**
	 * @return Returns the docWord.
	 */
	public String getDocWord() {
		return docWord;
	}
	/**
	 * @param docWord The docWord to set.
	 */
	public void setDocWord(String docWord) {
		this.docWord = docWord;
	}
	/**
	 * @return Returns the draftDepartment.
	 */
	public String getDraftDepartment() {
		return draftDepartment;
	}
	/**
	 * @param draftDepartment The draftDepartment to set.
	 */
	public void setDraftDepartment(String draftDepartment) {
		this.draftDepartment = draftDepartment;
	}
	/**
	 * @return Returns the draftPerson.
	 */
	public String getDraftPerson() {
		return draftPerson;
	}
	/**
	 * @param draftPerson The draftPerson to set.
	 */
	public void setDraftPerson(String draftPerson) {
		this.draftPerson = draftPerson;
	}
	/**
	 * @return Returns the keyword.
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword The keyword to set.
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return Returns the mainSend.
	 */
	public String getMainSend() {
		return mainSend;
	}
	/**
	 * @param mainSend The mainSend to set.
	 */
	public void setMainSend(String mainSend) {
		if("".equals(mainSend)) {
			mainSend = null;
		}
		this.mainSend = mainSend;
	}
	/**
	 * @return Returns the markSequence.
	 */
	public int getMarkSequence() {
		return markSequence;
	}
	/**
	 * @param markSequence The markSequence to set.
	 */
	public void setMarkSequence(int markSequence) {
		this.markSequence = markSequence;
	}
	/**
	 * @return Returns the markYear.
	 */
	public int getMarkYear() {
		return markYear;
	}
	/**
	 * @param markYear The markYear to set.
	 */
	public void setMarkYear(int markYear) {
		this.markYear = markYear;
	}
	/**
	 * @return Returns the printNumber.
	 */
	public int getPrintNumber() {
		return printNumber;
	}
	/**
	 * @param printNumber The printNumber to set.
	 */
	public void setPrintNumber(int printNumber) {
		this.printNumber = printNumber;
	}
	/**
	 * @return Returns the priority.
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * @return Returns the queryLevel.
	 */
	public String getQueryLevel() {
		return queryLevel;
	}
	/**
	 * @param queryLevel The queryLevel to set.
	 */
	public void setQueryLevel(String queryLevel) {
		this.queryLevel = queryLevel;
	}
	/**
	 * @return Returns the readerNames.
	 */
	public String getReaderNames() {
		return readerNames;
	}
	/**
	 * @param readerNames The readerNames to set.
	 */
	public void setReaderNames(String readerNames) {
		this.readerNames = readerNames;
	}
	/**
	 * @return Returns the remark.
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark The remark to set.
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return Returns the secureLevel.
	 */
	public String getSecureLevel() {
		return secureLevel;
	}
	/**
	 * @param secureLevel The secureLevel to set.
	 */
	public void setSecureLevel(String secureLevel) {
		this.secureLevel = secureLevel;
	}
	/**
	 * @return Returns the secureTerm.
	 */
	public String getSecureTerm() {
		return secureTerm;
	}
	/**
	 * @param secureTerm The secureTerm to set.
	 */
	public void setSecureTerm(String secureTerm) {
		this.secureTerm = secureTerm;
	}
	/**
	 * @return Returns the signPerson.
	 */
	public String getSignPerson() {
		return signPerson;
	}
	/**
	 * @param signPerson The signPerson to set.
	 */
	public void setSignPerson(String signPerson) {
		this.signPerson = signPerson;
	}
	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		if("".equals(subject)) {
			subject = null;
		}
		this.subject = subject;
	}
	/**
	 * @return Returns the draftDate.
	 */
	public Timestamp getDraftDate() {
		return draftDate;
	}
	/**
	 * @param draftDate The draftDate to set.
	 */
	public void setDraftDate(Timestamp draftDate) {
		this.draftDate = draftDate;
	}
    /**
     * @return Returns the interSendVisitors.
     */
    public RecordVisitorList getInterSendVisitors() {
        return interSendVisitors;
    }
    /**
     * @param interSendVisitors The interSendVisitors to set.
     */
    public void setInterSendVisitors(RecordVisitorList interSendVisitors) {
        this.interSendVisitors = interSendVisitors;
    }
    /**
     * @return Returns the htmlBody.
     */
    public String getHtmlBody() {
        return htmlBody;
    }
    /**
     * @param htmlBody The htmlBody to set.
     */
    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }
	/**
	 * @return Returns the pageCount.
	 */
	public int getPageCount() {
		return pageCount;
	}
	/**
	 * @param pageCount The pageCount to set.
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	/**
	 * @return Returns the docType.
	 */
	public String getDocType() {
		return docType;
	}
	/**
	 * @param docType The docType to set.
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}
	/**
	 * @return Returns the filingConfig.
	 */
	public DispatchFilingConfig getFilingConfig() {
		return filingConfig;
	}
	/**
	 * @param filingConfig The filingConfig to set.
	 */
	public void setFilingConfig(DispatchFilingConfig filingConfig) {
		this.filingConfig = filingConfig;
	}
	/**
	 * @return the publicReason
	 */
	public String getPublicReason() {
		return publicReason;
	}
	/**
	 * @param publicReason the publicReason to set
	 */
	public void setPublicReason(String publicReason) {
		this.publicReason = publicReason;
	}
	/**
	 * @return the publicType
	 */
	public String getPublicType() {
		return publicType;
	}
	/**
	 * @param publicType the publicType to set
	 */
	public void setPublicType(String publicType) {
		this.publicType = publicType;
	}
	/**
	 * @return the filingTime
	 */
	public Timestamp getFilingTime() {
		return filingTime;
	}
	/**
	 * @param filingTime the filingTime to set
	 */
	public void setFilingTime(Timestamp filingTime) {
		this.filingTime = filingTime;
	}
	/**
	 * @return the bodies
	 */
	public Set getBodies() {
		return bodies;
	}
	/**
	 * @param bodies the bodies to set
	 */
	public void setBodies(Set bodies) {
		this.bodies = bodies;
	}
	/**
	 * @return the forceFiling
	 */
	public boolean isForceFiling() {
		return forceFiling;
	}
	/**
	 * @param forceFiling the forceFiling to set
	 */
	public void setForceFiling(boolean forceFiling) {
		this.forceFiling = forceFiling;
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
	 * @return the distributeDate
	 */
	public Date getDistributeDate() {
		return distributeDate;
	}
	/**
	 * @param distributeDate the distributeDate to set
	 */
	public void setDistributeDate(Date distributeDate) {
		this.distributeDate = distributeDate;
	}
	/**
	 * @return the signDate
	 */
	public Date getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return the handling
	 */
	public String getHandling() {
		return handling;
	}
	/**
	 * @param handling the handling to set
	 */
	public void setHandling(String handling) {
		this.handling = handling;
	}
	/**
	 * @return the filingOption
	 */
	public FilingOption getFilingOption() {
		return filingOption;
	}
	/**
	 * @param filingOption the filingOption to set
	 */
	public void setFilingOption(FilingOption filingOption) {
		this.filingOption = filingOption;
	}
}