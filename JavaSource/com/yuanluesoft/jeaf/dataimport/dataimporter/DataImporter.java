package com.yuanluesoft.jeaf.dataimport.dataimporter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.mysql.MysqlDatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.oracle.OracleDatabaseDialect;
import com.yuanluesoft.jeaf.database.dialect.postgres.PostgresDatabaseDialect;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.pojo.ImportedRecord;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.pojo.RecordPrivilege;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 数据导入器
 * @author linchuan
 *
 */
public abstract class DataImporter {
	protected DatabaseService databaseService = null; //数据库服务
	
	/**
	 * 获取导入的数据名称
	 * @return
	 */
	public abstract String getImportDataName();

	/**
	 * 生成获取数据的SQL
	 * @return
	 */
	public abstract String[] generateRetrieveDataSQL();
	
	/**
	 * 保存导入的数据,需要判断数据是否已经导入过,务必使用dataFieldReader读取数据
	 * @param targetSite
	 * @param connection
	 * @param dataImportServiceClass
	 * @param parameter
	 * @param sourceDataId
	 * @param importedRecordId 之前导入过的记录ID,-1表示无法确定是否导入过
	 * @param sameSystem
	 * @return
	 * @throws ServiceException
	 */
	protected abstract long saveImportedData(ResultSet resultSet, WebSite targetSite, Connection connection, String dataImportServiceClass, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception;
	
	/**
	 * 生成获取数据的SQL
	 * @param targetSiteId
	 * @param dataImportServiceClass
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @return
	 * @throws Exception
	 */
	protected String[] generateRetrieveDataSQL(long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		return generateRetrieveDataSQL();
	}
	
	/**
	 * 导入数据
	 * @param targetSiteId
	 * @param connection
	 * @param parameter
	 * @param sameSystem
	 * @throws Exception
	 */
	public void importData(long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		String[] retrieveDataSQL = generateRetrieveDataSQL(targetSiteId, dataImportServiceClass, connection, parameter, sameSystem);
		if(retrieveDataSQL==null) {
			return;
		}
		if(databaseService==null) {
			databaseService = (DatabaseService)Environment.getService("databaseService");
		}
		DatabaseDialect databaseDialect = JdbcUtils.getDatabaseDialect(parameter.getJdbcURL());
		boolean pagingSupport = (databaseDialect instanceof PostgresDatabaseDialect) ||
								(databaseDialect instanceof OracleDatabaseDialect) ||
								(databaseDialect instanceof MysqlDatabaseDialect);
		WebSite targetSite = (WebSite)((SiteService)Environment.getService("siteService")).getDirectory(targetSiteId);
		Statement statement = connection.createStatement();
		for(int k=0; k<retrieveDataSQL.length; k++) {
			Logger.info(retrieveDataSQL[k]);
			for(int offset = 0; ; offset+=200) {
				String sql = pagingSupport ? databaseDialect.generatePagingSql(retrieveDataSQL[k], offset, 200) : retrieveDataSQL[k];
				ResultSet resultSet = statement.executeQuery(sql);
				int count = 0;
				for(int i=0; resultSet.next() && i+offset<parameter.getMaxImport(); i++) {
					count++;
					try {
						String importDataId = resultSet.getString("importDataId");
						ImportedRecord importedRecord = null;
						if(importDataId!=null && !importDataId.isEmpty()) {
							String hql = "from ImportedRecord ImportedRecord" +
										 " where ImportedRecord.siteId=" + targetSiteId + //站点ID
										 " and ImportedRecord.importDataName='" + JdbcUtils.resetQuot(getImportDataName()) + "'" + //被导入数据名称,如：投诉、政府信息
										 " and ImportedRecord.remoteRecordId='" + JdbcUtils.resetQuot(importDataId) + "'"; //导入的记录ID
							importedRecord = (ImportedRecord)databaseService.findRecordByHql(hql);
						}
						long localRecordId = saveImportedData(resultSet, targetSite, connection, dataImportServiceClass, parameter, importDataId, importedRecord==null ? -1 : importedRecord.getLocalRecordId(), sameSystem);
						Logger.debug("DataImporter: import data that id is " + importDataId + ", save as a record that id is " + localRecordId + ".");
						if(localRecordId>0 && importDataId!=null && !importDataId.isEmpty()) {
							if(importedRecord==null) {
								importedRecord = new ImportedRecord();
								importedRecord.setId(UUIDLongGenerator.generateId()); //ID
								importedRecord.setImportDataName(getImportDataName()); //被导入数据名称,如：投诉、政府信息
								importedRecord.setSiteId(targetSiteId); //站点ID
								importedRecord.setRemoteRecordId(importDataId); //导入的记录ID
								importedRecord.setLocalRecordId(localRecordId); //本系统的记录ID
								databaseService.saveRecord(importedRecord);
							}
							else if(localRecordId!=importedRecord.getLocalRecordId()) { //记录ID改变了
								importedRecord.setLocalRecordId(localRecordId); //本系统的记录ID
								databaseService.updateRecord(importedRecord);
							}
						}
					}
					catch(Exception e) {
						Logger.exception(e);
					}
				}
				resultSet.close();
				if(!pagingSupport || count<200) {
					break;
				}
			}
		}
		statement.close();
	}
	
	/**
	 * POJO填充以后,继承者可以对bean做进一步处理
	 * @param bean
	 * @param resultSet
	 * @param connection
	 */
	public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
		if(bean instanceof Record) {
			((Record)bean).setExtendPropertyValue("importData", Boolean.TRUE); //标记为导入的数据
		}
	}
	
	/**
	 * 按站点的管理员、编辑创建权限记录
	 * @param privilegeClass
	 * @param mainRecordId
	 * @param siteId
	 * @param databaseService
	 */
	protected void cloneSitePrivileges(Class privilegeClass, long mainRecordId, WebSite site, DatabaseService databaseService) {
		//获取站点自己的管理员和编辑
		//List userIds = ListUtils.generatePropertyList(ListUtils.getSubListByProperty(site.getDirectoryPopedoms(), "isInherit", new Character('0')), "userId");
		List userIds = ListUtils.generatePropertyList(site.getDirectoryPopedoms(), "userId");
		//如果没有配置,允许所有人访问
		if(userIds==null || userIds.isEmpty()) { //没有设置
			userIds = new ArrayList();
			userIds.add(new Long(0)); //允许所有人查看
		}
		List addIds = new ArrayList();
		for(Iterator iterator = userIds.iterator(); iterator.hasNext();) {
			Number userId = (Number)iterator.next();
			if(addIds.indexOf(userId)!=-1) {
				continue;
			}
			addIds.add(userId);
			//创建权限记录
			RecordPrivilege privilege;
			try {
				privilege = (RecordPrivilege)privilegeClass.newInstance();
			}
			catch(Exception e) {
				Logger.exception(e);
				throw new Error(e.getMessage());
			}
			privilege.setId(UUIDLongGenerator.generateId());
			privilege.setRecordId(mainRecordId);
			privilege.setVisitorId(userId.longValue());
			privilege.setAccessLevel(RecordControlService.ACCESS_LEVEL_READONLY);
			try {
				databaseService.saveRecord(privilege);
			}
			catch(Exception e) {
				
			}
		}
	}

	/**
	 * 替换正文中的图片、附件等的路径
	 * @param htmlBody
	 * @param resultSet
	 * @param connection
	 * @param parameter
	 * @return
	 */
	public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
		if(htmlBody==null) {
			return null;
		}
		//替换<?xml.../>和图片路径
		return htmlBody.replaceAll("(?i)<\\x3Fxml[^>]+>", "").replaceAll("(?i)src=\"/", "src=\"" + parameter.getSourceSiteURL()).replaceAll("(?i)href=\"/", "href=\"" + parameter.getSourceSiteURL());
	}

	/**
	 * 下载附件,返回新的URL
	 * @param itemId
	 * @param url
	 * @param parameter
	 * @return
	 * @throws ServiceException
	 */
	protected String downloadAttachment(String url, DataImportParameter parameter, String attachmentServiceName, String applicationName, String attachmentType, long recordId) throws ServiceException {
		if(url==null ||
		   url.isEmpty() ||
		   url.indexOf("/pages/")!=-1 ||	
		   parameter.getSourceSiteURL()==null || parameter.getSourceSiteURL().isEmpty() || parameter.getSourceSiteURL().equals("/")) {
			return url;
		}
		if(url.startsWith("/")) {
			url = parameter.getSourceSiteURL() + url.substring(1);
		}
		AttachmentService siteAttachmentService = (AttachmentService)Environment.getService(attachmentServiceName);
		Logger.info("DataImporter: download file form " + url + ".");
		//下载文件
		try {
			String fileName = ((FileDownloadService)Environment.getService("fileDownloadService")).downloadFile(url, siteAttachmentService.getSavePath(applicationName, attachmentType, recordId, true));
			return siteAttachmentService.createDownload(applicationName, attachmentType, recordId, fileName.substring(fileName.lastIndexOf("/") + 1), true, null);
		}
		catch(Exception e) {
			
		}
		return url;
	}
}