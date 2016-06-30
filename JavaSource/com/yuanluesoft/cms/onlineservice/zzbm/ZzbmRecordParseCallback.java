package com.yuanluesoft.cms.onlineservice.zzbm;

import java.util.Map;

/**
 * 漳州行政服务中心记录解析结果处理器
 * @author linchuan
 *
 */
public interface ZzbmRecordParseCallback {

	/**
	 * 处理解析后的记录
	 * @param propertyTextValues
	 * @param record
	 */
	public void processParsedRecord(Map propertyTextValues, Object record) throws Exception;
}