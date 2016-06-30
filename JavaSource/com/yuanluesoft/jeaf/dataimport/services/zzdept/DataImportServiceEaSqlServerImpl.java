package com.yuanluesoft.jeaf.dataimport.services.zzdept;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 漳州部门网站(使用SqlServer的网站),地震局
 * @author linchuan
 *
 */
public class DataImportServiceEaSqlServerImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "漳州地震局";
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
					return "select id as directoryId," +
						   " name as directoryName" +
						   " from columns" +
						   " where parent is null" +
						   " union (select 0 as directoryId," +
						   " '其他' as directoryName" +
						   " from columns" +
						   " where id=(select min(id) from columns))";
				}
				else { //子目录
					return "select id as directoryId," +
						   " name as directoryName" +
						   " from columns" +
						   " where parent=" + parentDirectoryId +
						   " order by name";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   "id as importDataId," + //被导入的记录ID
					   "(case when (select weblets.cid from weblets where weblets.id=(select min(docset.wid) from docset where docset.docid=doc.id)) is null then 0 else (select weblets.cid from weblets where weblets.id=(select min(docset.wid) from docset where docset.docid=doc.id)) end) as directoryId," + //原来的栏目ID
					   "title as subject," + //标题
					   "(case when substring(content, 1, 5)='/data' then '<a href=\"' + cast(content as varchar(500)) + '\">' + title + '</a>' else content end) as body," + //正文
					   "'' as subhead," + //副标题
					   "'' as source," + //来源
					   "authors as author," + //作者
					   "keyword," + //关键字
					   "apdate as created," + //创建时间
					   "pdate as issueTime," + //发布时间
					   "'　' as Editor," + //创建者
					   "'' as orgName," + //创建者所在部门名称
					   "'' as unitName" + //创建者所在单位名称
					   " from doc",
					   
					   "select " +
					   "replace(cid_list, ';', '') as directoryId," + //原来的栏目ID
					   "title as subject," + //标题
					   "(case when substring(content, 1, 5)='/data' then '<a href=\"' + cast(content as varchar(500)) + '\">' + title + '</a>' else content end) as body," + //正文
					   "'' as subhead," + //副标题
					   "'' as source," + //来源
					   "'' as author," + //作者
					   "'' as keyword," + //关键字
					   "apdate as created," + //创建时间
					   "pdate as issueTime," + //发布时间
					   "'　' as Editor," + //创建者
					   "'' as orgName," + //创建者所在部门名称
					   "'' as unitName" + //创建者所在单位名称
					   " from paper" +
					   " where title not in (select title from doc)"};
			}

			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				if(htmlBody==null) {
					return htmlBody;
				}
				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				return htmlBody.replaceAll("<META http-equiv=Content-Type content=\"text/html; charset=gb2312\">", "");
				//return htmlBody.replaceAll("src=\"UploadFiles", "src=\"" + sourceSiteURL + "UploadFiles").replaceAll("href=\"UploadFiles", "href=\"" + sourceSiteURL + "UploadFiles").replaceAll("src=\"images", "src=\"" + sourceSiteURL + "images").replaceAll("href=\"images", "href=\"" + sourceSiteURL + "images");
			}
		});
		
		//信息公开数据导入
		dataImporters.add(
			new PublicInfoImporter() {
				public String generateListChildDirectoriesSQL(String parentDirectoryId) {
					if(parentDirectoryId==null) { //第一级目录
						return "select id as directoryId," +
							   " name as directoryName" +
							   " from gk_columns" +
							   " where parent=0" +
							   " order by name";
					}
					else { //子目录
						return "select id as directoryId," +
							   " name as directoryName" +
							   " from gk_columns" +
							   " where parent=" + parentDirectoryId +
							   " order by name";
					}
				}
				
				public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
					return new String[] {"select " +
							   "id as importDataId," + //被导入的记录ID
							   "sort as directoryId," + //原来的栏目ID
							   "sid as infoIndex," + //索引号
							   "department as infoFrom," + //发布机构
							   "remark as mark," + //文号
							   "CONVERT(datetime, createdate, 112) as generateDate," + //生成日期
							   "title as subject," + //标题
							   "content as body," + //正文
							   "department as creator," + //创建人
							   "[update] as created," + //创建时间
							   "[update] as issueTime," + //发布时间
							   "summary as summarize," + //内容概述
							   "'' as category," + //主题分类
							   "'' as keywords," + //主题词
							   "department as orgName," + //创建者所在部门名称
							   "department as unitName" + //创建者所在单位名称
							   " from gk_doc"};
				}

				public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
					if(htmlBody==null) {
						return htmlBody;
					}
					return super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
					//return htmlBody.replaceAll("src=\"UploadFiles", "src=\"" + sourceSiteURL + "UploadFiles").replaceAll("href=\"UploadFiles", "href=\"" + sourceSiteURL + "UploadFiles").replaceAll("src=\"images", "src=\"" + sourceSiteURL + "images").replaceAll("href=\"images", "href=\"" + sourceSiteURL + "images");
				}
			}
		);
		
		//局长信箱数据导入
		dataImporters.add(
			new LeaderMailImporter() {
				public String[] generateRetrieveDataSQL() {
					return new String[] {
						"select " +
						"id as importDataId," + //被导入的记录ID
						 "id as sn," + //编号,在所有的公众服务中唯一
						 "'' as queryPassword," + //查询密码
						 "title as subject," + //主题
						 "content," + //正文
						 "name as creator," + //创建人姓名
						 "pdate as created," + //创建时间
						 "'' as creatorTel," + //联系电话
						 "email as creatorMail," + //邮箱
						 "(case when salutation='先生' then 'M' else 'F' end) as creatorSex," + //性别 M/F
						 "'身份证' as creatorCertificateName," + //创建人证件名称
						 "'' as creatorIdentityCard," + //创建人身份证/证件号码
						 "'' as creatorIP," + //创建人IP
						 "'' as creatorMobile," + //创建人手机
						 "'' as creatorFax," + //创建人传真
						 "'' as creatorUnit," + //创建人所在单位
						 "'' as creatorJob," + //创建人职业
						 "'' as creatorAddress," + //创建人地址
						 "'' as creatorPostalcode," + //创建人邮编
						 "'0' as isPublic," + //是否允许公开
						 "'' as remark," + //附注
						 "'' as popedom," + //事件辖区
						 "'' as area," + //事件地点
						 "'' as type," + //类型
						 //" as happenTime," + //事件时间
						 "cdate as opinionTime," + //办理意见填写时间
						 "replied_content as opinionBody," + //办理意见填写内容
						 "replied_by as opinionCreator" + //办理意见填写人
						 " from pmb_list"
					};
				}
			}
		);
		return dataImporters;
	}
}