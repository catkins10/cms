package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.onlineservice.interactive.accept.dataimporter.OnlineServiceAcceptImporter;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAccept;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 网上办事在线受理导入
 * @author linchuan
 *
 */
public class OnlineServiceAcceptImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "漳州网上办事在线受理";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		dataImporters.add(new OnlineServiceAcceptImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "case_id as importDataId," + //被导入的记录ID
						 "case_id as id," + //ID,使用行政服务中心的ID
						 "'0' as creatorType," + //申报人类型,0/个人,1/企业
						 "case_no as sn," + //编号,在所有的公众服务中唯一
						 "case_subno as subNo," + //子编号
						 "(select serv_name from t_service where t_service.serv_no=t_case.case_serv_NO) as itemName," + //办理事项名称
						 "(select Subdp_Name from t_subDepartment where t_subDepartment.SubDp_No=t_case.case_subno) as unitName," + //受理部门名称
						 "case_statusId as caseAccept," + //是否受理,'0'/'1'
						 "'' as caseDeclinedReason," + //不受理原因
						 "case_startdate as caseAcceptTime," + //受理时间
						 "case_LimitLastDate as caseLimitTime," + //受理截止时间
						 "case_okday as caseCompleteTime," + //办结时间
						 "case_takeday as pickupTime," + //取件时间
						 "case_number as caseNumber," + //受理件数,默认是1，集体受理时允许大于1
						 "case_ServicePrice as price," + //价格
						 "case_OrganCode as businessLicence," + //营业执照号码
						 "case_LawMan as legalRepresentative," + //法定代表人
						 "case_LinkMan as linkman," + //联系人,申报人为企业时有效
						 "case_statusId as acceptStatus," + //办理状态,用于导入的数据
						 "'' as queryPassword," + //查询密码
						 "case_OrganName as creator," + //创建人姓名
						 "case_startdate as created," + //创建时间
						 "case_LinkPhone as creatorTel," + //联系电话
						 "case_email as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "'身份证' as creatorCertificateName," + //创建人证件名称
						 "case_idcard as creatorIdentityCard," + //创建人身份证/证件号码
						 "'' as creatorIP," + //创建人IP
						 "case_sms as creatorMobile," + //创建人手机
						 "'' as creatorFax," + //创建人传真
						 "'' as creatorUnit," + //创建人所在单位
						 "'' as creatorJob," + //创建人职业
						 "case_address as creatorAddress," + //创建人地址
						 "case_PossCode as creatorPostalcode," + //创建人邮编
						 "'' as remark" + //附注
						 " from t_case"
				};
			}
		
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				OnlineServiceAccept accept = (OnlineServiceAccept)bean;
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
			}
		});
		return dataImporters;
	}
}