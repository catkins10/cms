package com.yuanluesoft.jeaf.dataimport.services.zzdept;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 编办数据导入
 * @author linchuan
 *
 */
public class DataImportServiceScopsrImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "漳州编办数据导入";
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
					return "(select ID as directoryId," +
						   " zf11_names as directoryName" +
						   " from zf11_type" +
						   " where zf11_big_id=0" +
						   " or zf11_big_id=-1" +
						   " order by zf11_names)" +
						   " union " +
						   "(select 100 as directoryId," +
						   " '新闻' as directoryName" +
						   " from zf11_type" +
						   " where ID=77)";
				}
				else { //子目录
					return "select ID as directoryId," +
						   " zf11_names as directoryName" +
						   " from zf11_type" +
						   " where zf11_big_id=" + parentDirectoryId +
						   " order by zf11_names";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "Sort2 as directoryId," + //原来的栏目ID
					   "title as subject," + //标题
					   "Content as body," + //正文
					   "'' as subhead," + //副标题
					   "'' as source," + //来源
					   "writer as Author," + //作者
					   "'' as keyword," + //关键字
					   "AddDate as created," + //创建时间
					   "AddDate as issueTime," + //发布时间
					   "author as Editor," + //创建者
					   "'编办' as orgName," + //创建者所在部门名称
					   "'编办' as unitName" + //创建者所在单位名称
					   " from Info",
					   
					   "select " +
					   "id as importDataId," + //被导入的记录ID
					   "100 as directoryId," + //原来的栏目ID
					   "title as subject," + //标题
					   "Content as body," + //正文
					   "'' as subhead," + //副标题
					   "'' as source," + //来源
					   "'' as Author," + //作者
					   "'' as keyword," + //关键字
					   "time as created," + //创建时间
					   "time as issueTime," + //发布时间
					   "Admin as Editor," + //创建者
					   "'编办' as orgName," + //创建者所在部门名称
					   "'编办' as unitName" + //创建者所在单位名称
					   " from zf11_News"};
			}
		});
		return dataImporters;
	}
}