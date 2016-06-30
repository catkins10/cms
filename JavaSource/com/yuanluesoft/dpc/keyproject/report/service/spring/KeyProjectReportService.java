package com.yuanluesoft.dpc.keyproject.report.service.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.LRUMap;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProject;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectAnnualObjective;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectArea;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectDevelopmentAreaCategory;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvestComplete;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectProblem;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectProgress;
import com.yuanluesoft.dpc.keyproject.report.model.ProjectExcelReport;
import com.yuanluesoft.dpc.keyproject.report.model.ProjectExcelReportData;
import com.yuanluesoft.dpc.keyproject.service.KeyProjectService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.report.excel.ExcelReportCallback;
import com.yuanluesoft.jeaf.report.excel.ExcelReportService;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReport;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReportData;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public abstract class KeyProjectReportService {
	private DatabaseService databaseService;
	private KeyProjectService keyProjectService;
	private ExcelReportService excelReportService;
	
	/**
	 * 获取报表内容
	 * @param report
	 * @param year
	 * @param month
	 * @param tenDay
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projects
	 * @param industries
	 * @param areas
	 * @param childDevelopmentAreas
	 * @param projectReportDataCache
	 */
	protected abstract ExcelReport getExcelReportSheet(ProjectExcelReport report, String templateFileName, String sheetName, int year, int month, char tenDay, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache);

	/**
	 * 输出报表
	 * @param projectId 大于0时,输出指定的项目
	 * @param templateFile
	 * @param outputName
	 * @param year
	 * @param month
	 * @param keyMonth
	 * @param developmentArea
	 * @param projectLevels
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	protected void writeReport(long projectId, final String templateFile, String outputName, final int year, final int month, final char tenDay, final int keyMonth, final String developmentArea, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		final List projects;
		List lazyLoadProperties = ListUtils.generateList("areas,invests,annualObjectives,investCompletes,progresses,stageProgresses,problems", ",");
		if(projectId>0) {
			projects = ListUtils.generateList(getDatabaseService().findRecordById(KeyProject.class.getName(), projectId, lazyLoadProperties));
		}
		else {
			String hqlWhere = "KeyProjectDeclare.declareYear=" + year +
							  " and KeyProjectDeclare.isKeyProject='1'" +
							  " and KeyProject.fiveYearPlan!='1'" +
								  (projectLevels==null || projectLevels[0].equals("") ? "" : " and KeyProject.level in ('" + ListUtils.join(projectLevels, "','", false) + "')");
			//获取项目数据
			projects = getDatabaseService().findPrivilegedRecords(
									KeyProject.class.getName(), 
									null,
									"left join KeyProject.declares KeyProjectDeclare", 
									hqlWhere,
									"KeyProjectDeclare.priority DESC, KeyProject.name",
									null,
									RecordControlService.ACCESS_LEVEL_READONLY,
									false,
									lazyLoadProperties,
									0,
									0,
									sessionInfo);
		}
		//获取开发区分类列表
		final List developmentAreaCategories = keyProjectService.listDevelopmentAreaCategories();
	
		//获取当前开发区下的开发区列表
		KeyProjectDevelopmentAreaCategory developmentAreaCategory = (KeyProjectDevelopmentAreaCategory)ListUtils.findObjectByProperty(developmentAreaCategories, "category", developmentArea);
		final List childDevelopmentAreas = developmentAreaCategory==null ? null : ListUtils.generateList(developmentAreaCategory.getDevelopmentAreas(), ",");
		
		//获取地区列表
		final List areas = ListUtils.generateList(keyProjectService.getKeyProjectParameter().getArea(), ",");
	
		//获取行业列表
		String hql = "select KeyProjectIndustry.industry from KeyProjectIndustry KeyProjectIndustry order by KeyProjectIndustry.priority DESC, KeyProjectIndustry.industry";
		final List industries = getDatabaseService().findRecordsByHql(hql);
		
		//创建缓存,缓存500个项目,避免重复添加,提高效率
		final LRUMap projectReportDataCache = new LRUMap(500);
		ExcelReportCallback reportCallback = new ExcelReportCallback() {
			public ExcelReport getExcelReport(String sheetName) {
				ProjectExcelReport report = new ProjectExcelReport();
				report.setYear(year); //年度
				report.setMonth(month); //月份
				report.setTenDay(tenDay); //旬
				report.setDevelopmentArea(developmentArea); //开发区
				//数据列表
				List dataSet = new ArrayList();
				report.setDataSet(dataSet);
				//获取报表内容
				return getExcelReportSheet(report, templateFile, sheetName, year, month, tenDay, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}

			public ExcelReportData getExcelReportData(int rowNumner, String[] rowContents) {
				return null; //没有需要按行数据输出的报表内容
			}
		};
		excelReportService.writeExcelReport(Environment.getWebinfPath() + "dpc/keyproject/report/template/" + templateFile, outputName, response, reportCallback);
	}
	
	/**
	 * 统计
	 * @param projects
	 * @param area 地区
	 * @param industry 行业
	 * @param classify 项目类别,储备、在建和续建
	 * @param statuses 项目状态,在谈、报批、续建、新开工、在建、促竣工、竣工
	 * @param keyMonth
	 * @param developmentArea 指定开发区或者分类
	 * @param developmentAreaCategories 开发区分类配置
	 * @return
	 */
	protected ProjectExcelReportData total(List projects, String area, String industry, String classify, String statuses, int year, int month, char tenDay, int keyMonth, String developmentArea, List developmentAreaCategories, LRUMap projectReportDataCache) {
		ProjectExcelReportData data = new ProjectExcelReportData();
		projects = findProjects(projects, area, industry, classify, statuses, developmentArea, developmentAreaCategories, null, null, null);
		if(projects==null) {
			return data;
		}
		for(Iterator iterator = projects.iterator(); iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			ProjectExcelReportData projectData = getProjectExcelReportData(project, year, month, tenDay, keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			data.setCount(data.getCount() + 1); //合计:总项目数
			data.setPropertyValue("invest", new Double(data.getPropertyNumberValue("invest", 0) +  projectData.getPropertyNumberValue("invest", 0))); //合计:总投资（万元）
			data.setPropertyValue("investPlan150", new Double(data.getPropertyNumberValue("investPlan150", 0) +  projectData.getPropertyNumberValue("investPlan150", 0))); //合计:大干150天
			data.setLastYearInvest(data.getLastYearInvest() + projectData.getLastYearInvest()); //开工至上一年度底累计完成投资
			data.setYearInvest(data.getYearInvest() + projectData.getYearInvest()); //年计划投资(万元)
			data.setYearComplete(data.getYearComplete() + projectData.getYearComplete()); //年完成投资(万元)
			data.setMonthsInvest(data.getMonthsInvest() + projectData.getMonthsInvest()); //1-[n-1]月完成投资（万元）
			data.setLastMonthsInvestPlan(data.getLastMonthsInvestPlan() + projectData.getLastMonthsInvestPlan()); //n-12月计划完成投资（万元）
			
			data.setTenDayInvest(data.getTenDayInvest() + projectData.getTenDayInvest()); //旬完成投资（万元）
			data.setTenDaysInvest(data.getTenDaysInvest() + projectData.getTenDaysInvest()); //至...旬完成投资额（万元）
			
			//旬计划完成投资（万元）
			data.setMonthBeginInvestPlan(data.getMonthBeginInvestPlan() + projectData.getMonthBeginInvestPlan()); //月上旬计划完成投资（万元）
			data.setMonthMiddleInvestPlan(data.getMonthMiddleInvestPlan() + projectData.getMonthMiddleInvestPlan()); //月上旬计划完成投资（万元）
			data.setMonthEndInvestPlan(data.getMonthEndInvestPlan() + projectData.getMonthEndInvestPlan()); //月下旬计划完成投资（万元）
		
			data.setSecondMonthBeginInvestPlan(data.getSecondMonthBeginInvestPlan() + projectData.getSecondMonthBeginInvestPlan()); //第二个月上旬计划完成投资（万元）
			data.setSecondMonthMiddleInvestPlan(data.getSecondMonthMiddleInvestPlan() + projectData.getSecondMonthMiddleInvestPlan()); //第二个月上旬计划完成投资（万元）
			data.setSecondMonthEndInvestPlan(data.getSecondMonthEndInvestPlan() + projectData.getSecondMonthEndInvestPlan()); //第二个月下旬计划完成投资（万元）
			
			data.setThirdMonthBeginInvestPlan(data.getThirdMonthBeginInvestPlan() + projectData.getThirdMonthBeginInvestPlan()); //第三个月上旬计划完成投资（万元）
			data.setThirdMonthMiddleInvestPlan(data.getThirdMonthMiddleInvestPlan() + projectData.getThirdMonthMiddleInvestPlan()); //第三个月上旬计划完成投资（万元）
			data.setThirdMonthEndInvestPlan(data.getThirdMonthEndInvestPlan() + projectData.getThirdMonthEndInvestPlan()); //第三个月下旬计划完成投资（万元）
			
			data.setFourthMonthBeginInvestPlan(data.getFourthMonthBeginInvestPlan() + projectData.getFourthMonthBeginInvestPlan()); //第四个月上旬计划完成投资（万元）
			data.setFourthMonthMiddleInvestPlan(data.getFourthMonthMiddleInvestPlan() + projectData.getFourthMonthMiddleInvestPlan()); //第四个月上旬计划完成投资（万元）
			data.setFourthMonthEndInvestPlan(data.getFourthMonthEndInvestPlan() + projectData.getFourthMonthEndInvestPlan()); //第四个月下旬计划完成投资（万元）
			
			data.setFifthMonthBeginInvestPlan(data.getFifthMonthBeginInvestPlan() + projectData.getFifthMonthBeginInvestPlan()); //第五个月上旬计划完成投资（万元）
			data.setFifthMonthMiddleInvestPlan(data.getFifthMonthMiddleInvestPlan() + projectData.getFifthMonthMiddleInvestPlan()); //第五个月上旬计划完成投资（万元）
			data.setFifthMonthEndInvestPlan(data.getFifthMonthEndInvestPlan() + projectData.getFifthMonthEndInvestPlan()); //第五个月下旬计划完成投资（万元）
			
			//旬完成投资（万元）
			data.setMonthBeginInvest(data.getMonthBeginInvest() + projectData.getMonthBeginInvest()); //月上旬完成投资（万元）
			data.setMonthMiddleInvest(data.getMonthMiddleInvest() + projectData.getMonthMiddleInvest()); //月上旬完成投资（万元）
			data.setMonthEndInvest(data.getMonthEndInvest() + projectData.getMonthEndInvest()); //月下旬完成投资（万元）
		
			data.setSecondMonthBeginInvest(data.getSecondMonthBeginInvest() + projectData.getSecondMonthBeginInvest()); //第二个月上旬完成投资（万元）
			data.setSecondMonthMiddleInvest(data.getSecondMonthMiddleInvest() + projectData.getSecondMonthMiddleInvest()); //第二个月上旬完成投资（万元）
			data.setSecondMonthEndInvest(data.getSecondMonthEndInvest() + projectData.getSecondMonthEndInvest()); //第二个月下旬完成投资（万元）
			
			data.setThirdMonthBeginInvest(data.getThirdMonthBeginInvest() + projectData.getThirdMonthBeginInvest()); //第三个月上旬完成投资（万元）
			data.setThirdMonthMiddleInvest(data.getThirdMonthMiddleInvest() + projectData.getThirdMonthMiddleInvest()); //第三个月上旬完成投资（万元）
			data.setThirdMonthEndInvest(data.getThirdMonthEndInvest() + projectData.getThirdMonthEndInvest()); //第三个月下旬完成投资（万元）
			
			data.setFourthMonthBeginInvest(data.getFourthMonthBeginInvest() + projectData.getFourthMonthBeginInvest()); //第四个月上旬完成投资（万元）
			data.setFourthMonthMiddleInvest(data.getFourthMonthMiddleInvest() + projectData.getFourthMonthMiddleInvest()); //第四个月上旬完成投资（万元）
			data.setFourthMonthEndInvest(data.getFourthMonthEndInvest() + projectData.getFourthMonthEndInvest()); //第四个月下旬完成投资（万元）
			
			data.setFifthMonthBeginInvest(data.getFifthMonthBeginInvest() + projectData.getFifthMonthBeginInvest()); //第五个月上旬完成投资（万元）
			data.setFifthMonthMiddleInvest(data.getFifthMonthMiddleInvest() + projectData.getFifthMonthMiddleInvest()); //第五个月上旬完成投资（万元）
			data.setFifthMonthEndInvest(data.getFifthMonthEndInvest() + projectData.getFifthMonthEndInvest()); //第五个月下旬完成投资（万元）
		}
		return data;
	}
	
	/**
	 * 输出一个项目
	 * @param project
	 * @param year
	 * @param month
	 * @param tenDay
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projectReportDataCache
	 * @return
	 */
	protected ProjectExcelReportData getProjectExcelReportData(KeyProject project, int year, int month, char tenDay, int keyMonth, String developmentArea, List developmentAreaCategories, LRUMap projectReportDataCache) {
		ProjectExcelReportData data = (ProjectExcelReportData)projectReportDataCache.get(new Long(project.getId())); //从缓存获取数据
		if(data!=null) {
			return data;
		}
		data = new ProjectExcelReportData();
		data.setPropertyMap(BeanUtils.generatePropertyMap(project)); //属性列表
		
		//开工至上一年度底累计完成投资
		data.setLastYearInvest(getYearsInvestCompleted(project, 0, year-1));
		
		//上一年度累计完成投资
		data.setPreviousYearInvest(getYearsInvestCompleted(project, year-1, year-1));
		
		//年计划投资
		KeyProjectAnnualObjective annualObjective = (KeyProjectAnnualObjective)ListUtils.findObjectByProperty(project.getAnnualObjectives(), "objectiveYear", new Integer(year));
		if(annualObjective!=null && annualObjective.getNeedApproval()!='1') {
			data.setYearInvest(annualObjective.getInvestPlan());
		}
		
		//keyMonth-12月计划完成投资（万元）
		double lastMonthsInvestPlan = 0;
		for(Iterator iterator=(project.getInvestCompletes()!=null ? project.getInvestCompletes().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
			if(investComplete.getNeedApproval()!='1' && investComplete.getCompleteYear()==year && investComplete.getCompleteMonth()>=keyMonth) {
				lastMonthsInvestPlan += investComplete.getInvestPlan();
			}
		}
		data.setLastMonthsInvestPlan(lastMonthsInvestPlan);
		
		//年完成投资
		data.setYearComplete(getMonthsInvestCompleted(project, year, 1, 12));
		
		//1-[n-1]月完成投资（万元） TODO: 南平是1-n月
		data.setMonthsInvest(getMonthsInvestCompleted(project, year, 1, month - 1));
		
		//形象进度
		String projectProgress = "";
		for(Iterator iterator=(project.getProgresses()!=null ? project.getProgresses().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectProgress progress = (KeyProjectProgress)iterator.next();
			/*if(progress.getProgressYear()<year ||
			   (progress.getProgressYear()==year && progress.getProgressMonth()<=month)) {
				projectProgress = progress.getProgress();
				break;
			}*/
			if(progress.getProgressYear()==year && progress.getProgressMonth()==month && progress.getNeedApproval()=='0' && progress.getCompleted()=='1') {
				projectProgress = progress.getProgress();
				break;
			}
		}
		data.setProgress(projectProgress);
		
		//项目中存在的问题
		for(Iterator iterator=(project.getProblems()!=null ? project.getProblems().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectProblem projectProblem = (KeyProjectProblem)iterator.next();
			if(projectProblem.getProblemYear()==year && projectProblem.getProblemMonth()==month && projectProblem.getNeedApproval()!='1') {
				data.setProblem(projectProblem.getProblem());
				data.setProblemResponsible(projectProblem.getResponsible());
				data.setProblemTimeLimit(projectProblem.getTimeLimit());
				data.setProblemResult(projectProblem.getResult());
				break;
			}
		}
		
		//后五个月项目进度安排
		String fiveMonthsPlans = "";
		int planCount = 0;
		for(Iterator iterator=(project.getProgresses()!=null ? project.getProgresses().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectProgress progress = (KeyProjectProgress)iterator.next();
			if(progress.getNeedApproval()=='1') { //安排需要审核
				continue;
			}
			int months = progress.getProgressYear()*12 + progress.getProgressMonth();
			if(months >= year * 12 + month && months < year * 12 + month + 5) {
				planCount++;
				fiveMonthsPlans = progress.getProgressMonthTitle() + " " +  progress.getPlan() + (fiveMonthsPlans.equals("") ? "" : "  " + fiveMonthsPlans);
			}
		}
		data.setFiveMonthsPlans(planCount==1 ? fiveMonthsPlans.substring(fiveMonthsPlans.indexOf(' ') + 1) : fiveMonthsPlans);
		
		//旬完成投资（万元）
		data.setTenDayInvest(getTenDaysInvestCompleted(project, year, month, tenDay, tenDay));
		
		//至...旬完成投资额（万元）
		data.setTenDaysInvest(getTenDaysInvestCompleted(project, year, month, '1', tenDay));
		
		//旬计划完成投资
		data.setMonthBeginInvestPlan(getTenDaysInvestPlan(project, year, month, '1', '1')); //月上旬计划完成投资（万元）
		data.setMonthMiddleInvestPlan(getTenDaysInvestPlan(project, year, month, '2', '2')); //月上旬计划完成投资（万元）
		data.setMonthEndInvestPlan(getTenDaysInvestPlan(project, year, month, '3', '3')); //月下旬计划完成投资（万元）
		
		data.setSecondMonthBeginInvestPlan(getTenDaysInvestPlan(project, year, month + 1, '1', '1')); //第二个月上旬计划完成投资（万元）
		data.setSecondMonthMiddleInvestPlan(getTenDaysInvestPlan(project, year, month + 1, '2', '2')); //第二个月上旬计划完成投资（万元）
		data.setSecondMonthEndInvestPlan(getTenDaysInvestPlan(project, year, month + 1, '3', '3')); //第二个月下旬计划完成投资（万元）
		
		data.setThirdMonthBeginInvestPlan(getTenDaysInvestPlan(project, year, month + 2, '1', '1')); //第三个月上旬计划完成投资（万元）
		data.setThirdMonthMiddleInvestPlan(getTenDaysInvestPlan(project, year, month + 2, '2', '2')); //第三个月上旬计划完成投资（万元）
		data.setThirdMonthEndInvestPlan(getTenDaysInvestPlan(project, year, month + 2, '3', '3')); //第三个月下旬计划完成投资（万元）
		
		data.setFourthMonthBeginInvestPlan(getTenDaysInvestPlan(project, year, month + 3, '1', '1')); //第四个月上旬计划完成投资（万元）
		data.setFourthMonthMiddleInvestPlan(getTenDaysInvestPlan(project, year, month + 3, '2', '2')); //第四个月上旬计划完成投资（万元）
		data.setFourthMonthEndInvestPlan(getTenDaysInvestPlan(project, year, month + 3, '3', '3')); //第四个月下旬计划完成投资（万元）
		
		data.setFifthMonthBeginInvestPlan(getTenDaysInvestPlan(project, year, month + 4, '1', '1')); //第五个月上旬计划完成投资（万元）
		data.setFifthMonthMiddleInvestPlan(getTenDaysInvestPlan(project, year, month + 4, '2', '2')); //第五个月上旬计划完成投资（万元）
		data.setFifthMonthEndInvestPlan(getTenDaysInvestPlan(project, year, month + 4, '3', '3')); //第五个月下旬计划完成投资（万元）
	
		//旬完成投资（万元）
		data.setMonthBeginInvest(getTenDaysInvestCompleted(project, year, month, '1', '1')); //月上旬完成投资（万元）
		data.setMonthMiddleInvest(getTenDaysInvestCompleted(project, year, month, '2', '2')); //月上旬完成投资（万元）
		data.setMonthEndInvest(getTenDaysInvestCompleted(project, year, month, '3', '3')); //月下旬完成投资（万元）
		
		data.setSecondMonthBeginInvest(getTenDaysInvestCompleted(project, year, month + 1, '1', '1')); //第二个月上旬完成投资（万元）
		data.setSecondMonthMiddleInvest(getTenDaysInvestCompleted(project, year, month + 1, '2', '2')); //第二个月上旬完成投资（万元）
		data.setSecondMonthEndInvest(getTenDaysInvestCompleted(project, year, month + 1, '3', '3')); //第二个月下旬完成投资（万元）
		
		data.setThirdMonthBeginInvest(getTenDaysInvestCompleted(project, year, month + 2, '1', '1')); //第三个月上旬完成投资（万元）
		data.setThirdMonthMiddleInvest(getTenDaysInvestCompleted(project, year, month + 2, '2', '2')); //第三个月上旬完成投资（万元）
		data.setThirdMonthEndInvest(getTenDaysInvestCompleted(project, year, month + 2, '3', '3')); //第三个月下旬完成投资（万元）
		
		data.setFourthMonthBeginInvest(getTenDaysInvestCompleted(project, year, month + 3, '1', '1')); //第四个月上旬完成投资（万元）
		data.setFourthMonthMiddleInvest(getTenDaysInvestCompleted(project, year, month + 3, '2', '2')); //第四个月上旬完成投资（万元）
		data.setFourthMonthEndInvest(getTenDaysInvestCompleted(project, year, month + 3, '3', '3')); //第四个月下旬完成投资（万元）
		
		data.setFifthMonthBeginInvest(getTenDaysInvestCompleted(project, year, month + 4, '1', '1')); //第五个月上旬完成投资（万元）
		data.setFifthMonthMiddleInvest(getTenDaysInvestCompleted(project, year, month + 4, '2', '2')); //第五个月上旬完成投资（万元）
		data.setFifthMonthEndInvest(getTenDaysInvestCompleted(project, year, month + 4, '3', '3')); //第五个月下旬完成投资（万元）
		
		//旬的工作计划安排
		data.setMonthBeginProgressPlan(getMonthProgressPlan(project, year, month, '1')); //月上旬工作计划安排
		data.setMonthMiddleProgressPlan(getMonthProgressPlan(project, year, month, '2')); //月上旬工作计划安排
		data.setMonthEndProgressPlan(getMonthProgressPlan(project, year, month, '3')); //月下旬工作计划安排
		
		data.setSecondMonthBeginProgressPlan(getMonthProgressPlan(project, year, month + 1, '1')); //第二个月上旬工作计划安排
		data.setSecondMonthMiddleProgressPlan(getMonthProgressPlan(project, year, month + 1, '2')); //第二个月上旬工作计划安排
		data.setSecondMonthEndProgressPlan(getMonthProgressPlan(project, year, month + 1, '3')); //第二个月下旬工作计划安排
		
		data.setThirdMonthBeginProgressPlan(getMonthProgressPlan(project, year, month + 2, '1')); //第三个月上旬工作计划安排
		data.setThirdMonthMiddleProgressPlan(getMonthProgressPlan(project, year, month + 2, '2')); //第三个月上旬工作计划安排
		data.setThirdMonthEndProgressPlan(getMonthProgressPlan(project, year, month + 2, '3')); //第三个月下旬工作计划安排
		
		data.setFourthMonthBeginProgressPlan(getMonthProgressPlan(project, year, month + 3, '1')); //第四个月上旬工作计划安排
		data.setFourthMonthMiddleProgressPlan(getMonthProgressPlan(project, year, month + 3, '2')); //第四个月上旬工作计划安排
		data.setFourthMonthEndProgressPlan(getMonthProgressPlan(project, year, month + 3, '3')); //第四个月下旬工作计划安排
		
		data.setFifthMonthBeginProgressPlan(getMonthProgressPlan(project, year, month + 4, '1')); //第五个月上旬工作计划安排
		data.setFifthMonthMiddleProgressPlan(getMonthProgressPlan(project, year, month + 4, '2')); //第五个月上旬工作计划安排
		data.setFifthMonthEndProgressPlan(getMonthProgressPlan(project, year, month + 4, '3')); //第五个月下旬工作计划安排
		
		//月形象进度
		data.setMonthProgress(getMonthProgress(project, year, month)); //月形象进度
		data.setSecondMonthProgress(getMonthProgress(project, year, month + 1)); //第二个月形象进度
		data.setThirdMonthProgress(getMonthProgress(project, year, month + 2)); //第三个月形象进度
		data.setFourthMonthProgress(getMonthProgress(project, year, month + 3)); //第四个月形象进度
		data.setFifthMonthProgress(getMonthProgress(project, year, month + 4)); //第五个月形象进度
		
		projectReportDataCache.put(new Long(project.getId()), data); //写入缓存
		return data;
	}
	
	/**
	 * 获取n个旬计划完成投资
	 * @param project
	 * @param year
	 * @param month
	 * @param beginTenDay
	 * @param endTenDay
	 * @return
	 */
	private double getTenDaysInvestPlan(KeyProject project, int year, int month, char beginTenDay, char endTenDay) {
		year += (month-1) / 12;
		month = (month-1) % 12 + 1;
		double tenDaysInvestPlan = 0;
		for(Iterator iterator=(project.getInvestCompletes()!=null ? project.getInvestCompletes().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
			if(investComplete.getNeedApproval()!='1' &&
			   investComplete.getCompleteYear()==year &&
			   investComplete.getCompleteMonth()==month &&
			   investComplete.getCompleteTenDay()>=beginTenDay && investComplete.getCompleteTenDay()<=endTenDay) {
				tenDaysInvestPlan +=  investComplete.getInvestPlan();
			}
		}
		return tenDaysInvestPlan;
	}
	
	/**
	 * 获取n个旬完成投资
	 * @param project
	 * @param year
	 * @param month
	 * @param beginTenDay
	 * @param endTenDay
	 * @return
	 */
	private double getTenDaysInvestCompleted(KeyProject project, int year, int month, char beginTenDay, char endTenDay) {
		year += (month-1) / 12;
		month = (month-1) % 12 + 1;
		double tenDaysInvest = 0;
		for(Iterator iterator=(project.getInvestCompletes()!=null ? project.getInvestCompletes().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
			if(investComplete.getNeedApproval()=='0' && investComplete.getCompleted()=='1' &&
			   investComplete.getCompleteYear()==year &&
			   investComplete.getCompleteMonth()==month &&
			   investComplete.getCompleteTenDay()>=beginTenDay && investComplete.getCompleteTenDay()<=endTenDay) {
				tenDaysInvest +=  investComplete.getCompleteInvest();
			}
		}
		return tenDaysInvest;
	}
	
	/**
	 * 获取年完成投资
	 * @param project
	 * @param beginYear
	 * @param endYear
	 * @return
	 */
	private double getYearsInvestCompleted(KeyProject project, int beginYear, int endYear) {
		double yearsInvest = 0;
		for(Iterator iterator=(project.getInvestCompletes()!=null ? project.getInvestCompletes().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
			if(investComplete.getCompleteYear()>=beginYear && investComplete.getCompleteYear()<=endYear && investComplete.getNeedApproval()=='0' && investComplete.getCompleted()=='1') {
				yearsInvest += investComplete.getCompleteInvest();
			}
		}
		return yearsInvest;
	}
	
	/**
	 * 获取n个月完成投资
	 * @param project
	 * @param year
	 * @param beginMonth
	 * @param endMonth
	 * @param tenDay
	 * @return
	 */
	private double getMonthsInvestCompleted(KeyProject project, int year, int beginMonth, int endMonth) {
		double monthsInvest = 0;
		for(Iterator iterator=(project.getInvestCompletes()!=null ? project.getInvestCompletes().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
			if(investComplete.getCompleteYear()==year &&
			   investComplete.getCompleteMonth()>=beginMonth &&
			   investComplete.getCompleteMonth()<=endMonth &&
			   investComplete.getNeedApproval()=='0' && investComplete.getCompleted()=='1') {
				monthsInvest += investComplete.getCompleteInvest();
			}
		}
		return monthsInvest;
	}
	
	/**
	 * 获取n个月计划完成投资
	 * @param project
	 * @param year
	 * @param beginMonth
	 * @param endMonth
	 * @return
	 */
	protected double getMonthsInvestPlan(KeyProject project, int year, int beginMonth, int endMonth) {
		double monthsInvestPlan = 0;
		for(Iterator iterator=(project.getInvestCompletes()!=null ? project.getInvestCompletes().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
			if(investComplete.getNeedApproval()!='1' &&
			   investComplete.getCompleteYear()==year &&
			   investComplete.getCompleteMonth()>=beginMonth &&
			   investComplete.getCompleteMonth()<=endMonth) {
				monthsInvestPlan += investComplete.getInvestPlan();
			}
		}
		return monthsInvestPlan;
	}
	
	/**
	 * 获取旬形象进度安排
	 * @param project
	 * @param year
	 * @param month
	 * @param tenDay
	 * @return
	 */
	private String getMonthProgressPlan(KeyProject project, int year, int month, char tenDay) {
		year += (month-1) / 12;
		month = (month-1) % 12 + 1;
		for(Iterator iterator=(project.getProgresses()!=null ? project.getProgresses().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectProgress progress = (KeyProjectProgress)iterator.next();
			if(progress.getProgressYear()==year && progress.getProgressMonth()==month && progress.getProgressTenDay()==tenDay) {
				return progress.getNeedApproval()!='1' ? progress.getPlan() : null;
			}
		}
		return null;
	}
	
	/**
	 * 获取月形象进度
	 * @param project
	 * @param year
	 * @param month
	 * @return
	 */
	private String getMonthProgress(KeyProject project, int year, int month) {
		year += (month-1) / 12;
		month = (month-1) % 12 + 1;
		String monthProgress = "";
		for(Iterator iterator=(project.getProgresses()!=null ? project.getProgresses().iterator() : null); iterator!=null && iterator.hasNext();) {
			KeyProjectProgress progress = (KeyProjectProgress)iterator.next();
			if(progress.getProgressYear()==year && progress.getProgressMonth()==month && progress.getNeedApproval()=='0' && progress.getCompleted()=='1') {
				monthProgress = (monthProgress==null ? progress.getProgress() : progress.getProgress() + "  " + monthProgress);
			}
		}
		return monthProgress;
	}
	
	/**
	 * 从项目中筛选符合条件的项目
	 * @param projects
	 * @param area
	 * @param industry
	 * @param classify
	 * @param statuses 状态列表
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param sortByAreas 不为null时按地区排序
	 * @param sortByIndustries 不为null时按行业排序
	 * @param sortByChildDevelopmentAreas 不为null时按开发区排序
	 * @return
	 */
	protected List findProjects(List projects, String area, String industry, String classify, String statuses, String developmentArea, final List developmentAreaCategories, final List sortByAreas, final List sortByIndustries, final List sortByChildDevelopmentAreas) {
		if(projects==null || projects.isEmpty()) {
			return null;
		}
		List foundProjects = new ArrayList();
		for(Iterator iterator = projects.iterator(); iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			//检查项目是否属于当前开发区
			if(developmentArea!=null && !inDevelopmentArea(project.getDevelopmentArea(), developmentArea, developmentAreaCategories, 0)) {
				continue;
			}
			if(area!=null) { //地区比较
				boolean inArea = false;
				for(Iterator iteratorArea = project.getAreas().iterator(); iteratorArea.hasNext();) {
					KeyProjectArea projectArea = (KeyProjectArea)iteratorArea.next();
					if(projectArea.getArea().startsWith(area)) {
						inArea = true;
						break;
					}
				}
				if(!inArea) {
					continue;
				}
			}
			if(industry!=null) { //行业比较
				if(!industry.equals(project.getIndustry())) {
					continue;
				}
			}
			if(classify!=null) {
				boolean pre = ("储备".equals(project.getClassify()) || "预备".equals(project.getClassify()));
				if("预备".equals(classify)) {
					if(!pre) {
						continue;
					}
				}
				else if(pre) {
					continue;
				}
			}
			if(statuses!=null) { //状态检查
				if(("," + statuses + ",").indexOf("," + project.getStatus() + ",")==-1) {
					continue;
				}
			}
			foundProjects.add(project);
		}
		if(foundProjects.isEmpty()) {
			return null;
		}
		//按地区和行业重新排序
		Collections.sort(foundProjects, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				KeyProject project0 = (KeyProject)arg0;
				KeyProject project1 = (KeyProject)arg1;
				int areaIndex0 = getPorjectAreaIndex(project0);
				int areaIndex1 = getPorjectAreaIndex(project1);
				if(areaIndex0>areaIndex1) {
					return 1;
				}
				else if(areaIndex0<areaIndex1) {
					return -1;
				}
				
				//按行业排序
				int industryIndex0 = getPorjectIndustryIndex(project0);
				int industryIndex1 = getPorjectIndustryIndex(project1);
				if(industryIndex0>industryIndex1) {
					return 1;
				}
				else if(industryIndex0<industryIndex1) {
					return -1;
				}
				
				//按开发区排序
				int developmentAreaIndex0 = getPorjectDevelopmentAreaIndex(project0);
				int developmentAreaIndex1 = getPorjectDevelopmentAreaIndex(project1);
				if(developmentAreaIndex0>developmentAreaIndex1) {
					return 1;
				}
				else if(developmentAreaIndex0<developmentAreaIndex1) {
					return -1;
				}
				return 0;
			}
			
			private int getPorjectAreaIndex(KeyProject project) { //获取项目的地区序号
				if(sortByAreas==null) {
					return 0;
				}
				int areaIndex = Integer.MAX_VALUE;
				for(Iterator iteratorArea = project.getAreas().iterator(); iteratorArea.hasNext();) {
					KeyProjectArea projectArea = (KeyProjectArea)iteratorArea.next();
					int index = sortByAreas.indexOf(projectArea.getArea());
					if(index<areaIndex) {
						areaIndex = index;
					}
				}
				return areaIndex;
			}
			
			private int getPorjectIndustryIndex(KeyProject project) { //获取项目的行业序号
				if(sortByIndustries==null) {
					return 0;
				}
				return sortByIndustries.indexOf(project.getIndustry());
			}
			
			private int getPorjectDevelopmentAreaIndex(KeyProject project) { //获取项目的开发区序号
				if(sortByChildDevelopmentAreas==null) {
					return 0;
				}
				int i=0;
				for(; i<sortByChildDevelopmentAreas.size(); i++) {
					String childDevelopmentArea = (String)sortByChildDevelopmentAreas.get(i);
					if(inDevelopmentArea(project.getDevelopmentArea(), childDevelopmentArea, developmentAreaCategories, 0)) {
						break;
					}
				}
				return i;
			}
		});
		return foundProjects;
	}
	
	/**
	 * 递归函数:检查项目是否属于指定的开发区或者开发区分类
	 * @param projectDevelopmentArea
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @return
	 */
	private boolean inDevelopmentArea(String projectDevelopmentArea, String developmentArea, List developmentAreaCategories, int deep) {
		if(developmentArea==null || developmentArea.equals("")) {
			return true;
		}
		if(deep>10) { //深度超过10,说明有死循环
			return false;
		}
		if(projectDevelopmentArea==null || projectDevelopmentArea.equals("")) { //项目所属开发区为空
			return false;
		}
		if(projectDevelopmentArea.equals(developmentArea)) { ///项目所属开发区就是指定的开发区
			return true;
		}
		if(developmentAreaCategories==null || developmentAreaCategories.isEmpty()) {
			return false;
		}
		//检查隶属的分类
		for(Iterator iterator = developmentAreaCategories.iterator(); iterator.hasNext();) {
			KeyProjectDevelopmentAreaCategory developmentAreaCategory = (KeyProjectDevelopmentAreaCategory)iterator.next();
			if(("," + developmentAreaCategory.getDevelopmentAreas() + ",").indexOf("," + projectDevelopmentArea + ",")!=-1 && //隶属于分类
			   inDevelopmentArea(developmentAreaCategory.getCategory(), developmentArea, developmentAreaCategories, deep + 1)) { //所属分类就是指定的开发区分类
				return true;
			}
		}
		return false;
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
	 * @return the keyProjectService
	 */
	public KeyProjectService getKeyProjectService() {
		return keyProjectService;
	}

	/**
	 * @param keyProjectService the keyProjectService to set
	 */
	public void setKeyProjectService(KeyProjectService keyProjectService) {
		this.keyProjectService = keyProjectService;
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
}