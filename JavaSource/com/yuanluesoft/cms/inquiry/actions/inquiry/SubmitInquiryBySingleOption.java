package com.yuanluesoft.cms.inquiry.actions.inquiry;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 松溪十大感动人物评选，单个选项投票
 * @author kangshiwei
 *
 */
public class SubmitInquiryBySingleOption extends BaseAction {
   
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long inquiryId=RequestUtils.getParameterLongValue(request, "inquiryId");
		long optionId=RequestUtils.getParameterLongValue(request, "optionId");
		
		String resultCode=null;//结果代码。0成功，-1失败
		String message=null;//结果信息
		if(inquiryId==0||optionId==0){//没有调查ID和所投选项ID
			resultCode="-1";
			message="主题ID和选项ID不能为空";
			writeJSONResult(resultCode, message, request, response);
			return null;
		}
		
		InquiryService inquiryService = (InquiryService)getService("inquiryService");
		
		//会话检查
		SessionInfo sessionInfo = null;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			//会话异常,检查是否实名调查,如果是,重定向到登录界面
			if(!inquiryService.isAnonymous(inquiryId+"")) { //检查是否匿名调查
				resultCode="-1";
				message="请先登陆";
				writeJSONResult(resultCode, message, request, response);
				return null;
			}
		}
		
		//保存调查结果
        try{
			inquiryService.submitInquiryBySingleOption(inquiryId, optionId, RequestUtils.getRemoteAddress(request), sessionInfo);
			resultCode="0";
			message="投票成功";
        }
        catch(Exception e){
			if(e instanceof ServiceException){
				resultCode="-1";
				message=e.getMessage();
			}
			else{
				resultCode="-1";
				message="系统错误";
				Logger.exception(e);
			}
		}
        writeJSONResult(resultCode, message, request, response);		
		return null;
    }
	
	/**
	 * 输出结果
	 * @param resultCode
	 * @param message
	 * @param request
	 * @param response
	 */
	private void writeJSONResult(String resultCode,String message, HttpServletRequest request, HttpServletResponse response)throws Exception{
		response.setContentType("text/html;charset=utf-8");//以utf-8编码返回json格式数据
		PrintWriter out=response.getWriter();
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("resultCode",resultCode);
		jsonObject.put("message",message);
		out.print(jsonObject.toJSONString());
		out.close();
	}
}