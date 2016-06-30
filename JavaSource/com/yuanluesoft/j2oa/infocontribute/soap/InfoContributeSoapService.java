package com.yuanluesoft.j2oa.infocontribute.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContributeRevise;
import com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService;
import com.yuanluesoft.j2oa.infocontribute.soap.model.InfoContribute;
import com.yuanluesoft.j2oa.infocontribute.soap.model.InfoReviseResult;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.soap.BaseSoapService;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InfoContributeSoapService extends BaseSoapService {
	
	/**
	 * 添加或者更新刊物定义
	 * @param id
	 * @param name
	 * @param unitName
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void addOrUpdateMagazineDefine(long id, String name, String unitName) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.addOrUpdateMagazineDefine(id, name, unitName);
	}
	
	/**
	 * 删除刊物定义
	 * @param id
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void deleteMagazineDefine(long id) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.deleteMagazineDefine(id);
	}
	
	/**
	 * 获取投稿列表
	 * @param beginTime
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public InfoContribute[] listContributeInfos(String unitName, Calendar beginTime, int offset, int limit)  throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		List infos = infoContributeService.listContributeInfos(unitName, beginTime==null ? null : new Timestamp(beginTime.getTimeInMillis()), offset, limit);
		if(infos==null || infos.isEmpty()) {
			return null;
		}
		AttachmentService attachmentService = (AttachmentService)getSpringService("attachmentService");
		InfoContribute[] infoContributes = new InfoContribute[infos.size()];
		for(int i=0; i<infoContributes.length; i++) {
			com.yuanluesoft.j2oa.infocontribute.pojo.InfoContribute info = (com.yuanluesoft.j2oa.infocontribute.pojo.InfoContribute)infos.get(i);
			infoContributes[i] = new InfoContribute();
			try {
				PropertyUtils.copyProperties(infoContributes[i], info);
			}
			catch (Exception e) {
				throw new SoapException();
			}
			infoContributes[i].setContributed(DateTimeUtils.timestampToCalendar(info.getContributeTime()));
			//获取附件列表
			String fileNames = ListUtils.join(attachmentService.list("j2oa/infocontribute", "attachments", info.getId(), false, 0, null), "name", "/", false);
			infoContributes[i].setAttachmentFileNames(fileNames==null || fileNames.isEmpty() ? null : fileNames.split("/")); //附件文件名称列表
		}
		return infoContributes;
	}
	
	/**
	 * 读取附件
	 * @param infoId
	 * @param fileName
	 * @param offset
	 * @param len
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public String readAttachment(long infoId, String fileName, int offset, int len) throws ServiceException, SoapException {
		getSpringService("infoContributeService"); //SOAP身份认证
		AttachmentService attachmentService = (AttachmentService)getSpringService("attachmentService");
		List attachments = attachmentService.list("j2oa/infocontribute", "attachments", infoId, false, 0, null);
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
	
	/**
	 * 退改稿
	 * @param reviseId
	 * @param infoId
	 * @param reviseOpinion
	 * @param revisePersonId
	 * @param revisePersonName
	 * @param revisePersonTel
	 * @throws ServiceException
	 */
	public void reviseInfo(long reviseId, long infoId, String reviseOpinion, long revisePersonId, String revisePersonName, String revisePersonTel) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.reviseInfo(reviseId, infoId, reviseOpinion, revisePersonId, revisePersonName, revisePersonTel);
	}
	
	/**
	 * 获取退改稿修改结果
	 * @param reviseId
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public InfoReviseResult getInfoReviseResult(long reviseId) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		InfoContributeRevise revise = (InfoContributeRevise)infoContributeService.load(InfoContributeRevise.class, reviseId);
		if(revise==null || revise.getEditTime()==null) {
			return null;
		}
		InfoReviseResult result = new InfoReviseResult();
		result.setNewBody(revise.getNewBody()); //修改后的正文
		result.setEditTime(DateTimeUtils.timestampToCalendar(revise.getEditTime())); //修改时间
		result.setEditorId(revise.getEditorId()); //修改人ID
		result.setEditor(revise.getEditor()); //修改人
		return result;
	}
	
	/**
	 * 添加采用情况
	 * @param useId
	 * @param infoIds
	 * @param level
	 * @param sendTime
	 * @param useTime
	 * @param magazineId
	 * @param magazine
	 * @throws ServiceException
	 */
	public void addUsage(long useId, String infoIds, int level, Calendar sendTime, Calendar useTime, long magazineId, String magazine) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.addUsage(useId, infoIds, level, DateTimeUtils.calendarToTimestamp(sendTime), DateTimeUtils.calendarToTimestamp(useTime), magazineId, magazine);
	}
	
	/**
	 * 删除采用情况
	 * @param useId
	 * @throws ServiceException
	 */
	public void deleteUsage(long useId) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.deleteUsage(useId);
	}
	
	/**
	 * 添加领导批示
	 * @param instructId
	 * @param infoIds
	 * @param leader
	 * @param level
	 * @param instruct
	 * @param instructTime
	 * @param creatorId
	 * @param creator
	 * @param created
	 * @throws ServiceException
	 */
	public void addInstruct(long instructId, String infoIds, String leader, int level, String instruct, Calendar instructTime, long creatorId, String creator, Calendar created) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.addInstruct(instructId, infoIds, leader, level, instruct, DateTimeUtils.calendarToTimestamp(instructTime), creatorId, creator, DateTimeUtils.calendarToTimestamp(created));
	}
	
	/**
	 * 删除领导批示
	 * @param instructId
	 * @throws ServiceException
	 */
	public void deleteInstruct(long instructId) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.deleteInstruct(instructId);
	}
	
	/**
	 * 添加刊物
	 * @param magazineId
	 * @param name
	 * @param issueTime
	 * @param sn
	 * @param snTotal
	 * @param level
	 * @param useInfoIds
	 * @param visitorIds
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void addMagazine(long magazineId, String name, Calendar issueTime, long sn, long snTotal, int level, String useInfoIds, String visitorIds) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.addMagazine(magazineId, name, DateTimeUtils.calendarToTimestamp(issueTime), sn, snTotal, level, useInfoIds, visitorIds);
	}
	
	/**
	 * 上传刊物文件
	 * @param magazineId
	 * @param fileName
	 * @param base64FileData
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void uploadMagazineFile(long magazineId, String fileName, String base64FileData) throws ServiceException, SoapException {
		getSpringService("infoContributeService"); //SOAP身份验证
		AttachmentService attachmentService = (AttachmentService)getSpringService("attachmentService");
		attachmentService.upload("j2oa/infocontribute", "magazine", null, magazineId, fileName, base64FileData);
	}
	
	/**
	 * 删除刊物
	 * @param magazineId
	 * @throws ServiceException
	 */
	public void deleteMagazine(long magazineId) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.deleteMagazine(magazineId);
	}
	
	/**
	 * 添加补录的信息
	 * @param infoId
	 * @param subject
	 * @param keywords
	 * @param magazineName
	 * @param magazineSN
	 * @param fromUnit
	 * @param fromUnitId
	 * @param body
	 * @param secretLevel
	 * @param supplementTime
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void addSupplementInfo(long infoId, String subject, String keywords, String magazineName, int magazineSN, int magazineLevel, String fromUnit, long fromUnitId, String body, String secretLevel, Calendar supplementTime) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.addSupplementInfo(infoId, subject, keywords, magazineName, magazineSN, magazineLevel, fromUnit, fromUnitId, body, secretLevel, DateTimeUtils.calendarToTimestamp(supplementTime));
	}
	
	/**
	 * 删除补录的信息
	 * @param infoId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void deleteSupplementInfo(long infoId) throws ServiceException, SoapException {
		InfoContributeService infoContributeService = (InfoContributeService)getSpringService("infoContributeService");
		infoContributeService.deleteSupplementInfo(infoId);
	}
}