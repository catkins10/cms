package com.yuanluesoft.cms.onlineservice.interactive.accept.dataimporter;

import java.sql.Connection;
import java.sql.ResultSet;

import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAccept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAcceptPrivilege;
import com.yuanluesoft.cms.onlineservice.interactive.accept.service.OnlineServiceAcceptService;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 网上办事在线受理导入,SQL格式如下:
 "select " +
 " as importDataId," + //被导入的记录ID
 " as creatorType," + //申报人类型,0/个人,1/企业
 " as sn," + //编号,在所有的公众服务中唯一
 " as subNo," + //子编号
 " as itemName," + //办理事项名称
 " as unitName," + //受理部门名称
 " as caseAccept," + //是否受理,'0'/'1'
 " as caseDeclinedReason," + //不受理原因
 " as caseAcceptTime," + //受理时间
 " as caseLimitTime," + //受理截止时间
 " as caseCompleteTime," + //办结时间
 " as pickupTime," + //取件时间
 " as caseNumber," + //受理件数,默认是1，集体受理时允许大于1
 " as price," + //价格
 " as businessLicence," + //营业执照号码
 " as legalRepresentative," + //法定代表人
 " as linkman," + //联系人,申报人为企业时有效
 " as acceptStatus," + //办理状态,用于导入的数据
 " as queryPassword," + //查询密码
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
 " as remark" + //附注
 " from [原表名]";
 * @author linchuan
 *
 */
public abstract class OnlineServiceAcceptImporter extends DataImporter {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#getImportDataName()
	 */
	public String getImportDataName() {
		return "网上办事在线受理";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#saveImportedData(com.yuanluesoft.jeaf.dataimport.dataimporter.callback.DataImporterCallback, java.sql.ResultSet)
	 */
	protected long saveImportedData(ResultSet resultSet, WebSite targetSite, Connection connection, String dataImportServiceClass, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		if(importedRecordId>0 && databaseService.findRecordById(OnlineServiceAccept.class.getName(), importedRecordId)!=null) { //检查是否原来导入的记录是否存在
			return importedRecordId;
		}
		
		OnlineServiceAcceptService onlineServiceAcceptService = (OnlineServiceAcceptService)Environment.getService("onlineServiceAcceptService");
		//创建投诉建议
		OnlineServiceAccept accept  = new OnlineServiceAccept();
		JdbcUtils.copyFields(accept, resultSet);
		//设置项目ID
		long itemId = getItemId(accept.getItemName(), resultSet.getString("unitName"), databaseService, resultSet, connection);
		if(itemId>0) {
			accept.setItemId(itemId);
		}
		afterPojoGenerated(accept, resultSet, connection);
		if(accept.getId()==0) {
			accept.setId(UUIDLongGenerator.generateId()); //ID
		}
		accept.setSiteId(targetSite.getId());
		onlineServiceAcceptService.save(accept);
		
		//创建权限记录
		cloneSitePrivileges(OnlineServiceAcceptPrivilege.class, accept.getId(), targetSite, databaseService);
		return accept.getId();
	}
	
	/**
	 * 获取事项ID
	 * @param itemName
	 * @param unitName
	 * @param databaseService
	 * @param resultSet
	 * @param connection
	 * @return
	 */
	protected long getItemId(String itemName, String unitName, DatabaseService databaseService, ResultSet resultSet, Connection connection) {
		if(itemName==null) {
			return 0;
		}
		String hql = "select OnlineServiceItem.id" +
					 " from OnlineServiceItem OnlineServiceItem, OnlineServiceItemUnit OnlineServiceItemUnit" +
					 " where OnlineServiceItemUnit.itemId=OnlineServiceItem.id" +
					 " and OnlineServiceItem.name='" + JdbcUtils.resetQuot(itemName.trim()) + "'" +
					 " and OnlineServiceItemUnit.unitName='" + JdbcUtils.resetQuot(unitName) + "'";
		Number itemId = (Number)databaseService.findRecordByHql(hql);
		return itemId==null ? 0 : itemId.longValue();
	}
}