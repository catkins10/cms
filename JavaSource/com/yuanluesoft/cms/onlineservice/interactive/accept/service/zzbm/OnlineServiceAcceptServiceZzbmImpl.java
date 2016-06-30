package com.yuanluesoft.cms.onlineservice.interactive.accept.service.zzbm;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAccept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAcceptPrivilege;
import com.yuanluesoft.cms.onlineservice.interactive.accept.service.spring.OnlineServiceAcceptServiceImpl;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.zzbm.ZzbmRecordParseCallback;
import com.yuanluesoft.cms.onlineservice.zzbm.ZzbmRecordParser;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.ZipUtils;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceAcceptServiceZzbmImpl extends OnlineServiceAcceptServiceImpl {

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.interactive.accept.service.spring.OnlineServiceAcceptServiceImpl#importCases(java.util.List)
	 */
	public void importCases(List uploadFiles) throws ServiceException {
		/*Attachment attachment = (Attachment)ListUtils.findObjectByProperty(uploadFiles, "name", "t_case.fmt");
		if(attachment==null) {
			throw new ServiceException("文件“t_case.fmt”未上传");
		}
		List fields = parseFields(attachment.getFilePath());
		attachment = (Attachment)ListUtils.findObjectByProperty(uploadFiles, "name", "t_case.txt");
		if(attachment==null) {
			throw new ServiceException("文件“t_case.txt”未上传");
		}*/
		if(uploadFiles==null || uploadFiles.isEmpty()) {
			throw new ServiceException("数据文件未上传");
		}
		String[] fieldNames = new String[] {
				"case_id",
				"case_no",
				"case_serv_NO",
				"case_Depa_No",
				"case_user_id",
				"case_Bjuserid",
				"case_statusId",
				"case_TimeLimit",
				"case_ServicePrice",
				"case_note",
				"case_TypeId",
				"case_LbNote",
				"case_number",
				"case_PriceFlag",
				"case_subno",
				"case_Customer_no",
				"case_ServiceTypeId",
				"case_HandleDays",
				"case_startdate",
				"case_LimitLastDate",
				"case_okday",
				"case_takeday",
				"case_LastDBDate",
				"case_PayDate",
				"case_warnDate",
				"case_organName",
				"case_OrganCode",
				"case_Applicant",
				"case_idcard",
				"case_LawMan",
				"case_address",
				"case_PossCode",
				"case_LinkMan",
				"case_LinkPhone",
				"case_sms",
				"case_email",
				"case_material",
				"case_PostilNote",
				"case_smsFlag",
				"case_leader",
				"case_querypassword",
				"case_takeflag",
				"case_bz",
				"case_PriceText",
				"case_ifchaoshi",
				"case_ifbaoyan",
				"case_source",
				"case_priceb",
				"case_pricec",
				"case_bankname",
				"case_bankaccount",
				"case_cheque",
				"case_typeFlag",
				"case_ifsp",
				"show_flag",
				"dep_id",
				"fLeader_id",
				"spLevel",
				"case_payKindId",
				"case_payMode",
				"case_execCode",
				"case_item",
				"case_serv_prop",
				"case_ifVip",
				"case_sp_over",
				"case_sp_mayor",
				"case_jkhNo",
				"case_pauseStartDate",
				"case_restartDate",
				"zLeaderId",
				"case_lbflag",
				"case_standby",
				"case_LawTimeLimit",
				"functionname",
				"instanceid",
				"workitemid",
				"taskname",
				"workflowTemplate",
				"subFlowInstanceID",
				"csuser",
				"csdate",
				"csidea",
				"csstatus",
				"waitStatus",
				"case_ifspecialban",
				"case_lbStatus",
				"case_yewuNo",
				"case_isZscl",
				"workflowDaibanAllowDel",
				"case_iftaskcommit",
				"shstatus",
				"shuser",
				"shdate",
				"shidea",
				"case_ifFclb",
				"case_hzOption",
				"case_cabz",
				"caShIdea",
				"caCsIdea",
				"case_servParentNo",
				"case_servParentName",
				"case_ApplyTime",
				"case_ApplyPerson",
				"case_ApplyDeptName",
				"case_Message",
				"case_lddistCode"
		};
		String[][] fieldMappings = {
				{"case_id", "id", null}, //ID
				{"case_no", "sn", null}, //编号,在所有的公众服务中唯一
				{"case_subno", "subNo", null}, //子编号
				{"case_serv_NO", "itemId", null}, //事项ID,需要转换
				{"case_statusId", "caseAccept", null}, //是否受理,'0'/'1'
				{"case_startdate", "caseAcceptTime", "yyyy-MM-dd HH:mm:ss.SSS"}, //受理时间
				{"case_LimitLastDate", "caseLimitTime", "yyyy-MM-dd HH:mm:ss.SSS"}, //受理截止时间
				{"case_okday", "caseCompleteTime", "yyyy-MM-dd HH:mm:ss.SSS"}, //办结时间
				{"case_takeday", "pickupTime", "yyyy-MM-dd HH:mm:ss.SSS"}, //取件时间
				{"case_number", "caseNumber", null}, //受理件数
				{"case_ServicePrice", "price", null}, //价格
				{"case_OrganCode", "businessLicence", null}, //营业执照号码
				{"case_LawMan", "legalRepresentative", null}, //法定代表人
				{"case_LinkMan", "linkman", null}, //联系人,申报人为企业时有效
				{"case_statusId", "acceptStatus", null}, //办理状态,用于导入的数据
				{"case_organName", "creator", null}, //创建人姓名
				{"case_startdate", "created", "yyyy-MM-dd HH:mm:ss.SSS"}, //创建时间
				{"case_LinkPhone", "creatorTel", null}, //联系电话
				{"case_email", "creatorMail", null}, //邮箱
				{"case_idcard", "creatorIdentityCard", null}, //创建人身份证/证件号码
				{"case_sms", "creatorMobile", null}, //创建人手机
				{"case_address", "creatorAddress", null}, //创建人地址
				{"case_PossCode", "creatorPostalcode", null} //创建人邮编
		};
		
		//记录解析回调
		ZzbmRecordParseCallback recordParseCallback = new ZzbmRecordParseCallback() {
	
			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.onlineservice.zzbm.ZzbmRecordParseCallback#processParsedRecord(java.util.Map, java.lang.Object)
			 */
			public void processParsedRecord(Map propertyTextValues, Object record) throws Exception {
				OnlineServiceAccept accept = (OnlineServiceAccept)record;
				String hql = "from OnlineServiceAccept OnlineServiceAccept where OnlineServiceAccept.id=" + accept.getId();
				OnlineServiceAccept oldAccept = (OnlineServiceAccept)getDatabaseService().findRecordByHql(hql);
				if(oldAccept!=null) {
					oldAccept.setLazyBody(null);
					oldAccept.setMissings(null);
					oldAccept.setWorkItems(null);
					oldAccept.setOpinions(null);
					oldAccept.setVisitors(null);
				}
				//设置申报项目ID、名称
				hql = "from OnlineServiceItem OnlineServiceItem where OnlineServiceItem.code='" + accept.getItemId() + "'";
				OnlineServiceItem item = (OnlineServiceItem)getDatabaseService().findRecordByHql(hql);
				if(item!=null) {
					accept.setItemId(item.getId());
					accept.setItemName(item.getName());
				}
				accept.setCaseAccept(accept.getCaseAccept()=='5' ? '0' : '1'); //是否受理
				if("1".equals(accept.getAcceptStatus())) {
					accept.setAcceptStatus("待办中");
				}
				else if("2".equals(accept.getAcceptStatus())) {
					accept.setAcceptStatus("已办理");
				}
				else if("3".equals(accept.getAcceptStatus())) {
					accept.setAcceptStatus("待启动联办件");
				}
				else if("4".equals(accept.getAcceptStatus())) {
					accept.setAcceptStatus("退件处理");
				}
				else if("5".equals(accept.getAcceptStatus())) {
					accept.setAcceptStatus("废件处理");
				}
				else if("6".equals(accept.getAcceptStatus())) {
					accept.setAcceptStatus("需补办");
				}
				else if("7".equals(accept.getAcceptStatus())) {
					accept.setAcceptStatus("审批完成，待取件");
				}
				else if("8".equals(accept.getAcceptStatus())) {
					accept.setAcceptStatus("已经审批，已取件");
				}
				else {
					accept.setAcceptStatus("其它");
				}
				accept.setExtendPropertyValue("importData", Boolean.TRUE); //标记为导入的数据
				if(oldAccept==null) { //新记录
					save(accept);
					//创建权限记录
					OnlineServiceAcceptPrivilege privilege = new OnlineServiceAcceptPrivilege();
					privilege.setId(UUIDLongGenerator.generateId());
					privilege.setAccessLevel(RecordControlService.ACCESS_LEVEL_READONLY);
					privilege.setRecordId(accept.getId());
					privilege.setVisitorId(0);
					getDatabaseService().saveRecord(privilege);
				}
				else if(!BeanUtils.equals(accept, oldAccept)) { //旧记录,检查记录是否有变化
					update(accept);
				}
			}
		};
		//解压数据文件
		String filePath = ((Attachment)uploadFiles.get(0)).getFilePath();
		String dataPath = FileUtils.createDirectory(filePath.substring(0, filePath.lastIndexOf('/') + 1) + "casesdata");
		try {
			ZipUtils.unZip(filePath, dataPath);
			ZzbmRecordParser.parseDataFile(new File(dataPath).listFiles()[0].getPath(), OnlineServiceAccept.class, fieldNames, fieldMappings, recordParseCallback);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		FileUtils.deleteDirectory(dataPath);
	}
}