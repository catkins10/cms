package com.yuanluesoft.educ.teach.service.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yuanluesoft.educ.teach.forms.ImportDateExcel;
import com.yuanluesoft.educ.teach.pojo.Teach;
import com.yuanluesoft.educ.teach.service.TeachService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

public class TeachImportServiceImpl extends BusinessServiceImpl {

	private AttachmentService attachmentService; //附件服务
	private TeachService teachService;

	/**
	 * @return teachService
	 */
	public TeachService getTeachService() {
		return teachService;
	}

	/**
	 * @param teachService 要设置的 teachService
	 */
	public void setTeachService(TeachService teachService) {
		this.teachService = teachService;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public void importData(ImportDateExcel excel, SessionInfo sessionInfo)
			throws ServiceException {

		List attachments = attachmentService.list(
				"educ/teach", "data", excel.getId(),false, 1,
				null);
		if (attachments == null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}

		Attachment attachment = (Attachment) attachments.get(0);//得到具体的附件对象
		try {
			importDate(attachment, sessionInfo);
			attachmentService.deleteAll("educ/teach",
					null, excel.getId());//删除上传的文件
		} catch (Exception e) {
			attachmentService.deleteAll("educ/teach",
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
		String[] colName = new String[] { "系部" ,
				"姓名", "职工号|S" ,"性别" ,"证件号|S"};
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
		while (colNum[0] == -1) {//系部不能为空
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
				break;
			}
			Teach teach = new Teach();
			teach.setCreated(DateTimeUtils.now());
			teach.setId(UUIDLongGenerator.generateId());
			
			String loginId=getStringValue(content.getCell(colNum[2]));	//工号/账号
			String sex=getStringValue(content.getCell(colNum[3]));
			char charSex="男".equals(sex)?'M':"女".equals(sex)?'F':'0';	//性别需要转化为char类型
			
			teach.setDepartment(getStringValue(content.getCell(colNum[0])));
			teach.setName(getStringValue(content.getCell(colNum[1])));
			teach.setLoginId(loginId);
			teach.setSex(charSex);
			teach.setIdcardNumber(getStringValue(content.getCell(colNum[4])));
			
			String hql = "from Teach Teach where Teach.loginId = '"+loginId+"'";
			Teach old = (Teach)getDatabaseService().findRecordByHql(hql);
			if(old!=null){
				old.setDepartment(getStringValue(content.getCell(colNum[0])));
				old.setName(getStringValue(content.getCell(colNum[1])));
				old.setSex(charSex);
				old.setIdcardNumber(getStringValue(content.getCell(colNum[4])));
				teachService.update(old);
			}else{
				teachService.save(teach);
			}
		}

	}
	public boolean isLoginNameInUse(String loginName, long personId) throws ServiceException {
		//检查用户表
		loginName = loginName.toLowerCase(); //用户名不区分大小写
		Number id = (Number)getDatabaseService().findRecordByHql("select Person.id from Person Person where Person.loginName='" + JdbcUtils.resetQuot(loginName) + "'");
		if(id!=null) {
			return (id.longValue()!=personId);
		}
		return false;
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
	public String getStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		if(cell==null) {
			return null;
		}
		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return null;
			
		case Cell.CELL_TYPE_BOOLEAN:
			return "" + cell.getBooleanCellValue();

		case Cell.CELL_TYPE_ERROR:
			return "" + cell.getErrorCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			double d = cell.getNumericCellValue();  
			if(HSSFDateUtil.isCellDateFormatted(cell) || HSSFDateUtil.isCellInternalDateFormatted(cell)||DateUtil.isValidExcelDate(d)) { //判断是否为日期
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
           case Cell.CELL_TYPE_STRING:
        	                 String date=cell.getStringCellValue();
        	                 if(date!=null&&date.trim().length()!=0){
   			                 	try {
   			                     		return DateTimeUtils.parseDate(date.trim(), dataFormat==null||dataFormat.isEmpty()?"yyyy-MM-dd":dataFormat);
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
	
	
}
