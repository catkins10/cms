package com.yuanluesoft.jeaf.dataimport.services.longhai;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceImporter;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.HttpUtils;

/**
 * 龙海市政府网上办事
 * @author linchuan
 *
 */
public class OnlineServiceImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "龙海市政府网上办事";
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
									 "'http://sc.longhai.gov.cn/' as acceptUrl," + //在线受理链接
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
									 "'龙海市行政服务中心' as address," + //办理地点
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
				if(true) {
					return null;
				}
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
		});
		return dataImporters;
	}
}