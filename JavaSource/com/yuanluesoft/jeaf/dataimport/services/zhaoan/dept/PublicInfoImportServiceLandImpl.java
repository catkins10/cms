package com.yuanluesoft.jeaf.dataimport.services.zhaoan.dept;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.opinion.dataimporter.PublicOpinionImporter;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.request.dataimporter.PublicRequestImporter;
import com.yuanluesoft.cms.infopublic.request.pojo.PublicRequest;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 
 * @author linchuan
 *
 */
public class PublicInfoImportServiceLandImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "诏安审计局、国土局、计生局、水利局、旅游区、金星乡、四都镇、建设乡、南诏镇、深桥镇、西潭乡、金星乡信息公开导入";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		//信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select id as directoryId," +
						   " ClassName as directoryName" +
						   " from xxgkClass" +
						   " where AboveLevel=0";
				}
				else { //其他目录
					return "select id as directoryId," +
						   " ClassName as directoryName" +
						   " from xxgkClass" +
						   " where AboveLevel=" + parentDirectoryId;
				}
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter#generateDirectoryCodeSql(java.lang.String)
			 */
			public String generateDirectoryCodeSql(String sourceDirectoryId) throws Exception {
				return "select ClassNo as code from xxgkClass where id=" + sourceDirectoryId;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "xxgkID as importDataId," + //被导入的记录ID
					   "ClassID as directoryId," + //原来的目录ID
					   "xxgkNos as infoIndex," + //索引号
					   "Original as infoFrom," + //发布机构
					   "Meno as mark," + //文号
					   "UpdateTime as generateDate," + //生成日期
					   "Title as subject," + //标题
					   "Content as body," + //正文
					   "Original as creator," + //创建人
					   "UpdateTime as created," + //创建时间
					   "UpdateTime as issueTime," + //发布时间
					   "'' as summarize," + //内容概述
					   "'' as category," + //主题分类
					   "'' as keywords," + //主题词
					   "Original as orgName," + //创建者所在部门名称
					   "Original as unitName" + //创建者所在单位名称
					   " from xxgk"
				};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				PublicInfo info = (PublicInfo)bean;
				if(info.getBody()!=null) {
					info.setBody(generateHtml(info.getBody()));
				}
			}
			
			private String generateHtml(String text) {
				if(text==null) {
					return text;
				}
				return text.replaceAll("&nbsp;", " ")
						   .replaceAll("&quot;", "\"")
						   .replaceAll("&amp;", "&")
						   .replaceAll("&lt;", "<")
						   .replaceAll("&gt;", ">");
			}
		});
		
		//信息公开意见箱数据导入
		dataImporters.add(new PublicOpinionImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "Id as importDataId," + //被导入的记录ID
						 "null as sn," + //编号,在所有的公众服务中唯一
						 "null as queryPassword," + //查询密码
						 "Title as subject," + //主题
						 "Content," + //正文
						 "Name as creator," + //创建人姓名
						 "UpdateTime as created," + //创建时间
						 "Tel as creatorTel," + //联系电话
						 "EmailAdd as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "null as creatorCertificateName," + //创建人证件名称
						 "null as creatorIdentityCard," + //创建人身份证/证件号码
						 "null as creatorIP," + //创建人IP
						 "Tel as creatorMobile," + //创建人手机
						 "null as creatorFax," + //创建人传真
						 "null as creatorUnit," + //创建人所在单位
						 "null as creatorJob," + //创建人职业
						 "Address as creatorAddress," + //创建人地址
						 "null as creatorPostalcode," + //创建人邮编
						 "'1' as isPublic," + //提交人是否允许公开
						 "'0' as publicPass," + //是否公开到网站
						 "'0' as publicBody," + //是否公开正文
						 "'0' as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "null as remark," + //附注
						 "UpdateTime as opinionTime," + //办理意见填写时间
						 "FreckBack as opinionBody," + //办理意见填写内容
						 "'' as opinionCreator" + //办理意见填写人
						 " from xxgkyjx"
				};
			}
			
		});
	
		//依申请公开数据导入
		dataImporters.add(new PublicRequestImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "Id as importDataId," + //被导入的记录ID
						 "null as sn," + //编号,在所有的公众服务中唯一
						 "null as queryPassword," + //查询密码
						 "(Sort-1) as proposerType," + //申请人类型,0|公民,1|法人/其他组织
						 "yxxgkName as creator," + //申请人/单位联系人姓名
						 "Corp as creatorUnit," + //单位名称
						 "Certificate as creatorCertificateName," + //创建人证件名称
						 "No as creatorIdentityCard," + //创建人身份证/证件号码
						 "Tel as creatorTel," + //联系电话
						 "Tel as creatorMobile," + //手机
						 "Fax as creatorFax," + //传真
						 "Addr as creatorAddress," + //地址
						 "Code as creatorPostalcode," + //邮编
						 "Email as creatorMail," + //邮箱
						 "'M' as creatorSex," + //申请人性别 M/F
						 "null as creatorIP," + //申请人IP
						 "null as creatorJob," + //申请人职业
						 "AddTime as created," + //申请时间
						 "null as applyMode," + //申请方式,默认“网站”
						 "Corp as code," + //机构代码
						 "Certificate as legalRepresentative," + //法人代表
						 "Content as subject," + //内容描述
						 "Meno as purpose," + //用途
						 "null as unit," + //涉及单位
						 "null as medium," + //介质,纸面/电子邮件/光盘/磁盘
						 "OfferMode as receiveMode," + //获取信息的方式,邮寄/快递/电子邮件/传真/自行领取/当场阅读、抄录 
						 "null as approvalResult," + //审批结果,同意公开/同意部分公开/不公开
						 "null as notPublicType," + //不公开类别,信息不存在/非本部门掌握/申请内容不明确/免予公开/其他原因未能提供信息
						 "null as notPublicReason," + //其他原因说明
						 "null as doneMedium," + //实际提供介质
						 "GetMode as doneReceiveMode," + //实际提供方式
						 "'1' as isPublic," + //是否允许公开
						 "'0' as publicPass," + //是否公开到网站
						 "'0' as publicBody," + //是否公开正文
						 "'0' as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "null as remark," + //附注
						 "AddTime as opinionTime," + //办理意见填写时间
						 "FreckBack as opinionBody," + //办理意见填写内容
						 "null as opinionCreator" + //办理意见填写人
						 " from yxxgk"
				};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				PublicRequest publicRequest = (PublicRequest)bean;
				if(publicRequest.getReceiveMode()!=null) {
					publicRequest.setReceiveMode(publicRequest.getReceiveMode().replace(" ", "").replace("IsSelf", "自行领取/当场阅读、抄录").replace("IsPager", "自行领取/当场阅读、抄录").replace("IsMail", "邮寄").replace("IsExpress", "快递").replace("IsEmail", "电子邮件").replace("IsFax", "传真"));
				}
				if(publicRequest.getDoneReceiveMode()!=null) {
					publicRequest.setDoneReceiveMode(publicRequest.getDoneReceiveMode().replace(" ", "").replace("IsSelf", "自行领取/当场阅读、抄录").replace("IsPager", "自行领取/当场阅读、抄录").replace("IsMail", "邮寄").replace("IsExpress", "快递").replace("IsEmail", "电子邮件").replace("IsFax", "传真"));
				}
			}
		});
		return dataImporters;
	}
}