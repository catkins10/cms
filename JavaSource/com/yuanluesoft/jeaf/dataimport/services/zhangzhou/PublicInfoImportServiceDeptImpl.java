package com.yuanluesoft.jeaf.dataimport.services.zhangzhou;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.opinion.dataimporter.PublicOpinionImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 漳州部门网站信息公开导入
 * @author linchuan
 *
 */
public class PublicInfoImportServiceDeptImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "漳州部门网站信息公开导入";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		
		//信息公开数据导入
		dataImporters.add(
			new PublicInfoImporter() {
				public String generateListChildDirectoriesSQL(String parentDirectoryId) {
					if(parentDirectoryId==null) { //第一级目录
						return "select fid as directoryId," +
							   " LMname as directoryName" +
							   " from LMTab" +
							   " order by LMname";
					}
					return null;
				}
				
				public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
					return new String[] {"select " +
							   "id as importDataId," + //被导入的记录ID
							   "LMid as directoryId," + //原来的栏目ID
							   "indexno as infoIndex," + //索引号
							   "(select max(pname) from gv_part) as infoFrom," + //发布机构
							   "wh as mark," + //文号
							   "indate as generateDate," + //生成日期
							   "title as subject," + //标题
							   "content as body," + //正文
							   "check_name as creator," + //创建人
							   "indate1 as created," + //创建时间
							   "indate1 as issueTime," + //发布时间
							   "'' as summarize," + //内容概述
							   "'' as category," + //主题分类
							   "keywords," + //主题词
							   "(select max(pname) from gv_part) as orgName," + //创建者所在部门名称
							   "(select max(pname) from gv_part) as  unitName" + //创建者所在单位名称
							   " from newsTab"};
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
		
		//信息公开意见数据导入
		dataImporters.add(
			new PublicOpinionImporter() {
				public String[] generateRetrieveDataSQL() {
					return new String[] {
						"select " +
						"id as importDataId," + //被导入的记录ID
						 "id as sn," + //编号,在所有的公众服务中唯一
						 "'' as queryPassword," + //查询密码
						 "title as subject," + //主题
						 "cont as content," + //正文
						 "name as creator," + //创建人姓名
						 "intime as created," + //创建时间
						 "tel as creatorTel," + //联系电话
						 "email as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "'' as creatorCertificateName," + //创建人证件名称
						 "'' as creatorIdentityCard," + //创建人身份证/证件号码
						 "inip as creatorIP," + //创建人IP
						 "'' as creatorMobile," + //创建人手机
						 "'' as creatorFax," + //创建人传真
						 "'' as creatorUnit," + //创建人所在单位
						 "'' as creatorJob," + //创建人职业
						 "address as creatorAddress," + //创建人地址
						 "'' as creatorPostalcode," + //创建人邮编
						 "'1' as isPublic," + //是否允许公开
						 "'' as remark," + //附注
						 "'' as opinionTime," + //办理意见填写时间
						 "'' as opinionBody," + //办理意见填写内容
						 "'' as opinionCreator" + //办理意见填写人
						 " from zfgk_yj"
					};
				}
			}
		);
		return dataImporters;
	}
}