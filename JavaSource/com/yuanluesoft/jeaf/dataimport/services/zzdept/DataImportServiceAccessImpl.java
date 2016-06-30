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
 * 漳州部门网站(使用Access的网站)
 * @author linchuan
 *
 */
public class DataImportServiceAccessImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "漳州部门网站(使用Access的网站)";
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
					return "select ClassID as directoryId," +
						   " ClassName as directoryName" +
						   " from ArticleClass" +
						   " where ParentId=0" +
						   " order by ClassName";
				}
				else { //子目录
					return "select ClassId as directoryId," +
						   " ClassName as directoryName" +
						   " from ArticleClass" +
						   " where ParentId=" + parentDirectoryId +
						   " order by ClassName";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "ClassId as directoryId," + //原来的栏目ID
					   "title as subject," + //标题
					   "Content as body," + //正文
					   "'' as subhead," + //副标题
					   "CopyFrom as source," + //来源
					   "Author," + //作者
					   "key as keyword," + //关键字
					   "UpdateTime as created," + //创建时间
					   "UpdateTime as issueTime," + //发布时间
					   "Editor," + //创建者
					   "'' as orgName," + //创建者所在部门名称
					   "'' as unitName" + //创建者所在单位名称
					   " from Article"};
			}
			
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				return htmlBody.replaceAll("src=\"UploadFiles", "src=\"" + parameter.getSourceSiteURL() + "UploadFiles").replaceAll("href=\"UploadFiles", "href=\"" + parameter.getSourceSiteURL() + "UploadFiles").replaceAll("src=\"images", "src=\"" + parameter.getSourceSiteURL() + "images").replaceAll("href=\"images", "href=\"" + parameter.getSourceSiteURL() + "images");
			}
		});
		
		//信息公开数据导入
		dataImporters.add(
			new PublicInfoImporter() {
				public String generateListChildDirectoriesSQL(String parentDirectoryId) {
					if(parentDirectoryId==null) { //第一级目录
						return "select ClassID as directoryId," +
							   " ClassName as directoryName" +
							   " from ArticleClass" +
							   " where ParentId=0" +
							   " order by ClassName";
					}
					else { //子目录
						return "select ClassId as directoryId," +
							   " ClassName as directoryName" +
							   " from ArticleClass" +
							   " where ParentId=" + parentDirectoryId +
							   " order by ClassName";
					}
				}
				
				public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
					return new String[] {"select " +
							   "id as importDataId," + //被导入的记录ID
							   "(select ArticleClass.ClassID from ArticleClass where ArticleClass.OrderId=val(mid(Article.indexcode, 9, 2))) as directoryId," + //原来的栏目ID
							   "indexcode as infoIndex," + //索引号
							   "Editor as infoFrom," + //发布机构
							   "remark as mark," + //文号
							   "CreateTime as generateDate," + //生成日期
							   "title as subject," + //标题
							   "Content as body," + //正文
							   "Editor as creator," + //创建人
							   "UpdateTime as created," + //创建时间
							   "UpdateTime as issueTime," + //发布时间
							   "'' as category," + //主题分类
							   "key as keywords," + //主题词
							   "Editor as orgName," + //创建者所在部门名称
							   "Editor as unitName" + //创建者所在单位名称
							   " from Article" +
							   " where indexcode<>''"};
				}

				public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
					if(htmlBody==null) {
						return htmlBody;
					}
					htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
					return htmlBody.replaceAll("src=\"UploadFiles", "src=\"" + parameter.getSourceSiteURL() + "UploadFiles").replaceAll("href=\"UploadFiles", "href=\"" + parameter.getSourceSiteURL() + "UploadFiles").replaceAll("src=\"images", "src=\"" + parameter.getSourceSiteURL() + "images").replaceAll("href=\"images", "href=\"" + parameter.getSourceSiteURL() + "images");
				
				}
			}
		);
		return dataImporters;
	}
}