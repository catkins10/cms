package com.yuanluesoft.credit.enterprise.apply.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author zyh
 *
 */
public class Apply extends ActionForm {
	private long bankId;//拟申请银行ID
	private String bankName;//拟申请银行名称
	
	private String applyPerson;//申请人
	private String nature;//企业性质
	private String industry;//行业类别
	private String level;//信用等级
	private String companyAddr;//公司地址
	private String code;//三证合一信用代码
	private String licenseNo;//营业执照号
	private String orgCode;//组织机构代码
	private String legalPerson;//法定代表人
	private String phone;//联系电话
	private String idCard;//身份证号码
	private double totalMoney;//总资产（万元）
	private double cleanMoney;//净资产(万元)
	private double totalOwe;//总负债(万元)
	private double nowOwe;//目前结欠贷款(万元)
	private double saleMoney;//销售收入（万元）
	private double scale;//经营规模（万元）
	private double applyMoney;//申请金额
	private int borrowNature;//借款性质（0:新增，1:还旧借新，2:借新还旧，3:转换主体）
	private int borrowType;//贷款方式(0:保证、1:抵押)
	private String applyPurpose;//借款用途
	private Timestamp applyStart;//借款期限自
	private Timestamp applyEnd;//借款期限至
	private String payment;//还款来源
	private String payMethod;//还款方式
	private String guarantorf;//保证人名称1
	private String guarantorLevelf;//保证人信用等级1
	private String guarantors;//保证人名称2
	private String guarantorLevels;//保证人信用等级2
	private String guarantort;//保证人名称3
	private String guarantorLevelt;//保证人信用等级3
	private String guarantorfo;//保证人名称4
	private String guarantorLevelfo;//保证人信用等级4
	private String mortgager;//抵押人（出质人）名称
	private String collateral;//抵押物（质物）名称
	private String collateralAddr;//抵押物（质物）具体地理位置
	private String collateralDetail;//抵押物（质物）具体情况（包括面积、评估价、是否出租等）
	private String applyReason;//申    请    理    由
	private int status;//状态：0：未处理，1：已处理
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public Timestamp getApplyEnd() {
		return applyEnd;
	}
	public void setApplyEnd(Timestamp applyEnd) {
		this.applyEnd = applyEnd;
	}
	public String getApplyPerson() {
		return applyPerson;
	}
	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}
	public String getApplyPurpose() {
		return applyPurpose;
	}
	public void setApplyPurpose(String applyPurpose) {
		this.applyPurpose = applyPurpose;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public Timestamp getApplyStart() {
		return applyStart;
	}
	public void setApplyStart(Timestamp applyStart) {
		this.applyStart = applyStart;
	}
	public long getBankId() {
		return bankId;
	}
	public void setBankId(long bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public int getBorrowNature() {
		return borrowNature;
	}
	public void setBorrowNature(int borrowNature) {
		this.borrowNature = borrowNature;
	}
	public int getBorrowType() {
		return borrowType;
	}
	public void setBorrowType(int borrowType) {
		this.borrowType = borrowType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCollateral() {
		return collateral;
	}
	public void setCollateral(String collateral) {
		this.collateral = collateral;
	}
	public String getCollateralAddr() {
		return collateralAddr;
	}
	public void setCollateralAddr(String collateralAddr) {
		this.collateralAddr = collateralAddr;
	}
	public String getCollateralDetail() {
		return collateralDetail;
	}
	public void setCollateralDetail(String collateralDetail) {
		this.collateralDetail = collateralDetail;
	}
	public String getCompanyAddr() {
		return companyAddr;
	}
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public String getGuarantorf() {
		return guarantorf;
	}
	public void setGuarantorf(String guarantorf) {
		this.guarantorf = guarantorf;
	}
	public String getGuarantorfo() {
		return guarantorfo;
	}
	public void setGuarantorfo(String guarantorfo) {
		this.guarantorfo = guarantorfo;
	}
	public String getGuarantorLevelf() {
		return guarantorLevelf;
	}
	public void setGuarantorLevelf(String guarantorLevelf) {
		this.guarantorLevelf = guarantorLevelf;
	}
	public String getGuarantorLevelfo() {
		return guarantorLevelfo;
	}
	public void setGuarantorLevelfo(String guarantorLevelfo) {
		this.guarantorLevelfo = guarantorLevelfo;
	}
	public String getGuarantorLevels() {
		return guarantorLevels;
	}
	public void setGuarantorLevels(String guarantorLevels) {
		this.guarantorLevels = guarantorLevels;
	}
	public String getGuarantorLevelt() {
		return guarantorLevelt;
	}
	public void setGuarantorLevelt(String guarantorLevelt) {
		this.guarantorLevelt = guarantorLevelt;
	}
	public String getGuarantors() {
		return guarantors;
	}
	public void setGuarantors(String guarantors) {
		this.guarantors = guarantors;
	}
	public String getGuarantort() {
		return guarantort;
	}
	public void setGuarantort(String guarantort) {
		this.guarantort = guarantort;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getMortgager() {
		return mortgager;
	}
	public void setMortgager(String mortgager) {
		this.mortgager = mortgager;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getCleanMoney() {
		return cleanMoney;
	}
	public void setCleanMoney(double cleanMoney) {
		this.cleanMoney = cleanMoney;
	}
	public double getNowOwe() {
		return nowOwe;
	}
	public void setNowOwe(double nowOwe) {
		this.nowOwe = nowOwe;
	}
	public double getSaleMoney() {
		return saleMoney;
	}
	public void setSaleMoney(double saleMoney) {
		this.saleMoney = saleMoney;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public double getTotalOwe() {
		return totalOwe;
	}
	public void setTotalOwe(double totalOwe) {
		this.totalOwe = totalOwe;
	}
	public double getApplyMoney() {
		return applyMoney;
	}
	public void setApplyMoney(double applyMoney) {
		this.applyMoney = applyMoney;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
	
	
	
	
}