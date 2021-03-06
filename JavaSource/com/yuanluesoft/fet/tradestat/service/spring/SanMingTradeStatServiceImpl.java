package com.yuanluesoft.fet.tradestat.service.spring;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.yuanluesoft.fet.tradestat.pojo.FetCompany;
import com.yuanluesoft.fet.tradestat.pojo.FetCounty;
import com.yuanluesoft.fet.tradestat.pojo.TradeExport;
import com.yuanluesoft.fet.tradestat.pojo.TradeImport;
import com.yuanluesoft.fet.tradestat.service.TradeStatService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author yuanluesoft
 *
 */
public class SanMingTradeStatServiceImpl extends TradeStatServiceImpl implements TradeStatService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.tradestat.service.TradeStatService#isCountyQueryByCode()
	 */
	public boolean isCountyQueryByCode() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.tradestat.service.TradeStatService#importData(java.lang.String, boolean, int, int)
	 */
	public void importData(String dataFileName, boolean isExport, int year, int month) throws ServiceException {
		InputStream is = null;
		Workbook rwb = null;
		try {
			is = new FileInputStream(dataFileName);
			rwb = Workbook.getWorkbook(is);
			//获得第一个Sheet中的数据
			Sheet rs = rwb.getSheet(0);
			int rows = rs.getRows();
			//读取日期
			Cell[] cells = rs.getRow(1); //读取第二行
			String dateText = cells[0].getContents();
			//解析日期
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(formatter.parse(dateText.replaceAll("\"", "")).getTime());
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
			String county = null;
			for(int i=5; i<rows; i++) { //从第6行开始读取
				//0.序号 1.海关码 2.企业名称 3.当月出口 4.去年同期 5.比增 6.累计出口 7.去年同期 8.比增 9.当月进口 10.去年同期 11.比增 12.累计进口 13.去年同期 14.比增
				cells = rs.getRow(i); //读取一行
				if(cells.length==0) {
					break;
				}
				String companyCode = cells[1].getContents();
				if("0".equals(companyCode)) { //海关码=0,本行是区县信息
					county = cells[2].getContents().trim();
					continue;
				}
				TradeExport tradeExport = new TradeExport();
				tradeExport.setId(UUIDLongGenerator.generateId());
				tradeExport.setCompanyCode(companyCode); //企业编号
				tradeExport.setCompanyName(cells[2].getContents().trim()); //单位名称
				tradeExport.setMonthlyTotal(parseDouble(cells[3].getContents())); //本月数
				tradeExport.setLastYearMonthlyTotal(parseDouble(cells[4].getContents())); //同期数
				tradeExport.setMonthlyGrowthRate(parseDouble(cells[5].getContents())); //同期比增
				tradeExport.setYearTotal(parseDouble(cells[6].getContents())); //累计数
				tradeExport.setLastYearTotal(parseDouble(cells[7].getContents())); //同期累计数
				tradeExport.setGrowthRate(parseDouble(cells[8].getContents())); //累计比增
				tradeExport.setCounty(county); //区县
				//tradeExport.setCountyCode(cells[7].getContents()); //县码
				//tradeExport.setIsState("1".equals(cells[8].getContents()) ? '1' : '0'); //国有
				//tradeExport.setIsMachine("1".equals(cells[9].getContents()) ? '1' : '0'); //机电
				//tradeExport.setIsImportant("1".equals(cells[10].getContents()) ? '1' : '0'); //重点企业
				//tradeExport.setCountryCode(cells[11].getContents()); //投资国别
				//tradeExport.setDevelopmentAreaCode(cells[12].getContents()); //开发区
				tradeExport.setDataYear(year); //年份
				tradeExport.setDataMonth(month); //月份
				try {
					getDatabaseService().saveRecord(tradeExport);
				}
				catch(Exception e) {
					//保存失败,删除原有的记录,重新保存
					String hql = "from TradeExport TradeExport" +
								 " where TradeExport.companyCode='" + JdbcUtils.resetQuot(tradeExport.getCompanyCode()) + "'" +
								 " and TradeExport.dataYear=" + tradeExport.getDataYear() +
								 " and TradeExport.dataMonth=" + tradeExport.getDataMonth();
					getDatabaseService().deleteRecordsByHql(hql);
					getDatabaseService().saveRecord(tradeExport);
				}

				TradeImport tradeImport = new TradeImport();
				tradeImport.setId(UUIDLongGenerator.generateId());
				tradeImport.setCompanyCode(companyCode); //企业编号
				tradeImport.setCompanyName(cells[2].getContents().trim()); //单位名称
				tradeImport.setMonthlyTotal(parseDouble(cells[9].getContents())); //本月数
				tradeImport.setLastYearMonthlyTotal(parseDouble(cells[10].getContents())); //同期数
				tradeImport.setMonthlyGrowthRate(parseDouble(cells[11].getContents())); //同期比增
				tradeImport.setYearTotal(parseDouble(cells[12].getContents())); //累计数
				tradeImport.setLastYearTotal(parseDouble(cells[13].getContents())); //同期累计数
				tradeImport.setGrowthRate(parseDouble(cells[14].getContents())); //累计比增
				tradeImport.setCounty(county); //区县
				//tradeImport.setCountyCode(cells[7].getContents()); //县码
				//tradeImport.setIsState("1".equals(cells[8].getContents()) ? '1' : '0'); //国有
				//tradeImport.setIsMachine("1".equals(cells[9].getContents()) ? '1' : '0'); //机电
				//tradeImport.setIsImportant("1".equals(cells[10].getContents()) ? '1' : '0'); //重点企业
				//tradeImport.setCountryCode(cells[11].getContents()); //投资国别
				//tradeImport.setDevelopmentAreaCode(cells[12].getContents()); //开发区
				tradeImport.setDataYear(year); //年份
				tradeImport.setDataMonth(month); //月份
				try {
					getDatabaseService().saveRecord(tradeImport);
				}
				catch(Exception e) {
					//保存失败,删除原有的记录,重新保存
					String hql = "from TradeImport TradeImport" +
								 " where TradeImport.companyCode='" + JdbcUtils.resetQuot(tradeImport.getCompanyCode()) + "'" +
								 " and TradeImport.dataYear=" + tradeImport.getDataYear() +
								 " and TradeImport.dataMonth=" + tradeImport.getDataMonth();
					getDatabaseService().deleteRecordsByHql(hql);
					getDatabaseService().saveRecord(tradeImport);
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			try {
				rwb.close();
			}
			catch(Exception e) {
				
			}
			try {
				is.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.tradestat.service.TradeStatService#initData()
	 */
	public void initData() throws ServiceException {
		InputStream is = null;
		Workbook rwb = null;
		try {
			is = new FileInputStream("c:/smdata.xls");
			rwb = Workbook.getWorkbook(is);
			//获得第一个Sheet中的数据
			Sheet rs = rwb.getSheet(0);
			int rows = rs.getRows();
			//读取日期
			Cell[] cells = rs.getRow(1); //读取第二行
			String county = null;
			for(int i=5; i<rows; i++) { //从第6行开始读取
				//0.序号 1.海关码 2.企业名称 3.当月出口 4.去年同期 5.比增 6.累计出口 7.去年同期 8.比增 9.当月进口 10.去年同期 11.比增 12.累计进口 13.去年同期 14.比增
				cells = rs.getRow(i); //读取一行
				if(cells.length==0) {
					break;
				}
				String companyCode = cells[1].getContents();
				if("0".equals(companyCode)) { //海关码=0,本行是区县信息
					county = cells[2].getContents().trim();
					FetCounty fetCounty = new FetCounty();
					fetCounty.setId(UUIDLongGenerator.generateId());
					fetCounty.setLoginName(county);
					fetCounty.setName(county);
					fetCounty.setPassword(new FetCompanyServiceImpl().cryptPassword("123456"));
					try {
						getDatabaseService().saveRecord(fetCounty);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					continue;
				}
				FetCompany company = new FetCompany();
				company.setId(UUIDLongGenerator.generateId());
				company.setPassword(new FetCompanyServiceImpl().cryptPassword(companyCode));
				company.setCode(companyCode); //企业编号
				company.setLoginName(companyCode);
				company.setCounty(county);
				company.setName(cells[2].getContents().trim()); //单位名称
				try {
					getDatabaseService().saveRecord(company);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			try {
				rwb.close();
			}
			catch(Exception e) {
				
			}
			try {
				is.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.tradestat.service.TradeStatService#exportExportData(java.util.List, java.util.List, java.util.List, java.util.List, java.lang.String, java.lang.String, int, int, javax.servlet.http.HttpServletResponse)
	 */
	public void exportExportData(List countyCodes, List countyNames, List developmentAreaCodes, List developmentAreaNames, String companyCode, String companyName, int year, int month, HttpServletResponse response) throws ServiceException {
		jxl.write.WritableWorkbook  wwb = null;
		jxl.Workbook rw = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "inline; filename=\"" + FileUtils.encodeFileName("出口数据.xls", "utf-8") + "\"");
			//读入报表模板
			rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "fet/tradestat/template/export.template.xls"));

			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(response.getOutputStream(), rw);
			jxl.write.WritableSheet ws = wwb.getSheet(0);
			
			final int TOP_ROWS = 4; //从第5行开始输出数据
			
			//获取数据
			String where = null;
			if(countyNames!=null && !countyNames.isEmpty()) {
				where = "TradeExport.county" + (countyNames.size()==1 ? "='" + countyNames.get(0) + "'" : " in ('" + ListUtils.join(countyNames, "','", false) + "')");
			}
			if(companyCode!=null && !companyCode.equals("")) {
				where = (where==null ? "" : where + " and ") + "TradeExport.companyCode='" + JdbcUtils.resetQuot(companyCode) + "'";
			}
			if(year>0) {
				where = (where==null ? "" : where + " and ") + "TradeExport.dataYear=" + year;
			}
			if(month>0) {
				where = (where==null ? "" : where + " and ") + "TradeExport.dataMonth=" + month;
			}
			if(companyName!=null && !companyName.equals("")) {
				where = (where==null ? "" : where + " and ") + "TradeExport.companyName like '%" + JdbcUtils.resetQuot(companyName) + "%'";
			}
			String hql = "from TradeExport TradeExport" +
						 (where==null ? "" : " where " + where) +
						 " order by TradeExport.companyCode";
			int rowIndex = TOP_ROWS + 1;
			for(int i=0; ;i+=100) {
				List data = getDatabaseService().findRecordsByHql(hql, i, 100); //每次取100条记录
				if(data==null || data.isEmpty()) {
					break;
				}
				//输出记录
				for(Iterator iterator = data.iterator(); iterator.hasNext();) {
					TradeExport tradeExport = (TradeExport)iterator.next();
					writeExportData(ws, tradeExport, rowIndex++, TOP_ROWS);
				}
			}
			//删除第一行,模板数据
			ws.removeRow(TOP_ROWS);
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
	 * 输出一条出口数据
	 * @param wwb
	 * @param tradeExport
	 * @throws Exception
	 */
	private void writeExportData(jxl.write.WritableSheet ws, TradeExport tradeExport, int rowIndex, int firstRowIndex) throws Exception {
		//企业编号
		jxl.write.Label label = new jxl.write.Label(0, rowIndex, tradeExport.getCompanyCode(), ws.getCell(0, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//单位名称
		label = new jxl.write.Label(1, rowIndex, tradeExport.getCompanyName(), ws.getCell(1, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//本月数
		jxl.write.Number labelNumber = new jxl.write.Number(2, rowIndex, tradeExport.getMonthlyTotal(), ws.getCell(2, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//累计数
		labelNumber = new jxl.write.Number(3, rowIndex, tradeExport.getYearTotal(), ws.getCell(3, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//同期数
		labelNumber = new jxl.write.Number(4, rowIndex, tradeExport.getLastYearMonthlyTotal(), ws.getCell(4, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//同期累计数
		labelNumber = new jxl.write.Number(5, rowIndex, tradeExport.getLastYearTotal(), ws.getCell(5, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//累计比增
		labelNumber = new jxl.write.Number(6, rowIndex, tradeExport.getGrowthRate(), ws.getCell(6, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//县码
		label = new jxl.write.Label(7, rowIndex, tradeExport.getCountyCode(), ws.getCell(7, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//国有
		label = new jxl.write.Label(8, rowIndex, tradeExport.getIsState() + "", ws.getCell(8, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//机电
		label = new jxl.write.Label(9, rowIndex, tradeExport.getIsMachine() + "", ws.getCell(9, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//重点企业
		label = new jxl.write.Label(10, rowIndex, tradeExport.getIsImportant() + "", ws.getCell(10, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//投资国别
		label = new jxl.write.Label(11, rowIndex, tradeExport.getCountryCode(), ws.getCell(11, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//开发区
		label = new jxl.write.Label(12, rowIndex, tradeExport.getDevelopmentAreaCode(), ws.getCell(12, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//年份
		labelNumber = new jxl.write.Number(13, rowIndex, tradeExport.getDataYear(), ws.getCell(13, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//月份
		labelNumber = new jxl.write.Number(14, rowIndex, tradeExport.getDataMonth(), ws.getCell(14, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.tradestat.service.TradeStatService#exportImportData(java.util.List, java.util.List, java.util.List, java.util.List, java.lang.String, java.lang.String, int, int, javax.servlet.http.HttpServletResponse)
	 */
	public void exportImportData(List countyCodes, List countyNames, List developmentAreaCodes, List developmentAreaNames, String companyCode, String companyName, int year, int month, HttpServletResponse response) throws ServiceException {
		jxl.write.WritableWorkbook  wwb = null;
		jxl.Workbook rw = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "inline; filename=\"" + FileUtils.encodeFileName("进口数据.xls", "utf-8") + "\"");
			//读入报表模板
			rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "fet/tradestat/template/import.template.xls"));

			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(response.getOutputStream(), rw);
			jxl.write.WritableSheet ws = wwb.getSheet(0);
			
			final int TOP_ROWS = 4; //从第5行开始输出数据
			
			//获取数据
			String where = null;
			if(countyNames!=null && !countyNames.isEmpty()) {
				where = "TradeImport.county" + (countyNames.size()==1 ? "='" + countyNames.get(0) + "'" : " in ('" + ListUtils.join(countyNames, "','", false) + "')");
			}
			if(companyCode!=null && !companyCode.equals("")) {
				where = (where==null ? "" : where + " and ") + "TradeImport.companyCode='" + JdbcUtils.resetQuot(companyCode) + "'";
			}
			if(year>0) {
				where = (where==null ? "" : where + " and ") + "TradeImport.dataYear=" + year;
			}
			if(month>0) {
				where = (where==null ? "" : where + " and ") + "TradeImport.dataMonth=" + month;
			}
			if(companyName!=null && !companyName.equals("")) {
				where = (where==null ? "" : where + " and ") + "TradeImport.companyName like '%" + JdbcUtils.resetQuot(companyName) + "%'";
			}
			String hql = "from TradeImport TradeImport" +
						 (where==null ? "" : " where " + where) +
						 " order by TradeImport.companyCode";
			int rowIndex = TOP_ROWS + 1;
			for(int i=0; ;i+=100) {
				List data = getDatabaseService().findRecordsByHql(hql, i, 100); //每次取100条记录
				if(data==null || data.isEmpty()) {
					break;
				}
				//输出记录
				for(Iterator iterator = data.iterator(); iterator.hasNext();) {
					TradeImport tradeImport = (TradeImport)iterator.next();
					writeImportData(ws, tradeImport, rowIndex++, TOP_ROWS);
				}
			}
			//删除第一行,模板数据
			ws.removeRow(TOP_ROWS);
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
	 * 输出一条进口数据
	 * @param wwb
	 * @param tradeImport
	 * @throws Exception
	 */
	private void writeImportData(jxl.write.WritableSheet ws, TradeImport tradeImport, int rowIndex, int firstRowIndex) throws Exception {
		//企业编号
		jxl.write.Label label = new jxl.write.Label(0, rowIndex, tradeImport.getCompanyCode(), ws.getCell(0, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//单位名称
		label = new jxl.write.Label(1, rowIndex, tradeImport.getCompanyName(), ws.getCell(1, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//本月数
		jxl.write.Number labelNumber = new jxl.write.Number(2, rowIndex, tradeImport.getMonthlyTotal(), ws.getCell(2, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//累计数
		labelNumber = new jxl.write.Number(3, rowIndex, tradeImport.getYearTotal(), ws.getCell(3, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//同期数
		labelNumber = new jxl.write.Number(4, rowIndex, tradeImport.getLastYearMonthlyTotal(), ws.getCell(4, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//同期累计数
		labelNumber = new jxl.write.Number(5, rowIndex, tradeImport.getLastYearTotal(), ws.getCell(5, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//累计比增
		labelNumber = new jxl.write.Number(6, rowIndex, tradeImport.getGrowthRate(), ws.getCell(6, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//县码
		label = new jxl.write.Label(7, rowIndex, tradeImport.getCountyCode(), ws.getCell(7, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//国有
		label = new jxl.write.Label(8, rowIndex, tradeImport.getIsState() + "", ws.getCell(8, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//机电
		label = new jxl.write.Label(9, rowIndex, tradeImport.getIsMachine() + "", ws.getCell(9, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//重点企业
		label = new jxl.write.Label(10, rowIndex, tradeImport.getIsImportant() + "", ws.getCell(10, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//投资国别
		label = new jxl.write.Label(11, rowIndex, tradeImport.getCountryCode(), ws.getCell(11, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//开发区
		label = new jxl.write.Label(12, rowIndex, tradeImport.getDevelopmentAreaCode(), ws.getCell(12, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//年份
		labelNumber = new jxl.write.Number(13, rowIndex, tradeImport.getDataYear(), ws.getCell(13, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//月份
		labelNumber = new jxl.write.Number(14, rowIndex, tradeImport.getDataMonth(), ws.getCell(14, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
	}
	
	/**
	 * 字符串转换成浮点数
	 * @param value
	 * @return
	 */
	private double parseDouble(String value) {
		try {
			return (value==null || value.equals("") ? 0 : Double.parseDouble(value));
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * 字符串转换成整数
	 * @param value
	 * @return
	 */
	protected int parseInteger(String value) {
		return (value==null || value.equals("") ? 0 : Integer.parseInt(value));
	}
	
	public static void main(String[] args) {
		try {
			new SanMingTradeStatServiceImpl().importData("C:/Documents and Settings/linchuan.KDSOFT-HP/桌面/三明市进出口企业总值表(海关数).xls", true, 2008, 1);
		}
		catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
