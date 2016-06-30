package com.yuanluesoft.jeaf.struts.service.spring;

import java.io.UnsupportedEncodingException;

import org.apache.struts.config.ModuleConfig;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.struts.parser.StrutsParser;
import com.yuanluesoft.jeaf.struts.service.StrutsDefineService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * 
 * @author linchuan
 *
 */
public class StrutsDefineServiceImpl implements StrutsDefineService {
	private StrutsParser strutsParser; //解析器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.struts.service.StrutsDefineService#saveStrutsConfig(org.apache.struts.config.ModuleConfig, java.lang.String)
	 */
	public void saveStrutsConfig(ModuleConfig strutsConfig, String applicationName) throws ServiceException {
		try {
			strutsParser.saveStrutsModuleConfig(strutsConfig, FileUtils.createDirectory(Environment.getWebinfPath() + applicationName) + "struts-config.xml");
		}
		catch (ParseException e) {
			throw new ServiceException(e);
		}
		registApplication(applicationName); //注册
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.struts.service.StrutsDefineService#registApplication(java.lang.String)
	 */
	public void registApplication(String applicationName) throws ServiceException {
		//添加到WEB-INF/web.xml
		String hibernateConfig = FileUtils.readStringFromFile(Environment.getWebinfPath() + "web.xml", "utf-8");
		if(hibernateConfig.indexOf("config/" + applicationName)==-1) {
			String config = "\n" +
						    "  <init-param>\n" +   
		   				  	"   <param-name>config/" + applicationName + "</param-name>\n" +
		   				  	"   <param-value>/WEB-INF/" + applicationName + "/struts-config.xml</param-value>\n" +
		   				  	"  </init-param>";
			hibernateConfig = hibernateConfig.replaceFirst("(<param-value>/WEB-INF/struts-config.xml</param-value>[\\s\\S]*?</init-param>)", "$1" + config);
			try {
				FileUtils.saveDataToFile(Environment.getWebinfPath() + "web.xml", hibernateConfig.getBytes("utf-8"));
			}
			catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		}
	}

	/**
	 * @return the strutsParser
	 */
	public StrutsParser getStrutsParser() {
		return strutsParser;
	}

	/**
	 * @param strutsParser the strutsParser to set
	 */
	public void setStrutsParser(StrutsParser strutsParser) {
		this.strutsParser = strutsParser;
	}
}