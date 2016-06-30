package com.yuanluesoft.fdi.industry.service.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.fdi.industry.pojo.FdiIndustry;
import com.yuanluesoft.fdi.industry.service.FdiIndustryService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class FdiIndustryServiceImpl extends BusinessServiceImpl implements FdiIndustryService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fdi.customer.service.FdiCustomerService#getIndustry(long)
	 */
	public FdiIndustry getIndustry(long industryId) throws ServiceException {
		return (FdiIndustry)load(FdiIndustry.class, industryId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fdi.industry.service.FdiIndustryService#getIndustryAccessLevel(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char getIndustryAccessLevel(String industryIds, SessionInfo sessionInfo) throws ServiceException {
		char accessLevel = (char)127;
		String[] ids = industryIds.split(",");
		for(int i=0; i<ids.length; i++) {
			String hql = "select max(FdiIndustryPrivilege.accessLevel)" +
						 " from FdiIndustryPrivilege FdiIndustryPrivilege" +
						 " where FdiIndustryPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")" +
						 " and (FdiIndustryPrivilege.recordId=" + ids[i] +
						 " or FdiIndustryPrivilege.recordId=(select min(FdiIndustry.parentCategoryId) from FdiIndustry FdiIndustry where FdiIndustry.id=" + ids[i] + "))";
			Character result = (Character)getDatabaseService().findRecordByHql(hql);
			char level = result==null ? RecordControlService.ACCESS_LEVEL_NONE : result.charValue();
			if(level<accessLevel) {
				accessLevel = level;
			}
		}
		return accessLevel;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fdi.customer.service.FdiCustomerService#createIndustryTree(boolean, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Tree createIndustryTree(boolean editabled, boolean readabled, SessionInfo sessionInfo) throws ServiceException {
		String hql = "select FdiIndustry" +
					 " from FdiIndustry FdiIndustry";
		if(sessionInfo!=null && !sessionInfo.isAnonymous() && (editabled || readabled)) {
			hql += " where FdiIndustry.id in (" + //对分类本身有权限
				   "  select FdiIndustryPrivilege.recordId" +
				   "   from FdiIndustryPrivilege FdiIndustryPrivilege" +
				   "   where FdiIndustryPrivilege.recordId=FdiIndustry.id" +
				   "   and FdiIndustryPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")" +
				   "   and FdiIndustryPrivilege.accessLevel" + (editabled ? ">" : ">=") + "'" + RecordControlService.ACCESS_LEVEL_READONLY + "'" +
				   " )" +
				   " or FdiIndustry.parentCategoryId in (" + //对父分类录有权限
				   "  select FdiIndustryPrivilege.recordId" +
				   "   from FdiIndustryPrivilege FdiIndustryPrivilege" +
				   "   where FdiIndustryPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")" +
				   "   and FdiIndustryPrivilege.accessLevel" + (editabled ? ">" : ">=") + "'" + RecordControlService.ACCESS_LEVEL_READONLY + "'" +
				   " )" +
				   " or FdiIndustry.id in (" + //对子分类有权限
				   "  select FdiIndustry.parentCategoryId" +
				   "   from FdiIndustry FdiIndustry left join FdiIndustry.visitors FdiIndustryPrivilege" +
				   "   where FdiIndustryPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")" +
				   "   and FdiIndustryPrivilege.accessLevel" + (editabled ? ">" : ">=") + "'" + RecordControlService.ACCESS_LEVEL_READONLY + "'" +
				   " )";
		}
		hql += " order by FdiIndustry.category";
		Tree tree = new Tree("0", "行业", "root", null);
		List industries = getDatabaseService().findRecordsByHql(hql);
		if(industries==null || industries.isEmpty()) {
			return tree;
		}
		for(Iterator iterator = ListUtils.getSubListByProperty(industries, "parentCategoryId", new Long(0)).iterator(); iterator.hasNext();) {
			FdiIndustry industry = (FdiIndustry)iterator.next();
			List childIndustries = ListUtils.getSubListByProperty(industries, "parentCategoryId", new Long(industry.getId()));
			TreeNode node = tree.appendChildNode(industry.getId() + "", industry.getCategory(), "industry", null, childIndustries!=null && !childIndustries.isEmpty());
			if(childIndustries!=null && !childIndustries.isEmpty()) {
				for(Iterator childIterator = childIndustries.iterator(); childIterator.hasNext();) {
					industry = (FdiIndustry)childIterator.next();
					node.appendChildNode(industry.getId() + "", industry.getCategory(), "industry", null, false);
				}
			}
		}
		return tree;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fdi.industry.service.FdiIndustryService#listIndustryIds(boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listIndustryIds(boolean editabled, SessionInfo sessionInfo) throws ServiceException {
		String hql = "select FdiIndustry.id" +
					 " from FdiIndustry FdiIndustry" +
					 " where FdiIndustry.id in (" + //对本身有权限
					 "  select FdiIndustryPrivilege.recordId" +
					 "   from FdiIndustryPrivilege FdiIndustryPrivilege" +
					 "   where FdiIndustryPrivilege.recordId=FdiIndustry.id" +
					 "   and FdiIndustryPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")" +
					 "   and FdiIndustryPrivilege.accessLevel>='" + (editabled ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY) + "'" +
					 " )" +
					 " or FdiIndustry.parentCategoryId in (" + //对父分类录有权限
					 "  select FdiIndustryPrivilege.recordId" +
					 "   from FdiIndustryPrivilege FdiIndustryPrivilege" +
					 "   where FdiIndustryPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")" +
					 "   and FdiIndustryPrivilege.accessLevel>='" + (editabled ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY) + "'" +
					 " )";
		return getDatabaseService().findRecordsByHql(hql);
	}
}