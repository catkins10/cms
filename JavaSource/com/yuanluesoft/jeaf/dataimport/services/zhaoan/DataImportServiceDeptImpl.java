package com.yuanluesoft.jeaf.dataimport.services.zhaoan;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 诏安部门网站
 * @author linchuan
 *
 */
public class DataImportServiceDeptImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "诏安部门网站";
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
					return "select 'first_' & id as directoryId," +
						   " lm as directoryName" +
						   " from lm" +
						   " where lmid is null" +
						   " or lmid=''";
				}
				else if(parentDirectoryId.startsWith("first_")) { //第二级目录
					return "select 'second_' & id as directoryId," +
						   " lm2 as directoryName" +
						   " from lm" +
						   " where lmid='" + parentDirectoryId.substring("first_".length()) + "'";
				}
				else if(parentDirectoryId.startsWith("second_")) { //第三级目录
					return "select 'third_' & id as directoryId," +
						   " lm3 as directoryName" +
						   " from lm" +
						   " where lmid='" + parentDirectoryId.substring("second_".length()) + "'";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "iif(lm3<>'0', 'third_' & lm3, iif(lm2<>'0', 'second_' & lm2, 'first_' & lm)) as directoryId," + //原来的栏目ID
					   "title as subject," + //标题
					   "content as body," + //正文
					   "'' as subhead," + //副标题
					   "piczz as mark," + //文号
					   "LaiYuan as source," + //来源
					   "'' as author," + //作者
					   "'' as keyword," + //关键字
					   "time as created," + //创建时间
					   "time as issueTime," + //发布时间
					   "zz as editor," + //创建者
					   "zz as orgName," + //创建者所在部门名称
					   "zz as unitName" + //创建者所在单位名称
					   " from news" +
					   " where not LaiYuan like 'zz%'"};
			}
		});
		
		//信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select 'first_' & id as directoryId," +
						   " lm as directoryName" +
						   " from lm" +
						   " where lmid is null" +
						   " or lmid=''";
				}
				else if(parentDirectoryId.startsWith("first_")) { //第二级目录
					return "select 'second_' & id as directoryId," +
						   " lm2 as directoryName" +
						   " from lm" +
						   " where lmid='" + parentDirectoryId.substring("first_".length()) + "'";
				}
				else if(parentDirectoryId.startsWith("second_")) { //第三级目录
					return "select 'third_' & id as directoryId," +
						   " lm3 as directoryName" +
						   " from lm" +
						   " where lmid='" + parentDirectoryId.substring("second_".length()) + "'";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "iif(lm3<>'0', 'third_' & lm3, iif(lm2<>'0', 'second_' & lm2, 'first_' & lm)) as directoryId," + //原来的目录ID
					   "LaiYuan as infoIndex," + //索引号
					   "zz as infoFrom," + //发布机构
					   "piczz as mark," + //文号
					   "time as generateDate," + //生成日期
					   "title as subject," + //标题
					   "content as body," + //正文
					   "zz as creator," + //创建人
					   "time+1 as created," + //创建时间
					   "time+1 as issueTime," + //发布时间
					   "htitle as summarize," + //内容概述
					   "'' as category," + //主题分类
					   "'' as keywords," + //主题词
					   "zz as orgName," + //创建者所在部门名称
					   "zz as unitName" + //创建者所在单位名称
					   " from news" +
					   " where LaiYuan like 'zz%'"
				};
			}
		});
		return dataImporters;
	}
}
