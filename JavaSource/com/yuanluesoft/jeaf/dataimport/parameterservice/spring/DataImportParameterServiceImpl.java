package com.yuanluesoft.jeaf.dataimport.parameterservice.spring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;
import com.yuanluesoft.jeaf.dataimport.parameterservice.DataImportParameterService;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.jeaf.util.callback.FileSearchCallback;

/**
 * 
 * @author chuan
 *
 */
public class DataImportParameterServiceImpl extends BusinessServiceImpl implements DataImportParameterService {
	private AttachmentService attachmentService;

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.parameterservice.DataImportParameterService#loadDataImportParameter(long, java.lang.String)
	 */
	public DataImportParameter loadDataImportParameter(long targetSiteId, String dataImportServiceClass) throws Exception {
		String path = attachmentService.getSavePath("jeaf/dataimport", "parameter", targetSiteId, false) + "parameter_" + dataImportServiceClass + ".dat";
		if(!FileUtils.isExists(path)) {
			return new DataImportParameter();
		}
		File file = new File(path);
		byte[] data = new byte[(int)file.length()];
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			input.read(data);
		}
		finally {
			input.close();
		}
		return (DataImportParameter)ObjectSerializer.deserialize(data); //反序列化
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dataimport.parameterservice.DataImportParameterService#saveDataImportParameter(long, java.lang.String, com.yuanluesoft.jeaf.dataimport.model.DataImportParameter)
	 */
	public void saveDataImportParameter(long targetSiteId, String dataImportServiceClass, DataImportParameter parameter) throws Exception {
		String path = attachmentService.getSavePath("jeaf/dataimport", "parameter", targetSiteId, true) + "parameter_" + dataImportServiceClass + ".dat";
		byte[] bytes = ObjectSerializer.serialize(parameter); //序列化
		FileOutputStream output = new FileOutputStream(path);
		try {
			output.write(bytes);
		}
		finally {
			output.close();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("dataImportServices".equals(itemsName)) { //数据导入服务列表
			final List services = new ArrayList();
			//遍历目录,获取数据导入服务
			String classesPath = Environment.getWebinfPath() + "classes/com/yuanluesoft/jeaf/dataimport/services";
			FileUtils.fileSearch(classesPath, null, new FileSearchCallback() {
				public void onFileFound(File file) {
					if(!file.getName().endsWith(".class") || file.getName().equals("DataImportService.class")) {
						return;
					}
					String className = file.getPath();
					className = className.substring(className.indexOf("classes") + "classes".length() + 1, className.length() - ".class".length()).replaceAll("[\\\\/]", ".");
					try {
						DataImportService dataImportService = (DataImportService)Class.forName(className).newInstance();
						services.add(new String[]{dataImportService.getName(), dataImportService.getClassName()});
					}
					catch(NoClassDefFoundError e) {
						
					}
					catch(Exception e) {
						
					}
				}
			});
			Collections.sort(services, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					String[] service0 = (String[])arg0;
					String[] service1 = (String[])arg1;
					return Collator.getInstance(Locale.CHINA).compare(service0[0], service1[0]);
				}
			});
			return services;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
}