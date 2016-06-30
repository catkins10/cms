/*
 * Created on 2006-9-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.administrative.services.spring;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.archives.administrative.pojo.AdministrativeArchives;
import com.yuanluesoft.archives.administrative.services.AdministrativeArchivesService;
import com.yuanluesoft.archives.services.ArchivesCodeService;
import com.yuanluesoft.archives.services.ArchivesConfigService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.pojo.RecordPrivilege;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 *
 * @author linchuan
 *
 */
public class AdministrativeArchivesServiceImpl implements AdministrativeArchivesService {
	private ArchivesConfigService archivesConfigService; //档案配置服务
	private ArchivesCodeService archivesCodeService; //档号服务
	private AttachmentService attachmentService; //附件管理服务
	private RecordControlService recordControlService; //记录权限控制服务
	private AccessControlService accessControlService; //访问控制服务
	private DatabaseService databaseService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.administrative.services.AdministrativeArchivesService#filing(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Date, int, int, int, java.util.List, java.util.List, java.util.List, java.lang.String)
	 */
	public void filing(String subject, String keyword, String type,
			String docCategory, String docWord, String responsibilityPerson,
			String fondsName, String secureLevel,
			String rotentionPeriod, String unit, Date signDate,
			int filingYear, int count, int pageCount, List bodyFileNames,
			List attachmentFileNames, List readers, String remark)
			throws ServiceException {
		AdministrativeArchives pojoArchives = new AdministrativeArchives();
		pojoArchives.setId(UUIDLongGenerator.generateId()); //ID
		//pojoArchives.setCategoryId(); //分类ID
		pojoArchives.setCount(count); //数量
		pojoArchives.setDocCategory(docCategory); //公文种类
		pojoArchives.setDocWord(docWord); //文件字
		pojoArchives.setFilingYear(filingYear); //归档年度
		pojoArchives.setKeyword(keyword); //主题词
		pojoArchives.setPageCount(pageCount); //页数
		pojoArchives.setRemark(remark); //备注
		pojoArchives.setResponsibilityPerson(responsibilityPerson); //责任者
		pojoArchives.setSubject(subject); //文件题名
		pojoArchives.setArchivesType(type); //文件类型
		pojoArchives.setFilingDate(DateTimeUtils.date()); //归档日前
		pojoArchives.setSignDate(new java.sql.Date(signDate==null ? System.currentTimeMillis() : signDate.getTime())); //成文日期
		pojoArchives.setFondsName(fondsName); //全宗名称
		pojoArchives.setFondsCode(archivesConfigService.getFondsCode(fondsName)); //全宗号
		pojoArchives.setUnit(unit); //机构或问题
		pojoArchives.setUnitCode(archivesConfigService.getArchivesUnitCode(unit)); //机构或问题编号
		pojoArchives.setSecureLevel(secureLevel); //文件密级
		pojoArchives.setSecureLevelCode(archivesConfigService.getSecureLevelCode(secureLevel)); //密级编号
		pojoArchives.setRotentionPeriod(rotentionPeriod); //保管期限
		pojoArchives.setRotentionPeriodCode(archivesConfigService.getRotentionPeriodCode(rotentionPeriod)); //保管期限编号
		//获取件号,生成档号
		int[] serialNumber = new int[1];
		serialNumber[0] = 0;
		pojoArchives.setArchivesCode(archivesCodeService.generateArchivesCode("文书档案", pojoArchives.getFondsCode(), pojoArchives.getFilingYear(), pojoArchives.getRotentionPeriodCode(), pojoArchives.getUnitCode(), serialNumber));
		pojoArchives.setSerialNumber(serialNumber[0]);
		databaseService.saveRecord(pojoArchives);
		//上传正文
		if(bodyFileNames!=null) {
			for(Iterator iterator = bodyFileNames.iterator(); iterator.hasNext();) {
				attachmentService.uploadFile("archives/administrative", "body", null, pojoArchives.getId(), (String)iterator.next());
			}
		}
		//上传附件
		if(attachmentFileNames!=null) {
			for(Iterator iterator = attachmentFileNames.iterator(); iterator.hasNext();) {
				attachmentService.uploadFile("archives/administrative", "attachment", null, pojoArchives.getId(), (String)iterator.next());
			}
		}
		//设置访问者
		if(readers==null || readers.isEmpty()) { //允许所有人访问
			recordControlService.appendVisitor(pojoArchives.getId(), AdministrativeArchives.class.getName(), 0, RecordControlService.ACCESS_LEVEL_READONLY);
		}
		else {
			for(Iterator iterator = readers.iterator(); iterator.hasNext();) {
				RecordPrivilege recordPrivilege = (RecordPrivilege)iterator.next();
				recordControlService.appendVisitor(pojoArchives.getId(), AdministrativeArchives.class.getName(), recordPrivilege.getVisitorId(), RecordControlService.ACCESS_LEVEL_READONLY);
			}
		}
		//将文书档案管理员设为编辑者
		List managers = accessControlService.listVisitors("archives/administrative", "manageUnit_administrativeArchivesManage");
		if(managers!=null) {
			for(Iterator iterator = managers.iterator(); iterator.hasNext();) {
				Element manager = (Element)iterator.next();
				recordControlService.appendVisitor(pojoArchives.getId(), AdministrativeArchives.class.getName(), Long.parseLong(manager.getId()), RecordControlService.ACCESS_LEVEL_EDITABLE);
			}
		}
	}

	/**
	 * @return Returns the archivesCodeService.
	 */
	public ArchivesCodeService getArchivesCodeService() {
		return archivesCodeService;
	}
	/**
	 * @param archivesCodeService The archivesCodeService to set.
	 */
	public void setArchivesCodeService(ArchivesCodeService archivesCodeService) {
		this.archivesCodeService = archivesCodeService;
	}
	/**
	 * @return Returns the archivesConfigService.
	 */
	public ArchivesConfigService getArchivesConfigService() {
		return archivesConfigService;
	}
	/**
	 * @param archivesConfigService The archivesConfigService to set.
	 */
	public void setArchivesConfigService(
			ArchivesConfigService archivesConfigService) {
		this.archivesConfigService = archivesConfigService;
	}
	/**
	 * @return Returns the databaseService.
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	/**
	 * @param databaseService The databaseService to set.
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	/**
	 * @return Returns the recordControlService.
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}
	/**
	 * @param recordControlService The recordControlService to set.
	 */
	public void setRecordControlService(
			RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}
	/**
	 * @return Returns the accessControlService.
	 */
	public AccessControlService getAccessControlService() {
		return accessControlService;
	}
	/**
	 * @param accessControlService The accessControlService to set.
	 */
	public void setAccessControlService(
			AccessControlService accessControlService) {
		this.accessControlService = accessControlService;
	}

	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
}
