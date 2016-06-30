package com.yuanluesoft.lss.cardquery.service.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.struts.action.ActionForm;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.lss.cardquery.forms.admin.ImportData;
import com.yuanluesoft.lss.cardquery.pojo.CardQuery;
import com.yuanluesoft.lss.cardquery.service.CardQueryService;


/**
 * 
 * @author linchuan
 *
 */
public class CardQueryServiceImpl extends BusinessServiceImpl implements CardQueryService {
	private AttachmentService attachmentService; //附件服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.lss.insurance.service.InsuranceService#importData(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void importData(ActionForm form,long importId, SessionInfo sessionInfo,HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		List attachments = attachmentService.list("lss/cardquery", "data", importId, false, 1, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		Attachment attachment = (Attachment)attachments.get(0);
		try {			
			importDate(form,attachment, sessionInfo, request, response);//执行导入		
			attachmentService.deleteAll("lss/cardquery",  "data", importId);//删除上传的文件
		} catch (Exception e) {
			attachmentService.deleteAll("lss/cardquery",  "data", importId);//删除上传的文件

			Logger.exception(e);
			throw new ServiceException("文件解析失败，请检查文件格式及数据类型是否正确");
		}
	}

	/**
	 * 导入Excel
	 * @param attachment
	 * @param sessionInfo
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ServiceException
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private void importDate(ActionForm form,Attachment attachment, SessionInfo sessionInfo,HttpServletRequest request, HttpServletResponse response)throws FileNotFoundException, IOException, ServiceException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(attachment.getFilePath()));
		// 创建工作簿
		HSSFWorkbook workBook = new HSSFWorkbook(fs);
		HSSFSheet sheet = workBook.getSheetAt(0); // 得到工作表对象,只导第一页

		HSSFRow tableTitle = findTitleRow(sheet);
        if(tableTitle==null){
        	return ;
        }
		String[] rowName=new String[]{"姓名","性别","社会保障号","批号","参保辖区","制卡类型","接收数据日期","制卡日期","移出卡片日期"};
		int[] rowNum=rowNum(tableTitle,rowName);
		int firstRow = tableTitle.getRowNum();
		int lastRow = sheet.getLastRowNum();
         
		for (int contentRow=firstRow+1; contentRow <= lastRow; contentRow++) {
			
            HSSFRow content=sheet.getRow(contentRow);// 内容行
            if(content==null){
            	continue;
            }
            
            DatabaseService databaseService=getDatabaseService();
//            防止excel存储成数字格式
            String securityNumber=getVaule(content.getCell(rowNum[2]));//社会保障号
            String batchNumber=getVaule(content.getCell(rowNum[3]));//批号
//            没有配置该制卡类型，不导入
            List oldRecord=databaseService.findRecordsByHql("select CardQueryConfig.cardType from com.yuanluesoft.lss.cardquery.pojo.CardQueryConfig CardQueryConfig " +
            		" where  CardQueryConfig.cardType='"+content.getCell(rowNum[5]).getStringCellValue().trim()+"'");
            if(oldRecord==null||oldRecord.isEmpty()){
            	Logger.info("filter the lss record that name is '"+content.getCell(rowNum[0]).getStringCellValue()
            			+"' where the card type '"+content.getCell(rowNum[5]).getStringCellValue()+"' is not exist");
            	continue;
            }
            ImportData importData=(ImportData)form;
			CardQuery cardQuery = new CardQuery();
			cardQuery.setCreator(sessionInfo.getUserName());
			cardQuery.setCreatorId(sessionInfo.getUserId());
			cardQuery.setCreated(DateTimeUtils.now());
			cardQuery.setId(UUIDLongGenerator.generateId());
			cardQuery.setMark(importData.getMark());

            HSSFCell cell=null;
//            性别解析
            cell= content.getCell(rowNum[1]);
            if(cell!=null){
            	String sex=cell.getStringCellValue();
            	if(sex.indexOf("男")!=-1){
            		cardQuery.setSex(0);
            	}else {
            		cardQuery.setSex(1);
            	}
            }
            

//            日期解析（"接收数据日期","制卡日期","移出卡片日期）

			Date timestamp=null;
            cell= content.getCell(rowNum[6]);//接收数据日期           
            timestamp=getDate(cell);
			cardQuery.setReceiveDate(timestamp);
       
            cell= content.getCell(rowNum[7]);//制卡日期
            timestamp=getDate(cell);
			cardQuery.setMakeCardDate(timestamp);

            cell= content.getCell(rowNum[8]);//移出卡片日期
            timestamp=getDate(cell);
			cardQuery.setRemovedCardDate(timestamp);
            
            cardQuery.setName(content.getCell(rowNum[0]).getStringCellValue().trim());
            cardQuery.setSecurityNumber(securityNumber==null?null:securityNumber.trim());
            cardQuery.setBatchNumber(batchNumber==null?null:batchNumber.trim());
            cardQuery.setJurisdiction(content.getCell(rowNum[4]).getStringCellValue());
            cardQuery.setCardType(content.getCell(rowNum[5]).getStringCellValue().trim());
//          旧数据判断。若是姓名、社会保障号、制卡类型、制卡日期都相同的则覆盖
            try {
				oldRecord = databaseService.findRecordsByHql("from com.yuanluesoft.lss.cardquery.pojo.CardQuery CardQuery "
								+ " where CardQuery.name='"
								+ cardQuery.getName()
								+ "' and CardQuery.securityNumber='"
								+ cardQuery.getSecurityNumber()
								+ "' and CardQuery.cardType='"
								+ cardQuery.getCardType()
								+ "' and CardQuery.makeCardDate='"
								+ cardQuery.getMakeCardDate() + "'");

				if (oldRecord != null && !oldRecord.isEmpty()) {
					CardQuery cardQueryOld = (CardQuery) oldRecord.get(0);
					cardQuery.setId(cardQueryOld.getId());// id设为一样
//					PropertyUtils.copyProperties(cardQueryOld, cardQuery);// 复制新属性
					update(cardQuery);
					continue;
				}
			}catch(Exception e){//查询出错则该数据有问题，不导入
            	Logger.info("search lss old record fail where the name is '"+cardQuery.getName()+"'");
    			Logger.exception(e);
            	continue;
            }
			try{
				save(cardQuery);
			}catch(Exception e){
            	Logger.info("save lss record fail where the name is '"+cardQuery.getName()+"'");
    			Logger.exception(e);
            	continue;
			}
		}
	}
	/**
	 * 获取单元格的内容值
	 * @param cell
	 * @return
	 */
	private String getVaule(HSSFCell cell){
		if(cell==null){
			return null;
		}
		 int cellType=cell.getCellType();
		 String value=null;
         switch(cellType){
           case Cell.CELL_TYPE_STRING:value=cell.getStringCellValue().trim();break;
           case Cell.CELL_TYPE_NUMERIC:value=(cell.getNumericCellValue()+"").trim();break;
           default:;//
         }
		return value;
	}
	/**
	 * 获取日期
	 * @param cell
	 * @return
	 */
	private Date getDate(HSSFCell cell){
		if(cell==null){
			return null;
		}
		 int cellType=cell.getCellType();
         switch(cellType){
           case Cell.CELL_TYPE_STRING:
        	                 String date=cell.getStringCellValue();
        	                 if(date!=null&&date.length()!=0){
   			                 	try {
   			                     		return DateTimeUtils.parseDate(date, "yyyy-MM-dd");
   			                      	} catch (ParseException e) {
                                    return null;//无效格式
   				                     }
                             } break;
//         poi读取data日期格式时默认以数字格式存储。用HSSFDateUtil类转换
           case Cell.CELL_TYPE_NUMERIC:
        	                if (HSSFDateUtil.isCellDateFormatted(cell)) {//判断是否为日期       	   
        	                    java.util.Date dateTemp = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
        	                    return new java.sql.Date(dateTemp.getTime());
        	                 };break;
           default:return null;//其他类型的不能转换成日期格式
         }
         return null;
	}
/**
 * 查找表头行，有“姓名”字段的行为表头行
 * @param sheet
 * @return
 */
	private HSSFRow findTitleRow(HSSFSheet sheet){
		int firstRowNum=sheet.getFirstRowNum();
		int lastRowNum=sheet.getLastRowNum();

		for(;firstRowNum<=lastRowNum;firstRowNum++){
			HSSFRow row = sheet.getRow(firstRowNum);
			if(row==null){
				continue;
			}
	        int firstCell=row.getFirstCellNum();
            int lastCell=row.getLastCellNum();
            for(;firstCell<=lastCell;firstCell++){
                if(row.getCell(firstCell).getStringCellValue().indexOf("姓名")!=-1){
                   return row;
              }
		   }
		}
		return null;
	}
/**
 * 返回对应的列的序号.返回的数据顺序和提供的名称顺序对应
 * @param tableTitle 表头行
 * @param rowName 表头行单元格名称
 * @return
 */
    private int[] rowNum(HSSFRow tableTitle,String[] rowName){
        int[] rowNum=new int[rowName.length];//基本类型默认初始化和类成员变量默认初始化一样
            
        for(int i=0;i<rowName.length;i++){
        	int firstCell=tableTitle.getFirstCellNum();
            int lastCell=tableTitle.getLastCellNum();
              for(;firstCell<lastCell;firstCell++){
               if(tableTitle.getCell(firstCell).getStringCellValue().indexOf(rowName[i])!=-1){
            	   rowNum[i]=firstCell;
            	   break;
               }
           }
        }
    	return rowNum;    	
    }
	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
}