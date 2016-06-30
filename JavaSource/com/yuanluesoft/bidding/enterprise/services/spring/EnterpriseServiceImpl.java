package com.yuanluesoft.bidding.enterprise.services.spring;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingBidEnterprise;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEmployee;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterpriseCert;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterpriseCertSurvey;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingJobholder;
import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author yuanlue
 *
 */
public class EnterpriseServiceImpl extends BusinessServiceImpl implements EnterpriseService {
	private String enterpriseKinds; //企业性质列表
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private PageService pageService; //页面服务
	private AttachmentService attachmentService; //附件服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof BiddingBidEnterprise) {
			BiddingBidEnterprise bidEnterprise = (BiddingBidEnterprise)record;
			((BiddingService)Environment.getService("biddingService")).updateSignUps(bidEnterprise);
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof BiddingBidEnterprise) {
			BiddingBidEnterprise bidEnterprise = (BiddingBidEnterprise)record;
			((BiddingService)Environment.getService("biddingService")).updateSignUps(bidEnterprise);
		}
		else if(record instanceof BiddingEnterprise) {
			BiddingEnterprise enterprise = (BiddingEnterprise)record;
			//更新变更描述
			if(enterprise.getIsAlter()=='1' && enterprise.getIsValid()=='0') {
				BiddingEnterprise originalEnterprise = (BiddingEnterprise)load(BiddingEnterprise.class, enterprise.getAlterEnterpriseId());
				if(originalEnterprise!=null) {
					try {
						List descriptions = compareEnterprise(enterprise, originalEnterprise);
						if(descriptions==null || descriptions.isEmpty()) {
							enterprise.setAlterDescription(null);
						}
						else {
							String description = null;
							int index = 1;
							for(Iterator iterator = descriptions.iterator(); iterator.hasNext();) {
								description = (description==null ? "" : description + "\r\n") + (index++) + "、" + iterator.next();
							}
							enterprise.setAlterDescription(description);
						}
					}
					catch (Exception e) {
						Logger.exception(e);
						throw new ServiceException(e.getMessage());
					}
				}
			}
		}
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#getEnterprise(long)
	 */
	public BiddingEnterprise getEnterprise(long id) throws ServiceException {
		return (BiddingEnterprise)load(BiddingEnterprise.class, id);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(java.lang.Object)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		List errors = null;
		if(record instanceof BiddingEnterprise) {
			//检查是否重名
			BiddingEnterprise enterprise = (BiddingEnterprise)record;
			if(enterprise.getIsAlter()!='1' && enterprise.getIsNullify()!='1') {
				//检查名称是否重复
				String hql = "select BiddingEnterprise.id" +
							 " from BiddingEnterprise BiddingEnterprise" +
							 " where BiddingEnterprise.isValid='1'" +
							 " and BiddingEnterprise.name='" + JdbcUtils.resetQuot(enterprise.getName()) +"'" +
							 " and BiddingEnterprise.id!=" + enterprise.getId();
				if(getDatabaseService().findRecordByHql(hql)!=null) {
					errors = new ArrayList();
					errors.add("企业“" + enterprise.getName() + "”已经注册过。");
				}
			}
		}
		else if(record instanceof BiddingJobholder) { //从业人员
			//检查是否重复
			BiddingJobholder jobholder = (BiddingJobholder)record;
			String hql = "select BiddingJobholder.id" +
						 " from BiddingJobholder BiddingJobholder, BiddingEnterprise BiddingEnterprise" +
						 " where BiddingJobholder.enterpriseId=BiddingEnterprise.id" +
						 " and BiddingEnterprise.isValid!='C'" +
						 " and BiddingEnterprise.isNullify!='1'" +
						 " and BiddingJobholder.certificateNumber='" + JdbcUtils.resetQuot(jobholder.getCertificateNumber()) +"'" +
						 " and BiddingJobholder.id!=" + jobholder.getId() +
						 (jobholder.getAlterId()==0 ? "" : " and BiddingJobholder.id!=" + jobholder.getAlterId() + " and BiddingJobholder.alterId!=" + jobholder.getAlterId());
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				errors = new ArrayList();
				errors.add(jobholder.getName() + "已经注册过。");
			}
		}
		return errors;
	}
	
	/**
	 * 企业信息比较,返回变更信息列表
	 * @param enterprise
	 * @param originalEnterprise
	 * @return
	 * @throws Exception
	 */
	private List compareEnterprise(BiddingEnterprise enterprise, BiddingEnterprise originalEnterprise) throws Exception {
		String[] properties = {"area", "所在地区",
							   "name", "企业名称",
							   "businessLicence", "营业执照号码",
							   "statisticsLicence", "单位统计证",
							   "safeLicence", "安全许可证",
							   "registDate", "注册时间",
							   "registeredCapital", "注册资金",
							   "kind", "企业性质",
							   "legalRepresentative", "法定代表人",
							   "representativeIdNumber", "法定代表人身份证号码",
							   "representativeTel", "法定代表人联系电话",
							   "manager", "企业经理",
							   "managerIdNumber", "企业经理身份证号码",
							   "managerTel", "企业经理联系电话",
							   "technicalLeader", "技术负责人",
							   "technicalLeaderIdNumner", "技术负责人身份证号码",
							   "technicalLeaderTel", "技术负责人电话",
							   "address", "通讯地址",
							   "postalcode", "邮政编码",
							   "linkman", "联系人",
							   "tel", "联系电话",
							   "mobile", "手机",
							   "fax", "传真",
							   "email", "电子邮件",
							   "website", "企业主页",
							   "bank", "开户银行",
							   "account", "开户帐号",
							   "introduction", "经营范围",
							   "remark", "备注"};
		List descriptions = comparePojo(enterprise, originalEnterprise, properties);
		//检查新增资质
		List certs = ListUtils.getNotInsideSubList(originalEnterprise.getCerts(), "id", enterprise.getCerts(), "alterId");
		if(certs!=null) {
			for(Iterator iterator = certs.iterator(); iterator.hasNext();) {
				BiddingEnterpriseCert cert = (BiddingEnterpriseCert)iterator.next();
				descriptions.add("新增资质：" + cert.getType());
			}
		}
		//检查被删除资质
		certs = ListUtils.getNotInsideSubList(enterprise.getCerts(), "alterId", originalEnterprise.getCerts(), "id");
		if(certs!=null) {
			for(Iterator iterator = certs.iterator(); iterator.hasNext();) {
				BiddingEnterpriseCert cert = (BiddingEnterpriseCert)iterator.next();
				descriptions.add("删除资质：" + cert.getType());
			}
		}
		//检查资质修改记录
		certs = ListUtils.getInsideSubList(originalEnterprise.getCerts(), "id", enterprise.getCerts(), "alterId");
		String[] surveyProperties = {"surveyDate", "年检时间",
								 	 "surveyResult", "年检情况",
								 	 "surveyYear", "年份",
								 	 "remark", "备注"};
		if(certs!=null) {
			properties = new String[] {"certificateNumber", "资质证书编号",
									   "level", "资质等级",
									   "approvalDate", "取得资质时间",
									   "approvalNumber", "批准文号",
									   "lib", "代理所属名录库",
									   "remark", "备注"};
			for(Iterator iterator = certs.iterator(); iterator.hasNext();) {
				BiddingEnterpriseCert originalCert = (BiddingEnterpriseCert)iterator.next();
				//比较资质
				BiddingEnterpriseCert cert = (BiddingEnterpriseCert)ListUtils.findObjectByProperty(enterprise.getCerts(), "alterId", new Long(originalCert.getId()));
				List certDifferences = comparePojo(cert, originalCert, properties);
				if(certDifferences!=null && !certDifferences.isEmpty()) {
					String description = "修改资质：" + cert.getType();
					for(Iterator iteratorDiff = certDifferences.iterator(); iteratorDiff.hasNext();) {
						description += "\r\n    " + iteratorDiff.next();
					}
					descriptions.add(description);
				}
				//检查新增年检记录
				List surveies = ListUtils.getNotInsideSubList(originalCert.getSurveies(), "id", cert.getSurveies(), "alterId");
				if(surveies!=null) {
					for(Iterator iteratorSurvey = surveies.iterator(); iteratorSurvey.hasNext();) {
						BiddingEnterpriseCertSurvey survey = (BiddingEnterpriseCertSurvey)iteratorSurvey.next();
						descriptions.add("新增年检记录：" + cert.getType() + "," + survey.getSurveyYear() + "年," + survey.getSurveyResult());
					}
				}
				//检查被删除年检记录
				surveies = ListUtils.getNotInsideSubList(cert.getSurveies(), "alterId", originalCert.getSurveies(), "id");
				if(surveies!=null) {
					for(Iterator iteratorSurvey = surveies.iterator(); iteratorSurvey.hasNext();) {
						BiddingEnterpriseCertSurvey survey = (BiddingEnterpriseCertSurvey)iteratorSurvey.next();
						descriptions.add("删除年检记录：" + cert.getType() + "," + survey.getSurveyYear() + "年," + survey.getSurveyResult());
					}
				}
				//检查年检记录变更
				surveies = ListUtils.getInsideSubList(originalCert.getSurveies(), "id", cert.getSurveies(), "alterId");
				if(surveies!=null) {
					for(Iterator iteratorSurvey = surveies.iterator(); iteratorSurvey.hasNext();) {
						BiddingEnterpriseCertSurvey originalSurvey = (BiddingEnterpriseCertSurvey)iteratorSurvey.next();
						BiddingEnterpriseCertSurvey survey = (BiddingEnterpriseCertSurvey)ListUtils.findObjectByProperty(cert.getSurveies(), "alterId", new Long(originalSurvey.getId()));
						List surveyDifferences = comparePojo(survey, originalSurvey, surveyProperties);
						if(surveyDifferences!=null && !surveyDifferences.isEmpty()) {
							String description = "修改年检记录：" + cert.getType() + "," + originalSurvey.getSurveyYear() + "年," + originalSurvey.getSurveyResult();
							for(Iterator iteratorDiff = surveyDifferences.iterator(); iteratorDiff.hasNext();) {
								description += "\r\n    " + iteratorDiff.next();
							}
							descriptions.add(description);
						}
					}
				}
			}
		}
		//检查新增企业人员
		List jobholders = ListUtils.getNotInsideSubList(originalEnterprise.getJobholders(), "id", enterprise.getJobholders(), "alterId");
		if(jobholders!=null) {
			for(Iterator iterator = jobholders.iterator(); iterator.hasNext();) {
				BiddingJobholder jobholder = (BiddingJobholder)iterator.next();
				descriptions.add("新增企业人员：" + jobholder.getCategory() + "," + jobholder.getName());
			}
		}
		//检查被删除企业人员
		jobholders = ListUtils.getNotInsideSubList(enterprise.getJobholders(), "alterId", originalEnterprise.getJobholders(), "id");
		if(jobholders!=null) {
			for(Iterator iterator = jobholders.iterator(); iterator.hasNext();) {
				BiddingJobholder jobholder = (BiddingJobholder)iterator.next();
				descriptions.add("删除企业人员：" + jobholder.getCategory() + "," + jobholder.getName());
			}
		}
		//检查企业人员修改记录
		jobholders = ListUtils.getInsideSubList(originalEnterprise.getJobholders(), "id", enterprise.getJobholders(), "alterId");
		if(jobholders!=null) {
			properties = new String[] {"name", "姓名",
									   "category", "类别",
									   "identityCard", "身份证号码",
									   "school", "毕业（培训）院校",
									   "education", "学历",
									   "schoolProfessional", "毕业专业",
									   "professional", "专业",
									   "secondProfessional", "第二专业",
									   "otherProfessional", "其它专业",
									   "duty", "职务",
									   "job", "职称",
									   "qualification", "资质等级/人员类别",
									   "certificateNumber", "证书号码/培训证号",
									   "certificateCreated", "发证时间",
									   "certificateLimit", "有效期",
									   "tel", "电话",
									   "mobile", "手机",
									   "address", "通讯地址",
									   "postalCode", "邮编",
									   "remark", "备注"};
			for(Iterator iterator = jobholders.iterator(); iterator.hasNext();) {
				BiddingJobholder originalJobholder = (BiddingJobholder)iterator.next();
				//比较
				BiddingJobholder jobholder = (BiddingJobholder)ListUtils.findObjectByProperty(enterprise.getJobholders(), "alterId", new Long(originalJobholder.getId()));
				List jobholderDifferences = comparePojo(jobholder, originalJobholder, properties);
				if(jobholderDifferences!=null && !jobholderDifferences.isEmpty()) {
					String description = "修改企业人员：" + originalJobholder.getCategory() + "," + originalJobholder.getName();
					for(Iterator iteratorDiff = jobholderDifferences.iterator(); iteratorDiff.hasNext();) {
						description += "\r\n    " + iteratorDiff.next();
					}
					descriptions.add(description);
				}
			}
		}
		return descriptions;
	}
	
	/**
	 * 比较对象
	 * @param dest
	 * @param orig
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	private List comparePojo(Object dest, Object orig, String[] properties) throws Exception {
		List differences = new ArrayList();
		for(int i=0; i<properties.length; i+=2) {
			Object newValue = PropertyUtils.getProperty(dest, properties[i]);
			Object originalValue = PropertyUtils.getProperty(orig, properties[i]);
			if(((newValue==null || "".equals(newValue)) && (originalValue==null || "".equals(originalValue))) || (newValue!=null && newValue.equals(originalValue))) {
				continue;
			}
			//添加变更记录
			if(newValue==null || "".equals(newValue)) {
				differences.add("删除：" + properties[i+1]+ "，原值为“" + StringUtils.format(originalValue, null, "") + "”");
			}
			else if(originalValue==null || "".equals(originalValue)) {
				differences.add("添加：" + properties[i+1] + "，值为“" + StringUtils.format(newValue, null, "") + "”");
			}
			else {
				differences.add("修改：" + properties[i+1] + "，从“" + StringUtils.format(originalValue, null, "") + "”改为“" + StringUtils.format(newValue, null, "") + "”");
			}
		}
		return differences;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#listEnterpriseCategories()
	 */
	public List listEnterpriseCategories() throws ServiceException {
		//添加主要的企业类型
		List types = ListUtils.generateList("招标代理,施工企业,监理企业,房地产企业", ",");
		//添加其它企业类型
		String hql = "select distinct BiddingEnterpriseCert.type" +
					 " from BiddingEnterpriseCert BiddingEnterpriseCert" +
					 " where not BiddingEnterpriseCert.type in ('招标代理', '施工企业', '监理企业', '房地产企业', '其它')" +
					 " order by BiddingEnterpriseCert.type";
		List otherTypes = getDatabaseService().findRecordsByHql(hql);
		if(otherTypes!=null) {
			types.addAll(otherTypes);
		}
		//添加"其它"
		types.add("其它");
		return types;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#listEnterpriseAreas(java.lang.String)
	 */
	public List listEnterpriseAreas(String enterpriseType) throws ServiceException {
		String hql;
		if(enterpriseType==null) {
			hql = "select distinct BiddingEnterprise.area" +
				  " from BiddingEnterprise BiddingEnterprise" +
				  " order by BiddingEnterprise.area";
		}
		else {
			hql = "select distinct BiddingEnterprise.area" +
				  " from BiddingEnterprise BiddingEnterprise, BiddingEnterpriseCert BiddingEnterpriseCert" +
				  " where BiddingEnterpriseCert.enterpriseId=BiddingEnterprise.id" +
				  " and BiddingEnterpriseCert.type='" + JdbcUtils.resetQuot(enterpriseType) + "'" +
				  " order by BiddingEnterprise.area";
		}
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#listEnterprises(java.lang.String, java.lang.String)
	 */
	public List listEnterprises(String enterpriseType, String area) throws ServiceException {
		String hql = "select BiddingEnterprise" +
					 " from BiddingEnterprise BiddingEnterprise, BiddingEnterpriseCert BiddingEnterpriseCert" +
					 " where BiddingEnterpriseCert.enterpriseId=BiddingEnterprise.id" +
					 " and BiddingEnterpriseCert.type='" + JdbcUtils.resetQuot(enterpriseType) + "'" +
					 " and BiddingEnterprise.area='" + JdbcUtils.resetQuot(area) + "'" +
					 " and BiddingEnterprise.isValid='1'" +
					 " order by BiddingEnterprise.name";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#completeRegist(com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void completeRegist(BiddingEnterprise enterprise, SessionInfo sessionInfo) throws ServiceException {
		enterprise.setIsValid('1');
		//重新生成静态页面
		rebuildStaticPage(enterprise, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
	}

	/**
	 * 创建变更或注册记录
	 * @param enterpriseId
	 * @param sessionInfo
	 * @param isAlter
	 * @return
	 * @throws ServiceException
	 */
	private BiddingEnterprise createAlterOrNullify(long enterpriseId, SessionInfo sessionInfo, boolean isAlter) throws ServiceException {
		String workflowInstanceId = null;
		try {
			//获取工作流人口列表
			List workflowEntries = workflowExploitService.listWorkflowEntries("bidding/enterprise", null, sessionInfo);
			if(workflowEntries==null || workflowEntries.isEmpty()) {
				throw new ServiceException("no privilege");
			}
			
	    	//获取企业记录
			BiddingEnterprise enterprise = (BiddingEnterprise)load(BiddingEnterprise.class, enterpriseId);
			//复制企业信息
			BiddingEnterprise enterpriseAlter = new BiddingEnterprise();
			PropertyUtils.copyProperties(enterpriseAlter, enterprise);
			enterpriseAlter.setId(UUIDLongGenerator.generateId()); //ID
			enterpriseAlter.setAlterEnterpriseId(enterpriseId); //变更前的ID
			enterpriseAlter.setIsValid('0');
			enterpriseAlter.setIsAlter(isAlter ? '1' : '0'); //设置为变更记录
			enterpriseAlter.setIsNullify(isAlter ? '0' : '1'); //设置为注销记录
	    	enterpriseAlter.setOpinions(null);
	    	//复制企业资质
	    	if(enterprise.getCerts()!=null && !enterprise.getCerts().isEmpty()) {
	    		enterpriseAlter.setCerts(new HashSet());
	    		for(Iterator iterator = enterprise.getCerts().iterator(); iterator.hasNext();) {
	    			BiddingEnterpriseCert cert = (BiddingEnterpriseCert)iterator.next();
	    			BiddingEnterpriseCert certAlter = new BiddingEnterpriseCert();
	    			PropertyUtils.copyProperties(certAlter, cert);
	    			certAlter.setId(UUIDLongGenerator.generateId()); //ID
	    			certAlter.setAlterId(cert.getId());
	    			certAlter.setEnterpriseId(enterpriseAlter.getId());
	    			getDatabaseService().saveRecord(certAlter);
	    			enterpriseAlter.getCerts().add(certAlter);
	    			//复制资质年审记录
	    			if(cert.getSurveies()!=null && !cert.getSurveies().isEmpty()) {
	    				certAlter.setSurveies(new HashSet());
	    				for(Iterator iteratorSurvey = cert.getSurveies().iterator(); iteratorSurvey.hasNext();) {
	    	    			BiddingEnterpriseCertSurvey survey = (BiddingEnterpriseCertSurvey)iteratorSurvey.next();
	    	    			BiddingEnterpriseCertSurvey surveyAlter = new BiddingEnterpriseCertSurvey();
	    	    			PropertyUtils.copyProperties(surveyAlter, survey);
	    	    			surveyAlter.setId(UUIDLongGenerator.generateId()); //ID
	    	    			surveyAlter.setAlterId(survey.getId());
	    	    			surveyAlter.setCertId(certAlter.getId());
	    	    			getDatabaseService().saveRecord(surveyAlter);
	    	    			certAlter.getSurveies().add(surveyAlter);
	    				}
	    			}
	    		}
	    	}
	    	//复制企业人员
	    	if(enterprise.getJobholders()!=null && !enterprise.getJobholders().isEmpty()) {
	    		enterpriseAlter.setJobholders(new HashSet());
	    		for(Iterator iterator = enterprise.getJobholders().iterator(); iterator.hasNext();) {
	    			BiddingJobholder jobholder = (BiddingJobholder)iterator.next();
	    			BiddingJobholder jobholderAlter = new BiddingJobholder();
	    			PropertyUtils.copyProperties(jobholderAlter, jobholder);
	    			jobholderAlter.setId(UUIDLongGenerator.generateId()); //ID
	    			jobholderAlter.setAlterId(jobholder.getId());
	    			jobholderAlter.setEnterpriseId(enterpriseAlter.getId());
	    			getDatabaseService().saveRecord(jobholderAlter);
	    			enterpriseAlter.getJobholders().add(jobholderAlter);
	    		}
	    	}
			//获取流程
			WorkflowEntry workflowEntry = (WorkflowEntry)ListUtils.findObjectByProperty(workflowEntries, "workflowName", (isAlter ? "企业变更" : "企业注销"));
			//创建工作流实例
			workflowInstanceId = workflowExploitService.createWorkflowInstance(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId(), false, enterpriseAlter, null, sessionInfo);

	    	enterpriseAlter.setWorkflowInstanceId(workflowInstanceId);
	    	getDatabaseService().saveRecord(enterpriseAlter); //工作流实例ID
			return enterpriseAlter;
		}
		catch(Exception e) {
			Logger.exception(e);
			try {
				//删除流程实例
				workflowExploitService.removeWorkflowInstance(workflowInstanceId, null, sessionInfo);
			}
			catch(Exception we) {

			}
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#createAlter(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public BiddingEnterprise createAlter(long enterpriseId, SessionInfo sessionInfo) throws ServiceException {
		return createAlterOrNullify(enterpriseId, sessionInfo, true);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#completeAlter(com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void completeAlter(BiddingEnterprise enterpriseAlter, SessionInfo sessionInfo) throws ServiceException {
		try {
			//获取企业记录
			BiddingEnterprise enterprise = (BiddingEnterprise)load(BiddingEnterprise.class, enterpriseAlter.getAlterEnterpriseId());
			if(enterprise==null) {
				enterpriseAlter.setIsValid('C');
				return;
			}
			//变更前调用重新生成静态页面,以保证代理资质被删除后,对应的页面得到更新
			rebuildStaticPage(enterprise, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);

			//更新企业用户
			if(enterprise.getEmployees()!=null && !enterprise.getEmployees().isEmpty()) {
				for(Iterator iterator = enterprise.getEmployees().iterator(); iterator.hasNext();) {
					BiddingEmployee employee = (BiddingEmployee)iterator.next();
					employee.setEnterpriseName(enterpriseAlter.getName()); //更新企业名称
					getDatabaseService().updateRecord(employee);
				}
			}
			
			//更新主记录
			String workflowInstanceId = enterprise.getWorkflowInstanceId(); //工作流实例ID
			Timestamp created = enterprise.getCreated();

			PropertyUtils.copyProperties(enterprise, enterpriseAlter);

			enterprise.setId(enterprise.getAlterEnterpriseId()); //ID
			enterprise.setCreated(created); //保持原来的登记日期
			enterprise.setAlterEnterpriseId(0);
			enterprise.setIsValid('1');
			enterprise.setIsAlter('0');
			enterprise.setAlterDescription(null);
			enterprise.setWorkflowInstanceId(workflowInstanceId); //工作流实例ID
			getDatabaseService().updateRecord(enterprise);

			//删除原有的资质
			getDatabaseService().deleteRecordsByHql("from BiddingEnterpriseCert BiddingEnterpriseCert where BiddingEnterpriseCert.enterpriseId=" + enterprise.getId());
			//更新资质
			if(enterpriseAlter.getCerts()!=null && !enterpriseAlter.getCerts().isEmpty()) {
				for(Iterator iterator = enterpriseAlter.getCerts().iterator(); iterator.hasNext();) {
					BiddingEnterpriseCert certAlter = (BiddingEnterpriseCert)iterator.next();
					BiddingEnterpriseCert cert = new BiddingEnterpriseCert();
					PropertyUtils.copyProperties(cert, certAlter);
					cert.setId(cert.getAlterId()==0 ? UUIDLongGenerator.generateId() : cert.getAlterId()); //ID
					cert.setAlterId(0);
					cert.setEnterpriseId(enterprise.getId());
					getDatabaseService().saveRecord(cert);
					
					//复制资质年审记录
	    			if(certAlter.getSurveies()!=null && !certAlter.getSurveies().isEmpty()) {
	    				for(Iterator iteratorSurvey = certAlter.getSurveies().iterator(); iteratorSurvey.hasNext();) {
	    	    			BiddingEnterpriseCertSurvey surveyAlter  = (BiddingEnterpriseCertSurvey)iteratorSurvey.next();
	    	    			BiddingEnterpriseCertSurvey survey = new BiddingEnterpriseCertSurvey();
	    	    			PropertyUtils.copyProperties(survey, surveyAlter);
	    	    			survey.setId(survey.getAlterId()==0 ? UUIDLongGenerator.generateId() : survey.getAlterId()); //ID
	    	    			survey.setAlterId(0);
	    	    			survey.setCertId(cert.getId());
	    	    			getDatabaseService().saveRecord(survey);
	    				}
	    			}
				}
			}
			//删除原有的企业人员
			getDatabaseService().deleteRecordsByHql("from BiddingJobholder BiddingJobholder where BiddingJobholder.enterpriseId=" + enterprise.getId());
			//更新企业人员
			if(enterpriseAlter.getJobholders()!=null && !enterpriseAlter.getJobholders().isEmpty()) {
				for(Iterator iterator = enterpriseAlter.getJobholders().iterator(); iterator.hasNext();) {
					BiddingJobholder jobholderAlter = (BiddingJobholder)iterator.next();
					BiddingJobholder jobholder = new BiddingJobholder();
					PropertyUtils.copyProperties(jobholder, jobholderAlter);
					jobholder.setId(jobholder.getAlterId()==0 ? UUIDLongGenerator.generateId() : jobholder.getAlterId()); //ID
					jobholder.setAlterId(0);
					jobholder.setEnterpriseId(enterprise.getId());
					getDatabaseService().saveRecord(jobholder);
				}
			}
			enterpriseAlter.setIsValid('C');
			//重新生成静态页面
			rebuildStaticPage(enterprise, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		} 
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#createNullify(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public BiddingEnterprise createNullify(long enterpriseId, SessionInfo sessionInfo) throws ServiceException {
		return createAlterOrNullify(enterpriseId, sessionInfo, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#completeNullify(com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void completeNullify(BiddingEnterprise enterpriseNullify, SessionInfo sessionInfo) throws ServiceException {
		BiddingEnterprise enterprise = (BiddingEnterprise)load(BiddingEnterprise.class, enterpriseNullify.getAlterEnterpriseId());
		getDatabaseService().deleteRecord(enterprise);
		enterpriseNullify.setIsValid('C');
		//重新生成静态页面
		rebuildStaticPage(enterprise, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#importBidEnterprises(long)
	 */
	public void importBidEnterprises(long importId) throws ServiceException {
		List attachments = attachmentService.list("bidding/enterprise", "data", importId, false, 0, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		Attachment attachment = (Attachment)attachments.get(0);
		Workbook wb = null;
		try {
			//导入数据
			wb = Workbook.getWorkbook(new File(attachment.getFilePath())); //构造Workbook（工作薄）对象
			Sheet sheet = wb.getSheet(0);
			importSheet(sheet); //导入SHEET
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException("导入失败");
		}
		finally {
			try {
				wb.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 导入一个SHEET
	 * @param sheet
	 * @throws ServiceException
	 */
	private void importSheet(Sheet sheet) throws ServiceException {
		List colNames = new ArrayList(); //表头行
		for(int i=0; i<sheet.getRows(); i++) {
			Cell[] cells = sheet.getRow(i);
			//如果表头为空,检查是否表头,判断依据该行必须要有“账号”单元格
			if(colNames.isEmpty()) {
				for(int j=0; j<cells.length; j++) {
					String content = ExcelUtils.getCellString(cells, j);
					if(content==null) {
						content = "";
					}
					colNames.add(content.trim());
				}
				int index = colNames.indexOf("账号");
				if(index==-1) {
					colNames.clear(); //不含“姓名”单元格,不是表头
				}
				continue;
			}
			
			//读取评议员记录
			BiddingBidEnterprise bidEnterprise = new BiddingBidEnterprise();
			for(int j=0; j<cells.length; j++) {
				String content = ExcelUtils.getCellString(cells, j);
				if(content!=null) {
					content = content.trim().replaceAll("\\xa0", "").replaceAll(new String(new char[]{65533}), "");
				}
				//根据表头设置属性
				String colName = (String)colNames.get(j);
				if("账号".equals(colName)) {
					bidEnterprise.setAccount(content);
				}
				else if("户名".equals(colName)) {
					bidEnterprise.setName(content);
				}
				else if("开户行".equals(colName)) {
					bidEnterprise.setBank(content);
				}
			}
			if(bidEnterprise.getName()==null || bidEnterprise.getName().isEmpty() || //户名为空
			   bidEnterprise.getAccount()==null || bidEnterprise.getAccount().isEmpty() || //账号
			   bidEnterprise.getBank()==null || bidEnterprise.getBank().isEmpty()) { //开户行
				continue;
			}
			//检查相同帐号是否已经存在
			String hql = "select BiddingBidEnterprise.id from BiddingBidEnterprise BiddingBidEnterprise where BiddingBidEnterprise.account='" + bidEnterprise.getAccount() + "'";
			Number id = (Number)getDatabaseService().findRecordByHql(hql);
			if(id==null) {
				//保存
				bidEnterprise.setId(UUIDLongGenerator.generateId()); //ID
				save(bidEnterprise);
			}
			else {
				//更新
				bidEnterprise.setId(id.longValue()); //ID
				update(bidEnterprise);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#listAgentLibs()
	 */
	public List listAgentLibs() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from BiddingAgentLib BiddingAgentLib order by BiddingAgentLib.id");
	}
	
	/**
	 * 增加投标企业
	 * @param account
	 * @param accountName
	 * @param bank
	 * @throws ServiceException
	 */
	public void addBidEnterprise(String account, String accountName, String bank) throws ServiceException {
		if(account==null || account.trim().isEmpty() || account.indexOf('*')!=-1 || accountName==null || accountName.trim().isEmpty()) {
			return;
		}
		//从投标企业库中提取
		String hql = "from BiddingBidEnterprise BiddingBidEnterprise" +
					 " where BiddingBidEnterprise.account='" + JdbcUtils.resetQuot(account.trim()) + "'";
		List bidEnterprises = getDatabaseService().findRecordsByHql(hql, 0, 2);
		if(bidEnterprises==null || bidEnterprises.isEmpty()) {
			BiddingBidEnterprise bidEnterprise = new BiddingBidEnterprise();
			bidEnterprise.setId(UUIDLongGenerator.generateId());
			bidEnterprise.setAccount(account.trim());
			bidEnterprise.setName(accountName.trim());
			bidEnterprise.setBank(bank==null ? null : bank.trim());
			getDatabaseService().saveRecord(bidEnterprise);
		}
		else if(bidEnterprises.size()==1) {
			BiddingBidEnterprise bidEnterprise = (BiddingBidEnterprise)bidEnterprises.get(0);
			bidEnterprise.setName(accountName.trim());
			if(bank!=null && !bank.isEmpty()) {
				bidEnterprise.setBank(bank.trim());
			}
			getDatabaseService().updateRecord(bidEnterprise);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#findBidEnterprise(java.lang.String, java.lang.String)
	 */
	public List findBidEnterprise(String account, String accountName) throws ServiceException {
		String hql = null;
		if(account.indexOf('*')==-1) { //账户中不含*号
			hql = "from BiddingBidEnterprise BiddingBidEnterprise" +
				  " where BiddingBidEnterprise.account='" + JdbcUtils.resetQuot(account) + "'";
		}
		else if(accountName!=null && !accountName.isEmpty() && accountName.indexOf('*')==-1) {
			String accountPart = account.replaceAll("[*]", "");
			if(!accountPart.isEmpty()) {
				hql = "from BiddingBidEnterprise BiddingBidEnterprise" +
					  " where BiddingBidEnterprise.name='" + JdbcUtils.resetQuot(accountName) + "'" +
					  " and BiddingBidEnterprise.account like '%" + JdbcUtils.resetQuot(accountPart) + "%'";
			}
		}
		return hql==null ? null : getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseService#isEnterpriseValid(long)
	 */
	public boolean isEnterpriseValid(long enterpriseId) {
		String hql = "select BiddingEnterprise.isValid from BiddingEnterprise BiddingEnterprise where BiddingEnterprise.id=" + enterpriseId;
		Character isValid = (Character)getDatabaseService().findRecordByHql(hql);
		return isValid!=null && isValid.charValue()=='1';
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("agentLib".equals(itemsName)) { //代理名录库
			return getDatabaseService().findRecordsByHql("select BiddingAgentLib.lib from BiddingAgentLib BiddingAgentLib order by BiddingAgentLib.id");
		}
		else if("area".equals(itemsName)) { //地区列表
			String hql = "select distinct BiddingEnterprise.area" +
						 " from BiddingEnterprise BiddingEnterprise" +
						 " order by BiddingEnterprise.area";
			return getDatabaseService().findRecordsByHql(hql);
		}
		else if("enterpriseKind".equals(itemsName)) { //企业类型
			return ListUtils.generateList(enterpriseKinds, ",");
		}
		else if("enterpriseCategory".equals(itemsName)) { //企业类型
			return listEnterpriseCategories();
		}
		else if("qualification".equals(itemsName)) { //资质等级/人员类别
			String category = null;
			try {
				category = (String)PropertyUtils.getProperty(bean, "jobholder.category");
			}
			catch (Exception e) {
				
			}
			if("项目经理".equals(category)) {
				return ListUtils.generateList("一级,二级,三级", ",");
			}
			else if("注册监理师".equals(category)) {
				return ListUtils.generateList("初级,中级,高级", ",");
			}
			else if("注册建造师".equals(category)) {
				return ListUtils.generateList("一级,二级", ",");
			}
			else if("五大员".equals(category)) {
				return ListUtils.generateList("预算员,施工员,质检员,安全员,材料员", ",");
			}
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}
	
	/**
	 * 重新生成静态页面
	 * @param enterprise
	 * @param modifyAction
	 * @throws ServiceException
	 */
	private void rebuildStaticPage(BiddingEnterprise enterprise, String modifyAction) throws ServiceException {
		//重新生成和企业关联的静态页面
		pageService.rebuildStaticPageForModifiedObject(enterprise, modifyAction);
		//重新和招标按代理资质关联的静态页面
		BiddingEnterpriseCert biddingEnterpriseCert = (BiddingEnterpriseCert)ListUtils.findObjectByProperty(enterprise.getCerts(), "type", "招标代理");
		if(biddingEnterpriseCert!=null) {
			pageService.rebuildStaticPageForModifiedObject(biddingEnterpriseCert, modifyAction);
		}
	}

	/**
	 * @return the enterpriseKinds
	 */
	public String getEnterpriseKinds() {
		return enterpriseKinds;
	}

	/**
	 * @param enterpriseKinds the enterpriseKinds to set
	 */
	public void setEnterpriseKinds(String enterpriseKinds) {
		this.enterpriseKinds = enterpriseKinds;
	}

	/**
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
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

	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
}