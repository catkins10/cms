/*
 * Created on 2005-9-19
 *
 */
package com.yuanluesoft.jeaf.form.service.spring;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.parser.FormDefineParser;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.callback.FileSearchCallback;

/**
 * 
 * @author linchuan
 *
 */
public class FormDefineServiceImpl implements FormDefineService {
	private Map formDefineMap = new HashMap();; //业务对象定义映射表
	private Cache cache; //缓存
	private FormDefineParser formDefineParser; //表单定义解析器
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormDefineService#loadFormDefine(java.lang.Class)
	 */
	public Form loadFormDefine(Class formClass) throws ServiceException {
		initFormDefineMap(); //初始化映射表
		for(; !formClass.equals(org.apache.struts.action.ActionForm.class) && !formClass.equals(Object.class) ; formClass = formClass.getSuperclass()) {
			String applicationName = (String)formDefineMap.get(formClass.getName());
			if(applicationName==null) {
				continue;
			}
			List forms = listForms(applicationName);
			Form form = (Form)ListUtils.findObjectByProperty(forms, "className", formClass.getName());
			try {
				form = (Form)form.clone();
			}
			catch (CloneNotSupportedException e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
			return form;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormDefineService#loadFormDefine(java.lang.String)
	 */
	public Form loadFormDefine(String formClassName) throws ServiceException {
		if(formClassName==null) {
			return null;
		}
		try {
			return loadFormDefine(Class.forName(formClassName));
		} 
		catch (ClassNotFoundException e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormDefineService#loadFormDefine(java.lang.String, java.lang.String)
	 */
	public Form loadFormDefine(String applicationName, String formName) throws ServiceException {
		initFormDefineMap(); //初始化映射表
		List forms = listForms(applicationName);
		if(forms==null) {
			return null;
		}
		Form form = (Form)ListUtils.findObjectByProperty(forms, "name", formName);
		if(form==null) {
			return null;
		}
		try {
			form = (Form)form.clone();
		}
		catch (CloneNotSupportedException e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormDefineService#listFormDefines(java.lang.String)
	 */
	public List listFormDefines(String applicationName) throws ServiceException {
		initFormDefineMap(); //初始化映射表
		List originalForms = listForms(applicationName);
		if(originalForms==null) {
			return null;
		}
		List forms = new ArrayList();
		for(Iterator iterator = originalForms.iterator(); iterator.hasNext();) {
			Form form = (Form)iterator.next();
			try {
				form = (Form)form.clone();
			}
			catch (CloneNotSupportedException e) {
				
			}
			forms.add(form);
		}
		return forms;
	}

	/**
	 * 初始化业务对象定义映射表
	 * @throws ServiceException
	 */
	private void initFormDefineMap() throws ServiceException {
		synchronized(formDefineMap) {
			if(!formDefineMap.isEmpty()) {
				return;
			}
			FileUtils.fileSearch(Environment.getWebinfPath(), "form-config.xml", new FileSearchCallback() {
				public void onFileFound(File file) {
					String applicationName = file.getPath().replace('\\', '/');
					int index = applicationName.toLowerCase().lastIndexOf("web-inf/");
					applicationName = applicationName.substring(index + "web-inf/".length(), applicationName.lastIndexOf('/'));
					if(applicationName.indexOf("deployment")!=-1) {
						return;
					}
					Logger.info("FormDefineService: load form define of application " + applicationName + ".");
					//获取业务逻辑定义
					List forms;
					try {
						forms = formDefineParser.parse(applicationName, Environment.getWebinfPath() + applicationName + "/form-config.xml");
					}
					catch (Exception e) {
						Logger.exception(e);
						return;
					}
					for(Iterator iteratorForm = forms.iterator(); iteratorForm.hasNext();) {
						Form form = (Form)iteratorForm.next();
						//添加到映射表
						formDefineMap.put(form.getClassName(), form.getApplicationName());
					}
				}
			});
		}
	}
	
	/**
	 * 获取表单定义
	 * @param applicationName
	 * @return
	 * @throws ServiceException
	 */
	private List listForms(String applicationName) throws ServiceException {
		try {
			List forms = (List)cache.get("formDefine" + applicationName);
			if(forms==null) {
				String filePath = Environment.getWebinfPath() + applicationName + "/form-config.xml";
				if(!FileUtils.isExists(filePath)) {
					return null;
				}
				forms = formDefineParser.parse(applicationName, filePath);
				for(Iterator iterator = forms.iterator(); iterator.hasNext();) {
					Form form = (Form)iterator.next();
					try {
						inheritParentForm(form); //继承父类的字段列表
					}
					catch(Exception e) {
						Logger.exception(e);
					}
				}
				cache.put("formDefine" + applicationName, forms);
			}
			return forms;
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 递归:继承父类的字段、参数列表
	 * @param businessObject
	 * @throws ServiceException
	 */
	private void inheritParentForm(Form form) throws Exception {
		if(form.getClassName()==null) {
			return;
		}
		Class formClass = null;
		try {	
			formClass = Class.forName(form.getClassName());
		}
		catch(ClassNotFoundException e) {
			return;
		}
		if(formClass.equals(org.apache.struts.action.ActionForm.class) || formClass.equals(Object.class)) {
			return;
		}
		String applicationName = null;
		for(formClass = formClass.getSuperclass(); !formClass.equals(org.apache.struts.action.ActionForm.class) && !formClass.equals(Object.class); formClass = formClass.getSuperclass()) {
			//获取父类对应的表单对象
			applicationName = (String)formDefineMap.get(formClass.getName());
			if(applicationName!=null) { //有对应的表单对象
				break;
			}
		}
		if(applicationName==null) { //没有对应的表单对象,不需要继承
			return;
		}
		List forms = formDefineParser.parse(applicationName, Environment.getWebinfPath() + applicationName + "/form-config.xml");
		Form parentForm = (Form)ListUtils.findObjectByProperty(forms, "className", formClass.getName());
		inheritParentForm(parentForm); //先递归更新父类表单对象
		
		//继承字段
		if(parentForm.getFields()!=null && !parentForm.getFields().isEmpty()) { //父对象没有配置字段
			if(form.getFields()==null || form.getFields().isEmpty()) { //当前表单对象自己没有配置字段
				form.setFields(parentForm.getFields());
			}
			else {
				//合并父类表单对象的字段列表
				for(Iterator iterator = parentForm.getFields().iterator(); iterator.hasNext();) {
					Field parentField = (Field)iterator.next();
					Field field = (Field)ListUtils.findObjectByProperty(form.getFields(), "name", parentField.getName());
					//检查当前表单对象是否有配置过同名字段
					if(field==null) {
						form.getFields().add(parentField);
					}
					else if(parentField.getParameters()!=null && !parentField.getParameters().isEmpty()) { //父对象字段有配置参数
						//合并父类字段的参数列表
						for(Iterator iteratorField = parentField.getParameters().keySet().iterator(); iteratorField.hasNext();) {
							String key = (String)iteratorField.next();
							if(field.getParameter(key)==null) { //检查当前业务对象是否有配置过同名参数
								field.setParameter(key, parentField.getParameter(key));
							}
						}
					}
				}
			}
		}
		
		//继承参数
		if(parentForm.getExtendedParameters()!=null && !parentForm.getExtendedParameters().isEmpty()) { //父对象有配置参数
			if(form.getExtendedParameters()==null || form.getExtendedParameters().isEmpty()) { //当前业务对象自己没有配置字段
				form.setExtendedParameters(parentForm.getExtendedParameters());
			}
			else {
				//合并父类业务对象的参数列表
				for(Iterator iterator = parentForm.getExtendedParameters().keySet().iterator(); iterator.hasNext();) {
					Object key = iterator.next();
					if(form.getExtendedParameters().get(key)==null) { //检查当前业务对象是否有配置过同名参数
						form.getExtendedParameters().put(key, parentForm.getExtendedParameters().get(key));
					}
				}
			}
		}
	}
		
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.service.FormDefineService#saveFormDefine(java.lang.String, java.util.List)
	 */
	public void saveFormDefine(String applicationName, List forms) throws ServiceException {
		try {
			formDefineParser.saveFormDefine(forms, FileUtils.createDirectory(Environment.getWebinfPath() + applicationName) + "form-config.xml");
		}
		catch (ParseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * @return Returns the cache.
	 */
	public Cache getCache() {
		return cache;
	}
	/**
	 * @param cache The cache to set.
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	/**
	 * @return Returns the formDefineParser.
	 */
	public FormDefineParser getFormDefineParser() {
		return formDefineParser;
	}
	/**
	 * @param formDefineParser The formDefineParser to set.
	 */
	public void setFormDefineParser(FormDefineParser formDefineParser) {
		this.formDefineParser = formDefineParser;
	}
}