package com.yuanluesoft.dpc.keyproject.report.model;

import java.sql.Date;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectStageProgress;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReportData;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 项目数据
 * @author linchuan
 *
 */
public class ProjectExcelReportData extends ExcelReportData {
	private ProjectExcelReport report; //所在的报表
	private String number; //序号
	private int count = 0; //总项目数
	private double lastYearInvest; //开工至上一年度底累计完成投资
	private double previousYearInvest; //上一年度底累计完成投资
	private double yearInvest = 0; //年计划投资(万元)
	private double yearComplete = 0; //年完成投资(万元)
	private double monthsInvest = 0; //1-[n-1]月完成投资（万元）
	private double lastMonthsInvestPlan = 0; //n-12月计划完成投资（万元）
	private String progress; //形象进度
	private String problem; //项目中存在的问题
	private String problemResponsible; //问题责任单位或责任人
	private Date problemTimeLimit; //问题解决时限
	private String problemResult; //问题的解决情况
	private String dataCategory; //分类
	
	private String fiveMonthsPlans; //后五个月项目进度关键节点的安排
	
	private double tenDayInvest = 0; //旬完成投资（万元）
	private double tenDaysInvest = 0; //至...旬完成投资额（万元）
	
	//旬计划完成投资
	private double monthBeginInvestPlan = 0; //月上旬计划完成投资（万元）
	private double monthMiddleInvestPlan = 0; //月上旬计划完成投资（万元）
	private double monthEndInvestPlan = 0; //月下旬计划完成投资（万元）
	
	private double secondMonthBeginInvestPlan = 0; //第二个月上旬计划完成投资（万元）
	private double secondMonthMiddleInvestPlan = 0; //第二个月上旬计划完成投资（万元）
	private double secondMonthEndInvestPlan = 0; //第二个月下旬计划完成投资（万元）
	
	private double thirdMonthBeginInvestPlan = 0; //第三个月上旬计划完成投资（万元）
	private double thirdMonthMiddleInvestPlan = 0; //第三个月上旬计划完成投资（万元）
	private double thirdMonthEndInvestPlan = 0; //第三个月下旬计划完成投资（万元）
	
	private double fourthMonthBeginInvestPlan = 0; //第四个月上旬计划完成投资（万元）
	private double fourthMonthMiddleInvestPlan = 0; //第四个月上旬计划完成投资（万元）
	private double fourthMonthEndInvestPlan = 0; //第四个月下旬计划完成投资（万元）
	
	private double fifthMonthBeginInvestPlan = 0; //第五个月上旬计划完成投资（万元）
	private double fifthMonthMiddleInvestPlan = 0; //第五个月上旬计划完成投资（万元）
	private double fifthMonthEndInvestPlan = 0; //第五个月下旬计划完成投资（万元）
	
	//旬完成投资
	private double monthBeginInvest = 0; //月上旬完成投资（万元）
	private double monthMiddleInvest = 0; //月上旬完成投资（万元）
	private double monthEndInvest = 0; //月下旬完成投资（万元）
	
	private double secondMonthBeginInvest = 0; //第二个月上旬完成投资（万元）
	private double secondMonthMiddleInvest = 0; //第二个月上旬完成投资（万元）
	private double secondMonthEndInvest = 0; //第二个月下旬完成投资（万元）
	
	private double thirdMonthBeginInvest = 0; //第三个月上旬完成投资（万元）
	private double thirdMonthMiddleInvest = 0; //第三个月上旬完成投资（万元）
	private double thirdMonthEndInvest = 0; //第三个月下旬完成投资（万元）
	
	private double fourthMonthBeginInvest = 0; //第四个月上旬完成投资（万元）
	private double fourthMonthMiddleInvest = 0; //第四个月上旬完成投资（万元）
	private double fourthMonthEndInvest = 0; //第四个月下旬完成投资（万元）
	
	private double fifthMonthBeginInvest = 0; //第五个月上旬完成投资（万元）
	private double fifthMonthMiddleInvest = 0; //第五个月上旬完成投资（万元）
	private double fifthMonthEndInvest = 0; //第五个月下旬完成投资（万元）
	
	//旬的工作计划安排
	private String monthBeginProgressPlan; //月上旬工作计划安排
	private String monthMiddleProgressPlan; //月上旬工作计划安排
	private String monthEndProgressPlan; //月下旬工作计划安排
	
	private String secondMonthBeginProgressPlan; //第二个月上旬工作计划安排
	private String secondMonthMiddleProgressPlan; //第二个月上旬工作计划安排
	private String secondMonthEndProgressPlan; //第二个月下旬工作计划安排
	
	private String thirdMonthBeginProgressPlan; //第三个月上旬工作计划安排
	private String thirdMonthMiddleProgressPlan; //第三个月上旬工作计划安排
	private String thirdMonthEndProgressPlan; //第三个月下旬工作计划安排
	
	private String fourthMonthBeginProgressPlan; //第四个月上旬工作计划安排
	private String fourthMonthMiddleProgressPlan; //第四个月上旬工作计划安排
	private String fourthMonthEndProgressPlan; //第四个月下旬工作计划安排
	
	private String fifthMonthBeginProgressPlan; //第五个月上旬工作计划安排
	private String fifthMonthMiddleProgressPlan; //第五个月上旬工作计划安排
	private String fifthMonthEndProgressPlan; //第五个月下旬工作计划安排
	
	//形象进度
	private String monthProgress; //月形象进度
	private String secondMonthProgress; //第二个月形象进度
	private String thirdMonthProgress; //第三个月形象进度
	private String fourthMonthProgress; //第四个月形象进度
	private String fifthMonthProgress; //第五个月形象进度
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.report.excel.model.ExcelReportData#StringUtils.getPropertyValue(java.lang.String)
	 */
	public Object getPropertyValue(String propertyName) {
		//获取关键节点的属性
		String title = null;
		String name = null;
		if(propertyName.endsWith("时间")) {
			title = "时间";
			name = "timeLimit";
		}
		else if(propertyName.endsWith("责任人")) {
			title = "责任人";
			name = "responsiblePerson";
		}
		else if(propertyName.endsWith("责任单位")) {
			title = "责任单位";
			name = "responsibleUnit";
		}
		else if(propertyName.endsWith("安排")) {
			title = "安排";
			name = "plan";
		}
		else if(propertyName.endsWith("完成进度")) {
			title = "完成进度";
			name = "progress";
		}
		if(title==null) {
			return super.getPropertyValue(propertyName);
		}
		try {
			KeyProjectStageProgress stageProgress = (KeyProjectStageProgress)ListUtils.findObjectByProperty((Set)getPropertyValue("stageProgresses"), "stage", propertyName.substring(0, propertyName.length() - title.length()));
			if(stageProgress==null) {
				return null;
			}
			if("progress".equals(name)) { //获取完成进度
				if(stageProgress.getNeedApproval()!='0' || stageProgress.getCompleted()!='1') { //完成进度待审批或者未提交完成进度
					return null;
				}
			}
			else if(stageProgress.getNeedApproval()=='1') { //待审批
				return null;
			}
			return PropertyUtils.getProperty(stageProgress, name);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 年力争完成投资, 1-7月完成投资+大干150天完成投资
	 * @return
	 */
	public double getYearStriveInvest() {
		return getMonthsInvest() + ((Number)getPropertyValue("investPlan150")).doubleValue();
	}
	
	/**
	 * 1-n月完成投资占年计划%
	 * @return
	 */
	public float getMonthsInvestPercent() {
		if(yearInvest==0) {
			return 0;
		}
		return Math.round(monthsInvest / yearInvest * 10000.0) /100.0f;
	}
	
	/**
	 * n月完成投资占年计划%
	 * @return
	 */
	public float getMonthInvestPercent() {
		if(yearInvest==0) {
			return 0;
		}
		return Math.round(getMonthInvest() / yearInvest * 10000.0) /100.0f;
	}
	
	/**
	 * n+1月完成投资占年计划%
	 * @return
	 */
	public float getSecondMonthInvestPercent() {
		if(yearInvest==0) {
			return 0;
		}
		return Math.round(getSecondMonthInvest() / yearInvest * 10000.0) /100.0f;
	}
	
	/**
	 * n+2月完成投资占年计划%
	 * @return
	 */
	public float getThirdMonthInvestPercent() {
		if(yearInvest==0) {
			return 0;
		}
		return Math.round(getThirdMonthInvest() / yearInvest * 10000.0) /100.0f;
	}
	
	/**
	 * n+3月完成投资占年计划%
	 * @return
	 */
	public float getFourthMonthInvestPercent() {
		if(yearInvest==0) {
			return 0;
		}
		return Math.round(getFourthMonthInvest() / yearInvest * 10000.0) /100.0f;
	}
	
	/**
	 * n+4月完成投资占年计划%
	 * @return
	 */
	public float getFifthMonthInvestPercent() {
		if(yearInvest==0) {
			return 0;
		}
		return Math.round(getFifthMonthInvest() / yearInvest * 10000.0) /100.0f;
	}
	
	/**
	 * 本旬占月计划投资额（%）
	 * @return
	 */
	public float getTenDayInvestPercent() {
		if(getMonthInvestPlan()==0) {
			return 0;
		}
		return Math.round(tenDayInvest / getMonthInvestPlan() * 10000.0) /100.0f;
	}
	
	/**
	 * /至...旬完成投资额占月计划投资（%）
	 * @return
	 */
	public float getTenDaysInvestPercent() {
		if(getMonthInvestPlan()==0) {
			return 0;
		}
		return Math.round(tenDaysInvest / getMonthInvestPlan() * 10000.0) /100.0f;
	}
	
	
	/**
	 * @return the monthInvest
	 */
	public double getMonthInvestPlan() {
		return monthBeginInvestPlan + monthMiddleInvestPlan + monthEndInvestPlan;
	}

	/**
	 * @return the secondMonthInvestPlan
	 */
	public double getSecondMonthInvestPlan() {
		return secondMonthBeginInvestPlan + secondMonthMiddleInvestPlan + secondMonthEndInvestPlan;
	}

	/**
	 * @return the thirdMonthInvestPlan
	 */
	public double getThirdMonthInvestPlan() {
		return thirdMonthBeginInvestPlan + thirdMonthMiddleInvestPlan + thirdMonthEndInvestPlan;
	}
	
	/**
	 * @return the fourthMonthInvestPlan
	 */
	public double getFourthMonthInvestPlan() {
		return fourthMonthBeginInvestPlan + fourthMonthMiddleInvestPlan + fourthMonthEndInvestPlan;
	}

	/**
	 * @return the fifthMonthInvestPlan
	 */
	public double getFifthMonthInvestPlan() {
		return fifthMonthBeginInvestPlan + fifthMonthMiddleInvestPlan + fifthMonthEndInvestPlan;
	}

	/**
	 * @return the monthInvest
	 */
	public double getMonthInvest() {
		return monthBeginInvest + monthMiddleInvest + monthEndInvest;
	}

	/**
	 * @return the secondMonthInvest
	 */
	public double getSecondMonthInvest() {
		return secondMonthBeginInvest + secondMonthMiddleInvest + secondMonthEndInvest;
	}

	/**
	 * @return the thirdMonthInvest
	 */
	public double getThirdMonthInvest() {
		return thirdMonthBeginInvest + thirdMonthMiddleInvest + thirdMonthEndInvest;
	}
	
	/**
	 * @return the fourthMonthInvest
	 */
	public double getFourthMonthInvest() {
		return fourthMonthBeginInvest + fourthMonthMiddleInvest + fourthMonthEndInvest;
	}

	/**
	 * @return the fifthMonthInvest
	 */
	public double getFifthMonthInvest() {
		return fifthMonthBeginInvest + fifthMonthMiddleInvest + fifthMonthEndInvest;
	}
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the monthsInvest
	 */
	public double getMonthsInvest() {
		return monthsInvest;
	}
	/**
	 * @param monthsInvest the monthsInvest to set
	 */
	public void setMonthsInvest(double monthsInvest) {
		this.monthsInvest = monthsInvest;
	}
	/**
	 * @return the yearInvest
	 */
	public double getYearInvest() {
		return yearInvest;
	}
	/**
	 * @param yearInvest the yearInvest to set
	 */
	public void setYearInvest(double yearInvest) {
		this.yearInvest = yearInvest;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the dataCategory
	 */
	public String getDataCategory() {
		return dataCategory;
	}
	/**
	 * @param dataCategory the dataCategory to set
	 */
	public void setDataCategory(String dataCategory) {
		this.dataCategory = dataCategory;
	}

	/**
	 * @return the progress
	 */
	public String getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(String progress) {
		this.progress = progress;
	}

	/**
	 * @return the lastYearInvest
	 */
	public double getLastYearInvest() {
		return lastYearInvest;
	}

	/**
	 * @param lastYearInvest the lastYearInvest to set
	 */
	public void setLastYearInvest(double lastYearInvest) {
		this.lastYearInvest = lastYearInvest;
	}

	/**
	 * @return the fifthMonthEndInvest
	 */
	public double getFifthMonthEndInvest() {
		return fifthMonthEndInvest;
	}

	/**
	 * @param fifthMonthEndInvest the fifthMonthEndInvest to set
	 */
	public void setFifthMonthEndInvest(double fifthMonthEndInvest) {
		this.fifthMonthEndInvest = fifthMonthEndInvest;
	}

	/**
	 * @return the fifthMonthMiddleInvest
	 */
	public double getFifthMonthMiddleInvest() {
		return fifthMonthMiddleInvest;
	}

	/**
	 * @param fifthMonthMiddleInvest the fifthMonthMiddleInvest to set
	 */
	public void setFifthMonthMiddleInvest(double fifthMonthMiddleInvest) {
		this.fifthMonthMiddleInvest = fifthMonthMiddleInvest;
	}

	/**
	 * @return the fifthMonthProgress
	 */
	public String getFifthMonthProgress() {
		return fifthMonthProgress;
	}

	/**
	 * @param fifthMonthProgress the fifthMonthProgress to set
	 */
	public void setFifthMonthProgress(String fifthMonthProgress) {
		this.fifthMonthProgress = fifthMonthProgress;
	}

	/**
	 * @return the fourthMonthBeginInvest
	 */
	public double getFourthMonthBeginInvest() {
		return fourthMonthBeginInvest;
	}

	/**
	 * @param fourthMonthBeginInvest the fourthMonthBeginInvest to set
	 */
	public void setFourthMonthBeginInvest(double fourthMonthBeginInvest) {
		this.fourthMonthBeginInvest = fourthMonthBeginInvest;
	}

	/**
	 * @return the fourthMonthEndInvest
	 */
	public double getFourthMonthEndInvest() {
		return fourthMonthEndInvest;
	}

	/**
	 * @param fourthMonthEndInvest the fourthMonthEndInvest to set
	 */
	public void setFourthMonthEndInvest(double fourthMonthEndInvest) {
		this.fourthMonthEndInvest = fourthMonthEndInvest;
	}

	/**
	 * @return the fourthMonthMiddleInvest
	 */
	public double getFourthMonthMiddleInvest() {
		return fourthMonthMiddleInvest;
	}

	/**
	 * @param fourthMonthMiddleInvest the fourthMonthMiddleInvest to set
	 */
	public void setFourthMonthMiddleInvest(double fourthMonthMiddleInvest) {
		this.fourthMonthMiddleInvest = fourthMonthMiddleInvest;
	}

	/**
	 * @return the fourthMonthProgress
	 */
	public String getFourthMonthProgress() {
		return fourthMonthProgress;
	}

	/**
	 * @param fourthMonthProgress the fourthMonthProgress to set
	 */
	public void setFourthMonthProgress(String fourthMonthProgress) {
		this.fourthMonthProgress = fourthMonthProgress;
	}

	/**
	 * @return the monthBeginInvest
	 */
	public double getMonthBeginInvest() {
		return monthBeginInvest;
	}

	/**
	 * @param monthBeginInvest the monthBeginInvest to set
	 */
	public void setMonthBeginInvest(double monthBeginInvest) {
		this.monthBeginInvest = monthBeginInvest;
	}

	/**
	 * @return the monthEndInvest
	 */
	public double getMonthEndInvest() {
		return monthEndInvest;
	}

	/**
	 * @param monthEndInvest the monthEndInvest to set
	 */
	public void setMonthEndInvest(double monthEndInvest) {
		this.monthEndInvest = monthEndInvest;
	}

	/**
	 * @return the monthMiddleInvest
	 */
	public double getMonthMiddleInvest() {
		return monthMiddleInvest;
	}

	/**
	 * @param monthMiddleInvest the monthMiddleInvest to set
	 */
	public void setMonthMiddleInvest(double monthMiddleInvest) {
		this.monthMiddleInvest = monthMiddleInvest;
	}

	/**
	 * @return the monthProgress
	 */
	public String getMonthProgress() {
		return monthProgress;
	}

	/**
	 * @param monthProgress the monthProgress to set
	 */
	public void setMonthProgress(String monthProgress) {
		this.monthProgress = monthProgress;
	}

	/**
	 * @return the secondMonthBeginInvest
	 */
	public double getSecondMonthBeginInvest() {
		return secondMonthBeginInvest;
	}

	/**
	 * @param secondMonthBeginInvest the secondMonthBeginInvest to set
	 */
	public void setSecondMonthBeginInvest(double secondMonthBeginInvest) {
		this.secondMonthBeginInvest = secondMonthBeginInvest;
	}

	/**
	 * @return the secondMonthEndInvest
	 */
	public double getSecondMonthEndInvest() {
		return secondMonthEndInvest;
	}

	/**
	 * @param secondMonthEndInvest the secondMonthEndInvest to set
	 */
	public void setSecondMonthEndInvest(double secondMonthEndInvest) {
		this.secondMonthEndInvest = secondMonthEndInvest;
	}

	/**
	 * @return the secondMonthMiddleInvest
	 */
	public double getSecondMonthMiddleInvest() {
		return secondMonthMiddleInvest;
	}

	/**
	 * @param secondMonthMiddleInvest the secondMonthMiddleInvest to set
	 */
	public void setSecondMonthMiddleInvest(double secondMonthMiddleInvest) {
		this.secondMonthMiddleInvest = secondMonthMiddleInvest;
	}

	/**
	 * @return the secondMonthProgress
	 */
	public String getSecondMonthProgress() {
		return secondMonthProgress;
	}

	/**
	 * @param secondMonthProgress the secondMonthProgress to set
	 */
	public void setSecondMonthProgress(String secondMonthProgress) {
		this.secondMonthProgress = secondMonthProgress;
	}

	/**
	 * @return the thirdMonthBeginInvest
	 */
	public double getThirdMonthBeginInvest() {
		return thirdMonthBeginInvest;
	}

	/**
	 * @param thirdMonthBeginInvest the thirdMonthBeginInvest to set
	 */
	public void setThirdMonthBeginInvest(double thirdMonthBeginInvest) {
		this.thirdMonthBeginInvest = thirdMonthBeginInvest;
	}

	/**
	 * @return the thirdMonthEndInvest
	 */
	public double getThirdMonthEndInvest() {
		return thirdMonthEndInvest;
	}

	/**
	 * @param thirdMonthEndInvest the thirdMonthEndInvest to set
	 */
	public void setThirdMonthEndInvest(double thirdMonthEndInvest) {
		this.thirdMonthEndInvest = thirdMonthEndInvest;
	}

	/**
	 * @return the thirdMonthMiddleInvest
	 */
	public double getThirdMonthMiddleInvest() {
		return thirdMonthMiddleInvest;
	}

	/**
	 * @param thirdMonthMiddleInvest the thirdMonthMiddleInvest to set
	 */
	public void setThirdMonthMiddleInvest(double thirdMonthMiddleInvest) {
		this.thirdMonthMiddleInvest = thirdMonthMiddleInvest;
	}

	/**
	 * @return the thirdMonthProgress
	 */
	public String getThirdMonthProgress() {
		return thirdMonthProgress;
	}

	/**
	 * @param thirdMonthProgress the thirdMonthProgress to set
	 */
	public void setThirdMonthProgress(String thirdMonthProgress) {
		this.thirdMonthProgress = thirdMonthProgress;
	}

	/**
	 * @return the fiveMonthsPlans
	 */
	public String getFiveMonthsPlans() {
		return fiveMonthsPlans;
	}

	/**
	 * @param fiveMonthsPlans the fiveMonthsPlans to set
	 */
	public void setFiveMonthsPlans(String fiveMonthsPlans) {
		this.fiveMonthsPlans = fiveMonthsPlans;
	}

	/**
	 * @return the fifthMonthBeginInvest
	 */
	public double getFifthMonthBeginInvest() {
		return fifthMonthBeginInvest;
	}

	/**
	 * @param fifthMonthBeginInvest the fifthMonthBeginInvest to set
	 */
	public void setFifthMonthBeginInvest(double fifthMonthBeginInvest) {
		this.fifthMonthBeginInvest = fifthMonthBeginInvest;
	}

	/**
	 * @return the yearComplete
	 */
	public double getYearComplete() {
		return yearComplete;
	}

	/**
	 * @param yearComplete the yearComplete to set
	 */
	public void setYearComplete(double yearComplete) {
		this.yearComplete = yearComplete;
	}

	/**
	 * @return the lastMonthsInvestPlan
	 */
	public double getLastMonthsInvestPlan() {
		return lastMonthsInvestPlan;
	}

	/**
	 * @param lastMonthsInvestPlan the lastMonthsInvestPlan to set
	 */
	public void setLastMonthsInvestPlan(double lastMonthsInvestPlan) {
		this.lastMonthsInvestPlan = lastMonthsInvestPlan;
	}

	/**
	 * @return the problem
	 */
	public String getProblem() {
		return problem;
	}

	/**
	 * @param problem the problem to set
	 */
	public void setProblem(String problem) {
		this.problem = problem;
	}

	/**
	 * @return the tenDayInvest
	 */
	public double getTenDayInvest() {
		return tenDayInvest;
	}

	/**
	 * @param tenDayInvest the tenDayInvest to set
	 */
	public void setTenDayInvest(double tenDayInvest) {
		this.tenDayInvest = tenDayInvest;
	}

	/**
	 * @return the tenDaysInvest
	 */
	public double getTenDaysInvest() {
		return tenDaysInvest;
	}

	/**
	 * @param tenDaysInvest the tenDaysInvest to set
	 */
	public void setTenDaysInvest(double tenDaysInvest) {
		this.tenDaysInvest = tenDaysInvest;
	}

	/**
	 * @return the fifthMonthBeginProgressPlan
	 */
	public String getFifthMonthBeginProgressPlan() {
		return fifthMonthBeginProgressPlan;
	}

	/**
	 * @param fifthMonthBeginProgressPlan the fifthMonthBeginProgressPlan to set
	 */
	public void setFifthMonthBeginProgressPlan(String fifthMonthBeginProgressPlan) {
		this.fifthMonthBeginProgressPlan = fifthMonthBeginProgressPlan;
	}

	/**
	 * @return the fifthMonthEndProgressPlan
	 */
	public String getFifthMonthEndProgressPlan() {
		return fifthMonthEndProgressPlan;
	}

	/**
	 * @param fifthMonthEndProgressPlan the fifthMonthEndProgressPlan to set
	 */
	public void setFifthMonthEndProgressPlan(String fifthMonthEndProgressPlan) {
		this.fifthMonthEndProgressPlan = fifthMonthEndProgressPlan;
	}

	/**
	 * @return the fifthMonthMiddleProgressPlan
	 */
	public String getFifthMonthMiddleProgressPlan() {
		return fifthMonthMiddleProgressPlan;
	}

	/**
	 * @param fifthMonthMiddleProgressPlan the fifthMonthMiddleProgressPlan to set
	 */
	public void setFifthMonthMiddleProgressPlan(String fifthMonthMiddleProgressPlan) {
		this.fifthMonthMiddleProgressPlan = fifthMonthMiddleProgressPlan;
	}

	/**
	 * @return the fourthMonthBeginProgressPlan
	 */
	public String getFourthMonthBeginProgressPlan() {
		return fourthMonthBeginProgressPlan;
	}

	/**
	 * @param fourthMonthBeginProgressPlan the fourthMonthBeginProgressPlan to set
	 */
	public void setFourthMonthBeginProgressPlan(String fourthMonthBeginProgressPlan) {
		this.fourthMonthBeginProgressPlan = fourthMonthBeginProgressPlan;
	}

	/**
	 * @return the fourthMonthEndProgressPlan
	 */
	public String getFourthMonthEndProgressPlan() {
		return fourthMonthEndProgressPlan;
	}

	/**
	 * @param fourthMonthEndProgressPlan the fourthMonthEndProgressPlan to set
	 */
	public void setFourthMonthEndProgressPlan(String fourthMonthEndProgressPlan) {
		this.fourthMonthEndProgressPlan = fourthMonthEndProgressPlan;
	}

	/**
	 * @return the fourthMonthMiddleProgressPlan
	 */
	public String getFourthMonthMiddleProgressPlan() {
		return fourthMonthMiddleProgressPlan;
	}

	/**
	 * @param fourthMonthMiddleProgressPlan the fourthMonthMiddleProgressPlan to set
	 */
	public void setFourthMonthMiddleProgressPlan(
			String fourthMonthMiddleProgressPlan) {
		this.fourthMonthMiddleProgressPlan = fourthMonthMiddleProgressPlan;
	}

	/**
	 * @return the monthBeginProgressPlan
	 */
	public String getMonthBeginProgressPlan() {
		return monthBeginProgressPlan;
	}

	/**
	 * @param monthBeginProgressPlan the monthBeginProgressPlan to set
	 */
	public void setMonthBeginProgressPlan(String monthBeginProgressPlan) {
		this.monthBeginProgressPlan = monthBeginProgressPlan;
	}

	/**
	 * @return the monthEndProgressPlan
	 */
	public String getMonthEndProgressPlan() {
		return monthEndProgressPlan;
	}

	/**
	 * @param monthEndProgressPlan the monthEndProgressPlan to set
	 */
	public void setMonthEndProgressPlan(String monthEndProgressPlan) {
		this.monthEndProgressPlan = monthEndProgressPlan;
	}

	/**
	 * @return the monthMiddleProgressPlan
	 */
	public String getMonthMiddleProgressPlan() {
		return monthMiddleProgressPlan;
	}

	/**
	 * @param monthMiddleProgressPlan the monthMiddleProgressPlan to set
	 */
	public void setMonthMiddleProgressPlan(String monthMiddleProgressPlan) {
		this.monthMiddleProgressPlan = monthMiddleProgressPlan;
	}

	/**
	 * @return the secondMonthBeginProgressPlan
	 */
	public String getSecondMonthBeginProgressPlan() {
		return secondMonthBeginProgressPlan;
	}

	/**
	 * @param secondMonthBeginProgressPlan the secondMonthBeginProgressPlan to set
	 */
	public void setSecondMonthBeginProgressPlan(String secondMonthBeginProgressPlan) {
		this.secondMonthBeginProgressPlan = secondMonthBeginProgressPlan;
	}

	/**
	 * @return the secondMonthEndProgressPlan
	 */
	public String getSecondMonthEndProgressPlan() {
		return secondMonthEndProgressPlan;
	}

	/**
	 * @param secondMonthEndProgressPlan the secondMonthEndProgressPlan to set
	 */
	public void setSecondMonthEndProgressPlan(String secondMonthEndProgressPlan) {
		this.secondMonthEndProgressPlan = secondMonthEndProgressPlan;
	}

	/**
	 * @return the secondMonthMiddleProgressPlan
	 */
	public String getSecondMonthMiddleProgressPlan() {
		return secondMonthMiddleProgressPlan;
	}

	/**
	 * @param secondMonthMiddleProgressPlan the secondMonthMiddleProgressPlan to set
	 */
	public void setSecondMonthMiddleProgressPlan(
			String secondMonthMiddleProgressPlan) {
		this.secondMonthMiddleProgressPlan = secondMonthMiddleProgressPlan;
	}

	/**
	 * @return the thirdMonthBeginProgressPlan
	 */
	public String getThirdMonthBeginProgressPlan() {
		return thirdMonthBeginProgressPlan;
	}

	/**
	 * @param thirdMonthBeginProgressPlan the thirdMonthBeginProgressPlan to set
	 */
	public void setThirdMonthBeginProgressPlan(String thirdMonthBeginProgressPlan) {
		this.thirdMonthBeginProgressPlan = thirdMonthBeginProgressPlan;
	}

	/**
	 * @return the thirdMonthEndProgressPlan
	 */
	public String getThirdMonthEndProgressPlan() {
		return thirdMonthEndProgressPlan;
	}

	/**
	 * @param thirdMonthEndProgressPlan the thirdMonthEndProgressPlan to set
	 */
	public void setThirdMonthEndProgressPlan(String thirdMonthEndProgressPlan) {
		this.thirdMonthEndProgressPlan = thirdMonthEndProgressPlan;
	}

	/**
	 * @return the thirdMonthMiddleProgressPlan
	 */
	public String getThirdMonthMiddleProgressPlan() {
		return thirdMonthMiddleProgressPlan;
	}

	/**
	 * @param thirdMonthMiddleProgressPlan the thirdMonthMiddleProgressPlan to set
	 */
	public void setThirdMonthMiddleProgressPlan(String thirdMonthMiddleProgressPlan) {
		this.thirdMonthMiddleProgressPlan = thirdMonthMiddleProgressPlan;
	}

	/**
	 * @return the fifthMonthBeginInvestPlan
	 */
	public double getFifthMonthBeginInvestPlan() {
		return fifthMonthBeginInvestPlan;
	}

	/**
	 * @param fifthMonthBeginInvestPlan the fifthMonthBeginInvestPlan to set
	 */
	public void setFifthMonthBeginInvestPlan(double fifthMonthBeginInvestPlan) {
		this.fifthMonthBeginInvestPlan = fifthMonthBeginInvestPlan;
	}

	/**
	 * @return the fifthMonthEndInvestPlan
	 */
	public double getFifthMonthEndInvestPlan() {
		return fifthMonthEndInvestPlan;
	}

	/**
	 * @param fifthMonthEndInvestPlan the fifthMonthEndInvestPlan to set
	 */
	public void setFifthMonthEndInvestPlan(double fifthMonthEndInvestPlan) {
		this.fifthMonthEndInvestPlan = fifthMonthEndInvestPlan;
	}

	/**
	 * @return the fifthMonthMiddleInvestPlan
	 */
	public double getFifthMonthMiddleInvestPlan() {
		return fifthMonthMiddleInvestPlan;
	}

	/**
	 * @param fifthMonthMiddleInvestPlan the fifthMonthMiddleInvestPlan to set
	 */
	public void setFifthMonthMiddleInvestPlan(double fifthMonthMiddleInvestPlan) {
		this.fifthMonthMiddleInvestPlan = fifthMonthMiddleInvestPlan;
	}

	/**
	 * @return the fourthMonthBeginInvestPlan
	 */
	public double getFourthMonthBeginInvestPlan() {
		return fourthMonthBeginInvestPlan;
	}

	/**
	 * @param fourthMonthBeginInvestPlan the fourthMonthBeginInvestPlan to set
	 */
	public void setFourthMonthBeginInvestPlan(double fourthMonthBeginInvestPlan) {
		this.fourthMonthBeginInvestPlan = fourthMonthBeginInvestPlan;
	}

	/**
	 * @return the fourthMonthEndInvestPlan
	 */
	public double getFourthMonthEndInvestPlan() {
		return fourthMonthEndInvestPlan;
	}

	/**
	 * @param fourthMonthEndInvestPlan the fourthMonthEndInvestPlan to set
	 */
	public void setFourthMonthEndInvestPlan(double fourthMonthEndInvestPlan) {
		this.fourthMonthEndInvestPlan = fourthMonthEndInvestPlan;
	}

	/**
	 * @return the fourthMonthMiddleInvestPlan
	 */
	public double getFourthMonthMiddleInvestPlan() {
		return fourthMonthMiddleInvestPlan;
	}

	/**
	 * @param fourthMonthMiddleInvestPlan the fourthMonthMiddleInvestPlan to set
	 */
	public void setFourthMonthMiddleInvestPlan(double fourthMonthMiddleInvestPlan) {
		this.fourthMonthMiddleInvestPlan = fourthMonthMiddleInvestPlan;
	}

	/**
	 * @return the monthBeginInvestPlan
	 */
	public double getMonthBeginInvestPlan() {
		return monthBeginInvestPlan;
	}

	/**
	 * @param monthBeginInvestPlan the monthBeginInvestPlan to set
	 */
	public void setMonthBeginInvestPlan(double monthBeginInvestPlan) {
		this.monthBeginInvestPlan = monthBeginInvestPlan;
	}

	/**
	 * @return the monthEndInvestPlan
	 */
	public double getMonthEndInvestPlan() {
		return monthEndInvestPlan;
	}

	/**
	 * @param monthEndInvestPlan the monthEndInvestPlan to set
	 */
	public void setMonthEndInvestPlan(double monthEndInvestPlan) {
		this.monthEndInvestPlan = monthEndInvestPlan;
	}

	/**
	 * @return the monthMiddleInvestPlan
	 */
	public double getMonthMiddleInvestPlan() {
		return monthMiddleInvestPlan;
	}

	/**
	 * @param monthMiddleInvestPlan the monthMiddleInvestPlan to set
	 */
	public void setMonthMiddleInvestPlan(double monthMiddleInvestPlan) {
		this.monthMiddleInvestPlan = monthMiddleInvestPlan;
	}

	/**
	 * @return the secondMonthBeginInvestPlan
	 */
	public double getSecondMonthBeginInvestPlan() {
		return secondMonthBeginInvestPlan;
	}

	/**
	 * @param secondMonthBeginInvestPlan the secondMonthBeginInvestPlan to set
	 */
	public void setSecondMonthBeginInvestPlan(double secondMonthBeginInvestPlan) {
		this.secondMonthBeginInvestPlan = secondMonthBeginInvestPlan;
	}

	/**
	 * @return the secondMonthEndInvestPlan
	 */
	public double getSecondMonthEndInvestPlan() {
		return secondMonthEndInvestPlan;
	}

	/**
	 * @param secondMonthEndInvestPlan the secondMonthEndInvestPlan to set
	 */
	public void setSecondMonthEndInvestPlan(double secondMonthEndInvestPlan) {
		this.secondMonthEndInvestPlan = secondMonthEndInvestPlan;
	}

	/**
	 * @return the secondMonthMiddleInvestPlan
	 */
	public double getSecondMonthMiddleInvestPlan() {
		return secondMonthMiddleInvestPlan;
	}

	/**
	 * @param secondMonthMiddleInvestPlan the secondMonthMiddleInvestPlan to set
	 */
	public void setSecondMonthMiddleInvestPlan(double secondMonthMiddleInvestPlan) {
		this.secondMonthMiddleInvestPlan = secondMonthMiddleInvestPlan;
	}

	/**
	 * @return the thirdMonthBeginInvestPlan
	 */
	public double getThirdMonthBeginInvestPlan() {
		return thirdMonthBeginInvestPlan;
	}

	/**
	 * @param thirdMonthBeginInvestPlan the thirdMonthBeginInvestPlan to set
	 */
	public void setThirdMonthBeginInvestPlan(double thirdMonthBeginInvestPlan) {
		this.thirdMonthBeginInvestPlan = thirdMonthBeginInvestPlan;
	}

	/**
	 * @return the thirdMonthEndInvestPlan
	 */
	public double getThirdMonthEndInvestPlan() {
		return thirdMonthEndInvestPlan;
	}

	/**
	 * @param thirdMonthEndInvestPlan the thirdMonthEndInvestPlan to set
	 */
	public void setThirdMonthEndInvestPlan(double thirdMonthEndInvestPlan) {
		this.thirdMonthEndInvestPlan = thirdMonthEndInvestPlan;
	}

	/**
	 * @return the thirdMonthMiddleInvestPlan
	 */
	public double getThirdMonthMiddleInvestPlan() {
		return thirdMonthMiddleInvestPlan;
	}

	/**
	 * @param thirdMonthMiddleInvestPlan the thirdMonthMiddleInvestPlan to set
	 */
	public void setThirdMonthMiddleInvestPlan(double thirdMonthMiddleInvestPlan) {
		this.thirdMonthMiddleInvestPlan = thirdMonthMiddleInvestPlan;
	}

	/**
	 * @return the problemResult
	 */
	public String getProblemResult() {
		return problemResult;
	}

	/**
	 * @param problemResult the problemResult to set
	 */
	public void setProblemResult(String problemResult) {
		this.problemResult = problemResult;
	}

	/**
	 * @return the problemResponsible
	 */
	public String getProblemResponsible() {
		return problemResponsible;
	}

	/**
	 * @param problemResponsible the problemResponsible to set
	 */
	public void setProblemResponsible(String problemResponsible) {
		this.problemResponsible = problemResponsible;
	}

	/**
	 * @return the problemTimeLimit
	 */
	public Date getProblemTimeLimit() {
		return problemTimeLimit;
	}

	/**
	 * @param problemTimeLimit the problemTimeLimit to set
	 */
	public void setProblemTimeLimit(Date problemTimeLimit) {
		this.problemTimeLimit = problemTimeLimit;
	}

	/**
	 * @return the previousYearInvest
	 */
	public double getPreviousYearInvest() {
		return previousYearInvest;
	}

	/**
	 * @param previousYearInvest the previousYearInvest to set
	 */
	public void setPreviousYearInvest(double previousYearInvest) {
		this.previousYearInvest = previousYearInvest;
	}

	/**
	 * @return the report
	 */
	public ProjectExcelReport getReport() {
		return report;
	}

	/**
	 * @param report the report to set
	 */
	public void setReport(ProjectExcelReport report) {
		this.report = report;
	}
}