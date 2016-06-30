package com.yuanluesoft.cms.sitemanage.dataimporter;

import java.io.File;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.sitemanage.pojo.WebColumn;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectorySynch;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.pojo.WebViewReference;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourcePrivilege;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryTableMapping;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 站点数据导入,SQL格式如下：
 "select " +
 " as importDataId," + //被导入的记录ID
 " as directoryId," + //原来的栏目ID
 " as subject," + //标题
 " as body," + //正文
 " as subhead," + //副标题
 " as source," + //来源
 " as mark," + //文号
 " as author," + //作者
 " as keyword," + //关键字
 " as created," + //创建时间
 " as issueTime," + //发布时间
 " as editor," + //创建者
 " as orgName," + //创建者所在部门名称
 " as unitName" + //创建者所在单位名称
 " from [原来的文章表]";
 * @author linchuan
 *
 */
public abstract class SiteDataImporter extends DirectoryDataImporter {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getImportDataName()
	 */
	public String getImportDataName() {
		return "站点";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getDirectoryServiceName()
	 */
	public String getDirectoryServiceName() {
		return "siteService";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getDirectoryTableMappings()
	 */
	public DirectoryTableMapping[] getDirectoryTableMappings() {
		return new DirectoryTableMapping[] {
			new DirectoryTableMapping("column", "cms_directory", "cms_column", WebColumn.class.getName()),
			new DirectoryTableMapping("site", "cms_directory", "cms_site", WebSite.class.getName()),
			new DirectoryTableMapping("viewReference", "cms_directory", "cms_reference", WebViewReference.class.getName())
		};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#getSameNameDirectory(java.lang.String, long)
	 */
	protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		SiteService siteService = (SiteService)Environment.getService("siteService");
		String childDirectoryIds = siteService.getChildDirectoryIds(targetSiteId + "", "column");
		if(childDirectoryIds==null || childDirectoryIds.isEmpty()) {
			return null;
		}
		String hql = "select WebDirectory.id" +
					 " from WebDirectory WebDirectory, WebDirectorySubjection WebDirectorySubjection" +
					 " where WebDirectory.directoryName='" + JdbcUtils.resetQuot(directoryName) + "'" +
					 " and WebDirectory.id=WebDirectorySubjection.directoryId" +
					 " and WebDirectorySubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(childDirectoryIds) + ")";
		List sameNameDirectoryIds = databaseService.findRecordsByHql(hql, 0, 2);
		if(sameNameDirectoryIds!=null && sameNameDirectoryIds.size()==1) {
			long directoryId = ((Long)sameNameDirectoryIds.get(0)).longValue();
			return new DirectoryMapping("" + directoryId, siteService.getDirectoryFullName(directoryId, "/", "site"));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#afterChildDirectoryCreated(com.yuanluesoft.jeaf.directorymanage.service.DirectoryService, com.yuanluesoft.jeaf.directorymanage.pojo.Directory, java.lang.String, java.lang.String, boolean, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
	 */
	protected void afterChildDirectoryCreated(DirectoryService directoryService, Directory directory, String sourceDirectoryId, String sourceDirectoryName, boolean sameSystem, Connection connection, DataImportParameter parameter) throws Exception {
		super.afterChildDirectoryCreated(directoryService, directory, sourceDirectoryId, sourceDirectoryName, sameSystem, connection, parameter);
		if(!sameSystem) { //系统不同
			return;
		}
		//导入同步关系
		String sql = "select * from cms_directory_synch where directoryId=" + sourceDirectoryId;
		Logger.info(sql);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while(resultSet.next()) {
			WebDirectorySynch directorySynch = new WebDirectorySynch();
			JdbcUtils.copyFields(directorySynch, resultSet);
			try {
				directoryService.save(directorySynch);
			}
			catch(Exception e) {
				
			}
		}
		resultSet.close();
		statement.close();
		//系统相同,开始复制模板
		sql = "select * from cms_template where siteId=" + sourceDirectoryId;
		copyTemplate(sql, directory, connection, parameter);
	}
	
	/**
	 * 拷贝模板
	 * @param sql
	 * @param directoryService
	 * @param directory
	 * @param sourceDirectoryId
	 * @param sourceDirectoryName
	 * @param sameSystem
	 * @param connection
	 * @param parameter
	 * @throws Exception
	 */
	protected void copyTemplate(String sql, Directory directory, Connection connection, DataImportParameter parameter) throws Exception {
		//系统相同,开始复制模板
		String sourceTemplatePath = parameter.getTemplatePath();
		if(parameter.getTemplatePath()==null || parameter.getTemplatePath().isEmpty()) { //没有指定模板路径
			return;
		}
		sourceTemplatePath = sourceTemplatePath.replaceAll("\\\\", "/");
		if(!sourceTemplatePath.endsWith("/")) {
			sourceTemplatePath += "/";
		}
		TemplateService templateService = (TemplateService)Environment.getService("templateService");
		String tempatePath = (String)Environment.getService("tempatePath");
		if(!tempatePath.endsWith("/")) {
			tempatePath += "/";
		}
		Logger.info(sql);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while(resultSet.next()) {
			Template template = new Template();
			JdbcUtils.copyFields(template, resultSet);
			if(FileUtils.isExists(tempatePath + template.getId())) { //模板已经存在
				continue;
			}
			if(!FileUtils.isExists(sourceTemplatePath + template.getId())) { //源模板不存在
				continue;
			}
			//检查是否特点模板
			String[] templateTables = {"com.yuanluesoft.cms.sitemanage.pojo.SiteTemplate", "cms_site_template",
									   "com.yuanluesoft.bbs.templatemanage.pojo.BbsTemplate", "bbs_template",
									   "com.yuanluesoft.cms.scene.pojo.SceneTemplate", "scene_template",
									   "com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceTemplate", "onlineservice_template",
									   "com.yuanluesoft.cms.inquiry.pojo.InquiryTemplate", "cms_inquiry_template",
									   "com.yuanluesoft.bidding.project.pojo.BiddingProjectTemplate", "bidding_project_template"};
			Statement statementTemplate = connection.createStatement();
			ResultSet resultSetTemplate = null;
			for(int i=0; i<templateTables.length; i+=2) {
				try {
					resultSetTemplate = statementTemplate.executeQuery("select * from " + templateTables[i+1] + " where id=" + template.getId());
					if(resultSetTemplate.next()) {
						Template specialTemplate = (Template)Class.forName(templateTables[i]).newInstance();
						JdbcUtils.copyFields(specialTemplate, resultSetTemplate);
						PropertyUtils.copyProperties(specialTemplate, template);
						template = specialTemplate;
						break;
					}
				}
				catch(Exception e) {
					
				}
				finally {
					try {
						resultSetTemplate.close();
					}
					catch(Exception e) {
						
					}
				}
			}
			//拷贝模板文件
			FileUtils.copyDirectory(sourceTemplatePath + template.getId(), FileUtils.createDirectory(tempatePath + template.getId()), true, false);
			resetTemplate(tempatePath + template.getId() + "/", directory, connection, parameter); //重置模板
			//保存模板记录
			template.setSiteId(directory.getId());
			templateService.save(template);
			statementTemplate.close();
		}
		resultSet.close();
		statement.close();
	}
	
	/**
	 * 重置模板
	 * @param templatePath
	 * @param parameter
	 */
	private void resetTemplate(String templatePath, Directory directory, Connection connection, DataImportParameter parameter) throws Exception {
		//修改template.html中的ContextPath
		String text = FileUtils.readStringFromFile(templatePath + "template.html", "utf-8");
		//检查子模板
		//<DIV id="subTemplate" style="border-bottom: #0000cd 1px dotted; border-left: #0000cd 1px dotted; border-top: #0000cd 1px dotted; border-right: #0000cd 1px dotted" title="子模板=头部-教育&amp;ID=200224221524550000&amp;禁止继承=true">
		Pattern pattern = Pattern.compile("<div[^>]*id=\"subTemplate\"[^>]*title=\"子模板=[^&]*&amp;ID=([^&]*)&[^>]*>", Pattern.CASE_INSENSITIVE); //不区分大小写
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()) {
			String subTemplateId = matcher.group(1);
			//拷贝子模板
			copyTemplate("select * from cms_template where id=" + subTemplateId, directory, connection, parameter);
		}
		//修改ContextPath
		if(parameter.getTemplateUrl()==null || parameter.getTemplateUrl().isEmpty()) {
			return;
		}
		String tempateUrl = (String)Environment.getService("tempateUrl");
		if(tempateUrl.equals(parameter.getTemplateUrl())) {
			return;
		}
		FileUtils.saveStringToFile(templatePath + "template.html", text.replaceAll(parameter.getTemplateUrl(), tempateUrl), "utf-8", true);
		
		//修改css中的ContextPath
		File[] cssFiles = new File(templatePath + "css").listFiles();
		for(int i=(cssFiles==null ? -1 : cssFiles.length-1); i>=0; i--) {
			text = FileUtils.readStringFromFile(cssFiles[i].getPath(), "utf-8");
			FileUtils.saveStringToFile(cssFiles[i].getPath(), text.replaceAll(parameter.getTemplateUrl(), tempateUrl), "utf-8", true);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#saveImportData(com.yuanluesoft.jeaf.dataimport.dataimporter.callback.DirectoryDataImporterCallback, java.sql.ResultSet, java.lang.String)
	 */
	protected long saveImportData(ResultSet resultSet, String mappingDirectoryIds, WebSite targetSite, Connection connection, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		SiteResourceService siteResourceService = (SiteResourceService)Environment.getService("siteResourceService");
		SiteService siteService = (SiteService)Environment.getService("siteService");
		if(sameSystem) { //相同系统
			long articleId = resultSet.getLong("importDataId");
			if(databaseService.findRecordById(SiteResource.class.getName(), articleId, null)!=null) {
				return articleId;
			}
			SiteResource article = new SiteResource();
			String sql = "select *, (select body from cms_resource_body where cms_resource_body.id=cms_resource.id) body from cms_resource where id=" + articleId;
			Statement statement = connection.createStatement();
			ResultSet resultSetArticle = statement.executeQuery(sql);
			resultSetArticle.next();
			JdbcUtils.copyFields(article, resultSetArticle);
			resultSetArticle.close();
			article.setWorkflowInstanceId(null);
			article.setBody(updateAttachmentPath(article.getBody(), resultSet, connection, parameter)); //更新正文中的附件URL,以下载到本服务器
			afterPojoGenerated(article, resultSet, connection); //回调
			siteResourceService.save(article); //保存
			//下载链接图片
			if(article.getType()==SiteResourceService.RESOURCE_TYPE_LINK && article.getFirstImageName()!=null && !article.getFirstImageName().isEmpty()) {
				String contextPath = "";
				if(parameter.getTemplatePath()!=null) {
					int index = parameter.getTemplatePath().indexOf("/cms/templates");
					if(index!=-1) {
						contextPath = parameter.getTemplatePath().substring(0, index);
					}
				}
				String url =  contextPath + "/cms/pages/" + article.getId() + "/images/" + URLEncoder.encode(article.getFirstImageName(), "utf-8");
				downloadAttachment(url, parameter, "siteAttachmentService", "cms/siteresource", "images", article.getId());
			}
			//创建权限记录
			cloneSitePrivileges(SiteResourcePrivilege.class, article.getId(), targetSite, databaseService);
		
			//设置所在站点
			sql = "select siteId from cms_resource_subjection where resourceId=" + articleId;
			Logger.debug(sql);
			resultSetArticle = statement.executeQuery(sql);
			String siteIds = mappingDirectoryIds;
			while(resultSetArticle.next()) {
				siteIds = (siteIds==null ? "" : siteIds + ",") + resultSetArticle.getString("siteId");
			}
			resultSetArticle.close();
			statement.close();
			String hql = "select WebDirectory.id from WebDirectory WebDirectory where WebDirectory.id in (" + JdbcUtils.validateInClauseNumbers(siteIds) + ")";
			List ids = databaseService.findRecordsByHql(hql);
			siteResourceService.updateSiteResourceSubjections(article, true, ListUtils.join(ids, ",", false));
			return article.getId();
		}
		
		SiteResource article = new SiteResource();
		JdbcUtils.copyFields(article, resultSet);
		article.setBody(updateAttachmentPath(article.getBody(), resultSet, connection, parameter)); //更新正文中的附件URL,以下载到本服务器
		article.setSubject(StringUtils.filterHtmlElement(article.getSubject(), false));
		afterPojoGenerated(article, resultSet, connection); //回调

		SiteResource oldArticle;
		if(importedRecordId>0 && (oldArticle = (SiteResource)databaseService.findRecordById(SiteResource.class.getName(), importedRecordId, ListUtils.generateList("lazyBody,subjections", ",")))!=null) { //检查是否原来导入的记录是否存在
			if(oldArticle.getBody()==null || oldArticle.getBody().isEmpty()) { //仅更新正文为空的记录,因此,如果正文有问题,需要事先清空正文,SQL: update cms_resource set body=null where id in (select localRecordId from dataimport_record)
				oldArticle.setBody(article.getBody()); //更新正文
				siteResourceService.update(oldArticle); //更新
			}
			siteResourceService.updateSiteResourceSubjections(oldArticle, false, mappingDirectoryIds);
			return importedRecordId;
		}
		
		//新的文章
		article.setId(sameSystem ? Long.parseLong(sourceDataId) : UUIDLongGenerator.generateId()); //ID
		if(article.getStatus()==SiteResourceService.RESOURCE_STATUS_TODO) { //待办状态,说明导入的数据中没有状态标志
			article.setStatus(SiteResourceService.RESOURCE_STATUS_ISSUE);
		}
		article.setType(SiteResourceService.RESOURCE_TYPE_ARTICLE);
		if(article.getBody()==null || article.getBody().isEmpty()) {
			article.setBody("　");
		}
		article.setIssuePersonId(100);
		article.setAnonymousLevel(siteService.getAnonymousLevel(Long.parseLong(mappingDirectoryIds.split(",")[0]))); //匿名用户访问级别
		siteResourceService.save(article); //保存
		
		//创建权限记录
		cloneSitePrivileges(SiteResourcePrivilege.class, article.getId(), targetSite, databaseService);
		
		//设置所在站点
		siteResourceService.updateSiteResourceSubjections(article, true, mappingDirectoryIds);
		return article.getId();
	}
}