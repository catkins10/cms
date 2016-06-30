package com.yuanluesoft.bidding.project.service.spring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectCity;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectFile;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectFileItem;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectParameter;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectWorkflow;
import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.numeration.service.NumerationCallback;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
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
public class BiddingProjectParameterServiceImpl extends BusinessServiceImpl implements BiddingProjectParameterService {
	private String projectCategories; //工程类别列表,如：建安|建安|building,市政|市政|publicworks,水利|水利|irrigation,其他|其他|other
	private String projectProcedures; //招标内容列表,如：施工|construct,设计|design,监理|supervision,专业工程|professional,公用项目|communal
	private String cities; //地区列表,福州|fuzhou,南平|nanping
	private String realNameProjectProcedures; //实名报名的工程类别和环节,如:福州/南平|建安/市政|施工/设计/监理
	private NumerationService numerationService; //编号服务
	private Cache cache; //缓存,用来存放区域信息

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof BiddingProjectCity) {
			try {
				cache.clear();
			}
			catch (CacheException e) {
			
			}
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof BiddingProjectCity) {
			try {
				cache.clear();
			}
			catch (CacheException e) {
			
			}
		}
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingParameterService#generateBiddingNumber(com.yuanluesoft.bidding.project.pojo.BiddingProject, boolean)
	 */
	public String generateBiddingNumber(BiddingProject project, boolean preview) throws ServiceException {
		return generateNumeration(project, "biddingNumberFormat", "招标编号", preview);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingParameterService#generateSignUpNumber(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public String generateSignUpNumber(BiddingProject project, boolean preview) throws ServiceException {
		return generateNumeration(project, "signUpNumberFormat", "报名号", preview);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingParameterService#generateDeclareNumber(com.yuanluesoft.bidding.project.pojo.BiddingProject, boolean)
	 */
	public String generateDeclareNumber(BiddingProject project, boolean preview) throws ServiceException {
		return generateNumeration(project, "declareNumberFormat", "报建编号", preview);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingParameterService#generateDeclareReceiveNumber(com.yuanluesoft.bidding.project.pojo.BiddingProject, boolean)
	 */
	public String generateDeclareReceiveNumber(BiddingProject project, boolean preview) throws ServiceException {
		return generateNumeration(project, "declareReceiveNumberFormat", "报建受理编号", preview);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingParameterService#generateNoticeNumber(com.yuanluesoft.bidding.project.pojo.BiddingProject, boolean)
	 */
	public String generateNoticeNumber(BiddingProject project, boolean preview) throws ServiceException {
		return generateNumeration(project, "noticeNumberFormat", "中标通知书编号", preview);
	}

	/**
	 * 生成编号
	 * @param format
	 * @param preview
	 * @return
	 * @throws ServiceException
	 */
	private synchronized String generateNumeration(final BiddingProject project, String formatProperty, String numerationName, boolean preview) throws ServiceException {
		BiddingProjectParameter parameter = getParameter(project.getProjectCategory(), project.getProjectProcedure(), project.getCity());
		String format;
		try {
			format = (String)PropertyUtils.getProperty(parameter, formatProperty);
		}
		catch (Exception e) {
			return null;
		}
		if(format==null) {
			return null;
		}
		NumerationCallback numerationCallback = new NumerationCallback() {
			public Object getFieldValue(String fieldName, int fieldLength) {
				if("地区简称".equals(fieldName)) {
					try {
						return getCityDetail(project.getCity()).getShortName();
					}
					catch (ServiceException e) {
						Logger.exception(e);
					}
				}
				else if("类别简称".equals(fieldName)) {
					String[] categories = projectCategories.split(",");
					for(int i=0; i<categories.length; i++) {
						if(categories[i].startsWith(project.getProjectCategory() + "|")) {
							return categories[i].split("\\|")[1];
						}
					}
				}
				else if("项目编号".equals(fieldName)) {
					return project.getProjectNumber();
				}
				return null;
			}
		};
		return numerationService.generateNumeration("项目管理", numerationName, format, preview, numerationCallback);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectParameterService#getParameter(java.lang.String, java.lang.String, java.lang.String)
	 */
	public BiddingProjectParameter getParameter(String projectCategory, String projectProcedure, String projectCity) throws ServiceException {
		String hql = "from BiddingProjectParameter BiddingProjectParameter" +
					 " where BiddingProjectParameter.categories like '%," + JdbcUtils.resetQuot(projectCategory) + ",%'" +
					 " and BiddingProjectParameter.procedures like '%," + JdbcUtils.resetQuot(projectProcedure) + ",%'" +
					 " and BiddingProjectParameter.cities like '%," + JdbcUtils.resetQuot(projectCity) + ",%'";
		return (BiddingProjectParameter)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectParameterService#getWorkflowId(java.lang.String, java.lang.String, java.lang.String)
	 */
	public long getWorkflowId(String projectCategory, String projectProcedure, String projectCity) throws ServiceException {
		String hql = "select BiddingProjectWorkflow.workflowId" +
					 " from BiddingProjectWorkflow BiddingProjectWorkflow" +
				     " where BiddingProjectWorkflow.categories like '%," + JdbcUtils.resetQuot(projectCategory) + ",%'" +
				     " and BiddingProjectWorkflow.procedures like '%," + JdbcUtils.resetQuot(projectProcedure) + ",%'" +
				     " and BiddingProjectWorkflow.cities like '%," + JdbcUtils.resetQuot(projectCity) + ",%'";
		Number workflowId = (Number)getDatabaseService().findRecordByHql(hql);
		return workflowId==null ? 0 : workflowId.longValue();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectParameterService#getCityDetail(java.lang.String)
	 */
	public BiddingProjectCity getCityDetail(String city) throws ServiceException {
		try {
			BiddingProjectCity biddingProjectCity = (BiddingProjectCity)cache.get(city);
			if(biddingProjectCity==null) {
				biddingProjectCity = (BiddingProjectCity)getDatabaseService().findRecordByHql("from BiddingProjectCity BiddingProjectCity where BiddingProjectCity.name='" + JdbcUtils.resetQuot(city) + "'");
				cache.put(city, biddingProjectCity);
			}
			return biddingProjectCity;
		}
		catch(CacheException ce) {
			throw new ServiceException(ce);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectParameterService#getCityDetail(long)
	 */
	public BiddingProjectCity getCityDetail(long projectId) throws ServiceException {
		String hql = "select BiddingProject.city from BiddingProject BiddingProject where BiddingProject.id=" + projectId;
		return getCityDetail((String)getDatabaseService().findRecordByHql(hql));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectParameterService#addProjectFileItem(long, float, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addProjectFileItem(long projectFileConfigId, double fileSn, String fileType, String fileName, String fileCategory, String remark) throws ServiceException {
		BiddingProjectFileItem fileItem = new BiddingProjectFileItem();
		fileItem.setId(UUIDLongGenerator.generateId()); //ID
		fileItem.setFileType(fileType); //资料类型
		fileItem.setSn(fileSn); //序号
		fileItem.setConfigId(projectFileConfigId); //项目分类ID
		fileItem.setFileCategory(fileCategory); //资料分类
		fileItem.setName(fileName); //资料名称
		fileItem.setRemark(remark); //备注
		getDatabaseService().saveRecord(fileItem);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingParameterService#deleteProjectFile(long)
	 */
	public void deleteProjectFileItem(long fileItemId) throws ServiceException {
		getDatabaseService().deleteRecordById(BiddingProjectFileItem.class.getName(), fileItemId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectParameterService#listProjectFileItems(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List listProjectFileItems(String projectCategory, String projectProcedure, String projectCity, String fileType) throws ServiceException {
		String hql = "from BiddingProjectFile BiddingProjectFile" +
				     " where BiddingProjectFile.categories like '%," + JdbcUtils.resetQuot(projectCategory) + ",%'" +
				     " and BiddingProjectFile.procedures like '%," + JdbcUtils.resetQuot(projectProcedure) + ",%'" +
				     " and BiddingProjectFile.cities like '%," + JdbcUtils.resetQuot(projectCity) + ",%'";
		BiddingProjectFile file = (BiddingProjectFile)getDatabaseService().findRecordByHql(hql);
		if(file==null) {
			return null;
		}
		hql = "from BiddingProjectFileItem BiddingProjectFileItem" +
			  " where BiddingProjectFileItem.configId=" + file.getId() +
			  " and BiddingProjectFileItem.fileType='" + JdbcUtils.resetQuot(fileType) + "'" +
			  " order by BiddingProjectFileItem.sn";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectParameterService#isNeedFullProjectFiles(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean isNeedFullProjectFiles(String projectCategory, String projectProcedure, String projectCity, String fileType) throws ServiceException {
		String hql = "from BiddingProjectFile BiddingProjectFile" +
				     " where BiddingProjectFile.categories like '%," + JdbcUtils.resetQuot(projectCategory) + ",%'" +
				     " and BiddingProjectFile.procedures like '%," + JdbcUtils.resetQuot(projectProcedure) + ",%'" +
				     " and BiddingProjectFile.cities like '%," + JdbcUtils.resetQuot(projectCity) + ",%'";
		BiddingProjectFile file = (BiddingProjectFile)getDatabaseService().findRecordByHql(hql);
		if(file==null) {
			return false;
		}
		return file.getNeedFull()=='1';
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectParameterService#isPledgeInternetPayment(long)
	 */
	public boolean isPledgeInternetPayment(long projectId) throws ServiceException {
		String hql = "select BiddingProjectCity.pledgeInternetPayment" +
			  		 " from BiddingProjectCity BiddingProjectCity" +
			  		 " where BiddingProjectCity.name=(select BiddingProject.city from BiddingProject BiddingProject where BiddingProject.id=" + projectId + ")";
		Number pledgeInternetPayment = (Number)getDatabaseService().findRecordByHql(hql);
		return pledgeInternetPayment!=null && pledgeInternetPayment.intValue()==1;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectParameterService#getCityEnglishName(java.lang.String)
	 */
	public String getCityEnglishName(String cityName) throws ServiceException {
		if(cityName==null || cityName.isEmpty()) {
			return null;
		}
		String[] values = cities.split(",");
		for(int i=0; i<values.length; i++) {
			if(values[i].startsWith(cityName + "|")) {
				return values[i].substring(values[i].lastIndexOf('|') + 1);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectParameterService#getProjectCategoryEnglishName(java.lang.String)
	 */
	public String getProjectCategoryEnglishName(String projectCategory) throws ServiceException {
		if(projectCategory==null || projectCategory.isEmpty()) {
			return null;
		}
		String[] values = projectCategories.split(",");
		for(int i=0; i<values.length; i++) {
			if(values[i].startsWith(projectCategory + "|")) {
				return values[i].substring(values[i].lastIndexOf('|') + 1);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectParameterService#getProjectProcedureEnglishName(java.lang.String)
	 */
	public String getProjectProcedureEnglishName(String projectProcedure) throws ServiceException {
		if(projectProcedure==null || projectProcedure.isEmpty()) {
			return null;
		}
		String[] values = projectProcedures.split(",");
		for(int i=0; i<values.length; i++) {
			if(values[i].startsWith(projectProcedure + "|")) {
				return values[i].substring(values[i].lastIndexOf('|') + 1);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectParameterService#listProjectCreatableCities(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listProjectCreatableCities(SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo==null) {
			return null;
		}
		if(sessionInfo instanceof BiddingSessionInfo) {
			BiddingSessionInfo biddingSessionInfo = (BiddingSessionInfo)sessionInfo;
			return biddingSessionInfo.isAgent() ? listCities() : null; //如果是代理,返回所有的地区
		}
		//获取用户有创建权限的地区列表
		return getDatabaseService().findPrivilegedRecords(BiddingProjectCity.class.getName(), "BiddingProjectCity.name", null, null, "BiddingProjectCity.name", null, BIDDING_CITY_PROJECT_CREATOR, true, null, 0, 0, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectParameterService#listReportVisitableCities(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listReportVisitableCities(SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo==null) {
			return null;
		}
		//获取用户有报表查看权限的地区列表
		return getDatabaseService().findPrivilegedRecords(BiddingProjectCity.class.getName(), "BiddingProjectCity.name", null, null, "BiddingProjectCity.name", null, BIDDING_CITY_REPORT_VISITOR, true, null, 0, 0, sessionInfo);
	}

	/**
	 * 获取地区列表
	 * @return
	 * @throws ServiceException
	 */
	private List listCities() throws ServiceException {
		String[] values = cities.split(",");
		List items = new ArrayList();
		for(int i=0; i<values.length; i++) {
			items.add(values[i].split("\\|")[0]);
		}
		return items;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("city".equals(itemsName)) { //地区列表
			return listCities();
		}
		else if("category".equals(itemsName)) { //项目分类
			String[] values = projectCategories.split(",");
			List items = new ArrayList();
			for(int i=0; i<values.length; i++) {
				items.add(values[i].split("\\|")[0]);
			}
			return items;
		}
		else if("procedure".equals(itemsName)) { //招标内容
			String[] values = projectProcedures.split(",");
			List items = new ArrayList();
			for(int i=0; i<values.length; i++) {
				items.add(values[i].split("\\|")[0]);
			}
			return items;
		}
		else if("archiveFileCategory".equals(itemsName)) { //归档资料分类
			try {
				String hql = "select distinct BiddingProjectFileItem.fileCategory" +
							 " from BiddingProjectFileItem BiddingProjectFileItem" +
							 " where BiddingProjectFileItem.configId=" + PropertyUtils.getProperty(bean, "id") +
							 " and BiddingProjectFileItem.fileType='归档资料'" +
							 " and not BiddingProjectFileItem.fileCategory is null" +
							 " order by BiddingProjectFileItem.fileCategory";
				return getDatabaseService().findRecordsByHql(hql);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		else if("creatableCity".equals(itemsName)) {
			return listProjectCreatableCities(sessionInfo);
		}
		else if("reportVisitableCity".equals(itemsName)) {
			return listReportVisitableCities(sessionInfo);
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long workflowConfigId = RequestUtils.getParameterLongValue(notifyRequest, "workflowConfigId"); //工作流配置ID
		BiddingProjectWorkflow workflowConfig = (BiddingProjectWorkflow)load(BiddingProjectWorkflow.class, workflowConfigId);
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) { //流程删除操作
			workflowConfig.setWorkflowId(0); //流程ID 
			workflowConfig.setWorkflowName(null); //流程名称
		}
		else { //新建或更新流程
			workflowConfig.setWorkflowId(Long.parseLong(workflowId)); //流程ID 
			workflowConfig.setWorkflowName(workflowPackage.getName()); //流程名称
		}
		update(workflowConfig);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectParameterService#isRealNameSignUp(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean isRealNameSignUp(String city, String projectCategory, String projectProcedure) throws ServiceException {
		if(this.realNameProjectProcedures==null) {
			return false; 
		}
		String[] values = {city, projectCategory, projectProcedure};
		String[] procedures = this.realNameProjectProcedures.split(","); //realNameProjectProcedures:福州/南平|建安/市政|施工/设计/监理
		for(int i=0; i<procedures.length; i++) {
			String[] parts = procedures[i].split("\\|");
			int j=0;
			for(; j<3 && ("/" + parts[j] + "/").indexOf("/" + values[j] + "/")!=-1; j++);
			if(j==3) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the cities
	 */
	public String getCities() {
		return cities;
	}

	/**
	 * @param cities the cities to set
	 */
	public void setCities(String cities) {
		this.cities = cities;
	}

	/**
	 * @return the projectProcedures
	 */
	public String getProjectProcedures() {
		return projectProcedures;
	}

	/**
	 * @param projectProcedures the projectProcedures to set
	 */
	public void setProjectProcedures(String projectProcedures) {
		this.projectProcedures = projectProcedures;
	}

	/**
	 * @return the projectCategories
	 */
	public String getProjectCategories() {
		return projectCategories;
	}

	/**
	 * @param projectCategories the projectCategories to set
	 */
	public void setProjectCategories(String projectCategories) {
		this.projectCategories = projectCategories;
	}

	/**
	 * @return the numerationService
	 */
	public NumerationService getNumerationService() {
		return numerationService;
	}

	/**
	 * @param numerationService the numerationService to set
	 */
	public void setNumerationService(NumerationService numerationService) {
		this.numerationService = numerationService;
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	/**
	 * @return the realNameProjectProcedures
	 */
	public String getRealNameProjectProcedures() {
		return realNameProjectProcedures;
	}

	/**
	 * @param realNameProjectProcedures the realNameProjectProcedures to set
	 */
	public void setRealNameProjectProcedures(String realNameProjectProcedures) {
		this.realNameProjectProcedures = realNameProjectProcedures;
	}
}