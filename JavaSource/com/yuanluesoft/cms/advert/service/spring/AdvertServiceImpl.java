package com.yuanluesoft.cms.advert.service.spring;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.advert.model.FixedAdvert;
import com.yuanluesoft.cms.advert.model.FloatAdvert;
import com.yuanluesoft.cms.advert.pojo.Advert;
import com.yuanluesoft.cms.advert.pojo.AdvertCustomer;
import com.yuanluesoft.cms.advert.pojo.AdvertPut;
import com.yuanluesoft.cms.advert.pojo.AdvertPutPage;
import com.yuanluesoft.cms.advert.pojo.AdvertSpace;
import com.yuanluesoft.cms.advert.service.AdvertService;
import com.yuanluesoft.cms.pagebuilder.PageBuilder;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class AdvertServiceImpl extends BusinessServiceImpl implements AdvertService {
	private SiteService siteService; //站点服务
	private HTMLParser htmlParser; //HTML解析器
	private PageBuilder pageBuilder; //页面生成器
	private Cache advertCache; //广告缓存
	private ExchangeClient exchangeClient; //数据交换

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record!=null && (record instanceof AdvertPutPage)) { //广告投放页面
			AdvertPutPage advertPutPage = (AdvertPutPage)record;
			if(advertPutPage.getPageNames()!=null && advertPutPage.getPageNames().startsWith(",") && advertPutPage.getPageNames().endsWith(",")) {
				advertPutPage.setPageNames(advertPutPage.getPageNames().substring(1, advertPutPage.getPageNames().length()-1));
			}
			if(advertPutPage.getSiteIds()!=null && advertPutPage.getSiteIds().startsWith(",") && advertPutPage.getSiteIds().endsWith(",")) {
				advertPutPage.setSiteIds(advertPutPage.getSiteIds().substring(1, advertPutPage.getSiteIds().length()-1));
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		synchronized(this) {
			if(record instanceof AdvertPut) { //广告投放
				AdvertPut advertPut = (AdvertPut)record;
				//获取广告
				Advert advert = (Advert)load(Advert.class, advertPut.getAdvertId());
				//获取当前投放的广告
				AdvertPut currentAdvertPut = getCurrentAdvertPut(advert.getSpaceId());
				if(currentAdvertPut!=null) {
					Timestamp endTime = currentAdvertPut.getFactEndTime()==null ? currentAdvertPut.getEndTime() : currentAdvertPut.getFactEndTime();
					if(endTime==null || endTime.after(advertPut.getBeginTime())) {
						throw new ValidateException("广告[" + currentAdvertPut.getAdvertName() + "]投放尚未结束");
					}
				}
				//检查账户余额
				if(advertPut.getPrice()>0) {
					AdvertCustomer advertCustomer = (AdvertCustomer)load(AdvertCustomer.class, advert.getCreatorId());
					if(advertCustomer==null) {
						throw new ValidateException("广告客户已经删除");
					}
					if(advertCustomer.getAccount()<advertPut.getPrice()) {
						throw new ValidateException("账户余额" + new DecimalFormat("#.##").format(advertCustomer.getAccount()) + "元,不足以支付");
					}
					advertCustomer.setAccount(advertCustomer.getAccount()-advertPut.getPrice());
					getDatabaseService().updateRecord(advertCustomer);
				}
			}
			if(record instanceof AdvertPutPage) { //广告投放页面
				AdvertPutPage advertPutPage = (AdvertPutPage)record;
				resetAdvertPutPage(advertPutPage);
			}
			super.save(record);
			clearAdvertCache(record); //清理广告缓存
			exchangeClient.synchUpdate(record, null, 2000); //数据交换
			return record;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		synchronized(this) {
			if(record instanceof AdvertPutPage) { //广告投放页面
				AdvertPutPage advertPutPage = (AdvertPutPage)record;
				resetAdvertPutPage(advertPutPage);
			}
			super.update(record);
			clearAdvertCache(record); //清理广告缓存
			exchangeClient.synchUpdate(record, null, 2000); //数据交换
			return record;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		clearAdvertCache(record); //清理广告缓存
		exchangeClient.synchDelete(record, null, 2000); //数据交换
	}
	
	/**
	 * 根据记录类型,清理广告缓存
	 * @param record
	 */
	private void clearAdvertCache(Record record) {
		if(record==null) {
			return;
		}
		if((record instanceof Advert) || (record instanceof AdvertPut) || (record instanceof AdvertPutPage) || (record instanceof AdvertSpace)) {
			try {
				advertCache.clear();
			}
			catch(CacheException e) {
				
			}
		}
	}

	/**
	 * 重置广告投放页面
	 * @param advertPutPage
	 */
	private void resetAdvertPutPage(AdvertPutPage advertPutPage) {
		if(advertPutPage.getPageNames()!=null && !advertPutPage.getPageNames().isEmpty() && !advertPutPage.getPageNames().startsWith(",")) {
			advertPutPage.setPageNames("," + advertPutPage.getPageNames() + ",");
		}
		if(advertPutPage.getSiteIds()!=null && !advertPutPage.getSiteIds().isEmpty() && !advertPutPage.getSiteIds().startsWith(",")) {
			advertPutPage.setSiteIds("," + advertPutPage.getSiteIds() + ",");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.advert.service.AdvertService#getCurrentAdvertPut(long)
	 */
	public AdvertPut getCurrentAdvertPut(long spaceId) throws ServiceException {
		String hql = "select AdvertPut" +
					 " from AdvertPut AdvertPut, Advert Advert" +
					 " where AdvertPut.advertId=Advert.id" +
					 " and Advert.spaceId=" + spaceId +
					 " order by AdvertPut.beginTime DESC";
		AdvertPut advertPut = (AdvertPut)getDatabaseService().findRecordByHql(hql);
		if(advertPut==null) {
			return null;
		}
		Timestamp endTime = advertPut.getFactEndTime()==null ? advertPut.getEndTime() : advertPut.getFactEndTime();
		if(endTime!=null && !endTime.after(DateTimeUtils.now())) {
			return null;
		}
		return advertPut;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.advert.service.AdvertService#loadFloatAdvertsAsJs(long, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void loadFloatAdvertsAsJs(long siteId, String applicationName, String pageName, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		response.setContentType("text/javascript");
    	response.setCharacterEncoding("utf-8");
    	PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write("\r\n");
		}
		catch(IOException e) {
			
		}
    	List pageAdverts = loadPageAdverts(siteId, applicationName, pageName, request); //获取页面广告列表
		if(pageAdverts==null || pageAdverts.isEmpty()) {
    		return;
    	}
    	//输出广告
    	try {
    		for(Iterator iterator = pageAdverts.iterator(); iterator.hasNext();) {
	    		FloatAdvert pageAdvert = (FloatAdvert)iterator.next();
	    		writeAdvertContent(pageAdvert.getContent(), "html", siteId, writer, request); //输出广告内容
	    		writeAdvertContent(pageAdvert.getMinimizeContent(), "minimizeHtml", siteId, writer, request); //输出最小化时广告内容
	    		String[] putModes = {pageAdvert.getMode()};
	    		if(pageAdvert.getMode().endsWith("Balance")) { //左右对称时,自动拆分成两个广告
	    			if("windowTopBalance".equals(pageAdvert.getMode())) {
	    				putModes = new String[]{"windowLeftTop", "windowRightTop"};
	    			}
	    			else if("windowBottomBalance".equals(pageAdvert.getMode())) {
	    				putModes = new String[]{"windowLeftBottom", "windowRightBottom"};
	    			}
	    			else if("pageTopBalance".equals(pageAdvert.getMode())) {
	    				putModes = new String[]{"pageLeftTop", "pageRightTop"};
	    			}
	    			else if("pageBottomBalance".equals(pageAdvert.getMode())) {
	    				putModes = new String[]{"pageLeftBottom", "pageRightBottom"};
	    			}
	    		}
	    		for(int i=0; i<putModes.length; i++) {
	    			String js = "Advert.adverts[Advert.adverts.length]=" +
	    						"new Advert(" +
	    						"'" + pageAdvert.getAdvertPutId() + "'," +
	    						"'" + putModes[i] + "'," +
	    						"'" + pageAdvert.getLoadMode() + "'," +
	    						"'" + pageAdvert.getHideMode() + "'," +
	    						"" + pageAdvert.getDisplaySeconds() + "," +
	    						"html," +
	    						"minimizeHtml," +
	    						pageAdvert.getX() + "," +
	    						pageAdvert.getY() + "," +
	    						pageAdvert.getSpeed() + "," +
	    						"'" + pageAdvert.getWidth() + "'," +
	    						"'" + pageAdvert.getHeight() + "'," +
	    						"'" + pageAdvert.getMinimizeWidth() + "'," +
	    						"'" + pageAdvert.getMinimizeHeight() + "'," +
	    						"'" + (pageAdvert.getHref()==null || pageAdvert.getHref().isEmpty() ? "" : Environment.getContextPath() + "/cms/advert/advert.shtml?advertPutId=" + pageAdvert.getAdvertPutId()) + "'" +
	    						");\r\n";
	    			writer.write(js);
		    	}
	    	}
    	}
    	catch(Exception e) {
    		Logger.exception(e);
    		throw new ServiceException();
    	}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.advert.service.AdvertService#loadFixedAdvert(long)
	 */
	public FixedAdvert loadFixedAdvert(long advertSpaceId) throws ServiceException {
    	//从缓存中获取广告
		FixedAdvert fixedAdvert = null;
		try {
			fixedAdvert = (FixedAdvert)advertCache.get(new Long(advertSpaceId));
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
    	if(fixedAdvert!=null) {
    		return fixedAdvert;
    	}
    	fixedAdvert = new FixedAdvert();
		//获取当前投放的广告
		AdvertPut currentAdvertPut = getCurrentAdvertPut(advertSpaceId);
		if(currentAdvertPut!=null) { //有投放
			//获取广告
			Advert advert = (Advert)load(Advert.class, currentAdvertPut.getAdvertId());
			fixedAdvert.setAdvertPutId(currentAdvertPut.getId()); //投放ID
	    	fixedAdvert.setContent(advert.getContent()); //广告内容HTML
    		fixedAdvert.setMinimizeContent(advert.getMinimizeContent()); //最小化时HTML
    		fixedAdvert.setWidth(advert.getWidth()); //宽度
    		fixedAdvert.setHeight(advert.getHeight()); //高度
    		fixedAdvert.setMinimizeWidth(advert.getMinimizeWidth()); //最小化时宽度
    		fixedAdvert.setMinimizeHeight(advert.getMinimizeHeight()); //最小化时高度
    		fixedAdvert.setHref(advert.getHref()); //链接地址
		}
		else { //没有投放
			//获取无广告时显示内容
			AdvertSpace advertSpace = (AdvertSpace)load(AdvertSpace.class, advertSpaceId);
			if(advertSpace.getFreeContent()!=null && !advertSpace.getFreeContent().isEmpty()) { //没有定义无广告时显示内容
				fixedAdvert.setContent(advertSpace.getFreeContent()); //广告内容
				fixedAdvert.setWidth(advertSpace.getWidth()); //宽度
	    		fixedAdvert.setHeight(advertSpace.getHeight()); //高度
			}
		}
		
    	//写入缓存
    	try {
			advertCache.put(new Long(advertSpaceId), fixedAdvert);
		} 
		catch(CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		return fixedAdvert;
	}

	/**
	 * 获取页面广告列表
	 * @param siteId
	 * @param applicationName
	 * @param pageName
	 * @param response
	 * @return
	 * @throws ServiceException
	 */
	private List loadPageAdverts(long siteId, String applicationName, String pageName, HttpServletRequest request) throws ServiceException {
    	RequestInfo requestInfo = RequestUtils.getRequestInfo(request);
    	String clientType = requestInfo.isWechatRequest() ? "wechat" : ("computer".equals(requestInfo.getTerminalType()) ? "computer" : "phone");
    	//从缓存中获取广告
    	String cacheKey = applicationName + "_" + pageName + "_" + siteId + "_" + clientType;
		List pageAdverts = null;
		try {
			pageAdverts = (List)advertCache.get(cacheKey);
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
    	if(pageAdverts!=null) {
    		return pageAdverts;
    	}
    	String hqlFilterColumn =  null; //过滤父栏目
    	String hqlFilterSite =  null; //过滤父站点
    	boolean firstSite = true;
    	List parentDirectories = siteService.listParentDirectories(siteId, null);
    	for(int i=parentDirectories==null ? -1 : parentDirectories.size()-1; i>=0; i--) {
    		WebDirectory webDirectory = (WebDirectory)parentDirectories.get(i);
    		boolean isSite = webDirectory.getDirectoryType().equals("site");
    		if(!isSite || firstSite) { //不是站点
    			hqlFilterColumn = (hqlFilterColumn==null ? "" : hqlFilterColumn + " or ") + "AdvertPutPage.siteIds like '%," + webDirectory.getId() + ",%'";
    		}
    		else {
    			hqlFilterSite = (hqlFilterSite==null ? "" : hqlFilterSite + " or ") + "AdvertPutPage.siteIds like '%," + webDirectory.getId() + ",%'";
    		}
    		if(isSite) {
    			firstSite = false;
    		}
    	}
    	//获取当前页面投放的浮动广告列表
    	String hql = "from AdvertPutPage AdvertPutPage" +
    				 " where AdvertPutPage.pageNames like '%," + JdbcUtils.resetQuot(applicationName) + "__" + JdbcUtils.resetQuot(pageName) + ",%'" + //页面名称
    				 " and AdvertPutPage.clientTypes like '%" + clientType + "%'" + //客户端类型
    				 " and (AdvertPutPage.siteIds like '%," + siteId + ",%'"; //站点/栏目ID
    	if(hqlFilterColumn!=null) {
    		hql += " or (AdvertPutPage.containChildSite>0 and (" + hqlFilterColumn + "))"; //0/不含,1/子站和子栏目,2/仅子栏目
    		if(hqlFilterSite!=null) {
    			hql += " or (AdvertPutPage.containChildSite=1 and (" + hqlFilterSite + "))"; //0/不含,1/子站和子栏目,2/仅子栏目
    		}
    	}
    	hql += ")";
    	pageAdverts = new ArrayList();
    	List advertPutPages = getDatabaseService().findRecordsByHql(hql);
		//构造广告列表
    	for(Iterator iterator = advertPutPages==null ? null : advertPutPages.iterator(); iterator!=null && iterator.hasNext();) {
    		AdvertPutPage advertPutPage = (AdvertPutPage)iterator.next();
    		FloatAdvert pageAdvert = new FloatAdvert();
    		//获取当前投放的广告
    		AdvertPut currentAdvertPut = getCurrentAdvertPut(advertPutPage.getSpaceId());
    		if(currentAdvertPut!=null) { //有投放
    			//获取广告
    			Advert advert = (Advert)load(Advert.class, currentAdvertPut.getAdvertId());
    			pageAdvert.setAdvertPutId(currentAdvertPut.getId()); //投放ID
    	    	pageAdvert.setContent(advert.getContent()); //广告内容HTML
	    		pageAdvert.setMinimizeContent(advert.getMinimizeContent()); //最小化时HTML
	    		pageAdvert.setWidth(advert.getWidth()); //宽度
	    		pageAdvert.setHeight(advert.getHeight()); //高度
	    		pageAdvert.setMinimizeWidth(advert.getMinimizeWidth()); //最小化时宽度
	    		pageAdvert.setMinimizeHeight(advert.getMinimizeHeight()); //最小化时高度
	    		pageAdvert.setHref(advert.getHref()); //链接地址
    		}
    		else { //没有投放
    			//获取无广告时显示内容
    			AdvertSpace advertSpace = (AdvertSpace)load(AdvertSpace.class, advertPutPage.getSpaceId());
    			if(advertSpace.getFreeContent()==null || advertSpace.getFreeContent().isEmpty()) { //没有定义无广告时显示内容
    				continue;
    			}
    			pageAdvert.setContent(advertSpace.getFreeContent()); //广告内容
    			//pageAdvert.setMinimizeContent(null); //最小化时HTML
	    		pageAdvert.setWidth(advertSpace.getWidth()); //宽度
	    		pageAdvert.setHeight(advertSpace.getHeight()); //高度
	    		//pageAdvert.setMinimizeWidth(null); //最小化时宽度
	    		//pageAdvert.setMinimizeHeight(null); //最小化时高度
    		}
    		pageAdvert.setMode(advertPutPage.getMode()); //投放方式,static/绝对位置,popup/弹出窗口,固定在窗口指定位置(absoluteLeft/左,absoluteRight/右,absoluteTop/上,absoluteBottom/下,absoluteLeftTop/左上,absoluteRightTop/右上,absoluteLeftBottom/左下,absoluteRightBottom/右下),fly/全屏滚动
    		pageAdvert.setX(advertPutPage.getX()); //水平边距,可以是绝对坐标和相对坐标(按窗口大小)
    		pageAdvert.setY(advertPutPage.getY()); //垂直边距,可以是绝对坐标和相对坐标(按窗口大小)
    		pageAdvert.setSpeed(advertPutPage.getSpeed()); //移动速度,像数/秒
    		pageAdvert.setDisplaySeconds(advertPutPage.getDisplaySeconds()); //显示时长,以秒为单位
    		pageAdvert.setLoadMode(",fly,pageTop,".indexOf("," + advertPutPage.getMode() + ",")!=-1 ? 0 : advertPutPage.getLoadMode()); //加载方式
    		pageAdvert.setHideMode("fly".equals(advertPutPage.getMode()) ? 0 : advertPutPage.getHideMode()); //隐藏方式
    		pageAdverts.add(pageAdvert);
    	}
    	//写入缓存
    	try {
			advertCache.put(cacheKey, pageAdverts);
		} 
		catch(CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		return pageAdverts;
	}
	
	/**
	 * 输出广告内容
	 * @param advertContent
	 * @param propertyName
	 * @param siteId
	 * @param writer
	 * @param request
	 * @throws ServiceException
	 * @throws IOException
	 */
	private void writeAdvertContent(String advertContent, String propertyName, long siteId, PrintWriter writer, HttpServletRequest request) throws Exception {
		writer.write("var " + propertyName + " = '';\r\n");
		if(advertContent==null || advertContent.isEmpty()) {
			return;
		}
		//解析广告内容
		HTMLDocument document = htmlParser.parseHTMLString(advertContent, "utf-8");
		document = pageBuilder.buildHTMLDocument(document, siteId, new SitePage(), request, false, false, false, false);
		String html = htmlParser.getBodyHTML(document, "utf-8", false);
		int beginIndex = 0;
    	int endIndex;
    	while((endIndex = html.indexOf('\n', beginIndex))!=-1) {
    		writer.write(propertyName + " += '" + Encoder.getInstance().utf8JsEncode(html.substring(beginIndex, endIndex)).replaceAll("SCRIPT", "SCR' + 'IPT") + "';\r\n");
    		beginIndex = endIndex + 1;
    	}
    	writer.write(propertyName + " += '" + Encoder.getInstance().utf8JsEncode(html.substring(beginIndex)).replaceAll("SCRIPT", "SCR' + 'IPT") + "';\r\n");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.advert.service.AdvertService#openAdvert(long)
	 */
	public String openAdvert(long advertPutId) throws ServiceException {
		AdvertPut advertPut = (AdvertPut)load(AdvertPut.class, advertPutId);
		synchronized(this) {
			advertPut.setAccessTimes(advertPut.getAccessTimes() + 1);
			getDatabaseService().updateRecord(advertPut);
		}
		String hql = "select Advert.href" +
					 " from Advert Advert" +
					 " where Advert.id=" + advertPut.getAdvertId();
		return (String)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.advert.service.AdvertService#listAdvertSpaces(long, boolean, boolean)
	 */
	public List listAdvertSpaces(long siteId, boolean floatOnly, boolean fixedOnly) throws ServiceException {
		String hql = "from AdvertSpace AdvertSpace" +
					 " where AdvertSpace.siteId=" + siteId +
					 (floatOnly ? " and AdvertSpace.isFloat=1" : "") +
					 (fixedOnly ? " and AdvertSpace.isFloat!=1" : "") +
					 " order by AdvertSpace.name";
		return getDatabaseService().findRecordsByHql(hql);
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
	 * @return the advertCache
	 */
	public Cache getAdvertCache() {
		return advertCache;
	}

	/**
	 * @param advertCache the advertCache to set
	 */
	public void setAdvertCache(Cache advertCache) {
		this.advertCache = advertCache;
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