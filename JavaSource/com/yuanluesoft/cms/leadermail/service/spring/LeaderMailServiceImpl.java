package com.yuanluesoft.cms.leadermail.service.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.leadermail.pojo.LeaderMail;
import com.yuanluesoft.cms.leadermail.pojo.LeaderMailDepartment;
import com.yuanluesoft.cms.leadermail.pojo.LeaderMailType;
import com.yuanluesoft.cms.leadermail.service.LeaderMailService;
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
public class LeaderMailServiceImpl extends PublicServiceImpl implements LeaderMailService {
	private boolean alwaysPublishAll = false; //发布时,是否总是公开信件的全部信息
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if((record instanceof LeaderMailType) ) {
			getExchangeClient().synchUpdate(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof LeaderMailType) {
			getExchangeClient().synchUpdate(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof LeaderMailType) {
			getExchangeClient().synchDelete(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.leadermail.service.LeaderMailService#listTypes(long)
	 */
	public List listTypes(long siteId) throws ServiceException {
		String hql = "from LeaderMailType LeaderMailType" +
					 " where LeaderMailType.siteId=" + siteId + 
					 " order By LeaderMailType.type";
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
				hql = "from LeaderMailType LeaderMailType" +
					  " where LeaderMailType.siteId=" + site.getId() + 
					  " order By LeaderMailType.type";
				types = getDatabaseService().findRecordsByHql(hql);
				if(types!=null && !types.isEmpty()) { //配置不为空
					return types;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.leadermail.service.LeaderMailService#getLeaderMailDepartment(long)
	 */
	public LeaderMailDepartment getLeaderMailDepartment(long siteId) throws ServiceException {
		return (LeaderMailDepartment)getDatabaseService().findRecordByHql("from LeaderMailDepartment LeaderMailDepartment where LeaderMailDepartment.siteId=" + siteId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		WebSite parentSite = (WebSite)request.getAttribute("parentSite");
		if("type".equals(itemsName)) { //信件类型
			List types = listTypes(parentSite==null ? 0 : parentSite.getId());
			//转换为Object[]列表
			for(int i=0; i<(types==null ? -1 : types.size()); i++) {
				LeaderMailType type = (LeaderMailType)types.get(i);
				types.set(i, new Object[]{type.getType(), type.getType()});
			}
			return types;
		}
		else if("department".equals(itemsName)) { //受理部门
			LeaderMailDepartment leaderMailDepartment = getLeaderMailDepartment(parentSite==null ? 0 : parentSite.getId());
			if(leaderMailDepartment==null || leaderMailDepartment.getDepartments()==null || leaderMailDepartment.getDepartments().isEmpty()) {
				return null;
			}
			String[] departments = leaderMailDepartment.getDepartments().split(",");
			List items = new ArrayList();
			for(int i=0; i<departments.length; i++) {
				items.add(new Object[]{departments[i], departments[i]});
			}
			return items;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#listSmsContentDefinitions()
	 */
	public List listSmsContentDefinitions() throws ServiceException {
		List contentDefinitions = new ArrayList();
		contentDefinitions.add(new SmsContentDefinition("给领导写信", "给领导写信,返回受理编号", SmsContentService.SEND_MODE_REPLY, "查询密码,正文"));
		return contentDefinitions;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsContentService#getSmsReplyContent(java.lang.String, java.util.Map, java.lang.String, java.lang.String, long)
	 */
	public String getSmsReplyContent(String contentName, Map fieldValueMap, String message, String senderNumber, long siteId) throws ServiceException {
		LeaderMail leaderMail;
		try {
			leaderMail = (LeaderMail)smsSubmit(LeaderMail.class, fieldValueMap, senderNumber, siteId);
		} 
		catch (Exception e) {
			Logger.exception(e);
			return "系统错误，提交失败。";
		}
		List types = listTypes(siteId);
		if(types!=null && !types.isEmpty()) {
			leaderMail.setWorkingDay(((LeaderMailType)types.get(0)).getWorkingDay()); //设置为第一个信件类型的办理期限
		}
		leaderMail.setType("短信提交");
		update(leaderMail);
		return "提交完成，受理编号：" + leaderMail.getSn() + "。";
	}

	/**
	 * @return the alwaysPublishAll
	 */
	public boolean isAlwaysPublishAll() {
		return alwaysPublishAll;
	}

	/**
	 * @param alwaysPublishAll the alwaysPublishAll to set
	 */
	public void setAlwaysPublishAll(boolean alwaysPublishAll) {
		this.alwaysPublishAll = alwaysPublishAll;
	}
}