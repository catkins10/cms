package com.yuanluesoft.cms.publicservice.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.util.LazyBodyUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 公众服务(cms_publicservice)
 * @author linchuan
 *
 */
public class PublicService extends WorkflowData {
	private String sn; //编号
	private String queryPassword; //查询密码
	private String subject; //主题
	private int workingDay; //指定工作日
	private Timestamp workingDate; //办理截止时间
	private String creator; //创建人姓名
	private Timestamp created; //创建时间
	private String creatorTel; //联系电话
	private String creatorMail; //邮箱
	private char creatorSex = 'M'; //性别
	private String creatorCertificateName; //创建人证件名称
	private String creatorIdentityCard; //创建人身份证/证件号码
	private String creatorIP; //创建人IP
	private String creatorMobile; //创建人手机
	private String creatorFax; //创建人传真
	private String creatorUnit; //创建人所在单位
	private String creatorJob; //创建人职业
	private String creatorAddress; //创建人地址
	private String creatorPostalcode; //创建人邮编
	private char isPublic = '0'; //是否允许公开
	private String remark; //附注
	private long siteId; //隶属站点ID
	private Set lazyBody; //正文
	private char publicPass = '0'; //同意公开
	private char publicBody = '0'; //公开正文
	private char publicWorkflow = '0'; //公开办理流程
	private String publicSubject; //公开的主题
	private Timestamp publicEnd; //公开截止时间
	private String publicPersonName; //公开经办人
	private long publicPersonId; //公开经办人ID
	private Timestamp completeTime; //办结时间
	private int isDeleted = 0; //是否删除
	private String clientDeviceId; //客户端设备ID
	
	/**
	 * 获取公开的办理意见
	 * @return
	 */
	public String getPublicOpinion() {
		if(publicWorkflow!='1') {
			return null;
		}
		try {
			return ListUtils.join(getOpinions(), "opinion", "\r\n\r\n", false);
		}
		catch(Exception e) {
			
		}
		return null;
	}
	
	/**
	 * 获取公开的正文
	 * @return
	 */
	public String getPublicContent() {
		return publicBody=='1' ? getContent() : null;
	}
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return LazyBodyUtils.getBody(this);
	}
	
	/**
	 * 获取HTML格式的正文
	 * @return
	 */
	public String getHtmlContent() {
		String content = getContent();
		return content==null ? null : content.replaceAll(" ", "&nbsp;").replaceAll("\\r", "").replaceAll("\\n", "<br/>");
	}
	
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		LazyBodyUtils.setBody(this, content);
	}
	
	/**
	 * 获取回复人
	 * @return
	 */
	public String getReplier() {
		PublicServiceOpinion replyOpinon = getReplyOpinion(false);
		return replyOpinon==null ? null : replyOpinon.getPersonName() + (replyOpinon.getAgentName()==null ? "" : "(" + replyOpinon.getAgentName() + ")");
	}

	/**
	 * 设置回复人
	 * @param replier
	 */
	public void setReplier(String replier) {
		getReplyOpinion(true).setPersonName(replier);
	}

	/**
	 * 获取回复内容
	 * @return
	 */
	public String getReplyContent() {
		PublicServiceOpinion replyOpinon = getReplyOpinion(false);
		return replyOpinon==null ? null : replyOpinon.getOpinion();
	}

	/**
	 * 设置回复内容
	 * @param replyContent
	 */
	public void setReplyContent(String replyContent) {
		getReplyOpinion(true).setOpinion(replyContent);
	}

	/**
	 * 获取回复时间
	 * @return
	 */
	public Timestamp getReplyTime() {
		PublicServiceOpinion replyOpinon = getReplyOpinion(false);
		return replyOpinon==null ? null : replyOpinon.getCreated();
	}

	/**
	 * 设置回复时间
	 * @param replyTime
	 */
	public void setReplyTime(Timestamp replyTime) {
		getReplyOpinion(true).setCreated(replyTime);
	}
	
	/**
	 * 获取回复意见
	 * @param createIfNotExists
	 * @return
	 */
	private PublicServiceOpinion getReplyOpinion(boolean createIfNotExists) {
		Set opinions = null;
		try {
			opinions = getOpinions();
		}
		catch(Exception e) {
		
		}
		if(!createIfNotExists && (opinions==null || opinions.isEmpty())) {
			return null;
		}
		//获取意见字段定义
		List fields = FieldUtils.listRecordFields(this.getClass().getName(), "opinion", null, null, null, true, true, false, false, 0);
		Field replyOpinionField = (Field)ListUtils.findObjectByProperty(fields, "required", new Boolean(true));
		if(replyOpinionField==null) {
			replyOpinionField = (Field)ListUtils.findObjectByProperty(fields, "name", "办理");
			if(replyOpinionField==null) {
				if(fields.size()!=0)
					replyOpinionField = (Field)fields.get(0);
			}
		}
		PublicServiceOpinion replyOpinion = null;
		if(replyOpinionField!=null)
			replyOpinion = (PublicServiceOpinion)ListUtils.findObjectByProperty(opinions, "opinionType", replyOpinionField.getName());
		if(replyOpinion!=null || !createIfNotExists) {
			return replyOpinion;
		}
		//创建意见
		replyOpinion = new PublicServiceOpinion();
		replyOpinion.setId(UUIDLongGenerator.generateId()); //ID
		replyOpinion.setMainRecordId(getId()); //主记录ID
		if(replyOpinionField!=null)
			replyOpinion.setOpinionType(replyOpinionField.getName()); //意见类型
		if(opinions==null) {
			opinions = new HashSet();
			setOpinions(opinions);
		}
		opinions.add(replyOpinion);
		return replyOpinion;
	}
	
	/**
	 * @return the publicSubject
	 */
	public String getPublicSubject() {
		return publicSubject==null || publicSubject.equals("") ? subject : publicSubject;
	}
	/**
	 * @param publicSubject the publicSubject to set
	 */
	public void setPublicSubject(String publicSubject) {
		this.publicSubject = publicSubject;
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
	 * @return the creatorFax
	 */
	public String getCreatorFax() {
		return creatorFax;
	}
	/**
	 * @param creatorFax the creatorFax to set
	 */
	public void setCreatorFax(String creatorFax) {
		this.creatorFax = creatorFax;
	}
	/**
	 * @return the creatorIdentityCard
	 */
	public String getCreatorIdentityCard() {
		return creatorIdentityCard;
	}
	/**
	 * @param creatorIdentityCard the creatorIdentityCard to set
	 */
	public void setCreatorIdentityCard(String creatorIdentityCard) {
		this.creatorIdentityCard = creatorIdentityCard;
	}
	/**
	 * @return the creatorIP
	 */
	public String getCreatorIP() {
		return creatorIP;
	}
	/**
	 * @param creatorIP the creatorIP to set
	 */
	public void setCreatorIP(String creatorIP) {
		this.creatorIP = creatorIP;
	}
	/**
	 * @return the creatorJob
	 */
	public String getCreatorJob() {
		return creatorJob;
	}
	/**
	 * @param creatorJob the creatorJob to set
	 */
	public void setCreatorJob(String creatorJob) {
		this.creatorJob = creatorJob;
	}
	/**
	 * @return the creatorMail
	 */
	public String getCreatorMail() {
		return creatorMail;
	}
	/**
	 * @param creatorMail the creatorMail to set
	 */
	public void setCreatorMail(String creatorMail) {
		this.creatorMail = creatorMail;
	}
	/**
	 * @return the creatorMobile
	 */
	public String getCreatorMobile() {
		return creatorMobile;
	}
	/**
	 * @param creatorMobile the creatorMobile to set
	 */
	public void setCreatorMobile(String creatorMobile) {
		this.creatorMobile = creatorMobile;
	}
	/**
	 * @return the creatorTel
	 */
	public String getCreatorTel() {
		return creatorTel;
	}
	/**
	 * @param creatorTel the creatorTel to set
	 */
	public void setCreatorTel(String creatorTel) {
		this.creatorTel = creatorTel;
	}
	/**
	 * @return the creatorUnit
	 */
	public String getCreatorUnit() {
		return creatorUnit;
	}
	/**
	 * @param creatorUnit the creatorUnit to set
	 */
	public void setCreatorUnit(String creatorUnit) {
		this.creatorUnit = creatorUnit;
	}
	/**
	 * @return the isPublic
	 */
	public char getIsPublic() {
		return isPublic;
	}
	/**
	 * @param isPublic the isPublic to set
	 */
	public void setIsPublic(char isPublic) {
		this.isPublic = isPublic;
	}
	/**
	 * @return the queryPassword
	 */
	public String getQueryPassword() {
		return queryPassword;
	}
	/**
	 * @param queryPassword the queryPassword to set
	 */
	public void setQueryPassword(String queryPassword) {
		this.queryPassword = queryPassword;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the workingDay
	 */
	public int getWorkingDay() {
		return workingDay;
	}
	/**
	 * @param workingDay the workingDay to set
	 */
	public void setWorkingDay(int workingDay) {
		this.workingDay = workingDay;
	}
	/**
	 * @return the lazyBody
	 */
	public Set getLazyBody() {
		return lazyBody;
	}
	/**
	 * @param lazyBody the lazyBody to set
	 */
	public void setLazyBody(Set lazyBody) {
		this.lazyBody = lazyBody;
	}
	/**
	 * @return the creatorSex
	 */
	public char getCreatorSex() {
		return creatorSex;
	}
	/**
	 * @param creatorSex the creatorSex to set
	 */
	public void setCreatorSex(char creatorSex) {
		this.creatorSex = creatorSex;
	}
	/**
	 * @return the creatorAddress
	 */
	public String getCreatorAddress() {
		return creatorAddress;
	}
	/**
	 * @param creatorAddress the creatorAddress to set
	 */
	public void setCreatorAddress(String creatorAddress) {
		this.creatorAddress = creatorAddress;
	}
	/**
	 * @return the creatorPostalcode
	 */
	public String getCreatorPostalcode() {
		return creatorPostalcode;
	}
	/**
	 * @param creatorPostalcode the creatorPostalcode to set
	 */
	public void setCreatorPostalcode(String creatorPostalcode) {
		this.creatorPostalcode = creatorPostalcode;
	}
	/**
	 * @return the creatorCertificateName
	 */
	public String getCreatorCertificateName() {
		return creatorCertificateName;
	}
	/**
	 * @param creatorCertificateName the creatorCertificateName to set
	 */
	public void setCreatorCertificateName(String creatorCertificateName) {
		this.creatorCertificateName = creatorCertificateName;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the publicBody
	 */
	public char getPublicBody() {
		return publicBody;
	}
	/**
	 * @param publicBody the publicBody to set
	 */
	public void setPublicBody(char publicBody) {
		this.publicBody = publicBody;
	}
	/**
	 * @return the publicPass
	 */
	public char getPublicPass() {
		return publicPass;
	}
	/**
	 * @param publicPass the publicPass to set
	 */
	public void setPublicPass(char publicPass) {
		this.publicPass = publicPass;
	}
	/**
	 * @return the publicWorkflow
	 */
	public char getPublicWorkflow() {
		return publicWorkflow;
	}
	/**
	 * @param publicWorkflow the publicWorkflow to set
	 */
	public void setPublicWorkflow(char publicWorkflow) {
		this.publicWorkflow = publicWorkflow;
	}

	/**
	 * @return the isDeleted
	 */
	public int getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the completeTime
	 */
	public Timestamp getCompleteTime() {
		return completeTime;
	}

	/**
	 * @param completeTime the completeTime to set
	 */
	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}

	/**
	 * @return the publicEnd
	 */
	public Timestamp getPublicEnd() {
		return publicEnd;
	}

	/**
	 * @param publicEnd the publicEnd to set
	 */
	public void setPublicEnd(Timestamp publicEnd) {
		this.publicEnd = publicEnd;
	}

	/**
	 * @return the publicPersonId
	 */
	public long getPublicPersonId() {
		return publicPersonId;
	}

	/**
	 * @param publicPersonId the publicPersonId to set
	 */
	public void setPublicPersonId(long publicPersonId) {
		this.publicPersonId = publicPersonId;
	}

	/**
	 * @return the publicPersonName
	 */
	public String getPublicPersonName() {
		return publicPersonName;
	}

	/**
	 * @param publicPersonName the publicPersonName to set
	 */
	public void setPublicPersonName(String publicPersonName) {
		this.publicPersonName = publicPersonName;
	}

	/**
	 * @return the clientDeviceId
	 */
	public String getClientDeviceId() {
		return clientDeviceId;
	}

	/**
	 * @param clientDeviceId the clientDeviceId to set
	 */
	public void setClientDeviceId(String clientDeviceId) {
		this.clientDeviceId = clientDeviceId;
	}

	/**
	 * @return the workingDate
	 */
	public Timestamp getWorkingDate() {
		return workingDate;
	}

	/**
	 * @param workingDate the workingDate to set
	 */
	public void setWorkingDate(Timestamp workingDate) {
		this.workingDate = workingDate;
	}
}