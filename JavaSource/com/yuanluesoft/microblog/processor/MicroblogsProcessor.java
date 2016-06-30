package com.yuanluesoft.microblog.processor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.microblog.model.Microblog;
import com.yuanluesoft.microblog.model.MicroblogImage;
import com.yuanluesoft.microblog.service.MicroblogService;

/**
 * 
 * @author linchuan
 *
 */
public class MicroblogsProcessor extends RecordListProcessor {
	private MicroblogService microblogService; //微博服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		List microblogs =  microblogService.readMicroblogs(StringUtils.getPropertyLongValue(recordListModel.getExtendProperties(), "microblogAccountId", 0), view.getPageRows());
		return new RecordListData(microblogs);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#writeRecord(com.yuanluesoft.jeaf.view.model.View, java.lang.Object, int, int, org.w3c.dom.NodeList, org.w3c.dom.Element, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, javax.servlet.http.HttpServletRequest, int)
	 */
	protected void writeRecord(View view, Object record, int recordIndex, int offset, NodeList recordFormatNodes, Element recordContainer, HTMLElement pageElement, RecordList recordListModel, SitePage sitePage, HTMLParser htmlParser, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		super.writeRecord(view, record, recordIndex, offset, recordFormatNodes, recordContainer, pageElement, recordListModel, sitePage, htmlParser, webDirectory, parentSite, requestInfo, request);
		Microblog microblog = (Microblog)record;
		//查找微博配图
		HTMLElement microblogImagesElement = htmlParser.getElementById((HTMLElement)recordContainer, "div", "microblogImages");
		writeMicroblogImages(microblogImagesElement, microblog, htmlParser);
		//处理被转发的原微博
		if(microblog.getRetweetedMicroblog()!=null) {
			microblogImagesElement = htmlParser.getElementById((HTMLElement)recordContainer, "div", "retweetedMicroblogImages");
			writeMicroblogImages(microblogImagesElement, microblog.getRetweetedMicroblog(), htmlParser);
		}
	}
	
	/**
	 * 输出微博配图
	 * @param microblogImagesElement
	 * @param microblog
	 * @param htmlParser
	 * @throws ServiceException
	 */
	private void writeMicroblogImages(HTMLElement microblogImagesElement, Microblog microblog, HTMLParser htmlParser) throws ServiceException {
		if(microblogImagesElement==null) {
			return;
		}
		if(microblog.getImages()==null || microblog.getImages().isEmpty()) {
			microblogImagesElement.getParentNode().removeChild(microblogImagesElement);
			return;
		}
		htmlParser.setTextContent(microblogImagesElement, null);
		String width = microblogImagesElement.getAttribute("width");
		String height = microblogImagesElement.getAttribute("height");
		microblogImagesElement.removeAttribute("id");
		microblogImagesElement.removeAttribute("width");
		microblogImagesElement.removeAttribute("height");
		width = width==null ? "80px" : width;
		height = height==null ? "80px" : height;
		for(int i=0; i<microblog.getImages().size(); i++) {
			MicroblogImage microblogImage = (MicroblogImage)microblog.getImages().get(i);
			if(microblog.getImages().size()>3 && i>0 && i % (microblog.getImages().size()<5 ? 2 : 3)==0) {
				microblogImagesElement.appendChild(microblogImagesElement.getOwnerDocument().createElement("br"));
			}
			HTMLAnchorElement a = (HTMLAnchorElement)microblogImagesElement.getOwnerDocument().createElement("a");
			a.setAttribute("class", "microblogImage");
			a.setHref(microblogImage.getOriginalUrl());
			a.setTarget("_blank");
			microblogImagesElement.appendChild(a);
			HTMLImageElement image = (HTMLImageElement)microblogImagesElement.getOwnerDocument().createElement("img");
			image.setBorder("0");
			image.setSrc(microblogImage.getThumbnailUrl());
			a.appendChild(image);
			if(microblog.getImages().size()>1) {
				image.setWidth(width);
				a.setAttribute("style", "width:" + width + "; height:" + height + "; margin-right:5px; margin-bottom:5px; display:inline-block; overflow:hidden;");
			}
		}
	}

	/**
	 * @return the microblogService
	 */
	public MicroblogService getMicroblogService() {
		return microblogService;
	}

	/**
	 * @param microblogService the microblogService to set
	 */
	public void setMicroblogService(MicroblogService microblogService) {
		this.microblogService = microblogService;
	}
}