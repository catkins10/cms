package com.yuanluesoft.credit.enterprisecredit.trafficcredit.service.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yuanluesoft.credit.enterprisecredit.trafficcredit.forms.CarImportDateExcel;
import com.yuanluesoft.credit.enterprisecredit.trafficcredit.pojo.CarCredit;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author zyh
 *
 */
public class CarImportServiceImpl extends BusinessServiceImpl {
	private AttachmentService attachmentService; //附件服务

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public void importData(CarImportDateExcel excel, SessionInfo sessionInfo)
			throws ServiceException {
		
		List attachments = attachmentService.list(
				"credit/enterprisecredit/trafficcredit", "data", excel.getId(), false, 1,
				null);
		if (attachments == null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}

		Attachment attachment = (Attachment) attachments.get(0);//得到具体的附件对象
		try {
			importDate(attachment, sessionInfo);
			attachmentService.deleteAll("credit/enterprisecredit/trafficcredit",
					null, excel.getId());//删除上传的文件
		} catch (Exception e) {
			attachmentService.deleteAll("credit/enterprisecredit/trafficcredit",
					null, excel.getId());//删除上传的文件
			Logger.exception(e);
			throw new ServiceException("文件解析失败，请检查文件格式及数据类型是否正确");
		}

	}

	/**
	 * 信息导入	
	 * @param attachment
	 * @param sessionInfo
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ServiceException
	 */
	private void importDate(Attachment attachment, SessionInfo sessionInfo)
			throws Exception {
		Workbook workBook = null;
		Sheet sheet = null;
		Row tableTitle = null;
		int firstRow;
		int lastRow;
		String[] colName = new String[] { "处罚决定书案号", "单位", "当事人","车号", "案由" ,"案发时间" ,"缴款日期","强制措施","移交中队","处罚依据","处罚机关","罚款金额"};
		try {
			workBook = new XSSFWorkbook(new FileInputStream(attachment
					.getFilePath()));
		} catch (Exception ex) {
			workBook = new HSSFWorkbook(new FileInputStream(attachment
					.getFilePath()));
		}
		sheet = workBook.getSheetAt(0);// 得到工作表对象
		firstRow = sheet.getFirstRowNum();
		lastRow = sheet.getLastRowNum();
		tableTitle = sheet.getRow(firstRow);// 表头行
		int[] colNum = findTitleColNumInArray(tableTitle, colName);
		while (colNum[0] == -1) {//处罚决定书案号不能为空 
			firstRow++;
			if (firstRow > lastRow) {
				Logger.info("没有标题行！");
				return;
			}
			tableTitle = sheet.getRow(firstRow);
			colNum = findTitleColNumInArray(tableTitle, colName);
		}

		for (int contentRow = firstRow + 1; contentRow <= lastRow; contentRow++) {
			Row content = sheet.getRow(contentRow);// 内容行
			if (content == null) {
				continue;
			}
			if(getStringValue(content.getCell(colNum[0]))==null || getStringValue(content.getCell(colNum[0])).equals("")){
				continue;
			}
			CarCredit carCredit = new CarCredit();
			carCredit.setCreator(sessionInfo.getUserName());
			carCredit.setCreatorId(sessionInfo.getUserId());
			carCredit.setCreated(DateTimeUtils.now());
			carCredit.setId(UUIDLongGenerator.generateId());
			carCredit.setBookCode(getStringValue(content.getCell(colNum[0]))) ; //处罚决定书案号
			carCredit.setUnit(getStringValue(content.getCell(colNum[1]))); //单位
			carCredit.setPerson(getStringValue(content.getCell(colNum[2])));//当事人
			carCredit.setCarNo(getStringValue(content.getCell(colNum[3])));//车号
			carCredit.setReson(getStringValue(content.getCell(colNum[4])));//案由
			carCredit.setHappenTime(getStringValue(content.getCell(colNum[5])));//案发时间
			carCredit.setPayTime(getStringValue(content.getCell(colNum[6])));//缴款日期
			carCredit.setAuditor(getStringValue(content.getCell(colNum[7])));//强制措施
			carCredit.setTransfer(getStringValue(content.getCell(colNum[8])));//移交中队
			carCredit.setBasis(getStringValue(content.getCell(colNum[9])));//处罚依据
			carCredit.setDepartment(getStringValue(content.getCell(colNum[10])));//处罚机关
			carCredit.setMoney(getDoubleValue(content.getCell(colNum[11]))) ;//罚款金额
			save(carCredit);

		}

	}
	
	/**
	 * 获取单元格日期值。Excel中日期是用数字类型存储。展现依然是日期的样式
	 * @param cell 目标单元格
	 * @return
	 */
	public static Date getDateValue(Cell cell,String dataFormat){
		
		if(cell==null){
			return null;
		}
		 int cellType=cell.getCellType();
         switch(cellType){
         case Cell.CELL_TYPE_FORMULA:
        	 Workbook wb = cell.getSheet().getWorkbook();  
             CreationHelper crateHelper = wb.getCreationHelper();  
             FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();  
             String dates=getStringValue(evaluator.evaluateInCell(cell));
            if(dates!=null&&dates.trim().length()!=0){
               	try { 
               			return DateTimeUtils.parseDate(dates.trim(), dataFormat==null||dataFormat.isEmpty()?"yyyy-MM-dd":dataFormat);
               	} catch (ParseException e) {
               		e.printStackTrace();
               		return null;//无效格式
               	}
            } break;
           case Cell.CELL_TYPE_STRING:
        	                 String date=cell.getStringCellValue();
        	                 if(date!=null&&date.trim().length()!=0){
   			                 	try {
   			                 		 return new Date(new SimpleDateFormat(dataFormat).parse(date).getTime());
   			                     		//return DateTimeUtils.parseDate(date.trim(), dataFormat==null||dataFormat.isEmpty()?"yyyy-MM-dd":dataFormat);
   			                 	} catch (ParseException e) {
   			                 		e.printStackTrace();
   			                 		return null;//无效格式
   			                 	}
                             } break;
//         poi读取data日期格式时默认以数字格式存储。用HSSFDateUtil类转换
           case Cell.CELL_TYPE_NUMERIC:
        	   double d = cell.getNumericCellValue(); 
        	                if (HSSFDateUtil.isCellDateFormatted(cell)|| HSSFDateUtil.isCellInternalDateFormatted(cell)||DateUtil.isValidExcelDate(d)) {//判断是否为日期       	   
        	                    java.util.Date dateTemp = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
        	                    return new java.sql.Date(dateTemp.getTime());
        	                }else{
//      	                	不是正确的日期，取出数字直接转换
        	                	try{
        	                		java.util.Date dateTemp= HSSFDateUtil.getJavaDate(cell.getNumericCellValue()); 
        	                		return new java.sql.Date(dateTemp.getTime());
        	                	}catch(Exception e){
        	                		e.printStackTrace();
        	                	}
        	                }break;
           default:return null;//其他类型的不能转换成日期格式
         }
         return null;
	}

	/**
	 * 查找表头对应的列序号，不存在的列序号为-1.返回的序号和给定的列名顺序一致
	 * @param tableTitle 表头行
	 * @param rowName 表头行单元格名称
	 * @return
	 */
	public static int[] findTitleColNumInArray(Row tableTitle, String[] colName) {

		if (tableTitle == null || colName == null) {
			return null;
		} else {
			int[] colNum = new int[colName.length];
			for (int i = 0; i < colName.length; i++) {
				colNum[i] = -1;//默认为-1。即为-1时表示该值无效，导入文件中不存在该值
				int firstCell = tableTitle.getFirstCellNum();
				int lastCell = tableTitle.getLastCellNum();
				for (; firstCell <= lastCell; firstCell++) {
					if (tableTitle.getCell(firstCell) == null) {
						continue;
					} else {
						try {//不是字符串格式的单元格不符合规范，不提取
							String title = tableTitle.getCell(firstCell)
									.getStringCellValue();
							if (title == null || title.isEmpty()) {
								continue;
							}
							if (title.replaceAll("\\s*|\t|\r|\n", "").indexOf(
									colName[i]) != -1) {//过滤空格换行
								colNum[i] = firstCell;
								break;
							}
						} catch (Exception e) {
							continue;
						}
					}
				}
			}
			return colNum;
		}

	}

	/**
	 * 获取单元格的值并格式化成字符串类型
	 * @param cell
	 * @return
	 */
	public static String getStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		if(cell==null) {
			return null;
		}
		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_FORMULA:
			Workbook wb = cell.getSheet().getWorkbook();  
            CreationHelper crateHelper = wb.getCreationHelper();  
            FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();  
            return  getStringValue(evaluator.evaluateInCell(cell)); 
		case Cell.CELL_TYPE_BLANK:
			return null;
			
		case Cell.CELL_TYPE_BOOLEAN:
			return "" + cell.getBooleanCellValue();

		case Cell.CELL_TYPE_ERROR:
			return "" + cell.getErrorCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(cell) || HSSFDateUtil.isCellInternalDateFormatted(cell)) { //判断是否为日期
				long time = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).getTime();
				return DateTimeUtils.formatTimestamp(new Timestamp(time), time % (24*3600*1000)==0 ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss");
			}
			return StringUtils.format(new Double(cell.getNumericCellValue()), null, null);

		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		}
		return null;
	
	}

	/**
	 * 获取单元格的值并格式化成double类型
	 * @param cell
	 * @return
	 */
	public double getDoubleValue(Cell cell) {
		if (cell == null) {
			return 0;
		}
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return 0;
		case HSSFCell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case HSSFCell.CELL_TYPE_STRING:
			try {
				return Double.parseDouble(cell.getStringCellValue());
			} catch (Exception e) {
				return 0;
			}
		default:
			return 0;
		}
	}
	public static void main(String[] args) {
		try {
			System.out.println(new Date(new SimpleDateFormat("yyyyMMddHHmm").parse("201512281005").getTime()));
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}

}
