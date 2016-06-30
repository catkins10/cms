package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 网上办事申报材料和表格导入
 * @author linchuan
 *
 */
public class MaterialImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "网上办事申报材料和表格导入";
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
				return "表格";
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
				String hql = "select OnlineServiceItem.id from OnlineServiceItem OnlineServiceItem";
				List itemIds = databaseService.findRecordsByHql(hql);
				for(Iterator iterator = itemIds.iterator(); iterator.hasNext();) {
					try {
						long itemId = ((Number)iterator.next()).longValue();
						OnlineServiceItem item = (OnlineServiceItem)onlineServiceItemService.load(OnlineServiceItem.class, itemId);
						if(item.getCode()==null || item.getCode().isEmpty()) {
							continue;
						}
						//解析页面获取样表
						String sql = "select serv_id from t_service where serv_no=" + item.getCode() + "";
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery(sql);
						String remoteRecordId = null;
						if(rs.next()) {
							remoteRecordId = rs.getString("serv_id");
						}
						rs.close();
						statement.close();
						if(remoteRecordId!=null && !remoteRecordId.isEmpty()) {
							//删除原来的材料
							for(Iterator iteratorMaterial = (item.getMaterials()==null ? null : item.getMaterials().iterator()); iteratorMaterial!=null && iteratorMaterial.hasNext();) {
								OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)iteratorMaterial.next();
								databaseService.deleteRecord(material);
								iteratorMaterial.remove();
							}
							if(item.getMaterials()==null) {
								item.setMaterials(new HashSet());
							}
							statement = connection.createStatement();
							sql = "select " +
							   "mItem_name as name," + //申报材料名称
							   "'' as description," + //申报说明
							   "'' as tableName," + //表格名称
							   "'' as tableURL," + //表格URL
							   "'' as exampleURL" + //样表URL
							   " from t_materialItem" +
							   " where mItem_serv_id=" + remoteRecordId;
							rs = statement.executeQuery(sql);
							while(rs.next()) {
								OnlineServiceItemMaterial material = new OnlineServiceItemMaterial();
								material.setId(UUIDLongGenerator.generateId());
								material.setItemId(item.getId()); //办理事项ID
								material.setName(rs.getString("name")); //申报材料名称
								//private String description; //申报说明
								//private float priority; //优先级
								//private String tableName; //表格名称
								//private String tableURL; //表格URL
								//private String exampleURL; //样表URL
								databaseService.saveRecord(material);
								item.getMaterials().add(material);
							}
							rs.close();
							statement.close();
							
							List downloads = listServiceItemDownloads(remoteRecordId, connection);
							if(downloads!=null && !downloads.isEmpty()) {
								HashSet itemDownloads = new HashSet();
								for(Iterator iteratorDownload = downloads.iterator(); iteratorDownload.hasNext();) {
									OnlineServiceItemMaterial download = (OnlineServiceItemMaterial)iteratorDownload.next();
									if(download.getTableName().indexOf("范本")==-1 && download.getTableName().indexOf("表样")==-1 && download.getTableName().indexOf("样表")==-1 && download.getTableName().indexOf("样本")==-1) {
										System.out.println("***********download table:" + download.getTableName() + "," + download.getTableURL());
										OnlineServiceItemMaterial material = new OnlineServiceItemMaterial();
										material.setId(UUIDLongGenerator.generateId());
										material.setItemId(itemId);
					    	        	//删除表格名称中的后缀名
										material.setTableName(resetTableName(download.getTableName()));
										material.setTableURL(downloadAttachment(download.getTableURL(), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", material.getItemId()));
										databaseService.saveRecord(material);
										item.getMaterials().add(material);
										itemDownloads.add(material);
									}
								}
								for(Iterator iteratorDownload = downloads.iterator(); iteratorDownload.hasNext();) {
									OnlineServiceItemMaterial download = (OnlineServiceItemMaterial)iteratorDownload.next();
									if(download.getTableName().indexOf("范本")!=-1 || download.getTableName().indexOf("表样")!=-1 || download.getTableName().indexOf("样表")!=-1 || download.getTableName().indexOf("样本")!=-1) {
										String exampleName = resetTableName(download.getTableName());
										OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)ListUtils.findObjectByProperty(itemDownloads, "tableName", exampleName);
										System.out.println("***********download example:" + download.getTableName() + "," + download.getTableURL());
										if(material==null) {
											material = new OnlineServiceItemMaterial();
											material.setId(UUIDLongGenerator.generateId());
											material.setItemId(itemId);
						    	        	//删除表格名称中的后缀名
											material.setTableName(exampleName);
											material.setExampleURL(downloadAttachment(download.getTableURL(), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", material.getItemId()));
											databaseService.saveRecord(material);
											item.getMaterials().add(material);
											itemDownloads.add(material);
										}
										else {
											material.setExampleURL(downloadAttachment(download.getTableURL(), parameter, "siteAttachmentService", "cms/onlineservice", "attachments", material.getItemId()));
											databaseService.updateRecord(material);
										}
									}
								}
							}
							//更新事项记录,以重建静态页面
							onlineServiceItemService.update(item);
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			private String resetTableName(String tableName) {
				int index = tableName.lastIndexOf('.');
				if(index!=-1) {
					tableName = tableName.substring(0, index);
				}
				return tableName.replace("空表", "").replace("表样", "").replace("样表", "").replace("样本", "").replace("范本", "").replace("()", "").replace("（）", "").replace("(）", "").replace("（)", "");
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