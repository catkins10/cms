package com.yuanluesoft.jeaf.numeration.service;

/**
 * 编号服务回调
 * @author linchuan
 *
 */
public interface NumerationCallback {

	/**
	 * 按字段名称获取字段值
	 * @param fieldName
	 * @param fieldLength
	 * @return
	 */
	public Object getFieldValue(String fieldName, int fieldLength);
}
