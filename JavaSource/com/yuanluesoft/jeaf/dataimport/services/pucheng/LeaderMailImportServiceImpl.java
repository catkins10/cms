package com.yuanluesoft.jeaf.dataimport.services.pucheng;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 浦城县长信箱
 * @author linchuan
 *
 */
public class LeaderMailImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "浦城县长信箱";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		
		//信箱导入
		dataImporters.add(new LeaderMailImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "id as importDataId," + //被导入的记录ID
						 "searchCode as sn," + //编号,在所有的公众服务中唯一
						 "'' as queryPassword," + //查询密码
						 "uTitle as subject," + //主题
						 "uContent as content," + //正文
						 "uName as creator," + //创建人姓名
						 "uDate as created," + //创建时间
						 "uTel as creatorTel," + //联系电话
						 "uEmail as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "'' as creatorCertificateName," + //创建人证件名称
						 "'' as creatorIdentityCard," + //创建人身份证/证件号码
						 "uIp as creatorIP," + //创建人IP
						 "'' as creatorMobile," + //创建人手机
						 "'' as creatorFax," + //创建人传真
						 "uWork as creatorUnit," + //创建人所在单位
						 "'' as creatorJob," + //创建人职业
						 "uAddress as creatorAddress," + //创建人地址
						 "upost as creatorPostalcode," + //创建人邮编
						 "(case ugk when 1 then '1' else '0' end) as isPublic," + //是否允许公开
						 "(case step when '3' then '1' else '0' end) as publicPass," + //是否公开到网站
						 "(case step when '3' then '1' else '0' end) as publicBody," + //是否公开正文
						 "(case step when '3' then '1' else '0' end) as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "'' as remark," + //附注
						 "'' as popedom," + //事件辖区
						 "'' as area," + //事件地点
						 "'' as type," + //类型
						 "null as happenTime," + //事件时间
						 "mDate as opinionTime," + //办理意见填写时间
						 "mRe as opinionBody," + //办理意见填写内容
						 "'' as opinionCreator" + //办理意见填写人
						 " from pcGovMessage" +
						 " where udate>'2009-06-16 19:55:52'"
				};
			}
			
		});
		return dataImporters;
	}
}