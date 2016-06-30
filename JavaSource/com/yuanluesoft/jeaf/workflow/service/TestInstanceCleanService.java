/*
 * Created on 2006-9-9
 *
 */
package com.yuanluesoft.jeaf.workflow.service;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface TestInstanceCleanService {
    
    /**
     * 清除测试工作流实例
     * @throws ServiceException
     */
    public void clean() throws ServiceException;
}
