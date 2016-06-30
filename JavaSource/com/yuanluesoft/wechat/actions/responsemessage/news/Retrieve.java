package com.yuanluesoft.wechat.actions.responsemessage.news;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.wechat.forms.ResponseMessageNews;
import com.yuanluesoft.wechat.pojo.WechatMessageNews;

/**
 * 
 * @author linchuan
 *
 */
public class Retrieve extends NewsAction {
        
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeLoadComponentAction(mapping, form, "messageNews", request, response);
    	if(forward!=null && "load".equals(forward.getName())) {
    		return mapping.findForward("result");
    	}
    	return forward;
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadComponentRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		ResponseMessageNews newsForm = (ResponseMessageNews)form;
		WechatMessageNews news = (WechatMessageNews)super.loadComponentRecord(form, mainRecord, componentName, sessionInfo, request);
		if(news!=null) {
			news.setTitle(newsForm.getMessageNews().getTitle());
			news.setUrl(newsForm.getMessageNews().getUrl());
		}
		//检查是否已经上传过图片
		ImageService imageService = (ImageService)getService("imageService");
		List images = imageService.list("wechat", "image", newsForm.getMessageNews().getId(), false, 1, request);
		if(images!=null && !images.isEmpty()) {
			return news;
		}
		//更新图片
		long recordId;
		Attachment attachment = null;
		if(newsForm.getMessageNews().getUrl().indexOf("/cms/siteresource/")!=-1 && //站点资源
		   (recordId = StringUtils.getPropertyLongValue(newsForm.getMessageNews().getUrl(), "id", 0))>0) {
			SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
			SiteResource siteResource = (SiteResource)siteResourceService.load(SiteResource.class, recordId);
			String imageName = siteResource.getFirstImageName();
			attachment = (Attachment)ListUtils.findObjectByProperty((List)FieldUtils.getFieldValue(siteResource, "images", request), "name", imageName);
		}
		if(attachment!=null) {
			imageService.uploadFile("wechat", "image", FieldUtils.getFormField(newsForm.getFormDefine(), "messageNews.image", request), newsForm.getMessageNews().getId(), attachment.getFilePath());
		}
		return news;
	}
}