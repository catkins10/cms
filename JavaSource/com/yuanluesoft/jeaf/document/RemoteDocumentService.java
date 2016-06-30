package com.yuanluesoft.jeaf.document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.document.callback.ProcessWordDocumentCallback;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 远程文档服务
 * @author linchuan
 *
 */
public interface RemoteDocumentService {
	//文档应用
	public static String APPLICATION_WORD = "Word";
	public static String APPLICATION_EXCEL = "Excel";
	
	//文档命令
	public static String COMMAND_CREATE_TEMPLATE = "createTemplate"; //创建模板
	public static String COMMAND_EDIT_TEMPLATE = "editTemplate"; //编辑模板
	public static String COMMAND_CREATE_DOCUMENT = "createDocument"; //创建文档
	public static String COMMAND_EDIT_DOCUMENT = "editDocument"; //编辑文档
	public static String COMMAND_VIEW_DOCUMENT = "viewDocument"; //根据模板显示文件内容
	//public static String COMMAND_RETRIEVE_DOCUMENT = "retrieveDocument"; //获取文档内容
	
	//文档命令参数
	public static String COMMAND_PARAMETER_KEEP_FIELD_VALUE = "keepFieldValue"; //保留文档中现有的字段值
	public static String COMMAND_PARAMETER_KEEP_RECORD_LIST = "keepRecordList"; //保留文档中现有的记录列表
	public static String COMMAND_PARAMETER_ALWAYS_SUBMIT = "alwaysSubmit"; //总是提交文档,不管有没有更新过
	public static String COMMAND_PARAMETER_GENERATE_OFFICAL = "generateOffical"; //保是否需要生成正式文件
	
	
	/**
	 * 输出远程文档
	 * @param documentApplicationName 文档应用
	 * @param documentCommand 文档命令
	 * @param documentCommandParameter 文档命令参数
	 * @param applicationName
	 * @param formName
	 * @param form
	 * @param documentFiles
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 * @throws IOException
	 */
	public void writeRemoteDocument(String documentApplicationName, String documentCommand, Map documentCommandParameter, String applicationName, String formName, ActionForm form, List documentFiles, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException, IOException;

	/**
	 * 根据任务ID获取远程文档ID
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 */
	public long getRemoteDocumentId(String taskId) throws ServiceException;
	
	/**
	 * 下载远程文档
	 * @param taskId
	 * @param response
	 * @throws ServiceException
	 */
	public void downloadRemoteDocument(String taskId, HttpServletResponse response) throws ServiceException, IOException;
	
	/**
	 * 处理已经上传的WORD文档
	 * @param request
	 * @param callback
	 * @throws ServiceException
	 */
	public void processWordDocument(HttpServletRequest request, ProcessWordDocumentCallback callback) throws ServiceException;
	
	/**
	 * 更新文件
	 * @param documentFilePath
	 * @param record
	 * @param fieldName
	 * @param newFileName
	 * @throws ServiceException
	 */
	public void updateRecordFile(String documentFilePath, Record record, String fieldName, String newFileName) throws ServiceException;
	
	/**
	 * 获取HTML正文
	 * @param htmlPagePath
	 * @param htmlFilesPath
	 * @param pageWidth
	 * @param record
	 * @param htmlFilesFieldName
	 * @param htmlFilesReplacement
	 * @return
	 * @throws ServiceException
	 */
	public String retrieveRecordHtmlBody(String htmlPagePath, String htmlFilesPath, double pageWidth, Record record, String htmlFilesFieldName, String htmlFilesReplacement) throws ServiceException;
}