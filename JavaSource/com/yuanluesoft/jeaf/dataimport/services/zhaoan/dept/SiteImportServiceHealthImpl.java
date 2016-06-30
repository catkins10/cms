package com.yuanluesoft.jeaf.dataimport.services.zhaoan.dept;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 
 * @author linchuan
 *
 */
public class SiteImportServiceHealthImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "诏安卫生局局站点导入";
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
					return "select typeid as directoryId," +
						   " typename as directoryName" +
						   " from type";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						"select " +
						 "NewsID as importDataId," + //被导入的记录ID
						 "typeid as directoryId," + //原来的栏目ID
						 "Title as subject," + //标题
						 "Content as body," + //正文
						 "null as subhead," + //副标题
						 "null as source," + //来源
						 "Author," + //作者
						 "null as keyword," + //关键字
						 "UpdateTime as created," + //创建时间
						 "UpdateTime as issueTime," + //发布时间
						 "editor," + //创建者
						 "'' as orgName," + //创建者所在部门名称
						 "'' as unitName" + //创建者所在单位名称
						 " from News"
				};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String, java.sql.ResultSet, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
			 */
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				return htmlBody.replaceAll("(?i)href=\"uploadfile", "src=\"" + parameter.getSourceSiteURL() + "uploadfile")
							   .replaceAll("(?i)src=\"uploadfile", "src=\"" + parameter.getSourceSiteURL() + "uploadfile");
			}
		});
		return dataImporters;
	}
}