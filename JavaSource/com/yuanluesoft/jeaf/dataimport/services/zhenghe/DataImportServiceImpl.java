package com.yuanluesoft.jeaf.dataimport.services.zhenghe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.messageboard.dataimporter.MessageBoardImporter;
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
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "政和";
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
					return "(select 'first_公告' as directoryId," +
						   " '公告' as directoryName" +
						   " from fg_class" +
						   " where class_id=(select max(class_id) from fg_class))" +
						   " union " +
						   "(select distinct ('first_' + class_lm) as directoryId," +
						   " class_lm as directoryName" +
						   " from fg_class order by class_lm)";
				}
				else {
					return "select distinct ('second_' + class_name) as directoryId," +
						   " class_name as directoryName" +
						   " from fg_class" +
						   " where class_lm='" + parentDirectoryId.substring("first_".length()) + "'" +
						   " order by class_name";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "iif(news_secclass='', 'first_' + news_mainclass, 'second_' + news_secclass) as directoryId," + //原来的栏目ID
					   "news_title as subject," + //标题
					   "news_text as body," + //正文
					   "'' as subhead," + //副标题
					   "'' as source," + //来源
					   "'' as author," + //作者
					   "'' as keyword," + //关键字
					   "news_time as created," + //创建时间
					   "news_time as issueTime," + //发布时间
					   "news_writer as editor," + //创建者
					   "news_from as orgName," + //创建者所在部门名称
					   "news_from as unitName" + //创建者所在单位名称
					   " from fg_dj",
					   
					   "select " +
					   "('first_公告') as directoryId," + //原来的栏目ID
					   "news_title as subject," + //标题
					   "news_text as body," + //正文
					   "'' as subhead," + //副标题
					   "'' as source," + //来源
					   "'' as author," + //作者
					   "'' as keyword," + //关键字
					   "news_time as created," + //创建时间
					   "news_time as issueTime," + //发布时间
					   "'管理员' as editor," + //创建者
					   "'政和县人民政府' as orgName," + //创建者所在部门名称
					   "'政和县人民政府' as unitName" + //创建者所在单位名称
					   " from fg_gg"};
			}
		});
		
		
		//信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录,各部门
					return "select distinct ('first_' + news_from) as directoryId," +
							" news_from as directoryName" +
							" from info" +
							" order by news_from";
				}
				else if(parentDirectoryId.startsWith("first_")) { //第二级目录,主分类
					return "select distinct ('second_' + news_mainclass + '_' + news_from) as directoryId," +
							" news_mainclass as directoryName" +
							" from info" +
							" where news_from='" + parentDirectoryId.substring("first_".length()) + "'" +
							" order by news_mainclass";
				}
				else if(parentDirectoryId.startsWith("second_")) { //第三级目录,二级分类
					parentDirectoryId = parentDirectoryId.substring("second_".length());
					String[] names = parentDirectoryId.split("_");
					return "select distinct ('third_' + news_secclass + '_' + news_mainclass + '_' + news_from) as directoryId," +
						   " news_secclass as directoryName" +
						   " from info" +
						   " where news_from='" + names[1] + "'" +
						   " and news_mainclass='" + names[0] + "'" +
						   " and news_secclass<>''" +
						   " order by news_secclass";
				}
				return null;
			}
			
			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "iif(news_secclass='', 'second_' + news_mainclass + '_' + news_from, 'third_' + news_secclass + '_' + news_mainclass + '_' + news_from) as directoryId," + //原来的目录ID
					   "news_lsh as infoIndex," + //索引号
					   "news_from as infoFrom," + //发布机构
					   "news_hw as mark," + //文号
					   "news_sy as generateDate," + //生成日期
					   "news_title as subject," + //标题
					   "('<a href=\"' + news_picurl + '\">点击此处下载原文件</a>') as body," + //正文
					   "news_writer as creator," + //创建人
					   "news_time as created," + //创建时间
					   "news_time as issueTime," + //发布时间
					   "'' as summarize," + //内容概述
					   "'' as category," + //主题分类
					   "'' as keywords," + //主题词
					   "news_from as orgName," + //创建者所在部门名称
					   "news_from as unitName," + //创建者所在单位名称
					   "news_zt as indexYear," + //索引号：年度
					   "(select ju_bm from ju where ju_class=info.news_from) as unitCode," + //索引号：单位编码
					   "(select main_px from gbclass where main_class=info.news_mainclass) as dirIndex1," + //索引号：目录索引号
					   "(select class_px from gxclass where class_name=info.news_secclass) as dirIndex2" + //索引号：目录索引号
					   " from info"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				if(bean instanceof PublicInfo) {
					//重信息索引号
					PublicInfo info = (PublicInfo)bean;
					String indexYear = resultSet.getString("indexYear");
					String unitCode = resultSet.getString("unitCode");
					String dirIndex1 = resultSet.getString("dirIndex1");
					String dirIndex2 = resultSet.getString("dirIndex2");
					info.setInfoIndex("NP10" + unitCode + "-" + dirIndex1 + (dirIndex2==null || dirIndex2.equals("") ? "00" : dirIndex2) + "-" + indexYear + "-" + info.getInfoIndex());
				}
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String)
			 */
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				//替换<?xml.../>和图片路径
				htmlBody = htmlBody.replaceAll("href=\"UploadFiles", "href=\"" + parameter.getSourceSiteURL() + "UploadFiles");
				return super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
			}
		});
		
		//留言板导入
		dataImporters.add(new MessageBoardImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "(year(news_time) & '' & month(news_time) & day(news_time) & id) as sn," + //编号
					   "'' as queryPassword," + //查询密码
					   "news_title as subject," + //主题
					   "news_text as content," + //正文
					   "news_writer as creator," + //创建人姓名
					   "news_time as created," + //创建时间
					   "news_phone as creatorTel," + //联系电话
					   "'' as creatorMail," + //邮箱
					   "'M' as creatorSex," + //性别 M/F
					   "'身份证' as creatorCertificateName," + //创建人证件名称
					   "news_idcard as creatorIdentityCard," + //创建人身份证/证件号码
					   "news_ip as creatorIP," + //创建人IP
					   "'' as creatorMobile," + //创建人手机
					   "'' as creatorFax," + //创建人传真
					   "'' as creatorUnit," + //创建人所在单位
					   "'' as creatorJob," + //创建人职业
					   "'' as creatorAddress," + //创建人地址
					   "'' as creatorPostalcode," + //创建人邮编
					   "'1' as isPublic," + //是否允许公开
					   "'' as remark," + //附注
					   "news_time as opinionTime," + //办理意见填写时间
					   "news_hftext as opinionBody," + //办理意见填写内容
					   "'' as opinionCreator" + //办理意见填写人
					   " from guestbook"};
			}
		});
		
		return dataImporters;
	}
}
