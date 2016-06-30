package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageRecordList;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.pagebuilder.util.PageUtils;
import com.yuanluesoft.cms.pagebuilder.util.RecordListUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.HTMLTraversalCallback;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.callback.FillParametersCallback;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.Link;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.model.search.Condition;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;
import com.yuanluesoft.jeaf.view.util.ViewUtils;

/**
 * 记录列表处理器
 * @author linchuan
 *
 */
public class RecordListProcessor implements PageElementProcessor {
	//记录列表的属性名称
	public static final String RECORDLIST_ATTRIBUTE_RECORDS = "records";
	public static final String RECORDLIST_ATTRIBUTE_SEARCH_CONDITIONS = "searchConditions";
	
	private PageDefineService pageDefineService; //站点应用服务
	private PageBuilder pageBuilder; //页面生成器
	private FormDefineService formDefineService; //表单定义服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, final WebDirectory webDirectory, final WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		String urn = pageElement.getAttribute("urn");
		for(int i=1; ; i++) {
			String value = pageElement.getAttribute("urn" + i);
			if(value==null || value.isEmpty()) {
				break;
			}
			urn += value;
		}
		//解析记录列表模型
		RecordList recordListModel = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, urn, null);
		resetRecordListModel(recordListModel); //重置记录列表模型
		//获取视图定义
		View view = request==null ? null : (View)request.getAttribute("viewDefine");
		if(view!=null) {
			request.removeAttribute("viewDefine"); //清除属性
		}
		else {
			view = pageDefineService.getRecordList(recordListModel.getApplicationName(), recordListModel.getRecordListName(), recordListModel.isPrivateRecordList(), recordListModel.getRecordClassName(), RequestUtils.getSessionInfo(request));
		}
		if(view==null) {
			pageElement.getParentNode().removeChild(pageElement); //删除配置元素
			return;
		}
		//解析记录格式
		HTMLDocument recordFormatDocument = htmlParser.parseHTMLString(recordListModel.getRecordFormat(), "utf-8");
		//重设视图
		resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
		//生成搜索条件列表
		List searchConditions = null;
		if(recordListModel.isSearchResults()) {
			searchConditions = generateSearchConditions(view, recordListModel, webDirectory, parentSite, sitePage, request);
		}
		if(searchConditions!=null && !searchConditions.isEmpty()) {
			sitePage.setAttribute(RECORDLIST_ATTRIBUTE_SEARCH_CONDITIONS, searchConditions);
		}
		//处理需要分页的记录列表
		boolean readRecordsOnly = true;
		boolean countRecordsOnly = false;
		int pageIndex = 1;
		boolean loadMore = "true".equals(RequestUtils.getParameterStringValue(request, "client.loadMore")); //是否加载更多页面
		if(!loadMore && ("paging".equals(pageElement.getAttribute("target")) || "total".equals(pageElement.getAttribute("target")))) { //当前记录列表需要分页显示、或者需要统计
			try {
				pageIndex = Integer.parseInt(request.getParameter("page"));
			}
			catch(Exception e) {
				 
			}
			readRecordsOnly = false;
			countRecordsOnly = "total".equals(pageElement.getAttribute("target")); //统计时,不需要获取记录
		}
		//计算起始行
		int beginRow = 0;
		if(loadMore) { //加载更多页面
			beginRow = Integer.parseInt(RequestUtils.getParameterStringValue(request, "client.recordCount_" + urn.hashCode()));
		}
		else {
			beginRow = (pageIndex-1) * recordListModel.getRecordCount();
		}
		//读取数据
		RecordListData recordListData = readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		request.setAttribute("recordCount", new Integer(recordListData==null ? 0 : recordListData.getRecordCount())); //设置属性,用于输出“记录数”字段
		//设置属性,供分页操作使用
		if(!readRecordsOnly && recordListData!=null) {
			sitePage.setAttribute(PagingElementProcessor.PAGING_ATTRIBUTE_RECORD_COUNT, new Integer(recordListData.getRecordCount()));
			pageIndex = recordListModel.getRecordCount()<=0 ? 1 : Math.max(1, Math.min(recordListData.getRecordCount() - 1, beginRow) / recordListModel.getRecordCount() + 1);
			sitePage.setAttribute(PagingElementProcessor.PAGING_ATTRIBUTE_PAGE_INDEX, new Integer(pageIndex));
			sitePage.setAttribute(PagingElementProcessor.PAGING_ATTRIBUTE_PAGE_ROWS, new Integer(recordListModel.getRecordCount()));
		}
		//处理加载更多页面
		if(loadMore && recordListData!=null && recordListData.getRecords()!=null && !recordListData.getRecords().isEmpty()) {
			long lastRecordId = RequestUtils.getParameterLongValue(request, "client.lastRecordId_" + urn.hashCode()); //最后一条记录ID
			//检查最后一条记录是否出现在结果中
			int index = recordListData.getRecords().size() - 1;
			for(; index>=0; index--) {
				Object record = recordListData.getRecords().get(index);
				if(!(record instanceof Record)) {
					index = -1;
					break;
				}
				if(((Record)record).getId()==lastRecordId) {
					break;
				}
			}
			if(index!=-1) { //找到重复的记录
				RecordListData nextPageData = null; //补足记录
				if(recordListData.getRecords().size()==recordListModel.getRecordCount()) {
					nextPageData = readRecordListData(view, recordListModel, searchConditions, beginRow + recordListModel.getRecordCount(), true, false, webDirectory, parentSite, sitePage, request); 
				}
				recordListData.setRecords(recordListData.getRecords().subList(index+1, recordListData.getRecords().size()));
				if(nextPageData!=null && nextPageData.getRecords()!=null && !nextPageData.getRecords().isEmpty()) {
					recordListData.getRecords().addAll(nextPageData.getRecords());
				}
			}
		}
		//检查记录数是否超出需要显示的记录数
		if(!loadMore && recordListModel.getRecordCount()>0 && //不是加载更多,且记录数不是0
		   recordListData!=null && recordListData.getRecords()!=null && recordListData.getRecords().size()>recordListModel.getRecordCount()) {
			recordListData.setRecords(recordListData.getRecords().subList(0, recordListModel.getRecordCount())); //截取记录列表
		}
		//设置属性,给调用者使用,如：RSS
		sitePage.setAttribute(RECORDLIST_ATTRIBUTE_RECORDS, recordListData==null ? null : recordListData.getRecords());
		//克隆页面,避免属性值被覆盖
		try {
			sitePage = (SitePage)sitePage.clone();
		}
		catch(CloneNotSupportedException e) {
			
		}
		//输出记录列表
		NodeList recordFormatNodes = recordFormatDocument==null ? null : recordFormatDocument.getBody().getChildNodes();
		if(recordFormatNodes!=null && recordFormatNodes.getLength()>0) { //有需要输出的字段
			writeRecords(recordListData==null ? null : recordListData.getRecords(), view, recordListModel, recordFormatNodes, pageIndex, pageElement, webDirectory, parentSite, htmlParser, sitePage, requestInfo, request);
		}
		//删除配置元素
		pageElement.getParentNode().removeChild(pageElement);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#createStaticPageRegenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		if(!sitePage.isRealtimeStaticPage()) { //不需要实时生成静态页面,避免过多的重建静态页面
			return;
		}
		//解析记录列表模型
		RecordList recordListModel = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, pageElement.getAttribute("urn"), null);
		if(recordListModel.isSearchResults()) {
			return;
		}
		//获取视图定义
		View view = pageDefineService.getRecordList(recordListModel.getApplicationName(), recordListModel.getRecordListName(), recordListModel.isPrivateRecordList(), recordListModel.getRecordClassName(), RequestUtils.getSessionInfo(request));
		if(view==null) {
			return;
		}
		createStaticPageRebuildBasisByRecordList(recordListModel, view, staticPageId, pageElement, sitePage, webDirectory, parentSite, htmlParser, databaseService, request);
	}
	
	/**
	 * 根据记录列表创建重建静态页面的依据
	 * @param recordListModel
	 * @param view
	 * @param staticPageId
	 * @param pageElement
	 * @param sitePage
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param databaseService
	 * @param request
	 * @throws ServiceException
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		//获取记录所在的站点/栏目ID列表
		String siteIds = getRecordListSiteIds(recordListModel, view, pageElement, sitePage, webDirectory, parentSite, request);
		if(siteIds==null || siteIds.isEmpty()) { //没有指定站点或栏目ID时,默认就是根站点
			siteIds = "0";
		}
		if(("," + siteIds + ",").indexOf(",0,")!=-1) { //包括主站
			siteIds = "0";
		}
		String[] ids = siteIds.split(",");
		for(int i=0; i<ids.length; i++) {
			if(!"-1".equals(ids[i])) {
				saveStaticPageRecordList(staticPageId, view.getPojoClassName(), Long.parseLong(ids[i]), databaseService);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		String siteIds = getRecordSiteIds(object, databaseService);
		//获取引用了siteIds及其上级站点记录列表的页面
		String hql = "select distinct StaticPage" +
					 " from StaticPage StaticPage left join StaticPage.recordLists StaticPageRecordList, WebDirectorySubjection WebDirectorySubjection" +
					 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
					 " and StaticPageRecordList.siteId=WebDirectorySubjection.parentDirectoryId" +
					 (siteIds==null || siteIds.isEmpty() ? "" : " and WebDirectorySubjection.directoryId in (" + JdbcUtils.validateInClauseNumbers(siteIds) + ")") + //没有指定站点或栏目ID时,更新所有站点的页面
					 " and StaticPageRecordList.recordClassName='" + getRecordClassNameForStaticPage(object) + "'" +
					 " and StaticPageRecordList.processorClassName='" + this.getClass().getName() + "'";
		return databaseService.findRecordsByHql(hql, 0, max);
	}
	
	/**
	 * 保存静态页面引用的记录列表
	 * @param staticPageId
	 * @param recordClassName
	 * @param siteId
	 * @param databaseService
	 */
	public void saveStaticPageRecordList(long staticPageId, String recordClassName, long siteId, DatabaseService databaseService) {
		//检查是否已经创建过
		String hql = "select StaticPageRecordList.id" +
					 " from StaticPageRecordList StaticPageRecordList" +
					 " where StaticPageRecordList.pageId=" + staticPageId +
					 " and StaticPageRecordList.recordClassName='" + recordClassName + "'" +
					 " and StaticPageRecordList.siteId=" + siteId;
		if(databaseService.findRecordByHql(hql)==null) {
			//保存引用的记录列表
			StaticPageRecordList staticPageRecordList = new StaticPageRecordList();
			staticPageRecordList.setId(UUIDLongGenerator.generateId()); //页面ID
			staticPageRecordList.setPageId(staticPageId); //页面ID
			staticPageRecordList.setRecordClassName(recordClassName); //记录类名称
			staticPageRecordList.setProcessorClassName(this.getClass().getName()); //处理器类名称
			staticPageRecordList.setSiteId(siteId); //隶属的站点/栏目ID
			databaseService.saveRecord(staticPageRecordList);
		}
	}
	
	/**
	 * 获取记录列表隶属的站点/栏目ID列表
	 * @param recordListModel
	 * @param view
	 * @param pageElement
	 * @param sitePage
	 * @param webDirectory
	 * @param parentSite
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected String getRecordListSiteIds(RecordList recordListModel, View view, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		return "" + parentSite.getId();
	}
	
	/**
	 * 获取静态页面更新时使用的记录类名称
	 * @param object
	 * @return
	 * @throws ServiceException
	 */
	protected String getRecordClassNameForStaticPage(Object object) throws ServiceException {
		return object.getClass().getName();
	}
	
	/**
	 * 获取记录隶属的站点/栏目ID
	 * @param object
	 * @param databaseService
	 * @return
	 * @throws ServiceException
	 */
	protected String getRecordSiteIds(Object object, DatabaseService databaseService) throws ServiceException {
		try {
			return PropertyUtils.getProperty(object, "siteId").toString();
		}
		catch(Exception e) {
			return null; //记录中没有站点ID
		}
	}
	
	/**
	 * 重设视图,继承者可以在此调整视图属性,如：hql的where子句
	 * @param view
	 * @param pageElement
	 * @param recordListModel
	 * @param recordFormatDocument
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @throws ServiceException
	 */
	protected void resetView(View view, HTMLElement pageElement, final RecordList recordListModel, HTMLDocument recordFormatDocument, final WebDirectory webDirectory, final WebSite parentSite, HTMLParser htmlParser, final SitePage sitePage, HttpServletRequest request) throws ServiceException {
		//把字段列表转换为视图列
		if(recordFormatDocument!=null) {
			NodeList elements = recordFormatDocument.getElementsByTagName("a");
			view.setColumns(new ArrayList());
			for(int i=(elements==null ? -1 : elements.getLength() - 1); i>=0; i--) {
				HTMLAnchorElement a = (HTMLAnchorElement)elements.item(i);
				if("field".equals(a.getId()) && !inTableHeader(a)) { //字段,且不在表头中
					String fieldName = StringUtils.getPropertyValue(a.getAttribute("urn"), "name");
					Column column = view instanceof StatisticView ? new com.yuanluesoft.jeaf.view.statisticview.model.Column() : new Column();
					column.setName(fieldName);
					view.getColumns().add(column);
				}
				else if("recordList".equals(a.getId()) && !inTableHeader(a)) { //内嵌记录列表,且不在表头中
					RecordList nestedRecordListModel = (RecordList)BeanUtils.generateBeanByProperties(RecordList.class, a.getAttribute("urn"), null);
					if(nestedRecordListModel.isPrivateRecordList()) { //私有记录列表
						Column column = new Column();
						column.setName(nestedRecordListModel.getRecordListName() + ".id"); //将私有记录列表的ID设置为列,使视图服务可以加载记录
						view.getColumns().add(column);
					}
				}
			}
		}
		//重置where子句
		if(view.getWhere()!=null) {
			view.setWhere(StringUtils.fillParameters(view.getWhere(), false, false, true, "utf-8", (sitePage==null ? null : sitePage.getAttribute("record")), request, new FillParametersCallback() {
				public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request) {
					if("columnId".equals(parameterName)) { //栏目ID
						return "" + webDirectory.getId();
					}
					else if("siteId".equals(parameterName)) { //父站点ID
						return "" + parentSite.getId();
					}
					else if(parameterName.startsWith("parentRecord.")) { //父记录ID
						try {
							return PropertyUtils.getProperty(sitePage.getAttribute("parentRecord"), parameterName.substring("parentRecord.".length()));
						}
						catch (Exception e) {
							Logger.exception(e);
							return null;
						}
					}
					return StringUtils.getPropertyValue(recordListModel.getExtendProperties(), parameterName); //从记录列表的扩展属性中获取属性值
				}
			}));
		}
	}
	
	/**
	 * 是否总是输出搜索结果,不管有没有搜索条件
	 * @param view
	 * @param recordListModel
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param request
	 * @return
	 */
	protected boolean isAlwaysWriteSearchResults(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) {
		return false;
	}
	
	/**
	 * 读取记录列表数据
	 * @param view
	 * @param recordListModel
	 * @param searchConditions
	 * @param beginRow
	 * @param readRecordsOnly
	 * @param countRecordsOnly
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		//判断是否不需要输出数据
		if(recordListModel.isSearchResults() && //搜索结果
		   !isAlwaysWriteSearchResults(view, recordListModel, webDirectory, parentSite, sitePage, request) && //不总是输出搜索结果,不管有没有搜索条件
		   request!=null && !"post".equalsIgnoreCase(request.getMethod()) && //不是提交
		   (searchConditions==null || searchConditions.isEmpty())) { //没有搜索条件
			return null;
		}
		if(!recordListModel.isPrivateRecordList()) { //不是私有记录列表
			ViewService viewService = ViewUtils.getViewService(view);
			view.setPageRows(recordListModel.getRecordCount());
			ViewPackage viewPackage = new ViewPackage();
			view.setPageRows(recordListModel.getRecordCount()); //每页记录数
			viewPackage.setView(view);
			viewPackage.setSearchConditionList(searchConditions); //搜索条件列表
			viewPackage.setSearchMode(recordListModel.isSearchResults()); //是否搜索
			viewPackage.setViewMode(View.VIEW_DISPLAY_MODE_NORMAL);
			try {
				viewService.retrieveViewPackage(viewPackage, view, beginRow, true, readRecordsOnly, countRecordsOnly, request, RequestUtils.getSessionInfo(request));
			}
			catch(PrivilegeException e) {
				Logger.exception(e);
				return new RecordListData(null, 0);
			}
			return new RecordListData(viewPackage.getRecords(), viewPackage.getRecordCount());
		}
		else { //私有记录列表
			Object record = sitePage.getAttribute("record");
			Object fieldValue;
			try {
				fieldValue = FieldUtils.getFieldValue(record, view.getName(), request);
			}
			catch (Exception e) {
				Logger.exception(e);
				return null;
			}
			if(fieldValue==null) {
				return null;
			}
			List records = null;
			if(fieldValue instanceof List) {
				records = (List)fieldValue;
			}
			else if(fieldValue instanceof Collection) {
				records = new ArrayList((Collection)fieldValue);
			}
			if(records!=null) {
				if(beginRow>=records.size()) {
					records = null;
				}
				else {
					records = records.subList(beginRow, Math.min(beginRow + recordListModel.getRecordCount(), records.size()));
				}
			}
			return new RecordListData(records, records==null ? 0 : records.size());
		}
	}
	
	/**
	 * 生成搜索条件
	 * @param view
	 * @param recordListModel
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected List generateSearchConditions(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String key = request.getParameter("key"); //关键字
		if(key!=null && !key.isEmpty()) { //关键字不为空
			return ListUtils.generateList(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_KEY, key, null)); //按关键字搜索
		}
		//获取表单定义
		Form form = formDefineService.loadFormDefine(request.getParameter("currentFormApplicationName"), request.getParameter("currentFormName"));
		//构造搜索条件
		Enumeration parameterNames = request.getParameterNames();
		if(parameterNames==null) {
			return null;
		}
		List searchConditions = new ArrayList();
		while(parameterNames.hasMoreElements()) {
			String parameterName = (String)parameterNames.nextElement();
			if("id".equals(parameterName) || "siteId".equals(parameterName)) { //ID和siteId不处理
				continue;
			}
			//获取字段值
			String[] parameterValues = request.getParameterValues(parameterName);
			if(parameterValues==null || parameterValues.length==0) { //没有值
				continue;
			}
			//检查是否值都为空
			int i=0;
			for(; i<parameterValues.length && (parameterValues[i]==null || parameterValues[i].trim().isEmpty()); i++);
			if(i==parameterValues.length) {
				continue;
			}
			//查找对应的POJO字段
			Field field = FieldUtils.getRecordField(view.getPojoClassName(), parameterName, request);
			String fieldName = parameterName;
			boolean isRangeField = false;
			if(field==null) { //没有对应的字段
				//处理时间和数字类型的字段
				String[] ends = {"Begin", "End", "Min", "Max"};
				for(i=0; i<ends.length; i++) {
					if(!parameterName.endsWith(ends[i])) {
						continue;
					}
					isRangeField = true;
					fieldName = parameterName.substring(0, parameterName.length() - ends[i].length());
					field = FieldUtils.getRecordField(view.getPojoClassName(), fieldName, request);
					break;
				}
			}
			Field formField = null;
			String searchCondition = null;
			String expression = null;
			String value = parameterValues[0];
			if(form!=null &&
			   (formField=FieldUtils.getFormField(form, parameterName, request))!=null && //有对应的表单字段
			   (searchCondition=(String)formField.getParameter("searchCondition"))!=null) { //字段定义了自己的搜索条件
				if(!searchCondition.equals("") && !searchCondition.equals("NONE")) { //如果空的,说明不需要构造条件
					expression = Condition.CONDITION_EXPRESSION_HQL;
					value = LinkUtils.fillParameters(searchCondition, false, false, true, "utf-8", webDirectory.getId(), parentSite.getId(), null, sitePage, request);
				}
			}
			else if(field==null) { //没有对应的字段
				continue;
			}
			else if(parameterValues.length>1) { //参数值大于1,多选框
				expression = Condition.CONDITION_EXPRESSION_MEMBER; 
				value = ListUtils.join(parameterValues, ",", false);
			}
			else if(formField!=null && //有对应的表单字段
					!isRangeField &&
					(",radio,checkbox,multibox,".indexOf("," + formField.getInputMode() + ",")!=-1 ||
					 "true".equals(formField.getParameter("selectOnly")))) { //只允许选择的字段
				expression = Condition.CONDITION_EXPRESSION_EQUAL;
			}
			else if("number".equals(field.getType()) || "char".equals(field.getType())) { //数字
				if(!isRangeField) {
					expression = Condition.CONDITION_EXPRESSION_EQUAL;
				}
				else if(parameterName.endsWith("Min")) {
					expression = Condition.CONDITION_EXPRESSION_GREATER_THAN_OR_EQUAL;
				}
				else if(parameterName.endsWith("Max")) {
					expression = Condition.CONDITION_EXPRESSION_LESS_THAN_OR_EQUAL;
				}
			}
			else if(!"date".equals(field.getType()) && !"time".equals(field.getType()) && !"timestamp".equals(field.getType())) { //不是日期或时间
				expression = Condition.CONDITION_EXPRESSION_CONTAIN;
			}
			else { //日期或时间
				if(field.getName().equals(parameterName)) {
					expression = Condition.CONDITION_EXPRESSION_EQUAL;
				}
				else if(parameterName.endsWith("Begin")) {
					expression = Condition.CONDITION_EXPRESSION_GREATER_THAN_OR_EQUAL;
				}
				else if(parameterName.endsWith("End")) {
					if(parameterValues[0].indexOf(':')!=-1) { //时间
						expression = Condition.CONDITION_EXPRESSION_LESS_THAN_OR_EQUAL;
					}
					else { //日期
						try {
							expression = Condition.CONDITION_EXPRESSION_LESS_THAN;
							value = DateTimeUtils.formatDate(DateTimeUtils.add(DateTimeUtils.parseDate(parameterValues[0], null), Calendar.DAY_OF_MONTH, 1), null);
						} 
						catch (ParseException e) {
							continue;
						}
					}
				}
			}
			if(expression!=null) {
				searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, fieldName, field==null ? null : field.getType(), expression, value, null));
			}
		}
		return searchConditions;
	}
	
	/**
	 * 输出记录列表
	 * @param records
	 * @param view
	 * @param recordListModel
	 * @param recordFormatNodes
	 * @param pageIndex
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param requestInfo
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeRecords(List records, View view, RecordList recordListModel, NodeList recordFormatNodes, int pageIndex, HTMLElement pageElement, final WebDirectory webDirectory, final WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLElement recordsContainer = null;
		if(recordListModel.isSimpleMode() &&
		   recordListModel.getScrollMode()==null &&
		   (recordListModel.getAreaWidth()==null || recordListModel.getAreaWidth().isEmpty()) &&
		   (recordListModel.getAreaHeight()==null || recordListModel.getAreaHeight().isEmpty())) {
			HTMLElement parentElement = (HTMLElement)pageElement.getParentNode();
			if(parentElement.getId()==null || parentElement.getId().isEmpty()) { //没有设置ID
				//检查子对象中是否只有记录列表元素
				NodeList childNodes = parentElement.getChildNodes();
				boolean recordListOnly = true;
				for(int i=0; i<childNodes.getLength() && recordListOnly; i++) {
					Node node = childNodes.item(i);
					if(node instanceof Text) {
						String text = ((Text)node).getTextContent();
						if(text!=null && !text.trim().isEmpty()) {
							recordListOnly = false;
						}
					}
					else if(node!=pageElement) {
						recordListOnly = false;
					}
				}
				if(recordListOnly) {
					recordsContainer = parentElement;
				}
			}
			if(recordsContainer==null) {
				recordsContainer = (HTMLElement)pageElement.getOwnerDocument().createElement("span");
				pageElement.getParentNode().insertBefore(recordsContainer, pageElement);
			}
		}
		else {
			recordsContainer = (HTMLElement)pageElement.getOwnerDocument().createElement("div");
			String width = "100%";
			if(recordListModel.getAreaWidth()!=null && !"".equals(recordListModel.getAreaWidth())) {
				width = recordListModel.getAreaWidth();
			}
			recordsContainer.setAttribute("style", "float:left; width:" + width + (recordListModel.getAreaHeight()!=null && !"".equals(recordListModel.getAreaHeight()) ? "; height:" + recordListModel.getAreaHeight(): ""));
			pageElement.getParentNode().insertBefore(recordsContainer, pageElement);
		}
		recordsContainer.setId(recordListModel.getRecordListName());
		if(requestInfo.getPageType()==RequestInfo.PAGE_TYPE_CLIENT_DATA) { //客户端数据页面
			recordsContainer.setAttribute("recordList", "true"); //标记为记录列表,对应的链接,使用记录页面连展现
			if("paging".equals(pageElement.getAttribute("target"))) { //需要分页
				Object lastRecord = records==null || records.isEmpty() ? null : records.get(records.size()-1);
				String extend = "paging=" + pageElement.getAttribute("urn").hashCode() +
								"&recordCount=" + (records==null ? 0 : records.size()) +
							    "&lastRecordId=" + (lastRecord==null || !(lastRecord instanceof Record) ? -1 : ((Record)lastRecord).getId());
				recordsContainer.setAttribute("tabindex", extend);
			}
		}
		if(requestInfo.getPageType()==RequestInfo.PAGE_TYPE_EDIT) { //编辑模式
			//添加处理鼠标移动事件,以便进入编辑模式
		}
		sitePage.setAttribute("fromRecordList", "true");
		if(recordListModel.isSearchResults()) {
			sitePage.setAttribute("searchResults", "true");
		}
		sitePage.setAttribute("linkOpenMode", recordListModel.getLinkOpenMode());
		int offset = (pageIndex-1) * recordListModel.getRecordCount();
		if(recordListModel.isTableMode()) { //表格方式
			writeRecordsTable(view, recordFormatNodes, recordsContainer, records, offset, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
		}
		else if("switch".equals(recordListModel.getScrollMode())) { //滚动切换方式
			writeSlideShowRecords(view, recordFormatNodes, recordsContainer, records, offset, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
		}
		else {
			writeRecords(view, recordFormatNodes, recordsContainer, records, offset, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
		}
	}
	
	/**
	 * 获取记录时间,用来检查是否新记录
	 * @param view
	 * @param record
	 * @return
	 * @throws ServiceException
	 */
	protected java.util.Date getDateForCheckNewest(View view, Object record) throws ServiceException {
		if(view.getNewestCheckBy()==null) {
			return null;
		}
		java.util.Date date = null;
		try {
			date = (java.util.Date)PropertyUtils.getProperty(record, view.getNewestCheckBy());
		}
		catch (Exception e) {
			
		}
		return date;
	}
	
	/**
	 * 判断记录是否当前记录
	 * @param record
	 * @param pageElement
	 * @param recordListModel
	 * @param sitePage
	 * @param webDirectory
	 * @param parentSite
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected boolean isCurrentRecord(Object record, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HttpServletRequest request) throws ServiceException {
		return false;
	}
	
	/**
	 * 表格方式输出记录
	 * @param view
	 * @param recordFormatNodes
	 * @param recordsElement
	 * @param records
	 * @param offset
	 * @param pageElement
	 * @param recordListModel
	 * @param sitePage
	 * @param htmlParser
	 * @param webDirectory
	 * @param parentSite
	 * @param request
	 * @param pageMode
	 * @throws ServiceException
	 */
	private void writeRecordsTable(View view, NodeList recordFormatNodes, Element recordsContainer, List records, int offset, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, HTMLParser htmlParser, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//获取表格
		NodeList tables = recordFormatNodes.item(0).getOwnerDocument().getElementsByTagName("table");
		if(tables!=null && tables.getLength()>0) {
			HTMLTableElement table = (HTMLTableElement)tables.item(0);
			HTMLTableRowElement row = null;
			//找到表格,查找有配置项的行
			NodeList anchors = table.getElementsByTagName("a");
			for(int i=0; i<anchors.getLength(); i++) {
				HTMLAnchorElement anchor = (HTMLAnchorElement)anchors.item(i);
				if(!"field".equals(anchor.getId()) || inTableHeader(anchor)) { //不是字段,或者在表头中
					continue;
				}
				//查找所在的行
				Node parentNode = anchor.getParentNode();
				for(; parentNode!=null && parentNode!=table; parentNode=parentNode.getParentNode()) {
					if(parentNode instanceof HTMLTableRowElement) {
						row = (HTMLTableRowElement)parentNode;
						break;
					}
				}
				break;
			}
			if(row!=null) {
				final HTMLTableRowElement dataRow = row;
				recordFormatNodes = new NodeList() {
					public int getLength() {
						return 1;
					}
					public Node item(int index) {
						return dataRow;
					}
				};
				row.getParentNode().removeChild(row); //从表格删除数据行
				//把表格插入页面
				recordsContainer = (Element)recordsContainer.appendChild(recordsContainer.getOwnerDocument().importNode(table, true));
				//处理表头
				pageBuilder.processPageElement((HTMLElement)recordsContainer, webDirectory, parentSite, sitePage, requestInfo, request);
			}
		}
		if(records!=null) {
			for(int i=0; i<records.size(); i++) {
				Object record = records.get(i);
				writeRecord(view, record, i, offset, recordFormatNodes, recordsContainer, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
			}
		}
	}
	
	/**
	 * 滚动切换方式输出记录
	 * @param view
	 * @param recordFormatNodes
	 * @param recordsElement
	 * @param records
	 * @param pageElement
	 * @param recordListModel
	 * @param sitePage
	 * @param htmlParser
	 * @param parentSite
	 * @param request
	 * @param pageMode
	 * @throws ServiceException
	 */
	private void writeSlideShowRecords(View view, NodeList recordFormatNodes, Element recordsContainer, List records, int offset, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, HTMLParser htmlParser, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		try {
			if(records==null || records.isEmpty()) {
				return;
			}
			//引用样式表
			htmlParser.appendCssFile((HTMLDocument)recordsContainer.getOwnerDocument(), Environment.getContextPath() + "/cms/css/slideShow.css", false);
			//引用脚本
			htmlParser.appendScriptFile((HTMLDocument)recordsContainer.getOwnerDocument(), Environment.getContextPath() + "/jeaf/common/js/scroller.js");
			htmlParser.appendScriptFile((HTMLDocument)recordsContainer.getOwnerDocument(), Environment.getContextPath() + "/cms/js/recordList.js");
			
			//解析图像或者视频尺寸
			final int[] size = getImageOrVideoSize(recordFormatNodes, -1, -1, -1, htmlParser);
			final int imageWidth = size[0]; //图片宽度
			final int imageHeight = size[1]; //图片高度
			final boolean imageClipEnabled = size[2] == 1;
			final int recordWidth = StringUtils.parseInt(recordListModel.getRecordWidth(), imageWidth<=0 ? 800 : 0); //记录宽度
			final int recordHeight = StringUtils.parseInt(recordListModel.getRecordHeight(), 0); //记录高度
			
			String position = recordListModel.getControlBarPosition();
			
			//是否延迟加载
			boolean lazyLoad = imageWidth==0 || imageWidth>800;
			//是否垂直滚动
			boolean verticalScroll =  "vertical".equals(recordListModel.getScrollDirection());
			
			//生成ID
			String slideShowId = getSequence(request, pageElement);
			//创建表格
			HTMLTableElement slideShowTable = (HTMLTableElement)recordsContainer.getOwnerDocument().createElement("table");
			slideShowTable.setBorder("0");
			slideShowTable.setCellPadding("0");
			slideShowTable.setCellSpacing("0");
			slideShowTable.setAttribute("id", "slideShow_" + slideShowId);
			slideShowTable.setAttribute("class", "slideShow");
			slideShowTable.setAttribute("style", "position:relative; visibility:hidden;");
			HTMLTableCellElement contentCell = (HTMLTableCellElement)((HTMLTableRowElement)slideShowTable.insertRow(-1)).insertCell(-1);
			contentCell.setNoWrap(true);
			contentCell.setAlign("center");
			if(imageWidth<=0 && recordWidth>0) {
				contentCell.setWidth(recordWidth + "px");
			}
			recordsContainer.appendChild(slideShowTable);
			
			//创建内容DIV
			HTMLElement contentDiv = (HTMLElement)recordsContainer.getOwnerDocument().createElement("div");
			contentDiv.setId("contentDiv");
			contentDiv.setAttribute("align", "left");
			contentDiv.setAttribute("style", "position:relative; overflow:hidden; height:1px; width:1px;");
			contentCell.appendChild(contentDiv);
			
			//输出记录列表
			HTMLTableElement recordsTable = (HTMLTableElement)recordsContainer.getOwnerDocument().createElement("table");
			recordsTable.setId("records");
			recordsTable.setBorder("0");
			recordsTable.setCellPadding("0");
			recordsTable.setCellSpacing("0");
			contentDiv.appendChild(recordsTable);
			for(int i=(lazyLoad ? 0 : -1); i <= records.size() - (lazyLoad ? 1 : 0); i++) {
				HTMLTableRowElement row = (HTMLTableRowElement)(i==(lazyLoad ? 0 : -1) || verticalScroll ? recordsTable.insertRow(-1) : recordsTable.getRows().item(0));
				HTMLTableCellElement cell = (HTMLTableCellElement)row.insertCell(-1);
				cell.setId("record_" + i);
				cell.setVAlign(imageWidth==0 || imageHeight==0 ? "middle" : "top");
				cell.setAttribute("style", "text-align:center;");
				int recordIndex = i==-1 ? records.size()-1 : (i==records.size() ? 0 : i);
				writeRecord(view, records.get(recordIndex), recordIndex, offset, recordFormatNodes, cell, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
				if(!lazyLoad) {
					continue;
				}
				htmlParser.setStyle(cell, "display", "none");
				NodeList images = cell.getElementsByTagName("img");
				for(int j=0; j<images.getLength(); j++) {
					HTMLImageElement image = (HTMLImageElement)images.item(j);
					image.setClassName("imageLoading");
					int width = StringUtils.parseInt(image.getWidth(), 0);
					if(recordWidth>0 &&  width > recordWidth) {
						image.setWidth("" + recordWidth);
						image.setHeight("" + (recordWidth * StringUtils.parseInt(image.getHeight(), 0) / width));
					}
					if(i>0) {
						image.setId(image.getSrc());
						image.setSrc(null);
					}
				}
			}
			
			//创建控制栏
			HTMLTableCellElement controlBarCell = contentCell;
			HTMLElement controlBar = createControlBar(slideShowId, imageWidth, imageHeight, imageClipEnabled, view, records, offset, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
			if(controlBar!=null) {
				controlBar.setId("controlBar"); //ID
				String controlBarCellStyle = "";
				String controlBarStyle = "display:inline-block; position:" + (position.startsWith("inner") ? "absolute" : "relative") + ";";
				if(position.startsWith("inner")) { //内部控制栏
					if(position.indexOf("Right")!=-1) { //水平方向居右
						controlBarStyle += " right:" + recordListModel.getControlBarXMargin() + "px;";
					}
					else { //水平方向居中或者居左
						controlBarStyle += " left:" + (position.indexOf("Center")!=-1 ? "50%;" : recordListModel.getControlBarXMargin() + "px;");
					}
					if(position.indexOf("Bottom")!=-1) { //垂直方向底部
						controlBarStyle += " bottom:" + recordListModel.getControlBarYMargin() + "px;";
					}
					else { //垂直方向居中或者顶部
						controlBarStyle += " top:" + (position.indexOf("Middle")!=-1 ? "50%;" : recordListModel.getControlBarYMargin() + "px;");
					}
				}
				else if(position.startsWith("outerLeft") || position.startsWith("outerRight")) { //外部居左或者居右
					controlBarCell = (HTMLTableCellElement)((HTMLTableRowElement)slideShowTable.getRows().item(0)).insertCell(position.startsWith("outerLeft") ? 0 : -1);
					controlBarCell.setNoWrap(true);
					controlBarCell.setAlign(position.startsWith("outerLeft") ? "right" : "left"); //设置水平位置
					controlBarCell.setVAlign(position.indexOf("Top")!=-1 ? "top" : (position.indexOf("Middle")!=-1 ? "middle" : "bottom")); //设置垂直位置
					if(recordListModel.getControlBarXMargin()>0) { //设置水平边距
						controlBarCellStyle += (position.startsWith("outerLeft") ? "padding-right" : "padding-left") + ":" + recordListModel.getControlBarXMargin() + "px;";
					}
					if(position.indexOf("Middle")==-1 && recordListModel.getControlBarYMargin()>0) { //设置垂直边距
						controlBarCellStyle += (position.indexOf("Top")!=-1 ? "padding-top" : "padding-bottom") + ":" + recordListModel.getControlBarYMargin() + "px;";
					}
				}
				else if(position.startsWith("outerTop") || position.startsWith("outerBottom")) { //外部顶部或者底部
					controlBarCell = (HTMLTableCellElement)((HTMLTableRowElement)slideShowTable.insertRow(position.startsWith("outerTop") ? 0 : -1)).insertCell(-1);
					controlBarCell.setNoWrap(true);
					controlBarCell.setAlign(position.indexOf("Left")!=-1 ? "left" : (position.indexOf("Center")!=-1 ? "center" : "right")); //设置水平位置
					controlBarCell.setVAlign(position.startsWith("outerTop") ? "bottom" : "top"); //设置垂直位置
					if(position.indexOf("Center")==-1 && recordListModel.getControlBarXMargin()>0) { //设置水平边距
						controlBarCellStyle += (position.indexOf("Left")!=-1 ? "padding-left" : "padding-right") + ":" + recordListModel.getControlBarXMargin() + "px;";
					}
					if(recordListModel.getControlBarYMargin()>0) { //设置垂直边距
						controlBarCellStyle += (position.startsWith("outerTop") ? "padding-bottom" : "padding-top") + ":" + recordListModel.getControlBarYMargin() + "px;";
					}
				}
				if(!controlBarCellStyle.isEmpty()) {
					controlBarCell.setAttribute("style", controlBarCellStyle);
				}
				controlBar.setAttribute("style", controlBarStyle);
				controlBarCell.appendChild(controlBar);
			}
			
			//点击图片执行的操作
			if("nextImage".equals(recordListModel.getImageClickAction()) || "leftPreviousImageAndRightNextImage".equals(recordListModel.getImageClickAction())) {
				contentCell.setAttribute("onclick", "SlideShow.onClickImage('" + slideShowId + "', '" + recordListModel.getImageClickAction() + "', this, event);");
			}
			
			//创建“下一图片”“上一图片”按钮
			createNextPreviousButton(slideShowId, recordListModel, contentCell, htmlParser);
			
			//添加脚本,创建图片切换对象
			int speed = Math.max(1000, recordListModel.getScrollSpeed());
			HTMLScriptElement scriptElement = (HTMLScriptElement)recordsContainer.getOwnerDocument().createElement("script");
			//SlideShow = function(slideShowId, recordCount, imageWidth, imageHeight, imageLazyLoading, verticalScroll, speed, manualSwitch, switchByKey, autoScaling)
			scriptElement.setText("new SlideShow('" + slideShowId + "'," +
												 records.size() + "," +
												 //recordListModel.getRecordCount() + "," +
												 imageWidth + "," +
												 imageHeight + "," +
												 //recordWidth + ", " +
												 //recordHeight + "," +
												 lazyLoad + "," +
												 verticalScroll + "," +
												 speed + "," +
												 recordListModel.isManualSwitch() + "," +
												 recordListModel.isSwitchByKey() + "," +
												 (recordWidth==0 && recordHeight==0 && recordListModel.isAutoScaling()) + ");");
			recordsContainer.appendChild(scriptElement);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 获取图片或者视频字段,返回值:{宽度, 高度, 是否允许裁剪(1/0)}
	 * @param nodes
	 * @param htmlParser
	 * @return
	 * @throws ServiceException
	 */
	private int[] getImageOrVideoSize(NodeList nodes, final int newWidth, final int newHeight, final int newClipEnabled, HTMLParser htmlParser) throws ServiceException {
		final int[] size = {-1, -1, 0};
		htmlParser.traversalChildNodes(nodes, true, new HTMLTraversalCallback() {
			public boolean processNode(Node node) {
				HTMLElement element = (HTMLElement)node;
				if(!"field".equals(element.getId())) {
					return false;
				}
				String urn = element.getAttribute("urn");
				String width = StringUtils.getPropertyValue(urn, "imageWidth");
				int fieldType = 0; //字段类型:1/图片,2/视频
				if(width!=null) {
					fieldType = 1;
					size[0] = StringUtils.parseInt(width, 0);
					size[1] = StringUtils.parseInt(StringUtils.getPropertyValue(urn, "imageHeight"), 0);
					size[2] = "true".equals(StringUtils.getPropertyValue(urn, "clipEnabled")) ? 1 : 0;
				}
				else if((width=StringUtils.getPropertyValue(urn, "videoWidth"))!=null) {
					fieldType = 2;
					size[0] = StringUtils.parseInt(width, 0);
					size[1] = StringUtils.parseInt(StringUtils.getPropertyValue(urn, "videoHeight"), 0);
				}
				if(fieldType!=0 && size[0]==0 && size[1]==0 && newWidth>0 && newHeight>0) {
					if(fieldType==1) {
						urn = urn.replaceFirst("imageWidth=0", "imageWidth=" + newWidth).replaceFirst("imageHeight=0", "imageHeight=" + newHeight);
					}
					else {
						urn = urn.replaceFirst("videoWidth=0", "videoWidth=" + newWidth).replaceFirst("videoHeight=0", "videoHeight=" + newHeight);
					}
					element.setAttribute("urn", urn);
				}
				if(fieldType!=0 && newClipEnabled > 0) {
					if(newClipEnabled==0) { //不允许裁剪
						urn = urn.replaceAll("&clipEnabled=true", "").replaceAll("clipEnabled=true", "");
					}
					else if(urn.indexOf("clipEnabled=true")==-1) { //允许裁剪
						urn += "&clipEnabled=true";
					}
					element.setAttribute("urn", urn);
				}
				return true;
			}
		});
		return size;
	}
	
	/**
	 * 创建记录列表控制栏
	 * @param slideShowId
	 * @param imageWidth
	 * @param imageHeight
	 * @param imageClipEnabled
	 * @param view
	 * @param records
	 * @param offset
	 * @param pageElement
	 * @param recordListModel
	 * @param sitePage
	 * @param htmlParser
	 * @param webDirectory
	 * @param parentSite
	 * @param requestInfo
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private HTMLElement createControlBar(String slideShowId, int imageWidth, int imageHeight, boolean imageClipEnabled, View view, List records, int offset, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, HTMLParser htmlParser, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//创建控制栏
		HTMLElement controlBar = (HTMLElement)pageElement.getOwnerDocument().createElement("div");
		
		//解析控制栏格式
		HTMLDocument controlBarDocument = htmlParser.parseHTMLString(recordListModel.getControlBarFormat(), "utf-8");
		if(controlBarDocument!=null) {
			htmlParser.importNodes(controlBarDocument.getBody().getChildNodes(), controlBar, false);
		}
		
		//获取未选中记录元素
		final HTMLElement unselectedRecordElement = htmlParser.getElementById(controlBar, null, "unselectedRecord");
		if(unselectedRecordElement==null) { //没有未选中记录元素
			return controlBar;
		}
		//获取选中记录元素
		final HTMLElement selectedRecordElement = htmlParser.getElementById(controlBar, null, "selectedRecord");
		if(selectedRecordElement==null) { //没有选中记录元素
			return controlBar;
		}
		//获取图片或者视频字段,检查是否有配置尺寸,如果未设置则重置为imageWidth/记录数,imageHeight/记录数
		int newWidth = imageWidth==0 ? 100 : imageWidth/recordListModel.getRecordCount();
		int newHeight = imageWidth==0 ? 75 : imageHeight/recordListModel.getRecordCount();
		getImageOrVideoSize(unselectedRecordElement.getChildNodes(), newWidth, newHeight, imageClipEnabled ? 1 : 0, htmlParser);
		getImageOrVideoSize(selectedRecordElement.getChildNodes(), newWidth, newHeight, imageClipEnabled ? 1 : 0, htmlParser);
		
		unselectedRecordElement.removeAttribute("id");
		selectedRecordElement.removeAttribute("id");
		
		HTMLElement controlBarRecordsContainer = (HTMLElement)unselectedRecordElement.getParentNode();
		String cssText = controlBarRecordsContainer.getAttribute("style");
		if(cssText!=null) {
			controlBarRecordsContainer.setAttribute("style", cssText.replaceAll("(?i)-ms-(overflow)", "$1"));
		}
		String position = StringUtils.getStyle(cssText, "position");
		if(position==null || position.isEmpty()) {
			htmlParser.setStyle(controlBarRecordsContainer, "position", "relative");
		}
		
		HTMLElement nobr = (HTMLElement)pageElement.getOwnerDocument().createElement("nobr");
		controlBarRecordsContainer.appendChild(nobr);
		boolean verticalScroll =  "vertical".equals(recordListModel.getScrollDirection());
		//输出记录列表
		for(int i=0; i<(records==null ? 0 : records.size()); i++) {
			Object record = records.get(i);
			for(int j=0; j<2; j++) {
				HTMLElement span = (HTMLElement)nobr.getOwnerDocument().createElement("span");
				span.setId((j==0 ? "un" : "") + "selectedRecord_" + i);
				String style = "display:" + ((j==0 && i>0) || (j==1 && i==0)  ? "inline-block;" : "none;");
				if(recordListModel.getControlBarRecordSpacing()>0 && i>0) {
					style += "margin-" + (verticalScroll ? "top" : "left") + ":" + recordListModel.getControlBarRecordSpacing() + "px";
				}
				span.setAttribute("style", style);
				if("switch".equals(recordListModel.getMouseOverControlBarRecord())) { //鼠标移动时切换图片
					span.setAttribute("onmouseover", "SlideShow.slide('" + slideShowId + "'," + i + ");");
				}
				if("switch".equals(recordListModel.getClickControlBarRecord())) { //点击时切换图片
					span.setAttribute("onclick", "SlideShow.slide('" + slideShowId + "'," + i + ");");
				}
				else if("openLink".equals(recordListModel.getClickControlBarRecord())) { //点击时打开连接
					span.setAttribute("onclick", "SlideShow.openLink('" + slideShowId + "'," + i + ");");
				}
				nobr.appendChild(span);
				final HTMLElement recordElement = j==0 ? unselectedRecordElement : selectedRecordElement;
				NodeList nodeList = new NodeList() {
					public int getLength() {
						return 1;
					}
					public Node item(int index) {
						return recordElement;
					}
				};
				writeRecord(view, record, i, offset, nodeList, span, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
			}
			if(i<records.size()-1 && verticalScroll) { //垂直滚动
				nobr.appendChild(nobr.getOwnerDocument().createElement("br")); //添加换行
			}
		}
		
		//清除未选中记录元素、选中记录元素
		controlBarRecordsContainer.removeChild(unselectedRecordElement);
		selectedRecordElement.getParentNode().removeChild(selectedRecordElement);
		
		//处理分页按钮
		HTMLElement previousPageButton = htmlParser.getElementById(controlBar, null, "previousPageButton");
		if(previousPageButton!=null) {
			previousPageButton.setAttribute("onclick", "SlideShow.gotoPage('" + slideShowId + "',false);");
		}
		HTMLElement nextPageButton = htmlParser.getElementById(controlBar, null, "nextPageButton");
		if(nextPageButton!=null) {
			nextPageButton.setAttribute("onclick", "SlideShow.gotoPage('" + slideShowId + "',true);");
		}
		return controlBar;
	}
	
	/**
	 * 创建“下一图片”“上一图片”按钮
	 * @param slideShowId
	 * @param recordListModel
	 * @param parentElement
	 * @param htmlParser
	 * @throws ServiceException
	 */
	private void createNextPreviousButton(String slideShowId, RecordList recordListModel, HTMLElement parentElement, HTMLParser htmlParser) throws ServiceException {
		if(!"always".equals(recordListModel.getDisplayNextPreviousButton()) &&
		   !"mouseOver".equals(recordListModel.getDisplayNextPreviousButton())) {
			return;
		}
		if(recordListModel.getNextPreviousButtonFormat()==null ||
		    recordListModel.getNextPreviousButtonFormat().isEmpty()) {
			 return;
		}
		boolean showWhenMouseOver = "mouseOver".equals(recordListModel.getDisplayNextPreviousButton());
		HTMLDocument buttonDocument = htmlParser.parseHTMLString(recordListModel.getNextPreviousButtonFormat(), "utf-8");
		HTMLElement previousButtonFormat = (HTMLElement)buttonDocument.getElementById("previousButtonFormat");
		String onclick = parentElement.getAttribute("onclick");
		if(previousButtonFormat!=null) {
			HTMLDivElement previousImageButton = (HTMLDivElement)parentElement.getOwnerDocument().createElement("div");
			previousImageButton.setId("previousImageButton");
			previousImageButton.setAttribute("style", "position:absolute; left:0px;" + (showWhenMouseOver ? " visibility:hidden;" : ""));
			if(onclick==null || onclick.isEmpty()) {
				previousImageButton.setAttribute("onclick", "SlideShow.slideToNextImage('" + slideShowId + "', false);");
			}
			htmlParser.importNodes(previousButtonFormat.getChildNodes(), previousImageButton, false);
			parentElement.appendChild(previousImageButton);
		}
		HTMLElement nextButtonFormat = (HTMLElement)buttonDocument.getElementById("nextButtonFormat");
		if(nextButtonFormat!=null) {
			HTMLDivElement nextImageButton = (HTMLDivElement)parentElement.getOwnerDocument().createElement("div");
			nextImageButton.setId("nextImageButton");
			nextImageButton.setAttribute("style", "position:absolute; right:0px; " + (showWhenMouseOver ? "visibility:hidden;" : ""));
			if(onclick==null || onclick.isEmpty()) {
				nextImageButton.setAttribute("onclick", "SlideShow.slideToNextImage('" + slideShowId + "', true);");
			}
			htmlParser.importNodes(nextButtonFormat.getChildNodes(), nextImageButton, false);
			parentElement.appendChild(nextImageButton);
		}
		if(showWhenMouseOver) { //鼠标经过时显示
			parentElement.setAttribute("onmousemove", "SlideShow.showNextPreviousButton('" + slideShowId + "', true, this, event);");
			parentElement.setAttribute("onmouseout", "SlideShow.showNextPreviousButton('" + slideShowId + "', false, this, event);");
		}
	}
	
	/**
	 * 重置记录列表模型
	 * @param recordListModel
	 */
	private void resetRecordListModel(RecordList recordListModel) {
		if(!"switch".equals(recordListModel.getScrollMode())) { //不是滚动切换方式
			return;
		}
		String switchMode = recordListModel.getSwitchMode();
		if(recordListModel.getControlBarPosition()==null && (switchMode==null || switchMode.isEmpty())) { //没有指定显示位置
			switchMode = "none";
		}
		if(switchMode==null || switchMode.isEmpty()) { //有配置过控制栏格式
			return;
		}
		recordListModel.setControlBarFormat(RecordListUtils.getDefaultControlBarFormat(switchMode)); //控制栏格式
		//显示位置
		recordListModel.setControlBarPosition("number".equals(switchMode) || "none".equals(switchMode) ? "innerRightBottom" : ("rightSmallImage".equals(switchMode) ? "outerRightTop" : "outerBottomCenter"));
		recordListModel.setControlBarXMargin("rightSmallImage".equals(switchMode) ? 8 : 0); //水平边距
		recordListModel.setControlBarYMargin("bottomSmallImage".equals(switchMode) ? 8 : 0); //垂直边距
		recordListModel.setScrollDirection("rightSmallImage".equals(switchMode) ? "vertical" : "horizontal"); //滚动方向,horizontal/横向,vertical/纵向
		recordListModel.setControlBarRecordSpacing("rightSmallImage".equals(switchMode) || "bottomSmallImage".equals(switchMode) ? 2 : 0); //控制栏记录分隔距离
		recordListModel.setMouseOverControlBarRecord("number".equals(switchMode) ? "none" : "switch"); //鼠标经过控制记录时的动作
		recordListModel.setClickControlBarRecord("number".equals(switchMode) ? "switch" : "openLink"); //点击控制记录时的动作
	}
	
	/**
	 * 按默认方式输出记录
	 * @param view
	 * @param recordFormatNodes
	 * @param recordsContainer
	 * @param records
	 * @param offset
	 * @param pageElement
	 * @param recordListModel
	 * @param sitePage
	 * @param htmlParser
	 * @param webDirectory
	 * @param parentSite
	 * @param requestInfo
	 * @param request
	 * @throws ServiceException
	 */
	private void writeRecords(View view, NodeList recordFormatNodes, HTMLElement recordsContainer, List records, int offset, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, HTMLParser htmlParser, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if(records==null || records.isEmpty()) { //没有记录
			//输出提示信息
			if(recordListModel.getEmptyPrompt()!=null && !recordListModel.getEmptyPrompt().equals("")) {
				HTMLDocument emptyPropmtDocument = htmlParser.parseHTMLString(recordListModel.getEmptyPrompt(), "utf-8");
				htmlParser.importNodes(emptyPropmtDocument.getBody().getChildNodes(), recordsContainer, false);
			}
			return;
		}
		Element recordContainer = recordsContainer;
		int scrollSpeed = Math.max(10, recordListModel.getScrollSpeed());
		boolean joinScroll = false;
		String sequence = getSequence(request, pageElement);
		if(recordListModel.getScrollMode()!=null && !"".equals(recordListModel.getScrollMode())) {
			if(!recordListModel.isScrollJoin()) { //不需要头尾衔接
				//插入MARQUEE
				Element marquee =  recordContainer.getOwnerDocument().createElement("MARQUEE");
				marquee.setAttribute("direction", recordListModel.getScrollMode());
				marquee.setAttribute("scrollAmount", "" + recordListModel.getScrollAmount());
				marquee.setAttribute("scrollDelay", "" + scrollSpeed);
				marquee.setAttribute("onmousemove", "this.stop()");
				marquee.setAttribute("onmouseout", "this.start()");
				//设置宽度和高度
				String width = "100%";
				if(recordListModel.getAreaWidth()!=null && !"".equals(recordListModel.getAreaWidth())) {
					width = recordListModel.getAreaWidth();
				}
				marquee.setAttribute("style", "width:" + width + (recordListModel.getAreaHeight()!=null && !"".equals(recordListModel.getAreaHeight()) ? "; height:" + recordListModel.getAreaHeight(): ""));
				recordContainer.appendChild(marquee);
				recordContainer = marquee;
			}
			else { //需要头尾衔接
				joinScroll = true;
				recordContainer.setAttribute("id", "marquee_" + sequence);
				recordContainer.setAttribute("style", recordContainer.getAttribute("style") + ";overflow:hidden");

				//创建PRE,避免被换行
				Element pre =  pageElement.getOwnerDocument().createElement("pre");
				recordContainer.appendChild(pre);

				//创建滚动内容区域
				Element span =  pageElement.getOwnerDocument().createElement("span");
				span.setAttribute("id", "marqueeContent_" + sequence);
				pre.appendChild(span);
				recordContainer = span;
			}
		}
		String separatorMode = recordListModel.getSeparatorMode();
		if(separatorMode.equals("tile")) { //平铺
			//填充记录
			for(int i=0; i<records.size(); i++) {
				Object record = records.get(i);
				Element container = recordContainer;
				if(i<records.size()-1) {
					container = (Element)recordContainer.getParentNode().cloneNode(true);
					recordContainer.getParentNode().getParentNode().insertBefore(container, recordContainer.getParentNode());
					HTMLElement element = htmlParser.getElementById((HTMLElement)container, "a", pageElement.getId());
					element.getParentNode().removeChild(element);
					container = htmlParser.getElementById((HTMLElement)container, "div", recordContainer.getAttribute("id"));
				}
				writeRecord(view, record, i, offset, recordFormatNodes, container, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
			}
			return;
		}
		Element recordSeparatorNode = null;
		if(separatorMode.equals("newLine") || separatorMode.equals("image")) { //记录分隔:换行/图片
			recordSeparatorNode = recordContainer.getOwnerDocument().createElement("div");
			String style = "clear:both; line-height:0px; font-size:0px; width:100%; height:" + (recordListModel.getSeparatorHeight()==null || recordListModel.getSeparatorHeight().equals("") ? "0" : recordListModel.getSeparatorHeight()) + "px;";
			if(separatorMode.equals("image")) {
				style += "background-image:url(" + recordListModel.getSeparatorImage() + ");background-repeat:repeat-x;background-position:0,0";
			}
			htmlParser.setTextContent(recordSeparatorNode, " ");
			recordSeparatorNode.setAttribute("style", style);
		}
		else if(separatorMode.equals("blank") || separatorMode.equals("custom")) { //记录分隔:空格/自定义
			recordSeparatorNode = recordContainer.getOwnerDocument().createElement("span");
			String style = "display:inline-block;";
			if(recordListModel.getRecordWidth()!=null || recordListModel.getRecordHeight()!=null) {
				style += "float:left;";
			}
			if(recordListModel.getSeparatorCustom()==null || recordListModel.getSeparatorCustom().isEmpty()) {
				htmlParser.setTextContent(recordSeparatorNode, " ");
				style += "line-height:0px; font-size:0px;" + (recordListModel.getSeparatorHeight()==null || recordListModel.getSeparatorHeight().equals("") ? "" : "width:" + recordListModel.getSeparatorHeight() + "px");
			}
			else {
				HTMLDocument separatorCustomDocument = htmlParser.parseHTMLString(recordListModel.getSeparatorCustom(), "utf-8");
				htmlParser.importNodes(separatorCustomDocument.getBody().getChildNodes(), (HTMLElement)recordSeparatorNode, false);
			}
			recordSeparatorNode.setAttribute("style", style);
		}
		//填充记录
		for(int i=0; i<records.size(); i++) {
			Object record = records.get(i);
			//判断最后一条记录是否需要输出分隔符
			boolean lastRowSeparator = (recordListModel.isSeparatorImageOfLastRecord() && separatorMode.equals("image")) || //以图片做分隔符,并且最后一条记录需要显示分隔符
									   (recordListModel.isSeparatorOfLastRecord() && separatorMode.equals("custom")); //自定义分隔符,并且最后一条记录需要显示分隔符
			if(i==0 && recordSeparatorNode!=null && !lastRowSeparator &&
			   "true".equals(RequestUtils.getParameterStringValue(request, "client.loadMore"))) { //加载更多页面,且最后一行不输出分隔符
				recordContainer.appendChild(recordSeparatorNode.cloneNode(true));
			}
			//不是最后一条记录,或者以图片做分隔符,插入分隔符
			writeRecord(view, record, i, offset, recordFormatNodes, recordContainer, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
			//输出记录分隔
			if(recordSeparatorNode!=null && (i<records.size()-1 || lastRowSeparator)) { //不是最后一条记录,或者最后一条也要显示
				recordContainer.appendChild(recordSeparatorNode.cloneNode(true));
			}
		}
		if(joinScroll) { //头尾衔接滚动显示
			//追加一个分隔符
			if(recordSeparatorNode!=null) {
				recordContainer.appendChild(recordSeparatorNode.cloneNode(true));
			}
			//把滚动区域中的对象重新复制一遍
			NodeList nodes = recordContainer.getChildNodes();
			int size = nodes.getLength();
			for(int i=0; i<size; i++) {
				recordContainer.appendChild(nodes.item(i).cloneNode(true));
			}
			//插入脚本
			htmlParser.appendScriptFile((HTMLDocument)recordsContainer.getOwnerDocument(), Environment.getContextPath() + "/cms/js/recordList.js");
			HTMLScriptElement scriptElement = (HTMLScriptElement)recordContainer.getOwnerDocument().createElement("script");
			scriptElement.setText("new JoinScrollMarquee(document.getElementById('marquee_" + sequence + "'), document.getElementById('marqueeContent_" + sequence + "'), " + recordListModel.getScrollAmount() + ", " + scrollSpeed + ");");
			recordsContainer.getParentNode().appendChild(scriptElement);
		}
	}
	
	/**
	 * 输出一条记录
	 * @param view
	 * @param record
	 * @param recordIndex
	 * @param offset
	 * @param recordFormatNodes
	 * @param recordContainer
	 * @param pageElement
	 * @param recordListModel
	 * @param sitePage
	 * @param htmlParser
	 * @param webDirectory
	 * @param parentSite
	 * @param requestInfo
	 * @param request
	 * @throws ServiceException
	 */
	protected void writeRecord(View view, Object record, int recordIndex, int offset, NodeList recordFormatNodes, Element recordContainer, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, HTMLParser htmlParser, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		try {
			sitePage.setAttribute("record", record);
			sitePage.setAttribute("recordNumber", new Integer(offset + recordIndex + 1));
			sitePage.setAttribute("recordUrl", getRecordUrl(view, record, recordListModel.getLinkTitle(), sitePage, recordListModel, webDirectory, parentSite, requestInfo, request));
			String width = recordListModel.getRecordWidth();
			String height = recordListModel.getRecordHeight();
			if(recordListModel.getRecordIndent()!=null && !recordListModel.getRecordIndent().equals("")) { //处理记录缩进
				Element p = recordContainer.getOwnerDocument().createElement("p");
				p.setAttribute("style","margin-left:" + recordListModel.getRecordIndent() + "; text-indent:-" + recordListModel.getRecordIndent() + "; margin-top:0px; margin-bottom:0px");
				recordContainer.appendChild(p);
				recordContainer = p;
			}
			if(width!=null || height!=null || (recordListModel.isScrollJoin() && !"switch".equals(recordListModel.getScrollMode()))) {
				//插入SPAN显示记录
				Element span = recordContainer.getOwnerDocument().createElement("span");
				span.setAttribute("style", "text-align:center; display:inline-block" + (width==null ? "" : ";width:" + width) + (height==null ? "" : ";height:" + height));
				recordContainer.appendChild(span);
				recordContainer = span;
			}
			for(int j=0; j<recordFormatNodes.getLength(); j++) {
				Node recordNode = recordContainer.getOwnerDocument().importNode(recordFormatNodes.item(j), true);
				recordContainer.appendChild(recordNode);
				if(!(recordNode instanceof Element)) {
					continue;
				}
				if(isCurrentRecord(record, pageElement, recordListModel, sitePage, webDirectory, parentSite, request)) { //当前记录
					//设置样式
					Element element = (Element)recordNode;
					String styleClass = element.getAttribute("class");
					element.setAttribute("class", styleClass!=null && !styleClass.equals("") ? styleClass + " " + styleClass + "Current" : "current");  //自动修改样式
				}
				if(recordNode instanceof HTMLAnchorElement) {
					HTMLAnchorElement fieldElement = (HTMLAnchorElement)recordNode;
					writeRecordElement(view, record, fieldElement, webDirectory, parentSite, htmlParser, sitePage, recordListModel, requestInfo, request);
				}
				else {
					NodeList elements = ((Element)recordNode).getElementsByTagName("a");
					for(int k=elements.getLength()-1; k>=0; k--) {
						HTMLAnchorElement fieldElement = (HTMLAnchorElement)elements.item(k);
						writeRecordElement(view, record, fieldElement, webDirectory, parentSite, htmlParser, sitePage, recordListModel, requestInfo, request);
					}
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 获取记录URL
	 * @param view
	 * @param record
	 * @param linkTitle
	 * @param sitePage
	 * @param recordListModel
	 * @param webDirectory
	 * @param parentSite
	 * @param requestInfo
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected String getRecordUrl(View view, Object record, String linkTitle, SitePage sitePage, RecordList recordListModel, final WebDirectory webDirectory, final  WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if(view.getLinks()==null || view.getLinks().isEmpty()) {
			return null;
		}
		Link link = null;
		if(linkTitle!=null) { //指定了链接的标题
			link = (Link)ListUtils.findObjectByProperty(view.getLinks(), "title", linkTitle);
		}
		if(link==null) {
			link = (Link)view.getLinks().get(0);
		}
		return link.getUrl();
	}
	
	/**
	 * 输出记录元素
	 * @param view
	 * @param record
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param recordListModel
	 * @param requestInfo
	 * @param request
	 * @param fieldElement
	 * @throws ServiceException
	 */
	protected void writeRecordElement(View view, Object record, HTMLAnchorElement recordElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RecordList recordListModel, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		String anchorId = recordElement.getId();
		if("field".equals(anchorId)) { //字段
			pageBuilder.processPageElement(recordElement, webDirectory, parentSite, sitePage, requestInfo, request);
		}
		else if("newestImage".equals(anchorId)) { //"new"图片
			writeNewestImage(view, record, recordElement, sitePage, htmlParser, request);
		}
		else if("recordLink".equals(anchorId)) { //链接
			HTMLAnchorElement a = (HTMLAnchorElement)recordElement;
			//获取链接名称
			String urn = recordElement.getAttribute("urn");
			String linkTitle = StringUtils.getPropertyValue(urn, "linkTitle");
			if(linkTitle==null || linkTitle.isEmpty()) {
				linkTitle = urn;
			}
			String url = getRecordUrl(view, record, linkTitle, sitePage, recordListModel, webDirectory, parentSite, requestInfo, request);
			if(url==null) {
				a.getParentNode().removeChild(a);
				return;
			}
			a.removeAttribute("id");
			a.removeAttribute("urn");
			//输出链接
			long relationSiteId = StringUtils.getPropertyLongValue(urn, "siteId", -1);
			LinkUtils.writeLink(a, url, a.getTarget(), webDirectory.getId(), (relationSiteId==-1 ? parentSite.getId() : relationSiteId), record, true, true, sitePage, request);
		}
		else if("recordList".equals(anchorId)) { //内嵌记录列表
			sitePage.setAttribute("parentRecord", record);
			pageBuilder.processPageElement(recordElement, webDirectory, parentSite, sitePage, requestInfo, request);
			sitePage.setAttribute("parentRecord", null);
		}
	}
	
	/**
	 * 输出new图片
	 * @param view
	 * @param record
	 * @param newestImageElement
	 * @param sitePage
	 * @param htmlParser
	 * @param request
	 * @throws ServiceException
	 */
	private void writeNewestImage(View view,  Object record, HTMLAnchorElement newestImageElement, SitePage sitePage, HTMLParser htmlParser, HttpServletRequest request) throws ServiceException {
		String urn = newestImageElement.getAttribute("urn");
		String imageURL = StringUtils.getPropertyValue(urn, "imageURL");
		int timeLimit = StringUtils.getPropertyIntValue(urn, "timeLimit", 10); //小时为单位
		if(imageURL!=null && timeLimit>0) {
			java.util.Date date = getDateForCheckNewest(view, record); //获取记录时间
			if(date!=null && System.currentTimeMillis() - date.getTime() < timeLimit * 3600000l) {
				HTMLImageElement img = (HTMLImageElement)newestImageElement.getOwnerDocument().createElement("img");
				img.setSrc(imageURL);
				newestImageElement.getParentNode().replaceChild(img, newestImageElement);
				//设置页面的有效时间
				PageUtils.setPageExpiresTime(new Timestamp(date.getTime() + timeLimit * 3600000l), request);
				return;
			}
		}
		newestImageElement.getParentNode().removeChild(newestImageElement);
	}
	
	/**
	 * 检查HTML元素是否在表头中
	 * @param element
	 * @return
	 */
	private boolean inTableHeader(HTMLElement element) {
		for(HTMLElement parentElement=(HTMLElement)element.getParentNode(); ;parentElement=(HTMLElement)parentElement.getParentNode()) {
			if("tr".equalsIgnoreCase(parentElement.getTagName()) || "table".equalsIgnoreCase(parentElement.getTagName()) || "body".equalsIgnoreCase(parentElement.getTagName())) {
				break;
			}
			if("th".equalsIgnoreCase(parentElement.getTagName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取顺序号
	 * @return
	 */
	private String getSequence(HttpServletRequest request, HTMLElement pageElement) {
		Integer recordListIndex = (Integer)request.getAttribute("recordListIndex");
		if(recordListIndex==null) {
			recordListIndex = new Integer(0);
		}
		request.setAttribute("recordListIndex", new Integer(recordListIndex.intValue() + 1));
		return pageElement.getAttribute("urn").hashCode() + "_" + recordListIndex.intValue();
	}

	/**
	 * @return the pageDefineService
	 */
	public PageDefineService getPageDefineService() {
		return pageDefineService;
	}

	/**
	 * @param pageDefineService the pageDefineService to set
	 */
	public void setPageDefineService(
			PageDefineService pageDefineService) {
		this.pageDefineService = pageDefineService;
	}

	/**
	 * @return the pageBuilder
	 */
	public PageBuilder getPageBuilder() {
		return pageBuilder;
	}

	/**
	 * @param pageBuilder the pageBuilder to set
	 */
	public void setPageBuilder(PageBuilder pageBuilder) {
		this.pageBuilder = pageBuilder;
	}

	/**
	 * @return the formDefineService
	 */
	public FormDefineService getFormDefineService() {
		return formDefineService;
	}

	/**
	 * @param formDefineService the formDefineService to set
	 */
	public void setFormDefineService(FormDefineService formDefineService) {
		this.formDefineService = formDefineService;
	}
}