package com.yuanluesoft.bidding.project.service.spring;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import FZTDecode.FZTDecodeJava;

import com.jinrun.filetransfer.JinRunFileFilter;
import com.yuanluesoft.bidding.biddingroom.service.BiddingRoomService;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectBidopening;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectCompletion;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectComponent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectDocument;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectGrade;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectJobholder;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectNotice;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectOwner;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectParameter;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPitchon;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPitchout;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPlan;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectTender;
import com.yuanluesoft.bidding.project.pojo.supervise.SuperviseBiddingProjectGrade;
import com.yuanluesoft.bidding.project.pojo.supervise.SuperviseBiddingProjectPitchout;
import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUpJobholder;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author yuanlue
 *
 */
public class BiddingProjectServiceImpl extends BusinessServiceImpl implements BiddingProjectService {
	private AttachmentService attachmentService; //附件管理服务
	private TemporaryFileManageService temporaryFileManageService; //临时文件管理服务
	private BiddingProjectParameterService biddingProjectParameterService; //参数服务
	private BiddingRoomService biddingRoomService; //开评标室服务
	private PageService pageService; //页面服务
	private String jinrunIp; //评标系统IP
	//评标系统中招标文件存放路径
	private String jinrunBiddingDocumentsPath;
	//评标系统中投标报名清单存放路径
	private String jinrunSignUpsPath;
	//评标系统中开标xml路径和文件名称
	private String jinrunBidopeningPath;
	//评标系统中中标结果xml路径和文件名称
	private String jinrunPitchonPath;
	//评标系统中投标文件路径
	private String jinrunBidPath;
	
	//监察数据库
	private DatabaseService superviseDAO;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		if(recordClass.equals(BiddingProject.class)) {
			BiddingService biddingService = (BiddingService)Environment.getService("biddingService");
			biddingService.updatePledgeReturnStatus(id); //如果报名记录中有保证金已转账但未确认退还的,则检查保证金是否退还成功
		}
		Record record = super.load(recordClass, id);
		if(record instanceof BiddingProject) {
			BiddingService biddingService = (BiddingService)Environment.getService("biddingService");
			BiddingProject project = (BiddingProject)record;
			if(project.getSignUps()!=null) {
				biddingService.decryptSignUps(project);
				List signUps = new ArrayList(project.getSignUps());
				//报名记录按评标排名排序
				Collections.sort(signUps, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						BiddingSignUp signUp0 = (BiddingSignUp)arg0;
						BiddingSignUp signUp1 = (BiddingSignUp)arg1;
						if(signUp0.getRanking()==signUp1.getRanking()) {
							return 0;
						}
						else if(signUp0.getRanking()==0) {
							return 1;
						}
						else if(signUp1.getRanking()==0) {
							return -1;
						}
						else {
							return signUp0.getRanking()>signUp1.getRanking() ? 1 : -1;
						}
					}
				});
				project.setSignUps(new LinkedHashSet(signUps));
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		super.save(record);
		if(record instanceof BiddingProject) {
			updateProjectJobholders((BiddingProject)record, true);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		if(!(record instanceof BiddingProject)) {
			return super.update(record);
		}
		boolean superviseSynch = false;
		BiddingProject project = (BiddingProject)record;
		if(project.getRegistTime()!=null && superviseDAO!=null) { //登记时间不为空
			//检查原来的登记时间是否为空
			superviseSynch = getDatabaseService().findRecordByHql("select BiddingProject.registTime from BiddingProject BiddingProject where BiddingProject.id=" + project.getId())==null;
		}
		project = (BiddingProject)super.update(record);
		updateProjectJobholders(project, false);
		if(superviseSynch) {
			superviseSynch(project, "save");
		}
		if("是".equals(project.getFailed())) { //招标失败
			//解锁全部从业人员
			unlockJobholders(project, false, false);
		}
		rebuildStaticPage(project, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重新生成静态页面
		return project;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		superviseSynch(record, "delete");
		//解锁全部从业人员
		if(record instanceof BiddingProject) {
			unlockJobholders((BiddingProject)record, false, false);
		}
		rebuildStaticPage((BiddingProject)record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
	}
	
	/**
	 * 更新项目参与人员
	 * @param project
	 * @param isNew
	 * @throws ServiceException
	 */
	private void updateProjectJobholders(BiddingProject project, boolean isNew) throws ServiceException {
		if(!isNew) {
			getDatabaseService().deleteRecordsByHql("from BiddingProjectJobholder BiddingProjectJobholder where BiddingProjectJobholder.projectId=" + project.getId());
		}
		for(Iterator iterator = project.getJobholders()==null ? null : project.getJobholders().iterator(); iterator!=null &&iterator.hasNext();) {
			BiddingProjectJobholder jobholder = (BiddingProjectJobholder)iterator.next();
			jobholder.setId(UUIDLongGenerator.generateId()); //ID
			jobholder.setProjectId(project.getId()); //项目ID
			save(jobholder);
		}
	}
	
	/**
	 * 解锁从业人员
	 * @param project
	 * @param rejectRanked
	 * @param rejectPitchon
	 * @throws ServiceException
	 */
	private void unlockJobholders(BiddingProject project, boolean rejectRanked, boolean rejectPitchon) throws ServiceException {
		String hql = "select BiddingSignUpJobholder" +
					 " from BiddingSignUpJobholder BiddingSignUpJobholder, BiddingSignUp BiddingSignUp" +
					 " where BiddingSignUpJobholder.locked=1" +
					 " and BiddingSignUpJobholder.signUpId=BiddingSignUp.id" +
					 " and BiddingSignUp.projectId=" + project.getId();
		if(rejectRanked) { //排除中标候选人
			hql += " and BiddingSignUp.ranking=0";
		}
		else if(rejectPitchon && project.getNotice()!=null) { //排除中标人
			hql += " and BiddingSignUp.id!=" + project.getNotice().getPitchonEnterpriseId();
		}
		hql += " order by BiddingSignUp.id";
		for(;;) {
			List jobholders = getDatabaseService().findRecordsByHql(hql, 0, 200);
			for(Iterator iterator = jobholders==null ? null : jobholders.iterator(); iterator!=null && iterator.hasNext();) {
				BiddingSignUpJobholder jobholder = (BiddingSignUpJobholder)iterator.next();
				jobholder.setLocked(0);
				jobholder.setUnlockTime(DateTimeUtils.now());
				getDatabaseService().updateRecord(jobholder);
			}
			if(jobholders==null || jobholders.size()<200) {
				break;
			}
		}
	}
	
	/**
	 * 重新生成静态页面
	 * @param project
	 * @param modifyAction
	 * @throws ServiceException
	 */
	private void rebuildStaticPage(BiddingProject project, String modifyAction) throws ServiceException {
		pageService.rebuildStaticPageForModifiedObject(project, modifyAction); //重新生成静态页面
		//重新生成所有组成部分关联的静态页面
		BusinessObject businessObject = getBusinessDefineService().getBusinessObject(project.getClass());
		for(Iterator iterator=businessObject.getFields().iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(!"components".equals(field.getType())) { //不是组成部分
				continue;
			}
			try {
				Set components = (Set)PropertyUtils.getProperty(project, field.getName());
				if(components==null || components.isEmpty()) {
					continue;
				}
				for(Iterator iteratorComponent=components.iterator(); iteratorComponent.hasNext();) {
					BiddingProjectComponent component = (BiddingProjectComponent)iteratorComponent.next();
					pageService.rebuildStaticPageForModifiedObject(component, modifyAction); //重新生成静态页面
				}
			}
			catch(Exception e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#getProject(long)
	 */
	public BiddingProject getProject(long id) throws ServiceException {
		return (BiddingProject)load(BiddingProject.class, id);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#saveProjectComponent(long, java.lang.String, java.lang.String, com.yuanluesoft.bidding.project.pojo.BiddingProjectComponent)
	 */
	public void saveProjectComponent(long projectId, String projectName, String biddingMode, BiddingProjectComponent component) throws ServiceException {
		boolean isNew = (component.getId()==0);
		if(isNew) { //新记录
			component.setId(UUIDLongGenerator.generateId());
		}
		component.setProjectId(projectId);
		component.setProjectName(projectName);
		component.setBiddingMode(biddingMode);
		if(isNew) {
			getDatabaseService().saveRecord(component);
		}
		else {
			getDatabaseService().updateRecord(component);
		}
		if(component.getPublicBeginTime()!=null) {
			pageService.rebuildStaticPageForModifiedObject(component, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重新生成静态页面
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#removeProjectComponent(com.yuanluesoft.bidding.project.pojo.BiddingProjectComponent)
	 */
	public void removeProjectComponent(BiddingProjectComponent component) throws ServiceException {
		if(component.getId()==0) {
			return;
		}
		getDatabaseService().deleteRecord(component);
		if(component.getPublicBeginTime()!=null) {
			pageService.rebuildStaticPageForModifiedObject(component, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE); //重新生成静态页面
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#publicProjectComponent(com.yuanluesoft.bidding.project.pojo.BiddingProjectComponent, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void publicProjectComponent(BiddingProjectComponent component, SessionInfo sessionInfo) throws ServiceException {
		if(sessionInfo!=null) {
			component.setPublicPersonId(sessionInfo.getUserId()); //发布人ID
			component.setPublicPersonName(sessionInfo.getUserName()); //发布人姓名
		}
		component.setPublicBeginTime(DateTimeUtils.now()); //发布时间
		if(component.getPublicLimit()>0) { //公示期限>0
			component.setPublicEndTime(DateTimeUtils.add(component.getPublicBeginTime(), Calendar.DAY_OF_MONTH, component.getPublicLimit())); //截止时间
		}
		if(component.getId()==0) {
			component.setId(UUIDLongGenerator.generateId());
			getDatabaseService().saveRecord(component);
		}
		else {
			getDatabaseService().updateRecord(component);
		}
		superviseSynch(component, "save");
		if(component instanceof BiddingProjectPitchon) { //中标公示
			//解锁非候选人的从业人员
			unlockJobholders(getProject(component.getProjectId()), true, false);
		}
		else if(component instanceof BiddingProjectNotice) { //中标通知
			//解锁非中标人的从业人员
			unlockJobholders(getProject(component.getProjectId()), false, true);
		}
		else if(component instanceof BiddingProjectCompletion) { //竣工
			//解锁从业人员
			unlockJobholders(getProject(component.getProjectId()), false, false);
		}
		pageService.rebuildStaticPageForModifiedObject(component, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重新生成静态页面
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#uploadBiddingDocument(com.yuanluesoft.bidding.project.pojo.BiddingProject, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void uploadBiddingDocument(BiddingProject project, SessionInfo sessionInfo) throws ServiceException {
		boolean isNew = project.getDocuments()==null || project.getDocuments().isEmpty();
		BiddingProjectDocument biddingDocument = isNew ? null : (BiddingProjectDocument)project.getDocuments().iterator().next();
		
		//校验上传的招标文件
		String path = getBiddingDocumentPath(project.getId());
		if(path==null) { //没有上传招标文件
			if(biddingDocument!=null) {
				removeProjectComponent(biddingDocument);
			}
			return;
		}

		//获取原来的上传记录
		if(isNew) {
			biddingDocument = new BiddingProjectDocument();
			biddingDocument.setId(UUIDLongGenerator.generateId()); //ID
			biddingDocument.setProjectId(project.getId()); //项目ID
		}
		biddingDocument.setProjectName(project.getProjectName()); //项目名称
		biddingDocument.setUploadPersonId(sessionInfo.getUserId()); //上传用户ID
		biddingDocument.setUploadPersonName(sessionInfo.getUserName()); //上传用户
		biddingDocument.setUploadTime(DateTimeUtils.now());
		if(isNew) {
			getDatabaseService().saveRecord(biddingDocument);
			HashSet documents = new HashSet();
			documents.add(biddingDocument);
			project.setDocuments(documents);
		}
		else {
			getDatabaseService().updateRecord(biddingDocument);
		}
		
		if(jinrunIp!=null) {
			//检查文件
			synchronized(this) {
				if(Logger.isDebugEnabled()) {
					Logger.debug("JinRun: validate gef file " + path + "...");
				}
				if(!JinRunFileFilter.VerifyGEF(path)) {
					if(Logger.isDebugEnabled()) {
						Logger.debug("JinRun: validate gef file " + path + ", failed.");
					}
					throw new ServiceException("招标文件不合格");
				}
				if(Logger.isDebugEnabled()) {
					Logger.debug("JinRun: validate gef file " + path + ", pass.");
				}
			}
			//导出招标公告
			exportTenderFromBiddingDocument(project, path);

			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRun: bidding document saved.");
			}
		}
	}
	
	/**
	 * 导出招标公告
	 * @param project
	 * @throws ServiceException
	 */
	private synchronized void exportTenderFromBiddingDocument(BiddingProject project, String gefPath) throws ServiceException {
		String temporaryDirectory = temporaryFileManageService.createTemporaryDirectory(null);
		String xmlFilePath = temporaryDirectory.replace('/', '\\') + UUIDLongGenerator.generateId() + ".xml";
		if(xmlFilePath.charAt(0)=='\\') {
			xmlFilePath = xmlFilePath.substring(1);
		}
		try {
			synchronized(this) {
				for(int i=0; i<10; i++) {
					if(!JinRunFileFilter.Extract(0, gefPath, xmlFilePath)) {
						if(i>6) {
							throw new ServiceException("JinRunFileFilter: extract error.");
						}
						Thread.sleep(500);
					}
				}
			}
			//解析xml文件,获取需要的信息
			SAXReader reader = new SAXReader();
			Element rootElement = reader.read(new File(xmlFilePath)).getRootElement();
			//Element rootElement = reader.read(new File("C:\\Documents and Settings\\linchuan.KDSOFT-HP\\桌面\\新建 文本文档 (2).txt")).getRootElement();
			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRunFileFilter: extract bidding document, content is " + rootElement.asXML());
			}
			//获取当前项目的招标公告
			boolean isNew = false;
			BiddingProjectTender tender = null;
			if(project.getTenders()==null || project.getTenders().isEmpty()) {
				isNew = true;
			}
			else {
				tender = (BiddingProjectTender)project.getTenders().iterator().next();
			}
			if(isNew) { 
				tender = new BiddingProjectTender();
				tender.setId(UUIDLongGenerator.generateId());
				tender.setProjectId(project.getId());
			}
			//1. 招标条件 
			Element element = rootElement.element("ZhaoBiao_Tj");
			tender.setProjectName(element.elementText("PRJ_Name")); //项目名称
			tender.setTenderProjectName(tender.getProjectName());
			tender.setApprovalUnit(element.elementText("PRJ_BAJG")); //项目审批机关 
			tender.setProjectSn(element.elementText("PRJ_Code")); //批名称及编号
			tender.setOwner(element.elementText("PRJ_Host")); //项目业主
			tender.setInvestSource(element.elementText("PrJ_JSZJ")); //资金来源
			tender.setTenderee(element.elementText("PrJ_ZhaoBiaoRen")); //招标人
			tender.setAgent(element.elementText("PrJ_ZhaoBiaoRenProxy")); //招标代理
			tender.setBiddingMode(element.elementText("PrJ_ZhaoBiaoFangSi")); //招标方式
			//2.项目概况和招标范围
			element = rootElement.element("Prj_GK_ZhaoBiao_FW");
			tender.setProjectAddress(element.elementText("BuildPostion")); //建设地点
			tender.setProjectScale(element.elementText("PrjGM")); //建设规模
			tender.setBiddingContent(element.elementText("ZhaoBiaoContent")); //招标范围和内容
			tender.setTimeLimit(element.elementText("GQ")); //总工期,日历天
			tender.setKeysTimeLimit(element.elementText("GJGQ")); //关键节点的工期要求
			tender.setQuality(element.elementText("Prj_ZL")); //工程质量要求
			tender.setConsultationUnit(element.elementText("ZXDW")); //咨询单位
			tender.setDesignUnit(element.elementText("SJDW")); //设计单位
			tender.setFillinUnit(element.elementText("DJDW")); //代建单位
			tender.setSupervisorUnit(element.elementText("JLDW")); //监理单位
			//3. 投标人资格要求及审查办法
			element = rootElement.element("TBRen_YQYSC");
			tender.setBidderLevel(element.elementText("ZZLB")); //资质类别及等级
			tender.setManagerLevel(element.elementText("ZY")); //项目经理等级
			tender.setManagerSubject(element.elementText("CYZS")); //项目经理专业
			tender.setUnionBid(element.elementText("ISJS")); //接受/不接受联合体投标
			tender.setMajorBidder(element.elementText("ZBF")); //联合体投标主办方
			tender.setManagerAchievement(element.elementText("LSGCYJYQ")); //项目经理"类似工程业绩"要求
			tender.setSimilarityProject(element.elementText("FiveYearsYS")); //类似工程业绩
			tender.setApprovalMode(element.elementText("ZGSCFS")); //资格审查采用的方式
			tender.setRemark31(element.elementText("Remark31")); //备注31
			tender.setRemark32(element.elementText("Remark32")); //备注32
			tender.setRemark1(element.elementText("Remark37")); //备注1
			tender.setRemark2(element.elementText("Remark38")); //备注2
			tender.setRemark3(element.elementText("Remark39")); //备注3
			tender.setRemark4(element.elementText("Remark310")); //备注4
			tender.setRemark311(element.elementText("Remark311")); //备注3.11
			tender.setRemark312(element.elementText("Remark312")); //备注3.12
			tender.setInvitation(element.elementText("Prologue")); //邀请函,邀请招标时有效
			//4. 招标文件的获取 
			element = rootElement.element("ZB_File_Get");
			tender.setBuyDocumentAddress(element.elementText("XXDZ")); //购买招标文件地址
			tender.setBuyDocumentBegin(DateTimeUtils.parseTimestamp(element.elementText("ZhaoBiaoBeginDate") + " " + element.elementText("ZhaoBiaoSWBeginTime").replaceAll("上午 ", ""), "yyyy-M-d H:m:s")); //购买招标文件开始时间
			Timestamp buyDocumentEnd = DateTimeUtils.parseTimestamp(element.elementText("ZhaoBiaoEndDate") + " " + element.elementText("ZhaoBiaoXWEndTime").replaceAll("下午 ", ""), "yyyy-M-d H:m:s");
			tender.setBuyDocumentEnd(DateTimeUtils.add(buyDocumentEnd, Calendar.HOUR_OF_DAY, 12)); //购买招标文件结束时间
			String value = element.elementText("ZhaoBiaoWJJG");
			tender.setDocumentPrice(value==null || value.equals("") ? 0 : Integer.parseInt(value.replaceAll(",", "").replaceAll("元", ""))); //招标文件每份售价
			value = element.elementText("ZhaoBiaoTZJG");
			tender.setDrawingPrice(value==null || value.equals("") ? 0 : Integer.parseInt(value.replaceAll(",", "").replaceAll("元", ""))); //招标图纸每份售价
			tender.setRemark43(element.elementText("Remark43")); //备注43
			//5.评标办法
			element = rootElement.element("PBBF");
			tender.setEvaluateMethod(element.elementText("PBBF")); //采用的评标办法,综合评标
			//6.投标保证金的提交 
			element = rootElement.element("BZJTJ");
			tender.setPledgeTime(DateTimeUtils.parseTimestamp(element.elementText("BZJTJDate").replaceAll("上午 ", ""), "yyyy-M-d H:m:s")); //保证金提交的时间
			tender.setPledgeMode(element.elementText("BZJTJFS")); //保证金提交的方式
			value = element.elementText("BZJTJJE");
			tender.setPledgeMoneyText(value); //保证金提交的金额
			tender.setRemark62(element.elementText("Remark62")); //备注6.2
			//7.投标文件的递交 
			element = rootElement.element("TB_FileAccept");
			value = element.elementText("TBWJDJDate");
			tender.setSubmitTime(DateTimeUtils.parseTimestamp(value.replaceAll("上午 ", "").replaceAll("下午 ", ""), "yyyy-M-d H:m:s")); //投标文件的递交截止时间
			if(value.indexOf("下午")!=-1) {
				tender.setSubmitTime(DateTimeUtils.add(tender.getSubmitTime(), Calendar.HOUR_OF_DAY, 12)); //购买招标文件结束时间
			}
			tender.setSubmitAddress(element.elementText("TBWJTJDD")); //投标文件的递交地点,福州市建设工程交易管理中心
			tender.setRemark71(element.elementText("Remark71")); //备注7.1
			//8.发布公告的媒介
			element = rootElement.element("PublishNotice");
			tender.setMedia(element.elementText("PublishNotice")); //发布公告的媒体名称
			//9. 联系方式 
			element = rootElement.element("Contact_FS");
			tender.setTendereeAddress(element.elementText("ZhaoBiaoRen_Address")); //招标人地址
			tender.setTendereePostalCode(element.elementText("ZhaoBiaoRen_YB")); //招标人邮编
			tender.setTendereeTel(element.elementText("ZhaoBiaoRen_Phone")); //招标人电话
			tender.setTendereeFax(element.elementText("ZhaoBiaoRen_CZ")); //招标人传真
			tender.setTendereeLinkman(element.elementText("ZhaoBiaoRen_ContactRen")); //招标人联系人
			tender.setAgentAddress(element.elementText("DLJG_Address")); //招标代理机构地址
			tender.setAgentPostalCode(element.elementText("DLJG_YB")); //招标代理机构邮编
			tender.setAgentTel(element.elementText("DLJG_Phone")); //招标代理机构电话
			tender.setAgentFax(element.elementText("DLJG_CZ")); //招标代理机构传真
			tender.setAgentLinkman(element.elementText("DLJG_ContactRen")); //招标代理机构联系人
			tender.setBank(element.elementText("Khyh")); //开户银行
			tender.setAccountName(element.elementText("KHMC")); //帐户名称
			tender.setAccounts(element.elementText("YHZH")); //帐号
			tender.setTradingName(element.elementText("JYZXMC")); //交易中心名称
			tender.setTradingAddress(element.elementText("BZJYH_Address")); //地址
			
			if(isNew) {
				getDatabaseService().saveRecord(tender);
				HashSet tenders = new HashSet();
				tenders.add(tender);
				project.setTenders(tenders);
			}
			else {
				getDatabaseService().updateRecord(tender);
			}
			
			//获取当前项目的招标时间安排
			isNew = false;
			BiddingProjectPlan plan = null;
			if(project.getPlans()==null || project.getPlans().isEmpty()) {
				isNew = true;
			}
			else {
				plan = (BiddingProjectPlan)project.getPlans().iterator().next();
			}
			if(isNew) { 
				plan = new BiddingProjectPlan();
				plan.setId(UUIDLongGenerator.generateId());
				plan.setProjectId(project.getId());
			}
			plan.setProjectName(tender.getProjectName()); //项目名称
			plan.setBuyDocumentAddress(tender.getBuyDocumentAddress()); //购买标书地点
			plan.setBuyDocumentBegin(tender.getBuyDocumentBegin()); //购买标书开始时间
			plan.setBuyDocumentEnd(tender.getBuyDocumentEnd()); //购买标书截止时间
			plan.setSubmitAddress(tender.getSubmitAddress()); //提交地点
			plan.setSubmitTime(tender.getSubmitTime()); //提交截止时间
			if(isNew) {
				getDatabaseService().saveRecord(plan);
				HashSet plans = new HashSet();
				plans.add(plan);
				project.setPlans(plans);
			}
			else {
				getDatabaseService().updateRecord(plan);
			}
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException("不能读取招标公告,请确认格式是否正确");
		}
		finally {
			FileUtils.deleteDirectory(temporaryDirectory);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#sendBiddingDocumentToEvaluatingSystem(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public void sendBiddingDocumentToEvaluateSystem(BiddingProject project) throws ServiceException {
		if(jinrunIp==null) {
			return;
		}
		String path = getBiddingDocumentPath(project.getId());
		if(path==null) {
			return;
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("JinRun SendBiddingDocument:" + jinrunBiddingDocumentsPath + project.getProjectNumber() + path.substring(path.lastIndexOf('\\')));
		}
		synchronized(this) {
			if(!JinRunFileFilter.TranFile(path, jinrunBiddingDocumentsPath.replaceAll("\\x7bprojectNumber\\x7d", project.getProjectNumber()), jinrunIp)) {
				throw new ServiceException();
			}
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("JinRun SendBiddingDocument:" + jinrunBiddingDocumentsPath + project.getProjectNumber() + path.substring(path.lastIndexOf('\\')) + " completed");
		}
	}

	/**
	 * 获取招标文件
	 * @param projectId
	 * @return
	 */
	private String getBiddingDocumentPath(long projectId) {
		List attachments = null;
		try {
			attachments = attachmentService.list("bidding/project", "biddingDocuments", projectId, false, 1, null);
		}
		catch (ServiceException e) {
			
		}
		if(attachments==null || attachments.isEmpty()) {
			return null; //没有招标文件
		}
		Attachment biddingDocuments = (Attachment)attachments.get(0);
		return new File(biddingDocuments.getFilePath()).getAbsolutePath();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#publicBidopenings()
	 */
	public void publicBidopenings() throws ServiceException {
		//获取开标时间已经过了,还没有发布开标公示的项目
		String hql = "select BiddingProject" +
					 " from BiddingProject BiddingProject,BiddingProjectPlan BiddingProjectPlan" +
					 " where BiddingProjectPlan.bidopeningTime<=TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")" +
					 " and BiddingProjectPlan.projectId=BiddingProject.id" +
					 " and (select max(BiddingProjectBidopening.id) from BiddingProjectBidopening BiddingProjectBidopening where BiddingProjectBidopening.projectId=BiddingProject.id) is null";
		List projects = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("prophases,documents,agentDraws,biddingAgents,askQuestions,kcs,tenders,plans,supplements,answers,preapprovals,bidopenings,grades,pitchons,pitchouts,notices,invites,useFees,pays,supplements,bidopeningRoomSchedules,evaluatingRoomSchedules,materials,archives,declares,licences", ","));
		if(projects==null || projects.isEmpty()) {
			return;
		}
		for(Iterator iterator = projects.iterator(); iterator.hasNext();) {
			BiddingProject project = (BiddingProject)iterator.next();
			try {
				BiddingProjectBidopening bidopening = synchBidopening(project);
				if(bidopening!=null) {
					publicProjectComponent(bidopening, null);
				}
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#synchBidopening(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public BiddingProjectBidopening synchBidopening(BiddingProject project) throws ServiceException {
		if(jinrunIp==null) {
			return null;
		}
		BiddingProjectBidopening bidopening = null;
		boolean isNew = false;
		if(project.getBidopenings()==null || project.getBidopenings().isEmpty()) {
			isNew = true;
		}
		else {
			bidopening = (BiddingProjectBidopening)project.getBidopenings().iterator().next();
			//检查是否已经同步过
			if(bidopening.getSynch()=='1' || (bidopening.getBody()!=null && !bidopening.getBody().equals(""))) {
				return bidopening;
			}
		}
		//获取开标xml
		String temporaryDirectory = temporaryFileManageService.createTemporaryDirectory(null);
		String xmlFilePath = temporaryDirectory.replace('/', '\\') + UUIDLongGenerator.generateId() + ".xml";
		if(xmlFilePath.startsWith("\\")) {
			xmlFilePath = xmlFilePath.substring(1);
		}
		try {
			String path = jinrunBidopeningPath.replaceAll("\\x7bprojectNumber\\x7d", project.getProjectNumber());
			int index = path.lastIndexOf('\\') + 1;
			String dir = path.substring(0, index);
			String fileName = path.substring(index);
			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRun SynchBidopening: " + path + "...");
			}
			String result;
			synchronized(this) {
				result = JinRunFileFilter.GetRemoveXmlFile(fileName, dir, jinrunIp, xmlFilePath);
			}
			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRun SynchBidopening: " + path + ", result is " + result + ".");
			}
			if(result==null || result.equals("")) {
				return bidopening;
			}
			//解析xml
			SAXReader reader = new SAXReader();
			Element rootElement = reader.read(new File(xmlFilePath)).getRootElement();
			if(isNew) { 
				bidopening = new BiddingProjectBidopening();
				bidopening.setId(UUIDLongGenerator.generateId());
				bidopening.setProjectId(project.getId());
			}
			Element element = rootElement.element("ProjectInfo");
			bidopening.setProjectName(element.elementText("ProjectName")); //项目名称
			BiddingProjectParameter parameter = biddingProjectParameterService.getParameter(project.getProjectCategory(), project.getProjectProcedure(), project.getCity());
			bidopening.setPublicLimit(parameter.getBidopeningDays()); //公示期限
			bidopening.setRoom(element.elementText("OpenRoom")); //开标室
			try {
				bidopening.setBeginTime(DateTimeUtils.parseTimestamp(element.elementText("OpenTime"), "yyyy-M-d H:m:s")); //开标时间
			}
			catch(Exception e) {
				
			}
			//bidopening.setEndTime(DateTimeUtils.parseTimestamp(element.elementText("CloseTime"), "yyyy-M-d H:m:s")); //结束时间
			//private String linkman; //联系人
			//private String description; //开标情况描述
			//private String recorder; //记录人
			//private String surverllant; //监标人
			//<ProjectCode>MJ2009246</ProjectCode> 
			bidopening.setRemark(element.elementText("Remark"));
			bidopening.setSynch('1');
			
			//获取投标单位评分
			Set grades = new LinkedHashSet();
			element = rootElement.element("BidderInfo");
			for(Iterator iterator = element.elementIterator(); iterator.hasNext();) {
				Element gradeElement = (Element)iterator.next();
				BiddingProjectGrade grade = new BiddingProjectGrade();
				grade.setId(UUIDLongGenerator.generateId()); //ID
				grade.setProjectId(project.getId()); //项目ID
				grade.setProjectName(bidopening.getProjectName()); //项目名称
				grade.setSerial(Integer.parseInt(gradeElement.elementText("Index"))); //序号
				grade.setEnterpriseName(gradeElement.elementText("CompanyName")); //单位名称
				//<CompanyCode>2</CompanyCode>
				//<LegalRep />
				grade.setSeal(gradeElement.elementText("SealState")); //密封情况
				grade.setPrice(Double.parseDouble(gradeElement.elementText("TotalQuote"))); //报价
				grade.setTimeLimit(Integer.parseInt(gradeElement.elementText("TotalTimeLimit"))); //工期
				grade.setQuality(gradeElement.elementText("QltyLevel")); //质量
				grade.setManager(gradeElement.elementText("ProjectMgr")); //项目经理
				grade.setPledge(gradeElement.elementText("Bail")); //保证金
				grade.setSign(gradeElement.elementText("BidderSign")); //签名
				getDatabaseService().saveRecord(grade);
				grades.add(grade);
			}
			if(isNew) {
				getDatabaseService().saveRecord(bidopening);
				HashSet bidopenings = new HashSet();
				bidopenings.add(bidopening);
				project.setBidopenings(bidopenings);
			}
			else {
				getDatabaseService().updateRecord(bidopening);
			}
			project.setGrades(grades);
			return bidopening;
		} 
		catch (Exception e) {
			Logger.exception(e);
			//删除投标单位评分
			getDatabaseService().deleteRecordsByHql("from BiddingProjectGrade BiddingProjectGrade where BiddingProjectGrade.projectId=" + project.getId());
		}
		finally {
			FileUtils.deleteDirectory(temporaryDirectory);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#synchPitchon(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public BiddingProjectPitchon synchPitchon(BiddingProject project) throws ServiceException {
		if(jinrunIp==null) {
			return null;
		}
		BiddingProjectPitchon pitchon = null;
		boolean isNew = false;
		if(project.getPitchons()==null || project.getPitchons().isEmpty()) {
			isNew = true;
		}
		else {
			pitchon = (BiddingProjectPitchon)project.getPitchons().iterator().next();
			//检查是否已经同步过
			if(pitchon.getSynch()=='1' || (pitchon.getPitchonEnterprise()!=null && !pitchon.getPitchonEnterprise().equals("")) || (pitchon.getBody()!=null && !pitchon.getBody().equals(""))) {
				return pitchon;
			}
		}
		//获取开标xml
		String temporaryDirectory = temporaryFileManageService.createTemporaryDirectory(null);
		String xmlFilePath = temporaryDirectory.replace('/', '\\') + UUIDLongGenerator.generateId() + ".xml";
		if(xmlFilePath.startsWith("\\")) {
			xmlFilePath = xmlFilePath.substring(1);
		}
		try {
			String path = jinrunPitchonPath.replaceAll("\\x7bprojectNumber\\x7d", project.getProjectNumber());
			int index = path.lastIndexOf('\\') + 1;
			String dir = path.substring(0, index);
			String fileName = path.substring(index);
			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRun SynchPitchon: " + path + "...");
			}
			String result;
			synchronized(this) {
				result = JinRunFileFilter.GetRemoveXmlFile(fileName, dir, jinrunIp, xmlFilePath);
			}
			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRun SynchPitchon: " + path + ", result is " + result + ".");
			}
			if(result==null || result.equals("")) {
				return pitchon;
			}
			//解析xml
			SAXReader reader = new SAXReader();
			Element rootElement = reader.read(new File(xmlFilePath)).getRootElement().element("PrjInfo");
			if(isNew) { 
				pitchon = new BiddingProjectPitchon();
				pitchon.setId(UUIDLongGenerator.generateId());
				pitchon.setProjectId(project.getId());
			}
			pitchon.setProjectName(project.getProjectName()); //项目名称
			pitchon.setProjectNumber(project.getProjectNumber()); //项目编号
			BiddingProjectParameter parameter = biddingProjectParameterService.getParameter(project.getProjectCategory(), project.getProjectProcedure(), project.getCity());
			pitchon.setPublicLimit(parameter.getPitchonDays()); //公示期限
			if(project.getPlans()!=null && !project.getPlans().isEmpty()) {
				BiddingProjectPlan plan = (BiddingProjectPlan)project.getPlans().iterator().next();
				pitchon.setBidopeningTime(plan.getBidopeningTime()); //开标时间
			}
			pitchon.setBiddingMode(project.getBiddingMode()); //招标方式
			pitchon.setPitchonEnterpriseId(0); //中标人ID
			pitchon.setPitchonEnterprise(rootElement.attributeValue("PrjBeChoose")); //中标人
			//从开标评分列表获取项目经理
			String hql = "from BiddingProjectGrade BiddingProjectGrade" +
						 " where BiddingProjectGrade.projectId=" + project.getId() +
						 " and BiddingProjectGrade.enterpriseName='" + JdbcUtils.resetQuot(pitchon.getPitchonEnterprise()) + "'";
			BiddingProjectGrade grade = (BiddingProjectGrade)getDatabaseService().findRecordByHql(hql);
			if(grade!=null) {
				pitchon.setManager(grade.getManager()); //项目经理
				pitchon.setPitchonMoney(grade.getPrice()); //中标金额
			}
			//评标专家
			//pitchon.setOwnerRater(""); //业主评委
			Element element = rootElement.element("PrjExpertInfo");
			String raters = null;
			for(Iterator iterator = element.elementIterator(); iterator.hasNext();) {
				Element raterElement = (Element)iterator.next();
				raters = (raters==null ? "" : raters + "，") + raterElement.elementText("ExpertName");
			}
			pitchon.setRaters(raters); //评标专家
			
			//获取招标公告
			if(project.getTenders()!=null && !project.getTenders().isEmpty()) {
				BiddingProjectTender tender = (BiddingProjectTender)project.getTenders().iterator().next();
				pitchon.setTenderee(tender.getTenderee()); //招标人
				pitchon.setTendereeAddress(tender.getTendereeAddress()); //招标人地址
				pitchon.setTendereePostalCode(tender.getTendereePostalCode()); //招标人邮政编码
				//pitchon.setTendereeMail(tender.gettendereeMail); //招标人电子邮件
				pitchon.setTendereeLinkman(tender.getTendereeLinkman()); //招标人联系人
				pitchon.setTendereeTel(tender.getTendereeTel()); //招标人联系电话
				pitchon.setTendereeFax(tender.getTendereeFax()); //招标人传真
				pitchon.setAgent(tender.getAgent()); //招标代理机构
				pitchon.setAgentAddress(tender.getAgentAddress()); //代理办公地址
				pitchon.setAgentPostalCode(tender.getAgentPostalCode()); //代理邮政编码
				pitchon.setAgentFax(tender.getAgentFax()); //代理传真
				pitchon.setAgentTel(tender.getAgentTel()); //代理电话
				pitchon.setAgentLinkman(tender.getAgentLinkman()); //代理联系人
			}
			//pitchon.setAgentMail(tender.getagentMail); //代理电子邮件
			//pitchon.setSupervision(supervision); //招投标监督机构
			//pitchon.setSupervisionAddress(supervisionAddress); //监督机构地址
			//pitchon.setSupervisionTel(supervisionTel); //监督电话
			pitchon.setSynch('1');
			
			//获取不合格投标人
			Set pitchouts = new LinkedHashSet();
			element = rootElement.element("BidCancelInfo");
			for(Iterator iterator = element.elementIterator(); iterator.hasNext();) {
				Element pitchoutElement = (Element)iterator.next();
				BiddingProjectPitchout pitchout = new BiddingProjectPitchout();
				pitchout.setId(UUIDLongGenerator.generateId()); //ID
				pitchout.setProjectId(project.getId()); //项目ID
				pitchout.setProjectName(project.getProjectName()); //项目名称
				pitchout.setEnterpriseName(pitchoutElement.elementText("BidName")); //投标人名称
				pitchout.setResult(pitchoutElement.elementText("CancelType")); //评审结果
				pitchout.setReason(pitchoutElement.elementText("CancelReason")); //原因
				pitchout.setBasis(pitchoutElement.elementText("CancelAccord")); //依据及理由
				//pitchout.setRemark(remark); //备注
				getDatabaseService().saveRecord(pitchout);
				pitchouts.add(pitchout);
			}
			if(isNew) {
				getDatabaseService().saveRecord(pitchon);
				HashSet pitchons = new HashSet();
				pitchons.add(pitchon);
				project.setPitchons(pitchons);
			}
			else {
				getDatabaseService().updateRecord(pitchon);
			}
			project.setPitchouts(pitchouts);
			return pitchon;
		} 
		catch (Exception e) {
			Logger.exception(e);
			//删除不合格投标人
			getDatabaseService().deleteRecordsByHql("from BiddingProjectPitchout BiddingProjectPitchout where BiddingProjectPitchout.projectId=" + project.getId());
		}
		finally {
			FileUtils.deleteDirectory(temporaryDirectory);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#generateNotice(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public BiddingProjectNotice generateNotice(BiddingProject project) throws ServiceException {
		BiddingProjectNotice notice = null;
		boolean isNew = false;
		if(project.getNotices()==null || project.getNotices().isEmpty()) {
			isNew = true;
		}
		else {
			notice = (BiddingProjectNotice)project.getNotices().iterator().next();
			//检查是否已经创建过
			if(notice.getPitchonEnterprise()!=null) {
				return notice;
			}
		}
		//获取中标结果公示
		if(project.getPitchons()==null || project.getPitchons().isEmpty()) {
			return notice;
		}
		BiddingProjectPitchon pitchon = (BiddingProjectPitchon)project.getPitchons().iterator().next();
		if(isNew) { 
			notice = new BiddingProjectNotice();
			notice.setId(UUIDLongGenerator.generateId());
			notice.setProjectId(project.getId());
		}
		notice.setProjectName(project.getProjectName());
		notice.setPitchonEnterpriseId(pitchon.getPitchonEnterpriseId()); //中标人ID
		notice.setPitchonEnterprise(pitchon.getPitchonEnterprise()); //中标人
		notice.setPitchonPrice(pitchon.getPitchonMoney()); //中标价
		//获取投标单位评分
		String hql = "from BiddingProjectGrade BiddingProjectGrade" +
		 			 " where BiddingProjectGrade.projectId=" + project.getId() +
		 			 " and BiddingProjectGrade.enterpriseName='" + JdbcUtils.resetQuot(pitchon.getPitchonEnterprise()) + "'";
		BiddingProjectGrade grade = (BiddingProjectGrade)getDatabaseService().findRecordByHql(hql);
		if(grade!=null) {
			notice.setTimeLimit(grade.getTimeLimit()); //总工期
			//projectForm.getNotice().setKeysTimeLimit(keysTimeLimit); //关键节点工期要求
			//projectForm.getNotice().setContractDays(contractDays); //签订合同时限,天
			//projectForm.getNotice().setContractAddress(contractAddress); //签订合同地点
			notice.setQuality(grade.getQuality()); //工程质量
			//projectForm.getNotice().setStage(pitchon.get); //标段
		}
		//获取报名时间
		hql = "from BiddingSignUp BiddingSignUp" +
			  " where BiddingSignUp.projectId=" + project.getId() +
			  " and BiddingSignUp.enterpriseName='" + JdbcUtils.resetQuot(pitchon.getPitchonEnterprise()) + "'";
		BiddingSignUp signUp = (BiddingSignUp)getDatabaseService().findRecordByHql(hql);
		if(signUp!=null && signUp.getPaymentTime()!=null) {
			notice.setBiddingDate(new Date(signUp.getPaymentTime().getTime())); //投标日期
		}
		notice.setManager(pitchon.getManager()); //项目经理
		notice.setTenderee(pitchon.getTenderee()); //招标人
		//notice.setTendereeRepresentative(tendereeRepresentative); //法定代表人
		notice.setNoticeDate(DateTimeUtils.date()); //通知时间
		if(isNew) {
			getDatabaseService().saveRecord(notice);
			HashSet notices = new HashSet();
			notices.add(notice);
			project.setNotices(notices);
		}
		else {
			getDatabaseService().updateRecord(notice);
		}
		return notice;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#exportSignUps()
	 */
	public void exportSignUps() throws ServiceException {
		if(jinrunIp==null) {
			return;
		}
		//获取报名截止时间已经过了且尚未导出报名列表的项目
		String hql = "from BiddingProjectPlan BiddingProjectPlan" +
					 " where BiddingProjectPlan.bidopeningTime<=TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.add(DateTimeUtils.now(), Calendar.MINUTE, 30), null) + ")" +
					 " and BiddingProjectPlan.signUpExported!='1'";
		List plans = getDatabaseService().findRecordsByHql(hql);
		if(plans==null || plans.isEmpty()) {
			return;
		}
		for(Iterator iterator = plans.iterator(); iterator.hasNext();) {
			BiddingProjectPlan plan = (BiddingProjectPlan)iterator.next();
			try {
				sendSignUpsXml(plan.getProjectId());
				//更新计划
				plan.setSignUpExported('1');
				getDatabaseService().updateRecord(plan);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 发送报名清单
	 * @param projectId
	 * @throws ServiceException
	 */
	private synchronized void sendSignUpsXml(long projectId) throws ServiceException {
		//获取项目编号
		String hql = "select BiddingProject.projectNumber" +
					 " from BiddingProject BiddingProject" +
					 " where BiddingProject.id=" + projectId;
		String projectNumber = (String)getDatabaseService().findRecordByHql(hql);
		if(projectNumber==null) {
			//throw new ServiceException("project number is null");
			return;
		}
		//获取报名列表
		hql = "from BiddingSignUp BiddingSignUp" +
			  " where BiddingSignUp.projectId=" + projectId +
			  " and not BiddingSignUp.paymentTime is null" +
			  " order by BiddingSignUp.signUpNo";
		List signUps = getDatabaseService().findRecordsByHql(hql);
		//生成报名清单xml
		Document document = DocumentHelper.createDocument();
		Element projectElement =  document.addElement("project");
		projectElement.addAttribute("code", projectNumber);
		DesSecurity des = new DesSecurity("fjfzztbw", "fjfzztbw");
		if(signUps!=null) {
			for(Iterator iterator = signUps.iterator(); iterator.hasNext();) {
				BiddingSignUp signUp = (BiddingSignUp)iterator.next();
				Element signUpElement = projectElement.addElement("signUp");   
				try {
					signUpElement.addElement("signUpNo").setText(des.encrypt(signUp.getSignUpNo()));
				} 
				catch (Exception e) {
					Logger.exception(e);
					throw new ServiceException(e.getMessage());
				}
				//报名号
				signUpElement.addElement("signUpTime").setText(DateTimeUtils.formatTimestamp(signUp.getPaymentTime(), null)); //报名时间
			}
		}
		XMLWriter writer = null;
		String path = jinrunSignUpsPath.replaceAll("\\x7bprojectNumber\\x7d", projectNumber);
		int index = path.lastIndexOf('\\') + 1;
		String destDir = path.substring(0, index);
		String temporaryDirectory = temporaryFileManageService.createTemporaryDirectory(null);
		String xmlFilePath = temporaryDirectory.replace('/', '\\') + path.substring(index);
		if(xmlFilePath.charAt(0)=='\\') {
			xmlFilePath = xmlFilePath.substring(1);
		}
		try {
			//将document中的内容写入文件中
			OutputFormat format = OutputFormat.createPrettyPrint();      
			format.setEncoding("UTF-8");      
			writer = new XMLWriter(new FileOutputStream(new File(xmlFilePath)), format);   
			writer.write(document);
			writer.close();
			//调用金润传输接口把文件传输到金润招标服务器
			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRun SendSignUpsXml: file is " + xmlFilePath + " dest dir is " + destDir + ", sending...");
			}
			synchronized(this) {
				if(!JinRunFileFilter.TranFile(xmlFilePath, destDir, jinrunIp)) {
					throw new ServiceException();
				}
			}
			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRun SendSignUpsXml: file is " + xmlFilePath + " dest dir is " + destDir + ", send completed.");
			}
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		finally {
			try {
				writer.close();
			}
			catch(Exception e) {
				
			}
			FileUtils.deleteDirectory(temporaryDirectory);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#saveOrUpdateOwner(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void saveOrUpdateOwner(String owner, String ownerType, String ownerRepresentative, String ownerLinkman, String ownerLinkmanIdCard, String ownerTel, String ownerFax, String ownerMail) throws ServiceException {
		if(owner==null || owner.equals("")) {
			return;
		}
		//查找原来的记录
		String hql = "from BiddingProjectOwner BiddingProjectOwner where BiddingProjectOwner.owner='" + JdbcUtils.resetQuot(owner) + "'";
		BiddingProjectOwner projectOwner = (BiddingProjectOwner)getDatabaseService().findRecordByHql(hql);
		boolean isNew = projectOwner==null;
		if(isNew) {
			projectOwner = new BiddingProjectOwner();
			projectOwner.setId(UUIDLongGenerator.generateId()); //ID
			projectOwner.setOwner(owner);
		}
		projectOwner.setOwnerType(ownerType); //建设单位性质,全民
		projectOwner.setOwnerRepresentative(ownerRepresentative); //建设单位法人代表
		projectOwner.setOwnerLinkman(ownerLinkman); //建设单位联系人
		projectOwner.setOwnerLinkmanIdCard(ownerLinkmanIdCard); //建设单位联系人身份证
		projectOwner.setOwnerTel(ownerTel); //建设单位联系电话
		projectOwner.setOwnerFax(ownerFax); //建设单位传真
		projectOwner.setOwnerMail(ownerMail); //建设单位电子邮件
		if(isNew) {
			getDatabaseService().saveRecord(projectOwner);
		}
		else {
			getDatabaseService().updateRecord(projectOwner);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#isBidopeningProjectOverflow(com.yuanluesoft.bidding.project.pojo.BiddingProjectPlan, java.lang.String)
	 */
	public boolean isBidopeningProjectOverflow(BiddingProjectPlan projectPlan, String projectCity) throws ServiceException {
		Date beginTime = new Date(projectPlan.getBidopeningTime().getTime());
		Date endTime = DateTimeUtils.add(beginTime, Calendar.DAY_OF_MONTH, 1);
		//获取同一天内开标的项目数
		String hql = "select count(BiddingProjectPlan.id)" +
					 " from BiddingProjectPlan BiddingProjectPlan" + 
					 " where BiddingProjectPlan.bidopeningTime>=DATE(" + DateTimeUtils.formatDate(beginTime, null) + ")" +
					 " and BiddingProjectPlan.bidopeningTime<DATE(" + DateTimeUtils.formatDate(endTime, null) + ")" +
					 " and BiddingProjectPlan.id!=" + projectPlan.getId();
		Number count = (Number)getDatabaseService().findRecordByHql(hql);
		if(count==null || count.intValue()==0) { //没有项目
			return false;
		}
		//获取开标室数量
		List rooms = biddingRoomService.listRooms("开标", projectCity);
		return (rooms!=null && count.intValue()>=rooms.size() * 2); //一天按两个项目计算
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#sendBidToBiddingSystem(com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp)
	 */
	public void sendBidToBiddingSystem(BiddingSignUp signUp) throws ServiceException {
		if(jinrunIp==null) {
			return;
		}
		//获取投标文件目录
		List attachments = attachmentService.list("bidding/project/signup", "bid", signUp.getId(), false, 0, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("没有招标文件");
		}
		//获取fzt文件
		String filePath = new File(((Attachment)attachments.get(0)).getFilePath()).getAbsolutePath();
		//校验fzt文件
		FZTDecodeJava fztDecode = new FZTDecodeJava();
		if(Logger.isDebugEnabled()) {
			Logger.debug("FZT validate: " + filePath);
		}
  		if(fztDecode.Check(filePath, "fzt")==0) {
  			throw new ServiceException("投标文件格式有误");
  		}
  		if(Logger.isDebugEnabled()) {
			Logger.debug("FZT validate: " + filePath + ", pass.");
		}
		
		BiddingProject project = getProject(signUp.getProjectId());
		if(Logger.isDebugEnabled()) {
			Logger.debug("JinRun SendBid:" + filePath);
		}
		String path = jinrunBidPath.replaceAll("\\x7bprojectNumber\\x7d", project.getProjectNumber()).replaceAll("\\x7bsignUpNo\\x7d", signUp.getSignUpNo());
		int index = path.lastIndexOf('\\') + 1;
		String dir = path.substring(0, index);
		String fileName = path.substring(index);
		if(!fileName.equals("")) { //文件名不为空
			//把当前的gef重命名
			String oldFilePath = filePath.replace('/', '\\');
			int indexFile = oldFilePath.lastIndexOf('\\') + 1;
			filePath = oldFilePath.substring(0, indexFile) + fileName;
			FileUtils.renameFile(oldFilePath, filePath, true, true);
		}
		synchronized(this) {
			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRun: send bid, path is " + path + "...");
			}
			if(!JinRunFileFilter.TranFile(filePath, dir, jinrunIp)) {
				throw new ServiceException("传送到开标服务器失败");
			}
			if(Logger.isDebugEnabled()) {
				Logger.debug("JinRun: send bid, path is " + path + ".");
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.BiddingProjectService#isRealNameSignUp(long)
	 */
	public boolean isRealNameSignUp(long projectId) throws ServiceException {
		String hql = "select BiddingProject.isRealNameSignUp" +
					 " from BiddingProject BiddingProject" +
					 " where BiddingProject.id=" + projectId;
		return "是".equals(getDatabaseService().findRecordByHql(hql));
	}

	/**
	 * @return the jinrunBiddingDocumentsPath
	 */
	public String getJinrunBiddingDocumentsPath() {
		return jinrunBiddingDocumentsPath;
	}

	/**
	 * @param jinrunBiddingDocumentsPath the jinrunBiddingDocumentsPath to set
	 */
	public void setJinrunBiddingDocumentsPath(String jinrunBiddingDocumentsPath) {
		this.jinrunBiddingDocumentsPath = jinrunBiddingDocumentsPath;
	}

	/**
	 * @return the jinrunBidopeningPath
	 */
	public String getJinrunBidopeningPath() {
		return jinrunBidopeningPath;
	}

	/**
	 * @param jinrunBidopeningPath the jinrunBidopeningPath to set
	 */
	public void setJinrunBidopeningPath(String jinrunBidopeningPath) {
		this.jinrunBidopeningPath = jinrunBidopeningPath;
	}

	/**
	 * @return the jinrunIp
	 */
	public String getJinrunIp() {
		return jinrunIp;
	}

	/**
	 * @param jinrunIp the jinrunIp to set
	 */
	public void setJinrunIp(String jinrunIp) {
		this.jinrunIp = jinrunIp;
	}

	/**
	 * @return the jinrunPitchonPath
	 */
	public String getJinrunPitchonPath() {
		return jinrunPitchonPath;
	}

	/**
	 * @param jinrunPitchonPath the jinrunPitchonPath to set
	 */
	public void setJinrunPitchonPath(String jinrunPitchonPath) {
		this.jinrunPitchonPath = jinrunPitchonPath;
	}

	/**
	 * @return the jinrunSignUpsPath
	 */
	public String getJinrunSignUpsPath() {
		return jinrunSignUpsPath;
	}

	/**
	 * @param jinrunSignUpsPath the jinrunSignUpsPath to set
	 */
	public void setJinrunSignUpsPath(String jinrunSignUpsPath) {
		this.jinrunSignUpsPath = jinrunSignUpsPath;
	}
	
	private class DesSecurity {
		private Cipher enCipher;
		private Cipher deCipher;
		
		/**
		 * @param key 加密密钥
		 * @param iv 初始化向量
		 * @throws Exception
		 */
		public DesSecurity(String key, String iv) {
			if (key == null) {
				throw new NullPointerException("Parameter is null!");
			}
			try {
				initCipher(key.getBytes(), iv.getBytes());
			}
			catch(Exception e) {
				
			}
		}
		
		/**
		 * 初始化
		 * @param secKey
		 * @param secIv
		 * @throws Exception
		 */
		private void initCipher(byte[] secKey, byte[] secIv) throws Exception {
			//创建MD5散列对象
			MessageDigest md = MessageDigest.getInstance("MD5");
			//散列密钥
			md.update(secKey);
			//获得DES密钥
			DESKeySpec dks = new DESKeySpec(md.digest());
			//获得DES加密密钥工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
			//生成加密密钥
			SecretKey key = keyFactory.generateSecret(dks);
			//创建初始化向量对象
			IvParameterSpec iv = new IvParameterSpec(secIv);
			AlgorithmParameterSpec paramSpec = iv;
			//为加密算法指定填充方式，创建加密会话对象
			enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			//为加密算法指定填充方式，创建解密会话对象
			deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			//初始化加解密会话对象
			enCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec); 
			deCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		}

		/**
		 * 数据加密
		 * @param data
		 * @return
		 * @throws Exception
		 */
		public String encrypt(String data) throws Exception {
			byte[] bytes = enCipher.doFinal(data.getBytes());
			String ret = "";
			for(int i=0; i<bytes.length; i++) {
				ret += toHexString(bytes[i]);
			}
			return ret;
		}
		
		 /**
	     * 转换字节为16进制字符
	     * @param b
	     * @return
	     */
	    private String toHexString(byte b) {
	        int intValue = (b>=0 ? b : 256+b);
	        String hex = Integer.toHexString(intValue).toUpperCase();
	        return hex.length()<2 ? "0" + hex : hex;
	    }
	}
	
	/**
	 * 同步监察数据
	 * @param record
	 * @param action
	 * @throws ServiceException
	 */
	private void superviseSynch(final Record record, final String action) throws ServiceException {
		if(superviseDAO==null) {
			return;
		}
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					String className = record.getClass().getName();
					className = className.substring(className.lastIndexOf('.') + 1);
					Record synchRecord = (Record)Class.forName("com.yuanluesoft.bidding.project.pojo.supervise.Supervise" + className).newInstance();
					PropertyUtils.copyProperties(synchRecord, record);
					if("save".equals(action)) { //新建
						superviseDAO.saveRecord(synchRecord);
						if(record instanceof BiddingProjectBidopening) { //开标
							//保存开标数据
							List grades = getDatabaseService().findRecordsByHql("from BiddingProjectGrade BiddingProjectGrade where BiddingProjectGrade.projectId=" + ((BiddingProjectBidopening)record).getProjectId());
							if(grades!=null && !grades.isEmpty()) {
								for(Iterator iterator = grades.iterator(); iterator.hasNext();) {
									BiddingProjectGrade grade = (BiddingProjectGrade)iterator.next();
									SuperviseBiddingProjectGrade superviseBiddingProjectGrade = new SuperviseBiddingProjectGrade();
									PropertyUtils.copyProperties(superviseBiddingProjectGrade, grade);
									superviseDAO.saveRecord(superviseBiddingProjectGrade);
								}
							}
						}
						else if(record instanceof BiddingProjectPitchon) { //中标公示
							//保存开标数据
							List pitchouts = getDatabaseService().findRecordsByHql("from BiddingProjectPitchout BiddingProjectPitchout where BiddingProjectPitchout.projectId=" + ((BiddingProjectPitchon)record).getProjectId());
							if(pitchouts!=null && !pitchouts.isEmpty()) {
								for(Iterator iterator = pitchouts.iterator(); iterator.hasNext();) {
									BiddingProjectPitchout pitchout = (BiddingProjectPitchout)iterator.next();
									SuperviseBiddingProjectPitchout superviseBiddingProjectPitchout = new SuperviseBiddingProjectPitchout();
									PropertyUtils.copyProperties(superviseBiddingProjectPitchout, pitchout);
									superviseDAO.saveRecord(superviseBiddingProjectPitchout);
								}
							}
						}
					}
					else if("update".equals(action)) { //更新
						superviseDAO.updateRecord(synchRecord);
					}
					else if("delete".equals(action)) { //删除
						superviseDAO.deleteRecord(synchRecord);
					}
				}
				catch (Exception e) {
					Logger.exception(e);
				}
				timer.cancel();
			}
		}, 10);
	}

	/**
	 * @return the biddingRoomService
	 */
	public BiddingRoomService getBiddingRoomService() {
		return biddingRoomService;
	}

	/**
	 * @param biddingRoomService the biddingRoomService to set
	 */
	public void setBiddingRoomService(BiddingRoomService biddingRoomService) {
		this.biddingRoomService = biddingRoomService;
	}

	/**
	 * @return the superviseDAO
	 */
	public DatabaseService getSuperviseDAO() {
		return superviseDAO;
	}

	/**
	 * @param superviseDAO the superviseDAO to set
	 */
	public void setSuperviseDAO(DatabaseService superviseDAO) {
		this.superviseDAO = superviseDAO;
	}

	/**
	 * @return the jinrunBidPath
	 */
	public String getJinrunBidPath() {
		return jinrunBidPath;
	}

	/**
	 * @param jinrunBidPath the jinrunBidPath to set
	 */
	public void setJinrunBidPath(String jinrunBidPath) {
		this.jinrunBidPath = jinrunBidPath;
	}

	/**
	 * @return the temporaryFileManageService
	 */
	public TemporaryFileManageService getTemporaryFileManageService() {
		return temporaryFileManageService;
	}

	/**
	 * @param temporaryFileManageService the temporaryFileManageService to set
	 */
	public void setTemporaryFileManageService(
			TemporaryFileManageService temporaryFileManageService) {
		this.temporaryFileManageService = temporaryFileManageService;
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

	/**
	 * @return the biddingProjectParameterService
	 */
	public BiddingProjectParameterService getBiddingProjectParameterService() {
		return biddingProjectParameterService;
	}

	/**
	 * @param biddingProjectParameterService the biddingProjectParameterService to set
	 */
	public void setBiddingProjectParameterService(
			BiddingProjectParameterService biddingProjectParameterService) {
		this.biddingProjectParameterService = biddingProjectParameterService;
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