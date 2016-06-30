package com.yuanluesoft.jeaf.gps.actions.placenamejs;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.gps.service.GpsService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 地名解析
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	response.setContentType("text/javascript");
    	response.setCharacterEncoding("utf-8");
    	PrintWriter writer = response.getWriter();
    	GpsService gpsService = (GpsService)getService("gpsService");
		String placeName = gpsService.getPlaceName(RequestUtils.getParameterDoubleValue(request, "latitude"), RequestUtils.getParameterDoubleValue(request, "longitude"));
		if(placeName!=null && "true".equals(RequestUtils.getParameterStringValue(request, "cityOnly"))) { //仅城市
			int index = placeName.indexOf("县");
			if(index==-1) {
				index = placeName.indexOf("区");
				if(index==-1) {
					index = placeName.lastIndexOf("市");
					if(index!=-1) {
						int indexParent = placeName.lastIndexOf("市", index - 1);
						System.out.println(indexParent);
						if(indexParent!=-1) {
							if(placeName.substring(0, indexParent + 1).endsWith(placeName.substring(indexParent + 1, index + 1))) {
								index = indexParent;
							}
						}
					}
				}
			}
			if(index!=-1) {
				placeName = placeName.substring(0, index + 1);
			}
		}
		String script = "var getPlaceNameScript = document.getElementById('getPlaceNameScript');\n" +
						"getPlaceNameScript.parentNode.removeChild(getPlaceNameScript);\n" +
						"getPlaceNameCallback.call(null, '" + (placeName==null ? "未知" : placeName) + "');";
    	writer.write(script);
		return null;
    }
}