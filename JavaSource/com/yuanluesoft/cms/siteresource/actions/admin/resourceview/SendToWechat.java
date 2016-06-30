package com.yuanluesoft.cms.siteresource.actions.admin.resourceview;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.wechat.model.MessageNews;
import com.yuanluesoft.wechat.service.WechatService;

/**
 * 
 * @author chuan
 *
 */
public class SendToWechat extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	WechatService wechatService = (WechatService)getService("wechatService");
    	SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		List newsList =  new ArrayList();
    	String[] ids = request.getParameter("ids").split(",");
		for(int i=0; i<Math.min(12, ids.length); i++) {
			MessageNews news = new MessageNews();
	    	SiteResource siteResource = (SiteResource)siteResourceService.load(SiteResource.class, Long.parseLong(ids[i]));
	    	news.setTitle(siteResource.getSubject());
	    	news.setUrl(siteResource.getType()==SiteResourceService.RESOURCE_TYPE_LINK ? siteResource.getLink() : request.getContextPath() + "/cms/siteresource/article.shtml?id=" + siteResource.getId());
			String content = siteResource.getBody(); //内容
			if(content!=null && !content.isEmpty()) {
				String text = StringUtils.filterHtmlElement(content, false);
				String description = text.length()>120 ? text.substring(0, 120) : text;
				news.setContent(content);
				news.setDescription(description);
			}
			String imageName = siteResource.getFirstImageName();
			if(imageName!=null && !imageName.isEmpty()) {
				Attachment attachment = (Attachment)ListUtils.findObjectByProperty((List)FieldUtils.getFieldValue(siteResource, "images", request), "name", imageName);
				news.setImageFilePath(attachment.getFilePath());
			}
			newsList.add(news);
		}
		response.sendRedirect(wechatService.createWechatMessageNews(newsList, sessionInfo));
    	return null;
    }
}