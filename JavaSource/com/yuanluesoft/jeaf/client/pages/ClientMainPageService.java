package com.yuanluesoft.jeaf.client.pages;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.client.ClientHeader;
import com.yuanluesoft.jeaf.database.DatabaseDefineService;
import com.yuanluesoft.jeaf.database.model.Table;
import com.yuanluesoft.jeaf.database.model.TableColumn;
import com.yuanluesoft.jeaf.database.model.TableIndex;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.HTMLTraversalCallback;
import com.yuanluesoft.jeaf.htmlparser.model.StyleDefine;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ClientMainPageService extends ClientPageService {
	private HTMLParser htmlParser; //HTML解析器
	private SiteService siteService; //站点服务
	private DatabaseDefineService databaseDefineService; //数据库定义服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, long, int, int, boolean, boolean, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		HTMLDocument template = super.getTemplate(applicationName, pageName, sitePage, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request, editMode);
		//检查是否有启动页面模板
		/*if(super.getTemplate(applicationName, "startup", sitePage, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request, editMode)!=null) {
			sitePage.setAttribute("startupPage", "true");
		}*/
		//检查是否有引导页面模板
		if(super.getTemplate(applicationName, "pilot", sitePage, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request, editMode)!=null) {
			sitePage.setAttribute("pilotPage", "true");
		}
		//添加脚本,控制页面尺寸
		String script = "var index = location.search.indexOf('client.densityDpi=');" +
						"var viewport = 'width=device-width,user-scalable=no';" +
						"if(index!=-1) {" +
						"	index += 'client.densityDpi='.length;" +
						"	var indexEnd = location.search.indexOf('&', index);" +
						"	viewport += ',target-densitydpi=' + location.search.substring(index, indexEnd==-1 ? location.search.length : indexEnd);" +
						"}" +
						"else {" +
						"	index = location.search.indexOf('client.width=') + 'client.width='.length;" +
						"	var indexEnd = location.search.indexOf('&', index);" +
						"	var width = Number(location.search.substring(index, indexEnd==-1 ? location.search.length : indexEnd));" +
						"	var scale = Math.round(screen.width / width * 1000) / 1000;" +
						"	viewport += ',initial-scale=' + scale + ', minimum-scale=' + scale + ', maximum-scale=' + scale;" +
						"}" +
						"document.write('<meta name=\"viewport\" id=\"viewport\" content=\"' + viewport + '\">');";
		htmlParser.appendScript(template, script);
		return template;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, final SitePage sitePage, final long siteId, final HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//处理样式列表
		String styleDefineMeta = htmlParser.getMeta(template, "styleDefineMeta");
		if(styleDefineMeta!=null) {
			htmlParser.removeMeta(template, "styleDefineMeta");
			List styleDefines = htmlParser.parseStyleDefines(styleDefineMeta);
			int index = 0;
			for(Iterator iterator = styleDefines==null ? null : styleDefines.iterator(); iterator!=null && iterator.hasNext();) {
				StyleDefine styleDefine = (StyleDefine)iterator.next();
				String cssPath = RequestUtils.getFilePath(styleDefine.getCssUrl());
				File file;
				if(cssPath==null || !(file=new File(cssPath)).exists()) {
					continue;
				}
				try {
					response.addHeader(ClientHeader.CLIENT_HEADER_STYLE_NAME + "-" + index, URLEncoder.encode(styleDefine.getStyleName(), "utf-8"));
				}
				catch (UnsupportedEncodingException e) {
				
				}
				response.addHeader(ClientHeader.CLIENT_HEADER_STYLE_URL + "-" + index, styleDefine.getCssUrl() + "?client.modified=" + file.lastModified() + "&client.fileSize=" + file.length());
				index++;
			}
		}
		if("true".equals(sitePage.getAttribute("startupPage"))) { //有启动页面
			addStartupPage(template, siteId, request);
		}
		
		//插入JS调用
		String js = "/jeaf/common/js/scroller.js,updater.js,channel.js,column.js,preference.js,client.js,progress.js,link.js,form.js,form.picker.js,dialog.js";
		//解析设备调用META
		String deviceMeta = htmlParser.getMeta(template, "deviceMeta");
		boolean databaseSupport = "true".equals(StringUtils.getPropertyValue(deviceMeta, "database"));
		boolean fileSystemSupport = "true".equals(StringUtils.getPropertyValue(deviceMeta, "fileSystem"));
		if(databaseSupport) { //需要访问数据库
			js += ",database.js";
		}
		if(fileSystemSupport) { //需要访问文件
			js += ",fileSystem.js";
		}
		if("true".equals(StringUtils.getPropertyValue(deviceMeta, "camera"))) { //需要访问数据库
			js += ",camera.js";
		}
		htmlParser.removeMeta(template, "deviceMeta");
		String[] files = js.split(",");
		for(int i=0; i<files.length; i++) {
			HTMLScriptElement script = (HTMLScriptElement)template.createElement("script");
			script.setLang("javascript");
			script.setSrc(request.getContextPath() + (files[i].startsWith("/") ? "" : "/jeaf/client/js/") + files[i]);
			template.getBody().appendChild(script);
		}
		
		final StringBuffer script = new StringBuffer();
		
		//处理频道栏,<div id=channelBar title=频道栏 name="channelBar" urn="displayMode=left2right&amp;overflowX=false&amp;overflowY=true&amp;currentChannelStyle=current_channel">
		HTMLElement channelBar = (HTMLElement)template.getElementById("channelBar");
		if(channelBar!=null) {
			processChannelBar(channelBar, script, siteId, request);
		}
		
		//处理栏目栏,<DIV id=columnBar title=栏目栏 urn="displayMode=autoHide&amp;displaySeconds=20&amp;currentColumnStyle=1&amp;columnStyle=2&amp;moreColumnsOnLeftStyle=3&amp;moreColumnsOnRightStyle=4"></DIV>
		HTMLElement columnBar = (HTMLElement)template.getElementById("columnBar");
		if(columnBar!=null) {
			String urn = columnBar.getAttribute("urn");
			columnBar.removeAttribute("urn");
			columnBar.removeAttribute("title");
			String displayMode = StringUtils.getPropertyValue(urn, "displayMode"); //显示方式,autoHide/alwaysDisplay
			int displaySeconds = StringUtils.getPropertyIntValue(urn, "displaySeconds", 8); //自动隐藏时,显示的秒数
			String currentColumnStyle = StringUtils.getPropertyValue(urn, "currentColumnStyle"); //当前栏目样式
			String columnStyle = StringUtils.getPropertyValue(urn, "columnStyle"); //栏目样式
			String moreColumnsOnLeftStyle = StringUtils.getPropertyValue(urn, "moreColumnsOnLeftStyle"); //左侧有栏目提示样式
			String moreColumnsOnRightStyle = StringUtils.getPropertyValue(urn, "moreColumnsOnRightStyle"); //右侧有栏目提示样式
			script.append("var columnBar = new ColumnBar(document.getElementById('columnBar'), '" + displayMode + "', " + displaySeconds + ", '" + currentColumnStyle + "', '" + columnStyle + "', '" + moreColumnsOnLeftStyle + "', '" + moreColumnsOnRightStyle + "');");
		}
		//处理栏目容器
		HTMLElement columnContainer = (HTMLElement)template.getElementById("columnContainer");
		if(columnContainer!=null) {
			columnContainer.removeAttribute("title");
		}
		
		//处理参数设置栏
		HTMLElement preferenceBar = (HTMLElement)template.getElementById("preferenceBar");
		if(preferenceBar!=null) {
			//创建参数设置栏对象脚本
			String urn = preferenceBar.getAttribute("urn");
			preferenceBar.removeAttribute("urn");
			preferenceBar.removeAttribute("title");
			String displayMode = StringUtils.getPropertyValue(urn, "displayMode"); //显示方式,alwaysDisplay/总是显示,left2right/从左向右滑动时显示,right2left/从右向左滑动时显示,top2bottom/从上向下滑动时显示,bottom2top/从下向上滑动时显示,leftBorder2right/从左边框向右滑动时显示,rightBorder2left/从右边框向左滑动时显示,topBorder2bottom/从上边框向下滑动时显示,bottomBorder2Top/从下边框向上滑动时显示
			if(!"alwaysDisplay".equals(displayMode)) {
				htmlParser.setStyle(preferenceBar, "display", "none");
			}
			script.append("var preferenceBar = new PreferenceBar(document.getElementById('preferenceBar')," +
																 "'" + displayMode + "'," + //显示方式,alwaysDisplay/总是显示,left2right/从左向右滑动时显示,right2left/从右向左滑动时显示,top2bottom/从上向下滑动时显示,bottom2top/从下向上滑动时显示,leftBorder2right/从左边框向右滑动时显示,rightBorder2left/从右边框向左滑动时显示,topBorder2bottom/从上边框向下滑动时显示,bottomBorder2Top/从下边框向上滑动时显示
																 "true".equals(StringUtils.getPropertyValue(urn, "overflowX")) + "," + //是否显示水平滚动条
																 "true".equals(StringUtils.getPropertyValue(urn, "overflowY")) + "," + //是否显示垂直滚动条
																 StringUtils.getPropertyIntValue(urn, "marginTop", 0) + "," + //顶部边距
																 StringUtils.getPropertyIntValue(urn, "marginBottom", 0) + ");"); //底部边距
		}
		
		//处理频道图标,<a id="channelIcon" urn="iconWidth=50&amp;iconHeight=20">&lt;频道图标&gt;</a>
		HTMLElement channelIcon = (HTMLElement)template.getElementById("channelIcon");
		int channelIconWidth = 0;
		int channelIconHeight = 0;
		if(channelIcon!=null) {
			String urn = channelIcon.getAttribute("urn");
			channelIconWidth = StringUtils.getPropertyIntValue(urn, "iconWidth", 0);
			channelIconHeight = StringUtils.getPropertyIntValue(urn, "iconHeight", 0);
			HTMLElement channelIconSpan = (HTMLElement)template.createElement("span");
			channelIconSpan.setId("channelIcon");
			channelIcon.getParentNode().replaceChild(channelIconSpan, channelIcon);
		}
		
		//处理频道名称,<a id="channelName">&lt;频道名称&gt;</a>
		HTMLElement channelName = (HTMLElement)template.getElementById("channelName");
		if(channelName!=null) {
			HTMLElement channelNameSpan = (HTMLElement)template.createElement("span");
			channelNameSpan.setId("channelName");
			channelName.getParentNode().replaceChild(channelNameSpan, channelName);
		}
		
		//处理栏目图标,<a id="columnIcon" urn="iconWidth=30&amp;iconHeight=20">&lt;栏目图标&gt;</a>
		HTMLElement columnIcon = (HTMLElement)template.getElementById("columnIcon");
		int columnIconWidth = 0;
		int columnIconHeight = 0;
		if(columnIcon!=null) {
			String urn = columnIcon.getAttribute("urn");
			columnIconWidth = StringUtils.getPropertyIntValue(urn, "iconWidth", 0);
			columnIconHeight = StringUtils.getPropertyIntValue(urn, "iconHeight", 0);
			HTMLElement columnIconSpan = (HTMLElement)template.createElement("span");
			columnIconSpan.setId("columnIcon");
			columnIcon.getParentNode().replaceChild(columnIconSpan, columnIcon);
		}
		
		//处理栏目名称,<a id="columnName">&lt;栏目名称&gt;</a>
		HTMLElement columnName = (HTMLElement)template.getElementById("columnName");
		if(columnName!=null) {
			HTMLElement columnNameSpan = (HTMLElement)template.createElement("span");
			columnNameSpan.setId("columnName");
			columnName.getParentNode().replaceChild(columnNameSpan, columnName);
		}
		
		//处理显示频道栏按钮, <img id="showChannelBarButton" title="显示频道栏"/>
		HTMLElement showChannelBarButton = (HTMLElement)template.getElementById("showChannelBarButton");
		if(showChannelBarButton!=null) {
			showChannelBarButton.removeAttribute("title");
		}
		
		//处理显示栏目栏按钮, <img id="showColumnBarButton" title="显示栏目栏"/>
		HTMLElement showColumnBarButton = (HTMLElement)template.getElementById("showColumnBarButton");
		if(showColumnBarButton!=null) {
			showColumnBarButton.removeAttribute("title");
		}
		
		//处理提示信息框, <div id="clientToast" class="clientToast" title="提示信息框">提示信息</div>
		HTMLElement clientToast = (HTMLElement)template.getElementById("clientToast");
		if(clientToast!=null) {
			clientToast.removeAttribute("title");
			String style = clientToast.getAttribute("style");
			clientToast.setAttribute("style", (style==null || style.isEmpty() ? "" : style + ";") + "display:none"); //隐藏
		}
		
		//处理对话框:clientDialog
		HTMLElement clientDialog = (HTMLElement)template.getElementById("clientDialog");
		if(clientDialog!=null) {
			String style = clientDialog.getAttribute("style");
			clientDialog.setAttribute("style", (style==null || style.isEmpty() ? "" : style + ";") + "display:none"); //隐藏
		}
		
		//处理对话框标题:clientDialogTitle
		HTMLElement clientDialogTitle = (HTMLElement)template.getElementById("clientDialogTitle");
		if(clientDialogTitle!=null) {
			HTMLElement clientDialogTitleSpan = (HTMLElement)template.createElement("span");
			clientDialogTitleSpan.setId("clientDialogTitle");
			clientDialogTitle.getParentNode().replaceChild(clientDialogTitleSpan, clientDialogTitle);
		}
		
		//生成加载客户端脚本 Client = function(pilotEnabled, mainPageHintEnabled, recordPageHintEnabled, channelBar, columnBar, preferenceBar, columnContainer, channelIconSpan, channelIconWidth, channelIconHeight, channelNameSpan, columnIconSpan, columnIconWidth, columnIconHeight, columnNameSpan, showChannelBarButton, showColumnBarButton)
		script.append("window.client = new Client(" + "true".equals(sitePage.getAttribute("pilotPage")) + ", " +
												  (channelBar==null ? "null" : "channelBar") + ", " +
												  (columnBar==null ? "null" : "columnBar") + ", " +
												  (preferenceBar==null ? "null" : "preferenceBar") + ", " +
												  "document.getElementById('columnContainer'), " +
												  "document.getElementById('channelIcon'), " + 
												  channelIconWidth + ", " + 
												  channelIconHeight + ", " +
												  "document.getElementById('channelName'), " +
												  "document.getElementById('columnIcon'), " +
												  columnIconWidth + ", " +
												  columnIconHeight + ", " +
												  "document.getElementById('columnName'), " +
												  "document.getElementById('showChannelBarButton'), " +
												  "document.getElementById('showColumnBarButton'));");
		script.append("EventUtils.addEvent(window, 'load', function() {window.client.create();});");
		//添加数据库表列表
		if(databaseSupport) {
			script.append("window.database = new Database();");
			addDatabaseTables(script, StringUtils.getPropertyValue(deviceMeta, "databaseApplicationNames"));
		}
		if(fileSystemSupport) {
			script.append("window.fileSystem = new FileSystem();");
		}
		//创建script元素
		HTMLScriptElement scriptElement = (HTMLScriptElement)template.createElement("script");
		scriptElement.setLang("javascript");
		htmlParser.setTextContent(scriptElement, script.toString());
		template.getBody().appendChild(scriptElement);
	}
	
	/**
	 * 加入启动页面
	 * @param template
	 * @param siteId
	 * @param request
	 */
	private void addStartupPage(HTMLDocument template, long siteId, HttpServletRequest request) {
		String url = request.getContextPath() + "/jeaf/client/startup.shtml" + (siteId>0 ? "?siteId=" + siteId : "");
		HTMLIFrameElement iframe = (HTMLIFrameElement)template.createElement("iframe");
		iframe.setAttribute("allowtransparency", "false");
		iframe.setId("startupFrame");
		iframe.setAttribute("style", "z-index:1000;" +
				   					 "background-color:#000;" +
				   					 "position:absolute;" +
				   					 "border-style:none;" +
				   					 "left:0;" +
				   					 "top:0;" +
				   					 "width:100%;" +
									 "height:100%;");
		iframe.setSrc(url);
		template.getBody().insertBefore(iframe, template.getBody().getFirstChild());
	}
	
	/**
	 * 处理频道栏
	 * @param channelBar
	 * @param script
	 * @param siteId
	 * @param request
	 * @throws ServiceException
	 */
	private void processChannelBar(HTMLElement channelBar, final StringBuffer script, long siteId, final HttpServletRequest request) throws ServiceException {
		//处理频道
		final WebDirectory webDirectory = (WebDirectory)siteService.getDirectory(siteId); //获取栏目
		final WebSite parentSite =  ((webDirectory instanceof WebSite) ? (WebSite)webDirectory : siteService.getParentSite(webDirectory.getId())); //获取站点
		script.append("var channels = new Array();");
		final int[] channelIndex = {0}; 
		htmlParser.traversalChildNodes(channelBar.getChildNodes(), true, new HTMLTraversalCallback() {
			public boolean processNode(Node node) {
				HTMLElement element = (HTMLElement)node;
				if(!"channel".equals(element.getId())) { //不是频道
					return false;
				}
				//<DIV id=channel title=频道:要闻 name="channel" urn="name=要闻&amp;icon=http://localhost/cms/cms/templates/440276954201960000/images/youcaihuasmall.jpg&amp;iconWidth=80&amp;iconHeight=80&amp;columnCount=6&amp;column0=name%3D今日要闻%26icon%3Dhttp://localhost/cms/cms/templates/440276954201960000/images/logo.jpg%26iconWidth%3D51%26iconHeight%3D24%26category%3D新闻%26link%3D<A id%253DsiteLink urn%253DsiteName%253D今日建阳%2526amp;siteId%253D990149411249370000%2526amp;openMode%253D_blank>今日建阳</A>&amp;column1=name%3D专题%26category%3D专题%26link%3D<A id%253DsiteLink urn%253DsiteName%253D专题栏目%2526amp;siteId%253D10654915662000%2526amp;openMode%253D_blank>专题栏目</A>">要闻<BR></DIV>
				element.setId("channel_" + channelIndex[0]);
				String urn = element.getAttribute("urn");
				element.removeAttribute("urn");
				element.removeAttribute("title");
				String channelName = StringUtils.getPropertyValue(urn, "name"); //频道名称
				String channelIcon = StringUtils.getPropertyValue(urn, "icon"); //频道图标
				int channelColumnCount = StringUtils.getPropertyIntValue(urn, "columnCount", 0); //显示的栏目数
				//解析栏目列表
				script.append("var columns = new Array();");
				for(int i=0; ; i++) {
					String columnDefine = StringUtils.getPropertyValue(urn, "column" + i);
					if(columnDefine==null) {
						break;
					}
					//column0=name%3D今日要闻%26icon%3Dhttp://localhost/cms/cms/templates/440276954201960000/images/logo.jpg%26iconWidth%3D51%26iconHeight%3D24%26category%3D新闻%26link%3D<A id%253DsiteLink urn%253DsiteName%253D今日建阳%2526amp;siteId%253D990149411249370000%2526amp;openMode%253D_blank>今日建阳</A>
					String columnName = StringUtils.getPropertyValue(columnDefine, "name"); //栏目名称
					String columnIcon = StringUtils.getPropertyValue(columnDefine, "icon"); //栏目图标
					String columnCategory = StringUtils.getPropertyValue(columnDefine, "category"); //栏目分类
					String columnLink = StringUtils.getPropertyValue(columnDefine, "link"); //栏目链接
					//解析栏目链接
					String columnUrl = null;
					try {
						HTMLDocument linkDocument = htmlParser.parseHTMLString(columnLink, "utf-8");
						RequestInfo requestInfo = RequestUtils.getRequestInfo(request);
						requestInfo.setPageType(RequestInfo.PAGE_TYPE_NORMAL);
						getPageBuilder().processPageElement(linkDocument.getBody(), webDirectory, parentSite, new SitePage(), requestInfo, request);
						columnUrl = ((HTMLAnchorElement)linkDocument.getElementsByTagName("A").item(0)).getHref();
					}
					catch(Exception e) {
						Logger.exception(e);
					}
					script.append("columns[columns.length] = new Column('" + columnName + "', '" + columnCategory + "', '" + columnIcon + "', '" + columnUrl + "');");
				}
				script.append("channels[channels.length] = new Channel(document.getElementById('channel_" + channelIndex[0] + "'), '" + channelName + "', '" + channelIcon + "', " + channelColumnCount + ", columns);");
				channelIndex[0]++;
				return true;
			}
		});
		
		//创建频道栏对象脚本
		String urn = channelBar.getAttribute("urn");
		channelBar.removeAttribute("urn");
		channelBar.removeAttribute("title");
		String displayMode = StringUtils.getPropertyValue(urn, "displayMode"); //显示方式,alwaysDisplay/总是显示,left2right/从左向右滑动时显示,right2left/从右向左滑动时显示,top2bottom/从上向下滑动时显示,bottom2top/从下向上滑动时显示,leftBorder2right/从左边框向右滑动时显示,rightBorder2left/从右边框向左滑动时显示,topBorder2bottom/从上边框向下滑动时显示,bottomBorder2Top/从下边框向上滑动时显示
		if(!"alwaysDisplay".equals(displayMode)) {
			htmlParser.setStyle(channelBar, "display", "none");
		}
		script.append("var channelBar = new ChannelBar(document.getElementById('channelBar')," +
													   "'" + displayMode + "'," + //显示方式,alwaysDisplay/总是显示,left2right/从左向右滑动时显示,right2left/从右向左滑动时显示,top2bottom/从上向下滑动时显示,bottom2top/从下向上滑动时显示,leftBorder2right/从左边框向右滑动时显示,rightBorder2left/从右边框向左滑动时显示,topBorder2bottom/从上边框向下滑动时显示,bottomBorder2Top/从下边框向上滑动时显示
													   "true".equals(StringUtils.getPropertyValue(urn, "overflowX")) + "," + //是否显示水平滚动条
													   "true".equals(StringUtils.getPropertyValue(urn, "overflowY")) + "," + //是否显示垂直滚动条
													   StringUtils.getPropertyIntValue(urn, "marginTop", 0) + "," + //顶部边距
													   StringUtils.getPropertyIntValue(urn, "marginBottom", 0) + "," + //底部边距
													   "'" + StringUtils.getPropertyValue(urn, "currentChannelStyle") + "'," + //当前频道样式
													   "channels);");
	}
	
	/**
	 * 增加数据库表
	 * @param script
	 * @param databaseApplicationNames
	 * @throws ServiceException
	 */
	private void addDatabaseTables(final StringBuffer script, final String databaseApplicationNames) throws ServiceException {
		if(databaseApplicationNames==null || databaseApplicationNames.isEmpty()) {
			return;
		}
		String[] applicationNames = databaseApplicationNames.split(",");
		for(int i=0; i<applicationNames.length; i++) {
			List tables = databaseDefineService.listTables(applicationNames[i]);
			for(Iterator iterator = tables==null ? null : tables.iterator(); iterator!=null && iterator.hasNext();) {
				Table table = (Table)iterator.next();
				script.append("window.database.addDatabaseTableDefine({");
				script.append("		name: '" + table.getTableName() + "',"); //名称
				script.append("		description: '" + table.getDescription() + "',"); //描述
				script.append("		columns: [");
				for(Iterator iteratorColumn = table.getColumns().iterator(); iteratorColumn.hasNext();) {
					TableColumn tableColumn = (TableColumn)iteratorColumn.next();
					script.append("	{");
					script.append("		name: '" + tableColumn.getName() + "',"); //名称
					script.append("		description: '" + tableColumn.getDescription() + "',"); //描述
					script.append("		type: '" + tableColumn.getType() + "'"); //类型,包括:varchar/text/char/number/date/timestamp
					if(tableColumn.getLength()!=null) {
						script.append("	,length: '" + tableColumn.getLength() + "'"); //长度
					}
					if(tableColumn.getReferenceTable()!=null) {
						script.append(" ,referenceTable: '" + tableColumn.getReferenceTable() + "'"); //关联表
					}
					script.append("	}" + (iteratorColumn.hasNext() ? "," : ""));
				}
				script.append("		]");
				if(table.getIndexes()!=null && !table.getIndexes().isEmpty()) {
					script.append("	,indexes: [");
					for(Iterator iteratorIndex = table.getIndexes().iterator(); iteratorIndex.hasNext();) {
						TableIndex tableIndex = (TableIndex)iteratorIndex.next();
						script.append("	{");
						script.append("		name: '" + tableIndex.getIndexName() + "',"); //名称
						script.append("		columns: '" + tableIndex.getIndexColumns() + "'"); //字段列表
						script.append("	}" + (iteratorIndex.hasNext() ? "," : ""));
					}
					script.append("	]");
				}
				script.append("});");
			}
		}
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
	 * @return the databaseDefineService
	 */
	public DatabaseDefineService getDatabaseDefineService() {
		return databaseDefineService;
	}

	/**
	 * @param databaseDefineService the databaseDefineService to set
	 */
	public void setDatabaseDefineService(DatabaseDefineService databaseDefineService) {
		this.databaseDefineService = databaseDefineService;
	}
}