package com.yuanluesoft.jeaf.report.excel.jxl;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.CellType;
import jxl.Range;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.report.excel.ExcelReportCallback;
import com.yuanluesoft.jeaf.report.excel.ExcelReportService;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReport;
import com.yuanluesoft.jeaf.report.excel.model.ExcelReportData;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.callback.FillParametersCallback;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;
import com.yuanluesoft.jeaf.view.util.ViewUtils;

/**
 * jxl宽度/32=pixel jxl高度/15=pixel
 * @author linchuan
 *
 */
public class ExcelReportServiceImpl implements ExcelReportService {
	private int pageWidth = 120; //页面宽度,默认A4的宽度
	private ImageService imageService; //图像服务
	private TemporaryFileManageService temporaryFileManageService; //临时文件管理

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportService#createExcelReport(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void createExcelReport(View view, String currentCategories, String searchConditions, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		//创建电子表格
		WritableWorkbook  wwb = null;
		Workbook rw = null;
		try {
			//获取标题
			String title = (String)view.getExtendParameter("print.title");
			if(title!=null && !title.isEmpty()) {
				title = StringUtils.fillParameters(title, false, false, false, "utf-8", null, request, null);
			}
			else {
				title = view.getTitle();
			}
		 	response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", (!"true".equals(request.getParameter("asAttachment")) ? "inline" : "attachment") + "; filename=\"" + FileUtils.encodeFileName(title + ".xls", "utf-8") + "\"");
			
			WritableSheet ws = null;
			String temporaryFilePath = null;
			int rowIndex = 1;
			ViewService viewService = ViewUtils.getViewService(view);
			//构造视图包
			ViewPackage viewPackage = new ViewPackage();
			view.setPageRows(Math.max(view.getPageRows(), 300)); //每次获取300条记录
			viewPackage.setView(view);
			viewPackage.setSearchConditions(searchConditions);
			viewPackage.setSearchMode(searchConditions!=null);
			viewPackage.setCategories(currentCategories);
			viewPackage.setViewMode(View.VIEW_DISPLAY_MODE_PRINT);
			for(int page=1; ; page++) {
				//设置当前页
				viewPackage.setCurPage(page);
				//获取记录
				viewService.retrieveViewPackage(viewPackage, view, 0, false, false, false, request, sessionInfo);
				if(page==1) { //第一页,创建电子表格,并插入表头
					//读入报表模板
					rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "jeaf/view/template/view.template.xls"));
					//创建可写入的Excel工作薄对象
					wwb = Workbook.createWorkbook(response.getOutputStream(), rw);
					ws = wwb.getSheet(0);

					//设置视图名称
					ws.addCell(new Label(0, 0, title, ws.getCell(0, 0).getCellFormat()));
					
					//填充标题行
					resetColumnWidth(view, pageWidth);
					for(int i=0; i<view.getColumns().size(); i++) {
						Column column = (Column)view.getColumns().get(i);
						ws.addCell(new Label(i, 1, StringUtils.filterHtmlElement(column.getTitle(), true), ws.getCell(0, 1).getCellFormat()));
						//设置列宽度
						ws.setColumnView(i, (int)Float.parseFloat(column.getWidth()));
					}
					ws.mergeCells(0, 0, view.getColumns().size() - 1, 0);
					
					//获取并输出记录
					if(ListUtils.findObjectByProperty(view.getColumns(), "type", "image")!=null) {
						temporaryFilePath = temporaryFileManageService.createTemporaryDirectory(null);
					}
				}
				if(viewPackage.getRecords()==null || viewPackage.getRecords().isEmpty()) {
					break;
				}
				//输出记录
				for(Iterator iterator = viewPackage.getRecords().iterator(); iterator.hasNext(); ) {
					Object record = iterator.next();
					addExcelReportRow(ws, view, record, rowIndex++, temporaryFilePath, request); //添加记录行
				}
				if(viewPackage.getRecords().size()<view.getPageRows() || page>=viewPackage.getPageCount()) {
					break;
				}
			}
			ws.removeRow(2);
			wwb.write();
			
			if(temporaryFilePath!=null) {
				FileUtils.deleteDirectory(temporaryFilePath);
			}
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
			try {
				rw.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 加入报表记录行
	 * @param ws
	 * @param view
	 * @param record
	 * @param rowIndex
	 * @param temporaryFilePath
	 * @param request
	 * @throws Exception
	 */
	private void addExcelReportRow(WritableSheet ws, View view, Object record, int rowIndex, String temporaryFilePath, HttpServletRequest request) throws Exception {
		//检查是否统计数据行
		String statisticTitle = (record instanceof Record ? (String)((Record)record).getExtendPropertyValue("statisticTitle") : null);
		int columnIndex = 0;
		if(statisticTitle!=null) { //是统计数据
			columnIndex = ((StatisticView)view).getHeadColCount(); //头部列数
			//设置对齐方式
			WritableCellFormat format = new WritableCellFormat(ws.getCell(0, 2).getCellFormat());
			Column column = (Column)view.getColumns().get(0);
			format.setAlignment(getAlignment(column));
			ws.addCell(new Label(0, rowIndex + 2, statisticTitle, format));
			if(columnIndex>1) {
				//创建空的单元格
				for(int i=1; i<columnIndex; i++) {
					ws.addCell(new Label(i, rowIndex + 2, statisticTitle, format));
				}
				//合并单元格
				ws.mergeCells(0, rowIndex + 2, columnIndex-1, rowIndex + 2);
			}
		}
		for(; columnIndex<view.getColumns().size(); columnIndex++) {
			try {
				Column column = (Column)view.getColumns().get(columnIndex);
				//设置对齐方式
				WritableCellFormat format = new WritableCellFormat(ws.getCell(0, 2).getCellFormat());
				format.setAlignment(getAlignment(column));
				if("rownum".equals(column.getType())) { //序号
					ws.addCell(new jxl.write.Number(columnIndex, rowIndex + 2, rowIndex, format));
				}
				else if("image".equals(column.getType())) { //图片
					ws.addCell(new Label(columnIndex, rowIndex + 2, "", format));
					String imageFilePath = Environment.getWebAppPath() + view.getApplicationName() + "/" + PropertyUtils.getProperty(record, column.getName());
					if(!imageFilePath.endsWith(".png")) {
						String fileName = imageFilePath.substring(imageFilePath.lastIndexOf('/') + 1);
						String newImageFilePath = temporaryFilePath + fileName + ".png";
						if(!FileUtils.isExists(newImageFilePath)) {
							imageService.convertImage(imageFilePath, newImageFilePath, "png");
						}
						imageFilePath = newImageFilePath;
					}
					Dimension imageSize = imageService.getImageDimension(imageFilePath);
					File imgFile = new File(imageFilePath);
					double cellWidth = ws.getColumnView(columnIndex).getSize() / 32; //jxl宽度/32=pixel
					double cellHeight = ws.getRowView(rowIndex + 2).getSize() / 15; //jxl高度/15=pixel
					WritableImage image = new WritableImage(columnIndex + (1 - imageSize.getWidth()/cellWidth) / 2, rowIndex + 1 + (1 - imageSize.getHeight()/cellHeight) / 2, imageSize.getWidth()/cellWidth, imageSize.getHeight()/cellHeight, imgFile);    
					ws.addImage(image);
				}
				else { //字段
					String value = ViewUtils.getViewFieldValue(view, column, record, false, request);
					if(column.isHideZero() && "0".equals(value)) { //隐藏"0"
						value = "";
					}
					if(column.getMaxCharCount()>0) { //有字数限制
						value = StringUtils.slice(value, column.getMaxCharCount(), column.getEllipsis());
					}
					ws.addCell(new Label(columnIndex, rowIndex + 2, value, format));
				}
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 获取对齐方式
	 * @param column
	 * @return
	 */
	private Alignment getAlignment(Column column) {
		if("center".equals(column.getAlign())) {
			return Alignment.CENTRE;
		}
		else if("right".equals(column.getAlign())) {
			return Alignment.RIGHT;
		}
		else {
			return Alignment.LEFT;
		}
	}
	
	/**
	 * 重新设置列宽度
	 * @param view
	 * @param pageWidth
	 * @param scale
	 */
	private void resetColumnWidth(View view, float pageWidth) {
		float totalWidth = 0;
		int countNullWidth = 0;
		for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			String width = column.getWidth();
			if(width==null || width.isEmpty()) {
				countNullWidth++;
				continue;
			}
			float newWidth;
			if(width.lastIndexOf('%')==-1) {
				newWidth = Float.parseFloat(width.replace("px", "")) * 0.15f;
			}
			else {
				newWidth = Float.parseFloat(width.substring(0, width.length() - 1)) * pageWidth / 15;
			}
			totalWidth += newWidth;
			column.setWidth(newWidth + "");
		}
		for(; pageWidth < totalWidth; pageWidth+=this.pageWidth); //如果页面宽度不够显示,加宽页面
		float otherColumnWidth = (pageWidth - totalWidth) / countNullWidth; //未指定尺寸的列,平均分配剩余的宽度
		for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			if(column.getWidth()==null || column.getWidth().isEmpty()) {
				column.setWidth((otherColumnWidth<=0 ? 20 : otherColumnWidth) + "");
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportService#writeExcelReport(java.lang.String, java.lang.String, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.report.excel.ExcelReportCallback)
	 */
	public void writeExcelReport(String templateFile, String outputName, HttpServletResponse response, ExcelReportCallback reportCallback) throws ServiceException {
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", "inline; filename=\"" + FileUtils.encodeFileName(outputName, "utf-8") + "\"");
		try {
			writeExcelReport(templateFile, response.getOutputStream(), reportCallback);
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.report.excel.ExcelReportService#writeExcelReport(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.report.excel.ExcelReportCallback)
	 */
	public void writeExcelReport(String templateFile, String outputFilePath, ExcelReportCallback reportCallback) throws ServiceException {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(outputFilePath);
			writeExcelReport(templateFile, output, reportCallback);
			output.close();
		}
		catch (Exception e) {
			if(output!=null) {
				try {
					output.close();
				}
				catch(Exception ex) {
					
				}
				FileUtils.deleteFile(outputFilePath); //删除错误的文件
			}
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 输出Excel报表
	 * @param templateFile
	 * @param output
	 * @param reportCallback
	 * @throws ServiceException
	 */
	private void writeExcelReport(String templateFile, OutputStream output, ExcelReportCallback reportCallback) throws ServiceException {
		WritableWorkbook  wwb = null;
		Workbook rw = null;
		try {
			rw = Workbook.getWorkbook(new File(templateFile)); //读入报表模板
			wwb = Workbook.createWorkbook(output, rw); //创建可写入的Excel工作薄对象
			for(int i=0; i<wwb.getSheets().length; i++) {
				WritableSheet ws = wwb.getSheet(i);
				writeExcelReportSheet(ws, reportCallback);
			}
			//输出报表
			wwb.write();
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		finally {
			try {
				wwb.close(); //关闭Excel工作薄对象
			}
			catch(Exception e) {
				
			}
			rw.close(); //关闭只读的Excel对象
		}
	}
	
	/**
	 * 输出报表页
	 * @param ws
	 * @param reportCallback
	 * @throws ServiceException
	 */
	private void writeExcelReportSheet(WritableSheet ws, ExcelReportCallback reportCallback) throws Exception {
		ExcelReport excelReport = reportCallback.getExcelReport(ws.getName());
		if(excelReport==null) {
			return;
		}
		Range[] ranges = ws.getMergedCells();
		//填充表头
		if(excelReport.getHeadRowNumbers()!=null) {
			String[] rowNumbers = excelReport.getHeadRowNumbers().split(",");
			for(int i=0; i<rowNumbers.length; i++) {
				int row = Integer.parseInt(rowNumbers[i]);
				writeExcelReportRow(ws, ranges, row, row, excelReport);
			}
		}
		
		//输出非表头和非格式参考行
		for(int row=0; row<ws.getRows(); row++) {
			if(("," + excelReport.getHeadRowNumbers() + "," + excelReport.getReferenceRowNumbers() + ",").indexOf("," + row + ",")==-1) {
				Cell[] cells = ws.getRow(row);
				//获取行数据
				String[] rowContents = new String[cells.length];
				boolean isNull = true;
				for(int i=0; i<cells.length; i++) {
					rowContents[i] = cells[i].getContents();
					if(rowContents[i]!=null && !rowContents[i].equals("")) {
						isNull = false;
					}
				}
				if(isNull) {
					continue;
				}
				//根据行数据获取报表数据
				ExcelReportData data = reportCallback.getExcelReportData(row, rowContents);
				//输出报表数据
				writeExcelReportRow(ws, ranges, row, row, data);
			}
		}
		
		//输出数据
		if(excelReport.getDataSet()!=null) {
			int dataRowNumber = excelReport.getDataRowNumber();
			for(Iterator iterator = excelReport.getDataSet().iterator(); iterator.hasNext();) {
				ExcelReportData data = (ExcelReportData)iterator.next();
				String[] rowNumbers = data.getReferenceRowNumbers().split(",");
				for(int i=0; i<rowNumbers.length; i++) {
					int row = Integer.parseInt(rowNumbers[i]);
					ws.insertRow(dataRowNumber); //插入数据行
					writeExcelReportRow(ws, ranges, row, dataRowNumber, data);
					if(data.isFixedRowHeight()) {
						ws.setRowView(dataRowNumber, ws.getRowView(row));
					}
					dataRowNumber++;
				}
			}
		}
		
		//删除格式参考行
		if(excelReport.getReferenceRowNumbers()!=null) {
			String[] rowNumbers = excelReport.getReferenceRowNumbers().split(",");
			for(int i=rowNumbers.length-1; i>=0; i--) {
				int row = Integer.parseInt(rowNumbers[i]);
				//清除备注
				for(int j=0; j<ws.getColumns(); j++) {
					Cell cell = ws.getCell(j, row);
					CellFeatures features = cell.getCellFeatures();
					if(features!=null) {
						features.removeComment();
					}
				}
				ws.removeRow(row);
			}
		}
	}
	
	/**
	 * 输出报表行
	 * @param ws
	 * @param referenceRow
	 * @param writeRow
	 * @param data
	 * @throws ServiceException
	 */
	private void writeExcelReportRow(WritableSheet ws, Range[] ranges, int referenceRow, int writeRow, Object data) throws Exception {
		for(int i=0; i<ws.getColumns(); i++) {
			Cell cell = ws.getCell(i, referenceRow);
			if(cell.getCellFormat()==null) {
				Logger.error("ExcelReportService error: cell format is null, sheet is " + ws.getName() + ", col is " + (i<26 ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(i,i+1) : "" + i) + ", row is " + referenceRow);
			}
			try {
				//检查是否合并的单元格
				Range currentRange = null;
				for(int j=0; ranges!=null && j<ranges.length; j++) {
					if(ranges[j].getTopLeft().getColumn()<=i &&
					   ranges[j].getTopLeft().getRow()<=referenceRow && 
					   ranges[j].getBottomRight().getColumn()>=i &&
					   ranges[j].getBottomRight().getRow()>=referenceRow) {
						currentRange = ranges[j];
						break;
					}
				}
				if(currentRange!=null && currentRange.getTopLeft()!=cell) {
					if(referenceRow==writeRow) { //原有的单元格
						continue;
					}
					Label label = new Label(i, writeRow, "", cell.getCellFormat());
					ws.addCell(label);
					if(currentRange.getBottomRight()==cell) {
						ws.mergeCells(currentRange.getTopLeft().getColumn(), writeRow, currentRange.getBottomRight().getColumn(), writeRow);
					}
					continue;
				}
				//获取字段值
				CellFeatures features = cell.getCellFeatures();
				String content = null;
				if(features==null || (content=features.getComment())==null) {
					content = cell.getContents();
				}
				if(content!=null) {
					FillParametersCallback callback = new FillParametersCallback() {
						public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request) {
							Object value = null;
							if(bean instanceof ExcelReport) {
								value = ((ExcelReport)bean).getPropertyValue(parameterName);
							}
							else if(bean instanceof ExcelReportData) {
								value = ((ExcelReportData)bean).getPropertyValue(parameterName);
							}
							else {
								return null;
							}
							if(value!=null) {
								return value;
							}
							int index = parameterName.indexOf(".");
							if(index==-1) {
								return null;
							}
							bean = (bean instanceof ExcelReport ? ((ExcelReport)bean).getPropertyMap() : ((ExcelReportData)bean).getPropertyMap()).get(parameterName.substring(0, index));
							try {
								return PropertyUtils.getProperty(bean, parameterName.substring(index + 1));
							} 
							catch(Exception e) {
								return null;
							}
						}
					};
					content = StringUtils.fillParameters(content, false, false, false, "utf-8", data, null, callback); //替换
				}
				//输出字段
				if((cell.getType().equals(CellType.NUMBER) || cell.getType().equals(CellType.NUMBER_FORMULA)) &&
				    content!=null && !content.equals("")) {
					try {
						jxl.write.Number number = new jxl.write.Number(i, writeRow, Double.parseDouble(content), cell.getCellFormat());
						ws.addCell(number);
					}
					catch(NumberFormatException ne) {
						if(cell.getCellFormat()==null) {
							Logger.error("ExcelReportService error: not number field, sheet is " + ws.getName() + ", col is " + (i<26 ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(i,i+1) : "" + i) + ", row is " + referenceRow);
						}
					}
				}
				else if((cell.getType().equals(CellType.DATE) || cell.getType().equals(CellType.DATE_FORMULA)) &&
						 content!=null && !content.equals("")) {
					jxl.write.DateTime date = new jxl.write.DateTime(i, writeRow, (content.length()<11 ? (Date)DateTimeUtils.parseDate(content, null) : (Date)DateTimeUtils.parseTimestamp(content, null)), cell.getCellFormat());
					ws.addCell(date);
				}
				else {
					Label label = new Label(i, writeRow, content, cell.getCellFormat());
					ws.addCell(label);
				}
				if(referenceRow==writeRow && features!=null) { //直接把数据写入到格式参考行
					features.removeComment(); //删除备注
				}
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	} 

	/**
	 * @return the pageWidth
	 */
	public int getPageWidth() {
		return pageWidth;
	}

	/**
	 * @param pageWidth the pageWidth to set
	 */
	public void setPageWidth(int pageWidth) {
		this.pageWidth = pageWidth;
	}

	/**
	 * @return the imageService
	 */
	public ImageService getImageService() {
		return imageService;
	}

	/**
	 * @param imageService the imageService to set
	 */
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	/**
	 * @return the temporaryFileManageService
	 */
	public TemporaryFileManageService getTemporaryFileManageService() {
		return temporaryFileManageService;
	}

	/**
	 * @param temporaryFileManageService the temporaryFileManageService to set
	 */
	public void setTemporaryFileManageService(
			TemporaryFileManageService temporaryFileManageService) {
		this.temporaryFileManageService = temporaryFileManageService;
	}
}