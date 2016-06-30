package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.io.File;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.HTMLTraversalCallback;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.video.model.Video;
import com.yuanluesoft.jeaf.video.service.VideoService;
import com.yuanluesoft.jeaf.video.util.VideoPlayerUtils;
import com.yuanluesoft.jeaf.view.util.HighLightCallback;
import com.yuanluesoft.jeaf.view.util.ViewUtils;

/**
 * 页面元素处理器：字段处理
 * @author linchuan
 *
 */
public class RecordFieldProcessor implements PageElementProcessor {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		//获取记录
		Object record = sitePage.getAttribute("record");
		//获取字段名称
		String fieldName;
		if(record==null || (fieldName=StringUtils.getPropertyValue(pageElement.getAttribute("urn"), "name"))==null || fieldName.equals("")) {
			//删除配置元素
			pageElement.getParentNode().removeChild(pageElement);
			return;
		}
		//获取字段定义
		Field field;
		if(record instanceof ActionForm) {
			field = FieldUtils.getFormField(((ActionForm)record).getFormDefine(), fieldName, request);
		}
		else if(record instanceof SqlResult) {
			String recordName = (String)((SqlResult)record).get("sqlRecordName");
			field = FieldUtils.getRecordField(recordName, fieldName, request);
		}
		else {
			field = FieldUtils.getRecordField(record.getClass().getName(), fieldName, request);
		}
		if(field==null) { //没有字段定义
			pageElement.getParentNode().removeChild(pageElement); //删除配置元素
			return;
		}
		//获取字段值
		Object fieldValue = getFieldValue(record, field, pageElement, webDirectory, parentSite, sitePage, request);
		if(fieldValue==null) { //没有值
			if(",td,th,".indexOf("," + pageElement.getParentNode().getNodeName().toLowerCase() + ",")!=-1) {
				//上级元素是表格,自动插入空格
				pageElement.getParentNode().replaceChild(pageElement.getOwnerDocument().createTextNode("\u00a0"), pageElement);
			}
			else {
				//删除配置元素
				pageElement.getParentNode().removeChild(pageElement);
			}
			return;
		}
		String fieldUrl = (String)field.getParameter("fieldUrl"); //字段自定义的URL
		//输出字段
		if("imageName".equals(field.getType())) { //图片字段
			writeImageField(record, field, (Image)fieldValue, fieldUrl, pageElement, webDirectory, parentSite, htmlParser, sitePage, request, requestInfo.getPageType());
		}
		else if("videoName".equals(field.getType())) { //视频字段
			writeVideoField(record, field, (Video)fieldValue, fieldUrl, pageElement, webDirectory, parentSite, htmlParser, sitePage, request, requestInfo.getPageType());
		}
		else { //其他,按文本格式输出
			writeTextField(record, field, fieldValue, fieldUrl, pageElement, webDirectory, parentSite, htmlParser, sitePage, request, requestInfo.getPageType());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#createStaticPageRegenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}
	
	/**
	 * 获取字段值
	 * @param record
	 * @param field
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected Object getFieldValue(Object record, Field field, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		Object value = sitePage.getAttribute(field.getName()); //从站点页面获取值
		if(value!=null) {
			return value;
		}
		try {
			value = FieldUtils.getFieldValue(record, field.getName(), request);
		}
		catch(Exception e) {
			return null;
		}
		return value!=null ? value : request.getAttribute(field.getName());
	}
	
	/**
	 * 获取字段颜色
	 * @param record
	 * @param field
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected String getFieldColor(Object record, Field field, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String color = (String)field.getParameter("color");
		if(color==null || color.isEmpty()) {
			return null;
		}
		return StringUtils.fillParameters(color, false, false, false, "utf-8", BeanUtils.getComponent(record, field.getName()), request, null);
	}
	
	/**
	 * 获取字段提示信息
	 * @param record
	 * @param field
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	protected String getFieldTitle(Object record, Field field, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String title = (String)field.getParameter("title");
		if(title==null || title.isEmpty()) {
			return null;
		}
		return StringUtils.fillParameters(title, false, false, false, "utf-8", BeanUtils.getComponent(record, field.getName()), request, null);
	}
	
	/**
	 * 输出文本格式的字段
	 * @param record
	 * @param field
	 * @param fieldValue
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @param pageMode
	 * @throws ServiceException
	 */
	protected HTMLElement writeTextField(final Object record, final Field field, Object fieldValue, String fieldUrl, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, final HTMLParser htmlParser, final SitePage sitePage, HttpServletRequest request, int pageMode) throws ServiceException {
		//解析字段样式
		String urn = pageElement.getAttribute("urn");
		String fontName = StringUtils.getPropertyValue(urn, "fontName"); //保存和旧系统兼容
		String fontSize = StringUtils.getPropertyValue(urn, "fontSize"); //保存和旧系统兼容
		String fontColor = StringUtils.getPropertyValue(urn, "fontColor"); //保存和旧系统兼容
		boolean writeLink = "true".equals(StringUtils.getPropertyValue(urn, "link"));
		boolean chineseNumber = "true".equals(StringUtils.getPropertyValue(urn, "chineseNumber"));
		int maxLength = StringUtils.getPropertyIntValue(urn, "maxLength", 0);
		String fieldFormat = StringUtils.getPropertyValue(urn, "fieldFormat");
		
		//获取字段值
		String textValue;
		try {
			textValue = FieldUtils.formatFieldValue(fieldValue, field, record, true, fieldFormat, false, (maxLength>0), !"true".equals(sitePage.getAttribute("fullAccess")), 0, null, null, request);
		}
		catch(Exception e) {
			Logger.exception(e);
			pageElement.getParentNode().removeChild(pageElement); //删除配置元素
			return null;
		}
		boolean isHtmlContent = maxLength==0 && ",html,opinion,attachment,image,video,".indexOf("," + field.getType() + ",")!=-1;
		boolean replace = isHtmlContent;
		if(!isHtmlContent) {
			pageElement.removeAttribute("id"); //删除ID
			pageElement.removeAttribute("urn"); //删除URL
			String style = pageElement.getAttribute("style");
			if(style==null) {
				style = "";
			}
			if(fontName!=null) { //字体
				style += ";font-name:" + fontName;
			}
			if(fontSize!=null) { //字体大小
				style += ";font-size:" + fontSize;
			}
			//字体颜色
			String color = getFieldColor(record, field, pageElement, webDirectory, parentSite, sitePage, request);
			if(color!=null && !color.isEmpty()) { //由字段本身决定
				fontColor = color;
			}
			if(fontColor!=null) { //字体颜色
				style = StringUtils.removeStyles(style, "color") + ";color:" + fontColor;
			}
			//设置提示信息
			String title = getFieldTitle(record, field, pageElement, webDirectory, parentSite, sitePage, request);
			if(title!=null && !title.isEmpty()) {
				pageElement.setTitle(title);
			}
			if(!style.equals("")) { //设置样式
				pageElement.setAttribute("style", style);
			}
			if(writeLink) { //设置链接
				writeLink(record, field, fieldValue, fieldUrl, pageElement, webDirectory, parentSite, htmlParser, sitePage, request, pageMode);
			}
			if(textValue!=null && "true".equals(sitePage.getAttribute("fromRecordList")) && (maxLength>0 || !"true".equals(field.getParameter("keepAllInRecordList")))) { //记录列表
				textValue = textValue.replaceAll("[\\r\\n]+", " ").replaceAll("[ ]+", " "); //清除换行以及重复的空格
			}
			if(textValue!=null && maxLength>0) { //设置最大字符数
				String newValue = StringUtils.slice(textValue, maxLength, "...");
				if(!newValue.equals(textValue)) {
					if(",html,opinion,attachment,image,video,".indexOf("," + field.getType() + ",")==-1) { //不是html,option等类型的字段
						pageElement.setTitle(textValue);
					}
					textValue = newValue;
				}
			}
			//获取替代图片
			String insteadImage = StringUtils.getPropertyValue(urn, "imageInstead_" + field.getName() + "_" + textValue);
			if(insteadImage!=null && !insteadImage.equals("")) {
				HTMLImageElement img = (HTMLImageElement)pageElement.getOwnerDocument().createElement("img");
				img.setBorder("0");
				img.setSrc(insteadImage);
				img.setTitle(textValue);
				//设置替代图片的尺寸
				String insteadImageWidth = StringUtils.getPropertyValue(urn, "insteadImageWidth");
				String insteadImageHeight = StringUtils.getPropertyValue(urn, "insteadImageHeight");
				if(insteadImageWidth!=null && !insteadImageWidth.isEmpty()) {
					img.setWidth(insteadImageWidth);
				}
				if(insteadImageHeight!=null && !insteadImageHeight.isEmpty()) {
					img.setHeight(insteadImageHeight);
				}
				//设置替代图片的对齐方式
				String insteadImageAlign = StringUtils.getPropertyValue(urn, "insteadImageAlign");
				if(insteadImageAlign!=null && !insteadImageAlign.isEmpty()) {
					img.setAlign(insteadImageAlign);
				}
				if(pageElement.getAttributes().getLength()==0) {  //不保留<A>
					pageElement.getParentNode().replaceChild(img, pageElement);
					return img;
				}
				else { //保留<A>
					htmlParser.setTextContent(pageElement, null);
					pageElement.appendChild(img);
					return pageElement;
				}
			}
			
			//如果字段值为空,且上级元素是表格,自动插入空格
			if(textValue==null || textValue.isEmpty() && ",td,th,".indexOf("," + pageElement.getParentNode().getNodeName().toLowerCase() + ",")!=-1) {
				textValue = "\u00a0";
			}
			if(chineseNumber && textValue!=null && "number".equals(field.getType())) {
				try {
					textValue = StringUtils.getChineseNumber(Integer.parseInt(textValue), false);
				}
				catch(Exception e) {
					
				}
			}
			//输出字段
			if(pageElement.getAttributes().getLength()==0) {  //不保留<A>
				replace = true;
			}
			else if(pageElement.getAttributes().getLength()==1 && pageElement.getAttributes().item(0).getNodeName().equalsIgnoreCase("title")) { //只有一个title属性
				//转换为span
				HTMLElement element = (HTMLElement)pageElement.getOwnerDocument().createElement("span");
				element.setTitle(pageElement.getTitle());
				pageElement.getParentNode().replaceChild(element, pageElement);
				pageElement = element;
			}
			else { //保留<A>
				htmlParser.setTextContent(pageElement, null);
			}
			textValue = StringUtils.escape(textValue);
			if(textValue!=null && maxLength<=0) { //未设置最大字符数
				textValue = textValue.replaceAll("\\r", "").replaceAll("\\n", "<br/>");
			}
		}
		if(textValue==null || textValue.isEmpty()) {
			pageElement.getParentNode().removeChild(pageElement);
			return null;
		}
		//解析正文
		HTMLDocument bodyDocument = htmlParser.parseHTMLString(textValue, "utf-8");
		final HTMLDocument page = (HTMLDocument)pageElement.getOwnerDocument();
		htmlParser.traversalChildNodes(bodyDocument.getBody().getChildNodes(), false, new HTMLTraversalCallback() {
			public boolean processNode(Node node) {
				try {
					if(node instanceof Text) {
						highlightKeywords((Text)node, field, record, sitePage, htmlParser);
						return true;
					}
					else if(node instanceof HTMLScriptElement) {
						//引入视频播放器JS
						if((Environment.getContextPath() + "/jeaf/video/player/js/videoplayer.js").equals(((HTMLScriptElement)node).getSrc()) ||
						   (Environment.getContextPath() + "/jeaf/common/js/progressbar.js").equals(((HTMLScriptElement)node).getSrc())) {
							htmlParser.appendScriptFile(page, ((HTMLScriptElement)node).getSrc());
							node.getParentNode().removeChild(node);
							return true;
						}
					}
					else if(node instanceof HTMLLinkElement) {
						//引入样式表
						if((Environment.getContextPath() + "/jeaf/video/player/css/videocontroller.css").equals(((HTMLLinkElement)node).getHref())) {
							htmlParser.appendCssFile(page, ((HTMLLinkElement)node).getHref(), true);
							node.getParentNode().removeChild(node);
							return true;
						}
					}
				}
				catch(Exception e) {
					
				}
				return false;
			}
		});
		if(isHtmlContent) { //客户端页面
			resetImagesInClientPage(bodyDocument, record, pageMode, sitePage, request); //重设图片和视频
		}
		//插入HTML内容
		for(Node bodyNode = bodyDocument.getBody().getFirstChild(); bodyNode!=null; bodyNode = bodyNode.getNextSibling()) {
			try {
				if(replace) {
					pageElement.getParentNode().insertBefore(pageElement.getOwnerDocument().importNode(bodyNode, true), pageElement);
				}
				else {
					pageElement.appendChild(pageElement.getOwnerDocument().importNode(bodyNode, true));
				}
			}
			catch(DOMException e) {
				if(e.getMessage()==null || e.getMessage().indexOf("INVALID_CHARACTER_ERR")==-1) {
					continue;	
				}
				try {
					PropertyUtils.setProperty(pageElement.getOwnerDocument(), "errorChecking", Boolean.FALSE); //不做错误校验
				}
				catch (Exception ex) {
					
				}
				if(replace) {
					pageElement.getParentNode().insertBefore(pageElement.getOwnerDocument().importNode(bodyNode, true), pageElement);
				}
				else {
					pageElement.appendChild(pageElement.getOwnerDocument().importNode(bodyNode, true));
				}
			}
		}
		if(!replace) {
			return pageElement;
		}
		//删除字段配置
		Node node = pageElement.getParentNode();
		node.removeChild(pageElement);
		return (HTMLElement)node;
	}
	
	/**
	 * 高亮关键字
	 * @param textNode
	 * @param fieldName
	 * @param record
	 * @param sitePage
	 * @param htmlParser
	 */
	private void highlightKeywords(final Text textNode, Field field, Object record, SitePage sitePage, final HTMLParser htmlParser) {
		if(!"true".equals(sitePage.getAttribute("searchResults")) || //不是搜索结果
		   ",string,html,".indexOf("," + field.getType() + ",")==-1) { //字段类型不是字符串、HTML
			return;
		}
		List searchConditionList = (List)sitePage.getAttribute(RecordListProcessor.RECORDLIST_ATTRIBUTE_SEARCH_CONDITIONS);
		boolean chnaged = ViewUtils.writeHighLightKeyWords(searchConditionList, field.getName(), textNode.getTextContent(), new HighLightCallback() {
			public void write(String text, boolean highLight) {
				Node node;
				if(!highLight) {
					node = textNode.getOwnerDocument().createTextNode(text);
				}
				else {
					node = textNode.getOwnerDocument().createElement("span");
					((HTMLElement)node).setClassName("highlight"); //设置样式为"highlight"
					htmlParser.setTextContent(node, text);
				}
				textNode.getParentNode().insertBefore(node, textNode);
			}
		});
		if(chnaged) {
			textNode.getParentNode().removeChild(textNode);
		}
	}
	
	/**
	 * 判断是否客户端页面,如果是重设图片尺寸
	 * @param bodyDocument
	 * @param record
	 * @param pageMode
	 * @param sitePage
	 * @param request
	 */
	private void resetImagesInClientPage(HTMLDocument bodyDocument, Object record, int pageMode, SitePage sitePage, HttpServletRequest request) {
		if((pageMode!=RequestInfo.PAGE_TYPE_CLIENT_DATA && pageMode!=RequestInfo.PAGE_TYPE_CLIENT_POST) || !sitePage.isRecordPage()) { //不是客户端页面,或者不是记录页面
			return;
		}
		if(record==null || !(record instanceof Record)) { //记录不为空
			return;
		}
		int imageWidth = RequestUtils.getParameterIntValue(request, "client.imageWidth"); //图片宽度
		if(imageWidth <= 0) {
			return;
		}
		//获取图片定义
		List imageFields = FieldUtils.listRecordFields(record.getClass().getName(), "image", null, null, null, true, true, false, false, 0);
		if(imageFields==null || imageFields.isEmpty()) {
			return;
		}
		double density = Math.max(1, RequestUtils.getParameterDoubleValue(request, "client.density")); //密度
		//重设图片
		NodeList images = bodyDocument.getElementsByTagName("img");
		for(int i=(images==null ? -1 : images.getLength()-1); i>=0; i--) {
			HTMLImageElement imageElement = (HTMLImageElement)images.item(i);
			if("window.open(src)".equals(imageElement.getAttribute("onclick"))) {
				imageElement.removeAttribute("onclick");
			}
			for(Iterator iterator = imageFields.iterator(); iterator.hasNext();) {
				Field imageField = (Field)iterator.next();
				try {
					String serviceName = (String)imageField.getParameter("serviceName");
					ImageService imageService = (ImageService)Environment.getService(serviceName==null ? "imageService" : serviceName);
					String imagePath = imageService.getDownloadFilePath(imageElement.getSrc());
					String imageType = imageService.getFileType(imageElement.getSrc());
					Image image = imageService.getImage(sitePage.getApplicationName(), imageType, ((Record)record).getId(), new File(imagePath).getName(), request);
					if(image.getWidth()<=imageWidth) { //未超过客户端能显示的宽度
						break;
					}
					imageElement.setHeight(null);
					imageElement.setWidth("" + imageWidth);
					String style = StringUtils.removeStyles(imageElement.getAttribute("style"), "width,height");
					if(style!=null) {
						imageElement.setAttribute("style", style);
					}
					if(image.getWidth() <= imageWidth * density) {
						break;
					}
					//创建链接,把图片框起来
					HTMLAnchorElement a = (HTMLAnchorElement)bodyDocument.createElement("a");
					a.setHref(imageElement.getSrc());
					imageElement.getParentNode().replaceChild(a, imageElement);
					a.appendChild(imageElement);
					//输出缩略图
					double width = Math.ceil(imageWidth * density / 160.0) * 160;
					if(width < image.getWidth()) {
						image = imageService.getBreviaryImage(sitePage.getApplicationName(), imageType, ((Record)record).getId(), image.getName(), (int)width, (int)((image.getHeight()  + 0.0) * width / image.getWidth()), false, request);
						imageElement.setSrc(image.getUrlInline());
					}
					break;
				}
				catch(Exception e) {
					
				}
			}
		}
	}
	
	/**
	 * 输出图片字段
	 * @param record
	 * @param field
	 * @param image
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @param pageMode
	 * @throws ServiceException
	 */
	protected HTMLElement writeImageField(Object record, Field field, Image image, String fieldUrl, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request, int pageMode) throws ServiceException {
		//解析字段配置
		String urn = pageElement.getAttribute("urn");
		int imageWidth = StringUtils.getPropertyIntValue(urn, "imageWidth", 0);
		int imageHeight = StringUtils.getPropertyIntValue(urn, "imageHeight", 0);
		boolean clipEnabled = "true".equals(StringUtils.getPropertyValue(urn, "clipEnabled"));
		String align = StringUtils.getPropertyValue(urn, "imageAlign");
		boolean writeLink = "true".equals(StringUtils.getPropertyValue(urn, "link"));
		if((imageWidth>0 || imageHeight>0) && image.getService()!=null) { //指定了图像的尺寸
			try {
				//生成缩略图
				ImageService imageService = (ImageService)image.getService();
				double scale = 1;
				if(pageMode==RequestInfo.PAGE_TYPE_CLIENT_DATA || pageMode==RequestInfo.PAGE_TYPE_CLIENT_POST) { //客户端页面,图片尺寸最多放大2倍
					scale = Math.max(1, Math.min(Math.min(640.0/imageWidth, 480.0/imageHeight), 2));
				}
				image = imageService.getBreviaryImage(image.getApplicationName(), image.getType(), image.getRecordId(), image.getName(), (int)(imageWidth * scale), (int)(imageHeight * scale), clipEnabled, request);
			}
			catch(Exception e) {
				
			}
		}
		if(image.getUrl()==null) { //没有图片链接
			//删除配置元素
			pageElement.getParentNode().removeChild(pageElement);
			return null;
		}
		pageElement.removeAttribute("id"); //删除ID
		pageElement.removeAttribute("urn"); //删除URL
		//创建IMG元素
		HTMLImageElement imageElement = (HTMLImageElement)pageElement.getOwnerDocument().createElement("img");
		imageElement.setAlign(align);
		imageElement.setBorder("0");
		imageElement.setSrc(image.getUrl());
		imageElement.setWidth("" + (imageWidth>0 ? imageWidth : image.getWidth()));
		imageElement.setHeight("" + (imageHeight>0 ? imageHeight : image.getHeight()));
		String alt = (String)field.getParameter("alt");
		if(alt!=null) {
			alt = StringUtils.fillParameters(alt, false, false, false, "utf-8", BeanUtils.getComponent(record, field.getName()), request, null);
			if(alt!=null && !alt.isEmpty()) {
				imageElement.setAlt(alt);
				imageElement.setTitle(alt);
			}
		}
		String title = getFieldTitle(record, field, pageElement, webDirectory, parentSite, sitePage, request);
		if(title!=null && !title.isEmpty()) {
			imageElement.setTitle(title);
		}
		if(writeLink) {
			pageElement.removeAttribute("urn");
			writeLink(record, field, image, fieldUrl, pageElement, webDirectory, parentSite, htmlParser, sitePage, request, pageMode); //设置链接
		}
		if(pageElement.getAttributes().getLength()==0) {
			imageElement.setAttribute("style", pageElement.getAttribute("style"));
			pageElement.getParentNode().replaceChild(imageElement, pageElement);
			return imageElement;
		}
		else {
			htmlParser.setTextContent(pageElement, null);
			pageElement.appendChild(imageElement);
			return pageElement;
		}
	}
	
	/**
	 * 输出视频字段
	 * @param record
	 * @param field
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param request
	 * @param pageMode
	 * @param videoValue
	 * @throws ServiceException
	 */
	protected void writeVideoField(Object record, Field field, Video video, String fieldUrl, HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request, int pageMode) throws ServiceException {
		//解析格式
		String urn = pageElement.getAttribute("urn");
		int videoWidth = StringUtils.getPropertyIntValue(urn, "videoWidth", 320);
		int videoHeight = StringUtils.getPropertyIntValue(urn, "videoHeight", 240);
		String align = StringUtils.getPropertyValue(urn, "videoAlign");
		boolean autoStart = "true".equals(StringUtils.getPropertyValue(urn, "autostart")); //是否自动播放
		boolean controlbar = "true".equals(StringUtils.getPropertyValue(urn, "controlbar")); //是否显示操作条
		boolean writeLink = "true".equals(StringUtils.getPropertyValue(urn, "link")); //是否显示链接
		boolean captureImage = "true".equals(StringUtils.getPropertyValue(urn, "captureImage")); //是否显示截屏

		//获取视频字段
		if(videoWidth==0 || videoHeight==0) { //没有指定视频大小
			videoWidth = video.getVideoWidth();
			videoHeight = video.getVideoHeight();
		}
		
		//获取缩略图
		String previewImageUrl = null;
		try {
			//生成缩略图
			VideoService videoService = (VideoService)video.getService();
			previewImageUrl = videoService.getBreviaryImage(video.getApplicationName(), video.getType(), video.getRecordId(), video.getName(), videoWidth, videoHeight, request).getUrl();
		}
		catch(Exception e) {
			
		}
		if(!captureImage) {
			//引入视频播放器JS
			htmlParser.appendScriptFile((HTMLDocument)pageElement.getOwnerDocument(), Environment.getContextPath() + "/jeaf/video/player/js/videoplayer.js");
			htmlParser.appendScriptFile((HTMLDocument)pageElement.getOwnerDocument(), Environment.getContextPath() + "/jeaf/common/js/progressbar.js");
			//引入样式表
			htmlParser.appendCssFile((HTMLDocument)pageElement.getOwnerDocument(), Environment.getContextPath() + "/jeaf/video/player/css/videocontroller.css", true);
			//创建加载视频脚本
			HTMLScriptElement script = (HTMLScriptElement)pageElement.getOwnerDocument().createElement("script");
			htmlParser.setTextContent(script, VideoPlayerUtils.generateVideoPlayerScript(video.getApplicationName(), video.getRecordId(), video.getType(), video.getName(), videoWidth, videoHeight, autoStart, !controlbar, parentSite.getId(), request));
			//替换页面元素
			pageElement.getParentNode().replaceChild(script, pageElement);
			return;
		}
		
		//显示截屏
		pageElement.removeAttribute("id"); //删除ID
		pageElement.removeAttribute("urn"); //删除URL
		
		//创建IMG元素
		HTMLImageElement imageElement = (HTMLImageElement)pageElement.getOwnerDocument().createElement("img");
		imageElement.setAlign(align);
		imageElement.setBorder("0");
		imageElement.setSrc(previewImageUrl);
		if(videoWidth>0) {
			imageElement.setWidth("" + videoWidth);
		}
		if(videoHeight>0) {
			imageElement.setHeight("" + videoHeight);
		}
		if(!writeLink) { //不需要链接
			pageElement.getParentNode().replaceChild(imageElement, pageElement);
			pageElement = imageElement;
		}
		else {
			pageElement.removeAttribute("urn");
			writeLink(record, field, null, fieldUrl, pageElement, webDirectory, parentSite, htmlParser, sitePage, request, pageMode); //设置链接
			htmlParser.setTextContent(pageElement, null);
			pageElement.appendChild(imageElement);
		}
		if(videoWidth<=0 || videoHeight<=0) {
			return;
		}
		String showPlayIcon = StringUtils.getPropertyValue(urn, "showPlayIcon"); //显示播放图标,不显示/none,预置图标/default,自定义/custom
		if(!"default".equals(showPlayIcon) && !"custom".equals(showPlayIcon)) {
			return;
		}
		String playIconURL = StringUtils.getPropertyValue(urn, "playIconURL"); //播放图标URL
		int playIconWidth = StringUtils.getPropertyIntValue(urn, "playIconWidth", 0); //播放图标宽度
		int playIconHeight = StringUtils.getPropertyIntValue(urn, "playIconHeight", 0); //播放图标高度
		String playIconPosition = StringUtils.getPropertyValue(urn, "playIconPosition"); //播放图标显示位置,居中|innerCenter\0左上|innerLeftTop\0左下|innerLeftBottom\0左侧居中|innerLeftMiddle\0右上|innerRightTop\0右下|innerRightBottom\0右侧居中|innerRightMiddle\0顶部居中|innerTopCenter\0底部居中
		int playIconXMargin = StringUtils.getPropertyIntValue(urn, "playIconXMargin", 0); //播放图标水平边距
		int playIconYMargin = StringUtils.getPropertyIntValue(urn, "playIconYMargin", 0); //播放图标垂直边距
		if("default".equals(showPlayIcon)) {
			if(videoWidth<200 || videoHeight<150) {
				playIconURL = Environment.getContextPath() + "/cms/image/video_play_small.png";
				playIconWidth = 22;
				playIconHeight = 22;
			}
			else {
				playIconURL = Environment.getContextPath() + "/cms/image/video_play.png";
				playIconWidth = 50;
				playIconHeight = 50;
			}
		}
		if(videoWidth<=playIconWidth || videoHeight<=playIconHeight) {
			return;
		}
		//创建SPAN,并把图片放置进去
		HTMLElement span = (HTMLElement)pageElement.getOwnerDocument().createElement("span");
		span.setAttribute("style", "display:inline-block; position:relative;");
		pageElement.getParentNode().insertBefore(span, pageElement);
		span.appendChild(pageElement);
		
		//创建播放图标
		HTMLElement playSpan = (HTMLElement)pageElement.getOwnerDocument().createElement("span");
		span.appendChild(playSpan);
		if(writeLink) {
			playSpan.setAttribute("onclick", "EventUtils.clickElement(this.parentNode.getElementsByTagName('a')[0])");
		}
		playSpan.setAttribute("style", "display:inline-block; position:absolute; width:" + playIconWidth + "px; height:" + playIconHeight + "px;");
		if(!playIconURL.endsWith(".png")) { //不是PNG
			htmlParser.setStyle(playSpan, "background-image", "url(" + playIconURL + ")");
		}
		else {
			htmlParser.setStyle(playSpan, "background-image", "url(" + playIconURL + ")");
			htmlParser.setStyle(playSpan, "_background-image", "none");
			htmlParser.setStyle(playSpan, "filter", "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + playIconURL + "', sizingMethod='crop')"); //IE6
		}
		//设置播放图标位置
		if(playIconPosition.indexOf("Right")!=-1) { //水平方向居右
			htmlParser.setStyle(playSpan, "right", playIconXMargin + "px");
		}
		else if(playIconPosition.indexOf("Left")!=-1) { //水平方向居左
			htmlParser.setStyle(playSpan, "left", playIconXMargin + "px");
		}
		else { //水平方向居中
			htmlParser.setStyle(playSpan, "left", (videoWidth - playIconWidth) / 2 + "px");
		}
		if(playIconPosition.indexOf("Bottom")!=-1) { //垂直方向底部
			htmlParser.setStyle(playSpan, "bottom", playIconYMargin + "px");
		}
		else if(playIconPosition.indexOf("Top")!=-1) { //垂直方向顶部
			htmlParser.setStyle(playSpan, "top", playIconYMargin + "px");
		}
		else { //垂直方向居中
			htmlParser.setStyle(playSpan, "top", (videoHeight - playIconHeight) / 2 + "px");
		}
	}
	
	/**
	 * 设置链接
	 * @param pageElement
	 * @param sitePage
	 */
	private void writeLink(Object record, Field field, Object fieldValue, String fieldUrl, HTMLElement pageElement, final WebDirectory webDirectory, final WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, HttpServletRequest request, int pageMode) {
		String url = fieldUrl==null ? (String)sitePage.getAttribute("recordUrl") : fieldUrl;
		if(url==null) {
			return;
		}
		HTMLAnchorElement a = (HTMLAnchorElement)pageElement;
		if(a.getAttribute("style")==null && a.getAttribute("class")==null) {
			a.setAttribute("style", "float:none");
		}
		//输出链接
		LinkUtils.writeLink(a, url, (String)sitePage.getAttribute("linkOpenMode"), webDirectory.getId(), parentSite.getId(), record, true, false, sitePage, request);
	}
}