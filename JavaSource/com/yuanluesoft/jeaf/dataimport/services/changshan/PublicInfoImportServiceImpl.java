package com.yuanluesoft.jeaf.dataimport.services.changshan;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 常山信息公开
 * @author linchuan
 *
 */
public class PublicInfoImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "常山信息公开";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//政府信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "(select class_id & '_' & menu_id as directoryId," +
						   " class_name as directoryName" +
						   " from v_class_ida" +
						   " order by class_name)" +
						   " union " +
						   "(select '_X05' as directoryId," +
						   " '年度报告' as directoryName" +
						   " from v_class_ida" +
						   " where class_id='0101'" +
						   ")" +
						   "union" +
						   "(select '_X06' as directoryId," +
						   " '申请流程' as directoryName" +
						   " from v_class_ida" +
						   " where class_id='0101'" +
						   ")" +
						   "union" +
						   "(select '_X07' as directoryId," +
						   " '受理单位' as directoryName" +
						   " from v_class_ida" +
						   " where class_id='0101'" +
						   ")" +
						   "union" +
						   "(select '_X02' as directoryId," +
						   " '信息公开指南' as directoryName" +
						   " from v_class_ida" +
						   " where class_id='0101'" +
						   ")" +
						   "union" +
						   "(select '_X03' as directoryId," +
						   " '信息公开规定' as directoryName" +
						   " from v_class_ida" +
						   " where class_id='0101'" +
						   ")";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " id as importDataId," + //被导入的记录ID
					   " class_id & '_' & menu_id as directoryId," + //原来的栏目ID
					   " titles as subject," + //标题
					   " contents as body," + //正文
					   " sn as infoIndex," + //索引号
					   " postdept as infoFrom," + //发布机构
					   " txtno as mark," + //文号
					   " posttime as generateDate," + //生成日期
					   " '' as summarize," + //内容概述
					   " '' as category," + //主题分类
					   " tkey as keywords," + //关键字
					   " posttime as created," + //创建时间
					   " posttime as issueTime," + //发布时间
					   " '' as creator," + //创建者
					   " postdept as orgName," + //创建者所在部门名称
					   " postdept as unitName," + //创建者所在单位名称
					   " iif(menu_id='X01', 0, 1) as type" +
					   " from v_contents" +
					   " where online=true"};
			}
		});
		return dataImporters;
	}
}