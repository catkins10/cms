package com.yuanluesoft.aic.barcode.service.spring;

import com.yuanluesoft.aic.barcode.pojo.Barcode;
import com.yuanluesoft.aic.barcode.service.BarcodeService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class BarcodeServiceImpl extends BusinessServiceImpl implements BarcodeService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.aic.barcode.service.BarcodeCompanyService#createBarcodes(java.lang.String, long)
	 */
	public void createBarcodes(String barcodes, long companyId) throws ServiceException {
		if(barcodes==null || barcodes.isEmpty()) {
			return;
		}
		String[] values = barcodes.replace('，', ',').split(",");
		for(int i=0; i<values.length; i++) {
			values[i] = values[i].trim();
			if(values[i].length()!=13) {
				continue;
			}
			Barcode barcode = new Barcode();
			barcode.setId(UUIDLongGenerator.generateId()); //ID
			barcode.setCompanyId(companyId); //厂商ID
			barcode.setBarcode(values[i]); //条码
			getDatabaseService().saveRecord(barcode);
		}
	}
}