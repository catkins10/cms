package com.yuanluesoft.bidding.project.report.service.spring;

import java.sql.Date;
import java.text.Collator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectCity;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPlan;
import com.yuanluesoft.bidding.project.report.service.BiddingProjectReportService;
import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.report.excel.ExcelReportCallback;
import com.yuanluesoft.jeaf.report.excel.ExcelReportService;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReport;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReportData;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class BiddingProjectReportServiceImpl implements BiddingProjectReportService {
	private DatabaseService databaseService; //数据库访问
	private BiddingProjectParameterService biddingProjectParameterService; //参数服务
	private BiddingProjectService biddingProjectService; //招标项目服务
	private ExcelReportService excelReportService; //电子表格报表服务
	private String agentSettleDate; //代理费用结算时间,原来的报表是按报名时间来结算的,这样会造成投标信息泄密,现已调整为结算开标后的项目,故增加本字段,避免多次结算
	private boolean totalAfterBidopening = false; //开标后统计,避免在开标前泄漏报名情况

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.service.ProjectService#writeProjectReport(java.sql.Date, java.sql.Date, javax.servlet.http.HttpServletResponse)
	 */
	public void writeProjectReport(Date beginDate, Date endDate, HttpServletResponse response) throws ServiceException {
		//统计
		String hql = "select BiddingProject.city, BiddingProject.projectCategory, BiddingProject.projectProcedure, BiddingProject.biddingMode, count(BiddingProject.id), sum(BiddingProject.area), sum(BiddingProjectTender.controlPrice), sum(BiddingProjectPitchon.pitchonMoney)" +
					 " from BiddingProjectPitchon BiddingProjectPitchon, BiddingProject BiddingProject, BiddingProjectTender BiddingProjectTender" +
					 " where BiddingProject.id=BiddingProjectPitchon.projectId" +
					 " and BiddingProjectTender.projectId=BiddingProjectPitchon.projectId" +
					 (beginDate==null ? "" : " and BiddingProjectPitchon.publicBeginTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")") +
					 (endDate==null ? "" : " and BiddingProjectPitchon.publicBeginTime<=DATE(" + DateTimeUtils.formatDate(endDate, null) + ")") +
					 " group by BiddingProject.city, BiddingProject.projectCategory, BiddingProject.projectProcedure, BiddingProject.biddingMode";
		final List totals = databaseService.findRecordsByHql(hql);
		
		//报表回调
		ExcelReportCallback reportCallback = new ExcelReportCallback() {
			private String category = null; //工程类别
			private String projectProcedure = null; //招标内容
	
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReport(java.lang.String)
			 */
			public ExcelReport getExcelReport(String sheetName) {
				return new ExcelReport();
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReportData(int, java.lang.String[])
			 */
			public ExcelReportData getExcelReportData(int rowNumner, String[] rowContents) {
				if(rowNumner<2) {
					return null;
				}
				ExcelReportData projectTotalData = new ExcelReportData();
				if(totals==null || totals.isEmpty()) {
					return projectTotalData;
				}
				if(rowContents[0]!=null && !rowContents[0].isEmpty()) {
					category = rowContents[0];
				}
				if(rowContents[1]!=null && !rowContents[1].isEmpty()) {
					projectProcedure = rowContents[1];
				}
				int projectCount = 0; //工程个数
				double biddingRate = 1; //应招工程招标率
				double area = 0; //建筑面积
				double controlPrice = 0; //控制价	
				double pitchonMoney = 0; //中标价
				double reduceRate = 0; //降低率
				String city = "八县".equals(category) ? "八县" : ("合计".equals(category) ? null : "福州");
				String projectCategory = "八县".equals(category) || "合计".equals(category) ? "建安,市政" : category;
				projectProcedure = "合计".equals(category) ? null : projectProcedure; //招标内容
				String biddingMode = "合计".equals(rowContents[2]) ? null : rowContents[2] + "招标";
				for(Iterator iterator = totals.iterator(); iterator.hasNext();) {
					Object[] values = (Object[])iterator.next();
					//地区过滤
					if("八县".equals(city)) {
						if(values[0].equals("福州")) {
							continue;
						}
					}
					else if(city!=null && !city.equals(values[0])) {
						continue;
					}
					//项目类型过滤
					if(projectCategory!=null && !projectCategory.isEmpty() && projectCategory.indexOf((String)values[1])==-1) {
						continue;
					}
					//招标内容过滤
					if(projectProcedure!=null && !projectProcedure.isEmpty() && projectProcedure.indexOf((String)values[2])==-1) {
						continue;
					}
					//招标方式过滤
					if(biddingMode!=null && !biddingMode.equals(values[3])) {
						continue;
					}
					projectCount += ((Number)values[4]).intValue(); //工程个数
					area += ((Number)values[5]).doubleValue(); //建筑面积
					controlPrice += ((Number)values[6]).doubleValue(); //控制价	
					pitchonMoney += ((Number)values[7]).doubleValue(); //中标价
				}
				reduceRate = controlPrice==0 ? 0 : (pitchonMoney - controlPrice) / controlPrice; //降低率
				//设置属性
				projectTotalData.setPropertyValue("projectCount", new Integer(projectCount)); //工程个数
				projectTotalData.setPropertyValue("biddingRate", new Double(biddingRate)); //应招工程招标率
				projectTotalData.setPropertyValue("", new Double(area)); //建筑面积
				projectTotalData.setPropertyValue("", new Double(controlPrice)); //控制价	
				projectTotalData.setPropertyValue("", new Double(pitchonMoney)); //中标价
				projectTotalData.setPropertyValue("", new Double(reduceRate)); //降低率
				return projectTotalData;
			}
		};
		excelReportService.writeExcelReport(Environment.getWebinfPath() + "bidding/project/report/template/招标项目统计报表(福州).xls", "招标项目统计报表.xls", response, reportCallback);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.report.service.BiddingProjectReportService#writeAgentSalesReport(java.sql.Date, java.sql.Date, long, java.lang.String, java.lang.String[], javax.servlet.http.HttpServletResponse)
	 */
	public void writeAgentSalesReport(final Date beginDate, final Date endDate, final long agentId, final String agentName, String[] cities, HttpServletResponse response) throws ServiceException {
		String templateFile = Environment.getWebinfPath() + "bidding/project/report/template/";
		String outputName = "标书出售汇总表";
		if(agentId==0) { //没有指定代理
			templateFile += "标书出售汇总表(所有代理).xls";
		}
		else { //指定代理
			templateFile += "标书出售汇总表(单个代理).xls";
			outputName += "(" + agentName + ")";
		}
		outputName += ".xls";
		String hql;
		if(totalAfterBidopening) { //开标后统计
			//获取报名列表
			hql = "select BiddingSignUp" +
				  " from BiddingSignUp BiddingSignUp, BiddingProject BiddingProject, BiddingRoomSchedule BiddingRoomSchedule" + (agentId==0 ? "" : ", BiddingProjectAgent BiddingProjectAgent") +
				  " where BiddingProject.id=BiddingSignUp.projectId" +
				  " and BiddingRoomSchedule.projectId=BiddingProject.id" +
				  (agentId==0 ? "" : " and BiddingProjectAgent.projectId=BiddingProject.id and BiddingProjectAgent.agentId=" + agentId) + //代理
				  " and BiddingProject.city in ('" + ListUtils.join(cities, "','", false) + "')" + //地区
				  " and BiddingRoomSchedule.roomType='开标'" + //开标室安排
				  " and BiddingRoomSchedule.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")" + //开标时间已经过了
				  " and not BiddingSignUp.paymentTime is null" + //标书购买时间不为空
				  " and BiddingRoomSchedule.beginTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" + //开标时间大等于开始时间
				  " and BiddingRoomSchedule.beginTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")" + //开标时间小于结束时间
				  " order by " + (agentId==0 ? "" : "BiddingProjectAgent.agentId,") + "BiddingProject.projectName, BiddingSignUp.paymentTime";
		}
		else { //不是开标后统计
			//获取报名列表
			hql = "select BiddingSignUp" +
				  " from BiddingSignUp BiddingSignUp, BiddingProject BiddingProject" + (agentId==0 ? "" : ", BiddingProjectAgent BiddingProjectAgent") +
				  " where BiddingProject.id=BiddingSignUp.projectId" +
				  (agentId==0 ? "" : " and BiddingProjectAgent.projectId=BiddingProject.id and BiddingProjectAgent.agentId=" + agentId) + //代理
				  " and BiddingProject.city in ('" + ListUtils.join(cities, "','", false) + "')" + //地区
				  " and ((BiddingSignUp.paymentTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" + //支付时间大等于开始时间
				  "  and BiddingSignUp.paymentTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + "))" + //支付时间小于结束时间
				  "  or (BiddingSignUp.drawPaymentTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" + //图纸支付时间大等于开始时间
				  "  and BiddingSignUp.drawPaymentTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")))" + //图纸支付时间小于结束时间
				  " order by " + (agentId==0 ? "" : "BiddingProjectAgent.agentId,") + "BiddingProject.projectName, BiddingSignUp.paymentTime";
		}
		
		final List biddingSignUps = databaseService.findRecordsByHql(hql);
		
		ExcelReportCallback reportCallback = new ExcelReportCallback() {
			/*
			 * (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReport(java.lang.String)
			 */
			public ExcelReport getExcelReport(String sheetName) {
				ExcelReport report = new ExcelReport();
				report.setPropertyValue("beginDate", beginDate);
				report.setPropertyValue("endDate", endDate);
				report.setPropertyValue("agentName", agentName);
				long previousProjectId = 0;
				String agentName = null;
				report.setHeadRowNumbers("0,1");
				report.setReferenceRowNumbers("2");
				report.setDataRowNumber(3);
				report.setDataSet(biddingSignUps);
				//输出报表行
				for(int i = biddingSignUps==null ? -1 : biddingSignUps.size()-1; i>=0; i--) {
					BiddingSignUp signUp = (BiddingSignUp)biddingSignUps.get(i);
					if(totalAfterBidopening) { //开标后统计
						if(signUp.getPaymentTime()==null) { //未购买标书
							signUp.setPaymentMoney(0);
						}
						if(signUp.getDrawPaymentTime()==null) { //未购买图纸
							signUp.setDrawPaymentMoney(0);
						}
					}
					else { //不是开标后统计
						if(signUp.getPaymentTime()==null || //未购买标书
						  signUp.getPaymentTime().before(beginDate) || //支付时间早于开始时间
						  !signUp.getPaymentTime().before(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1))) { //支付时间晚于结束时间
							signUp.setPaymentMoney(0);
						}
						if(signUp.getDrawPaymentTime()==null || //未购买图纸
						   signUp.getDrawPaymentTime().before(beginDate) || //图纸支付时间
						   !signUp.getDrawPaymentTime().before(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1))) { //图纸支付时间晚于结束时间
							signUp.setDrawPaymentMoney(0);
						}
					}
					ExcelReportData reportData = new ExcelReportData();
					reportData.setPropertyMap(BeanUtils.generatePropertyMap(signUp));
					reportData.setReferenceRowNumbers("2"); //参考行号
					//设置代理名称
					if(signUp.getProjectId()!=previousProjectId) {
						previousProjectId = signUp.getProjectId();
						agentName = (String)databaseService.findRecordByHql("select BiddingProjectAgent.agentName from BiddingProjectAgent BiddingProjectAgent where BiddingProjectAgent.projectId=" + previousProjectId);
						if(agentName==null) { //没有代理,自行招标
							agentName = (String)databaseService.findRecordByHql("select BiddingProject.owner from BiddingProject BiddingProject where BiddingProject.id=" + previousProjectId);
						}
					}
					reportData.setPropertyValue("agentName", agentName);
					reportData.setPropertyValue("index", new Integer(i+1));
					biddingSignUps.set(i, reportData);
				}
				return report;
			}
			
			/*
			 * (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReportData(int, java.lang.String[])
			 */
			public ExcelReportData getExcelReportData(int rowNumner, String[] rowContents) {
				return null;
			}
		};
		excelReportService.writeExcelReport(templateFile, outputName, response, reportCallback);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.report.service.BiddingProjectReportService#writeProjectSignUpReport(long, javax.servlet.http.HttpServletResponse)
	 */
	public void writeProjectSignUpReport(final BiddingProject project, HttpServletResponse response) throws ServiceException {
		BiddingProjectPlan plan = (BiddingProjectPlan)project.getPlans().iterator().next();
		if(plan.getBuyDocumentEnd()!=null && !DateTimeUtils.now().after(plan.getBuyDocumentEnd())) {
			throw new ServiceException();
		}
		//报表回调
		ExcelReportCallback reportCallback = new ExcelReportCallback() {
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReport(java.lang.String)
			 */
			public ExcelReport getExcelReport(String sheetName) {
				ExcelReport excelReport = new ExcelReport("0,1", "3", 4);
				excelReport.setPropertyMap(BeanUtils.generatePropertyMap(project));
				if(project.getBiddingAgents()!=null && !project.getBiddingAgents().isEmpty()) { //代理机构
					excelReport.setPropertyValue("agent", project.getBiddingAgents().iterator().next()); 
				}
				if(project.getTenders()!=null && !project.getTenders().isEmpty()) { //招标公告
					excelReport.setPropertyValue("tender", project.getTenders().iterator().next()); 
				}
				double documentMoneyTotal = 0;  //标书金额汇总
				double drawMoneyTotal = 0;  //图纸金额汇总
				double pledgeMoneyTotal = 0;  //保证金汇总
				//报名记录
				excelReport.setDataSet(new ArrayList());
				int index = 0;
				for(Iterator iterator = project.getSignUps()==null ? null : project.getSignUps().iterator(); iterator!=null && iterator.hasNext();) {
					BiddingSignUp signUp = (BiddingSignUp)iterator.next();
					if(signUp.getPaymentTime()==null && signUp.getDrawPaymentTime()==null && signUp.getPledgePaymentTime()==null) {
						continue;
					}
					String remark = null;
					if(signUp.getPaymentTime()!=null) {
						documentMoneyTotal += signUp.getPaymentMoney(); //标书金额汇总
					}
					else {
						signUp.setPaymentMoney(0);
						remark = "未购标书";
					}
					if(signUp.getDrawPaymentTime()!=null) {
						drawMoneyTotal += signUp.getDrawPaymentMoney(); //图纸金额汇总
					}
					else {
						signUp.setDrawPaymentMoney(0);
						remark = (remark==null ? "" : remark + "，") + "未购图纸";
					}
					
					if(signUp.getPledgePaymentTime()!=null) {
						pledgeMoneyTotal += signUp.getPledgePaidMoney(); //保证金汇总
					}
					else {
						signUp.setPledgePaidMoney(0);
						remark = (remark==null ? "" : remark + "，") + "未缴保证金";
					}
					ExcelReportData reportData = new ExcelReportData();
					reportData.setReferenceRowNumbers("3"); //格式参考行
					reportData.setRowIndex((++index) + ""); //行号
					reportData.setPropertyMap(BeanUtils.generatePropertyMap(signUp));
					reportData.setPropertyValue("remark", remark); //添加备注
					excelReport.getDataSet().add(reportData);
				}
				//添加合计
				ExcelReportData reportData = new ExcelReportData();
				reportData.setReferenceRowNumbers("3"); //格式参考行
				reportData.setRowIndex("合计"); //行号
				reportData.setPropertyValue("paymentMoney", new Double(documentMoneyTotal));
				reportData.setPropertyValue("drawPaymentMoney", new Double(drawMoneyTotal));
				reportData.setPropertyValue("pledgePaidMoney", new Double(pledgeMoneyTotal));
				excelReport.getDataSet().add(reportData);
				return excelReport;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReportData(int, java.lang.String[])
			 */
			public ExcelReportData getExcelReportData(int rowNumner, String[] rowContents) {
				return null;
			}
		};
		excelReportService.writeExcelReport(Environment.getWebinfPath() + "bidding/project/report/template/项目投标报名汇总.xls", "项目投标报名汇总(" + project.getProjectName() + ").xls", response, reportCallback);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.report.service.BiddingProjectReportService#writeProjectPledgeReport(com.yuanluesoft.bidding.project.pojo.BiddingProject, int, javax.servlet.http.HttpServletResponse)
	 */
	public void writeProjectPledgeReport(final BiddingProject project, final int status, HttpServletResponse response) throws ServiceException {
		//按企业名称排序
		final List signUps = project.getSignUps()==null ? new ArrayList() : new ArrayList(project.getSignUps());
		Collections.sort(signUps, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				BiddingSignUp signUp0 = (BiddingSignUp)arg0;
				BiddingSignUp signUp1 = (BiddingSignUp)arg1;
				return Collator.getInstance(Locale.CHINA).compare(signUp0.getEnterpriseName()==null ? "" : signUp0.getEnterpriseName(), signUp1.getEnterpriseName()==null ? "" : signUp1.getEnterpriseName());
			}
		});
		//报表回调
		ExcelReportCallback reportCallback = new ExcelReportCallback() {
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReport(java.lang.String)
			 */
			public ExcelReport getExcelReport(String sheetName) {
				ExcelReport excelReport = new ExcelReport("0,1", "3", 4);
				excelReport.setPropertyMap(BeanUtils.generatePropertyMap(project));
				if(project.getBiddingAgents()!=null && !project.getBiddingAgents().isEmpty()) { //代理机构
					excelReport.setPropertyValue("agent", project.getBiddingAgents().iterator().next()); 
				}
				if(project.getTenders()!=null && !project.getTenders().isEmpty()) { //招标公告
					excelReport.setPropertyValue("tender", project.getTenders().iterator().next()); 
				}
				double pledgePaidMoney = 0;  //保证金汇总
				double pledgeReturnMoney = 0; //保证金退还汇总
				//报名记录
				excelReport.setDataSet(new ArrayList());
				int index = 0;
				for(Iterator iterator = signUps.iterator(); iterator.hasNext();) {
					BiddingSignUp signUp = (BiddingSignUp)iterator.next();
					if(signUp.getPledgePaymentTime()==null || signUp.getPledgeConfirm()!='1') { //未缴保证金,或者未被确认
						continue;
					}
					if(status==1) { //0/全部, 1/待返还, 2/已返还
						if(signUp.getPledgeReturnEnabled()!='1' || signUp.getPledgeReturnTime()!=null) {
							continue;
						}
					}
					else if(status==2) { //0/全部, 1/待返还, 2/已返还
						if(signUp.getPledgeReturnTime()==null) {
							continue;
						}
					}
					pledgePaidMoney += signUp.getPledgePaidMoney(); //保证金汇总
					pledgeReturnMoney += signUp.getPledgeReturnMoney(); //保证金退还汇总
					ExcelReportData reportData = new ExcelReportData();
					reportData.setReferenceRowNumbers("3"); //格式参考行
					reportData.setRowIndex((++index) + ""); //行号
					reportData.setPropertyMap(BeanUtils.generatePropertyMap(signUp));
					excelReport.getDataSet().add(reportData);
				}
				//添加合计
				ExcelReportData reportData = new ExcelReportData();
				reportData.setReferenceRowNumbers("3"); //格式参考行
				reportData.setRowIndex("合计"); //行号
				reportData.setPropertyValue("pledgePaidMoney", new Double(pledgePaidMoney));
				reportData.setPropertyValue("pledgeReturnMoney", new Double(pledgeReturnMoney));
				excelReport.getDataSet().add(reportData);
				return excelReport;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReportData(int, java.lang.String[])
			 */
			public ExcelReportData getExcelReportData(int rowNumner, String[] rowContents) {
				return null;
			}
		};
		String fileName = "项目投标保证金汇总";
		if(status==1) { //0/全部, 1/待返还, 2/已返还
			fileName = "项目投标保证金返还名单";
		}
		else if(status==2) {
			fileName = "项目投标保证金退还清单";
		}
		excelReportService.writeExcelReport(Environment.getWebinfPath() + "bidding/project/report/template/" + fileName + ".xls", fileName + "(" + project.getProjectName() + ").xls", response, reportCallback);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.report.service.BiddingProjectReportService#writeDocumentSalesReport(java.sql.Date, java.sql.Date, java.lang.String, boolean, java.lang.String[], boolean, javax.servlet.http.HttpServletResponse)
	 */
	public void writeDocumentSalesReport(final Date beginDate, final Date endDate, final String paymentBanks, final String[] cities, final boolean isReturn, HttpServletResponse response) throws ServiceException {
		//报表回调
		ExcelReportCallback reportCallback = new ExcelReportCallback() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReport(java.lang.String)
			 */
			public ExcelReport getExcelReport(String sheetName) {
				Map fees = new HashMap();
				ExcelReport excelReport = isReturn ? new ExcelReport("0,1", "3,4", 5) : (paymentBanks==null ? new ExcelReport("0,1,2", "4,5,6", 7) : new ExcelReport("0,1", "3,4,5", 6));
				excelReport.setPropertyValue("beginDate", beginDate);
				excelReport.setPropertyValue("endDate", endDate);
				String hql;
				if(totalAfterBidopening) { //开标后统计
					hql = "select BiddingProject" +
						  " from BiddingProject BiddingProject" +
						  " left join BiddingProject.biddingAgents BiddingProjectAgent" +
						  " left join BiddingProject.bidopeningRoomSchedules BiddingRoomSchedule" + //开标室安排
						  " where BiddingRoomSchedule.roomType='开标'" + //开标室安排
						  " and BiddingRoomSchedule.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")" + //开标时间已经过了
						  " and BiddingRoomSchedule.beginTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" + //开标时间大等于开始时间
						  " and BiddingRoomSchedule.beginTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")" +
						  " and BiddingProject.city in ('" + ListUtils.join(cities, "','", false) + "')" + //地区
						  " order by BiddingProjectAgent.agentName, BiddingProject.owner";
				}
				else { //不是开标后统计
					hql = "select BiddingProject" +
						  " from BiddingProject BiddingProject" +
						  " left join BiddingProject.biddingAgents BiddingProjectAgent" +
						  " where BiddingProject.id in (" +
						  "  select distinct BiddingSignUp.projectId" +
						  "   from BiddingSignUp BiddingSignUp" +
						  "   where (BiddingSignUp.paymentTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" + //支付时间大等于开始时间
						  "   and BiddingSignUp.paymentTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + "))" + //支付时间小于结束时间
						  "   or (BiddingSignUp.drawPaymentTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" + //图纸支付时间大等于开始时间
						  "   and BiddingSignUp.drawPaymentTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")))" + //图纸支付时间小于结束时间
						  " and BiddingProject.city in ('" + ListUtils.join(cities, "','", false) + "')" + //地区
						  " order by BiddingProjectAgent.agentName, BiddingProject.owner";
				}
				List projects = databaseService.findRecordsByHql(hql, ListUtils.generateList("biddingAgents"));
				if(projects==null || projects.isEmpty()) {
					return excelReport;
				}
				Collections.sort(projects, new Comparator() {
					/* (non-Javadoc)
					 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
					 */
					public int compare(Object arg0, Object arg1) {
						BiddingProject project0 = (BiddingProject)arg0;
						BiddingProject project1 = (BiddingProject)arg1;
						String agentName0 = "是".equals(project0.getAgentEnable()) ? ((BiddingProjectAgent)project0.getBiddingAgents().iterator().next()).getAgentName() : project0.getOwner();
						String agentName1 = "是".equals(project1.getAgentEnable()) ? ((BiddingProjectAgent)project1.getBiddingAgents().iterator().next()).getAgentName() : project1.getOwner();
						return Collator.getInstance(Locale.CHINA).compare(agentName0, agentName1);
					}
				});
				
				//标书购买统计
				try {
					if(totalAfterBidopening) { //开标后统计
						hql = "select BiddingSignUp.projectId, BiddingSignUp.paymentBank, sum(BiddingSignUp.paymentMoney), count(BiddingSignUp.id)" +
							  " from BiddingSignUp BiddingSignUp" +
							  " where BiddingSignUp.projectId in (" + ListUtils.join(projects, "id", ",", false) + ")" +
							  " and not BiddingSignUp.paymentTime is null" +
							  (agentSettleDate==null || endDate.before(DateTimeUtils.parseDate(agentSettleDate, null)) ? "" : " and BiddingSignUp.paymentTime>=DATE(" + agentSettleDate + ")") + //统计结算时间以后记录
							  (paymentBanks==null ? "" : " and BiddingSignUp.paymentBank in ('" + JdbcUtils.resetQuot(paymentBanks).replaceAll(",", "','") + "')") + //如果指定银行,则只统计指定的银行
							  " group by BiddingSignUp.projectId, BiddingSignUp.paymentBank";
					}
					else { //不是开标后统计
						hql = "select BiddingSignUp.projectId, BiddingSignUp.paymentBank, sum(BiddingSignUp.paymentMoney), count(BiddingSignUp.id)" +
							  " from BiddingSignUp BiddingSignUp" +
							  " where BiddingSignUp.projectId in (" + ListUtils.join(projects, "id", ",", false) + ")" +
							  " and BiddingSignUp.paymentTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" + //支付时间大等于开始时间
							  " and BiddingSignUp.paymentTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")" + //支付时间小于于结束时间
							  (paymentBanks==null ? "" : " and BiddingSignUp.paymentBank in ('" + JdbcUtils.resetQuot(paymentBanks).replaceAll(",", "','") + "')") + //如果指定银行,则只统计指定的银行
							  " group by BiddingSignUp.projectId, BiddingSignUp.paymentBank";
					}
				}
				catch(ParseException e) {
					
				}
				List documentPayments = databaseService.findRecordsByHql(hql);
				
				//图纸购买统计
				List drawingPayments = databaseService.findRecordsByHql(hql.replaceAll("payment", "drawPayment"));
				
				ExcelReportData totalData = new ExcelReportData(isReturn ? "4" : (paymentBanks!=null ? "5" : "6"), false, null); //合计
				ExcelReportData currentAgentData = null; //当前代理
				int agentIndex = 0;
				int agentProjectIndex = 0;
				for(Iterator iterator = projects.iterator(); iterator.hasNext();) {
					BiddingProject project = (BiddingProject)iterator.next();
					String agentName = "是".equals(project.getAgentEnable()) ? ((BiddingProjectAgent)project.getBiddingAgents().iterator().next()).getAgentName() : project.getOwner();
					if(currentAgentData==null || !agentName.equals(currentAgentData.getPropertyValue("agentName"))) {
						agentProjectIndex = 0;
						if(currentAgentData!=null && currentAgentData.getPropertyNumberValue("money", 0)==0) { //没有支付记录
							agentIndex = Math.max(0, agentIndex-1);
							excelReport.getDataSet().remove(currentAgentData);
						}
						currentAgentData = new ExcelReportData(isReturn ? "3" : (paymentBanks!=null ? "3" : "4"), false, "" + (++agentIndex));
						currentAgentData.setPropertyValue("agentName", agentName);
						excelReport.addReportData(-1, currentAgentData); //代理
					}
					ExcelReportData projectData = new ExcelReportData(paymentBanks!=null ? "4" : "5", false, "" + (++agentProjectIndex));
					projectData.setPropertyValue("project", project);
					if(!isReturn) {
						excelReport.addReportData(-1, projectData); //单个项目
					}
					
					//获取项目标书购买情况
					for(Iterator iteratorPayment = documentPayments==null ? null : documentPayments.iterator(); iteratorPayment!=null && iteratorPayment.hasNext();) {
						Object[] values = (Object[])iteratorPayment.next();
						if(values[0].equals(new Long(project.getId()))) {
							double money = ((Number)values[2]).doubleValue();
							Double fee = (Double)fees.get(project.getCity());
							if(fee==null) {
								BiddingProjectCity city = null;
								try {
									city = biddingProjectParameterService.getCityDetail(project.getCity());
								}
								catch (ServiceException e) {
								
								}
								fee = new Double(city==null ? 0 : city.getAgentSettleFee());
								fees.put(project.getCity(), fee);
							}
							int count = ((Number)values[3]).intValue();
							ExcelReportData[] dataArray = {projectData, currentAgentData, totalData};
							for(int i = 0; i<dataArray.length; i++) {
								dataArray[i].addPropertyNumberValue("documentMoney_" + values[1], money); //指定银行的标书购买费用
								dataArray[i].addPropertyNumberValue("documentCount_" + values[1], count); //指定银行的标书购买份数
								dataArray[i].addPropertyNumberValue("documentMoney", money); //标书购买费用小计
								dataArray[i].addPropertyNumberValue("documentCount", count); //标书购买份数小计
								dataArray[i].addPropertyNumberValue("money", money); //费用合计
								dataArray[i].addPropertyNumberValue("fee", count * fee.doubleValue()); //手续费
								dataArray[i].addPropertyNumberValue("actualPayment", money - count * fee.doubleValue()); //实付款
							}
						}
					}
					
					//获取项目图纸购买情况
					for(Iterator iteratorPayment = drawingPayments==null ? null : drawingPayments.iterator(); iteratorPayment!=null && iteratorPayment.hasNext();) {
						Object[] values = (Object[])iteratorPayment.next();
						if(values[0].equals(new Long(project.getId()))) {
							double money = ((Number)values[2]).doubleValue();
							int count = ((Number)values[3]).intValue();
							ExcelReportData[] dataArray = {projectData, currentAgentData, totalData};
							for(int i = 0; i<dataArray.length; i++) {
								dataArray[i].addPropertyNumberValue("drawingMoney_" + values[1], money); //指定银行的图纸购买费用
								dataArray[i].addPropertyNumberValue("drawingCount_" + values[1], count); //指定银行的图纸购买份数
								dataArray[i].addPropertyNumberValue("drawingMoney", money); //图纸购买费用小计
								dataArray[i].addPropertyNumberValue("drawingCount", count); //图纸购买份数小计
								dataArray[i].addPropertyNumberValue("money", money); //费用合计
								dataArray[i].addPropertyNumberValue("actualPayment", money); //实付款
							}
						}
					}
					if(projectData.getPropertyNumberValue("money", 0)==0) { //没有支付记录
						agentProjectIndex = Math.max(0, agentProjectIndex-1);
						excelReport.getDataSet().remove(projectData);
					}
				}
				if(currentAgentData!=null && currentAgentData.getPropertyNumberValue("money", 0)==0) { //没有支付记录
					excelReport.getDataSet().remove(currentAgentData);
				}
				excelReport.addReportData(-1, totalData); //合计
				return excelReport;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReportData(int, java.lang.String[])
			 */
			public ExcelReportData getExcelReportData(int rowNumner, String[] rowContents) {
				return null;
			}
		};
		String templateFile = isReturn ? "标书费返还明细表.xls" : (paymentBanks==null ? "标书费汇总表.xls" : "标书费汇总表(按银行).xls");
		excelReportService.writeExcelReport(Environment.getWebinfPath() + "bidding/project/report/template/" + templateFile, templateFile, response, reportCallback);
	}

	/**
	 * @return the excelReportService
	 */
	public ExcelReportService getExcelReportService() {
		return excelReportService;
	}

	/**
	 * @param excelReportService the excelReportService to set
	 */
	public void setExcelReportService(ExcelReportService excelReportService) {
		this.excelReportService = excelReportService;
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
	 * @return the agentSettleDate
	 */
	public String getAgentSettleDate() {
		return agentSettleDate;
	}

	/**
	 * @param agentSettleDate the agentSettleDate to set
	 */
	public void setAgentSettleDate(String agentSettleDate) {
		this.agentSettleDate = agentSettleDate;
	}

	/**
	 * @return the totalAfterBidopening
	 */
	public boolean isTotalAfterBidopening() {
		return totalAfterBidopening;
	}

	/**
	 * @param totalAfterBidopening the totalAfterBidopening to set
	 */
	public void setTotalAfterBidopening(boolean totalAfterBidopening) {
		this.totalAfterBidopening = totalAfterBidopening;
	}
}