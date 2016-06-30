package com.yuanluesoft.j2oa.info.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefine;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class InfoMagazineViewServiceImpl extends ViewServiceImpl {
	private InfoService infoService; //信息采编服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewActions(com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewActions(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		super.retrieveViewActions(view, request, sessionInfo);
		//获取用户任编辑的刊物
		List magazineDefines = infoService.listEditableMagazineDefines(sessionInfo);
		if(magazineDefines==null || magazineDefines.isEmpty()) {
			return;
		}
		String execute;
		if(magazineDefines.size()==1) { //只有一个刊物
			InfoMagazineDefine magazineDefine = (InfoMagazineDefine)magazineDefines.get(0);
			execute = "PageUtils.newrecord('j2oa/info', 'magazine', 'mode=fullscreen', 'defineId=" + magazineDefine.getId() + "')";
		}
		else {
			String itemsText = null;
			for(Iterator iterator = magazineDefines.iterator(); iterator.hasNext();) {
				InfoMagazineDefine magazineDefine = (InfoMagazineDefine)iterator.next();
				itemsText = (itemsText==null ? "" : itemsText + ",") + magazineDefine.getName() + "|" + magazineDefine.getId();
			}
			execute = "DialogUtils.openListDialog('选择刊型', '', 500, 300, false, '', 'PageUtils.newrecord(\"j2oa/info\", \"magazine\", \"mode=fullscreen\", \"defineId={value}\")', '', '" + itemsText + "')";
		}
		if(view.getActions()==null) {
			view.setActions(new ArrayList());
		}
		ViewAction viewAction = new ViewAction();
		view.getActions().add(0, viewAction);
		viewAction.setTitle("排版");
		viewAction.setExecute(execute);
	}

	/**
	 * @return the infoService
	 */
	public InfoService getInfoService() {
		return infoService;
	}

	/**
	 * @param infoService the infoService to set
	 */
	public void setInfoService(InfoService infoService) {
		this.infoService = infoService;
	}

}