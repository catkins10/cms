package com.yuanluesoft.cms.sitemanage.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.PagingElementProcessor;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.cms.sitemanage.model.IssueTotal;
import com.yuanluesoft.cms.sitemanage.model.TotalColumn;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.report.graph.GraphReportService;
import com.yuanluesoft.jeaf.report.graph.model.ChartData;
import com.yuanluesoft.jeaf.stat.service.StatService;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 统计列表处理器,栏目排行(特定栏目)处理器
 * @author linchuan
 *
 */
public class TotalsProcessor extends RecordListProcessor {
	private HTMLParser htmlParser; //HTML解析器
	private StatService statService; //访问统计服务
	private DatabaseService databaseService; //数据库服务
	private GraphReportService graphReportService; //图形报表服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, boolean, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//解析记录列表模型
		RecordList recordListModel = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, pageElement.getAttribute("urn"), null);
		if("accessTotals".equals(recordListModel.getRecordListName())) { //统计访问次数
			//获取输出方式
			String writeMode = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "writeMode");
			if("totalAll".equals(writeMode)) { //统计总数
				View view = getPageDefineService().getRecordList(recordListModel.getApplicationName(), recordListModel.getRecordListName(), recordListModel.isPrivateRecordList(), recordListModel.getRecordClassName(), RequestUtils.getSessionInfo(request));
				RecordListData recordListData = readRecordListData(view, recordListModel, null, 0, false, false, webDirectory, parentSite, sitePage, request);
				List totals = recordListData==null ? null : recordListData.getRecords();
				int total = 0;
				for(Iterator iterator = totals==null ? null : totals.iterator(); iterator!=null && iterator.hasNext();) {
					TotalColumn totalColumn = (TotalColumn)iterator.next();
					total += totalColumn.getTotal();
				}
				pageElement.getParentNode().replaceChild(pageElement.getOwnerDocument().createTextNode("" + total), pageElement);
				return;
			}
			else if(!"text".equals(writeMode)) { //输出图表
				HTMLImageElement image = (HTMLImageElement)pageElement.getOwnerDocument().createElement("img");
				image.setBorder("0");
				//把当前记录列表模型写入会话
				long chartId = UUIDLongGenerator.generateId();
				request.getSession().setAttribute("" + chartId, recordListModel);
				image.setSrc(Environment.getContextPath() + "/cms/sitemanage/accessTotalChart.shtml?chartId=" + chartId);
				pageElement.getParentNode().replaceChild(image, pageElement);
				return;
			}
		}
		super.writePageElement(pageElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		if(view.getName().equals("totals")) { //统计记录数
			List records = recordTotal(view, recordListModel, webDirectory, parentSite, sitePage, request);
			return records==null ? null : new RecordListData(records, records.size());
		}
		else if(view.getName().equals("accessTotals")) { //统计访问次数
			List records = accessTotal(view, recordListModel, webDirectory, parentSite, sitePage, request);
			return records==null ? null : new RecordListData(records, records.size());
		}
		else if(view.getName().equals("issueTotals")) { //发布统计
			List records = issueTotal(view, recordListModel, beginRow, webDirectory, parentSite, sitePage, request);
			records = records.subList(beginRow, Math.min(beginRow + recordListModel.getRecordCount(), records.size()));
			return records==null ? null : new RecordListData(records, 0);
		}
		return null;
	}
	
	/**
	 * 统计记录数
	 * @param view
	 * @param recordListModel
	 * @param beginRow
	 * @param pageRows
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected List recordTotal(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		//统计栏目数量
		int totalCount = StringUtils.getPropertyIntValue(recordListModel.getExtendProperties(), "totalCount", 0);
		if(totalCount==0) {
			return null;
		}
		//解析统计栏目列表，并获取记录数
		List totalColumns = new ArrayList();
		for(int i=0; i<totalCount; i++) {
			try {
				TotalColumn totalColumn = new TotalColumn();
				String totalColumnProperties = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "total" + i);
				totalColumn.setName(StringUtils.getPropertyValue(totalColumnProperties, "totalTitle")); //设置统计栏目名称
				totalColumn.setLink(StringUtils.getPropertyValue(totalColumnProperties, "totalLink")); //链接
				
				//构造一个记录列表
				RecordList recordList = new RecordList();
				recordList.setExtendProperties(StringUtils.getPropertyValue(totalColumnProperties, "totalRecordListProperties")); //记录列表扩展属性
				recordList.setRecordListName(StringUtils.getPropertyValue(totalColumnProperties, "totalName")); //记录列表名称
				recordList.setApplicationName(StringUtils.getPropertyValue(totalColumnProperties, "totalApplication")); //记录列表应用名称
				
				sitePage = (SitePage)sitePage.clone();
				//解析记录列表
				HTMLDocument recordListDocument = htmlParser.parseHTMLString(RecordListUtils.gernerateRecordListElement(recordList, true), "utf-8");
				//处理记录列表,获取记录数
				getPageBuilder().processPageElement(recordListDocument.getBody(), webDirectory, parentSite, sitePage, RequestUtils.getRequestInfo(request), request);
				//输出记录数
				Number total = (Number)sitePage.getAttribute(PagingElementProcessor.PAGING_ATTRIBUTE_RECORD_COUNT);
				totalColumn.setTotal(total==null ? 0 : total.intValue());
				totalColumns.add(totalColumn); //添加到列表
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		//排序
		if("true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "totalSort")) && !totalColumns.isEmpty()) {
			Collections.sort(totalColumns, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					TotalColumn column0 = (TotalColumn)arg0;
					TotalColumn column1 = (TotalColumn)arg1;
					return column0.getTotal()==column1.getTotal() ? 0 : (column0.getTotal()<column1.getTotal() ? 1 : -1);
				}
			});
		}
		return totalColumns;
	}
	
	/**
	 * 访问次数统计
	 * @param view
	 * @param recordListModel
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected List accessTotal(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		//统计栏目数量
		int totalCount = StringUtils.getPropertyIntValue(recordListModel.getExtendProperties(), "totalCount", 0);
		if(totalCount==0) {
			return null;
		}
		
		//解析统计栏目列表，并获取记录数
		List totalColumns = new ArrayList();
		for(int i=0; i<totalCount; i++) {
			try {
				TotalColumn totalColumn = new TotalColumn();
				String totalColumnProperties = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "total" + i);
				totalColumn.setName(StringUtils.getPropertyValue(totalColumnProperties, "totalTitle")); //设置统计栏目名称
				totalColumn.setLink(StringUtils.getPropertyValue(totalColumnProperties, "totalLink")); //链接
				//获取访问次数
				String[] pageName = StringUtils.getPropertyValue(totalColumnProperties, "pageName").split("__");
				long recordId = StringUtils.getPropertyLongValue(totalColumnProperties, "recordId", -1);
				Long total = (Long)request.getAttribute(pageName[0] + "_" + pageName[1] + "_" + recordId); //从请求中获取,提高统计效率
				if(total==null) {
					total = new Long(statService.getAccessTimes(pageName[0], pageName[1], recordId));
					request.getSession().setAttribute(pageName[0] + "_" + pageName[1] + "_" + recordId, total);
				}
				totalColumn.setTotal(total.intValue());
				totalColumns.add(totalColumn); //添加到列表
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		//排序
		if("true".equals(StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "totalSort")) && !totalColumns.isEmpty()) {
			Collections.sort(totalColumns, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					TotalColumn column0 = (TotalColumn)arg0;
					TotalColumn column1 = (TotalColumn)arg1;
					return column0.getTotal()==column1.getTotal() ? 0 : (column0.getTotal()<column1.getTotal() ? 1 : -1);
				}
			});
		}
		return totalColumns;
	}
	
	/**
	 * 发布统计
	 * @param view
	 * @param recordListModel
	 * @param beginRow
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected List issueTotal(View view, RecordList recordListModel, int beginRow, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String parentOrgIds = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "parentOrgIds");
		String hql = "select SiteResource.unitName, count(SiteResource.id)" +
					 " from SiteResource SiteResource, Org Org" +
					 " where SiteResource.status='" + SiteResourceService.RESOURCE_STATUS_ISSUE + "'" +
					 " and Org.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(parentOrgIds) + ")" +
					 " and SiteResource.unitName=Org.directoryName" +
					 " group by SiteResource.unitName";
		List totals = databaseService.findRecordsByHql(hql);
		if(totals==null || totals.isEmpty()) {
			return null;
		}
		if(totals.size()<beginRow) {
			return null;
		}
		for(int i=0; i<totals.size(); i++) {
			Object[] values = (Object[])totals.get(i);
			IssueTotal issueTotal = new IssueTotal();
			issueTotal.setUnitName((String)values[0]);
			issueTotal.setIssueCount(((Number)values[1]).intValue());
			totals.set(i, issueTotal);
		}
		//排序
		Collections.sort(totals, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				IssueTotal issueTotal0 = (IssueTotal)arg0;
				IssueTotal issueTotal1 = (IssueTotal)arg1;
				return issueTotal0.getIssueCount()==issueTotal1.getIssueCount() ? 0 : (issueTotal0.getIssueCount()<issueTotal1.getIssueCount() ? 1 : -1);
			}
		});
		return totals;
	}
	
	/**
	 * 输出访问统计图表
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void writeAccessTotalChart(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		RecordList recordListModel = (RecordList)request.getSession().getAttribute(request.getParameter("chartId"));
		if(recordListModel==null) {
			return;
		}
		request.getSession().removeAttribute(request.getParameter("chartId"));
		View view = getPageDefineService().getRecordList(recordListModel.getApplicationName(), recordListModel.getRecordListName(), recordListModel.isPrivateRecordList(), recordListModel.getRecordClassName(), RequestUtils.getSessionInfo(request));
		RecordListData recordListData = readRecordListData(view, recordListModel, null, 0, false, false, null, null, null, request);
		List totals = recordListData==null ? null : recordListData.getRecords();
		int chartWidth = StringUtils.getPropertyIntValue(recordListModel.getExtendProperties(), "chartWidth", 640);
		int chartHeight = StringUtils.getPropertyIntValue(recordListModel.getExtendProperties(), "chartHeight", 480);
		String chartMode = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "writeMode");
		List chartDataList = new ArrayList();
		for(Iterator iterator=totals==null ? null : totals.iterator(); iterator!=null && iterator.hasNext();) {
			TotalColumn totalColumn = (TotalColumn)iterator.next();
			chartDataList.add(new ChartData(totalColumn.getName(), totalColumn.getTotal()));
		}
		graphReportService.writeChart(chartDataList, chartWidth, chartHeight, chartMode, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}

	/**
	 * @return the statService
	 */
	public StatService getStatService() {
		return statService;
	}

	/**
	 * @param statService the statService to set
	 */
	public void setStatService(StatService statService) {
		this.statService = statService;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the graphReportService
	 */
	public GraphReportService getGraphReportService() {
		return graphReportService;
	}

	/**
	 * @param graphReportService the graphReportService to set
	 */
	public void setGraphReportService(GraphReportService graphReportService) {
		this.graphReportService = graphReportService;
	}
}