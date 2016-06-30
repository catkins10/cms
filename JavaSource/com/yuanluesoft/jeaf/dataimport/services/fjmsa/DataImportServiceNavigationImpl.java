package com.yuanluesoft.jeaf.dataimport.services.fjmsa;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 福建海事局航行通告/警告导入
 * @author linchuan
 *
 */
public class DataImportServiceNavigationImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "福建海事局航行通告/警告";
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
					return "select distinct 'hxtg' as directoryId," +
						   " '航行通告' as directoryName " +
						   " union select distinct 'hxjg' as directoryId, " +
						   " '航行警告' as directoryName ";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						   "select " +
						   " HXTG.ID as importDataId," + //被导入的记录ID
						   " 'hxtg' as directoryId," +
						   " HXTG.TGBT as subject," + //标题
						   " HXTG.TGNR as body," + //正文
						   " '' as subhead," + //副标题
						   " '' as source," + //来源
						   " '' as Author," + //作者
						   " '' as keyword," + //关键字
						   " HXTG.FBRQ as created," + //创建时间
						   " HXTG.FBRQ as issueTime," + //发布时间
						   " '' as Editor," + //创建者
						   " RGROUP.NAME as orgName," + //创建者所在部门名称
						   " RGROUP.NAME as unitName" + //创建者所在单位名称
						   " from MTYW_HXTG HXTG left join RBAC_GROUP RGROUP on HXTG.FBDW = RGROUP.ID",
						   
						   "select " +
						   " HXJG.ID as importDataId," + //被导入的记录ID
						   " 'hxjg' as directoryId," +
						   " HXJG.JGBT as subject," + //标题
						   " HXJG.JGNR as body," + //正文
						   " '' as subhead," + //副标题
						   " '' as source," + //来源
						   " '' as Author," + //作者
						   " '' as keyword," + //关键字
						   " HXJG.CZSJ as created," + //创建时间
						   " HXJG.CZSJ as issueTime," + //发布时间
						   " '' as Editor," + //创建者
						   " RGROUP.NAME as orgName," + //创建者所在部门名称
						   " RGROUP.NAME as unitName" + //创建者所在单位名称
						   " from MTYW_HXJG HXJG left join RBAC_GROUP RGROUP on HXJG.FBDW = RGROUP.ID"					   
				};
			}
		});		
		return dataImporters;
	}
}