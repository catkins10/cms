package com.yuanluesoft.credit.enterprisecredit.item.service.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yuanluesoft.credit.enterprisecredit.item.forms.ImportDateExcel;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.BigStandard;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.Handle;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.LinkPerson;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.Permit;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.Punish;
import com.yuanluesoft.credit.enterprisecredit.item.pojo.SmallStandard;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.DatabaseService;
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
public class ItemImportServiceImpl extends BusinessServiceImpl {
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
				"credit/enterprisecredit/item", "data", excel.getId(), false,1,
				null);
		if (attachments == null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}

		Attachment attachment = (Attachment) attachments.get(0);//得到具体的附件对象
		try {
			importDate(attachment, sessionInfo);
			attachmentService.deleteAll("credit/enterprisecredit/item",
					null, excel.getId());//删除上传的文件
		} catch (Exception e) {
			attachmentService.deleteAll("credit/enterprisecredit/item",
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
		String[] colName = new String[] { "姓名", "职务", "联系电话"};
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
		while (colNum[0] == -1) {//姓名不能为空 
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
			LinkPerson linkPerson = new LinkPerson();
			linkPerson.setCreator(sessionInfo.getUserName());
			linkPerson.setCreatorId(sessionInfo.getUserId());
			linkPerson.setCreated(DateTimeUtils.now());
			linkPerson.setId(UUIDLongGenerator.generateId());

			linkPerson.setRole(getStringValue(content.getCell(0))) ; //角色
			linkPerson.setName(getStringValue(content.getCell(colNum[0]))); //姓名
			linkPerson.setJob(getStringValue(content.getCell(colNum[1])));//职务
			linkPerson.setTel(getStringValue(content.getCell(colNum[2])));//联系电话

			//旧数据判断
			String hql="from LinkPerson LinkPerson where LinkPerson.role='"+linkPerson.getRole()+"' and LinkPerson.name='"+linkPerson.getName()+"' and LinkPerson.job='"+linkPerson.getJob()+"'";
			DatabaseService databaseService = getDatabaseService();
			LinkPerson oldRecord = (LinkPerson) databaseService.findRecordByHql(hql);
			if (oldRecord != null) {
				oldRecord.setTel(linkPerson.getTel());
				oldRecord.setCreator(sessionInfo.getUserName());
				oldRecord.setCreatorId(sessionInfo.getUserId());
				oldRecord.setCreated(DateTimeUtils.now());
				update(oldRecord);
			} else {
				save(linkPerson);
			}

		}
		
		//第二个sheet
		colName = new String[] { "序号","单位", "企业名称", "行业","拟达标等级","进展情况","证书编号","有效期","备注"};
		sheet = workBook.getSheetAt(1);// 得到工作表对象
		firstRow = sheet.getFirstRowNum();
		lastRow = sheet.getLastRowNum();
		tableTitle = sheet.getRow(firstRow);// 表头行
		colNum = findTitleColNumInArray(tableTitle, colName);
		while (colNum[2] == -1) {//姓名不能为空 
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
			if(getStringValue(content.getCell(colNum[2]))==null || getStringValue(content.getCell(colNum[2])).equals("")){
				break;
			}
			BigStandard bigStandard = new BigStandard();
			bigStandard.setCreator(sessionInfo.getUserName());
			bigStandard.setCreatorId(sessionInfo.getUserId());
			bigStandard.setCreated(DateTimeUtils.now());
			bigStandard.setId(UUIDLongGenerator.generateId());
			bigStandard.setUnit("龙海市") ; //单位
			bigStandard.setName(getStringValue(content.getCell(colNum[2]))); //企业名称
			bigStandard.setIndustry(getStringValue(content.getCell(colNum[3])));//行业
			bigStandard.setLevel(getStringValue(content.getCell(colNum[4])));//拟达标等级
			bigStandard.setSituation(getStringValue(content.getCell(colNum[5])));//进展情况
			bigStandard.setBookNum(getStringValue(content.getCell(colNum[6])));//证书编号
			bigStandard.setUsefulDate(getStringValue(content.getCell(colNum[7])));//有效期
			bigStandard.setRemark(getStringValue(content.getCell(colNum[8])));//备注
			save(bigStandard);

		}
		
//		第三个sheet
		colName = new String[] { "序号","单位名称", "行业", "所属","证书编号","达标日期","有效日期","行业主管单位"};
		sheet = workBook.getSheetAt(2);// 得到工作表对象
		firstRow = sheet.getFirstRowNum();
		lastRow = sheet.getLastRowNum();
		tableTitle = sheet.getRow(firstRow);// 表头行
		colNum = findTitleColNumInArray(tableTitle, colName);
		while (colNum[1] == -1) {//姓名不能为空 
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
			if(getStringValue(content.getCell(colNum[1]))==null || getStringValue(content.getCell(colNum[1])).equals("")){
				break;
			}
			SmallStandard smallStandard = new SmallStandard();
			smallStandard.setCreator(sessionInfo.getUserName());
			smallStandard.setCreatorId(sessionInfo.getUserId());
			smallStandard.setCreated(DateTimeUtils.now());
			smallStandard.setId(UUIDLongGenerator.generateId());
			smallStandard.setUnit(getStringValue(content.getCell(colNum[1]))) ; //单位名称
			smallStandard.setIndustry(getStringValue(content.getCell(colNum[2])));//行业
			smallStandard.setLevel(getStringValue(content.getCell(colNum[3])));//所属乡镇
			smallStandard.setBookNum(getStringValue(content.getCell(colNum[4])));//证书编号
			smallStandard.setStartDate(getStringValue(content.getCell(colNum[5])));//达标日期
			smallStandard.setUsefulDate(getStringValue(content.getCell(colNum[6])));//有效期
			smallStandard.setMainUnit(getStringValue(content.getCell(colNum[7])));//行业主管单位
			save(smallStandard);

		}
		
//		第四个sheet
		colName = new String[] { "序号","许可证号", "单位名称", "许可经营范围","办结时间","证书有效期","备注"};
		sheet = workBook.getSheetAt(3);// 得到工作表对象
		firstRow = sheet.getFirstRowNum();
		lastRow = sheet.getLastRowNum();
		tableTitle = sheet.getRow(firstRow);// 表头行
		colNum = findTitleColNumInArray(tableTitle, colName);
		while (colNum[1] == -1) {//姓名不能为空 
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
			if(getStringValue(content.getCell(colNum[1]))==null || getStringValue(content.getCell(colNum[1])).equals("")){
				break;
			}
			Permit permit = new Permit();
			permit.setCreator(sessionInfo.getUserName());
			permit.setCreatorId(sessionInfo.getUserId());
			permit.setCreated(DateTimeUtils.now());
			permit.setId(UUIDLongGenerator.generateId());
			
			permit.setPermitNum(getStringValue(content.getCell(colNum[1]))) ; //许可证号
			permit.setUnit(getStringValue(content.getCell(colNum[2])));//单位名称
			permit.setRange(getStringValue(content.getCell(colNum[3])));//许可经营范围
			permit.setOverTime(getStringValue(content.getCell(colNum[4])));//办结时间
			permit.setUsefulDate(getStringValue(content.getCell(colNum[5])));//证书有效期
			permit.setRemark(getStringValue(content.getCell(colNum[6])));//备注
			save(permit);

		}
		
//		第五个sheet
		colName = new String[] { "序号","案件名称", "行政处罚单位", "处罚事由","法定代表人","统一社会","备注"};
		sheet = workBook.getSheetAt(4);// 得到工作表对象
		firstRow = sheet.getFirstRowNum();
		lastRow = sheet.getLastRowNum();
		tableTitle = sheet.getRow(firstRow);// 表头行
		colNum = findTitleColNumInArray(tableTitle, colName);
		while (colNum[1] == -1) {//案件名称不能为空 
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
			if(getStringValue(content.getCell(colNum[1]))==null || getStringValue(content.getCell(colNum[1])).equals("")){
				break;
			}
			Punish punish = new Punish();
			punish.setCreator(sessionInfo.getUserName());
			punish.setCreatorId(sessionInfo.getUserId());
			punish.setCreated(DateTimeUtils.now());
			punish.setId(UUIDLongGenerator.generateId());
			
			punish.setName(getStringValue(content.getCell(colNum[1]))) ; //案件名称
			punish.setUnit(getStringValue(content.getCell(colNum[2])));//行政处罚单位或个人名称
			punish.setReason(getStringValue(content.getCell(colNum[3])));//处罚事由
			punish.setPerson(getStringValue(content.getCell(colNum[4])));//法定代表人（负责人）姓名
			punish.setCode(getStringValue(content.getCell(colNum[5])));//统一社会信用代码
			punish.setRemark(getStringValue(content.getCell(colNum[6])));//备注
			save(punish);

		}
		
//		第五个sheet
		colName = new String[] { "序号","案件名称", "处罚单位", "案件事由","法定代表人","处罚决定书及其文号","备注"};
		sheet = workBook.getSheetAt(5);// 得到工作表对象
		firstRow = sheet.getFirstRowNum();
		lastRow = sheet.getLastRowNum();
		tableTitle = sheet.getRow(firstRow);// 表头行
		colNum = findTitleColNumInArray(tableTitle, colName);
		while (colNum[1] == -1) {//案件名称不能为空 
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
			if(getStringValue(content.getCell(colNum[1]))==null || getStringValue(content.getCell(colNum[1])).equals("")){
				break;
			}
			Handle handle = new Handle();
			handle.setCreator(sessionInfo.getUserName());
			handle.setCreatorId(sessionInfo.getUserId());
			handle.setCreated(DateTimeUtils.now());
			handle.setId(UUIDLongGenerator.generateId());
			
			handle.setName(getStringValue(content.getCell(colNum[1]))) ; //案件名称
			handle.setUnit(getStringValue(content.getCell(colNum[2])));//行政处罚单位或个人名称
			handle.setReason(getStringValue(content.getCell(colNum[3])));//处罚事由
			handle.setPerson(getStringValue(content.getCell(colNum[4])));//法定代表人（负责人）姓名
			handle.setBookAndCode(getStringValue(content.getCell(colNum[5])));//处罚决定书及其文号
			handle.setRemark(getStringValue(content.getCell(colNum[6])));//备注
			save(handle);

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

}
