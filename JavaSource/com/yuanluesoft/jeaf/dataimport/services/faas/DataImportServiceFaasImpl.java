package com.yuanluesoft.jeaf.dataimport.services.faas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 农科院,福建省农业科技信息网(http://www.fjny.org/)
 * @author linchuan
 *
 */
public class DataImportServiceFaasImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "农科院,福建省农业科技信息网";
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
						   " name as directoryName" +
						   " from cms_ar_category" +
						   " where parentId=0" +
						   " order by name";
				}
				else { //子目录
					return "select id as directoryId," +
						   " name as directoryName" +
						   " from cms_ar_category" +
						   " where parentId=" + parentDirectoryId +
						   " order by name";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "cid as directoryId," + //原来的栏目ID
					   "title as subject," + //标题
					   "content as body," + //正文
					   "'' as subhead," + //副标题
					   "fromSite as source," + //来源
					   "'' as author," + //作者
					   "keyword," + //关键字
					   "CONVERT(newstime, DATETIME) as created," + //创建时间
					   "CONVERT(newstime, DATETIME) as issueTime," + //发布时间
					   "editor," + //创建者
					   "'' as orgName," + //创建者所在部门名称
					   "'' as unitName" + //创建者所在单位名称
					   " from cms_ar_article"};
			}
			
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				return htmlBody.replaceAll("src=\"../../../", "src=\"" + parameter.getSourceSiteURL()).replaceAll("href=\"../../../", "href=\"" + parameter.getSourceSiteURL());
			}
		});
		return dataImporters;
	}
}