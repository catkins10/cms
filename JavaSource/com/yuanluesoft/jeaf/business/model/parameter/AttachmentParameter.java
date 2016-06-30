package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数:附件
 * @author linchuan
 *
 */
public class AttachmentParameter {
	private String attachmentEditor; //附件编辑URL,默认为attachmentEditor.shtml
	private int maxUploadSize; //附件最大大小,超出时不允许上传,默认为100000000(100M)
	private String serviceName; //使用的服务名称,默认为attachmentService
	private String fileExtension; //文件后缀名列表,如:所有图片|*.jpg;*.jpeg;*.jpe;*.bmp;*.gif;*.png|
	private String extendJs; //附件扩展JS文件
	private boolean dynamicUrl = false; //是否生成动态的下载链接,默认为false(生成静态的下载链接)
	private String type; //动态附件类型,默认为空
	private boolean simpleMode = false; //是否启用精简模式,默认为false
	
	/**
	 * @return the attachmentEditor
	 */
	public String getAttachmentEditor() {
		return attachmentEditor;
	}
	/**
	 * @param attachmentEditor the attachmentEditor to set
	 */
	public void setAttachmentEditor(String attachmentEditor) {
		this.attachmentEditor = attachmentEditor;
	}
	/**
	 * @return the dynamicUrl
	 */
	public boolean isDynamicUrl() {
		return dynamicUrl;
	}
	/**
	 * @param dynamicUrl the dynamicUrl to set
	 */
	public void setDynamicUrl(boolean dynamicUrl) {
		this.dynamicUrl = dynamicUrl;
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
	 * @return the maxUploadSize
	 */
	public int getMaxUploadSize() {
		return maxUploadSize;
	}
	/**
	 * @param maxUploadSize the maxUploadSize to set
	 */
	public void setMaxUploadSize(int maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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
}