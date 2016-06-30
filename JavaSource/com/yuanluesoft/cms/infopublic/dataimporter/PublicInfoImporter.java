package com.yuanluesoft.cms.infopublic.dataimporter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.cms.infopublic.pojo.PublicArticleDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectoryCategory;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoPrivilege;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoSequence;
import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryTableMapping;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 信息公开导入,SQL格式如下：
 * 
 "select " +
 " as importDataId," + //被导入的记录ID
 " as directoryId," + //原来的目录ID
 " as infoIndex," + //索引号
 " as infoFrom," + //发布机构
 " as mark," + //文号
 " as generateDate," + //生成日期
 " as subject," + //标题
 " as body," + //正文
 " as creator," + //创建人
 " as created," + //创建时间
 " as issueTime," + //发布时间
 " as summarize," + //内容概述
 " as category," + //主题分类
 " as keywords," + //主题词
 " as orgName," + //创建者所在部门名称
 " as unitName" + //创建者所在单位名称
 " as type" + //类型,0/信息,1/年度报告、制度等
 " from [原来的政府信息表]";
 * @author linchuan
 *
 */
public abstract class PublicInfoImporter extends DirectoryDataImporter {
	private Map sequenceMap; //索引号流水号MAP
	protected boolean updateOldInfo = false; //是否更新旧信息

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getImportDataName()
	 */
	public String getImportDataName() {
		return "信息公开";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getDirectoryServiceName()
	 */
	public String getDirectoryServiceName() {
		return "publicDirectoryService";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getDirectoryTableMappings()
	 */
	public DirectoryTableMapping[] getDirectoryTableMappings() {
		return new DirectoryTableMapping[] {
				new DirectoryTableMapping("info", "public_directory", "public_info_directory", PublicInfoDirectory.class.getName()),
				new DirectoryTableMapping("main", "public_directory", "public_main_directory", PublicMainDirectory.class.getName()),
				new DirectoryTableMapping("category", "public_directory", "public_directory_category", PublicDirectoryCategory.class.getName()),
				new DirectoryTableMapping("article", "public_directory", "public_article_directory", PublicArticleDirectory.class.getName())
			};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getSameNameDirectory(java.lang.String, long)
	 */
	protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)Environment.getService("publicDirectoryService");
		//获取站点对应的主目录
		PublicDirectory mainDirectory = publicDirectoryService.getMainDirectoryBySite(targetSiteId);
		String hql = "select PublicDirectory.id" +
					 " from PublicDirectory PublicDirectory, PublicDirectorySubjection PublicDirectorySubjection" +
					 " where PublicDirectory.directoryName='" + JdbcUtils.resetQuot(directoryName) + "'" +
					 " and PublicDirectory.id=PublicDirectorySubjection.directoryId" +
					 " and PublicDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(publicDirectoryService.getChildDirectoryIds(mainDirectory==null ? "0" : "" + mainDirectory.getId(), "category,info")) + ")";
		List sameNameDirectoryIds = databaseService.findRecordsByHql(hql, 0, 2);
		if(sameNameDirectoryIds!=null && sameNameDirectoryIds.size()==1) {
			long directoryId = ((Long)sameNameDirectoryIds.get(0)).longValue();
			return new DirectoryMapping("" + directoryId, publicDirectoryService.getDirectoryFullName(directoryId, "/", "main"));
		}
		return null;
	}
	
	/**
	 * 生成获取信息公开目录编号的SQL
	 * select [原来的编号] as code from [表名称] where [原来的目录ID]='" + sourceDirectoryId + "'"
	 * @param sourceDirectoryId
	 * @return
	 * @throws Exception
	 */
	public String generateDirectoryCodeSql(String sourceDirectoryId) throws Exception {
		return null;
	}
	
	/**
	 * 获取信息公开目录编码
	 * @param sourceDirectoryId
	 * @param sourceDirectoryName
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	protected String getDirectoryCode(String sourceDirectoryId, String sourceDirectoryName, Connection connection) throws Exception {
		String code = null;
		String sql = generateDirectoryCodeSql(sourceDirectoryId);
		if(sql!=null) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if(resultSet.next()) {
				code = resultSet.getString("code");
			}
			resultSet.close();
			statement.close();
		}
		return code;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#afterChildDirectoryCreated(com.yuanluesoft.jeaf.directorymanage.service.DirectoryService, com.yuanluesoft.jeaf.directorymanage.pojo.Directory, java.lang.String, java.lang.String, boolean, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
	 */
	protected void afterChildDirectoryCreated(DirectoryService directoryService, Directory directory, String sourceDirectoryId, String sourceDirectoryName, boolean sameSystem, Connection connection, DataImportParameter parameter) throws Exception {
		super.afterChildDirectoryCreated(directoryService, directory, sourceDirectoryId, sourceDirectoryName, sameSystem, connection, parameter);
		PublicDirectory publicDirectory = (PublicDirectory)directory;
		if(publicDirectory.getCode()==null || publicDirectory.getCode().isEmpty()) {
			try {
				String code = getDirectoryCode(sourceDirectoryId, sourceDirectoryName, connection);
				if(code!=null) {
					publicDirectory.setCode(code);
					directoryService.update(publicDirectory);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#saveImportData(com.yuanluesoft.jeaf.dataimport.dataimporter.callback.DirectoryDataImporterCallback, java.sql.ResultSet, java.lang.String)
	 */
	protected long saveImportData(ResultSet resultSet, String mappingDirectoryIds, WebSite targetSite, Connection connection, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		PublicInfoService publicInfoService = (PublicInfoService)Environment.getService("publicInfoService");
	
		PublicInfo info = new PublicInfo();
		JdbcUtils.copyFields(info, resultSet);
		info.setBody(updateAttachmentPath(info.getBody(), resultSet, connection, parameter)); //更新正文中的附件URL,以下载到本服务器
		info.setSubject(StringUtils.filterHtmlElement(info.getSubject(), false));
		afterPojoGenerated(info, resultSet, connection); //回调
		Logger.info("PublicInfoImporter: import info " + info.getSubject() + ".");

		PublicInfo oldInfo;
		if(importedRecordId>0 && (oldInfo = (PublicInfo)databaseService.findRecordById(PublicInfo.class.getName(), importedRecordId, ListUtils.generateList("lazyBody,subjections", ",")))!=null) { //检查是否原来导入的记录是否存在
			if(updateOldInfo) {
				info.setId(oldInfo.getId());
				if(info.getSummarize()==null || info.getSummarize().equals("")) {
					info.setSummarize(oldInfo.getSummarize());
				}
				if(info.getMark()==null || info.getMark().equals("")) {
					info.setMark(oldInfo.getMark());
				}
				info.setStatus(oldInfo.getStatus());
				info.setIssueSite(oldInfo.getIssueSite());
				info.setCreatorId(oldInfo.getCreatorId());; //必须设置,否则同步到网站时异常
				info.setIssuePersonId(oldInfo.getIssuePersonId()); //必须设置,否则打开时会提示没有权限
				info.setDirectoryName((String)databaseService.findRecordByHql("select PublicDirectory.directoryName from PublicDirectory PublicDirectory where PublicDirectory.id=" + mappingDirectoryIds.split(",")[0])); //所在目录名称
				if(info.getBody()==null || info.getBody().isEmpty()) {
					info.setBody(oldInfo.getBody());
				}
				publicInfoService.update(info); //更新
			}
			else if(oldInfo.getBody()==null || oldInfo.getBody().isEmpty()) { //仅更新正文为空的记录,因此,如果正文有问题,需要事先清空正文,SQL: update public_info set body=null where id in (select localRecordId from dataimport_record)
				oldInfo.setBody(info.getBody()); //更新正文
				publicInfoService.update(oldInfo); //更新
			}
			publicInfoService.updateInfoSubjections(oldInfo, false, mappingDirectoryIds);
			//更新流水号
			updateSequenceMap(info);
			return importedRecordId;
		}
		
		info.setId(UUIDLongGenerator.generateId()); //ID
		if(info.getSummarize()==null || info.getSummarize().equals("")) {
			info.setSummarize("　");
		}
		if(info.getMark()==null || info.getMark().equals("")) {
			info.setMark("　");
		}
		if(info.getStatus()==PublicInfoService.INFO_STATUS_TODO) {
			info.setStatus(PublicInfoService.INFO_STATUS_ISSUE);
		}
		info.setIssueSite('1');
		info.setCreatorId(100);; //必须设置,否则同步到网站时异常
		info.setIssuePersonId(100); //必须设置,否则打开时会提示没有权限
		if(info.getBody()==null || info.getBody().isEmpty()) {
			info.setBody("　");
		}
		publicInfoService.save(info); //保存
		
		//创建权限记录
		cloneSitePrivileges(PublicInfoPrivilege.class, info.getId(), targetSite, databaseService);
		
		//设置所在目录
		publicInfoService.updateInfoSubjections(info, true, mappingDirectoryIds);
		
		//更新流水号
		updateSequenceMap(info);
		return info.getId();
	}
	
	/**
	 * 更新流水号
	 * @param info
	 */
	private void updateSequenceMap(PublicInfo info) {
		try {
			String[] values = info.getInfoIndex().replaceAll("－", "-").split("-");
			String key = values[0].trim().toUpperCase() + "-" + Integer.parseInt(values[2]);
			Integer sequence = new Integer(values[3]);
			Integer saved = (Integer)sequenceMap.get(key);
			if(saved==null || saved.intValue()<sequence.intValue()) {
				sequenceMap.put(key, sequence);
			}
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#importData(long, java.lang.String, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter, boolean)
	 */
	public void importData(long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
		sequenceMap = new HashMap();
		super.importData(targetSiteId, dataImportServiceClass, connection, parameter, sameSystem);
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		//数据导入完成后,更新索引号
		for(Iterator iterator = sequenceMap.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			Integer sequence = (Integer)sequenceMap.get(key);
			Logger.info("Info index sequence of " + key + " is " + sequence + ".");
			String[] values = key.split("-");
			//获取现有的流水号记录
			String hql = "from PublicInfoSequence PublicInfoSequence" +
						 " where PublicInfoSequence.year=" + values[1] +
						 " and PublicInfoSequence.category='" + JdbcUtils.resetQuot(values[0]) + "'";
			PublicInfoSequence infoSequence = (PublicInfoSequence)databaseService.findRecordByHql(hql);
			if(infoSequence==null) {
				infoSequence = new PublicInfoSequence();
				infoSequence.setId(UUIDLongGenerator.generateId()); //ID
				infoSequence.setYear(Integer.parseInt(values[1])); //年度
				infoSequence.setSequence(sequence.intValue()); //序号
				infoSequence.setCategory(values[0]); //信息类目代码
				databaseService.saveRecord(infoSequence);
			}
			else if(infoSequence.getSequence()<sequence.intValue()) {
				infoSequence.setSequence(sequence.intValue());
				databaseService.updateRecord(infoSequence);
			}
		}
		sequenceMap.clear();
		sequenceMap = null;
	}
}