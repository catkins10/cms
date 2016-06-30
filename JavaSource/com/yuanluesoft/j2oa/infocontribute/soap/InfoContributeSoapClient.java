package com.yuanluesoft.j2oa.infocontribute.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import com.yuanluesoft.j2oa.infocontribute.soap.model.InfoContribute;
import com.yuanluesoft.j2oa.infocontribute.soap.model.InfoReviseResult;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.FileUtils;

/**
 * 信息投稿WEB服务客户端
 * @author linchuan
 *
 */
public class InfoContributeSoapClient {
	private SoapConnectionPool soapConnectionPool; //SOAP连接池
	private SoapPassport soapPassport; //SOAP许可证

	public InfoContributeSoapClient(SoapConnectionPool soapConnectionPool, SoapPassport soapPassport) {
		super();
		this.soapConnectionPool = soapConnectionPool;
		this.soapPassport = soapPassport;
	}
	
	/**
	 * 添加或者更新刊物定义
	 * @param id
	 * @param name
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void addOrUpdateMagazineDefine(long id, String name, String unitName) throws ServiceException {
		try {
			Object[] args = {new Long(id), name, unitName};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "addOrUpdateMagazineDefine", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 删除刊物定义
	 * @param id
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void deleteMagazineDefine(long id) throws ServiceException {
		try {
			Object[] args = {new Long(id)};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "deleteMagazineDefine", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 获取投稿列表
	 * @param beginTime
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public InfoContribute[] listContributeInfos(String unitName, Timestamp beginTime, int offset, int limit)  throws ServiceException {
		try {
			Object[] args = {unitName, DateTimeUtils.timestampToCalendar(beginTime), new Integer(offset), new Integer(limit)};
			return (InfoContribute[])soapConnectionPool.invokeRemoteMethod("InfoContributeService", "listContributeInfos", soapPassport, args, new Class[]{InfoContribute.class});
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 读取附件
	 * @param infoId
	 * @param fileNames
	 * @param savePath
	 * @throws ServiceException
	 */
	public void readAttachments(long infoId, String[] fileNames, String savePath) throws ServiceException {
		if(fileNames==null || fileNames.length==0) {
			return;
		}
		Base64Decoder base64Decoder = new Base64Decoder();
		for(int i=0; i<fileNames.length; i++) {
			FileOutputStream output = null;
			try {
				output = new FileOutputStream(savePath + fileNames[i].replaceAll("•", "."));
				for(int offset = 0; ; offset+=65536) {
					//public String readAttachment(long infoId, String fileName, int offset, int len)
					Object[] args = {new Long(infoId), fileNames[i], new Integer(offset), new Integer(65536)};
					String base64FileData = (String)soapConnectionPool.invokeRemoteMethod("InfoContributeService", "readAttachment", soapPassport, args, null);
					if(base64FileData==null) {
						break;
					}
					output.write(base64Decoder.decode(base64FileData));
				}
				output.close();
			}
			catch(Exception e) {
				try {
					output.close();
				}
				catch(IOException ioe) {
					
				}
				FileUtils.deleteFile(savePath + fileNames[i]);
				throw new ServiceException(e);
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
	public void reviseInfo(long reviseId, long infoId, String reviseOpinion, long revisePersonId, String revisePersonName, String revisePersonTel) throws ServiceException {
		try {
			Object[] args = {new Long(reviseId), new Long(infoId), reviseOpinion, new Long(revisePersonId), revisePersonName, revisePersonTel};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "reviseInfo", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 获取退改稿修改结果
	 * @param reviseId
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public InfoReviseResult getInfoReviseResult(long reviseId) throws ServiceException {
		try {
			Object[] args = {new Long(reviseId)};
			return (InfoReviseResult)soapConnectionPool.invokeRemoteMethod("InfoContributeService", "getInfoReviseResult", soapPassport, args, new Class[]{InfoReviseResult.class});
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
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
	public void addUsage(long useId, String infoIds, int level, Timestamp sendTime, Timestamp useTime, long magazineId, String magazine) throws ServiceException {
		try {
			if(infoIds==null || infoIds.isEmpty()) {
				return;
			}
			Object[] args = {new Long(useId), infoIds, new Integer(level), DateTimeUtils.timestampToCalendar(sendTime), DateTimeUtils.timestampToCalendar(useTime), new Long(magazineId), magazine};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "addUsage", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 删除采用情况
	 * @param useId
	 * @throws ServiceException
	 */
	public void deleteUsage(long useId) throws ServiceException {
		try {
			Object[] args = {new Long(useId)};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "deleteUsage", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
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
	public void addInstruct(long instructId, String infoIds, String leader, int level, String instruct, Timestamp instructTime, long creatorId, String creator, Timestamp created) throws ServiceException {
		try {
			if(infoIds==null || infoIds.isEmpty()) {
				return;
			}
			Object[] args = {new Long(instructId), infoIds, leader, new Integer(level), instruct, DateTimeUtils.timestampToCalendar(instructTime), new Long(creatorId), creator, DateTimeUtils.timestampToCalendar(created)};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "addInstruct", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 删除领导批示
	 * @param instructId
	 * @throws ServiceException
	 */
	public void deleteInstruct(long instructId) throws ServiceException {
		try {
			Object[] args = {new Long(instructId)};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "deleteInstruct", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
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
	 */
	public void addMagazine(long magazineId, String name, Timestamp issueTime, long sn, long snTotal, int level, String useInfoIds, String visitorIds) throws ServiceException {
		try {
			//public void addMagazine(long magazineId, String name, Calendar issueTime, long sn, long snTotal, int level, String useInfoIds, String visitorIds)
			Object[] args = {new Long(magazineId), name, DateTimeUtils.timestampToCalendar(issueTime),  new Long(sn), new Long(snTotal), new Integer(level), useInfoIds, visitorIds};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "addMagazine", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
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
	public void uploadMagazineFile(long magazineId, String magazineFilePath) throws ServiceException {
		try {
			if(magazineFilePath==null) {
				return;
			}
			byte[] buffer = new byte[65536]; //64K
			byte[] base64Bytes = new byte[(int)(buffer.length * 1.35)]; //64K/3*4
			Base64Encoder base64Encoder = new Base64Encoder();
			FileInputStream input = null;
			try {
				File file = new File(magazineFilePath);
				input = new FileInputStream(file);
				int readLen;
				while((readLen=input.read(buffer))!=-1) {
					int base64Len = base64Encoder.encode(buffer, 0, readLen, base64Bytes);
					//public void uploadMagazineFile(long magazineId, String fileName, String base64FileData)
					Object[] args = {new Long(magazineId), file.getName(), new String(base64Bytes, 0, base64Len)};
					soapConnectionPool.invokeRemoteMethod("InfoContributeService", "uploadMagazineFile", soapPassport, args, null);
				}
			}
			finally {
				input.close();
			}
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 删除刊物
	 * @param magazineId
	 * @throws ServiceException
	 */
	public void deleteMagazine(long magazineId) throws ServiceException {
		try {
			Object[] args = {new Long(magazineId)};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "deleteMagazine", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
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
	public void addSupplementInfo(long infoId, String subject, String keywords, String magazineName, int magazineSN, int magazineLevel, String fromUnit, long fromUnitId, String body, String secretLevel, Timestamp supplementTime) throws ServiceException {
		try {
			Object[] args = {new Long(infoId), subject, keywords, magazineName, new Integer(magazineSN), new Integer(magazineLevel), fromUnit, new Long(fromUnitId), body, secretLevel, DateTimeUtils.timestampToCalendar(supplementTime)};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "addSupplementInfo", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 删除补录的信息
	 * @param infoId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void deleteSupplementInfo(long infoId) throws ServiceException {
		try {
			Object[] args = {new Long(infoId)};
			soapConnectionPool.invokeRemoteMethod("InfoContributeService", "deleteSupplementInfo", soapPassport, args, null);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}