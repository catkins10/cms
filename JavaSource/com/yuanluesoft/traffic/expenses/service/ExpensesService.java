package com.yuanluesoft.traffic.expenses.service;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.traffic.expenses.pojo.Expenses;

/**
 * 养路费服务
 * @author yuanluesoft
 *
 */
public interface ExpensesService {
	
	/**
	 * 获取数据库最后更新时间
	 * @return
	 * @throws ServiceException
	 */
	public Timestamp getLastUpdated() throws ServiceException;

	/**
	 * 养路费查询
	 * @param plateNumberPrefix 车牌前缀,闽A
	 * @param plateNumber 车牌号
	 * @param vehicleType 车子类型,小车等
	 * @return
	 * @throws ServiceException
	 */
	public Expenses getExpenses(String plateNumberPrefix, String plateNumber, String vehicleType) throws ServiceException;
}
