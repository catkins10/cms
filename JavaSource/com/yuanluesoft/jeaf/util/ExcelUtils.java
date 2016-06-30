package com.yuanluesoft.jeaf.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback;

/**
 * 
 * @author linchuan
 *
 */
public class ExcelUtils {

	/**
	 * 输出一行数据
	 * @param ws
	 * @param values 值列表
	 * @param rowIndex
	 * @param formatRowIndex 格式参考行的序号
	 * @param pojoIndex 记录序号,从1开始
	 * @throws Exception
	 */
	public static void writeExcelRow(WritableSheet ws, Object[] values, int rowIndex, int formatRowIndex) throws Exception {
		for(int i=0; i<values.length; i++) {
			CellFormat cellFormat = ws.getCell(i, formatRowIndex).getCellFormat();
			if(values[i]==null) { //空值
				Label label = new Label(i, rowIndex, " ", cellFormat);
				ws.addCell(label);
			}
			else if(values[i] instanceof Date) { //日期
				jxl.write.DateTime jxlDateTime = new jxl.write.DateTime(i, rowIndex, new java.util.Date(((Date)values[i]).getTime()), cellFormat);
				ws.addCell(jxlDateTime);
			}
			else if(values[i] instanceof Number) { //数字
				jxl.write.Number jxlNumber = new jxl.write.Number(i, rowIndex, ((Number)values[i]).doubleValue(), cellFormat);
				ws.addCell(jxlNumber);
			}
			else {
				Label label = new Label(i, rowIndex, "" + values[i], cellFormat);
				ws.addCell(label);
			}
		}
	}
	
	/**
	 * 导入电子表格(POI)
	 * @param sheet
	 * @param callback
	 */
	public static void importExcelSheet(HSSFSheet sheet, ExcelSheetImportCallback callback) throws Exception {
		List colNames = new ArrayList(); //表头行
		int mergedRows = 1;
		for(int i=sheet.getFirstRowNum(); i<=sheet.getLastRowNum(); i+=mergedRows) {
			Row row = sheet.getRow(i);
			mergedRows = getMergedRows(sheet, i, row.getFirstCellNum()); //获取被合并的行数
			//如果表头为空,检查是否表头,判断依据该行必须要有“项目名称”单元格
			if(colNames.isEmpty()) {
				for(int j=row.getFirstCellNum(); j<=row.getLastCellNum(); j++) {
					String content = getCellContent(sheet, i, i+mergedRows-1, j);
					if(content==null) {
						content = "";
					}
					else {
						content = StringUtils.removePunctuation(content);
					}
					colNames.add(content);
				}
				if(!callback.isHeader(colNames)) { //检查是否表头
					colNames.clear(); //不是表头
				}
				continue;
			}
			//创建新记录
			Object record = callback.createNewRecord();
			//读取记录
			for(int j=row.getFirstCellNum(); j<=row.getLastCellNum(); j++) {
				if(j-row.getFirstCellNum()>=colNames.size()) {
					continue;
				}
				String colName = (String)colNames.get(j-row.getFirstCellNum());
				String content = getCellContent(sheet, i, i + mergedRows - 1, j);
				if(content==null || (content=content.trim()).isEmpty()) {
					continue;
				}
				callback.setRecordPropertyValue(record, colName, content);
			}
			//保存记录
			callback.saveRecrd(record);
		}
	}
	
	/**
	 * 导入电子表格
	 * @param sheet
	 * @param callback
	 */
	public static void importExcelSheet(Sheet sheet, ExcelSheetImportCallback callback) throws Exception {
		Range[] ranges = sheet.getMergedCells();
		List colNames = new ArrayList(); //表头行
		int mergedRows = 1;
		for(int i=0; i<sheet.getRows(); i+=mergedRows) {
			Cell[] cells = sheet.getRow(i);
			mergedRows = getMergedRows(ranges, i, 0); //获取被合并的行数
			//如果表头为空,检查是否表头,判断依据该行必须要有“项目名称”单元格
			if(colNames.isEmpty()) {
				for(int j=0; j<cells.length; j++) {
					String content = getCellContent(sheet, i, i+mergedRows-1, j);
					if(content==null) {
						content = "";
					}
					else {
						content = StringUtils.removePunctuation(content);
					}
					colNames.add(content);
				}
				if(!callback.isHeader(colNames)) { //检查是否表头
					colNames.clear(); //不是表头
				}
				continue;
			}
			//创建新记录
			Object record = callback.createNewRecord();
			//读取记录
			for(int j=0; j<cells.length; j++) {
				if(j>=colNames.size()) {
					continue;
				}
				String colName = (String)colNames.get(j);
				String content = getCellContent(sheet, i, i + mergedRows - 1, j);
				if(content==null || (content=content.trim()).isEmpty()) {
					continue;
				}
				callback.setRecordPropertyValue(record, colName, content);
			}
			//保存记录
			callback.saveRecrd(record);
		}
	}
	
	/**
	 * 获取单元格内容
	 * @param sheet
	 * @param beginRow
	 * @param endRow
	 * @param col
	 * @return
	 */
	private static String getCellContent(HSSFSheet sheet, int beginRow, int endRow, int col) {
		String content = null;
		for(int i=beginRow; i<=endRow; i++) {
			Row row = sheet.getRow(i);
			String text = ExcelUtils.getCellString(row.getCell(col));
			if(text!=null && !text.trim().isEmpty()) {
				content = (content==null ? "" : content) + text.trim().replaceAll("\\xa0", "").replaceAll(new String(new char[]{65533}), "");
			}
		}
		return content;
	}
	
	/**
	 * 获取单元格内容
	 * @param sheet
	 * @param beginRow
	 * @param endRow
	 * @param col
	 * @return
	 */
	private static String getCellContent(Sheet sheet, int beginRow, int endRow, int col) {
		String content = null;
		for(int i=beginRow; i<=endRow; i++) {
			Cell[] cells = sheet.getRow(i);
			String text = ExcelUtils.getCellString(cells, col);
			if(text!=null && !text.trim().isEmpty()) {
				content = (content==null ? "" : content) + text.trim().replaceAll("\\xa0", "").replaceAll(new String(new char[]{65533}), "");
			}
		}
		return content;
	}
	
	/**
	 * 获取合并的行数
	 * @param sheet
	 * @param row
	 * @param col
	 * @return
	 */
	private static int getMergedRows(HSSFSheet sheet, int row, int col) {
		for(int i=0; i<sheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
			if(cellRangeAddress.getFirstRow()<=row && cellRangeAddress.getFirstColumn()<=col && cellRangeAddress.getLastRow()>=row && cellRangeAddress.getLastColumn()>=col) {
				return cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow() + 1;
			}
		}
		return 1;
	}
	
	/**
	 * 获取合并的行数
	 * @param ranges
	 * @param row
	 * @param col
	 * @return
	 */
	private static int getMergedRows(Range[] ranges, int row, int col) {
		for(int i=0; i<ranges.length; i++) {
			Cell topLeftCell = ranges[i].getTopLeft();
			Cell bottomRight = ranges[i].getBottomRight();
			if(topLeftCell.getRow()<=row && topLeftCell.getColumn()<=col && bottomRight.getRow()>=row && bottomRight.getColumn()>=col) {
				return bottomRight.getRow() - topLeftCell.getRow() + 1;
			}
		}
		return 1;
	}
	
	/**
	 * 获取单元格文本内容
	 * @param cell
	 * @return
	 */
	public static String getCellString(org.apache.poi.ss.usermodel.Cell cell) {
		if(cell==null) {
			return null;
		}
		switch(cell.getCellType()) {
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK:
			return null;
			
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
			return "" + cell.getBooleanCellValue();

		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR:
			return "" + cell.getErrorCellValue();

		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(cell) || HSSFDateUtil.isCellInternalDateFormatted(cell)) { //判断是否为日期
				long time = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).getTime();
				return DateTimeUtils.formatTimestamp(new Timestamp(time), time % (24*3600*1000)==0 ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss");
			}
			return StringUtils.format(new Double(cell.getNumericCellValue()), null, null);

		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		}
		return null;
	}
	
	/**
	 * 获取单元内容
	 * @param cells
	 * @param index
	 * @return
	 */
	public static String getCellString(Cell[] cells, int index) {
		return cells.length<=index ? null : cells[index].getContents();
	}
	

	/**
	 * 获取单元内容：双精度数字
	 * @param cells
	 * @param index
	 * @return
	 */
	public static double getCellDouble(Cell[] cells, int index) {
		try {
			return cells.length<=index ? 0 : Double.parseDouble(cells[index].getContents().replaceAll(",", ""));
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取单元内容：整数
	 * @param cells
	 * @param index
	 * @return
	 */
	public static int getCellInteger(Cell[] cells, int index) {
		try {
			return cells.length<=index ? 0 : Integer.parseInt(cells[index].getContents().replaceAll(",", ""));
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取单元内容：日期
	 * @param cells
	 * @param index
	 * @param format
	 * @return
	 */
	public static Date getCellDate(Cell[] cells, int index, String format) {
		try {
			return cells.length<=index ? null : DateTimeUtils.parseDate(cells[index].getContents(), format);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取单元内容：时间
	 * @param cells
	 * @param index
	 * @param format
	 * @return
	 */
	public static Timestamp getCellTimestamp(Cell[] cells, int index, String format) {
		try {
			return cells.length<=index ? null : DateTimeUtils.parseTimestamp(cells[index].getContents(), format);
		}
		catch(Exception e) {
			return null;
		}
	}
}