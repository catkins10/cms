package com.yuanluesoft.jeaf.dataimport.services.zhaoan.dept;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 
 * @author linchuan
 *
 */
public class PublicInfoImportServiceFoodImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "诏安粮食局信息公开导入";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		//信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select ID as directoryId," +
						   " Names as directoryName" +
						   " from Sort";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "ID as importDataId," + //被导入的记录ID
					   "Sort1 as directoryId," + //原来的目录ID
					   "writefrom as infoIndex," + //索引号
					   "'诏安县粮食局' as infoFrom," + //发布机构
					   "null as mark," + //文号
					   "Date as generateDate," + //生成日期
					   "Title as subject," + //标题
					   "Content as body," + //正文
					   "null as creator," + //创建人
					   "AddDate as created," + //创建时间
					   "AddDate as issueTime," + //发布时间
					   "'' as summarize," + //内容概述
					   "'' as category," + //主题分类
					   "'' as keywords," + //主题词
					   "'诏安县粮食局' as orgName," + //创建者所在部门名称
					   "'诏安县粮食局' as unitName" + //创建者所在单位名称
					   " from Info"
				};
			}
		});
		return dataImporters;
	}
}