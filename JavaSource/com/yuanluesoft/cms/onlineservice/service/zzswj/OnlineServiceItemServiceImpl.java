package com.yuanluesoft.cms.onlineservice.service.zzswj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemCodeRule;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemGuide;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemSubjection;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemTransactor;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemUnit;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.numeration.service.NumerationCallback;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceItemServiceImpl extends BusinessServiceImpl implements OnlineServiceItemService {
	private OnlineServiceDirectoryService onlineServiceDirectoryService; //网上办事目录服务
	private SiteResourceService siteResourceService; //站点资源服务
	public ExchangeClient exchangeClient; //数据交换服务
	private PageService pageService; //页面服务
	private NumerationService numerationService; //编号服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if((record instanceof OnlineServiceItem) || (record instanceof OnlineServiceInteractive)) {
			exchangeClient.synchUpdate(record, null, 2000);
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重建静态页面
			if(record instanceof OnlineServiceItem) {
				OnlineServiceItem onlineServiceItem = (OnlineServiceItem)record;
				//禁止行政处罚,行政强制,行政征收在线申报
				if(",行政处罚,行政强制,行政征收,".indexOf("," + onlineServiceItem.getItemType() + ",")!=-1) {
					onlineServiceItem.setAcceptSupport('0');
				}
				synchSite(onlineServiceItem); //同步到网站
			}
		}
		else if(record instanceof OnlineServiceItemMaterial) {
			OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)record;
			if(material.getName()!=null && material.getName().isEmpty()) {
				material.setName(null);
			}
			if(material.getTableURL()!=null && material.getTableURL().isEmpty()) {
				material.setTableURL(null);
			}
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if((record instanceof OnlineServiceItem) || (record instanceof OnlineServiceInteractive)) {
			exchangeClient.synchUpdate(record, null, 2000);
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重建静态页面
			if(record instanceof OnlineServiceItem) {
				OnlineServiceItem onlineServiceItem = (OnlineServiceItem)record;
				if(onlineServiceItem.getIsPublic()!='1') { //不公开
					siteResourceService.deleteResourceBySourceRecordId("" + record.getId()); //从站点删除
				}
				else {
					synchSite(onlineServiceItem); //同步到网站
				}
			}
		}
		else if(record instanceof OnlineServiceItemMaterial) {
			OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)record;
			if(material.getName()!=null && material.getName().isEmpty()) {
				material.setName(null);
			}
			if(material.getTableURL()!=null && material.getTableURL().isEmpty()) {
				material.setTableURL(null);
			}
		}
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if((record instanceof OnlineServiceItem) || (record instanceof OnlineServiceInteractive)) {
			exchangeClient.synchDelete(record, null, 2000);
			//重建静态页面
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
			if(record instanceof OnlineServiceItem) {
				//从站点删除
				siteResourceService.deleteResourceBySourceRecordId("" + record.getId());
			}
		}
	}
	
	/**
	 * 办理事项同步到网站
	 * @param onlineServiceItem
	 * @throws ServiceException
	 */
	private void synchSite(OnlineServiceItem onlineServiceItem) throws ServiceException {
		if(onlineServiceItem.getIsPublic()!='1') { //未公开
			return;
		}
		if(onlineServiceItem.getSubjections()==null || onlineServiceItem.getSubjections().isEmpty()) {
			return;
		}
		//获取目录
		String issueSiteIds = onlineServiceDirectoryService.getSynchSiteIds(ListUtils.join(onlineServiceItem.getSubjections(), "directoryId", ",", false), "item");
		if(issueSiteIds==null || "".equals(issueSiteIds)) {
			siteResourceService.deleteResourceBySourceRecordId("" + onlineServiceItem.getId());
			return;
		}
		String link = Environment.getContextPath() + "/cms/onlineservice/guide.shtml" +
					  "?itemId=" + onlineServiceItem.getId() +
					  "&directoryId=" + ((OnlineServiceItemSubjection)onlineServiceItem.getSubjections().iterator().next()).getDirectoryId();
		OnlineServiceItemGuide guide = onlineServiceItem.getGuide()==null || onlineServiceItem.getGuide().isEmpty() ? null : (OnlineServiceItemGuide)onlineServiceItem.getGuide().iterator().next();
		siteResourceService.addResource(issueSiteIds,
										SiteResourceService.RESOURCE_TYPE_LINK,
										onlineServiceItem.getName(), //标题
										null, //副标题
										null, //来源
										null, //作者
										null, //关键字
										null,
										SiteResourceService.ANONYMOUS_LEVEL_ALL,
										link, //链接
										onlineServiceItem.getCreated(), //创建时间
										onlineServiceItem.getCreated(), //发布时间
										true, //设置为已发布
										(guide==null ? null : guide.getCondition() + guide.getAccording() + guide.getProgram() + guide.getTimeLimit() + guide.getChargeAccording() + guide.getChargeStandard()), //正文,用于搜索
										"" + onlineServiceItem.getId(),
										onlineServiceItem.getClass().getName(),
										link,
										onlineServiceItem.getCreatorId(),
										onlineServiceItem.getCreator(),
										0,
										null,
										0,
										null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#generateItemCode(com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem, long)
	 */
	public void generateItemCode(OnlineServiceItem onlineServiceItem, long directoryId) throws ServiceException {
		//获取编号规则
		String hql = "from OnlineServiceItemCodeRule OnlineServiceItemCodeRule " +
					  "where OnlineServiceItemCodeRule.itemType='" + JdbcUtils.resetQuot(onlineServiceItem.getItemType()) + "'";
		OnlineServiceItemCodeRule rule = (OnlineServiceItemCodeRule)getDatabaseService().findRecordByHql(hql);
		if(rule==null) {
			return;
		}
		if(onlineServiceItem.getCode()!=null && !onlineServiceItem.getCode().trim().isEmpty() && rule.getManualCodeEnabled()=='1') { //不为空,且允许手工编号
			return;
		}
		//获取目录
		OnlineServiceDirectory directory = (OnlineServiceDirectory)onlineServiceDirectoryService.getDirectory(directoryId);
		//获取父目录
		List parentDirectories = onlineServiceDirectoryService.listParentDirectories(directoryId, "mainDirectory");
		//获取目录编号
		String directoryCode = null;
		for(Iterator iterator = parentDirectories==null ? null : parentDirectories.iterator(); iterator!=null && iterator.hasNext();) {
			OnlineServiceDirectory parentDirectory = (OnlineServiceDirectory)iterator.next();
			if(parentDirectory.getCode()!=null && !parentDirectory.getCode().trim().isEmpty()) {
				directoryCode = (directoryCode==null ? "" : directoryCode) + parentDirectory.getCode();
			}
		}
		if(directory.getCode()!=null && !directory.getCode().trim().isEmpty()) {
			directoryCode = (directoryCode==null ? "" : directoryCode) + directory.getCode();
		}
		if(directoryCode==null) {
			return;
		}
		final String myDirectoryCode = directoryCode;
		//编号
		String code = numerationService.generateNumeration("网上办事", "事项编号", rule.getRule(), false, new NumerationCallback() {
			public Object getFieldValue(String fieldName, int fieldLength) {
				if("目录编号".equals(fieldName)) {
					return myDirectoryCode;
				}
				return null;
			}
		});
		onlineServiceItem.setCode(code);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#resynchServiceItems(long)
	 */
	public void resynchServiceItems(long directoryId) throws ServiceException {
		String hql;
		if(directoryId==0) { //根目录
			hql = "from OnlineServiceItem OnlineServiceItem" +
				  " order by OnlineServiceItem.id";
		}
		else {
			hql = "select OnlineServiceItem" +
			 	  " from OnlineServiceItem OnlineServiceItem, OnlineServiceItemSubjection OnlineServiceItemSubjection, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
			 	  " where OnlineServiceItem.isPublic='1'" +
			 	  " and OnlineServiceItemSubjection.itemId=OnlineServiceItem.id" +
			 	  " and OnlineServiceItemSubjection.directoryId=OnlineServiceDirectorySubjection.directoryId" +
			 	  " and OnlineServiceDirectorySubjection.parentDirectoryId=" + directoryId +
			 	  " order by OnlineServiceItem.id";
		}
		List items = null;
		for(int i=0; i==0 || (items!=null && items.size()==100); i+=100) {
			items = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections"), i, 100);
			if(items==null || items.isEmpty()) {
				break;
			}
			for(Iterator iterator = items.iterator(); iterator.hasNext();) {
				OnlineServiceItem onlineServiceItem = (OnlineServiceItem)iterator.next();
				synchSite(onlineServiceItem);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#getOnlineServiceItem(long)
	 */
	public OnlineServiceItem getOnlineServiceItem(long id) throws ServiceException {
		return (OnlineServiceItem)load(OnlineServiceItem.class, id);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#listOnlineServiceItems(long, boolean)
	 */
	public List listOnlineServiceItems(long directoryId, boolean publicOnly) throws ServiceException {
		String hql = "select OnlineServiceItem" +
					 " from OnlineServiceItem OnlineServiceItem, OnlineServiceItemSubjection OnlineServiceItemSubjection" +
					 " where OnlineServiceItemSubjection.itemId=OnlineServiceItem.id" +
					 " and OnlineServiceItemSubjection.directoryId=" + directoryId +
					 (publicOnly ? " and OnlineServiceItem.isPublic='1'" : "") +
					 " order by OnlineServiceItem.priority DESC, OnlineServiceItem.name";
		return getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections", ","));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#importOnlineServiceItems(long, java.lang.String)
	 */
	public void importOnlineServiceItems(long directoryId, String importServiceItemIds) throws ServiceException {
		if(importServiceItemIds==null || importServiceItemIds.equals("")) {
			return;
		}
		String hql = "from OnlineServiceItem OnlineServiceItem where OnlineServiceItem.id in (" + JdbcUtils.validateInClauseNumbers(importServiceItemIds) + ")";
		List serviceItems = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("subjections", ","));
		for(Iterator iterator = serviceItems.iterator(); iterator.hasNext();) {
			OnlineServiceItem serviceItem = (OnlineServiceItem)iterator.next();
			if(ListUtils.findObjectByProperty(serviceItem.getSubjections(), "directoryId", new Long(directoryId))==null) { //不在原来的目录列表中
				OnlineServiceItemSubjection subjection = new OnlineServiceItemSubjection();
				subjection.setId(UUIDLongGenerator.generateId()); //ID
				subjection.setItemId(serviceItem.getId()); //办理事项ID
				subjection.setDirectoryId(directoryId); //隶属目录ID
				getDatabaseService().saveRecord(subjection);
				serviceItem.getSubjections().add(subjection);
			}
			//同步更新
			exchangeClient.synchUpdate(serviceItem, null, 2000);
			//重建静态页面
			pageService.rebuildStaticPageForModifiedObject(serviceItem, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			//同步到网站
			synchSite(serviceItem);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemSerivce#copyServiceItem(long, long)
	 */
	public void copyServiceItem(long fromDirectoryId, long toDirectoryId) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemSerivce#updateSericeItemSubjectios(com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem, boolean, java.lang.String)
	 */
	public void updateServiceItemSubjectios(OnlineServiceItem serviceItem, boolean isNew, String subjectionDirectoryIds) throws ServiceException {
		if(subjectionDirectoryIds==null || subjectionDirectoryIds.equals("")) {
			return;
		}
		String[] ids = subjectionDirectoryIds.split(",");
		long firstDirectoryId = Long.parseLong(ids[0]);
		boolean firstDirectoryChanged = true;
		String oldSubjectionDirectoryIds = null; //旧的目录隶属关系
		if(!isNew) {
			//检查隶属栏目是否发生变化
			oldSubjectionDirectoryIds = ListUtils.join(serviceItem.getSubjections(), "directoryId", ",", false);
			if(subjectionDirectoryIds.equals(oldSubjectionDirectoryIds)) {
				return;
			}
			firstDirectoryChanged = serviceItem.getSubjections()==null || serviceItem.getSubjections().isEmpty() || (firstDirectoryId!=((OnlineServiceItemSubjection)serviceItem.getSubjections().iterator().next()).getDirectoryId());
			//删除旧的隶属关系
			for(Iterator iterator = serviceItem.getSubjections()==null ? null : serviceItem.getSubjections().iterator(); iterator!=null && iterator.hasNext();) {
				OnlineServiceItemSubjection subjection = (OnlineServiceItemSubjection)iterator.next();
				getDatabaseService().deleteRecord(subjection);
				if(("," + subjectionDirectoryIds + ",").indexOf("," + subjection.getDirectoryId() + ",")==-1) { //已经被删除了
					//重建静态页面
					pageService.rebuildStaticPageForModifiedObject(subjection, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
				}
			}
		}
		//保存新的隶属关系
		serviceItem.setSubjections(new HashSet());
		for(int i=0; i<ids.length; i++) {
			OnlineServiceItemSubjection subjection = new OnlineServiceItemSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setItemId(serviceItem.getId());
			subjection.setDirectoryId(Long.parseLong(ids[i]));
			getDatabaseService().saveRecord(subjection);
			serviceItem.getSubjections().add(subjection);
			if(!firstDirectoryChanged && ("," + oldSubjectionDirectoryIds + ",").indexOf("," + subjection.getDirectoryId() + ",")==-1) { //新增的
				//重建静态页面
				pageService.rebuildStaticPageForModifiedObject(subjection, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
		}
		if(firstDirectoryChanged) {
			update(serviceItem);
		}
		else {
			//同步到网站
			synchSite(serviceItem);
			//同步更新
			exchangeClient.synchUpdate(serviceItem, null, 2000);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#updateSericeItemGuide(com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem, com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemGuide)
	 */
	public void updateServiceItemGuide(OnlineServiceItem serviceItem, OnlineServiceItemGuide guide) throws ServiceException {
		//保存指南
		guide.setItemId(serviceItem.getId());
		if(guide.getId()==0) {
			guide.setId(UUIDLongGenerator.generateId());
			getDatabaseService().saveRecord(guide);
		}
		else {
			getDatabaseService().updateRecord(guide);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#updateSericeItemTransactors(com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem, boolean, java.lang.String, java.lang.String)
	 */
	public void updateServiceItemTransactors(OnlineServiceItem serviceItem, boolean isNew, String userIds, String userNames) throws ServiceException {
		if(userIds==null) {
			return;
		}
		if(!isNew) { //不是新记录
			//删除原来的隶属关系
			getDatabaseService().deleteRecordsByHql("from OnlineServiceItemTransactor OnlineServiceItemTransactor where OnlineServiceItemTransactor.itemId=" + serviceItem.getId());
		}
		if(userIds.equals("")) {
			return;
		}
		//保存新的经办人
		String[] ids = userIds.split(",");
		String[] names = userNames.split(",");
		for(int i=0; i<ids.length; i++) {
			OnlineServiceItemTransactor transactor = new OnlineServiceItemTransactor();
			transactor.setId(UUIDLongGenerator.generateId());
			transactor.setItemId(serviceItem.getId());
			transactor.setUserId(Long.parseLong(ids[i]));
			transactor.setUserName(names[i]);
			getDatabaseService().saveRecord(transactor);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#updateSericeItemUnits(com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem, boolean, java.lang.String, java.lang.String)
	 */
	public void updateServiceItemUnits(OnlineServiceItem serviceItem, boolean isNew, String unitIds, String unitNames) throws ServiceException {
		if(unitNames==null) {
			return;
		}
		if(!isNew) { //不是新记录
			//删除原来的隶属关系
			getDatabaseService().deleteRecordsByHql("from OnlineServiceItemUnit OnlineServiceItemUnit where OnlineServiceItemUnit.itemId=" + serviceItem.getId());
		}
		//保存新的单位列表
		String[] ids = unitIds.split(",");
		String[] names = unitNames.split(",");
		for(int i=0; i<names.length; i++) {
			if(names[i].isEmpty()) {
				continue;
			}
			OnlineServiceItemUnit unit = new OnlineServiceItemUnit();
			unit.setId(UUIDLongGenerator.generateId());
			unit.setItemId(serviceItem.getId());
			try {
				unit.setUnitId(Long.parseLong(ids[i]));
			}
			catch(Exception e) {
				unit.setUnitId(0);
			}
			unit.setUnitName(names[i]);
			getDatabaseService().saveRecord(unit);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#getWorkflowId(long, java.lang.String)
	 */
	public String getWorkflowId(long serviceItemId, String worflowType) throws ServiceException {
		try {
			//获取办理事项
			OnlineServiceItem onlineServiceItem = (OnlineServiceItem)load(OnlineServiceItem.class, serviceItemId);
			Character support = (Character)PropertyUtils.getProperty(onlineServiceItem, worflowType + "Support");
			if(support==null || support.charValue()!='1') {
				return null;
			}
			String workflowId = null;
			workflowId = (String)PropertyUtils.getProperty(onlineServiceItem, worflowType + "WorkflowId");
			if(workflowId!=null && !workflowId.equals("")) { //办理事项有自定义的流程
				return workflowId;
			}
			long directoryId = ((OnlineServiceItemSubjection)onlineServiceItem.getSubjections().iterator().next()).getDirectoryId();
			//获取目录的流程配置
			return onlineServiceDirectoryService.getWorkflowId(directoryId, worflowType);
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#listSericeItemTransactors(long)
	 */
	public List listServiceItemTransactors(long serviceItemId) throws ServiceException {
		//获取办理事项本身的经办人
		List transactors = getDatabaseService().findRecordsByHql("from OnlineServiceItemTransactor OnlineServiceItemTransactor where OnlineServiceItemTransactor.itemId=" + serviceItemId + " order by OnlineServiceItemTransactor.id");
		if(transactors!=null && !transactors.isEmpty()) { //办理事项有指定经办人
			return transactors;
		}
		//获取所在目录的经办人
		OnlineServiceDirectory onlineServiceDirectory = (OnlineServiceDirectory)onlineServiceDirectoryService.getDirectory(((Number)getDatabaseService().findRecordByHql("select OnlineServiceItemSubjection.directoryId from OnlineServiceItemSubjection OnlineServiceItemSubjection where OnlineServiceItemSubjection.itemId=" + serviceItemId + " order by OnlineServiceItemSubjection.id")).longValue());
		return ListUtils.getSubListByProperty(onlineServiceDirectory.getDirectoryPopedoms(), "popedomName", "transactor");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#listSericeItemManagers(long)
	 */
	public List listServiceItemManagers(long serviceItemId) throws ServiceException {
		OnlineServiceDirectory onlineServiceDirectory = (OnlineServiceDirectory)onlineServiceDirectoryService.getDirectory(((Number)getDatabaseService().findRecordByHql("select OnlineServiceItemSubjection.directoryId from OnlineServiceItemSubjection OnlineServiceItemSubjection where OnlineServiceItemSubjection.itemId=" + serviceItemId + " order by OnlineServiceItemSubjection.id")).longValue());
		return ListUtils.getSubListByProperty(onlineServiceDirectory.getDirectoryPopedoms(), "popedomName", "manager");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#importServiceItems(java.util.List)
	 */
	public void importServiceItems(List uploadFiles) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService#importAuthority(long, java.util.List, boolean)
	 */
	public void importAuthority(long directoryId, List uploadFiles, boolean sheetAsDirectory) throws ServiceException {
		if(uploadFiles==null || uploadFiles.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		Attachment attachment = (Attachment)uploadFiles.get(0);
		Workbook wb = null;
		try {
			//导入数据
			wb = Workbook.getWorkbook(new File(attachment.getFilePath())); //构造Workbook（工作薄）对象
			for(int i=0; i<wb.getSheets().length; i++) {
				Sheet sheet = wb.getSheet(i);
				long dir = directoryId;
				if(sheetAsDirectory && sheet.getName()!=null && sheet.getName().toLowerCase().indexOf("sheet")==-1) {
					dir = onlineServiceDirectoryService.createDirectory(-1, directoryId, sheet.getName().trim(), "directory", null, 0, null).getId();
				}
				importSheet(sheet, dir); //导入SHEET
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException("导入失败");
		}
		finally {
			try {
				wb.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 导入一个SHEET
	 * @param sheet
	 * @param directoryId
	 * @throws ServiceException
	 */
	private void importSheet(Sheet sheet, long directoryId) throws ServiceException {
		Range[] ranges = sheet.getMergedCells();
		List colNames = new ArrayList(); //表头行
		int mergedRows = 1;
		for(int i=0; i<sheet.getRows(); i+=mergedRows) {
			Cell[] cells = sheet.getRow(i);
			mergedRows = getMergedRows(ranges, i, 0); //获取被合并的行数
			//如果表头为空,检查是否表头,判断依据该行必须要有“项目名称”单元格
			if(colNames.isEmpty()) {
				for(int j=0; j<cells.length; j++) {
					String content = getCellContent(sheet, i, i+mergedRows-1, j);
					if(content==null) {
						content = "";
					}
					else {
						content = StringUtils.removePunctuation(content);
					}
					colNames.add(content);
				}
				if(colNames.indexOf("项目名称")==-1 && colNames.indexOf("事项名称")==-1) {
					colNames.clear(); //不含“项目名称”单元格,不是表头
				}
				continue;
			}
			//读取项目
			OnlineServiceItem serviceItem = new OnlineServiceItem();
			serviceItem.setId(UUIDLongGenerator.generateId()); //ID
			OnlineServiceItemGuide guide = new OnlineServiceItemGuide();
			guide.setId(UUIDLongGenerator.generateId());
			guide.setItemId(serviceItem.getId());
			String unitName = null;
			String materials = null;
			for(int j=0; j<cells.length; j++) {
				String content = getCellContent(sheet, i, i + mergedRows - 1, j);
				//根据表头设置属性
				if(j>=colNames.size()) {
					continue;
				}
				String colName = (String)colNames.get(j);
				if("项目名称".equals(colName) || "事项名称".equals(colName)) {
					serviceItem.setName(content);
				}
				else if("类别".equals(colName) || "行政类别".equals(colName) || "事项类别".equals(colName)) {
					serviceItem.setItemType(StringUtils.removePunctuation(content)); //行政许可,行政处罚,行政确认,行政征收,行政强制,行政裁决,行政监督检查,行政给付,其他行政行为
					String[] types = "非行政许可,行政许可,行政处罚,行政确认,行政征收,行政强制,行政裁决,行政监督检查,行政给付,其他行政行为".split(",");
					for(int k=0; k<types.length; k++) {
						if(serviceItem.getItemType()!=null && serviceItem.getItemType().indexOf(types[k])!=-1) {
							serviceItem.setItemType(types[k]);
							break;
						}
					}
					if(serviceItem.getItemType()!=null && (serviceItem.getItemType().indexOf("其他")!=-1 || serviceItem.getItemType().indexOf("其它")!=-1)) {
						serviceItem.setItemType("其他行政行为");
					}
				}
				else if("实施依据".equals(colName) || "法定依据".equals(colName)) {
					guide.setAccording(content);
				}
				else if("申报条件".equals(colName)) {
					guide.setCondition(content);
				}
				else if("办理流程".equals(colName)) {
					guide.setProgram(content);
				}
				else if("数量限制".equals(colName)) {
					guide.setAcceptLimit(content);
				}
				else if("法定时限".equals(colName) || "法定期限".equals(colName)) {
					guide.setLegalLimit(content);
				}
				else if("承诺时限".equals(colName) || "承诺期限".equals(colName)) {
					guide.setTimeLimit(content);
				}
				else if("办理机关".equals(colName)) {
					unitName = content;
				}
				else if("公开形式".equals(colName)) {
					guide.setPublicMode(content);
				}
				else if("公开范围".equals(colName)) {
					guide.setPublicRange(content);
				}
				else if("公开时间".equals(colName)) {
					guide.setPublicTime(content);
				}
				else if("收费依据".equals(colName)) {
					guide.setChargeAccording(content);
				}
				else if("收费标准".equals(colName) || "收费依据和标准".equals(colName) || "征收标准".equals(colName)) {
					guide.setChargeStandard(content);
				}
				else if("承办科室".equals(colName) || "承办科室和负责人".equals(colName) || "责任部门".equals(colName)) {
					guide.setResponsibleDepartment(content);
				}
				else if("受理地址".equals(colName)) {
					guide.setAddress(content);
				}
				else if("受理形式".equals(colName)) {
					guide.setAcceptMode(content);
				}
				else if("办公时间".equals(colName)) {
					guide.setAcceptTime(content);
				}
				else if("责任人".equals(colName) || "经办人".equals(colName) || "联系人".equals(colName)) {
					guide.setTransactor(content);
				}
				else if("联系电话".equals(colName)) {
					guide.setTransactorTel(content);
				}
				else if("监管层级".equals(colName)) {
					guide.setSuperviseLevel(content);
				}
				else if("监督电话".equals(colName)) {
					guide.setComplaintTel(content);
				}
				else if("编码".equals(colName) || "事项编码".equals(colName)) {
					serviceItem.setCode(content);
				}
				else if("备注".equals(colName)) {
					serviceItem.setRemark(content);
				}
				else if("处罚种类".equals(colName)) {
					guide.setPunishType(content);
				}
				else if("裁量规则".equals(colName)) {
					guide.setDiscretionRule(content);
				}
				else if("强制措施".equals(colName)) {
					guide.setCoerciveMeasure(content);
				}
				else if("申报材料".equals(colName)) {
					materials = content;
				}
				else if("结果查询".equals(colName)) {
					if(content!=null && !content.trim().isEmpty()) {
						serviceItem.setResultSupport('1');
						serviceItem.setResultUrl(content.trim());
					}
				}
				else if("状态查询".equals(colName)) {
					if(content!=null && !content.trim().isEmpty()) {
						serviceItem.setQuerySupport('1');
						serviceItem.setQueryUrl(content.trim());
					}
				}
				else if("在线申报".equals(colName)) {
					if(content!=null && !content.trim().isEmpty()) {
						serviceItem.setAcceptSupport('1');
						serviceItem.setAcceptUrl(content.trim());
					}
				}
				else if("在线咨询".equals(colName)) {
					if(content!=null && !content.trim().isEmpty()) {
						serviceItem.setConsultSupport('1');
						serviceItem.setConsultUrl(content.trim());
					}
				}
			}
			if(serviceItem.getName()==null || serviceItem.getName().isEmpty() || //项目名称为空
			   serviceItem.getItemType()==null || serviceItem.getItemType().isEmpty()) { //事项类型为空
			   continue;
			}
			try {
				//保存事项
				serviceItem.setCreator("系统管理员");
				serviceItem.setCreated(DateTimeUtils.now());
				save(serviceItem);
				//保存指南
				save(guide);
				//保存隶属目录
				updateServiceItemSubjectios(serviceItem, true, "" + directoryId);
				//保存单位
				updateServiceItemUnits(serviceItem, true, "0", unitName==null ? onlineServiceDirectoryService.getDirectoryName(directoryId) : unitName);
				//保存申报材料
				//材料格式为(每条材料信息用“&”连接)  材料名称：这里填写材料名称 申报说明：这里填写申报说明。备注：这里填写备注 & 材料名称：这里填写材料名称 申报说明：这里填写申报说明。备注：这里填写备注
				if(materials!=null && !materials.trim().isEmpty()) {
					String[] itemMaterials = materials.split("&");
					for(int j=0; j<itemMaterials.length; j++) {
						itemMaterials[j] = itemMaterials[j].trim();
						if(itemMaterials[j].isEmpty()) {
							continue;
						}
						OnlineServiceItemMaterial material = new OnlineServiceItemMaterial();
						material.setId(UUIDLongGenerator.generateId()); //ID
						material.setItemId(serviceItem.getId()); //办理事项ID
						int descriptionIndex = itemMaterials[j].indexOf("申报说明：");//申报说明：的位置
						int remarkIndex = itemMaterials[j].indexOf("备注：");
						material.setName(itemMaterials[j].substring(5, descriptionIndex)); //申报材料名称
						material.setDescription(itemMaterials[j].substring(descriptionIndex+5, remarkIndex)); //申报说明
						material.setRemark(itemMaterials[j].substring(remarkIndex+3)); //备注
						material.setPriority(itemMaterials.length - j); //优先级
						save(material);
					}
				}
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 获取合并的行数
	 * @param ranges
	 * @param row
	 * @param col
	 * @return
	 */
	private int getMergedRows(Range[] ranges, int row, int col) {
		for(int i=0; i<ranges.length; i++) {
			Cell topLeftCell = ranges[i].getTopLeft();
			Cell bottomRight = ranges[i].getBottomRight();
			if(topLeftCell.getRow()<=row && topLeftCell.getColumn()<=col && bottomRight.getRow()>=row && bottomRight.getColumn()>=col) {
				return bottomRight.getRow() - topLeftCell.getRow() + 1;
			}
		}
		return 1;
	}
	
	/**
	 * 获取单元格内容
	 * @param sheet
	 * @param beginRow
	 * @param endRow
	 * @param col
	 * @return
	 */
	private String getCellContent(Sheet sheet, int beginRow, int endRow, int col) {
		String content = null;
		for(int i=beginRow; i<=endRow; i++) {
			Cell[] cells = sheet.getRow(i);
			String text = ExcelUtils.getCellString(cells, col);
			if(text!=null && !text.trim().isEmpty()) {
				content = (content==null ? "" : content) + text.trim().replaceAll("\\xa0", "").replaceAll(new String(new char[]{65533}), "");
			}
		}
		return content;
	}

	/**
	 * @return the onlineServiceDirectoryService
	 */
	public OnlineServiceDirectoryService getOnlineServiceDirectoryService() {
		return onlineServiceDirectoryService;
	}

	/**
	 * @param onlineServiceDirectoryService the onlineServiceDirectoryService to set
	 */
	public void setOnlineServiceDirectoryService(
			OnlineServiceDirectoryService onlineServiceDirectoryService) {
		this.onlineServiceDirectoryService = onlineServiceDirectoryService;
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
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

	/**
	 * @return the siteResourceService
	 */
	public SiteResourceService getSiteResourceService() {
		return siteResourceService;
	}

	/**
	 * @param siteResourceService the siteResourceService to set
	 */
	public void setSiteResourceService(SiteResourceService siteResourceService) {
		this.siteResourceService = siteResourceService;
	}

	/**
	 * @return the numerationService
	 */
	public NumerationService getNumerationService() {
		return numerationService;
	}

	/**
	 * @param numerationService the numerationService to set
	 */
	public void setNumerationService(NumerationService numerationService) {
		this.numerationService = numerationService;
	}
}