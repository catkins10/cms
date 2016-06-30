package com.yuanluesoft.cms.leadermail.soap;

import java.sql.Timestamp;
import java.util.Calendar;

import com.yuanluesoft.cms.leadermail.pojo.LeaderMail;
import com.yuanluesoft.cms.leadermail.service.LeaderMailService;
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
public class LeaderMailSoapService extends BaseSoapService {

	/**
	 * 创建邮件
	 * @param type 类型
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
	public String createLeaderMail(String type, String subject, String queryPassword, String popedom, String area, Calendar happenTime, String creator, String creatorTel, String creatorMail, String creatorCertificateName, String creatorIdentityCard, String creatorIP, String creatorMobile, String creatorFax, String creatorUnit, String creatorJob, String creatorAddress, String creatorPostalcode, boolean isPublic, long siteId, String content, String ssoSessionId) throws ServiceException, SoapException {
		if(getSessionInfo(ssoSessionId)==null) {
			return null;
		}
		LeaderMailService leaderMailService = (LeaderMailService)getSpringService("leaderMailService");
		String sn = leaderMailService.getSN();
		LeaderMail mail = new LeaderMail();
		mail.setId(UUIDLongGenerator.generateId()); //ID
		mail.setType(type); //类型
		mail.setPopedom(popedom); //事件辖区
		mail.setArea(area); //事件地点
		mail.setHappenTime(happenTime==null ? null : new Timestamp(happenTime.getTimeInMillis())); //事件时间
		mail.setSn(sn); //编号
		mail.setQueryPassword(queryPassword); //查询密码
		mail.setSubject(subject); //主题
		mail.setCreator(creator); //创建人姓名
		mail.setCreated(DateTimeUtils.now()); //创建时间
		mail.setCreatorTel(creatorTel); //联系电话
		mail.setCreatorMail(creatorMail); //邮箱
		mail.setCreatorSex('M'); //性别
		mail.setCreatorCertificateName(creatorCertificateName); //创建人证件名称
		mail.setCreatorIdentityCard(creatorIdentityCard); //创建人身份证/证件号码
		mail.setCreatorIP(creatorIP); //创建人IP
		mail.setCreatorMobile(creatorMobile); //创建人手机
		mail.setCreatorFax(creatorFax); //创建人传真
		mail.setCreatorUnit(creatorUnit); //创建人所在单位
		mail.setCreatorJob(creatorJob); //创建人职业
		mail.setCreatorAddress(creatorAddress); //创建人地址
		mail.setCreatorPostalcode(creatorPostalcode); //创建人邮编
		mail.setIsPublic(isPublic ? '1' : '0'); //是否允许公开
		//mail.setremark; //附注
		mail.setSiteId(siteId); //隶属站点ID
		mail.setContent(content); //正文
		leaderMailService.save(mail);
		return sn;
	}
}