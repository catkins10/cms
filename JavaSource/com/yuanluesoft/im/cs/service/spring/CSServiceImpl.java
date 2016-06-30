package com.yuanluesoft.im.cs.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.im.cs.pojo.CSParameter;
import com.yuanluesoft.im.cs.service.CSService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class CSServiceImpl extends BusinessServiceImpl implements CSService {
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.cs.service.CSService#loadParameter(long)
	 */
	public CSParameter loadParameter(long siteId) throws ServiceException {
		String hql = "select CSParameter" +
					 " from CSParameter CSParameter, WebDirectorySubjection WebDirectorySubjection" +
					 " where CSParameter.siteId=WebDirectorySubjection.parentDirectoryId" +
					 " and WebDirectorySubjection.directoryId=" + siteId +
					 " order by WebDirectorySubjection.id";
		return (CSParameter)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.leadermail.service.LeaderMailService#listTypes(long)
	 */
	public List listOftenUsedReplies(long siteId) throws ServiceException {
		String hql = "select CSReply.reply" +
					 " from CSReply CSReply" +
					 " where CSReply.siteId=" + siteId + 
					 " order By CSReply.reply";
		List replies = getDatabaseService().findRecordsByHql(hql);
		if(siteId==0 || (replies!=null && !replies.isEmpty())) { //不为空
			return replies;
		}
		//配置为空,获取上级站点的配置
		List parentDirectories = getSiteService().listParentDirectories(siteId, null);
		if(parentDirectories!=null && !parentDirectories.isEmpty()) {
			for(int i=parentDirectories.size() - 1; i>=0; i--) {
				WebDirectory site = (WebDirectory)parentDirectories.get(i);
				if(!"site".equals(site.getDirectoryType())) {
					continue;
				}
				hql = "select CSReply.reply" +
					  " from CSReply CSReply" +
					  " where CSReply.siteId=" + site.getId() + 
					  " order By CSReply.reply";
				replies = getDatabaseService().findRecordsByHql(hql);
				if(replies!=null && !replies.isEmpty()) { //配置不为空
					return replies;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("oftenUsedReply".equals(itemsName)) { //常用答复
			return listOftenUsedReplies(RequestUtils.getParameterLongValue(request, "siteId"));
		}
		else if("evaluateLevels".equals(itemsName)) { //服务评价等级列表
			CSParameter parameter = loadParameter(RequestUtils.getParameterLongValue(request, "siteId"));
			return ListUtils.generateList(parameter==null || parameter.getEvaluateLevels()==null || parameter.getEvaluateLevels().isEmpty() ? "非常满意,满意,一般,不满意" : parameter.getEvaluateLevels(), ",");
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}