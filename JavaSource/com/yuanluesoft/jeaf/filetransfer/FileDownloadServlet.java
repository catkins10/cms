package com.yuanluesoft.jeaf.filetransfer;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;

/**
 * 
 * @author linchuan
 *
 */
public class FileDownloadServlet extends HttpServlet {
	private FileDownloadService fileDownloadService;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		super.init();
		ServletContext servletContext = getServletContext();
		WebApplicationContext webApplicationContext =	WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		fileDownloadService = (FileDownloadService)webApplicationContext.getBean("fileDownloadService");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			fileDownloadService.downloadFile(request, response);
		}
		catch(ServiceException e) {
			
		}
	}
}