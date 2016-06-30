package com.yuanluesoft.jeaf.dataimport.services.yunxiao;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.opinion.dataimporter.PublicOpinionImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 云霄信息公开
 * @author linchuan
 *
 */
public class PublicInfoImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "云霄信息公开";
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
					return "select fid as directoryId," +
						   " LMname as directoryName" +
						   " from LMtab";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " id as importDataId," + //被导入的记录ID
					   " LMid as directoryId," + //原来的栏目ID
					   " title as subject," + //标题
					   " content as body," + //正文
					   " indexno as infoIndex," + //索引号
					   " (select pname from gv_part where pid=newsTab.pid) as infoFrom," + //发布机构
					   " wh as mark," + //文号
					   " indate1 as generateDate," + //生成日期
					   " llgs as summarize," + //内容概述
					   " '　' as category," + //主题分类
					   " keywords," + //关键字
					   " indate as created," + //创建时间
					   " indate as issueTime," + //发布时间
					   " '管理员' as creator," + //创建者
					   " (select pname from gv_part where pid=newsTab.pid) as orgName," + //创建者所在部门名称
					   " (select pname from gv_part where pid=newsTab.pid) as unitName" + //创建者所在单位名称
					   " from newsTab"};
			}
		});
				
		//信息公开意见箱
		dataImporters.add(new PublicOpinionImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
					"select " +
					"ID as importDataId," + //被导入的记录ID
					"'' as sn," + //编号,在所有的公众服务中唯一
					"'' as queryPassword," + //查询密码
					"title as subject," + //主题
					"cont as content," + //正文
					"name as creator," + //创建人姓名
					"intime as created," + //创建时间
					"tel as creatorTel," + //联系电话
					"email as creatorMail," + //邮箱
					"'M' as creatorSex," + //性别 M/F
					"'' as creatorCertificateName," + //创建人证件名称
					"'' as creatorIdentityCard," + //创建人身份证/证件号码
					"inip as creatorIP," + //创建人IP
					"'' as creatorMobile," + //创建人手机
					"'' as creatorFax," + //创建人传真
					"'' as creatorUnit," + //创建人所在单位
					"'' as creatorJob," + //创建人职业
					"address as creatorAddress," + //创建人地址
					"'' as creatorPostalcode," + //创建人邮编
					"'1' as isPublic," + //是否允许公开
					"'0' as publicPass," + //是否公开到网站
					"'0' as publicBody," + //是否公开正文
					"'0' as publicWorkflow," + //是否公开办理流程
					"null as publicSubject," + //公开时的主题
					"'' as remark," + //附注
					"null as opinionTime," + //办理意见填写时间
					"'' as opinionBody," + //办理意见填写内容
					"'' as opinionCreator" + //办理意见填写人
					" from zfgk_yj"
				};
			}
		});
		return dataImporters;
	}
}