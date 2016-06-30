package com.yuanluesoft.jeaf.dataimport.services.yunxiao;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 云霄县建设局网站
 * @author linchuan
 *
 */
public class DataImportServiceJsImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "云霄县建设局网站";
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
					return "select SID as directoryId," +
						   " S_Name as directoryName" +
						   " from ProSort" +
						   " WHERE FID=0";
				}
				else { //子目录
					return "select SID as directoryId," +
						   " S_Name as directoryName" +
						   " from ProSort" +
						   " where FID=" + parentDirectoryId + " and SID<>0";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " NID as importDataId," + //被导入的记录ID
					   " NClass as directoryId," + //原来的栏目ID
					   " Title as subject," + //标题
					   " Content as body," + //正文
					   " '' as subhead," + //副标题
					   " Original as Source," + //来源
					   " '' as Author," + //作者
					   " '' as keyword," + //关键字
					   " DateAndTime as created," + //创建时间
					   " DateAndTime as issueTime," + //发布时间
					   " Author as editor," + //创建者
					   " '云霄县建设局' as orgName," + //创建者所在部门名称
					   " '云霄县建设局' as unitName" + //创建者所在单位名称
					   " from BTXWorks_Info"
				};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter#getSameNameDirectory(java.lang.String, long)
			 */
			protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
				return null;
			}
		});
		return dataImporters;
	}
}