package com.yuanluesoft.jeaf.dataimport.services.zzdept;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 漳州审计局数据导入
 * @author linchuan
 *
 */
public class DataImportServiceAuditImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "漳州审计局数据导入";
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
					return "select ClassId as directoryId," +
						   " ClassName as directoryName" +
						   " from Cl_Class" +
						   " where ParentID=0";
				}
				else { //子目录
					return "select ClassId as directoryId," +
						   " ClassName as directoryName" +
						   " from Cl_Class" +
						   " where ParentID=" + parentDirectoryId;
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "ClassID as directoryId," + //原来的栏目ID
					   "Title as subject," + //标题
					   "Content as body," + //正文
					   "'' as subhead," + //副标题
					   "'' as source," + //来源
					   "author," + //作者
					   "keyword," + //关键字
					   "UpdateTime as created," + //创建时间
					   "UpdateTime as issueTime," + //发布时间
					   "Editor," + //创建者
					   "'' as orgName," + //创建者所在部门名称
					   "'' as unitName" + //创建者所在单位名称
					   " from Cl_Article"};
			}
			
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				return htmlBody;
			}
		});
		
		//信息公开数据导入
		dataImporters.add(
			new PublicInfoImporter() {
				public String generateListChildDirectoriesSQL(String parentDirectoryId) {
					if(parentDirectoryId==null) { //第一级目录
						return "select ClassId as directoryId," +
							   " ClassName as directoryName" +
							   " from Cl_Class" +
							   " where ParentID=0";
					}
					else { //子目录
						return "select ClassId as directoryId," +
							   " ClassName as directoryName" +
							   " from Cl_Class" +
							   " where ParentID=" + parentDirectoryId;
					}
				}
				
				public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
					return new String[] {"select " +
							   "id as importDataId," + //被导入的记录ID
							   "ClassID as directoryId," + //原来的栏目ID
							   "Cl_syh as infoIndex," + //索引号
							   "Cl_fbjg as infoFrom," + //发布机构
							   "Cl_wh as mark," + //文号
							   "UpdateTime as generateDate," + //生成日期
							   "title as subject," + //标题
							   "Content as body," + //正文
							   "Editor as creator," + //创建人
							   "UpdateTime as created," + //创建时间
							   "UpdateTime as issueTime," + //发布时间
							   "Intro as summarize," + //内容概述
							   "'' as category," + //主题分类
							   "keyword as keywords," + //主题词
							   "'' as orgName," + //创建者所在部门名称
							   "'' as unitName" + //创建者所在单位名称
							   " from Cl_Article"};
				}

				public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
					if(htmlBody==null) {
						return htmlBody;
					}
					return super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
					//return htmlBody.replaceAll("src=\"UploadFiles", "src=\"" + sourceSiteURL + "UploadFiles").replaceAll("href=\"UploadFiles", "href=\"" + sourceSiteURL + "UploadFiles").replaceAll("src=\"images", "src=\"" + sourceSiteURL + "images").replaceAll("href=\"images", "href=\"" + sourceSiteURL + "images");
				}
			}
		);
		return dataImporters;
	}
}