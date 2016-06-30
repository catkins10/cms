package com.yuanluesoft.jeaf.dataimport.services.jianou;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.complaint.dataimporter.ComplaintImporter;
import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

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
		return "建瓯";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		//站点数据导入
		//dataImporters.add(
			new SiteDataImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select distinct subtype as directoryId," +
							" subtype as directoryName" +
							" from newstype order by subtype";
				}
				else {
					return "select distinct recid as directoryId," +
						   " typename as directoryName" +
						   " from newstype" +
						   " where subtype='" + parentDirectoryId + "'" +
						   " order by typename";
				}
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
									 "id as importDataId," + //被导入的记录ID
								   	 "newstypeid as directoryId," + //原来的栏目ID
								   	 "title as subject," + //标题
								   	 "content as body," + //正文
								   	 "'' as subhead," + //副标题
								   	 "source," + // as source," + //来源
								   	 "'' as author," + //作者
								   	 "'' as keyword," + //关键字
								   	 "lrtime as created," + //创建时间
								   	 "lrtime as issueTime," + //发布时间
								   	 "author as editor," + //创建者
								   	 "'' as orgName," + //创建者所在部门名称
								   	 "'' as unitName" + //创建者所在单位名称
								   	 " from news"};
			}
		};
		//);
		
		//信息公开数据导入
		//dataImporters.add(
			new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select distinct ('first_' + jgtype) as directoryId," +
						   " jgtype as directoryName" +
						   " from jgsz order by jgtype";
				}
				else if(parentDirectoryId.startsWith("first_")) { //第二级目录
					return "select distinct ('second_' + jgname) as directoryId," +
						   " jgname as directoryName" +
						   " from jgsz" +
						   " where jgtype='" + parentDirectoryId.substring("first_".length()) + "'" +
						   " order by jgname";
				}
				else if(parentDirectoryId.startsWith("second_")) { //第三级目录
					return "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '人事任免' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='0')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '工作规则' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='1')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '规范性文件' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='2')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '政府工作报告' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='3')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '总体规划' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='4')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '年度计划' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='5')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '统计信息' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='6')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '财政预算、决算报告' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='7')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '行政事业性收费' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='8')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '政府集中采购项目' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='9')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '行政许可' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='10')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '重点项目' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='11')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '为民办实事项目' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='12')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '扶贫、教育、医疗、社会保障、促进就业等方面的政策、措施及其实施' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='13')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '应急管理' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='14')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '环境保护、公共卫生、安全生产、食品药品、产品质量的监督检查情况' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='15')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '城乡建设和管理的重大事项' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='16')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '社会公益事业' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='17')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '征地拆迁' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='18')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '救灾、优抚、救济、社会捐助等款物的管理、使用、分配情况' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='19')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '工作动态' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='20')" +
						   "union" +
						   "(select distinct (fabujigou + '_' + suoshuleibie) as directoryId," +
						   " '其他应主动公开的政府信息' as directoryName" +
						   " from szfxxgk" +
						   " where fabujigou='" + parentDirectoryId.substring("second_".length()) + "'" +
						   " and suoshuleibie='21')";
				}
				return null;
			}
			
			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
						   "id as importDataId," + //被导入的记录ID
						   "(fabujigou + '_' + suoshuleibie) as directoryId," + //原来的目录ID
						   "id as infoIndex," + //索引号
						   "fabujigou as infoFrom," + //发布机构
						   "beizhuwenhao as mark," + //文号
						   "addtime as generateDate," + //生成日期
						   "title as subject," + //标题
						   "contents as body," + //正文
						   "'' as creator," + //创建人
						   "addtime as created," + //创建时间
						   "addtime as issueTime," + //发布时间
						   "gaishu as summarize," + //内容概述
						   "'' as category," + //主题分类
						   "'' as keywords," + //主题词
						   "fabujigou as orgName," + //创建者所在部门名称
						   "fabujigou as unitName," + //创建者所在单位名称
						   "generateDate as indexYear," + //索引号：年度
						   "suoshuleibie as directoryIndex" + //索引号：目录索引号
						   " from szfxxgk"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				if(bean instanceof PublicInfo) {
					String directoryIndex = resultSet.getString("directoryIndex");
					Date indexYear = resultSet.getDate("indexYear");
					int infoIndex = resultSet.getInt("infoIndex");
					((PublicInfo)bean).setInfoIndex("NP04100-" + (directoryIndex.length()==1 ? "0" : "") + directoryIndex + "00-" + DateTimeUtils.getYear(indexYear) + "-" + StringUtils.formatNumber(infoIndex, 5, true)); //索引号规则: NP04100-[suoshuleibie*2]00-[yyyy]-[id*5]
				}
			}
		};
		//);
		
		//投诉导入
		dataImporters.add(new ComplaintImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {"select " +
									 "id as importDataId," + //被导入的记录ID
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
									 "id as importDataId," + //被导入的记录ID
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
		
		return dataImporters;
	}
}
