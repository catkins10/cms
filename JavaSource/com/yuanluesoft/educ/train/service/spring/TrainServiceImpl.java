package com.yuanluesoft.educ.train.service.spring;

import com.yuanluesoft.educ.train.pojo.TrainConfig;
import com.yuanluesoft.educ.train.service.TrainService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;

public class TrainServiceImpl extends BusinessServiceImpl implements
		TrainService {

	public TrainConfig getTrainConfig() throws ServiceException {
		// TODO 自动生成方法存根
		return (TrainConfig)getDatabaseService().findRecordByHql("from TrainConfig TrainConfig");
	}

}
