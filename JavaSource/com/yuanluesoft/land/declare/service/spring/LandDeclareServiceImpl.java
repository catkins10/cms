package com.yuanluesoft.land.declare.service.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback;
import com.yuanluesoft.land.declare.pojo.DisastersUnit;
import com.yuanluesoft.land.declare.pojo.ExplorationRights;
import com.yuanluesoft.land.declare.pojo.Geopark;
import com.yuanluesoft.land.declare.pojo.InvestigateUnit;
import com.yuanluesoft.land.declare.pojo.LandRegister;
import com.yuanluesoft.land.declare.pojo.MiningRights;
import com.yuanluesoft.land.declare.service.LandDeclareService;
/**
 * 
 * @author kangshiwei
 *
 */
public class LandDeclareServiceImpl extends BusinessServiceImpl implements LandDeclareService {
	private PageService pageService; //页面服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		record = super.save(record);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.land.declare.service.LandDeclareService#importData(java.lang.String)
	 */
	public void importData(String excelFilePath) throws ServiceException {
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelFilePath));
			//创建工作簿
			HSSFWorkbook workBook = new HSSFWorkbook(fs);
			for(int i = 0; i < workBook.getNumberOfSheets(); i++){
				HSSFSheet sheet = workBook.getSheetAt(i); //得到工作表对象
				if(sheet.getSheetName().indexOf("采矿权")!=-1) {
					importMiningRights(sheet);
				}
				else if(sheet.getSheetName().indexOf("探矿权")!=-1) {
					importExplorationRights(sheet);
				}
				else if(sheet.getSheetName().indexOf("地质灾害")!=-1) {
					importDisastersUnit(sheet);
				}
				else if(sheet.getSheetName().indexOf("地质勘察")!=-1 || sheet.getSheetName().indexOf("地质勘查")!=-1) {
					importInvestigateUnit(sheet);
				}
				else if(sheet.getSheetName().indexOf("地质公园")!=-1) {
					importGeopark(sheet);
				}
				else if(sheet.getSheetName().indexOf("土地登记")!=-1) {
					importLandRegister(sheet);
				}
			}
		}
		catch(ServiceException e) {
			throw e;
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 导入采矿权
	 * @param sheet
	 * @throws ServiceException
	 */
	private void importMiningRights(HSSFSheet sheet) throws ServiceException {
		if(sheet.getLastRowNum()<2) {
			return;
		}
		Date date = null;
		try {
			String value = sheet.getRow(0).getCell(0).getStringCellValue();
			date = DateTimeUtils.parseDate(value.substring(value.indexOf("（") + 1, value.indexOf("）")), "yyyy年MM月");
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		final Date approvalTime = date;
		ExcelSheetImportCallback callback = new ExcelSheetImportCallback() {
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#isHeader(java.util.List)
			 */
			public boolean isHeader(List rowContents) throws Exception {
				//申请人	标题	矿山名称	地址	许可证号	开采主矿种	有效期起	有效期止	矿区面积	采深上限	采深下限	项目类型	坐标（矿区范围）
				return rowContents.indexOf("申请人")!=-1 && rowContents.indexOf("矿山名称")!=-1 && rowContents.indexOf("许可证号")!=-1;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#createNewRecord()
			 */
			public Object createNewRecord() throws Exception {
				MiningRights miningRights = new MiningRights();
				miningRights.setId(UUIDLongGenerator.generateId()); //ID
				miningRights.setApprovalTime(approvalTime);  //审批时间
				miningRights.setCreated(DateTimeUtils.now()); //导入时间
				return miningRights;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#saveRecrd(java.lang.Object)
			 */
			public void saveRecrd(Object record) throws Exception {
				MiningRights miningRights = (MiningRights)record;
				//申请人、矿山名称、许可证号不能为空
				if(miningRights.getApplicant()==null || miningRights.getApplicant().isEmpty() ||
				   miningRights.getMineName()==null || miningRights.getMineName().isEmpty() ||
				   miningRights.getLicenseNum()==null || miningRights.getLicenseNum().isEmpty()) {
					return;
				}
				//检查申请人、矿山名称、许可证号都相同的记录
				String hql = "select MiningRights.id" +
							 " from MiningRights MiningRights" +
							 " where MiningRights.applicant='" + miningRights.getApplicant() + "'" + //申请人
							 " and MiningRights.mineName='" + miningRights.getMineName() + "'" + //矿山名称
							 " and MiningRights.licenseNum='" + miningRights.getLicenseNum() + "'"; //许可证号
				Number oldId = (Number)getDatabaseService().findRecordByHql(hql);
	            if(oldId!=null) {
	            	miningRights.setId(oldId.longValue());
	            	update(miningRights);
	            }
	            else{
	            	save(miningRights);
	            }
    			//重新生成静态页面
    			pageService.rebuildStaticPageForModifiedObject(miningRights, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#setRecordPropertyValue(java.lang.Object, java.lang.String, java.lang.String)
			 */
			public void setRecordPropertyValue(Object record, String columnName, String cellContent) throws Exception {
				MiningRights miningRights = (MiningRights)record;
				//申请人	标题	矿山名称	地址	许可证号	开采主矿种	有效期起	有效期止	矿区面积	采深上限	采深下限	项目类型	坐标（矿区范围）
				if("申请人".equals(columnName)) {
					miningRights.setApplicant(cellContent); //申请人
				}
				else if("标题".equals(columnName)) {
					miningRights.setSubject(cellContent); //标题
				}
				else if("矿山名称".equals(columnName)) {
					miningRights.setMineName(cellContent); //矿山名称
				}
				else if("地址".equals(columnName)) {
					miningRights.setAddress(cellContent); //地址
				}
				else if("许可证号".equals(columnName)) {
					miningRights.setLicenseNum(cellContent); //许可证号
				}
				else if("开采主矿种".equals(columnName)) {
					miningRights.setMainMinerals(cellContent); //开采主矿种
				}
				else if("有效期起".equals(columnName)) {
					try {
						miningRights.setValidFrom(DateTimeUtils.parseDate(cellContent, "yyyy-MM-dd"));
					}
					catch(ParseException e) {
					
					}
				}
				else if("有效期止".equals(columnName)) {
					try {
						miningRights.setValidEnd(DateTimeUtils.parseDate(cellContent, "yyyy-MM-dd"));
					}
					catch(ParseException e) {
					
					}
				}
				else if("矿区面积".equals(columnName)) {
					miningRights.setMiningArea(Double.parseDouble(cellContent)); //矿区面积
				}
				else if("采深上限".equals(columnName)) {
					miningRights.setCaps(Double.parseDouble(cellContent)); //采深上限
				}
				else if("采深下限".equals(columnName)) {
					miningRights.setLower(Double.parseDouble(cellContent)); //采深下限
				}
				else if("项目类型".equals(columnName)) {
					miningRights.setProjectType(cellContent); //项目类型
				}
				else if(columnName.startsWith("坐标")) {
					miningRights.setCoordinate(cellContent); //坐标
				}
			}
		};
		try {
			ExcelUtils.importExcelSheet(sheet, callback);
		} 
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 导入探矿权
	 * @param sheet
	 * @throws ServiceException
	 */
	private void importExplorationRights(HSSFSheet sheet) throws ServiceException {
		if(sheet.getLastRowNum()<2) {
			return;
		}
		Date date = null;
		try {
			String value = sheet.getRow(0).getCell(0).getStringCellValue();
			date = DateTimeUtils.parseDate(value.substring(value.indexOf("（") + 1, value.indexOf("）")), "yyyy年MM月");
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		final Date approvalTime = date;
		ExcelSheetImportCallback callback = new ExcelSheetImportCallback() {
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#isHeader(java.util.List)
			 */
			public boolean isHeader(List rowContents) throws Exception {
				//许可证号	项目名称	发证日期	申请人	勘查单位	勘查矿种	地理位置	有效期起	有效期止	总面积	项目类型	坐标（区块范围）
				return rowContents.indexOf("申请人")!=-1 && rowContents.indexOf("项目名称")!=-1 && rowContents.indexOf("许可证号")!=-1;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#createNewRecord()
			 */
			public Object createNewRecord() throws Exception {
				ExplorationRights explorationRights = new ExplorationRights();
				explorationRights.setId(UUIDLongGenerator.generateId()); //ID
				explorationRights.setApprovalTime(approvalTime);  //审批时间
				explorationRights.setCreated(DateTimeUtils.now()); //导入时间
				return explorationRights;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#saveRecrd(java.lang.Object)
			 */
			public void saveRecrd(Object record) throws Exception {
				ExplorationRights explorationRights = (ExplorationRights)record;
				//申请人、项目名称、许可证号不能为空
				if(explorationRights.getApplicant()==null || explorationRights.getApplicant().isEmpty() ||
				   explorationRights.getProjectName()==null || explorationRights.getProjectName().isEmpty() ||
				   explorationRights.getLicenseNum()==null || explorationRights.getLicenseNum().isEmpty()) {
					return;
				}
				//检查申请人、项目名称、许可证号都相同的记录
				String hql = "select ExplorationRights.id" +
							 " from ExplorationRights ExplorationRights" +
							 " where ExplorationRights.applicant='" + explorationRights.getApplicant() + "'" + //申请人
							 " and ExplorationRights.projectName='" + explorationRights.getProjectName() + "'" + //项目名称
							 " and ExplorationRights.licenseNum='" + explorationRights.getLicenseNum() + "'"; //许可证号
				Number oldId = (Number)getDatabaseService().findRecordByHql(hql);
	            if(oldId!=null) {
	            	explorationRights.setId(oldId.longValue());
	            	update(explorationRights);
	            }
	            else{
	            	save(explorationRights);
	            }
    			//重新生成静态页面
    			pageService.rebuildStaticPageForModifiedObject(explorationRights, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#setRecordPropertyValue(java.lang.Object, java.lang.String, java.lang.String)
			 */
			public void setRecordPropertyValue(Object record, String columnName, String cellContent) throws Exception {
				ExplorationRights explorationRights = (ExplorationRights)record;
				//许可证号	项目名称	发证日期	申请人	勘查单位	勘查矿种	地理位置	有效期起	有效期止	总面积	项目类型	坐标（区块范围）
				if("许可证号".equals(columnName)) {
					explorationRights.setLicenseNum(cellContent); //许可证号
				}
				else if("项目名称".equals(columnName)) {
					explorationRights.setProjectName(cellContent); //项目名称
				}
				else if("发证日期".equals(columnName)) {
					try {
						explorationRights.setIssueDate(DateTimeUtils.parseDate(cellContent, "yyyy-MM-dd"));
					}
					catch (ParseException e) {
					
					}
				}
				else if("申请人".equals(columnName)) {
					explorationRights.setApplicant(cellContent); //申请人
				}
				else if("勘察单位".equals(columnName) || "勘查单位".equals(columnName)) {
					explorationRights.setInvestigationUnit(cellContent); //勘察单位
				}
				else if("勘察矿种".equals(columnName) || "勘查矿种".equals(columnName)) {
					explorationRights.setMinerals(cellContent); //勘察矿种
				}
				else if("地理位置".equals(columnName)) {
					explorationRights.setLocation(cellContent); //地理位置
				}
				else if("有效期起".equals(columnName)) {
					try {
						explorationRights.setValidFrom(DateTimeUtils.parseDate(cellContent, "yyyy-MM-dd"));
					}
					catch (ParseException e) {
					
					}
				}
				else if("有效期止".equals(columnName)) {
					try {
						explorationRights.setValidEnd(DateTimeUtils.parseDate(cellContent, "yyyy-MM-dd"));
					}
					catch (ParseException e) {
					
					}
				}
				else if("总面积".equals(columnName)) {
					explorationRights.setArea(Double.parseDouble(cellContent)); //总面积
				}
				else if("项目类型".equals(columnName)) {
					explorationRights.setProjectType(cellContent); //项目类型
				}
				else if(columnName.startsWith("坐标")) {
					explorationRights.setCoordinate(cellContent); //坐标
				}
			}
		};
		try {
			ExcelUtils.importExcelSheet(sheet, callback);
		} 
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 导入地址灾害单位资质
	 * @param sheet
	 * @throws ServiceException
	 */
	private void importDisastersUnit(HSSFSheet sheet) throws ServiceException {
		Date date = null;
		try {
			String value = sheet.getRow(0).getCell(0).getStringCellValue();
			date = DateTimeUtils.parseDate(value.substring(value.indexOf("（") + 1, value.indexOf("）")), "yyyy年MM月");
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		final Date approvalTime = date;
		ExcelSheetImportCallback callback = new ExcelSheetImportCallback() {
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#isHeader(java.util.List)
			 */
			public boolean isHeader(List rowContents) throws Exception {
				//勘查单位 资质类型 资质等级
				return rowContents.indexOf("勘查单位")!=-1;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#createNewRecord()
			 */
			public Object createNewRecord() throws Exception {
				DisastersUnit disastersUnit = new DisastersUnit();
				disastersUnit.setId(UUIDLongGenerator.generateId()); //ID
				disastersUnit.setApprovalTime(approvalTime);  //审批时间
				disastersUnit.setCreated(DateTimeUtils.now()); //导入时间
				return disastersUnit;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#saveRecrd(java.lang.Object)
			 */
			public void saveRecrd(Object record) throws Exception {
				DisastersUnit disastersUnit = (DisastersUnit)record;
				//勘查单位 资质类型 资质等级
				if(disastersUnit.getInvestigationUnit()==null || disastersUnit.getInvestigationUnit().isEmpty()) {
					return;
				}
				//检查勘察单位都相同的记录
				String hql = "select DisastersUnit.id" +
							 " from DisastersUnit DisastersUnit" +
							 " where DisastersUnit.investigationUnit='" + disastersUnit.getInvestigationUnit() + "'" + //单位名称
							 " and DisastersUnit.qualificationType='" + disastersUnit.getQualificationType() + "'"; //资质类型
				Number oldId = (Number)getDatabaseService().findRecordByHql(hql);
	            if(oldId!=null) {
	            	disastersUnit.setId(oldId.longValue());
	            	update(disastersUnit);
	            }
	            else{
	            	save(disastersUnit);
	            }
    			//重新生成静态页面
    			pageService.rebuildStaticPageForModifiedObject(disastersUnit, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#setRecordPropertyValue(java.lang.Object, java.lang.String, java.lang.String)
			 */
			public void setRecordPropertyValue(Object record, String columnName, String cellContent) throws Exception {
				DisastersUnit disastersUnit = (DisastersUnit)record;
				//勘查单位 资质类型 资质等级
				if("勘察单位".equals(columnName) || "勘查单位".equals(columnName)) {
					disastersUnit.setInvestigationUnit(cellContent); //勘察单位
				}
				else if("资质类型".equals(columnName)) {
					disastersUnit.setQualificationType(cellContent); //资质类型
				}
				else if("资质等级".equals(columnName)) {
					disastersUnit.setQualificationLevel(cellContent); //资质等级
				}
			}
		};
		try {
			ExcelUtils.importExcelSheet(sheet, callback);
		} 
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 导入地质勘察单位资质
	 * @param sheet
	 * @throws ServiceException
	 */
	private void importInvestigateUnit(HSSFSheet sheet) throws ServiceException {
		ExcelSheetImportCallback callback = new ExcelSheetImportCallback() {
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#isHeader(java.util.List)
			 */
			public boolean isHeader(List rowContents) throws Exception {
				//证书编号	单位名称	申请类型	有效期限	住所	法定代表人	邮政编码	联系电话	资质类别和资质等级	发证机关	所属省份
				return rowContents.indexOf("证书编号")!=-1 && rowContents.indexOf("单位名称")!=-1;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#createNewRecord()
			 */
			public Object createNewRecord() throws Exception {
				InvestigateUnit investigateUnit = new InvestigateUnit();
				investigateUnit.setId(UUIDLongGenerator.generateId()); //ID
				investigateUnit.setCreated(DateTimeUtils.now()); //导入时间
				return investigateUnit;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#saveRecrd(java.lang.Object)
			 */
			public void saveRecrd(Object record) throws Exception {
				InvestigateUnit investigateUnit = (InvestigateUnit)record;
				//证书编号	单位名称	申请类型	有效期限	住所	法定代表人	邮政编码	联系电话	资质类别和资质等级	发证机关	所属省份
				if(investigateUnit.getCertificateNum()==null || investigateUnit.getCertificateNum().isEmpty() ||
				   investigateUnit.getUnitName()==null || investigateUnit.getUnitName().isEmpty()) {
					return;
				}
				//检查勘察单位都相同的记录
				String hql = "select InvestigateUnit.id" +
							 " from InvestigateUnit InvestigateUnit" +
							 " where InvestigateUnit.unitName='" + investigateUnit.getUnitName() + "'" + //单位名称
							 " and InvestigateUnit.certificateNum='" + investigateUnit.getCertificateNum() + "'"; //证书编号
				Number oldId = (Number)getDatabaseService().findRecordByHql(hql);
	            if(oldId!=null) {
	            	investigateUnit.setId(oldId.longValue());
	            	update(investigateUnit);
	            }
	            else{
	            	save(investigateUnit);
	            }
    			//重新生成静态页面
    			pageService.rebuildStaticPageForModifiedObject(investigateUnit, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#setRecordPropertyValue(java.lang.Object, java.lang.String, java.lang.String)
			 */
			public void setRecordPropertyValue(Object record, String columnName, String cellContent) throws Exception {
				InvestigateUnit investigateUnit = (InvestigateUnit)record;
				//证书编号	单位名称	申请类型	有效期限	住所	法定代表人	邮政编码	联系电话	资质类别和资质等级	发证机关	所属省份
				if("证书编号".equals(columnName)) {
					investigateUnit.setCertificateNum(cellContent); //证书编号
				}
				else if("单位名称".equals(columnName)) {
					investigateUnit.setUnitName(cellContent); //单位名称
				}
				else if("申请类型".equals(columnName)) {
					investigateUnit.setApplyType(cellContent); //申请类型
				}
				else if("有效期限".equals(columnName)) {
					String[] values =  cellContent.split("至");
					try {
						investigateUnit.setValidFrom(DateTimeUtils.parseDate(values[0].trim(), "yyyy年MM月dd日"));
					}
					catch (ParseException e) {
					
					}
					try {
						investigateUnit.setValidEnd(DateTimeUtils.parseDate(values[1].trim(), "yyyy年MM月dd日"));
					}
					catch (ParseException e) {
					
					}
				}
				else if("住所".equals(columnName)) {
					investigateUnit.setResidence(cellContent); //住所
				}
				else if("法定代表人".equals(columnName)) {
					investigateUnit.setLeader(cellContent); //法定代表人
				}
				else if("邮政编码".equals(columnName)) {
					investigateUnit.setPostal(cellContent); //邮编
				}
				else if("联系电话".equals(columnName)) {
					investigateUnit.setTel(cellContent); //联系电话
				}
				else if("资质类别和资质等级".equals(columnName)) {
					investigateUnit.setQualificationTypeLeve(cellContent); //资质类别和等级
				}
				else if("发证机关".equals(columnName)) {
					investigateUnit.setIssuingUnit(cellContent); //发证机关
				}
				else if("所属省份".equals(columnName)) {
					investigateUnit.setProvinces(cellContent); //所属省份
				}
			}
		};
		try {
			ExcelUtils.importExcelSheet(sheet, callback);
		} 
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 导入地质公园
	 * @param attachment
	 * @param sessionInfo
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ServiceException
	 */
	private void importGeopark(HSSFSheet sheet) throws ServiceException {
		ExcelSheetImportCallback callback = new ExcelSheetImportCallback() {
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#isHeader(java.util.List)
			 */
			public boolean isHeader(List rowContents) throws Exception {
				//级别	地质公园名称	公园位置	面积（km2）	批准时间	开园时间	地质遗迹
				return rowContents.indexOf("地质公园名称")!=-1;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#createNewRecord()
			 */
			public Object createNewRecord() throws Exception {
				Geopark geopark = new Geopark();
				geopark.setId(UUIDLongGenerator.generateId()); //ID
				geopark.setCreated(DateTimeUtils.now()); //导入时间
				return geopark;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#saveRecrd(java.lang.Object)
			 */
			public void saveRecrd(Object record) throws Exception {
				Geopark geopark = (Geopark)record;
				//级别	地质公园名称	公园位置	面积（km2）	批准时间	开园时间	地质遗迹
				if(geopark.getParkName()==null || geopark.getParkName().isEmpty()) {
					return;
				}
				//检查地质公园名称相同的记录
				String hql = "select Geopark.id" +
							 " from Geopark Geopark" +
							 " where Geopark.parkName='" + geopark.getParkName() + "'"; //地质公园名称
				Number oldId = (Number)getDatabaseService().findRecordByHql(hql);
	            if(oldId!=null) {
	            	geopark.setId(oldId.longValue());
	            	update(geopark);
	            }
	            else{
	            	save(geopark);
	            }
    			//重新生成静态页面
    			pageService.rebuildStaticPageForModifiedObject(geopark, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#setRecordPropertyValue(java.lang.Object, java.lang.String, java.lang.String)
			 */
			public void setRecordPropertyValue(Object record, String columnName, String cellContent) throws Exception {
				Geopark geopark = (Geopark)record;
				//级别	地质公园名称	公园位置	面积（km2）	批准时间	开园时间	地质遗迹
				if("级别".equals(columnName)) {
					geopark.setLevel(cellContent);
				}
				else if("地质公园名称".equals(columnName)) {
					geopark.setParkName(cellContent);
				}
				else if("公园位置".equals(columnName)) {
					geopark.setLocation(cellContent);
				}
				else if(columnName.startsWith("面积")) {
					geopark.setArea(Double.parseDouble(cellContent));
				}
				else if("批准时间".equals(columnName)) {
					geopark.setApprovalTime(Integer.parseInt(cellContent));
				}
				else if("开园时间".equals(columnName)) {
					geopark.setEnableTime(Integer.parseInt(cellContent));
				}
				else if("地质遗迹".equals(columnName)) {
					geopark.setGeologicalHeritage(cellContent);
				}
			}
		};
		try {
			ExcelUtils.importExcelSheet(sheet, callback);
		} 
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 导入土地登记
	 * @param attachment
	 * @param sessionInfo
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ServiceException
	 */
	private void importLandRegister(HSSFSheet sheet) throws ServiceException {
		ExcelSheetImportCallback callback = new ExcelSheetImportCallback() {
			
			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#isHeader(java.util.List)
			 */
			public boolean isHeader(List rowContents) throws Exception {
				//权利人名称	土地坐落	登记类型	土地证号	变更前土地证号	使用权面积	使用权类型	登记日期
				return rowContents.indexOf("权利人名称")!=-1 && rowContents.indexOf("土地证号")!=-1;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#createNewRecord()
			 */
			public Object createNewRecord() throws Exception {
				LandRegister landRegister = new LandRegister();
				landRegister.setId(UUIDLongGenerator.generateId()); //ID
				landRegister.setCreated(DateTimeUtils.now()); //导入时间
				return landRegister;
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#saveRecrd(java.lang.Object)
			 */
			public void saveRecrd(Object record) throws Exception {
				LandRegister landRegister = (LandRegister)record;
				//权利人名称	土地坐落	登记类型	土地证号	变更前土地证号	使用权面积	使用权类型	登记日期
				if(landRegister.getPersonName()==null || landRegister.getPersonName().isEmpty() ||
				   landRegister.getCertificateNum()==null || landRegister.getCertificateNum().isEmpty()) {
					return;
				}
				//检查权利人名称、土地证号都相同的记录
				String hql = "select LandRegister.id" +
							 " from LandRegister LandRegister" +
							 " where LandRegister.personName='" + landRegister.getPersonName() + "'" + //申请人
							 " and LandRegister.certificateNum='" + landRegister.getCertificateNum() + "'"; //土地证号
				Number oldId = (Number)getDatabaseService().findRecordByHql(hql);
	            if(oldId!=null) {
	            	landRegister.setId(oldId.longValue());
	            	update(landRegister);
	            }
	            else{
	            	save(landRegister);
	            }
    			//重新生成静态页面
    			pageService.rebuildStaticPageForModifiedObject(landRegister, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}

			/* (non-Javadoc)
			 * @see com.yuanluesoft.jeaf.util.callback.ExcelSheetImportCallback#setRecordPropertyValue(java.lang.Object, java.lang.String, java.lang.String)
			 */
			public void setRecordPropertyValue(Object record, String columnName, String cellContent) throws Exception {
				LandRegister landRegister = (LandRegister)record;
				//权利人名称	土地坐落	登记类型	土地证号	变更前土地证号	使用权面积	使用权类型	登记日期
				if("权利人名称".equals(columnName)) {
					landRegister.setPersonName(cellContent);
				}
				else if("土地坐落".equals(columnName)) {
					landRegister.setLocation(cellContent);
				}
				else if("登记类型".equals(columnName)) {
					landRegister.setRegisterType(cellContent);
				}
				else if("土地证号".equals(columnName)) {
					landRegister.setCertificateNum(cellContent);
				}
				else if("变更前土地证号".equals(columnName)) {
					landRegister.setOldCertificateNum(cellContent);
				}
				else if("使用权面积".equals(columnName)) {
					landRegister.setArea(Double.parseDouble(cellContent));
				}
				else if("使用权类型".equals(columnName)) {
					landRegister.setUserType(cellContent);
				}
				else if("登记日期".equals(columnName)) {
					try {
						landRegister.setRegisterDate(DateTimeUtils.parseDate(cellContent, "yyyy-MM-dd"));
					}
					catch(Exception e) {
						landRegister.setRegisterDate(new Date(HSSFDateUtil.getJavaDate(Double.parseDouble(cellContent)).getTime()));
					}
				}
			}
		};
		try {
			ExcelUtils.importExcelSheet(sheet, callback);
		} 
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
}