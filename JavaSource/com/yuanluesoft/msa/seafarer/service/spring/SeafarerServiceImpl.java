package com.yuanluesoft.msa.seafarer.service.spring;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.msa.seafarer.pojo.MsaCertificationExam;
import com.yuanluesoft.msa.seafarer.pojo.MsaCertificationExaminee;
import com.yuanluesoft.msa.seafarer.pojo.MsaImportLog;
import com.yuanluesoft.msa.seafarer.pojo.MsaSeafarerPassportOrg;
import com.yuanluesoft.msa.seafarer.pojo.MsaServiceOrg;
import com.yuanluesoft.msa.seafarer.pojo.MsaTrainOrg;
import com.yuanluesoft.msa.seafarer.pojo.MsaTrainShip;
import com.yuanluesoft.msa.seafarer.service.SeafarerService;

/**
 * 
 * @author linchuan
 *
 */
public class SeafarerServiceImpl extends BusinessServiceImpl implements SeafarerService {
	private AttachmentService attachmentService; //附件服务
	private ExchangeClient exchangeClient; //数据交换客户端
	private PageService pageService; //页面服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		super.save(record);
		if(!(record instanceof MsaImportLog)) {
			exchangeClient.synchUpdate(record, null, 2000); //同步更新
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //更新静态页面
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		super.update(record);
		if(!(record instanceof MsaImportLog)) {
			exchangeClient.synchUpdate(record, null, 2000); //同步更新
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //更新静态页面
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(!(record instanceof MsaImportLog)) {
			exchangeClient.synchDelete(record, null, 2000); //同步删除
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE); //更新静态页面
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.msa.seafarer.service.SeafarerService#importData(long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public MsaImportLog importData(long importId, String dataType, SessionInfo sessionInfo) throws ServiceException {
		List attachments = attachmentService.list("msa/seafarer", "data", importId, false, 1, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		Attachment attachment = (Attachment)attachments.get(0);
		MsaImportLog msaImportLog = new MsaImportLog();
		msaImportLog.setId(importId);
		msaImportLog.setDataType(dataType); //导入数据类型,培训机构、服务机构等
		msaImportLog.setFileName(attachment.getName()); //文件名
		msaImportLog.setImportTime(DateTimeUtils.now()); //导入时间
		msaImportLog.setPersonId(sessionInfo.getUserId()); //操作人ID
		msaImportLog.setPersonName(sessionInfo.getUserName()); //操作人姓名
		//msaImportLog.setremark; //备注
		save(msaImportLog);
		if("passportOrg".equals(dataType)) { //船员证申办单位
			importPassportOrg(attachment.getFilePath(), importId);
		}
		else if("serviceOrg".equals(dataType)) { //服务机构
			importServiceOrg(attachment.getFilePath(), importId);
		}
		else if("trainOrg".equals(dataType)) { //培训机构
			importTrainOrg(attachment.getFilePath(), importId);
		}
		else if("trainShip".equals(dataType)) { //培训船舶
			importTrainShip(attachment.getFilePath(), importId);
		}
		else if("certification".equals(dataType)) { //合格证书
			importCertification(attachment.getFilePath(), importId);
		}
		return msaImportLog;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.msa.seafarer.service.SeafarerService#deleteImportedData(long, java.lang.String)
	 */
	public void deleteImportedData(long importId, String dataType) throws ServiceException {
		String recordClassName = null;
		if("passportOrg".equals(dataType)) { //船员证申办单位
			recordClassName = "MsaSeafarerPassportOrg";
		}
		else if("serviceOrg".equals(dataType)) { //服务机构
			recordClassName = "MsaServiceOrg";
		}
		else if("trainOrg".equals(dataType)) { //培训机构
			recordClassName = "MsaTrainOrg";
		}
		else if("trainShip".equals(dataType)) { //培训船舶
			recordClassName = "MsaTrainShip";
		}
		else if("certification".equals(dataType)) { //合格证书
			recordClassName = "MsaCertificationExam";
		}
		String hql = "from " + recordClassName + " " + recordClassName + " where " + recordClassName + ".importId=" + importId;
		for(int i=0; i<10000; i+=200) {
			List records = getDatabaseService().findRecordsByHql(hql, i, 200);
			if(records==null || records.isEmpty()) {
				break;
			}
			for(Iterator iterator = records.iterator(); iterator.hasNext();) {
				Record record = (Record)iterator.next();
				delete(record);
			}
			if(records.size()<200) {
				break;
			}
		}
	}

	/**
	 * 导入船员证申办单位
	 * @param filePath
	 * @param importId
	 * @throws ServiceException
	 */
	private void importPassportOrg(String filePath, long importId) throws ServiceException {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(new File(filePath)); //构造Workbook（工作薄）对象
			Sheet sheet = wb.getSheet(0);
			//从第二行开始,第一、二行是标题
			for(int i=2; i<sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i); //序号	海员证申办单位名称	单位编号	联系地址	法人代表	联系电话
				MsaSeafarerPassportOrg passportOrg = new MsaSeafarerPassportOrg();
				passportOrg.setId(UUIDLongGenerator.generateId()); //ID
				passportOrg.setImportId(importId); //导入日志ID
				passportOrg.setName(ExcelUtils.getCellString(cells, 1)); //海员证申办单位名称
				passportOrg.setUnitNo(ExcelUtils.getCellString(cells, 2)); //单位编号
				passportOrg.setAddress(ExcelUtils.getCellString(cells, 3)); //联系地址
				passportOrg.setLegalRepresentative(ExcelUtils.getCellString(cells, 4)); //法人代表
				passportOrg.setTel(ExcelUtils.getCellString(cells, 5)); //联系电话
				if(passportOrg.getName()==null || passportOrg.getName().isEmpty()) {
					continue;
				}
				//检查是否已相同的机构,有则先删除旧的
				String hql = "from MsaSeafarerPassportOrg MsaSeafarerPassportOrg where MsaSeafarerPassportOrg.name='" + JdbcUtils.resetQuot(passportOrg.getName()) + "'";
				MsaSeafarerPassportOrg oldOrg =  (MsaSeafarerPassportOrg)getDatabaseService().findRecordByHql(hql);
				if(oldOrg!=null) {
					delete(oldOrg);
				}
				save(passportOrg);
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException("导入失败");
		}
		finally {
			try {
				wb.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 导入服务机构
	 * @param filePath
	 * @param importId
	 * @throws ServiceException
	 */
	private void importServiceOrg(String filePath, long importId) throws ServiceException {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(new File(filePath)); //构造Workbook（工作薄）对象
			Sheet sheet = wb.getSheet(0);
			Cell[] cells = sheet.getRow(0); //第一行，标题，用来解析服务机构类型
			String category = cells[0].getContents().replaceAll("名单", "");
			//从第二行开始,第一、二行是标题
			for(int i=2; i<sheet.getRows(); i++) {
				cells = sheet.getRow(i); //序号	海员外派机构名称	许可证号	服务内容	联系地址	法人代表	联系电话
				MsaServiceOrg serviceOrg = new MsaServiceOrg();
				serviceOrg.setId(UUIDLongGenerator.generateId()); //ID
				serviceOrg.setImportId(importId); //导入日志ID
				serviceOrg.setCategory(category); //类型
				serviceOrg.setName(ExcelUtils.getCellString(cells, 1)); //机构名称
				serviceOrg.setLicenseNumber(ExcelUtils.getCellString(cells, 2)); //许可证号
				serviceOrg.setServiceContent(ExcelUtils.getCellString(cells, 3)); //服务内容
				serviceOrg.setAddress(ExcelUtils.getCellString(cells, 4)); //联系地址
				serviceOrg.setLegalRepresentative(ExcelUtils.getCellString(cells, 5)); //法人代表
				serviceOrg.setTel(ExcelUtils.getCellString(cells, 6)); //联系电话
				if(serviceOrg.getName()==null || serviceOrg.getName().isEmpty()) {
					continue;
				}
				//检查是否已相同的机构,有则先删除旧的
				String hql = "from MsaServiceOrg MsaServiceOrg where MsaServiceOrg.name='" + JdbcUtils.resetQuot(serviceOrg.getName()) + "' and MsaServiceOrg.category='" + JdbcUtils.resetQuot(category) + "'";
				MsaServiceOrg oldOrg =  (MsaServiceOrg)getDatabaseService().findRecordByHql(hql);
				if(oldOrg!=null) {
					delete(oldOrg);
				}
				save(serviceOrg);
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException("导入失败");
		}
		finally {
			try {
				wb.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 导入培训机构
	 * @param filePath
	 * @param importId
	 * @throws ServiceException
	 */
	private void importTrainOrg(String filePath, long importId) throws ServiceException {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(new File(filePath)); //构造Workbook（工作薄）对象
			Sheet sheet = wb.getSheet(0);
			//从第二行开始,第一、二行是标题
			for(int i=2; i<sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i); //序号	培训机构名称	地址	联系电话	传真	邮编	 法定代表人	培训机构性质	许可证号
				MsaTrainOrg trainOrg = new MsaTrainOrg();
				trainOrg.setId(UUIDLongGenerator.generateId()); //ID
				trainOrg.setImportId(importId); //导入日志ID
				trainOrg.setName(ExcelUtils.getCellString(cells, 1)); //名称
				trainOrg.setAddress(ExcelUtils.getCellString(cells, 2)); //地址
				trainOrg.setTel(ExcelUtils.getCellString(cells, 3)); //联系电话
				trainOrg.setFax(ExcelUtils.getCellString(cells, 4)); //传真
				trainOrg.setPostalcode(ExcelUtils.getCellString(cells, 5)); //邮编
				trainOrg.setLegalRepresentative(ExcelUtils.getCellString(cells, 6)); //法定代表人
				trainOrg.setKind(ExcelUtils.getCellString(cells, 7)); //性质
				trainOrg.setLicenseNumber(ExcelUtils.getCellString(cells, 8)); //许可证号
				if(trainOrg.getName()==null || trainOrg.getName().isEmpty()) {
					continue;
				}
				//检查是否已相同的机构,有则先删除旧的
				String hql = "from MsaTrainOrg MsaTrainOrg where MsaTrainOrg.name='" + JdbcUtils.resetQuot(trainOrg.getName()) + "'";
				MsaTrainOrg oldOrg =  (MsaTrainOrg)getDatabaseService().findRecordByHql(hql);
				if(oldOrg!=null) {
					delete(oldOrg);
				}
				save(trainOrg);
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException("导入失败");
		}
		finally {
			try {
				wb.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 导入培训船舶
	 * @param filePath
	 * @param importId
	 * @throws ServiceException
	 */
	private void importTrainShip(String filePath, long importId) throws ServiceException {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(new File(filePath)); //构造Workbook（工作薄）对象
			Sheet sheet = wb.getSheet(0);
			//从第二行开始,第一、二行是标题
			for(int i=2; i<sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i); //序号,船舶名称,船籍港,船舶种类,总吨,功率,船舶所有人,船上培训/见习单位,属地海事机构,备注
				MsaTrainShip trainShip = new MsaTrainShip();
				trainShip.setId(UUIDLongGenerator.generateId()); //ID
				trainShip.setImportId(importId); //导入日志ID
				trainShip.setName(ExcelUtils.getCellString(cells, 1)); //名称
				trainShip.setPort(ExcelUtils.getCellString(cells, 2)); //船籍港
				trainShip.setCategory(ExcelUtils.getCellString(cells, 3)); //船舶种类
				trainShip.setTonnage(ExcelUtils.getCellString(cells, 4)); //总吨位
				trainShip.setPower(ExcelUtils.getCellString(cells, 5)); //功率
				trainShip.setShipBelong(ExcelUtils.getCellString(cells, 6)); //船舶所有人
				trainShip.setPracticeOrg(ExcelUtils.getCellString(cells, 7)); //船上培训/见习单位
				trainShip.setOrgBelong(ExcelUtils.getCellString(cells, 8)); //单位所属海事机构
				trainShip.setRemark(ExcelUtils.getCellString(cells, 9)); //备注
				if(trainShip.getName()==null || trainShip.getName().isEmpty()) {
					continue;
				}
				//检查是否已相同的机构,有则先删除旧的
				String hql = "from MsaTrainShip MsaTrainShip where MsaTrainShip.name='" + JdbcUtils.resetQuot(trainShip.getName()) + "'";
				MsaTrainShip oldShip =  (MsaTrainShip)getDatabaseService().findRecordByHql(hql);
				if(oldShip!=null) {
					delete(oldShip);
				}
				save(trainShip);
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException("导入失败");
		}
		finally {
			try {
				wb.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 导入合格证书
	 * @param filePath
	 * @param importId
	 * @throws ServiceException
	 */
	private void importCertification(String filePath, long importId) throws ServiceException {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(new File(filePath)); //构造Workbook（工作薄）对象
			Sheet sheet = wb.getSheet(0);
			MsaCertificationExam exam = new MsaCertificationExam();
			exam.setId(UUIDLongGenerator.generateId()); //ID
			exam.setImportTime(DateTimeUtils.now()); //导入时间
			exam.setImportId(importId); //导入记录ID
			//第二行: 专业：轮机                类别：甲类                   期数：10101005                考场：福建交通职业技术学院									
			Cell[] cells = sheet.getRow(1);
			String[] values = cells[0].getContents().replaceAll("：", " ").split(" ");
			for(int i=0; i<values.length; i++) {
				if(i<values.length-1 && !values[i].isEmpty()) {
					if(values[i].equals("专业")) {
						exam.setSpeciality(values[++i]); //专业
					}
					else if(values[i].equals("期数")) {
						exam.setPeriod(values[++i]); //期数
					}
					else if(values[i].equals("类别")) {
						exam.setCategory(values[++i]); //类别
					}
					else if(values[i].equals("考场")) {
						exam.setExamAddress(values[++i]); //考试地点
					}
				}
			}
			//检查是否已相同的考试,有则先删除旧的
			String hql = "from MsaCertificationExam MsaCertificationExam" +
						 " where MsaCertificationExam.speciality='" + JdbcUtils.resetQuot(exam.getSpeciality()) + "'" +
						 " and MsaCertificationExam.category='" + JdbcUtils.resetQuot(exam.getCategory()) + "'" +
						 " and MsaCertificationExam.period='" + JdbcUtils.resetQuot(exam.getPeriod()) + "'";
			MsaCertificationExam oldExam =  (MsaCertificationExam)getDatabaseService().findRecordByHql(hql);
			if(oldExam!=null) {
				delete(oldExam);
			}
			
			//从第五行开始是合格的考生
			for(int i=4; i<sheet.getRows(); i++) {
				cells = sheet.getRow(i); //准考证号码,姓名,申考等级,申考职务,考试结果,评估结果,合格证明序列号,发放时间,领取时间,备注(身份证号码)
				MsaCertificationExaminee examinee = new MsaCertificationExaminee();
				examinee.setId(UUIDLongGenerator.generateId()); //ID
				examinee.setExamId(exam.getId()); //考试ID
				examinee.setPermit(ExcelUtils.getCellString(cells, 0)); //准考证号码
				examinee.setName(ExcelUtils.getCellString(cells, 1)); //姓名
				examinee.setLevel(ExcelUtils.getCellString(cells, 2)); //申考等级
				examinee.setJob(ExcelUtils.getCellString(cells, 3)); //申考职务
				examinee.setExamResult(ExcelUtils.getCellString(cells, 4)); //考试结果
				examinee.setResult(ExcelUtils.getCellString(cells, 5)); //评估结果
				examinee.setSn(ExcelUtils.getCellString(cells, 6)); //合格证明序列号
				examinee.setGrantDate(ExcelUtils.getCellDate(cells, 7, null)); //发放日期
				examinee.setReceiveDate(ExcelUtils.getCellDate(cells, 8, null)); //领取日期
				examinee.setIdentityCard(ExcelUtils.getCellString(cells, 9)); //身份证号码
				if(examinee.getName()==null || examinee.getName().isEmpty()) {
					continue;
				}
				getDatabaseService().saveRecord(examinee);
			}
			save(exam); //保存考试
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException("导入失败");
		}
		finally {
			try {
				wb.close();
			}
			catch (Exception e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("serviceOrgCategory".equals(itemsName)) { //服务机构类型
			String hql = "select distinct MsaServiceOrg.category from MsaServiceOrg MsaServiceOrg";
			return getDatabaseService().findRecordsByHql(hql);
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
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

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}
}