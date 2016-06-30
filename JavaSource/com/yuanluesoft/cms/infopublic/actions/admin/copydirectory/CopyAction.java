package com.yuanluesoft.cms.infopublic.actions.admin.copydirectory;

import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
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
		return (PublicDirectoryService)getService("publicDirectoryService");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.CopyDirectoryAction#getTargetDirectoryTypes(java.lang.String)
	 */
	protected String getTargetDirectoryTypes(String fromDirectoryType) throws SystemUnregistException {
		if("main".equals(fromDirectoryType)) {
			return "main";
		}
		else if("category".equals(fromDirectoryType)) {
			return "main,category";
		}
		else if("info".equals(fromDirectoryType)) {
			return "main,category,info";
		}
		else if("article".equals(fromDirectoryType)) {
			return "main,category,article";
		}
		return null;
	}
}