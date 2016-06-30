package com.yuanluesoft.jeaf.dataimport.services.longhai;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.complaint.dataimporter.ComplaintImporter;
import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.cms.messageboard.dataimporter.MessageBoardImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 龙海市政府网站
 * @author linchuan
 *
 */
public class SiteImportServiceImpl extends DataImportService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "龙海市政府网站";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//投诉
		/*dataImporters.add(new ComplaintImporter() {

			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "ComplainID as importDataId," + //被导入的记录ID
						 "acceptnum as sn," + //编号,在所有的公众服务中唯一
						 "wMcms_PassWord as queryPassword," + //查询密码
						 "Topic as subject," + //主题
						 "content," + //正文
						 "b_person as creator," + //创建人姓名
						 "UpdateTime as created," + //创建时间
						 "b_tel as creatorTel," + //联系电话
						 "Email as creatorMail," + //邮箱
						 "null as creatorSex," + //性别 M/F
						 "null as creatorCertificateName," + //创建人证件名称
						 "null as creatorIdentityCard," + //创建人身份证/证件号码
						 "ComplainIP as creatorIP," + //创建人IP
						 "mobile as creatorMobile," + //创建人手机
						 "null as creatorFax," + //创建人传真
						 "null as creatorUnit," + //创建人所在单位
						 "b_name as creatorJob," + //创建人职业
						 "b_adress as creatorAddress," + //创建人地址
						 "null as creatorPostalcode," + //创建人邮编
						 "'1' as isPublic," + //是否允许公开
						 "'0' as publicPass," + //是否公开到网站
						 "'0' as publicBody," + //是否公开正文
						 "'0' as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "null as remark," + //附注
						 "null as popedom," + //事件辖区
						 "null as area," + //事件地点
						 "ComplainClass as type," + //类型
						 "UpdateTime as happenTime," + //事件时间
						 "null as opinionTime," + //办理意见填写时间
						 "null as opinionBody," + //办理意见填写内容
						 "null as opinionCreator" + //办理意见填写人
						 " from wmcms_Complain"
				};
			}
		});*/
		
		//建言献策->投诉
		dataImporters.add(new ComplaintImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "OpinionID as importDataId," + //被导入的记录ID
						 "null as sn," + //编号,在所有的公众服务中唯一
						 "null as queryPassword," + //查询密码
						 "Topic as subject," + //主题
						 "content," + //正文
						 "YourName as creator," + //创建人姓名
						 "UpdateTime as created," + //创建时间
						 "TelePhone as creatorTel," + //联系电话
						 "Email as creatorMail," + //邮箱
						 "null as creatorSex," + //性别 M/F
						 "null as creatorCertificateName," + //创建人证件名称
						 "null as creatorIdentityCard," + //创建人身份证/证件号码
						 "OpinionIP as creatorIP," + //创建人IP
						 "null as creatorMobile," + //创建人手机
						 "null as creatorFax," + //创建人传真
						 "null as creatorUnit," + //创建人所在单位
						 "null as creatorJob," + //创建人职业
						 "Address as creatorAddress," + //创建人地址
						 "Zip as creatorPostalcode," + //创建人邮编
						 "checked as isPublic," + //是否允许公开
						 "checked as publicPass," + //是否公开到网站
						 "checked as publicBody," + //是否公开正文
						 "checked as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "null as remark," + //附注
						 "null as popedom," + //事件辖区
						 "null as area," + //事件地点
						 "OpinionClass as type," + //类型
						 "UpdateTime as happenTime," + //事件时间
						 "ReplyTime as opinionTime," + //办理意见填写时间
						 "ReplyContent as opinionBody," + //办理意见填写内容
						 "'　' as opinionCreator" + //办理意见填写人
						 " from wmcms_Opinion"
				};
			}
		});
		
		//领导信箱
		dataImporters.add(new LeaderMailImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						 "select " +
						 "LeadMailID as importDataId," + //被导入的记录ID
						 "AcceptNum as sn," + //编号,在所有的公众服务中唯一
						 "wMcms_PassWord as queryPassword," + //查询密码
						 "Topic as subject," + //主题
						 "content," + //正文
						 "YourName as creator," + //创建人姓名
						 "UpdateTime as created," + //创建时间
						 "TelePhone as creatorTel," + //联系电话
						 "Email as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "null as creatorCertificateName," + //创建人证件名称
						 "null as creatorIdentityCard," + //创建人身份证/证件号码
						 "LeadMailIP as creatorIP," + //创建人IP
						 "null as creatorMobile," + //创建人手机
						 "null as creatorFax," + //创建人传真
						 "null as creatorUnit," + //创建人所在单位
						 "null as creatorJob," + //创建人职业
						 "Address as creatorAddress," + //创建人地址
						 "Zip as creatorPostalcode," + //创建人邮编
						 "Checked as isPublic," + //是否允许公开
						 "Checked as publicPass," + //是否公开到网站
						 "'0' as publicBody," + //是否公开正文
						 "Checked as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "null as remark," + //附注
						 "null as popedom," + //事件辖区
						 "null as area," + //事件地点
						 "Content_Type as type," + //类型
						 "UpdateTime as happenTime," + //事件时间
						 "ReplyTime as opinionTime," + //办理意见填写时间
						 "ReplyContent as opinionBody," + //办理意见填写内容
						 "'　' as opinionCreator" + //办理意见填写人
						 " from wmcms_LeadMail"
				};
			}
		});
		
		//留言板
		dataImporters.add(new MessageBoardImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						 "select " +
						 "ReViewID as importDataId," + //被导入的记录ID
						 "null as sn," + //编号,在所有的公众服务中唯一
						 "null as queryPassword," + //查询密码
						 "title as subject," + //主题
						 "content," + //正文
						 "Author as creator," + //创建人姓名
						 "UpdateTime as created," + //创建时间
						 "null as creatorTel," + //联系电话
						 "Email as creatorMail," + //邮箱
						 "null as creatorSex," + //性别 M/F
						 "null as creatorCertificateName," + //创建人证件名称
						 "null as creatorIdentityCard," + //创建人身份证/证件号码
						 "reviewip as creatorIP," + //创建人IP
						 "null as creatorMobile," + //创建人手机
						 "null as creatorFax," + //创建人传真
						 "null as creatorUnit," + //创建人所在单位
						 "null as creatorJob," + //创建人职业
						 "cfrom as creatorAddress," + //创建人地址
						 "null as creatorPostalcode," + //创建人邮编
						 "passed as isPublic," + //是否允许公开
						 "passed as publicPass," + //是否公开到网站
						 "passed as publicBody," + //是否公开正文
						 "passed as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "null as remark," + //附注
						 "UpdateTime+3 as opinionTime," + //办理意见填写时间
						 "reply as opinionBody," + //办理意见填写内容
						 "'　' as opinionCreator" + //办理意见填写人
						 " from wmcms_Review"
				};
			}
		});
		return dataImporters;
	}
}