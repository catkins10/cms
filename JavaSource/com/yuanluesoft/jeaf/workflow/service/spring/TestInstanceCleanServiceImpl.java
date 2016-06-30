/*
 * Created on 2006-9-9
 *
 */
package com.yuanluesoft.jeaf.workflow.service.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.workflow.service.TestInstanceCleanService;

/**
 * 
 * @author linchuan
 *
 */
public class TestInstanceCleanServiceImpl implements TestInstanceCleanService {
    private List testInstanceCleanServices; //测试流程删除服务列表

    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.workflowform.service.TestInstanceCleanService#clean()
     */
    public void clean() throws ServiceException {
    	Logger.info("**************** clean test instances **************");
        if(testInstanceCleanServices==null) {
        	return;
        }
        for(Iterator iterator = testInstanceCleanServices.iterator(); iterator.hasNext();) {
        	((TestInstanceCleanService)iterator.next()).clean();
        }
    }

	/**
	 * @return Returns the testInstanceCleanServices.
	 */
	public List getTestInstanceCleanServices() {
		return testInstanceCleanServices;
	}
	/**
	 * @param testInstanceCleanServices The testInstanceCleanServices to set.
	 */
	public void setTestInstanceCleanServices(List testInstanceCleanServices) {
		this.testInstanceCleanServices = testInstanceCleanServices;
	}
}