package com.yuanluesoft.msa.exam.service.spring;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.msa.exam.pojo.MsaExam;
import com.yuanluesoft.msa.exam.pojo.MsaExamImportLog;
import com.yuanluesoft.msa.exam.pojo.MsaExamScore;
import com.yuanluesoft.msa.exam.pojo.MsaExamSubject;
import com.yuanluesoft.msa.exam.pojo.MsaExamTranscript;
import com.yuanluesoft.msa.exam.service.MsaExamService;

/**
 * 
 * @author linchuan
 *
 */
public class MsaExamServiceImpl extends BusinessServiceImpl implements MsaExamService {
	private AttachmentService attachmentService; //附件服务
	private ExchangeClient exchangeClient; //数据交换客户端
	private PageService pageService; //页面服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		super.save(record);
		MsaExam exam = null;
		if(record instanceof MsaExam) { //考试
			exam = (MsaExam)record;
			exchangeClient.synchUpdate(record, null, 2000); //同步更新
		}
		else if(record instanceof MsaExamTranscript) { //成绩单
			exchangeClient.synchUpdate(record, null, 2000); //同步更新
			MsaExamTranscript transcript = (MsaExamTranscript)record;
			if(transcript.getExam()!=null) {
				exam = transcript.getExam();
			}
			else {
				exam = (MsaExam)getDatabaseService().findRecordById(MsaExam.class.getName(), transcript.getExamId());
			}
		}
		if(exam!=null && exam.getPublishTime()!=null) { //已经发布
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //更新静态页面
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		super.update(record);
		if((record instanceof MsaExam) || (record instanceof MsaExamTranscript)) {
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
		if((record instanceof MsaExam) || (record instanceof MsaExamTranscript)) {
			exchangeClient.synchDelete(record, null, 2000); //同步删除
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE); //更新静态页面
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.msa.exam.servie.MsaExamService#importData(long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public MsaExamImportLog importData(long importId, String examName, SessionInfo sessionInfo) throws ServiceException {
		List attachments = attachmentService.list("msa/exam", "data", importId, false, 1, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		Attachment attachment = (Attachment)attachments.get(0);
		Workbook wb = null;
		try {
			//保存导入日志
			MsaExamImportLog importLog = new MsaExamImportLog();
			importLog.setId(importId);
			importLog.setExamName(examName); //考试名称,从导入的文件名解析
			importLog.setCreatorId(sessionInfo.getUserId()); //创建者ID
			importLog.setCreator(sessionInfo.getUserName()); //创建者
			importLog.setCreated(DateTimeUtils.now()); //创建时间
			save(importLog);
			
			//导入数据
			wb = Workbook.getWorkbook(new File(attachment.getFilePath())); //构造Workbook（工作薄）对象
			for(int i=0; i<wb.getSheets().length; i++) {
				Sheet sheet = wb.getSheet(i);
				importSheet(sheet, importId, examName, sessionInfo); //导入SHEET
			}
			return importLog;
		}
		catch (Exception e) {
			Logger.exception(e);
			deleteImportedData(importId); //删除已经导入的数据
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
	 * 导入一个SHEET
	 * @param sheet
	 * @param importId
	 * @param examName
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void importSheet(Sheet sheet, long importId, String examName, SessionInfo sessionInfo) throws ServiceException {
		List exams = new ArrayList(); //考试列表
		List colNames = new ArrayList(); //表头行
		String speciality = null; //专业名称
		String period = null; //期数
		String examAddress = null; //考试地点
		for(int i=0; i<sheet.getRows(); i++) {
			Cell[] cells = sheet.getRow(i);
			//如果表头为空,检查是否表头,判断依据该行必须要有“姓名”单元格
			if(colNames.isEmpty()) {
				for(int j=0; j<cells.length; j++) {
					String content = ExcelUtils.getCellString(cells, j);
					if(content==null) {
						content = "";
					}
					else if(content.indexOf("专业")!=-1) {
						if(content.replaceAll("[\\r\\n \\t]", "").endsWith("专业") && j==0) { //检查是否专业名称, 如：“驾驶专业”
							speciality = content;
						}
						else { //专业： 三副    类别：丙类              期数：5.23     考场：福建航运学校												
							List values = ListUtils.generateList(content.replaceAll("：", " "), " ");
							String fieldName = null;
							for(Iterator iterator = values.iterator(); iterator.hasNext();) {
								String value = (String)iterator.next();
								if(value!=null && !value.isEmpty()) {
									if(fieldName==null) {
										fieldName = value;
									}
									else {
										if("专业".equals(fieldName)) {
											speciality = value;
										}
										else if("期数".equals(fieldName)) {
											period = value;
										}
										else if("考场".equals(fieldName)) {
											examAddress = value;
										}
										fieldName = null;
									}
								}
							}
						}
					}
					colNames.add(content);
				}
				int index = colNames.indexOf("姓名");
				if(index==-1) {
					colNames.clear(); //不含“姓名”单元格,不是表头
				}
				else { //已经找到表头
					//检查下一行,是否两行表头
					cells = sheet.getRow(i+1);
					String content = ExcelUtils.getCellString(cells, index);
					if(content==null || content.isEmpty()) { //是两行表头
						for(int j=0; j<cells.length; j++) {
							content = ExcelUtils.getCellString(cells, j);
							if(content!=null && !content.isEmpty()) {
								colNames.set(j, content);
							}
						}
						i++;
					}
				}
				continue;
			}
			
			//读取学生成绩
			MsaExamTranscript transcript = new MsaExamTranscript();
			transcript.setId(UUIDLongGenerator.generateId()); //ID
			transcript.setExam(new MsaExam()); //考试
			transcript.getExam().setPeriod(period); //考试:期数
			transcript.getExam().setSpeciality(speciality); //考试:专业
			transcript.getExam().setExamAddress(examAddress); //考试:地点
			List scores = new ArrayList(); //各科目成绩
			for(int j=0; j<cells.length; j++) {
				String content = ExcelUtils.getCellString(cells, j);
				if(content!=null) {
					content = content.trim().replaceAll("\\xa0", "").replaceAll(new String(new char[]{65533}), "");
				}
				//根据表头设置属性
				String colName = (String)colNames.get(j);
				if("姓名".equals(colName)) {
					transcript.setName(content); //姓名
				}
				else if(colName.indexOf("身份证")!=-1) {
					transcript.setIdentityCard(content); //身份证号码
				}
				else if(colName.indexOf("准考证")!=-1) {
					transcript.setPermit(content); //准考证号码
				}
				else if(colName.indexOf("总成绩")!=-1) {
					transcript.setTotalScore(content); //总成绩
				}
				else if(colName.indexOf("通过")!=-1 || colName.indexOf("及格")!=-1 || colName.indexOf("评估结果")!=-1) {
					transcript.setPass(content); //是否通过
				}
				else if(colName.indexOf("申考等级")!=-1) {
					transcript.setExamLevel(content); //申考等级
				}
				else if(colName.indexOf("申考职务")!=-1) {
					transcript.setJob(content); //申考职务
				}
				else if(colName.indexOf("期数")!=-1) {
					transcript.getExam().setPeriod(content); //考试:期数
				}
				else if(colName.indexOf("试卷代码")!=-1) {
					transcript.getExam().setExamPaperCode(content); //考试:试卷代码
				}
				else if(colName.indexOf("考试时间")!=-1) {
					transcript.getExam().setExamTime(content); //考试:考试时间
				}
				else if(colName.indexOf("考试地点")!=-1) {
					transcript.getExam().setExamAddress(content); //考试:考试地点
				}
				else if(",专业,科目,学科,序号,,".indexOf("," + colName + ",")==-1) {
					//成绩字段
					MsaExamScore score = new MsaExamScore();
					score.setId(UUIDLongGenerator.generateId()); //ID
					score.setTranscriptId(transcript.getId()); //成绩单ID
					score.setSubject(colName); //科目名称
					score.setScore(content); //成绩
					scores.add(score);
				}
			}
			if(transcript.getName()==null || transcript.getName().isEmpty() || //姓名为空
			   transcript.getPermit()==null || transcript.getPermit().isEmpty()) { //准考证号码为空
				continue;
			}
			
			//保存考试
			boolean isNewExam = true;
			for(Iterator iterator = exams.iterator(); iterator.hasNext();) {
				MsaExam exam = (MsaExam)iterator.next();
				if(StringUtils.isEquals(transcript.getExam().getPeriod(), exam.getPeriod()) && //期数相同
				   StringUtils.isEquals(transcript.getExam().getExamPaperCode(), exam.getExamPaperCode())) { //试卷代码相同
					isNewExam = false;
					transcript.setExamId(exam.getId()); //设置考试ID
					break;
				}
			}
			if(isNewExam) { //新的考试
				transcript.getExam().setId(UUIDLongGenerator.generateId()); //ID
				transcript.getExam().setName(examName); //名称
				transcript.getExam().setCreatorId(sessionInfo.getUserId()); //创建者ID
				transcript.getExam().setCreator(sessionInfo.getUserName()); //创建者
				transcript.getExam().setCreated(DateTimeUtils.now()); //创建时间
				transcript.getExam().setImportId(importId); //导入日志ID
				save(transcript.getExam()); //保存考试
				exams.add(transcript.getExam()); //加入列表
				transcript.setExamId(transcript.getExam().getId()); //设置考试ID
				//保存学科列表
				for(Iterator iterator = scores.iterator(); iterator.hasNext();) {
					MsaExamScore score = (MsaExamScore)iterator.next();
					MsaExamSubject subject = new MsaExamSubject();
					subject.setId(UUIDLongGenerator.generateId()); //ID
					subject.setExamId(transcript.getExam().getId()); //考试ID
					subject.setSubject(score.getSubject()); //科目名称
					save(subject);
				}
			}
		
			//保存各科目成绩
			for(Iterator iterator = scores.iterator(); iterator.hasNext();) {
				MsaExamScore score = (MsaExamScore)iterator.next();
				getDatabaseService().saveRecord(score);
			}

			//保存成绩单
			save(transcript);
		}
		
		//发布考试
		for(Iterator iterator = exams.iterator(); iterator.hasNext();) {
			MsaExam exam = (MsaExam)iterator.next();
			exam.setPublishTime(DateTimeUtils.now()); //发布时间
			update(exam);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.msa.exam.servie.MsaExamService#deleteImportedData(long)
	 */
	public void deleteImportedData(long importId) throws ServiceException {
		String hql = "from MsaExam MsaExam where MsaExam.importId=" + importId;
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