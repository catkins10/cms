package com.yuanluesoft.j2oa.reimburse.actions.chargestandard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.reimburse.service.ChargeStandardService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Encoder;

/**
 * 
 * @author linchuan
 *
 */
public class GetChargeStandard extends ChargeStandardAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionInfo sessionInfo;
        response.setContentType("text/javascript; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
            sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
		    outputStandard(response, "NOSESSIONINFO");
			return null;
		}
		ChargeStandardService chargeStandardService = (ChargeStandardService)getService("chargeStandardService");
		String standard = chargeStandardService.getChargeStandard(sessionInfo, request.getParameter("chargeCategory"));
		outputStandard(response, standard);
		return null;
    }
    
    /**
     * 输出
     * @param response
     * @param standard
     * @throws Exception
     */
    private void outputStandard(HttpServletResponse response, String standard) throws Exception {
        response.getWriter().println("afterGetChargeStandard('" + Encoder.getInstance().utf8JsEncode(standard) + "');");
    }
}