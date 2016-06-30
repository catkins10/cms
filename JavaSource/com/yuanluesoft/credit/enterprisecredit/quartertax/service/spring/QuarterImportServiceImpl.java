package com.yuanluesoft.credit.enterprisecredit.quartertax.service.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yuanluesoft.credit.enterprisecredit.quartertax.forms.ImportDateExcel;
import com.yuanluesoft.credit.enterprisecredit.quartertax.pojo.QuarterTax;
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
public class QuarterImportServiceImpl extends BusinessServiceImpl {
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
				"credit/enterprisecredit/quartertax", "data", excel.getId(),false, 1,
				null);
		if (attachments == null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}

		Attachment attachment = (Attachment) attachments.get(0);//得到具体的附件对象
		try {
			importDate(attachment, sessionInfo);
			attachmentService.deleteAll("credit/enterprisecredit/quartertax",
					null, excel.getId());//删除上传的文件
		} catch (Exception e) {
			attachmentService.deleteAll("credit/enterprisecredit/quartertax",
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
		String[] colName = new String[] { "序号", "基层管征单位", "企业或单位名称",
				"纳税人识别号","法定代表人或负责人姓名","居民身份证或其他有效身份证件号码","经营地点","欠税税种"};
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
		while (colNum[0] == -1) {//纳税人识别号不能为空 
			firstRow++;
			if (firstRow > lastRow) {
				Logger.info("龙海市纳税人欠税情况季报表(企业或单位类)没有标题行！");
				return;
			}
			tableTitle = sheet.getRow(firstRow);
			colNum = findTitleColNumInArray(tableTitle, colName);
		}
		Row unitContent = sheet.getRow(firstRow-1);
		String unit = getStringValue(unitContent.getCell(0)).substring(3);
		String oweTime = getStringValue(unitContent.getCell(4));
		List listCombineCell = getCombineCell(sheet);
		for (int contentRow = firstRow + 2; contentRow <= lastRow; contentRow++) {
			Row content = sheet.getRow(contentRow);// 内容行
			if(getStringValue(content.getCell(colNum[1]))!=null && getStringValue(content.getCell(colNum[1])).equals("合计")){
				break;
			}
			QuarterTax quarterTax = new QuarterTax();
			quarterTax.setCreator(sessionInfo.getUserName());
			quarterTax.setCreatorId(sessionInfo.getUserId());
			quarterTax.setCreated(DateTimeUtils.now());
			quarterTax.setId(UUIDLongGenerator.generateId());
			
			quarterTax.setNuit(unit);//单位
			quarterTax.setOweTime(oweTime);//欠税时间
			quarterTax.setDepartment(isCombineCell(listCombineCell, content.getCell(colNum[1]), sheet)) ; //基层管征单位
			quarterTax.setEnterprise(isCombineCell(listCombineCell, content.getCell(colNum[2]), sheet)); //企业或单位名称
			quarterTax.setNumber(isCombineCell(listCombineCell, content.getCell(colNum[3]), sheet));//纳税人识别号
			quarterTax.setPerson(isCombineCell(listCombineCell, content.getCell(colNum[4]), sheet));//法定代表人或负责人姓名
			quarterTax.setCardNum(isCombineCell(listCombineCell, content.getCell(colNum[5]), sheet));//居民身份证或其他有效身份证件号码
			quarterTax.setAddr(isCombineCell(listCombineCell, content.getCell(colNum[6]), sheet));//经营地点
			quarterTax.setTaxType(isCombineCell(listCombineCell, content.getCell(colNum[7]), sheet));//欠税税种
			quarterTax.setTotal(isCombineCell(listCombineCell, content.getCell(8), sheet));//累计欠税余额
			quarterTax.setThisYear(isCombineCell(listCombineCell, content.getCell(9), sheet));//本年欠税余额
			save(quarterTax);

		}

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
	
	/**
	  * 合并单元格处理,获取合并行
	  * @param sheet
	  * @return List<CellRangeAddress>
	  */
	  public List getCombineCell(Sheet sheet)
	  {
	    List list = new ArrayList();
	    //获得一个 sheet 中合并单元格的数量
	    int sheetmergerCount = sheet.getNumMergedRegions();
	    //遍历合并单元格
	    for(int i = 0; i<sheetmergerCount;i++) 
	    {
	      //获得合并单元格加入list中
	      CellRangeAddress ca = sheet.getMergedRegion(i);
	      list.add(ca);
	    }
	    return list;
	  }
	  /**
	  * 判断单元格是否为合并单元格，是的话则将单元格的值返回
	  * @param listCombineCell 存放合并单元格的list
	  * @param cell 需要判断的单元格
	  * @param sheet sheet
	  * @return
	  */
	  public String isCombineCell(List listCombineCell,Cell cell,Sheet sheet) throws Exception
	  {
	    int firstC = 0;
	    int lastC = 0;
	    int firstR = 0;
	    int lastR = 0;
	    String cellValue = null;
	    for (Iterator iter = listCombineCell.iterator(); iter.hasNext();) {
	    	CellRangeAddress ca = (CellRangeAddress) iter.next();
	      //获得合并单元格的起始行, 结束行, 起始列, 结束列
	      firstC = ca.getFirstColumn();
	      lastC = ca.getLastColumn();
	      firstR = ca.getFirstRow();
	      lastR = ca.getLastRow();
	      if(cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR){
	        if(cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC){
	          Row fRow = sheet.getRow(firstR);
	          Cell fCell = fRow.getCell(firstC);
	          cellValue = getStringValue(fCell);
	          break;
	        }
	      }else{
	        cellValue = getStringValue(cell);
	      }
	    }
	    return cellValue;
	  }
	

}
