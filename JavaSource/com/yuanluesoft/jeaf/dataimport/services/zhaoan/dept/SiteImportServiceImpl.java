package com.yuanluesoft.jeaf.dataimport.services.zhaoan.dept;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 
 * @author linchuan
 *
 */
public class SiteImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "诏安金星乡、四都镇、劳保局、国土局站点导入";
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
						   " ClassName as directoryName" +
						   " from NewsClass" +
						   " where AboveLevel=0";
				}
				else { //其他目录
					return "select id as directoryId," +
						   " ClassName as directoryName" +
						   " from NewsClass" +
						   " where AboveLevel=" + parentDirectoryId;
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						"select " +
						 "NewsID as importDataId," + //被导入的记录ID
						 "ClassID as directoryId," + //原来的栏目ID
						 "Title as subject," + //标题
						 "Content as body," + //正文
						 "null as subhead," + //副标题
						 "null as source," + //来源
						 "Author," + //作者
						 "null as keyword," + //关键字
						 "AddTime as created," + //创建时间
						 "AddTime as issueTime," + //发布时间
						 "Original as editor," + //创建者
						 "'' as orgName," + //创建者所在部门名称
						 "'' as unitName" + //创建者所在单位名称
						 " from News"
				};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				SiteResource resource = (SiteResource)bean;
				if(resource.getBody()!=null) {
					resource.setBody(generateHtml(resource.getBody()));
				}
			}
			
			private String generateHtml(String text) {
				if(text==null) {
					return text;
				}
				return text.replaceAll("&nbsp;", " ")
						   .replaceAll("&quot;", "\"")
						   .replaceAll("&amp;", "&")
						   .replaceAll("&lt;", "<")
						   .replaceAll("&gt;", ">");
			}
		});
		return dataImporters;
	}
}