package com.yuanluesoft.jeaf.dataimport.services.fjmsa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsReply;
import com.yuanluesoft.bbs.forum.dataimporter.BbsDataImporter;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 福建海事局论坛
 * @author linchuan
 *
 */
public class DataImportServiceBBSImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "福建海事局论坛";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//站点数据导入
		dataImporters.add(new BbsDataImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select 'category_' + convert(varchar, CategoryID) as directoryId," +
						   " CategoryName as directoryName" +
						   " from mvnforumCategory";
				}
				else if(parentDirectoryId.startsWith("category_")) { //子目录
					return "select convert(varchar, ForumID) as directoryId," +
					   " ForumName as directoryName" +
					   " from mvnforumForum" +
					   " where 'category_' + convert(char, CategoryID) = '" + parentDirectoryId + "'";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				//[url=ftp://198.21.88.3/%CE%E0%D6%DD%BE%D6/XPizev41MCE.exe] 转为ftp://198.21.88.3/%CE%E0%D6%DD%BE%D6/XPizev41MCE.exe
//				[url=http://198.21.88.3/%CE%E0%D6%DD%BE%D6/XPizev41MCE.exe] 转为http://198.21.88.3/%CE%E0%D6%DD%BE%D6/XPizev41MCE.exe
				//附件 http://198.20.1.6/mvnforum/mvnforum/getattachment?attach=10000065
				return new String[] {
//					   "select " +
//					   " 'article' as bbsarticletype," + 
//					   " convert(varchar,ForumID) as directoryId," + 
//					   " ThreadTopic as subject," + 
//					   " case when thread.ThreadAttachCount = 0 then thread.ThreadBody else thread.ThreadBody  + '<a href=\"http://198.20.1.6/mvnforum/mvnforum/getattachment?attach=' + convert(varchar,attach.AttachID) + '\">附件</a>' end, " +
//					   " thread.ThreadBody as body," + 
//					   " MemberName as creatorNickname," + 
//					   " ThreadCreationDate as created," + 
//					   " ThreadViewCount as accessTimes" + 
//					   " from mvnforumThread thread left join mvnforumAttachment attach on attach.ThreadID = thread.ThreadID ",
//					   " from mvnforumThread",
					   
					   "select " +
					   "id as importDataId," + //被导入的记录ID
					   " 'article' as bbsarticletype," + 
					   " post.PostID as pid," +
					   " convert(varchar,thread.ForumID) as directoryId," + 
					   " thread.ThreadTopic as subject," + 
					   " thread.ThreadBody as body," + 
					   " thread.MemberName as creatorNickname," + 
					   " thread.ThreadCreationDate as created," + 
					   " thread.ThreadViewCount as accessTimes" + 
					   " from mvnforumThread thread, mvnforumPost post where post.ThreadID = thread.ThreadID and post.ParentPostID = 0",						
						
						
					   "select " +
					   "id as importDataId," + //被导入的记录ID
					   " 'reply' as bbsarticletype," + 
					   " post.PostID as pid," +
					   " convert(varchar,post.ForumID) as directoryId," + 
					   " post.PostTopic as subject," + 
					   " parent.PostTopic as articleSubject," + 
					   " post.PostBody as body," + 
					   " post.MemberName as creatorNickname," + 
					   " post.PostCreationDate as created," + 
					   " parent.PostCreationDate as articleCreated," + 
					   " 0 as replyId" + 
					   " from mvnforumPost post, mvnforumPost parent where post.ParentPostID <> 0 and parent.ParentPostID = 0 and  post.ParentPostID = parent.PostID",
					   
					   "select " +
					   "id as importDataId," + //被导入的记录ID
					   " 'reply' as bbsarticletype," + 
					   " post.PostID as pid," +
					   " convert(varchar,post.ForumID) as directoryId," + 
					   " post.PostTopic as subject," + 
					   " parent.PostTopic as parentSubject," + 
					   " thread.ThreadTopic as articleSubject," + 
					   //" case when post.PostAttachCount = 0 then post.PostBody else post.PostBody  + '<a href=\"\">附件</a>' end, " +
					   " post.PostBody as body," + 
					   " post.MemberName as creatorNickname," + 
					   " post.PostCreationDate as created," + 
					   " parent.PostCreationDate as parentCreated," + 
					   " thread.ThreadCreationDate as articleCreated," + 
					   " 1 as replyId" + 
					   " from mvnforumPost post, mvnforumPost parent, mvnforumThread thread where post.ParentPostID <> 0 and parent.ParentPostID <> 0 and  post.ParentPostID = parent.PostID and thread.ThreadID = post.ThreadID" +
					   " order by post.PostID"
				};
			}
			//附件 http://198.20.1.6/mvnforum/mvnforum/getattachment?attach=10000065
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				if(!(bean instanceof SiteResource)) {
					return;
				}
				String postID = resultSet.getString("pid");
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("select * from mvnforumAttachment where PostID = " + postID);
				String attachHtml = "";
				while(rs.next()) {
					attachHtml += "<a href=\"http://198.20.1.6/mvnforum/mvnforum/getattachment?attach=" + rs.getInt("AttachID") + "&fileName=" + rs.getInt("AttachFilename") + "\">附件(" + rs.getInt("AttachFileSize") + " bytes)</a>  ";
				}
				if(!attachHtml.trim().isEmpty()) {
					if(bean instanceof com.yuanluesoft.bbs.article.pojo.BbsArticle) {
						BbsArticle article = (BbsArticle)bean;
						article.setBody(article.getBody() + "<br/>" + attachHtml);
					} else if(bean instanceof com.yuanluesoft.bbs.article.pojo.BbsReply) {
						BbsReply reply = (BbsReply)bean;
						reply.setBody(reply.getBody() + "<br/>" + attachHtml);						
					}
				}
			}
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String, java.sql.ResultSet, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
			 */
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				//[url=ftp://198.21.88.3/%CE%E0%D6%DD%BE%D6/XPizev41MCE.exe] 转为ftp://198.21.88.3/%CE%E0%D6%DD%BE%D6/XPizev41MCE.exe
				//[url=http://198.21.88.3/%CE%E0%D6%DD%BE%D6/XPizev41MCE.exe] 转为http://198.21.88.3/%CE%E0%D6%DD%BE%D6/XPizev41MCE.exe
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				htmlBody = htmlBody.replaceAll("<META http-equiv=Content-Type content=\"text/html; charset=gb2312\">", "");
				htmlBody = htmlBody.replaceAll("\\[url=(http|ftp)(://)([^\\]]+)\\](.+)\\[/url\\]", " <a href=\"$1$2$3\">$4</a> ");
				htmlBody = htmlBody.replaceAll("[/quote]", "[/quote]<br/>");
				//sourceSiteURL = 内网http://198.20.1.6/
				return htmlBody.replaceAll("src=\"/message", "src=\"" + parameter.getSourceSiteURL() + "message").replaceAll("href=\"/message", "href=\"" + parameter.getSourceSiteURL() + "message");
			}
			
		});
		
		return dataImporters;
	}
}
