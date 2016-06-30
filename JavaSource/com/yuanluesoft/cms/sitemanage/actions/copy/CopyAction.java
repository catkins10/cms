package com.yuanluesoft.cms.sitemanage.actions.copy;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.directorymanage.actions.CopyDirectoryAction;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class CopyAction extends CopyDirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.CopyDirectoryAction#getDirectoryService()
	 */
	protected DirectoryService getDirectoryService() throws SystemUnregistException {
		return (SiteService)getService("siteService");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.CopyDirectoryAction#getTargetDirectoryTypes(java.lang.String)
	 */
	protected String getTargetDirectoryTypes(String fromDirectoryType) throws SystemUnregistException {
		if("column".equals(fromDirectoryType)) {
			return "site,column";
		}
		else if("site".equals(fromDirectoryType)) {
			return "site";
		}
		return null;
	}
}