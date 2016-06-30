package com.yuanluesoft.bidding.project.processor;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class BiddingRecordListProcessor extends RecordListProcessor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#resetView(com.yuanluesoft.jeaf.view.model.View, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, org.w3c.dom.html.HTMLDocument, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected void resetView(View view, HTMLElement pageElement, RecordList recordListModel, HTMLDocument recordFormatDocument, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String pojoName = BiddingProject.class.getName().equals(view.getPojoClassName()) ? "BiddingProject" : "project";
		//工程类别
		String projectCategory = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "category");
		if(projectCategory!=null && !projectCategory.isEmpty()) {
			view.addWhere(pojoName + ".projectCategory in ('" + JdbcUtils.resetQuot(projectCategory).replaceAll(",", "','")  + "')");
		}
		//招标内容
		String projectProcedure = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "procedure");
		if(projectProcedure!=null && !projectProcedure.isEmpty()) {
			view.addWhere(pojoName + ".projectProcedure in ('" + JdbcUtils.resetQuot(projectProcedure).replaceAll(",", "','")  + "')");
		}
		//所属地区
		String city = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "city");
		if(city!=null && !city.isEmpty()) {
			view.addWhere(pojoName + ".city in ('" + JdbcUtils.resetQuot(city).replaceAll(",", "','")  + "')");
		}
		//招标方式
		String biddingMode = StringUtils.getPropertyValue(recordListModel.getExtendProperties(), "biddingMode");
		if(biddingMode!=null && !biddingMode.isEmpty()) {
			view.addWhere(pojoName + ".biddingMode in ('" + JdbcUtils.resetQuot(biddingMode).replaceAll(",", "','")  + "')");
		}
		super.resetView(view, pageElement, recordListModel, recordFormatDocument, webDirectory, parentSite, htmlParser, sitePage, request);
	}
}