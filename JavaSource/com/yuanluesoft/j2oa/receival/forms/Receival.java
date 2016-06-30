package com.yuanluesoft.j2oa.receival.forms;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.archives.administrative.model.FilingOption;
import com.yuanluesoft.j2oa.receival.pojo.ReceivalFilingConfig;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author LinChuan
 * 
 */
public class Receival extends WorkflowForm {
	private String docWord; //文件字号,文件字号
	private String fromUnit; //来文单位,来文单位
	private String registPerson; //登记人,登记人
	private String subject; //标题,标题
	private int sequence; //收文序号(TODO)
	private String docType; //文件分类
	private String secureLevel; //秘密等级,秘密等级
	private String secureTerm; //保密期限,保密期限
	private String priority; //紧急程度,紧急程度
	private int receivalCount; //份数,份数
	private int pageCount; //页数,页数
	private Date signDate; //成文日期,成文日期
	private String registDepartment; //登记部门,登记部门
	private Date receivalDate; //收文日期,收文日期
	private Date transactDate; //办理期限,办理期限
	private String keyword; //主题词,主题词
	private String content; //来文摘要,来文摘要
	private Timestamp filingTime; //归档时间
	private String mainDo; //主办部门
	private String remark; //附注,附注
	
	//增加传阅人
	private String appendReaderIds;
	private String appendReaderNames;
	//内部分发
	private RecordVisitorList interSendVisitors = new RecordVisitorList();
	private String readerNames; //读者名称列表
	//归档选项
	private ReceivalFilingConfig filingConfig = new ReceivalFilingConfig();
	private FilingOption filingOption = new FilingOption();
	//办理单
	private String handling;
	private String filingHandling; //办理单归档
	
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
	 * @return Returns the content.
	 */
	public java.lang.String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	/**
	 * @return Returns the docWord.
	 */
	public java.lang.String getDocWord() {
		return docWord;
	}
	/**
	 * @param docWord The docWord to set.
	 */
	public void setDocWord(java.lang.String docWord) {
		this.docWord = docWord;
	}
	/**
	 * @return Returns the fromUnit.
	 */
	public java.lang.String getFromUnit() {
		return fromUnit;
	}
	/**
	 * @param fromUnit The fromUnit to set.
	 */
	public void setFromUnit(java.lang.String fromUnit) {
		this.fromUnit = fromUnit;
	}
	/**
	 * @return Returns the keyword.
	 */
	public java.lang.String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword The keyword to set.
	 */
	public void setKeyword(java.lang.String keyword) {
		this.keyword = keyword;
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
	 * @return Returns the priority.
	 */
	public java.lang.String getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(java.lang.String priority) {
		this.priority = priority;
	}
	/**
	 * @return Returns the receivalCount.
	 */
	public int getReceivalCount() {
		return receivalCount;
	}
	/**
	 * @param receivalCount The receivalCount to set.
	 */
	public void setReceivalCount(int receivalCount) {
		this.receivalCount = receivalCount;
	}
	/**
	 * @return Returns the receivalDate.
	 */
	public Date getReceivalDate() {
		return receivalDate;
	}
	/**
	 * @param receivalDate The receivalDate to set.
	 */
	public void setReceivalDate(Date receivalDate) {
		this.receivalDate = receivalDate;
	}
	/**
	 * @return Returns the registDepartment.
	 */
	public java.lang.String getRegistDepartment() {
		return registDepartment;
	}
	/**
	 * @param registDepartment The registDepartment to set.
	 */
	public void setRegistDepartment(java.lang.String registDepartment) {
		this.registDepartment = registDepartment;
	}
	/**
	 * @return Returns the registPerson.
	 */
	public java.lang.String getRegistPerson() {
		return registPerson;
	}
	/**
	 * @param registPerson The registPerson to set.
	 */
	public void setRegistPerson(java.lang.String registPerson) {
		this.registPerson = registPerson;
	}
	/**
	 * @return Returns the remark.
	 */
	public java.lang.String getRemark() {
		return remark;
	}
	/**
	 * @param remark The remark to set.
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	/**
	 * @return Returns the secureLevel.
	 */
	public java.lang.String getSecureLevel() {
		return secureLevel;
	}
	/**
	 * @param secureLevel The secureLevel to set.
	 */
	public void setSecureLevel(java.lang.String secureLevel) {
		this.secureLevel = secureLevel;
	}
	/**
	 * @return Returns the secureTerm.
	 */
	public java.lang.String getSecureTerm() {
		return secureTerm;
	}
	/**
	 * @param secureTerm The secureTerm to set.
	 */
	public void setSecureTerm(java.lang.String secureTerm) {
		this.secureTerm = secureTerm;
	}
	/**
	 * @return Returns the signDate.
	 */
	public Date getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate The signDate to set.
	 */
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return Returns the subject.
	 */
	public java.lang.String getSubject() {
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(java.lang.String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the transactDate.
	 */
	public Date getTransactDate() {
		return transactDate;
	}
	/**
	 * @param transactDate The transactDate to set.
	 */
	public void setTransactDate(Date transactDate) {
		this.transactDate = transactDate;
	}
	/**
	 * @return Returns the appendReaderIds.
	 */
	public String getAppendReaderIds() {
		return appendReaderIds;
	}
	/**
	 * @param appendReaderIds The appendReaderIds to set.
	 */
	public void setAppendReaderIds(String appendReaderIds) {
		this.appendReaderIds = appendReaderIds;
	}
	/**
	 * @return Returns the appendReaderNames.
	 */
	public String getAppendReaderNames() {
		return appendReaderNames;
	}
	/**
	 * @param appendReaderNames The appendReaderNames to set.
	 */
	public void setAppendReaderNames(String appendReaderNames) {
		this.appendReaderNames = appendReaderNames;
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
	 * @return Returns the filingConfig.
	 */
	public ReceivalFilingConfig getFilingConfig() {
		return filingConfig;
	}
	/**
	 * @param filingConfig The filingConfig to set.
	 */
	public void setFilingConfig(ReceivalFilingConfig filingConfig) {
		this.filingConfig = filingConfig;
	}
	/**
	 * @return Returns the docType.
	 */
	public java.lang.String getDocType() {
		return docType;
	}
	/**
	 * @param docType The docType to set.
	 */
	public void setDocType(java.lang.String docType) {
		this.docType = docType;
	}
	/**
	 * @return Returns the filingTime.
	 */
	public java.sql.Timestamp getFilingTime() {
		return filingTime;
	}
	/**
	 * @param filingTime The filingTime to set.
	 */
	public void setFilingTime(java.sql.Timestamp filingTime) {
		this.filingTime = filingTime;
	}
	/**
	 * @return Returns the mainDo.
	 */
	public java.lang.String getMainDo() {
		return mainDo;
	}
	/**
	 * @param mainDo The mainDo to set.
	 */
	public void setMainDo(java.lang.String mainDo) {
		this.mainDo = mainDo;
	}
	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
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
	 * @return the filingHandling
	 */
	public String getFilingHandling() {
		return filingHandling;
	}
	/**
	 * @param filingHandling the filingHandling to set
	 */
	public void setFilingHandling(String filingHandling) {
		this.filingHandling = filingHandling;
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