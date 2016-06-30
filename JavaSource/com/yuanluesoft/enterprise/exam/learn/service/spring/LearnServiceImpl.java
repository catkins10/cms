package com.yuanluesoft.enterprise.exam.learn.service.spring;

import java.util.List;

import com.yuanluesoft.enterprise.exam.learn.pojo.LearnFile;
import com.yuanluesoft.enterprise.exam.learn.pojo.LearnMission;
import com.yuanluesoft.enterprise.exam.learn.pojo.LearnMissionAccomplish;
import com.yuanluesoft.enterprise.exam.learn.service.LearnService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class LearnServiceImpl extends BusinessServiceImpl implements LearnService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof LearnMission) {
			LearnMission learnMission = (LearnMission)record;
			learnMission.setLearnFile((LearnFile)load(LearnFile.class, learnMission.getFileId()));
		}
		return record;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.learn.service.LearnService#startupLearned(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public int startupLearned(long missionId, SessionInfo sessionInfo) throws ServiceException {
		LearnMissionAccomplish learnMissionAccomplish = getLearnMissionAccomplish(missionId, sessionInfo.getUserId());
		if(learnMissionAccomplish!=null) {
			learnMissionAccomplish.setLastTime(DateTimeUtils.now()); //最后学习时间
			getDatabaseService().updateRecord(learnMissionAccomplish);
			return learnMissionAccomplish.getLearnSeconds()/60;
		}
		learnMissionAccomplish = new LearnMissionAccomplish();
		learnMissionAccomplish.setId(UUIDLongGenerator.generateId()); //ID
		learnMissionAccomplish.setMissionId(missionId); //任务ID
		learnMissionAccomplish.setPersonId(sessionInfo.getUserId()); //用户ID
		learnMissionAccomplish.setPersonName(sessionInfo.getUserName()); //用户名
		learnMissionAccomplish.setBeginTime(DateTimeUtils.now()); //开始学习时间
		learnMissionAccomplish.setLastTime(DateTimeUtils.now()); //最后学习时间
		learnMissionAccomplish.setLearnSeconds(0); //已学习时间数,以秒为单位
		getDatabaseService().saveRecord(learnMissionAccomplish);
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.learn.service.LearnService#updateLearnTime(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void updateLearnTime(long missionId, SessionInfo sessionInfo) throws ServiceException {
		LearnMissionAccomplish learnMissionAccomplish = getLearnMissionAccomplish(missionId, sessionInfo.getUserId());
		int learnSeconds = learnMissionAccomplish.getLearnSeconds() + (int)(System.currentTimeMillis()-learnMissionAccomplish.getLastTime().getTime())/1000;
		learnMissionAccomplish.setLearnSeconds(Math.min(learnSeconds, 999999));
		learnMissionAccomplish.setLastTime(DateTimeUtils.now()); //最后学习时间
		getDatabaseService().updateRecord(learnMissionAccomplish);
	}
	
	/**
	 * 获取任务
	 * @param missionId
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	private LearnMissionAccomplish getLearnMissionAccomplish(long missionId, long personId) throws ServiceException {
		String hql = "from LearnMissionAccomplish LearnMissionAccomplish" +
					 " where LearnMissionAccomplish.missionId=" + missionId +
					 " and LearnMissionAccomplish.personId=" + personId;
		return (LearnMissionAccomplish)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.learn.service.LearnService#listLearnMissionAccomplishs(long)
	 */
	public List listLearnMissionAccomplishs(long missionId) throws ServiceException {
		String hql = "from LearnMissionAccomplish LearnMissionAccomplish" +
					 " where LearnMissionAccomplish.missionId=" + missionId +
					 " order by LearnMissionAccomplish.lastTime";
		return getDatabaseService().findRecordsByHql(hql, 0, 2000);
	}
}