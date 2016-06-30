package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 漳州市政府网上办事
 * @author linchuan
 *
 */
public class OnlineServiceImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "漳州市政府网上办事";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#importData(long, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
	 */
	public void importData(long targetSiteId, DataImportParameter parameter) throws Exception {
		//仅更新窗口编码
		Connection connection = createConnection(parameter);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select Depa_Name, Depa_No from t_department");
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)Environment.getService("onlineServiceDirectoryService");
		while(resultSet.next()) {
			String deptName = resultSet.getString("Depa_Name");
			String deptNo = resultSet.getString("Depa_No");
			//获取
			OnlineServiceDirectory directory = (OnlineServiceDirectory)onlineServiceDirectoryService.getDirectoryByName(20229309033290000l, deptName, false);
			if(directory!=null) {
				directory.setRemark("窗口编号:" + deptNo);
				databaseService.updateRecord(directory);
			}
		}
		resultSet.close();
		statement.close();
		connection.close();
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
					return "select Depa_No as directoryId," +
						   " Depa_Name as directoryName" +
						   " from t_department" +
						   " order by Depa_Name";
				}
				return null;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
									 "serv_id as importDataId," + //被导入的记录ID
									 "serv_Depa_No as directoryId," + //原来的目录ID
									 "serv_no as code," + //编码
									 "serv_name as name," + //名称
									 "serv_prop as itemType," + //审批服务类型
									 "'http://www.zzbm.org/' as acceptUrl," + //在线受理链接
									 "'' as creator," + //创建人
									 "null as created," + //创建时间
									 "'' as faq," + //问题和解答
									 "'' as serviceCondition," + //申办条件
									 "serv_PriceRule as serviceAccording," + //办理依据
									 "serv_flow as serviceProgram," + //办理程序
									 "serv_TimeLimitText as timeLimit," + //承诺时限
									 "'' as chargeAccording," + //收费依据
									 "serv_PriceText as chargeStandard," + //收费标准
									 "'' as legalRight," + //法律权利,申请人法律权利及申诉途径
									 "'漳州市行政服务中心' as address," + //办理地点
									 "'' as traffic," + //交通线路
									 "'' as transactor," + //经办人
									 "'' as transactorTel," + //经办人联系电话
									 "(select Depa_Name from t_department where t_department.Depa_No=t_service.serv_Depa_No) as unitName" + //办理机构名称
									 " from t_service"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceDataImporter#generateListServiceItemMaterialsSQL(java.lang.String)
			 */
			public String generateListServiceItemMaterialsSQL(String importItemId) {
				return "select " +
					   "mItem_name as name," + //申报材料名称
					   "'' as description," + //申报说明
					   "'' as tableName," + //表格名称
					   "'' as tableURL," + //表格URL
					   "'' as exampleURL" + //样表URL
					   " from t_materialItem" +
					   " where mItem_serv_id=" + importItemId;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceDataImporter#generateListServiceItemDownloadsSQL(java.lang.String)
			 */
			public String generateListServiceItemDownloadsSQL(String importItemId) {
				/*return "select " +
				   	   "file_name as tableName," + //表格名称
				   	   "file_id as tableURL," + //表格URL
				   	   "'' as exampleURL" + //样表URL
				   	   " from down_table" +
				   	   " where serv_id=" + importItemId;*/
				return null;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceDataImporter#listServiceItemDownloads(java.lang.String, java.sql.Connection)
			 */
			public List listServiceItemDownloads(long localItemId, String importItemId, Connection connection) throws Exception {
				//从行政服务中心抓取表格
				String url = "http://www.zzbm.org/bsjg_show.asp?id=" + importItemId;
				String htmlContent = HttpUtils.getHttpContent(url, null, true, null, 60000).getResponseBody();
				int index = htmlContent.indexOf("<td><strong>表格下载</strong></td>");
                if(index==-1) {
                	return null;
                }
                index += "<td><strong>表格下载</strong></td>".length();
                int endIndex = htmlContent.indexOf("</td>", index);
                Pattern pattern = Pattern.compile("<a href=\"([^\"]*)\"[^>]*>([^<]*)</a>");
    	        Matcher matcher = pattern.matcher(htmlContent.substring(index, endIndex));
                List downloads = new ArrayList();
    	        while(matcher.find()) {
    	        	OnlineServiceItemMaterial material = new OnlineServiceItemMaterial();
    	        	material.setTableName(matcher.group(2)); //表格名称
    	        	material.setTableURL("http://www.zzbm.org/" + matcher.group(1)); //表格URL
    	        	downloads.add(material);
    	        }
            	return downloads.isEmpty() ? null : downloads;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter#saveImportData(java.sql.ResultSet, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.WebSite, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter, java.lang.String, long, boolean)
			 */
			protected long saveImportData(ResultSet resultSet, String mappingDirectoryIds, WebSite targetSite, Connection connection, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
				//只更新办理事项编码
				DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
				OnlineServiceItem item = null;
				if(importedRecordId<=0) {
					String name = resultSet.getString("name");
					List items = databaseService.findRecordsByHql("from OnlineServiceItem OnlineServiceItem where OnlineServiceItem.name='" + JdbcUtils.resetQuot(name) + "'", 0, 2);
					if(items!=null && items.size()==1) {
						item = (OnlineServiceItem)items.get(0);
					}
				}
				else {
					item = (OnlineServiceItem)databaseService.findRecordById(OnlineServiceItem.class.getName(), importedRecordId, null);
				}
				if(item!=null && (item.getCode()==null || item.getCode().isEmpty())) {
					item.setCode(resultSet.getString("code"));
					databaseService.updateRecord(item);
				}
				return importedRecordId;
			}
		});
		return dataImporters;
	}
}