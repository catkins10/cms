package com.yuanluesoft.bidding.project.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 地区配置(bidding_project_city)
 * @author linchuan
 *
 */
public class BiddingProjectCity extends Record {
	private String name; //名称,福州市
	private String shortName; //简称,市,在项目编号时使用
	private String trading; //交易中心名称
	private String tradingAddress; //交易中心地址
	private String drawAddress; //代理抽签地址
	private String inviteMedia; //发布招标邀请书媒体,福州市建设工程交易管理中心公告栏；福州建设工程招标投标网（网址：http://www.fzztb.rog）或福建招标与采购网（http://www.fjbid.gov.cn）
	private String buyDocumentAddress; //购买招标文件地址
	private String askMedia; //招标文件质疑地点,福州市建设工程交易管理中心公告栏；福州建设工程招标投标网（网址：http://www.fzztb.rog）或福建招标与采购网（http://www.fjbid.gov.cn）
	private String submitAddress; //投标文件的递交地点,福州市建设工程交易管理中心
	private String bidopeningAddress; //开标、评标地点,福州市建设工程交易管理中心
	private String pitchonAddress; //确定中标人地点,福州市建设工程交易管理中心
	private String publicPitchonMedia; //中标结果公示媒体
	private String noticeAddress; //发放中标通知书地点,招标人
	private String archiveAddress; //书面报告备案地点,招标人
	private String declareAddress; //项目报建地点,政务中心
	private String licenceAddress; //施工许可证发放地点,政务中心
	private String workBeginAm; //上午上班时间
	private String workEndAm; //上午下班时间
	private String workBeginPm; //下午上班时间
	private String workEndPm; //下午下班时间
	private String agentChargeStandard; //代理取费标准
	private String agentDrawRemark; //代理抽签说明
	private double agentSettleFee; //代理结算手续费
	private String merchantIds; //网银商户ID
	private String merchantNames; //网银商户名称
	private int documentsB2CPayment; //标书和图纸强制B2C支付
	private String paymentMerchantIds; //保证金网银商户ID
	private String paymentMerchantNames; //保证金网银商户名称
	private int pledgeB2BPayment; //保证金强制B2B支付
	private int documentsUniformPrice; //标书统一定价
	private int pledgeInternetPayment; //网络支付保证金
	private double pledgeReturnRate; //保证金利率
	private double pledgeReturnTax; //保证金利息所得税率
	private Timestamp lastTransactionQuery; //交易记录最后查询时间
	private Set visitors; //访问者列表
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * @return the archiveAddress
	 */
	public String getArchiveAddress() {
		return archiveAddress;
	}
	/**
	 * @param archiveAddress the archiveAddress to set
	 */
	public void setArchiveAddress(String archiveAddress) {
		this.archiveAddress = archiveAddress;
	}
	/**
	 * @return the askMedia
	 */
	public String getAskMedia() {
		return askMedia;
	}
	/**
	 * @param askMedia the askMedia to set
	 */
	public void setAskMedia(String askMedia) {
		this.askMedia = askMedia;
	}
	/**
	 * @return the bidopeningAddress
	 */
	public String getBidopeningAddress() {
		return bidopeningAddress;
	}
	/**
	 * @param bidopeningAddress the bidopeningAddress to set
	 */
	public void setBidopeningAddress(String bidopeningAddress) {
		this.bidopeningAddress = bidopeningAddress;
	}
	/**
	 * @return the buyDocumentAddress
	 */
	public String getBuyDocumentAddress() {
		return buyDocumentAddress;
	}
	/**
	 * @param buyDocumentAddress the buyDocumentAddress to set
	 */
	public void setBuyDocumentAddress(String buyDocumentAddress) {
		this.buyDocumentAddress = buyDocumentAddress;
	}
	/**
	 * @return the drawAddress
	 */
	public String getDrawAddress() {
		return drawAddress;
	}
	/**
	 * @param drawAddress the drawAddress to set
	 */
	public void setDrawAddress(String drawAddress) {
		this.drawAddress = drawAddress;
	}
	/**
	 * @return the inviteMedia
	 */
	public String getInviteMedia() {
		return inviteMedia;
	}
	/**
	 * @param inviteMedia the inviteMedia to set
	 */
	public void setInviteMedia(String inviteMedia) {
		this.inviteMedia = inviteMedia;
	}
	/**
	 * @return the noticeAddress
	 */
	public String getNoticeAddress() {
		return noticeAddress;
	}
	/**
	 * @param noticeAddress the noticeAddress to set
	 */
	public void setNoticeAddress(String noticeAddress) {
		this.noticeAddress = noticeAddress;
	}
	/**
	 * @return the pitchonAddress
	 */
	public String getPitchonAddress() {
		return pitchonAddress;
	}
	/**
	 * @param pitchonAddress the pitchonAddress to set
	 */
	public void setPitchonAddress(String pitchonAddress) {
		this.pitchonAddress = pitchonAddress;
	}
	/**
	 * @return the publicPitchonMedia
	 */
	public String getPublicPitchonMedia() {
		return publicPitchonMedia;
	}
	/**
	 * @param publicPitchonMedia the publicPitchonMedia to set
	 */
	public void setPublicPitchonMedia(String publicPitchonMedia) {
		this.publicPitchonMedia = publicPitchonMedia;
	}
	/**
	 * @return the submitAddress
	 */
	public String getSubmitAddress() {
		return submitAddress;
	}
	/**
	 * @param submitAddress the submitAddress to set
	 */
	public void setSubmitAddress(String submitAddress) {
		this.submitAddress = submitAddress;
	}
	/**
	 * @return the trading
	 */
	public String getTrading() {
		return trading;
	}
	/**
	 * @param trading the trading to set
	 */
	public void setTrading(String trading) {
		this.trading = trading;
	}
	/**
	 * @return the tradingAddress
	 */
	public String getTradingAddress() {
		return tradingAddress;
	}
	/**
	 * @param tradingAddress the tradingAddress to set
	 */
	public void setTradingAddress(String tradingAddress) {
		this.tradingAddress = tradingAddress;
	}
	/**
	 * @return the declareAddress
	 */
	public String getDeclareAddress() {
		return declareAddress;
	}
	/**
	 * @param declareAddress the declareAddress to set
	 */
	public void setDeclareAddress(String declareAddress) {
		this.declareAddress = declareAddress;
	}
	/**
	 * @return the licenceAddress
	 */
	public String getLicenceAddress() {
		return licenceAddress;
	}
	/**
	 * @param licenceAddress the licenceAddress to set
	 */
	public void setLicenceAddress(String licenceAddress) {
		this.licenceAddress = licenceAddress;
	}
	/**
	 * @return the workBeginAm
	 */
	public String getWorkBeginAm() {
		return workBeginAm;
	}
	/**
	 * @param workBeginAm the workBeginAm to set
	 */
	public void setWorkBeginAm(String workBeginAm) {
		this.workBeginAm = workBeginAm;
	}
	/**
	 * @return the workBeginPm
	 */
	public String getWorkBeginPm() {
		return workBeginPm;
	}
	/**
	 * @param workBeginPm the workBeginPm to set
	 */
	public void setWorkBeginPm(String workBeginPm) {
		this.workBeginPm = workBeginPm;
	}
	/**
	 * @return the workEndAm
	 */
	public String getWorkEndAm() {
		return workEndAm;
	}
	/**
	 * @param workEndAm the workEndAm to set
	 */
	public void setWorkEndAm(String workEndAm) {
		this.workEndAm = workEndAm;
	}
	/**
	 * @return the workEndPm
	 */
	public String getWorkEndPm() {
		return workEndPm;
	}
	/**
	 * @param workEndPm the workEndPm to set
	 */
	public void setWorkEndPm(String workEndPm) {
		this.workEndPm = workEndPm;
	}
	/**
	 * @return the agentChargeStandard
	 */
	public String getAgentChargeStandard() {
		return agentChargeStandard;
	}
	/**
	 * @param agentChargeStandard the agentChargeStandard to set
	 */
	public void setAgentChargeStandard(String agentChargeStandard) {
		this.agentChargeStandard = agentChargeStandard;
	}
	/**
	 * @return the agentDrawRemark
	 */
	public String getAgentDrawRemark() {
		return agentDrawRemark;
	}
	/**
	 * @param agentDrawRemark the agentDrawRemark to set
	 */
	public void setAgentDrawRemark(String agentDrawRemark) {
		this.agentDrawRemark = agentDrawRemark;
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
	 * @return the documentsUniformPrice
	 */
	public int getDocumentsUniformPrice() {
		return documentsUniformPrice;
	}
	/**
	 * @param documentsUniformPrice the documentsUniformPrice to set
	 */
	public void setDocumentsUniformPrice(int documentsUniformPrice) {
		this.documentsUniformPrice = documentsUniformPrice;
	}
	/**
	 * @return the merchantIds
	 */
	public String getMerchantIds() {
		return merchantIds;
	}
	/**
	 * @param merchantIds the merchantIds to set
	 */
	public void setMerchantIds(String merchantIds) {
		this.merchantIds = merchantIds;
	}
	/**
	 * @return the merchantNames
	 */
	public String getMerchantNames() {
		return merchantNames;
	}
	/**
	 * @param merchantNames the merchantNames to set
	 */
	public void setMerchantNames(String merchantNames) {
		this.merchantNames = merchantNames;
	}
	/**
	 * @return the pledgeInternetPayment
	 */
	public int getPledgeInternetPayment() {
		return pledgeInternetPayment;
	}
	/**
	 * @param pledgeInternetPayment the pledgeInternetPayment to set
	 */
	public void setPledgeInternetPayment(int pledgeInternetPayment) {
		this.pledgeInternetPayment = pledgeInternetPayment;
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
	 * @return the documentsB2CPayment
	 */
	public int getDocumentsB2CPayment() {
		return documentsB2CPayment;
	}
	/**
	 * @param documentsB2CPayment the documentsB2CPayment to set
	 */
	public void setDocumentsB2CPayment(int documentsB2CPayment) {
		this.documentsB2CPayment = documentsB2CPayment;
	}
	/**
	 * @return the pledgeB2BPayment
	 */
	public int getPledgeB2BPayment() {
		return pledgeB2BPayment;
	}
	/**
	 * @param pledgeB2BPayment the pledgeB2BPayment to set
	 */
	public void setPledgeB2BPayment(int pledgeB2BPayment) {
		this.pledgeB2BPayment = pledgeB2BPayment;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the paymentMerchantIds
	 */
	public String getPaymentMerchantIds() {
		return paymentMerchantIds;
	}
	/**
	 * @param paymentMerchantIds the paymentMerchantIds to set
	 */
	public void setPaymentMerchantIds(String paymentMerchantIds) {
		this.paymentMerchantIds = paymentMerchantIds;
	}
	/**
	 * @return the paymentMerchantNames
	 */
	public String getPaymentMerchantNames() {
		return paymentMerchantNames;
	}
	/**
	 * @param paymentMerchantNames the paymentMerchantNames to set
	 */
	public void setPaymentMerchantNames(String paymentMerchantNames) {
		this.paymentMerchantNames = paymentMerchantNames;
	}
	/**
	 * @return the lastTransactionQuery
	 */
	public Timestamp getLastTransactionQuery() {
		return lastTransactionQuery;
	}
	/**
	 * @param lastTransactionQuery the lastTransactionQuery to set
	 */
	public void setLastTransactionQuery(Timestamp lastTransactionQuery) {
		this.lastTransactionQuery = lastTransactionQuery;
	}
}
