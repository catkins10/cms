package com.yuanluesoft.jeaf.dataimport.services.songxi;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.cms.messageboard.dataimporter.MessageBoardImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 松溪县政府网站
 * @author linchuan
 *
 */
public class DataImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "松溪县政府网站";
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
					return "select classid as directoryId," +
						   " classname as directoryName" +
						   " from News_Class" +
						   " where upclass=0";
				}
				else { //子目录
					return "select classid as directoryId," +
						   " classname as directoryName" +
						   " from News_Class" +
						   " where upclass=" + parentDirectoryId;
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " ID as importDataId," + //被导入的记录ID
					   " ClassID as directoryId," + //原来的栏目ID
					   " title as subject," + //标题
					   " Content as body," + //正文
					   " '' as subhead," + //副标题
					   " ComeFrom as Source," + //来源
					   " Author," + //作者
					   " null as keyword," + //关键字
					   " WriteTime as created," + //创建时间
					   " WriteTime as issueTime," + //发布时间
					   " write_name as editor," + //创建者
					   " null as orgName," + //创建者所在部门名称
					   " null as unitName" + //创建者所在单位名称
					   " from News"
				};
			}
		});
		
		//政府信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select classid as directoryId," +
						   " classname as directoryName" +
						   " from infoOpen_channel" +
						   " where upclass=0";
				}
				else { //子目录
					return "select classid as directoryId," +
						   " classname as directoryName" +
						   " from infoOpen_channel" +
						   " where upclass=" + parentDirectoryId;
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " ID as importDataId," + //被导入的记录ID
					   " ClassID as directoryId," + //原来的栏目ID
					   " title as subject," + //标题
					   " content as body," + //正文
					   " sy_num as infoIndex," + //索引号
					   " iif(write_name='管理员', '县政府', write_name) as infoFrom," + //发布机构
					   " re_wen as mark," + //文号
					   " WriteTime as generateDate," + //生成日期
					   " '　' as summarize," + //内容概述
					   " null as category," + //主题分类
					   " null as keywords," + //关键字
					   " WriteTime+1 as created," + //创建时间
					   " WriteTime+1 as issueTime," + //发布时间
					   " write_name as creator," + //创建者
					   " iif(write_name='管理员', '县政府', write_name) as orgName," + //创建者所在部门名称
					   " iif(write_name='管理员', '县政府', write_name) as unitName" + //创建者所在单位名称
					   " from infoOpen_file"
				};
			}
		});
		
		//市长信箱
		dataImporters.add(new LeaderMailImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
					"select " +
					"ID as importDataId," + //被导入的记录ID
					"ID as sn," + //编号,在所有的公众服务中唯一
					"null as queryPassword," + //查询密码
					"Title as subject," + //主题
					"intro as content," + //正文
					"name as creator," + //创建人姓名
					"WriteTime as created," + //创建时间
					"tel as creatorTel," + //联系电话
					"email as creatorMail," + //邮箱
					"iif(sex='男', 'M', 'F') as creatorSex," + //性别 M/F
					"'' as creatorCertificateName," + //创建人证件名称
					"'' as creatorIdentityCard," + //创建人身份证/证件号码
					"getip as creatorIP," + //创建人IP
					"phone as creatorMobile," + //创建人手机
					"fax as creatorFax," + //创建人传真
					"company as creatorUnit," + //创建人所在单位
					"job as creatorJob," + //创建人职业
					"address as creatorAddress," + //创建人地址
					"zip as creatorPostalcode," + //创建人邮编
					"'1' as isPublic," + //是否允许公开
					"'1' as publicPass," + //是否公开到网站
					"'1' as publicBody," + //是否公开正文
					"'1' as publicWorkflow," + //是否公开办理流程
					"null as publicSubject," + //公开时的主题
					"'' as remark," + //附注
					"eare as popedom," + //事件辖区
					"sjaddress as area," + //事件地点
					"kind as type," + //类型
					"eventtime as happenTime," + //事件时间
					"iif(RepTime is null, WriteTime+3, RepTime) as opinionTime," + //办理意见填写时间
					"Reply as opinionBody," + //办理意见填写内容
					"'　' as opinionCreator" + //办理意见填写人
					" from Message"
				};
			}
		});
		
		//留言板
		dataImporters.add(new MessageBoardImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "gbook_classid as importDataId," + //被导入的记录ID
						 "gbook_classid as sn," + //编号,在所有的公众服务中唯一
						 "null as queryPassword," + //查询密码
						 "gbook_name as subject," + //主题
						 "intro as content," + //正文
						 "name as creator," + //创建人姓名
						 "writetime as created," + //创建时间
						 "tel as creatorTel," + //联系电话
						 "email as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "null as creatorCertificateName," + //创建人证件名称
						 "null as creatorIdentityCard," + //创建人身份证/证件号码
						 "getip as creatorIP," + //创建人IP
						 "null as creatorMobile," + //创建人手机
						 "null as creatorFax," + //创建人传真
						 "null as creatorUnit," + //创建人所在单位
						 "null as creatorJob," + //创建人职业
						 "null as creatorAddress," + //创建人地址
						 "null as creatorPostalcode," + //创建人邮编
						 "'1' as isPublic," + //是否允许公开
						 "'1' as publicPass," + //是否公开到网站
						 "'1' as publicBody," + //是否公开正文
						 "'1' as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "null as remark," + //附注
						 "null as opinionTime," + //办理意见填写时间
						 "null as opinionBody," + //办理意见填写内容
						 "null as opinionCreator" + //办理意见填写人
						 " from gbook_class"
				};
			}
		});
		return dataImporters;
	}
}