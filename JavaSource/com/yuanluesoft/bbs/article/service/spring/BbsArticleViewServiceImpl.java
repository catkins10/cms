package com.yuanluesoft.bbs.article.service.spring;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsReply;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class BbsArticleViewServiceImpl extends ViewServiceImpl {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.view.model.ViewPackage, java.lang.String, java.lang.String, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List records = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		if(records==null || records.isEmpty()) {
			return records;
		}
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
			BbsArticle article = (BbsArticle)iterator.next();
			//获取回帖数量
			String hql = "select count(BbsReply.id)" +
						 " from BbsReply BbsReply" +
						 " where BbsReply.articleId=" + article.getId() +
						 " and not (BbsReply.created is null)";
			Object result = getDatabaseService().findRecordByHql(hql);
			int count = result!=null ? ((Number)result).intValue() : 0;
			Timestamp lastTime = article.getCreated();
			if(count>0) {
				article.setReplyCount(count);
				//获取最后回复人和回复时间
				hql = "select BbsReply" +
					  " from BbsReply BbsReply" +
					  " where BbsReply.articleId=" + article.getId() +
					  " and not (BbsReply.created is null)" +
					  " order by BbsReply.created DESC";
				BbsReply reply = (BbsReply)getDatabaseService().findRecordByHql(hql);
				article.setLastReply(reply);
				lastTime = reply.getCreated();
			}
			//创建或回复时间在24小时以内的,则视为有新帖
			article.setNewReply(lastTime.getTime() > Calendar.getInstance().getTimeInMillis() - 24 * 3600 * 1000);
		}
		return records;
	}
}
