package com.yuanluesoft.jeaf.form.model;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.upload.FormFile;

/**
 * 
 * @author linchuan
 *
 */
public class AttachmentSelector implements Serializable {
	public static final String SELECTOR_ACTION_LOAD = "load";
	public static final String SELECTOR_ACTION_UPLOAD = "upload";
	public static final String SELECTOR_ACTION_DELETE = "delete";
	public static final String SELECTOR_ACTION_DELETE_ALL = "deleteAll";
	public static final String SELECTOR_ACTION_PASSPORT = "passport";
	public static final String SELECTOR_ACTION_PROCESS_UPLOADED_FILES = "processUploadFiles";
	public static final String SELECTOR_ACTION_REFRESH = "refresh";
	
	private String field; //对应的附件字段名称
	private String type; //附件类型,如image/attachment/video
	private String title; //附件标题,如图片/附件/视频
	private boolean isImage; //是否图片
	private boolean isVideo; //是否视频
    private String selectedTitles; //选中的附件标题(一般是附件名称+附件大小)列表,用\t分隔
	private String selectedNames; //选中的附件名称列表,用*分隔
	private String fileExtension; //文件扩展名
	private String extendJs; //附件扩展JS文件
	private boolean simpleMode; //是否使用简洁模式
	private int maxUpload; //最多允许上传的文件数
	private boolean uploadFrame; //是否上传帧
	
	//附件上传,页面模式即<input type="file" name="">模式
	private FormFile upload; //附件内容
	
	private String scriptRunAfterSelect; //选中后执行的脚本
	private boolean uploadDisabled; //是否禁止用户上传
	private String action; //执行的操作,SELECTOR_ACTION_LOAD(默认)/SELECTOR_ACTION_UPLOAD/SELECTOR_ACTION_DELETE
	private String lastUploadFiles; //最后上传的文件名称,用*分隔
	private List lastUploadAttachments; //最后上传的的附件
	private List attachments; //附件列表
	private int attachmentCount; //附件数量
	
	/**
	 * @return the scriptRunAfterSelect
	 */
	public String getScriptRunAfterSelect() {
		return scriptRunAfterSelect;
	}

	/**
	 * @param scriptRunAfterSelect the scriptRunAfterSelect to set
	 */
	public void setScriptRunAfterSelect(String scriptRunAfterSelect) {
		this.scriptRunAfterSelect = scriptRunAfterSelect;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the uploadDisabled
	 */
	public boolean isUploadDisabled() {
		return uploadDisabled;
	}

	/**
	 * @param uploadDisabled the uploadDisabled to set
	 */
	public void setUploadDisabled(boolean uploadDisabled) {
		this.uploadDisabled = uploadDisabled;
	}

	/**
	 * @return the lastUploadFile
	 */
	public String getLastUploadFiles() {
		return lastUploadFiles;
	}

	/**
	 * @param lastUploadFile the lastUploadFile to set
	 */
	public void setLastUploadFiles(String lastUploadFile) {
		this.lastUploadFiles = lastUploadFile;
	}

	/**
	 * @return the isImage
	 */
	public boolean isImage() {
		return isImage;
	}

	/**
	 * @param isImage the isImage to set
	 */
	public void setImage(boolean isImage) {
		this.isImage = isImage;
	}

	/**
	 * @return the isVideo
	 */
	public boolean isVideo() {
		return isVideo;
	}

	/**
	 * @param isVideo the isVideo to set
	 */
	public void setVideo(boolean isVideo) {
		this.isVideo = isVideo;
	}

	/**
	 * @return the selectedNames
	 */
	public String getSelectedNames() {
		return selectedNames;
	}

	/**
	 * @param selectedNames the selectedNames to set
	 */
	public void setSelectedNames(String selectedNames) {
		this.selectedNames = selectedNames;
	}

	/**
	 * @return the selectedTitles
	 */
	public String getSelectedTitles() {
		return selectedTitles;
	}

	/**
	 * @param selectedTitles the selectedTitles to set
	 */
	public void setSelectedTitles(String selectedTitles) {
		this.selectedTitles = selectedTitles;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the upload
	 */
	public FormFile getUpload() {
		return upload;
	}

	/**
	 * @param upload the upload to set
	 */
	public void setUpload(FormFile upload) {
		this.upload = upload;
	}

	/**
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * @param fileExtension the fileExtension to set
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the simpleMode
	 */
	public boolean isSimpleMode() {
		return simpleMode;
	}

	/**
	 * @param simpleMode the simpleMode to set
	 */
	public void setSimpleMode(boolean simpleMode) {
		this.simpleMode = simpleMode;
	}

	/**
	 * @return the maxUpload
	 */
	public int getMaxUpload() {
		return maxUpload;
	}

	/**
	 * @param maxUpload the maxUpload to set
	 */
	public void setMaxUpload(int maxUpload) {
		this.maxUpload = maxUpload;
	}

	/**
	 * @return the uploadFrame
	 */
	public boolean isUploadFrame() {
		return uploadFrame;
	}

	/**
	 * @param uploadFrame the uploadFrame to set
	 */
	public void setUploadFrame(boolean uploadFrame) {
		this.uploadFrame = uploadFrame;
	}

	/**
	 * @return the extendJs
	 */
	public String getExtendJs() {
		return extendJs;
	}

	/**
	 * @param extendJs the extendJs to set
	 */
	public void setExtendJs(String extendJs) {
		this.extendJs = extendJs;
	}

	/**
	 * @return the lastUploadAttachments
	 */
	public List getLastUploadAttachments() {
		return lastUploadAttachments;
	}

	/**
	 * @param lastUploadAttachments the lastUploadAttachments to set
	 */
	public void setLastUploadAttachments(List lastUploadAttachments) {
		this.lastUploadAttachments = lastUploadAttachments;
	}

	/**
	 * @return the attachmentCount
	 */
	public int getAttachmentCount() {
		return attachmentCount;
	}

	/**
	 * @param attachmentCount the attachmentCount to set
	 */
	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	/**
	 * @return the attachments
	 */
	public List getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List attachments) {
		this.attachments = attachments;
	}
}
