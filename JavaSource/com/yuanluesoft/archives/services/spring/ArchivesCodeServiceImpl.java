/*
 * Created on 2006-9-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.services.spring;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.archives.pojo.ArchivesCode;
import com.yuanluesoft.archives.services.ArchivesCodeService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.numeration.service.NumerationCallback;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 *
 * @author linchuan
 *
 */
public class ArchivesCodeServiceImpl extends BusinessServiceImpl implements ArchivesCodeService {
	private NumerationService numerationService; //编号服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.archives.services.ArchivesCodeService#generateArchivesCode(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, int[])
	 */
	public String generateArchivesCode(String archivesType, final String fondsCode, final int filingYear, final String rotentionPeriod, final String archivesUnit, final int[] serialNumber) throws ServiceException {
		ArchivesCode archivesCode = (ArchivesCode)getDatabaseService().findRecordByHql("from ArchivesCode ArchivesCode where ArchivesCode.archivesType='" + JdbcUtils.resetQuot(archivesType) + "'");
		NumerationCallback numerationCallback = new NumerationCallback() {
			public Object getFieldValue(String fieldName, int fieldLength) {
				if("全宗号".equals(fieldName)) {
					return fondsCode;
				}
				else if("归档年度".equals(fieldName)) {
					return new Integer(filingYear);
				}
				else if("保管期限".equals(fieldName)) {
					return rotentionPeriod;
				}
				else if("机构或问题".equals(fieldName)) {
					return archivesUnit;
				}
				else if("序号".equals(fieldName)) {
					return serialNumber[0]==0 ? null : new Integer(serialNumber[0]);
				}
				return null;
			}
		};
		if(serialNumber[0]==0) { //件号为0,获取新件号
			serialNumber[0] = numerationService.getNextSequence("档案管理", "档号", archivesCode.getCodeConfig(), false, numerationCallback);
		}
		return numerationService.generateNumeration("档案管理", "档号", archivesCode.getCodeConfig(), true, numerationCallback);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(java.lang.Object)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		ArchivesCode archivesCode = (ArchivesCode)record;
		if(getDatabaseService().findRecordByHql("from ArchivesCode ArchivesCode where ArchivesCode.archivesType='" + JdbcUtils.resetQuot(archivesCode.getArchivesType()) + "'")!=null) {
			List errors = new ArrayList();
			errors.add(archivesCode.getArchivesType() + "的档号配置已存在，档号不允许重复配置！");
			return errors;
		}
		return null;
	}

	/**
	 * @return the numerationService
	 */
	public NumerationService getNumerationService() {
		return numerationService;
	}

	/**
	 * @param numerationService the numerationService to set
	 */
	public void setNumerationService(NumerationService numerationService) {
		this.numerationService = numerationService;
	}
}
