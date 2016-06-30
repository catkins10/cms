package com.yuanluesoft.jeaf.dataimport.services.general;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 信息公开数据导入
 * @author linchuan
 *
 */
public class InfoPublicDataImportServiceImpl extends DataImportService {

	public InfoPublicDataImportServiceImpl() {
		super();
		sameSystem = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "相同系统:信息公开数据导入服务";
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
						   " from public_directory" +
						   " where id=0";
				}
				else { //子目录
					return "select id as directoryId," +
						   " directoryName" +
						   " from public_directory" +
						   " where parentDirectoryId=" + parentDirectoryId +
						   " and id!=0";
				}
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter#generateDirectoryCode(java.lang.String)
			 */
			public String generateDirectoryCodeSql(String sourceDirectoryId) throws Exception {
				return "select code from public_directory where id=" + sourceDirectoryId;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " id as importDataId," + //被导入的记录ID
					   " (select min(directoryId) from public_info_subjection where public_info_subjection.infoId=public_info.id and directoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(selectedSourceDirectoryIds, ",", false)) + ")) as directoryId," + //原来的栏目ID
					   " subject," + //标题
					   " (select body from public_info_body where public_info_body.id=public_info.id) body," + //正文
					   " infoIndex," + //索引号
					   " infoFrom," + //发布机构
					   " mark," + //文号
					   " generateDate," + //生成日期
					   " summarize," + //内容概述
					   " category," + //主题分类
					   " keywords," + //关键字
					   " created," + //创建时间
					   " issueTime," + //发布时间
					   " creator," + //创建者
					   " orgName," + //创建者所在部门名称
					   " unitName" + //创建者所在单位名称
					   " from public_info" +
					   " where status='" + PublicInfoService.INFO_STATUS_ISSUE + "'" +
	   			   	   " and id in (" +
	   			   	   "  select public_info_subjection.infoId" +
	   			   	   "   from public_info_subjection, public_directory_subjection" +
	   			   	   "   where public_info_subjection.directoryId=public_directory_subjection.directoryId" +
	   			   	   "   and parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(selectedSourceDirectoryIds, ",", false)) + ")" +
	   			   	   ")" +
	   			   	   "order by id"};
			}
		});
		return dataImporters;
	}
}