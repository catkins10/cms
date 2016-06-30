package com.yuanluesoft.cms.infopublic.actions.directorytree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.actions.selectdirectory.SelectDirectoryAction;
import com.yuanluesoft.cms.infopublic.forms.SelectDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Load extends SelectDirectoryAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SelectDirectory selectForm = (SelectDirectory)form;
    	selectForm.setAnonymousAlways(true);
    	selectForm.setCountPublicInfo(true);
    	PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
    	//获取站点关联的信息公开目录
    	long directoryId = RequestUtils.getParameterLongValue(request, "directoryId"); //url指定的目录ID
		long rootDirectoryId; //根目录ID
		if(directoryId>0) { //目录链接,且没有指定站点
			//查找目录对应的主目录
			PublicMainDirectory mainDirectory = publicDirectoryService.getMainDirectory(directoryId);
			rootDirectoryId = mainDirectory.getId();
		}
		else {
	    	long siteId = RequestUtils.getParameterLongValue(request, "siteId"); //站点ID
			//按站点获取主目录
	    	PublicMainDirectory mainDirectory = publicDirectoryService.getMainDirectoryBySite(siteId);
			rootDirectoryId = mainDirectory.getId();
			directoryId = rootDirectoryId;
		}
		selectForm.setParentNodeId("" + rootDirectoryId);
    	return executeOpenDialogAction(mapping, form, request, response);
    }
}