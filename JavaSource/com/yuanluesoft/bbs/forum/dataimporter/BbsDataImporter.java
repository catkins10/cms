package com.yuanluesoft.bbs.forum.dataimporter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsReply;
import com.yuanluesoft.bbs.article.service.BbsArticleService;
import com.yuanluesoft.bbs.article.service.BbsReplyService;
import com.yuanluesoft.bbs.forum.pojo.Bbs;
import com.yuanluesoft.bbs.forum.pojo.Forum;
import com.yuanluesoft.bbs.forum.pojo.ForumCategory;
import com.yuanluesoft.bbs.forum.service.ForumService;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryTableMapping;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 论坛数据导入,SQL格式如下：
 * 主题:
 " select " +
 " as importDataId," + //被导入的记录ID
 " as bbsarticletype," + 类型,'article'为文章,'reply'为回复 
 " as directoryId," + //原来的栏目ID
 " as subject," + //标题
 " as body," + //正文
 " as creatorNickname," + //发布人昵称
 " as created," + //发帖时间
 " as accessTimes," + //访问次数
 " from [原来的论坛主题]";
 * 回复主题:
 " select " +
 " as bbsarticletype," + 类型,'article'为文章,'reply'为回复 
 " as articleId," + //原来的栏目ID
 " as subject," + //标题
 " as articleSubject," + //文章标题
 " as body," + //正文
 " 0 as replyId," + //被回复的回复ID,0表示是直接对文章的回复
 " as creatorNickname," + //发布人昵称
 " as created," + //发帖时间
 " as articleCreated" + //文章发帖时间
 " from [原来的论坛回复]"; 
 *
 * 回复回复:
 " select " +
 " as importDataId," + //被导入的记录ID
 " as bbsarticletype," + 类型,'article'为文章,'reply'为回复 
 " as articleId," + //原来的栏目ID
 " as subject," + //标题
 " as parentSubject," + //父标题
 " as body," + //正文
 " 1 as replyId," + //1表示有回复,在后续操作中设置回复ID
 " as creatorNickname," + //发布人昵称
 " as created," + //发帖时间
 " as parentCreated" + //父发帖时间
 " from [原来的论坛回复]"; 
 * @author lmiky
 *
 */
public abstract class BbsDataImporter extends DirectoryDataImporter {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getImportDataName()
	 */
	public String getImportDataName() {
		return "论坛";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getDirectoryServiceName()
	 */
	public String getDirectoryServiceName() {
		return "forumService";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getDirectoryTableMappings()
	 */
	public DirectoryTableMapping[] getDirectoryTableMappings() {
		return new DirectoryTableMapping[] {
				new DirectoryTableMapping("forum", "bbs_directory", "bbs_forum", Forum.class.getName()),
				new DirectoryTableMapping("bbs", "bbs_directory", "bbs_bbs", Bbs.class.getName()),
				new DirectoryTableMapping("category", "bbs_directory", "bbs_forum_category", ForumCategory.class.getName())
			};
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getSameNameDirectory(java.lang.String, long)
	 */
	protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		ForumService forumService = (ForumService)Environment.getService("forumService");
		String hql = "select BbsDirectory.id" +
					 " from BbsDirectory BbsDirectory" +
					 " where BbsDirectory.directoryName='" + JdbcUtils.resetQuot(directoryName) + "'";
		List sameNameDirectoryIds = databaseService.findRecordsByHql(hql, 0, 2);
		if(sameNameDirectoryIds!=null && sameNameDirectoryIds.size()==1) {
			long directoryId = ((Long)sameNameDirectoryIds.get(0)).longValue();
			return new DirectoryMapping("" + directoryId, forumService.getDirectoryFullName(directoryId, "/", "main"));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#saveImportData(java.sql.ResultSet, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.WebSite, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
	 */
	protected long saveImportData(ResultSet resultSet, String mappingDirectoryIds, WebSite targetSite, Connection connection, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		String beanType = resultSet.getString("bbsarticletype");
		if("article".equals(beanType)) { //主题
			BbsArticleService bbsArticleService = (BbsArticleService)Environment.getService("bbsArticleService");
			
			BbsArticle article = new BbsArticle();
			JdbcUtils.copyFields(article, resultSet);
			article.setBody(updateAttachmentPath(article.getBody(), resultSet, connection, parameter)); //更新正文中的附件URL,以下载到本服务器
			afterPojoGenerated(article, resultSet, connection); //回调
			
			BbsArticle oldArticle;
			if(importedRecordId>0 && (oldArticle = (BbsArticle)databaseService.findRecordById(BbsArticle.class.getName(), importedRecordId, ListUtils.generateList("subjections", ",")))!=null) { //检查是否原来导入的记录是否存在
				oldArticle.setBody(article.getBody()); //更新正文
				bbsArticleService.update(oldArticle); //更新
				bbsArticleService.updateArticleSubjections(oldArticle, false, mappingDirectoryIds);
				return importedRecordId;
			}
			
			article.setId(UUIDLongGenerator.generateId()); //ID
			article.setCreatorId(100);
			
			//保存
			bbsArticleService.save(article);
			//保存文章隶属版块
			bbsArticleService.updateArticleSubjections(article, true, mappingDirectoryIds);
			return article.getId();
		}
		else { //回复
			BbsReplyService bbsReplyService = (BbsReplyService)Environment.getService("bbsReplyService");
			
			BbsReply reply = new BbsReply();
			JdbcUtils.copyFields(reply, resultSet);
			reply.setBody(updateAttachmentPath(reply.getBody(), resultSet, connection, parameter)); //更新正文中的附件URL,以下载到本服务器
			afterPojoGenerated(reply, resultSet, connection); //回调
			
			BbsReply oldReply;
			if(importedRecordId>0 && (oldReply = (BbsReply)databaseService.findRecordById(BbsReply.class.getName(), importedRecordId, ListUtils.generateList("subjections", ",")))!=null) { //检查是否原来导入的记录是否存在
				oldReply.setBody(reply.getBody()); //更新正文
				bbsReplyService.update(oldReply); //更新
				return importedRecordId;
			}
			
			//设置回复所属主题
			String articleSubject = resultSet.getString("articleSubject");
			Timestamp articleCreated = resultSet.getTimestamp("articleCreated");
			String hql = "from BbsArticle BbsArticle" + 
							 " where BbsArticle.subject='" + JdbcUtils.resetQuot(articleSubject) + "'" +
							 " and BbsArticle.created=TIMESTAMP(" + DateTimeUtils.formatTimestamp(articleCreated, null) + ")";
			BbsArticle article =  (BbsArticle)databaseService.findRecordByHql(hql, ListUtils.generateList("lazyBody", ","));
			if(article!=null) {
				reply.setArticleId(article.getId());
			}
			
			//设置回复的回复id
			if(reply.getReplyId() != 0) {
				String parentSubject = resultSet.getString("parentSubject");
				Timestamp parentCreated = resultSet.getTimestamp("parentCreated");
				hql = "from BbsReply BbsReply" + 
					  " where BbsReply.subject='" + JdbcUtils.resetQuot(parentSubject) + "'" +
					  " and BbsReply.created=TIMESTAMP(" + DateTimeUtils.formatTimestamp(parentCreated, null) + ")";
				BbsReply parentReply =  (BbsReply)databaseService.findRecordByHql(hql, ListUtils.generateList("lazyBody", ","));
				if(parentReply == null) {
					reply.setReplyId(0);
				} else {
					reply.setReplyId(parentReply.getId());
				}
			}
			
			reply.setId(UUIDLongGenerator.generateId()); //ID
			reply.setCreatorId(100);
			bbsReplyService.save(reply); //保存
			return reply.getId();
		}
	}
}