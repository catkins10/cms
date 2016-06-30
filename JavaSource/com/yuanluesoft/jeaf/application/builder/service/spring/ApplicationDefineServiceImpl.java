package com.yuanluesoft.jeaf.application.builder.service.spring;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationField;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationFieldParameter;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationView;
import com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder;
import com.yuanluesoft.jeaf.application.builder.service.ApplicationDefineService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class ApplicationDefineServiceImpl extends BusinessServiceImpl implements ApplicationDefineService {
	private ApplicationBuilder applicationBuilder;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof ApplicationField) {
			retrieveFieldParameters((ApplicationField)record);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		record = super.save(record);
		if(record instanceof ApplicationForm) { //表单
			saveOrUpdatePresettingFields((ApplicationForm)record, true); //创建预置字段列表
		}
		else if(record instanceof ApplicationField) { //字段
			saveFieldParameters((ApplicationField)record, true);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		if(record instanceof ApplicationForm) { //表单
			saveOrUpdatePresettingFields((ApplicationForm)record, false); //更新预置字段列表
		}
		else if(record instanceof ApplicationField) { //字段
			saveFieldParameters((ApplicationField)record, false);
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(com.yuanluesoft.jeaf.database.Record, boolean)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		if(record instanceof ApplicationForm) { //表单
			ApplicationForm form = (ApplicationForm)record;
			FormTemplate formTemplate = applicationBuilder.getFormTemplate(form.getTemplateName());
			if(formTemplate.isUniqueInApplicaton()) { //相同模板的表单在应用中只能有一个
				String hql = "select ApplicationForm.id" +
							 " from ApplicationForm ApplicationForm" +
							 " where ApplicationForm.id!=" + form.getId() +
							 " and ApplicationForm.applicationId=" + form.getApplicationId() +
							 " and ApplicationForm.templateName='" + JdbcUtils.resetQuot(formTemplate.getName()) + "'";
				if(getDatabaseService().findRecordByHql(hql)!=null) {
					return ListUtils.generateList("相同模板的表单仅允许有一个");
				}
			}
			String hql = "select ApplicationForm.id" +
						 " from ApplicationForm ApplicationForm" +
						 " where ApplicationForm.id!=" + form.getId() +
						 " and lower(ApplicationForm.englishName)='" + JdbcUtils.resetQuot(form.getEnglishName().toLowerCase()) + "'";
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				return ListUtils.generateList("表单[" + form.getEnglishName() + "]已经存在，不允许重名");
			}
		}
		else if(record instanceof ApplicationField) { //字段
			ApplicationField field = (ApplicationField)record;
			String hql = "select ApplicationField.id" +
						 " from ApplicationField ApplicationField" +
						 " where ApplicationField.id!=" + field.getId() +
						 " and ApplicationField.formId=" + field.getFormId();
			if(getDatabaseService().findRecordByHql(hql + " and ApplicationField.name='" + JdbcUtils.resetQuot(field.getName()) + "'")!=null) {
				return ListUtils.generateList("字段[" + field.getName() + "]已经存在，不允许重名");
			}
			if(getDatabaseService().findRecordByHql(hql + " and lower(ApplicationField.englishName)='" + JdbcUtils.resetQuot(field.getEnglishName().toLowerCase()) + "'")!=null) {
				return ListUtils.generateList("字段[" + field.getEnglishName() + "]已经存在，不允许重名");
			}
		}
		return super.validateBusiness(record, isNew);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("templateName".equals(itemsName)) { //模板名称列表
			List selectItems = new ArrayList();
			for(Iterator iterator = applicationBuilder.listFormTemplates().iterator(); iterator.hasNext();) {
				FormTemplate formTemplate = (FormTemplate)iterator.next();
				selectItems.add(new String[]{formTemplate.getLabel(), formTemplate.getName()});
			}
			return selectItems;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}
	
	/**
	 * 保存或者更新预设字段
	 * @param applicationForm
	 * @param isNewForm
	 * @throws ServiceException
	 */
	private void saveOrUpdatePresettingFields(ApplicationForm applicationForm, boolean isNewForm) throws ServiceException {
		FormTemplate formTemplate = applicationBuilder.getFormTemplate(applicationForm.getTemplateName());
		List businessFields = FieldUtils.listRecordFields(formTemplate.getParentRecordClass().getName(), null, null, null, null, false, false, false, false, 0);
		if(!isNewForm) { //不是新表单
			//删除已经不存在的字段
			String hql = "from ApplicationField ApplicationField" +
						 " where ApplicationField.formId=" + applicationForm.getId() +
						 " and ApplicationField.isPresetting=1" +
						 " and ApplicationField.englishName not in ('" + ListUtils.join(businessFields, "name", "', '", false) + "')";
			getDatabaseService().deleteRecordsByHql(hql);
		}
		for(int i=businessFields.size()-1; i>=0; i--) {
			Field businessField = (Field)businessFields.get(i);
			ApplicationField applicationField = (ApplicationField)ListUtils.findObjectByProperty(applicationForm.getFields(), "englishName", businessField.getName());
			boolean isNewField = applicationField==null;
			if(isNewField) {
				applicationField = new ApplicationField();
				applicationField.setId(UUIDLongGenerator.generateId()); //ID
				applicationField.setFormId(applicationForm.getId()); //表单ID
				applicationField.setEnglishName(businessField.getName()); //英文名称
				applicationField.setName(businessField.getTitle()); //字段名称
				applicationField.setRequired(businessField.isRequired() ? 1 : 0); //是否必填字段
				applicationField.setInputMode(businessField.getInputMode()); //输入方式
				//参数列表
				for(Iterator iteratorParameter = businessField.getParameters()==null ? null : businessField.getParameters().keySet().iterator(); iteratorParameter!=null && iteratorParameter.hasNext();) {
					String parameterName = (String)iteratorParameter.next();
					String parameterValue = (String)businessField.getParameters().get(parameterName);
					ApplicationFieldParameter fieldParameter = new ApplicationFieldParameter();
					fieldParameter.setId(UUIDLongGenerator.generateId()); //ID
					fieldParameter.setFieldId(applicationField.getId()); //字段ID
					fieldParameter.setParameterName("class".equals(parameterName) ? "className" : parameterName); //参数名称
					fieldParameter.setParameterValue("itemsText".equals(parameterName) ? parameterValue.replaceAll("\\\\0", ",") : parameterValue); //参数值
					getDatabaseService().saveRecord(fieldParameter);
				}
			}
			//设置数据类型,文本|varchar\0长文本|text\0数字|number\0日期|date\0时间|timestamp\0附件|attachment\0图片|image\0视频|video\0办理意见|opinion
			if(",string,html,".indexOf("," + businessField.getType() + ",")!=-1) {
				applicationField.setFieldType(businessField.isPersistence() && (businessField.getLength()==null || Integer.parseInt(businessField.getLength())>4000) ? "text" : "varchar"); 
			}
			else if(",char,number,date,timestamp,attachment,image,video,opinion,".indexOf("," + businessField.getType() + ",")!=-1) {
				applicationField.setFieldType(businessField.getType()); 
			}
			applicationField.setFieldLength(businessField.getLength()); //字段长度
			applicationField.setIsPresetting(1); //是否预设字段
			applicationField.setIsPersistence(businessField.isPersistence() ? 1 : 0); //是否数据库字段
			if(isNewField) {
				getDatabaseService().saveRecord(applicationField);
			}
			else {
				getDatabaseService().updateRecord(applicationField);
			}
		}
	}

	/**
	 * 设置数据类型、输入方式参数
	 * @param field
	 * @throws ServiceException
	 */
	private void retrieveFieldParameters(ApplicationField field) throws ServiceException {
		if(field.getParameters()==null || field.getParameters().isEmpty()) {
			return;
		}
		Object dataTypeParameter = getDataTypeParameter(field); //数据类型对应的参数配置
		Object inputModeParameter = getInputModeParameter(field); //输入方式对应的参数配置
		for(Iterator iterator = field.getParameters().iterator(); iterator.hasNext();) {
			ApplicationFieldParameter fieldParameter = (ApplicationFieldParameter)iterator.next();
			BeanUtils.setPropertyValue(dataTypeParameter, fieldParameter.getParameterName(), fieldParameter.getParameterValue(), null);
			BeanUtils.setPropertyValue(inputModeParameter, fieldParameter.getParameterName(), fieldParameter.getParameterValue(), null);
		}
	}

	/**
	 * 保存字段参数
	 * @param field
	 * @param isNew
	 * @throws ServiceException
	 */
	private void saveFieldParameters(ApplicationField field, boolean isNew) throws ServiceException {
		if(!isNew) {
			getDatabaseService().deleteRecordsByHql("from ApplicationFieldParameter ApplicationFieldParameter where ApplicationFieldParameter.fieldId=" + field.getId());
		}
		field.setParameters(new LinkedHashSet());
		saveParameter(field, getDataTypeParameter(field)); //数据类型对应的参数配置
		saveParameter(field, getInputModeParameter(field)); //输入方式对应的参数配置
	}
	
	/**
	 * 保存参数
	 * @param field
	 * @param parameter
	 * @throws ServiceException
	 */
	private void saveParameter(ApplicationField field, Object parameter) throws ServiceException {
		if(parameter==null) {
			return;
		}
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(parameter);
		for(int i=0; i<properties.length; i++) {
			String propertyName = properties[i].getName();
			if("class".equals(propertyName)) {
				continue;
			}
			ApplicationFieldParameter fieldParameter = new ApplicationFieldParameter();
			fieldParameter.setId(UUIDLongGenerator.generateId());
			fieldParameter.setFieldId(field.getId()); //字段ID
			fieldParameter.setParameterName(propertyName); //参数名称
			try {
				fieldParameter.setParameterValue(StringUtils.format(PropertyUtils.getProperty(parameter, propertyName), null, null)); //参数值
			}
			catch(Exception e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
			getDatabaseService().saveRecord(fieldParameter);
			field.getParameters().add(fieldParameter);
		}
	}
	
	/**
	 * 获取数据类型参数配置
	 * @param field
	 * @return
	 */
	private Object getDataTypeParameter(ApplicationField field) throws ServiceException {
		String[] dataTypeMapping =  {
			"varchar", "stringParameter", "text", "", "char", "charParameter", "number", "numberParameter",
			"date", "dateParameter", "timestamp", "timestampParameter",
			"attachment", "", "image", "", "video", "", "opinion", "opinionParameter"};
		try {
			for(int i=0; i<dataTypeMapping.length; i++) {
				if(dataTypeMapping[i].equals(field.getFieldType())) {
					return dataTypeMapping[i+1].isEmpty() ? null : PropertyUtils.getProperty(field, dataTypeMapping[i+1]);
				}
			}
			return null;
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 获取输入方式参数
	 * @param field
	 * @return
	 * @throws ServiceException
	 */
	private Object getInputModeParameter(ApplicationField field) throws ServiceException {
		String[] inputModeMapping = {"text", "", "password", "", "textarea", "textareaParameter", "htmleditor", "htmlEditorParameter", "radio", "radioParameter",
				 "checkbox", "checkboxParameter", "dropdown", "dropdownParameter", "select", "selectParameter", "date", "", "datetime", "",
				 "time", "timeInputParameter", "hidden", "hiddenParameter", "readonly", "",
				 "attachment", "attachmentParameter", "image", "imageParameter", "video", "videoParameter"};
		try {
			for(int i=0; i<inputModeMapping.length; i++) {
				if(inputModeMapping[i].equals(field.getInputMode())) {
					return inputModeMapping[i+1].isEmpty() ? null : PropertyUtils.getProperty(field, inputModeMapping[i+1]);
				}
			}
			return null;
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder#createNavigators(long)
	 */
	public void createNavigators(long applicationId) throws ServiceException {
		String hql = "select ApplicationView" +
					 " from ApplicationView  ApplicationView, ApplicationForm ApplicationForm" +
					 " where ApplicationView.formId=ApplicationForm.id" +
					 " and ApplicationForm.applicationId=" + applicationId +
					 " and ApplicationView.id not in (" +
					 "	select ApplicationNavigator.viewId" +
					 "   from ApplicationNavigator ApplicationNavigator" +
					 "   where ApplicationNavigator.applicationId=" + applicationId + ")" +
					 " order by ApplicationView.id";
		List views = getDatabaseService().findRecordsByHql(hql);
		if(views==null || views.isEmpty()) {
			return;
		}
		for(Iterator iterator = views.iterator(); iterator.hasNext();) {
			ApplicationView view = (ApplicationView)iterator.next();
			ApplicationNavigator navigator = new ApplicationNavigator();
			navigator.setId(UUIDLongGenerator.generateId()); //ID
			navigator.setApplicationId(applicationId); //应用ID
			navigator.setLabel(view.getName()); //名称
			navigator.setViewId(view.getId()); //视图ID
			navigator.setViewName(view.getName()); //视图名称
			getDatabaseService().saveRecord(navigator);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder#createViews(com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void createViews(ApplicationForm applicationForm, String viewFieldIds, String viewFieldNames, String sortFieldIds, String sortFieldNames, String sortFieldDirections) throws ServiceException {
		FormTemplate formTemplate = applicationBuilder.getFormTemplate(applicationForm.getTemplateName());
		List views = formTemplate.generateApplicationViews(applicationForm, viewFieldIds, viewFieldNames, sortFieldIds, sortFieldNames, sortFieldDirections);
		for(Iterator iterator = views.iterator(); iterator.hasNext();) {
			ApplicationView view = (ApplicationView)iterator.next();
			view.setFormId(applicationForm.getId()); //表单ID
			save(view);
		}
	}

	/**
	 * @return the applicationBuilder
	 */
	public ApplicationBuilder getApplicationBuilder() {
		return applicationBuilder;
	}

	/**
	 * @param applicationBuilder the applicationBuilder to set
	 */
	public void setApplicationBuilder(ApplicationBuilder applicationBuilder) {
		this.applicationBuilder = applicationBuilder;
	}
}