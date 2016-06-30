package com.yuanluesoft.j2oa.exchange.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument;
import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocumentUnit;
import com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService;
import com.yuanluesoft.j2oa.exchange.soap.model.Document;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.soap.BaseSoapService;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;

/**
 * 
 * @author linchuan
 *
 */
public class ExchangeDocumentSoapService extends BaseSoapService {

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
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public long createDocument(String sourceRecordId, //源记录ID
							   String subject, //标题
							   String documentUnit, //发文单位
							   String sign, //签发人
							   String docWord, //发文字号
							   Calendar generateDate, //成文日期
							   String docType, //发文种类
							   String secureLevel, //秘密等级
							   String secureTerm, //保密期限
							   String priority, //紧急程度
							   String keyword, //主题词
							   int printNumber, //印发份数
							   Calendar distributeDate, //印发日期
							   String mainSend, //主送单位
							   String copySend, //抄送单位
							   String otherSend, //其他接收单位
							   String remark, //备注
							   String ssoSessionId) throws ServiceException, SoapException {
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		//检查用户的发送权限
		if(!((OrgService)getSpringService("orgService")).checkPopedom(sessionInfo.getUnitId(), "sendDocument", sessionInfo)) {
			throw new SoapException("no popedom");
		}
		ExchangeDocumentService exchangeDocumentService = (ExchangeDocumentService)getSpringService("exchangeDocumentService");
		if(sourceRecordId==null || sourceRecordId.isEmpty()) {
			throw new SoapException("Field \"sourceRecordId\" is empty.");
		}
		ExchangeDocument document = exchangeDocumentService.getDocumentBySourceRecordId(sourceRecordId, sessionInfo.getUnitId());
		if(document!=null) {
			exchangeDocumentService.unissueDocument(document, "重新发布", true, sessionInfo);
		}
		boolean isNew = (document==null);
		if(isNew) {
			document = new ExchangeDocument();
			document.setId(UUIDLongGenerator.generateId()); //ID
			document.setSourceRecordId(sourceRecordId); //源记录ID
		}
		document.setSubject(subject); //标题
		document.setDocumentUnit(documentUnit); //发文单位
		document.setSign(sign); //签发人
		document.setDocWord(docWord); //发文字号
		document.setGenerateDate(DateTimeUtils.calendarToDate(generateDate)); //成文日期
		document.setDocType(docType); //发文种类
		document.setSecureLevel(secureLevel); //秘密等级
		document.setSecureTerm(secureTerm); //保密期限
		document.setPriority(priority); //紧急程度
		document.setKeyword(keyword); //主题词
		document.setPrintNumber(printNumber); //印发份数
		document.setDistributeDate(DateTimeUtils.calendarToDate(distributeDate)); //印发日期
		document.setMainSend(mainSend); //主送单位
		document.setCopySend(copySend); //抄送单位
		document.setOtherSend(otherSend); //其他接收单位
		document.setCreated(DateTimeUtils.now()); //创建时间
		document.setCreatorId(sessionInfo.getUserId()); //创建人ID
		document.setCreator(sessionInfo.getUserName()); //创建人
		document.setCreatorUnit(sessionInfo.getUnitName()); //创建人所在单位
		document.setCreatorUnitId(sessionInfo.getUnitId()); //创建人所在单位ID
		document.setRemark(remark); //备注
		if(isNew) {
			exchangeDocumentService.save(document);
		}
		else {
			exchangeDocumentService.update(document);
			//清空附件
			AttachmentService attachmentService = (AttachmentService)getSpringService("attachmentService");
			attachmentService.deleteAll("j2oa/exchange", null, document.getId());
		}
		return document.getId();
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
	public void uploadFile(long documentId, boolean documentBody, String fileName, String base64FileData, String ssoSessionId) throws ServiceException, SoapException {
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		getDocument(documentId, true, sessionInfo);
		AttachmentService attachmentService = (AttachmentService)getSpringService("attachmentService");
		attachmentService.upload("j2oa/exchange", documentBody ? "body" : "attachment", null, documentId, fileName, base64FileData);
	}
	
	/**
	 * 获取公文
	 * @param documentId
	 * @param forSend
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	private ExchangeDocument getDocument(long documentId, boolean forSend, SessionInfo sessionInfo) throws ServiceException, SoapException {
		ExchangeDocumentService exchangeDocumentService = (ExchangeDocumentService)getSpringService("exchangeDocumentService");
		ExchangeDocument document = (ExchangeDocument)exchangeDocumentService.load(ExchangeDocument.class, documentId);
		if(document==null) {
			throw new SoapException("not exists");
		}
		//检查用户权限
		OrgService orgService = (OrgService)getSpringService("orgService");
		if(forSend) {
			if(document.getCreatorUnitId()!=sessionInfo.getUnitId() || //不是本单位发布的
			   !orgService.checkPopedom(sessionInfo.getUnitId(), "sendDocument", sessionInfo)) { //没有发布权限
				throw new SoapException("no popedom");
			}
		}
		else {
			if(ListUtils.findObjectByProperty(document.getExchangeUnits(), "unitId", new Long(sessionInfo.getUnitId()))==null || //不在签收单位中
			   !orgService.checkPopedom(sessionInfo.getUnitId(), "receiveDocument", sessionInfo)) { //没有签收权限
				throw new SoapException("no popedom");
			}
		}
		return document;
	}
	
	/**
	 * 发布公文
	 * @param documentId
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void issueDocument(long documentId, String ssoSessionId) throws ServiceException, SoapException {
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		ExchangeDocumentService exchangeDocumentService = (ExchangeDocumentService)getSpringService("exchangeDocumentService");
		ExchangeDocument document = getDocument(documentId, true, sessionInfo);
		exchangeDocumentService.issueDocument(document, sessionInfo);
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
	public void unissueDocument(long documentId, String reason, boolean resign, String ssoSessionId) throws ServiceException, SoapException {
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		ExchangeDocumentService exchangeDocumentService = (ExchangeDocumentService)getSpringService("exchangeDocumentService");
		ExchangeDocument document = getDocument(documentId, true, sessionInfo);
		exchangeDocumentService.unissueDocument(document, reason, resign, sessionInfo);
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
	public Document[] listToSignDocuments(int offset, int max, String ssoSessionId) throws ServiceException, PrivilegeException, SoapException {
		return listDocuments("admin/todoSign", offset, max, ssoSessionId);
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
	public Document[] listSignedDocuments(int offset, int max, String ssoSessionId) throws ServiceException, PrivilegeException, SoapException {
		return listDocuments("admin/signed", offset, max, ssoSessionId);
	}
	
	/**
	 * 获取公文列表
	 * @param viewName
	 * @param offset
	 * @param max
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	private Document[] listDocuments(String viewName, int offset, int max, String ssoSessionId) throws ServiceException, PrivilegeException, SoapException {
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		ViewDefineService viewDefineService = (ViewDefineService)getSpringService("viewDefineService");
		View view = viewDefineService.getView("j2oa/exchange", viewName, sessionInfo);
		ViewService viewService = (ViewService)getSpringService("viewService");
		view.setPageRows(max<=0 ? 100 : Math.min(200, max)); //每页记录数
		ViewPackage viewPackage = new ViewPackage();
		viewPackage.setView(view);
		viewPackage.setViewMode(View.VIEW_DISPLAY_MODE_NORMAL);
		HttpServletRequest request = getHttpServletRequest();
		request.getSession().setAttribute("SessionInfo", sessionInfo);
		viewService.retrieveViewPackage(viewPackage, view, offset, true, true, false, request, sessionInfo);
		//转换为Document模型列表
		if(viewPackage.getRecords()==null || viewPackage.getRecords().isEmpty()) {
			return null;
		}
		AttachmentService attachmentService = (AttachmentService)getSpringService("attachmentService");
		Document[] documents = new Document[viewPackage.getRecords().size()];
		for(int i=0; i<documents.length; i++) {
			ExchangeDocument document = (ExchangeDocument)viewPackage.getRecords().get(i);
			documents[i] = new Document();
			documents[i].setId(document.getId()); //ID
			documents[i].setSubject(document.getSubject()); //标题
			documents[i].setDocumentUnit(document.getDocumentUnit()); //发文单位
			documents[i].setSign(document.getSign()); //签发人
			documents[i].setDocWord(document.getDocWord()); //发文字号
			documents[i].setGenerateDate(DateTimeUtils.dateToCalendar(document.getGenerateDate())); //成文日期
			documents[i].setDocType(document.getDocType()); //发文种类
			documents[i].setSecureLevel(document.getSecureLevel()); //秘密等级
			documents[i].setSecureTerm(document.getSecureTerm()); //保密期限
			documents[i].setPriority(document.getPriority()); //紧急程度
			documents[i].setKeyword(document.getKeyword()); //主题词
			documents[i].setPrintNumber(document.getPrintNumber()); //印发份数
			documents[i].setDistributeDate(DateTimeUtils.dateToCalendar(document.getDistributeDate())); //印发日期
			documents[i].setMainSend(document.getMainSend()); //主送单位
			documents[i].setCopySend(document.getCopySend()); //抄送单位
			documents[i].setOtherSend(document.getOtherSend()); //其他接收单位
			documents[i].setCreated(DateTimeUtils.timestampToCalendar(document.getCreated())); //创建时间
			documents[i].setCreatorId(document.getCreatorId()); //创建人ID
			documents[i].setCreator(document.getCreator()); //创建人
			documents[i].setCreatorUnit(document.getCreatorUnit()); //创建人所在单位
			documents[i].setCreatorUnitId(document.getCreatorUnitId()); //创建人所在单位ID
			documents[i].setIssue(document.getIssue()=='1' ? 1 : 0); //是否发布
			documents[i].setIssuePersonId(document.getIssuePersonId()); //最后发布人Id
			documents[i].setIssuePerson(document.getIssuePerson()); //最后发布人
			documents[i].setIssueTime(DateTimeUtils.timestampToCalendar(document.getIssueTime())); //最后发布时间
			documents[i].setRemark(document.getRemark()); //备注
			String fileNames = ListUtils.join(attachmentService.list("j2oa/exchange", "body", document.getId(), false, 0, null), "name", "/", false); 
			documents[i].setBodyFileNames(fileNames==null || fileNames.isEmpty() ? null : fileNames.split("/")); //正文文件名称列表
			fileNames = ListUtils.join(attachmentService.list("j2oa/exchange", "attachment", document.getId(), false, 0, null), "name", "/", false);
			documents[i].setAttachmentFileNames(fileNames==null || fileNames.isEmpty() ? null : fileNames.split("/")); //附件文件名称列表
		}
		return documents;
	}
	
	/**
	 * 签收公文
	 * @param documentId
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void signDocument(long documentId, String ssoSessionId) throws ServiceException, SoapException {
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		ExchangeDocumentService exchangeDocumentService = (ExchangeDocumentService)getSpringService("exchangeDocumentService");
		ExchangeDocument document = getDocument(documentId, false, sessionInfo);
		exchangeDocumentService.signDocument(document, sessionInfo);
	}
	
	/**
	 * 读取文件,如果返回null,则读取完成
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
	public String readFile(long documentId, boolean documentBody, String fileName, int offset, int len, String ssoSessionId) throws ServiceException, SoapException {
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		ExchangeDocument document = getDocument(documentId, false, sessionInfo);
		//检查是否已经签收
		ExchangeDocumentUnit unit = (ExchangeDocumentUnit)ListUtils.findObjectByProperty(document.getExchangeUnits(), "unitId", new Long(sessionInfo.getUnitId()));
		if(unit.getSignTime()==null) {
			throw new SoapException("sign first");
		}
		AttachmentService attachmentService = (AttachmentService)getSpringService("attachmentService");
		List attachments = attachmentService.list("j2oa/exchange", documentBody ? "body" : "attachment", document.getId(), false, 0, null);
		Attachment attachment = (Attachment)ListUtils.findObjectByProperty(attachments, "name", fileName);
		if(attachment==null) {
			return null;
		}
		if(new File(attachment.getFilePath()).length()<=offset) {
			return null;
		}
		FileInputStream input = null;
		try {
			input = new FileInputStream(attachment.getFilePath());
			input.skip(offset);
			byte[] buffer = new byte[len];
			input.read(buffer);
			return new Base64Encoder().encode(buffer);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new SoapException("read failed");
		}
		finally {
			try {
				input.close();
			}
			catch(IOException e) {
				
			}
		}
	}
}