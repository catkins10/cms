package com.yuanluesoft.jeaf.dataimport.services.fzztb;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import com.yuanluesoft.bidding.biddingroom.pojo.BiddingRoom;
import com.yuanluesoft.bidding.biddingroom.pojo.BiddingRoomSchedule;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgentDraw;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPitchon;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPrivilege;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectSupplement;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectTender;
import com.yuanluesoft.bidding.project.service.BiddingProjectImportService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 项目数据导入服务
 * @author linchuan
 *
 */
public class ProjectImportServiceImpl implements BiddingProjectImportService {
	private DatabaseService databaseService;
	

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectImportService#importDate(java.lang.String)
	 */
	public void importData(String dateFilePath) throws ServiceException {
		String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=" + new File(dateFilePath).getAbsolutePath() + ";";
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  
			connection = DriverManager.getConnection(url, "", "");
			statement = connection.createStatement();
			try {
				importTenders(statement);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			try {
				importAgents(statement);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			try {
				importPitchons(statement);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			try {
				importSupplements(statement);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
			try {
				importRoomSchedule(statement);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
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
	 * 导入招标公告
	 * @param statement
	 */
	private void importTenders(Statement statement) throws Exception {
		ResultSet resultSet = statement.executeQuery("select tblBidAnnounce.*, tblPrices.Price from tblBidAnnounce left join tblPrices on tblBidAnnounce.priceId=tblPrices.actionid");
		while(resultSet.next()) {
			//创建项目
			BiddingProject project = new BiddingProject();
			project.setId(UUIDLongGenerator.generateId()); //ID
			project.setProjectName(getString(resultSet, "project")); //工程名称
			project.setProjectCategory(getString(resultSet, "BidBigClass")); //工程类别,房建工程/装修工程/监理工程/市政工程/专业工程/设计招标
			project.setProjectProcedure(getString(resultSet, "BidSmallClass")); //招标内容,施工/设计/监理等
			String centerName = getString(resultSet, "DealCenter");
			project.setCity(centerName==null ? "福州" : centerName.replaceAll("建设工程交易管理中心", "").replaceAll("建设工程交易中心", "").replaceAll("市", "").replaceAll("县", "").replaceAll("区", "")); //所属地区,福州/福清/长乐
			project.setProjectNumber(getString(resultSet, "BidNo")); //项目编号,榕市建安招2009001
			String biddingForm = getString(resultSet, "BiddingForm");
			project.setAgentEnable(biddingForm==null ? "是" : (biddingForm.startsWith("委托") ? "是" : "否")); //是否委托招标代理,是/否
			project.setBiddingMode(getString(resultSet, "BidClass")); //招标方式,公开招标/邀请招标
			project.setApprovalMode(getString(resultSet, "CheckMethod")); //资格审查方式,资格后审/资格预审
			project.setAgentMode("随机抽签"); //代理产生方式,随机抽签/直接指定
			project.setOwner(getString(resultSet, "BuildCompany")); //建设单位
			project.setOwnerType(null); //建设单位性质,全民
			project.setOwnerRepresentative(null); //建设单位法人代表
			project.setOwnerLinkman(null); //建设单位联系人
			project.setOwnerLinkmanIdCard(null); //建设单位联系人身份证
			project.setOwnerTel(null); //建设单位联系电话
			project.setOwnerFax(null); //建设单位传真
			project.setOwnerMail(null); //建设单位电子邮件
			project.setProjectAddress(getString(resultSet, "Address")); //建设地点
			project.setScale(getString(resultSet, "BuildScale")); //建设规模
			project.setCreated(resultSet.getTimestamp("SubTime")); //创建时间
			project.setRegistTime(project.getCreated()); //完成登记时间
			project.setRemark("从建设信息网导入"); //备注
			databaseService.saveRecord(project);
			//添加权限控制记录
			BiddingProjectPrivilege privilege = new BiddingProjectPrivilege();
			privilege.setId(UUIDLongGenerator.generateId()); //ID
			privilege.setRecordId(project.getId());
			privilege.setVisitorId(0);
			privilege.setAccessLevel(RecordControlService.ACCESS_LEVEL_READONLY);
			databaseService.saveRecord(privilege);
			
			//创建招标公告
			BiddingProjectTender tender = new BiddingProjectTender();
			tender.setId(UUIDLongGenerator.generateId()); //ID
			tender.setProjectId(project.getId()); //项目ID
			tender.setProjectName(project.getProjectName()); //项目名称
			tender.setBiddingMode(project.getBiddingMode()); //招标方式
			tender.setPublicBeginTime(project.getCreated()); //公示时间,resultSet.getTimestamp("ApplyTime")
			tender.setPublicEndTime(resultSet.getTimestamp("ApplyTime2")); //公示时间
			tender.setBody(getHTMLContent(resultSet, "BiddingContent")); //正文
			tender.setAgent(getString(resultSet, "AgentCompany")); //代理
			tender.setApprovalMode(getString(resultSet, "ApplyMethod")); //资格审查采用的方式
			tender.setControlPrice(Double.parseDouble(getString(resultSet, "Price")));
			tender.setRemark("从建设信息网导入"); //备注
			databaseService.saveRecord(tender);
		}
		resultSet.close();
	}
	
	/**
	 * 导入代理抽签公告和中选代理
	 * @param statement
	 */
	private void importAgents(Statement statement) throws Exception {
		ResultSet resultSet = statement.executeQuery("select * from tblAgentAnnounce");
		while(resultSet.next()) {
			//按项目名称查找项目
			String projectName = getString(resultSet, "project");
			String hql = "from BiddingProject BiddingProject where BiddingProject.projectName='" + JdbcUtils.resetQuot(projectName) + "'";
			BiddingProject project = (BiddingProject)databaseService.findRecordByHql(hql);
			if(project==null) {
				//创建项目
				project = new BiddingProject();
				project.setId(UUIDLongGenerator.generateId()); //ID
				project.setProjectName(projectName); //工程名称
				project.setProjectCategory(getString(resultSet, "BigClass")); //工程类别,房建工程/装修工程/监理工程/市政工程/专业工程/设计招标
				project.setAgentEnable("是"); //是否委托招标代理,是/否
				project.setBiddingMode(getString(resultSet, "AppBiddingForm").indexOf("公开")!=-1 ? "公开招标" : "邀请招标"); //招标方式,公开招标/邀请招标
				//project.setApprovalMode(getString(resultSet, "CheckMethod")); //资格审查方式,资格后审/资格预审
				project.setAgentMode("随机抽签"); //代理产生方式,随机抽签/直接指定
				project.setOwner(getString(resultSet, "Company")); //建设单位
				project.setOwnerType(null); //建设单位性质,全民
				project.setOwnerRepresentative(null); //建设单位法人代表
				project.setOwnerLinkman(null); //建设单位联系人
				project.setOwnerLinkmanIdCard(null); //建设单位联系人身份证
				project.setOwnerTel(null); //建设单位联系电话
				project.setOwnerFax(null); //建设单位传真
				project.setOwnerMail(null); //建设单位电子邮件
				//project.setProjectAddress(getString(resultSet, "Address")); //建设地点
				project.setScale(getString(resultSet, "BuildScale")); //建设规模
				project.setCreated(resultSet.getTimestamp("SubTime")); //创建时间
				project.setRegistTime(project.getCreated()); //完成登记时间
				project.setRemark("从建设信息网导入"); //备注
				databaseService.saveRecord(project);
				//添加权限控制记录
				BiddingProjectPrivilege privilege = new BiddingProjectPrivilege();
				privilege.setId(UUIDLongGenerator.generateId()); //ID
				privilege.setRecordId(project.getId());
				privilege.setVisitorId(0);
				privilege.setAccessLevel(RecordControlService.ACCESS_LEVEL_READONLY);
				databaseService.saveRecord(privilege);
			}
			
			//创建代理抽签公告
			BiddingProjectAgentDraw agentDraw = new BiddingProjectAgentDraw();
			agentDraw.setId(UUIDLongGenerator.generateId()); //ID
			agentDraw.setProjectId(project.getId()); //项目ID
			agentDraw.setProjectName(project.getProjectName()); //项目名称
			agentDraw.setBiddingMode(project.getBiddingMode()); //招标方式
			try {
				agentDraw.setPublicBeginTime(resultSet.getTimestamp("SubTime")); //公示时间
			}
			catch(Exception e) {
				agentDraw.setPublicBeginTime(project.getCreated()); //公示时间
			}
			//agentDraw.setDrawMode(drawMode); //代理申请方式,法定代表人或授权委托人携带书面申请书及身份证（到场核验）/无需申请
			agentDraw.setAgentLevel(getString(resultSet, "lotQua")); //代理资格条件,福州市省、市重点项目招标代理机构名录库/福州市招标代理机构名录库/福州市招标代理机构名册
			//agentDraw.setContent(content); //委托代理内容,勘察/设计/监理/施工/工程货物/附属/其它
			agentDraw.setDrawTime(resultSet.getTimestamp("lotDate")); //抽选时间
			agentDraw.setDrawAddress(getString(resultSet, "lotAddr")); //抽选地点
			//agentDraw.setMoney(getString(resultSet, "InvestScale")); //代理取费标准
			agentDraw.setMoney("招标代理费按照《福建省物价局转发国家计委关于印发招标代理服务收费管理暂行办法的通知》（闽价［2002］服610号）、造价咨询费按照《福建省物价局关于规范建设工程造价咨询服务收费有关问题的通知》（闽价[2002]房457号）规定的标准及允许的浮动率收取，本招标项目允许浮动率为20%。"); //代理取费标准
			agentDraw.setRemark("从建设信息网导入"); //备注
			databaseService.saveRecord(agentDraw);
			
			//创建中选代理记录
			String agentName = getString(resultSet, "lotCompany");
			if(agentName!=null && !agentName.equals("")) {
				BiddingProjectAgent agent = new BiddingProjectAgent();
				agent.setId(UUIDLongGenerator.generateId()); //ID
				agent.setProjectId(project.getId()); //项目ID
				agent.setProjectName(project.getProjectName()); //项目名称
				agent.setBiddingMode(project.getBiddingMode()); //招标方式
				agent.setPublicBeginTime(agentDraw.getPublicBeginTime()); //公示时间
				agent.setAgentName(agentName); //中选代理
				agent.setRemark("从建设信息网导入"); //备注
				databaseService.saveRecord(agent);
			}
		}
		resultSet.close();
	}
	
	/**
	 * 导入中标公示
	 * @param statement
	 */
	private void importPitchons(Statement statement) throws Exception {
		ResultSet resultSet = statement.executeQuery("select * from tblDetail");
		while(resultSet.next()) {
			//按项目名称查找项目
			String projectNumber = getString(resultSet, "BidNO");
			String hql = "from BiddingProject BiddingProject where BiddingProject.projectNumber='" + JdbcUtils.resetQuot(projectNumber) + "'";
			BiddingProject project = (BiddingProject)databaseService.findRecordByHql(hql);
			if(project==null) { // || !"1".equals(resultSet.getString("CheckOK"))) {
				continue;
			}
			BiddingProjectPitchon pitchon = new BiddingProjectPitchon();
			pitchon.setId(UUIDLongGenerator.generateId()); //ID
			pitchon.setProjectId(project.getId()); //项目ID
			pitchon.setProjectName(project.getProjectName()); //项目名称
			pitchon.setBiddingMode(project.getBiddingMode()); //招标方式
			pitchon.setPublicBeginTime(resultSet.getTimestamp("SubTime")); //公示时间
			pitchon.setProjectNumber(projectNumber); //项目编号
			pitchon.setBidopeningTime(resultSet.getTimestamp("CheckTime")); //开标时间
			pitchon.setAgent(getString(resultSet, "AgentCompany")); //招标代理机构
			pitchon.setSynch('1'); //是否同步过
			pitchon.setBody(getHTMLContent(resultSet, "Content")); //正文,没有固定模板时使用
			pitchon.setRemark("从建设信息网导入"); //备注
			databaseService.saveRecord(pitchon);
		}
		resultSet.close();
	}
	
	/**
	 * 导入补充通知
	 * @param statement
	 */
	private void importSupplements(Statement statement) throws Exception {
		ResultSet resultSet = statement.executeQuery("select * from tblFeedBack");
		while(resultSet.next()) {
			//按项目名称查找项目
			String projectNumber = getString(resultSet, "BidNO");
			String hql = "from BiddingProject BiddingProject where BiddingProject.projectNumber='" + JdbcUtils.resetQuot(projectNumber) + "'";
			BiddingProject project = (BiddingProject)databaseService.findRecordByHql(hql);
			if(project==null) {
				continue;
			}
			BiddingProjectSupplement supplement = new BiddingProjectSupplement();
			supplement.setId(UUIDLongGenerator.generateId()); //ID
			supplement.setProjectId(project.getId()); //项目ID
			supplement.setProjectName(project.getProjectName()); //项目名称
			supplement.setBiddingMode(project.getBiddingMode()); //招标方式
			supplement.setPublicBeginTime(resultSet.getTimestamp("SubTime")); //公示时间
			supplement.setBody(getHTMLContent(resultSet, "Content")); //正文,没有固定模板时使用
			supplement.setRemark("从建设信息网导入"); //备注
			databaseService.saveRecord(supplement);
		}
		resultSet.close();
	}
	
	/**
	 * 导入会场安排
	 * @param statement
	 * @throws Exception
	 */
	private void importRoomSchedule(Statement statement) throws Exception {
		ResultSet resultSet = statement.executeQuery("select * from tblmeeting");
		while(resultSet.next()) {
			//按项目名称查找项目
			String projectName = getString(resultSet, "project");
			String hql = "from BiddingProject BiddingProject where BiddingProject.projectName='" + JdbcUtils.resetQuot(projectName) + "'";
			BiddingProject project = (BiddingProject)databaseService.findRecordByHql(hql);
			if(project==null) {
				continue;
			}
			BiddingRoomSchedule schedule = new BiddingRoomSchedule();
			schedule.setId(UUIDLongGenerator.generateId()); //ID
			schedule.setProjectId(project.getId()); //项目ID
			schedule.setProjectName(project.getProjectName()); //项目名称
			schedule.setBiddingMode(project.getBiddingMode()); //招标方式
			schedule.setPublicBeginTime(resultSet.getTimestamp("SubTime")); //公示时间
			if(schedule.getPublicBeginTime()==null) {
				schedule.setPublicBeginTime(DateTimeUtils.now());
			}
			schedule.setRoomType(getString(resultSet, "Content")); //开标/评标
			hql = "from BiddingRoom BiddingRoom where BiddingRoom.name='第" +  getString(resultSet, "Room") + "开标室'";
			BiddingRoom room = (BiddingRoom)databaseService.findRecordByHql(hql);
			schedule.setRoomId(room.getId()); //开标室ID
			schedule.setRoomName(room.getName()); //开标室名称
			schedule.setBeginTime(resultSet.getTimestamp("MeetingDate")); //使用开始时间
			schedule.setEndTime(DateTimeUtils.set(schedule.getBeginTime(), Calendar.HOUR_OF_DAY, 12)); //使用结束时间
			
			schedule.setRemark("从建设信息网导入"); //备注
			databaseService.saveRecord(schedule);
		}
		resultSet.close();
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
	 * 获取HTML内容
	 * @param rs
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	private String getHTMLContent(ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName);
		if(value==null) {
			return null;
		}
		return value.replaceAll("<\\?xml([^>]*)>", "");
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
