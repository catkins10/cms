package com.yuanluesoft.cms.complaint.soap;

import java.sql.Timestamp;
import java.util.Calendar;

import com.yuanluesoft.cms.complaint.pojo.Complaint;
import com.yuanluesoft.cms.complaint.service.ComplaintService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.soap.BaseSoapService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class ComplaintSoapService extends BaseSoapService {

	/**
	 * 创建投诉
	 * @param type
	 * @param subject
	 * @param queryPassword
	 * @param popedom
	 * @param area
	 * @param happenTime
	 * @param creator
	 * @param creatorTel
	 * @param creatorMail
	 * @param creatorCertificateName
	 * @param creatorIdentityCard
	 * @param creatorIP
	 * @param creatorMobile
	 * @param creatorFax
	 * @param creatorUnit
	 * @param creatorJob
	 * @param creatorAddress
	 * @param creatorPostalcode
	 * @param isPublic
	 * @param siteId
	 * @param content
	 * @throws ServiceException
	 */
	public String createComplaint(String type, String subject, String queryPassword, String popedom, String area, Calendar happenTime, String creator, String creatorTel, String creatorMail, String creatorCertificateName, String creatorIdentityCard, String creatorIP, String creatorMobile, String creatorFax, String creatorUnit, String creatorJob, String creatorAddress, String creatorPostalcode, boolean isPublic, long siteId, String content, String ssoSessionId) throws ServiceException, SoapException {
		if(getSessionInfo(ssoSessionId)==null) {
			return null;
		}
		ComplaintService complaintService = (ComplaintService)getSpringService("complaintService");
		String sn = complaintService.getSN();
		Complaint complaint = new Complaint();
		complaint.setId(UUIDLongGenerator.generateId()); //ID
		complaint.setType(type); //类型
		complaint.setPopedom(popedom); //事件辖区
		complaint.setArea(area); //事件地点
		complaint.setHappenTime(happenTime==null ? null : new Timestamp(happenTime.getTimeInMillis())); //事件时间
		complaint.setSn(sn); //编号
		complaint.setQueryPassword(queryPassword); //查询密码
		complaint.setSubject(subject); //主题
		complaint.setCreator(creator); //创建人姓名
		complaint.setCreated(DateTimeUtils.now()); //创建时间
		complaint.setCreatorTel(creatorTel); //联系电话
		complaint.setCreatorMail(creatorMail); //邮箱
		complaint.setCreatorSex('M'); //性别
		complaint.setCreatorCertificateName(creatorCertificateName); //创建人证件名称
		complaint.setCreatorIdentityCard(creatorIdentityCard); //创建人身份证/证件号码
		complaint.setCreatorIP(creatorIP); //创建人IP
		complaint.setCreatorMobile(creatorMobile); //创建人手机
		complaint.setCreatorFax(creatorFax); //创建人传真
		complaint.setCreatorUnit(creatorUnit); //创建人所在单位
		complaint.setCreatorJob(creatorJob); //创建人职业
		complaint.setCreatorAddress(creatorAddress); //创建人地址
		complaint.setCreatorPostalcode(creatorPostalcode); //创建人邮编
		complaint.setIsPublic(isPublic ? '1' : '0'); //是否允许公开
		//complaint.setremark; //附注
		complaint.setSiteId(siteId); //隶属站点ID
		complaint.setContent(content); //正文
		complaintService.save(complaint);
		return sn;
	}
}