package com.yuanluesoft.cms.attachmentmanage.spring;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AttachmentServiceImpl extends com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl {
	private String webUrl; //WEB路径

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl#createDownload(java.lang.String, java.lang.String, long, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String createDownload(String applicationName, String type, long mainRecordId, String name, boolean asAttachment, HttpServletRequest request) throws ServiceException {
		return webUrl + mainRecordId + "/" + type + "/" + FileUtils.encodeFileName(name, "utf-8");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl#getSavePath(java.lang.String, java.lang.String, long, boolean)
	 */
	public String getSavePath(String applicationName, String dataCategory, long id, boolean mkdir) {
		String path = getBaseDirectory();
		if(!path.endsWith("/")) {
			path += "/";
		}
		path += id + "/" + (dataCategory==null ? "" : dataCategory + "/");
		if(!mkdir) {
			return path;
		}
		return FileUtils.createDirectory(getBaseDirectory() + id + "/" + (dataCategory==null ? "" : dataCategory + "/"));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl#getDownloadFilePath(java.lang.String)
	 */
	public String getDownloadFilePath(String downloadUrl) throws ServiceException {
		int index = downloadUrl.lastIndexOf("/");
		String filePath = getBaseDirectory() + downloadUrl.substring(webUrl.length(), index + 1);
		return filePath + decodeFileName(filePath,  downloadUrl.substring(index + 1));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#getFileType(java.lang.String)
	 */
	public String getFileType(String downloadUrl) throws ServiceException {
		int index = downloadUrl.lastIndexOf("/");
		int attachmentTypeIndex = downloadUrl.lastIndexOf("/", index - 1);
		return downloadUrl.substring(attachmentTypeIndex + 1, index);
	}

	/**
	 * @return the webUrl
	 */
	public String getWebUrl() {
		return webUrl;
	}

	/**
	 * @param webUrl the webUrl to set
	 */
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
}