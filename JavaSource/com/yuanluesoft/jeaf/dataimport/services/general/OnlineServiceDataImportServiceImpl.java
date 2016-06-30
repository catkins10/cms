package com.yuanluesoft.jeaf.dataimport.services.general;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 网上办事数据导入
 * @author linchuan
 *
 */
public class OnlineServiceDataImportServiceImpl extends DataImportService {

	public OnlineServiceDataImportServiceImpl() {
		super();
		sameSystem = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "相同系统:网上办事数据导入";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//网上办事数据导入
		dataImporters.add(new OnlineServiceImporter() {
			
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select id as directoryId," +
						   " directoryName" +
						   " from onlineservice_directory" +
						   " where id=0";
				}
				else { //子目录
					return "select id as directoryId," +
						   " directoryName" +
						   " from onlineservice_directory" +
						   " where parentDirectoryId=" + parentDirectoryId +
						   " and id!=0";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " id as importDataId," + //被导入的记录ID
					   " (select min(directoryId) from onlineservice_item_subjection where onlineservice_item_subjection.itemId=onlineservice_item.id and directoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(selectedSourceDirectoryIds, ",", false)) + ")) as directoryId," + //原来的目录ID
					   " name," + //名称
					   " itemType," + //审批服务类型
					   " acceptUrl," + //在线受理链接
					   " creator," + //创建人
					   " created," + //创建时间
					   " '' as faq," + //问题和解答
					   " '' as serviceCondition," + //申办条件
					   " '' as serviceAccording," + //办理依据
					   " '' as serviceProgram," + //办理程序
					   " '' as timeLimit," + //承诺时限
					   " '' as chargeAccording," + //收费依据
					   " '' as chargeStandard," + //收费标准
					   " '' as legalRight," + //法律权利,申请人法律权利及申诉途径
					   " '' as address," + //办理地点
					   " '' as traffic," + //交通线路
					   " '' as transactor," + //经办人
					   " '' as transactorTel," + //经办人联系电话
					   " '' as unitName" + //办理机构名称
					   " from onlineservice_item" +
					   " where id in (" +
	   			   	   "  select onlineservice_item_subjection.itemId" +
	   			   	   "   from onlineservice_item_subjection, onlineservice_dir_subjection" +
	   			   	   "   where onlineservice_item_subjection.directoryId=onlineservice_dir_subjection.directoryId" +
	   			   	   "   and parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(selectedSourceDirectoryIds, ",", false)) + ")" +
	   			   	   ")"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter#generateListServiceItemMaterialsSQL(java.lang.String)
			 */
			public String generateListServiceItemMaterialsSQL(String importItemId) {
				return null;
			}
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter#generateListServiceItemDownloadsSQL(java.lang.String)
			 */
			public String generateListServiceItemDownloadsSQL(String importItemId) {
				return null;
			}
		});
		return dataImporters;
	}
}