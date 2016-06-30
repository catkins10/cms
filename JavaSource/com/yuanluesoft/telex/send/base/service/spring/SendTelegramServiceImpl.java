package com.yuanluesoft.telex.send.base.service.spring;

import java.io.File;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.telex.base.pojo.TelegramSn;
import com.yuanluesoft.telex.send.base.pojo.SendTelegram;
import com.yuanluesoft.telex.send.base.service.SendTelegramService;

/**
 * 
 * @author linchuan
 *
 */
public class SendTelegramServiceImpl extends BusinessServiceImpl implements SendTelegramService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof SendTelegram) {
			SendTelegram currentTelegram = (SendTelegram)record;
			if(currentTelegram.getSequence()!=null && !currentTelegram.getSequence().equals("")) {
				//调整当前电报之后的其他电报的顺序号
				int year = DateTimeUtils.getYear(currentTelegram.getCreated());
				String hql = "from SendTelegram SendTelegram" +
							 " where SendTelegram.id!=" + currentTelegram.getId() +
							 " and SendTelegram.isCryptic" + (currentTelegram.getIsCryptic()=='1' ? "=" : "!=") + "'1'" +
							 " and SendTelegram.created>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(currentTelegram.getCreated(), null) + ")" +
							 " and SendTelegram.created<TIMESTAMP(" + (year + 1) + "-01-01 00:00:00)" + //同一年
							 " order by SendTelegram.created";
				int sequence = Integer.parseInt(currentTelegram.getSequence());
				List telegrams = getDatabaseService().findRecordsByHql(hql);
				if(telegrams!=null && !telegrams.isEmpty()) {
					for(Iterator iterator = telegrams.iterator(); iterator.hasNext();) {
						SendTelegram telegram = (SendTelegram)iterator.next();
						telegram.setSequence((sequence++) + "");
						getDatabaseService().updateRecord(telegram);
					}
				}
				//修改参数配置中的当前顺序号
				hql = "from TelegramSn TelegramSn" +
					  " where TelegramSn.isSend='1'" +
					  " and TelegramSn.isCryptic" + (currentTelegram.getIsCryptic()=='1' ? "=" : "!=") +  "'1'" +
					  " and TelegramSn.year=" + year;
				TelegramSn telegramSn = (TelegramSn)getDatabaseService().findRecordByHql(hql);
				telegramSn.setSn(sequence - 1);
				getDatabaseService().updateRecord(telegramSn);
			}
		}
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.send.base.service.SendTelegramService#exportListing(int, int, boolean, javax.servlet.http.HttpServletResponse)
	 */
	public void exportListing(int year, int month, boolean isCryptic, HttpServletResponse response) throws ServiceException {
		jxl.write.WritableWorkbook  wwb = null;
		jxl.Workbook rw = null;
		try {
			String title = year + "年" + month + "月份密码电报发报核对清单";
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "inline; filename=\"" + URLEncoder.encode(title + ".xls", "UTF-8") + "\"");
			//读入报表模板
			rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "telex/send/cryptic/template/telex.template.xls"));

			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(response.getOutputStream(), rw);
			jxl.write.WritableSheet ws = wwb.getSheet(0);
			
			//标题
			jxl.write.Label label = new jxl.write.Label(0, 0, title, ws.getCell(0, 0).getCellFormat());
			ws.addCell(label);
			
			final int TOP_ROWS = 2; //从第3行开始输出数据
			
			//获取数据
			Date beginDate = DateTimeUtils.parseDate(year + "-" + month + "-1", "yyyy-M-d");
			Date endDate = DateTimeUtils.add(beginDate, Calendar.MONTH, 1);
			String hql = "from SendTelegram SendTelegram" +
						 " where SendTelegram.isCryptic" + (isCryptic ? "=" : "!=") + "'1'" +
						 " and SendTelegram.created>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")"  +
						 " and SendTelegram.created<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")"  +
						 " order by SendTelegram.created";
			int rowIndex = TOP_ROWS + 1;
			int totalNumber = 0; //份数累计
			int totalPages = 0; //页数累计
			for(int i=0; ;i+=100) {
				List telegrams = getDatabaseService().findRecordsByHql(hql, i, 100); //每次取100条记录
				if(telegrams==null || telegrams.isEmpty()) {
					break;
				}
				//输出记录
				for(Iterator iterator = telegrams.iterator(); iterator.hasNext();) {
					SendTelegram telegram = (SendTelegram)iterator.next();
					int number = 1; //telegram.getTelegramNumber();
					int pages = telegram.getPages(); //telegram.getTotalPages();
					totalNumber += number;
					totalPages += pages;
					ws.insertRow(rowIndex);
					writeTelegram(ws, telegram, number, pages, rowIndex++, TOP_ROWS);
				}
			}
			//删除第一行,模板数据
			ws.removeRow(TOP_ROWS);
			
			rowIndex--;
			//总份数
			jxl.write.Number labelNumber = new jxl.write.Number(4, rowIndex, totalNumber, ws.getCell(4, rowIndex).getCellFormat());
			ws.addCell(labelNumber);
			//总页数
			labelNumber = new jxl.write.Number(7, rowIndex, totalPages, ws.getCell(7, rowIndex).getCellFormat());
			ws.addCell(labelNumber);
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
	 * 输出一个电报
	 * @param wwb
	 * @param tradeExport
	 * @throws Exception
	 */
	private void writeTelegram(jxl.write.WritableSheet ws, SendTelegram telegram, int number, int pages, int rowIndex, int firstRowIndex) throws Exception {
		//日期
		jxl.write.Label label = new jxl.write.Label(0, rowIndex, DateTimeUtils.getDay(telegram.getCreated()) + "", ws.getCell(0, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//发电单位
		label = new jxl.write.Label(1, rowIndex, telegram.getFromUnitNames(), ws.getCell(1, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//榕机密收序号
		label = new jxl.write.Label(2, rowIndex, telegram.getSequence(), ws.getCell(2, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//标题
		label = new jxl.write.Label(3, rowIndex, telegram.getSubject(), ws.getCell(3, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//份数
		jxl.write.Number labelNumber = new jxl.write.Number(4, rowIndex, number, ws.getCell(4, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//等级
		label = new jxl.write.Label(5, rowIndex, telegram.getTelegramLevel(), ws.getCell(5, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//密级
		label = new jxl.write.Label(6, rowIndex, telegram.getSecurityLevel(), ws.getCell(6, firstRowIndex).getCellFormat());
		ws.addCell(label);
		//页数
		labelNumber = new jxl.write.Number(7, rowIndex, pages, ws.getCell(7, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		//备注
		label = new jxl.write.Label(8, rowIndex, telegram.getRemark(), ws.getCell(8, firstRowIndex).getCellFormat());
		ws.addCell(label);
	}
}
