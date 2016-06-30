package com.yuanluesoft.cms.infopublic.request.dataimporter;

import java.sql.Connection;
import java.sql.ResultSet;

import com.yuanluesoft.cms.infopublic.request.pojo.PublicRequest;
import com.yuanluesoft.cms.publicservice.pojo.PublicServicePrivilege;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.opinionmanage.service.OpinionService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 信息公开意见箱导入,SQL格式如下:
 "select " +
 " as importDataId," + //被导入的记录ID
 " as sn," + //编号,在所有的公众服务中唯一
 " as queryPassword," + //查询密码
 " as proposerType," + //申请人类型,0|公民,1|法人/其他组织
 " as creator," + //申请人/单位联系人姓名
 " as creatorUnit," + //单位名称
 " as creatorCertificateName," + //创建人证件名称
 " as creatorIdentityCard," + //创建人身份证/证件号码
 " as creatorTel," + //联系电话
 " as creatorMobile," + //手机
 " as creatorFax," + //传真
 " as creatorAddress," + //地址
 " as creatorPostalcode," + //邮编
 " as creatorMail," + //邮箱
 " as creatorSex," + //申请人性别 M/F
 " as creatorIP," + //申请人IP
 " as creatorJob," + //申请人职业
 " as created," + //申请时间
 " as applyMode," + //申请方式,默认“网站”
 " as code," + //机构代码
 " as legalRepresentative," + //法人代表
 " as subject," + //内容描述
 " as purpose," + //用途
 " as unit," + //涉及单位
 " as medium," + //介质,纸面/电子邮件/光盘/磁盘
 " as receiveMode," + //获取信息的方式,邮寄/快递/电子邮件/传真/自行领取/当场阅读、抄录
 " as approvalResult," + //审批结果,同意公开/同意部分公开/不公开
 " as notPublicType," + //不公开类别,信息不存在/非本部门掌握/申请内容不明确/免予公开/其他原因未能提供信息
 " as notPublicReason," + //其他原因说明
 " as doneMedium," + //实际提供介质
 " as doneReceiveMode," + //实际提供方式
 " as isPublic," + //是否允许公开
 " as publicPass," + //是否公开到网站
 " as publicBody," + //是否公开正文
 " as publicWorkflow," + //是否公开办理流程
 " as publicSubject," + //公开时的主题
 " as remark," + //附注
 " as opinionTime," + //办理意见填写时间
 " as opinionBody," + //办理意见填写内容
 " as opinionCreator" + //办理意见填写人
 " from [原表名]";
 * @author linchuan
 *
 */
public abstract class PublicRequestImporter extends DataImporter {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#getImportDataName()
	 */
	public String getImportDataName() {
		return "依申请公开";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#saveImportedData(com.yuanluesoft.jeaf.dataimport.dataimporter.callback.DataImporterCallback, java.sql.ResultSet)
	 */
	protected long saveImportedData(ResultSet resultSet, WebSite targetSite, Connection connection, String dataImportServiceClass, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		PublicService publicService = (PublicService)Environment.getService("publicService");
		if(importedRecordId>0 && databaseService.findRecordById(PublicRequest.class.getName(), importedRecordId)!=null) { //检查是否原来导入的记录是否存在
			return importedRecordId;
		}
		
		PublicRequest publicRequest = new PublicRequest();
		JdbcUtils.copyFields(publicRequest, resultSet);
		if(publicRequest.getMedium()!=null && publicRequest.getMedium().endsWith(",")) {
			publicRequest.setMedium(publicRequest.getMedium().substring(0, publicRequest.getMedium().length()-1));
		}
		if(publicRequest.getReceiveMode()!=null && publicRequest.getReceiveMode().endsWith(",")) {
			publicRequest.setReceiveMode(publicRequest.getReceiveMode().substring(0, publicRequest.getReceiveMode().length()-1));
		}
		if(publicRequest.getDoneMedium()!=null && publicRequest.getDoneMedium().endsWith(",")) {
			publicRequest.setDoneMedium(publicRequest.getDoneMedium().substring(0, publicRequest.getDoneMedium().length()-1));
		}
		if(publicRequest.getDoneReceiveMode()!=null && publicRequest.getDoneReceiveMode().endsWith(",")) {
			publicRequest.setDoneReceiveMode(publicRequest.getDoneReceiveMode().substring(0, publicRequest.getDoneReceiveMode().length()-1));
		}
		afterPojoGenerated(publicRequest, resultSet, connection); //回调
		publicRequest.setId(UUIDLongGenerator.generateId()); //ID
		publicRequest.setSiteId(targetSite.getId());
		publicService.save(publicRequest);
		
		//创建权限记录
		cloneSitePrivileges(PublicServicePrivilege.class, publicRequest.getId(), targetSite, databaseService);
		
		//保存办理意见
		String opinionContent = StringUtils.filterHtmlElement(JdbcUtils.getString(resultSet, "opinionBody"), false);
		if(opinionContent!=null && !opinionContent.equals("")) {
			OpinionService opinionService = (OpinionService)Environment.getService("opinionService");
			opinionService.saveOpinion(PublicRequest.class.getName(), 0, publicRequest.getId(), opinionContent, "部门办理", 0, JdbcUtils.getString(resultSet, "opinionCreator"), 0, null, null, null, null, JdbcUtils.getTimestamp(resultSet, "opinionTime"));
		}
		return publicRequest.getId();
	}
}