package com.yuanluesoft.bbs.article.service.spring;

import java.util.HashSet;
import java.util.Iterator;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsArticleSubjection;
import com.yuanluesoft.bbs.article.service.BbsArticleService;
import com.yuanluesoft.bbs.usermanage.service.BbsUserService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.model.HTMLBodyInfo;
import com.yuanluesoft.jeaf.htmlparser.util.HTMLBodyUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * TODO：论坛版块增加是否内部版块,用来判断是否需要做同步
 * @author yuanluesoft
 *
 */
public class BbsArticleServiceImpl extends BusinessServiceImpl implements BbsArticleService {
	private BbsUserService bbsUserService; //BBS用户服务
	private ExchangeClient exchangeClient; //数据交换客户端
	private PageService pageService; //页面服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		BbsArticle article = (BbsArticle)record;
		try {
			updateAttachmentInfo(article);
		} 
		catch (Exception e) {
			
		}
		super.save(record);
		//同步更新
		exchangeClient.synchUpdate(article, null, 2000);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(article, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return article;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		BbsArticle article = (BbsArticle)record;
		try {
			updateAttachmentInfo(article);
		} 
		catch (Exception e) {
			
		}
		super.update(record);
		//同步更新
		exchangeClient.synchUpdate(article, null, 2000);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(article, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return article;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		BbsArticle article = (BbsArticle)record;
		if(article.getCreated()!=null) {
			//更新用户的发帖记录数
			bbsUserService.increasePost(article.getCreatorId(), -1);
		}
		super.delete(record);
		//同步删除
		exchangeClient.synchDelete(article, null, 2000);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(article, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
	}
	
	/**
	 * 更新图片和视频数量
	 * @param resource
	 */
	private void updateAttachmentInfo(BbsArticle article) throws Exception {
		HTMLBodyInfo htmlBodyInfo = HTMLBodyUtils.analysisHTMLBody(article, article.getBody(), null);
		if(htmlBodyInfo.isBodyChanged()) {
			article.setBody(htmlBodyInfo.getNewBody());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bbs.article.service.BbsArticleService#press(com.yuanluesoft.bbs.article.pojo.BbsArticle)
	 */
	public void press(BbsArticle article) throws ServiceException {
		if(article.getCreated()==null) {
			//设置创建时间,作为已发布的标记
			article.setCreated(DateTimeUtils.now());
			update(article);
			//更新用户的发帖记录数
			bbsUserService.increasePost(article.getCreatorId(), 1);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.article.service.BbsArticleService#updateArticleSubjections(com.yuanluesoft.bbs.article.pojo.BbsArticle, boolean, java.lang.String)
	 */
	public void updateArticleSubjections(BbsArticle article, boolean isNew, String subjectionForumIds) throws ServiceException {
		if(subjectionForumIds==null || subjectionForumIds.isEmpty()) {
			return;
		}
		String[] ids = subjectionForumIds.split(",");
		long firstDirectoryId = Long.parseLong(ids[0]);
		boolean firstDirectoryChanged = true;
		String oldSubjectionDirectoryIds = null; //旧的目录隶属关系
		if(!isNew) {
			oldSubjectionDirectoryIds = ListUtils.join(article.getSubjections(), "forumId", ",", false);
			//检查隶属版块是否发生变化
			if(subjectionForumIds.equals(oldSubjectionDirectoryIds)) {
				return;
			}
			firstDirectoryChanged = article.getSubjections()==null || article.getSubjections().isEmpty() || (firstDirectoryId!=((BbsArticleSubjection)article.getSubjections().iterator().next()).getForumId());
			//删除旧的隶属关系
			for(Iterator iterator = article.getSubjections()==null ? null : article.getSubjections().iterator(); iterator!=null && iterator.hasNext();) {
				BbsArticleSubjection subjection = (BbsArticleSubjection)iterator.next();
				getDatabaseService().deleteRecord(subjection);
				if(("," + subjectionForumIds + ",").indexOf("," + subjection.getForumId() + ",")==-1) { //已经被删除了
					//重建静态页面
					pageService.rebuildStaticPageForModifiedObject(subjection, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
				}
			}
		}
		//保存新的隶属关系
		article.setSubjections(new HashSet());
		for(int i=0; i<ids.length; i++) {
			BbsArticleSubjection subjection = new BbsArticleSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setArticleId(article.getId());
			subjection.setForumId(Long.parseLong(ids[i]));
			getDatabaseService().saveRecord(subjection);
			article.getSubjections().add(subjection);
			if(!firstDirectoryChanged && ("," + oldSubjectionDirectoryIds + ",").indexOf("," + subjection.getForumId() + ",")==-1) { //新增的
				//重建静态页面
				pageService.rebuildStaticPageForModifiedObject(subjection, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
		}
		if(firstDirectoryChanged) {
			update(article);
		}
		else {
			//同步更新
			exchangeClient.synchUpdate(article, null, 2000);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.article.service.BbsArticleService#getArticle(long)
	 */
	public BbsArticle getArticle(long articleId) throws ServiceException {
		BbsArticle bbsArticle = (BbsArticle)load(BbsArticle.class, articleId);
		bbsArticle.setAuthor(bbsUserService.getBbsUserModel(bbsArticle.getCreatorId()));
		return bbsArticle;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.article.service.BbsArticleService#getNextArticle(com.yuanluesoft.bbs.article.pojo.BbsArticle, long)
	 */
	public BbsArticle getNextArticle(BbsArticle article, long forumId) throws ServiceException {
		return getArticle(article, forumId, true);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.article.service.BbsArticleService#getPreviousArticle(com.yuanluesoft.bbs.article.pojo.BbsArticle, long)
	 */
	public BbsArticle getPreviousArticle(BbsArticle article, long forumId) throws ServiceException {
		return getArticle(article, forumId, false);
	}
	
	/**
	 * 获取文章
	 * @param article
	 * @param forumId
	 * @param isNext
	 * @return
	 * @throws ServiceException
	 */
	private BbsArticle getArticle(BbsArticle article, long forumId, boolean isNext) throws ServiceException {
		if(article.getCreated()==null) {
			return null;
		}
		String hql = "select BbsArticle" +
					 " from BbsArticle BbsArticle left join BbsArticle.subjections BbsArticleSubjection" +
					 " where BbsArticleSubjection.forumId=" + forumId + 
					 " and BbsArticle.id!=" + article.getId() +
					 " and BbsArticle.created" + (isNext ? "<=" : ">=") + "TIMESTAMP(" + DateTimeUtils.formatTimestamp(article.getCreated(), null) + ")" +
					 " order by BbsArticle.created" + (isNext ? " DESC" : "");
		return (BbsArticle)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.article.service.BbsArticleService#increaseAccessCount(com.yuanluesoft.bbs.article.pojo.BbsArticle)
	 */
	public synchronized void increaseAccessCount(BbsArticle bbsArticle) throws ServiceException {
		bbsArticle.setAccessTimes(bbsArticle.getAccessTimes() + 1);
		getDatabaseService().updateRecord(bbsArticle);
	}
	
	/**
	 * @return the bbsUserService
	 */
	public BbsUserService getBbsUserService() {
		return bbsUserService;
	}

	/**
	 * @param bbsUserService the bbsUserService to set
	 */
	public void setBbsUserService(BbsUserService bbsUserService) {
		this.bbsUserService = bbsUserService;
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}
}
