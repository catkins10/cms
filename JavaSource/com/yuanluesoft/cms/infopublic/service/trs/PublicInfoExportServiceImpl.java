package com.yuanluesoft.cms.infopublic.service.trs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultCDATA;

import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoSubjection;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoExportService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.Mime;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewService;

/**
 * 
 * @author linchuan
 *
 */
public class PublicInfoExportServiceImpl implements PublicInfoExportService {
	private ViewService viewService; //视图服务
	private DatabaseService databaseService; //数据库服务
	private String serverUrl = "http://www.fzjt.gov.cn/"; //服务器URL
	private String version = "6.0"; //版本号
	private String currentUser = "fz00119"; //当前用户
	private String channelId = "24128"; //频道ID
	private String unitName = "福州市交通运输委员会"; //单位名称
	private String unitId = "19137"; //单位ID
	private Map directoryMap; //目录映射

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.infopublic.service.PublicInfoExportService#exportXML(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.lang.String, javax.servlet.http.HttpServletResponse, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void exportXML(View view, String currentCategories, String searchConditions, HttpServletResponse response, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if(directoryMap==null || directoryMap.isEmpty()) {
			directoryMap = new HashMap();
			directoryMap.put("机构职能", "机构设置、主要职能、办事程序|18888");
			directoryMap.put("法律法规", "行政法规、规章和规范性文件|18889");
			directoryMap.put("规划计划", "国民经济和社会发展规划、专项规划、区域规划及相关政策|18890");
			directoryMap.put("统计信息", "国民经济和社会发展统计信息|18891");
			directoryMap.put("资金补助", "财政预算决算|18892");
			directoryMap.put("交通规费", "行政事业性收费的项目、依据、标准|18893");
			directoryMap.put("招标投标", "重大建设项目的批准和实施情况|18894");
			directoryMap.put("基础设施", "城乡建设和管理的重大事项|18900");
			directoryMap.put("行政许可", "行政许可的事项、依据、条件、数量、程序、期限以及申请行政许可需要提交的全部材料目录及办理情况|18895");
			directoryMap.put("行政确认", "行政许可的事项、依据、条件、数量、程序、期限以及申请行政许可需要提交的全部材料目录及办理情况|18895");
			directoryMap.put("行政处罚", "行政许可的事项、依据、条件、数量、程序、期限以及申请行政许可需要提交的全部材料目录及办理情况|18895");
			directoryMap.put("行政强制", "行政许可的事项、依据、条件、数量、程序、期限以及申请行政许可需要提交的全部材料目录及办理情况|18895");
			directoryMap.put("行政监督", "环境保护、公共卫生、安全生产、食品药品、产品质量的监督检查情况|18899");
			directoryMap.put("安全质量", "环境保护、公共卫生、安全生产、食品药品、产品质量的监督检查情况|18899");
			directoryMap.put("应急预案", "突发公共事件的应急预案、预警信息及应对情况|18898");
			directoryMap.put("人事管理", "人事任免|19301");
			directoryMap.put("公共信息", "应主动公开的其他信息|18905");
			directoryMap.put("信誉资质", "环境保护、公共卫生、安全生产、食品药品、产品质量的监督检查情况|18899");
			directoryMap.put("举报投诉", "工作动态|18904");
			directoryMap.put("其他信息", "应主动公开的其他信息|18905");
		}
		view.addWhere("PublicInfo.status='" + PublicInfoService.INFO_STATUS_ISSUE + "'"); //已发布
		//构造视图包
		ViewPackage viewPackage = new ViewPackage();
		view.setPageRows(100); //每次获取100条记录
		viewPackage.setView(view);
		viewPackage.setSearchConditions(searchConditions);
		viewPackage.setSearchMode(searchConditions!=null);
		viewPackage.setCategories(currentCategories);
		//构造XML文档
		Document document = DocumentHelper.createDocument();
		Element rootElement =  document.addElement("MetaViewDataS");
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)Environment.getService("publicDirectoryService");
		for(int page=1; ; page++) {
			//设置当前页
			viewPackage.setCurPage(page);
			try {
				viewService.retrieveViewPackage(viewPackage, view, 0, true, false, false, request, sessionInfo);
			}
			catch (PrivilegeException e) {
				
			}
			//获取记录
			if(viewPackage.getRecords()==null || viewPackage.getRecords().isEmpty()) {
				break;
			}
			//输出记录
			for(Iterator iterator = viewPackage.getRecords().iterator(); iterator.hasNext(); ) {
				PublicInfo publicInfo = (PublicInfo)iterator.next();
				if(publicInfo.getStatus()!=PublicInfoService.INFO_STATUS_ISSUE) {
					continue;
				}
				PublicInfoSubjection subjection = (PublicInfoSubjection)databaseService.findRecordByHql("from PublicInfoSubjection PublicInfoSubjection where PublicInfoSubjection.infoId=" + publicInfo.getId());
				String[] directoryFullName = publicDirectoryService.getDirectoryFullName(subjection.getDirectoryId(), "/", "main").split("/");
				//获取隶属目录名称
				String trsDirectory = (String)directoryMap.get(directoryFullName[1]);
				if(trsDirectory==null) {
					continue;
				}
				/**
				 * <MetaViewDataS>
					  <MetaViewData Version="6.0">
					    <PROPERTIES>
					      <IDXID><![CDATA[FZ00100-1100-2012-00003]]></IDXID>
					      <CONTENT><![CDATA[222]]></CONTENT>
					      <PUBLISHER><![CDATA[福州市人民政府]]></PUBLISHER>
					      <CRUSER><![CDATA[wdp]]></CRUSER>
					      <DESCRIPTION><![CDATA[关于组织防空警报试鸣的通告]]></DESCRIPTION>
					      <WCMMETATABLEGOVINFOID>38735</WCMMETATABLEGOVINFOID>
					      <ABOLIDATE/>
					      <DOCCONTENT><![CDATA[]]></DOCCONTENT>
					      <PUBDATE>2012-06-05 00:00:00</PUBDATE>
					      <TITLE><![CDATA[福州市人民政府关于组织防空警报试鸣的通告]]></TITLE>
					      <METADATAID>55202299</METADATAID>
					      <SEARCHCONTENT/>
					      <SUBCAT RealValue="突发公共事件的应急预案、预警信息及应对情况"><![CDATA[18898]]></SUBCAT>
					      <EFECTDATE/>
					      <MDUPDTIME/>
					      <CHANNELID>24128</CHANNELID>
					      <CRTIME>2012-06-12 09:31:22</CRTIME>
					      <ORGANCAT RealValue="福州市人民政府"><![CDATA[19118]]></ORGANCAT>
					      <FILENUM><![CDATA[榕政〔2012〕3号]]></FILENUM>
					      <DOCTITLE><![CDATA[福州市人民政府关于组织防空警报试鸣的通告]]></DOCTITLE>
					    </PROPERTIES>
					    <WCMAPPENDIXS/>
					  </MetaViewData>
					</MetaViewDataS>
				 */
				Element metaViewData = rootElement.addElement("MetaViewData");
				metaViewData.addAttribute("Version", version); //版本号
				Element properties = metaViewData.addElement("PROPERTIES");
				properties.addElement("IDXID").add(new DefaultCDATA(publicInfo.getInfoIndex()));
				String hql = "select PublicInfoBody.body from PublicInfoBody PublicInfoBody where PublicInfoBody.id=" + publicInfo.getId();
				String body = (String)databaseService.findRecordByHql(hql);
				properties.addElement("CONTENT").add(new DefaultCDATA(body==null ? "" : body.replaceAll("(?i)src=\"/", "src=\"" + serverUrl).replaceAll("(?i)href=\"/", "href=\"" + serverUrl)));
				properties.addElement("PUBLISHER").add(new DefaultCDATA(publicInfo.getInfoFrom()));
				properties.addElement("CRUSER").add(new DefaultCDATA(currentUser));
				properties.addElement("DESCRIPTION").add(new DefaultCDATA(publicInfo.getSummarize()));
				properties.addElement("WCMMETATABLEGOVINFOID").setText("" + (publicInfo.getId() + "").hashCode());
				properties.addElement("ABOLIDATE");
				properties.addElement("DOCCONTENT").add(new DefaultCDATA(""));
				properties.addElement("PUBDATE").add(new DefaultCDATA(DateTimeUtils.formatDate(publicInfo.getGenerateDate(), "yyyy-MM-dd") + " 00:00:00"));
				properties.addElement("TITLE").add(new DefaultCDATA(publicInfo.getSubject()));
				properties.addElement("METADATAID").setText(publicInfo.getId() + "");
				properties.addElement("SEARCHCONTENT");
				Element subcat = properties.addElement("SUBCAT");
				String[] values = trsDirectory.split("\\|");
				subcat.addAttribute("RealValue", values[0]);
				subcat.add(new DefaultCDATA(values[1]));
				properties.addElement("EFECTDATE");
				properties.addElement("MDUPDTIME");
				properties.addElement("CHANNELID").setText(channelId);
				properties.addElement("CRTIME").setText(DateTimeUtils.formatTimestamp(publicInfo.getCreated(), null));
			    Element organcat = properties.addElement("ORGANCAT");
			    organcat.addAttribute("RealValue", unitName);
			    organcat.add(new DefaultCDATA(unitId));
			    properties.addElement("FILENUM").add(new DefaultCDATA(publicInfo.getMark()));
			    properties.addElement("DOCTITLE").add(new DefaultCDATA(publicInfo.getSubject()));
			}
			if(page>=viewPackage.getPageCount()) {
				break;
			}
		}
		XMLWriter writer = null;
		try {
			response.setContentType(Mime.MIME_XML);
			response.setCharacterEncoding("utf-8");
			//输出附件头
			response.addHeader("Content-Disposition", "attachment; filename=" + FileUtils.encodeFileName("ST" + DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyyMMdd") + ".xml", "utf-8"));
			OutputFormat format = OutputFormat.createPrettyPrint();      
			format.setEncoding("UTF-8");      
			writer = new XMLWriter(response.getOutputStream(), format);   
			writer.write(document);
			writer.close();
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		finally {
			try {
				writer.close();
			}
			catch(Exception e) {
				
			}
		}
	}

	/**
	 * @return the viewService
	 */
	public ViewService getViewService() {
		return viewService;
	}

	/**
	 * @param viewService the viewService to set
	 */
	public void setViewService(ViewService viewService) {
		this.viewService = viewService;
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the currentUser
	 */
	public String getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	
	/**
	 * @return the serverUrl
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param serverUrl the serverUrl to set
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the directoryMap
	 */
	public Map getDirectoryMap() {
		return directoryMap;
	}

	/**
	 * @param directoryMap the directoryMap to set
	 */
	public void setDirectoryMap(Map directoryMap) {
		this.directoryMap = directoryMap;
	}

	/**
	 * @return the unitId
	 */
	public String getUnitId() {
		return unitId;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}