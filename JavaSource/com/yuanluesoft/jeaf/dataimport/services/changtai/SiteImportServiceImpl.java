package com.yuanluesoft.jeaf.dataimport.services.changtai;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.complaint.dataimporter.ComplaintImporter;
import com.yuanluesoft.cms.infopublic.request.dataimporter.PublicRequestImporter;
import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.cms.messageboard.dataimporter.MessageBoardImporter;
import com.yuanluesoft.cms.preapproval.dataimporter.PreapprovalImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 长泰站点
 * @author linchuan
 *
 */
public class SiteImportServiceImpl extends DataImportService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "长泰县政府网站";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//网站数据导入
		dataImporters.add(new SiteDataImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#generateListChildDirectoriesSQL(java.lang.String)
			 */
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "(select 'first_' & E_typeid as directoryId," +
							" E_typename as directoryName" +
							" from EC_type" +
							" order by E_typename)" +
							"union" +
							"(select 'board' as directoryId," +
							" '公告栏' as directoryName" +
							" from EC_type" +
							" where E_typeid=28)";
				}
				else if(parentDirectoryId.startsWith("first_")) {
					return "select 'second_' & E_BigClassID as directoryId," +
						   " E_BigClassName as directoryName" +
						   " from EC_BigClass" +
						   " where E_typeid=" + parentDirectoryId.substring("first_".length()) +
						   " order by E_BigClassName";
				}
				else if(parentDirectoryId.startsWith("second_")) {
					return "select 'third_' & E_SmallClassID as directoryId," +
						   " E_smallclassname as directoryName" +
						   " from EC_smallclass" +
						   " where E_BigClassID=" + parentDirectoryId.substring("second_".length()) +
						   " order by E_smallclassname";
				}
				return null;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#generateRetrieveDataSQL(java.util.List)
			 */
			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						   "select " +
						   " EC_ArticleID as importDataId," + //被导入的记录ID
						   " iif(E_smallclassid is null, iif(E_bigclassid is null, 'first_' & E_typeid, 'second_' & E_bigclassid), 'third_' & E_smallclassid) as directoryId," + //原来的栏目ID
						   " Title as subject," + //标题
						   " Content as body," + //正文
						   " '' as subhead," + //副标题
						   " null as Source," + //来源
						   " Author," + //作者
						   " about as keyword," + //关键字
						   " UpdateTime as created," + //创建时间
						   " UpdateTime as issueTime," + //发布时间
						   " editor," + //创建者
						   " Original as orgName," + //创建者所在部门名称
						   " Original as unitName" + //创建者所在单位名称
						   " from EC_Article",
						   
						   "select " +
						   " id as importDataId," + //被导入的记录ID
						   " 'board' as directoryId," + //原来的栏目ID
						   " title as subject," + //标题
						   " content as body," + //正文
						   " '' as subhead," + //副标题
						   " null as Source," + //来源
						   " '' as Author," + //作者
						   " '' as keyword," + //关键字
						   " dateandtime as created," + //创建时间
						   " dateandtime as issueTime," + //发布时间
						   " upload as editor," + //创建者
						   " '' as orgName," + //创建者所在部门名称
						   " '' as unitName" + //创建者所在单位名称
						   " from EC_board"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String, java.sql.ResultSet, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
			 */
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				//<P align=center><EMBED src=uploadfile/wmv/2011-12/一周要闻20111225.wmv width=360 height=315 type=video/x-ms-wmv loop="-1" autostart="true"></EMBED><BR><A href="">下载地址</A></P>
				//<P align=center><EMBED src=uploadfile/wmv/2011-7/2011715201152495.wmv width=360 height=315 type=video/x-ms-wmv loop="-1" autostart="true"></EMBED>
				//请点击图标下载浏览：<BR><A href="ECDownload.asp?FileName=doc/2008-10/20081030143551343.doc"><IMG src="TEMPLETS/0/images/doc.gif" border=0></A><BR>附件
				//<A title=点击图片看全图 href="uploadfile/jpg/2008-8/20088718123750.jpg" target=_blank><P align=center><IMG alt="" src="uploadfile/jpg/2008-8/20088718123750.jpg" border=0></A><BR></P>
				//<P align=center><EMBED src=uploadfile/wmv/2009-10/20091019175531518.wmv width=360 height=315 type=video/x-ms-wmv loop="-1" autostart="true"></EMBED><BR><A href="uploadfile/wmv/2009-10/20091019175531518.wmv">下载地址uploadfile/wmv/2009-10/20091019175531518.wmv</A></P>

				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				if(htmlBody==null || htmlBody.isEmpty()) {
					return htmlBody;
				}
				String[] keywords = {"uploadfile", "ECDownload", "TEMPLETS"};
				for(int i=0; i<keywords.length; i++) {
					htmlBody = htmlBody.replaceAll("(?i)src=\"" + keywords[i], "src=\"" + parameter.getSourceSiteURL() + keywords[i])
								   	   .replaceAll("(?i)src=" + keywords[i], "src=" + parameter.getSourceSiteURL() + keywords[i])
									   .replaceAll("(?i)href=\"" + keywords[i], "href=\"" + parameter.getSourceSiteURL() + keywords[i])
									   .replaceAll("(?i)href=" + keywords[i], "href=" + parameter.getSourceSiteURL() + keywords[i]);
				}
				return htmlBody;
			}
		});
		
		//预审
		dataImporters.add(new PreapprovalImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "ApproveID as importDataId," + //被导入的记录ID
						 "ReceptNum as sn," + //编号,在所有的公众服务中唯一
						 "G_password as queryPassword," + //查询密码
						 "Topic as subject," + //主题
						 "Content as body," + //正文
						 "YourName as creator," + //创建人姓名
						 "UpdateTime as created," + //创建时间
						 "TelePhone as creatorTel," + //联系电话
						 "Email as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "null as creatorCertificateName," + //创建人证件名称
						 "null as creatorIdentityCard," + //创建人身份证/证件号码
						 "ApproveIP as creatorIP," + //创建人IP
						 "null as creatorMobile," + //创建人手机
						 "null as creatorFax," + //创建人传真
						 "null as creatorUnit," + //创建人所在单位
						 "null as creatorJob," + //创建人职业
						 "Address as creatorAddress," + //创建人地址
						 "Zip as creatorPostalcode," + //创建人邮编
						 "'0' as isPublic," + //是否允许公开
						 "UserName as remark," + //附注,单位名称
						 "'' as projectName," + //预审项目名称
						 "ReplyTime as opinionTime," + //办理意见填写时间
						 "ReplyContent as opinionBody," + //办理意见填写内容
						 "UserName as opinionCreator" + //办理意见填写人
						 " from EC_Approve"
				};
			}
		});
		
		/*/投诉
		dataImporters.add(new ComplaintImporter() {

			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "ComplainID as importDataId," + //被导入的记录ID
						 "acceptnum as sn," + //编号,在所有的公众服务中唯一
						 "E_password as queryPassword," + //查询密码
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
						 " from EC_Complain"
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
						 " from EC_Opinion"
				};
			}
		});
		
		//依申请公开
		dataImporters.add(new PublicRequestImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "gmsqID as importDataId," + //被导入的记录ID
						 "null as sn," + //编号,在所有的公众服务中唯一
						 "null as queryPassword," + //查询密码
						 "'0' as proposerType," + //申请人类型,0|公民,1|法人/其他组织
						 "YourName as creator," + //申请人/单位联系人姓名
						 "company as creatorUnit," + //单位名称
						 "certname as creatorCertificateName," + //创建人证件名称
						 "certnum as creatorIdentityCard," + //创建人身份证/证件号码
						 "Tel as creatorTel," + //联系电话
						 "null as creatorMobile," + //手机
						 "tax as creatorFax," + //传真
						 "Address as creatorAddress," + //地址
						 "Zip as creatorPostalcode," + //邮编
						 "Email as creatorMail," + //邮箱
						 "'M' as creatorSex," + //申请人性别 M/F
						 "gmsqIP as creatorIP," + //申请人IP
						 "null as creatorJob," + //申请人职业
						 "UpdateTime as created," + //申请时间
						 "'网站' as applyMode," + //申请方式,默认“网站”
						 "null as code," + //机构代码
						 "null as legalRepresentative," + //法人代表
						 "Content as subject," + //内容描述
						 "youtu as purpose," + //用途
						 "UserName as unit," + //涉及单位
						 "tgfs1 as medium," + //介质,纸面/电子邮件/光盘/磁盘
						 "hqfs3 as receiveMode," + //获取信息的方式,邮寄/快递/电子邮件/传真/自行领取/当场阅读、抄录
						 "null as approvalResult," + //审批结果,同意公开/同意部分公开/不公开
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
						 "null as opinionTime," + //办理意见填写时间
						 "null as opinionBody," + //办理意见填写内容
						 "null as opinionCreator" + //办理意见填写人
						 " from EC_gmsq"
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
						 "E_PassWord as queryPassword," + //查询密码
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
						 "Checked as publicBody," + //是否公开正文
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
						 " from EC_LeadMail"
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
						 "comefrom as creatorAddress," + //创建人地址
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
						 " from EC_Review" +
						 " where EC_ArticleID<=0"
				};
			}
		});
		return dataImporters;
	}
}