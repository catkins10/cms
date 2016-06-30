package com.yuanluesoft.jeaf.dataimport.services.pucheng;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.complaint.dataimporter.ComplaintImporter;
import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.inquiry.dataimporter.InquiryImporter;
import com.yuanluesoft.cms.leadermail.dataimporter.LeaderMailImporter;
import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.jeaf.dataimport.model.DirectoryMapping;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 浦城
 * @author linchuan
 *
 */
public class DataImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName(java.lang.String)
	 */
	public String getName() {
		return "浦城";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();
		
		dataImporters.add(new SiteDataImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select distinct 'first_' & FirstLevel as directoryId," +
						   " FirstLevel as directoryName" +
						   " from newstype" +
						   " union " +
						   "select '图片新闻'  as directoryId," +
						   " '图片新闻'  as directoryName" +
						   " from newstype" +
						   " where recid=1" +
						   " union " +
						   "select '网站信息'  as directoryId," +
						   " '网站信息'  as directoryName" +
						   " from newstype" +
						   " where recid=1";
				}
				else if(parentDirectoryId.startsWith("first_")) { //第二级目录
					return "select distinct 'second_' & FirstLevel & '_' & SecondLevel as directoryId," +
						   " SecondLevel as directoryName" +
						   " from newstype" +
						   " where FirstLevel='" + parentDirectoryId.substring("first_".length()) + "'";
				}
				else if(parentDirectoryId.startsWith("second_")) { //第三级目录
					String[] values = parentDirectoryId.split("_");
					return "select distinct 'third_' & FirstLevel & '_' & SecondLevel & '_' & ThirdLevel as directoryId," +
						   " ThirdLevel as directoryName" +
						   " from newstype" +
						   " where FirstLevel='" + values[1] + "'" +
						   " and  SecondLevel='" + values[2] + "'";
				}
				else if(parentDirectoryId.startsWith("third_")) { //第四级目录
					String[] values = parentDirectoryId.split("_");
					return "select recid as directoryId," +
						   " FourthLevel as directoryName" +
						   " from newstype" +
						   " where FirstLevel='" + values[1] + "'" +
						   " and  SecondLevel='" + values[2] + "'" +
						   " and  ThirdLevel='" + values[3] + "'";
				}
				else if(parentDirectoryId.startsWith("图片新闻")) { //图片新闻
					return "select 'picture_' & typename as directoryId," +
					   " typename as directoryName" +
					   " from pictype";
				}
				else if(parentDirectoryId.startsWith("网站信息")) { //网站信息
					return "select distinct 'infofirst_' & FirstLevel as directoryId," +
					   " FirstLevel as directoryName" +
					   " from infotype";
				}
				else if(parentDirectoryId.startsWith("infofirst_")) { //第二级目录
					return "select distinct 'infosecond_' & FirstLevel & '_' & SecondLevel as directoryId," +
						   " SecondLevel as directoryName" +
						   " from infotype" +
						   " where FirstLevel='" + parentDirectoryId.substring("infofirst_".length()) + "'";
				}
				else if(parentDirectoryId.startsWith("infosecond_")) { //第三级目录
					String[] values = parentDirectoryId.split("_");
					return "select distinct 'infothird_' & FirstLevel & '_' & SecondLevel & '_' & ThirdLevel as directoryId," +
						   " ThirdLevel as directoryName" +
						   " from infotype" +
						   " where FirstLevel='" + values[1] + "'" +
						   " and  SecondLevel='" + values[2] + "'";
				}
				else if(parentDirectoryId.startsWith("infothird_")) { //第四级目录
					String[] values = parentDirectoryId.split("_");
					return "select 'info_' & recid as directoryId," +
						   " FourthLevel as directoryName" +
						   " from infotype" +
						   " where FirstLevel='" + values[1] + "'" +
						   " and  SecondLevel='" + values[2] + "'" +
						   " and  ThirdLevel='" + values[3] + "'";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
					   "select " +
					   " recid as importDataId," + //被导入的记录ID
					   " newstypeid as directoryId," + //原来的栏目ID
					   " title as subject," + //标题
					   " content as body," + //正文
					   " '' as subhead," + //副标题
					   " Source," + //来源
					   " Author," + //作者
					   " '' as keyword," + //关键字
					   " lrtime as created," + //创建时间
					   " lrtime as issueTime," + //发布时间
					   " '' as editor," + //创建者
					   " '浦城县政府' as orgName," + //创建者所在部门名称
					   " '浦城县政府' as unitName" + //创建者所在单位名称
					   " from news",
					   
					   //图片新闻
					   "select " +
					   " 'picture_' & id as importDataId," + //被导入的记录ID
					   " 'picture_' & pictype as directoryId," + //原来的栏目ID
					   " picsub as subject," + //标题
					   " '<img src=\"/' & picname & '\">'  as body," + //正文
					   " '' as subhead," + //副标题
					   " '' as Source," + //来源
					   " pic_user as Author," + //作者
					   " '' as keyword," + //关键字
					   " lrtime as created," + //创建时间
					   " lrtime as issueTime," + //发布时间
					   " '' as editor," + //创建者
					   " '浦城县政府' as orgName," + //创建者所在部门名称
					   " '浦城县政府' as unitName" + //创建者所在单位名称
					   " from pic",
					   
					   //信息
					   "select " +
					   " 'info_' & recid as importDataId," + //被导入的记录ID
					   " 'info_' & infotypeid as directoryId," + //原来的栏目ID
					   " '[信息标题]' as subject," + //标题
					   " content as body," + //正文
					   " '' as subhead," + //副标题
					   " '' as Source," + //来源
					   " '' as Author," + //作者
					   " '' as keyword," + //关键字
					   " null as created," + //创建时间
					   " null as issueTime," + //发布时间
					   " '' as editor," + //创建者
					   " '浦城县政府' as orgName," + //创建者所在部门名称
					   " '浦城县政府' as unitName" + //创建者所在单位名称
					   " from news"
					};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter#getSameNameDirectory(java.lang.String, long)
			 */
			protected DirectoryMapping getSameNameDirectory(String directoryName, long targetSiteId) throws Exception {
				return null;
			}
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				SiteResource resource = (SiteResource)bean;
				if("[信息标题]".equals(resource.getSubject())) {
					if(resource.getBody()!=null) {
						resource.setSubject(StringUtils.slice(StringUtils.filterHtmlElement(resource.getBody(), false), 100, "..."));
					}
					resource.setCreated(DateTimeUtils.parseTimestamp("2010-01-01 00:00:00", null));
					resource.setIssueTime(resource.getCreated());
				}
			}
		});
		
		//信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select id as directoryId," +
						   " jgname as directoryName" +
						   " from mytype";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {"select " +
					   "id as importDataId," + //被导入的记录ID
					   "suoshuleibie as directoryId," + //原来的目录ID
					   "id as infoIndex," + //索引号
					   "fabujigou as infoFrom," + //发布机构
					   "beizhuwenhao as mark," + //文号
					   "addTime-2 as generateDate," + //生成日期
					   "title as subject," + //标题
					   "contents as body," + //正文
					   "fabujigou as creator," + //创建人
					   "addTime as created," + //创建时间
					   "addTime as issueTime," + //发布时间
					   "'' as summarize," + //内容概述
					   "'' as category," + //主题分类
					   "'' as keywords," + //主题词
					   "fabujigou as orgName," + //创建者所在部门名称
					   "fabujigou as unitName," + //创建者所在单位名称
					   "suoshuleibie as indexCategory" + //索引号
					   " from szfxxgk"
				};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#afterPojoGenerated(java.lang.Object, java.sql.ResultSet, java.sql.Connection)
			 */
			public void afterPojoGenerated(Object bean, ResultSet resultSet, Connection connection) throws Exception {
				super.afterPojoGenerated(bean, resultSet, connection);
				PublicInfo info = (PublicInfo)bean;
				info.setInfoIndex("NP07100-" + StringUtils.formatNumber(resultSet.getInt("indexCategory"), 2, true) + "00-" + DateTimeUtils.getYear(info.getGenerateDate()) + "-" + StringUtils.formatNumber(resultSet.getInt("infoIndex"), 5, true));
			}
		});
		
		//投诉导入
		dataImporters.add(new ComplaintImporter() {
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "recid as importDataId," + //被导入的记录ID
						 "codenum as sn," + //编号,在所有的公众服务中唯一
						 "pwd as queryPassword," + //查询密码
						 "title as subject," + //主题
						 "content," + //正文
						 "bfr as creator," + //创建人姓名
						 "lrtime as created," + //创建时间
						 "yphone as creatorTel," + //联系电话
						 "yemail as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "'' as creatorCertificateName," + //创建人证件名称
						 "'' as creatorIdentityCard," + //创建人身份证/证件号码
						 "cip as creatorIP," + //创建人IP
						 "ymobile as creatorMobile," + //创建人手机
						 "'' as creatorFax," + //创建人传真
						 "ydw as creatorUnit," + //创建人所在单位
						 "'' as creatorJob," + //创建人职业
						 "yaddress as creatorAddress," + //创建人地址
						 "'' as creatorPostalcode," + //创建人邮编
						 "'1' as isPublic," + //是否允许公开
						 "isfabu as publicPass," + //是否公开到网站
						 "isfabu as publicBody," + //是否公开正文
						 "isfabu as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "'' as remark," + //附注
						 "'' as popedom," + //事件辖区
						 "baddress as area," + //事件地点
						 "'' as type," + //类型
						 "lrtime as happenTime," + //事件时间
						 "retime as opinionTime," + //办理意见填写时间
						 "recontent as opinionBody," + //办理意见填写内容
						 "'' as opinionCreator" + //办理意见填写人
						 " from tsjb"
				};
			}
		});
		
		//在线调查导入
		dataImporters.add(new InquiryImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "recid as importDataId," + //被导入的记录ID
						 "votesubject as subject," + //调查主题
						 "null as endTime," + //调查截止时间
						 "'0' as isMultiSelect," + //是否多选, '1'/多选, '0'/单选
						 "'2' as publishResult," + //投票结果公示,0/不对外公开,1/投票结束后公开,2/总是公开
						 "'1' as isAnonymous," + //是否匿名投票, '1'、'0'
						 "0 as minVote," + //最小投票数,0表示不限制
						 "0 as maxVote," + //最大投票数,0表示不限制
						 "'' as creator," + //创建人
						 "null as created," + //创建时间
						 "null as inquiryOptions," + //选项列表
						 "null as inquiryOptionSeparator," + //选项列表分隔符
						 "null as inquiryVotes," + //投票数列表
						 "null as inquiryVotesSeparator" + //投票数列表分隔符
						 " from votesubject"
				};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.cms.inquiry.dataimporter.InquiryImporter#generateInquiryOptionsSql(java.lang.String)
			 */
			public String generateInquiryOptionsSql(String inquirySubjectId) {
				return "select " +
				 "votecontent as inquiryOption," + //选项
				 "'' as description," + //描述
				 "votenum as vote" + //投票数
				 " from votedetail" +
				 " where subjectid=" + inquirySubjectId;
			}
		});
		
		//信箱导入
		dataImporters.add(new LeaderMailImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#generateRetrieveDataSQL()
			 */
			public String[] generateRetrieveDataSQL() {
				return new String[] {
						"select " +
						 "id as importDataId," + //被导入的记录ID
						 "'' as sn," + //编号,在所有的公众服务中唯一
						 "'' as queryPassword," + //查询密码
						 "title as subject," + //主题
						 "content," + //正文
						 "lyname as creator," + //创建人姓名
						 "questdate as created," + //创建时间
						 "tel as creatorTel," + //联系电话
						 "email as creatorMail," + //邮箱
						 "'M' as creatorSex," + //性别 M/F
						 "'' as creatorCertificateName," + //创建人证件名称
						 "'' as creatorIdentityCard," + //创建人身份证/证件号码
						 "ip as creatorIP," + //创建人IP
						 "'' as creatorMobile," + //创建人手机
						 "'' as creatorFax," + //创建人传真
						 "unit as creatorUnit," + //创建人所在单位
						 "'' as creatorJob," + //创建人职业
						 "address as creatorAddress," + //创建人地址
						 "postcode as creatorPostalcode," + //创建人邮编
						 "'1' as isPublic," + //是否允许公开
						 "iif(clqk='办理完毕', '1', '0') as publicPass," + //是否公开到网站
						 "iif(clqk='办理完毕', '1', '0') as publicBody," + //是否公开正文
						 "iif(clqk='办理完毕', '1', '0') as publicWorkflow," + //是否公开办理流程
						 "null as publicSubject," + //公开时的主题
						 "'' as remark," + //附注
						 "'' as popedom," + //事件辖区
						 "'' as area," + //事件地点
						 "questiontype as type," + //类型
						 "questdate as happenTime," + //事件时间
						 "hfdate as opinionTime," + //办理意见填写时间
						 "hfnr as opinionBody," + //办理意见填写内容
						 "'' as opinionCreator" + //办理意见填写人
						 " from xzxx"
				};
			}
			
		});
		return dataImporters;
	}
}