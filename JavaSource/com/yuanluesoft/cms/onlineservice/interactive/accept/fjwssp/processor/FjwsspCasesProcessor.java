package com.yuanluesoft.cms.onlineservice.interactive.accept.fjwssp.processor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.interactive.accept.fjwssp.service.FjwsspService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class FjwsspCasesProcessor extends RecordListProcessor {
	private FjwsspService fjwsspService; //福建省网上审批服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		return new RecordListData(fjwsspService.listCases());
	}

	/**
	 * @return the fjwsspService
	 */
	public FjwsspService getFjwsspService() {
		return fjwsspService;
	}

	/**
	 * @param fjwsspService the fjwsspService to set
	 */
	public void setFjwsspService(FjwsspService fjwsspService) {
		this.fjwsspService = fjwsspService;
	}
}