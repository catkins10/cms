package com.yuanluesoft.job.talent.pages;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.job.talent.pojo.JobTalentReport;

/**
 * 
 * @author chuan
 *
 */
public class TalentReportPageService extends PageService {
	private DatabaseService databaseService;
	private HTMLParser htmlParser;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#buildHTMLDocument(org.w3c.dom.html.HTMLDocument, long, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument buildHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, boolean editMode) throws ServiceException, IOException {
		String talentIds = request.getParameter("talentIds");
		String hql = "from JobTalentReport JobTalentReport" +
					 " where JobTalentReport.talentId in (" + JdbcUtils.validateInClauseNumbers(talentIds) + ")";
		List reports = getDatabaseService().findRecordsByHql(hql);
		if(reports==null || reports.isEmpty()) {
			return super.buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode);
		}
		reports = ListUtils.sortByProperty(reports, "talentId", talentIds);
		HTMLDocument printDocument = null;
		for(Iterator iterator = reports.iterator(); iterator.hasNext();) {
			JobTalentReport report = (JobTalentReport)iterator.next();
			HTMLDocument htmlDocument = (HTMLDocument)templateDocument.cloneNode(true);
			sitePage.setAttribute("record", report);
			htmlDocument = getPageBuilder().buildHTMLDocument(htmlDocument, 0, sitePage, request, false, false, false, false);
			if(printDocument==null) {
				printDocument = (HTMLDocument)htmlDocument.cloneNode(true);
				printDocument.getBody().setAttribute("onload", "window.print()");
				continue;
			}
			//添加分页标志
			HTMLElement pageBreak = (HTMLElement)printDocument.createElement("div");
			pageBreak.setAttribute("style", "page-break-after:always;");
			printDocument.getBody().appendChild(pageBreak);
			//添加下一个记录
			htmlParser.importNodes(htmlDocument.getBody().getChildNodes(), printDocument.getBody(), false);
		}
		return printDocument;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}
}