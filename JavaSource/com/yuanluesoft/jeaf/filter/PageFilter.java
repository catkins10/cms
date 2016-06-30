package com.yuanluesoft.jeaf.filter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.ChineseConverter;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PageFilter implements Filter {
	private String[] disabledKeywords = null; //不允许转换的关键字
	private FileDownloadService fileDownloadService = null; //文件下载服务
	private SiteService siteService = null; //站点服务

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		String value = config.getInitParameter("disabledKeywords");
		if(value!=null) {
			disabledKeywords = value.split(",");
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		disabledKeywords = null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		try {
			if(siteService==null) {
				siteService = (SiteService)Environment.getService("siteService");
			}
			if(!siteService.validateHostName((HttpServletRequest)request)) {
				((HttpServletResponse)response).setStatus(HttpServletResponse.SC_NOT_FOUND); //404
				return;
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		final String uri = ((HttpServletRequest)request).getRequestURI();
		String lowerCaseURI = uri==null ? null : uri.toLowerCase().replace('\\', '/');
		if(!request.getServerName().equals("localhost") && (((HttpServletRequest)request).getContextPath() + "/services").equalsIgnoreCase(lowerCaseURI)) {
			((HttpServletResponse)response).setStatus(HttpServletResponse.SC_NOT_FOUND); //404
			return;
		}
		//检查cookie,判断用户是否使用繁体中文
		boolean convert = "true".equals(CookieUtils.getCookie((HttpServletRequest)request, "traditionalChinese")); //检查cookie是否要求繁体输出
		//检查是否属于不能转换的页面
		if(convert && disabledKeywords!=null && uri.indexOf(".shtml")!=-1) {
			for(int i=0; i<disabledKeywords.length; i++) {
				if(uri.indexOf(disabledKeywords[i])!=-1) {
					convert = false;
					CookieUtils.removeCookie((HttpServletResponse)response, "traditionalChinese", "/", null); //后台禁止使用繁体
					break;
				}
			}
		}
		String path;
		if(lowerCaseURI.indexOf(".shtml")==-1 && lowerCaseURI.indexOf(".jsp")==-1  && !lowerCaseURI.startsWith(Environment.getContextPath().toLowerCase() + "/attachments/") && (path = RequestUtils.getFilePath(uri))!=null) { //不是动态页面或者附件
			if(path.endsWith(".html") && convert) {
				writeTraditionalHTMLFile(path, (HttpServletRequest)request, (HttpServletResponse)response);
				return;
			}
			try {
				if(fileDownloadService==null) {
					fileDownloadService = (FileDownloadService)Environment.getService("fileDownloadService");
				}
				fileDownloadService.httpDownload((HttpServletRequest)request, (HttpServletResponse)response, path,	null, false, null);
			}
			catch(Exception e) {
				throw new ServletException(e);
			}
			return;
		}
		if(convert) {
			request = new TraditionalRequestWrapper((HttpServletRequest)request);
			response = new TraditionalResponseWrapper((HttpServletResponse)response);
		}
		filterChain.doFilter(request, response);
	}
	
	/**
	 * 输出繁体的HTML页面
	 * @param path
	 * @param request
	 * @param response
	 */
	private void writeTraditionalHTMLFile(String path, HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		InputStreamReader reader =  null;
		FileInputStream input = null;
		PrintWriter writer = null;
		try {
			input = new FileInputStream(path);
			reader = new InputStreamReader(input, "utf-8");
			writer = response.getWriter();
			char[] buffer = new char[8192];
			for(int readLen = reader.read(buffer); readLen!=-1; readLen = reader.read(buffer)) {
				for(int i=0; i<readLen; i++) {
					buffer[i] = ChineseConverter.convertToTraditionalChinese(buffer[i]);
				}
				writer.write(buffer, 0, readLen);
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try {
				reader.close();
			}
			catch(Exception e) {
				
			}
			try {
				input.close();
			}
			catch(Exception e) {
				
			}
			try {
				writer.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 为繁体转换封装的HTTP请求
	 * @author linchuan
	 *
	 */
	public class TraditionalRequestWrapper extends HttpServletRequestWrapper {

		public TraditionalRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		/* (non-Javadoc)
		 * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
		 */
		public String[] getParameterValues(String name) {
			String[] values = super.getParameterValues(name);
			if(values==null || values.length==0) {
				return values;
			}
			for(int i=0; i<values.length; i++) {
				values[i] = ChineseConverter.convertToSimpleChinese(values[i]);
			}
			return values;
		}

		/* (non-Javadoc)
		 * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
		 */
		public String getParameter(String name) {
			String value = super.getParameter(name);
			return value==null ? null : ChineseConverter.convertToSimpleChinese(value);
		}
	}
	
	/**
	 * 为繁体转换封装的HTTP应答
	 * 如果需要对其他文件做转换需要继承getOutputStream()
	 * @author linchuan
	 *
	 */
	public class TraditionalResponseWrapper extends HttpServletResponseWrapper { 

		public TraditionalResponseWrapper(HttpServletResponse response) { 
			super(response);
		}
		
		/*
		 * (non-Javadoc)
		 * @see javax.servlet.ServletResponseWrapper#getWriter()
		 */
		public PrintWriter getWriter() throws IOException {
			if(getContentType().startsWith("text/html") || getContentType().startsWith("text/javascript")) {
				return new TraditionalWriter(super.getWriter());
			}
			else {
				return super.getWriter();
			}
		}
	}
	
	/**
	 * 繁体中文输出
	 * @author linchuan
	 *
	 */
	public class TraditionalWriter extends PrintWriter {
		
		public TraditionalWriter(Writer out) {
			super(out);
		} 

		/*
		 * (non-Javadoc)
		 * @see java.io.PrintWriter#write(int)
		 */
		public void write(int c) {
			if(c<256) { //不是中文
				super.write((char)c);
			}
			else { //中文
				super.write(ChineseConverter.convertToTraditionalChinese((char)c));
			}
		}

		/*
		 * (non-Javadoc)
		 * @see java.io.PrintWriter#write(char[], int, int)
		 */
		public void write(char buf[], int off, int len) {
			for(int i=0; i<len; i++) {
				if(buf[i+off]>=256) { //中文
					buf[i+off] = ChineseConverter.convertToTraditionalChinese(buf[i+off]);
				}
			}
			super.write(buf, off, len);
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.io.PrintWriter#write(java.lang.String, int, int)
		 */
		public void write(String s, int off, int len) {
			for(int i=0; i<len; i++) {
				write(s.charAt(i + off));
			}
		}
	}
}