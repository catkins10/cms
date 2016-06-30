package com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.impl;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 移除站群记录列表
 * @author chuan
 *
 */
public class RemoveAllSiteRecordList implements TemplateUpgrader {
	private String rootSiteName = null;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader#getCreateDate()
	 */
	public String getCreateDate() {
		return "2015-7-17";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader#upgrade(com.yuanluesoft.cms.templatemanage.pojo.Template, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String upgrade(Template template, String templateHTML, HttpServletRequest request) throws Exception {
		if(rootSiteName==null) {
			SiteService siteService = (SiteService)Environment.getService("siteService");
			rootSiteName = siteService.getDirectoryName(0);
		}
		StringBuffer newHTML = null;
		int beginIndex = 0, endIndex;
		while((endIndex = templateHTML.indexOf("recordListName=allSite", beginIndex))!=-1) {
			endIndex = templateHTML.lastIndexOf("urn=\"", endIndex);
			if(endIndex<=beginIndex) {
				break;
			}
			endIndex += "urn=\"".length();
			if(newHTML == null) {
				newHTML = new StringBuffer();
			}
			newHTML.append(templateHTML.substring(beginIndex, endIndex));
			beginIndex = endIndex;
			endIndex = templateHTML.indexOf('\"', beginIndex);
			if(endIndex==-1) {
				break;
			}
			String urn = templateHTML.substring(beginIndex, endIndex);
			beginIndex = endIndex;
			//重置记录列表名称
			int propertyBegin = urn.indexOf("recordListName=") + "recordListName=".length();
			newHTML.append(urn.substring(0, propertyBegin));
			int propertyEnd = urn.indexOf("&amp;", propertyBegin);
			String recordListName = urn.substring(propertyBegin, propertyEnd);
			recordListName = recordListName.substring("allSite".length() + (recordListName.startsWith("allSites") ? 1 : 0));
			recordListName = recordListName.substring(0, 1).toLowerCase() + recordListName.substring(1);
			newHTML.append(recordListName);
			//重置扩展属性
			String extendProperties = "siteIds%3D0%26siteNames%3D" + rootSiteName + "%26containChildren%3Dtrue";
			propertyBegin = propertyEnd;
			propertyEnd = urn.indexOf("&amp;extendProperties=", propertyBegin);
			if(propertyEnd==-1) {
				newHTML.append(urn.substring(propertyBegin));
				newHTML.append("&amp;extendProperties=");
				newHTML.append(extendProperties);
			}
			else {
				propertyEnd += "&amp;extendProperties=".length();
				newHTML.append(urn.substring(propertyBegin, propertyEnd));
				newHTML.append(extendProperties);
				newHTML.append("%26");
				newHTML.append(urn.substring(propertyEnd));
			}
		}
		if(newHTML==null) {
			return templateHTML;
		}
		newHTML.append(templateHTML.substring(beginIndex));
		return newHTML.toString();
	}
}