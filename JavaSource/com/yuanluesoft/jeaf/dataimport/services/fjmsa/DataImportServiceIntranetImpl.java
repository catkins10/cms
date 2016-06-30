package com.yuanluesoft.jeaf.dataimport.services.fjmsa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.htmlparser.util.HTMLBodyUtils;

/**
 * 福建海事局内网
 * @author linchuan
 *
 */
public class DataImportServiceIntranetImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "福建海事局内网网站";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//站点数据导入
		//遇到文章的标题相同以及发布时间相同时,不更改所属栏目,添加文章
		dataImporters.add(new SiteDataImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select ID as directoryId," +
						   " NAME as directoryName" +
						   " from MESS_DIRECTORY" +
						   " WHERE PARENTID = '000025' AND (ISSTYPE = '2' OR ISSTYPE = '0' )";
				}
				else { //子目录
					return "select ID as directoryId," +
					   " NAME as directoryName" +
					   " from MESS_DIRECTORY" +
					   " where PARENTID = '" + parentDirectoryId + "' AND PARENTID <> '000025'";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " MESSAGE.ID as importDataId," + //被导入的记录ID
					   " MESSAGE.ID as messageid," + 
					   " DIRECTORY.TYPE as directoryType," +
					   " MESSAGE.RECOMMENDATORYIMG as imgURI," +
					   " MESSAGE.DIRECTORYID as directoryId," + //原来的栏目ID
					   " MESSAGE.TITLE as subject," + //标题
					   " MESSAGE.CONTENT as body," + //正文
					   " MESSAGE.SUBTITLE as subhead," + //副标题
					   " MESSAGE.SOURCE as source," + //来源
					   " RUSER.NAME as Author," + //作者
					   " ' ' as keyword," + //关键字
					   " MESSAGE.ISSDATE as created," + //创建时间
					   " MESSAGE.ISSDATE as issueTime," + //发布时间
					   " RUSER.NAME as Editor," + //创建者
					   " RGROUP.NAME as orgName," + //创建者所在部门名称
					   " RGROUP.NAME as unitName" + //创建者所在单位名称
					   " from MESS_MESSAGE MESSAGE left join RBAC_USER RUSER on MESSAGE.ISSUER = RUSER.ID left join RBAC_GROUP RGROUP on  RUSER.GROUPID = RGROUP.ID left join MESS_DIRECTORY DIRECTORY  on MESSAGE.DIRECTORYID = DIRECTORY.ID " +
	   			   	   //" WHERE datalength(MESSAGE.CONTENT)<300 AND MESSAGE.STATE = 'publish' AND (MESSAGE.ISSTYPE = '0' OR MESSAGE.ISSTYPE = '2')"};
					   " WHERE MESSAGE.STATE = 'publish' AND (MESSAGE.ISSTYPE = '0' OR MESSAGE.ISSTYPE = '2')"};
			}
			
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				if(!(bean instanceof SiteResource)) {
					return;
				}
				SiteResource article = (SiteResource)bean;
				//是否提取WORD内容作为正文
				boolean wordOnly = FjmsaDataImportUtil.isWordOnly(article.getBody());
				
				String messageid = resultSet.getString("messageid");
				String directoryType = resultSet.getString("directoryType");
				String imgURI = resultSet.getString("imgURI");
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("select * from MESS_ATTACHMENT where MESSAGEID = '" + messageid + "'");
				String resourceBody = article.getBody();
				try {
					//如果是下载栏目
					if(directoryType.equals("downLoad")) {
						String attachmentHtmlStr = FjmsaDataImportUtil.attachmentFormat(resourceBody, rs);
						String bodyHtmlStr = "";
						if(resourceBody != null) {
							bodyHtmlStr = FjmsaDataImportUtil.resourceFormat(resourceBody);
						}
						
						if(attachmentHtmlStr != null) {
							article.setBody(bodyHtmlStr + attachmentHtmlStr);
						}
						else {
							article.setBody(bodyHtmlStr);
						}
					}
					else { //如果是新闻栏目
						String bodyHtmlStr = "";
						if(imgURI != null && !(imgURI.trim().isEmpty())) {
							bodyHtmlStr +=	FjmsaDataImportUtil.imgFormat(imgURI);
						}
						if(resourceBody != null) {
							bodyHtmlStr += FjmsaDataImportUtil.resourceFormat(resourceBody);
						}
						
						String attachmentHtmlStr = FjmsaDataImportUtil.attachmentFormat(resourceBody, rs);
						if(attachmentHtmlStr != null) {
							article.setBody(bodyHtmlStr + attachmentHtmlStr);
						}
						else {
							article.setBody(bodyHtmlStr);
						}
					}

					if(wordOnly) {
						String htmlBody = HTMLBodyUtils.msWord2body(article.getBody(), true);
						if(htmlBody!=null) {
							article.setBody(htmlBody);
						}
					}
				}
				finally {
					if(rs != null) {
						rs.close();
						rs = null;
					}
					if(statement != null) {
						statement.close();
						statement = null;
					}
				}
			}
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String, java.sql.ResultSet, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
			 */
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				htmlBody = htmlBody.replaceAll("<META http-equiv=Content-Type content=\"text/html; charset=gb2312\">", "");
				//sourceSiteURL = 内网http://198.20.1.6/
				return htmlBody.replaceAll("src=\"/message", "src=\"" + parameter.getSourceSiteURL() + "message").replaceAll("href=\"/message", "href=\"" + parameter.getSourceSiteURL() + "message");
			}
		});
		return dataImporters;
	}
}