package com.yuanluesoft.cms.onlineservice.actions.admin.copydirectory;

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
		return (DirectoryService)getService("onlineServiceDirectoryService");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.CopyDirectoryAction#getTargetDirectoryTypes(java.lang.String)
	 */
	protected String getTargetDirectoryTypes(String fromDirectoryType) throws SystemUnregistException {
		if("mainDirectory".equals(fromDirectoryType)) {
			return "mainDirectory";
		}
		else if("directory".equals(fromDirectoryType)) {
			return "mainDirectory,directory";
		}
		return null;
	}
}