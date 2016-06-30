package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 漳州市政府新闻
 * @author linchuan
 *
 */
public class ArticleImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "漳州市政府新闻";
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
					return "select str(ID) as directoryId," +
						   " TypeName as directoryName" +
						   " from T_BaseType" +
						   " WHERE ParentID=0";
				}
				else { //子目录
					return "select ID as directoryId," +
						   " TypeName as directoryName" +
						   " from T_BaseType" +
						   " where ParentID=" + parentDirectoryId + " and ParentID<>0";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " ID as importDataId," + //被导入的记录ID
					   " TypeID as directoryId," + //原来的栏目ID
					   " Title as subject," + //标题
					   " Content as body," + //正文
					   " '' as subhead," + //副标题
					   " Source," + //来源
					   " Author," + //作者
					   " Tags as keyword," + //关键字
					   " AddDate as created," + //创建时间
					   " AddDate as issueTime," + //发布时间
					   " UserName as editor," + //创建者
					   " UserName as orgName," + //创建者所在部门名称
					   " UserName as unitName" + //创建者所在单位名称
					   " from T_Information" +
	   			   	   " where IsOK='1'" +
	   			   	   " and TypeID in (" + ListUtils.join(selectedSourceDirectoryIds, ",", false) + ")"};
			}
			
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				htmlBody = htmlBody.replaceAll("<META http-equiv=Content-Type content=\"text/html; charset=gb2312\">", "");
				return htmlBody.replaceAll("(?i)href=\"UploadFiles", "src=\"" + parameter.getSourceSiteURL() + "UploadFiles")
							   .replaceAll("(?i)href=\"images", "href=\"" + parameter.getSourceSiteURL() + "images")
							   .replaceAll("(?i)src=\"UploadFiles", "src=\"" + parameter.getSourceSiteURL() + "UploadFiles")
							   .replaceAll("(?i)src=\"images", "href=\"" + parameter.getSourceSiteURL() + "images");
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter#getSameNameDirectory(java.lang.String, long)
			 */
			protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
				return null;
			}
		});
		return dataImporters;
	}
}