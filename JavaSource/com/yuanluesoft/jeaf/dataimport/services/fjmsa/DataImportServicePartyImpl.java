package com.yuanluesoft.jeaf.dataimport.services.fjmsa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.htmlparser.util.HTMLBodyUtils;

/**
 * 福建海事局党务
 * @author linchuan
 *
 */
public class DataImportServicePartyImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "福建海事局党工网站";
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
					return "select ID as directoryId," +
						   " Code_Name as directoryName" +
						   " from Code" +
						   " WHERE Code_Flag = 0";
				}
				else { //子目录
					return "select ID as directoryId," +
					   " Code_Name as directoryName" +
					   " from Code" +
					   " where Code_Flag = " + parentDirectoryId;
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " id as importDataId," + //被导入的记录ID
					   " 'article' as articleType," + //文章类型： 文章
					   " id as newsid, " + //原文章id
					   " files," + //附件
					   " filesName," + //附件名
					   " img," + //图片
					   " types as directoryId," +
					   " title as subject," + //标题
					   " content as body," + //正文
					   " '' as subhead," + //副标题
					   " '' as source," + //来源
					   " EmpName as Author," + //作者
					   " '' as keyword," + //关键字
					   " updates as created," + //创建时间
					   " updates as issueTime," + //发布时间
					   " EmpName as Editor," + //创建者
					   " '' as orgName," + //创建者所在部门名称
					   " '' as unitName" + //创建者所在单位名称
					   " from news where psign = 1",
					   
					   " select " +
					   " 'img_' + id as importDataId," + //被导入的记录ID
					   " 'img' as articleType," + //文章类型： 图片
					   " id as newsid, " + //原文章id
					   " '' as files," + //附件
					   " '' as filesName," + //附件名
					   " '' as img," + //图片
					   " 45 as directoryId," +
					   " title as subject," + //标题
					   " '' as body," + //正文
					   " '' as subhead," + //副标题
					   " '' as source," + //来源
					   " '' as Author," + //作者
					   " '' as keyword," + //关键字
					   " addDate as created," + //创建时间
					   " addDate as issueTime," + //发布时间
					   " '' as Editor," + //创建者
					   " '' as orgName," + //创建者所在部门名称
					   " '' as unitName" + //创建者所在单位名称
					   " from Zttp"
					   
				};
			}
			
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				if(!(bean instanceof SiteResource)) {
					return;
				}
				SiteResource article = (SiteResource)bean;
				int resourceId = resultSet.getInt("newsid");
				article.setIssueTime(new Timestamp(article.getIssueTime().getTime() + resourceId * 1000));
				String articleType = resultSet.getString("articleType");
				if("article".equals(articleType)) {
					//是否提取WORD内容作为正文
					boolean wordOnly = FjmsaDataImportUtil.isWordOnly(article.getBody());
					
					String img = resultSet.getString("img");
					if(img != null && !img.isEmpty()) {
						String imgHtml = "<div id=\"articleImgBody\"  align=\"center\"  style=\"margin-top:5px; margin-bottom:5px; table-layout:fixed; word-break:break-all; text-align:justify;\">" +
					    					"<img src=\"http://198.20.1.8:81/Upload/" + img + "\" width=\"470\">" +
					    				 "</div>";
						article.setBody(imgHtml + article.getBody());
					}
					String files = resultSet.getString("files");
					if(files != null && !files.isEmpty()) {
						String filesHtml = "<br/><strong>相关附件:</strong><a href=\"http://198.20.1.8:81/Upload/" + files + "\" target=\"_blank\">" + resultSet.getString("filesName") + "</a>";
						article.setBody(article.getBody() + filesHtml);
					}

					if(wordOnly) {
						String htmlBody = HTMLBodyUtils.msWord2body(article.getBody(), true);
						if(htmlBody!=null) {
							article.setBody(htmlBody);
						}
					}
				}
				if("img".equals(articleType)) {
					Statement statement = connection.createStatement();
					ResultSet rs = statement.executeQuery("select * from ZttpFile where p_id = " + resourceId);
					String bodyHtml = "";
					while(rs.next()) {
						bodyHtml += "<div id=\"imgArticleImgBody\" align=\"center\"  style=\"margin-top:5px;\"><img src=\"http://198.20.1.8:81/Upload/" + rs.getString("filesave") + "\"  width=\"470\" ></div>" +
									"<div id=\"imgArticleImgBody\" align=\"center\"  style=\"margin-top:2px;\">" + rs.getString("content") + "</div>";
					}
					article.setBody(bodyHtml);
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
				//sourceSiteURL = 政务http://198.20.1.8:81/
				return htmlBody.replaceAll("src=\"/Upload", "src=\"" + parameter.getSourceSiteURL() + "Upload").replaceAll("href=\"/Upload", "href=\"" + parameter.getSourceSiteURL() + "Upload");
			}
		});
		return dataImporters;
	}
}