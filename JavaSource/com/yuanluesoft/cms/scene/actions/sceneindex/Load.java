package com.yuanluesoft.cms.scene.actions.sceneindex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 场景服务（带场景目录树）
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	PageService pageService = (PageService)getService("sceneServicePageService");
    	pageService.writePage("cms/scene", "sceneIndex", request, response, false);
    	return null;
    }
}