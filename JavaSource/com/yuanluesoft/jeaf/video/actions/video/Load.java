package com.yuanluesoft.jeaf.video.actions.video;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.video.model.Video;
import com.yuanluesoft.jeaf.video.service.VideoService;

/**
 * 
 * @author chuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!"post".equalsIgnoreCase(request.getMethod())) { //强制POST
        	return null;
        }
        String properties = Encoder.getInstance().desDecode(request.getParameter("video"), "20041227", "utf-8", "DES");
    	String applicationName = StringUtils.getPropertyValue(properties, "applicationName");
		long recordId = StringUtils.getPropertyLongValue(properties, "recordId", 0);
		String videoName = StringUtils.getPropertyValue(properties, "videoName");
		String videoType = StringUtils.getPropertyValue(properties, "videoType");
		int width = StringUtils.getPropertyIntValue(properties, "width", 400);
		int height = StringUtils.getPropertyIntValue(properties, "height", 300);;
		boolean autoPlay = "true".equals(StringUtils.getPropertyValue(properties, "autoStart"));
		boolean hideControls = "true".equals(StringUtils.getPropertyValue(properties, "hideControls"));
		RequestInfo requestInfo = RequestUtils.getRequestInfo(request);
		if(!requestInfo.isClientRequest() && !"computer".equals(requestInfo.getTerminalType())) { //手机或者平板
			int clientWidth = RequestUtils.getParameterIntValue(request, "clientWidth");
			if(width > clientWidth - 20) {
				height = (int)(height * (clientWidth - 20.0) / width);
				width = clientWidth - 20;
			}
		}
		//获取视频服务
		VideoService videoService = (VideoService)FieldUtils.getAttachmentService(applicationName, videoType, recordId);
		//获取视频
		Video video = videoService.getVideo(applicationName, videoType, recordId, videoName, request);
		String videoUrl = video.getUrl();
		//加密视频URL
		String key = UUIDStringGenerator.generateId().substring(0, 8);
		videoUrl = Encoder.getInstance().desEncode(videoUrl, key, "utf-8", "DES");
		//获取预览图URL
		String previewImageUrl = videoService.getBreviaryImage(applicationName, videoType, recordId, videoName, width, height, request).getUrl();
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		String logoUrl = request.getContextPath() + "/cms/sitemanage/videoPlayerLogo.shtml" + (siteId==0 ? "" : "?siteId=" + siteId);
		response.setHeader("Cache-Control", "no-cache"); //禁止缓存
		PrintWriter writer = response.getWriter();
		writer.write("<html>");
		writer.write("<body>");
		writer.write("	<script type=\"text/javascript\" src=\"" + request.getContextPath() + "/jeaf/common/js/des.js\" charset=\"utf-8\"></script>");
		writer.write("	<script>");
		writer.write("		try {parent.setTimeout(\"new VideoPlayer(document.getElementById('" + request.getParameter("id") + "'), '\" + DES.decrypt('" + videoUrl + "', '" + key + "') + \"', '" + previewImageUrl + "', '" + logoUrl + "', " + width + ", " + height + "," + autoPlay + ", " + hideControls + ", " + video.getVideoLength() + ", '" + requestInfo.getTerminalType() + "');\", 1);} catch(e) {}");
		writer.write("		location.replace('" + request.getContextPath() + "/blank.html');");
		if(!RequestUtils.getRequestInfo(request).isClientRequest()) { //不是客户端请求
			writer.write("	try {window.frameElement.parentNode.removeChild(window.frameElement);}catch(e){}");
		}
		writer.write("	</script>");
		writer.write("</body>");
		writer.write("</html>");
        return null;
    }
}