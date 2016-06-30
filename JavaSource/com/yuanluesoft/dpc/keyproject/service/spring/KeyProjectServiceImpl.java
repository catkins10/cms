package com.yuanluesoft.dpc.keyproject.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProject;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectAccountableInvest;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectAnnualObjective;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectArea;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectDeclare;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvest;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvestComplete;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvestPaid;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectParameter;
import com.yuanluesoft.dpc.keyproject.service.KeyProjectService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class KeyProjectServiceImpl extends BusinessServiceImpl implements KeyProjectService {
	private boolean isApprovalDebrief = true; //汇报是否需要审核,默认为需要
	private String componentNames; //用户需要的组成部分列表

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof KeyProject) { //项目
			KeyProject project = (KeyProject)record;
			if(project.getParentName()==null) {
				project.setParentName("　");
			}
		}
		else if(record instanceof KeyProjectComponent) {
			KeyProjectComponent component = (KeyProjectComponent)record;
			component.setNeedApproval(isApprovalDebrief ? '1' : '0'); //设置为需要审批
		}
		record = super.save(record);
		if(record instanceof KeyProject) { //项目
			long projectId = ((KeyProject)record).getId();
			sumInvest(projectId); //第一次保存,更新总投资
			updateCompletedInvest(projectId);
			updatePaid(projectId);
		}
		else if(record instanceof KeyProjectInvest) { //总投资
			long projectId = ((KeyProjectInvest)record).getProjectId();
			sumInvest(projectId); //更新总投资
			updateCompletedInvest(projectId);
			updatePaid(projectId);
		}
		else if(record instanceof KeyProjectAccountableInvest) { //总投资(责任制)
			sumInvest(((KeyProjectAccountableInvest)record).getProjectId()); //更新总投资
		}
		else if(record instanceof KeyProjectAnnualObjective) { //年度目标
			updateCompletedInvest(((KeyProjectAnnualObjective)record).getProjectId());
			updatePaid(((KeyProjectAnnualObjective)record).getProjectId());
		}
		else if(record instanceof KeyProjectInvestComplete) { //完成投资
			updateCompletedInvest(((KeyProjectInvestComplete)record).getProjectId());
		}
		else if(record instanceof KeyProjectInvestPaid) { //资金到位
			updatePaid(((KeyProjectInvestPaid)record).getProjectId());
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		if(record instanceof KeyProjectInvest) { //总投资
			long projectId = ((KeyProjectInvest)record).getProjectId();
			sumInvest(projectId);
			updateCompletedInvest(projectId);
			updatePaid(projectId);
		}
		else if(record instanceof KeyProjectAccountableInvest) { //总投资(责任制)
			sumInvest(((KeyProjectAccountableInvest)record).getProjectId());
		}
		else if(record instanceof KeyProjectAnnualObjective) { //年度目标
			updateCompletedInvest(((KeyProjectAnnualObjective)record).getProjectId());
			updatePaid(((KeyProjectAnnualObjective)record).getProjectId());
		}
		else if(record instanceof KeyProjectInvestComplete) { //完成投资
			updateCompletedInvest(((KeyProjectInvestComplete)record).getProjectId());
		}
		else if(record instanceof KeyProjectInvestPaid) { //资金到位
			updatePaid(((KeyProjectInvestPaid)record).getProjectId());
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(record instanceof KeyProjectInvest) { //总投资
			long projectId = ((KeyProjectInvest)record).getProjectId();
			sumInvest(projectId);
			updateCompletedInvest(projectId);
			updatePaid(projectId);
		}
		else if(record instanceof KeyProjectAccountableInvest) { //总投资(责任制)
			sumInvest(((KeyProjectAccountableInvest)record).getProjectId());
		}
		else if(record instanceof KeyProjectAnnualObjective) { //年度目标
			updateCompletedInvest(((KeyProjectAnnualObjective)record).getProjectId());
			updatePaid(((KeyProjectAnnualObjective)record).getProjectId());
		}
		else if(record instanceof KeyProjectInvestComplete) { //完成投资
			updateCompletedInvest(((KeyProjectInvestComplete)record).getProjectId());
		}
		else if(record instanceof KeyProjectInvestPaid) { //资金到位
			updatePaid(((KeyProjectInvestPaid)record).getProjectId());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(java.lang.Object)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		if(record instanceof KeyProject) {
			//检查项目名称是否重复
			KeyProject project = (KeyProject)record;
			String hql = "select KeyProject.id" +
						 " from KeyProject KeyProject" +
						 " where KeyProject.id!=" + project.getId() +
						 " and KeyProject.name='" + project.getName() + "'" +
						 " and " + (project.getParentName()==null || project.getParentName().equals("") ? "KeyProject.parentName is null" : "KeyProject.parentName='" + project.getParentName() + "'");
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				List errors = new ArrayList();
				errors.add("项目“" + project.getName() + "”已经存在");
				return errors;
			}
		}
		return null;
	}

	/**
	 * 计算总投资
	 * @param project
	 * @throws ServiceException
	 */
	private void sumInvest(long projectId) throws ServiceException {
		KeyProject project = (KeyProject)load(KeyProject.class, projectId);
		if(project==null) {
			return;
		}
		//项目总投资资金（责任制）
		String hql = "select sum(KeyProjectAccountableInvest.amount)" +
					 " from KeyProjectAccountableInvest KeyProjectAccountableInvest" +
					 " where KeyProjectAccountableInvest.projectId=" + project.getId();
		Number invest = (Number)getDatabaseService().findRecordByHql(hql);
		project.setAccountableInvest(invest==null ? 0 : invest.doubleValue());
		
		//项目总投资资金
		hql = "select sum(KeyProjectInvest.amount)" +
			  " from KeyProjectInvest KeyProjectInvest" +
			  " where KeyProjectInvest.projectId=" + projectId;
		invest = (Number)getDatabaseService().findRecordByHql(hql);
		project.setInvest(invest==null ? 0 : invest.doubleValue());
		getDatabaseService().updateRecord(project);
	}
	
	/**
	 * 按投资计划来查找完成投资记录,更新已完成的投资
	 * @param investPlan
	 * @throws ServiceException
	 */
	private void updateCompletedInvest(long projectId) throws ServiceException {
		KeyProject project = (KeyProject)load(KeyProject.class, projectId);
		if(project==null) {
			return;
		}
		if(project.getAnnualObjectives()!=null && !project.getAnnualObjectives().isEmpty()) {
			//更新已完成投资
			for(Iterator iterator = project.getAnnualObjectives().iterator(); iterator.hasNext();) {
				KeyProjectAnnualObjective annualObjective = (KeyProjectAnnualObjective)iterator.next();
				annualObjective.setInvestCompleted(0);
				if(project.getInvestCompletes()!=null) {
					for(Iterator iteratorComplete = project.getInvestCompletes().iterator(); iteratorComplete.hasNext();) {
						KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iteratorComplete.next();
						if(investComplete.getCompleteYear()==annualObjective.getObjectiveYear()) { //年度比较
							annualObjective.setInvestCompleted(annualObjective.getInvestCompleted()+investComplete.getCompleteInvest());
						}
					}
				}
				getDatabaseService().updateRecord(annualObjective);
			}
		}
		if(project.getInvestCompletes()==null) {
			return;
		}
		int currentYear = -1;
		double yearInvestPlan = 0; //年计划完成投资
		double yearTotal = 0; //年初至报告期累计完成投资（万元）
		double total = 0; //开工至报告期累计完成投资（万元）
		for(Iterator iterator = project.getInvestCompletes().iterator(); iterator.hasNext();) {
			KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
			if(currentYear!=investComplete.getCompleteYear()) {
				yearTotal = 0;
				currentYear = investComplete.getCompleteYear();
				KeyProjectAnnualObjective annualObjective = (KeyProjectAnnualObjective)ListUtils.findObjectByProperty(project.getAnnualObjectives(), "objectiveYear", new Integer(currentYear));
				yearInvestPlan = (annualObjective==null ? 0 : annualObjective.getInvestPlan());
			}
			yearTotal += investComplete.getCompleteInvest();
			total += investComplete.getCompleteInvest();
		
			//年初至报告期累计完成投资（万元）
			investComplete.setYearInvest(yearTotal);
			
			//占年计划（%）
			investComplete.setPercentage(yearInvestPlan==0 ? 0 : Math.round(yearTotal / yearInvestPlan * 10000) / 100.0f);
			
			//开工至报告期累计完成投资（万元）
			investComplete.setTotalComplete(total);
			
			//占总投资（%）
			investComplete.setCompletePercentage(project.getInvest()==0 ? 0 : Math.round(total / project.getInvest() * 10000)/100.0f);
			
			//更新记录
			getDatabaseService().updateRecord(investComplete);
		}
	}
	
	/**
	 * 更新资金到位情况
	 * @param projectId
	 * @throws ServiceException
	 */
	private void updatePaid(long projectId) throws ServiceException {
		KeyProject project = (KeyProject)load(KeyProject.class, projectId);
		if(project==null) {
			return;
		}
		if(project.getInvestPaids()==null || project.getInvestPaids().isEmpty()) {
			return;
		}
		int currentYear = -1;
		double yearInvestPlan = 0; //yearInvestPlan
		double yearTotal = 0; //年初至报告期累计到位投资（万元）
		for(Iterator iterator = project.getInvestPaids().iterator(); iterator.hasNext();) {
			KeyProjectInvestPaid investPaid = (KeyProjectInvestPaid)iterator.next();
			if(currentYear!=investPaid.getPaidYear()) {
				yearTotal = 0;
				currentYear = investPaid.getPaidYear();
				KeyProjectAnnualObjective annualObjective = (KeyProjectAnnualObjective)ListUtils.findObjectByProperty(project.getAnnualObjectives(), "objectiveYear", new Integer(currentYear));
				yearInvestPlan = (annualObjective==null ? 0 : annualObjective.getInvestPlan());
			}
			yearTotal += investPaid.getPaidInvest();
			//total += investPaid.getCompleteInvest();
		
			//年初至报告期累计完成投资（万元）
			investPaid.setYearInvest(yearTotal);
			
			//占年计划（%）
			investPaid.setPercentage(yearInvestPlan==0 ? 0 : Math.round(yearTotal/yearInvestPlan * 10000)/100.0f);
			
			//更新记录
			getDatabaseService().updateRecord(investPaid);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.service.KeyProjectService#updateProjectArea(com.yuanluesoft.dpc.keyproject.pojo.KeyProject, java.lang.String)
	 */
	public void updateProjectArea(KeyProject keyProject, String area) throws ServiceException {
		//添加新的所在地
		if(area==null || area.equals("")) {
			return;
		}
		//删除原来的所在地
		getDatabaseService().deleteRecordsByHql("from KeyProjectArea KeyProjectArea where KeyProjectArea.projectId=" + keyProject.getId());
		//添加新的所在地
		String[] values = area.split(",");
		for(int i=0; i<values.length; i++) {
			KeyProjectArea projectArea = new KeyProjectArea();
			projectArea.setId(UUIDLongGenerator.generateId()); //ID
			projectArea.setProjectId(keyProject.getId()); //项目ID
			projectArea.setArea(values[i]);
			getDatabaseService().saveRecord(projectArea);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.service.KeyProjectService#declareProject(com.yuanluesoft.dpc.keyproject.pojo.KeyProject, int, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void declareProject(KeyProject keyProject, int declareYear, SessionInfo sessionInfo) throws ServiceException {
		KeyProjectDeclare declare = (KeyProjectDeclare)ListUtils.findObjectByProperty(keyProject.getDeclares(), "declareYear", new Integer(declareYear));
		if(declare!=null && declare.getApprovalTime()==null) { //已经创建过申报记录,且没有经过审批
			return;
		}
		//创建新的申报记录
		declare = new KeyProjectDeclare();
		declare.setId(UUIDLongGenerator.generateId()); //ID
		declare.setProjectId(keyProject.getId()); //项目ID
		declare.setDeclareYear(declareYear); //申报年度
		declare.setDeclareTime(DateTimeUtils.now()); //申报时间
		getDatabaseService().saveRecord(declare);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.service.KeyProjectService#setAsKeyProject(com.yuanluesoft.dpc.keyproject.pojo.KeyProject, boolean)
	 */
	public void setAsKeyProject(KeyProject keyProject, boolean isKeyProject) throws ServiceException {
		//获取最近的申报记录,也就是第一条记录
		Iterator iterator = keyProject.getDeclares().iterator();
		KeyProjectDeclare lastDeclare = (KeyProjectDeclare)iterator.next();
		lastDeclare.setIsKeyProject(isKeyProject ? '1' : '0');
		lastDeclare.setApprovalTime(DateTimeUtils.now());
		if(isKeyProject) {
			//复制前一年的项目优先级
			for(; iterator.hasNext();) {
				KeyProjectDeclare declare = (KeyProjectDeclare)iterator.next();
				if(declare.getDeclareYear()==lastDeclare.getDeclareYear()-1) {
					if(declare.getIsKeyProject()=='1') {
						lastDeclare.setPriority(declare.getPriority());
					}
					break;
				}
			}
		}
		getDatabaseService().updateRecord(lastDeclare);
		if(isKeyProject) {
			//把所有项目组成部分设置为不需要审批
			completeApptovalProjectComponents(keyProject, 0);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.service.KeyProjectService#completeApprovalDebrief(com.yuanluesoft.dpc.keyproject.pojo.KeyProject, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void completeApprovalDebrief(KeyProject keyProject, SessionInfo sessionInfo) throws ServiceException {
		//把所有项目组成部分设置为不需要审批
		completeApptovalProjectComponents(keyProject, sessionInfo.getUserId());
	}

	/**
	 * 完成对项目组成部分的审批
	 * @param keyProject
	 * @param approverId
	 * @throws ServiceException
	 */
	private void completeApptovalProjectComponents(KeyProject keyProject, long approverId) throws ServiceException {
		setProjectComponentsApptovaled(keyProject.getOfficialDocuments(), approverId); //审批文件
		setProjectComponentsApptovaled(keyProject.getUnits(), approverId); //参建单位
		setProjectComponentsApptovaled(keyProject.getAnnualObjectives(), approverId); //年度目标
		setProjectComponentsApptovaled(keyProject.getProgresses(), approverId); //形象进度
		setProjectComponentsApptovaled(keyProject.getAccountableInvests(), approverId); //总投资(责任制)
		setProjectComponentsApptovaled(keyProject.getInvests(), approverId); //总投资
		setProjectComponentsApptovaled(keyProject.getInvestPaids(), approverId); //资金到位情况
		setProjectComponentsApptovaled(keyProject.getInvestCompletes(), approverId); //投资完成情况
		setProjectComponentsApptovaled(keyProject.getProblems(), approverId); //问题及措施
		setProjectComponentsApptovaled(keyProject.getPhotos(), approverId); //进展实景
		setProjectComponentsApptovaled(keyProject.getPlans(), approverId); //参建单位安排
		setProjectComponentsApptovaled(keyProject.getStageProgresses(), approverId); //关键节点安排
	}
	
	/**
	 * 更新项目组成部分
	 * @param components
	 * @param needApproval
	 * @param approverId
	 * @throws ServiceException
	 */
	private void setProjectComponentsApptovaled(Set components, long approverId) throws ServiceException {
		if(components==null || components.isEmpty()) {
			return;
		}
		Timestamp now = DateTimeUtils.now();
		for(Iterator iterator = components.iterator(); iterator.hasNext();) {
			Object component = iterator.next();
			if(!(component instanceof KeyProjectComponent)) {
				return;
			}
			KeyProjectComponent projectComponent = (KeyProjectComponent)component;
			if(projectComponent.getNeedApproval()=='0') { //已经设置为不需要审批
				continue;
			}
			try {
				projectComponent.setNeedApproval("1".equals(PropertyUtils.getProperty(component, "completed")+"") ? '0' : '2');
			}
			catch (Exception e) {
				//没有completed属性
				projectComponent.setNeedApproval('0');
			}
			projectComponent.setApproverId(approverId);
			projectComponent.setApprovalTime(now);
			getDatabaseService().updateRecord(projectComponent);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.service.KeyProjectService#getKeyProjectParameter()
	 */
	public KeyProjectParameter getKeyProjectParameter() throws ServiceException {
		KeyProjectParameter parameter = (KeyProjectParameter)getDatabaseService().findRecordByHql("from KeyProjectParameter KeyProjectParameter");
		if(parameter==null) {
			parameter = new KeyProjectParameter();
			parameter.setId(UUIDLongGenerator.generateId());
			parameter.setProjectStatus("前期,新开工,在建,竣工,其他"); //项目状态,前期、在建、竣工、其他
			parameter.setProjectLevel("省级重点,市级重点"); //项目级别,省级重点、市级重点、县级重点
			parameter.setProjectClassify("储备,在建,续建"); //项目类别,储备、在建和续建
			//parameter.setArea(area); //所属区域
			parameter.setProjectCategory("考核类（政府为主投资项目、外资项目）,跟踪服务类”改为“跟踪服务类（除考核类以外项目）"); //项目分类,考核类、跟踪服务类
			parameter.setInvestmentSubject("国有,国有控股与外资合股,国有控股与民营合股,民营,民营控股与国有合资,外资独资,外资控股与国有合资,外资控股与民营合资,其他"); //投资主体,国有、国有控股与外资合股、国有控股与民营合股、民营、民营控股与国有合资、外资独资、外资控股与国有合资、外资控股与民营合资、其他
			//parameter.setManagementUnit(managementUnit); //项目管理部门
			parameter.setConstructionType("在建重点项目,预备重点项目"); //建设性质,新建、扩建
			getDatabaseService().saveRecord(parameter);
		}
		return parameter;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		KeyProjectParameter parameter = (KeyProjectParameter)request.getAttribute("keyProjectParameter");
		if(parameter==null) {
			parameter = getKeyProjectParameter();
			request.setAttribute("keyProjectParameter", parameter);
		}
		if("projectStatus".equals(itemsName)) { //项目状态,前期、在建、竣工、其他
			return ListUtils.generateList(parameter.getProjectStatus(), ",");
		}
		else if("projectLevel".equals(itemsName)) { //项目级别,省级重点、市级重点、县级重点
			return ListUtils.generateList(parameter.getProjectLevel(), ",");
		}
		else if("projectClassify".equals(itemsName)) { //项目类别,储备、在建和续建
			return ListUtils.generateList(parameter.getProjectClassify(), ",");
		}
		else if("projectCategory".equals(itemsName)) { //项目分类,考核类、跟踪服务类
			return ListUtils.generateList(parameter.getProjectCategory(), ",");
		}
		else if("investmentSubject".equals(itemsName)) { //投资主体,国有、国有控股与外资合股、国有控股与民营合股、民营、民营控股与国有合资、外资独资、外资控股与国有合资、外资控股与民营合资、其他
			return ListUtils.generateList(parameter.getInvestmentSubject(), ",");
		}
		else if("managementUnit".equals(itemsName)) { //项目管理部门
			return ListUtils.generateList(parameter.getManagementUnit(), ",");
		}
		else if("constructionType".equals(itemsName)) { //建设性质,新建、扩建
			return ListUtils.generateList(parameter.getConstructionType(), ",");
		}
		else if("developmentArea".equals(itemsName)) { //开发区
			return ListUtils.generateList(parameter.getDevelopmentArea(), ",");
		}
		else if("developmentAreaCategory".equals(itemsName)) { //开发区分类
			return getDatabaseService().findRecordsByHql("select KeyProjectDevelopmentAreaCategory.category,KeyProjectDevelopmentAreaCategory.id from KeyProjectDevelopmentAreaCategory KeyProjectDevelopmentAreaCategory order by KeyProjectDevelopmentAreaCategory.category");
		}
		else if("developmentAreaAndCategory".equals(itemsName)) { //开发区分类和开发区
			List result = getDatabaseService().findRecordsByHql("select KeyProjectDevelopmentAreaCategory.category from KeyProjectDevelopmentAreaCategory KeyProjectDevelopmentAreaCategory order by KeyProjectDevelopmentAreaCategory.category");
			if(result==null) {
				result = new ArrayList();
			}
			List developmentAreas = ListUtils.generateList(parameter.getDevelopmentArea(), ",");
			if(developmentAreas!=null) {
				result.addAll(developmentAreas);
			}
			return result;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.service.KeyProjectService#listKeyProjectIndustries()
	 */
	public List listKeyProjectIndustries() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from KeyProjectIndustry KeyProjectIndustry order by KeyProjectIndustry.priority DESC, KeyProjectIndustry.industry");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.service.KeyProjectService#listKeyProjectInvestSources()
	 */
	public List listKeyProjectInvestSources() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from KeyProjectInvestSource KeyProjectInvestSource order by KeyProjectInvestSource.priority DESC, KeyProjectInvestSource.source");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.service.KeyProjectService#listDevelopmentAreaCategories()
	 */
	public List listDevelopmentAreaCategories() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from KeyProjectDevelopmentAreaCategory KeyProjectDevelopmentAreaCategory order by KeyProjectDevelopmentAreaCategory.category");
	}

	/**
	 * @return the isApprovalDebrief
	 */
	public boolean isApprovalDebrief() {
		return isApprovalDebrief;
	}

	/**
	 * @param isApprovalDebrief the isApprovalDebrief to set
	 */
	public void setApprovalDebrief(boolean isApprovalDebrief) {
		this.isApprovalDebrief = isApprovalDebrief;
	}

	/**
	 * @return the componentNames
	 */
	public String getComponentNames() {
		return componentNames;
	}

	/**
	 * @param componentNames the componentNames to set
	 */
	public void setComponentNames(String componentNames) {
		this.componentNames = componentNames;
	}
}