/**
 * 
 */
package com.yuanluesoft.bbs.article.service;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * @author yuanluesoft
 *
 */
public interface BbsArticleService extends BusinessService {
	
	/**
	 * 获取帖子
	 * @param articleId
	 * @return
	 * @throws ServiceException
	 */
	public BbsArticle getArticle(long articleId) throws ServiceException;
	
	/**
	 * 更新论坛隶属的版块
	 * @param article
	 * @param isNew
	 * @param subjectionForumIds
	 * @throws ServiceException
	 */
	public void updateArticleSubjections(BbsArticle article, boolean isNew, String subjectionForumIds) throws ServiceException;
	
	/**
	 * 获取下一主题,不获取附加信息,如作者详情
	 * @param article
	 * @param forumId
	 * @return
	 * @throws ServiceException
	 */
	public BbsArticle getNextArticle(BbsArticle article, long forumId) throws ServiceException;
	
	/**
	 * 获取上一主题,不获取附加信息,如作者详情
	 * @param article
	 * @param forumId
	 * @return
	 * @throws ServiceException
	 */
	public BbsArticle getPreviousArticle(BbsArticle article, long forumId) throws ServiceException;
	
	/**
	 * 发布
	 * @param article
	 * @throws ServiceException
	 */
	public void press(BbsArticle article) throws ServiceException;
	
	/**
	 * 增加文章的访问次数
	 * @param bbsArticle
	 * @throws ServiceException
	 */
	public void increaseAccessCount(BbsArticle bbsArticle) throws ServiceException;
}
