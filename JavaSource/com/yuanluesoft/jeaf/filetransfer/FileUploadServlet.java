package com.yuanluesoft.jeaf.filetransfer;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuanluesoft.jeaf.filetransfer.services.FileUploadService;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 文件上传servlet
 * @author linchuan
 *
 */
public class FileUploadServlet extends HttpServlet {
	private FileUploadService fileUploadService;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		super.init();
		ServletContext servletContext = getServletContext();
		WebApplicationContext webApplicationContext =	WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		fileUploadService = (FileUploadService)webApplicationContext.getBean("fileUploadService");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			fileUploadService.uploadFile(request, response);
		}
		catch(Exception e) {
			Logger.exception(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			fileUploadService.uploadFile(request, response);
		}
		catch(Exception e) {
			Logger.exception(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
