package com.yuanluesoft.appraise.appraiser.service.spring;

import java.io.File;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.yuanluesoft.appraise.appraiser.pojo.Appraiser;
import com.yuanluesoft.appraise.appraiser.pojo.AppraiserImport;
import com.yuanluesoft.appraise.appraiser.service.AppraiserService;
import com.yuanluesoft.appraise.pojo.AppraiseParameter;
import com.yuanluesoft.appraise.service.AppraiseService;
import com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.applicationtree.ApplicationTreeNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.usermanage.pojo.Area;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiserServiceImpl extends BusinessServiceImpl implements AppraiserService {
	private OrgService orgService; //组织机构服务
	private AttachmentService attachmentService; //附件服务
	private AppraiseService appraiseService; //评议服务

	/**
	 * 初始化
	 *
	 */
	public void init() {
		try {
			orgService.appendDirectoryPopedomType("appraiseManager", "评议管理员", "area", DirectoryPopedomType.INHERIT_FROM_PARENT_NO, false, true);
			orgService.appendDirectoryPopedomType("appraiseTransactor", "评议责任人", "unit", DirectoryPopedomType.INHERIT_FROM_PARENT_NO, false, true);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof AppraiserImport) {
			AppraiserImport appraiserImport = (AppraiserImport)record;
			//获取评议参数配置
			AppraiseParameter appraiseParameter = appraiseService.getAppraiseParameter();
			//检查是否超时
			if(appraiseParameter!=null && appraiseParameter.getRecipientsUploadEnd()<DateTimeUtils.getDay(DateTimeUtils.date())) {
				appraiserImport.setTimeout(1);
			}
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.appraiser.service.AppraiserService#importAppraisers(long, long, java.sql.Date, int, int, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List importAppraisers(long importId, long orgId, Date expire, int appraiserType, int appraiserStatus, SessionInfo sessionInfo) throws ServiceException {
		if(appraiserType==APPRAISER_TYPE_BASIC) { //基础库
			doImportAppraisers(true, importId, orgId, expire, appraiserType, appraiserStatus, sessionInfo); //校验身份类型
		}
		List appraisers = doImportAppraisers(false, importId, orgId, expire, appraiserType, appraiserStatus, sessionInfo);
		if(appraisers==null || appraisers.isEmpty()) {
			throw new ServiceException("导入失败,请检查文件格式是否正确。");
		}
		return appraisers;
	}
	
	/**
	 * 导入评议员
	 * @param validateAppraiserOnly 是否只做校验
	 * @param importId
	 * @param orgId
	 * @param expire
	 * @param appraiserType
	 * @param appraiserStatus
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private List doImportAppraisers(boolean validateAppraiserOnly, long importId, long orgId, Date expire, int appraiserType, int appraiserStatus, SessionInfo sessionInfo) throws ServiceException {
		List attachments = attachmentService.list("appraise/appraiser", "data", importId, false, 0, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		//删除之前导入的记录
		if(!validateAppraiserOnly) {
			getDatabaseService().deleteRecordsByHql("from Appraiser Appraiser where Appraiser.importId=" + importId);
		}
		//获取评议参数
		int[] limit = {Integer.MAX_VALUE};
		if(appraiserType==APPRAISER_TYPE_RECIPIENT) { //服务对象
			AppraiseParameter appraiseParameter = appraiseService.getAppraiseParameter();
			limit[0] = (appraiseParameter==null ? 200 : appraiseParameter.getRecipientsLimit());
		}
		//导入新数据
		String orgName = orgService.getDirectoryName(orgId);
		Area area = (Area)orgService.getAreaOfOrg(orgId);
		String areaName = (area==null ? null : area.getDirectoryName());
		String error = null;
		List appraisers = new ArrayList();
		for(Iterator iterator = attachments.iterator(); iterator.hasNext();) {
			Attachment attachment = (Attachment)iterator.next();
			Workbook wb = null;
			try {
				//导入数据
				wb = Workbook.getWorkbook(new File(attachment.getFilePath())); //构造Workbook（工作薄）对象
				for(int i=0; i<wb.getSheets().length && limit[0]>0; i++) {
					Sheet sheet = wb.getSheet(i);
					String wrongRowNumbers = importSheet(appraisers, validateAppraiserOnly, sheet, importId, orgId, orgName, areaName, expire, appraiserType, appraiserStatus, sessionInfo, limit); //导入SHEET
					if(wrongRowNumbers!=null) {
						error = (error==null ? "" : error + "\\n") + (attachments.size()==1 ? "" : attachment.getName()) + "表格“" + sheet.getName() + "”第" + wrongRowNumbers + "行身份类型不正确";
					}
				}
			}
			catch (Exception e) {
				Logger.exception(e);
				if(!validateAppraiserOnly) {
					deleteImportedAppraisers(importId); //删除已经导入的数据
					throw new ServiceException("导入失败");
				}
			}
			finally {
				try {
					wb.close();
				}
				catch (Exception e) {
					
				}
			}
		}
		if(error!=null) {
			throw new ServiceException(error);
		}
		return appraisers;
	}
	
	/**
	 * 导入一个SHEET,校验时返回错误的行号,用顿号分隔
	 * @param validateAppraiserOnly 是否只做校验
	 * @param sheet
	 * @param importId
	 * @param orgId
	 * @param orgName
	 * @param areaName
	 * @param expire
	 * @param appraiserType
	 * @param appraiserStatus
	 * @param sessionInfo
	 * @param limit
	 * @throws ServiceException
	 */
	private String importSheet(List appraisers, boolean validateAppraiserOnly, Sheet sheet, long importId, long orgId, String orgName, String areaName, Date expire, int appraiserType, int appraiserStatus, SessionInfo sessionInfo, int[] limit) throws ServiceException {
		String wrongRowNumbers = null;
		List colNames = new ArrayList(); //表头行
		for(int i=0; i<sheet.getRows() && limit[0]>0; i++) {
			Cell[] cells = sheet.getRow(i);
			//如果表头为空,检查是否表头,判断依据该行必须要有“姓名”单元格
			if(colNames.isEmpty()) {
				for(int j=0; j<cells.length; j++) {
					String content = ExcelUtils.getCellString(cells, j);
					if(content==null) {
						content = "";
					}
					colNames.add(content.trim());
				}
				int index = colNames.indexOf("姓名");
				if(index==-1) {
					colNames.clear(); //不含“姓名”单元格,不是表头
				}
				continue;
			}
			//读取评议员记录
			Appraiser appraiser = new Appraiser();
			appraiser.setId(UUIDLongGenerator.generateId()); //ID
			appraiser.setImportId(importId); //导入日志ID
			appraiser.setType(appraiserType); //类型,0/基础,1/管理服务对象
			appraiser.setOrgId(orgId); //隶属机构ID
			appraiser.setOrgName(orgName); //隶属机关名称
			appraiser.setCreatorId(sessionInfo.getUserId()); //登记人ID
			appraiser.setCreator(sessionInfo.getUserName()); //登记人
			appraiser.setCreated(DateTimeUtils.now()); //登记时间
			appraiser.setExpire(expire); //有效期
			appraiser.setStatus(appraiserStatus); //状态,0/待审核,1/已启用,2/已过期
			for(int j=0; j<Math.min(cells.length, colNames.size()); j++) {
				String content = ExcelUtils.getCellString(cells, j);
				if(content!=null) {
					content = content.trim().replaceAll("\\xa0", "").replaceAll(new String(new char[]{65533}), "");
				}
				//根据表头设置属性
				String colName = (String)colNames.get(j);
				if("姓名".equals(colName)) {
					appraiser.setName(content); //姓名
				}
				else if(colName.indexOf("单位或居住地")!=-1 || colName.indexOf("工作单位")!=-1 || colName.equals("单位") || colName.equals("居住地")) {
					appraiser.setUnit(content); //单位或居住地
				}
				else if(colName.indexOf("通讯地址")!=-1 || colName.equals("地址")) {
					appraiser.setAddress(content); //通讯地址
				}
				else if(colName.equals("区域")) {
					appraiser.setArea(content); //区域
				}
				else if(colName.equals("乡镇或街道") || colName.equals("街道") || colName.equals("乡镇")) {
					appraiser.setStreet(content); //乡镇或街道
				}
				else if(colName.equals("居住地类别") || colName.equals("居住地类型")) {
					appraiser.setAreaType(content); //居住地类别,1、城区市区（不含县市级）；2、县城城区；3、乡镇政府所在地（不含县城所在地）；4、农村
				}
				else if(colName.indexOf("手机")!=-1 || colName.indexOf("电话")!=-1) {
					appraiser.setMobileNumber(content); //手机号码
				}
				else if(colName.equals("身份") || colName.equals("身份类别")) {
					appraiser.setJob(content); //身份,人大代表,政协委员,民评代表,特邀监察员,党风,效能监督员,党代表,企业主,其他,  旧分类:公务员,人大代表,政协委员,企业主,城市居民,农民
				}
				else if(colName.indexOf("人大")!=-1 || colName.indexOf("政协")!=-1) {
					appraiser.setNpcLevel(content); //哪级人大代表政协委员
				}
				else if(colName.equals("级别")) {
					appraiser.setLevel(content); //级别,地厅级,县处级,乡科级,其它
				}
				else if(colName.equals("文化程度")) {
					appraiser.setEducation(content); //; //文化程度,暂不适用
				}
				else if(colName.indexOf("服务")!=-1 && colName.indexOf("单位")!=-1) {
					appraiser.setServiceUnit(content); //; //提供服务或管理单位,只对管理服务对象有效
				}
				else if(colName.indexOf("服务")!=-1 && colName.indexOf("项目")!=-1) {
					appraiser.setServiceContent(content); //; //服务或管理项目、内容,只对管理服务对象有效
				}
				else if(colName.equals("备注")) {
					appraiser.setRemark(content); //备注
				}
			}
			if(appraiser.getName()==null || appraiser.getName().isEmpty() || //姓名为空
			   appraiser.getMobileNumber()==null || appraiser.getMobileNumber().isEmpty()) { //手机号码为空
				continue;
			}
			if(validateAppraiserOnly) {
				//检查身份类别
				String jobTypes = (String)FieldUtils.getRecordField(Appraiser.class.getName(), "job", null).getParameter("itemsText");
				if(("\\0" + jobTypes + "\\0").indexOf("\\0" + appraiser.getJob() + "\\0")==-1) {
					wrongRowNumbers = (wrongRowNumbers==null ? "" : wrongRowNumbers + "、") + (i+1);
				}
				continue;
			}
			if(appraiser.getType()==APPRAISER_TYPE_RECIPIENT && (appraiser.getServiceUnit()==null || appraiser.getServiceUnit().isEmpty())) { //设置服务单位
				appraiser.setServiceUnit(orgName);
			}
			if(appraiser.getArea()==null || appraiser.getArea().isEmpty()) { //设置所在区域
				appraiser.setArea(areaName);
			}
			if(appraiser.getJob()==null || appraiser.getJob().isEmpty()) {
				appraiser.setJob("其他");
			}
			//保存
			save(appraiser);
			appraisers.add(appraiser);
			limit[0]--;
		}
		if(validateAppraiserOnly) {
			return wrongRowNumbers;
		}
		return null;
	}

	/**
	 * 根据导入ID删除已经导入的数据
	 * @param importId
	 * @throws ServiceException
	 */
	private void deleteImportedAppraisers(long importId) throws ServiceException {
		getDatabaseService().deleteRecordsByHql("from Appraiser Appraiser where Appraiser.importId=" + importId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.appraiser.service.AppraiserService#completeImportRecipients(com.yuanluesoft.appraise.appraiser.pojo.AppraiserImport, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void completeImportRecipients(AppraiserImport appraiserImport, SessionInfo sessionInfo) throws ServiceException {
		String hql = "from Appraiser Appraiser where Appraiser.importId=" + appraiserImport.getId();
		List appraisers = getDatabaseService().findRecordsByHql(hql);
		if(appraisers==null || appraisers.isEmpty()) {
			return;
		}
		//把原来的服务对象设置为已经过期
		Appraiser appraiser = (Appraiser)appraisers.iterator().next();
		Date expireBegin = DateTimeUtils.set(appraiser.getExpire(), Calendar.DAY_OF_MONTH, 1);
		setAppraisersDisabled(appraiserImport.getUnitId(), appraiserImport.getId(), -1, expireBegin, DateTimeUtils.add(expireBegin, Calendar.MONTH, 1));
		//把新服务对象设置为启用
		for(Iterator iterator = appraisers.iterator(); iterator.hasNext();) {
			appraiser = (Appraiser)iterator.next();
			appraiser.setStatus(AppraiserService.APPRAISER_STATUS_ENABLED);
			getDatabaseService().updateRecord(appraiser);
		}
		//保存审批人信息
		appraiserImport.setApprover(sessionInfo.getUserName());
		appraiserImport.setApproverId(sessionInfo.getUserId());
		appraiserImport.setApprovalTime(DateTimeUtils.now());
		getDatabaseService().updateRecord(appraiserImport);
		
		//删除上传任务
		hql = "from AppraiserImportTask AppraiserImportTask" +
			  " where AppraiserImportTask.unitId=" + appraiserImport.getUnitId() +
			  " and AppraiserImportTask.taskYear=" + DateTimeUtils.getYear(appraiserImport.getCreated()) + //年度
			  " and AppraiserImportTask.taskMonth=" + (DateTimeUtils.getMonth(appraiserImport.getCreated()) + 1); //月份
		getDatabaseService().deleteRecordsByHql(hql);

	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.appraiser.service.AppraiserService#setAppraisersDisabled(long, int)
	 */
	public void setAppraisersDisabled(long orgId, int appraiserType) throws ServiceException {
		setAppraisersDisabled(orgId, 0, appraiserType, null, null);
	}
	
	/**
	 * 将评议员设为无效,如果评议员未参加过评议,则直接删除
	 * @param orgId
	 * @param exceptImportId
	 * @param expireBegin
	 * @param expireEnd
	 * @throws ServiceException
	 */
	private void setAppraisersDisabled(long orgId, long exceptImportId, int appraiserType, Date expireBegin, Date expireEnd) throws ServiceException {
		//删除未参加过评议的评议员
		String hql = "from Appraiser Appraiser" +
					 " where Appraiser.orgId=" + orgId +
					 " and Appraiser.status=" + AppraiserService.APPRAISER_STATUS_ENABLED +
					 (exceptImportId>0 ? " and Appraiser.importId!=" + exceptImportId : " and Appraiser.type=" + appraiserType) +
					 (expireBegin==null ? "" : " and Appraiser.expire>=DATE(" + DateTimeUtils.formatDate(expireBegin, null) + ")") +
					 (expireEnd==null ? "" : " and Appraiser.expire<DATE(" + DateTimeUtils.formatDate(expireEnd, null) + ")") +
					 " and (select min(AppraiseSms.id) from AppraiseSms AppraiseSms where AppraiseSms.appraiserId=Appraiser.id) is null";
		getDatabaseService().deleteRecordsByHql(hql);
		
		//已参加过评议的评议员设为过期
		hql = "from Appraiser Appraiser" +
			  " where Appraiser.orgId=" + orgId +
			  " and Appraiser.status<" + AppraiserService.APPRAISER_STATUS_DISABLED +
			  (exceptImportId>0 ? " and Appraiser.importId!=" + exceptImportId : " and Appraiser.type=" + appraiserType) +
			  (expireBegin==null ? "" : " and Appraiser.expire>=DATE(" + DateTimeUtils.formatDate(expireBegin, null) + ")") +
			  (expireEnd==null ? "" : " and Appraiser.expire<DATE(" + DateTimeUtils.formatDate(expireEnd, null) + ")");
		List appraisers = getDatabaseService().findRecordsByHql(hql);
		for(Iterator iterator = appraisers==null ? null : appraisers.iterator(); iterator!=null && iterator.hasNext();) {
			Appraiser appraiser = (Appraiser)iterator.next();
			appraiser.setStatus(AppraiserService.APPRAISER_STATUS_DISABLED);
			getDatabaseService().updateRecord(appraiser);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.appraiser.service.AppraiserService#listImportedRecipients(long)
	 */
	public List listImportedRecipients(long importId) throws ServiceException {
		String hql = "from Appraiser Appraiser" +
					 " where Appraiser.importId=" + importId +
					 " order by Appraiser.mobileNumber";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.appraiser.service.AppraiserService#listOrgAppraisers(long, int, int, int, java.lang.String, int, int)
	 */
	public List listOrgAppraisers(long orgId, int year, int month, int appraiserType, String appraiserJobs, int offset, int limit) throws ServiceException {
		Date date = null;
		try {
			date = DateTimeUtils.parseDate(year + "-" + month + "-1", "yyyy-M-d");
		}
		catch (ParseException e) {
			
		}
		String hqlAppraiserOrg = "Appraiser.orgId=" + orgId;
		Org org = getOrgService().getOrg(orgId);
		if((org instanceof Area) && appraiserType==APPRAISER_TYPE_RECIPIENT) { //地区,且要获取服务对象
			hqlAppraiserOrg = "Appraiser.orgId in (select OrgSubjection.directoryId from OrgSubjection OrgSubjection where OrgSubjection.parentDirectoryId=" + orgId + ")";
		}
		//获取离date最近的评议员有效期
		String hql = "select Appraiser.expire" +
					 " from Appraiser Appraiser" +
					 " where " + hqlAppraiserOrg +
					 " and Appraiser.status=" + APPRAISER_STATUS_ENABLED +
					 " and Appraiser.type=" + appraiserType +
					 " and Appraiser.expire>DATE(" + DateTimeUtils.formatDate(date, null) + ")" +
					 " order by Appraiser.expire";
		Date expire = (Date)getDatabaseService().findRecordByHql(hql);
		if(expire==null) {
			return null;
		}
		hql = " from Appraiser Appraiser" +
			  " where " + hqlAppraiserOrg +
			  (appraiserJobs==null || appraiserJobs.isEmpty() ? "" : " and Appraiser.job in ('" + JdbcUtils.resetQuot(appraiserJobs).replaceAll(",", "','") + "')") +
			  " and Appraiser.status=" + APPRAISER_STATUS_ENABLED +
			  " and Appraiser.type=" + appraiserType +
			  " and Appraiser.expire=DATE(" + DateTimeUtils.formatDate(expire, null) + ")" +
			  " order by Appraiser.id";
		return getDatabaseService().findRecordsByHql(hql, offset, limit);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#getApplicationNavigator(java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ApplicationNavigator getApplicationNavigator(String applicationName, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//获取用户有评议管理权限的目录
		List managedIds = orgService.listDirectoryIds("area", "appraiseManager", true, sessionInfo, 0, 1);
		Tree tree = null;
		if(managedIds!=null && !managedIds.isEmpty()) {
			tree = createAreaTree(((Number)managedIds.get(0)).longValue(), request, sessionInfo);
		}
		else if((managedIds = orgService.listDirectoryIds("unit", "appraiseTransactor", true, sessionInfo, 0, 1))!=null && !managedIds.isEmpty()) { //获取用户有评议责任的单位
			long unitId = ((Number)managedIds.get(0)).longValue(); 
			tree = new Tree("" + unitId, orgService.getDirectoryName(unitId) + "评议员信息库", "unit", Environment.getContextPath() + "/jeaf/usermanage/icons/unit.gif");
			tree.getRootNode().setHasChildNodes(false);
		}
		else { //用户没有评议权限
			tree = new Tree("0", "评议员信息库", "root", Environment.getContextPath() + "/jeaf/usermanage/icons/root.gif");
			tree.getRootNode().setHasChildNodes(false);
		}
		resetTreeNode(tree.getRootNode(), null); //设置获取数据的URL
		return new ApplicationTreeNavigator(tree);
	}
	
	/**
	 * 创建地区目录树
	 * @param areaId
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	private Tree createAreaTree(long areaId, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String areaName = orgService.getDirectoryName(areaId);
		Tree tree = new Tree(areaId + "", areaName + "评议员信息库", "root", Environment.getContextPath() + "/jeaf/usermanage/icons/root.gif");
		//创建基础库目录
		List categoryAreaNodes = orgService.listChildTreeNodes(areaId, "category,area", null, null, null, sessionInfo); //获取下级目录列表
		TreeNode node = tree.appendChildNode(areaId + "_" + APPRAISER_TYPE_BASIC + "_category", "基础评议员", "category", Environment.getContextPath() + "/jeaf/usermanage/icons/category.gif", categoryAreaNodes!=null && !categoryAreaNodes.isEmpty());
		node.setExpandTree(true);
		//添加省直/市直/县直/区直节点
		String nodeName = areaName;
		if(categoryAreaNodes==null) {
			categoryAreaNodes = new ArrayList();
		}
		else if(areaName.endsWith("省")) {
			nodeName = "省直";
		}
		else if(areaName.endsWith("市")) {
			nodeName = "市直";
		}
		else if(areaName.endsWith("县")) {
			nodeName = "县直";
		}
		else if(areaName.endsWith("区")) {
			nodeName = "区直";
		}
		categoryAreaNodes.add(0, new TreeNode(areaId + "_" + APPRAISER_TYPE_BASIC + "_direct", nodeName, "area", Environment.getContextPath() + "/jeaf/usermanage/icons/area.gif", false));
		node.setChildNodes(categoryAreaNodes);
		
		//创建动态库目录
		List categoryAreaUnitNodes = orgService.listChildTreeNodes(areaId, "category,area,unit", null, null, null, sessionInfo); //获取下级目录列表
		node = tree.appendChildNode(areaId + "_" + APPRAISER_TYPE_RECIPIENT + "_category", "动态评议员", "category", Environment.getContextPath() + "/jeaf/usermanage/icons/category.gif", categoryAreaUnitNodes!=null && !categoryAreaUnitNodes.isEmpty());
		//添加省直/市直/县直/区直节点
		if(categoryAreaUnitNodes==null) {
			categoryAreaUnitNodes = new ArrayList();
		}
		List unitNodes = new ArrayList();
		for(Iterator iterator = categoryAreaUnitNodes.iterator(); iterator.hasNext();) {
			TreeNode treeNode = (TreeNode)iterator.next();
			if("unit".equals(treeNode.getNodeType())) {
				unitNodes.add(treeNode);
				iterator.remove();
			}
		}
		TreeNode directNode = new TreeNode(areaId + "_" + APPRAISER_TYPE_RECIPIENT + "_direct", nodeName, "area", Environment.getContextPath() + "/jeaf/usermanage/icons/area.gif", !unitNodes.isEmpty());
		directNode.setChildNodes(unitNodes);
		categoryAreaUnitNodes.add(0, directNode);
		node.setChildNodes(categoryAreaUnitNodes);
		
		//创建评议代表目录
		categoryAreaNodes = orgService.listChildTreeNodes(areaId, "category,area", null, null, null, sessionInfo); //获取下级目录列表
		node = tree.appendChildNode(areaId + "_" + APPRAISER_TYPE_DELEGATE + "_category", "评议代表", "category", Environment.getContextPath() + "/jeaf/usermanage/icons/category.gif", categoryAreaNodes!=null && !categoryAreaNodes.isEmpty());
		//添加省直/市直/县直/区直节点
		if(categoryAreaNodes==null) {
			categoryAreaNodes = new ArrayList();
		}
		categoryAreaNodes.add(0, new TreeNode(areaId + "_" + APPRAISER_TYPE_DELEGATE + "_direct", nodeName, "area", Environment.getContextPath() + "/jeaf/usermanage/icons/area.gif", false));
		node.setChildNodes(categoryAreaNodes);
		return tree;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#listApplicationNavigatorTreeNodes(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listApplicationNavigatorTreeNodes(String applicationName, String parentTreeNodeId, SessionInfo sessionInfo) throws ServiceException {
		String[] values = parentTreeNodeId.split("_");
		String popedomNames = "appraiseManager,appraiseTransactor";
		if(orgService.checkParentDirectoryPopedom(Long.parseLong(values[0]), popedomNames, sessionInfo)) { //用户有父目录的权限
			popedomNames = null; //不检查子目录权限
		}
		String orgTypes = "category,area";
		if(values.length>1 && ("" + APPRAISER_TYPE_RECIPIENT).equals(values[1])) {
			orgTypes += ",unit";
		}
		List treeNodes = orgService.listChildTreeNodes(Long.parseLong(values[0]), orgTypes, popedomNames, null, null, sessionInfo);
		if(treeNodes==null || treeNodes.isEmpty()) {
			return null;
		}
		for(Iterator iterator = treeNodes.iterator(); iterator.hasNext();) {
			TreeNode node = (TreeNode)iterator.next();
			resetTreeNode(node, parentTreeNodeId);
		}
		return treeNodes;
	}
	
	/**
	 * 递归函数:设置获取数据的URL
	 * @param treeNode
	 * @return
	 */
	private void resetTreeNode(TreeNode treeNode, String parentNodeId) {
		String[] values = treeNode.getNodeId().split("_");
		if(values.length==1 && parentNodeId!=null) { //没有指定评议员类型
			//重设目录ID
			treeNode.setNodeId(treeNode.getNodeId() + "_" + parentNodeId.split("_")[1]);
			values = treeNode.getNodeId().split("_");
		}
		//设置获取数据URL
		String viewName = null;
		if("unit".equals(treeNode.getNodeType())) {
			viewName = "admin/recipient";
		}
		else if(values.length<2 || values[1].equals("" + APPRAISER_TYPE_BASIC)) { //基础库
			viewName = "admin/appraiser";
		}
		else if(values.length>=2 && values[1].equals("" + APPRAISER_TYPE_DELEGATE)) { //评议代表
			viewName = "admin/delegate";
		}
		else {
			viewName = "admin/recipientByArea";
		}
		treeNode.setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/appraise/appraiser/admin/appraiserView.shtml?viewName=" + viewName + "&orgId=" + values[0] + (values.length>2 && values[2].equals("direct") ? "&direct=true" : ""));
		
		//设置子节点的获取数据URL
		if(treeNode.getChildNodes()!=null) {
			for(Iterator iterator = treeNode.getChildNodes().iterator(); iterator.hasNext();) {
				TreeNode childNode = (TreeNode)iterator.next();
				resetTreeNode(childNode, treeNode.getNodeId());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#saveApplicationNavigatorDefinition(java.lang.String, com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition)
	 */
	public void saveApplicationNavigatorDefinition(String applicationName, ApplicationNavigatorDefinition navigatorDefinition) throws ServiceException {
		
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
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
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
}