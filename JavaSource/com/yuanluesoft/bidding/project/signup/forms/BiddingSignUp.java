package com.yuanluesoft.bidding.project.signup.forms;

import java.sql.Timestamp;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class BiddingSignUp extends ActionForm {
	private long projectId; //工程ID
	private String projectName; //工程名称
	private String signUpNo; //报名号,随机数
	private long enterpriseId; //企业ID
	private String enterpriseName; //企业名称
	private String enterpriseBank; //开户行,开标后由投标人或代理填写，退还保证金用
	private String enterpriseAccount; //帐号,开标后由投标人或代理填写，退还保证金用
	private long signUpPersonId; //报名用户ID
	private String signUpPersonName; //报名用户名称
	private Timestamp signUpTime; //报名时间
	//招标书支付相关属性
	private char isInternetPayment = '0'; //是否网络支付
	private double paymentMoney; //支付金额
	private Timestamp paymentTime; //支付时间
	private long paymentId; //支付单ID,网络支付时填写
	private String paymentBank; //银行,非网络支付时填写
	private String billBack; //银行回单ID,非网络支付时填写
	private long recorderId; //记录人ID,非网络支付时填写
	private String recorder; //记录人姓名,非网络支付时填写
	private Timestamp recordTime; //记录时间,非网络支付时填写
	//图纸书支付相关属性
	private char isDrawInternetPayment = '0'; //图纸是否网络支付
	private double drawPaymentMoney; //图纸支付金额
	private Timestamp drawPaymentTime; //图纸支付时间
	private long drawPaymentId; //图纸支付单ID,网络支付时填写
	private String drawPaymentBank; //图纸银行,非网络支付时填写
	private String drawBillBack; //图纸银行回单ID,非网络支付时填写
	private long drawRecorderId; //图纸支付记录人ID,非网络支付时填写
	private String drawRecorder; //图纸支付记录人姓名,非网络支付时填写
	private Timestamp drawRecordTime; //图纸支付记录时间,非网络支付时填写
	//保证金书支付相关属性
	private char isPledgeInternetPayment = '0'; //保证金是否网络支付
	private double pledgePaymentMoney; //保证金支付金额
	private double pledgePaidMoney; //保证金实收金额
	private Timestamp pledgePaymentTime; //保证金支付时间
	private long pledgePaymentId; //保证金支付单ID,网络支付时填写
	private String pledgePaymentBank; //保证金银行,非网络支付时填写
	private String pledgeBillBack; //保证金银行回单ID,非网络支付时填写
	private String pledgeBillBackRemark; //保证金银行回单说明
	private char pledgeConfirm = '0'; //保证金确认
	private long pledgeRecorderId; //保证金记录人ID,非网络支付时填写
	private String pledgeRecorder; //保证金记录人姓名,非网络支付时填写
	private Timestamp pledgeRecordTime; //保证金记录时间,非网络支付时填写
	private char pledgeReturnEnabled = '0'; //是否允许返还保证金
	private Timestamp pledgeReturnExportTime; //保证金返还导出时间
	private Timestamp pledgeReturnTransferTime; //保证金返还转账时间
	private String pledgeReturnTransferError; //保证金返还转账失败原因
	private Timestamp pledgeReturnTime; //保证金返还时间
	private long pledgeReturnTransactorId; //保证金返还人ID
	private String pledgeReturnTransactor; //保证金返还人
	private double pledgeReturnRate; //保证金利率,百分比
	private double pledgeReturnTax; //保证金利息所得税率,百分比
	private double pledgeReturnDays; //保证金计息天数
	private double pledgeReturnMoney; //保证金返还金额
	private Timestamp agentSettleTime; //代理费用结算时间,暂时未使用
	private double agentSettleFee; //代理结算手续费,暂时未使用
	private long agentSettleTransactorId; //代理费用结算人ID,暂时未使用
	private String agentSettleTransactor; //代理费用结算人,暂时未使用
	private int ranking; //评标排名,由代理填写，退还保证金时使用

	private Timestamp receivePaperDocumentsTime; //纸质标书领取时间
	private Timestamp uploadTime; //投标书上传时间
	
	private BiddingProject project; //项目信息

	/**
	 * @return the billBack
	 */
	public String getBillBack() {
		return billBack;
	}

	/**
	 * @param billBack the billBack to set
	 */
	public void setBillBack(String billBack) {
		this.billBack = billBack;
	}
	
	/**
	 * @return the enterpriseId
	 */
	public long getEnterpriseId() {
		return enterpriseId;
	}

	/**
	 * @param enterpriseId the enterpriseId to set
	 */
	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	/**
	 * @return the enterpriseName
	 */
	public String getEnterpriseName() {
		return enterpriseName;
	}

	/**
	 * @param enterpriseName the enterpriseName to set
	 */
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	/**
	 * @return the isInternetPayment
	 */
	public char getIsInternetPayment() {
		return isInternetPayment;
	}

	/**
	 * @param isInternetPayment the isInternetPayment to set
	 */
	public void setIsInternetPayment(char isInternetPayment) {
		this.isInternetPayment = isInternetPayment;
	}

	/**
	 * @return the isPledgeInternetPayment
	 */
	public char getIsPledgeInternetPayment() {
		return isPledgeInternetPayment;
	}

	/**
	 * @param isPledgeInternetPayment the isPledgeInternetPayment to set
	 */
	public void setIsPledgeInternetPayment(char isPledgeInternetPayment) {
		this.isPledgeInternetPayment = isPledgeInternetPayment;
	}

	/**
	 * @return the paymentBank
	 */
	public String getPaymentBank() {
		return paymentBank;
	}

	/**
	 * @param paymentBank the paymentBank to set
	 */
	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	/**
	 * @return the paymentId
	 */
	public long getPaymentId() {
		return paymentId;
	}

	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	/**
	 * @return the paymentMoney
	 */
	public double getPaymentMoney() {
		return paymentMoney;
	}

	/**
	 * @param paymentMoney the paymentMoney to set
	 */
	public void setPaymentMoney(double paymentMoney) {
		this.paymentMoney = paymentMoney;
	}

	/**
	 * @return the paymentTime
	 */
	public Timestamp getPaymentTime() {
		return paymentTime;
	}

	/**
	 * @param paymentTime the paymentTime to set
	 */
	public void setPaymentTime(Timestamp paymentTime) {
		this.paymentTime = paymentTime;
	}

	/**
	 * @return the pledgeBillBack
	 */
	public String getPledgeBillBack() {
		return pledgeBillBack;
	}

	/**
	 * @param pledgeBillBack the pledgeBillBack to set
	 */
	public void setPledgeBillBack(String pledgeBillBack) {
		this.pledgeBillBack = pledgeBillBack;
	}

	/**
	 * @return the pledgePaymentBank
	 */
	public String getPledgePaymentBank() {
		return pledgePaymentBank;
	}

	/**
	 * @param pledgePaymentBank the pledgePaymentBank to set
	 */
	public void setPledgePaymentBank(String pledgePaymentBank) {
		this.pledgePaymentBank = pledgePaymentBank;
	}

	/**
	 * @return the pledgePaymentId
	 */
	public long getPledgePaymentId() {
		return pledgePaymentId;
	}

	/**
	 * @param pledgePaymentId the pledgePaymentId to set
	 */
	public void setPledgePaymentId(long pledgePaymentId) {
		this.pledgePaymentId = pledgePaymentId;
	}

	/**
	 * @return the pledgePaymentMoney
	 */
	public double getPledgePaymentMoney() {
		return pledgePaymentMoney;
	}

	/**
	 * @param pledgePaymentMoney the pledgePaymentMoney to set
	 */
	public void setPledgePaymentMoney(double pledgePaymentMoney) {
		this.pledgePaymentMoney = pledgePaymentMoney;
	}

	/**
	 * @return the pledgePaymentTime
	 */
	public Timestamp getPledgePaymentTime() {
		return pledgePaymentTime;
	}

	/**
	 * @param pledgePaymentTime the pledgePaymentTime to set
	 */
	public void setPledgePaymentTime(Timestamp pledgePaymentTime) {
		this.pledgePaymentTime = pledgePaymentTime;
	}

	/**
	 * @return the pledgeRecorder
	 */
	public String getPledgeRecorder() {
		return pledgeRecorder;
	}

	/**
	 * @param pledgeRecorder the pledgeRecorder to set
	 */
	public void setPledgeRecorder(String pledgeRecorder) {
		this.pledgeRecorder = pledgeRecorder;
	}

	/**
	 * @return the pledgeRecorderId
	 */
	public long getPledgeRecorderId() {
		return pledgeRecorderId;
	}

	/**
	 * @param pledgeRecorderId the pledgeRecorderId to set
	 */
	public void setPledgeRecorderId(long pledgeRecorderId) {
		this.pledgeRecorderId = pledgeRecorderId;
	}

	/**
	 * @return the pledgeRecordTime
	 */
	public Timestamp getPledgeRecordTime() {
		return pledgeRecordTime;
	}

	/**
	 * @param pledgeRecordTime the pledgeRecordTime to set
	 */
	public void setPledgeRecordTime(Timestamp pledgeRecordTime) {
		this.pledgeRecordTime = pledgeRecordTime;
	}

	/**
	 * @return the project
	 */
	public BiddingProject getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(BiddingProject project) {
		this.project = project;
	}

	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the recorder
	 */
	public String getRecorder() {
		return recorder;
	}

	/**
	 * @param recorder the recorder to set
	 */
	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}

	/**
	 * @return the recorderId
	 */
	public long getRecorderId() {
		return recorderId;
	}

	/**
	 * @param recorderId the recorderId to set
	 */
	public void setRecorderId(long recorderId) {
		this.recorderId = recorderId;
	}

	/**
	 * @return the recordTime
	 */
	public Timestamp getRecordTime() {
		return recordTime;
	}

	/**
	 * @param recordTime the recordTime to set
	 */
	public void setRecordTime(Timestamp recordTime) {
		this.recordTime = recordTime;
	}

	/**
	 * @return the signUpNo
	 */
	public String getSignUpNo() {
		return signUpNo;
	}

	/**
	 * @param signUpNo the signUpNo to set
	 */
	public void setSignUpNo(String signUpNo) {
		this.signUpNo = signUpNo;
	}

	/**
	 * @return the signUpPersonId
	 */
	public long getSignUpPersonId() {
		return signUpPersonId;
	}

	/**
	 * @param signUpPersonId the signUpPersonId to set
	 */
	public void setSignUpPersonId(long signUpPersonId) {
		this.signUpPersonId = signUpPersonId;
	}

	/**
	 * @return the signUpPersonName
	 */
	public String getSignUpPersonName() {
		return signUpPersonName;
	}

	/**
	 * @param signUpPersonName the signUpPersonName to set
	 */
	public void setSignUpPersonName(String signUpPersonName) {
		this.signUpPersonName = signUpPersonName;
	}

	/**
	 * @return the signUpTime
	 */
	public Timestamp getSignUpTime() {
		return signUpTime;
	}

	/**
	 * @param signUpTime the signUpTime to set
	 */
	public void setSignUpTime(Timestamp signUpTime) {
		this.signUpTime = signUpTime;
	}

	/**
	 * @return the uploadTime
	 */
	public Timestamp getUploadTime() {
		return uploadTime;
	}

	/**
	 * @param uploadTime the uploadTime to set
	 */
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * @return the drawBillBack
	 */
	public String getDrawBillBack() {
		return drawBillBack;
	}

	/**
	 * @param drawBillBack the drawBillBack to set
	 */
	public void setDrawBillBack(String drawBillBack) {
		this.drawBillBack = drawBillBack;
	}

	/**
	 * @return the drawPaymentBank
	 */
	public String getDrawPaymentBank() {
		return drawPaymentBank;
	}

	/**
	 * @param drawPaymentBank the drawPaymentBank to set
	 */
	public void setDrawPaymentBank(String drawPaymentBank) {
		this.drawPaymentBank = drawPaymentBank;
	}

	/**
	 * @return the drawPaymentId
	 */
	public long getDrawPaymentId() {
		return drawPaymentId;
	}

	/**
	 * @param drawPaymentId the drawPaymentId to set
	 */
	public void setDrawPaymentId(long drawPaymentId) {
		this.drawPaymentId = drawPaymentId;
	}

	/**
	 * @return the drawPaymentMoney
	 */
	public double getDrawPaymentMoney() {
		return drawPaymentMoney;
	}

	/**
	 * @param drawPaymentMoney the drawPaymentMoney to set
	 */
	public void setDrawPaymentMoney(double drawPaymentMoney) {
		this.drawPaymentMoney = drawPaymentMoney;
	}

	/**
	 * @return the drawPaymentTime
	 */
	public Timestamp getDrawPaymentTime() {
		return drawPaymentTime;
	}

	/**
	 * @param drawPaymentTime the drawPaymentTime to set
	 */
	public void setDrawPaymentTime(Timestamp drawPaymentTime) {
		this.drawPaymentTime = drawPaymentTime;
	}

	/**
	 * @return the drawRecorder
	 */
	public String getDrawRecorder() {
		return drawRecorder;
	}

	/**
	 * @param drawRecorder the drawRecorder to set
	 */
	public void setDrawRecorder(String drawRecorder) {
		this.drawRecorder = drawRecorder;
	}

	/**
	 * @return the drawRecorderId
	 */
	public long getDrawRecorderId() {
		return drawRecorderId;
	}

	/**
	 * @param drawRecorderId the drawRecorderId to set
	 */
	public void setDrawRecorderId(long drawRecorderId) {
		this.drawRecorderId = drawRecorderId;
	}

	/**
	 * @return the drawRecordTime
	 */
	public Timestamp getDrawRecordTime() {
		return drawRecordTime;
	}

	/**
	 * @param drawRecordTime the drawRecordTime to set
	 */
	public void setDrawRecordTime(Timestamp drawRecordTime) {
		this.drawRecordTime = drawRecordTime;
	}

	/**
	 * @return the isDrawInternetPayment
	 */
	public char getIsDrawInternetPayment() {
		return isDrawInternetPayment;
	}

	/**
	 * @param isDrawInternetPayment the isDrawInternetPayment to set
	 */
	public void setIsDrawInternetPayment(char isDrawInternetPayment) {
		this.isDrawInternetPayment = isDrawInternetPayment;
	}

	/**
	 * @return the receivePaperDocumentsTime
	 */
	public Timestamp getReceivePaperDocumentsTime() {
		return receivePaperDocumentsTime;
	}

	/**
	 * @param receivePaperDocumentsTime the receivePaperDocumentsTime to set
	 */
	public void setReceivePaperDocumentsTime(Timestamp receivePaperDocumentsTime) {
		this.receivePaperDocumentsTime = receivePaperDocumentsTime;
	}

	/**
	 * @return the agentSettleFee
	 */
	public double getAgentSettleFee() {
		return agentSettleFee;
	}

	/**
	 * @param agentSettleFee the agentSettleFee to set
	 */
	public void setAgentSettleFee(double agentSettleFee) {
		this.agentSettleFee = agentSettleFee;
	}

	/**
	 * @return the agentSettleTime
	 */
	public Timestamp getAgentSettleTime() {
		return agentSettleTime;
	}

	/**
	 * @param agentSettleTime the agentSettleTime to set
	 */
	public void setAgentSettleTime(Timestamp agentSettleTime) {
		this.agentSettleTime = agentSettleTime;
	}

	/**
	 * @return the agentSettleTransactor
	 */
	public String getAgentSettleTransactor() {
		return agentSettleTransactor;
	}

	/**
	 * @param agentSettleTransactor the agentSettleTransactor to set
	 */
	public void setAgentSettleTransactor(String agentSettleTransactor) {
		this.agentSettleTransactor = agentSettleTransactor;
	}

	/**
	 * @return the agentSettleTransactorId
	 */
	public long getAgentSettleTransactorId() {
		return agentSettleTransactorId;
	}

	/**
	 * @param agentSettleTransactorId the agentSettleTransactorId to set
	 */
	public void setAgentSettleTransactorId(long agentSettleTransactorId) {
		this.agentSettleTransactorId = agentSettleTransactorId;
	}

	/**
	 * @return the enterpriseAccount
	 */
	public String getEnterpriseAccount() {
		return enterpriseAccount;
	}

	/**
	 * @param enterpriseAccount the enterpriseAccount to set
	 */
	public void setEnterpriseAccount(String enterpriseAccount) {
		this.enterpriseAccount = enterpriseAccount;
	}

	/**
	 * @return the enterpriseBank
	 */
	public String getEnterpriseBank() {
		return enterpriseBank;
	}

	/**
	 * @param enterpriseBank the enterpriseBank to set
	 */
	public void setEnterpriseBank(String enterpriseBank) {
		this.enterpriseBank = enterpriseBank;
	}

	/**
	 * @return the pledgeReturnDays
	 */
	public double getPledgeReturnDays() {
		return pledgeReturnDays;
	}

	/**
	 * @param pledgeReturnDays the pledgeReturnDays to set
	 */
	public void setPledgeReturnDays(double pledgeReturnDays) {
		this.pledgeReturnDays = pledgeReturnDays;
	}

	/**
	 * @return the pledgeReturnExportTime
	 */
	public Timestamp getPledgeReturnExportTime() {
		return pledgeReturnExportTime;
	}

	/**
	 * @param pledgeReturnExportTime the pledgeReturnExportTime to set
	 */
	public void setPledgeReturnExportTime(Timestamp pledgeReturnExportTime) {
		this.pledgeReturnExportTime = pledgeReturnExportTime;
	}

	/**
	 * @return the pledgeReturnMoney
	 */
	public double getPledgeReturnMoney() {
		return pledgeReturnMoney;
	}

	/**
	 * @param pledgeReturnMoney the pledgeReturnMoney to set
	 */
	public void setPledgeReturnMoney(double pledgeReturnMoney) {
		this.pledgeReturnMoney = pledgeReturnMoney;
	}

	/**
	 * @return the pledgeReturnRate
	 */
	public double getPledgeReturnRate() {
		return pledgeReturnRate;
	}

	/**
	 * @param pledgeReturnRate the pledgeReturnRate to set
	 */
	public void setPledgeReturnRate(double pledgeReturnRate) {
		this.pledgeReturnRate = pledgeReturnRate;
	}

	/**
	 * @return the pledgeReturnTax
	 */
	public double getPledgeReturnTax() {
		return pledgeReturnTax;
	}

	/**
	 * @param pledgeReturnTax the pledgeReturnTax to set
	 */
	public void setPledgeReturnTax(double pledgeReturnTax) {
		this.pledgeReturnTax = pledgeReturnTax;
	}

	/**
	 * @return the pledgeReturnTime
	 */
	public Timestamp getPledgeReturnTime() {
		return pledgeReturnTime;
	}

	/**
	 * @param pledgeReturnTime the pledgeReturnTime to set
	 */
	public void setPledgeReturnTime(Timestamp pledgeReturnTime) {
		this.pledgeReturnTime = pledgeReturnTime;
	}

	/**
	 * @return the pledgeReturnTransactor
	 */
	public String getPledgeReturnTransactor() {
		return pledgeReturnTransactor;
	}

	/**
	 * @param pledgeReturnTransactor the pledgeReturnTransactor to set
	 */
	public void setPledgeReturnTransactor(String pledgeReturnTransactor) {
		this.pledgeReturnTransactor = pledgeReturnTransactor;
	}

	/**
	 * @return the pledgeReturnTransactorId
	 */
	public long getPledgeReturnTransactorId() {
		return pledgeReturnTransactorId;
	}

	/**
	 * @param pledgeReturnTransactorId the pledgeReturnTransactorId to set
	 */
	public void setPledgeReturnTransactorId(long pledgeReturnTransactorId) {
		this.pledgeReturnTransactorId = pledgeReturnTransactorId;
	}

	/**
	 * @return the ranking
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	/**
	 * @return the pledgeReturnTransferError
	 */
	public String getPledgeReturnTransferError() {
		return pledgeReturnTransferError;
	}

	/**
	 * @param pledgeReturnTransferError the pledgeReturnTransferError to set
	 */
	public void setPledgeReturnTransferError(String pledgeReturnTransferError) {
		this.pledgeReturnTransferError = pledgeReturnTransferError;
	}

	/**
	 * @return the pledgeReturnTransferTime
	 */
	public Timestamp getPledgeReturnTransferTime() {
		return pledgeReturnTransferTime;
	}

	/**
	 * @param pledgeReturnTransferTime the pledgeReturnTransferTime to set
	 */
	public void setPledgeReturnTransferTime(Timestamp pledgeReturnTransferTime) {
		this.pledgeReturnTransferTime = pledgeReturnTransferTime;
	}

	/**
	 * @return the pledgePaidMoney
	 */
	public double getPledgePaidMoney() {
		return pledgePaidMoney;
	}

	/**
	 * @param pledgePaidMoney the pledgePaidMoney to set
	 */
	public void setPledgePaidMoney(double pledgePaidMoney) {
		this.pledgePaidMoney = pledgePaidMoney;
	}

	/**
	 * @return the pledgeReturnEnabled
	 */
	public char getPledgeReturnEnabled() {
		return pledgeReturnEnabled;
	}

	/**
	 * @param pledgeReturnEnabled the pledgeReturnEnabled to set
	 */
	public void setPledgeReturnEnabled(char pledgeReturnEnabled) {
		this.pledgeReturnEnabled = pledgeReturnEnabled;
	}

	/**
	 * @return the pledgeBillBackRemark
	 */
	public String getPledgeBillBackRemark() {
		return pledgeBillBackRemark;
	}

	/**
	 * @param pledgeBillBackRemark the pledgeBillBackRemark to set
	 */
	public void setPledgeBillBackRemark(String pledgeBillBackRemark) {
		this.pledgeBillBackRemark = pledgeBillBackRemark;
	}

	/**
	 * @return the pledgeConfirm
	 */
	public char getPledgeConfirm() {
		return pledgeConfirm;
	}

	/**
	 * @param pledgeConfirm the pledgeConfirm to set
	 */
	public void setPledgeConfirm(char pledgeConfirm) {
		this.pledgeConfirm = pledgeConfirm;
	}
}