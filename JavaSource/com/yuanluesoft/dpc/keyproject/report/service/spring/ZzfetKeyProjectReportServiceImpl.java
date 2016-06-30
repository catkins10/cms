package com.yuanluesoft.dpc.keyproject.report.service.spring;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.LRUMap;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProject;
import com.yuanluesoft.dpc.keyproject.report.model.ProjectExcelReport;
import com.yuanluesoft.dpc.keyproject.report.model.ProjectExcelReportData;
import com.yuanluesoft.dpc.keyproject.report.service.ZzfetKeyProjectReportService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReport;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ZzfetKeyProjectReportServiceImpl extends KeyProjectReportService implements ZzfetKeyProjectReportService {
	private Map statusMap; //项目状态表
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.report.service.ZzfetKeyProjectReportService#writeProcessReport(int, int, java.lang.String, java.lang.String[], javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeProgressReport(final int year, final int month, int keyMonth, final String developmentArea, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		writeReport(0, "zzfet/战役项目.xls", developmentArea + "战役项目" + year + (month>=10 ? "" : "0") + month + ".xls", year, month, '0', keyMonth, developmentArea, projectLevels, response, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.report.service.ZzfetKeyProjectReportService#writeAccountableReport(int, int, java.lang.String, java.lang.String[], javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeAccountableReport(final int year, final int month, int keyMonth, final String developmentArea, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		writeReport(0, "zzfet/项目目标责任制表.xls", developmentArea + "“4个一批”项目目标责任制表" + year + (month>=10 ? "" : "0") + month + ".xls", year, month, '0', keyMonth, developmentArea, projectLevels, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.report.service.ZzfetKeyProjectReportService#writePillarReport(int, int, java.lang.String, java.lang.String[], javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writePillarReport(final int year, final int month, int keyMonth, final String developmentArea, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		writeReport(0, "zzfet/重大支撑项目表.xls", developmentArea + "重大支撑项目表" + year + (month>=10 ? "" : "0") + month + ".xls", year, month, '0', keyMonth, developmentArea, new String[]{"重大支撑项目"}, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.report.service.ZzfetKeyProjectReportService#writeYearReport(int, int, java.lang.String, java.lang.String[], javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeYearReport(final int year, final int month, int keyMonth, final String developmentArea, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		writeReport(0, "zzfet/年度项目明细表.xls", developmentArea + "“4个一批”项目目标责任制表" + year + (month>=10 ? "" : "0") + month + ".xls", year, month, '0', keyMonth, developmentArea, projectLevels, response, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.report.service.ZzfetKeyProjectReportService#writeTenDayReport(int, int, char, java.lang.String, java.lang.String[], javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeTenDayReport(int year, int month, char tenDay, int keyMonth, String developmentArea, String[] projectLevels, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		writeReport(0, "zzfet/旬报表.xls", developmentArea + "旬报表.xls", year, month, tenDay, keyMonth, developmentArea, projectLevels, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.report.service.ZzfetKeyProjectReportService#writeProphaseDetail(long, int, int, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeProphaseDetail(long projectId, int year, int month, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		writeReport(projectId, "zzfet/项目前期工作台账.xls", "漳州市重点建设战役项目（前期工作）台账.xls", year, month, '0', 8, null, null, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.report.service.ZzfetKeyProjectReportService#writeBuildingDetail(long, int, int, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeBuildingDetail(long projectId, int year, int month, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		writeReport(projectId, "zzfet/在建、新开工项目台账.xls", "漳州市重点建设战役项目（在建、新开工）台账.xls", year, month, '0', 8, null, null, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.keyproject.report.service.spring.KeyProjectReportService#getExcelReportSheet(com.yuanluesoft.dpc.keyproject.report.model.PorjectExcelReport, java.lang.String, java.lang.String, int, int, char, int, java.lang.String, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, org.apache.commons.collections.map.LRUMap)
	 */
	protected ExcelReport getExcelReportSheet(ProjectExcelReport report, String templateFileName, String sheetName, int year, int month, char tenDay, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		//获取需要输出的项目状态
		String statuses = (String)statusMap.get(templateFileName + "." + sheetName);
		if(statuses==null) {
			statuses = (String)statusMap.get(templateFileName);
		}
		if(templateFileName.equals("zzfet/战役项目.xls")) {
			if("明细表".equals(sheetName)) {
				getProcessReportDetailSheet(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
			else if("进度一览表".equals(sheetName)) {
				getProcessReportProgressSheet(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
			else if("汇总表".equals(sheetName)) {
				getProcessReportTotalSheet(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
		}
		else if(templateFileName.equals("zzfet/项目目标责任制表.xls")) {
			if("竣工投产".equals(sheetName)) {
				getAccountableReport(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
			else if("加速开工".equals(sheetName)) {
				getAccountableReport(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
			else if("加速报批".equals(sheetName)) {
				getAccountableReport(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
			else if("加速签约".equals(sheetName)) {
				getAccountableReport(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
		}
		else if(templateFileName.equals("zzfet/重大支撑项目表.xls")) {
			getPillarReport(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
		}
		else if(templateFileName.equals("zzfet/年度项目明细表.xls")) {
			if("明细表".equals(sheetName)) {
				getYearReportDetailSheet(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
			else if("按地区".equals(sheetName)) {
				getYearReportAreaSheet(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
			else if("按行业".equals(sheetName)) {
				getYearReportIndustrySheet(report, statuses, year, month, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
			}
		}
		else if(templateFileName.equals("zzfet/旬报表.xls")) {
			getTenDayReport(report, statuses, year, month, tenDay, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
		}
		else if(templateFileName.equals("zzfet/项目前期工作台账.xls")) {
			getProphaseDetailReport(report, statuses, year, month, tenDay, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
		}
		else if(templateFileName.equals("zzfet/在建、新开工项目台账.xls")) {
			getBuildingDetailReport(report, statuses, year, month, tenDay, keyMonth, developmentArea, developmentAreaCategories, projects, industries, areas, childDevelopmentAreas, projectReportDataCache);
		}
		return report;
	}
	
	/**
	 * 战役项目报表:明细表
	 * @param statuses
	 * @param year
	 * @param month
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projects
	 * @param industries
	 * @param areas
	 * @param projectReportDataCache
	 * @return
	 */
	public void getProcessReportDetailSheet(ProjectExcelReport report, String statuses, int year, int month, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers("0,1,2"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("3,4"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(5); //数据输出的行号
		
		//合计
		ProjectExcelReportData data = total(projects, null, null, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
		data.setReferenceRowNumbers("3");
		data.setFixedRowHeight(true);
		report.getDataSet().add(data);

		//输出项目列表
		int projectIndex = 0;
		List foundProjects = findProjects(projects, null, null, null, statuses, developmentArea, developmentAreaCategories, null, null, null);
		for(Iterator iterator = (foundProjects==null ? null : foundProjects.iterator()); iterator!=null && iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			data = getProjectExcelReportData(project, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			data.setReferenceRowNumbers("4");
			data.setNumber((++projectIndex) + ""); //设置项目编号
			report.getDataSet().add(data);
		}
	}
	
	/**
	 * 战役项目报表:进度一览表
	 * @param statuses
	 * @param year
	 * @param month
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projects
	 * @param industries
	 * @param areas
	 * @param childDevelopmentAreas
	 * @param projectReportDataCache
	 * @return
	 */
	public void getProcessReportProgressSheet(ProjectExcelReport report, String statuses, int year, int month, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers("0,1,2,3,4"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("5,6"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(7); //数据输出的行号
		
		//合计
		ProjectExcelReportData data = total(projects, null, null, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
		data.setReferenceRowNumbers("5");
		data.setFixedRowHeight(true);
		report.getDataSet().add(data);

		//输出项目列表
		int projectIndex = 0;
		List foundProjects = findProjects(projects, null, null, null, statuses, developmentArea, developmentAreaCategories, null, null, null);
		for(Iterator iterator = (foundProjects==null ? null : foundProjects.iterator()); iterator!=null && iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			data = getProjectExcelReportData(project, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			data.setReferenceRowNumbers("6");
			data.setNumber((++projectIndex) + ""); //设置项目编号
			report.getDataSet().add(data);
		}
	}
	
	/**
	 * 战役项目报表:汇总表
	 * @param statuses
	 * @param year
	 * @param month
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projects
	 * @param industries
	 * @param areas
	 * @param childDevelopmentAreas
	 * @param projectReportDataCache
	 * @return
	 */
	public void getProcessReportTotalSheet(ProjectExcelReport report, String statuses, int year, int month, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers("0,1,2,3,4"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("5,6,7"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(8); //数据输出的行号
		
		//合计
		ProjectExcelReportData data = total(projects, null, null, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
		data.setReferenceRowNumbers("5");
		data.setFixedRowHeight(true);
		report.getDataSet().add(data);

		//输出下级开发区汇总列表
		int index = 0;
		ProjectExcelReportData lastData = null;
		for(Iterator iterator = childDevelopmentAreas==null ? null : childDevelopmentAreas.iterator(); iterator!=null && iterator.hasNext();) {
			String childDevelopmentArea = (String)iterator.next();
			data = total(projects, null, null, null, statuses, year, month, '0', keyMonth, childDevelopmentArea, developmentAreaCategories, projectReportDataCache);
			data.setReferenceRowNumbers("6");
			data.setDataCategory(childDevelopmentArea);
			data.setNumber((++index) + ""); //设置编号
			report.getDataSet().add(data);
			lastData = data;
		}
		if(lastData!=null) {
			lastData.setReferenceRowNumbers("7");
		}
	}
	
	/**
	 * 获取项目目标责任制表
	 * @param statuses
	 * @param year
	 * @param month
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projects
	 * @param industries
	 * @param areas
	 * @param childDevelopmentAreas
	 * @param projectReportDataCache
	 * @return
	 */
	public void getAccountableReport(ProjectExcelReport report, String statuses, int year, int month, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers("0,1,2"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("3,4"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(5); //数据输出的行号
		
		//合计
		ProjectExcelReportData data = total(projects, null, null, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
		data.setReferenceRowNumbers("3");
		data.setFixedRowHeight(true);
		report.getDataSet().add(data);

		//输出项目列表
		int projectIndex = 0;
		List foundProjects = findProjects(projects, null, null, null, statuses, developmentArea, developmentAreaCategories, null, null, null);
		for(Iterator iterator = (foundProjects==null ? null : foundProjects.iterator()); iterator!=null && iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			data = getProjectExcelReportData(project, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			data.setReferenceRowNumbers("4");
			data.setNumber((++projectIndex) + ""); //设置项目编号
			report.getDataSet().add(data);
		}
	}
	
	/**
	 * 输出重大支撑项目表
	 * @param statuses
	 * @param year
	 * @param month
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projects
	 * @param industries
	 * @param areas
	 * @param childDevelopmentAreas
	 * @param projectReportDataCache
	 * @return
	 */
	public void getPillarReport(ProjectExcelReport report, String statuses, int year, int month, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers("0,1,2"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("3,4"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(5); //数据输出的行号
	
		//合计
		ProjectExcelReportData data = total(projects, null, null, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
		data.setReferenceRowNumbers("3");
		data.setFixedRowHeight(true);
		report.getDataSet().add(data);

		//输出项目列表
		int projectIndex = 0;
		List foundProjects = findProjects(projects, null, null, null, statuses, developmentArea, developmentAreaCategories, null, null, null);
		for(Iterator iterator = (foundProjects==null ? null : foundProjects.iterator()); iterator!=null && iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			data = getProjectExcelReportData(project, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			data.setReferenceRowNumbers("4");
			data.setNumber((++projectIndex) + ""); //设置项目编号
			report.getDataSet().add(data);
		}
	}
	
	/**
	 *  输出经济开发区年度项目明细表:明细表
	 * @param statuses
	 * @param year
	 * @param month
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projects
	 * @param industries
	 * @param areas
	 * @param childDevelopmentAreas
	 * @param projectReportDataCache
	 * @return
	 */
	public void getYearReportDetailSheet(ProjectExcelReport report, String statuses, int year, int month, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers("0,1,2"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("3,4"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(5); //数据输出的行号
		
		//合计
		ProjectExcelReportData data = total(projects, null, null, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
		data.setReferenceRowNumbers("3");
		data.setFixedRowHeight(true);
		report.getDataSet().add(data);

		//输出项目列表
		int projectIndex = 0;
		List foundProjects = findProjects(projects, null, null, null, statuses, developmentArea, developmentAreaCategories, null, null, childDevelopmentAreas);
		for(Iterator iterator = (foundProjects==null ? null : foundProjects.iterator()); iterator!=null && iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			data = getProjectExcelReportData(project, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			data.setReferenceRowNumbers("4");
			data.setNumber((++projectIndex) + ""); //设置项目编号
			report.getDataSet().add(data);
		}
	}
	
	/**
	 * 输出经济开发区年度项目明细表:按地区
	 * @param statuses
	 * @param year
	 * @param month
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projects
	 * @param industries
	 * @param areas
	 * @param childDevelopmentAreas
	 * @param projectReportDataCache
	 * @return
	 */
	public void getYearReportAreaSheet(ProjectExcelReport report, String statuses, int year, int month, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers("0,1,2"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("3,4,5,6"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(7); //数据输出的行号
		
		//合计
		ProjectExcelReportData data = total(projects, null, null, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
		data.setReferenceRowNumbers("3");
		data.setFixedRowHeight(true);
		report.getDataSet().add(data);
		
		//按地区统计项目
		int areaIndex = 0; //行业序号
		boolean city = false;
		for(Iterator iterator = areas.iterator(); iterator.hasNext();) {
			String area = (String)iterator.next();
			if(area.startsWith("市")) {
				if(city) {
					continue;
				}
				area = "市";
				city = true;
			}
			//龙海市合计14个
			data = total(projects, area, null, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			if(data.getCount()==0) { //该地区没有项目
				continue;
			}
			areaIndex++;
			data.setNumber(StringUtils.getChineseNumber(areaIndex, false)); //设置地区序号
			data.setDataCategory(area.equals("市") ? "市本级" : area);
			data.setReferenceRowNumbers("4");
			report.getDataSet().add(data);
			
			//按行业统计项目
			int industryIndex = 0; //行业序号
			for(Iterator iteratorIndustry = industries.iterator(); iteratorIndustry.hasNext();) {
				String industry = (String)iteratorIndustry.next();
				//行业
				data = total(projects, area, industry, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
				if(data.getCount()==0) { //该行业没有项目
					continue;
				}
				industryIndex++;
				data.setNumber(StringUtils.getChineseNumber(industryIndex, false)); //设置行业序号
				data.setDataCategory(industry);
				data.setReferenceRowNumbers("5");
				report.getDataSet().add(data);
					
				int projectIndex = 0;
				//获取项目列表
				List foundProjects = findProjects(projects, area, industry, null, statuses, developmentArea, developmentAreaCategories, null, null, null);
				for(Iterator iteratorProject = foundProjects==null ? null : foundProjects.iterator(); iteratorProject!=null && iteratorProject.hasNext();) {
					KeyProject project = (KeyProject)iteratorProject.next();
					data = getProjectExcelReportData(project, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
					data.setReferenceRowNumbers("6");
					projectIndex++;
					data.setNumber(projectIndex + ""); //设置项目编号
					report.getDataSet().add(data);
				}
			}
		}
	}
	
	/**
	 * 输出经济开发区年度项目明细表:按行业
	 * @param statuses
	 * @param year
	 * @param month
	 * @param keyMonth
	 * @param developmentArea
	 * @param developmentAreaCategories
	 * @param projects
	 * @param industries
	 * @param areas
	 * @param childDevelopmentAreas
	 * @param projectReportDataCache
	 * @return
	 */
	public void getYearReportIndustrySheet(ProjectExcelReport report, String statuses, int year, int month, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers("0,1,2"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("3,4,5"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(6); //数据输出的行号
		
		//合计
		ProjectExcelReportData data = total(projects, null, null, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
		data.setReferenceRowNumbers("3");
		data.setFixedRowHeight(true);
		report.getDataSet().add(data);
			
		//按行业统计项目
		int industryIndex = 0; //行业序号
		for(Iterator iteratorIndustry = industries.iterator(); iteratorIndustry.hasNext();) {
			String industry = (String)iteratorIndustry.next();
			//行业
			data = total(projects, null, industry, null, statuses, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			if(data.getCount()==0) { //该行业没有项目
				continue;
			}
			industryIndex++;
			data.setNumber(StringUtils.getChineseNumber(industryIndex, false)); //设置行业序号
			data.setDataCategory(industry);
			data.setReferenceRowNumbers("4");
			report.getDataSet().add(data);
				
			int projectIndex = 0;
			//获取项目列表
			List foundProjects = findProjects(projects, null, industry, null, statuses, developmentArea, developmentAreaCategories, null, null, null);
			for(Iterator iterator = (foundProjects==null ? null : foundProjects.iterator()); iterator!=null && iterator.hasNext();) {
				KeyProject project = (KeyProject)iterator.next();
				data = getProjectExcelReportData(project, year, month, '0', keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
				data.setReferenceRowNumbers("5");
				projectIndex++;
				data.setNumber(projectIndex + ""); //设置项目编号
				report.getDataSet().add(data);
			}
		}
	}
	
	/**
	 * 输出旬报表
	 * @param report
	 * @param statuses
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
	public void getTenDayReport(ProjectExcelReport report, String statuses, int year, int month, char tenDay, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers("0,1,2,3"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("4,5"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(6); //数据输出的行号
	
		//合计
		ProjectExcelReportData data = total(projects, null, null, null, statuses, year, month, tenDay, keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
		data.setReferenceRowNumbers("4");
		data.setFixedRowHeight(true);
		report.getDataSet().add(data);

		//输出项目列表
		int projectIndex = 0;
		List foundProjects = findProjects(projects, null, null, null, statuses, developmentArea, developmentAreaCategories, null, null, null);
		for(Iterator iterator = (foundProjects==null ? null : foundProjects.iterator()); iterator!=null && iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			data = getProjectExcelReportData(project, year, month, tenDay, keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			data.setReferenceRowNumbers("5");
			data.setNumber((++projectIndex) + ""); //设置项目编号
			report.getDataSet().add(data);
		}
	}
	
	/**
	 * 输出项目前期工作台帐报表
	 * @param report
	 * @param statuses
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
	 * @return
	 */
	public void getProphaseDetailReport(ProjectExcelReport report, String statuses, int year, int month, char tenDay, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers(null); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("1,2,3,4,5,6,7,8,9,10"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(11); //数据输出的行号
	
		//输出项目列表
		for(Iterator iterator = projects.iterator(); iterator!=null && iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			ProjectExcelReportData data = getProjectExcelReportData(project, year, month, tenDay, keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			data.setReferenceRowNumbers("1,2,3,4,5,6,7,8,9,10");
			data.setFixedRowHeight(true);
			data.setReport(report);
			report.getDataSet().add(data);
		}
	}
	
	/**
	 * 输出在建项目台帐报表
	 * @param report
	 * @param statuses
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
	public void getBuildingDetailReport(ProjectExcelReport report, String statuses, int year, int month, char tenDay, int keyMonth, String developmentArea, List developmentAreaCategories, List projects, List industries, List areas, List childDevelopmentAreas, LRUMap projectReportDataCache) {
		report.setHeadRowNumbers(null); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(17); //数据输出的行号
	
		//输出项目列表
		for(Iterator iterator = projects.iterator(); iterator.hasNext();) {
			KeyProject project = (KeyProject)iterator.next();
			ProjectExcelReportData data = getProjectExcelReportData(project, year, month, tenDay, keyMonth, developmentArea, developmentAreaCategories, projectReportDataCache);
			data.setReferenceRowNumbers("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16");
			data.setFixedRowHeight(true);
			data.setReport(report);
			report.getDataSet().add(data);
		}
	}
	
	/**
	 * @return the statusMap
	 */
	public Map getStatusMap() {
		return statusMap;
	}

	/**
	 * @param statusMap the statusMap to set
	 */
	public void setStatusMap(Map statusMap) {
		this.statusMap = statusMap;
	}
}