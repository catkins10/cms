package com.yuanluesoft.jeaf.opinionmanage.processor;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class OpinionsProcessor extends RecordListProcessor {
	private OrgService orgService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		int recordCount = recordListModel.getRecordCount();
		recordListModel.setRecordCount(10000);
		RecordListData recordListData = super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		//获取意见类型
		String opinionType = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "opinionType");
		if(opinionType!=null && !opinionType.isEmpty() && !"全部".equals(opinionType)) {
			recordListData.setRecords(ListUtils.getSubListByProperty(recordListData.getRecords(), "opinionType", opinionType));
		}
		if(recordListData.getRecords()==null || beginRow > recordListData.getRecords().size()) {
			recordListData.setRecords(null);
		}
		else {
			recordListData.setRecords(recordListData.getRecords().subList(beginRow, Math.min(beginRow + recordCount, recordListData.getRecords().size())));
		}
		recordListData.setRecordCount(recordListData.getRecords()==null ? 0 : recordListData.getRecords().size());
		if(recordListData.getRecordCount()==0) {
			return recordListData;
		}
		//检查记录格式中是否包含“意见填写人所在单位”或者“意见填写人所在部门”,如果有则获取对应的单位或部门名称
		if(recordListModel.getRecordFormat().indexOf("unitName")!=-1) {
			for(Iterator iterator = recordListData.getRecords().iterator(); iterator.hasNext();) {
				Opinion opinion = (Opinion)iterator.next();
				Org unit = orgService.getPersonalUnitOrSchool(opinion.getPersonId());
				if(unit==null) {
					unit = orgService.getPersonalUnitOrSchool(opinion.getAgentId());
				}
				opinion.setUnitName(unit==null ? null : unit.getDirectoryName());
			}
		}
		if(recordListModel.getRecordFormat().indexOf("departmentName")!=-1) {
			for(Iterator iterator = recordListData.getRecords().iterator(); iterator.hasNext();) {
				Opinion opinion = (Opinion)iterator.next();
				Org department = orgService.getPersonalDepartment(opinion.getPersonId());
				if(department==null) {
					department = orgService.getPersonalDepartment(opinion.getAgentId());
				}
				opinion.setDepartmentName(department==null ? null : department.getDirectoryName());
			}
		}
		return recordListData;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}
}