package com.yuanluesoft.cms.situation.service;

import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.workflow.client.model.user.ParticipantDepartment;

/**
 * 
 * @author chuan
 *
 */
public interface SituationService extends PublicService {

	/**
	 * 获取民情办理人并转换为流程办理人
	 * @param unitId
	 * @param unitName
	 * @return
	 * @throws ServiceException
	 */
	public ParticipantDepartment getSituationTransactorParticipant(long unitId, String unitName) throws ServiceException;

	/**
	 * 获取民情办理上级单位
	 * @param unitId
	 * @return
	 * @throws ServiceException
	 */
	public Org getHigherSituationUnit(long unitId) throws ServiceException;
}