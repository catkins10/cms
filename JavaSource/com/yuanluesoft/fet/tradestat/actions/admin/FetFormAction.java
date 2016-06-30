package com.yuanluesoft.fet.tradestat.actions.admin;

import com.yuanluesoft.fet.tradestat.service.FetCompanyService;
import com.yuanluesoft.jeaf.form.actions.FormAction;

/**
 * 
 * @author yuanluesoft
 *
 */
public abstract class FetFormAction extends FormAction {
	
	/**
	 * 加密口令
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String encodePassword(String password) throws Exception {
		if(password.startsWith("{") && password.endsWith("}")) {
			return password.substring(1, password.length() - 1);
		}
		else {
			FetCompanyService fetCompanyService = (FetCompanyService)getService("fetCompanyService");
			return fetCompanyService.cryptPassword(password);
		}
	}
}
