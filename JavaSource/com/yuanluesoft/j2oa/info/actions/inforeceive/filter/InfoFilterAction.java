package com.yuanluesoft.j2oa.info.actions.inforeceive.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.info.actions.inforeceive.InfoReceiveAction;
import com.yuanluesoft.j2oa.info.forms.InfoFilter;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InfoFilterAction extends InfoReceiveAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		InfoFilter infoFilterForm = (InfoFilter)form;
		if(infoFilterForm.getInfoFilters()==null || infoFilterForm.getInfoFilters().isEmpty()) {
			infoFilterForm.setLevel("普通");
			if(infoFilterForm.getPresetMagazines()!=null && !infoFilterForm.getPresetMagazines().isEmpty()) {
				infoFilterForm.setMagazineIds(infoFilterForm.getPresetMagazines().split(","));
			}
		}
		else {
			com.yuanluesoft.j2oa.info.pojo.InfoFilter infoFilter = (com.yuanluesoft.j2oa.info.pojo.InfoFilter)infoFilterForm.getInfoFilters().iterator().next();
			infoFilterForm.setLevel(infoFilter.getLevel());
			infoFilterForm.setIsBrief(infoFilter.getIsBrief()); //是否简讯
			infoFilterForm.setIsVerified(infoFilter.getIsVerified()); //是否核实
			infoFilterForm.setIsCircular(infoFilter.getIsCircular()); //是否通报
			infoFilterForm.setMagazineIds(ListUtils.join(infoFilterForm.getInfoFilters(), "magazineDefineId", ",", false).split(","));
		}
	}
}