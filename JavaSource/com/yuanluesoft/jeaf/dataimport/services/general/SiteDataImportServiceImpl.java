package com.yuanluesoft.jeaf.dataimport.services.general;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 站点数据导入
 * @author linchuan
 *
 */
public class SiteDataImportServiceImpl extends DataImportService {

	public SiteDataImportServiceImpl() {
		super();
		sameSystem = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "相同系统:站点数据导入";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//站点数据导入
		dataImporters.add(new SiteDataImporter() {
			
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select id as directoryId," +
						   " directoryName" +
						   " from cms_directory" +
						   " where id=0";
				}
				else { //子目录
					return "select id as directoryId," +
						   " directoryName" +
						   " from cms_directory" +
						   " where parentDirectoryId=" + parentDirectoryId +
						   " and id!=0";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " id as importDataId," + //被导入的记录ID
					   " (select min(siteId) from cms_resource_subjection where cms_resource_subjection.resourceId=cms_resource.id and siteId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(selectedSourceDirectoryIds, ",", false)) + ")) as directoryId," + //原来的栏目ID
					   " subject," + //标题
					   " (select body from cms_resource_body where cms_resource_body.id=cms_resource.id) body," + //正文
					   " subhead," + //副标题
					   " source," + //来源
					   " author," + //作者
					   " keyword," + //关键字
					   " created," + //创建时间
					   " issueTime," + //发布时间
					   " editor," + //创建者
					   " orgName," + //创建者所在部门名称
					   " unitName" + //创建者所在单位名称
					   " from cms_resource" +
	   			   	   " where status='" + SiteResourceService.RESOURCE_STATUS_ISSUE + "'" +
	   			   	   " and id in (" +
	   			   	   "  select cms_resource_subjection.resourceId" +
	   			   	   "   from cms_resource_subjection, cms_directory_subjection" +
	   			   	   "   where cms_resource_subjection.siteId=cms_directory_subjection.directoryId" +
	   			   	   "   and parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(selectedSourceDirectoryIds, ",", false)) + ")" +
	   			   	   " )" +
	   			   	   " order by id"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String, java.sql.ResultSet, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
			 */
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return null;
				}
				return super.updateAttachmentPath(htmlBody, resultSet, connection, parameter).replaceAll("(?i)file=/", "file=" + parameter.getSourceSiteURL());
			}
		});
		return dataImporters;
	}
}