package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.complaint.dataimporter.ComplaintImporter;
import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.opinion.pojo.PublicOpinion;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.request.dataimporter.PublicRequestImporter;
import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.cms.supervision.dataimporter.SupervisionImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 漳州市政府网站
 * @author linchuan
 *
 */
public class DataImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "漳州市政府网站";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//站点数据导入
		/*dataImporters.add(new SiteDataImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "(select 'gazette' as directoryId," +
						   " '政府公报' as directoryName" +
						   " from Z_Gazette" +
						   " where ID=273)" +
						   "union" +
						   "(select str(ID) as directoryId," +
						   " TypeName as directoryName" +
						   " from T_BaseType" +
						   " WHERE ParentID=0)";
				}
				else { //子目录
					if("gazette".equals(parentDirectoryId)) { //公报
						return "select distinct 'gazette2th' + Volume as directoryId," +
						   	   " Volume as directoryName" +
						   	   " from Z_Gazette";
					}
					else if(parentDirectoryId.startsWith("gazette2th")) {
						String volume = parentDirectoryId.substring("gazette2th".length());
						return "select distinct 'gazette3th" + volume + "' + str(TypeID) as directoryId," +
						   	   " (select TypeName from T_BaseType where T_BaseType.ID=Z_Gazette.TypeID) as directoryName" +
						   	   " from Z_Gazette" +
						   	   " where Volume='" + volume + "'";
					}
					else if(!parentDirectoryId.startsWith("gazette3th")) {
						return "select ID as directoryId," +
							   " TypeName as directoryName" +
							   " from T_BaseType" +
							   " where ParentID=" + parentDirectoryId + " and ParentID<>0";
					}
					return null;
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " ID as importDataId," + //被导入的记录ID
					   " TypeID as directoryId," + //原来的栏目ID
					   " Title as subject," + //标题
					   " Content as body," + //正文
					   " '' as subhead," + //副标题
					   " Source," + //来源
					   " Author," + //作者
					   " Tags as keyword," + //关键字
					   " AddDate as created," + //创建时间
					   " AddDate as issueTime," + //发布时间
					   " UserName as editor," + //创建者
					   " UserName as orgName," + //创建者所在部门名称
					   " UserName as unitName" + //创建者所在单位名称
					   " from T_Information" +
	   			   	   " WHERE IsOK='1'",
	   			   	   
	   			   	   //政府公报
	   			   	   "select " +
					   " 'gazette' + str(ID) as importDataId," + //被导入的记录ID
					   " 'gazette3th' + Volume + str(TypeID) as directoryId," + //原来的栏目ID
					   " Title as subject," + //标题
					   " Content as body," + //正文
					   " '' as subhead," + //副标题
					   " '' as source," + //来源
					   " '' as author," + //作者
					   " Tags as keyword," + //关键字
					   " AddDate as created," + //创建时间
					   " AddDate as issueTime," + //发布时间
					   " '' as editor," + //创建者
					   " '' as orgName," + //创建者所在部门名称
					   " '' as unitName" + //创建者所在单位名称
					   " from Z_Gazette" +
	   			   	   " WHERE IsOK='1'"};
			}
			
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				htmlBody = htmlBody.replaceAll("<META http-equiv=Content-Type content=\"text/html; charset=gb2312\">", "");
				return htmlBody.replaceAll("(?i)href=\"UploadFiles", "src=\"" + parameter.getSourceSiteURL() + "UploadFiles")
							   .replaceAll("(?i)href=\"images", "href=\"" + parameter.getSourceSiteURL() + "images")
							   .replaceAll("(?i)src=\"UploadFiles", "src=\"" + parameter.getSourceSiteURL() + "UploadFiles")
							   .replaceAll("(?i)src=\"images", "href=\"" + parameter.getSourceSiteURL() + "images");
			}

			protected long saveImportedData(ResultSet resultSet, WebSite targetSite, Connection connection, String dataImportServiceClass, DataImportParameter parameter, String sourceDataId, long importedRecordId, boolean sameSystem) throws Exception {
				//加载源目录树
				Tree sourceDirectoryTree = (Tree)threadLocal.get();
				if(sourceDirectoryTree==null || !sourceDirectoryTree.getRootNode().getNodeId().equals("root_" + getImportDataName())) {
					sourceDirectoryTree = loadSourceDirectoryTree(true, targetSite.getId(), dataImportServiceClass, connection, parameter, sameSystem);
					threadLocal.set(sourceDirectoryTree);
				}
				String directoryId = SqlResultUtils.getString(resultSet, "directoryId"); //原来的目录ID
				TreeNode dataTreeNode = TreeUtils.findDataTreeNodeById((TreeNode)sourceDirectoryTree.getRootNode(), getImportDataName() + "_" + directoryId);
				if(dataTreeNode==null) {
					dataTreeNode = TreeUtils.findDataTreeNodeById((TreeNode)sourceDirectoryTree.getRootNode(), getImportDataName() + "_     " + directoryId);
					if(dataTreeNode==null) {
						return -1;
					}
				}
				String targetDirectoryIds = dataTreeNode.getExtendPropertyValue("targetDirectoryIds");
				if(targetDirectoryIds==null || targetDirectoryIds.equals("")) { //没有映射目录
					return -1;
				}
				return saveImportData(resultSet, targetDirectoryIds, targetSite, connection, parameter, sourceDataId, importedRecordId, sameSystem);
			}
		});*/
		
		//政府信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select ID as directoryId," +
						   " TypeName as directoryName" +
						   " from Z_InformationType";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				//updateOldInfo = true;
				return new String[] {
					   "select " +
					   " ID as importDataId," + //被导入的记录ID
					   " TypeID as directoryId," + //原来的栏目ID
					   " Title as subject," + //标题
					   " Content as body," + //正文
					   " IndexNumber as infoIndex," + //索引号
					   " (SELECT SourceName FROM Z_Source WHERE (Z_Source.ID = Z_PublicInformation.SourceID)) as infoFrom," + //发布机构
					   " Symbol as mark," + //文号
					   " ShowTime as generateDate," + //生成日期
					   " '　' as summarize," + //内容概述
					   " StatTypeID as category," + //主题分类
					   " Tags as keywords," + //关键字
					   " AddDate as created," + //创建时间
					   " AddDate as issueTime," + //发布时间
					   " '' as creator," + //创建者
					   " (SELECT SourceName FROM Z_Source WHERE (Z_Source.ID = Z_PublicInformation.SourceID)) as orgName," + //创建者所在部门名称
					   " (SELECT SourceName FROM Z_Source WHERE (Z_Source.ID = Z_PublicInformation.SourceID)) as unitName" + //创建者所在单位名称
					   " from Z_PublicInformation" +
	   			       " WHERE IsOK='1'"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				PublicInfo publicInfo = (PublicInfo)bean;
				if("0".equals(publicInfo.getCategory())) {
					publicInfo.setCategory(null);
				}
				else if("10112".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("1、机构职能类");
				}
				else if("10150".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("2、政策、规范性文件类");
				}
				else if("10151".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("3、规划计划类");
				}
				else if("10152".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("4、行政许可类");
				}
				else if("10153".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("5、重大建设项目、为民办实事类");
				}
				else if("10154".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("6、民政扶贫救灾社会保障就业类");
				}
				else if("10155".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("7、国土资源城乡建设环保能源类");
				}
				else if("10156".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("8、科教文体卫生类");
				}
				else if("10157".equals(publicInfo.getCategory())) {
					publicInfo.setCategory("9、安全生产、应急管理类");
				}
			}
		});
		
		//依申请公开
		dataImporters.add(new PublicRequestImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
					"select " +
					 "ID as importDataId," + //被导入的记录ID
					 "SerialNumber as sn," + //编号,在所有的公众服务中唯一
					 "Pword as queryPassword," + //查询密码
					 "(CASE RequestType WHEN '公民' THEN '0' ELSE '1' END) as proposerType," + //申请人类型,0|公民,1|法人/其他组织
					 "UserName as creator," + //申请人/单位联系人姓名
					 "Workplace as creatorUnit," + //单位名称
					 "CredentialName as creatorCertificateName," + //创建人证件名称
					 "CredentialNumber as creatorIdentityCard," + //创建人身份证/证件号码
					 "Phone as creatorTel," + //联系电话
					 "'' as creatorMobile," + //手机
					 "Fax as creatorFax," + //传真
					 "Address as creatorAddress," + //地址
					 "Postcode as creatorPostalcode," + //邮编
					 "'' as creatorMail," + //邮箱
					 "'M' as creatorSex," + //申请人性别 M/F
					 "UserIP as creatorIP," + //申请人IP
					 "'' as creatorJob," + //申请人职业
					 "AddDate as created," + //申请时间
					 "'网站' as applyMode," + //申请方式,默认“网站”
					 "OrganizationCode as code," + //机构代码
					 "ManagersName as legalRepresentative," + //法人代表
					 "Description as subject," + //内容描述
					 "Uses as purpose," + //用途
					 "replace(InformationGet, '|', ',') as medium," + //介质,纸面/电子邮件/光盘/磁盘
					 "replace(InformationWay, '|', ',') as receiveMode," + //获取信息的方式,邮寄/快递/电子邮件/传真/自行领取/当场阅读、抄录
					 "'' as approvalResult," + //审批结果,同意公开/同意部分公开/不公开
					 "'' as notPublicType," + //不公开类别,信息不存在/非本部门掌握/申请内容不明确/免予公开/其他原因未能提供信息
					 "'' as notPublicReason," + //其他原因说明
					 "'' as doneMedium," + //实际提供介质
					 "'' as doneReceiveMode," + //实际提供方式
					 "'1' as isPublic," + //是否允许公开
					 "'0' as publicPass," + //是否公开到网站
					 "'0' as publicBody," + //是否公开正文
					 "'0' as publicWorkflow," + //是否公开办理流程
					 "null as publicSubject," + //公开时的主题
					 "NoteInformation as remark," + //附注
					 "ReplyDate as opinionTime," + //办理意见填写时间
					 "ReplyContent as opinionBody," + //办理意见填写内容
					 "ReplyUserName as opinionCreator" + //办理意见填写人
					 " from Z_Application"
				};
			}
		});
		
		//信息公开意见箱
		dataImporters.add(new PublicRequestImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
					"select " +
					"ReleaseDate," +
					"ID as importDataId," + //被导入的记录ID
					"'' as sn," + //编号,在所有的公众服务中唯一
					"'' as queryPassword," + //查询密码
					"Title as subject," + //主题
					"content," + //正文
					"UserName as creator," + //创建人姓名
					"AddDate as created," + //创建时间
					"Phone as creatorTel," + //联系电话
					"Email as creatorMail," + //邮箱
					"'M' as creatorSex," + //性别 M/F
					"'' as creatorCertificateName," + //创建人证件名称
					"'' as creatorIdentityCard," + //创建人身份证/证件号码
					"IP as creatorIP," + //创建人IP
					"'' as creatorMobile," + //创建人手机
					"Fax as creatorFax," + //创建人传真
					"Company as creatorUnit," + //创建人所在单位
					"'' as creatorJob," + //创建人职业
					"Address as creatorAddress," + //创建人地址
					"'' as creatorPostalcode," + //创建人邮编
					"'1' as isPublic," + //是否允许公开
					"'0' as publicPass," + //是否公开到网站
					"'0' as publicBody," + //是否公开正文
					"'0' as publicWorkflow," + //是否公开办理流程
					"null as publicSubject," + //公开时的主题
					"'' as remark," + //附注
					"AnswerAddDate as opinionTime," + //办理意见填写时间
					"AnswerContent as opinionBody," + //办理意见填写内容
					"(select AdminAccounts from T_Admin where T_Admin.AdminID=Z_Voice.AnswerUserID) as opinionCreator" + //办理意见填写人
					" from Z_Voice"
				};
			}

			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				PublicOpinion publicOpinion = (PublicOpinion)bean;
				//根据发布日期来决定是否允许公开
				Timestamp releaseDate = resultSet.getTimestamp("ReleaseDate");
				if(releaseDate!=null && releaseDate.after(DateTimeUtils.parseTimestamp("2000-01-01 00:00:00", null))) {
					publicOpinion.setPublicPass('1');
					publicOpinion.setPublicBody('1');
					publicOpinion.setPublicWorkflow('1');
				}
			}
		});
		
		//市长信箱
		dataImporters.add(new LeaderMailImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
					"select " +
					"ID as importDataId," + //被导入的记录ID
					"ID as sn," + //编号,在所有的公众服务中唯一
					"AccessCode as queryPassword," + //查询密码
					"Title as subject," + //主题
					"content," + //正文
					"UserName as creator," + //创建人姓名
					"AddDate as created," + //创建时间
					"Phone as creatorTel," + //联系电话
					"Email as creatorMail," + //邮箱
					"'M' as creatorSex," + //性别 M/F
					"'' as creatorCertificateName," + //创建人证件名称
					"'' as creatorIdentityCard," + //创建人身份证/证件号码
					"IP as creatorIP," + //创建人IP
					"'' as creatorMobile," + //创建人手机
					"Fax as creatorFax," + //创建人传真
					"Company as creatorUnit," + //创建人所在单位
					"'' as creatorJob," + //创建人职业
					"Address as creatorAddress," + //创建人地址
					"Postcode as creatorPostalcode," + //创建人邮编
					"'0' as isPublic," + //是否允许公开
					"IsShow as publicPass," + //是否公开到网站
					"IsShow as publicBody," + //是否公开正文
					"IsShow as publicWorkflow," + //是否公开办理流程
					"null as publicSubject," + //公开时的主题
					"'' as remark," + //附注
					"'' as popedom," + //事件辖区
					"'' as area," + //事件地点
					"'　' as type," + //类型
					"AddDate as happenTime," + //事件时间
					"AnswerAddDate as opinionTime," + //办理意见填写时间
					"AnswerContent as opinionBody," + //办理意见填写内容
					"(select AdminAccounts from T_Admin where T_Admin.AdminID=T_FAQ.AnswerUserID) as opinionCreator" + //办理意见填写人
					" from T_FAQ" +
					" where TypeID=10081"
				};
			}
		});
		
		//投诉,建言献策
		dataImporters.add(new ComplaintImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
					"select " +
					"ID as importDataId," + //被导入的记录ID
					"ID as sn," + //编号,在所有的公众服务中唯一
					"AccessCode as queryPassword," + //查询密码
					"Title as subject," + //主题
					"content," + //正文
					"UserName as creator," + //创建人姓名
					"AddDate as created," + //创建时间
					"Phone as creatorTel," + //联系电话
					"Email as creatorMail," + //邮箱
					"'M' as creatorSex," + //性别 M/F
					"'' as creatorCertificateName," + //创建人证件名称
					"'' as creatorIdentityCard," + //创建人身份证/证件号码
					"IP as creatorIP," + //创建人IP
					"'' as creatorMobile," + //创建人手机
					"Fax as creatorFax," + //创建人传真
					"Company as creatorUnit," + //创建人所在单位
					"'' as creatorJob," + //创建人职业
					"Address as creatorAddress," + //创建人地址
					"Postcode as creatorPostalcode," + //创建人邮编
					"'0' as isPublic," + //是否允许公开
					"IsShow as publicPass," + //是否公开到网站
					"IsShow as publicBody," + //是否公开正文
					"IsShow as publicWorkflow," + //是否公开办理流程
					"null as publicSubject," + //公开时的主题
					"'' as remark," + //附注
					"'' as popedom," + //事件辖区
					"'' as area," + //事件地点
					"'建言献策' as type," + //类型
					"AddDate as happenTime," + //事件时间
					"AnswerAddDate as opinionTime," + //办理意见填写时间
					"AnswerContent as opinionBody," + //办理意见填写内容
					"(select AdminAccounts from T_Admin where T_Admin.AdminID=T_FAQ.AnswerUserID) as opinionCreator" + //办理意见填写人
					" from T_FAQ" +
					" where TypeID=10084"
				};
			}
		});
		
		//效能监督
		dataImporters.add(new SupervisionImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
					"select " +
					"ID as importDataId," + //被导入的记录ID
					"ID as sn," + //编号,在所有的公众服务中唯一
					"AccessCode as queryPassword," + //查询密码
					"Title as subject," + //主题
					"content," + //正文
					"UserName as creator," + //创建人姓名
					"AddDate as created," + //创建时间
					"Phone as creatorTel," + //联系电话
					"Email as creatorMail," + //邮箱
					"'M' as creatorSex," + //性别 M/F
					"'' as creatorCertificateName," + //创建人证件名称
					"'' as creatorIdentityCard," + //创建人身份证/证件号码
					"IP as creatorIP," + //创建人IP
					"'' as creatorMobile," + //创建人手机
					"Fax as creatorFax," + //创建人传真
					"Company as creatorUnit," + //创建人所在单位
					"'' as creatorJob," + //创建人职业
					"Address as creatorAddress," + //创建人地址
					"Postcode as creatorPostalcode," + //创建人邮编
					"'0' as isPublic," + //是否允许公开
					"IsShow as publicPass," + //是否公开到网站
					"IsShow as publicBody," + //是否公开正文
					"IsShow as publicWorkflow," + //是否公开办理流程
					"null as publicSubject," + //公开时的主题
					"'' as remark," + //附注
					"'' as unit," + //被监督机构
					"AnswerAddDate as opinionTime," + //办理意见填写时间
					"AnswerContent as opinionBody," + //办理意见填写内容
					"(select AdminAccounts from T_Admin where T_Admin.AdminID=T_FAQ.AnswerUserID) as opinionCreator" + //办理意见填写人
					" from T_FAQ" +
					" where TypeID=10504"
				};
			}
		});
		return dataImporters;
	}
}