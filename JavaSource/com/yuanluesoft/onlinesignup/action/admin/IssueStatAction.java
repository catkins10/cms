package com.yuanluesoft.onlinesignup.action.admin;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.onlinesignup.forms.admin.SignUpStat;

/**
 * 
 * @author zyh
 *
 */
public class IssueStatAction extends ViewFormAction {
     
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "onlinesignup";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "signUpStat";
	}
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		SignUpStat signUpForm = (SignUpStat)viewForm;
		String province = signUpForm.getProvince();
		String city = signUpForm.getCity();
		String country = signUpForm.getCountry();
		String where = "";
		if(province!=null && !province.equals("")){
			where = " SignUp.province = '"+province+"'";
			view.addWhere(where);
		}
		if(city!=null && !city.equals("")){
			where = " SignUp.city = '"+city+"'";
			view.addWhere(where);
		}
		if(country!=null && !country.equals("")){
			where = " SignUp.country = '"+country+"'";
			view.addWhere(where);
		}
    }
}