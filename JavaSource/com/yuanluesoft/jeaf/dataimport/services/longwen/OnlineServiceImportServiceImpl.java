package com.yuanluesoft.jeaf.dataimport.services.longwen;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.callback.FileSearchCallback;

/**
 * 龙文区网上办事
 * @author linchuan
 *
 */
public class OnlineServiceImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "龙文区网上办事";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		
		//网上办事数据导入
		dataImporters.add(new OnlineServiceImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#generateListChildDirectoriesSQL(java.lang.String)
			 */
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select 受理单位 as directoryId," +
						   " 受理单位 as directoryName" +
						   " from [all$]" +
						   " order by 受理单位";
				}
				return null;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
									 "事项名称 as importDataId," + //被导入的记录ID
									 "受理单位 as directoryId," + //原来的目录ID
									 "null as code," + //编码
									 "事项名称 as name," + //名称
									 "许可类型 as itemType," + //审批服务类型
									 "'http://www.lwxzfw.org' as acceptUrl," + //在线受理链接
									 "'' as creator," + //创建人
									 "null as created," + //创建时间
									 "'' as faq," + //问题和解答
									 "许可条件 as serviceCondition," + //申办条件
									 "许可依据 as serviceAccording," + //办理依据
									 "许可程序 as serviceProgram," + //办理程序
									 "承诺时限 as timeLimit," + //承诺时限
									 "收费依据 as chargeAccording," + //收费依据
									 "收费标准 as chargeStandard," + //收费标准
									 "'' as legalRight," + //法律权利,申请人法律权利及申诉途径
									 "联系地址 as address," + //办理地点
									 "'' as traffic," + //交通线路
									 "'' as transactor," + //经办人
									 "联系电话 as transactorTel," + //经办人联系电话
									 "受理单位 as unitName" + //办理机构名称
									 " from [all$]"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceDataImporter#generateListServiceItemMaterialsSQL(java.lang.String)
			 */
			public String generateListServiceItemMaterialsSQL(String importItemId) {
				return "select " +
					   "申请材料 as name," + //申报材料名称
					   "'' as description," + //申报说明
					   "'' as tableName," + //表格名称
					   "'' as tableURL," + //表格URL
					   "'' as exampleURL" + //样表URL
					   " from [all$]" +
					   " where 事项名称='" + importItemId + "'";
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter#generateListServiceItemDownloadsSQL(java.lang.String)
			 */
			public String generateListServiceItemDownloadsSQL(String importItemId) {
				return null;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter#listServiceItemDownloads(long, java.lang.String, java.sql.Connection)
			 */
			public List listServiceItemDownloads(final long localItemId, String importItemId, Connection connection) throws Exception {
				final List downloads = new ArrayList();
				final AttachmentService siteAttachmentService = (AttachmentService)Environment.getService("siteAttachmentService");
				//从行政服务中心抓取表格
				final String[] exampleWords = {"范本", "范例", "样表", "填写说明", "样张"};
                FileUtils.fileSearch(Environment.getWebinfPath() + "中心窗口各单位审批事项相关表格", importItemId, new FileSearchCallback() {
					public void onFileFound(File file) {
						File[] tables = file.listFiles();
						for(int i=0; i<(tables==null ? 0 : tables.length); i++) {
							if(!tables[i].isFile()) {
								continue;
							}
							try {
								siteAttachmentService.uploadFile("cms/onlineservice", "attachments", null, localItemId, tables[i].getPath());
								String url = siteAttachmentService.createDownload("cms/onlineservice", "attachments", localItemId, tables[i].getName(), false, null);
								OnlineServiceItemMaterial material = new OnlineServiceItemMaterial();
								int index = tables[i].getName().lastIndexOf('.');
								material.setTableName(index==-1 ? tables[i].getName() : tables[i].getName().substring(0, index)); //表格名称
								int j = exampleWords.length-1;
								for(; j>=0 && tables[i].getName().indexOf(exampleWords[j])==-1; j--);
								if(j>=0) {
									material.setExampleURL(url); //样表URL
								}
								else {
									material.setTableURL(url); //表格URL
								}
			    	        	downloads.add(material);	
							}
							catch(Exception e) {
								e.printStackTrace();
							}
						}
					}
                });
            	return downloads.isEmpty() ? null : downloads;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter#getSameNameDirectory(java.lang.String, long)
			 */
			protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
				return null;
			}
		});
		return dataImporters;
	}
}