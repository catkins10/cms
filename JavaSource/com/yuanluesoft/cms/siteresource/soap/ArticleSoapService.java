package com.yuanluesoft.cms.siteresource.soap;

import java.sql.Timestamp;
import java.util.Calendar;

import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.soap.BaseSoapService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ArticleSoapService extends BaseSoapService {
	
	/**
	 * 创建文章
	 * @param localArticleId 文章在自己系统中的ID,不能为空
	 * @param localArticleUrl 文章在自己系统中的URL,不能为空
	 * @param columnIds 需要发布的栏目ID列表,用逗号分隔,不能为空
	 * @param subject 主题,不能为空
	 * @param subhead 副标题,允许为空
	 * @param body 正文,不能为空
	 * @param source 来源,允许为空
	 * @param author 作者,允许为空
	 * @param keyword 主题词,允许为空
	 * @param mark 文件字,允许为空
	 * @param created 创建时间,允许为空
	 * @param issueTime 发布时间,直接发布时有效
	 * @param isDirectIssue 是否直接发布
	 * @param ssoSessionId SSO会话ID
	 * @throws Exception
	 */
	public void createArticle(String localArticleId, String localArticleUrl, String columnIds, String subject, String subhead, String body, String source, String author, String keyword, String mark, Calendar created, Calendar issueTime, boolean isDirectIssue, String ssoSessionId) throws ServiceException, SoapException {
		SiteResourceService siteResourceService = (SiteResourceService)getSpringService("siteResourceService");
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		siteResourceService.addResource(columnIds,
										SiteResourceService.RESOURCE_TYPE_ARTICLE,
										subject,
										subhead,
										source,
										author,
										keyword,
										mark,
										SiteResourceService.ANONYMOUS_LEVEL_AUTO,
										null,
										created==null ? DateTimeUtils.now() : new Timestamp(created.getTimeInMillis()),
										!isDirectIssue ? null : (issueTime==null ? DateTimeUtils.now() : new Timestamp(issueTime.getTimeInMillis())),
										isDirectIssue,
										body,
										localArticleId,
										null,
										localArticleUrl,
										sessionInfo.getUserId(),
										sessionInfo.getUserName(),
										sessionInfo.getDepartmentId(),
										sessionInfo.getDepartmentName(),
										sessionInfo.getUnitId(),
										sessionInfo.getUnitName());
	}

	/**
	 * 创建文章
	 * @param localArticleId 文章在自己系统中的ID,不能为空
	 * @param localArticleUrl 文章在自己系统中的URL,不能为空
	 * @param columnIds 需要发布的栏目ID列表,用逗号分隔,不能为空
	 * @param subject 主题,不能为空
	 * @param subhead 副标题,允许为空
	 * @param body 正文,不能为空
	 * @param source 来源,允许为空
	 * @param author 作者,允许为空
	 * @param keyword 主题词,允许为空
	 * @param mark 文件字,允许为空
	 * @param created 创建时间,允许为空
	 * @param issueTime 发布时间,直接发布时有效
	 * @param isDirectIssue 是否直接发布
	 * @param creatorId 用户ID
	 * @throws Exception
	 */
	public void createArticle(String localArticleId, String localArticleUrl, String columnIds, String subject, String subhead, String body, String source, String author, String keyword, String mark, Calendar created, Calendar issueTime, boolean isDirectIssue, long creatorId) throws Exception {
		SiteResourceService siteResourceService = (SiteResourceService)getSpringService("siteResourceService");
		PersonService personService = (PersonService)getSpringService("personService");
		SessionService sessionService = (SessionService)getSpringService("sessionService");
		SessionInfo sessionInfo = sessionService.getSessionInfo(personService.getPerson(creatorId).getLoginName());
		siteResourceService.addResource(columnIds,
										SiteResourceService.RESOURCE_TYPE_ARTICLE,
										subject,
										subhead,
										source,
										author,
										keyword,
										mark,
										SiteResourceService.ANONYMOUS_LEVEL_AUTO,
										null,
										created==null ? DateTimeUtils.now() : new Timestamp(created.getTimeInMillis()),
										!isDirectIssue ? null : (issueTime==null ? DateTimeUtils.now() : new Timestamp(issueTime.getTimeInMillis())),
										isDirectIssue,
										body,
										localArticleId,
										null,
										localArticleUrl,
										sessionInfo.getUserId(),
										sessionInfo.getUserName(),
										sessionInfo.getDepartmentId(),
										sessionInfo.getDepartmentName(),
										sessionInfo.getUnitId(),
										sessionInfo.getUnitName());
	}
} 