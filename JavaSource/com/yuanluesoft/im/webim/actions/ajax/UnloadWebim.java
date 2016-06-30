package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.im.model.message.OfflineCheck;

/**
 * WEBIM窗口关闭
 * @author linchuan
 *
 */
public class UnloadWebim extends AjaxAction {
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		return new OfflineCheck();
	}
}