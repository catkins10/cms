package com.yuanluesoft.msa.lawmen.service.spring;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.msa.lawmen.pojo.MsaLawmen;
import com.yuanluesoft.msa.lawmen.service.MsaLawmenService;

/**
 * 
 * @author linchuan
 *
 */
public class MsaLawmenServiceImpl extends BusinessServiceImpl implements MsaLawmenService {
	private AttachmentService attachmentService; //附件服务
	private ExchangeClient exchangeClient; //数据交换客户端

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		record = super.save(record);
		if(record instanceof MsaLawmen) {
			exchangeClient.synchUpdate(record, null, 2000);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		if(record instanceof MsaLawmen) {
			exchangeClient.synchUpdate(record, null, 2000);
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(record instanceof MsaLawmen) {
			exchangeClient.synchDelete(record, null, 2000);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(com.yuanluesoft.jeaf.database.Record, boolean)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		MsaLawmen lawmen = (MsaLawmen)record;
		//检查执法证编号
		String hql = "select MsaLawmen.id" +
					 " from MsaLawmen MsaLawmen" +
					 " where MsaLawmen.id!=" + lawmen.getId() +
					 " and MsaLawmen.certificateNumber='" + JdbcUtils.resetQuot(lawmen.getCertificateNumber()) + "'";
		if(getDatabaseService().findRecordByHql(hql)!=null) {
			return ListUtils.generateList("执法证编号不允许重复");
		}
		return super.validateDataIntegrity(record, isNew);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.msa.lawmen.service.MsaLawmenService#importData(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void importData(long importId, SessionInfo sessionInfo) throws ServiceException {
		List attachments = attachmentService.list("msa/lawmen", "data", importId, false, 1, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		Attachment attachment = (Attachment)attachments.get(0);
		Workbook wb = null;
		try {
			//导入数据
			wb = Workbook.getWorkbook(new File(attachment.getFilePath())); //构造Workbook（工作薄）对象
			for(int i=0; i<wb.getSheets().length; i++) {
				Sheet sheet = wb.getSheet(i);
				importSheet(sheet); //导入SHEET
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException("导入失败");
		}
		finally {
			try {
				wb.close();
			}
			catch (Exception e) {
				
			}
			attachmentService.deleteAll("msa/lawmen", "data", importId);
		}
	}
	
	/**
	 * 导入一个工作表
	 * @param sheet
	 * @throws ServiceException
	 */
	private void importSheet(Sheet sheet) throws ServiceException {
		for(int i=1; i<sheet.getRows(); i++) {
			Cell[] cells = sheet.getRow(i);
			//1	何易培	男	2010年3月10日	0800010001
			String name = ExcelUtils.getCellString(cells, 1);
			if(name==null || name.isEmpty()) {
				continue;
			}
			String certificateNumber = ExcelUtils.getCellString(cells, 4);
			if(certificateNumber==null || certificateNumber.isEmpty()) {
				continue;
			}
			//检查执法证编号是否已经存在
			MsaLawmen lawmen = (MsaLawmen)getDatabaseService().findRecordByHql("from MsaLawmen MsaLawmen where MsaLawmen.certificateNumber='" + JdbcUtils.resetQuot(certificateNumber) + "'");
			boolean isNew = (lawmen==null);
			if(isNew) {
				lawmen = new MsaLawmen();
				lawmen.setId(UUIDLongGenerator.generateId()); //ID
			}
			lawmen.setName(name); //姓名
			lawmen.setSex("男".equals(ExcelUtils.getCellString(cells, 2)) ? 'M' : 'F'); //性别
			try {
				lawmen.setCertificateDate(DateTimeUtils.parseDate(ExcelUtils.getCellString(cells, 3), "yyyy年M月d日"));
			} 
			catch(ParseException e) {
				throw new ServiceException(e);
			}
			//执法证发证日期
			lawmen.setCertificateNumber(certificateNumber); //执法证编号
			if(isNew) {
				save(lawmen);
			}
			else {
				update(lawmen);
			}
		}
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

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}
}