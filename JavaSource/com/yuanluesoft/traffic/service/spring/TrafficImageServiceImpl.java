package com.yuanluesoft.traffic.service.spring;

import java.awt.Dimension;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.upload.FormFile;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.exception.FileTransferException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.traffic.service.TrafficImageService;

/**
 * 
 * @author linchuan
 *
 */
public class TrafficImageServiceImpl implements TrafficImageService {
	private DatabaseService databaseService; //数据库服务
	private BusinessDefineService businessDefineService; //业务逻辑定义服务
	private FileDownloadService fileDownloadService; //文件下载服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#list(java.lang.String, java.lang.String, long, int, javax.servlet.http.HttpServletRequest)
	 */
	public List list(String applicationName, String type, long mainRecordId, boolean iconURLRequired, int max, HttpServletRequest request) throws ServiceException {
		SqlResult record = (SqlResult)request.getAttribute("SQL_RECORD");
		String mainRecordName = (String)record.get("sqlRecordName"); //主记录名称
		String jobholderId; //从业人员ID
		if(mainRecordName.equals("CMPSN")) { //当前查询的就是从业人员表
			jobholderId = (String)record.get("id");
		}
		else  {
			//获取主记录的业务逻辑定义
			BusinessObject businessObject = businessDefineService.getBusinessObject(mainRecordName);
			//获取从业人员定义
			Field field = businessObject.getFieldByParameter("class", "CMPSN");
			jobholderId = (String)record.get(field.getName() + "_CMPSNID");
		}
		/*<field name="PICLISID" title="照片列表ID" type="string" columnType="VARCHAR2(50)"/>
		<field name="PICTYPE" title="照片类型" type="string" columnType="CHAR(1)"/>
		<field name="OJBID" title="对象ID" type="string" columnType="VARCHAR2(50)"/>
		<field name="PICNAME" title="照片名称" type="string" columnType="VARCHAR2(20)"/>
		<field name="PIC" title="照片" type="binary" columnType="LONG RAW，行驶证、车辆正面照、侧面"/>
		<field name="ISEFFECT" title="是否有效" type="string" columnType="CHAR(1)"/>
		<field name="REMARKDOC" title="备注" type="string" columnType="VARCHAR2(50)"/>
		<field name="CREUSRCODE" title="创建者编号" type="string" columnType="VARCHAR2(50)"/>
		<field name="CREDATE" title="创建日期" type="string" columnType="CHAR(10)"/>
		<field name="CRETIME" title="创建时间" type="string" columnType="CHAR(8)"/>*/
		String sql = "SELECT PICLISID, PICNAME FROM PICLIST WHERE ISEFFECT = 0 AND OJBID='" + JdbcUtils.resetQuot(jobholderId) + "'";
		List images = databaseService.executeQueryBySql(sql, 0, 0);
		if(images==null || images.isEmpty()) {
			return null;
		}
		//转换为Image列表
		for(int i=0; i<images.size(); i++) {
			SqlResult imageRecord = (SqlResult)images.get(i);
			Image image = new Image();
			String url = Environment.getContextPath() + "/traffic/downloadImage.shtml?id=" + imageRecord.get("PICLISID");
			//image.setFilePath(filePath); //路径
			image.setName((String)imageRecord.get("PICNAME")); //附件名称
			//image.setSize(size); //附件大小
			//image.setLastModified(lastModified); //文件修改时间
			image.setUrlAttachment(url); //下载附件的URL
			image.setUrlInline(url); //下载附件的URL
			//image.setApplicationName(applicationName); //应用名称
			//image.setRecordId(recordId); //记录ID
			//image.setType(type); //附件类型
			//image.setService(service); //附件服务
			//image.setWidth(width); //图片宽度
			//image.setHeight(height); //图片高度
			image.setUrl(url); //图像URL
			images.set(i, image);
		}
		return images;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.traffic.service.TrafficImageService#downloadImage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void downloadImage(HttpServletRequest request, HttpServletResponse response) {
		String sql = "SELECT PIC,PICNAME FROM PICLIST WHERE PICLISID='" + JdbcUtils.resetQuot(request.getParameter("id")) + "'";
		List images = databaseService.executeQueryBySql(sql, 0, 1);
		if(images!=null && !images.isEmpty()) {
			SqlResult imageRecord = (SqlResult)images.get(0);
			try {
				fileDownloadService.httpDownload(request, response, (byte[])imageRecord.get("PIC"), (String)imageRecord.get("PICNAME"), null, false);
			} 
			catch (FileTransferException e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#copyAttachmentToExchangePath(java.lang.String)
	 */
	public void copyAttachmentToExchangePath(String attachmentFilePath) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#createDownload(java.lang.String, java.lang.String, long, java.lang.String, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public String createDownload(String applicationName, String type, long mainRecordId, String name, boolean asAttachment, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#createDynamicDownload(java.lang.String, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public String createDynamicDownload(String filePath, boolean attachment, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#createUploadPassport(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.business.model.Field, long, int, javax.servlet.http.HttpServletRequest)
	 */
	public String createUploadPassport(String applicationName, String type, Field fieldDefine, long mainRecordId, int fileSize, HttpServletRequest request) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#delete(java.lang.String, long, java.lang.String, java.lang.String)
	 */
	public void delete(String applicationName, long mainRecordId, String type, String names) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#deleteAll(java.lang.String, java.lang.String, long)
	 */
	public void deleteAll(String applicationName, String type, long mainRecordId) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#getDownloadFilePath(java.lang.String)
	 */
	public String getDownloadFilePath(String downloadPath) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#getFileType(java.lang.String)
	 */
	public String getFileType(String downloadUrl) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#getSavePath(java.lang.String, java.lang.String, long, boolean)
	 */
	public String getSavePath(String applicationName, String type, long id, boolean mkdir) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#listTypes(java.lang.String, long)
	 */
	public List listTypes(String applicationName, long mainRecordId) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#processUploadedFile(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.business.model.Field, long, java.lang.String)
	 */
	public String processUploadedFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String name) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#replace(java.lang.String, long, java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public void replace(String fromApplicationName, long fromRecordId, String fromType, String toApplicationName, long toRecordId, String toType) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.business.model.Field, long, org.apache.struts.upload.FormFile)
	 */
	public String upload(String applicationName, String type, Field fieldDefine, long mainRecordId, FormFile formFile) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.lang.String, java.lang.String)
	 */
	public String upload(String applicationName, String type, Field fieldDefine, long mainRecordId, String fileName, String base64FileData) throws ServiceException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.util.List)
	 */
	public List uploadFiles(String applicationName, String type, Field fieldDefine, long mainRecordId, List filePaths) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public String uploadFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String filePath) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#addWaterMark(java.lang.String, com.yuanluesoft.jeaf.image.model.WaterMark)
	 */
	public String addWaterMark(String imagePath, WaterMark waterMark) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#adjustImage(java.lang.String, int, int, boolean)
	 */
	public String adjustImage(String imagePath, int toWidth, int toHeight, boolean clipEnabled) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#convertImage(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void convertImage(String imageFilePath, String newImageFilePath, String newImageType) {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#getBreviaryImage(java.lang.String, java.lang.String, long, java.lang.String, int, int, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public Image getBreviaryImage(String applicationName, String imageType, long mainRecordId, String imageName, int breviaryWidth, int breviaryHeight, boolean clipEnabled, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#getImage(java.lang.String, java.lang.String, long, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public Image getImage(String applicationName, String imageType, long mainRecordId, String imageName, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#getImageDimension(java.lang.String)
	 */
	public Dimension getImageDimension(String imageFilePath) throws ServiceException {
		return null;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the fileDownloadService
	 */
	public FileDownloadService getFileDownloadService() {
		return fileDownloadService;
	}

	/**
	 * @param fileDownloadService the fileDownloadService to set
	 */
	public void setFileDownloadService(FileDownloadService fileDownloadService) {
		this.fileDownloadService = fileDownloadService;
	}

	/**
	 * @return the businessDefineService
	 */
	public BusinessDefineService getBusinessDefineService() {
		return businessDefineService;
	}

	/**
	 * @param businessDefineService the businessDefineService to set
	 */
	public void setBusinessDefineService(BusinessDefineService businessDefineService) {
		this.businessDefineService = businessDefineService;
	}
}