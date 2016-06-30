package com.yuanluesoft.cms.complaint.service.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.complaint.pojo.Complaint;
import com.yuanluesoft.cms.complaint.pojo.ComplaintType;
import com.yuanluesoft.cms.complaint.service.ComplaintService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.smssubscription.model.SmsContentDefinition;
import com.yuanluesoft.cms.smssubscription.service.SmsContentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ComplaintServiceImpl extends PublicServiceImpl implements ComplaintService {
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof ComplaintType) {
			getExchangeClient().synchUpdate(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof ComplaintType) {
			getExchangeClient().synchUpdate(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof ComplaintType) {
			getExchangeClient().synchDelete(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
		super.delete(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.complaint.service.ComplaintService#listTypes(long)
	 */
	public List listTypes(long siteId) throws ServiceException {
		String hql = "from ComplaintType ComplaintType" +
					 " where ComplaintType.siteId=" + siteId + 
					 " order By ComplaintType.type";
		List types = getDatabaseService().findRecordsByHql(hql);
		if(siteId==0 || (types!=null && !types.isEmpty())) { //配置不为空
			return types;
		}
		//配置为空,获取上级站点的配置
		List parentDirectories = getSiteService().listParentDirectories(siteId, null);
		if(parentDirectories!=null && !parentDirectories.isEmpty()) {
			for(int i=parentDirectories.size() - 1; i>=0; i--) {
				WebDirectory site = (WebDirectory)parentDirectories.get(i);
				if(!"site".equals(site.getDirectoryType())) {
					continue;
				}
				hql = "from ComplaintType ComplaintType" +
					  " where ComplaintType.siteId=" + site.getId() + 
					  " order By ComplaintType.type";
				types = getDatabaseService().findRecordsByHql(hql);
				if(types!=null && !types.isEmpty()) { //配置不为空
					return types;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		WebSite parentSite = (WebSite)request.getAttribute("parentSite");
		List types = listTypes(parentSite==null ? 0 : parentSite.getId());
		if(types==null || types.isEmpty()) {
			return types;
		}
		//转换为Object[]列表
		for(int i=0; i<types.size(); i++) {
			ComplaintType type = (ComplaintType)types.get(i);
			types.set(i, new Object[]{type.getType(), type.getType()});
		}
		return types;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#listSmsContentDefinitions()
	 */
	public List listSmsContentDefinitions() throws ServiceException {
		List contentDefinitions = new ArrayList();
		contentDefinitions.add(new SmsContentDefinition("投诉建议", "提交投诉建议,返回受理编号", SmsContentService.SEND_MODE_REPLY, "查询密码,正文"));
		return contentDefinitions;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsContentService#getSmsReplyContent(java.lang.String, java.util.Map, java.lang.String, java.lang.String, long)
	 */
	public String getSmsReplyContent(String contentName, Map fieldValueMap, String message, String senderNumber, long siteId) throws ServiceException {
		Complaint complaint;
		try {
			complaint = (Complaint)smsSubmit(Complaint.class, fieldValueMap, senderNumber, siteId);
		} 
		catch (Exception e) {
			Logger.exception(e);
			return "系统错误，提交失败。";
		}
		List types = listTypes(siteId);
		if(types!=null && !types.isEmpty()) {
			complaint.setWorkingDay(((ComplaintType)types.get(0)).getWorkingDay()); //设置为第一个信件类型的办理期限
		}
		complaint.setType("短信提交");
		update(complaint);
		return "提交完成，受理编号：" + complaint.getSn() + "。";
	}
}