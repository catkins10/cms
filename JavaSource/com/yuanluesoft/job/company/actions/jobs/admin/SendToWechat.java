package com.yuanluesoft.job.company.actions.jobs.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.job.company.pojo.Job;
import com.yuanluesoft.job.company.service.JobCompanyService;
import com.yuanluesoft.wechat.model.MessageNews;
import com.yuanluesoft.wechat.service.WechatService;

/**
 * 
 * @author linchuan
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
    	JobCompanyService jobCompanyService = (JobCompanyService)getService("jobCompanyService");
		SiteService siteService = (SiteService)getService("siteService");
		
		//获取用户站点
		WebSite webSite = siteService.getSiteByOwnerUnitId(sessionInfo.getUnitId());
    	long siteId = webSite==null ? 0 : webSite.getId();
    	
    	//生成图文列表
    	List newsList =  new ArrayList();
    	String[] ids = request.getParameter("ids").split(",");
		for(int i=0; i<Math.min(12, ids.length); i++) {
			MessageNews news = new MessageNews();
	    	Job job = (Job)jobCompanyService.load(Job.class, Long.parseLong(ids[i]));
	    	news.setTitle(job.getName() + "(" + job.getCompany().getName() + ")");
	    	news.setUrl(request.getContextPath() + "/job/company/job.shtml?id=" + job.getId() + (siteId==0 ? "" : "&siteId=" + siteId));
			news.setContent(job.getRequirement());
			news.setDescription(job.getDescription());
			List logos = (List)FieldUtils.getFieldValue(job.getCompany(), "logo", request);
			news.setImageFilePath(logos==null || logos.isEmpty() ? null : ((Image)logos.get(0)).getFilePath());
			newsList.add(news);
		}
		response.sendRedirect(wechatService.createWechatMessageNews(newsList, sessionInfo));
    	return null;
    }
}