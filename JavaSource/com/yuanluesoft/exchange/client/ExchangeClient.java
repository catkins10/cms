package com.yuanluesoft.exchange.client;

import java.io.Serializable;
import java.util.List;

import com.yuanluesoft.exchange.client.exception.ExchangeException;
import com.yuanluesoft.exchange.client.packet.data.SendObjectAck;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface ExchangeClient {
	
	/**
	 * 保存或者更新一个记录或对象
	 * @param object
	 * @param receiverNames receiverNames==null,则给所有用户发送
	 * @param delayMilliseconds 延时毫秒数
	 * @throws ServiceException
	 */
	public void synchUpdate(Serializable object, String receiverNames, int delayMilliseconds) throws ExchangeException;
	
	/**
	 * 删除一条记录或对象
	 * @param object
	 * @param receiverNames receiverNames==null,则给所有用户发送
	 * @param delayMilliseconds 延时毫秒数
	 * @throws ExchangeException
	 */
	public void synchDelete(Serializable object, String receiverNames, int delayMilliseconds) throws ExchangeException;
	
	/**
	 * 发送一组文件,返回远程文件路径列表
	 * @param filePaths
	 * @param receiverNames receiverNames==null,则给所有用户发送
	 * @param createDirectoryIfNotExists 如果对应的目录中不存在,自动创建
	 * @param async 是否异步传输
	 * @throws ExchangeException
	 */
	public List sendFiles(List filePaths, String receiverNames, boolean createDirectoryIfNotExists, boolean async) throws ExchangeException;
	
	/**
	 * 删除一组文件或目录
	 * @param filePaths
	 * @param receiverNames
	 * @param async 是否异步传输
	 * @throws ExchangeException
	 */
	public void deleteFiles(List filePaths, String receiverNames, boolean async) throws ExchangeException;
	
	/**
	 * 发送一个对象,返回处理结果
	 * @param object
	 * @param receiverName
	 * @return
	 * @throws ExchangeException
	 */
	public SendObjectAck sendObject(Serializable object, String receiverName) throws ExchangeException;
	
	/**
	 * 数据库查询
	 * @param databaseServiceName
	 * @param hql
	 * @param sql
	 * @param lazyLoadProperties
	 * @param offset
	 * @param limit
	 * @param receiverName
	 * @return
	 * @throws ExchangeException
	 */
	public List databaseQuery(String databaseServiceName, String hql, String sql, List lazyLoadProperties, int offset, int limit, String receiverName) throws ExchangeException;
	
	/**
	 * 获取所有的接收方列表,应用做交换配置时使用
	 * @return
	 * @throws ExchangeException
	 */
	public List listReceiverNames() throws ExchangeException;
	
	/**
	 * 能否数据交换
	 * @return
	 */
	public boolean isExchangeable();
}