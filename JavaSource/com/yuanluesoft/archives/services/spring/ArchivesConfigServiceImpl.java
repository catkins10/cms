/*
 * Created on 2006-9-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.services.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.archives.pojo.ArchivesSecureLevel;
import com.yuanluesoft.archives.services.ArchivesConfigService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 *
 * @author linchuan
 *
 */
public class ArchivesConfigServiceImpl extends BusinessServiceImpl implements ArchivesConfigService {
	private RecordControlService recordControlService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesConfigService#listArchivesUnits()
	 */
	public List listArchivesUnits() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from ArchivesUnit ArchivesUnit order by ArchivesUnit.unitCode");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesConfigService#listFonds()
	 */
	public List listFonds() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from ArchivesFonds ArchivesFonds order by ArchivesFonds.fondsCode");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesConfigService#listRotentionPeriods()
	 */
	public List listRotentionPeriods() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from ArchivesRotentionPeriod ArchivesRotentionPeriod order by ArchivesRotentionPeriod.rotentionPeriodCode");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesConfigService#listSecureLevels()
	 */
	public List listSecureLevels() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from ArchivesSecureLevel ArchivesSecureLevel order by ArchivesSecureLevel.secureLevelCode");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesConfigService#listSecureLevelVisitors(java.lang.String)
	 */
	public List listSecureLevelVisitors(String secureLevel) throws ServiceException {
		Long secureLevelId = (Long)getDatabaseService().findRecordByHql("select ArchivesSecureLevel.id from ArchivesSecureLevel ArchivesSecureLevel where ArchivesSecureLevel.secureLevel='" + JdbcUtils.resetQuot(secureLevel) + "'");
		if(secureLevelId==null) {
			return null;
		}
		return recordControlService.listVisitors(secureLevelId.longValue(), ArchivesSecureLevel.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesConfigService#getArchivesUnitCode(java.lang.String)
	 */
	public String getArchivesUnitCode(String archivesUnit) throws ServiceException {
		return (String)getDatabaseService().findRecordByHql("select ArchivesUnit.unitCode from ArchivesUnit ArchivesUnit where ArchivesUnit.unit='" + JdbcUtils.resetQuot(archivesUnit) + "'");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesConfigService#getFondsCode(java.lang.String)
	 */
	public String getFondsCode(String fondsName) throws ServiceException {
		return (String)getDatabaseService().findRecordByHql("select ArchivesFonds.fondsCode from ArchivesFonds ArchivesFonds where ArchivesFonds.fondsName='" + JdbcUtils.resetQuot(fondsName) + "'");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesConfigService#getRotentionPeriodCode(java.lang.String)
	 */
	public String getRotentionPeriodCode(String rotentionPeriod) throws ServiceException {
		return (String)getDatabaseService().findRecordByHql("select ArchivesRotentionPeriod.rotentionPeriodCode from ArchivesRotentionPeriod ArchivesRotentionPeriod where ArchivesRotentionPeriod.rotentionPeriod='" + JdbcUtils.resetQuot(rotentionPeriod) + "'");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesConfigService#getSecureLevelCode(java.lang.String)
	 */
	public String getSecureLevelCode(String secureLevel) throws ServiceException {
		return (String)getDatabaseService().findRecordByHql("select ArchivesSecureLevel.secureLevelCode from ArchivesSecureLevel ArchivesSecureLevel where ArchivesSecureLevel.secureLevel='" + JdbcUtils.resetQuot(secureLevel) + "'");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("fonds".equals(itemsName)) { //全宗号
			return getDatabaseService().findRecordsByHql("select ArchivesFonds.fondsName, ArchivesFonds.fondsCode from ArchivesFonds ArchivesFonds order by ArchivesFonds.fondsCode");
		}
		else if("unit".equals(itemsName)) { //机构或问题配置
			return getDatabaseService().findRecordsByHql("select ArchivesUnit.unit, ArchivesUnit.unitCode from ArchivesUnit ArchivesUnit order by ArchivesUnit.unitCode");
		}
		else if("rotentionPeriod".equals(itemsName)) { //保管期限
			return getDatabaseService().findRecordsByHql("select ArchivesRotentionPeriod.rotentionPeriod, ArchivesRotentionPeriod.rotentionPeriodCode from ArchivesRotentionPeriod ArchivesRotentionPeriod order by ArchivesRotentionPeriod.rotentionPeriodCode");
		}
		else if("secureLevel".equals(itemsName)) { //文件密级配置
			return getDatabaseService().findRecordsByHql("select ArchivesSecureLevel.secureLevel, ArchivesSecureLevel.secureLevelCode from ArchivesSecureLevel ArchivesSecureLevel order by ArchivesSecureLevel.secureLevelCode");
		}
		return null;
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
	public void setRecordControlService(RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}
}