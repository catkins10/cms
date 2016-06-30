package com.yuanluesoft.jeaf.usermanage.replicator.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService;

/**
 * 
 * @author linchuan
 *
 */
public class UserReplicateServiceList implements UserReplicateService {
	private List replicateServiceList; //复制服务列表

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#replicate()
	 */
	public void replicate() throws ServiceException {
		for(Iterator iterator = (replicateServiceList==null ? null : replicateServiceList.iterator()); iterator!=null && iterator.hasNext();) {
			UserReplicateService replicateService = (UserReplicateService)iterator.next();
			replicateService.replicate();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#registPerson(com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void registPerson(Person person) throws ServiceException {
		for(Iterator iterator = (replicateServiceList==null ? null : replicateServiceList.iterator()); iterator!=null && iterator.hasNext();) {
			UserReplicateService replicateService = (UserReplicateService)iterator.next();
			replicateService.registPerson(person);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#modifyPerson(com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void modifyPerson(Person person) throws ServiceException {
		for(Iterator iterator = (replicateServiceList==null ? null : replicateServiceList.iterator()); iterator!=null && iterator.hasNext();) {
			UserReplicateService replicateService = (UserReplicateService)iterator.next();
			replicateService.modifyPerson(person);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#deletePerson(long)
	 */
	public void deletePerson(long personId) throws ServiceException {
		for(Iterator iterator = (replicateServiceList==null ? null : replicateServiceList.iterator()); iterator!=null && iterator.hasNext();) {
			UserReplicateService replicateService = (UserReplicateService)iterator.next();
			replicateService.deletePerson(personId);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#registOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org)
	 */
	public void registOrg(Org org) throws ServiceException {
		for(Iterator iterator = (replicateServiceList==null ? null : replicateServiceList.iterator()); iterator!=null && iterator.hasNext();) {
			UserReplicateService replicateService = (UserReplicateService)iterator.next();
			replicateService.registOrg(org);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#modifyOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org)
	 */
	public void modifyOrg(Org org) throws ServiceException {
		for(Iterator iterator = (replicateServiceList==null ? null : replicateServiceList.iterator()); iterator!=null && iterator.hasNext();) {
			UserReplicateService replicateService = (UserReplicateService)iterator.next();
			replicateService.modifyOrg(org);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#deleteOrg(long, long)
	 */
	public void deleteOrg(long orgId, long parentOrgId) throws ServiceException {
		for(Iterator iterator = (replicateServiceList==null ? null : replicateServiceList.iterator()); iterator!=null && iterator.hasNext();) {
			UserReplicateService replicateService = (UserReplicateService)iterator.next();
			replicateService.deleteOrg(orgId, parentOrgId);
		}
	}
	
	/**
	 * @return the replicateServiceList
	 */
	public List getReplicateServiceList() {
		return replicateServiceList;
	}

	/**
	 * @param replicateServiceList the replicateServiceList to set
	 */
	public void setReplicateServiceList(List replicateServiceList) {
		this.replicateServiceList = replicateServiceList;
	}
}