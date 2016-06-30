package com.yuanluesoft.logistics.vehicle.processor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.logistics.vehicle.pojo.LogisticsVehicleFree;

/**
 * 
 * @author linchuan
 *
 */
public class VehicleFieldProcessor extends RecordFieldProcessor {
	private DatabaseService databaseService; //数据库服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor#writeTextField(java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, java.lang.Object, java.lang.String, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, int)
	 */
	protected HTMLElement writeTextField(Object record, Field field, Object fieldValue, String fieldUrl, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request, int pageMode) throws ServiceException {
		boolean locationField = false;
		if(field.getName().startsWith("location.")) {
			Number vehicleId = null;
			try {
				vehicleId = (Number)PropertyUtils.getProperty(record, "id");
			}
			catch(Exception e) {
			
			}
			pageElement.setAttribute("location", field.getName() + "_" + vehicleId);
			locationField = true;
			if(vehicleId.equals(request.getAttribute("currentVehicleId"))) {
				fieldValue = "\u00a0";
				String style = pageElement.getAttribute("style");
				pageElement.setAttribute("style", (style==null ? "" : style + ";") + "visibility: hidden;");
			}
			else {
				request.setAttribute("currentVehicleId", vehicleId);
				htmlParser.appendScriptFile((HTMLDocument)pageElement.getOwnerDocument(), Environment.getContextPath() + "/jeaf/gps/js/gps.js");
				String linkmanTel = getLinkmanTel(record, sitePage);
				htmlParser.appendScript((HTMLDocument)pageElement.getOwnerDocument(), "gpsLocation('" + linkmanTel + "', '" + vehicleId + "')");
			}
		}
		pageElement = super.writeTextField(record, field, fieldValue, fieldUrl, pageElement, webDirectory, parentSite, htmlParser, sitePage, request, pageMode);
		if(locationField) {
			pageElement.setId(pageElement.getAttribute("location"));
			pageElement.removeAttribute("location");
		}
		return pageElement;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordFieldProcessor#getFieldValue(java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected Object getFieldValue(Object record, Field field, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		if(field.getName().startsWith("location.")) {
			String linkmanTel = getLinkmanTel(record, sitePage);
			return linkmanTel==null || linkmanTel.isEmpty() ? null : "正在定位...";
		}
		return super.getFieldValue(record, field, pageElement, webDirectory, parentSite, sitePage, request);
	}
	
	/**
	 * 获取随车号码
	 * @param record
	 * @return
	 */
	private String getLinkmanTel(Object record, SitePage sitePage) {
		try {
			if(!(record instanceof LogisticsVehicleFree)) { //不是载货车辆
				return (String)PropertyUtils.getProperty(record, "linkmanTel"); //获取随车号码
			}
			else { //载货车辆
				Number recordId = null;
				try {
					recordId = (Number)PropertyUtils.getProperty(record, "id");
				}
				catch(Exception e) {
				
				}
				String linkmanTel = (String)sitePage.getAttribute("currentLinkmanTel_" + recordId);
				if(linkmanTel==null) {
					String hql = "select LogisticsVehicle.linkmanTel" +
								 " from LogisticsVehicle LogisticsVehicle" +
								 " where LogisticsVehicle.plateNumber='" + JdbcUtils.resetQuot(((LogisticsVehicleFree)record).getPlateNumber()) + "'";
					linkmanTel = (String)databaseService.findRecordByHql(hql);
					sitePage.setAttribute("currentLinkmanTel_" + recordId, (linkmanTel));
				}
				return linkmanTel==null || linkmanTel.isEmpty() ? null : linkmanTel;
			}
		}
		catch (Exception e) {
			return null;
		}
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
}