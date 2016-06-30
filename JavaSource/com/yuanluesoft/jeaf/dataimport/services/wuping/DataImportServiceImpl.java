package com.yuanluesoft.jeaf.dataimport.services.wuping;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 
 * @author linchuan
 *
 */
public class DataImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "武平县政府网站";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		//站点数据导入
		dataImporters.add(
			new SiteDataImporter() {
				public String generateListChildDirectoriesSQL(String parentDirectoryId) {
					if(parentDirectoryId==null) { //第一级目录
						return "(select distinct ('first_' & wp_BigClassId) as directoryId," +
							   " wp_BigClassName as directoryName" +
							   " from wp_BigClass" +
							   " where wp_BigClassId not in (select wp_BigClassId FROM  wp_Article where aindex<>'')" +
							   " order by wp_BigClassName)" +
							   " union " +
							   "(select 'board' as directoryId," +
							   " '公告栏' as directoryName" +
							   " from wp_board" +
							   " where id=16)";
					}
					else if(parentDirectoryId.startsWith("first_")) { //第二级目录
						return "select distinct ('second_' & wp_SmallClassId) as directoryId," +
							   " wp_SmallClassName as directoryName" +
							   " from  wp_SmallClass" +
							   " where wp_BigClassId=" + parentDirectoryId.substring("first_".length()) +
							   " order by wp_SmallClassName";
					}
					return null;
				}
	
				public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
					return new String[] {"select " +
										 "wp_ArticleId as importDataId," + //被导入的记录ID
										 "iif(wp_smallclassid is null, 'first_' & wp_bigclassid, 'second_' & wp_smallclassid) as directoryId," + //原来的目录ID
										 "Title as subject," + //标题
										 "Content as body," + //正文
										 "'' as subhead," + //副标题
										 "'' as source," + //来源
									   	 "author," + //作者
									   	 "'' as keyword," + //关键字
									   	 "UpdateTime as created," + //创建时间
									   	 "UpdateTime as issueTime," + //发布时间
									   	 "editor," + //创建者
									   	 "Original as orgName," + //创建者所在部门名称
									   	 "Original as unitName" + //创建者所在单位名称
										 " from wp_Article" +
										 " where aindex is null" +
										 " or aindex=''",
										 
										 "select " +
										 "'board_' & id as importDataId," + //被导入的记录ID
										 "'board' as directoryId," + //原来的目录ID
										 "Title as subject," + //标题
										 "Content as body," + //正文
										 "'' as subhead," + //副标题
										 "'' as source," + //来源
									   	 "''," + //作者
									   	 "'' as keyword," + //关键字
									   	 "dateandtime as created," + //创建时间
									   	 "dateandtime as issueTime," + //发布时间
									   	 "upload as editor," + //创建者
									   	 "'' as orgName," + //创建者所在部门名称
									   	 "'' as unitName" + //创建者所在单位名称
										 " from wp_board"};
				}

				/* (non-Javadoc)
				 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String, java.sql.ResultSet, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
				 */
				public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
					if(htmlBody==null) {
						return htmlBody;
					}
					
					//替换<?xml.../>和图片路径
					htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
					String[] prefix = {"uploadfile", "jpg", "doc", "rar", "pdf", "images", "down"};
					for(int i=0; i<prefix.length; i++) {
						htmlBody = htmlBody.replaceAll("src=\"" + prefix[i], "src=\"" + parameter.getSourceSiteURL() + prefix[i]).replaceAll("href=\"" + prefix[i], "href=\"" + parameter.getSourceSiteURL() + prefix[i]);
					}
					//替换<?xml.../>和图片路径
					htmlBody = htmlBody.replaceAll("(?i)href=\"wpG_Download.asp", "href=\"" + parameter.getSourceSiteURL() + "wpG_Download.asp");
					return htmlBody;
				}
			}
		);
		
		//信息公开数据导入
		dataImporters.add(
			new PublicInfoImporter() {
				public String generateListChildDirectoriesSQL(String parentDirectoryId) {
					if(parentDirectoryId==null) { //第一级目录
						return "select distinct ('first_' & wp_BigClassId) as directoryId," +
							   " wp_BigClassName as directoryName" +
							   " from wp_BigClass" +
							   " where wp_BigClassId in (select wp_BigClassId FROM  wp_Article where aindex<>'')" +
							   " order by wp_BigClassName";
					}
					else if(parentDirectoryId.startsWith("first_")) { //第二级目录
						return "select distinct ('second_' & wp_SmallClassId) as directoryId," +
							   " wp_SmallClassName as directoryName" +
							   " from  wp_SmallClass" +
							   " where wp_BigClassId=" + parentDirectoryId.substring("first_".length()) +
							   " order by wp_SmallClassName";
					}
					return null;
				}
				
				public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
					return new String[] {"select " +
							   "wp_ArticleId as importDataId," + //被导入的记录ID
							   "iif(wp_smallclassid is null, 'first_' & wp_bigclassid, 'second_' & wp_smallclassid) as directoryId," + //原来的目录ID
							   "aindex as infoIndex," + //索引号
							   "Original as infoFrom," + //发布机构
							   "beizhu as mark," + //文号
							   "UpdateTime as generateDate," + //生成日期
							   "Title as subject," + //标题
							   "Content as body," + //正文
							   "Author as creator," + //创建人
							   "UpdateTime as created," + //创建时间
							   "UpdateTime as issueTime," + //发布时间
							   "G_Content as summarize," + //内容概述
							   "'' as category," + //主题分类
							   "'' as keywords," + //主题词
							   "Original as orgName," + //创建者所在部门名称
							   "Original as unitName" + //创建者所在单位名称
							   " from wp_Article" +
							   " where aindex<>''"};
				}

				/* (non-Javadoc)
				 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String, java.sql.ResultSet, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
				 */
				public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
					if(htmlBody==null) {
						return htmlBody;
					}
					
					//替换<?xml.../>和图片路径
					htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
					String[] prefix = {"uploadfile", "jpg", "doc", "rar", "pdf", "images", "down"};
					for(int i=0; i<prefix.length; i++) {
						htmlBody = htmlBody.replaceAll("src=\"" + prefix[i], "src=\"" + parameter.getSourceSiteURL() + prefix[i]).replaceAll("href=\"" + prefix[i], "href=\"" + parameter.getSourceSiteURL() + prefix[i]);
					}
					return htmlBody;
				}
			}
		);
		
		//投诉导入
		/*dataImporters.add(new ComplaintImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {"select " +
								     "codenum as sn," + //编号
								     "pwd as queryPassword," + //查询密码
								     "title as subject," + //主题
								     "content," + //正文
								     "ydw as creator," + //创建人姓名
								     "lrtime as created," + //创建时间
								     "yphone as creatorTel," + //联系电话
								       "yemail as creatorMail," + //邮箱
								     "'M' as creatorSex," + //性别 M/F
								     "'' as creatorCertificateName," + //创建人证件名称
								     "'' as creatorIdentityCard," + //创建人身份证/证件号码
								     "cip as creatorIP," + //创建人IP
								     "ymobile as creatorMobile," + //创建人手机
								     "'' as creatorFax," + //创建人传真
								     "'' as creatorUnit," + //创建人所在单位
								     "'' as creatorJob," + //创建人职业
								     "yaddress as creatorAddress," + //创建人地址
								     "'' as creatorPostalcode," + //创建人邮编
								     "'1' as isPublic," + //是否允许公开
								     "'' as remark," + //附注
								     "'' as popedom," + //事件辖区
								     "'' as area," + //事件地点
								     "'' as type," + //类型
								     "lrtime as happenTime," + //事件时间
								     "retime as opinionTime," + //办理意见填写时间
								     "recontent as opinionBody," + //办理意见填写内容
								     "'' as opinionCreator" + //办理意见填写人
								     " from tsjb"};
			}
		});
		
		//市长信箱导入
		dataImporters.add(new LeaderMailImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {"select " +
								     "ccodenum as sn," + //编号
								     "'' as queryPassword," + //查询密码
								     "ctitle as subject," + //主题
								     "content," + //正文
								     "cname as creator," + //创建人姓名
								     "lrtime as created," + //创建时间
								     "cphone as creatorTel," + //联系电话
								     "cmail as creatorMail," + //邮箱
								     "'M' as creatorSex," + //性别 M/F
								     "'' as creatorCertificateName," + //创建人证件名称
								     "'' as creatorIdentityCard," + //创建人身份证/证件号码
								     "cip as creatorIP," + //创建人IP
								     "'' as creatorMobile," + //创建人手机
								     "'' as creatorFax," + //创建人传真
								     "cdanwei as creatorUnit," + //创建人所在单位
								     "'' as creatorJob," + //创建人职业
								     "caddress as creatorAddress," + //创建人地址
								     "ccode as creatorPostalcode," + //创建人邮编
								     "'1' as isPublic," + //是否允许公开
								     "'' as remark," + //附注
								     "'' as popedom," + //事件辖区
								     "'' as area," + //事件地点
								     "ctype as type," + //类型
								     "lrtime as happenTime," + //事件时间
								     "retime as opinionTime," + //办理意见填写时间
								     "recontent as opinionBody," + //办理意见填写内容
								     "'' as opinionCreator" + //办理意见填写人
								     " from guess"};
			}
		});
		*/
		return dataImporters;
	}
}
