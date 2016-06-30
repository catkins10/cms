package com.yuanluesoft.microblog.actions.microblog;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.microblog.forms.Microblog;
import com.yuanluesoft.microblog.pojo.MicroblogAccount;
import com.yuanluesoft.microblog.service.MicroblogService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends MicroblogAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeLoadAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Record record = super.load(form, request, response);
		Microblog microblogForm = (Microblog)form;
		if(microblogForm.getReferenceUrl()==null || microblogForm.getReferenceUrl().isEmpty()) {
			return record;
		}
		MicroblogService microblogService = (MicroblogService)getService("microblogService");
		//获取微博帐号配置
		List accounts = microblogService.listAccounts(microblogForm.getUnitId());
		//生成完整的URL
		String url = ((MicroblogAccount)accounts.get(0)).getSiteUrl();
		url +=  url.endsWith("/") ? microblogForm.getReferenceUrl().substring(1) : microblogForm.getReferenceUrl();
		//获取引用的记录
		long recordId = StringUtils.getPropertyLongValue(microblogForm.getReferenceUrl(), "id", 0);
		List attachments = null;
		String content = microblogForm.getReferenceTitle();
		if(microblogForm.getReferenceUrl().indexOf("/cms/siteresource/")!=-1) {//站点资源
			SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
			SiteResource siteResource = (SiteResource)siteResourceService.load(SiteResource.class, recordId);
			content = StringUtils.filterHtmlElement(siteResource.getBody(), false); //内容
			attachments = (List)FieldUtils.getFieldValue(siteResource, "images", request);
		}
		else if(microblogForm.getReferenceUrl().indexOf("/cms/infopublic/")!=-1) { //政府信息
			PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
			PublicInfo info = (PublicInfo)publicInfoService.load(PublicInfo.class, recordId);
			content = StringUtils.filterHtmlElement(info.getBody(), false); //内容
		}
		if(content!=null) {
			content = StringUtils.trim(content).replace("　", "");
		}
		if(content==null || content.isEmpty()) {
			content = microblogForm.getReferenceTitle();
		}
		//上传图片
		ImageService imageService = (ImageService)getService("imageService");
		imageService.uploadFiles("microblog", "image", FieldUtils.getRecordField(com.yuanluesoft.microblog.pojo.Microblog.class.getName(), "image", request), microblogForm.getId(), ListUtils.generatePropertyList(attachments, "filePath"));
		//生成微博内容
		if(content!=null && !content.isEmpty()) {
			microblogForm.setContent((content.length() > 1999-url.length() ? content.substring(0, 1999-url.length()) : content) + " " + url);
		}
		//清空引用的记录
		microblogForm.setReferenceUrl(null);
		microblogForm.setReferenceTitle(null);
		return record;
	}
}