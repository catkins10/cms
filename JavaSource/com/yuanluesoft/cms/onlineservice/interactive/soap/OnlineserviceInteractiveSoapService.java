package com.yuanluesoft.cms.onlineservice.interactive.soap;

import com.yuanluesoft.cms.onlineservice.interactive.complaint.pojo.OnlineServiceComplaint;
import com.yuanluesoft.cms.onlineservice.interactive.consult.pojo.OnlineServiceConsult;
import com.yuanluesoft.cms.onlineservice.interactive.services.OnlineserviceInteractiveService;
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
public class OnlineserviceInteractiveSoapService extends BaseSoapService {

	/**
	 * 创建咨询
	 * @param itemId
	 * @param itemName
	 * @param subject
	 * @param queryPassword
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
	public String createConsult(long itemId, String itemName, String subject, String queryPassword, String creator, String creatorTel, String creatorMail, String creatorCertificateName, String creatorIdentityCard, String creatorIP, String creatorMobile, String creatorFax, String creatorUnit, String creatorJob, String creatorAddress, String creatorPostalcode, boolean isPublic, long siteId, String content, String ssoSessionId) throws ServiceException, SoapException {
		if(getSessionInfo(ssoSessionId)==null) {
			return null;
		}
		OnlineserviceInteractiveService onlineserviceInteractiveService = (OnlineserviceInteractiveService)getSpringService("onlineserviceInteractiveService");
		String sn = onlineserviceInteractiveService.getSN();
		OnlineServiceConsult consult = new OnlineServiceConsult();
		consult.setId(UUIDLongGenerator.generateId()); //ID
		consult.setItemId(itemId); //办理事项ID
		consult.setItemName(itemName); //办理事项名称
		consult.setSn(sn); //编号
		consult.setQueryPassword(queryPassword); //查询密码
		consult.setSubject(subject); //主题
		consult.setCreator(creator); //创建人姓名
		consult.setCreated(DateTimeUtils.now()); //创建时间
		consult.setCreatorTel(creatorTel); //联系电话
		consult.setCreatorMail(creatorMail); //邮箱
		consult.setCreatorSex('M'); //性别
		consult.setCreatorCertificateName(creatorCertificateName); //创建人证件名称
		consult.setCreatorIdentityCard(creatorIdentityCard); //创建人身份证/证件号码
		consult.setCreatorIP(creatorIP); //创建人IP
		consult.setCreatorMobile(creatorMobile); //创建人手机
		consult.setCreatorFax(creatorFax); //创建人传真
		consult.setCreatorUnit(creatorUnit); //创建人所在单位
		consult.setCreatorJob(creatorJob); //创建人职业
		consult.setCreatorAddress(creatorAddress); //创建人地址
		consult.setCreatorPostalcode(creatorPostalcode); //创建人邮编
		consult.setIsPublic(isPublic ? '1' : '0'); //是否允许公开
		//consult.setremark; //附注
		consult.setSiteId(siteId); //隶属站点ID
		consult.setContent(content); //正文
		onlineserviceInteractiveService.save(consult);
		return sn;
	}
	
	/**
	 * 创建投诉
	 * @param itemId
	 * @param itemName
	 * @param subject
	 * @param queryPassword
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
	public String createComplaint(long itemId, String itemName, String subject, String queryPassword, String creator, String creatorTel, String creatorMail, String creatorCertificateName, String creatorIdentityCard, String creatorIP, String creatorMobile, String creatorFax, String creatorUnit, String creatorJob, String creatorAddress, String creatorPostalcode, boolean isPublic, long siteId, String content, String ssoSessionId) throws ServiceException, SoapException {
		if(getSessionInfo(ssoSessionId)==null) {
			return null;
		}
		OnlineserviceInteractiveService onlineserviceInteractiveService = (OnlineserviceInteractiveService)getSpringService("onlineserviceInteractiveService");
		String sn = onlineserviceInteractiveService.getSN();
		OnlineServiceComplaint complaint = new OnlineServiceComplaint();
		complaint.setId(UUIDLongGenerator.generateId()); //ID
		complaint.setItemId(itemId); //办理事项ID
		complaint.setItemName(itemName); //办理事项名称
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
		onlineserviceInteractiveService.save(complaint);
		return sn;
	}
}