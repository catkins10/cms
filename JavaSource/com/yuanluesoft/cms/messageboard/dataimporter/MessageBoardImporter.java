package com.yuanluesoft.cms.messageboard.dataimporter;

import java.sql.Connection;
import java.sql.ResultSet;

import com.yuanluesoft.cms.messageboard.pojo.MessageBoard;
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
 * 留言板导入,SQL格式如下:
 "select " +
 " as importDataId," + //被导入的记录ID
 " as sn," + //编号,在所有的公众服务中唯一
 " as queryPassword," + //查询密码
 " as subject," + //主题
 " as content," + //正文
 " as creator," + //创建人姓名
 " as created," + //创建时间
 " as creatorTel," + //联系电话
 " as creatorMail," + //邮箱
 " as creatorSex," + //性别 M/F
 " as creatorCertificateName," + //创建人证件名称
 " as creatorIdentityCard," + //创建人身份证/证件号码
 " as creatorIP," + //创建人IP
 " as creatorMobile," + //创建人手机
 " as creatorFax," + //创建人传真
 " as creatorUnit," + //创建人所在单位
 " as creatorJob," + //创建人职业
 " as creatorAddress," + //创建人地址
 " as creatorPostalcode," + //创建人邮编
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
public abstract class MessageBoardImporter extends DataImporter {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#getImportDataName()
	 */
	public String getImportDataName() {
		return "留言";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#saveImportedData(com.yuanluesoft.jeaf.dataimport.dataimporter.callback.DataImporterCallback, java.sql.ResultSet)
	 */
	protected long saveImportedData(ResultSet resultSet, WebSite targetSite, Connection connection, String dataImportServiceClass, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		PublicService publicService = (PublicService)Environment.getService("publicService");
		if(importedRecordId>0 && databaseService.findRecordById(MessageBoard.class.getName(), importedRecordId)!=null) { //检查是否原来导入的记录是否存在
			return importedRecordId;
		}
		
		MessageBoard messageBoard = new MessageBoard();
		JdbcUtils.copyFields(messageBoard, resultSet);
		afterPojoGenerated(messageBoard, resultSet, connection); //回调
		
		messageBoard.setId(UUIDLongGenerator.generateId()); //ID
		messageBoard.setSubject(StringUtils.filterHtmlElement(messageBoard.getSubject(), false));
		messageBoard.setContent(StringUtils.filterHtmlElement(messageBoard.getContent(), false));
		messageBoard.setSiteId(targetSite.getId());
		publicService.save(messageBoard);
		
		//创建权限记录
		cloneSitePrivileges(PublicServicePrivilege.class, messageBoard.getId(), targetSite, databaseService);
		
		//保存办理意见
		String opinionContent = StringUtils.filterHtmlElement(JdbcUtils.getString(resultSet, "opinionBody"), false);
		if(opinionContent!=null && !opinionContent.equals("")) {
			OpinionService opinionService = (OpinionService)Environment.getService("opinionService");
			opinionService.saveOpinion(MessageBoard.class.getName(), 0, messageBoard.getId(), opinionContent, "部门办理", 0, JdbcUtils.getString(resultSet, "opinionCreator"), 0, null, null, null, null, JdbcUtils.getTimestamp(resultSet, "opinionTime"));
		}
		return messageBoard.getId();
	}
}