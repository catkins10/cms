package com.yuanluesoft.jeaf.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.wap.WapPageService;

/**
 * WAP页面过滤器
 * @author linchuan
 *
 */
public class WapPageFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
			
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		//检查是否属于不能转换的页面
		if(RequestUtils.isWapRequest((HttpServletRequest)request) && !(response instanceof WapResponseWrapper)) {
			response = new WapResponseWrapper((HttpServletResponse)response);
		}
		filterChain.doFilter(request, response);
		try {
			if((response instanceof WapResponseWrapper) && (response.getWriter() instanceof WapWriter)) {
				((WapWriter)response.getWriter()).writeWapPage();
			}
		}
		catch(IllegalStateException e) {
			if("getOutputStream() has already been called for this response".equals(e.getMessage())) {
				Logger.error("DoFilter error, url is " + RequestUtils.getRequestURL((HttpServletRequest)request, true) + ", error is " + e.getMessage());
			}
			else {
				throw e;
			}
		}
	}
	
	/**
	 * WAP应答
	 * 如果需要对其他文件做转换需要继承getOutputStream()
	 * @author linchuan
	 *
	 */
	public class WapResponseWrapper extends HttpServletResponseWrapper {
		private PrintWriter writer = null;

		public WapResponseWrapper(HttpServletResponse response) { 
			super(response);
		}
		
		/*
		 * (non-Javadoc)
		 * @see javax.servlet.ServletResponseWrapper#getWriter()
		 */
		public PrintWriter getWriter() throws IOException {
			if(writer==null) {
				writer = super.getWriter();
				String contentType = getContentType();
				if(contentType!=null && contentType.startsWith("text/html") && !(writer instanceof WapWriter)) { //只处理text/html
					writer = new WapWriter(writer, this);
				}
			}
			return writer;
		}
	}
	
	/**
	 * WAP输出
	 * @author linchuan
	 *
	 */
	public class WapWriter extends PrintWriter {
		private StringBuffer htmlContent = new StringBuffer();
		private WapResponseWrapper response;
		
		public WapWriter(Writer writer, WapResponseWrapper response) {
			super(writer);
			this.response = response;
		} 

		/*
		 * (non-Javadoc)
		 * @see java.io.PrintWriter#write(int)
		 */
		public void write(int c) {
			htmlContent = htmlContent.append((char)c);
		}

		/*
		 * (non-Javadoc)
		 * @see java.io.PrintWriter#write(char[], int, int)
		 */
		public void write(char buf[], int offset, int len) {
			htmlContent = htmlContent.append(buf, offset, len);
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.io.PrintWriter#write(java.lang.String, int, int)
		 */
		public void write(String s, int offset, int len) {
			htmlContent = htmlContent.append(s, offset, len);
		}

		/* (non-Javadoc)
		 * @see java.io.PrintWriter#close()
		 */
		public void close() {
			writeWapPage();
			super.close();
		}

		/* (non-Javadoc)
		 * @see java.io.PrintWriter#close()
		 */
		public void writeWapPage() {
			if(htmlContent==null) {
				return;
			}
			try {
				response.setContentType("text/vnd.wap.wml");
				WapPageService wapPageService = (WapPageService)Environment.getService("wapPageService");
				wapPageService.writeWapPage(htmlContent.toString(), super.out);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			htmlContent = null;
		}
	}
}