package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 漳州市国土局
 * @author linchuan
 *
 */
public class SiteImportServiceZzgtImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "漳州市国土局";
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
					return "select fid as directoryId," +
						   " LMname as directoryName" +
						   " from LMtab1";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " ArticleID as importDataId," + //被导入的记录ID
					   " LMid as directoryId," + //原来的栏目ID
					   " Title as subject," + //标题
					   " Content as body," + //正文
					   " Subheading as subhead," + //副标题
					   " CopyFrom as Source," + //来源
					   " Author," + //作者
					   " keyword," + //关键字
					   " CreateTime as created," + //创建时间
					   " CreateTime as issueTime," + //发布时间
					   " '市国土局' as editor," + //创建者
					   " '市国土局' as orgName," + //创建者所在部门名称
					   " '市国土局' as unitName" + //创建者所在单位名称
					   " from PE_Article"};
			}
		});
		return dataImporters;
	}
}