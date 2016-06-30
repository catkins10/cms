package com.yuanluesoft.jeaf.dataimport.services.pinghe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 平和县部门网站
 * @author linchuan
 *
 */
public class DataImportServiceDeptImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "平和县部门网站";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		/*/站点数据导入
		dataImporters.add(new SiteDataImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select ClassID as directoryId," +
						   " ClassName as directoryName" +
						   " from Cl_Class" +
						   " where ParentID=0";
				}
				else { //子目录
					return "select ClassID as directoryId," +
						   " ClassName as directoryName" +
						   " from Cl_Class" +
						   " where ParentID=" + parentDirectoryId;
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						   "select " +
						   " InfoID as importDataId," + //被导入的记录ID
						   " ClassID as directoryId," + //原来的栏目ID
						   " Title as subject," + //标题
						   " Content as body," + //正文
						   " '' as subhead," + //副标题
						   " CopyFrom as Source," + //来源
						   " Author," + //作者
						   " Keyword," + //关键字
						   " UpdateTime as created," + //创建时间
						   " UpdateTime as issueTime," + //发布时间
						   " Editor," + //创建者
						   " '' as orgName," + //创建者所在部门名称
						   " '' as unitName," + //创建者所在单位名称
						   " status" + //状态
						   " from Cl_Article" +
		   			   	   " where ClassID in (" + ListUtils.join(selectedSourceDirectoryIds, ",") + ")" +
		   			   	   " and Deleted=false"};
			}

			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				SiteResource resource = (SiteResource)bean;
				resource.setStatus(resource.getStatus()=='1' ? SiteResourceService.RESOURCE_STATUS_ISSUE : SiteResourceService.RESOURCE_STATUS_UNISSUE); //-1=退稿 0=未审 1=已审
			}
		});*/
		
		//政府信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select ClassID as directoryId," +
						   " ClassName as directoryName" +
						   " from Cl_Class" +
						   " where ParentID=0" +
						   " and ClassName<>'政府信息公开制度规定'" +
						   " and ChannelID=(select ChannelID from Cl_Channel where ChannelName='政府信息公开')";
				}
				else { //子目录
					return "select ClassID as directoryId," +
						   " ClassName as directoryName" +
						   " from Cl_Class" +
						   " where ParentID=" + parentDirectoryId;
				}
			}

			protected String getDirectoryCode(String sourceDirectoryId, String sourceDirectoryName, Connection connection) throws Exception {
				sourceDirectoryName = sourceDirectoryName.trim();
				if("单位概况".equals(sourceDirectoryName)) {
					return "01";
				}
				else if("其它公开信息".equals(sourceDirectoryName)) {
					return "30";
				}
				
				//检查是否二级目录
				String sql = "select ParentDir from Cl_Class where ClassID=" + sourceDirectoryId;
				Logger.info(sql);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				boolean secondLevel = (resultSet.next() && "/".equals(resultSet.getString("ParentDir")));
				resultSet.close();
				
				//检查是否末级目录
				sql = "select ClassID from Cl_Class where ParentID=" + sourceDirectoryId;
				Logger.info(sql);
				resultSet = statement.executeQuery(sql);
				boolean lastLevel = !resultSet.next();
				resultSet.close();
				
				//根据目录下的信息来设置编号
				sql = "select Keyword" +
					  " from Cl_Article" +
					  " where Keyword like '%-%'" +
					  " and (Keyword like 'ZZ08%' or Keyword like 'zz08%')" +
					  " and (ClassID=" + sourceDirectoryId +
					  "  or ClassID in (select ClassID from Cl_Class where ParentID=" + sourceDirectoryId + "))";
				Logger.info(sql);
				resultSet = statement.executeQuery(sql);
				if(resultSet.next()) {
					String code = resultSet.getString("Keyword");
					if(code!=null && !code.isEmpty()) {
						code = code.replaceAll("－", "-").split("-")[1].trim();
						if(secondLevel && lastLevel) { //第二级、也是最后一级
							return code;
						}
						else if(secondLevel) { //第二级、不是最后一级
							return code.substring(0, 2);
						}
						else { //最后一级
							return code.substring(2);
						}
					}
				}
				resultSet.close();
				
				sql = "select ClassID" +
					  " from Cl_Class" +
					  " where ParentID=(select ParentID from Cl_Class where ClassID=" + sourceDirectoryId + ")" +
					  " order by OrderID, ClassID";
				Logger.info(sql);
				resultSet = statement.executeQuery(sql);
				int code = secondLevel ? 15 : 0;
				while(resultSet.next()) {
					code++;
					if(sourceDirectoryId.equals(resultSet.getString("ClassID"))) {
						break;
					}
				}
				resultSet.close();
				statement.close();
				return (code<10 ? "0" : "") + code;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						   "select " +
						   " InfoID as importDataId," + //被导入的记录ID
						   " ClassID as directoryId," + //原来的栏目ID
						   " Title as subject," + //标题
						   " Content as body," + //正文
						   " Keyword as infoIndex," + //索引号
						   " Author as infoFrom," + //发布机构
						   " CopyFrom as mark," + //文号
						   " UpdateTime as generateDate," + //生成日期
						   " Intro as summarize," + //内容概述
						   " '　' as category," + //主题分类
						   " '' as keywords," + //关键字
						   " CensorTime as created," + //创建时间
						   " CensorTime as issueTime," + //发布时间
						   " Editor as creator," + //创建者
						   " Author as orgName," + //创建者所在部门名称
						   " Author as unitName," + //创建者所在单位名称
						   " status" + //状态
						   " from Cl_Article" +
		   			   	   " where ClassID in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(selectedSourceDirectoryIds, ",", false)) + ")" +
						   " and Deleted=false"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				PublicInfo info = (PublicInfo)bean;
				info.setStatus(info.getStatus()=='1' ? PublicInfoService.INFO_STATUS_ISSUE : PublicInfoService.INFO_STATUS_UNISSUE); //-1=退稿 0=未审 1=已审
			}
		});
		return dataImporters;
	}
}