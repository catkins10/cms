package com.yuanluesoft.fet.project.service.spring;

import java.io.File;
import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Workbook;

import com.yuanluesoft.fet.project.pojo.FetProject;
import com.yuanluesoft.fet.project.pojo.FetProjectTarget;
import com.yuanluesoft.fet.project.service.FetProjectService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class FetProjectServiceImpl extends BusinessServiceImpl implements FetProjectService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("fairCategories".equals(itemsName)) { //活动分类
			String hql = "select distinct FetProjectFair.category from FetProjectFair FetProjectFair order by FetProjectFair.category";
			return getDatabaseService().findRecordsByHql(hql);
		}
		else if("fairs".equals(itemsName)) { //活动列表
			return getDatabaseService().findRecordsByHql("select FetProjectFair.name, FetProjectFair.fairNumber from FetProjectFair FetProjectFair order by FetProjectFair.name");
		}
		else if("merchants".equals(itemsName)) { //客商列表
			return getDatabaseService().findRecordsByHql("select FetMerchant.name from FetMerchant FetMerchant order by FetMerchant.name");
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.project.service.FetProjectService#exportFairPorjects(java.lang.String, int, javax.servlet.http.HttpServletResponse)
	 */
	public void exportFairPorjects(String fairName, int fairNumber, HttpServletResponse response) throws ServiceException {
		jxl.write.WritableWorkbook  wwb = null;
		jxl.Workbook rw = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "inline; filename=\"" + FileUtils.encodeFileName("可望签约项目情况表.xls", "utf-8") + "\"");
			//读入报表模板
			rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "fet/project/template/可望签约项目情况表.template.xls"));
			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(response.getOutputStream(), rw);
			//输出签约合同项目明细表
			writeFairPorjectsSheet(wwb.getSheet(0), fairName, fairNumber, true);
			//输出意向项目明细表
			writeFairPorjectsSheet(wwb.getSheet(1), fairName, fairNumber, false);
			//输出签约项目汇总表
			writeFairPorjectsTotalSheet(wwb.getSheet(2), fairName, fairNumber);
			//输出专场招商（期间）新接洽项目情况表
			writeFairPorjectsNewSheet(wwb.getSheet(3), fairName, fairNumber);
			wwb.write();
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			//关闭Excel工作薄对象
			try {
				wwb.close();
			}
			catch(Exception e) {
				
			}
			//关闭只读的Excel对象
			rw.close();
		}
	}
	
	/**
	 * 输出签约合同项目明细表
	 * @param ws
	 * @param fairName
	 * @param fairNumber
	 * @param isContract 是否合同项目
	 * @throws ServiceException
	 */
	private void writeFairPorjectsSheet(jxl.write.WritableSheet ws, String fairName, int fairNumber, boolean isContract) throws ServiceException {
		int TOP_ROWS = 4; //从第5行开始输出数据
		//获取数据
		String hql = "from FetProject FetProject" +
					 " where FetProject.fairName='" + JdbcUtils.resetQuot(fairName) + "'" +
					 " and FetProject.fairNumber=" + fairNumber +
					 " and FetProject.sign='" + (isContract ? "签订合同" : "签订意向") + "'" + //成果类别,初步接洽、签订意向、签订合同
					 " order by FetProject.name";
		int rowIndex = TOP_ROWS;
		for(int i=0; ;i+=100) {
			List data = getDatabaseService().findRecordsByHql(hql, i, 100); //每次取100条记录
			if(data==null || data.isEmpty()) {
				break;
			}
			//输出记录
			for(Iterator iterator = data.iterator(); iterator.hasNext();) {
				FetProject project = (FetProject)iterator.next();
				//输出一行,序号/项目名称/建设内容和规模/中方/外方/国别地区/合作方式/投资总额/合同外资/签约种类
				try {
					//序号
					ws.addCell(new jxl.write.Number(0, rowIndex, rowIndex - TOP_ROWS + 1, ws.getCell(0, TOP_ROWS).getCellFormat()));
					//项目名称
					ws.addCell(new jxl.write.Label(1, rowIndex, project.getName(), ws.getCell(1, TOP_ROWS).getCellFormat()));
					//建设内容和规模
					ws.addCell(new jxl.write.Label(2, rowIndex, project.getEnterpriseScale(), ws.getCell(2, TOP_ROWS).getCellFormat()));
					//中方
					ws.addCell(new jxl.write.Label(3, rowIndex, project.getChineseCompany(), ws.getCell(3, TOP_ROWS).getCellFormat()));
					//外方
					ws.addCell(new jxl.write.Label(4, rowIndex, project.getForeignCompany(), ws.getCell(4, TOP_ROWS).getCellFormat()));
					//国别地区
					ws.addCell(new jxl.write.Label(5, rowIndex, project.getCountry(), ws.getCell(5, TOP_ROWS).getCellFormat()));
					//合作方式
					ws.addCell(new jxl.write.Label(6, rowIndex, project.getInvestmentType(), ws.getCell(6, TOP_ROWS).getCellFormat()));
					//投资总额
					ws.addCell(new jxl.write.Number(7, rowIndex, project.getTotalInvestment(), ws.getCell(7, TOP_ROWS).getCellFormat()));
					//合同外资
					ws.addCell(new jxl.write.Number(8, rowIndex, project.getBargainInvestment(), ws.getCell(8, TOP_ROWS).getCellFormat()));
					//签约种类
					ws.addCell(new jxl.write.Label(9, rowIndex, project.getSignCategory(), ws.getCell(9, TOP_ROWS).getCellFormat()));
					ws.setRowView(rowIndex, ws.getRowView(TOP_ROWS).getSize());
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				rowIndex++;
			}
		}
	}
	
	/**
	 * 输出签约项目汇总表
	 * @param ws
	 * @param fairName
	 * @param fairNumber
	 * @throws ServiceException
	 */
	private void writeFairPorjectsTotalSheet(jxl.write.WritableSheet ws, String fairName, int fairNumber) throws ServiceException {
		int TOP_ROWS = 5; //从第5行开始输出数据
		//获取数据
		String sql = "select FetProject.county as county," +
					 " count(FetProject.id) as totalProjects," + //总签约情况:项目数
					 " sum(FetProject.totalInvestment) as totalInvestment," + //总签约情况:总投资
					 " sum(FetProject.bargainInvestment) as bargainInvestment," + //总签约情况:拟利用外资,TODO:bargainInvestment/合同外资, registInvestment/注册外资
					 " count(case when FetProject.sign='签订合同' then FetProject.id else null end) as contractTotalProjects," + //合同项目:项目数
					 " sum(case when FetProject.sign='签订合同' then FetProject.totalInvestment else 0 end) as contractTotalInvestment," + //合同项目:总投资
					 " sum(case when FetProject.sign='签订合同' then FetProject.bargainInvestment else 0 end) as contractBargainInvestment," + //合同项目:拟利用外资
					 " count(case when FetProject.sign='签订意向' then FetProject.id else null end) as orderTotalProjects," + //意向项目:项目数
					 " sum(case when FetProject.sign='签订意向' then FetProject.totalInvestment else null end) as orderTotalInvestment," + //意向项目:总投资
					 " sum(case when FetProject.sign='签订意向' then FetProject.bargainInvestment else null end) as orderBargainInvestment" + //意向项目:拟利用外资
					 " from fet_project FetProject" +
					 " where FetProject.fairName='" + JdbcUtils.resetQuot(fairName) + "'" +
					 " and FetProject.fairNumber=" + fairNumber +
					 " group by FetProject.county" +
					 " order by FetProject.county";
		List statData = getDatabaseService().executeQueryBySql(sql, 0, 0);
		//获取任务目标
		String hql = "from FetProjectTarget FetProjectTarget" +
					 " where FetProjectTarget.fairName='" + JdbcUtils.resetQuot(fairName) + "'" +
					 " and FetProjectTarget.fairNumber=" + fairNumber;
		List targets = getDatabaseService().findRecordsByHql(hql);
		for(int i=TOP_ROWS; ;i++) {
			//读取区县
			Cell[] cells = ws.getRow(i); //读取一行
			//输出记录
			String county = cells.length==0 ? null : cells[0].getContents().trim();
			if(county==null || county.equals("")) {
				break;
			}
			int task = 0; //总签约情况:目标任务
			int totalProjects = 0; //总签约情况:项目数
			float totalInvestment = 0; //总签约情况:总投资
			float bargainInvestment = 0; //总签约情况:拟利用外资
			
			int contractTask = 0; //合同项目:目标任务
			int contractTotalProjects = 0; //合同项目:项目数
			float contractTotalInvestment = 0; //合同项目:总投资
			float contractBargainInvestment = 0; //合同项目:拟利用外资
			
			int orderTask = 0; //意向项目:目标任务
			int orderTotalProjects = 0; //意向项目:项目数
			float orderTotalInvestment = 0; //意向项目:总投资
			float orderBargainInvestment = 0; //意向项目:拟利用外资
			
			if("合计".equals(county)) {
				if(targets!=null) {
					for(Iterator iterator = targets.iterator(); iterator.hasNext();) {
						FetProjectTarget target = (FetProjectTarget)iterator.next();
						task += target.getSignTarget();  //总签约情况:目标任务
						contractTask += target.getContractTarget(); //合同项目:目标任务
						orderTask += target.getOrderTarget(); //意向项目:目标任务
					}
				}
				if(statData!=null) {
					for(Iterator iterator = statData.iterator(); iterator.hasNext();) {
						SqlResult stat = (SqlResult)iterator.next();
						
						totalProjects += stat.getInt("totalProjects"); //总签约情况:项目数
						totalInvestment += stat.getFloat("totalInvestment"); //总签约情况:总投资
						bargainInvestment += stat.getFloat("bargainInvestment"); //总签约情况:拟利用外资
						
						contractTotalProjects += stat.getInt("contractTotalProjects"); //合同项目:项目数
						contractTotalInvestment += stat.getFloat("contractTotalInvestment"); //合同项目:总投资
						contractBargainInvestment += stat.getFloat("contractBargainInvestment"); //合同项目:拟利用外资
						
						orderTotalProjects += stat.getInt("orderTotalProjects"); //意向项目:项目数
						orderTotalInvestment += stat.getFloat("orderTotalInvestment"); //意向项目:总投资
						orderBargainInvestment += stat.getFloat("orderBargainInvestment"); //意向项目:拟利用外资
					}
				}
			}
			else {
				FetProjectTarget target = (FetProjectTarget)ListUtils.findObjectByProperty(targets, "county", county);
				if(target!=null) {
					task = target.getSignTarget();  //总签约情况:目标任务
					contractTask = target.getContractTarget(); //合同项目:目标任务
					orderTask = target.getOrderTarget(); //意向项目:目标任务
				}
				if(statData!=null) {
					for(Iterator iterator = statData.iterator(); iterator.hasNext();) {
						SqlResult stat = (SqlResult)iterator.next();
						if(!county.equals(stat.get("county"))) {
							continue;
						}
						totalProjects = stat.getInt("totalProjects"); //总签约情况:项目数
						totalInvestment = stat.getFloat("totalInvestment"); //总签约情况:总投资
						bargainInvestment = stat.getFloat("bargainInvestment"); //总签约情况:拟利用外资
						
						contractTotalProjects = stat.getInt("contractTotalProjects"); //合同项目:项目数
						contractTotalInvestment = stat.getFloat("contractTotalInvestment"); //合同项目:总投资
						contractBargainInvestment = stat.getFloat("contractBargainInvestment"); //合同项目:拟利用外资
						
						orderTotalProjects = stat.getInt("orderTotalProjects"); //意向项目:项目数
						orderTotalInvestment = stat.getFloat("orderTotalInvestment"); //意向项目:总投资
						orderBargainInvestment = stat.getFloat("orderBargainInvestment"); //意向项目:拟利用外资
						break;
					}
				}
			}
			//输出一行,县别/总签约情况:目标任务/总签约情况:项目数/总签约情况:总投资/总签约情况:拟利用外资/总签约情况:占目标任务%
			//合同:目标任务/合同:项目数/合同:总投资/合同:拟利用外资/合同:占目标任务%
			//意向:目标任务/意向:项目数/意向:总投资/意向:拟利用外资/意向:占目标任务%
			try {
				//总签约情况:目标任务
				ws.addCell(new jxl.write.Number(1, i, task, ws.getCell(1, TOP_ROWS).getCellFormat()));
				//总签约情况:项目数
				ws.addCell(new jxl.write.Number(2, i, totalProjects, ws.getCell(2, TOP_ROWS).getCellFormat()));
				//总签约情况:总投资
				ws.addCell(new jxl.write.Number(3, i, totalInvestment, ws.getCell(3, TOP_ROWS).getCellFormat()));
				//总签约情况:拟利用外资
				ws.addCell(new jxl.write.Number(4, i, bargainInvestment, ws.getCell(4, TOP_ROWS).getCellFormat()));
				//总签约情况:占目标任务%
				ws.addCell(new jxl.write.Number(5, i, (task==0 ? 0 : totalProjects/task), ws.getCell(5, TOP_ROWS).getCellFormat()));
				
				//合同项目:目标任务
				ws.addCell(new jxl.write.Number(6, i, contractTask, ws.getCell(6, TOP_ROWS).getCellFormat()));
				//合同项目:项目数
				ws.addCell(new jxl.write.Number(7, i, contractTotalProjects, ws.getCell(7, TOP_ROWS).getCellFormat()));
				//合同项目:总投资
				ws.addCell(new jxl.write.Number(8, i, contractTotalInvestment, ws.getCell(8, TOP_ROWS).getCellFormat()));
				//合同项目:拟利用外资
				ws.addCell(new jxl.write.Number(9, i, contractBargainInvestment, ws.getCell(9, TOP_ROWS).getCellFormat()));
				//合同项目:占目标任务%
				ws.addCell(new jxl.write.Number(10, i, (contractTask==0 ? 0 : contractTotalProjects/contractTask), ws.getCell(10, TOP_ROWS).getCellFormat()));
				
				//意向项目:目标任务
				ws.addCell(new jxl.write.Number(11, i, orderTask, ws.getCell(11, TOP_ROWS).getCellFormat()));
				//意向项目:项目数
				ws.addCell(new jxl.write.Number(12, i, orderTotalProjects, ws.getCell(12, TOP_ROWS).getCellFormat()));
				//意向项目:总投资
				ws.addCell(new jxl.write.Number(13, i, orderTotalInvestment, ws.getCell(13, TOP_ROWS).getCellFormat()));
				//意向项目:拟利用外资
				ws.addCell(new jxl.write.Number(14, i, orderBargainInvestment, ws.getCell(14, TOP_ROWS).getCellFormat()));
				//意向项目:占目标任务%
				ws.addCell(new jxl.write.Number(15, i, (orderTask==0 ? 0 : orderTotalProjects/orderTask), ws.getCell(15, TOP_ROWS).getCellFormat()));
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 输出专场招商（期间）新接洽项目情况表
	 * @param ws
	 * @param fairName
	 * @param fairNumber
	 * @throws ServiceException
	 */
	private void writeFairPorjectsNewSheet(jxl.write.WritableSheet ws, String fairName, int fairNumber) throws ServiceException {
		int TOP_ROWS = 3; //从第5行开始输出数据
		//获取数据
		String hql = "from FetProject FetProject" +
					 " where FetProject.fairName='" + JdbcUtils.resetQuot(fairName) + "'" +
					 " and FetProject.fairNumber=" + fairNumber +
					 //" and FetProject.sign='初步接洽'" + //成果类别,初步接洽、签订意向、签订合同
					 " order by FetProject.name";
		int rowIndex = TOP_ROWS;
		for(int i=0; ;i+=100) {
			List data = getDatabaseService().findRecordsByHql(hql, i, 100); //每次取100条记录
			if(data==null || data.isEmpty()) {
				break;
			}
			//输出记录
			for(Iterator iterator = data.iterator(); iterator.hasNext();) {
				FetProject project = (FetProject)iterator.next();
				//输出一行,序号/项目名称/拟建设内容和规模/中方/外方/投资方式/投资总额/拟利用外资/成果类别/对接洽谈情况
				try {
					//序号
					ws.addCell(new jxl.write.Number(0, rowIndex, rowIndex - TOP_ROWS + 1, ws.getCell(0, TOP_ROWS).getCellFormat()));
					//项目名称
					ws.addCell(new jxl.write.Label(1, rowIndex, project.getName(), ws.getCell(1, TOP_ROWS).getCellFormat()));
					//建设内容和规模
					ws.addCell(new jxl.write.Label(2, rowIndex, project.getEnterpriseScale(), ws.getCell(2, TOP_ROWS).getCellFormat()));
					//中方
					ws.addCell(new jxl.write.Label(3, rowIndex, project.getChineseCompany(), ws.getCell(3, TOP_ROWS).getCellFormat()));
					//外方
					ws.addCell(new jxl.write.Label(4, rowIndex, project.getForeignCompany(), ws.getCell(4, TOP_ROWS).getCellFormat()));
					//投资方式
					ws.addCell(new jxl.write.Label(5, rowIndex, project.getInvestmentType(), ws.getCell(5, TOP_ROWS).getCellFormat()));
					//投资总额
					ws.addCell(new jxl.write.Number(6, rowIndex, project.getTotalInvestment(), ws.getCell(6, TOP_ROWS).getCellFormat()));
					//拟利用外资
					ws.addCell(new jxl.write.Number(7, rowIndex, project.getBargainInvestment(), ws.getCell(7, TOP_ROWS).getCellFormat()));
					//成果类别
					ws.addCell(new jxl.write.Label(8, rowIndex, project.getSignCategory(), ws.getCell(8, TOP_ROWS).getCellFormat()));
					//对接洽谈情况
					ws.addCell(new jxl.write.Label(9, rowIndex, project.getConsult(), ws.getCell(9, TOP_ROWS).getCellFormat()));
					ws.setRowView(rowIndex, ws.getRowView(TOP_ROWS).getSize());
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				rowIndex++;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.project.service.FetProjectService#exportPorjectEvolve(java.lang.String, int, javax.servlet.http.HttpServletResponse)
	 */
	public void exportPorjectEvolve(String fairName, int fairNumber, HttpServletResponse response) throws ServiceException {
		jxl.write.WritableWorkbook  wwb = null;
		jxl.Workbook rw = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "inline; filename=\"" + FileUtils.encodeFileName("专场招商签约外资项目进展情况表.xls", "utf-8") + "\"");
			//读入报表模板
			rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "fet/project/template/专场招商签约外资项目进展情况表.template.xls"));
			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(response.getOutputStream(), rw);
			jxl.write.WritableSheet ws = wwb.getSheet(0);
			int TOP_ROWS = 5; //从第6行开始输出数据
			//获取数据
			String hql = "from FetProject FetProject" +
						 " where FetProject.fairName='" + JdbcUtils.resetQuot(fairName) + "'" +
						 " and FetProject.fairNumber=" + fairNumber +
						 " order by FetProject.name";
			int rowIndex = TOP_ROWS;
			for(int i=0; ;i+=100) {
				List data = getDatabaseService().findRecordsByHql(hql, i, 100); //每次取100条记录
				if(data==null || data.isEmpty()) {
					break;
				}
				//输出记录
				for(Iterator iterator = data.iterator(); iterator.hasNext();) {
					FetProject project = (FetProject)iterator.next();
					//输出一行,序号,项目名称,建设规模和内容,中方,外方,合作方式,签约种类,投资总额,利用外资,升格时间,批办时间,批办总投,批办外资,实际到资,动工时间,投产时间,其它详细情况
					try {
						//序号
						ws.addCell(new jxl.write.Number(0, rowIndex, rowIndex - TOP_ROWS + 1, ws.getCell(0, TOP_ROWS).getCellFormat()));
						//项目名称
						ws.addCell(new jxl.write.Label(1, rowIndex, project.getName(), ws.getCell(1, TOP_ROWS).getCellFormat()));
						//建设内容和规模
						ws.addCell(new jxl.write.Label(2, rowIndex, project.getEnterpriseScale(), ws.getCell(2, TOP_ROWS).getCellFormat()));
						//中方
						ws.addCell(new jxl.write.Label(3, rowIndex, project.getChineseCompany(), ws.getCell(3, TOP_ROWS).getCellFormat()));
						//外方
						ws.addCell(new jxl.write.Label(4, rowIndex, project.getForeignCompany(), ws.getCell(4, TOP_ROWS).getCellFormat()));
						//合作方式
						ws.addCell(new jxl.write.Label(5, rowIndex, project.getInvestmentType(), ws.getCell(5, TOP_ROWS).getCellFormat()));
						//签约种类
						ws.addCell(new jxl.write.Label(6, rowIndex, project.getSignCategory(), ws.getCell(6, TOP_ROWS).getCellFormat()));
						//投资总额
						ws.addCell(new jxl.write.Number(7, rowIndex, project.getTotalInvestment(), ws.getCell(7, TOP_ROWS).getCellFormat()));
						//利用外资
						ws.addCell(new jxl.write.Number(8, rowIndex, project.getBargainInvestment(), ws.getCell(8, TOP_ROWS).getCellFormat()));
						//升格时间
						if(project.getUpgradeDate()!=null) {
							ws.addCell(new jxl.write.DateTime(9, rowIndex, project.getUpgradeDate(), ws.getCell(9, TOP_ROWS).getCellFormat()));
						}
						else {
							ws.addCell(new jxl.write.Label(9, rowIndex, "", ws.getCell(9, TOP_ROWS).getCellFormat()));
						}
						//批办时间
						if(project.getApprovalTime()!=null) {
							ws.addCell(new jxl.write.DateTime(10, rowIndex, project.getApprovalTime(), ws.getCell(10, TOP_ROWS).getCellFormat()));
						}
						else {
							ws.addCell(new jxl.write.Label(10, rowIndex, "", ws.getCell(10, TOP_ROWS).getCellFormat()));
						}
						//批办总投
						ws.addCell(new jxl.write.Number(11, rowIndex, 0, ws.getCell(11, TOP_ROWS).getCellFormat()));
						//批办外资
						ws.addCell(new jxl.write.Number(12, rowIndex, 0, ws.getCell(12, TOP_ROWS).getCellFormat()));
						//实际到资
						ws.addCell(new jxl.write.Number(13, rowIndex, 0, ws.getCell(13, TOP_ROWS).getCellFormat()));
						//动工时间
						if(project.getBuildingDate()!=null) {
							ws.addCell(new jxl.write.DateTime(14, rowIndex, project.getBuildingDate(), ws.getCell(14, TOP_ROWS).getCellFormat()));
						}
						else {
							ws.addCell(new jxl.write.Label(14, rowIndex, "", ws.getCell(14, TOP_ROWS).getCellFormat()));
						}
						//投产时间
						if(project.getCompeletTime()!=null) {
							ws.addCell(new jxl.write.DateTime(15, rowIndex, project.getCompeletTime(), ws.getCell(15, TOP_ROWS).getCellFormat()));
						}
						else {
							ws.addCell(new jxl.write.Label(15, rowIndex, "", ws.getCell(15, TOP_ROWS).getCellFormat()));
						}
						//其它详细情况
						ws.addCell(new jxl.write.Label(16, rowIndex, "", ws.getCell(16, TOP_ROWS).getCellFormat()));
						ws.setRowView(rowIndex, ws.getRowView(TOP_ROWS).getSize());
					}
					catch(Exception e) {
						Logger.exception(e);
					}
					rowIndex++;
				}
			}
			wwb.write();
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			//关闭Excel工作薄对象
			try {
				wwb.close();
			}
			catch(Exception e) {
				
			}
			//关闭只读的Excel对象
			rw.close();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.project.service.FetProjectService#exportInvestment(int, int, javax.servlet.http.HttpServletResponse)
	 */
	public void exportInvestment(int year, int month, HttpServletResponse response) throws ServiceException {
		jxl.write.WritableWorkbook  wwb = null;
		jxl.Workbook rw = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "inline; filename=\"" + FileUtils.encodeFileName("利用外资进度表.xls", "utf-8") + "\"");
			//读入报表模板
			rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "fet/project/template/利用外资进度表.template.xls"));
			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(response.getOutputStream(), rw);
			
			//获取统计数据
			InvestmentStat curYearInvestmentStat = retrieveInvestmentData(year, month);
			InvestmentStat prevYearInvestmentStat = retrieveInvestmentData(year-1, month);
			
			//输出第一个页面
			jxl.write.WritableSheet ws = wwb.getSheet(0);
			writeInvestmentSheet0(ws, curYearInvestmentStat, prevYearInvestmentStat, year, month);
			//输出第二个页面
			ws = wwb.getSheet(1);
			writeInvestmentSheet1(ws, curYearInvestmentStat, prevYearInvestmentStat, year, month);
			//输出第三个页面
			ws = wwb.getSheet(2);
			writeInvestmentSheet2(ws, curYearInvestmentStat, prevYearInvestmentStat, year, month);
			wwb.write();
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			//关闭Excel工作薄对象
			try {
				wwb.close();
			}
			catch(Exception e) {
				
			}
			//关闭只读的Excel对象
			rw.close();
		}
	}
	
	/**
	 * 输出利用外资进度表页面1
	 * @param ws
	 * @param curYearInvestmentStat
	 * @param prevYearInvestmentStat
	 */
	private void writeInvestmentSheet0(jxl.write.WritableSheet ws, InvestmentStat curYearInvestmentStat, InvestmentStat prevYearInvestmentStat, int year, int month) {
		int TOP_ROWS = 7; //从第8开始输出数据
		//获取任务目标
		for(int i=TOP_ROWS; ;i++) {
			//读取区县
			Cell[] cells = ws.getRow(i); //读取一行
			//输出记录
			String county = cells.length==0 ? null : cells[0].getContents().trim();
			if(county==null || county.equals("")) {
				break;
			}
			try {
				ws.setName(month + "月");
				ws.addCell(new jxl.write.DateTime(0, 1, DateTimeUtils.parseTimestamp(year + "-" + month + "-01 00:00:00", null), ws.getCell(0, 1).getCellFormat()));
				ws.addCell(new jxl.write.Label(5, 4, "1-" + month + "月", ws.getCell(5, 4).getCellFormat()));
				ws.addCell(new jxl.write.Label(13, 4, "1-" + month + "月", ws.getCell(13, 4).getCellFormat()));
				ws.addCell(new jxl.write.Label(21, 4, "1-" + month + "月", ws.getCell(21, 4).getCellFormat()));
				//输出一行,新批企业,去年同期,同比%,合同外资（验资口径）全年计划,1－5月实绩,1－5月去年同期,1－5月同比%,1－5月完成计划%,当月实绩,当月去年同期,当月同比%
				//							  实际到资（验资口径）全年计划,1－5月实绩,1－5月去年同期,1－5月同比%,1－5月完成计划%,当月实绩,当月去年同期,当月同比%
				//							  实际到资（可比口径）全年计划,1－5月实绩,1－5月去年同期,1－5月同比%,1－5月完成计划%,当月实绩,当月去年同期,当月同比%
				//新批企业
				int curTotalCompanies = curYearInvestmentStat.getInt(county, "totalCompanies");
				ws.addCell(new jxl.write.Number(1, i, curTotalCompanies, ws.getCell(1, TOP_ROWS).getCellFormat()));
				//新批企业
				int prevTotalCompanies = prevYearInvestmentStat.getInt(county, "totalCompanies");
				ws.addCell(new jxl.write.Number(2, i, prevTotalCompanies, ws.getCell(2, TOP_ROWS).getCellFormat()));
				//同比%
				ws.addCell(new jxl.write.Number(3, i, (prevTotalCompanies==0 ? 0 : curTotalCompanies/prevTotalCompanies), ws.getCell(3, TOP_ROWS).getCellFormat()));
				
				//合同外资（验资口径）全年计划
				float contractCheckYearPlan = curYearInvestmentStat.getFloat(county, "contractCheckYearPlan");
				ws.addCell(new jxl.write.Number(4, i, contractCheckYearPlan, ws.getCell(4, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float bargainInvestment = curYearInvestmentStat.getFloat(county, "bargainInvestment");
				ws.addCell(new jxl.write.Number(5, i, bargainInvestment, ws.getCell(5, TOP_ROWS).getCellFormat()));
				//1－5月去年同期
				float prevBargainInvestment = prevYearInvestmentStat.getFloat(county, "bargainInvestment");
				ws.addCell(new jxl.write.Number(6, i, prevBargainInvestment, ws.getCell(6, TOP_ROWS).getCellFormat()));
				//1－5月同比%
				ws.addCell(new jxl.write.Number(7, i, (prevBargainInvestment==0 ? 0 : bargainInvestment/prevBargainInvestment), ws.getCell(7, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float contractCheckPlan = curYearInvestmentStat.getFloat(county, "contractCheckPlan");
				ws.addCell(new jxl.write.Number(8, i, (contractCheckPlan==0 ? 0 : bargainInvestment/contractCheckPlan), ws.getCell(8, TOP_ROWS).getCellFormat()));
				//当月实绩
				float monthBargainInvestment = curYearInvestmentStat.getFloat(county, "monthBargainInvestment");
				ws.addCell(new jxl.write.Number(9, i, monthBargainInvestment, ws.getCell(9, TOP_ROWS).getCellFormat()));
				//当月去年同期
				float prevMonthBargainInvestment = prevYearInvestmentStat.getFloat(county, "monthBargainInvestment");
				ws.addCell(new jxl.write.Number(10, i, prevMonthBargainInvestment, ws.getCell(10, TOP_ROWS).getCellFormat()));
				//当月同比%
				ws.addCell(new jxl.write.Number(11, i, (prevMonthBargainInvestment==0 ? 0 : monthBargainInvestment/prevMonthBargainInvestment), ws.getCell(11, TOP_ROWS).getCellFormat()));
				
				//实际到资（验资口径）全年计划
				float receiveCheckYearPlan = curYearInvestmentStat.getFloat(county, "receiveCheckYearPlan");
				ws.addCell(new jxl.write.Number(12, i, receiveCheckYearPlan, ws.getCell(12, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float moneyChecked = curYearInvestmentStat.getFloat(county, "moneyChecked");
				ws.addCell(new jxl.write.Number(13, i, moneyChecked, ws.getCell(13, TOP_ROWS).getCellFormat()));
				//1－5月去年同期
				float prevMoneyChecked = prevYearInvestmentStat.getFloat(county, "moneyChecked");
				ws.addCell(new jxl.write.Number(14, i, moneyChecked, ws.getCell(14, TOP_ROWS).getCellFormat()));
				//1－5月同比%
				ws.addCell(new jxl.write.Number(15, i, (prevMoneyChecked==0 ? 0 : moneyChecked/prevMoneyChecked), ws.getCell(15, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float receiveCheckPlan = curYearInvestmentStat.getFloat(county, "receiveCheckPlan");
				ws.addCell(new jxl.write.Number(16, i, (receiveCheckPlan==0 ? 0 : moneyChecked/receiveCheckPlan), ws.getCell(16, TOP_ROWS).getCellFormat()));
				//当月实绩
				float monthMoneyChecked = curYearInvestmentStat.getFloat(county, "monthMoneyChecked");
				ws.addCell(new jxl.write.Number(17, i, monthMoneyChecked, ws.getCell(17, TOP_ROWS).getCellFormat()));
				//当月去年同期
				float prevMonthMoneyChecked = prevYearInvestmentStat.getFloat(county, "monthMoneyChecked");
				ws.addCell(new jxl.write.Number(18, i, prevMonthMoneyChecked, ws.getCell(18, TOP_ROWS).getCellFormat()));
				//当月同比%
				ws.addCell(new jxl.write.Number(19, i, (prevMonthMoneyChecked==0 ? 0 : monthMoneyChecked/prevMonthMoneyChecked), ws.getCell(19, TOP_ROWS).getCellFormat()));
				
				//实际到资（可比口径）全年计划
				float receiveYearPlan = curYearInvestmentStat.getFloat(county, "receiveYearPlan");
				ws.addCell(new jxl.write.Number(20, i, receiveYearPlan, ws.getCell(20, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float money = curYearInvestmentStat.getFloat(county, "money");
				ws.addCell(new jxl.write.Number(21, i, money, ws.getCell(21, TOP_ROWS).getCellFormat()));
				//1－5月去年同期
				float prevMoney = prevYearInvestmentStat.getFloat(county, "money");
				ws.addCell(new jxl.write.Number(22, i, money, ws.getCell(22, TOP_ROWS).getCellFormat()));
				//1－5月同比%
				ws.addCell(new jxl.write.Number(23, i, (prevMoney==0 ? 0 : money/prevMoney), ws.getCell(23, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float receivePlan = curYearInvestmentStat.getFloat(county, "receivePlan");
				ws.addCell(new jxl.write.Number(24, i, (receivePlan==0 ? 0 : money/receivePlan), ws.getCell(24, TOP_ROWS).getCellFormat()));
				//当月实绩
				float monthMoney = curYearInvestmentStat.getFloat(county, "monthMoney");
				ws.addCell(new jxl.write.Number(25, i, monthMoney, ws.getCell(25, TOP_ROWS).getCellFormat()));
				//当月去年同期
				float prevMonthMoney = prevYearInvestmentStat.getFloat(county, "monthMoney");
				ws.addCell(new jxl.write.Number(26, i, prevMonthMoney, ws.getCell(26, TOP_ROWS).getCellFormat()));
				//当月同比%
				ws.addCell(new jxl.write.Number(27, i, (prevMonthMoney==0 ? 0 : monthMoney/prevMonthMoney), ws.getCell(27, TOP_ROWS).getCellFormat()));
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}

	/**
	 * 输出利用外资进度表页面2
	 * @param ws
	 * @param curYearInvestmentStat
	 * @param prevYearInvestmentStat
	 */
	private void writeInvestmentSheet1(jxl.write.WritableSheet ws, InvestmentStat curYearInvestmentStat, InvestmentStat prevYearInvestmentStat, int year, int month) {
		int TOP_ROWS = 7; //从第8开始输出数据
		//获取任务目标
		for(int i=TOP_ROWS; ;i++) {
			//读取区县
			Cell[] cells = ws.getRow(i); //读取一行
			//输出记录
			String county = cells.length==0 ? null : cells[0].getContents().trim();
			if(county==null || county.equals("")) {
				break;
			}
			try {
				ws.setName(month + "月无当月");
				ws.addCell(new jxl.write.DateTime(0, 1, DateTimeUtils.parseTimestamp(year + "-" + month + "-01 00:00:00", null), ws.getCell(0, 1).getCellFormat()));
				ws.addCell(new jxl.write.Label(5, 4, "1-" + month + "月", ws.getCell(5, 4).getCellFormat()));
				ws.addCell(new jxl.write.Label(10, 4, "1-" + month + "月", ws.getCell(10, 4).getCellFormat()));
				ws.addCell(new jxl.write.Label(15, 4, "1-" + month + "月", ws.getCell(15, 4).getCellFormat()));
				ws.addCell(new jxl.write.Label(20, 4, "1-" + month + "月", ws.getCell(20, 4).getCellFormat()));
				//输出一行,新批企业,去年同期,同比%,合同外资（验资口径）全年计划,1－5月实绩,1－5月去年同期,1－5月同比%,1－5月完成计划%
				//							  合同外资（可比口径）全年计划,1－5月实绩,1－5月去年同期,1－5月同比%,1－5月完成计划%
				//							  实际到资（验资口径）全年计划,1－5月实绩,1－5月去年同期,1－5月同比%,1－5月完成计划%
				//							  实际到资（可比口径）全年计划,1－5月实绩,1－5月去年同期,1－5月同比%,1－5月完成计划%
				//新批企业
				int curTotalCompanies = curYearInvestmentStat.getInt(county, "totalCompanies");
				ws.addCell(new jxl.write.Number(1, i, curTotalCompanies, ws.getCell(1, TOP_ROWS).getCellFormat()));
				//新批企业
				int prevTotalCompanies = prevYearInvestmentStat.getInt(county, "totalCompanies");
				ws.addCell(new jxl.write.Number(2, i, prevTotalCompanies, ws.getCell(2, TOP_ROWS).getCellFormat()));
				//同比%
				ws.addCell(new jxl.write.Number(3, i, (prevTotalCompanies==0 ? 0 : curTotalCompanies/prevTotalCompanies), ws.getCell(3, TOP_ROWS).getCellFormat()));
				
				//合同外资（验资口径）全年计划
				float contractCheckYearPlan = curYearInvestmentStat.getFloat(county, "contractCheckYearPlan");
				ws.addCell(new jxl.write.Number(4, i, contractCheckYearPlan, ws.getCell(4, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float bargainInvestment = curYearInvestmentStat.getFloat(county, "bargainInvestment");
				ws.addCell(new jxl.write.Number(5, i, bargainInvestment, ws.getCell(5, TOP_ROWS).getCellFormat()));
				//1－5月去年同期
				float prevBargainInvestment = prevYearInvestmentStat.getFloat(county, "bargainInvestment");
				ws.addCell(new jxl.write.Number(6, i, prevBargainInvestment, ws.getCell(6, TOP_ROWS).getCellFormat()));
				//1－5月同比%
				ws.addCell(new jxl.write.Number(7, i, (prevBargainInvestment==0 ? 0 : bargainInvestment/prevBargainInvestment), ws.getCell(7, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float contractCheckPlan = curYearInvestmentStat.getFloat(county, "contractCheckPlan");
				ws.addCell(new jxl.write.Number(8, i, (contractCheckPlan==0 ? 0 : bargainInvestment/contractCheckPlan), ws.getCell(8, TOP_ROWS).getCellFormat()));
				
				//合同外资（可比口径）全年计划
				float contractYearPlan = curYearInvestmentStat.getFloat(county, "contractYearPlan");
				ws.addCell(new jxl.write.Number(9, i, contractYearPlan, ws.getCell(9, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float totalInvestment = curYearInvestmentStat.getFloat(county, "totalInvestment");
				ws.addCell(new jxl.write.Number(10, i, totalInvestment, ws.getCell(10, TOP_ROWS).getCellFormat()));
				//1－5月去年同期
				float prevTotalInvestment = prevYearInvestmentStat.getFloat(county, "totalInvestment");
				ws.addCell(new jxl.write.Number(11, i, prevTotalInvestment, ws.getCell(11, TOP_ROWS).getCellFormat()));
				//1－5月同比%
				ws.addCell(new jxl.write.Number(12, i, (prevTotalInvestment==0 ? 0 : totalInvestment/prevTotalInvestment), ws.getCell(12, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float contractPlan = curYearInvestmentStat.getFloat(county, "contractPlan");
				ws.addCell(new jxl.write.Number(13, i, (contractPlan==0 ? 0 : totalInvestment/contractPlan), ws.getCell(13, TOP_ROWS).getCellFormat()));
				
				//实际到资（验资口径）全年计划
				float receiveCheckYearPlan = curYearInvestmentStat.getFloat(county, "receiveCheckYearPlan");
				ws.addCell(new jxl.write.Number(14, i, receiveCheckYearPlan, ws.getCell(14, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float moneyChecked = curYearInvestmentStat.getFloat(county, "moneyChecked");
				ws.addCell(new jxl.write.Number(15, i, moneyChecked, ws.getCell(15, TOP_ROWS).getCellFormat()));
				//1－5月去年同期
				float prevMoneyChecked = prevYearInvestmentStat.getFloat(county, "moneyChecked");
				ws.addCell(new jxl.write.Number(16, i, moneyChecked, ws.getCell(16, TOP_ROWS).getCellFormat()));
				//1－5月同比%
				ws.addCell(new jxl.write.Number(17, i, (prevMoneyChecked==0 ? 0 : moneyChecked/prevMoneyChecked), ws.getCell(17, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float receiveCheckPlan = curYearInvestmentStat.getFloat(county, "receiveCheckPlan");
				ws.addCell(new jxl.write.Number(18, i, (receiveCheckPlan==0 ? 0 : moneyChecked/receiveCheckPlan), ws.getCell(18, TOP_ROWS).getCellFormat()));
				
				//实际到资（可比口径）全年计划
				float receiveYearPlan = curYearInvestmentStat.getFloat(county, "receiveYearPlan");
				ws.addCell(new jxl.write.Number(19, i, receiveYearPlan, ws.getCell(19, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float money = curYearInvestmentStat.getFloat(county, "money");
				ws.addCell(new jxl.write.Number(20, i, money, ws.getCell(20, TOP_ROWS).getCellFormat()));
				//1－5月去年同期
				float prevMoney = prevYearInvestmentStat.getFloat(county, "money");
				ws.addCell(new jxl.write.Number(21, i, money, ws.getCell(21, TOP_ROWS).getCellFormat()));
				//1－5月同比%
				ws.addCell(new jxl.write.Number(22, i, (prevMoney==0 ? 0 : money/prevMoney), ws.getCell(22, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float receivePlan = curYearInvestmentStat.getFloat(county, "receivePlan");
				ws.addCell(new jxl.write.Number(23, i, (receivePlan==0 ? 0 : money/receivePlan), ws.getCell(23, TOP_ROWS).getCellFormat()));
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}

	/**
	 * 输出利用外资进度表页面3
	 * @param ws
	 * @param curYearInvestmentStat
	 * @param prevYearInvestmentStat
	 */
	private void writeInvestmentSheet2(jxl.write.WritableSheet ws, InvestmentStat curYearInvestmentStat, InvestmentStat prevYearInvestmentStat, int year, int month) {
		int TOP_ROWS = 7; //从第8开始输出数据
		//获取任务目标
		for(int i=TOP_ROWS; ;i++) {
			//读取区县
			Cell[] cells = ws.getRow(i); //读取一行
			//输出记录
			String county = cells.length==0 ? null : cells[0].getContents().trim();
			if(county==null || county.equals("")) {
				break;
			}
			try {
				ws.setName(month + "月无同比");
				ws.addCell(new jxl.write.DateTime(0, 1, DateTimeUtils.parseTimestamp(year + "-" + month + "-01 00:00:00", null), ws.getCell(0, 1).getCellFormat()));
				//输出一行,新批企业,去年同期,同比%,合同外资（验资口径）全年计划,1－5月实绩,1－5月完成计划%,当月实绩
				//							  实际到资（验资口径）全年计划,1－5月实绩,1－5月完成计划%,当月实绩
				//							  合同外资（可比口径）全年计划,1－5月实绩,1－5月完成计划%,当月实绩
				//							  实际到资（可比口径）全年计划,1－5月实绩,1－5月完成计划%,当月实绩
				//新批企业
				int curTotalCompanies = curYearInvestmentStat.getInt(county, "totalCompanies");
				ws.addCell(new jxl.write.Number(1, i, curTotalCompanies, ws.getCell(1, TOP_ROWS).getCellFormat()));
				
				//合同外资（验资口径）全年计划
				float contractCheckYearPlan = curYearInvestmentStat.getFloat(county, "contractCheckYearPlan");
				ws.addCell(new jxl.write.Number(2, i, contractCheckYearPlan, ws.getCell(2, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float bargainInvestment = curYearInvestmentStat.getFloat(county, "bargainInvestment");
				ws.addCell(new jxl.write.Number(3, i, bargainInvestment, ws.getCell(3, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float contractCheckPlan = curYearInvestmentStat.getFloat(county, "contractCheckPlan");
				ws.addCell(new jxl.write.Number(4, i, (contractCheckPlan==0 ? 0 : bargainInvestment/contractCheckPlan), ws.getCell(4, TOP_ROWS).getCellFormat()));
				//当月实绩
				float monthBargainInvestment = curYearInvestmentStat.getFloat(county, "monthBargainInvestment");
				ws.addCell(new jxl.write.Number(5, i, monthBargainInvestment, ws.getCell(5, TOP_ROWS).getCellFormat()));

				//实际到资（验资口径）全年计划
				float receiveCheckYearPlan = curYearInvestmentStat.getFloat(county, "receiveCheckYearPlan");
				ws.addCell(new jxl.write.Number(6, i, receiveCheckYearPlan, ws.getCell(6, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float moneyChecked = curYearInvestmentStat.getFloat(county, "moneyChecked");
				ws.addCell(new jxl.write.Number(7, i, moneyChecked, ws.getCell(7, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float receiveCheckPlan = curYearInvestmentStat.getFloat(county, "receiveCheckPlan");
				ws.addCell(new jxl.write.Number(8, i, (receiveCheckPlan==0 ? 0 : moneyChecked/receiveCheckPlan), ws.getCell(8, TOP_ROWS).getCellFormat()));
				//当月实绩
				float monthMoneyChecked = curYearInvestmentStat.getFloat(county, "monthMoneyChecked");
				ws.addCell(new jxl.write.Number(9, i, monthMoneyChecked, ws.getCell(9, TOP_ROWS).getCellFormat()));
				
				//合同外资（可比口径）全年计划
				float contractYearPlan = curYearInvestmentStat.getFloat(county, "contractYearPlan");
				ws.addCell(new jxl.write.Number(10, i, contractYearPlan, ws.getCell(10, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float totalInvestment = curYearInvestmentStat.getFloat(county, "totalInvestment");
				ws.addCell(new jxl.write.Number(11, i, totalInvestment, ws.getCell(11, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float contractPlan = curYearInvestmentStat.getFloat(county, "contractPlan");
				ws.addCell(new jxl.write.Number(12, i, (contractPlan==0 ? 0 : totalInvestment/contractPlan), ws.getCell(12, TOP_ROWS).getCellFormat()));
				//当月实绩
				float monthTotalInvestment = curYearInvestmentStat.getFloat(county, "monthTotalInvestment");
				ws.addCell(new jxl.write.Number(13, i, monthTotalInvestment, ws.getCell(13, TOP_ROWS).getCellFormat()));
				
				//实际到资（可比口径）全年计划
				float receiveYearPlan = curYearInvestmentStat.getFloat(county, "receiveYearPlan");
				ws.addCell(new jxl.write.Number(14, i, receiveYearPlan, ws.getCell(14, TOP_ROWS).getCellFormat()));
				//1－5月实绩
				float money = curYearInvestmentStat.getFloat(county, "money");
				ws.addCell(new jxl.write.Number(15, i, money, ws.getCell(15, TOP_ROWS).getCellFormat()));
				//1－5月完成计划%
				float receivePlan = curYearInvestmentStat.getFloat(county, "receivePlan");
				ws.addCell(new jxl.write.Number(16, i, (receivePlan==0 ? 0 : money/receivePlan), ws.getCell(16, TOP_ROWS).getCellFormat()));
				//当月实绩
				float monthMoney = curYearInvestmentStat.getFloat(county, "monthMoney");
				ws.addCell(new jxl.write.Number(17, i, monthMoney, ws.getCell(17, TOP_ROWS).getCellFormat()));
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 获取数据
	 * @param year
	 * @param month
	 * @return
	 */
	private InvestmentStat retrieveInvestmentData(int year, int month) {
		List[] data = new List[3];
		//当前月
		Calendar curMonth = Calendar.getInstance();
		curMonth.set(Calendar.YEAR, year);
		curMonth.set(Calendar.MONTH, month - 1);
		curMonth.set(Calendar.DAY_OF_MONTH, 1);
		//下月
		Calendar nextMonth = Calendar.getInstance();
		nextMonth.setTimeInMillis(curMonth.getTimeInMillis());
		nextMonth.add(Calendar.MONTH, 1);
		//元月
		Calendar jan = Calendar.getInstance();
		jan.setTimeInMillis(curMonth.getTimeInMillis());
		jan.set(Calendar.MONTH, 0);
		//获取当年：当月新批企业,当月合同外资（验资口径）实绩,1-当月合同外资（验资口径）实绩
		String sql = "select FetProject.county as county," +
					 " count(FetProject.id) as totalCompanies," + //总签约情况:项目数
					 " sum(FetProject.bargainInvestment) as bargainInvestment," + //1-当月合同外资（验资口径）实绩
					 " sum(case when FetProject.approvalTime>=DATE(" + DateTimeUtils.formatDate(new Date(curMonth.getTimeInMillis()), null) + ") then FetProject.bargainInvestment else 0 end) as monthBargainInvestment," + //当月合同外资（验资口径）实绩
					 " sum(FetProject.totalInvestment) as totalInvestment," + //1-当月合同外资（可比口径）实绩
					 " sum(case when FetProject.approvalTime>=DATE(" + DateTimeUtils.formatDate(new Date(curMonth.getTimeInMillis()), null) + ") then FetProject.totalInvestment else 0 end) as monthTotalInvestment" + //当月合同外资（可比口径）实绩
					 " from fet_project FetProject" +
					 " where FetProject.approvalTime>=DATE(" + DateTimeUtils.formatDate(new Date(jan.getTimeInMillis()), null) + ")" +
					 " and FetProject.approvalTime<DATE(" + DateTimeUtils.formatDate(new Date(nextMonth.getTimeInMillis()), null) + ")" +
					 " group by FetProject.county" +
					 " order by FetProject.county";
		data[0] = getDatabaseService().executeQueryBySql(sql, 0, 0);
		//获取当年：实际到资（验资口径）实绩,实际到资（可比口径）实绩							
		sql = "select FetProject.county as county," +
			  " sum(FetProjectInvestment.money) as money," + //1-当月实际到资（可比口径）实绩
		  	  " sum(FetProjectInvestment.moneyChecked) as moneyChecked," + //1-当月实际到资（验资口径）实绩
		  	  " sum(case when FetProjectInvestment.receiveTime>=DATE(" + DateTimeUtils.formatDate(new Date(curMonth.getTimeInMillis()), null) + ") then FetProjectInvestment.money else 0 end) as monthMoney," + //当月实际到资（可比口径）实绩
		  	  " sum(case when FetProjectInvestment.receiveTime>=DATE(" + DateTimeUtils.formatDate(new Date(curMonth.getTimeInMillis()), null) + ") then FetProjectInvestment.moneyChecked else 0 end) as monthMoneyChecked" + //当月实际到资（验资口径）实绩
			  " from fet_project FetProject left join fet_project_investment FetProjectInvestment on FetProject.id=FetProjectInvestment.projectId" +
			  " where FetProjectInvestment.receiveTime>=DATE(" + DateTimeUtils.formatDate(new Date(jan.getTimeInMillis()), null) + ")" +
			  " and FetProjectInvestment.receiveTime<DATE(" + DateTimeUtils.formatDate(new Date(nextMonth.getTimeInMillis()), null) + ")" +
			  " group by FetProject.county" +
			  " order by FetProject.county";
		data[1] = getDatabaseService().executeQueryBySql(sql, 0, 0);
		//获取当年计划
		sql = "select FetPlan.county as county," +
			  " sum(FetPlan.contractCheckPlan) as contractCheckYearPlan," + //合同外资计划(验资口径)年累计
			  " sum(FetPlan.receiveCheckPlan) as receiveCheckYearPlan," + //实际到资计划(验资口径)年累计
			  " sum(FetPlan.contractPlan) as contractYearPlan," + //合同外资计划(可比口径)年累计
			  " sum(FetPlan.receivePlan) as receiveYearPlan," + //实际到资计划(可比口径)年累计
			  " sum(case when FetPlan.planMonth<=" + month + " then FetPlan.contractCheckPlan else 0 end) as contractCheckPlan," + //合同外资计划(验资口径)1-当月累计
			  " sum(case when FetPlan.planMonth<=" + month + " then FetPlan.receiveCheckPlan else 0 end) as receiveCheckPlan," + //实际到资计划(验资口径)1-当月累计
			  " sum(case when FetPlan.planMonth<=" + month + " then FetPlan.contractPlan else 0 end) as contractPlan," + //合同外资计划(可比口径)1-当月累计
			  " sum(case when FetPlan.planMonth<=" + month + " then FetPlan.receivePlan else 0 end) as receivePlan," + //实际到资计划(可比口径)1-当月累计
			  " sum(case when FetPlan.planMonth=" + month + " then FetPlan.contractCheckPlan else 0 end) as contractCheckMonthPlan," + //合同外资计划(验资口径)当月累计
			  " sum(case when FetPlan.planMonth=" + month + " then FetPlan.receiveCheckPlan else 0 end) as receiveCheckMonthPlan," + //实际到资计划(验资口径)当月累计
			  " sum(case when FetPlan.planMonth=" + month + " then FetPlan.contractPlan else 0 end) as contractMonthPlan," + //合同外资计划(可比口径)当月累计
			  " sum(case when FetPlan.planMonth=" + month + " then FetPlan.receivePlan else 0 end) as receiveMonthPlan" + //实际到资计划(可比口径)当月累计
		 	  " from fet_project_plan FetPlan" +
			  " where FetPlan.planYear=" + year +
			  " group by FetPlan.county" +
			  " order by FetPlan.county";
		data[2] = getDatabaseService().executeQueryBySql(sql, 0, 0);
		InvestmentStat investmentStat = new InvestmentStat();
		investmentStat.setData(data);
		return investmentStat;
	}
	
	/**
	 * 
	 * @author linchuan
	 *
	 */
	private class InvestmentStat {
		private List[] data;

		public int getInt(String county, String propertyName) {
			Number number = (Number)getProperty(county, propertyName);
			return number==null ? 0 : number.intValue();
		}
		
		public float getFloat(String county, String propertyName) {
			Number number = (Number)getProperty(county, propertyName);
			return number==null ? 0 : number.floatValue();
		}
		
		/**
		 * 获取属性
		 * @param county
		 * @param propertyName
		 * @return
		 */
		public Object getProperty(String county, String propertyName) {
			int i=0;
			for(; i<data.length; i++) {
				if(data[i]==null || data[i].isEmpty()) {
					continue;
				}
				SqlResult record = (SqlResult)data[i].iterator().next();
				if(record!=null &&
				   record.containsColumn(propertyName)) {
					break;
				}
			}
			if(i==data.length) {
				return null;
			}
			if(county.equals("总计")) {
				float total = 0;
				for(Iterator iterator = data[i].iterator(); iterator.hasNext();) {
					SqlResult record = (SqlResult)iterator.next();
					total += ((Number)record.get(propertyName)).floatValue();
				}
				return new Float(total);
			}
			else {
				for(Iterator iterator = data[i].iterator(); iterator.hasNext();) {
					SqlResult record = (SqlResult)iterator.next();
					if(county.equals(record.get("county"))) {
						return record.get(propertyName);
					}
				}
			}
			return null;
		}
		
		/**
		 * @return the data
		 */
		public List[] getData() {
			return data;
		}

		/**
		 * @param data the data to set
		 */
		public void setData(List[] data) {
			this.data = data;
		}
	}
}