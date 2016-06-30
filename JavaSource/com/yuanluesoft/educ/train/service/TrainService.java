package com.yuanluesoft.educ.train.service;

import com.yuanluesoft.educ.train.pojo.TrainConfig;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

public interface TrainService extends BusinessService {
	
	/**
     * 获取培训配置
     * @return
     * @throws ServiceException
     */
    public TrainConfig getTrainConfig() throws ServiceException;
}
