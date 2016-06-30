/*
 * Created on 2006-9-10
 *
 */
package com.yuanluesoft.j2oa.dispatch.service;

import java.util.List;

import com.yuanluesoft.j2oa.dispatch.pojo.Dispatch;
import com.yuanluesoft.j2oa.dispatch.pojo.DispatchDocWordConfig;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 *
 * @author linchuan
 *
 */
public interface DispatchDocWordService extends BusinessService {
	
	/**
	 * 生成文件字
	 * @param dispatch
	 * @param isWorkflowTest
	 * @throws ServiceException
	 */
	public void generateDocWord(Dispatch dispatch, boolean isWorkflowTest) throws ServiceException;
	
	/**
	 * 更新组
	 * @param docWordConfig
	 * @param unionDocWords
	 */
	public void updateGroup(DispatchDocWordConfig docWordConfig, String unionDocWords) throws ServiceException;
	
	/**
	 * 获取联合编号的文件字列表
	 * @param docWordConfig
	 * @return
	 * @throws ServiceException
	 */
	public String getUnionDocWords(DispatchDocWordConfig docWordConfig) throws ServiceException;
	
	/**
	 * 获取文件字列表
	 * @return
	 * @throws ServiceException
	 */
	public List listDocWords() throws ServiceException;
}
