package com.yuanluesoft.jeaf.dataimport.services.fzztb;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterpriseCert;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprisePrivilege;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingJobholder;
import com.yuanluesoft.bidding.enterprise.services.EnterpriseImportService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class EnterpriseImportServiceImpl implements EnterpriseImportService {
	private DatabaseService databaseService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.services.EnterpriseImportService#importData(java.lang.String)
	 */
	public void importData(String dateFilePath) throws ServiceException {
		String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=" + new File(dateFilePath).getAbsolutePath() + ";";
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  
			connection = DriverManager.getConnection(url, "", "");
			statement = connection.createStatement();
			importEnterprises(statement);
			importEnterpriseCerts(statement);
			importJobholders(statement);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage()); 
		}
		finally {
			try {
				statement.close();
			}
			catch(Exception ex) {

			}
			try {
				connection.close();
			}
			catch(Exception ex) {

			}
		}
	}
	
	/**
	 * 导入企业
	 * @param statement
	 */
	private void importEnterprises(Statement statement) throws Exception {
		ResultSet rs = statement.executeQuery("select * from tblEnterprise where ClassName<>'建设单位'");
		while(rs.next()) {
			String name = getString(rs, "CompanyName");
			//判断企业是否已经存在
			if(getDatabaseService().findRecordByHql("from BiddingEnterprise BiddingEnterprise where BiddingEnterprise.name='" + JdbcUtils.resetQuot(name) + "'")!=null) {
				continue;
			}
			//创建企业
			BiddingEnterprise enterprise = new BiddingEnterprise();
			enterprise.setId(UUIDLongGenerator.generateId()); //ID
			enterprise.setArea(name.substring(0, 2)); //所在地区
			enterprise.setName(name); //企业名称
			enterprise.setBusinessLicence(getString(rs, "RegNum")); //营业执照号码
			enterprise.setStatisticsLicence(getString(rs, "CompanySC")); //单位统计证
			enterprise.setSafeLicence(getString(rs, "SecurityNO")); //安全许可证
			try {
				String regTime = getString(rs, "RegTime");
				enterprise.setRegistDate(regTime==null || regTime.equals("") ? null : DateTimeUtils.parseDate(regTime, null)); //注册时间
			}
			catch(Exception e) {
				
			}
			enterprise.setRegisteredCapital(rs.getInt("RegCapital")); //注册资金（万元）
			enterprise.setKind(getString(rs, "EnterPrisePro")); //企业性质,国有
			enterprise.setLegalRepresentative(getString(rs, "LRMan")); //法定代表人
			enterprise.setRepresentativeIdNumber(getString(rs, "LRNO")); //法定代表人身份证号码
			enterprise.setRepresentativeTel(getString(rs, "LRTel")); //法定代表人联系电话
			enterprise.setManager(getString(rs, "Manager")); //企业经理
			enterprise.setManagerIdNumber(getString(rs, "ManagerNO")); //企业经理身份证号码
			enterprise.setManagerTel(getString(rs, "ManagerTel")); //企业经理联系电话
			enterprise.setTechnicalLeader(getString(rs, "TechResponser")); //技术负责人
			enterprise.setTechnicalLeaderIdNumner(getString(rs, "TechResponserNO")); //技术负责人身份证号码
			enterprise.setTechnicalLeaderTel(getString(rs, "TechResponserTel")); //技术负责人电话
			enterprise.setAddress(getString(rs, "Address")); //通讯地址
			enterprise.setPostalcode(getString(rs, "zipcode")); //邮政编码
			enterprise.setLinkman(getString(rs, "Contactor")); //联系人
			enterprise.setLinkmanIdNumber(null); //联系人身份证号码
			enterprise.setTel(getString(rs, "ContactTel")); //联系电话
			enterprise.setMobile(null); //手机
			enterprise.setFax(getString(rs, "Fax")); //传真
			enterprise.setEmail(getString(rs, "Email")); //电子邮件
			enterprise.setWebsite(getString(rs, "HomePage")); //企业主页
			enterprise.setBank(getString(rs, "Bank")); //开户银行
			enterprise.setAccount(getString(rs, "Account")); //开户帐号
			enterprise.setIntroduction(getString(rs, "Scope")); //经营范围
			enterprise.setCreated(rs.getTimestamp("SubTime")); //登记时间
			enterprise.setIsValid('1'); //企业信息是否生效,完成注册完成后置1
			enterprise.setIsNullify('0'); //是否注销审批记录
			enterprise.setIsAlter('0'); //是否变更审批记录
			String remark = getString(rs, "Memo");
			enterprise.setRemark(remark==null ? null : remark.substring(0, Math.min(remark.length(), 100))); //备注
			databaseService.saveRecord(enterprise);
			//添加权限控制记录
			BiddingEnterprisePrivilege privilege = new BiddingEnterprisePrivilege();
			privilege.setId(UUIDLongGenerator.generateId()); //ID
			privilege.setRecordId(enterprise.getId());
			privilege.setVisitorId(0);
			databaseService.saveRecord(privilege);
		}
		rs.close();
	}
	
	/**
	 * 导入企业资质
	 * @param statement
	 * @throws Exception
	 */
	private void importEnterpriseCerts(Statement statement) throws Exception {
		//招标代理
		ResultSet rs = statement.executeQuery("select * from tblQuaAgent");
		while(rs.next()) {
			String enterpriseName = getString(rs, "CompanyName");
			//判断企业是否已经存在
			BiddingEnterprise enterprise = (BiddingEnterprise)getDatabaseService().findRecordByHql("from BiddingEnterprise BiddingEnterprise where BiddingEnterprise.name='" + JdbcUtils.resetQuot(enterpriseName) + "'");
			if(enterprise==null) {
				continue;
			}
			//创建资质
			BiddingEnterpriseCert cert = new BiddingEnterpriseCert();
			cert.setId(UUIDLongGenerator.generateId()); //ID
			cert.setEnterpriseId(enterprise.getId()); //企业ID
			cert.setType("招标代理"); //资质类型,施工/监理/招标代理/房地产
			cert.setCertificateNumber(getString(rs, "QuaCardNO")); //资质证书编号
			cert.setLevel(getString(rs, "QuaLevel")); //资质等级
			cert.setApprovalDate(rs.getDate("GetQuaTime")); //取得资质时间
			cert.setApprovalNumber(getString(rs, "FileNO")); //批准文号
			cert.setRemark(getString(rs, "Memo")); //备注
			databaseService.saveRecord(cert);
		}
		rs.close();

		//监理企业
		rs = statement.executeQuery("select * from tblQuaMonitor");
		while(rs.next()) {
			String enterpriseName = getString(rs, "CompanyName");
			//判断企业是否已经存在
			BiddingEnterprise enterprise = (BiddingEnterprise)getDatabaseService().findRecordByHql("from BiddingEnterprise BiddingEnterprise where BiddingEnterprise.name='" + JdbcUtils.resetQuot(enterpriseName) + "'");
			if(enterprise==null) {
				continue;
			}
			//创建资质
			BiddingEnterpriseCert cert = new BiddingEnterpriseCert();
			cert.setId(UUIDLongGenerator.generateId()); //ID
			cert.setEnterpriseId(enterprise.getId()); //企业ID
			cert.setType("监理企业"); //资质类型,施工/监理/招标代理/房地产
			cert.setCertificateNumber(getString(rs, "QuaCardNO")); //资质证书编号
			cert.setLevel(getString(rs, "MILevel")); //资质等级
			cert.setApprovalDate(rs.getDate("GetQuaTime")); //取得资质时间
			cert.setApprovalNumber(getString(rs, "FileNO")); //批准文号
			cert.setRemark(getString(rs, "Memo")); //备注
			databaseService.saveRecord(cert);
		}
		rs.close();
		
		//施工企业
		rs = statement.executeQuery("select * from tblQuaConstruct");
		while(rs.next()) {
			String enterpriseName = getString(rs, "CompanyName");
			//判断企业是否已经存在
			BiddingEnterprise enterprise = (BiddingEnterprise)getDatabaseService().findRecordByHql("from BiddingEnterprise BiddingEnterprise where BiddingEnterprise.name='" + JdbcUtils.resetQuot(enterpriseName) + "'");
			if(enterprise==null) {
				continue;
			}
			//创建资质
			BiddingEnterpriseCert cert = new BiddingEnterpriseCert();
			cert.setId(UUIDLongGenerator.generateId()); //ID
			cert.setEnterpriseId(enterprise.getId()); //企业ID
			cert.setType("施工企业"); //资质类型,施工/监理/招标代理/房地产
			cert.setCertificateNumber(getString(rs, "QuaCardNO")); //资质证书编号
			cert.setLevel(getString(rs, "MILevel")); //资质等级
			//cert.setApprovalDate(rs.getDate("GetQuaTime")); //取得资质时间
			cert.setApprovalNumber(getString(rs, "FileNO")); //批准文号
			cert.setRemark(getString(rs, "Memo")); //备注
			databaseService.saveRecord(cert);
		}
		rs.close();
	}
	
	/**
	 * 导入从业人员
	 * @param statement
	 * @throws Exception
	 */
	private void importJobholders(Statement statement) throws Exception {
		//项目经理
		ResultSet rs = statement.executeQuery("select * from tblProManager");
		while(rs.next()) {
			String enterpriseName = getString(rs, "Company");
			//判断企业是否已经存在
			BiddingEnterprise enterprise = (BiddingEnterprise)getDatabaseService().findRecordByHql("from BiddingEnterprise BiddingEnterprise where BiddingEnterprise.name='" + JdbcUtils.resetQuot(enterpriseName) + "'");
			if(enterprise==null) {
				continue;
			}
			//创建项目经理
			BiddingJobholder jobholder = new BiddingJobholder();
			jobholder.setId(UUIDLongGenerator.generateId()); //ID
			jobholder.setEnterpriseId(enterprise.getId()); //工作单位ID
			jobholder.setEnterpriseName(enterprise.getName()); //工作单位
			jobholder.setName(getString(rs, "realname")); //姓名
			jobholder.setCategory("项目经理"); //类别,项目经理/注册结构师/注册建筑师/注册监理师/代理监理师
			jobholder.setSex("男".equals(getString(rs, "Sex")) ? 'M' : 'F'); //性别,M/F
			jobholder.setIdentityCard(getString(rs, "IDnumber")); //身份证号码
			jobholder.setSchool(getString(rs, "College")); //毕业（培训）院校
			jobholder.setEducation(getString(rs, "degree")); //学历
			jobholder.setSchoolProfessional(null); //毕业专业
			jobholder.setProfessional(getString(rs, "major")); //专业
			jobholder.setSecondProfessional(getString(rs, "major2")); //第二专业
			jobholder.setOtherProfessional(null); //其它专业
			jobholder.setDuty(null); //职务
			jobholder.setJob(getString(rs, "title")); //职称
			jobholder.setQualification(getString(rs, "level")); //资质等级/人员类别
			jobholder.setCertificateNumber(getString(rs, "CardNumber")); //证书号码/培训证号
			jobholder.setCertificateCreated(rs.getDate("SendCardDate")); //发证时间
			jobholder.setCertificateLimit(null); //有效期
			jobholder.setTel(getString(rs, "ContactTel")); //电话
			jobholder.setMobile(getString(rs, "ContactTel2")); //手机
			jobholder.setAddress(getString(rs, "Address")); //通讯地址
			jobholder.setPostalCode(getString(rs, "zipcode")); //邮编
			String remark = getString(rs, "Memo");
			jobholder.setRemark(remark==null ? null : remark.substring(0, Math.min(remark.length(), 100))); //备注
			databaseService.saveRecord(jobholder);
		}
		rs.close();

		//注册监理师
		rs = statement.executeQuery("select * from tblRegStaff");
		while(rs.next()) {
			String enterpriseName = getString(rs, "Company");
			//判断企业是否已经存在
			BiddingEnterprise enterprise = (BiddingEnterprise)getDatabaseService().findRecordByHql("from BiddingEnterprise BiddingEnterprise where BiddingEnterprise.name='" + JdbcUtils.resetQuot(enterpriseName) + "'");
			if(enterprise==null) {
				continue;
			}
			//创建注册监理师
			BiddingJobholder jobholder = new BiddingJobholder();
			jobholder.setId(UUIDLongGenerator.generateId()); //ID
			jobholder.setEnterpriseId(enterprise.getId()); //工作单位ID
			jobholder.setEnterpriseName(enterprise.getName()); //工作单位
			jobholder.setName(getString(rs, "realname")); //姓名
			jobholder.setCategory("注册监理师"); //类别,项目经理/注册结构师/注册建筑师/注册监理师/代理监理师
			jobholder.setSex("男".equals(getString(rs, "Sex")) ? 'M' : 'F'); //性别,M/F
			jobholder.setIdentityCard(getString(rs, "IDnumber")); //身份证号码
			jobholder.setSchool(null); //毕业（培训）院校
			jobholder.setEducation(getString(rs, "degree")); //学历
			jobholder.setSchoolProfessional(null); //毕业专业
			jobholder.setProfessional(getString(rs, "major")); //专业
			jobholder.setSecondProfessional(getString(rs, "major2")); //第二专业
			jobholder.setOtherProfessional(null); //其它专业
			jobholder.setDuty(getString(rs, "place")); //职务
			jobholder.setJob(getString(rs, "title")); //职称
			jobholder.setQualification(getString(rs, "RegStaffClass")); //资质等级/人员类别
			jobholder.setCertificateNumber(getString(rs, "QCardNO")); //证书号码/培训证号
			jobholder.setCertificateCreated(null); //发证时间
			jobholder.setCertificateLimit(null); //有效期
			jobholder.setTel(getString(rs, "HomeTel")); //电话
			jobholder.setMobile(getString(rs, "Mobile")); //手机
			jobholder.setAddress(getString(rs, "Address")); //通讯地址
			jobholder.setPostalCode(getString(rs, "zipcode")); //邮编
			String remark = getString(rs, "Memo");
			jobholder.setRemark(remark==null ? null : remark.substring(0, Math.min(remark.length(), 100))); //备注
			databaseService.saveRecord(jobholder);
		}
		rs.close();
		
		//注册造价师
		rs = statement.executeQuery("select * from tblBudgetStaff");
		while(rs.next()) {
			String enterpriseName = getString(rs, "Company");
			//判断企业是否已经存在
			BiddingEnterprise enterprise = (BiddingEnterprise)getDatabaseService().findRecordByHql("from BiddingEnterprise BiddingEnterprise where BiddingEnterprise.name='" + JdbcUtils.resetQuot(enterpriseName) + "'");
			if(enterprise==null) {
				continue;
			}
			//创建注册造价师
			BiddingJobholder jobholder = new BiddingJobholder();
			jobholder.setId(UUIDLongGenerator.generateId()); //ID
			jobholder.setEnterpriseId(enterprise.getId()); //工作单位ID
			jobholder.setEnterpriseName(enterprise.getName()); //工作单位
			jobholder.setName(getString(rs, "realname")); //姓名
			jobholder.setCategory("注册造价师"); //类别,项目经理/注册结构师/注册建筑师/注册监理师/代理监理师
			jobholder.setSex("男".equals(getString(rs, "Sex")) ? 'M' : 'F'); //性别,M/F
			jobholder.setIdentityCard(getString(rs, "IDnumber")); //身份证号码
			jobholder.setSchool(null); //毕业（培训）院校
			jobholder.setEducation(getString(rs, "degree")); //学历
			jobholder.setSchoolProfessional(null); //毕业专业
			jobholder.setProfessional(getString(rs, "major")); //专业
			jobholder.setSecondProfessional(null); //第二专业
			jobholder.setOtherProfessional(null); //其它专业
			jobholder.setDuty(getString(rs, "place")); //职务
			jobholder.setJob(getString(rs, "title")); //职称
			jobholder.setQualification(null); //资质等级/人员类别
			jobholder.setCertificateNumber(getString(rs, "QCardNO")); //证书号码/培训证号
			jobholder.setCertificateCreated(null); //发证时间
			jobholder.setCertificateLimit(null); //有效期
			jobholder.setTel(getString(rs, "HomeTel")); //电话
			jobholder.setMobile(getString(rs, "Mobile")); //手机
			jobholder.setAddress(getString(rs, "HomeAddr")); //通讯地址
			jobholder.setPostalCode(null); //邮编
			String remark = getString(rs, "Memo");
			jobholder.setRemark(remark==null ? null : remark.substring(0, Math.min(remark.length(), 100))); //备注
			databaseService.saveRecord(jobholder);
		}
		rs.close();
		
		//五大员
		rs = statement.executeQuery("select * from tblStaffPosition left join tblStaff on tblStaffPosition.staffId=tblStaff.pkid where not realname is null");
		while(rs.next()) {
			String enterpriseName = getString(rs, "Company");
			//判断企业是否已经存在
			BiddingEnterprise enterprise = (BiddingEnterprise)getDatabaseService().findRecordByHql("from BiddingEnterprise BiddingEnterprise where BiddingEnterprise.name='" + JdbcUtils.resetQuot(enterpriseName) + "'");
			if(enterprise==null) {
				continue;
			}
			//创建注册造价师
			BiddingJobholder jobholder = new BiddingJobholder();
			jobholder.setId(UUIDLongGenerator.generateId()); //ID
			jobholder.setEnterpriseId(enterprise.getId()); //工作单位ID
			jobholder.setEnterpriseName(enterprise.getName()); //工作单位
			jobholder.setName(getString(rs, "realname")); //姓名
			jobholder.setCategory("五大员"); //类别,项目经理/注册结构师/注册建筑师/注册监理师/代理监理师
			jobholder.setSex("男".equals(getString(rs, "Sex")) ? 'M' : 'F'); //性别,M/F
			jobholder.setIdentityCard(getString(rs, "IDnumber")); //身份证号码
			jobholder.setSchool(getString(rs, "College")); //毕业（培训）院校
			jobholder.setEducation(getString(rs, "degree")); //学历
			jobholder.setSchoolProfessional(null); //毕业专业
			jobholder.setProfessional(getString(rs, "major")); //专业
			jobholder.setSecondProfessional(null); //第二专业
			jobholder.setOtherProfessional(null); //其它专业
			jobholder.setDuty(getString(rs, "Duty")); //职务
			jobholder.setJob(getString(rs, "title")); //职称
			jobholder.setQualification(getString(rs, "Position")); //资质等级/人员类别
			jobholder.setCertificateNumber(getString(rs, "CerNO")); //证书号码/培训证号
			jobholder.setCertificateCreated(rs.getDate("StartDate")); //发证时间
			jobholder.setCertificateLimit(rs.getDate("EndDate")); //有效期
			jobholder.setTel(getString(rs, "ContactTel")); //电话
			jobholder.setMobile(getString(rs, "Mobile")); //手机
			jobholder.setAddress(getString(rs, "Address")); //通讯地址
			jobholder.setPostalCode(getString(rs, "zipcode")); //邮编
			String remark = getString(rs, "Memo");
			jobholder.setRemark(remark==null ? null : remark.substring(0, Math.min(remark.length(), 100))); //备注
			databaseService.saveRecord(jobholder);
		}
		rs.close();
	}
	
	/**
	 * 获取字符串值
	 * @param rs
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	private String getString(ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName);
		return value==null ? null : value.trim();
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}
