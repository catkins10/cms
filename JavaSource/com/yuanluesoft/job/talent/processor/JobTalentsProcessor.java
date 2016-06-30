package com.yuanluesoft.job.talent.processor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author linchuan
 *
 */
public class JobTalentsProcessor extends RecordListProcessor {
	private JobTalentService jobTalentService; //人才服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		if("recommendJobs".equals(view.getName())) { //职位推荐(个人)
			SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
			if(sessionInfo!=null && sessionInfo.getUserType()==JobTalentService.PERSON_TYPE_JOB_TALENT) {
				return new RecordListData(jobTalentService.listRecommendJobs(sessionInfo.getUserId(), view.getPageRows()));
			}
			return null;
		}
		return super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
	}

	/**
	 * @return the jobTalentService
	 */
	public JobTalentService getJobTalentService() {
		return jobTalentService;
	}

	/**
	 * @param jobTalentService the jobTalentService to set
	 */
	public void setJobTalentService(JobTalentService jobTalentService) {
		this.jobTalentService = jobTalentService;
	}
}