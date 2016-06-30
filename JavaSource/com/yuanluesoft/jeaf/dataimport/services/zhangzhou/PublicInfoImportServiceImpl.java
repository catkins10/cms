package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 漳州市政府信息公开
 * @author linchuan
 *
 */
public class PublicInfoImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "漳州市政府信息公开";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		
		//政府信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select ID as directoryId," +
						   " TypeName as directoryName" +
						   " from Z_InformationType";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				//updateOldInfo = true;
				return new String[] {
					   "select " +
					   " ID as importDataId," + //被导入的记录ID
					   " TypeID as directoryId," + //原来的栏目ID
					   " Title as subject," + //标题
					   " Content as body," + //正文
					   " IndexNumber as infoIndex," + //索引号
					   " (SELECT SourceName FROM Z_Source WHERE (Z_Source.ID = Z_PublicInformation.SourceID)) as infoFrom," + //发布机构
					   " Symbol as mark," + //文号
					   " ShowTime as generateDate," + //生成日期
					   " '　' as summarize," + //内容概述
					   " StatTypeID as category," + //主题分类
					   " Tags as keywords," + //关键字
					   " AddDate as created," + //创建时间
					   " AddDate as issueTime," + //发布时间
					   " '' as creator," + //创建者
					   " (SELECT SourceName FROM Z_Source WHERE (Z_Source.ID = Z_PublicInformation.SourceID)) as orgName," + //创建者所在部门名称
					   " (SELECT SourceName FROM Z_Source WHERE (Z_Source.ID = Z_PublicInformation.SourceID)) as unitName" + //创建者所在单位名称
					   " from Z_PublicInformation" +
	   			       " WHERE IsOK='1'"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				PublicInfo publicInfo = (PublicInfo)bean;
				if("0".equals(publicInfo.getCategory())) {
					publicInfo.setCategory(null);
				}
				else if("10112".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("1、机构职能类");
				}
				else if("10150".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("2、政策、规范性文件类");
				}
				else if("10151".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("3、规划计划类");
				}
				else if("10152".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("4、行政许可类");
				}
				else if("10153".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("5、重大建设项目、为民办实事类");
				}
				else if("10154".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("6、民政扶贫救灾社会保障就业类");
				}
				else if("10155".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("7、国土资源城乡建设环保能源类");
				}
				else if("10156".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("8、科教文体卫生类");
				}
				else if("10157".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("9、安全生产、应急管理类");
				}
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter#getSameNameDirectory(java.lang.String, long)
			 */
			protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
				return null;
			}
		});
		return dataImporters;
	}
}