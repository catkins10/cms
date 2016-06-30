package com.yuanluesoft.jeaf.dataimport.services.changshan;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 常山华侨经济开发区
 * @author linchuan
 *
 */
public class SiteImportServiceImpl extends DataImportService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "常山华侨经济开发区网站";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//网站数据导入
		dataImporters.add(new SiteDataImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#generateListChildDirectoriesSQL(java.lang.String)
			 */
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select menu_id as directoryId," +
						   " menu_name as directoryName" +
						   " from item" +
						   " order by menu_id";
				}
				else {
					return "select menu_id & '_' & class_id as directoryId," +
						   " class_name as directoryName" +
						   " from cclass" +
						   " where menu_id='" + parentDirectoryId + "'" +
						   " order by class_name";
				}
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#generateRetrieveDataSQL(java.util.List)
			 */
			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						   "select " +
						   " menu_id & '_' & class_id & '_' & NewsID as importDataId," + //被导入的记录ID
						   " iif(class_id='', menu_id, menu_id & '_' & class_id) as directoryId," + //原来的栏目ID
						   " NewsTitle as subject," + //标题
						   " NewsContain as body," + //正文
						   " '' as subhead," + //副标题
						   " Source," + //来源
						   " Author," + //作者
						   " '' as keyword," + //关键字
						   " PubDate as created," + //创建时间
						   " PubDate as issueTime," + //发布时间
						   " Author as editor," + //创建者
						   " '常山华侨经济开发区' as orgName," + //创建者所在部门名称
						   " '常山华侨经济开发区' as unitName" + //创建者所在单位名称
						   " from contents" +
						   " where Online=true"};
			}
		});
		return dataImporters;
	}
}