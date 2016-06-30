package com.yuanluesoft.jeaf.dataimport.services.general;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.complaint.dataimporter.ComplaintImporter;
import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 远略ASP的系统
 * @author linchuan
 *
 */
public class DataImportServiceYunalueAspImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "远略ASP";
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
					return "select intClass_Id as directoryId," +
						   " strClass_Name as directoryName" +
						   " from YL_Class" +
						   " where intClass_Father=-1" +
						   " order by strClass_Name";
				}
				else { //其他目录
					return "select intClass_Id as directoryId," +
						   " strClass_Name as directoryName" +
						   " from YL_Class" +
						   " where intClass_Father=" + parentDirectoryId +
						   " order by strClass_Name";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "intArticle_Id as importDataId," + //被导入的记录ID
					   "intArticle_Catlog as directoryId," + //原来的栏目ID
					   "strArticle_Title as subject," + //标题
					   "strArticle_Text as body," + //正文
					   "'' as subhead," + //副标题
					   "strArticle_Source as source," + //来源
					   "strArticle_Subhead as mark," + //文号
					   "strArticle_Author as author," + //作者
					   "strArticle_Keyword as keyword," + //关键字
					   "dateArticle_Created as created," + //创建时间
					   "dateArticle_Issue as issueTime," + //发布时间
					   "strArticle_Editor as editor," + //创建者
					   "'' as orgName," + //创建者所在部门名称
					   "'' as unitName" + //创建者所在单位名称
					   " from YL_Article" +
					   " where ((strArticle_Text<>'' and strArticle_Text<>'<div>&nbsp;</div>')" +
					   " or intArticle_IsImg<>1)" +
					   " and intArticle_State=3",
					   
					  
					   "select " +
					   "intArticle_Id as importDataId," + //被导入的记录ID
					   "intArticle_Catlog as directoryId," + //原来的栏目ID
					   "strArticle_Title as subject," + //标题
					   "'<div align=\"center\"><img src=\"' & strArticle_ImgUrl & '\"><br/>' & strArticle_PicText & iif(strArticle_PicText=strArticle_Introduce, '', '<br/>' & strArticle_Introduce) & '</div>' as body," + //正文
					   "'' as subhead," + //副标题
					   "strArticle_Source as source," + //来源
					   "strArticle_Subhead as mark," + //文号
					   "strArticle_Author as author," + //作者
					   "strArticle_Keyword as keyword," + //关键字
					   "dateArticle_Created as created," + //创建时间
					   "dateArticle_Issue as issueTime," + //发布时间
					   "strArticle_Editor as editor," + //创建者
					   "'' as orgName," + //创建者所在部门名称
					   "'' as unitName" + //创建者所在单位名称
					   " from YL_Article" +
					   " where (strArticle_Text='' or strArticle_Text='<div>&nbsp;</div>')" +
					   " and intArticle_IsImg=1" +
					   " and strArticle_ImgUrl<>''" +
					   " and intArticle_State=3"
					};
			}
		});
		
		//投诉导入
		dataImporters.add(new ComplaintImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {"select " +
								     "intLeaveWord_Id as importDataId," + //被导入的记录ID" +
								     "intLeaveWord_Id as sn," + //编号
								     "'' as queryPassword," + //查询密码
								     "strLeaveWord_Title as subject," + //主题
								     "strLeaveWord_Content as content," + //正文
								     "strLeaveWord_Name as creator," + //创建人姓名
								     "dateLeaveWord_Created as created," + //创建时间
								     "strLeaveWord_Phone as creatorTel," + //联系电话
								     "strLeaveWord_Mail as creatorMail," + //邮箱
								     "'M' as creatorSex," + //性别 M/F
								     "'' as creatorCertificateName," + //创建人证件名称
								     "'' as creatorIdentityCard," + //创建人身份证/证件号码
								     "strLeaveWord_IP as creatorIP," + //创建人IP
								     "'' as creatorMobile," + //创建人手机
								     "'' as creatorFax," + //创建人传真
								     "strLeaveWord_WorkUnit as creatorUnit," + //创建人所在单位
								     "'' as creatorJob," + //创建人职业
								     "strLeaveWord_Address as creatorAddress," + //创建人地址
								     "strLeaveWord_PostCode as creatorPostalcode," + //创建人邮编
								     "'1' as isPublic," + //是否允许公开
								     "'' as remark," + //附注
								     "'' as popedom," + //事件辖区
								     "'' as area," + //事件地点
								     "'' as type," + //类型
								     "null as happenTime," + //事件时间
								     "dateLeaveWord_ReplyDate as opinionTime," + //办理意见填写时间
								     "strLeaveWord_Reply as opinionBody," + //办理意见填写内容
								     "strLeaveWord_Transact as opinionCreator" + //办理意见填写人
								     " from YL_LeaveWord" +
								     " where intLeaveWord_Type=(select intWordType_Id from YL_WordType where strWordType_Name like '%投诉%')"};
			}
		});
		
		//市长信箱导入
		dataImporters.add(new LeaderMailImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {"select " +
					     "intLeaveWord_Id as importDataId," + //被导入的记录ID" +
					     "intLeaveWord_Id as sn," + //编号
					     "'' as queryPassword," + //查询密码
					     "strLeaveWord_Title as subject," + //主题
					     "strLeaveWord_Content as content," + //正文
					     "strLeaveWord_Name as creator," + //创建人姓名
					     "dateLeaveWord_Created as created," + //创建时间
					     "strLeaveWord_Phone as creatorTel," + //联系电话
					     "strLeaveWord_Mail as creatorMail," + //邮箱
					     "'M' as creatorSex," + //性别 M/F
					     "'' as creatorCertificateName," + //创建人证件名称
					     "'' as creatorIdentityCard," + //创建人身份证/证件号码
					     "strLeaveWord_IP as creatorIP," + //创建人IP
					     "'' as creatorMobile," + //创建人手机
					     "'' as creatorFax," + //创建人传真
					     "strLeaveWord_WorkUnit as creatorUnit," + //创建人所在单位
					     "'' as creatorJob," + //创建人职业
					     "strLeaveWord_Address as creatorAddress," + //创建人地址
					     "strLeaveWord_PostCode as creatorPostalcode," + //创建人邮编
					     "'1' as isPublic," + //是否允许公开
					     "'' as remark," + //附注
					     "'' as popedom," + //事件辖区
					     "'' as area," + //事件地点
					     "'' as type," + //类型
					     "null as happenTime," + //事件时间
					     "dateLeaveWord_ReplyDate as opinionTime," + //办理意见填写时间
					     "strLeaveWord_Reply as opinionBody," + //办理意见填写内容
					     "strLeaveWord_Transact as opinionCreator" + //办理意见填写人
					     " from YL_LeaveWord" +
					     " where intLeaveWord_Type=(select intWordType_Id from YL_WordType where strWordType_Name like '%信箱%')"};
			}
		});
		return dataImporters;
	}
}
