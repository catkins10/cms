package com.yuanluesoft.bidding.project.ask.service.spring;

import java.util.List;

import com.yuanluesoft.bidding.project.ask.pojo.BiddingProjectAsk;
import com.yuanluesoft.bidding.project.ask.service.BiddingProjectAskService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class BiddingProjectAskServiceImpl extends BusinessServiceImpl implements BiddingProjectAskService {
	private boolean needReply; //是否需要应答,南平为true
	private PageService pageService; //页面服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		BiddingProjectAsk ack = (BiddingProjectAsk)record;
		super.update(record);
		pageService.rebuildStaticPageForModifiedObject(ack, ack.getIsPublic()=='1' ? StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE : StaticPageBuilder.OBJECT_MODIFY_ACTION_LOGICAL_DELETE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		BiddingProjectAsk ack = (BiddingProjectAsk)record;
		if(ack.getIsPublic()=='1') {
			pageService.rebuildStaticPageForModifiedObject(ack, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.ask.service.BiddingProjectAskService#listProjectAsks(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listProjectAsks(long projectId, SessionInfo sessionInfo) throws ServiceException {
		if(!needReply) { //不需要应答
			//返回全部的问题
			return getDatabaseService().findRecordsByHql("from BiddingProjectAsk BiddingProjectAsk where BiddingProjectAsk.projectId=" + projectId + " order by BiddingProjectAsk.askTime DESC");
		}
		else { //需要应答
			//返回有读取权限的问题
			return getDatabaseService().findPrivilegedRecords(BiddingProjectAsk.class.getName(), null, null, "BiddingProjectAsk.projectId=" + projectId, "BiddingProjectAsk.askTime DESC", null, RecordControlService.ACCESS_LEVEL_READONLY, false, null, 0, 0, sessionInfo);
		}
	}

	/**
	 * @return the needReply
	 */
	public boolean isNeedReply() {
		return needReply;
	}

	/**
	 * @param needReply the needReply to set
	 */
	public void setNeedReply(boolean needReply) {
		this.needReply = needReply;
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
}
