package com.yuanluesoft.jeaf.dataimport.services.changtai;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.infopublic.dataimporter.PublicInfoImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 长泰部门信息公开
 * @author linchuan
 *
 */
public class PublicInfoDeptImportServiceImpl extends DataImportService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "长泰部门信息公开";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//政府信息公开数据导入
		dataImporters.add(new PublicInfoImporter() {
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select 'first_' & E_typeid as directoryId," +
							" E_typename as directoryName" +
							" from EC_type" +
							" order by E_typename";
				}
				else if(parentDirectoryId.startsWith("first_")) {
					return "select 'second_' & E_BigClassID as directoryId," +
						   " E_BigClassName as directoryName" +
						   " from EC_BigClass" +
						   " where E_typeid=" + parentDirectoryId.substring("first_".length()) +
						   " order by E_BigClassName";
				}
				else if(parentDirectoryId.startsWith("second_")) {
					return "select 'third_' & E_SmallClassID as directoryId," +
						   " E_smallclassname as directoryName" +
						   " from EC_smallclass" +
						   " where E_BigClassID=" + parentDirectoryId.substring("second_".length()) +
						   " order by E_smallclassname";
				}
				return null;
			}

			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						"select " +
						 "EC_ArticleID as importDataId," + //被导入的记录ID
						 "iif(E_smallclassid is null, iif(E_bigclassid is null, 'first_' & E_typeid, 'second_' & E_bigclassid), 'third_' & E_smallclassid) as directoryId," + //原来的目录ID
						 "AIndex as infoIndex," + //索引号
						 "Original as infoFrom," + //发布机构
						 "BeiZhu as mark," + //文号
						 "UpdateTime as generateDate," + //生成日期
						 "Title as subject," + //标题
						 "Content as body," + //正文
						 "editor as creator," + //创建人
						 "UpdateTime as created," + //创建时间
						 "UpdateTime as issueTime," + //发布时间
						 "G_Content as summarize," + //内容概述
						 "null as category," + //主题分类
						 "about as keywords," + //主题词
						 "Original as orgName," + //创建者所在部门名称
						 "Original as unitName" + //创建者所在单位名称
						 " from EC_Article"};
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DataImporter#updateAttachmentPath(java.lang.String, java.sql.ResultSet, java.sql.Connection, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
			 */
			public String updateAttachmentPath(String htmlBody, ResultSet resultSet, Connection connection, DataImportParameter parameter) {
				//<P align=center><EMBED src=uploadfile/wmv/2011-12/一周要闻20111225.wmv width=360 height=315 type=video/x-ms-wmv loop="-1" autostart="true"></EMBED><BR><A href="">下载地址</A></P>
				//<P align=center><EMBED src=uploadfile/wmv/2011-7/2011715201152495.wmv width=360 height=315 type=video/x-ms-wmv loop="-1" autostart="true"></EMBED>
				//请点击图标下载浏览：<BR><A href="ECDownload.asp?FileName=doc/2008-10/20081030143551343.doc"><IMG src="TEMPLETS/0/images/doc.gif" border=0></A><BR>附件
				//<A title=点击图片看全图 href="uploadfile/jpg/2008-8/20088718123750.jpg" target=_blank><P align=center><IMG alt="" src="uploadfile/jpg/2008-8/20088718123750.jpg" border=0></A><BR></P>
				//<P align=center><EMBED src=uploadfile/wmv/2009-10/20091019175531518.wmv width=360 height=315 type=video/x-ms-wmv loop="-1" autostart="true"></EMBED><BR><A href="uploadfile/wmv/2009-10/20091019175531518.wmv">下载地址uploadfile/wmv/2009-10/20091019175531518.wmv</A></P>

				htmlBody = super.updateAttachmentPath(htmlBody, resultSet, connection, parameter);
				if(htmlBody==null || htmlBody.isEmpty()) {
					return htmlBody;
				}
				String[] keywords = {"uploadfile", "ECDownload", "TEMPLETS"};
				for(int i=0; i<keywords.length; i++) {
					htmlBody = htmlBody.replaceAll("(?i)src=\"" + keywords[i], "src=\"" + parameter.getSourceSiteURL() + keywords[i])
								   	   .replaceAll("(?i)src=" + keywords[i], "src=" + parameter.getSourceSiteURL() + keywords[i])
									   .replaceAll("(?i)href=\"" + keywords[i], "href=\"" + parameter.getSourceSiteURL() + keywords[i])
									   .replaceAll("(?i)href=" + keywords[i], "href=" + parameter.getSourceSiteURL() + keywords[i]);
				}
				return htmlBody;
			}
		});
		return dataImporters;
	}
}