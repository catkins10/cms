package com.yuanluesoft.jeaf.dataimport.services.zhaoan.dept;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 
 * @author linchuan
 *
 */
public class SiteImportServiceVillagesImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "诏安县秀篆镇站点导入";
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
						   " where zf11_big_id=0)" +
						   "union " +
						   "(select 100000 as directoryId," +
						   " '新闻' as directoryName" +
						   " from zf11_type" +
						   " where ID=1)" +
						   "union " +
						   "(select 100001 as directoryId," +
						   " '图片新闻' as directoryName" +
						   " from zf11_type" +
						   " where ID=1)";
				}
				else { //其他目录
					return "select ID as directoryId," +
						   " zf11_names as directoryName" +
						   " from zf11_type" +
						   " where zf11_big_id=" + parentDirectoryId;
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						"select " +
						 "100000+ID as importDataId," + //被导入的记录ID
						 "100000 as directoryId," + //原来的栏目ID
						 "Title as subject," + //标题
						 "Content as body," + //正文
						 "null as subhead," + //副标题
						 "null as source," + //来源
						 "null as author," + //作者
						 "null as keyword," + //关键字
						 "Time as created," + //创建时间
						 "Time as issueTime," + //发布时间
						 "null as editor," + //创建者
						 "'' as orgName," + //创建者所在部门名称
						 "'' as unitName" + //创建者所在单位名称
						 " from zf11_News",
						 
						 "select " +
						 "200000+id as importDataId," + //被导入的记录ID
						 "100001 as directoryId," + //原来的栏目ID
						 "pic_name as subject," + //标题
						 "'<center><img src=\"/' & pic_pic & '\"/><br>' & pic_desc & '</center>' as body," + //正文
						 "null as subhead," + //副标题
						 "null as source," + //来源
						 "null as author," + //作者
						 "null as keyword," + //关键字
						 "pic_date as created," + //创建时间
						 "pic_date as issueTime," + //发布时间
						 "null as editor," + //创建者
						 "'' as orgName," + //创建者所在部门名称
						 "'' as unitName" + //创建者所在单位名称
						 " from zf11_p_news",
						 
						 "select " +
						 "ID as importDataId," + //被导入的记录ID
						 "Sort1 as directoryId," + //原来的栏目ID
						 "Title as subject," + //标题
						 "Content as body," + //正文
						 "null as subhead," + //副标题
						 "null as source," + //来源
						 "null as author," + //作者
						 "null as keyword," + //关键字
						 "Date as created," + //创建时间
						 "Date as issueTime," + //发布时间
						 "null as editor," + //创建者
						 "'' as orgName," + //创建者所在部门名称
						 "'' as unitName" + //创建者所在单位名称
						 " from Info"
				};
			}
		});
		return dataImporters;
	}
}