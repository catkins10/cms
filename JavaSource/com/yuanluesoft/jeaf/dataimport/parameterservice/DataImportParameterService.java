package com.yuanluesoft.jeaf.dataimport.parameterservice;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.dataimport.model.DataImportParameter;

/**
 * 
 * @author chuan
 *
 */
public interface DataImportParameterService extends BusinessService {

	/**
	 * 获取站点的导入参数配置
	 * @param importDataForm
	 * @return
	 * @throws Exception
	 */
	public DataImportParameter loadDataImportParameter(long targetSiteId, String dataImportServiceClass) throws Exception;
	
	/**
	 * 保存站点导入参数配置
	 * @param targetSiteId
	 * @param parameter
	 * @throws Exception
	 */
	public void saveDataImportParameter(long targetSiteId, String dataImportServiceClass, DataImportParameter parameter) throws Exception;
}