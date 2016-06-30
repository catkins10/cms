package com.yuanluesoft.jeaf.tools.filebrowse.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileUploadService;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.tools.filebrowse.forms.FileBrowse;
import com.yuanluesoft.jeaf.tools.filebrowse.model.Folder;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * 
 * @author linchuan
 *
 */
public class FileBrowseAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//检查用户有没有用户管理的权限
		AccessControlService accessControlService = (AccessControlService)getService("accessControlService");
		try {
			acl = accessControlService.getAcl("jeaf/usermanage", sessionInfo);
		}
		catch (ServiceException e) {
			throw new PrivilegeException();
		}
		if(!acl.contains("application_manager")) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#createAttachmentUploadPassport(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService, int, java.lang.String, long, boolean, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public String createAttachmentUploadPassport(ActionForm actionForm, Field field, AttachmentService attachmentService, int fileLength, String recordClassName, long recordId, boolean uploadEnabled, boolean newRecord, HttpServletRequest request) throws Exception {
		FileBrowse fileBrowseForm = (FileBrowse)actionForm;
		FileUploadService fileUploadService = (FileUploadService)getService("fileUploadService");
		return fileUploadService.createUploadPassport(fileBrowseForm.getPath(), fileLength, 0, null, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#uploadAttachmentFormFile(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService, long)
	 */
	public String uploadAttachmentFormFile(ActionForm actionForm, Field field, AttachmentService attachmentService, long recordId) throws Exception {
		FileBrowse fileBrowseForm = (FileBrowse)actionForm;
		FileUploadService fileUploadService = (FileUploadService)getService("fileUploadService");
		return fileUploadService.httpUpload(fileBrowseForm.getAttachmentSelector().getUpload(), fileBrowseForm.getPath(), true);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#processUploadedAttachmentFile(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, com.yuanluesoft.jeaf.business.model.Field, com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService, long, javax.servlet.http.HttpServletRequest)
	 */
	public Attachment processUploadedAttachmentFile(ActionForm actionForm, String fileName, Field field, AttachmentService attachmentService, long recordId, HttpServletRequest request) throws Exception {
		return null; //不做任何处理
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		//获取文件列表
		FileBrowse fileBrowseForm = (FileBrowse)form;
		if(fileBrowseForm.getPath()==null) {
			fileBrowseForm.setPath("/");
		}
		File folder = new File(fileBrowseForm.getPath());
		fileBrowseForm.setPath(folder.getCanonicalPath());
		File[] files = folder.listFiles();
		if(files==null) {
			return;
		}
		//按名称排序
		Arrays.sort(files, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((File)o1).getName().compareToIgnoreCase(((File)o2).getName());
			}
		});
		List folderList = new ArrayList();
		List fileList = new ArrayList();
		AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) {
				Folder objFolder = new Folder();
				objFolder.setName(files[i].getName());
				objFolder.setPath(files[i].getCanonicalPath());
				folderList.add(objFolder);
			}
			else {
				Attachment attachment = new Attachment();
				attachment.setFilePath(files[i].getCanonicalPath());
				attachment.setName(files[i].getName());
				attachment.setSize(files[i].length());
				attachment.setLastModified(files[i].lastModified());
				attachment.setUrlAttachment(attachmentService.createDynamicDownload(files[i].getPath(), true, request));
				attachment.setUrlInline(attachmentService.createDynamicDownload(files[i].getPath(), false, request));
				attachment.setIconURL(FileUtils.getIconURL(attachment.getName()));
				fileList.add(attachment);
			}
		}
		fileBrowseForm.setFolders(folderList);
		fileBrowseForm.setFiles(fileList);
	}
}
