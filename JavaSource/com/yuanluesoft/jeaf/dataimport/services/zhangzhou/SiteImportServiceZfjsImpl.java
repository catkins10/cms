package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 漳州市作风建设网
 * @author linchuan
 *
 */
public class SiteImportServiceZfjsImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "漳州市作风建设网";
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
					return "select Id as directoryId," +
						   " ClassName as directoryName" +
						   " from Class";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " ID as importDataId," + //被导入的记录ID
					   " ClassID as directoryId," + //原来的栏目ID
					   " Title as subject," + //标题
					   " TitleColor as subjectColor," + //标题颜色
					   " Content as body," + //正文
					   " '' as subhead," + //副标题
					   " Original as Source," + //来源
					   " Author," + //作者
					   " '' as keyword," + //关键字
					   " AddTime as created," + //创建时间
					   " AddTime as issueTime," + //发布时间
					   " Inputer as editor," + //创建者
					   " Input_Depart as orgName," + //创建者所在部门名称
					   " Input_Depart as unitName" + //创建者所在单位名称
					   " from Article" +
					   " where IsCheck=1"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String, java.sql.ResultSet, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
			 */
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				try {
					HTMLParser htmlParser = (HTMLParser)Environment.getService("htmlParser");
					htmlBody = htmlParser.getTextContent(htmlParser.parseHTMLString(htmlBody, "utf-8")).replace((char)0xa0, ' ');
				}
				catch(Exception e) {
					throw new Error(e);
				}
				return super.updateAttachmentPath(htmlBody, resultSet, connection, parameter).replaceAll("\"upfiles/", "\"" + parameter.getSourceSiteURL() + "upfiles/");
			}
		});
		return dataImporters;
	}
}