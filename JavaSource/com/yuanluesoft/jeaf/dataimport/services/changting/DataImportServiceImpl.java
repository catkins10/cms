package com.yuanluesoft.jeaf.dataimport.services.changting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.cms.messageboard.dataimporter.MessageBoardImporter;
import com.yuanluesoft.cms.messageboard.pojo.MessageBoard;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 长汀县政府(使用Access的网站)
 * @author linchuan
 *
 */
public class DataImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "长汀县政府";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		
		//站点数据导入
		dataImporters.add(new SiteDataImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select fClassID as directoryId," +
						   " fClassName as directoryName" +
						   " from tArticleClass" +
						   " where fParentId=0" +
						   " order by fClassID";
				}
				else { //子目录
					return "select fClassID as directoryId," +
						   " fClassName as directoryName" +
						   " from tArticleClass" +
						   " where fParentId=" + parentDirectoryId +
						   " order by fClassID";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " fArticleID as importDataId," + //被导入的记录ID
					   " fClassID as directoryId," + //原来的栏目ID
					   " fTitle as subject," + //标题
					   " fContent as body," + //正文
					   " Memo as subhead," + //副标题
					   " fCopyFrom as Source," + //来源
					   " fAuthor as Author," + //作者
					   " fKey as keyword," + //关键字
					   " fUpdateTime as created," + //创建时间
					   " fUpdateTime as issueTime," + //发布时间
					   " editor," + //创建者
					   " '' as orgName," + //创建者所在部门名称
					   " '' as unitName" + //创建者所在单位名称
					   " from tArticle"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter#getSameNameDirectory(java.lang.String, long)
			 */
			protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
				return null;
			}
		});
		
		
		//信息公开数据导入
		dataImporters.add(
			new PublicInfoImporter() {
				public String generateListChildDirectoriesSQL(String parentDirectoryId) {
					if(parentDirectoryId==null) { //第一级目录
						return "select fClassID as directoryId," +
							   " fClassName as directoryName" +
							   " from tArticleClass" +
							   " where fClassName like '%信息公开%' " +
							   " order by fClassID";
					}
					else { //子目录
						return "select fClassID as directoryId," +
							   " fClassName as directoryName" +
							   " from tArticleClass" +
							   " where fParentId=" + parentDirectoryId +
							   " order by fClassID";
					}
				}
				
				public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
					return new String[] {"select " +
							   "fArticleID as importDataId," + //被导入的记录ID
							   "fClassID as directoryId," + //原来的栏目ID
							   "'LY05' + IndexUnitID + '-' + IndexNo1 + IndexNo2 + '-' + TheYear + '-' + IndexCount as infoIndex," + //索引号
							   "(select min(OtherName) from tAdmin where tAdmin.fMailPurview=tArticle.IndexUnitID) as infoFrom," + //发布机构
							   "fAuthor as mark," + //文号
							   "fUpdateTime as generateDate," + //生成日期
							   "fTitle as subject," + //标题
							   "fContent as body," + //正文
							   "Memo as summarize," + //概述
							   "Editor as creator," + //创建人
							   "fUpdateTime as created," + //创建时间
							   "fUpdateTime as issueTime," + //发布时间
							   "'' as category," + //主题分类
							   "fKey as keywords," + //主题词
							   "'' as orgName," + //创建者所在部门名称
							   "'' as unitName" + //创建者所在单位名称
							   " from tArticle"};
				}

				/* (non-Javadoc)
				 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
				 */
				public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
					super.afterPojoGenerated(bean, resultSet, connection);
					PublicInfo publicInfo = (PublicInfo)bean;
					if(publicInfo.getInfoIndex().startsWith("LY05100")) {
						publicInfo.setInfoFrom("长汀县人民政府");
					}
					else if(publicInfo.getInfoIndex().startsWith("LY05101")) {
						publicInfo.setInfoFrom("县政府办公室");
					}
				}

				/* (non-Javadoc)
				 * @see com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter#getSameNameDirectory(java.lang.String, long)
				 */
				protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
					return null;
				}
			}
		);
		
		//信箱
		dataImporters.add(new LeaderMailImporter() {
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {"select " +
				 " ID as importDataId," + //被导入的记录ID
				 " UserID as sn," + //编号,在所有的公众服务中唯一
				 " Password as queryPassword," + //查询密码
				 " Tittle as subject," + //主题
				 " UContent as content," + //正文
				 " UserName as creator," + //创建人姓名
				 " SendTime as created," + //创建时间
				 " Tel as creatorTel," + //联系电话
				 " Email as creatorMail," + //邮箱
				 " iif(Sex='男', 'M', 'F') as creatorSex," + //性别 M/F
				 " null as creatorCertificateName," + //创建人证件名称
				 " null as creatorIdentityCard," + //创建人身份证/证件号码
				 " UserIP as creatorIP," + //创建人IP
				 " null as creatorMobile," + //创建人手机
				 " null as creatorFax," + //创建人传真
				 " null as creatorUnit," + //创建人所在单位
				 " null as creatorJob," + //创建人职业
				 " Address as creatorAddress," + //创建人地址
				 " Pcc as creatorPostalcode," + //创建人邮编
				 " '1' as isPublic," + //是否允许公开
				 " iif(Upassed=true, '1', '0') as publicPass," + //是否公开到网站
				 " iif(IfKey=true, '1', '0') as publicBody," + //是否公开正文
				 " iif(IfKey=true, '1', '0') as publicWorkflow," + //是否公开办理流程
				 " null as publicSubject," + //公开时的主题
				 " null as remark," + //附注
				 " null as popedom," + //事件辖区
				 " null as area," + //事件地点
				 " UType as type," + //类型
				 " SendTime as happenTime," + //事件时间
				 " fTime as opinionTime," + //办理意见填写时间
				 " fContent as opinionBody," + //办理意见填写内容
				 " fDepar as opinionCreator" + //办理意见填写人
				 " from UserAndGov"};
			}
		});
		
		//留言
		dataImporters.add(new MessageBoardImporter() {
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {"select " +
						 "ID as importDataId," + //被导入的记录ID
						 "ID as sn," + //编号,在所有的公众服务中唯一
						 "null as queryPassword," + //查询密码
						 "Comments as subject," + //主题
						 "Comments as content," + //正文
						 "UserName as creator," + //创建人姓名
						 "Postdate as created," + //创建时间
						 "null as creatorTel," + //联系电话
						 "UserMail as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "null as creatorCertificateName," + //创建人证件名称
						 "null as creatorIdentityCard," + //创建人身份证/证件号码
						 "IP as creatorIP," + //创建人IP
						 "null as creatorMobile," + //创建人手机
						 "null as creatorFax," + //创建人传真
						 "null as creatorUnit," + //创建人所在单位
						 "null as creatorJob," + //创建人职业
						 "null as creatorAddress," + //创建人地址
						 "null as creatorPostalcode," + //创建人邮编
						 "'1' as isPublic," + //是否允许公开
						 "iif(Online=true, '1', '0') as publicPass," + //是否公开到网站
						 "iif(Online=true, '1', '0') as publicBody," + //是否公开正文
						 "iif(Online=true, '1', '0') as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "null as remark," + //附注
						 "iif(Replay is null, null, dateadd('d', 1, Postdate)) as opinionTime," + //办理意见填写时间
						 "Replay as opinionBody," + //办理意见填写内容
						 "null as opinionCreator" + //办理意见填写人
						 " from book"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				if(bean instanceof MessageBoard) {
					MessageBoard messageBoard = (MessageBoard)bean;
					messageBoard.setSubject(StringUtils.slice(messageBoard.getSubject(), 200, "..."));
				}
			}
		});
		
		return dataImporters;
	}
}