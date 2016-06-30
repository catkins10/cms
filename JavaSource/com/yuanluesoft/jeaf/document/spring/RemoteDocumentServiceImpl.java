package com.yuanluesoft.jeaf.document.spring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.document.RemoteDocumentService;
import com.yuanluesoft.jeaf.document.callback.ProcessWordDocumentCallback;
import com.yuanluesoft.jeaf.document.model.RecordListChange;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class RemoteDocumentServiceImpl implements RemoteDocumentService {
	private FormDefineService formDefineService; //表单定义服务
	private TemporaryFileManageService temporaryFileManageService; //临时文件管理服务
	private AttachmentService attachmentService; //附件服务
	private Cache cache; //缓存,存放远程文档处理任务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.document.RemoteDocumentService#writeRemoteDocument(java.lang.String, java.lang.String, java.util.Map, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.ActionForm, java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeRemoteDocument(String documentApplicationName, String documentCommand, Map documentCommandParameter, String applicationName, String formName, ActionForm form, List documentFiles, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException, IOException {
		//生成任务ID
		String taskId = UUIDStringGenerator.generateId();
		//生成并输出ini文件
		String iniFilePath = generateIniFile(taskId, documentApplicationName, documentCommand, documentCommandParameter, applicationName, formName, form, documentFiles, request, sessionInfo);
		//生成文档ID
		long documentId = UUIDLongGenerator.generateId();
		try {
			cache.put(taskId, new Object[]{new Long(documentId), iniFilePath, documentFiles});
		} 
		catch (CacheException e) {
			throw new ServiceException(e);
		}
		String downloadURL = RequestUtils.getServerURL(request) + request.getContextPath() + "/jeaf/document/downloadRemoteDocument.shtml?taskId=" + taskId;
		String uploadPassportURL = RequestUtils.getServerURL(request) + request.getContextPath() + "/jeaf/document/attachmentEditor.shtml" +
					 			   "?taskId=" + taskId + "&attachmentSelector.action=passport&attachmentSelector.field=document&attachmentSelector.type=document";
		String uploadServerURL = RequestUtils.getServerURL(request) + request.getContextPath() + "/fileUpload";
		String html = "<html><body><script>parent.setTimeout('window.onRemoteDocumentReady(\"" + taskId + "\", \"" + downloadURL + "\", \"" + uploadPassportURL + "\", \"" + uploadServerURL + "\", \"" + documentApplicationName + "\")', 1);</script></body></html>";
		response.getWriter().print(html);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.document.RemoteDocumentService#getRemoteDocumentId(java.lang.String)
	 */
	public long getRemoteDocumentId(String taskId) throws ServiceException {
		try {
			Object[] values = (Object[])cache.get(taskId);
			return ((Number)values[0]).longValue();
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.document.RemoteDocumentService#downloadRemoteDocument(java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void downloadRemoteDocument(String taskId, HttpServletResponse response) throws ServiceException, IOException {
		String iniFilePath = null;
		List documentFiles = null;
		try {
			Object[] values = (Object[])cache.get(taskId);
			iniFilePath = (String)values[1];
			documentFiles = (List)values[2];
			values[1] = null;
			values[2] = null;
			cache.put(taskId, values);
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
		//获取输出流
		OutputStream outputStream = response.getOutputStream();
		//输出标志
		writeInt(20050718, outputStream);
		//获取文件总长度
		int totalLength = (int)new File(iniFilePath).length();
		for(Iterator iterator = documentFiles==null ? null : documentFiles.iterator(); iterator!=null && iterator.hasNext();) {
			Attachment documentFile = (Attachment)iterator.next();
			totalLength += (int)new File(documentFile.getFilePath()).length();
		}
		//输出文件总长度
		writeInt(totalLength, outputStream);
		//输出ini文件
		writeFile(iniFilePath, null, outputStream);
		FileUtils.deleteFile(iniFilePath);
		//输出文档文件
		for(Iterator iterator = documentFiles==null ? null : documentFiles.iterator(); iterator!=null && iterator.hasNext();) {
			Attachment documentFile = (Attachment)iterator.next();
			writeFile(documentFile.getFilePath(), documentFile.getName(), outputStream);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.document.RemoteDocumentService#processWordDocument(javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.document.callback.ProcessWordDocumentCallback)
	 */
	public void processWordDocument(HttpServletRequest request, ProcessWordDocumentCallback callback) throws ServiceException {
		String taskId = CookieUtils.getCookie(request, "RemoteDocumentTask");
		Object[] values = null;
		try {
			values = (Object[])cache.get(taskId);
		}
		catch(CacheException e) {
			throw new ServiceException(e);
		}
		long documentId = ((Number)values[0]).longValue(); //文档ID
		//获取附件列表
		List attachments = attachmentService.list("jeaf/document", "document", documentId, false, 0, request);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("no files found");
		}
		int pageCount = 0; //页数
		double pageWidth = 0; //页面宽度
		List recordListChanges = null; //记录列表变动情况
		String documentPath = null; //文件路径
		String pdfDocumentPath = null; //PDF文件路径
		String officalDocumentPath = null; //正式文件路径
		String htmlPagePath = null; //HTML页面路径
		String htmlFilesPath = null; //HTML文件路径
		for(Iterator iterator = attachments.iterator(); iterator.hasNext();) {
			Attachment attachment = (Attachment)iterator.next();
			String name = attachment.getName().toLowerCase();
			if(Logger.isTraceEnabled()) {
				Logger.trace("RemoteDocumentService: found file " + name + "(" + attachment.getFileSize() + ")");
			}
			if(name.startsWith("~$")) {
				continue;
			}
			if(name.endsWith("_offical.doc")) { //正式文件
				officalDocumentPath = attachment.getFilePath();
			}
			else if(name.endsWith(".doc")) { //文档
				documentPath = attachment.getFilePath();
			}
			else if(name.endsWith(".pdf")) { //PDF
				pdfDocumentPath = attachment.getFilePath();
			}
			else if(name.endsWith(".html")) { //HTML页面
				htmlPagePath = attachment.getFilePath();
				htmlFilesPath = attachment.getFilePath().substring(0, attachment.getFilePath().length() - "html".length()) + "files";
			}
			else if(name.endsWith(".info")) { //文档消息
				Object[] infoValues = parseWordDocumentInfo(attachment.getFilePath());
				pageCount = ((Integer)infoValues[0]).intValue();
				pageWidth = ((Double)infoValues[1]).doubleValue();
				recordListChanges = (List)infoValues[2];
			}
		}
		//处理文档
		callback.process(documentPath, pdfDocumentPath, officalDocumentPath, htmlPagePath, htmlFilesPath, pageCount, pageWidth, recordListChanges);
		//清理文档
		cleanDocument(taskId, documentId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.document.RemoteDocumentService#updateFile(java.lang.String, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String)
	 */
	public void updateRecordFile(String documentFilePath, Record record, String fieldName, String newFileName) throws ServiceException {
		String path = FieldUtils.getAttachmentSavePath(record, null, fieldName, true);
		//清空原来的文件
		FileUtils.deleteFilesInDirectory(path);
		//移动文件
		documentFilePath = FileUtils.moveFile(documentFilePath, path, true, true);
		//使用文件标题对文件重命名
		if(newFileName!=null) {
			FileUtils.renameFile(documentFilePath, newFileName);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.document.RemoteDocumentService#retrieveRecordHtmlBody(com.yuanluesoft.jeaf.document.model.WordDocument, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String)
	 */
	public String retrieveRecordHtmlBody(String htmlPagePath, String htmlFilesPath, double pageWidth, Record record, String htmlFilesFieldName, String htmlFilesReplacement) throws ServiceException {
		if(htmlPagePath==null) {
			return null;
		}
		//读取HTML内容
		String htmlBody = FileUtils.readStringFromFile(htmlPagePath, "gbk");
		//过滤WORD元素
    	htmlBody = StringUtils.removeWordFormat(htmlBody, true, false);
    	//处理图片路径
    	if(htmlFilesPath!=null && htmlFilesFieldName!=null && htmlFilesReplacement!=null) {
    		String folderName = htmlFilesPath.substring(htmlFilesPath.lastIndexOf('/') + 1);
    		htmlBody = htmlBody.replaceAll("(?i)\"" + folderName.replaceAll(" ", "%20") + "/([^\"]+)\"", "\"" + htmlFilesReplacement + "\"");
    		String path = FieldUtils.getAttachmentSavePath(record, null, htmlFilesFieldName, true);
    		FileUtils.renameDirectory(htmlFilesPath, path, true, false);
    	}
    	//添加DIV限制页面宽度
    	htmlBody = "<div style=\"width:" + Math.round(pageWidth) + "pt\">" + htmlBody + "</div>";
    	return htmlBody;
	}

	/**
	 * 清理文档
	 * @param taskId
	 * @param documentId
	 * @throws ServiceException
	 */
	private void cleanDocument(String taskId, long documentId) throws ServiceException {
		//删除附件
		attachmentService.deleteAll("jeaf/document", "document", documentId);
		//清缓存
		try {
			cache.remove(taskId);
		}
		catch(CacheException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 输出文件,返回输出的字节数
	 * @param filePath
	 * @param fileName
	 * @param writeFileInfo
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	private void writeFile(String filePath, String fileName, OutputStream outputStream) throws IOException {
		FileInputStream input = null;
		try {
			File file = new File(filePath);
			if(fileName==null) {
				fileName = file.getName();
			}
			writeString(fileName, outputStream);
			writeInt((int)file.length(), outputStream);
			input = new FileInputStream(filePath);
			byte[] buffer = new byte[4096];
			for(int readLen = input.read(buffer); readLen!=-1; readLen = input.read(buffer)) {
				outputStream.write(buffer, 0, readLen);
			}
		}
		finally {
			input.close();
		}
	}
	
	/**
	 * 输出整数,返回总字节数
	 * @param intValue
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	private void writeInt(int intValue, OutputStream outputStream) throws IOException {
		outputStream.write(intValue);
		outputStream.write(intValue >> 8);
		outputStream.write(intValue >> 16);
		outputStream.write(intValue >> 24);
	}
	
	/**
	 * 输出字符串,返回总字节数
	 * @param string
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	private void writeString(String string, OutputStream outputStream) throws IOException {
		byte[] bytes = string.getBytes("gbk");
		writeInt(bytes.length, outputStream);
		outputStream.write(bytes);
	}
	
	/**
	 * 生成task.ini
	 * @param taskId
	 * @param documentApplicationName
	 * @param documentCommand
	 * @param documentCommandParameter
	 * @param applicationName
	 * @param formName
	 * @param form
	 * @param documentFiles
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 * @throws IOException
	 */
	private String generateIniFile(String taskId, String documentApplicationName, String documentCommand, Map documentCommandParameter, String applicationName, String formName, ActionForm form,  List documentFiles, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, IOException {
		//获取表单定义
		Form formDefine = formDefineService.loadFormDefine(applicationName, formName);
		String wordFields = formDefine.getExtendedParameter("wordFields");
		String wordRecordLists = formDefine.getExtendedParameter("wordRecordLists"); //格式:列表名称/子列表1名称/../子列表n名称,...
		String[] wordFieldNames = null;
		String[] wordRecordListNames = null;
		
		//获得临时目录
		String temporaryPath = temporaryFileManageService.createTemporaryDirectory(null);
		
		//创建task.ini
		FileOutputStream fileOutputStream = new FileOutputStream(temporaryPath + "task.ini");
		OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "gbk");
		
		//生成文件内容
		try {
			writer.write("[Task]\r\n");
			writer.write("taskId=" + taskId + "\r\n"); //任务ID
			writer.write("application=" + documentApplicationName + "\r\n"); //应用名称
			writer.write("command=" + documentCommand + "\r\n"); //命令
			for(int i=0; i<(documentFiles==null ? 0 : documentFiles.size()); i++) {
				Attachment documentFile = (Attachment)documentFiles.get(i);
				writer.write("file_" + (i + 1) + "=" + documentFile.getName() + "\r\n"); //文件名称
			}
			//输出加密过的用户名
			try {
				writer.write("userName=" + Encoder.getInstance().desEncode(Encoder.getInstance().desEncode(sessionInfo.getUserName(), "20050718", "gbk", "DES/ECB/NoPadding"), taskId.substring(0, 8), "gbk", "DES/ECB/NoPadding") + "\r\n"); //用户名
			}
			catch (Exception e1) {
				
			}
			//输出命令参数
			for(Iterator iterator = documentCommandParameter==null ? null : documentCommandParameter.keySet().iterator(); iterator!=null && iterator.hasNext();) {
				String name = (String)iterator.next();
				writer.write(name + "=" + documentCommandParameter.get(name) + "\r\n");
			}
			if(wordFields!=null && !wordFields.isEmpty()) {
				wordFieldNames = wordFields.split(",");
				wordFields = null;
				for(int i=0; i<wordFieldNames.length; i++) {
					wordFields = (wordFields==null ? "" : wordFields + ",") + hashEncode(wordFieldNames[i]);
				}
				writer.write("fields=" + wordFields + "\r\n");
			}
			if(wordRecordLists!=null && !wordRecordLists.isEmpty()) {
				wordRecordListNames = wordRecordLists.split(",");
				wordRecordLists = null;
				for(int i=0; i<wordRecordListNames.length; i++) {
					wordRecordLists = (wordRecordLists==null ? "" : wordRecordLists + ",") + hashEncode(wordRecordListNames[i]);
				}
				writer.write("recordLists=" + wordRecordLists + "\r\n");
			}
			//输出字段列表
			boolean writeValue = form.getFormDefine().getClassName().equals(formDefine.getClassName());
			try {
				writeFields(wordFieldNames, writeValue, formDefine, form, request, writer);
			} 
			catch (Exception e) {
				Logger.exception(e);
				throw new ServiceException(e);
			}
			//输出记录列表
			try {
				writeRecordLists(wordRecordListNames, writeValue, formDefine, form, request, writer);
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new ServiceException(e);
			}
			return temporaryPath + "task.ini";
		}
		finally {
			try {
				writer.close();
			}
			catch(Exception e) {
				
			}
			try {
				fileOutputStream.close();
			}
			catch(Exception e) {
				
			}
		}
	}

	/**
	 * 输出字段列表
	 * @param tagUtils
	 * @param formDefine
	 * @param formBean
	 * @param request
	 * @throws Exception
	 */
	private void writeFields(String[] wordFields, boolean writeValue, Form formDefine, ActionForm form, HttpServletRequest request, OutputStreamWriter writer) throws Exception {
		for(int i=0; i<(wordFields==null ? 0 : wordFields.length); i++) {
			Field field = FieldUtils.getFormField(formDefine, wordFields[i], request);
			if(field==null) {
				continue;
			}
			writer.write("[Field_" + hashEncode(wordFields[i]) + "]\r\n");
			writer.write("name=" + wordFields[i] + "\r\n");
			writer.write("title=" + field.getTitle() + "\r\n");
			writer.write("type=" + field.getType() + "\r\n");
			if(!writeValue) {
				continue;
			}
			writeValue("value", form, wordFields[i], field, request, writer);
		}
	}
	
	/**
	 * 获取并输出值
	 * @param iniPropertyName
	 * @param bean
	 * @param propertyName
	 * @param field
	 * @param request
	 * @param writer
	 * @throws Exception
	 */
	private void writeValue(String iniPropertyName, Object bean, String propertyName, Field field, HttpServletRequest request, OutputStreamWriter writer) throws Exception {
		Object fieldValue = FieldUtils.getFieldValue(bean, propertyName, request);
		String value;
		if("attachment".equals(field.getType()) || "video".equals(field.getType()) || "image".equals(field.getType())) {
			value = ListUtils.join((List)fieldValue, "shortName", "、", false);
		}
		else {
			value = FieldUtils.formatFieldValue(fieldValue, field, bean, true, null, false, true, false, 0, "、", null, request);
		}
		writeIniValue(iniPropertyName, value, writer);
	}
	
	/**
	 * 输出INI值
	 * @param name
	 * @param value
	 * @param writer
	 */
	private void writeIniValue(String name, String value, OutputStreamWriter writer) throws IOException {
		if(value==null || value.isEmpty()) {
			return;
		}
		value = value.replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
		if(value.length()<=1000) {
			writer.write(name + "=" + value + "\r\n");
			return;
		}
		int rows = (int)Math.ceil(value.length() / 1000.0);
		writer.write(name + "_rows=" + rows + "\r\n");
		for(int i=0; i<rows; i++) {
			writer.write(name + "_" + i + "=" + value.substring(i * 1000, i<rows-1 ? (i + 1) * 1000 : value.length()) + "\r\n");
		}
	}

	/**
	 * 输出记录列表
	 * @param wordRecordLists
	 * @param writeData
	 * @param formDefine
	 * @param form
	 * @param request
	 * @param writer
	 * @throws Exception
	 */
	private void writeRecordLists(String[] wordRecordLists, boolean writeData, Form formDefine, ActionForm form, HttpServletRequest request, OutputStreamWriter writer) throws Exception {
		Map sourceListNames = new HashMap(); //数据源列表
		for(int i=0; i<(wordRecordLists==null ? 0 : wordRecordLists.length); i++) {
			//输出记录列表定义
			writeRecordListDefine(wordRecordLists[i], FieldUtils.getFormField(formDefine, wordRecordLists[i], request), formDefine, sourceListNames, request, writer);
			//输出数据
			if(writeData) {
				writeRecordListData("RecordListData_" + hashEncode(wordRecordLists[i]), form, formDefine, wordRecordLists[i], formDefine.getExtendedParameter(wordRecordLists[i] + "_fields"), request, writer);
			}
		}
		//输出源数据列表
		for(Iterator iterator = !writeData ? null : sourceListNames.keySet().iterator(); iterator!=null && iterator.hasNext();) {
			String sourceListName = (String)iterator.next();
			String relationRecordList = (String)sourceListNames.get(sourceListName);
			writeRecordListData("SourceData_" + hashEncode(sourceListName), form, formDefine, sourceListName, formDefine.getExtendedParameter(relationRecordList + "_fields"), request, writer);
		}
	}
	
	/**
	 * 输出记录列表定义
	 * @param recordListName
	 * @param recordListField
	 * @param formDefine
	 * @param request
	 * @param writer
	 * @throws Exception
	 */
	private void writeRecordListDefine(String recordListName, Field recordListField, Form formDefine, Map sourceListNames, HttpServletRequest request, OutputStreamWriter writer) throws Exception {
		//输出记录列表的字段列表
		String fieldNames = formDefine.getExtendedParameter(recordListName + "_fields");
		fieldNames = "rowNum" + (fieldNames==null ? "" : "," + fieldNames);
		String[] fieldNameArray = fieldNames.split(",");
		String childRecordLists = formDefine.getExtendedParameter(recordListName + "_childRecordLists");
		String[] childRecordListNames = null;
		writer.write("[RecordList_" + hashEncode(recordListName) + "]\r\n");
		writer.write("name=" + recordListName + "\r\n");
		writer.write("title=" + recordListField.getTitle() + "\r\n");
		if("true".equals(formDefine.getExtendedParameter(recordListName + "_adjust"))) { //是否允许调整
			writer.write("adjust=true\r\n");
		}
		String source = formDefine.getExtendedParameter(recordListName + "_source");
		if(source!=null) {
			writer.write("source=" + hashEncode(source) + "\r\n");
			sourceListNames.put(source, recordListName);
		}
		//输出字段
		fieldNames = null;
		for(int i=0; i<fieldNameArray.length; i++) {
			fieldNames = (fieldNames==null ? "" : fieldNames + ",") + hashEncode(recordListName + "_" + fieldNameArray[i]);
		}
		writer.write("fields=" + fieldNames + "\r\n");
		//输出子记录列表
		if(childRecordLists!=null && !childRecordLists.isEmpty()) {
			childRecordListNames = childRecordLists.split(",");
			childRecordLists = null;
			for(int i=0; i<childRecordListNames.length; i++) {
				childRecordLists = (childRecordLists==null ? "" : childRecordLists + ",") + hashEncode(recordListName + "_" + childRecordListNames[i]);
			}
			writer.write("childRecordLists=" + childRecordLists + "\r\n");
		}
		String recordClassName = (String)recordListField.getParameter("class");
		//输出字段定义
		for(int i=0; i<fieldNameArray.length; i++) {
			Field field;
			if(fieldNameArray[i].equals("rowNum")) {
				field = new Field("rowNum", "序号", "number", null, "readonly", false, false);
			}
			else {
				field = FieldUtils.getRecordField(recordClassName, fieldNameArray[i], request);
			}
			if(field==null) {
				continue;
			}
			writer.write("[RecordListField_" + hashEncode(recordListName + "_" + fieldNameArray[i]) + "]\r\n");
			writer.write("name=" + fieldNameArray[i] + "\r\n");
			writer.write("title=" + field.getTitle() + "\r\n");
			writer.write("type=" + field.getType() + "\r\n");
		}
		//输出子记录列表定义
		for(int i=0; i<(childRecordListNames==null ? 0 : childRecordListNames.length); i++) {
			Field childRecordListField = FieldUtils.getRecordField(recordClassName, childRecordListNames[i], request);
			writeRecordListDefine(recordListName + "_" + childRecordListNames[i], childRecordListField, formDefine, sourceListNames, request, writer);
		}
	}
	
	/**
	 * 输出记录列表数据
	 * @param iniNamePrefix
	 * @param bean
	 * @param formDefine
	 * @param recordListName
	 * @param childRecordListNames
	 * @param fieldNames
	 * @param request
	 * @param writer
	 * @throws Exception
	 */
	private void writeRecordListData(String iniNamePrefix, Object bean, Form formDefine, String recordListName, String fieldNames, HttpServletRequest request, OutputStreamWriter writer) throws Exception {
		String[] values = recordListName.split("_");
		Collection recordList = (Collection)FieldUtils.getFieldValue(bean, values[values.length-1], request);
		if(recordList==null || recordList.isEmpty()) {
			return;
		}
		writer.write("[" + iniNamePrefix + "]\r\n");
		String[] fields = fieldNames.split(",");
		
		//输出记录ID列表
		String recordIds = null;
		for(Iterator iterator = recordList.iterator(); iterator.hasNext();) {
			Object record = iterator.next();
			if(record==null) {
				continue;
			}
			recordIds = (recordIds==null ? "" : recordIds + ",") + getRecordId(record, fields[0], true);
		}
		writeIniValue("records", recordIds, writer);
		
		//输出记录
		for(Iterator iterator = recordList.iterator(); iterator.hasNext();) {
			Object record = iterator.next();
			if(record==null) {
				continue;
			}
			String recordId = getRecordId(record, fields[0], true);
			writer.write("[Record_" + recordId + "]\r\n");
			writeIniValue("id", getRecordId(record, fields[0], false), writer);
			for(int i=0; i<fields.length; i++) {
				Field field = FieldUtils.getRecordField(record.getClass().getName(), fields[i], request);
				writeValue(fields[i], record, fields[i], field, request, writer);
			}
			//输出子记录列表
			String childRecordLists = formDefine.getExtendedParameter(recordListName + "_childRecordLists");
			if(childRecordLists!=null) {
				String[] childRecordListArray = childRecordLists.split(",");
				for(int i=0; i<childRecordListArray.length; i++) {
					String childRecordListName = recordListName + "_" + childRecordListArray[i];
					String childFieldNames = formDefine.getExtendedParameter(childRecordListName + "_fields");
					writeRecordListData("RecordListData_" + recordId + "_" + hashEncode(recordListName + "_" + childRecordListArray[i]), record, formDefine, childRecordListName, childFieldNames, request, writer);
				}
			}
		}
	}
	
	/**
	 * 获取记录ID
	 * @param record
	 * @param firstFieldName
	 * @param hashEncode
	 * @return
	 * @throws Exception
	 */
	private String getRecordId(Object record, String firstFieldName, boolean hashEncode) throws Exception {
		Object id = null;
		try {
			id = PropertyUtils.getProperty(record, "id");
		}
		catch(Exception e) {
			id = PropertyUtils.getProperty(record, firstFieldName);
		}
		return hashEncode ? hashEncode(record.getClass().getName() + id) : (id==null ? null : id.toString());
	}
	
	/**
	 * 编码
	 * @param text
	 * @return
	 */
	private String hashEncode(String text) {
		if(text==null) {
			text = "";
		}
		String encoded = Integer.toString(text.substring(text.length()/2).hashCode(), 36);
		return (Integer.toString(text.hashCode(), 36) + encoded.substring(encoded.length()/2)).replace('-', 'f');
	}
	
	/**
	 * 解析WORD文档信息
	 * @param infoFilePath
	 * @return
	 * @throws ServiceException
	 */
	private Object[] parseWordDocumentInfo(String infoFilePath) throws ServiceException {
		XmlParser xmlParser = new XmlParser();
		Element xmlRoot;
		try {
			xmlRoot = xmlParser.parseXmlFile(infoFilePath);
		} 
		catch (ParseException e) {
			throw new ServiceException(e);
		}
		Object[] values = new Object[3];
		//页数
		values[0] = new Integer(xmlRoot.elementText("pageCount"));
		//页面宽度
		values[1] = new Double(xmlRoot.elementText("pageWidth"));
		//记录列表变动情况, 格式:记录列表1名称__父记录ID=记录1ID,记录2ID...&记录列表2
		String recordListChange = xmlRoot.elementText("recordListChange");
		if(recordListChange==null || recordListChange.isEmpty()) {
			return values;
		}
		values[2] = new ArrayList();
		String[] changes = recordListChange.split("&");
		for(int i=0; i<changes.length; i++) {
			int index = changes[i].indexOf("=");
			int indexParentRecordId = changes[i].lastIndexOf("__", index - 1);
			RecordListChange change = new RecordListChange();
			change.setRecordListName(changes[i].substring(0, indexParentRecordId==-1 ? index : indexParentRecordId));
			change.setParentRecordId(indexParentRecordId==-1 ? null : changes[i].substring(indexParentRecordId + 2, index));
			change.setRecordIds(changes[i].substring(index + 1));
			((ArrayList)values[2]).add(change);
		}
		return values;
	}

	/**
	 * @return the temporaryFileManageService
	 */
	public TemporaryFileManageService getTemporaryFileManageService() {
		return temporaryFileManageService;
	}

	/**
	 * @param temporaryFileManageService the temporaryFileManageService to set
	 */
	public void setTemporaryFileManageService(
			TemporaryFileManageService temporaryFileManageService) {
		this.temporaryFileManageService = temporaryFileManageService;
	}

	/**
	 * @return the formDefineService
	 */
	public FormDefineService getFormDefineService() {
		return formDefineService;
	}

	/**
	 * @param formDefineService the formDefineService to set
	 */
	public void setFormDefineService(FormDefineService formDefineService) {
		this.formDefineService = formDefineService;
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
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