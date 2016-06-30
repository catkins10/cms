package com.yuanluesoft.appraise.processor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.appraise.pojo.Appraise;
import com.yuanluesoft.appraise.pojo.UnitAppraise;
import com.yuanluesoft.appraise.service.AppraiseService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class AppraisesProcessor extends RecordListProcessor {
	private OrgService orgService; //组织机构服务
	private AppraiseService appraiseService; //评议服务
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		if("appraiseParticipateUnits".equals(view.getName())) { //参评单位
			List webDirectories = listParticipateUnits(recordListModel, parentSite);
			if(webDirectories==null || webDirectories.size()<beginRow) {
				return null;
			}
			int count = webDirectories.size();
			webDirectories = webDirectories.subList(beginRow, Math.min(beginRow + recordListModel.getRecordCount(), webDirectories.size()));
			return new RecordListData(webDirectories, count);
		}
		else if("appraiseVoteTotal".equals(view.getName()) || "yearAppraiseVoteTotal".equals(view.getName())) { //投票统计,本年度投票统计
			//获取隶属单位所在地区
			Org area = orgService.getAreaOfOrg(parentSite.getOwnerUnitId());
			if(area==null) {
				return null;
			}
			return new RecordListData(ListUtils.generateList(appraiseService.appraiseVoteTotal(area.getId(), "yearAppraiseVoteTotal".equals(view.getName()))), 1);
		}
		//判断是否需要重新排序
		boolean resort = "unitAppraises".equals(view.getName()) && //单位评议列表
						 recordListModel.getRecordFormat().indexOf("scoreComprehensive")!=-1 && //配置了综合得分字段
						 recordListModel.getRecordFormat().indexOf("yearScoreComprehensive")==-1; //没有配置年累计综合得分字段
		int pageRows = recordListModel.getRecordCount();
		if(resort) { 
			recordListModel.setRecordCount(10000);
		}
		RecordListData recordListData = super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		if(resort && recordListData!=null && recordListData.getRecords()!=null) {
			sortUnitAppraisesByScoreComprehensive(recordListData.getRecords());
			if(recordListData.getRecords().size()>pageRows) { //单位评议列表
				recordListData.setRecords(recordListData.getRecords().subList(0, pageRows));
			}
		}
		return recordListData;
	}
	
	/**
	 * 按本次评议综合得分排序
	 * @param unitAppraises
	 */
	private void sortUnitAppraisesByScoreComprehensive(List unitAppraises) {
		if(unitAppraises==null || unitAppraises.isEmpty()) {
			return;
		}
		Collections.sort(unitAppraises, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				UnitAppraise unitAppraise0 = (UnitAppraise)arg0;
				UnitAppraise unitAppraise1 = (UnitAppraise)arg1;
				return unitAppraise0.getScoreComprehensive()==unitAppraise1.getScoreComprehensive() ? 0 : (unitAppraise0.getScoreComprehensive()<unitAppraise1.getScoreComprehensive() ? 1 : -1);
			}
		});
	}

	/**
	 * 获取参评单位栏目列表
	 * @param recordListModel
	 * @param parentSite
	 * @return
	 * @throws ServiceException
	 */
	private List listParticipateUnits(RecordList recordListModel, WebSite parentSite) throws ServiceException {
		//获取隶属单位所在地区
		Org area = orgService.getAreaOfOrg(parentSite.getOwnerUnitId());
		if(area==null) {
			return null;
		}
		//获取参评单位列表
		List participateUnits = appraiseService.listParticipateUnits(area.getId(), DateTimeUtils.getYear(DateTimeUtils.date()));
		String category = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "category"); //分类
		if(category!=null && !category.isEmpty()) {
			participateUnits = ListUtils.getSubListByProperty(participateUnits, "category", category);
		}
		//获取对应的栏目列表
		return siteService.listDirectoriesByNames(parentSite.getId(), ListUtils.join(participateUnits, "unitName", ",", false), false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
		if(Appraise.class.getName().equals(view.getPojoClassName())) { //评议
			//获取隶属单位所在地区
			Org area = orgService.getAreaOfOrg(parentSite.getOwnerUnitId());
			view.addWhere("Appraise.areaId=" + (area==null ? -1 : area.getId()));
		}
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

	/**
	 * @return the appraiseService
	 */
	public AppraiseService getAppraiseService() {
		return appraiseService;
	}

	/**
	 * @param appraiseService the appraiseService to set
	 */
	public void setAppraiseService(AppraiseService appraiseService) {
		this.appraiseService = appraiseService;
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