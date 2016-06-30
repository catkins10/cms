package com.yuanluesoft.jeaf.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ClientServlet extends HttpServlet {
	private final int BUFFER_SIZE = 1024 * 256; //256K
	private String webApplicationLocalUrl = null; //本地WEB服务URL
	private Cache requestCodeCache;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		super.init();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		webApplicationLocalUrl = StringUtils.fillLocalHostIP((String)webApplicationContext.getBean("webApplicationLocalUrl"));
		requestCodeCache = (Cache)webApplicationContext.getBean("requestCodeCache");
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURI();
		String authorization = request.getHeader("Authorization");
		authorization = authorization.substring(authorization.indexOf("Basic ") + "Basic ".length());
		long time;
		try {
			time = Long.parseLong(Encoder.getInstance().desDecode(authorization, "19800803", "utf-8", null));
		}
		catch(Exception e) {
			throw new IOException(e);
		}
		if(Math.abs(System.currentTimeMillis()-time) > 30*60*1000) { //服务器和客户端时间差超过30分钟
			throw new IOException("REQUEST_TIME_FAILED");
		}
		try {
			//写入缓存,避免请求被重复调用
			if(requestCodeCache.get(new Long(time))!=null) {
				throw new IOException();
			}
			requestCodeCache.put(new Long(time), new Character('1'));
		}
		catch(Exception e) {
			throw new IOException(e);
		}
		String prefix = request.getContextPath() + "/client/";
		try {
			url = Encoder.getInstance().desDecode(url.substring(url.indexOf(prefix) + prefix.length()), Encoder.getInstance().md5Encode("20050718" + time), "utf-8", null);
		}
		catch(Exception e) {
			throw new IOException(e);
		}
		doService(url, request.getMethod(), request, response, false, 0);
	}

	/**
	 * 处理请求
	 * @param request
	 * @param response
	 * @param retryTimes
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doService(final String url, String method, HttpServletRequest request, HttpServletResponse response, boolean redirectAfterPost, int retryTimes) throws ServletException, IOException {
		HttpURLConnection httpConnection = null;
		OutputStream outputStream = null;
		GZIPOutputStream zipOutputStream = null;
		InputStream httpInputStream = null;
		try {
			if(Logger.isTraceEnabled()) {
				Logger.info("ClientServlet: open local url " + url);
			}
			httpConnection = (HttpURLConnection)new URL(webApplicationLocalUrl + url).openConnection();
			HttpURLConnection.setFollowRedirects(false);
			httpConnection.setRequestMethod(method); //请求方法 
			httpConnection.setDoOutput(true); //允许输出
			httpConnection.setDoInput(true); //允许输入
			httpConnection.setUseCaches(false); //不使用缓存
			httpConnection.setConnectTimeout(30000); //超时时间30s
			httpConnection.setReadTimeout(30000); //超时时间30s
			//设置请求头部
			for(Enumeration headers = request.getHeaderNames(); headers.hasMoreElements();) {
				String headerName = (String)headers.nextElement();
				for(Enumeration values = request.getHeaders(headerName); values.hasMoreElements();) {
					httpConnection.addRequestProperty(headerName, (String)values.nextElement());
				}
			}
			if(redirectAfterPost) {
				httpConnection.addRequestProperty("client.redirectAfterPost", "true");
			}
			//把客户端IP地址加为HTTP头
			httpConnection.addRequestProperty(ClientHeader.CLIENT_HEADER_REMOTE_ADDRESS, request.getRemoteAddr());
			httpConnection.addRequestProperty(ClientHeader.CLIENT_HEADER_REMOTE_PORT, "" + request.getRemotePort());
			httpConnection.connect();
			if(method.equalsIgnoreCase("POST")) { //提交,转发浏览器请求的数据
			    OutputStream httpOutputStream = null;
			    InputStream inputStream = null;
			    try {
				    httpOutputStream = httpConnection.getOutputStream();
				    inputStream = request.getInputStream();
				    String contentLengthHader = request.getHeader("Content-Length");
					int contentLength = (contentLengthHader==null ? -1 : Integer.parseInt(contentLengthHader));
					byte[] buffer = new byte[contentLength==-1 || contentLength>BUFFER_SIZE ? BUFFER_SIZE : contentLength];
					int readLen;
					int readTotal = 0;
					while((readLen=inputStream.read(buffer))!=-1) {
						httpOutputStream.write(buffer, 0, readLen);
						readTotal += readLen;
						if(contentLength!=-1 && readTotal>=contentLength) {
							break;
						}
					}
			    }
			    finally {
			    	try {
			    		httpOutputStream.close();
			    	}
			    	catch(Exception e) {
			    		
			    	}
			    	try {
			    		inputStream.close();
			    	}
			    	catch(Exception e) {
			    		
			    	}
			    }
			}
			if(httpConnection.getResponseCode()<0) {
				if(retryTimes<5 && !method.equalsIgnoreCase("POST")) { //重试5次,且不是提交
					doService(url, method, request, response, redirectAfterPost, retryTimes+1);
				}
				return;
			}
			int responseCode = httpConnection.getResponseCode()<0 ? 500 : httpConnection.getResponseCode();
			if(Logger.isTraceEnabled()) {
				Logger.trace("ClientServlet: response code is " + responseCode);
			}
			if(responseCode==302) { //重定向
				String redirect = (String)((List)httpConnection.getHeaderFields().get("Location")).get(0);
				int index = redirect.indexOf("://");
				if(index==-1 || redirect.toLowerCase().indexOf(request.getServerName().toLowerCase())!=-1) {
					redirect = (index==-1 ? redirect : redirect.substring(redirect.indexOf('/', index + 3))).substring(request.getContextPath().length());
					Map queryParameters = StringUtils.getQueryParameters(url);
					for(Iterator iterator = queryParameters.entrySet().iterator(); iterator.hasNext();) {
						Map.Entry entry = (Map.Entry)iterator.next();
						if(!((String)entry.getKey()).startsWith("client.")) {
							iterator.remove();
						}
					}
					redirect += (redirect.indexOf('?')==-1 ? "?" : "&") + StringUtils.generateQueryString(queryParameters);
					doService(redirect, "GET", request, response, redirectAfterPost || "POST".equalsIgnoreCase(method), 0);
					return;
				}
			}
			response.setStatus(responseCode); //状态
			response.setCharacterEncoding("utf-8"); //字符集
			response.setContentType(httpConnection.getContentType()); //类型
			httpConnection.getDate();
			for(Iterator iterator = httpConnection.getHeaderFields().keySet().iterator(); iterator.hasNext();) {
				String headerName = (String)iterator.next();
				if(headerName==null) {
					continue;
				}
				List values = (List)httpConnection.getHeaderFields().get(headerName);
				if(headerName.equals(ClientHeader.CLIENT_HEADER_CACHE_EXPIRE)) { //缓存有效期,只输出最小的值
					long cacheExpire = Long.MAX_VALUE;
					for(Iterator iteratorValue = values==null ? null : values.iterator(); iteratorValue!=null && iteratorValue.hasNext();) {
						String value = (String)iteratorValue.next();
						cacheExpire = Math.min(cacheExpire, Long.parseLong(value));
					}
					Number templateLastModified = null;
					try {
						Cache templateCache = (Cache)Environment.getService("templateCache");
						templateLastModified = (Number)templateCache.get("templateLastModified");
						if(templateLastModified==null) {
							//获取模板最后更新时间
							DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
							Timestamp lastModified = (Timestamp)databaseService.findRecordByHql("select max(Template.lastModified) from Template Template");
							templateLastModified = new Long(lastModified==null ? 0 : lastModified.getTime());
							templateCache.put("templateLastModified", templateLastModified);
						}
					} 
					catch(Exception e) {
						e.printStackTrace();
					}
					if(templateLastModified!=null && System.currentTimeMillis() - templateLastModified.longValue() < 5 * 3600 * 1000) { //模板在5小时内更新过
						cacheExpire = cacheExpire==0 ? 0 : 1;
					}
					response.setHeader(headerName, "" + cacheExpire);
					continue;
				}
				for(Iterator iteratorValue = values==null ? null : values.iterator(); iteratorValue!=null && iteratorValue.hasNext();) {
					String value = (String)iteratorValue.next();
					if("Content-Length".equalsIgnoreCase(headerName)) {
						response.addHeader("File-Size", value);
					}
					else if(headerName.startsWith("Client-") || headerName.toLowerCase().startsWith("content") || ",set-cookie,location,".indexOf("," + headerName.toLowerCase() + ",")!=-1) {
						response.addHeader(headerName, value);
					}
				}
			}
			response.setHeader("Content-Encoding", "gzip");
			//输出数据
			outputStream = response.getOutputStream();
			if(responseCode>=400) {
				return;
			}
			zipOutputStream = new GZIPOutputStream(outputStream, BUFFER_SIZE);
			httpInputStream = httpConnection.getInputStream();
			//处理接收到的数据,重设图片/样式表/脚本的URL,如果是HTML,则同时检查模板的修改日期,并写入HEADER
			httpInputStream = resetResponseBody(httpInputStream, httpConnection.getContentType(), httpConnection.getURL(), request.getContextPath(), response);
			byte[] buffer = new byte[BUFFER_SIZE];
	    	int len;
	    	while((len=httpInputStream.read(buffer))!=-1) {
	    		zipOutputStream.write(buffer, 0, len);
	    		outputStream.flush();
	    	}
	    }
		catch(FileNotFoundException e) {
			Logger.error("ClientServlet: file "  + e.getMessage() + "  not found.");
		}
 		finally {
 			try {
 				httpInputStream.close();
 			}
 			catch(Exception e) {
 				
 			}
 			try {
 				zipOutputStream.close();
 			}
 			catch(Exception e) {
 				
 			}
 			try {
 				outputStream.close();
 			}
 			catch(Exception e) {
 				
 			}
 			try {
 				httpConnection.disconnect();
 			}
 			catch(Exception e) {
 				
 			}
	    }
	}
	
	/**
	 * 重设图片/样式表/脚本的URL,如果是HTML,则同时检查模板的修改日期,并写入HEADER
	 * @param httpInputStream
	 * @param contentType
	 * @param url
	 * @param contentPath
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private InputStream resetResponseBody(InputStream httpInputStream, String contentType, URL url, String contextPath, HttpServletResponse response) throws ServletException, IOException {
		if(contentType==null) {
			return httpInputStream;
		}
		contentType = contentType.toLowerCase();
		boolean isHtml = false, isCss = false;
		if(!(isHtml=contentType.indexOf("text/html")!=-1) && !(isCss=contentType.indexOf("text/css")!=-1)) {
			return httpInputStream;
		}
		ByteArrayOutputStream textOutputStream = null;
		try {
			//读取HTML/CSS内容
			textOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
	    	int len;
	    	while((len=httpInputStream.read(buffer))!=-1) {
				textOutputStream.write(buffer, 0, len);
	    	}
	    	httpInputStream.close();
	    	String text = textOutputStream.toString("utf-8");
	    	if(isHtml && text.indexOf(".createVideoPlayer")!=-1) { //网页,且页面中包含创建视频播放器脚本
				response.setHeader(ClientHeader.CLIENT_HEADER_CACHE_EXPIRE, "0"); //页面缓存时间设为0,页面不做缓存
			}
		   	//重设图片、css、js路径
			StringBuffer newText = new StringBuffer();
			Pattern pattern;
			if(isCss) { //CSS
				pattern = Pattern.compile("(?i)(url)\\([ \"']?([^\\) \"']*)[ \"']?\\)");
			}
			else { //HTML页面
				pattern = Pattern.compile("(?i)<[^a][^>]*(href|src)=[\"']?([^ \"'>]*)[\"']?[ />]|url\\([ \"']?([^\\) \"']*)[ \"']?\\)|<a[^>]*(href)=[\"']?([^ \"'>]*)[\"']?[ />]");
			}
			Matcher matcher = pattern.matcher(text);
			int index = 0;
			while(matcher.find()) {
				int groupIndex = 0;
				String path;
				if((path=matcher.group(2))!=null) { //<img src="..."
					groupIndex = 2;
				}
				else if((path=matcher.group(3))!=null) { //url(...)
					groupIndex = 3;
				}
				else if((path=matcher.group(5))!=null) { //<a href="..."
					groupIndex = 5;
					newText.append(text.substring(index, matcher.start(4)));
					newText.append("url"); //href替换为url
					index = matcher.end(4);
				}
				newText.append(text.substring(index, matcher.start(groupIndex)));
				path = path.trim();
				String prefix = null;
				if(path.startsWith("&quot;") && path.endsWith("&quot;")) {
					prefix = "&quot;";
				}
				else if(path.startsWith("\"") && path.endsWith("\"")) {
					prefix = "\"";
				}
				else if(path.startsWith("'") && path.endsWith("'")) {
					prefix = "'";
				}
				if(prefix!=null) {
					path = path.substring(prefix.length(), path.length() - prefix.length());
				}
				if(path.indexOf("/dynamic/")!=-1) { //动态URL
					response.setHeader(ClientHeader.CLIENT_HEADER_CACHE_EXPIRE, "0"); //页面缓存时间设为0,页面不做缓存
				}
				//获取文件保存路径
				String savePath = path.startsWith("/") ? path : url.getPath().substring(0, url.getPath().lastIndexOf("/") + 1) + path;
				savePath = RequestUtils.getFilePath(savePath);
				File file;
				if(savePath!=null && (file=new File(savePath)).exists()) {
					path += "?client.modified=" + file.lastModified() + "&client.fileSize=" + file.length(); 
				}
				if(prefix!=null) {
					newText.append(prefix);
				}
				newText.append(path);
				if(prefix!=null) {
					newText.append(prefix);
				}
				index = matcher.end(groupIndex);
			}
			newText.append(text.substring(index));
			byte[] bytes = newText.toString().getBytes("utf-8");
			return new ByteArrayInputStream(bytes, 0, bytes.length);
		}
		finally {
			try {
				textOutputStream.close();
			}
			catch(Exception e) {
				
			}
		}
	}
}