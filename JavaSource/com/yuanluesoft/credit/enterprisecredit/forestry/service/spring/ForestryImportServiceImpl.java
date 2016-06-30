package com.yuanluesoft.credit.enterprisecredit.forestry.service.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
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

import com.yuanluesoft.credit.enterprisecredit.forestry.forms.ImportDateExcel;
import com.yuanluesoft.credit.enterprisecredit.forestry.pojo.Forestry;
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
public class ForestryImportServiceImpl extends BusinessServiceImpl {
	private AttachmentService attachmentService; //附件服务

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public void importData(ImportDateExcel excel, SessionInfo sessionInfo)
			throws ServiceException {
		
		List attachments = attachmentService.list(
				"credit/enterprisecredit/forestry", "data", excel.getId(),false, 1,
				null);
		if (attachments == null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		Attachment attachment = (Attachment) attachments.get(0);//得到具体的附件对象
		try {
			importDate(attachment, sessionInfo);
			attachmentService.deleteAll("credit/enterprisecredit/forestry",
					null, excel.getId());//删除上传的文件
		} catch (Exception e) {
			attachmentService.deleteAll("credit/enterprisecredit/forestry",
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
		try {
			workBook = new XSSFWorkbook(new FileInputStream(attachment
					.getFilePath()));
		} catch (Exception ex) {
			workBook = new HSSFWorkbook(new FileInputStream(attachment
					.getFilePath()));
		}
		sheet = workBook.getSheetAt(0);// 得到工作表对象
			Row content = sheet.getRow(1);// 内容行
			if (content == null) {
				throw new ServiceException("文件解析失败，请检查文件格式及数据类型是否正确");
			}
			if(getStringValue(content.getCell(0))==null || getStringValue(content.getCell(0)).equals("")){
				return;
			}
			Forestry forestry = new Forestry();
			forestry.setCreator(sessionInfo.getUserName());
			forestry.setCreatorId(sessionInfo.getUserId());
			forestry.setCreated(DateTimeUtils.now());
			forestry.setId(UUIDLongGenerator.generateId());
			
			String person = returnValue(getStringValue(content.getCell(0)),"被处罚人（单位）");
			Row cardNumcontent = sheet.getRow(2);
			String cardNum = returnValue(getStringValue(cardNumcontent.getCell(0)),"身份证号或其他证件名称");
			Row personAddrcontent = sheet.getRow(3);
			String personAddr = returnValue(getStringValue(personAddrcontent.getCell(0)),"被处罚人地址");
			Row bookNumcontent = sheet.getRow(4);
			String bookNum = returnValue(getStringValue(bookNumcontent.getCell(0)),"处罚决定书号");
			Row addrcontent = sheet.getRow(5);
			String addr = returnValue(getStringValue(addrcontent.getCell(0)),"违法地点");
			Row summarycontent = sheet.getRow(6);
			String summary = returnValue(getStringValue(summarycontent.getCell(0)),"违法情况");
			Row resultcontent = sheet.getRow(7);
			String result = returnValue(getStringValue(resultcontent.getCell(0)),"处罚情况");
			Row othercontent = sheet.getRow(10);
			String other = returnValue(getStringValue(othercontent.getCell(0)),"其他");
			Row remarkcontent = sheet.getRow(11);
			String remark = returnValue(getStringValue(remarkcontent.getCell(0)),"备注");
			forestry.setPerson(person) ; //被处罚人（单位）
			forestry.setCardNum(cardNum); //身份证号或其他证件名称：
			forestry.setPersonAddr(personAddr);//被处罚人地址
			forestry.setBookNum(bookNum);//处罚决定书号
			forestry.setAddr(addr);//违法地点
			forestry.setSummary(summary);//违法情况
			forestry.setResult(result);//处罚情况
			forestry.setOther(other);//其他
			forestry.setRemark(remark);//备注
			save(forestry);
	}
	
	public String returnValue(String source,String findStr){
		return source.substring(source.indexOf(findStr)+findStr.length()+1).trim();
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
