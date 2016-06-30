package com.yuanluesoft.jeaf.dataimport.services.changtai;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.opinion.dataimporter.PublicOpinionImporter;
import com.yuanluesoft.cms.infopublic.request.dataimporter.PublicRequestImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 长泰信息公开
 * @author linchuan
 *
 */
public class PublicInfoImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "长泰县政府信息公开";
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
			
		//依申请公开
		dataImporters.add(new PublicRequestImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						 "select " +
						 "ID as importDataId," + //被导入的记录ID
						 "lsh as sn," + //编号,在所有的公众服务中唯一
						 "qpassword as queryPassword," + //查询密码
						 "iif(sqtype='公民', '0', '1') as proposerType," + //申请人类型,0|公民,1|法人/其他组织
						 "name as creator," + //申请人/单位联系人姓名
						 "textfield1 as creatorUnit," + //单位名称
						 "textfield2 as creatorCertificateName," + //创建人证件名称
						 "textfield3 as creatorIdentityCard," + //创建人身份证/证件号码
						 "textfield4 as creatorTel," + //联系电话
						 "'' as creatorMobile," + //手机
						 "textfield5 as creatorFax," + //传真
						 "textfield6 as creatorAddress," + //地址
						 "textfield8 as creatorPostalcode," + //邮编
						 "textfield7 as creatorMail," + //邮箱
						 "'M' as creatorSex," + //申请人性别 M/F
						 "inip as creatorIP," + //申请人IP
						 "null as creatorJob," + //申请人职业
						 "intime as created," + //申请时间
						 "'网站' as applyMode," + //申请方式,默认“网站”
						 "null as code," + //机构代码
						 "null as legalRepresentative," + //法人代表
						 "textarea as subject," + //内容描述
						 "textarea1 as purpose," + //用途
						 "null as unit," + //涉及单位
						 "dgtype as medium," + //介质,纸面/电子邮件/光盘/磁盘
						 "hqtype as receiveMode," + //获取信息的方式,邮寄/快递/电子邮件/传真/自行领取/当场阅读、抄录
						 "replaytype as approvalResult," + //审批结果,同意公开/同意部分公开/不公开
						 "null as notPublicType," + //不公开类别,信息不存在/非本部门掌握/申请内容不明确/免予公开/其他原因未能提供信息
						 "null as notPublicReason," + //其他原因说明
						 "null as doneMedium," + //实际提供介质
						 "null as doneReceiveMode," + //实际提供方式
						 "'0' as isPublic," + //是否允许公开
						 "'0' as publicPass," + //是否公开到网站
						 "'0' as publicBody," + //是否公开正文
						 "'0' as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "null as remark," + //附注
						 "rptime as opinionTime," + //办理意见填写时间
						 "null as opinionBody," + //办理意见填写内容
						 "replaypeople as opinionCreator" + //办理意见填写人
						 " from zfgk_ysqgk"
				};
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