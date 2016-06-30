package com.yuanluesoft.jeaf.numeration.service;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 编号服务
 * @author linchuan
 *
 */
public interface NumerationService {
	
	/**
	 * 生成编号
	 * @param applicationTitle 应用名称,如:信息公开
	 * @param numerationName 编号名称,如:索引号
	 * @param numerationConfig 编号配置,如：<项目分类><年*4>年<月*2>月<日*2>日[<序号*3>]号
	 * @param preview 是否只是预览
	 * @param numerationCallback 编号服务回调
	 * @return
	 * @throws ServiceException
	 */
	public String generateNumeration(String applicationTitle, String numerationName, String numerationConfig, boolean preview, NumerationCallback numerationCallback) throws ServiceException;
	
	/**
	 * 获取下一个顺序号
	 * @param applicationTitle 应用名称,如:信息公开
	 * @param numerationName 编号名称,如:索引号
	 * @param numerationConfig 编号配置,如：<项目分类><年*4>年<月*2>月<日*2>日[<序号*3>]号
	 * @param preview 是否只是预览
	 * @param numerationCallback 编号服务回调
	 * @return
	 * @throws ServiceException
	 */
	public int getNextSequence(String applicationTitle, String numerationName, String numerationConfig, boolean preview, NumerationCallback numerationCallback) throws ServiceException;
	
	/**
	 * 设置顺序号
	 * @param applicationTitle 应用名称,如:信息公开
	 * @param numerationName 编号名称,如:索引号
	 * @param numerationConfig 编号配置,如：<项目分类><年*4>年<月*2>月<日*2>日[<序号*3>]号
	 * @param sequence 顺序号
	 * @param numerationCallback 编号服务回调
	 * @throws ServiceException
	 */
	public void setSequence(String applicationTitle, String numerationName, String numerationConfig, int sequence, NumerationCallback numerationCallback) throws ServiceException;
}