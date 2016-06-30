package com.yuanluesoft.jeaf.dataimport.services.zhaoan.dept;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 
 * @author linchuan
 *
 */
public class SiteImportServiceCivilImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "诏安民政局站点导入";
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
					return "select Id as directoryId," +
						   " Name as directoryName" +
						   " from Menu" +
						   " where Languagetype='1'";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						"select " +
						 "Id as importDataId," + //被导入的记录ID
						 "Menuid as directoryId," + //原来的栏目ID
						 "Title as subject," + //标题
						 "Memos as body," + //正文
						 "null as subhead," + //副标题
						 "null as source," + //来源
						 "null as Author," + //作者
						 "null as keyword," + //关键字
						 "Date as created," + //创建时间
						 "Date as issueTime," + //发布时间
						 "null as editor," + //创建者
						 "'' as orgName," + //创建者所在部门名称
						 "'' as unitName" + //创建者所在单位名称
						 " from WEBINFO"
				};
			}
		});
		return dataImporters;
	}
}