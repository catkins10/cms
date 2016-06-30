package com.yuanluesoft.cms.situation.service.spring;

import java.util.List;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl;
import com.yuanluesoft.cms.situation.pojo.Situation;
import com.yuanluesoft.cms.situation.service.SituationService;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.workflow.client.model.user.ParticipantDepartment;

/**
 * 
 * @author chuan
 *
 */
public class SituationServiceImpl extends PublicServiceImpl implements SituationService {
	private OrgService orgService; //组织机构服务

	public void init() {
		try {
			orgService.appendDirectoryPopedomType("situationTransactor", "民情办理", "unit", DirectoryPopedomType.INHERIT_FROM_PARENT_NO, false, false);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#retrieveProgrammingParticipants(com.yuanluesoft.cms.publicservice.pojo.PublicService, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveProgrammingParticipants(PublicService publicService, String programmingParticipantId, String programmingParticipantName, SessionInfo sessionInfo) throws ServiceException {
		if("situationTransactor".equals(programmingParticipantId)) {
			Situation situation = (Situation)publicService;
			return ListUtils.generateList(getSituationTransactorParticipant(situation.getUnitId(), situation.getUnitName()));
		}
		return super.retrieveProgrammingParticipants(publicService, programmingParticipantId, programmingParticipantName, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.situation.service.SituationService#getSituationTransactorParticipant(long, java.lang.String)
	 */
	public ParticipantDepartment getSituationTransactorParticipant(long unitId, String unitName) throws ServiceException {
		List situationTransactors = orgService.listDirectoryVisitors(unitId, "situationTransactor", true, true, 100);
		ParticipantDepartment participantDepartment = new ParticipantDepartment();
		participantDepartment.setId("" + unitId);
		participantDepartment.setName(unitName);
		if(situationTransactors!=null && !situationTransactors.isEmpty()) {
			participantDepartment.setPersonIds(ListUtils.join(situationTransactors, "id", ",", true));
			participantDepartment.setPersonNames(ListUtils.join(situationTransactors, "name", ",", true));
		}
		return participantDepartment;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.situation.service.SituationService#getHigherSituationUnit(long)
	 */
	public Org getHigherSituationUnit(long unitId) throws ServiceException {
		for(;;) {
			Org parentOrg = (Org)orgService.getParentDirectory(unitId, "unit");
			if(parentOrg==null) {
				break;
			}
			List situationTransactors = orgService.listDirectoryVisitors(parentOrg.getId(), "situationTransactor", true, false, 1);
			if(situationTransactors!=null && !situationTransactors.isEmpty()) {
				return parentOrg;
			}
			unitId = parentOrg.getId();
		}
		return null;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}
}