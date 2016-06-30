package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.HttpUtils;

/**
 * 网上办事样表导入
 * @author linchuan
 *
 */
public class ExampleImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "网上办事样表导入";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		
		//网上办事样表导入
		dataImporters.add(new DataImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return null;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#getImportDataName()
			 */
			public String getImportDataName() {
				return "样表";
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#saveImportedData(java.sql.ResultSet, com.yuanluesoft.cms.sitemanage.pojo.WebSite, java.sql.Connection, java.lang.String, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter, java.lang.String, long, boolean)
			 */
			protected long saveImportedData(ResultSet resultSet, WebSite targetSite, Connection connection, String dataImportServiceClass, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
				return 0;
			}
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#importData(long, java.lang.String, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter, boolean)
			 */
			public void importData(long targetSiteId, String dataImportServiceClass, Connection connection, DataImportParameter parameter, boolean sameSystem) throws Exception {
				parameter.setSourceSiteURL("http://www.zzbm.org/");
				DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
				OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)Environment.getService("onlineServiceItemService");
				String hql = "from OnlineServiceItemMaterial OnlineServiceItemMaterial" +
							 " where OnlineServiceItemMaterial.tableURL is not null" +
							 " and OnlineServiceItemMaterial.tableURL!=''" +
							 " and (OnlineServiceItemMaterial.exampleURL is null or OnlineServiceItemMaterial.exampleURL='')";
				List materials = databaseService.findRecordsByHql(hql);
				for(Iterator iterator = materials.iterator(); iterator.hasNext();) {
					try {
						OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)iterator.next();
						//如果下载URL包含“zzbm.org”重新下载
						if(material.getTableURL().indexOf("zzbm.org")!=-1) {
							material.setTableURL(downloadAttachment(material.getTableURL(), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", material.getItemId()));
						}
						//删除表格名称中的后缀名
						int index = material.getTableName().lastIndexOf('.');
						if(index!=-1) {
							material.setTableName(material.getTableName().substring(0, index));
						}
						//解析页面获取样表
						hql = "select ImportedRecord.remoteRecordId from ImportedRecord ImportedRecord where ImportedRecord.localRecordId=" + material.getItemId();
						String remoteRecordId = (String)databaseService.findRecordByHql(hql);
						if(remoteRecordId!=null && !remoteRecordId.isEmpty()) {
							List downloads = listServiceItemDownloads(remoteRecordId, connection);
							if(downloads!=null && !downloads.isEmpty()) {
								for(Iterator iteratorDownload = downloads.iterator(); iteratorDownload.hasNext();) {
									OnlineServiceItemMaterial download = (OnlineServiceItemMaterial)iteratorDownload.next();
									if(download.getTableName().indexOf("范本")!=-1 && download.getTableName().indexOf(material.getTableName())!=-1) {
										System.out.println("***********download example:" + download.getTableName() + "," + download.getTableURL());
										material.setExampleURL(downloadAttachment(download.getTableURL(), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", material.getItemId()));
										break;
									}
								}
							}
						}
						//更新表格记录
						databaseService.updateRecord(material);
						//更新事项记录,以重建静态页面
						onlineServiceItemService.update(onlineServiceItemService.load(OnlineServiceItem.class, material.getItemId()));
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.dataimporter.OnlineServiceDataImporter#listServiceItemDownloads(java.lang.String, java.sql.Connection)
			 */
			public List listServiceItemDownloads(String importItemId, Connection connection) throws Exception {
				//从行政服务中心抓取表格
				String url = "http://www.zzbm.org/bsjg_show.asp?id=" + importItemId;
				System.out.println("**********parse page " + url);
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