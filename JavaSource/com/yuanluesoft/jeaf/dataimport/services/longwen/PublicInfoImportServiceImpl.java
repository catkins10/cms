package com.yuanluesoft.jeaf.dataimport.services.longwen;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 龙文区信息公开导入
 * @author linchuan
 *
 */
public class PublicInfoImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "龙文区信息公开导入";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		
		//信息公开数据导入
		dataImporters.add(
			new PublicInfoImporter() {
				public String generateListChildDirectoriesSQL(String parentDirectoryId) {
					if(parentDirectoryId==null) { //第一级目录
						return "select c_id as directoryId," +
							   " ClassName as directoryName" +
							   " from Zfxxgk_Class" +
							   " where f_id=0";
					}
					else { //其他目录
							return "select c_id as directoryId," +
							   " ClassName as directoryName" +
							   " from Zfxxgk_Class" +
							   " where f_id=" + parentDirectoryId +
							   " order by ClassName";
					}
				}
				
				public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
					return new String[] {"select " +
							   "GOVINFOID as importDataId," + //被导入的记录ID
							   "INFOCATALOG as directoryId," + //原来的栏目ID
							   "IDXID as infoIndex," + //索引号
							   "PUBDEPART as infoFrom," + //发布机构
							   "FILENUM as mark," + //文号
							   "CRTIME as generateDate," + //生成日期
							   "TITLE as subject," + //标题
							   "DOCUMENT as body," + //正文
							   "(select Uname from Admin where Admin.Admin_id=val(Zfxxgk_Article.Author)) as creator," + //创建人
							   "PUBDATE as created," + //创建时间
							   "PUBDATE as issueTime," + //发布时间
							   "nrgy as summarize," + //内容概述
							   "'' as category," + //主题分类
							   "ztc as keywords," + //主题词
							   "PUBDEPART as orgName," + //创建者所在部门名称
							   "PUBDEPART as  unitName" + //创建者所在单位名称
							   " from Zfxxgk_Article"};
				}
				
				/*public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
					return new String[] {"select " +
							   "articleId as importDataId," + //被导入的记录ID
							   "C_Id as directoryId," + //原来的栏目ID
							   "UCode as infoIndex," + //索引号
							   "Uname as infoFrom," + //发布机构
							   "bzwh as mark," + //文号
							   "AddTime as generateDate," + //生成日期
							   "title as subject," + //标题
							   "content as body," + //正文
							   "(select Uname from Admin where Admin.Admin_id=val(Zfxxgk_Article.Author)) as creator," + //创建人
							   "SendTime as created," + //创建时间
							   "SendTime as issueTime," + //发布时间
							   "nrgy as summarize," + //内容概述
							   "'' as category," + //主题分类
							   "ztc as keywords," + //主题词
							   "Uname as orgName," + //创建者所在部门名称
							   "Uname as  unitName" + //创建者所在单位名称
							   " from Zfxxgk_Article"};
				}*/
			}
		);
		return dataImporters;
	}
}