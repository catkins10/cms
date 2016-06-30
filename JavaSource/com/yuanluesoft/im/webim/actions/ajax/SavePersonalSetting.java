package com.yuanluesoft.im.webim.actions.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SavePersonalSetting extends AjaxAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.actions.ajax.AjaxAction#generateMessage(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected Message generateMessage(ActionForm form, HttpServletRequest request) throws Exception {
		com.yuanluesoft.im.model.message.SavePersonalSetting personalSetting = new com.yuanluesoft.im.model.message.SavePersonalSetting();
		personalSetting.setStatus((byte)RequestUtils.getParameterIntValue(request, "loginStatus")); //上线后状态,在线、忙碌、隐身、不在电脑旁
		personalSetting.setPlaySoundOnReceived("true".equals(request.getParameter("playSoundOnReceived")) ? 1 : 0); //消息到达是否发出声音
		personalSetting.setSetFocusOnReceived("true".equals(request.getParameter("setFocusOnReceived")) ? 1 : 0); //消息到达是否获取焦点
		personalSetting.setOpenChatDialogOnReceived("true".equals(request.getParameter("openChatDialogOnReceived")) ? 1 : 0); //是否主动弹出对话窗口
		personalSetting.setCtrlSend("true".equals(request.getParameter("ctrlSend")) ? 1 : 0); //CTRL+Enter发送消息
		personalSetting.setFontName(request.getParameter("fontName")); //字体
		personalSetting.setFontSize(request.getParameter("fontSize")); //字号
		personalSetting.setFontColor(request.getParameter("fontColor")); //颜色
		return personalSetting;
	}
}