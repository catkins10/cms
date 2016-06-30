package com.yuanluesoft.cms.onlineservice.dataimporter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemFaq;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemGuide;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemUnit;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceMainDirectory;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryTableMapping;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 网上办事数据导入,SQL格式如下：
 "select " +
 " as importDataId," + //被导入的记录ID
 " as directoryId," + //原来的目录ID
 " as code," + //编码
 " as name," + //名称
 " as itemType," + //审批服务类型
 " as acceptUrl," + //在线受理链接
 " as creator," + //创建人
 " as created," + //创建时间
 " as serviceCondition," + //申办条件
 " as serviceAccording," + //办理依据
 " as serviceProgram," + //办理程序
 " as timeLimit," + //承诺时限
 " as chargeAccording," + //收费依据
 " as chargeStandard," + //收费标准
 " as legalRight," + //法律权利,申请人法律权利及申诉途径
 " as address," + //办理地点
 " as traffic," + //交通线路
 " as transactor," + //经办人
 " as transactorTel," + //经办人联系电话
 " as unitName" + //办理机构名称
 " from [原来的办理事项表]"
 * @author linchuan
 *
 */
public abstract class OnlineServiceImporter extends DirectoryDataImporter {
	
	/**
	 * 生成获取办理材料列表的SQL
	 * 格式如下:
	 "select " +
	 " as name," + //申报材料名称
	 " as description," + //申报说明
	 " as tableName," + //表格名称
	 " as tableURL," + //表格URL
	 " as exampleURL" + //样表URL
	 " from [原来的申报材料表]"
	 * @param importItemId
	 * @return
	 */
	public abstract String generateListServiceItemMaterialsSQL(String importItemId);
	
	/**
	 * 生成获取表格下载列表的SQL,如果申报材料已经包含表格下载,则不需要实现
	 * 格式如下:
	 "select " +
     " as tableName," + //表格名称
     " as tableURL," + //表格URL
     " as exampleURL" + //样表URL
     " from [原来的表格下载表]";
	 * @param importItemId
	 * @return
	 */
	public abstract String generateListServiceItemDownloadsSQL(String importItemId);

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getImportDataName()
	 */
	public String getImportDataName() {
		return "网上办事";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getDirectoryServiceName()
	 */
	public String getDirectoryServiceName() {
		return "onlineServiceDirectoryService";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getDirectoryTableMappings()
	 */
	public DirectoryTableMapping[] getDirectoryTableMappings() {
		return new DirectoryTableMapping[] {
			new DirectoryTableMapping("directory", "onlineservice_directory", null, OnlineServiceDirectory.class.getName()),
			new DirectoryTableMapping("mainDirectory", "onlineservice_directory", "onlineservice_main_directory", OnlineServiceMainDirectory.class.getName())
		};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getSameNameDirectory(java.lang.String, long)
	 */
	protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)Environment.getService("onlineServiceDirectoryService");
		//获取主目录
		OnlineServiceDirectory directory = onlineServiceDirectoryService.getDirectoryBySiteId(targetSiteId);
		String hql = "select OnlineServiceDirectory.id" +
					 " from OnlineServiceDirectory OnlineServiceDirectory, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
					 " where OnlineServiceDirectory.directoryName='" + JdbcUtils.resetQuot(directoryName) + "'" +
					 " and OnlineServiceDirectory.id=OnlineServiceDirectorySubjection.directoryId" +
					 (directory==null ? "" : " and OnlineServiceDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(onlineServiceDirectoryService.getChildDirectoryIds(directory.getId() + "", "directory")) + ")");
		List sameNameDirectoryIds = databaseService.findRecordsByHql(hql, 0, 2);
		if(sameNameDirectoryIds!=null && sameNameDirectoryIds.size()==1) {
			long directoryId = ((Long)sameNameDirectoryIds.get(0)).longValue();
			return new DirectoryMapping("" + directoryId, onlineServiceDirectoryService.getDirectoryFullName(directoryId, "/", "mainDirectory"));
		}
		return null;
	}

	/**
	 * 获取办理事项的申报材料(OnlineServiceItemMaterial)列表
	 * @param importItemId
	 * @return
	 * @throws Exception
	 */
	public List listServiceItemMaterials(String importItemId, Connection connection) throws Exception {
		String sql = generateListServiceItemMaterialsSQL(importItemId);
		if(sql==null || sql.isEmpty()) {
			return null;
		}
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		try {
			List materials = new ArrayList();
			while(rs.next()) {
				OnlineServiceItemMaterial material = new OnlineServiceItemMaterial();
				JdbcUtils.copyFields(material, rs);
				materials.add(material);
			}
			return materials;
		}
		finally {
			rs.close();
			statement.close();
		}
	}
	
	/**
	 * 获取办理事项的表格下载(OnlineServiceItemMaterial)列表
	 * @param localItemId
	 * @param importItemId
	 * @return
	 * @throws Exception
	 */
	public List listServiceItemDownloads(long localItemId, String importItemId, Connection connection) throws Exception {
		String sql = generateListServiceItemDownloadsSQL(importItemId);
		if(sql==null || sql.isEmpty()) {
			return null;
		}
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		try {
			List downloads = new ArrayList();
			while(rs.next()) {
				OnlineServiceItemMaterial material = new OnlineServiceItemMaterial();
				JdbcUtils.copyFields(material, rs);
				material.setName(" ");
				downloads.add(material);
			}
			return downloads;
		}
		finally {
			rs.close();
			statement.close();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#saveImportData(com.yuanluesoft.jeaf.dataimport.dataimporter.callback.DirectoryDataImporterCallback, java.sql.ResultSet, java.lang.String)
	 */
	protected long saveImportData(ResultSet resultSet, String mappingDirectoryIds, WebSite targetSite, Connection connection, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)Environment.getService("onlineServiceItemService");
		
		if(sameSystem) { //相同系统
			long itemId = resultSet.getLong("importDataId");
			if(databaseService.findRecordById(OnlineServiceItem.class.getName(), itemId, null)!=null) {
				return itemId;
			}
			OnlineServiceItem item = new OnlineServiceItem();
			String sql = "select * from onlineservice_item where id=" + itemId;
			Logger.debug(sql);
			Statement statement = connection.createStatement();
			ResultSet resultSetItem = statement.executeQuery(sql);
			resultSetItem.next();
			JdbcUtils.copyFields(item, resultSetItem);
			resultSetItem.close();
			afterPojoGenerated(item, resultSet, connection); //回调
			onlineServiceItemService.save(item); //保存
			
			//保存办事指南
			sql = "select * from onlineservice_item_guide where itemId=" + itemId;
			Logger.debug(sql);
			resultSetItem = statement.executeQuery(sql);
			if(resultSetItem.next()) {
				OnlineServiceItemGuide guide = new OnlineServiceItemGuide();
				JdbcUtils.copyFields(guide, resultSetItem);
				onlineServiceItemService.save(guide); //保存
			}
			resultSetItem.close();
			
			//保存常见问题解答
			sql = "select * from onlineservice_item_faq where itemId=" + itemId;
			Logger.debug(sql);
			resultSetItem = statement.executeQuery(sql);
			if(resultSetItem.next()) {
				OnlineServiceItemFaq faq = new OnlineServiceItemFaq();
				JdbcUtils.copyFields(faq, resultSetItem);
				onlineServiceItemService.save(faq); //保存
			}
			resultSetItem.close();
			
			//保存材料列表
			sql = "select * from onlineservice_item_material where itemId=" + itemId;
			Logger.debug(sql);
			resultSetItem = statement.executeQuery(sql);
			while(resultSetItem.next()) {
				OnlineServiceItemMaterial material = new OnlineServiceItemMaterial();
				JdbcUtils.copyFields(material, resultSetItem);
				if(material.getTableURL()!=null && !material.getTableURL().isEmpty()) {
					material.setTableURL(downloadAttachment(parameter.getSourceSiteURL() + material.getTableURL().substring(1), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", item.getId())); //下载表格
				}
				if(material.getExampleURL()!=null && !material.getExampleURL().isEmpty()) {
					material.setExampleURL(downloadAttachment(parameter.getSourceSiteURL() + material.getExampleURL().substring(1), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", item.getId())); //下载样表
				}
				onlineServiceItemService.save(material); //保存
			}
			resultSetItem.close();
			
			//保存办理机构列表
			sql = "select * from onlineservice_item_unit where itemId=" + itemId;
			Logger.debug(sql);
			resultSetItem = statement.executeQuery(sql);
			while(resultSetItem.next()) {
				OnlineServiceItemUnit unit = new OnlineServiceItemUnit();
				JdbcUtils.copyFields(unit, resultSetItem);
				onlineServiceItemService.save(unit); //保存
			}
			resultSetItem.close();
			
			//设置所在目录
			sql = "select directoryId from onlineservice_item_subjection where itemId=" + itemId;
			Logger.debug(sql);
			resultSetItem = statement.executeQuery(sql);
			String directoryIds = mappingDirectoryIds;
			while(resultSetItem.next()) {
				directoryIds = (directoryIds==null ? "" : directoryIds + ",") + resultSetItem.getString("directoryId");
			}
			resultSetItem.close();
			String hql = "select OnlineServiceDirectory.id from OnlineServiceDirectory OnlineServiceDirectory where OnlineServiceDirectory.id in (" + JdbcUtils.validateInClauseNumbers(directoryIds) + ")";
			List ids = databaseService.findRecordsByHql(hql);
			onlineServiceItemService.updateServiceItemSubjectios(item, true, ListUtils.join(ids, ",", false));
			statement.close();
			return item.getId();
		}
		
		OnlineServiceItem item = new OnlineServiceItem();
		JdbcUtils.copyFields(item, resultSet);
		afterPojoGenerated(item, resultSet, connection); //回调

		OnlineServiceItem oldItem;
		if(importedRecordId>0 && (oldItem = (OnlineServiceItem)databaseService.findRecordById(OnlineServiceItem.class.getName(), importedRecordId, ListUtils.generateList("subjections")))!=null) { //检查是否原来导入的记录是否存在
			onlineServiceItemService.updateServiceItemSubjectios(oldItem, false, mappingDirectoryIds); //更新隶属目录
			return importedRecordId;
		}
		
		//新的办理事项
		item.setId(UUIDLongGenerator.generateId()); //ID
		item.setIsPublic('1'); //是否公开
		//item.setPriority(priority); //优先级
		//item.setItemType(itemType); //审批服务类型
		item.setAcceptSupport(item.getAcceptUrl()==null || item.getAcceptUrl().isEmpty() ? '0' : '1'); //是否支持在线受理,网上预审，同时提供查询功能
		item.setAcceptWorkflowName(null); //在线受理流程
		item.setAcceptWorkflowId(null); //在线受理流程ID
		item.setComplaintSupport('1'); //是否支持在线投诉,同时提供查询功能
		item.setComplaintWorkflowName(null); //在线投诉流程
		item.setComplaintWorkflowId(null); //在线投诉流程ID
		item.setConsultSupport('1'); //是否支持在线咨询,同时提供查询功能
		item.setConsultWorkflowName(null); //在线咨询流程
		item.setConsultWorkflowId(null); //在线咨询流程ID
		//item.setAcceptUrl(acceptUrl); //在线受理链接,链接到行政服务中心
		item.setComplaintUrl(null); //在线投诉链接,链接到行政服务中心
		item.setConsultUrl(null); //在线咨询链接,链接到行政服务中心
		//item.setCreator(creator); //创建人
		//item.setCreatorId(creatorId); //创建人ID
		item.setCreated(item.getCreated()==null ? DateTimeUtils.now() : item.getCreated()); //创建时间
		onlineServiceItemService.save(item); //保存
		
		//办理单位
		OnlineServiceItemUnit unit = new OnlineServiceItemUnit();
		JdbcUtils.copyFields(unit, resultSet);
		if(unit.getUnitName()!=null || !unit.getUnitName().isEmpty()) {
			unit.setId(UUIDLongGenerator.generateId()); //ID
			unit.setItemId(item.getId());
			onlineServiceItemService.save(unit); //保存
		}
		
		//常见问题解答
		/*OnlineServiceItemFaq faq = new OnlineServiceItemFaq();
		SqlUtils.copyFields(faq, resultSet);
		if(faq.getFaq()!=null || !faq.getFaq().isEmpty()) {
			faq.setId(UUIDLongGenerator.generateId()); //ID
			faq.setItemId(item.getId());
			onlineServiceItemService.save(faq); //保存
		}*/
		
		//保存办事指南
		OnlineServiceItemGuide guide = new OnlineServiceItemGuide();
		JdbcUtils.copyFields(guide, resultSet);
		guide.setId(UUIDLongGenerator.generateId()); //ID
		guide.setItemId(item.getId());
		onlineServiceItemService.save(guide); //保存
	
		//设置所在目录
		onlineServiceItemService.updateServiceItemSubjectios(item, true, mappingDirectoryIds);
		
		//保存申报材料
		List materials = listServiceItemMaterials(sourceDataId, connection);
		if(materials!=null && !materials.isEmpty()) {
			for(Iterator iterator = materials.iterator(); iterator.hasNext();) {
				OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)iterator.next();
				material.setTableURL(downloadAttachment(material.getTableURL(), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", item.getId())); //下载表格
				material.setExampleURL(downloadAttachment(material.getExampleURL(), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", item.getId())); //下载样表
				material.setId(UUIDLongGenerator.generateId()); //ID
				material.setItemId(item.getId());
				onlineServiceItemService.save(material); //保存
			}
		}
		
		//保存表格下载
		List downloads = listServiceItemDownloads(item.getId(), sourceDataId, connection);
		if(downloads!=null && !downloads.isEmpty()) {
			for(Iterator iterator = downloads.iterator(); iterator.hasNext();) {
				OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)iterator.next();
				material.setTableURL(downloadAttachment(material.getTableURL(), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", item.getId())); //下载表格
				material.setExampleURL(downloadAttachment(material.getExampleURL(), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", item.getId())); //下载样表
				material.setId(UUIDLongGenerator.generateId()); //ID
				material.setItemId(item.getId());
				onlineServiceItemService.save(material); //保存
			}
		}
		return item.getId();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
	 */
	public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
		super.afterPojoGenerated(bean, resultSet, connection);
		if(bean instanceof OnlineServiceItem) {
			OnlineServiceItem item = (OnlineServiceItem)bean;
			item.getServiceItemGuide().setCondition(resultSet.getString("serviceCondition")); //申办条件
			item.getServiceItemGuide().setAccording(resultSet.getString("serviceAccording")); //办理依据
			item.getServiceItemGuide().setProgram(resultSet.getString("serviceProgram")); //办理程序
		}
	}
}