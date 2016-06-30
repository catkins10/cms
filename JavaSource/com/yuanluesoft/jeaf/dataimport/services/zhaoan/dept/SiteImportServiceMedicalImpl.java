package com.yuanluesoft.jeaf.dataimport.services.zhaoan.dept;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.dataimporter.SiteDataImporter;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;

/**
 * 诏安药监局
 * @author linchuan
 *
 */
public class SiteImportServiceMedicalImpl extends DataImportService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getName()
	 */
	public String getName() {
		return "诏安药监局网站";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.services.DataImportService#getDataImporters()
	 */
	protected List getDataImporters() {
		List dataImporters = new ArrayList();

		//网站数据导入
		dataImporters.add(new SiteDataImporter() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#generateListChildDirectoriesSQL(java.lang.String)
			 */
			public String generateListChildDirectoriesSQL(String parentDirectoryId) {
				if(parentDirectoryId==null) { //第一级目录
					return "select 'first_' & typeid as directoryId," +
							" typename as directoryName" +
							" from type" +
							" order by typename";
				}
				else if(parentDirectoryId.startsWith("first_")) {
					return "select 'second_' & BigClassID as directoryId," +
						   " BigClassName as directoryName" +
						   " from BigClass" +
						   " where typeid=" + parentDirectoryId.substring("first_".length()) +
						   " order by BigClassName";
				}
				else if(parentDirectoryId.startsWith("second_")) {
					return "select 'third_' & SmallClassID as directoryId," +
						   " smallclassname as directoryName" +
						   " from smallclass" +
						   " where BigClassID=" + parentDirectoryId.substring("second_".length()) +
						   " order by smallclassname";
				}
				return null;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.dataimport.dataimporter.DirectoryDataImporter#generateRetrieveDataSQL(java.util.List)
			 */
			public String[] generateRetrieveDataSQL(List selectedSourceDirectoryIds) {
				return new String[] {
						   "select " +
						   " NewsID as importDataId," + //被导入的记录ID
						   " iif(smallclassid is null, iif(bigclassid is null, 'first_' & typeid, 'second_' & bigclassid), 'third_' & smallclassid) as directoryId," + //原来的栏目ID
						   " Title as subject," + //标题
						   " Content as body," + //正文
						   " '' as subhead," + //副标题
						   " null as Source," + //来源
						   " Author," + //作者
						   " about as keyword," + //关键字
						   " UpdateTime as created," + //创建时间
						   " UpdateTime as issueTime," + //发布时间
						   " editor," + //创建者
						   " Original as orgName," + //创建者所在部门名称
						   " Original as unitName" + //创建者所在单位名称
						   " from News"};
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