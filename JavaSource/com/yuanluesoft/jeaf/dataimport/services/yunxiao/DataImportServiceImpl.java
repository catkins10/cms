package com.yuanluesoft.jeaf.dataimport.services.yunxiao;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.messageboard.dataimporter.MessageBoardImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 云霄县政府网站
 * @author linchuan
 *
 */
public class DataImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "云霄县政府网站";
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
					return "select SID as directoryId," +
						   " S_Name as directoryName" +
						   " from ProSort" +
						   " WHERE FID=0";
				}
				else { //子目录
					return "select SID as directoryId," +
						   " S_Name as directoryName" +
						   " from ProSort" +
						   " where FID=" + parentDirectoryId + " and SID<>0";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " NID as importDataId," + //被导入的记录ID
					   " NClass as directoryId," + //原来的栏目ID
					   " Title as subject," + //标题
					   " Content as body," + //正文
					   " '' as subhead," + //副标题
					   " Original as Source," + //来源
					   " '' as Author," + //作者
					   " '' as keyword," + //关键字
					   " DateAndTime as created," + //创建时间
					   " DateAndTime as issueTime," + //发布时间
					   " Author as editor," + //创建者
					   " '云霄县政府' as orgName," + //创建者所在部门名称
					   " '云霄县政府' as unitName" + //创建者所在单位名称
					   " from BTXWorks_Info"
				};
			}
		});
		
		//留言板
		dataImporters.add(new MessageBoardImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
					"select " +
					"Gid as importDataId," + //被导入的记录ID
					"Gid as sn," + //编号,在所有的公众服务中唯一
					"'' as queryPassword," + //查询密码
					"GTitle as subject," + //主题
					"GContent as content," + //正文
					"GName as creator," + //创建人姓名
					"DateAndTime as created," + //创建时间
					"GTel as creatorTel," + //联系电话
					"GEmail as creatorMail," + //邮箱
					"'M' as creatorSex," + //性别 M/F
					"'' as creatorCertificateName," + //创建人证件名称
					"'' as creatorIdentityCard," + //创建人身份证/证件号码
					"GIp as creatorIP," + //创建人IP
					"GMobile as creatorMobile," + //创建人手机
					"'' as creatorFax," + //创建人传真
					"'' as creatorUnit," + //创建人所在单位
					"'' as creatorJob," + //创建人职业
					"GAddress as creatorAddress," + //创建人地址
					"'' as creatorPostalcode," + //创建人邮编
					"'0' as isPublic," + //是否允许公开
					"abs(IsShow) as publicPass," + //是否公开到网站
					"abs(IsShow) as publicBody," + //是否公开正文
					"abs(IsShow) as publicWorkflow," + //是否公开办理流程
					"null as publicSubject," + //公开时的主题
					"'' as remark," + //附注
					"GReplyDate as opinionTime," + //办理意见填写时间
					"GReplyContent as opinionBody," + //办理意见填写内容
					"GReplyMan as opinionCreator" + //办理意见填写人
					" from BTXWorks_GuestBook" +
					" where not Gemail  like '*222-*'" +
					" and not Gtel like '*222-*'"
				};
			}
		});
		return dataImporters;
	}
}