package com.yuanluesoft.chd.evaluation.service.spring;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationCompany;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDetail;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectory;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectoryPopedom;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectorySubjection;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationGenerator;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationIndicator;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationLevel;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlant;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantDetail;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantRule;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantType;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPoint;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPrerequisites;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPrerequisitesScore;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationRule;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationRuleScore;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryType;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryPopedom;
import com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationDirectoryServiceImpl extends DirectoryServiceImpl implements EvaluationDirectoryService {
	private ThreadLocal threadLocal = new ThreadLocal();
	private PageService pageService; //页面服务,生成静态页面用

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getBaseDirectoryClass()
	 */
	public Class getBaseDirectoryClass() {
		return ChdEvaluationDirectory.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryLinkClass()
	 */
	public Class getDirectoryLinkClass() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryTypes()
	 */
	public DirectoryType[] getDirectoryTypes() {
		return new DirectoryType[] {
				new DirectoryType("plantType", "company", "发电企业类型", ChdEvaluationPlantType.class, "/chd/evaluation/icons/plantType.gif", null, false), 
				new DirectoryType("company", "company", "公司", ChdEvaluationCompany.class, "/chd/evaluation/icons/company.gif", null, true),
				new DirectoryType("plant", "company", "发电企业", ChdEvaluationPlant.class, "/chd/evaluation/icons/plant.gif", null, true),
				new DirectoryType("rule", "plantType,rule", "评价项目", ChdEvaluationRule.class, "/chd/evaluation/icons/rule.gif", null, false),
				new DirectoryType("detail", "rule", "评价细则", ChdEvaluationDetail.class, "/chd/evaluation/icons/detail.gif", null, false),
				new DirectoryType("plantRule", "plant,plantRule", "评价项目", ChdEvaluationPlantRule.class, "/chd/evaluation/icons/rule.gif", null, false),
				new DirectoryType("plantDetail", "plantRule", "评价细则", ChdEvaluationPlantDetail.class, "/chd/evaluation/icons/detail.gif", null, false)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getDirectoryPopedomTypes()
	 */
	public DirectoryPopedomType[] getDirectoryPopedomTypes() {
		return new DirectoryPopedomType[] {
				new DirectoryPopedomType("manager", "管理员", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, true, true),
				new DirectoryPopedomType("visitor", "访问者", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true),
				new DirectoryPopedomType("evalVisitor", "自查情况检查", "all", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true),
				//new DirectoryPopedomType("declare", "星级企业申报人", true, false, true),
				//new DirectoryPopedomType("declareApproval", "星级企业审核人", false, false, true), //不需要被继承
				new DirectoryPopedomType("leader", "负责人", "plant,plantRule,plantDetail", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true),
				new DirectoryPopedomType("transactor", "责任人", "plant,plantRule,plantDetail", DirectoryPopedomType.INHERIT_FROM_PARENT_ALWAYS, false, true)};
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return "/chd/evaluation/admin/dataView.shtml?directoryId=" + directoryId;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#getTreeRootNodeIcon()
	 */
	public String getTreeRootNodeIcon() {
		return "/chd/evaluation/icons/root.gif";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#deleteDataInDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public void deleteDataInDirectory(Directory directory) throws ServiceException {
		//不删除已经上传的数据
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#listChildDirectoriesOrderBy(long)
	 */
	protected String listChildDirectoriesOrderBy(long directoryId) {
		return "ChdEvaluationDirectory.created, ChdEvaluationDirectory.id";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.system.services.InitializeService#init(java.lang.String, long, java.lang.String)
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException {
		if(getDirectory(0)!=null) {
			return false;
		}
		
		//创建根目录(集团公司)
		ChdEvaluationCompany company = new ChdEvaluationCompany();
		company.setId(0);
		company.setDirectoryName(systemName);
		company.setDirectoryType("company");
		getDatabaseService().saveRecord(company);
		
		//更新隶属关系
		ChdEvaluationDirectorySubjection subjection = new ChdEvaluationDirectorySubjection();
		subjection.setId(UUIDLongGenerator.generateId()); //ID
		subjection.setDirectoryId(0); //目录ID
		subjection.setParentDirectoryId(0); //上级ID
		getDatabaseService().saveRecord(subjection);
		
		//添加管理员
		ChdEvaluationDirectoryPopedom manager = new ChdEvaluationDirectoryPopedom();
		manager.setId(UUIDLongGenerator.generateId()); //ID
		manager.setDirectoryId(0); //目录ID
		manager.setUserId(managerId); //管理员ID
		manager.setUserName(managerName); //管理员昵称
		manager.setPopedomName("manager"); //访问级别
		manager.setIsInherit('0'); //不是继承的
		getDatabaseService().saveRecord(manager);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof ChdEvaluationDirectory) {
			ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)record;
			if(evaluationDirectory.getDataWorkflowId()!=null && evaluationDirectory.getDataWorkflowId().isEmpty()) {
				evaluationDirectory.setDataWorkflowId(null);
			}
			if(evaluationDirectory.getSelfEvalWorkflowId()!=null && evaluationDirectory.getSelfEvalWorkflowId().isEmpty()) {
				evaluationDirectory.setSelfEvalWorkflowId(null);
			}
			if(evaluationDirectory.getSelfEvalEnd()!=null && evaluationDirectory.getSelfEvalEnd().isEmpty()) {
				evaluationDirectory.setSelfEvalEnd(null);
			}
			if(evaluationDirectory.getSelfEvalUrgency()!=null && evaluationDirectory.getSelfEvalUrgency().isEmpty()) {
				evaluationDirectory.setSelfEvalUrgency(null);
			}
		}
		record = super.save(record);
		if(record instanceof ChdEvaluationPrerequisites) { //必备条件
			copyPrerequisites((ChdEvaluationPrerequisites)record);
		}
		else if(record instanceof ChdEvaluationIndicator) { //主要指标
			copyIndicator((ChdEvaluationIndicator)record);
		}
		else if(record instanceof ChdEvaluationPoint) { //考评要点
			copyPoint((ChdEvaluationPoint)record);
		}
		if(!(record instanceof ChdEvaluationDirectory)) {
			return record;
		}
		ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)record;
		if(evaluationDirectory.getSourceDirectoryId()>0) { //如果本身就是继承来的,不处理
			return evaluationDirectory;
		}
		try {
			if(record instanceof ChdEvaluationCompany) { //新建公司,复制上级的发电企业分类及子目录
				copyPlantTypes((ChdEvaluationCompany)record);
			}
			else if(record instanceof ChdEvaluationPlant) { //新建发电企业,根据企业类型,复制对应的评价项目
				copyPlantRules((ChdEvaluationPlant)record);
				pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
			else if(record instanceof ChdEvaluationPlantType) { //新增企业类型,复制到子公司、分公司
				copyPlantType((ChdEvaluationPlantType)record);
			}
			else if(record instanceof ChdEvaluationRule) { //新增评价项目,复制到下级目录中的对应位置
				copyRule((ChdEvaluationRule)record);
			}
			else if(record instanceof ChdEvaluationDetail) { //新增评价细则,复制到下级目录中的对应位置
				copyDetail((ChdEvaluationDetail)record);
			}
		}
		finally {
			threadLocal.remove();
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof ChdEvaluationDirectory) {
			ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)record;
			if(evaluationDirectory.getDataWorkflowId()!=null && evaluationDirectory.getDataWorkflowId().isEmpty()) {
				evaluationDirectory.setDataWorkflowId(null);
			}
			if(evaluationDirectory.getSelfEvalWorkflowId()!=null && evaluationDirectory.getSelfEvalWorkflowId().isEmpty()) {
				evaluationDirectory.setSelfEvalWorkflowId(null);
			}
			if(evaluationDirectory.getSelfEvalEnd()!=null && evaluationDirectory.getSelfEvalEnd().isEmpty()) {
				evaluationDirectory.setSelfEvalEnd(null);
			}
			if(evaluationDirectory.getSelfEvalUrgency()!=null && evaluationDirectory.getSelfEvalUrgency().isEmpty()) {
				evaluationDirectory.setSelfEvalUrgency(null);
			}
		}
		record = super.update(record);
		if(record instanceof ChdEvaluationPrerequisites) { //必备条件
			copyPrerequisites((ChdEvaluationPrerequisites)record);
		}
		else if(record instanceof ChdEvaluationIndicator) { //主要指标
			copyIndicator((ChdEvaluationIndicator)record);
		}
		else if(record instanceof ChdEvaluationPoint) { //考评要点
			copyPoint((ChdEvaluationPoint)record);
		}
		if(!(record instanceof ChdEvaluationDirectory)) {
			return record;
		}
		ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)record;
		if(evaluationDirectory.getSourceDirectoryId()>0) { //如果本身就是继承来的,不处理
			return evaluationDirectory;
		}
		if(record instanceof ChdEvaluationRule) { //更新评价项目,自动更新对应项目的项目名称
			ChdEvaluationRule rule = (ChdEvaluationRule)record;
			updateRelationDirectory(rule.getId(), rule, new String[]{"directoryName", "isIndicator"});
		}
		else if(record instanceof ChdEvaluationDetail) { //更新评价细则,自动更新对应项目的项目名称
			ChdEvaluationDetail detail = (ChdEvaluationDetail)record;
			updateRelationDirectory(detail.getId(), detail, new String[]{"directoryName", "dataCycle", "dataCycleEnd", "dataUrgency", "selfEvalEnd", "selfEvalUrgency"});
		}
		else if(record instanceof ChdEvaluationPlant) {
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(record instanceof ChdEvaluationPrerequisites) { //必备条件
			deleteRelationPrerequisites((ChdEvaluationPrerequisites)record);
		}
		else if(record instanceof ChdEvaluationIndicator) { //主要指标
			deleteRelationIndicator((ChdEvaluationIndicator)record);
		}
		else if(record instanceof ChdEvaluationPoint) { //要点
			deleteRelationPoint((ChdEvaluationPoint)record);
		}
		if(!(record instanceof ChdEvaluationDirectory)) {
			return;
		}
		ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)record;
		if(evaluationDirectory.getSourceDirectoryId()>0) {
			return;
		}
		//删除评价项目、发电企业类型,自动删除对应项目
		if((evaluationDirectory instanceof ChdEvaluationRule) || (evaluationDirectory instanceof ChdEvaluationDetail) || (evaluationDirectory instanceof ChdEvaluationPlantType)) {
			deleteRelationDirectory(evaluationDirectory.getId());
		}
		else if(record instanceof ChdEvaluationPlant) {
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
	}
	
	/**
	 * 复制发电企业类型
	 * @param company
	 * @throws ServiceException
	 */
	private void copyPlantTypes(ChdEvaluationCompany company) throws ServiceException {
		//获取发电企业分类ID列表
		String hql = "from ChdEvaluationPlantType ChdEvaluationPlantType" +
					 " where ChdEvaluationPlantType.parentDirectoryId=" + company.getParentDirectoryId() +
					 " order by ChdEvaluationPlantType.priority DESC, ChdEvaluationPlantType.created, ChdEvaluationPlantType.id";
		List plantTypes = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("indicators,prerequisites,generators", ","));
		threadLocal.set("TO:" + ChdEvaluationCompany.class.getName()); //设置标志
		for(Iterator iterator = plantTypes==null ? null : plantTypes.iterator(); iterator!=null && iterator.hasNext();) {
			ChdEvaluationPlantType plantType = (ChdEvaluationPlantType)iterator.next();
			copyDirectory(plantType.getId(), company.getId(), null, company.getCreator(), company.getCreatorId());
			//复制必备条件、主要指标、机组综合数据表模板
			for(Iterator iteratorPrerequisites = plantType.getPrerequisites()==null ? null : plantType.getPrerequisites().iterator(); iteratorPrerequisites!=null && iteratorPrerequisites.hasNext();) {
				ChdEvaluationPrerequisites prerequisites = (ChdEvaluationPrerequisites)iteratorPrerequisites.next();
				copyPrerequisites(prerequisites); //复制必备条件
			}
			for(Iterator iteratorIndicator = plantType.getIndicators()==null ? null : plantType.getIndicators().iterator(); iteratorIndicator!=null && iteratorIndicator.hasNext();) {
				ChdEvaluationIndicator indicator = (ChdEvaluationIndicator)iteratorIndicator.next();
				copyIndicator(indicator); //复制主要指标
			}
			for(Iterator iteratorGenerator = plantType.getGenerators()==null ? null : plantType.getGenerators().iterator(); iteratorGenerator!=null && iteratorGenerator.hasNext();) {
				ChdEvaluationGenerator generator = (ChdEvaluationGenerator)iteratorGenerator.next();
				copyGenerator(generator); //复制机组数据模板
			}
		}	
	}
	
	/**
	 * 复制发电企业对应的评价项目
	 * @param plant
	 * @throws ServiceException
	 */
	private void copyPlantRules(ChdEvaluationPlant plant) throws ServiceException {
		String hql = "select ChdEvaluationRule.id" +
					 " from ChdEvaluationRule ChdEvaluationRule" +
					 " where ChdEvaluationRule.parentDirectoryId=(" +
					 "  select max(ChdEvaluationPlantType.id)" +
					 "   from ChdEvaluationPlantType ChdEvaluationPlantType" +
					 "   where ChdEvaluationPlantType.parentDirectoryId=" + plant.getParentDirectoryId() +
					 "   and ChdEvaluationPlantType.directoryName='" + JdbcUtils.resetQuot(plant.getType()) + "')" +
					 " order by ChdEvaluationRule.priority DESC, ChdEvaluationRule.created, ChdEvaluationRule.id";
		List ruleIds = getDatabaseService().findRecordsByHql(hql);
		threadLocal.set("TO:" + ChdEvaluationPlant.class.getName()); //设置标志
		for(Iterator iterator = ruleIds==null ? null : ruleIds.iterator(); iterator!=null && iterator.hasNext();) {
			Number ruleId = (Number)iterator.next();
			copyDirectory(ruleId.longValue(), plant.getId(), null, plant.getCreator(), plant.getCreatorId()); //复制目录
		}
	}
	
	/**
	 * 递归函数:拷贝发电企业类型到分公司
	 * @param plantType
	 * @throws ServiceException
	 */
	private void copyPlantType(ChdEvaluationPlantType plantType) throws ServiceException {
		String hql = "select ChdEvaluationCompany.id" +
					 " from ChdEvaluationCompany ChdEvaluationCompany" +
					 " where ChdEvaluationCompany.parentDirectoryId=" + plantType.getParentDirectoryId() +
					 " and ChdEvaluationCompany.id!=0";
		List companyIds = getDatabaseService().findRecordsByHql(hql);
		threadLocal.set("FROM:" + ChdEvaluationPlantType.class.getName()); //设置标志
		for(Iterator iterator = companyIds==null ? null : companyIds.iterator(); iterator!=null && iterator.hasNext();) {
			Number companyId = (Number)iterator.next();
			plantType = (ChdEvaluationPlantType)copyDirectory(plantType.getId(), companyId.longValue(), null, plantType.getCreator(), plantType.getCreatorId());
			copyPlantType(plantType); //递归调用
		}
	}
	
	/**
	 * 递归函数:拷贝评价项目
	 * @param rule
	 * @throws ServiceException
	 */
	private void copyRule(ChdEvaluationRule rule) throws ServiceException {
		String hql = "select ChdEvaluationDirectory.id" +
					 " from ChdEvaluationDirectory ChdEvaluationDirectory" +
					 " where ChdEvaluationDirectory.sourceDirectoryId=" + rule.getParentDirectoryId();
		List parentDirectoryIds = getDatabaseService().findRecordsByHql(hql);
		threadLocal.set("FROM:" + ChdEvaluationRule.class.getName()); //设置标志
		for(Iterator iterator = parentDirectoryIds==null ? null : parentDirectoryIds.iterator(); iterator!=null && iterator.hasNext();) {
			Number parentDirectoryId = (Number)iterator.next();
			ChdEvaluationDirectory newRule = (ChdEvaluationDirectory)copyDirectory(rule.getId(), parentDirectoryId.longValue(), null, rule.getCreator(), rule.getCreatorId());
			if(newRule instanceof ChdEvaluationRule) {
				copyRule((ChdEvaluationRule)newRule); //递归调用
			}
		}
		//如果规则的上级目录是发电企业类型,复制到下级电厂
		hql = "select ChdEvaluationPlantType.parentDirectoryId, ChdEvaluationPlantType.directoryName" +
			  " from ChdEvaluationPlantType ChdEvaluationPlantType" +
			  " where ChdEvaluationPlantType.id=" + rule.getParentDirectoryId();
		Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
		if(values!=null) {
			hql = "select ChdEvaluationPlant.id" +
				  " from ChdEvaluationPlant ChdEvaluationPlant" +
				  " where ChdEvaluationPlant.parentDirectoryId=" + values[0] +
				  " and ChdEvaluationPlant.type='" + JdbcUtils.resetQuot((String)values[1]) + "'";
			List plantIds = getDatabaseService().findRecordsByHql(hql);
			for(Iterator iterator = plantIds==null ? null : plantIds.iterator(); iterator!=null && iterator.hasNext();) {
				Number plantId = (Number)iterator.next();
				copyDirectory(rule.getId(), plantId.longValue(), null, rule.getCreator(), rule.getCreatorId());
			}
		}
	}
	
	/**
	 * 递归函数:拷贝评价细则
	 * @param rule
	 * @throws ServiceException
	 */
	private void copyDetail(ChdEvaluationDetail detail) throws ServiceException {
		//获取源目录为当前目录上级目录的目录
		String hql = "select ChdEvaluationDirectory.id" +
					 " from ChdEvaluationDirectory ChdEvaluationDirectory" +
					 " where ChdEvaluationDirectory.sourceDirectoryId=" + detail.getParentDirectoryId();
		List parentDirectoryIds = getDatabaseService().findRecordsByHql(hql);
		threadLocal.set("FROM:" + ChdEvaluationDetail.class.getName()); //设置标志
		for(Iterator iterator = parentDirectoryIds==null ? null : parentDirectoryIds.iterator(); iterator!=null && iterator.hasNext();) {
			Number parentDirectoryId = (Number)iterator.next();
			ChdEvaluationDirectory newDetail = (ChdEvaluationDirectory)copyDirectory(detail.getId(), parentDirectoryId.longValue(), null, detail.getCreator(), detail.getCreatorId());
			if(newDetail instanceof ChdEvaluationDetail) {
				copyDetail((ChdEvaluationDetail)newDetail); //递归调用
			}
		}
	}
	
	/**
	 * 递归:复制发电企业类型的必备条件
	 * @param ChdEvaluationPlantType
	 */
	private void copyPrerequisites(ChdEvaluationPrerequisites prerequisites) {
		//查找分公司的的发电企业类型配置
		String hql = "from ChdEvaluationPlantType ChdEvaluationPlantType" +
					 " where ChdEvaluationPlantType.sourceDirectoryId=" + prerequisites.getPlantTypeId();
		List plantTypes = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("prerequisites"));
		for(Iterator iterator = plantTypes==null ? null : plantTypes.iterator(); iterator!=null && iterator.hasNext();) {
			ChdEvaluationPlantType plantType = (ChdEvaluationPlantType)iterator.next();
			//获取原来继承的记录
			ChdEvaluationPrerequisites companyPrerequisites = (ChdEvaluationPrerequisites)ListUtils.findObjectByProperty(plantType.getPrerequisites(), "sourceRecordId", new Long(prerequisites.getId()));
			if(companyPrerequisites!=null) { //继承过
				companyPrerequisites.setPrerequisites(prerequisites.getPrerequisites()); //条件
				companyPrerequisites.setRemark(prerequisites.getRemark()); //备注
				getDatabaseService().updateRecord(companyPrerequisites);
			}
			else { //没有继承过
				companyPrerequisites = new ChdEvaluationPrerequisites();
				companyPrerequisites.setId(UUIDLongGenerator.generateId()); //ID
				companyPrerequisites.setPlantTypeId(plantType.getId()); //发电企业类型ID
				companyPrerequisites.setPrerequisites(prerequisites.getPrerequisites()); //条件
				companyPrerequisites.setPriority(prerequisites.getPriority()); //优先级
				companyPrerequisites.setCreated(prerequisites.getCreated()); //创建时间
				companyPrerequisites.setSourceRecordId(prerequisites.getId()); //源记录ID
				companyPrerequisites.setRemark(prerequisites.getRemark()); //备注
				getDatabaseService().saveRecord(companyPrerequisites);
			}
			//递归更新下一级
			copyPrerequisites(companyPrerequisites);
		}
	}
	
	/**
	 * 递归:删除关联的必备条件
	 * @param ChdEvaluationPlantType
	 */
	private void deleteRelationPrerequisites(ChdEvaluationPrerequisites prerequisites) {
		String hql = "from ChdEvaluationPrerequisites ChdEvaluationPrerequisites" +
					 " where ChdEvaluationPrerequisites.sourceRecordId=" + prerequisites.getId();
		List relationPrerequisites = getDatabaseService().findRecordsByHql(hql);
		for(Iterator iterator = relationPrerequisites==null ? null : relationPrerequisites.iterator(); iterator!=null && iterator.hasNext();) {
			prerequisites = (ChdEvaluationPrerequisites)iterator.next();
			getDatabaseService().deleteRecord(prerequisites);
			deleteRelationPrerequisites(prerequisites); //递归删除
		}
	}
	
	/**
	 * 递归:复制考评要点
	 * @param indicator
	 */
	private void copyPoint(ChdEvaluationPoint point) {
		String hql = "from ChdEvaluationDetail ChdEvaluationDetail" +
					 " where ChdEvaluationDetail.sourceDirectoryId=" + point.getDetailId();
		List details = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("points"));
		for(Iterator iterator = details==null ? null : details.iterator(); iterator!=null && iterator.hasNext();) {
			ChdEvaluationDetail detail = (ChdEvaluationDetail)iterator.next();
			//获取原来继承的记录
			ChdEvaluationPoint detailPoint = (ChdEvaluationPoint)ListUtils.findObjectByProperty(detail.getPoints(), "sourceRecordId", new Long(point.getId()));
			if(detailPoint!=null) { //继承过
				detailPoint.setPoint(point.getPoint());
				detailPoint.setRemark(point.getRemark()); //备注
				getDatabaseService().updateRecord(detailPoint);
			}
			else { //没有继承过
				detailPoint = new ChdEvaluationPoint();
				detailPoint.setId(UUIDLongGenerator.generateId()); //ID
				detailPoint.setDetailId(detail.getId()); //发电企业类型ID
				detailPoint.setPoint(point.getPoint());
				detailPoint.setPriority(point.getPriority()); //优先级
				detailPoint.setCreated(point.getCreated()); //创建时间
				detailPoint.setSourceRecordId(point.getId()); //源记录ID
				detailPoint.setRemark(point.getRemark()); //备注
				getDatabaseService().saveRecord(detailPoint);
			}
			//递归更新下一级
			copyPoint(detailPoint);
		}
	}
	
	/**
	 * 递归:删除关联的要点
	 * @param ChdEvaluationPlantType
	 */
	private void deleteRelationPoint(ChdEvaluationPoint point) {
		String hql = "from ChdEvaluationPoint ChdEvaluationPoint" +
					 " where ChdEvaluationPoint.sourceRecordId=" + point.getId();
		List relationPoints = getDatabaseService().findRecordsByHql(hql);
		for(Iterator iterator = relationPoints==null ? null : relationPoints.iterator(); iterator!=null && iterator.hasNext();) {
			point = (ChdEvaluationPoint)iterator.next();
			getDatabaseService().deleteRecord(point);
			deleteRelationPoint(point); //递归删除
		}
	}
	
	/**
	 * 递归:复制发电企业类型的主要指标
	 * @param indicator
	 */
	private void copyIndicator(ChdEvaluationIndicator indicator) {
		//查找分公司的的发电企业类型配置
		String hql = "from ChdEvaluationPlantType ChdEvaluationPlantType" +
					 " where ChdEvaluationPlantType.sourceDirectoryId=" + indicator.getPlantTypeId();
		List plantTypes = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("indicators"));
		for(Iterator iterator = plantTypes==null ? null : plantTypes.iterator(); iterator!=null && iterator.hasNext();) {
			ChdEvaluationPlantType plantType = (ChdEvaluationPlantType)iterator.next();
			//获取原来继承的记录
			ChdEvaluationIndicator companyIndicator = (ChdEvaluationIndicator)ListUtils.findObjectByProperty(plantType.getIndicators(), "sourceRecordId", new Long(indicator.getId()));
			if(companyIndicator!=null) { //继承过
				companyIndicator.setName(indicator.getName()); //指标名称
				companyIndicator.setUnit(indicator.getUnit()); //单位
				companyIndicator.setRemark(indicator.getRemark()); //备注
				getDatabaseService().updateRecord(companyIndicator);
			}
			else { //没有继承过
				companyIndicator = new ChdEvaluationIndicator();
				companyIndicator.setId(UUIDLongGenerator.generateId()); //ID
				companyIndicator.setPlantTypeId(plantType.getId()); //发电企业类型ID
				companyIndicator.setName(indicator.getName()); //指标名称
				companyIndicator.setUnit(indicator.getUnit()); //单位
				companyIndicator.setPriority(indicator.getPriority()); //优先级
				companyIndicator.setCreated(indicator.getCreated()); //创建时间
				companyIndicator.setSourceRecordId(indicator.getId()); //源记录ID
				companyIndicator.setRemark(indicator.getRemark()); //备注
				getDatabaseService().saveRecord(companyIndicator);
			}
			//递归更新下一级
			copyIndicator(companyIndicator);
		}
	}
	
	/**
	 * 递归:删除关联的主要指标
	 * @param ChdEvaluationPlantType
	 */
	private void deleteRelationIndicator(ChdEvaluationIndicator indicator) {
		String hql = "from ChdEvaluationIndicator ChdEvaluationIndicator" +
					 " where ChdEvaluationIndicator.sourceRecordId=" + indicator.getId();
		List relationIndicators = getDatabaseService().findRecordsByHql(hql);
		for(Iterator iterator = relationIndicators==null ? null : relationIndicators.iterator(); iterator!=null && iterator.hasNext();) {
			indicator = (ChdEvaluationIndicator)iterator.next();
			getDatabaseService().deleteRecord(indicator);
			deleteRelationIndicator(indicator); //递归删除
		}
	}
	
	/**
	 * 递归:复制发电企业类型的机组数据模板
	 * @param generator
	 */
	private void copyGenerator(ChdEvaluationGenerator generator) {
		//查找分公司的的发电企业类型配置
		String hql = "from ChdEvaluationPlantType ChdEvaluationPlantType" +
					 " where ChdEvaluationPlantType.sourceDirectoryId=" + generator.getPlantTypeId();
		List plantTypes = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("generators"));
		for(Iterator iterator = plantTypes==null ? null : plantTypes.iterator(); iterator!=null && iterator.hasNext();) {
			ChdEvaluationPlantType plantType = (ChdEvaluationPlantType)iterator.next();
			ChdEvaluationGenerator companyGenerator = (ChdEvaluationGenerator)(plantType.getGenerators()==null || plantType.getGenerators().isEmpty() ? null : plantType.getGenerators().iterator().next());
			if(companyGenerator!=null && companyGenerator.getSourceRecordId()==0) { //分公司已经修改过模板
				return; //不复制
			}
			//获取原来继承的记录
			if(companyGenerator!=null) { //继承过
				companyGenerator.setBody(generator.getBody()); //机组综合数据
				getDatabaseService().updateRecord(companyGenerator);
			}
			else { //没有继承过
				companyGenerator = new ChdEvaluationGenerator();
				companyGenerator.setId(UUIDLongGenerator.generateId()); //ID
				companyGenerator.setPlantTypeId(plantType.getId()); //发电企业类型ID
				companyGenerator.setBody(generator.getBody()); //机组综合数据
				companyGenerator.setSourceRecordId(generator.getId()); //源记录ID
				getDatabaseService().saveRecord(companyGenerator);
			}
			//递归更新下一级
			copyGenerator(companyGenerator);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#resetCopiedDirectory(com.yuanluesoft.jeaf.directorymanage.pojo.Directory)
	 */
	public Directory resetCopiedDirectory(Directory sourceDirectory, Directory newDirectory) throws ServiceException {
		Object flag = threadLocal.get();
		if(flag==null) {
			return newDirectory;
		}
		ChdEvaluationDirectory newEvaluationDirectory = (ChdEvaluationDirectory)newDirectory;
		newEvaluationDirectory.setSourceDirectoryId(sourceDirectory.getId()); //设置源目录ID
		boolean convert = false; //是否需要转换为发电企业的评价项目或细则
		if(flag.equals("TO:" + ChdEvaluationPlant.class.getName())) { //新建发电企业,根据企业类型,复制对应的评价项目
			convert = true;
		}
		else if(flag.equals("FROM:" + ChdEvaluationRule.class.getName()) || flag.equals("FROM:" + ChdEvaluationDetail.class.getName())) { //新建评价项目或细则
			//获取父目录类型
			String hql = "select ChdEvaluationDirectory.directoryType" +
						 " from ChdEvaluationDirectory ChdEvaluationDirectory" +
						 " where ChdEvaluationDirectory.id=" + newDirectory.getParentDirectoryId();
			String parentDirectoryType = (String)getDatabaseService().findRecordByHql(hql);
			convert = ("plant".equals(parentDirectoryType) || "plantRule".equals(parentDirectoryType)); //父目录是发电企业、或者是发电企业评价项目
		}
		if(convert) {
			if(sourceDirectory instanceof ChdEvaluationRule) {
				//复制为发电企业评价项目
				ChdEvaluationPlantRule plantRule = new ChdEvaluationPlantRule();
				try {
					PropertyUtils.copyProperties(plantRule, newDirectory);
				}
				catch(Exception e) {
					
				}
				plantRule.setDirectoryType("plantRule");
				return plantRule;
			}
			else if(sourceDirectory instanceof ChdEvaluationDetail) {
				//复制为发电企业评价细则
				ChdEvaluationPlantDetail plantDetail = new ChdEvaluationPlantDetail();
				try {
					PropertyUtils.copyProperties(plantDetail, newDirectory);
				}
				catch(Exception e) {
					
				}
				plantDetail.setDirectoryType("plantDetail");
				return plantDetail;
			}
		}
		return newDirectory;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.service.spring.DirectoryServiceImpl#saveDirectoryPopedomConfigs(com.yuanluesoft.jeaf.directorymanage.pojo.Directory, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean saveDirectoryPopedomConfigs(Directory directory, List directoryPopedomConfigs, boolean isNewDirectory, SessionInfo sessionInfo) throws ServiceException {
		boolean changed = super.saveDirectoryPopedomConfigs(directory, directoryPopedomConfigs, isNewDirectory, sessionInfo);
		if(!changed) { //权限没有调整
			return changed;
		}
		ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)directory;
		inheritSourceDirectoryPopedom(evaluationDirectory, true); //重新继承源目录的权限
		return changed;
	}

	/**
	 * 递归:继承源目录的权限
	 * @param directory
	 * @param updateInheritors 是否更新继承者的权限
	 */
	private void inheritSourceDirectoryPopedom(ChdEvaluationDirectory directory, boolean updateInheritors) {
		if(directory.getSourceDirectoryId()>0) { //源目录ID>0
			//删除isInherit='2'的权限记录
			for(Iterator iteratorPopedom = directory.getDirectoryPopedoms().iterator(); iteratorPopedom.hasNext();) {
				DirectoryPopedom directoryPopedom = (DirectoryPopedom)iteratorPopedom.next();
				if(directoryPopedom.getIsInherit()=='2') {
					//删除继承的权限
					getDatabaseService().deleteRecord(directoryPopedom);
					iteratorPopedom.remove();
				}
			}
			//获取源目录的权限记录
			String hql = "from ChdEvaluationDirectoryPopedom ChdEvaluationDirectoryPopedom" +
						 " where ChdEvaluationDirectoryPopedom.directoryId=" + directory.getSourceDirectoryId();
			List sourceDirectoryPopedoms = getDatabaseService().findRecordsByHql(hql);
			for(Iterator iteratorPopedom = sourceDirectoryPopedoms==null ? null : sourceDirectoryPopedoms.iterator(); iteratorPopedom!=null && iteratorPopedom.hasNext();) {
				DirectoryPopedom sourceDirectoryPopedom = (DirectoryPopedom)iteratorPopedom.next();
				//检查是否已经配置过
				if(ListUtils.findObjectByProperty(directory.getDirectoryPopedoms(), "userId", new Long(sourceDirectoryPopedom.getUserId()))!=null) {
					continue;
				}
				try {
					ChdEvaluationDirectoryPopedom directoryPopedom = new ChdEvaluationDirectoryPopedom();
					PropertyUtils.copyProperties(directoryPopedom, sourceDirectoryPopedom);
					directoryPopedom.setId(UUIDLongGenerator.generateId()); //ID
					directoryPopedom.setIsInherit('2');
					directoryPopedom.setDirectoryId(directory.getId()); //目录ID
					getDatabaseService().saveRecord(directoryPopedom);
					directory.getDirectoryPopedoms().add(directoryPopedom);
				}
				catch (Exception e) {
					
				}
			}
		}
		//处理子目录
		String hql = "from ChdEvaluationDirectory ChdEvaluationDirectory" +
				 	 " where ChdEvaluationDirectory.parentDirectoryId=" + directory.getId() +
				 	 " and ChdEvaluationDirectory.id!=0";
		List childDirectories = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("directoryPopedoms"));
		for(Iterator iterator = childDirectories==null ? null : childDirectories.iterator(); iterator!=null && iterator.hasNext();) {
			directory = (ChdEvaluationDirectory)iterator.next();
			inheritSourceDirectoryPopedom(directory, false); //递归调用
		}
		//处理继承者
		if(updateInheritors) {
			hql = "from ChdEvaluationDirectory ChdEvaluationDirectory" +
				  " where ChdEvaluationDirectory.sourceDirectoryId=" + directory.getId();
			List inheritors = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("directoryPopedoms"));
			for(Iterator iterator = inheritors==null ? null : inheritors.iterator(); iterator!=null && iterator.hasNext();) {
				directory = (ChdEvaluationDirectory)iterator.next();
				inheritSourceDirectoryPopedom(directory, true); //递归调用
			}
		}
	}
	
	/**
	 * 递归:更新关联目录的名称
	 * @param sourceDirectoryId
	 * @param updatedDirectory
	 * @param propertyNames
	 * @throws ServiceException
	 */
	private void updateRelationDirectory(long sourceDirectoryId, ChdEvaluationDirectory updatedDirectory, String[] propertyNames) throws ServiceException {
		String hql = "from ChdEvaluationDirectory ChdEvaluationDirectory" +
					 " where ChdEvaluationDirectory.sourceDirectoryId=" + sourceDirectoryId;
		List directories = getDatabaseService().findRecordsByHql(hql);
		for(Iterator iterator = directories==null ? null : directories.iterator(); iterator!=null && iterator.hasNext();) {
			ChdEvaluationDirectory directory = (ChdEvaluationDirectory)iterator.next();
			for(int i=0; i<propertyNames.length; i++) {
				try {
					PropertyUtils.setProperty(directory, propertyNames[i], PropertyUtils.getProperty(updatedDirectory, propertyNames[i]));
				}
				catch (Exception e) {
					
				}
			}
			getDatabaseService().updateRecord(directory);
			updateRelationDirectory(directory.getId(), updatedDirectory, propertyNames); //递归更新
		}
	}
	
	/**
	 * 递归:删除关联目录
	 * @param sourceRuleId
	 * @param ruleName
	 * @throws ServiceException
	 */
	private void deleteRelationDirectory(long sourceDirectoryId) throws ServiceException {
		String hql = "from ChdEvaluationDirectory ChdEvaluationDirectory" +
					 " where sourceDirectoryId=" + sourceDirectoryId;
		List directories = getDatabaseService().findRecordsByHql(hql);
		for(Iterator iterator = directories==null ? null : directories.iterator(); iterator!=null && iterator.hasNext();) {
			ChdEvaluationDirectory directory = (ChdEvaluationDirectory)iterator.next();
			delete(directory);
			deleteRelationDirectory(directory.getId()); //递归删除
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService#listRuleScores(long, com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationRule)
	 */
	public Set listRuleScores(long directoryId, ChdEvaluationRule rule) throws ServiceException {
		Set evaluationLevels = listEvaluationLevels(directoryId);
		if(evaluationLevels==null || evaluationLevels.isEmpty()) {
			return null;
		}
		Set ruleScores = new LinkedHashSet();
		for(Iterator iterator = evaluationLevels.iterator(); iterator.hasNext();) {
			ChdEvaluationLevel level = (ChdEvaluationLevel)iterator.next();
			ChdEvaluationRuleScore ruleScore = (rule==null ? null : (ChdEvaluationRuleScore)ListUtils.findObjectByProperty(rule.getScores(), "levelId", new Long(level.getId())));
			if(ruleScore==null) {
				ruleScore = new ChdEvaluationRuleScore();
			}
			ruleScore.setLevel(level.getLevel()); //评价等级,一星级、二星级、三星级、四星级、五星级、省一星级...
			ruleScore.setLevelId(level.getId()); //评价等级ID 
			ruleScores.add(ruleScore);
		}
		return ruleScores.isEmpty() ? null : ruleScores;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService#saveRuleScores(long, java.lang.String[], java.lang.String[], java.lang.String[])
	 */
	public void saveRuleScores(long ruleId, String[] levelIds, String[] minScores, String[] maxScrores) throws ServiceException {
		if(levelIds==null || levelIds.length==0) {
			return;
		}
		Set evaluationLevels = listEvaluationLevels(ruleId);
		if(evaluationLevels==null || evaluationLevels.isEmpty()) {
			return;
		}
		//删除原来的配置
		getDatabaseService().deleteRecordsByHql("from ChdEvaluationRuleScore ChdEvaluationRuleScore where ChdEvaluationRuleScore.ruleId=" + ruleId);
		//保存新的配置
		for(int i=0; i<levelIds.length; i++) {
			if(minScores[i].isEmpty() && maxScrores[i].isEmpty()) { //没有配置
				continue;
			}
			try {
				ChdEvaluationLevel level = (ChdEvaluationLevel)ListUtils.findObjectByProperty(evaluationLevels, "id", new Long(levelIds[i]));
				ChdEvaluationRuleScore ruleScore = new ChdEvaluationRuleScore();
				ruleScore.setId(UUIDLongGenerator.generateId()); //ID
				ruleScore.setRuleId(ruleId); //评价细则ID
				ruleScore.setLevelId(level.getId()); //评价等级ID
				ruleScore.setLevel(level.getLevel()); //评价等级,一星级、二星级、三星级、四星级、五星级、省一星级...
				ruleScore.setMinScore(minScores[i].isEmpty() ? 0 : Integer.parseInt(minScores[i])); //最低分数
				ruleScore.setMaxScore(maxScrores[i].isEmpty() ? 0 : Integer.parseInt(maxScrores[i])); //最高分数
				getDatabaseService().saveRecord(ruleScore);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService#listPrerequisitesScores(long, com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPrerequisites)
	 */
	public Set listPrerequisitesScores(long plantTypeId, ChdEvaluationPrerequisites prerequisites) throws ServiceException {
		Set evaluationLevels = listEvaluationLevels(plantTypeId);
		if(evaluationLevels==null || evaluationLevels.isEmpty()) {
			return null;
		}
		Set prerequisitesScores = new LinkedHashSet();
		for(Iterator iterator = evaluationLevels.iterator(); iterator.hasNext();) {
			ChdEvaluationLevel level = (ChdEvaluationLevel)iterator.next();
			ChdEvaluationPrerequisitesScore prerequisitesScore = (prerequisites==null ? null : (ChdEvaluationPrerequisitesScore)ListUtils.findObjectByProperty(prerequisites.getScores(), "levelId", new Long(level.getId())));
			if(prerequisitesScore==null) {
				prerequisitesScore = new ChdEvaluationPrerequisitesScore();
			}
			prerequisitesScore.setLevel(level.getLevel()); //评价等级,一星级、二星级、三星级、四星级、五星级、省一星级...
			prerequisitesScore.setLevelId(level.getId()); //评价等级ID 
			prerequisitesScores.add(prerequisitesScore);
		}
		return prerequisitesScores.isEmpty() ? null : prerequisitesScores;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService#savePrerequisitesScores(long, long, java.lang.String[], java.lang.String[])
	 */
	public void savePrerequisitesScores(long plantTypeId, long prerequisitesId, String[] levelIds, String[] scores) throws ServiceException {
		if(levelIds==null || levelIds.length==0) {
			return;
		}
		Set evaluationLevels = listEvaluationLevels(plantTypeId);
		if(evaluationLevels==null || evaluationLevels.isEmpty()) {
			return;
		}
		//删除原来的配置
		getDatabaseService().deleteRecordsByHql("from ChdEvaluationPrerequisitesScore ChdEvaluationPrerequisitesScore where ChdEvaluationPrerequisitesScore.prerequisitesId=" + prerequisitesId);
		//保存新的配置
		for(int i=0; i<levelIds.length; i++) {
			if(scores[i].isEmpty()) { //没有配置
				continue;
			}
			try {
				ChdEvaluationLevel level = (ChdEvaluationLevel)ListUtils.findObjectByProperty(evaluationLevels, "id", new Long(levelIds[i]));
				ChdEvaluationPrerequisitesScore prerequisitesScore = new ChdEvaluationPrerequisitesScore();
				prerequisitesScore.setId(UUIDLongGenerator.generateId()); //ID
				prerequisitesScore.setPrerequisitesId(prerequisitesId); //必备条件ID
				prerequisitesScore.setLevelId(level.getId()); //评价等级ID
				prerequisitesScore.setLevel(level.getLevel()); //评价等级,一星级、二星级、三星级、四星级、五星级、省一星级...
				prerequisitesScore.setScore(scores[i]);
				getDatabaseService().saveRecord(prerequisitesScore);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService#saveGeneratorTemplate(java.lang.String, com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantType)
	 */
	public void saveGeneratorTemplate(String generatorTemplate, ChdEvaluationPlantType plantType) throws ServiceException {
		if(generatorTemplate==null) {
			return;
		}
		ChdEvaluationGenerator generator;
		if(plantType.getGenerators()!=null && !plantType.getGenerators().isEmpty()) {
			generator = (ChdEvaluationGenerator)plantType.getGenerators().iterator().next();
			if(generatorTemplate.equals(generator.getBody())) { //没有变化
				return;
			}
			generator.setSourceRecordId(0); //发生变化后,设置源记录ID为0,不再继承上级公司的模板
			generator.setBody(generatorTemplate);
			getDatabaseService().updateRecord(generator);
		}
		else {
			generator = new ChdEvaluationGenerator();
			generator.setId(UUIDLongGenerator.generateId());
			generator.setPlantTypeId(plantType.getId()); //发电企业类型ID
			generator.setBody(generatorTemplate);
			getDatabaseService().saveRecord(generator);
		}
		copyGenerator(generator); //复制到分公司
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService#isPlantTypeOf(java.lang.String)
	 */
	public boolean isPlantTypeUsed(String type) throws ServiceException {
		String hql = "select ChdEvaluationPlant.id" +
					 " from ChdEvaluationPlant ChdEvaluationPlant" +
					 " where ChdEvaluationPlant.type='" + JdbcUtils.resetQuot(type) + "'";
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/**
	 * 获取评价等级列表
	 * @param directoryId
	 * @return
	 * @throws ServiceException
	 */
	private Set listEvaluationLevels(long directoryId) throws ServiceException {
		ChdEvaluationCompany company = (ChdEvaluationCompany)getParentDirectory(directoryId, "company");
		company = (ChdEvaluationCompany)load(company.getClass(), company.getId()); //获取完整的公司信息
		return company.getLevels();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long directoryId = RequestUtils.getParameterLongValue(notifyRequest, "directoryId");
		String workflowType = RequestUtils.getParameterStringValue(notifyRequest, "workflowType");
		ChdEvaluationDirectory directory = (ChdEvaluationDirectory)getDirectory(directoryId);
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) { //流程删除操作
			if("dataWorkflow".equals(workflowType)) {
				directory.setDataWorkflowId(null); //流程ID 
				directory.setDataWorkflowName(null); //流程名称
			}
			else {
				directory.setSelfEvalWorkflowId(null); //流程ID 
				directory.setSelfEvalWorkflowName(null); //流程名称
			}
		}
		else { //新建或更新流程
			if("dataWorkflow".equals(workflowType)) {
				directory.setDataWorkflowId(workflowId); //流程ID 
				directory.setDataWorkflowName(workflowPackage.getName()); //流程名称
			}
			else {
				directory.setSelfEvalWorkflowId(workflowId); //流程ID 
				directory.setSelfEvalWorkflowName(workflowPackage.getName()); //流程名称
			}
		}
		update(directory);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService#getApprovalWorkflowId(long, java.lang.String)
	 */
	public String getApprovalWorkflowId(long directoryId, String workflowType) throws ServiceException {
		String hql = "select ChdEvaluationDirectory." + workflowType + "Id" +
			  		 " from ChdEvaluationDirectory ChdEvaluationDirectory left join ChdEvaluationDirectory.childSubjections ChdEvaluationDirectorySubjection" +
			  		 " where ChdEvaluationDirectorySubjection.directoryId=" + directoryId +
			  		 " and not ChdEvaluationDirectory." + workflowType + "Id is null" +
			  		 " order by ChdEvaluationDirectorySubjection.id";
		return (String)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService#listEvaluationTransactor(long, int)
	 */
	public List listEvaluationTransactor(long directoryId, int max) throws ServiceException {
		return listDirectoryVisitors(directoryId, "transactor", true, false, max);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService#listEvaluationLeader(long, boolean, int)
	 */
	public List listEvaluationLeader(long directoryId, boolean plantLeader, int max) throws ServiceException {
		if(plantLeader) {
			directoryId = getParentDirectory(directoryId, "plant").getId();
		}
		return listDirectoryVisitors(directoryId, "leader", true, false, max);
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
}