package com.yuanluesoft.jeaf.dataimport.services.general;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 使用TRS的网站
 * @author linchuan
 *
 */
public class DataImportServiceTrsImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "TRS";
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
					return "select 'site_' & SITEID as directoryId," +
						   " SITENAME as directoryName" +
						   " from WCMWEBSITE";
				}
				else if(parentDirectoryId.startsWith("site_")) { //第一级目录
					return "select 'column_' & CHANNELID as directoryId," +
						   " CHNLNAME as directoryName" +
						   " from WCMCHANNEL" +
						   " where SITEID=" + parentDirectoryId.substring("site_".length()) +
						   " and PARENTID=0";
				}
				else if(parentDirectoryId.startsWith("column_")) { //其他目录
					return "select 'column_' & CHANNELID as directoryId," +
						   " CHNLNAME as directoryName" +
						   " from WCMCHANNEL" +
						   " where PARENTID=" + parentDirectoryId.substring("column_".length());
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   "id as importDataId," + //被导入的记录ID
					   "'column_' & DOCCHANNEL as directoryId," + //原来的栏目ID
					   "DOCTITLE as subject," + //标题
					   "DOCPUBHTMLCON as body," + //正文
					   "'' as subhead," + //副标题
					   "'' as source," + //来源
					   "'' as author," + //作者
					   "'' as keyword," + //关键字
					   "CRTIME as created," + //创建时间
					   "DOCPUBTIME as issueTime," + //发布时间
					   "'　' as Editor," + //创建者
					   "'' as orgName," + //创建者所在部门名称
					   "'' as unitName," + //创建者所在单位名称
					   "DOCPUBURL" +
					   " from WCMDOCUMENT"};
			}

			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				String pubUrl;
				try {
					pubUrl = resultSet.getString("DOCPUBURL");
					pubUrl = pubUrl.substring(0, pubUrl.lastIndexOf('/') + 1);
					htmlBody = htmlBody.replaceAll("src=\"W", "src=\"" + pubUrl + "W").replaceAll("src=\"P", "src=\"" + pubUrl + "P");
					htmlBody = htmlBody.replaceAll("src='W", "src='" + pubUrl + "W").replaceAll("src='P", "src='" + pubUrl + "P");
					htmlBody = htmlBody.replaceAll("href=\"W", "href=\"" + pubUrl + "W").replaceAll("href=\"P", "href=\"" + pubUrl + "P");
					htmlBody = htmlBody.replaceAll("href='W", "href='" + pubUrl + "W").replaceAll("href='P", "href='" + pubUrl + "P");
				} 
				catch (Exception e) {
				}
				return htmlBody;
			}
		});
		return dataImporters;
	}
}