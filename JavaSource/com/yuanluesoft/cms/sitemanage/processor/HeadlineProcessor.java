package com.yuanluesoft.cms.sitemanage.processor;

import java.io.File;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageElement;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.Headline;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.image.generator.ImageGenerator;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class HeadlineProcessor implements PageElementProcessor {
	private SiteService siteService; //站点服务
	private PageBuilder pageBuilder;
	private ImageGenerator imageGenerator; //图片生成器
	private ExchangeClient exchangeClient; //数据交换客户端
	private String sitePagePath; //站点页面文件存储路径
	private String sitePageUrl; //站点页面文件URL
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement anchorElement = (HTMLAnchorElement)pageElement;
		//解析头版头条配置
		String urn = pageElement.getAttribute("urn");
		long siteId =  StringUtils.getPropertyLongValue(urn, "siteId", 0); //站点ID
		String openMode = StringUtils.getPropertyValue(urn, "openMode"); //打开方式
		String format = StringUtils.getPropertyValue(urn, "format"); //格式
		
		//获取头版头条
		final Headline headline = siteService.getHeadline(siteId==-1 ? webDirectory.getId() : siteId);
		if(headline==null) {
			pageElement.getParentNode().removeChild(pageElement); //没有配置头版头条,删除页面元素
			return;
		}
		
		//删除过期的头版头条图片
		deleteOverdueFiles(headline);
		
		//删除配置属性
		pageElement.removeAttribute("id");
		pageElement.removeAttribute("urn");
		
		//输出链接
		String url = headline.getHeadlineURL();
		LinkUtils.writeLink(anchorElement, "{FINAL}" + url, openMode, webDirectory.getId(), siteId, null, false, false, sitePage, request);
		
		//清空内容
		htmlParser.setTextContent(anchorElement, null);
		
		//解析格式
		HTMLDocument headlineDocument = htmlParser.parseHTMLString("<div id=\"headlineFormat\">" + format + "</div>", "utf-8");
		NodeList headlineNodes = headlineDocument.getElementById("headlineFormat").getChildNodes();
		if(headlineNodes==null || headlineNodes.getLength()==0) {
			return;
		}
		for(int i=0; i<headlineNodes.getLength(); i++) {
			Node headlineNode = anchorElement.getOwnerDocument().importNode(headlineNodes.item(i), true);
			anchorElement.appendChild(headlineNode);
			if(!(headlineNode instanceof Element)) {
				continue;
			}
			if(headlineNode instanceof HTMLAnchorElement) {
				HTMLAnchorElement fieldElement = (HTMLAnchorElement)headlineNode;
				writeHeadlineElement(headline, fieldElement, webDirectory, parentSite, htmlParser, sitePage, request);
			}
			else {
				NodeList elements = ((Element)headlineNode).getElementsByTagName("a");
				for(int k=elements.getLength()-1; k>=0; k--) {
					HTMLAnchorElement fieldElement = (HTMLAnchorElement)elements.item(k);
					writeHeadlineElement(headline, fieldElement, webDirectory, parentSite, htmlParser, sitePage, request);
				}
			}
		}
	}
	
	/**
	 * 删除原来的头版头条图片
	 * @param headline
	 */
	private void deleteOverdueFiles(final Headline headline) {
		if(headline.getLastModified()==null) {
			return;
		}
		final long headlineLastModified = headline.getLastModified().getTime();
		File headlineDirectory = new File(sitePagePath + "headline/" + headline.getDirectoryId());
		if(!headlineDirectory.exists()) {
			return;
		}
		final File[] headlineImageDirectories = headlineDirectory.listFiles();
		final List overdueFiles = new ArrayList();
		for(int i=(headlineImageDirectories==null ? -1 : headlineImageDirectories.length-1); i>=0; i--) {
			File[] files = headlineImageDirectories[i].listFiles();
			for(int j=(files==null ? -1 : files.length-1); j>=0; j--) {
				if(files[j].lastModified()<headlineLastModified) {
					overdueFiles.add(files[j].getPath());
				}
			}
		}
		if(overdueFiles.isEmpty()) { //没有过期的文件
			return;
		}
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					for(Iterator iterator = overdueFiles.iterator(); iterator.hasNext();) {
						String fileName = (String)iterator.next();
						File overdueFile = new File(fileName);
						if(overdueFile.lastModified()>=headlineLastModified) {
							iterator.remove();
						}
						else {
							overdueFile.delete();
						}
					}
					//删除空目录
					for(int i=(headlineImageDirectories==null ? -1 : headlineImageDirectories.length-1); i>=0; i--) {
						if(FileUtils.isEmpty(headlineImageDirectories[i].getPath())) {
							overdueFiles.add(headlineImageDirectories[i].getPath());
							FileUtils.deleteDirectory(headlineImageDirectories[i].getPath());
						}
					}
					if(!overdueFiles.isEmpty()) {
						exchangeClient.deleteFiles(overdueFiles, null, true); //数据交换
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				timer.cancel();
			}
		}, 1200000); //20分钟后删除
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageGenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		if(!sitePage.isRealtimeStaticPage()) { //不需要实时生成静态页面,避免过多的重建静态页面
			return;
		}
		String urn = pageElement.getAttribute("urn");
		long siteId =  StringUtils.getPropertyLongValue(urn, "siteId", 0); //站点ID
		if(siteId==-1) {
			siteId = webDirectory.getId(); 
		}
		//检查是否已经创建过
		String hql = "select StaticPageElement.id" +
					 " from StaticPageElement StaticPageElement" +
					 " where StaticPageElement.pageId=" + staticPageId +
					 " and StaticPageElement.elementName='" + JdbcUtils.resetQuot(pageElement.getId()) + "'" +
					 " and StaticPageElement.siteId=" + siteId;
		if(databaseService.findRecordByHql(hql)==null) {
			//保存引用的页面元素
			StaticPageElement staticPageElement = new StaticPageElement();
			staticPageElement.setId(UUIDLongGenerator.generateId()); //页面ID
			staticPageElement.setPageId(staticPageId); //页面ID
			staticPageElement.setElementName(pageElement.getId()); //页面元素名称
			staticPageElement.setSiteId(siteId); //隶属的站点/栏目ID
			databaseService.saveRecord(staticPageElement);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		if(!(object instanceof Headline)) {
			return null;
		}
		Headline headline = (Headline)object;
		String hql = "select distinct StaticPage" +
		  			 " from StaticPage StaticPage, StaticPageElement StaticPageElement" +
		  			 " where StaticPage.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(baseTime, null) + ")" +
		  			 " and StaticPageElement.pageId=StaticPage.id" +
		  			 " and StaticPageElement.elementName='headline'" +
		  			 " and StaticPageElement.siteId=" + headline.getDirectoryId();
		return databaseService.findRecordsByHql(hql, 0, max);
	}
	
	/**
	 * 输出头版头条元素
	 * @param headline
	 * @param headlineElement
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @param pageMode
	 * @throws ServiceException
	 */
	private void writeHeadlineElement(Headline headline, HTMLAnchorElement headlineElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String anchorId = headlineElement.getId();
		if("field".equals(anchorId)) { //概述
			try {
				sitePage = (SitePage)sitePage.clone();
			} 
			catch (CloneNotSupportedException e) {
				
			}
			sitePage.setAttribute("record", headline);
			pageBuilder.processPageElement(headlineElement, webDirectory, parentSite, sitePage, RequestUtils.getRequestInfo(request), request);
		}
		else if("headlineImage".equals(anchorId)) { //标题图片
			//解析参数
			String urn = headlineElement.getAttribute("urn");
			int minCharCount = StringUtils.getPropertyIntValue(urn, "minCharCount", 10);
			int maxCharCount = Math.min(200, StringUtils.getPropertyIntValue(urn, "maxCharCount", 50));
			int width = StringUtils.getPropertyIntValue(urn, "width", 380);
			int height = StringUtils.getPropertyIntValue(urn, "height", 38);
			String font = StringUtils.getPropertyValue(urn, "font");
			boolean bold = "true".equals(StringUtils.getPropertyValue(urn, "bold"));
			boolean italic = "true".equals(StringUtils.getPropertyValue(urn, "italic"));
			String textColor = StringUtils.getPropertyValue(urn, "textColor");
			String backgroundColor = "true".equals(StringUtils.getPropertyValue(urn, "backgroundTransparent")) ? null : StringUtils.getPropertyValue(urn, "backgroundColor");
			String ellipsis = StringUtils.getPropertyValue(urn, "ellipsis");

			//生成需要输出的文字
			String text = headline.getHeadlineName();
			int len = text.getBytes().length;
			if(len>maxCharCount) { //超出最大字节数
				text = StringUtils.slice(text, maxCharCount, (ellipsis==null ? "" : ellipsis));
			}
			else if(len<minCharCount) { //少于最少字节数
				//补空格
				String spaces = "";
				for(int i=0; i<(minCharCount-len + 1) / 2; i++) {
					spaces += " ";
				}
				text = spaces + text + spaces;
			}
			String headlineImageFile = Math.abs(text.hashCode()) + ".png";
			String headlineImagePath = "headline/" + webDirectory.getId() + "/" + Math.abs((sitePage.getApplicationName() + sitePage.getName() + width + height + font + bold + italic + textColor + backgroundColor).hashCode()) + "/";
			File headlineUmageFile;
			if(!FileUtils.isExists(sitePagePath + headlineImagePath + headlineImageFile)) {
				//生成图片
				String filePath = FileUtils.createDirectory(sitePagePath + headlineImagePath) + headlineImageFile;
				imageGenerator.generateTextImage(text, width, height, font, bold, italic, textColor, backgroundColor, filePath);
				exchangeClient.sendFiles(ListUtils.generateList(filePath), null, true, true); //数据交换
			}
			else if(headline.getLastModified()!=null && (headlineUmageFile=new File(sitePagePath + headlineImagePath + headlineImageFile)).lastModified()<headline.getLastModified().getTime()) {
				headlineUmageFile.setLastModified(headline.getLastModified().getTime());
			}
			//插入版头条标题图片
			/*HTMLImageElement img = (HTMLImageElement)headlineElement.getOwnerDocument().createElement("img");
			img.setBorder("0");
			img.setTitle(headline.getHeadlineName());
			try {
				img.setSrc(sitePageUrl + headlineImagePath + URLEncoder.encode(headlineImageFile, "utf-8"));
			}
			catch (Exception e) {
				Logger.exception(e);
			}
			headlineElement.getParentNode().insertBefore(img, headlineElement);*/
			HTMLElement span = (HTMLElement)headlineElement.getOwnerDocument().createElement("span");
			span.setId("" + UUIDLongGenerator.generateId());
			span.setAttribute("style", "display:inline-block; width:" + width + "px; height:" + height + "px;");
			span.setTitle(headline.getHeadlineName());
			String imageUrl = null; 
			try {
				imageUrl = sitePageUrl + headlineImagePath + URLEncoder.encode(headlineImageFile, "utf-8");
			}
			catch (Exception e) {
				Logger.exception(e);
			}
			headlineElement.getParentNode().insertBefore(span, headlineElement);
			HTMLScriptElement script = (HTMLScriptElement)headlineElement.getOwnerDocument().createElement("script");
			htmlParser.setTextContent(script, "var headlineSpan=document.getElementById('" + span.getId() + "');if(document.all){headlineSpan.style.filter='progid:DXImageTransform.Microsoft.AlphaImageLoader(src=" + imageUrl + ")';}else{headlineSpan.style.backgroundImage='url(" + imageUrl + ")'}");
			headlineElement.getParentNode().insertBefore(script, headlineElement);
			//删除配置元素
			headlineElement.getParentNode().removeChild(headlineElement);
		}
	}
	
	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	/**
	 * @return the imageGenerator
	 */
	public ImageGenerator getImageGenerator() {
		return imageGenerator;
	}

	/**
	 * @param imageGenerator the imageGenerator to set
	 */
	public void setImageGenerator(ImageGenerator imageGenerator) {
		this.imageGenerator = imageGenerator;
	}

	/**
	 * @return the sitePagePath
	 */
	public String getSitePagePath() {
		return sitePagePath;
	}

	/**
	 * @param sitePagePath the sitePagePath to set
	 */
	public void setSitePagePath(String sitePagePath) {
		this.sitePagePath = sitePagePath;
	}

	/**
	 * @return the sitePageUrl
	 */
	public String getSitePageUrl() {
		return sitePageUrl;
	}

	/**
	 * @param sitePageUrl the sitePageUrl to set
	 */
	public void setSitePageUrl(String sitePageUrl) {
		this.sitePageUrl = sitePageUrl;
	}

	/**
	 * @return the pageBuilder
	 */
	public PageBuilder getPageBuilder() {
		return pageBuilder;
	}

	/**
	 * @param pageBuilder the pageBuilder to set
	 */
	public void setPageBuilder(PageBuilder pageBuilder) {
		this.pageBuilder = pageBuilder;
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}
}