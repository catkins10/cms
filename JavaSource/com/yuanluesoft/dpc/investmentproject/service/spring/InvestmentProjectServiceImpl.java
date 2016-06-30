package com.yuanluesoft.dpc.investmentproject.service.spring;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.dpc.investmentproject.pojo.InvestmentProject;
import com.yuanluesoft.dpc.investmentproject.pojo.InvestmentProjectIndustry;
import com.yuanluesoft.dpc.investmentproject.pojo.InvestmentProjectParameter;
import com.yuanluesoft.dpc.investmentproject.service.InvestmentProjectService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.report.excel.ExcelReportCallback;
import com.yuanluesoft.jeaf.report.excel.ExcelReportService;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReport;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReportData;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class InvestmentProjectServiceImpl extends BusinessServiceImpl implements InvestmentProjectService {
	private ExcelReportService excelReportService; //电子表格报表服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.investmentproject.service.InvestmentProjectService#getParameter()
	 */
	public InvestmentProjectParameter getParameter() throws ServiceException {
		InvestmentProjectParameter parameter = (InvestmentProjectParameter)getDatabaseService().findRecordByHql("from InvestmentProjectParameter InvestmentProjectParameter");
		if(parameter==null) {
			parameter = new InvestmentProjectParameter();
			parameter.setId(UUIDLongGenerator.generateId());
			//parameter.setArea(area); //区域和开发区
			parameter.setInvestMode("独资,合资,合作,BT形式,BOT形式"); //利用外资方式
			parameter.setCurrency("人民币,美元"); //币种,人民币,美元
			getDatabaseService().saveRecord(parameter);
		}
		return parameter;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.investmentproject.service.InvestmentProjectService#listIndustries()
	 */
	public List listIndustries() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from InvestmentProjectIndustry InvestmentProjectIndustry order by InvestmentProjectIndustry.priority DESC, InvestmentProjectIndustry.industry");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		InvestmentProjectParameter parameter = (InvestmentProjectParameter)request.getAttribute("investmentProjectParameter");
		if(parameter==null) {
			parameter = getParameter();
			request.setAttribute("investmentProjectParameter", parameter);
		}
		if("area".equals(itemsName)) { //区域
			return ListUtils.generateList(parameter.getArea(), ",");
		}
		else if("currency".equals(itemsName)) { //币种
			return ListUtils.generateList(parameter.getCurrency(), ",");
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.dpc.investmentproject.service.InvestmentProjectService#writeProjectReport(java.sql.Date, java.sql.Date, java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	public void writeProjectReport(final Date beginDate, final Date endDate, final String area, final HttpServletResponse response) throws ServiceException {
		final List industries = listIndustries(); //获取行业列表
		ExcelReportCallback reportCallback = new ExcelReportCallback() {

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReport(java.lang.String)
			 */
			public ExcelReport getExcelReport(String sheetName) {
				try {
					return createSynthesizeReport(industries, beginDate, endDate, area, response);
				}
				catch (ServiceException e) {
					Logger.exception(e);
					return null;
				}
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportCallback#getExcelReportData(int, java.lang.String[])
			 */
			public ExcelReportData getExcelReportData(int rowNumner, String[] rowContents) {
				return null; //没有需要按行数据输出的报表内容
			}
		};
		excelReportService.writeExcelReport(Environment.getWebinfPath() + "dpc/investmentproject/template/招商项目汇总表.xls", "招商项目汇总表.xls", response, reportCallback);
	}
	
	/**
	 * 输出综合(全市)报表
	 * @param industries
	 * @param beginDate
	 * @param endDate
	 * @param area
	 * @param response
	 * @return
	 * @throws ServiceException
	 */
	private ProjectExcelReport createSynthesizeReport(List industries, Date beginDate, Date endDate, String area, HttpServletResponse response) throws ServiceException {
		final List reportDataSet = new ArrayList();
		for(int i=0; i<industries.size(); i++) {
			InvestmentProjectIndustry industry = (InvestmentProjectIndustry)industries.get(i);
			//输出行业
			ProjectExcelReportData industryData = new ProjectExcelReportData();
			industryData.setNumber(StringUtils.getChineseNumber(i+1, false));
			industryData.setReferenceRowNumbers("2"); //格式参考行
			industryData.setPropertyValue("industry", industry.getIndustry());
			reportDataSet.add(industryData);
			//输出子行业
			String[] childIndustry = (industry.getChildIndustry()==null ? "" : industry.getChildIndustry()).split(",");
			for(int j=0; j<childIndustry.length; j++) {
				ProjectExcelReportData childIndustryData = new ProjectExcelReportData();
				childIndustryData.setNumber(StringUtils.getChineseNumber(j+1, false));
				childIndustryData.setReferenceRowNumbers("3"); //格式参考行
				childIndustryData.setPropertyValue("childIndustry", childIndustry[j]);
				if(!childIndustry[j].isEmpty()) {
					reportDataSet.add(childIndustryData);
				}
				//输出项目列表
				List projects = listProjects(beginDate, endDate, area, industry.getIndustry(), childIndustry[j]);
				for(int k=0; k<(projects==null ? 0 : projects.size()); k++) {
					InvestmentProject project = (InvestmentProject)projects.get(k);
					ProjectExcelReportData projectData = new ProjectExcelReportData();
					projectData.setPropertyMap(BeanUtils.generatePropertyMap(project));
					projectData.setNumber((k+1) + "");
					projectData.setReferenceRowNumbers("4"); //格式参考行
					//设置投资总额
					projectData.addInvestment(project.getCurrency(), project.getInvestment());
					childIndustryData.addInvestment(project.getCurrency(), project.getInvestment());
					industryData.addInvestment(project.getCurrency(), project.getInvestment());
					//增加项目数
					childIndustryData.setCount(childIndustryData.getCount()+1);
					industryData.setCount(industryData.getCount()+1);
					reportDataSet.add(projectData);
				}
			}
		}
		ProjectExcelReport report = new ProjectExcelReport();
		report.setYear(DateTimeUtils.getYear(beginDate==null ? DateTimeUtils.date() : beginDate));
		report.setHeadRowNumbers("0,1"); //表头行号列表,如:1,2,3
		report.setReferenceRowNumbers("2,3,4"); //格式参考行号列表,如:4,5
		report.setDataRowNumber(5); //数据输出的行号
		report.setDataSet(reportDataSet); //数据列表
		return report;
	}
	
	/**
	 * 获取项目列表
	 * @param beginDate
	 * @param endDate
	 * @param area
	 * @param industry
	 * @param childIndustry
	 * @return
	 * @throws ServiceException
	 */
	private List listProjects(Date beginDate, Date endDate, String area, String industry, String childIndustry) throws ServiceException {
		String hql = "from InvestmentProject InvestmentProject" +
					 " where InvestmentProject.created>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 " and InvestmentProject.created<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")" +
					 (area==null || area.isEmpty() ? "" : " and InvestmentProject.area='" + area + "'") +
					 (industry==null || industry.isEmpty() ? "" : " and InvestmentProject.industry='" + industry + "'") +
					 (childIndustry==null || childIndustry.isEmpty() ? "" : " and InvestmentProject.childIndustry='" + childIndustry + "'") +
					 " order by InvestmentProject.name";
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/**
	 * 项目报表
	 * @author linchuan
	 *
	 */
	public class ProjectExcelReport extends ExcelReport {
		private int year; //年度

		/**
		 * @return the year
		 */
		public int getYear() {
			return year;
		}

		/**
		 * @param year the year to set
		 */
		public void setYear(int year) {
			this.year = year;
		}
	}
	
	/**
	 * 项目报表数据
	 * @author linchuan
	 *
	 */
	public class ProjectExcelReportData extends ExcelReportData {
		private String number; //序号
		private int count = 0; //总项目数
		private Map investments = new HashMap(); //各种币种对应的金额总额
		
		/**
		 * 用文本方式显示的投资总额
		 * @return
		 */
		public String getInvestmentText() {
			String text = null;
			Double investment = (Double)investments.get("人民币");
			if(investment!=null && investment.doubleValue()>0) {
				text = new DecimalFormat("#.#").format(investment.doubleValue()) + "万元";
			}
			for(Iterator iterator = investments.keySet().iterator(); iterator.hasNext();) {
				String currency = (String)iterator.next();
				if(currency.equals("人民币")) {
					continue;
				}
				investment = (Double)investments.get(currency);
				if(investment!=null && investment.doubleValue()>0) {
					text = (text==null ? "" : text + "、") + new DecimalFormat("#.#").format(investment.doubleValue()) + "万" + currency;
				}
			}
			return text;
		}
		
		/**
		 * 增加投资总额
		 * @param currency
		 * @param investment
		 */
		public void addInvestment(String currency, double investment) {
			if(currency==null || currency.isEmpty()) {
				return;
			}
			Double total = (Double)investments.get(currency);
			investments.put(currency, new Double((total==null ? 0 : total.doubleValue()) + investment));
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