package com.yuanluesoft.jeaf.dataimport.services.general;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 文章转换为政府信息
 * @author linchuan
 *
 */
public class InfoPublicImportFromSiteServiceImpl extends DataImportService {

	public InfoPublicImportFromSiteServiceImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "相同系统:文章转换为政府信息";
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
					return "select id as directoryId," +
						   " directoryName" +
						   " from cms_directory" +
						   " where id=0";
				}
				else { //子目录
					return "select id as directoryId," +
						   " directoryName" +
						   " from cms_directory" +
						   " where parentDirectoryId=" + parentDirectoryId +
						   " and id!=0";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " id as importDataId," + //被导入的记录ID
					   " (select min(siteId) from cms_resource_subjection where cms_resource_subjection.resourceId=cms_resource.id and siteId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(selectedSourceDirectoryIds, ",", false)) + ")) as directoryId," + //原来的栏目ID
					   " subject," + //标题
					   " (select body from cms_resource_body where cms_resource_body.id=cms_resource.id) body," + //正文
					   " '' as infoIndex," + //索引号
					   " unitName as infoFrom," + //发布机构
					   " mark," + //文号
					   " issueTime as generateDate," + //生成日期
					   " '' as summarize," + //内容概述
					   " '' as category," + //主题分类
					   " keyword as keywords," + //关键字
					   " created," + //创建时间
					   " issueTime," + //发布时间
					   " editor as creator," + //创建者
					   " orgName," + //创建者所在部门名称
					   " unitName," + //创建者所在单位名称
					   " status" + //状态
					   " from cms_resource" +
					   " where id in (" +
	   			   	   "  select cms_resource_subjection.resourceId" +
	   			   	   "   from cms_resource_subjection, cms_directory_subjection" +
	   			   	   "   where cms_resource_subjection.siteId=cms_directory_subjection.directoryId" +
	   			   	   "   and parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(selectedSourceDirectoryIds, ",", false)) + ")" +
	   			   	   ")"};
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