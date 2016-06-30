package com.yuanluesoft.bidding.project.signup.service.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingBidEnterprise;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingJobholder;
import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectCity;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectJobholder;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectParameter;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectTender;
import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.model.SignUpTotal;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUpJobholder;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.payment.model.Transaction;
import com.yuanluesoft.jeaf.payment.model.Transfer;
import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.payment.service.PaymentService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 投标服务
 * @author linchuan
 *
 */
public class BiddingServiceImpl extends BusinessServiceImpl implements BiddingService {
	private CryptService cryptService; //加密服务
	private BiddingProjectParameterService biddingProjectParameterService;
	private PaymentService paymentService; //支付服务
	private BiddingProjectService biddingProjectService; //招标项目服务
	private EnterpriseService enterpriseService; //企业服务
	private AttachmentService attachmentService; //附件服务
	private TemporaryFileManageService temporaryFileManageService; //临时文件服务
	private FileDownloadService fileDownloadService; //文件下载服务
	private AccessControlService accessControlService; //访问控制服务
	private RecordControlService recordControlService; //记录控制服务
	
	private int bidUploadPaddingMinutes = 0; //投标书上传的缓冲时间,以分钟为单位
	private int signUpNoCookieHours = 72; //报名号存放在COOKIE中的小时数,默认72小时
	private String pledgeQueryTime = "08:00"; //报名记录查询开始时间,以分钟为单位,仅针有manageUnit_signUpQuery权限的用户
	private int maxTransactionQueryDays = 30; //交易记录查询最大天数
	private Object transferMutex = new Object();
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof BiddingSignUp) {
			BiddingSignUp signUp = (BiddingSignUp)record;
			decryptSignUp(signUp);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof BiddingSignUp) {
			BiddingSignUp signUp = (BiddingSignUp)record;
			encryptSignUp(signUp);
		}
		super.save(record);
		if(record instanceof BiddingSignUp) {
			BiddingSignUp signUp = (BiddingSignUp)record;
			decryptSignUp(signUp); //解码
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof BiddingSignUp) {
			BiddingSignUp signUp = (BiddingSignUp)record;
			encryptSignUp(signUp);
		}
		super.update(record);
		if(record instanceof BiddingSignUp) {
			BiddingSignUp signUp = (BiddingSignUp)record;
			decryptSignUp(signUp); //解码
		}
		return record;
	}
	
	/**
	 * 企业信息加密
	 * @param signUp
	 * @throws ServiceException
	 */
	private void encryptSignUp(BiddingSignUp signUp) throws ServiceException {
		signUp.setEnterpriseName(cryptService.encrypt(signUp.getEnterpriseName(), "" + signUp.getId(), true));
		signUp.setEnterpriseBank(cryptService.encrypt(signUp.getEnterpriseBank(), "" + signUp.getId(), true));
		signUp.setEnterpriseAccount(cryptService.encrypt(signUp.getEnterpriseAccount(), "" + signUp.getId(), true));
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#decryptSignUp(com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp)
	 */
	public void decryptSignUp(BiddingSignUp signUp) throws ServiceException {
		signUp.setEnterpriseName(cryptService.decrypt(signUp.getEnterpriseName(), "" + signUp.getId(), true));
		signUp.setEnterpriseBank(cryptService.decrypt(signUp.getEnterpriseBank(), "" + signUp.getId(), true));
		signUp.setEnterpriseAccount(cryptService.decrypt(signUp.getEnterpriseAccount(), "" + signUp.getId(), true));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#decryptSignUps(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public void decryptSignUps(BiddingProject project) throws ServiceException {
		for(Iterator iterator = project.getSignUps()==null ? null : project.getSignUps().iterator(); iterator!=null && iterator.hasNext();) {
			BiddingSignUp signUp = (BiddingSignUp)iterator.next();
			decryptSignUp(signUp); //企业信息解密
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#completeSignUp(long, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public BiddingSignUp completeSignUp(long projectId, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		//获取项目信息
		BiddingProject project = biddingProjectService.getProject(projectId);
		//获取招标公告
		BiddingProjectTender tender = (BiddingProjectTender)project.getTenders().iterator().next();
		//检查报名时间是否超过
		Timestamp now = DateTimeUtils.now();
		if((tender.getBuyDocumentBegin()!=null && now.before(tender.getBuyDocumentBegin())) || (tender.getBuyDocumentEnd()!=null && now.after(tender.getBuyDocumentEnd()))) {
			throw new ServiceException("不在报名时间段内,不允许报名");
		}
		BiddingSignUp signUp = new BiddingSignUp();
		signUp.setId(UUIDLongGenerator.generateId()); //ID
		signUp.setSignUpTime(DateTimeUtils.now()); //报名时间
		signUp.setEnterpriseId(sessionInfo==null || sessionInfo.isAnonymous() ? 0 : sessionInfo.getDepartmentId()); //企业ID
		signUp.setEnterpriseName(sessionInfo==null || sessionInfo.isAnonymous() ? null : sessionInfo.getDepartmentName()); //企业名称
		signUp.setSignUpPersonId(0); //报名用户ID,匿名
		signUp.setSignUpPersonName(null); //报名用户,匿名
		signUp.setProjectId(projectId); //项目ID
		signUp.setIsInternetPayment('0');
		signUp.setPaymentMoney(getSignUpPrice(project)); //标书价格
		signUp.setDrawPaymentMoney(getDrawingPrice(project)); //图纸价格
		signUp.setPledgePaymentMoney(tender.getPledgeMoney()); //保证金
		signUp.setProjectName(project.getProjectName()); //项目名称
		if(signUp.getPaymentMoney()<=0) {
			signUp.setIsInternetPayment('1'); //网络支付
			signUp.setPaymentId(10); //支付ID
			signUp.setPaymentTime(signUp.getSignUpTime()); //支付时间
			signUp.setPaymentBank(null); //支付的银行
		}
		//生成报名号
		int i = 0;
		for(; i<1000; i++) {
			try {
				signUp.setSignUpNo(biddingProjectParameterService.generateSignUpNumber(project, false));
				save(signUp);
				//把报名号写入COOKIE
				int cookieAge = 3600 * signUpNoCookieHours;
				CookieUtils.addCookie(response, "BiddingSignUpNo_" + signUp.getSignUpNo(), "" + signUp.getId(), cookieAge, "/", null, null);
				break;
			}
			catch(Exception e) {
				Logger.error(e.getMessage());
			}
		}
		if(i==1000) {
			throw new ServiceException();
		}
		//检查项目从业人员数量
		for(Iterator iterator = !"是".equals(project.getIsRealNameSignUp()) || project.getJobholders()==null ? null : project.getJobholders().iterator(); iterator!=null && iterator.hasNext();) {
			BiddingProjectJobholder projectJobholder = (BiddingProjectJobholder)iterator.next();
			if(projectJobholder.getJobholderNumber()<=0) {
				continue;
			}
			String jobholderIds = request.getParameter("jobholderIds_" + projectJobholder.getId());
			if(jobholderIds==null || jobholderIds.isEmpty() || jobholderIds.split(",").length<projectJobholder.getJobholderNumber()) {
				throw new ServiceException(projectJobholder.getJobholderCategory() + "人数不足");
			}
			String hql = "from BiddingJobholder BiddingJobholder" +
						 " where BiddingJobholder.id in (" + JdbcUtils.validateInClauseNumbers(jobholderIds) + ")" +
						 " and BiddingJobholder.enterpriseId=" + sessionInfo.getDepartmentId() +
						 " and BiddingJobholder.category='" + projectJobholder.getJobholderCategory() + "'" +
						 (projectJobholder.getQualifications()==null || projectJobholder.getQualifications().isEmpty() ? "" : " and BiddingJobholder.qualification in ('" + JdbcUtils.resetQuot(projectJobholder.getQualifications()).replaceAll(",", "','") + "')") +
						 " and BiddingJobholder.id not in (select BiddingSignUpJobholder.jobholderId from BiddingSignUpJobholder BiddingSignUpJobholder where BiddingSignUpJobholder.jobholderId=BiddingJobholder.id and BiddingSignUpJobholder.locked=1)";
			List jobholders = getDatabaseService().findRecordsByHql(hql);
			if(jobholders==null || jobholders.size()<projectJobholder.getJobholderNumber()) {
				throw new ServiceException(projectJobholder.getJobholderCategory() + "人数不足");
			}
			//保存参与人员
			for(Iterator iteratorJobholder = jobholders.iterator(); iteratorJobholder.hasNext();) {
				BiddingJobholder jobholder = (BiddingJobholder)iteratorJobholder.next();
				BiddingSignUpJobholder signUpJobholder = new BiddingSignUpJobholder();
				signUpJobholder.setId(UUIDLongGenerator.generateId()); //ID
				signUpJobholder.setSignUpId(signUp.getId()); //报名ID
				signUpJobholder.setJobholderCategory(jobholder.getCategory()); //人员类别
				signUpJobholder.setJobholderId(jobholder.getId()); //人员ID
				signUpJobholder.setJobholderName(jobholder.getName()); //人员姓名
				signUpJobholder.setQualification(jobholder.getQualification()); //资质等级
				signUpJobholder.setCertificateNumber(jobholder.getCertificateNumber()); //资质证书编号
				signUpJobholder.setLocked(1); //是否锁定
				signUpJobholder.setUnlockTime(null); //解锁时间
				getDatabaseService().saveRecord(signUpJobholder);
			}
		}
		return signUp;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#loadSignUp(java.lang.String)
	 */
	public BiddingSignUp loadSignUp(String signUpNo, boolean checkPaid) throws ServiceException {
		String hql = "from BiddingSignUp BiddingSignUp" +
					 " where BiddingSignUp.signUpNo='" + JdbcUtils.resetQuot(signUpNo) + "'";
		BiddingSignUp signUp = (BiddingSignUp)getDatabaseService().findRecordByHql(hql);
		retrieveSignUp(signUp, checkPaid);
		return signUp;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#loadSignUpByEnterprise(long, long, boolean)
	 */
	public BiddingSignUp loadSignUpByEnterprise(long enterpriseId, long projectId, boolean checkPaid) throws ServiceException {
		String hql = "from BiddingSignUp BiddingSignUp" +
					 " where BiddingSignUp.projectId=" + projectId +
					 " and BiddingSignUp.enterpriseId=" + enterpriseId;
		BiddingSignUp signUp = (BiddingSignUp)getDatabaseService().findRecordByHql(hql);
		retrieveSignUp(signUp, checkPaid);
		return signUp;
	}

	/**
	 * 检查支付情况、企业解密、设置代理
	 * @param signUp
	 * @param checkPaid
	 * @throws ServiceException
	 */
	private void retrieveSignUp(BiddingSignUp signUp, boolean checkPaid) throws ServiceException {
		if(signUp==null) {
			return;
		}
		if(checkPaid) {
			checkSignUpPaied(signUp);
		}
		decryptSignUp(signUp); //企业信息解密
		//设置代理
		signUp.setBiddingAgent((BiddingProjectAgent)getDatabaseService().findRecordByHql("from BiddingProjectAgent BiddingProjectAgent where BiddingProjectAgent.projectId=" + signUp.getProjectId()));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#receivePaperDocuments(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void receivePaperDocuments(String signUpNo, SessionInfo sessionInfo) throws ServiceException {
		String hql = "from BiddingSignUp BiddingSignUp" +
					 " where BiddingSignUp.signUpNo='" + JdbcUtils.resetQuot(signUpNo) + "'";
		BiddingSignUp signUp = (BiddingSignUp)getDatabaseService().findRecordByHql(hql);
		if(signUp.getReceivePaperDocumentsTime()!=null) { //纸质标书和图纸已领取
			return;
		}
		if(signUp.getPaymentTime()==null && signUp.getDrawPaymentTime()!=null) { //标书、图纸未支付
			return;
		}
		BiddingProject project = biddingProjectService.getProject(signUp.getProjectId());
		if((sessionInfo instanceof BiddingSessionInfo) && project.getAgentId()==sessionInfo.getUnitId()) { //检查当前用户是否是当前项目的代理
			signUp.setReceivePaperDocumentsTime(DateTimeUtils.now());
			getDatabaseService().updateRecord(signUp);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#redirectToPayment(java.lang.String, java.lang.String, long, javax.servlet.http.HttpServletResponse)
	 */
	public void redirectToPayment(String signUpNo, String paymentType, long siteId, HttpServletResponse response) throws ServiceException {
		//获取报名记录
		BiddingSignUp signUp = loadSignUp(signUpNo, false);
    	
    	//获取付款金额
    	double paymentMoney = 0;
    	boolean biddingDocumentsPayment = false;
    	boolean drawingsPayment = false;
    	boolean pledgePayment = false;
    	String[] types = paymentType.split(","); //支付类型,biddingDocuments/标书,drawing/图纸,pledge/保证金,可以有多个,用逗号分隔
    	for(int i=0; i<types.length; i++) {
    		if("biddingDocuments".equals(types[i])) { //标书
    			if(signUp.getPaymentTime()==null && !biddingDocumentsPayment) {
    				paymentMoney += signUp.getPaymentMoney();
    				biddingDocumentsPayment = true;
    				if(isSignUpTimeout(signUp.getProject())) { //超出报名时间
    					throw new ServiceException("timeout");
    				}
    			}
    		}
    		else if("drawing".equals(types[i])) { //图纸
    			if(signUp.getDrawPaymentTime()==null && !drawingsPayment) {
    				paymentMoney += signUp.getDrawPaymentMoney();
    				drawingsPayment = true;
    				if(isSignUpTimeout(signUp.getProject())) { //超出报名时间
    					throw new ServiceException("timeout");
    				}
    			}
    		}
    		else if("pledge".equals(types[i])) { //保证金
    			if(signUp.getPledgePaymentTime()==null && !pledgePayment) {
    				paymentMoney += signUp.getPledgePaymentMoney();
    				pledgePayment = true;
    				if(isPledgePaymentTimeout(signUp.getProject())) { //超出保证金缴费时间
    					throw new ServiceException("timeout");
    				}
    			}
    		}
    	}
    	//支付订单ID
    	String paymentOrderId;
    	if(biddingDocumentsPayment && !pledgePayment && !drawingsPayment) { //只缴报名费
    		paymentOrderId = signUp.getSignUpNo();
    	}
    	else {
    		paymentOrderId = (biddingDocumentsPayment ? "A" : "") + (pledgePayment ? "B" : "") + (drawingsPayment ? "C" : "") + signUp.getSignUpNo();
    	}
    	//支付原因
    	String paymentReason = null;
    	if(biddingDocumentsPayment) {
    		paymentReason = "标书";
    	}
    	if(pledgePayment) {
    		paymentReason = (paymentReason==null ? "" : paymentReason + "、") + "保证金";
    	}
    	if(drawingsPayment) {
    		paymentReason = (paymentReason==null ? "" : paymentReason + "、") + "图纸";
    	}
    	paymentReason += "(" + signUp.getProjectName() + "，" + signUp.getSignUpNo() + ")";
    	
    	//获取区域配置
    	BiddingProjectCity city = biddingProjectParameterService.getCityDetail(signUp.getProjectId());
    	String pledgePaymentMerchantIds = null;
    	if(pledgePayment) {
    		pledgePaymentMerchantIds = getPledgePaymentMerchantIds(city, signUp.getProject().getPledgeAccount());
    		if(pledgePaymentMerchantIds==null) {
    			throw new ServiceException();
    		}
    	}
    	//加载支付页面
    	String redirectOfSuccess = Environment.getWebApplicationUrl() + "/bidding/project/signup/completePayment.shtml" +
    							   "?signUpNo=" + signUp.getSignUpNo() +
    							   (siteId==0 ? "" : "&siteId=" + siteId);
    	paymentService.redirectToPayment("bidding/project/signup",
    									 paymentOrderId,
    									 pledgePayment ? pledgePaymentMerchantIds : city.getMerchantIds(), //商户ID
    									 !pledgePayment && city.getDocumentsB2CPayment()==1, //强制B2C支付
    									 pledgePayment && city.getPledgeB2BPayment()==1, //强制B2B支付
    									 "0",
    									 "anonymous",
    									 paymentReason,
    									 paymentMoney,
    									 "0",
    									 "",
    									 0,
    									 redirectOfSuccess,
    									 null,
    									 siteId,
    									 response);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#getPledgePaymentMerchantIds(long, java.lang.String)
	 */
	public String getPledgePaymentMerchantIds(long projectId, String projectPledgeAccount) throws ServiceException {
		BiddingProjectCity city = biddingProjectParameterService.getCityDetail(projectId);
		return getPledgePaymentMerchantIds(city, projectPledgeAccount);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#updateSignUps(com.yuanluesoft.bidding.enterprise.pojo.BiddingBidEnterprise)
	 */
	public void updateSignUps(BiddingBidEnterprise bidEnterprise) throws ServiceException {
		if(bidEnterprise.getBank()==null || bidEnterprise.getBank().isEmpty()) {
			return;
		}
		//更新报名记录中的企业信息
		String hql = "from BiddingSignUp BiddingSignUp" +
					 " where BiddingSignUp.enterpriseAccount is not null" +
					 " and BiddingSignUp.pledgeReturnTime is null" + //保证金返还时间为空
					 " order by BiddingSignUp.id";
		for(int i=0; ; i+=200) {
			List signUps = getDatabaseService().findRecordsByHql(hql, i, 200);
			if(signUps==null || signUps.isEmpty()) {
				break;
			}
			for(Iterator iterator = signUps.iterator(); iterator.hasNext();) {
				BiddingSignUp signUp = (BiddingSignUp)iterator.next();
				//检查帐号是否一致
				if(!bidEnterprise.getAccount().equals(cryptService.decrypt(signUp.getEnterpriseAccount(), "" + signUp.getId(), true))) {
					continue;
				}
				signUp.setEnterpriseName(bidEnterprise.getName()); //企业名称
				signUp.setEnterpriseBank(bidEnterprise.getBank()); //开户行
				update(signUp);
			}
			if(signUps.size()<200) {
				break;
			}
		}
	}
	
	/**
	 * 获取保证金支付商户ID列表
	 * @param city
	 * @param projectPledgeAccount 项目指定的保证金帐号
	 * @return
	 * @throws ServiceException
	 */
	private String getPledgePaymentMerchantIds(BiddingProjectCity city, String projectPledgeAccount) throws ServiceException {
		if(city==null) { //没有地区配置
			return null;
		}
		if(city.getPaymentMerchantIds()==null || city.getPaymentMerchantIds().isEmpty()) { //保证金支付商户未配置
			return null;
		}
		if(projectPledgeAccount==null || projectPledgeAccount.isEmpty()) { //没有指定了保证金账户
			return city.getPaymentMerchantIds();
		}
		PaymentMerchant paymentMerchant = paymentService.getPaymentMerchantByAccount(projectPledgeAccount);
		if(paymentMerchant==null) { //没有对应的商户
			return null;
		}
		if(("," + city.getPaymentMerchantIds() + ",").indexOf("," + paymentMerchant.getId() + ",")!=-1) { //检查商户是否属于允许的保证金支付商户
			return paymentMerchant.getId() + "";
		}
		return null;
	}
	
	/**
	 * 检查报名缴费是否完成
	 * @param signUp
	 * @throws ServiceException
	 */
	private void checkSignUpPaied(BiddingSignUp signUp) throws ServiceException {
		if(signUp.getPaymentTime()!=null && //标书已经完成支付
		   signUp.getDrawPaymentTime()!=null && //图纸已经完成支付
		   ((signUp.getPledgePaymentTime()!=null && signUp.getEnterpriseAccount()!=null && !signUp.getEnterpriseAccount().trim().isEmpty()) || //保证金已经支付,且支付企业账号不为空
		    (signUp.getPledgePaymentTime()==null && !biddingProjectParameterService.isPledgeInternetPayment(signUp.getProjectId())))) { //保证金未支付,且不需要从网络上支付
			return;
		}
		String[] paymentOrderIds = {"", "B", "C", "AB", "AC", "BC", "ABC"};
		double[] paymentMoneys = {signUp.getPaymentMoney(), //""
								  signUp.getPledgePaymentMoney(), //B
								  signUp.getDrawPaymentMoney(), //C
								  Math.round((signUp.getPaymentMoney() + signUp.getPledgePaymentMoney()) * 100)/100.0d, //AB
								  Math.round((signUp.getPaymentMoney() + signUp.getDrawPaymentMoney()) * 100)/100.0d, //AC
								  Math.round((signUp.getPledgePaymentMoney() + signUp.getDrawPaymentMoney()) * 100)/100.0d, //BC
								  Math.round((signUp.getPaymentMoney() + signUp.getPledgePaymentMoney() + signUp.getDrawPaymentMoney()) * 100)/100.0d //ABC
								  };
		boolean paied = false;
		for(int i=0; i<paymentOrderIds.length; i++) {
			//获取支付记录
			Payment payment = null;
			try {
				payment = paymentService.loadPayment("bidding/project/signup", paymentOrderIds[i] + signUp.getSignUpNo(), paymentOrderIds[i].indexOf('B')!=-1);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			if(payment==null || payment.getTransactSuccess()!='1' || paymentMoneys[i]!=payment.getMoney()) { //支付不存在、没有成功、交易金额不匹配
				continue;
			}
			//标书
			if(signUp.getPaymentTime()==null && (paymentOrderIds[i].equals("") || paymentOrderIds[i].indexOf('A')!=-1)) {
				signUp.setIsInternetPayment('1'); //网络支付
				signUp.setPaymentId(payment.getId()); //支付ID
				signUp.setPaymentTime(payment.getTransactTime()); //支付时间
				signUp.setPaymentBank(payment.getPaymentMethod()); //支付的银行
				paied = true;
			}
			//保证金
			if(paymentOrderIds[i].indexOf('B')!=-1 && 
			   (signUp.getPledgePaymentTime()==null || signUp.getEnterpriseAccount()==null || signUp.getEnterpriseAccount().trim().isEmpty())) { //没有支付过,或者企业账号为空
				signUp.setIsPledgeInternetPayment('1'); //网络支付
				signUp.setPledgePaymentId(payment.getId()); //支付ID
				signUp.setPledgePaidMoney(payment.getMoney()); //实收金额
				signUp.setPledgePaymentTime(payment.getTransactTime()); //支付时间
				signUp.setPledgeConfirm('1'); //保证金确认,自动确认
				signUp.setPledgePaymentBank(payment.getPaymentMethod()); //支付的银行
				updateEnterpriseAccount(signUp, payment.getPayerAccount(), payment.getPayerAccountName(), payment.getPayerBank()); //更新支付人账户信息
				paied = true;
			}
			//图纸
			if(signUp.getDrawPaymentTime()==null && paymentOrderIds[i].indexOf('C')!=-1) {
				signUp.setIsDrawInternetPayment('1'); //网络支付
				signUp.setDrawPaymentId(payment.getId()); //支付ID
				signUp.setDrawPaymentTime(payment.getTransactTime()); //支付时间
				signUp.setDrawPaymentBank(payment.getPaymentMethod()); //支付的银行
				paied = true;
			}
		}
		if(paied) {
			update(signUp);
		}
	}
	
	/**
	 * 更新支付人账户信息
	 * @param signUp
	 * @param account
	 * @param accountName
	 * @param bank
	 */
	private void updateEnterpriseAccount(BiddingSignUp signUp, String account, String accountName, String bank) throws ServiceException {
		if(account==null || account.trim().isEmpty()) { //帐号为空
			return;
		}
		if(accountName==null || accountName.trim().isEmpty() || bank==null || bank.trim().isEmpty()) { //账户名称或者开户行为空
			//从投标企业库中提取
			List bidEnterprises = enterpriseService.findBidEnterprise(account, accountName);
			if(bidEnterprises==null || bidEnterprises.isEmpty()) { //没有对应的企业
				enterpriseService.addBidEnterprise(account, accountName, bank); //写入投标企业库
			}
			else if(bidEnterprises!=null && bidEnterprises.size()==1) {
				BiddingBidEnterprise bidEnterprise = (BiddingBidEnterprise)bidEnterprises.get(0);
				account = bidEnterprise.getAccount();
				accountName = bidEnterprise.getName();
				bank = bidEnterprise.getBank();
			}
		}
		signUp.setEnterpriseAccount(account.trim());
		signUp.setEnterpriseName(accountName==null ? null : accountName.trim());
		signUp.setEnterpriseBank(bank==null ? null : bank.trim());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#getSignUpPrice(long)
	 */
	public double getSignUpPrice(long projectId) throws ServiceException {
		BiddingProject project = biddingProjectService.getProject(projectId);
		return getSignUpPrice(project);
	}
	
	/**
	 * 按项目获取报名费
	 * @param project
	 * @return
	 * @throws ServiceException
	 */
	private double getSignUpPrice(BiddingProject project) throws ServiceException {
		if(biddingProjectParameterService.getCityDetail(project.getCity()).getDocumentsUniformPrice()==1) { //标书统一定价
			//获取项目信息
			BiddingProjectParameter parameter = biddingProjectParameterService.getParameter(project.getProjectCategory(), project.getProjectProcedure(), project.getCity());
			return (parameter==null ? 200.0f : parameter.getBiddingDocumentsPrice()); //标书价格
		}
		else {
			BiddingProjectTender tender = (BiddingProjectTender)project.getTenders().iterator().next();
			return tender.getDocumentPrice(); //标书价格
		}
	}
	
	/**
	 * 按项目获取图纸价格
	 * @param project
	 * @return
	 * @throws ServiceException
	 */
	private double getDrawingPrice(BiddingProject project) throws ServiceException {
		if(biddingProjectParameterService.getCityDetail(project.getCity()).getDocumentsUniformPrice()==1) { //标书统一定价
			//获取项目信息
			//BiddingProjectParameter parameter = biddingProjectParameterService.getParameter(project.getProjectCategory(), project.getProjectProcedure(), project.getCity());
			return 600; //(parameter==null ? 200 : parameter.getDrawingPrice()); //图纸价格
		}
		else {
			BiddingProjectTender tender = (BiddingProjectTender)project.getTenders().iterator().next();
			return tender.getDrawingPrice(); //图纸价格
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#totalSignUp(long)
	 */
	public SignUpTotal totalSignUp(long projectId) throws ServiceException {
		SignUpTotal signUpTotal = new SignUpTotal();
		//获取报名记录
		String hql = "from BiddingSignUp BiddingSignUp" +
					 " where BiddingSignUp.projectId=" + projectId;
		List signUps = getDatabaseService().findRecordsByHql(hql);
		if(signUps==null || signUps.isEmpty()) {
			return signUpTotal;
		}
		signUpTotal.setTotal(signUps.size());

		//由于企业网银不能将缴费完成情况实时回传,故更新报名的缴费情况
		for(Iterator iterator = signUps.iterator(); iterator.hasNext();) {
			BiddingSignUp signUp = (BiddingSignUp)iterator.next();
			//更新报名的缴费情况
			checkSignUpPaied(signUp);
			//报名费是否已缴
			if(signUp.getPaymentTime()!=null) { //报名费已缴
				signUpTotal.setPaymentTotal(signUpTotal.getPaymentTotal() + 1);
			}
			//保证金是否已缴
			if(signUp.getPledgePaymentTime()!=null) { //保证金已缴
				signUpTotal.setPledgePaymentTotal(signUpTotal.getPledgePaymentTotal() + 1);
			}
		}
		return signUpTotal;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#completeUploadBid(java.lang.String)
	 */
	public void completeUploadBid(String signUpNo) throws ServiceException {
		BiddingSignUp signUp = loadSignUp(signUpNo, false);
		if(signUp.getPaymentTime()==null) { // || (signUp.getPledgePaymentTime()==null && biddingProjectParameterService.isPledgeInternetPayment(signUp.getProjectId()))) {
			throw new ServiceException("未缴费");
		}
		biddingProjectService.sendBidToBiddingSystem(signUp); //发送到评标服务器
		attachmentService.deleteAll("bidding/project/signup", "bid", signUp.getId()); //删除投标文件
		signUp.setUploadTime(DateTimeUtils.now()); //设置上传时间
		getDatabaseService().updateRecord(signUp);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#updatePledgeReturnStatus(long)
	 */
	public void updatePledgeReturnStatus(long projectId) throws ServiceException {
		//检查报名记录中是否有保证金已转账但未确认退还的
		String hql = "select BiddingSignUp.id" +
					 " from BiddingSignUp BiddingSignUp" +
					 " where BiddingSignUp.projectId=" + projectId +
					 " and not BiddingSignUp.pledgeReturnTransferTime is null" + //保证金返还转账时间不为空
					 " and BiddingSignUp.pledgeReturnTransferError is null" + //保证金返还转账失败原因为空
					 " and BiddingSignUp.pledgeReturnTime is null"; //保证金返还时间为空
		if(getDatabaseService().findRecordByHql(hql)==null) {
			return;
		}
		//获取地区名称
		hql = "select BiddingProject.city from BiddingProject BiddingProject where BiddingProject.id=" + projectId;
		bankTransactionsQuery((String)getDatabaseService().findRecordByHql(hql));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#updatePledgeStatus(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public void updatePledgeStatus(BiddingProject project) throws ServiceException {
		//查询银行交易
		if(project.getPlan()!=null && project.getPlan().getSubmitTime().after(DateTimeUtils.now())) { //递交标书截止时间未到
			bankTransactionsQuery(project.getCity());
		}
		//更新报名记录
		String hql = "from BiddingSignUp BiddingSignUp" +
					 " where BiddingSignUp.projectId=" + project.getId() +
					 " order by BiddingSignUp.signUpNo DESC, BiddingSignUp.paymentTime, BiddingSignUp.pledgePaymentTime";
		List  signUps = getDatabaseService().findRecordsByHql(hql);
		project.setSignUps(signUps==null ? null : new LinkedHashSet(signUps));
		decryptSignUps(project); //信息解密
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#bankTransactionsQuery()
	 */
	public void bankTransactionsQuery() throws ServiceException {
		bankTransactionsQuery(null);
	}
	
	/**
	 * 银行交易记录查询
	 * @param cityName
	 * @throws ServiceException
	 */
	private void bankTransactionsQuery(String cityName) throws ServiceException {
		//获取地区配置
		String hql = "from BiddingProjectCity BiddingProjectCity" +
					 (cityName==null ? "" : " where BiddingProjectCity.name='" + JdbcUtils.resetQuot(cityName) + "'");
		List cities = getDatabaseService().findRecordsByHql(hql);
		if(cities==null || cities.isEmpty()) {
			return;
		}
		for(Iterator iterator = cities.iterator(); iterator.hasNext();) {
			BiddingProjectCity city = (BiddingProjectCity)iterator.next();
			try {
				if(city.getPaymentMerchantIds()==null || city.getPaymentMerchantIds().isEmpty()) { //保证金网银商户ID
					continue;
				}
				String[] paymentMerchantIds = city.getPaymentMerchantIds().split(",");
				Date lastSuccessDay = null;
				Date minDay = DateTimeUtils.add(DateTimeUtils.date(), Calendar.DAY_OF_MONTH, -maxTransactionQueryDays); //最早从30天前查起
				for(int i=0; i<paymentMerchantIds.length; i++) {
					boolean success = true;
					final PaymentMerchant paymentMerchant = (PaymentMerchant)paymentService.load(PaymentMerchant.class, Long.parseLong(paymentMerchantIds[i]));
					Date day = (city.getLastTransactionQuery()==null ? minDay : DateTimeUtils.parseDate(DateTimeUtils.formatTimestamp(city.getLastTransactionQuery(), "yyyy-MM-dd"), "yyyy-MM-dd"));
					Date currentSuccessDay = day;
					do {
						List transactions = null;
						try {
							transactions = paymentService.listTransactions(paymentMerchant.getId(), day); //交易记录查询
						}
						catch(Exception e) {
							Logger.exception(e);
							success = false;
						}
						processTransactions(transactions, paymentMerchant.getPaymentMethod(), null);
						if(success) {
							currentSuccessDay = day;
						}
						day = DateTimeUtils.add(day, Calendar.DAY_OF_MONTH, 1);
					}
					while(!day.after(DateTimeUtils.date()));
					if(lastSuccessDay==null || currentSuccessDay.before(lastSuccessDay)) {
						lastSuccessDay = currentSuccessDay;
					}
				}
				if(lastSuccessDay==null || lastSuccessDay.before(minDay)) {
 					lastSuccessDay = minDay;
				}
				city.setLastTransactionQuery(new Timestamp(lastSuccessDay.getTime())); //更新最后查询时间
				getDatabaseService().updateRecord(city);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#processBankTransactions(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void processBankTransactions(String bank, String transactionsFileName, SessionInfo sessionInfo) throws ServiceException {
		List transactions = paymentService.parseTransactionsFile(bank, transactionsFileName);
		processTransactions(transactions, bank, sessionInfo);
	}
	
	/**
	 * 处理交易记录
	 * @param transactions
	 * @param bank
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void processTransactions(List transactions, String bank, SessionInfo sessionInfo) throws ServiceException {
		if(transactions==null || transactions.isEmpty()) {
			return;
		}
		for(Iterator iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction)iterator.next();
			if(transaction.getMoney()==0 || //交易金额为0
			   transaction.getTransactionTime()==null || //交易时间为空
			   transaction.getPeerAccount()==null || transaction.getPeerAccount().trim().isEmpty()) { //没有对方账户
				continue;
			}
			//查找报名记录
			List signUps = null;
			String hql = "from BiddingSignUp BiddingSignUp " +
						 " where BiddingSignUp.signUpNo is not null" +
						 " and 'REMARK' like concat(concat('%', BiddingSignUp.signUpNo), '%')";
			if(transaction.getRemark()!=null && !transaction.getRemark().trim().isEmpty()) { //备注不为空
				signUps = getDatabaseService().findRecordsByHql(hql.replaceFirst("REMARK", StringUtils.fullWidthToHalfAngle(transaction.getRemark().trim()).replaceAll("[\r\n\t '\":]", "")), 0, 2);
			}
			if((signUps==null || signUps.isEmpty()) && transaction.getSummary()!=null && !transaction.getSummary().trim().isEmpty()) { //摘要不为空
				signUps = getDatabaseService().findRecordsByHql(hql.replaceFirst("REMARK", StringUtils.fullWidthToHalfAngle(transaction.getSummary().trim()).replaceAll("[\r\n\t '\":]", "")), 0, 2);
			}
			if((signUps==null || signUps.isEmpty()) && transaction.getMoney()<0) { //没有找到对应的报名记录,且是支出
				//根据报名ID查找报名记录
				hql = "from BiddingSignUp BiddingSignUp" +
					  " where BiddingSignUp.pledgeConfirm='1'" + //保证金已确认
					  " and BiddingSignUp.signUpNo is null" + //没有报名号
					  " and 'REMARK' like concat(concat('%退保证金', id), '%')";
				if(transaction.getRemark()!=null && !transaction.getRemark().trim().isEmpty()) { //备注不为空
					signUps = getDatabaseService().findRecordsByHql(hql.replaceFirst("REMARK", StringUtils.fullWidthToHalfAngle(transaction.getRemark().trim()).replaceAll("[\r\n\t '\":]", "")), 0, 2);
				}
				if((signUps==null || signUps.isEmpty()) && transaction.getSummary()!=null && !transaction.getSummary().trim().isEmpty()) { //摘要不为空
					signUps = getDatabaseService().findRecordsByHql(hql.replaceFirst("REMARK", StringUtils.fullWidthToHalfAngle(transaction.getSummary().trim()).replaceAll("[\r\n\t '\":]", "")), 0, 2);
				}
				if(signUps==null || signUps.isEmpty()) {
					//根据保证金回单ID查找报名记录,保持和旧模式兼容
					hql = "from BiddingSignUp BiddingSignUp" +
						  " where BiddingSignUp.pledgeConfirm='1'" + //保证金已确认
						  " and BiddingSignUp.signUpNo is null" + //没有报名号
						  " and BiddingSignUp.pledgeReturnMoney=" + (-transaction.getMoney()) +
						  " and BiddingSignUp.pledgeReturnTime is null" +
						  " and not BiddingSignUp.pledgeBillBack is null" +
						  " and 'REMARK' like concat(concat('%退保证金', pledgeBillBack), '%')";
					if(transaction.getRemark()!=null && !transaction.getRemark().trim().isEmpty()) { //备注不为空
						signUps = getDatabaseService().findRecordsByHql(hql.replaceFirst("REMARK", StringUtils.fullWidthToHalfAngle(transaction.getRemark().trim()).replaceAll("[\r\n\t '\":]", "")), 0, 2);
					}
					if((signUps==null || signUps.isEmpty()) && transaction.getSummary()!=null && !transaction.getSummary().trim().isEmpty()) { //摘要不为空
						signUps = getDatabaseService().findRecordsByHql(hql.replaceFirst("REMARK", StringUtils.fullWidthToHalfAngle(transaction.getSummary().trim()).replaceAll("[\r\n\t '\":]", "")), 0, 2);
					}
				}
			}
			if(signUps!=null && signUps.size()>1) { //超过2个报名记录
				continue;
			}
			BiddingSignUp signUp = signUps==null || signUps.isEmpty() ? null : (BiddingSignUp)signUps.get(0);
			if(signUp!=null && transaction.getMoney()<0) { //支出
				if(signUp.getPledgeReturnTime()==null) { //未退还
					//把保证金登记为已退
					signUp.setPledgeReturnTime(transaction.getTransactionTime()); //保证金返还时间
					signUp.setPledgeReturnMoney(-transaction.getMoney()); //保证金返还金额
					signUp.setPledgeReturnTransactorId(sessionInfo==null ? 0 : sessionInfo.getUserId()); //保证金返还人ID
					signUp.setPledgeReturnTransactor(sessionInfo==null ? "系统" : sessionInfo.getUserName()); //保证金返还人
					signUp.setPledgeReturnEnabled('0'); //保证金返还标志复位
					update(signUp);
				}
			}
			else if(signUp!=null && transaction.getMoney()>0) { //收入
				boolean changed = false; 
				if(signUp.getEnterpriseAccount()==null || signUp.getEnterpriseAccount().trim().isEmpty()) { //帐号为空
					updateEnterpriseAccount(signUp, transaction.getPeerAccount(), transaction.getPeerAccountName(), null);
					changed = true;
				}
				if((signUp.getEnterpriseName()==null || signUp.getEnterpriseName().trim().isEmpty()) &&  //企业名称为空
				   transaction.getPeerAccountName()!=null && !transaction.getPeerAccountName().isEmpty()) {  //交易记录中有对方企业名称
					signUp.setEnterpriseName(transaction.getPeerAccountName()); //企业名称
					changed = true;
				}
				if(signUp.getPledgePaymentTime()==null) { //未支付
					signUp.setIsPledgeInternetPayment('0'); //保证金是否网络支付
					signUp.setPledgePaidMoney(transaction.getMoney()); //实收金额
					signUp.setPledgePaymentTime(transaction.getTransactionTime()); //保证金支付时间
					signUp.setPledgeConfirm('1'); //保证金确认,自动确认
					signUp.setPledgePaymentBank(bank); //保证金银行,非网络支付时填写
					signUp.setPledgeBillBack(transaction.getTransactionNumber()); //保证金银行回单ID,非网络支付时填写
					signUp.setPledgeRecorderId(sessionInfo==null ? 0 : sessionInfo.getUserId()); //保证金记录人ID,非网络支付时填写
					signUp.setPledgeRecorder(sessionInfo==null ? "系统" : sessionInfo.getUserName()); //保证金记录人姓名,非网络支付时填写
					signUp.setPledgeRecordTime(DateTimeUtils.now()); //保证金记录时间,非网络支付时填写
					changed = true;
				}
				if(changed) {
					update(signUp);
				}
			}
			else if(transaction.getMoney()>0 && transaction.getTransactionNumber()!=null && !transaction.getTransactionNumber().isEmpty()) { //收入
				//检查购买招标文件开始时间<=交易时间<=投标文件的递交截止时间、且保证金金额相同的项目
				String time = "TIMESTAMP(" + DateTimeUtils.formatTimestamp(transaction.getTransactionTime(), null) + ")";
				hql = "select distinct BiddingProject" +
					  " from BiddingProject BiddingProject" +
					  " left join BiddingProject.plans BiddingProjectPlan" +
					  " left join BiddingProject.tenders BiddingProjectTender" +
					  " where BiddingProjectPlan.buyDocumentBegin<=" + time + //购买招标文件开始时间
					  " and BiddingProjectPlan.submitTime>=" + time + //投标文件的递交截止时间
					  " and BiddingProjectTender.pledgeMoney=" + transaction.getMoney() +
					  " and (select min(BiddingSignUp.id)" +
					  "  from BiddingSignUp BiddingSignUp" +
					  "  where BiddingSignUp.projectId=BiddingProject.id" +
					  "  and BiddingSignUp.pledgePaymentBank='" + JdbcUtils.resetQuot(bank) + "'" +
					  "  and BiddingSignUp.pledgeBillBack='" + JdbcUtils.resetQuot(transaction.getTransactionNumber()) + "'" +
					  "  and BiddingSignUp.pledgePaymentTime=" + time + ") is null";
				List projects = getDatabaseService().findRecordsByHql(hql);
				for(Iterator iteratorProject = projects==null ? null : projects.iterator(); iteratorProject!=null && iteratorProject.hasNext();) {
					BiddingProject project = (BiddingProject)iteratorProject.next();
					if(project.getPledgeAccount()!=null && !project.getPledgeAccount().isEmpty() && //项目指定了保证金账户
					   transaction.getMyAccount()!=null && !transaction.getMyAccount().isEmpty() && //交易记录中的本方帐号不为空
					   !transaction.getMyAccount().trim().equals(project.getPledgeAccount().trim())) { //和当前项目的保证金账户不一致
						continue;
					}
					//创建新的报名记录,用来记录保证金提交
					BiddingSignUp pledgeSignUp = new BiddingSignUp();
					pledgeSignUp.setId(UUIDLongGenerator.generateId()); //ID
					pledgeSignUp.setProjectId(project.getId()); //工程ID
					pledgeSignUp.setProjectName(project.getProjectName()); //工程名称
					//更新企业账户信息
					updateEnterpriseAccount(pledgeSignUp, transaction.getPeerAccount(), transaction.getPeerAccountName(), null);
					//保证金支付相关属性
					pledgeSignUp.setPledgePaymentMoney(transaction.getMoney()); //保证金支付金额
					pledgeSignUp.setPledgePaidMoney(transaction.getMoney()); //保证金实收金额
					pledgeSignUp.setPledgePaymentTime(transaction.getTransactionTime()); //保证金支付时间
					pledgeSignUp.setPledgePaymentBank(bank); //保证金银行,非网络支付时填写
					pledgeSignUp.setPledgeBillBack(transaction.getTransactionNumber()); //保证金银行回单ID,非网络支付时填写
					String remark = null;
					if(transaction.getRemark()!=null && !transaction.getRemark().trim().isEmpty()) {
						remark = transaction.getRemark().trim();
					}
					if(transaction.getSummary()!=null && !transaction.getSummary().trim().isEmpty()) {
						remark = (remark==null ? "" : remark + ",") + transaction.getSummary().trim();
					}
					if(remark!=null) {
						pledgeSignUp.setPledgeBillBackRemark(remark.length()<150 ? remark : remark.substring(0, 150));
					}
					save(pledgeSignUp);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#accountComplement(long, long, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void accountComplement(long projectId, long signUpId, String enterpriseName, String enterpriseBank, String enterpriseAccount) throws ServiceException {
		BiddingSignUp signUp = (BiddingSignUp)load(BiddingSignUp.class, signUpId);
		if(signUp.getProjectId()!=projectId) {
			return;
		}
		if(signUp.getEnterpriseName()==null || signUp.getEnterpriseName().isEmpty() || signUp.getEnterpriseName().indexOf('*')!=-1) {
			signUp.setEnterpriseName(enterpriseName);
		}
		signUp.setEnterpriseBank(enterpriseBank);
		if(signUp.getEnterpriseAccount()==null || signUp.getEnterpriseAccount().isEmpty() || signUp.getEnterpriseAccount().indexOf('*')!=-1) {
			signUp.setEnterpriseAccount(enterpriseAccount);
		}
		update(signUp);
		//更新投标企业库
		enterpriseService.addBidEnterprise(signUp.getEnterpriseAccount(), signUp.getEnterpriseName(), signUp.getEnterpriseBank());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#accountsComplement(com.yuanluesoft.bidding.project.pojo.BiddingProject, javax.servlet.http.HttpServletRequest)
	 */
	public void accountsComplement(BiddingProject project, HttpServletRequest request) throws ServiceException {
		String[] signUpIds = request.getParameterValues("signUpId");
		if(signUpIds==null || signUpIds.length==0) {
			return;
		}
		//确认要排除的保证金报名记录ID
		String[] pledgeConfirmSignUpIds = request.getParameterValues("pledgeConfirmSignUpId");
		//选中的返还ID
		String[] pledgeReturnSignUpIds = request.getParameterValues("pledgeReturnSignUpId");
		if(pledgeReturnSignUpIds!=null && pledgeReturnSignUpIds.length>0) {
			retrievePledgeReturnMoney(project); //重新计算返还金额
		}
		String[] enterpriseNames = request.getParameterValues("enterpriseName");
		String[] enterpriseBanks = request.getParameterValues("enterpriseBank");
		String[] enterpriseAccounts = request.getParameterValues("enterpriseAccount");
		for(int i=0; i<signUpIds.length; i++) {
			BiddingSignUp signUp = (BiddingSignUp)ListUtils.findObjectByProperty(project.getSignUps(), "id", new Long(signUpIds[i]));
			if(signUp.getEnterpriseName()==null || signUp.getEnterpriseName().isEmpty() || signUp.getEnterpriseName().indexOf('*')!=-1) {
				signUp.setEnterpriseName(enterpriseNames[i]);
			}
			signUp.setEnterpriseBank(enterpriseBanks[i]);
			if(signUp.getEnterpriseAccount()==null || signUp.getEnterpriseAccount().isEmpty() || signUp.getEnterpriseAccount().indexOf('*')!=-1) {
				signUp.setEnterpriseAccount(enterpriseAccounts[i]);
			}
			if(pledgeConfirmSignUpIds!=null &&
			   signUp.getPledgeConfirm()!='3' && //没有被其他项目使用
			   (signUp.getSignUpNo()==null || signUp.getSignUpNo().isEmpty())) { //没有报名号
				boolean remove = false;
				for(int j=0; j<pledgeConfirmSignUpIds.length; j++) {
					if(pledgeConfirmSignUpIds[j].equals(signUpIds[i])) {
						remove = true;
						break;
					}
				}
				if(remove) { //剔除
					if(signUp.getPledgeConfirm()=='2') {  //0/未确认, 1/已确认 2/本项目中排除 3/已被其他项目确认
						continue; //原来就是剔除的
					}
					if(signUp.getPledgeConfirm()=='1') { //原来是已确认的
						relationPledgeConfirm(signUp, '0'); //把其他项目中回单号相同的报名记录设置为"未确认"状态
					}
					signUp.setPledgeConfirm('2'); //0/未确认, 1/已确认 2/本项目中排除 3/已被其他项目确认
				}
				else { //确认有效
					if(signUp.getPledgeConfirm()=='1') { //原来就是有效的
						continue;
					}
					//原来是剔除的或者是未确认的
					relationPledgeConfirm(signUp, '3'); //把其他项目中回单号相同的报名记录设置为'3'
					signUp.setPledgeConfirm('1'); //0/未确认, 1/已确认 2/本项目中排除 3/已被其他项目确认
				}
			}
			if(pledgeReturnSignUpIds!=null) {
				//判断是否允许返还
				char pledgeReturnEnabled = '0';
				for(int j=0; j<pledgeReturnSignUpIds.length; j++) {
					if(pledgeReturnSignUpIds[j].equals(signUpIds[i])) {
						pledgeReturnEnabled = '1';
						break;
					}
				}
				if(pledgeReturnEnabled=='1' && signUp.getPledgeReturnEnabled()!=pledgeReturnEnabled) {
					signUp.setPledgeReturnExportTime(DateTimeUtils.now()); //导出时间
				}
				signUp.setPledgeReturnEnabled(pledgeReturnEnabled);
			}
			update(signUp);
			//更新投标企业库
			enterpriseService.addBidEnterprise(signUp.getEnterpriseAccount(), signUp.getEnterpriseName(), signUp.getEnterpriseBank());
		}
	}
	
	/**
	 * 关联报名记录保证金确认
	 * @param signUp
	 * @param status
	 */
	private void relationPledgeConfirm(BiddingSignUp signUp, char status) throws ServiceException {
		String hql = "from BiddingSignUp BiddingSignUp" +
					 " where BiddingSignUp.id!=" + signUp.getId() +
					 " and BiddingSignUp.pledgePaymentBank='" + JdbcUtils.resetQuot(signUp.getPledgePaymentBank()) + "'" +
					 " and BiddingSignUp.pledgeBillBack='" + JdbcUtils.resetQuot(signUp.getPledgeBillBack()) + "'" +
					 " and BiddingSignUp.signUpNo is null";
		List signUps = getDatabaseService().findRecordsByHql(hql);
		if(signUps==null || signUps.isEmpty()) {
			return;
		}
		for(Iterator iterator = signUps.iterator(); iterator.hasNext();) {
			BiddingSignUp relationSignUp = (BiddingSignUp)iterator.next();
			//检查帐号是否相同
			if(!signUp.getEnterpriseAccount().equals(cryptService.decrypt(relationSignUp.getEnterpriseAccount(), "" + relationSignUp.getId(), true))) {
				continue;
			}
			relationSignUp.setPledgeConfirm(status);
			update(relationSignUp);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#saveSignUpRanking(com.yuanluesoft.bidding.project.pojo.BiddingProject, java.lang.String)
	 */
	public void saveSignUpRanking(BiddingProject biddingProject, String rankingSignUpIds) throws ServiceException {
		if(biddingProject.getSignUps()==null || biddingProject.getSignUps().isEmpty()) {
			return;
		}
		List rankingSignUpList = rankingSignUpIds==null || rankingSignUpIds.isEmpty() ? new ArrayList() : ListUtils.generateList(rankingSignUpIds, ",");
		for(Iterator iterator = biddingProject.getSignUps().iterator(); iterator.hasNext();) {
			BiddingSignUp signUp = (BiddingSignUp)iterator.next();
			signUp.setRanking(rankingSignUpList.indexOf("" + signUp.getId()) + 1);
			update(signUp);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#listRankingSignUps(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public List listRankingSignUps(BiddingProject biddingProject) throws ServiceException {
		if(biddingProject.getSignUps()==null || biddingProject.getSignUps().isEmpty()) {
			return null;
		}
		List rankingSignUps = new ArrayList();
		for(Iterator iterator = biddingProject.getSignUps().iterator(); iterator.hasNext();) {
			BiddingSignUp signUp = (BiddingSignUp)iterator.next();
			if(signUp.getRanking()>0) {
				rankingSignUps.add(signUp);
			}
		}
		Collections.sort(rankingSignUps, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				BiddingSignUp signUp0 = (BiddingSignUp)arg0;
				BiddingSignUp signUp1 = (BiddingSignUp)arg1;
				return signUp0.getRanking()==signUp1.getRanking() ? 0 : (signUp0.getRanking()>signUp1.getRanking() ? 1 : -1);
			}
		});
		return rankingSignUps.isEmpty() ? null : rankingSignUps;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#retrievePledgeReturnMoney(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public void retrievePledgeReturnMoney(BiddingProject biddingProject) throws ServiceException {
		if(biddingProject.getSignUps()==null || biddingProject.getSignUps().isEmpty()) {
			return;
		}
		//获取地区配置
		BiddingProjectCity city = biddingProjectParameterService.getCityDetail(biddingProject.getCity());
		for(Iterator iterator = biddingProject.getSignUps().iterator(); iterator.hasNext();) {
			BiddingSignUp signUp = (BiddingSignUp)iterator.next();
			if(signUp.getPledgePaymentTime()==null || signUp.getPledgeReturnTime()!=null) { //未支付或者已经返还
				continue;
			}
			//计算计息天数
			long days = 0;
			try {
				days = (DateTimeUtils.date().getTime() - DateTimeUtils.parseDate(DateTimeUtils.formatTimestamp(signUp.getPledgePaymentTime(), "yyyy-MM-dd"), "yyyy-MM-dd").getTime()) / (24*3600*1000);
			}
			catch (ParseException e) {
			
			}
			signUp.setPledgeReturnDays(Math.max(0, days-1));
			//计算利息
			double interest = 0;
			if(city.getPledgeReturnRate()>0) {
				interest = signUp.getPledgePaidMoney() * city.getPledgeReturnRate() / 100 / 360 * signUp.getPledgeReturnDays();
				if(city.getPledgeReturnTax()>0 && city.getPledgeReturnTax()<100) { //利息税
					interest = interest * (100-city.getPledgeReturnTax()) / 100d;
				}
			}
			//计算保证金返还金额
			signUp.setPledgeReturnMoney(Math.round((signUp.getPledgePaidMoney() + interest) * 100) / 100.0d);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#pledgeReturnTransfer(com.yuanluesoft.bidding.project.pojo.BiddingProject, java.lang.String, com.yuanluesoft.jeaf.sso.matcher.Matcher, javax.servlet.http.HttpServletRequest)
	 */
	public void pledgeReturnTransfer(BiddingProject project, String transferPassword, Matcher passwordMatcher, HttpServletRequest request) throws ServiceException {
		accountsComplement(project, request); //完善账户信息
		PaymentMerchant paymentMerchant = getPledgePaymentMerchant(project); //获取保证金支付商户
		synchronized(transferMutex) {
			List transfers = generatePledgeReturnTransfers(project, paymentMerchant); //生成保证金返还转账记录列表
			if(transfers==null || transfers.isEmpty()) {
				return;
			}
			//余额校验
			double balance = paymentService.getBalance(paymentMerchant.getId());
			double totalTransfer = 0;
			for(Iterator iterator = transfers.iterator(); iterator.hasNext(); ) {
				Transfer transfer = (Transfer)iterator.next();
				totalTransfer += transfer.getMoney();
			}
			if(totalTransfer > balance) {
				for(Iterator iterator = transfers.iterator(); iterator.hasNext(); ) {
					Transfer transfer = (Transfer)iterator.next();
					BiddingSignUp signUp = (BiddingSignUp)transfer.getRelationRecord();
					signUp.setPledgeReturnTransferTime(DateTimeUtils.now());
					signUp.setPledgeReturnTransferError("可用余额不足"); //错误原因
					update(signUp);
				}
				return;
			}
			//记录转账时间
			for(Iterator iterator = transfers.iterator(); iterator.hasNext(); ) {
				Transfer transfer = (Transfer)iterator.next();
				BiddingSignUp signUp = (BiddingSignUp)transfer.getRelationRecord();
				signUp.setPledgeReturnTransferTime(DateTimeUtils.now());
				signUp.setPledgeReturnTransferError(null); //清空错误原因
				update(signUp);
			}
			//转账
			try {
				paymentService.transfer(paymentMerchant.getId(), transfers, transferPassword, passwordMatcher);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			catch(Error e) {
				e.printStackTrace();
			}
			for(Iterator iterator = transfers.iterator(); iterator.hasNext(); ) {
				Transfer transfer = (Transfer)iterator.next();
				if(transfer.isSuccess()) { //转账成功
					continue;
				}
				//转账失败
				BiddingSignUp signUp = (BiddingSignUp)transfer.getRelationRecord();
				signUp.setPledgeReturnTransferError(transfer.getError()==null || transfer.getError().isEmpty() ? "未知原因" : transfer.getError());
				update(signUp);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#writePledgeTransferFile(com.yuanluesoft.bidding.project.pojo.BiddingProject, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void writePledgeTransferFile(BiddingProject project, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		accountsComplement(project, request); //完善账户信息
		PaymentMerchant paymentMerchant = getPledgePaymentMerchant(project); //获取保证金支付商户
		synchronized(transferMutex) {
			List transfers = generatePledgeReturnTransfers(project, paymentMerchant); //生成保证金返还转账记录列表
			if(transfers==null) {
				return;
			}
			//输出文件
			String transferFilePath = temporaryFileManageService.createTemporaryDirectory(null);
			String transferFileName = paymentService.createTransferFile(paymentMerchant.getId(), transfers, transferFilePath, "保证金返还(" + project.getProjectName() + ")");
			try {
				fileDownloadService.httpDownload(request, response, transferFileName, null, true, null);
			}
			catch(Exception e) {
				throw new ServiceException(e);
			}
			finally {
				FileUtils.deleteFile(transferFileName);
			}
		}
	}
	
	/**
	 * 获取保证金支付商户
	 * @param project
	 * @return
	 * @throws ServiceException
	 */
	private PaymentMerchant getPledgePaymentMerchant(BiddingProject project) throws ServiceException {
		//获取项目保证金银行
		String account = project.getTender().getAccounts(); //从招标公告中获取保证金缴纳帐号
		PaymentMerchant paymentMerchant = null; //商户
		if(account!=null) { 
			paymentMerchant = paymentService.getPaymentMerchantByAccount(account); //按账户查找对应的商户
		}
		if(paymentMerchant!=null) {
			return paymentMerchant;
		}
		//获取地区配置
		BiddingProjectCity city = biddingProjectParameterService.getCityDetail(project.getCity());
		if(city.getPaymentMerchantIds()==null || city.getPaymentMerchantIds().isEmpty()) {
			throw new ServiceException("payment merchant not set");
		}
		//获取第一个保证金缴费银行
		paymentMerchant = (PaymentMerchant)paymentService.load(PaymentMerchant.class, Long.parseLong(city.getPaymentMerchantIds().split(",")[0]));
		if(paymentMerchant==null) { 
			throw new ServiceException("payment merchant not exists");
		}
		return paymentMerchant;
	}
	
	/**
	 * 生成转账记录列表
	 * @param project
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	private List generatePledgeReturnTransfers(BiddingProject project, PaymentMerchant paymentMerchant) throws ServiceException {
		List transfers = new ArrayList();
		//查找项目ID
		for(Iterator iterator = project.getSignUps()==null ? null : project.getSignUps().iterator(); iterator.hasNext();) {
			BiddingSignUp signUp = (BiddingSignUp)iterator.next();
			signUp = (BiddingSignUp)load(BiddingSignUp.class, signUp.getId());
			if(signUp.getPledgePaymentTime()==null || //未交保证金
			   signUp.getPledgeConfirm()!='1' || //未确认
			   signUp.getPledgeReturnTime()!=null || //已经返还
			   signUp.getPledgeReturnEnabled()!='1') { //不允许返还
				continue;
			}
			//如果转账时间不为空,且转账错误原因为空,则不允许继续转账,以避免多次转账
			if(signUp.getPledgeReturnTransferTime()!=null && (signUp.getPledgeReturnTransferError()==null || signUp.getPledgeReturnTransferError().isEmpty())) {
				continue;
			}
			if(signUp.getEnterpriseName()==null || signUp.getEnterpriseName().isEmpty() || //企业名称为空
			   signUp.getEnterpriseAccount()==null || signUp.getEnterpriseAccount().isEmpty() || //企业账户为空
			   signUp.getEnterpriseBank()==null || signUp.getEnterpriseBank().isEmpty()) { //填写开户行为空
				continue;
			}
			//生成转账记录
			Transfer transfer = new Transfer();
			transfer.setRelationRecord(signUp);
			transfer.setFromUnit(paymentMerchant.getName()); //汇出单位 文本格式,南平中心为“南平市招标投标服务中心”，各县市不同
			transfer.setFromUnitAcount(paymentMerchant.getAccount()); //汇出帐号 长数值格式，应生成数字串，不能为科学计数法，南平中心为“35001676107052505220”，各县市不同, 建设银行福建省分行电子银行部体验专户:35001002406050002965
			//transfer.setFromBankFirstCode(); //汇出行一级分行 数值格式，不能为科学计数法，南平及所辖各县市均为“350000000”
			transfer.setToUnitAccount(signUp.getEnterpriseAccount()); //收款帐号 长数值格式，应生成数字串，不能为科学计数法，数据从单位网银卸出的流水中提取
			transfer.setToUnit(signUp.getEnterpriseName()); //收款单位 文本格式 数据从单位网银卸出的流水中提取
			transfer.setToBankFirstCode(""); //收款单位一级分行 数值格式，可空（不是“0”）
			transfer.setToUnitBank(signUp.getEnterpriseBank()); //收款单位开户行 文本格式，必填项，数据无法从单位网银提取，可从投标单位的备案库中查找提取。投标单位备案库：初次由建行根据历史参加投标的单位数据流水，从建行系统查询其开户行，填写完整后，导入投标单位的备案库中。以后新增的投标单位均由建行工作人员查询其开户行后发给中心工作，添加入投标单位的备案库中。
			transfer.setToBankCode(""); //收款单位联行号 数值格式，可空（不是“0”）
			transfer.setToUnitOrgCode(""); //收款单位机构号 数值格式，可空（不是“0”）
			transfer.setMoney(signUp.getPledgeReturnMoney()); //金额 长数值格式，应生成数字串，不能为科学计数法，数据从单位网银卸出的流水中提取
			transfer.setCurrency("人民币"); //数值格式，为“1”，代表人民币
			transfer.setUses("退保证金" + (signUp.getSignUpNo()==null || signUp.getSignUpNo().isEmpty() ? "" + signUp.getId() : signUp.getSignUpNo())); //用途 文本格式，由系统根据标书名称自行填写
			transfers.add(transfer);
		}
		return transfers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#isPledgeVisible(com.yuanluesoft.bidding.project.pojo.BiddingProject, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isPledgeVisible(BiddingProject project, SessionInfo sessionInfo) throws ServiceException {
		if(project.getPlan()==null) {
			return false;
		}
		Timestamp time = project.getPlan().getSubmitTime(); //递交标书截止时间
		if(time==null) {
			time = project.getPlan().getBidopeningTime(); //开标时间
		}
		if(time==null) {
			return false;
		}
		//检查用户的项目访问权限
		if(recordControlService.getAccessLevel(project.getId(), BiddingProject.class.getName(), sessionInfo)<RecordControlService.ACCESS_LEVEL_READONLY) {
			return false;
		}
		if(time.before(DateTimeUtils.now())) { //开标时间已到
			return true;
		}
		if(pledgeQueryTime==null || pledgeQueryTime.isEmpty()) { //没有提前量
			return false;
		}
		//检查用户是否有manageUnit_signUpQuery权限
		List acl = accessControlService.getAcl("bidding/project/signup", sessionInfo);
		if(acl==null || !acl.contains("manageUnit_signUpQuery")) {
			return false;
		}
		try {
			if(DateTimeUtils.parseTimestamp(DateTimeUtils.formatTimestamp(time, "yyyy-MM-dd") + " " + pledgeQueryTime, "yyyy-MM-dd HH:mm").before(DateTimeUtils.now())) {
				return true;
			}
		}
		catch (ParseException e) {
			Logger.exception(e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#isPledgePaymentTimeout(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public boolean isPledgePaymentTimeout(BiddingProject project) {
		BiddingProjectTender tender = getTender(project);
		return tender.getPledgeTime()!=null && tender.getPledgeTime().before(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.signup.service.BiddingService#isSignUpTimeout(com.yuanluesoft.bidding.project.pojo.BiddingProject)
	 */
	public boolean isSignUpTimeout(BiddingProject project) {
		BiddingProjectTender tender = getTender(project);
		Timestamp now = DateTimeUtils.now();
		return (tender.getBuyDocumentBegin()!=null && now.before(tender.getBuyDocumentBegin())) || (tender.getBuyDocumentEnd()!=null && now.after(tender.getBuyDocumentEnd()));
	}
	
	/**
	 * 获取招标公告
	 * @param project
	 * @return
	 */
	public BiddingProjectTender getTender(BiddingProject project) {
		BiddingProjectTender tender = project.getTender();
		if(tender==null) {
			tender = (BiddingProjectTender)getDatabaseService().findRecordByHql("from BiddingProjectTender BiddingProjectTender where BiddingProjectTender.projectId=" + project.getId());
			project.setTenders(new HashSet());
			project.getTenders().add(tender);
		}
		return tender;
	}

	/**
	 * @return the paymentService
	 */
	public PaymentService getPaymentService() {
		return paymentService;
	}

	/**
	 * @param paymentService the paymentService to set
	 */
	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	/**
	 * @return the biddingProjectService
	 */
	public BiddingProjectService getBiddingProjectService() {
		return biddingProjectService;
	}

	/**
	 * @param biddingProjectService the biddingProjectService to set
	 */
	public void setBiddingProjectService(BiddingProjectService biddingProjectService) {
		this.biddingProjectService = biddingProjectService;
	}

	/**
	 * @return the bidUploadPaddingMinutes
	 */
	public int getBidUploadPaddingMinutes() {
		return bidUploadPaddingMinutes;
	}

	/**
	 * @param bidUploadPaddingMinutes the bidUploadPaddingMinutes to set
	 */
	public void setBidUploadPaddingMinutes(int bidUploadPaddingMinutes) {
		this.bidUploadPaddingMinutes = bidUploadPaddingMinutes;
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
	 * @return the signUpNoCookieHours
	 */
	public int getSignUpNoCookieHours() {
		return signUpNoCookieHours;
	}

	/**
	 * @param signUpNoCookieHours the signUpNoCookieHours to set
	 */
	public void setSignUpNoCookieHours(int signUpNoCookieHours) {
		this.signUpNoCookieHours = signUpNoCookieHours;
	}

	/**
	 * @return the cryptService
	 */
	public CryptService getCryptService() {
		return cryptService;
	}

	/**
	 * @param cryptService the cryptService to set
	 */
	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}

	/**
	 * @return the fileDownloadService
	 */
	public FileDownloadService getFileDownloadService() {
		return fileDownloadService;
	}

	/**
	 * @param fileDownloadService the fileDownloadService to set
	 */
	public void setFileDownloadService(FileDownloadService fileDownloadService) {
		this.fileDownloadService = fileDownloadService;
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
	 * @return the accessControlService
	 */
	public AccessControlService getAccessControlService() {
		return accessControlService;
	}

	/**
	 * @param accessControlService the accessControlService to set
	 */
	public void setAccessControlService(AccessControlService accessControlService) {
		this.accessControlService = accessControlService;
	}

	/**
	 * @return the recordControlService
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}

	/**
	 * @param recordControlService the recordControlService to set
	 */
	public void setRecordControlService(RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}

	/**
	 * @return the maxTransactionQueryDays
	 */
	public int getMaxTransactionQueryDays() {
		return maxTransactionQueryDays;
	}

	/**
	 * @param maxTransactionQueryDays the maxTransactionQueryDays to set
	 */
	public void setMaxTransactionQueryDays(int maxTransactionQueryDays) {
		this.maxTransactionQueryDays = maxTransactionQueryDays;
	}

	/**
	 * @return the pledgeQueryTime
	 */
	public String getPledgeQueryTime() {
		return pledgeQueryTime;
	}

	/**
	 * @param pledgeQueryTime the pledgeQueryTime to set
	 */
	public void setPledgeQueryTime(String pledgeQueryTime) {
		this.pledgeQueryTime = pledgeQueryTime;
	}

	/**
	 * @return the enterpriseService
	 */
	public EnterpriseService getEnterpriseService() {
		return enterpriseService;
	}

	/**
	 * @param enterpriseService the enterpriseService to set
	 */
	public void setEnterpriseService(EnterpriseService enterpriseService) {
		this.enterpriseService = enterpriseService;
	}
}