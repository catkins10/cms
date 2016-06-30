package com.yuanluesoft.j2oa.exchange.soap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.j2oa.exchange.soap.model.Document;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ExchangeDocumentSoapClient {
	private SoapConnectionPool soapConnectionPool; //SOAP连接池
	private SoapPassport soapPassport; //SOAP许可证

	public ExchangeDocumentSoapClient(SoapConnectionPool soapConnectionPool, SoapPassport soapPassport) {
		super();
		this.soapConnectionPool = soapConnectionPool;
		this.soapPassport = soapPassport;
	}
	
	/**
	 * 创建公文
	 * @param sourceRecordId
	 * @param subject
	 * @param documentUnit
	 * @param sign
	 * @param docWord
	 * @param generateDate
	 * @param docType
	 * @param secureLevel
	 * @param secureTerm
	 * @param priority
	 * @param keyword
	 * @param printNumber
	 * @param distributeDate
	 * @param mainSend
	 * @param copySend
	 * @param otherSend
	 * @param remark
	 * @param ssoSessionId
	 * @return
	 * @throws Exception
	 */
	public long createDocument(String sourceRecordId, //源记录ID
							   String subject, //标题
							   String documentUnit, //发文单位
							   String sign, //签发人
							   String docWord, //发文字号
							   Date generateDate, //成文日期
							   String docType, //发文种类
							   String secureLevel, //秘密等级
							   String secureTerm, //保密期限
							   String priority, //紧急程度
							   String keyword, //主题词
							   int printNumber, //印发份数
							   Date distributeDate, //印发日期
							   String mainSend, //主送单位
							   String copySend, //抄送单位
							   String otherSend, //其他接收单位
							   String remark, //备注
							   String ssoSessionId) throws Exception {
		Object[] args = {sourceRecordId, subject, documentUnit, sign, docWord, DateTimeUtils.dateToCalendar(generateDate), docType, secureLevel, secureTerm, priority, keyword, new Integer(printNumber), DateTimeUtils.dateToCalendar(distributeDate), mainSend, copySend, otherSend, remark, ssoSessionId};
		Number documentId = (Number)soapConnectionPool.invokeRemoteMethod("ExchangeDocumentService", "createDocument", soapPassport, args, null);
		return documentId.longValue();
	}
	
	/**
	 * 上传正文或附件
	 * @param documentId
	 * @param fileType
	 * @param fileName
	 * @param base64FileData
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void uploadFiles(long documentId, boolean documentBody, List attachments, String ssoSessionId) throws Exception {
		if(attachments==null || attachments.isEmpty()) {
			return;
		}
		byte[] buffer = new byte[65536]; //64K
		byte[] base64Bytes = new byte[(int)(buffer.length * 1.35)]; //64K/3*4
		Base64Encoder base64Encoder = new Base64Encoder();
		for(Iterator iterator = attachments.iterator(); iterator.hasNext();) {
			Attachment attachment = (Attachment)iterator.next();
			FileInputStream input = null;
			try {
				input = new FileInputStream(attachment.getFilePath());
				int readLen;
				while((readLen=input.read(buffer))!=-1) {
					int base64Len = base64Encoder.encode(buffer, 0, readLen, base64Bytes);
					//public void uploadFile(long documentId, boolean documentBody, String fileName, String base64FileData, String ssoSessionId)
					Object[] args = {new Long(documentId), new Boolean(documentBody), attachment.getName(), new String(base64Bytes, 0, base64Len), ssoSessionId};
					soapConnectionPool.invokeRemoteMethod("ExchangeDocumentService", "uploadFile", soapPassport, args, null);
				}
			}
			finally {
				input.close();
			}
		}
	}
	
	/**
	 * 发布公文
	 * @param documentId
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void issueDocument(long documentId, String ssoSessionId) throws Exception {
		Object[] args = {new Long(documentId), ssoSessionId};
		soapConnectionPool.invokeRemoteMethod("ExchangeDocumentService", "issueDocument", soapPassport, args, null);
	}
	
	/**
	 * 撤销发布
	 * @param documentId
	 * @param reason
	 * @param resign
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void unissueDocument(long documentId, String reason, boolean resign, String ssoSessionId) throws Exception {
		Object[] args = {new Long(documentId), reason, new Boolean(resign), ssoSessionId};
		soapConnectionPool.invokeRemoteMethod("ExchangeDocumentService", "unissueDocument", soapPassport, args, null);
	}
	
	/**
	 * 获取待签收公文
	 * @param offset
	 * @param max
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public Document[] listToSignDocuments(int offset, int max, String ssoSessionId) throws Exception {
		Object[] args = {new Integer(offset), new Integer(max), ssoSessionId};
		return (Document[])soapConnectionPool.invokeRemoteMethod("ExchangeDocumentService", "listToSignDocuments", soapPassport, args, new Class[]{Document.class});
	}
	
	/**
	 * 获取已签收公文
	 * @param offset
	 * @param max
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public Document[] listSignedDocuments(int offset, int max, String ssoSessionId) throws Exception {
		Object[] args = {new Integer(offset), new Integer(max), ssoSessionId};
		return (Document[])soapConnectionPool.invokeRemoteMethod("ExchangeDocumentService", "listSignedDocuments", soapPassport, args, new Class[]{Document.class});
	}
	
	/**
	 * 签收公文
	 * @param documentId
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void signDocument(long documentId, String ssoSessionId) throws Exception {
		Object[] args = {new Long(documentId), ssoSessionId};
		soapConnectionPool.invokeRemoteMethod("ExchangeDocumentService", "signDocument", soapPassport, args, null);
	}
	
	/**
	 * 读取文件,如果返回字节小于len,则读取完成
	 * @param documentId
	 * @param documentBody
	 * @param fileName
	 * @param offset
	 * @param len
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void readFiles(long documentId, boolean documentBody, String[] fileNames, String savePath, String ssoSessionId) throws ServiceException, SoapException {
		if(fileNames==null || fileNames.length==0) {
			return;
		}
		Base64Decoder base64Decoder = new Base64Decoder();
		for(int i=0; i<fileNames.length; i++) {
			FileOutputStream ouput = null;
			try {
				ouput = new FileOutputStream(savePath + fileNames[i]);
				for(int offset = 0; ; offset+=65536) {
					//public String readFile(long documentId, boolean documentBody, String fileName, int offset, int len, String ssoSessionId)
					Object[] args = {new Long(documentId), new Boolean(documentBody), fileNames[i], new Integer(offset), new Integer(65536), ssoSessionId};
					String base64FileData = (String)soapConnectionPool.invokeRemoteMethod("ExchangeDocumentService", "readFile", soapPassport, args, null);
					if(base64FileData==null) {
						break;
					}
					ouput.write(base64Decoder.decode(base64FileData));
				}
				ouput.close();
			}
			catch(Exception e) {
				try {
					ouput.close();
				} 
				catch(IOException ioe) {
					
				}
				FileUtils.deleteFile(savePath + fileNames[i]);
				throw new ServiceException(e);
			}
		}
	}
}