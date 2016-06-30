package com.yuanluesoft.aic.barcode.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface BarcodeService extends BusinessService {

	/**
	 * 创建条码
	 * @param barcodes
	 * @param companyId
	 * @return
	 * @throws ServiceException
	 */
	public void createBarcodes(String barcodes, long companyId) throws ServiceException;
}