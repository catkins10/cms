package com.yuanluesoft.bbs.article.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bbs.article.pojo.BbsReply;
import com.yuanluesoft.bbs.usermanage.service.BbsUserService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class BbsReplyViewServiceImpl extends ViewServiceImpl {
	private BbsUserService bbsUserService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.view.model.ViewPackage, java.lang.String, java.lang.String, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List records = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		if(records==null || records.isEmpty()) {
			return records;
		}
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
			BbsReply reply = (BbsReply)iterator.next();
			reply.setAuthor(bbsUserService.getBbsUserModel(reply.getCreatorId()));
		}
		return records;
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
	
}
