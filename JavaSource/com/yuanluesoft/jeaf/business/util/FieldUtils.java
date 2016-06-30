package com.yuanluesoft.jeaf.business.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.business.service.SqlQueryService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.video.model.Video;
import com.yuanluesoft.jeaf.video.service.VideoService;
import com.yuanluesoft.jeaf.video.util.VideoPlayerUtils;

/**
 * 
 * @author linchuan
 *
 */
public class FieldUtils {
	private static DatabaseService databaseService; //数据库访问对象
	private static BusinessDefineService businessDefineService; //业务逻辑定义服务
	private static FormDefineService formDefineService; //表单定义服务
	
	/**
	 * 获取表单字段定义
	 * @param formDefine
	 * @param fieldName
	 * @param request
	 * @return
	 */
	public static Field getFormField(Form formDefine, String fieldName, HttpServletRequest request) {
		if(fieldName==null || fieldName.isEmpty()) {
			return null;
		}
		Field fieldDefine = formDefine.getField(fieldName); //直接按名称获取字段定义,表单字段允许带"."
		if(fieldDefine!=null) {
			return fieldDefine;
		}
		else if(fieldName.equals("validateCode")) { //验证码
			return new Field(fieldName, "验证码", "string", null, "text", true, false);
		}
		int index = fieldName.indexOf('.');
		fieldDefine = formDefine.getField(index==-1 ? fieldName : fieldName.substring(0, index)); //获取对应的字段定义
		if(index!=-1 && fieldDefine!=null) { //表单的组成部分、组成部分列表
			String componentClassName = (String)fieldDefine.getParameter("class");
			fieldDefine = getRecordField(componentClassName, fieldName.substring(index + 1), request);
			if(fieldDefine==null) {
				return null;
			}
			try {
				fieldDefine = (Field)fieldDefine.clone();
			}
			catch (CloneNotSupportedException e) {
				
			}
			fieldDefine.setName(fieldName);
		}
		else if(formDefine.getRecordClassName()!=null) { //不是表单组成部分或者组成部分列表,且记录类名称不为空
			fieldDefine = getRecordField(formDefine.getRecordClassName(), fieldName, request); //获取记录的字段
		}
		return fieldDefine;
	}
	
	/**
	 * 获取记录字段定义
	 * @param recordClass
	 * @param fieldName
	 * @param request
	 * @return
	 */
	public static Field getRecordField(String recordClassName, String fieldName, HttpServletRequest request) {
		if(fieldName==null) {
			return null;
		}
		//获取业务对象
		BusinessObject businessObject = request==null ? null : (BusinessObject)request.getAttribute("businessObject_" + recordClassName);
		if(businessObject==null) {
			try {
				businessObject = getBusinessDefineService().getBusinessObject(recordClassName);
			}
			catch (ServiceException e) {
				e.printStackTrace();
			}
			if(request!=null) {
				request.setAttribute("businessObject_" + recordClassName, businessObject);
			}
		}
		if(businessObject==null) {
			Logger.error("business object of " + recordClassName + " is not exists.");
		}
		int index = fieldName.indexOf('.');
		Field fieldDefine = businessObject.getField(index==-1 ? fieldName : fieldName.substring(0, index)); //获取对应的字段定义
		if(index==-1 || fieldDefine==null) {
			if(fieldDefine==null && fieldName.equals("recordNumber")) {
				return new Field(fieldName, "序号", "number", null, "readonly", false, false);
			}
			else if(fieldDefine==null && fieldName.equals("recordCount")) {
				return new Field(fieldName, "记录数", "number", null, "readonly", false, false);
			}
		}
		else {
			//递归调用:获取组成部分的字段
			String componentClass = (String)fieldDefine.getParameter("class");
			String componentFieldName = fieldName.substring(index + 1);
			fieldDefine = getRecordField(componentClass, componentFieldName, request);
			if(fieldDefine!=null) {
				try {
					fieldDefine = (Field)fieldDefine.clone();
				}
				catch (CloneNotSupportedException e) {
					
				}
				fieldDefine.setName(fieldName);
			}
		}
		return fieldDefine;
	}
	
	/**
	 * 获取字段值
	 * @param bean 记录或者表单
	 * @param field
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Object getFieldValue(Object bean, String fieldName, HttpServletRequest request) throws Exception {
		int index = fieldName.indexOf('.');
		String propertyName = index==-1 ? fieldName : fieldName.substring(0, index);
		Field field;
		if(bean instanceof ActionForm) {
			ActionForm actionForm = (ActionForm)bean;
			if(actionForm.getFormDefine()==null || actionForm.getFormDefine().getClassName()==null) {
				actionForm.setFormDefine(getFormDefineService().loadFormDefine(actionForm.getClass()));
			}
			field = getFormField(actionForm.getFormDefine(), propertyName, request);
		}
		else if(bean instanceof SqlResult) {
			String recordName = (String)((SqlResult)bean).get("sqlRecordName");
			field = getRecordField(recordName, propertyName, request); //获取字段定义
			if("component".equals(field.getType())) { //组成部分
				propertyName = fieldName.replaceAll("\\.", "_");
				field = getRecordField((String)field.getParameter("class"), fieldName.substring(index + 1), request); //获取字段定义
			}
		}
		else {
			field = getRecordField(bean.getClass().getName(), propertyName, request); //获取字段定义
		}
		if(field==null) {
			if(bean instanceof Record) {
				return ((Record)bean).getExtendPropertyValue(propertyName);
			}
			throw new ServiceException("Field " + propertyName + " is not exists, class name is " + bean.getClass().getName() + ".");
		}
		
		//意见
		if("opinion".equals(field.getType())) {
			Collection opinions = (Collection)PropertyUtils.getProperty(bean, "opinions");
			return ListUtils.getSubListByProperty(opinions, "opinionType", propertyName);
		}

		//附件、图片、视频
		if("attachment".equals(field.getType()) || "image".equals(field.getType()) || "video".equals(field.getType())) {
			long recordId = 0;
			if(bean instanceof SqlResult) {
				request.setAttribute("SQL_RECORD", bean);
			}
			else {
				recordId = ((Number)PropertyUtils.getProperty(bean, "id")).longValue();
			}
			AttachmentService attachmentService = getAttachmentService(field);
			String applicationName = getApplicationName(bean);
			return attachmentService.list(applicationName, getAttachmentType(bean, request, field), recordId, false, 0, request);
		}
		
		//获取字段值
		Object value = null;
		if(bean instanceof SqlResult) { //sql查询
			value = ((SqlResult)bean).get(propertyName);
		}
		else {
			try {
				value = PropertyUtils.getProperty(bean, propertyName);
			}
			catch(Exception e) {
				value = request.getParameter(propertyName);
			}
		}
		
		//字段值为空
		if(value==null) {
			return null;
		}

		//图片名称,视频名称
		if("imageName".equals(field.getType()) || "videoName".equals(field.getType())) {
			if((bean instanceof Image) && "imageName".equals(field.getType())) { //当前记录就是图片
				return bean;
			}
			if((bean instanceof Video) && "videoName".equals(field.getType())) { //当前记录就是视频
				return bean;
			}
			//返回Image/Video模型
			String textValue = (String)value;
			String referenceFields = (String)field.getParameter("referenceFields");
			if(referenceFields!=null) { //有关联的图片、视频字段
				try {
					Field referenceField;
					long recordId;
					String applicationName;
					String referenceRecordClass = (String)field.getParameter("referenceRecordClass"); //关联的记录类
					if(referenceRecordClass!=null) {
						referenceField = getRecordField(referenceRecordClass, (String)field.getParameter("referenceRecordField"), request);
						recordId = ((Number)PropertyUtils.getProperty(bean, referenceFields)).longValue();
						applicationName = getBusinessDefineService().getBusinessObject(referenceRecordClass).getApplicationName();
					}
					else {
						String referenceFieldName = referenceFields.split(",")[0];
						referenceField = (bean instanceof ActionForm ? getFormField(((ActionForm)bean).getFormDefine(), referenceFieldName, request) : getRecordField(bean.getClass().getName(), referenceFieldName, request)); //获取字段定义
						recordId = ((Number)PropertyUtils.getProperty(bean, "id")).longValue();
						applicationName = getApplicationName(bean);
					}
					String attachmentType = getAttachmentType(bean, request, referenceField);
					AttachmentService attachmentService = getAttachmentService(referenceField);
					if("imageName".equals(field.getType())) { //图片
						return ((ImageService)attachmentService).getImage(applicationName, attachmentType, recordId, textValue, request);
					}
					else {
						return ((VideoService)attachmentService).getVideo(applicationName, attachmentType, recordId, textValue, request);
					}
				}
				catch(Exception e) {
					
				}
			}
			//没有关联的图片附件,或者被关联字段不是图片或者视频
			String url;
			String filePath = null;
			if(textValue.startsWith("/") || textValue.indexOf("://")!=-1) {
				url = textValue;
			}
			else {
				String applicationName = getApplicationName(bean);
				url = Environment.getContextPath() + "/" + applicationName + "/" + textValue;
				filePath = Environment.getWebAppPath() + applicationName + "/" + textValue;
			}
			if("imageName".equals(field.getType())) { //图片
				Image image = new Image();
				image.setUrl(url);
				image.setFilePath(filePath);
				image.setName(textValue);
				return image;
			}
			else { //视频
				Video video = new Video();
				video.setUrl(url);
				video.setFilePath(filePath);
				video.setName(textValue);
				return video;
			}
		}
		
		//组成部分
		if(index!=-1 && "component".equals(field.getType())) {
			return getFieldValue(value, fieldName.substring(index + 1), request);
		}
		
		//组成部分列表
		if(index!=-1 && "components".equals(field.getType())) {
			//直接返回记录列表
			Collection components = (Collection)value;
			List values = new ArrayList();
			for(Iterator iterator = components.iterator(); iterator.hasNext();) {
				Object component = iterator.next();
				values.add(getFieldValue(component, fieldName.substring(index + 1), request));
			}
			return values;
		}
		
		//其他类型的字段
		return value;
	}
	
	/**
	 * 获取字段默认值
	 * @param field
	 * @param isInit
	 * @param bean
	 * @param request
	 * @param applicationTitle
	 * @return
	 * @throws Exception
	 */
	public static Object getFieldDefaultValue(Field field, boolean isInit, String applicationName, Object bean, HttpServletRequest request) throws Exception {
		String[] defaultValue = {(String)field.getParameter("defaultValue")};
		if(defaultValue[0]==null || defaultValue[0].isEmpty()) {
			return null;
		}
		if("{FIRSTITEM}".equals(defaultValue[0])) { //第一个项目,针对有选项的字段
			List items = listSelectItems(field, bean, request);
			if(items==null || items.isEmpty()) {
				return null;
			}
			String[] values = (String[])items.get(0);
			return values[values.length==1 ? 0 : 1];
		}
		defaultValue[0] = defaultValue[0].replaceAll("\\{CONTEXTPATH\\}", Environment.getContextPath());
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		if(sessionInfo==null) {
			sessionInfo = SessionService.ANONYMOUS_SESSION;
		}
		List values = new ArrayList();
		if("multibox".equals(field.getInputMode())) {
			defaultValue = defaultValue[0].split("\\\\0");
		}
		for(int i=0; i<defaultValue.length; i++) {
			if("string".equals(field.getType())) { //文本,{用户名}|{USERNAME}\0{用户所在单位}|{UNITNAME}\0{用户所在部门}|{DEPARTMENTNAME}\0{编号}|{NUMERATION}
				if("{NUMERATION}".equals(defaultValue[i])) { //编号
					values.add(((NumerationService)Environment.getService("numerationService")).generateNumeration(applicationName, field.getTitle(), (String)field.getParameter("numerationFormat"), isInit && ",none,readonly,".indexOf("," + field.getInputMode() + ",")!=-1, null));
				}
				else {
					values.add(defaultValue[i].replaceAll("\\{USERNAME\\}", sessionInfo.getUserName())
									   		  .replaceAll("\\{UNITNAME\\}", sessionInfo.getUnitName())
									   		  .replaceAll("\\{DEPARTMENTNAME\\}", sessionInfo.getDepartmentName()));
				}
			}
			else if("number".equals(field.getType())) { //数字,{用户ID}|{USERID}\0{用户所在单位ID}|{UNITID}\0{用户所在部门ID}|{DEPARTMENTID}\0{年度}|{YEAR}\0{月份}|{MONTH}\0{日}|{DAY}\0{星期}|{WEEKDAY}\0{小时}|{HOUR}\0{分钟}|{MINUTE}\0{编号}|{NUMERATION}
				if("{USERID}".equals(defaultValue[i])) {
					values.add(new Long(sessionInfo.getUserId()));
				}
				else if("{UNITID}".equals(defaultValue[i])) {
					values.add(new Long(sessionInfo.getUnitId()));
				}
				else if("{DEPARTMENTID}".equals(defaultValue[i])) {
					values.add(new Long(sessionInfo.getDepartmentId()));
				}
				else if("{YEAR}".equals(defaultValue[i])) {
					values.add(new Integer(DateTimeUtils.getYear(DateTimeUtils.date())));
				}
				else if("{MONTH}".equals(defaultValue[i])) {
					values.add(new Integer(DateTimeUtils.getMonth(DateTimeUtils.date()) + 1));
				}
				else if("{DAY}".equals(defaultValue[i])) {
					values.add(new Integer(DateTimeUtils.getDay(DateTimeUtils.date())));
				}
				else if("{WEEKDAY}".equals(defaultValue[i])) { //{WEEKDAY}\0{小时}|{HOUR}\0{分钟}|{MINUTE}\0{编号}|{NUMERATION}
					values.add(new Integer(DateTimeUtils.getWeek(DateTimeUtils.date())));
				}
				else if("{HOUR}".equals(defaultValue[i])) {
					values.add(new Integer(DateTimeUtils.getHour(DateTimeUtils.now())));
				}
				else if("{MINUTE}".equals(defaultValue[i])) {
					values.add(new Integer(DateTimeUtils.getMinute(DateTimeUtils.now())));
				}
				else if("{NUMERATION}".equals(defaultValue[i])) {
					values.add(new Long(((NumerationService)Environment.getService("numerationService")).generateNumeration(applicationName, field.getTitle(), (String)field.getParameter("numerationFormat"), isInit && ",none,readonly,".indexOf("," + field.getInputMode() + ",")!=-1, null)));
				}
				else {
					values.add(defaultValue[i]);
				}
			}
			else if("date".equals(field.getType())) { //日期,{今天}|{TODAY}\0{本月第一天}|{FIRSTDAYOFMONTH}\0{本月最后一天}|{LASTDAYOFMONTH}\0{上月第一天}|{FIRSTDAYOFPREVIOUSMONTH}\0{上月最后一天}|{LASTDAYOFPREVIOUSMONTH}\0{次月第一天}|{FIRSTDAYOFNEXTMONTH}\0{次月最后一天}|{LASTDAYOFNEXTMONTH}\0{今年第一天}|{FIRSTDAYOFYEAR}\0{今年最后一天}|{LASTDAYOFYEAR}\0{本季度第一天}|{FIRSTDAYOFQUARTER}\0{本季度最后一天}|{LASTDAYOFQUARTER}
				if("{TODAY}".equals(defaultValue[i])) {
					values.add(DateTimeUtils.date());
				}
				else if("{FIRSTDAYOFMONTH}".equals(defaultValue[i])) { //本月第一天
					values.add(DateTimeUtils.getMonthBegin());
				}
				else if("{LASTDAYOFMONTH}".equals(defaultValue[i])) { //本月最后一天
					values.add(DateTimeUtils.getMonthEnd());
				}
				else if("{FIRSTDAYOFPREVIOUSMONTH}".equals(defaultValue[i])) { //上月第一天
					values.add(DateTimeUtils.getLastMonthBegin());
				}
				else if("{LASTDAYOFPREVIOUSMONTH}".equals(defaultValue[i])) { //上月最后一天
					values.add(DateTimeUtils.getLastMonthEnd());
				}
				else if("{FIRSTDAYOFNEXTMONTH}".equals(defaultValue[i])) { //次月第一天
					values.add(DateTimeUtils.getNextMonthBegin());
				}
				else if("{LASTDAYOFNEXTMONTH}".equals(defaultValue[i])) { //次月最后一天
					values.add(DateTimeUtils.getNextMonthEnd());
				}
				else if("{FIRSTDAYOFYEAR}".equals(defaultValue[i])) { //今年第一天
					values.add(DateTimeUtils.getYearBegin());
				}
				else if("{LASTDAYOFYEAR}".equals(defaultValue[i])) { //今年最后一天
					values.add(DateTimeUtils.getYearEnd());
				}
				else if("{FIRSTDAYOFLASTYEAR}".equals(defaultValue[i])) { //去年第一天
					values.add(DateTimeUtils.getLastYearBegin());
				}
				else if("{LASTDAYOFLASTYEAR}".equals(defaultValue[i])) { //去年最后一天
					values.add(DateTimeUtils.getLastYearEnd());
				}
				else if("{FIRSTDAYOFNEXTYEAR}".equals(defaultValue[i])) { //明年第一天
					values.add(DateTimeUtils.getNextYearBegin());
				}
				else if("{LASTDAYOFNEXTYEAR}".equals(defaultValue[i])) { //明年最后一天
					values.add(DateTimeUtils.getNextYearEnd());
				}
				else if("{FIRSTDAYOFQUARTER}".equals(defaultValue[i])) { //本季度第一天
					values.add(DateTimeUtils.getQuarterBegin());
				}
				else if("{LASTDAYOFQUARTER}".equals(defaultValue[i])) { //本季度最后一天
					values.add(DateTimeUtils.getQuarterEnd());
				}
				else if("{FIRSTDAYOFLASTQUARTER}".equals(defaultValue[i])) { //上季度第一天
					values.add(DateTimeUtils.getLastQuarterBegin());
				}
				else if("{LASTDAYOFLASTQUARTER}".equals(defaultValue[i])) { //上季度最后一天
					values.add(DateTimeUtils.getLastQuarterEnd());
				}
				else if("{FIRSTDAYOFNEXTQUARTER}".equals(defaultValue[i])) { //下季度第一天
					values.add(DateTimeUtils.getNextQuarterBegin());
				}
				else if("{LASTDAYOFNEXTQUARTER}".equals(defaultValue[i])) { //下季度最后一天
					values.add(DateTimeUtils.getNextQuarterEnd());
				}
				else {
					values.add(DateTimeUtils.parseDate(defaultValue[i], null));
				}
			}
			else if("timestamp".equals(field.getType())) { //时间,{当前时间}|{NOW}\0{今天的00:00:00}|{TODAY 00:00:00}\0{今天的23:59:59}|{TODAY 23:59:59}\0{第二天的00:00:00}|{NEXTDAY 00:00:00}
				if("{NOW}".equals(defaultValue[i])) { //当前时间
					values.add(DateTimeUtils.now());
				}
				else if(defaultValue[i].startsWith("{TODAY")) { //今天的00:00:00
					values.add(DateTimeUtils.parseTimestamp(DateTimeUtils.formatDate(DateTimeUtils.date(), null) + defaultValue[i].substring("{TODAY".length(), defaultValue[i].length()-1), null));
				}
				else if(defaultValue[i].startsWith("{NEXTDAY")) { //第二天的00:00:00
					values.add(DateTimeUtils.parseTimestamp(DateTimeUtils.formatDate(DateTimeUtils.add(DateTimeUtils.date(), Calendar.DAY_OF_MONTH, 1), null) + defaultValue[i].substring("{TODAY".length(), defaultValue[i].length()-1), null));
				}
				else {
					values.add(DateTimeUtils.parseTimestamp(defaultValue[i], null));
				}
			}
			else {
				values.add(defaultValue[i]);
			}
		}
		return "multibox".equals(field.getInputMode()) ? values.toArray() : (values.isEmpty() ? null : values.get(0));
	}
	
	/**
	 * 格式化字段值
	 * @param fieldValue
	 * @param fieldDefine
	 * @param bean POJO或者ActionForm
	 * @param readonly
	 * @param displayFormat 字段输出格式,空时使用系统默认的格式
	 * @param htmlEncode 是否对HTML特殊字符编码,如"<"、">"、"&"
	 * @param filterHtml 是否需要过滤掉HTML元素
	 * @param hideOpinionPersonName 是否隐藏意见填写人,只对opinion字段有效
	 * @param componentCount 显示的集合元素个数,只对components字段有效
	 * @param componentSeparator 集合元素分隔符,只对components字段有效
	 * @param componentEllipsis 省略符,只对components字段有效
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String formatFieldValue(Object fieldValue, Field fieldDefine, Object bean, boolean readonly, String displayFormat, boolean htmlEncode, boolean filterHtml, boolean hideOpinionPersonName, int componentCount, String componentSeparator, String componentEllipsis, HttpServletRequest request) throws Exception {
		if(fieldValue==null) { //值为空
            return "";
        }
		if(fieldDefine==null) {
			fieldDefine = new Field();
			fieldDefine.setInputMode("readonly");
			fieldDefine.setName("unknown");
		}
		readonly = (readonly ? readonly : "readonly".equals(fieldDefine.getInputMode())); //输入模式为readonly,强制按只读方式输出
		
		//附件、视频、图片
		if("attachment".equals(fieldDefine.getType()) || "video".equals(fieldDefine.getType()) || "image".equals(fieldDefine.getType())) {
			if(!readonly) { //编辑时,不返回任何值
				return "";
			}
			List attachments = (List)fieldValue;
			String html = null;
			for(int i=0; i<attachments.size(); i++) {
				Attachment attachment = (Attachment)attachments.get(i);
				//图片,TODO 显示为图片或者缩略图,由此必须增加field.parameter,指定缩略图的大小
				if("image".equals(fieldDefine.getType())) {
					
				}
				html = (html==null ? "" : html + "&nbsp;") +
					   "<a href=\"" + attachment.getUrlInline() + "\" target=\"_blank\">" +
					   (attachments.size()>1 ? (i+1) + "、" : "") + TagUtils.getInstance().filter(attachment.getTitle()) +
					   "</a>";
			}
			return html==null ? "" : html;
		}

		//意见
		if("opinion".equals(fieldDefine.getType())) {
			if(!readonly) { //不允许编辑
				return "";
			}
			List opinions = (List)fieldValue;
			String opinionHtml = null;
			for(Iterator iterator = opinions.iterator(); iterator.hasNext();) {
				Opinion opinion = (Opinion)iterator.next();
				String personName = opinion.getPersonName();
				if(opinion.getAgentName()!=null) {
					personName += "(" + opinion.getAgentName() + "代)";
				}
				if(filterHtml) {
					opinionHtml = (opinionHtml==null ? "" : opinionHtml + "\r\n\r\n") + opinion.getOpinion() + "                  " + (!hideOpinionPersonName ? personName + "&nbsp;" : "") + DateTimeUtils.formatTimestamp(opinion.getCreated(), "yyyy-MM-dd HH:mm");
				}
				else {
					opinionHtml = (opinionHtml==null ? "" : opinionHtml + "<br/>") + 
								  "<p class=\"opinionContent\" id=\"opinionContent\">" +
								  StringUtils.generateHtmlContent(opinion.getOpinion()) +
								  "</p>" +
								  "<div class=\"opinionAuthor\" style=\"text-align:right; padding-right:50px; padding-top:10px\">" +
								  (!hideOpinionPersonName ? "<span id=\"opinionAuthor\">" + personName + "</span>" + "&nbsp;" : "") + "<span id=\"opinionCreated\">" + DateTimeUtils.formatTimestamp(opinion.getCreated(), "yyyy-MM-dd HH:mm") + "</span>" +
								  "</div>";
				}
			}
			return opinionHtml;
		}
		
		//组成部分, 只读
		if(fieldValue instanceof List) {
			if(!readonly) { //不允许编辑
				return "";
			}
			String output = null;
			Iterator iterator = ((List)fieldValue).iterator();
			for(int count = 0; iterator.hasNext() && (componentCount==0 || count<componentCount); count++) {
				Object value = iterator.next();
				if(value==null || value.equals("")) {
					continue;
				}
				value = formatFieldValue(value, fieldDefine, bean, readonly, displayFormat, htmlEncode, filterHtml, hideOpinionPersonName, componentCount, componentSeparator, componentEllipsis, request);
				output = (output==null ? "" : output + (componentSeparator==null ? "," : componentSeparator)) + value;
			}
			if(output==null) {
				return "";
			}
			if(iterator.hasNext() && componentEllipsis!=null) {
				output +=  componentEllipsis;
			}
			return output;
		}
		
		if(fieldValue instanceof Image) { //图片
			Image image = (Image)fieldValue;
			if(!readonly) { //编辑
				return image.getName();
			}
			return "<img border=\"0\" src=\"" + image.getUrl() + "\"/>";
		}
		
		if(fieldValue instanceof Video) { //视频
			Video video = (Video)fieldValue;
			if(!readonly) { //编辑
				return video.getName();
			}
			long siteId = RequestUtils.getParameterLongValue(request, "siteId");
			String html = "";
			if(request!=null && request.getAttribute("VideoPlayerSupport")==null) {
				html = "<script type=\"text/javascript\" src=\"" + Environment.getContextPath() + "/jeaf/video/player/js/videoplayer.js\"></script>\r\n" +
					   "<script type=\"text/javascript\" src=\"" + Environment.getContextPath() + "/jeaf/common/js/progressbar.js\"></script>\r\n" +
					   "<link href=\"" + Environment.getContextPath() + "/jeaf/video/player/css/videocontroller.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n";
				request.setAttribute("VideoPlayerSupport", Boolean.TRUE);
			}
			return html + "<script>" + VideoPlayerUtils.generateVideoPlayerScript(video.getApplicationName(), video.getRecordId(), video.getType(), video.getName(), 400, 300, false, false, siteId, request) + "</script>";
		}
		
		//时间
		if(fieldValue instanceof Timestamp) {
			String format = null;
			if(!readonly) { //不是只读
				format = "yyyy-MM-dd HH:mm:ss";
			}
			else if(displayFormat!=null) { //只读
				format = displayFormat;
			}
			else if((displayFormat=(String)fieldDefine.getParameter("displayFormat"))!=null) {
				format = displayFormat;
			}
			else {
				format = "yyyy-MM-dd HH:mm";
			}
			return DateTimeUtils.formatTimestamp((Timestamp)fieldValue, format);
		}
		
		//日期
		if(fieldValue instanceof Date) {
			String format = null;
			if(!readonly) { //不是只读
				format = "yyyy-MM-dd";
			}
			else if(displayFormat!=null) { //只读
				format = displayFormat;
			}
			else if((displayFormat=(String)fieldDefine.getParameter("displayFormat"))!=null) {
				format = displayFormat;
			}
			else {
				format = "yyyy-MM-dd";
			}
			return DateTimeUtils.formatDate((Date)fieldValue, format);
		}
		
		//HTML, 只读
		if("html".equals(fieldDefine.getType())) {
			if(readonly) {
				if(filterHtml) {
					return StringUtils.filterHtmlElement((String)fieldValue, false);
				}
				else {
					return generateVideoHtml((String)fieldValue, true, request); //替换视频
				}
			}
			fieldValue = generateVideoHtml((String)fieldValue, false, request); //替换视频
		}
		
		String value;
		if(!(fieldValue instanceof Number)) { //不是数字
			if(!htmlEncode) {
				value = fieldValue.toString();
			}
			else if(readonly && !filterHtml) {
				value = StringUtils.generateHtmlContent(fieldValue.toString());
			}
			else {
				value = TagUtils.getInstance().filter(fieldValue.toString());
			}
		}
		else if(request!=null && request.getParameter(fieldDefine.getName())!=null && request.getParameter(fieldDefine.getName()).isEmpty()) { //请求参数有该字段,且值为空
			value = "";
		}
		else if(!readonly && //不是只读
				((Number)fieldValue).doubleValue()==0 && //值为0
				"text".equals(fieldDefine.getInputMode()) &&
				bean!=null && (bean instanceof ActionForm) && (((ActionForm)bean).getAct()==null || "create".equals(((ActionForm)bean).getAct()) || "createComponent".equals(((ActionForm)bean).getAct())) && //新记录
				request!=null && 
				request.getParameter(fieldDefine.getName())==null) { //请求参数没有该值
			value = "";
		}
		else { //数字
			String format = null;
			if(readonly) { //只读
				format = displayFormat==null ? (String)fieldDefine.getParameter("displayFormat") : displayFormat;
			}
			if(format==null) {
				if(fieldDefine.getLength()==null) {
					format = "#.####################";
				}
				else {
					int index = fieldDefine.getLength().indexOf(",");
					format = "#";
					if(index!=-1) {
						format += ".";
						for(int i=Integer.parseInt(fieldDefine.getLength().substring(index + 1)); i>0; i--) {
							format += "#";
						}
					}
				}
			}
			value = new DecimalFormat(format).format(fieldValue);
		}
		
		//单个复选框, 只读
		if(readonly && "checkbox".equals(fieldDefine.getInputMode())) {
			return (fieldDefine.getParameter("value").equals(value) ? (String)fieldDefine.getParameter("label") : "");
		}
	
		//单选框/下拉列表, 只读
		if(readonly && ("radio".equals(fieldDefine.getInputMode()) || ("dropdown".equals(fieldDefine.getInputMode())) && fieldDefine.getParameter("valueField")==null && fieldDefine.getParameter("titleField")==null)) {
			List selectItems = null;
			try {
				selectItems = listSelectItems(fieldDefine, bean, request);
			}
			catch(Exception e) {
				
			}
			for(Iterator iterator=(selectItems==null ? null : selectItems.iterator()); iterator!=null && iterator.hasNext();) {
				String[] itemValues = (String[])iterator.next();
				if(itemValues[1].equals(value)) {
					return itemValues[0];
				}
			}
			return value;
		}
		
		//多选框, 只读
		if(readonly && "multibox".equals(fieldDefine.getInputMode())) {
			String values = null;
			List selectItems = listSelectItems(fieldDefine, bean, request);
			Object[] multiboxValues = (Object[])fieldValue;
			for(int i=0; i<multiboxValues.length; i++) {
				for(Iterator iterator=(selectItems==null || multiboxValues[i]==null ? null : selectItems.iterator()); iterator!=null && iterator.hasNext();) {
					String[] itemValues = (String[])iterator.next();
					if(itemValues[1].equals(multiboxValues[i].toString())) {
						values = (values==null ? "" : values + ", ") + itemValues[0];
						break;
					}
				}
			}
			return values;
		}
		return value;
	}
	
	/**
	 * 生成视频HTML
	 * @param html
	 * @param readonly
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static String generateVideoHtml(String html, boolean readonly, HttpServletRequest request) throws Exception {
		Pattern pattern = Pattern.compile("<img[^>]*id=['\"]?video[^>]*>", Pattern.CASE_INSENSITIVE);
		String newHtml = "";
		Matcher match = pattern.matcher(html);
		while(match.find()) {
			//生成视频HTML
			String videoHtml = match.group();
			String properties = StringUtils.getHtmlElementAttribute(videoHtml, "alt");
	    	String applicationName = StringUtils.getPropertyValue(properties, "applicationName");
			long recordId = StringUtils.getPropertyLongValue(properties, "recordId", 0);
			String videoName = StringUtils.getPropertyValue(properties, "videoName");
			String videoType = StringUtils.getPropertyValue(properties, "videoType");
			int width = StringUtils.getPropertyIntValue(properties, "width", 0);
			int height = StringUtils.getPropertyIntValue(properties, "height", 0);
			boolean autoStart = "true".equals(StringUtils.getPropertyValue(properties, "autoStart"));
			if(!readonly) { //编辑状态
				//获取视频服务
				VideoService videoService = (VideoService)FieldUtils.getAttachmentService(applicationName, videoType, recordId);
				//获取预览图URL
				String previewImageUrl = videoService==null ? null : videoService.getBreviaryImage(applicationName, videoType, recordId, videoName, 0, 0, request).getUrl();
				//更新URL
				if(previewImageUrl!=null) {
					videoHtml = videoHtml.replaceFirst("(?i)(src=['\"]?)[^'\"\\s>]*", "$1" + previewImageUrl);
				}
			}
			else { //只读
				videoHtml = "";
				if(request!=null && request.getAttribute("VideoPlayerSupport")==null) {
					videoHtml = "<script type=\"text/javascript\" src=\"" + Environment.getContextPath() + "/jeaf/video/player/js/videoplayer.js\"></script>\r\n" +
						   		"<script type=\"text/javascript\" src=\"" + Environment.getContextPath() + "/jeaf/common/js/progressbar.js\"></script>\r\n" +
						   		"<link href=\"" + Environment.getContextPath() + "/jeaf/video/player/css/videocontroller.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n";
					request.setAttribute("VideoPlayerSupport", Boolean.TRUE);
				}
				long siteId = RequestUtils.getParameterLongValue(request, "siteId");
				videoHtml += "<script>" + VideoPlayerUtils.generateVideoPlayerScript(applicationName, recordId, videoType, videoName, width, height, autoStart, false, siteId, request) + "</script>\r\n";
			}
			newHtml += html.substring(0, match.start()) + videoHtml;
			html = html.substring(match.end());
			match = pattern.matcher(html);
		}
		return newHtml + html;
	}
	
	/**
	 * 获取应用名称
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	private static String getApplicationName(Object bean) throws Exception {
		if(bean instanceof ActionForm) {
			return ((ActionForm)bean).getFormDefine().getApplicationName();
		}
		else if(bean instanceof Form) {
			return ((Form)bean).getApplicationName();
		}
		else if(bean instanceof SqlResult) {
			String sqlRecordName = (String)((SqlResult)bean).get("sqlRecordName"); //主记录名称
			return getBusinessDefineService().getBusinessObject(sqlRecordName).getApplicationName();
		}
		//记录
		return getBusinessDefineService().getBusinessObject(bean.getClass()).getApplicationName();
	}
	
	/**
	 * 根据附件类型获取附件字段
	 * @param applicationName
	 * @param attachmentType
	 * @param recordId
	 * @return
	 * @throws ServiceException
	 */
	public static Field getAttachmentField(String applicationName, String attachmentType, long recordId) throws ServiceException {
		List businessObjects = getBusinessDefineService().listBusinessObjects(applicationName);
		for(Iterator iterator = businessObjects==null ? null : businessObjects.iterator(); iterator!=null && iterator.hasNext();) {
			BusinessObject businessObject = (BusinessObject)iterator.next();
			Field field = businessObject.getField(attachmentType);
			if(field==null || (!"attachment".equals(field.getType()) && !"image".equals(field.getType()) && !"video".equals(field.getType()))) {
				continue;
			}
			AttachmentService attachmentService = getAttachmentService(field);
			String attachmentSavePath = attachmentService.getSavePath(applicationName, attachmentType, recordId, false);
			if(FileUtils.isExists(attachmentSavePath)) { //记录对应的附件类型存在
				return field;
			}
		}
		List forms = getFormDefineService().listFormDefines(applicationName);
		for(Iterator iterator = forms==null ? null : forms.iterator(); iterator!=null && iterator.hasNext();) {
			Form form = (Form)iterator.next();
			Field field = form.getField(attachmentType);
			if(field==null || (!"attachment".equals(field.getType()) && !"image".equals(field.getType()) && !"video".equals(field.getType()))) {
				continue;
			}
			AttachmentService attachmentService = getAttachmentService(field);
			String attachmentSavePath = attachmentService.getSavePath(applicationName, attachmentType, recordId, false);
			if(FileUtils.isExists(attachmentSavePath)) { //记录对应的附件类型存在
				return field;
			}
		}
		return null;
	}
	
	/**
	 * 根据附件类型获取附件服务
	 * @param applicationName
	 * @param attachmentType
	 * @param recordId
	 * @return
	 * @throws ServiceException
	 */
	public static AttachmentService getAttachmentService(String applicationName, String attachmentType, long recordId) throws ServiceException {
		Field field = getAttachmentField(applicationName, attachmentType, recordId);
		if(field==null) {
			return null;
		}
		String serviceName = (String)field.getParameter("serviceName");
		return (AttachmentService)Environment.getService(serviceName==null ? field.getType() + "Service" : serviceName);
	}
	
	/**
	 * 获取附件服务
	 * @param attachmentFieldDefine
	 * @return
	 * @throws ServiceException
	 */
	private static AttachmentService getAttachmentService(Field attachmentFieldDefine) throws ServiceException {
		String serviceName = (String)attachmentFieldDefine.getParameter("serviceName");
		return (AttachmentService)Environment.getService(serviceName==null ? attachmentFieldDefine.getType() + "Service" : serviceName);
	}
	
	/**
	 * 获取附件目录
	 * @param record
	 * @param request
	 * @param fieldName
	 * @param mkdir
	 * @return
	 * @throws ServiceException
	 */
	public static String getAttachmentSavePath(Record record, HttpServletRequest request, String fieldName, boolean mkdir) throws ServiceException {
		BusinessObject businessObject = getBusinessDefineService().getBusinessObject(record.getClass());
		Field fieldDefine = getRecordField(record.getClass().getName(), fieldName, request);
		String attachmentType = FieldUtils.getAttachmentType(record, request, fieldDefine);
		AttachmentService attachmentService = getAttachmentService(fieldDefine);
		return attachmentService.getSavePath(businessObject.getApplicationName(), attachmentType, record.getId(), mkdir);
	}
	
	/**
	 * 获取附件类型
	 * @param record
	 * @param request
	 * @param fieldName
	 * @return
	 */
	public static String getAttachmentType(Record record, HttpServletRequest request, String fieldName) {
		Field fieldDefine = getRecordField(record.getClass().getName(), fieldName, request);
		return getAttachmentType(record, request, fieldDefine);
	}
	
	/**
	 * 获取附件类型
	 * @param form
	 * @param request
	 * @param fieldDefine
	 * @return
	 */
	public static String getAttachmentType(Object record, HttpServletRequest request, Field fieldDefine) {
		String attachmentType = (String)fieldDefine.getParameter("type");
		if(attachmentType!=null) {
			return StringUtils.fillParameters(attachmentType, false, false, false, "utf-8", record, request, null);
		}
		attachmentType = fieldDefine.getName();
		int index = attachmentType.lastIndexOf(".");
		if(index!=-1) {
			attachmentType = attachmentType.substring(index + 1);
		}
		return attachmentType;
	}
	
	/**
	 * 获取选择项列表,返回String[]列表,格式{label, value}
	 * @param fieldDefine
	 * @param bean
	 * @param request
	 * @return
	 */
	public static List listSelectItems(Field fieldDefine, Object bean, HttpServletRequest request) {
		try {
			List selectItems = (List)fieldDefine.getParameter("SELECTITEMS");
			if(selectItems!=null) {
				return selectItems.isEmpty() ? null : selectItems;
			}
			selectItems = new ArrayList();
			String itemsText = (String)fieldDefine.getParameter("itemsText");
			if(itemsText!=null) { //直接指定选择项
				String[] values = StringUtils.fillParameters(itemsText, false, false, false, "utf-8", bean, request, null).split("\\\\0");
				for(int i=0; i<values.length; i++) {
					String[] itemValues = values[i].split("\\x7c");
					selectItems.add(new String[]{itemValues[0].trim(), (itemValues.length>1 ? itemValues[1] : itemValues[0]).trim()});
				}
			}
			else {
				List items = null;
				String itemsHql;
				String itemsSql;
				String itemsServiceName = (String)fieldDefine.getParameter("itemsServiceName");
				if((itemsHql = (String)fieldDefine.getParameter("itemsHql"))!=null) { //根据itemsHql获取条目列表
					itemsHql = StringUtils.fillParameters(itemsHql, false, false, true, "utf-8", bean, request, null);
					items = (List)request.getAttribute("items_" + itemsHql);
					if(items==null) {
						if(databaseService==null) {
							databaseService = (DatabaseService)Environment.getService("databaseService");
						}
						items = databaseService.findRecordsByHql(itemsHql);
						request.setAttribute("items_" + itemsHql, items);
					}
				}
				else if((itemsSql = (String)fieldDefine.getParameter("itemsSql"))!=null) { //根据itemsSql获取条目列表
					itemsSql = StringUtils.fillParameters(itemsSql, false, false, true, "utf-8", bean, request, null);
					items = (List)request.getAttribute("items_" + itemsSql);
					if(items==null) {
						SqlQueryService sqlQueryService = (SqlQueryService)Environment.getService(itemsServiceName==null ? "sqlQueryService" : itemsServiceName);
						items = sqlQueryService.listSelectItems(null, bean, fieldDefine, request, RequestUtils.getSessionInfo(request));
						request.setAttribute("items_" + itemsSql, items);
					}
				}
				else if(itemsServiceName!=null) { //按条目服务获取
					String itemsName = (String)fieldDefine.getParameter("itemsName");
					items = ((BusinessService)Environment.getService(itemsServiceName)).listSelectItems((itemsName==null ? fieldDefine.getName() : itemsName), bean, fieldDefine, request, RequestUtils.getSessionInfo(request));
				}
				for(Iterator iterator = items==null ? null : items.iterator(); iterator!=null && iterator.hasNext();) {
					Object item = iterator.next();
					if(item==null) {
						continue;
					}
					if(!(item instanceof Object[])) { //不是数组
						selectItems.add(new String[]{item.toString(), item.toString()});
					}
					else {
						Object[] itemValues = (Object[])item;
						if(itemValues[0]!=null && (itemValues.length==1 || itemValues[0]!=null)) {
							selectItems.add(new String[]{itemValues[0].toString(), (itemValues.length>1 ? itemValues[1].toString() : itemValues[0].toString())});
						}
					}
				}
			}
			if(!"false".equals(fieldDefine.getParameter("itemsCacheable"))) { //条目是否允许缓存
				fieldDefine.setParameter("SELECTITEMS", selectItems);
			}
			return selectItems.isEmpty() ? null : selectItems;
		}
		catch(Exception e) {
			throw new Error(e);
		}
	}
	
	/**
	 * 获取选项列表文本
	 * @param fieldDefine
	 * @param bean
	 * @param request
	 * @return
	 */
	public static String getSelectItemsText(Field fieldDefine, Object bean, HttpServletRequest request) {
		String itemsText = (String)fieldDefine.getParameter("itemsText");
		if(itemsText!=null) { //直接指定选择项
			return StringUtils.fillParameters(itemsText, false, false, false, "utf-8", bean, request, null);
		}
		List items = listSelectItems(fieldDefine, bean, request);
		itemsText = "";
		//转换为itemsText
		for(Iterator iterator = (items==null ? null : items.iterator()); iterator!=null && iterator.hasNext();) {
			String[] itemValues = (String[])iterator.next();
			itemsText += (itemsText.equals("") ? "" : "\\0 ") + itemValues[0] + "|" + itemValues[1];
		}
		return itemsText;
	}
	
	/**
	 * 获取条目对应的标题
	 * @param itemsText
	 * @param itemValue
	 * @return
	 */
	public static String getItemTitle(String itemsText, String itemValue) {
		if(itemValue==null || itemValue.isEmpty()) { 
			return null;
		}
		String[] items = itemsText.split("\\\\0");
		for(int i=0; i<items.length; i++) {
			items[i] = items[i].trim();
			if(items[i].equals(itemValue)) {
				return itemValue;
			}
			if(items[i].endsWith("|" + itemValue)) {
				return items[i].substring(0, items[i].indexOf('|')).trim();
			}
		}
		return null;
	}
	
	/**
	 * 获取选择对话框执行的脚本
	 * @param fieldDefine
	 * @param bean
	 * @param columnId
	 * @param siteId
	 * @param sitePage
	 * @param request
	 * @return
	 */
	public static String getSelectDialogScript(Field fieldDefine, Object bean, HttpServletRequest request) {
		String execute = (String)fieldDefine.getParameter("execute"); //用户选择|{SELECTPERSON}\0{部门选择}|{SELECTDEPARTMENT}\0{单位选择}|{SELECTUNIT}
		boolean multiSelect = "true".equals(fieldDefine.getParameter("multiSelect")); //是否多选
		String separator = (String)fieldDefine.getParameter("separator"); //分隔符
		String param = fieldDefine.getName() + "{name}";
		if(multiSelect) {
			param += "," + fieldDefine.getName() + "{name|" + fieldDefine.getTitle() + "|100%}";
		}
		if("{SELECTPERSON}".equals(execute)) { //用户选择
			return "DialogUtils.selectPerson(550, 350, " + multiSelect + ", '" + param + "', '', '', '', '" + (separator==null || separator.isEmpty() ? "," : separator) + "')";
		}
		else if("{SELECTDEPARTMENT}".equals(execute) || "{SELECTUNIT}".equals(execute)) { //部门选择,单位选择
			String orgTypes = "{SELECTDEPARTMENT}".equals(execute) ? "unitDepartment" : "unit";
			return "DialogUtils.selectOrg(550, 350, " + multiSelect + ", '" + param + "', '', '" + orgTypes + "', '', '" + (separator==null || separator.isEmpty() ? "," : separator) + "')";
		}
		return StringUtils.fillParameters(execute, false, true, false, "utf-8", bean, request, null);
	}

	/**
	 * 获取表单字段列表
	 * @param formDefine
	 *  @param includeTypes 需要包含的字段类型
	 * @param excludeTypes 需要排除的字段类型
	 * @param includeInputModes 需要包含的输入方式
	 * @param excludeInputModes 需要排除的输入方式
	 * @param keepComponentField 是否保留组件字段
	 * @param listReadonlyComponentFields 是否获取只读组件的字段
	 * @param listComponentsField 是否获取组件列表的字段
	 * @param persistenceOnly 是否只获取持久字段
	 * @param deep 获取的深度,0表示无限制
	 * @param deep
	 * @return
	 */
	public static List listFormFields(Form formDefine, String includeTypes, String excludeTypes, String includeInputModes, String excludeInputModes, boolean keepComponentField, boolean listReadonlyComponentFields, boolean listComponentsField, boolean persistenceOnly, int deep) {
		try {
			if(formDefine==null) {
				return null;
			}
			List fields = new ArrayList();
			List parentClassNames = new ArrayList();
			parentClassNames.add(formDefine.getClassName());
			List formFields = formDefine.getFields()==null ? new ArrayList() : new ArrayList(formDefine.getFields()); //获取表单自己定义的字段
			if(formDefine.getRecordClassName()!=null) { //有配置记录类
				parentClassNames.add(formDefine.getRecordClassName());
				List businessFields = getBusinessDefineService().getBusinessObject(formDefine.getRecordClassName()).getFields();
				for(Iterator iterator = businessFields==null ? null : businessFields.iterator(); iterator!=null && iterator.hasNext();) {
					Field field = (Field)iterator.next();
					if(ListUtils.findObjectByProperty(formFields, "name", field.getName())==null) {
						formFields.add(field);
					}
				}
			}
			addFields(fields, formFields, null, parentClassNames, includeTypes, excludeTypes, includeInputModes, excludeInputModes, keepComponentField, listReadonlyComponentFields, listComponentsField, persistenceOnly, 1, deep);
			return fields;
		}
		catch (Exception e) {
			Logger.exception(e);
			return null;
		}
	}
	
	/**
	 * 获取记录的字段列表
	 * @param recordClassName 记录类名称
	 * @param includeTypes 需要包含的字段类型
	 * @param excludeTypes 需要排除的字段类型
	 * @param includeInputModes 需要包含的输入方式
	 * @param excludeInputModes 需要排除的输入方式
	 * @param keepComponentField 是否保留组件字段
	 * @param listReadonlyComponentFields 是否获取只读组件的字段
	 * @param listComponentsFields 是否获取组件列表的字段
	 * @param persistenceOnly 是否只获取持久字段
	 * @param deep 获取的深度,0表示无限制
	 * @return
	 */
	public static List listRecordFields(String recordClassName, String includeTypes, String excludeTypes, String includeInputModes, String excludeInputModes, boolean keepComponentField, boolean listReadonlyComponentFields, boolean listComponentsFields, boolean persistenceOnly, int deep) {
		try {
			List fields = new ArrayList();
			BusinessObject businessObject = getBusinessDefineService().getBusinessObject(recordClassName);
			List parentClassNames = new ArrayList();
			parentClassNames.add(recordClassName);
			addFields(fields, businessObject.getFields(), null, parentClassNames, includeTypes, excludeTypes, includeInputModes, excludeInputModes, keepComponentField, listReadonlyComponentFields, listComponentsFields, persistenceOnly, 1, deep);
			return fields;
		}
		catch (Exception e) {
			Logger.error("FieldUtils: get fields of " + recordClassName + " failed.");
			Logger.exception(e);
			return null;
		}
	}
	
	/**
	 * 将字段列表按输入方式排序
	 * @param fields
	 * @param lowPriorityInputModes
	 */
	public static void sortFieldsByInputMode(List fields, String lowPriorityInputModes) {
		if(fields==null) {
			return;
		}
		final String inputModes = "," + lowPriorityInputModes + ",";
		Collections.sort(fields, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				int index0 = inputModes.indexOf("," + ((Field)arg0).getInputMode() + ",");
				int index1 = inputModes.indexOf("," + ((Field)arg1).getInputMode() + ",");
				return index0==index1 ? 0 : (index0<index1 ? -1 : 1);
			}
		});
	}
	
	/**
	 * 添加字段
	 * @param fields
	 * @param toAddFields
	 * @param parentField
	 * @param parentClassNames
	 * @param includeTypes
	 * @param excludeTypes
	 * @param includeInputModes
	 * @param excludeInputModes
	 * @param keepComponentField
	 * @param listReadonlyComponentFields
	 * @param listComponentsFields
	 * @param persistenceOnly
	 * @param level
	 * @param deep
	 */
	private static void addFields(List fields, List toAddFields, Field parentField, List parentClassNames, String includeTypes, String excludeTypes, String includeInputModes, String excludeInputModes, boolean keepComponentField, boolean listReadonlyComponentFields, boolean listComponentsFields, boolean persistenceOnly, int level, int deep) {
		if(toAddFields==null) {
			return;
		}
		for(Iterator iterator = toAddFields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(field.getType()==null) {
				field.setType("string");
			}
			if(persistenceOnly && !field.isPersistence()) { //不是持久字段
				continue;
			}
			if(parentField!=null) {
				field.setName(parentField.getName() + "." + field.getName());
				field.setTitle(parentField.getTitle().equals(field.getTitle()) ? parentField.getTitle() : parentField.getTitle() + "_" + field.getTitle());
			}
			boolean isComponent = "component".equals(field.getType()); //组成部分
			boolean isComponents = "components".equals(field.getType()); //组成部分列表
			String componentClassName;
			int fieldIndex = fields.size();
			if((isComponent || isComponents) && //组成部分
			   (deep==0 || level<deep) && //深度未超过
			   (!isComponent || listReadonlyComponentFields || !"readonly".equals(field.getInputMode())) && //允许获取只读组成部分的字段、或者不是只读的字段
			   (!isComponents || listComponentsFields) && //需要获取组成部分列表的字段
			   (componentClassName=(String)field.getParameter("class"))!=null && //组件类名称不为空
			   parentClassNames.indexOf(componentClassName)==-1) { //不是父类
				try {
					//添加组成部分的字段列表
					BusinessObject componentBusinessObject = getBusinessDefineService().getBusinessObject(componentClassName);
					if(componentBusinessObject==null) {
						Logger.error("Business object is not found, class name is " + componentClassName + ".");
					}
					List classNames = new ArrayList(parentClassNames);
					classNames.add(componentClassName);
					addFields(fields, componentBusinessObject.getFields(), field, classNames, includeTypes, excludeTypes, includeInputModes, excludeInputModes, keepComponentField, listReadonlyComponentFields, listComponentsFields, persistenceOnly, level+1, deep);
				}
				catch (Exception e) {
					
				}
			}
			//处理包含的字段类型
			if(includeTypes!=null && ("," + includeTypes + ",").indexOf("," + field.getType().replaceAll("\\[\\]", "") + ",")==-1) {
				continue;
			}
			//处理排除的字段类型
			if(excludeTypes!=null && ("," + excludeTypes + ",").indexOf("," + field.getType().replaceAll("\\[\\]", "") + ",")!=-1) {
				continue;
			}
			if(field.getInputMode()!=null) {
				//处理包含的输入方式
				if(includeInputModes!=null && ("," + includeInputModes + ",").indexOf("," + field.getInputMode() + ",")==-1) {
					continue;
				}
				//处理排除的输入方式
				if(excludeInputModes!=null && ("," + excludeInputModes + ",").indexOf("," + field.getInputMode() + ",")!=-1) {
					continue;
				}
			}
			if(((!isComponent && !isComponents) || keepComponentField) && ListUtils.findObjectByProperty(fields, "name", field.getName())==null) { //不是组成部分、或者保持组成部分的字段
				fields.add(fieldIndex, field);
			}
		}
	}
	
	/**
	 * 获取表单定义服务
	 * @return
	 */
	private static FormDefineService getFormDefineService() {
		if(formDefineService==null) {
			try {
				formDefineService = (FormDefineService)Environment.getService("formDefineService");
			}
			catch (ServiceException e) {
				
			}
		}
		return formDefineService;
	}
	
	/**
	 * 获取业务逻辑定义服务
	 * @return
	 */
	private static BusinessDefineService getBusinessDefineService() {
		if(businessDefineService==null) {
			try {
				businessDefineService = (BusinessDefineService)Environment.getService("businessDefineService");
			}
			catch (ServiceException e) {
				
			}
		}
		return businessDefineService;
	}

	/**
	 * 获取字段输入时的长度
	 * @param field
	 * @return
	 */
	public static int getFieldInputLength(Field field) {
		if(field.getLength()==null) {
			return 0;
		}
		int index = field.getLength().indexOf(',');
        int length = index==-1 ? Integer.parseInt(field.getLength()) : (Integer.parseInt(field.getLength().substring(0, index)) + 1);
        if(",string,password,email,phone,html,imageName,videoName,attachmentName,".indexOf("," + field.getType().replaceAll("\\[\\]", "") + ",")!=-1 && //字符串
           !"true".equals(field.getParameter("singleByteCharacters"))) { //不是单字节
        	length = length/2;
		}
    	return length;
    }
}