package com.yuanluesoft.railway.event.service.spring;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.railway.event.pojo.RailwayEvent;
import com.yuanluesoft.railway.event.pojo.RailwayEventImport;
import com.yuanluesoft.railway.event.service.RailwayEventService;

/**
 * 
 * @author linchuan
 *
 */
public class RailwayEventServiceImpl extends BusinessServiceImpl implements RailwayEventService {
	private AttachmentService attachmentService; //附件服务
	private HTMLParser htmlParser; //HTML解析器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.railway.event.service.RailwayEventService#importData(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public RailwayEventImport importData(long importId, SessionInfo sessionInfo) throws ServiceException {
		List attachments = attachmentService.list("railway/event", "data", importId, false, 1, null);
		if(attachments==null || attachments.isEmpty()) {
			throw new ServiceException("未上传数据文件");
		}
		//保存导入日志
		RailwayEventImport importLog = new RailwayEventImport();
		importLog.setId(importId);
		importLog.setCreatorId(sessionInfo.getUserId()); //创建者ID
		importLog.setCreator(sessionInfo.getUserName()); //创建者
		importLog.setCreated(DateTimeUtils.now()); //创建时间
		save(importLog);

		Attachment attachment = (Attachment)attachments.get(0);
		try {
			HTMLDocument document = htmlParser.parseHTMLFile(attachment.getFilePath());
			importTable((HTMLTableElement)document.getElementsByTagName("table").item(0), importId, sessionInfo); //导入表格
			return importLog;
		}
		catch (Exception e) {
			Logger.exception(e);
			deleteImportedData(importId); //删除已经导入的数据
			throw new ServiceException("导入失败");
		}
	}
	
	/**
	 * 导入一个表格
	 * @param table
	 * @param importId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void importTable(HTMLTableElement table, long importId, SessionInfo sessionInfo) throws ServiceException {
		List colNames = new ArrayList(); //表头行
		for(int i=0; i<table.getRows().getLength(); i++) {
			HTMLTableRowElement tr = (HTMLTableRowElement)table.getRows().item(i);
			if(colNames.isEmpty()) {
				for(int j=0; j<tr.getCells().getLength(); j++) {
					HTMLTableCellElement td = (HTMLTableCellElement)tr.getCells().item(j);
					String content = htmlParser.getTextContent(td).trim();
					colNames.add(content);
				}
				continue;
			}
			RailwayEvent railwayEvent = new RailwayEvent();
			railwayEvent.setImportId(importId); //导入日志ID
			for(int j=0; j<tr.getCells().getLength(); j++) {
				HTMLTableCellElement td = (HTMLTableCellElement)tr.getCells().item(j);
				String content = htmlParser.getTextContent(td);
				if(content==null) {
					continue;
				}
				content = content.replace("　", "").trim();
				//根据表头设置属性
				String colName = (String)colNames.get(j);
				if("信息单号".equals(colName)) {
					if(content==null || content.isEmpty()) { //没有单号
						break;
					}
					railwayEvent.setSn(content);
				}
				else if(colName.indexOf("问题代码")!=-1 || colName.indexOf("条例编码")!=-1) {
					railwayEvent.setCode(content);
				}
				else if(colName.indexOf("检查人")!=-1) {
					railwayEvent.setChecker(content);
				}
				else if(colName.indexOf("检查时间")!=-1) {
					try {
						railwayEvent.setCheckTime(DateTimeUtils.parseTimestamp(content.replaceAll("[\\r\\n]", ""), "yyyy-MM-dd HH:mm"));
					}
					catch(ParseException e) {
						e.printStackTrace();
					}
				}
				else if(colName.indexOf("检查地点")!=-1) {
					railwayEvent.setCheckAddress(content);
				}
				else if(colName.indexOf("问题层级")!=-1) {
					railwayEvent.setEventLevel(content);
				}
				else if(colName.indexOf("问题大类")!=-1 || colName.indexOf("条例大类")!=-1) {
					railwayEvent.setEventCategory(content);
				}
				else if(colName.indexOf("问题类别")!=-1 || colName.indexOf("条例条目")!=-1) {
					railwayEvent.setEventCategory(railwayEvent.getEventCategory() + "/" + content);
				}
				else if(colName.indexOf("检查内容")!=-1) {
					railwayEvent.setDescription(content);
				}
				else if(colName.indexOf("责任人信息")!=-1) {
					if(content==null || content.trim().isEmpty()) {
						break;
					}
					//全部;福州南动地勤一班;龙林云;扣款:-70;
					String[] values = content.split(";");
					for(int k=0; k+4<=values.length; k+=4) {
						RailwayEvent event = new RailwayEvent();
						try {
							PropertyUtils.copyProperties(event, railwayEvent);
						} 
						catch (Exception e) {

						}
						event.setId(UUIDLongGenerator.generateId()); //ID
						event.setPersonName(values[k+2].replaceAll("[\\r\\n\\t 　]", "")); //责任人
						try {
							event.setDeduct(Math.abs(Double.parseDouble(values[k+3].replaceFirst("扣款:", "").replaceFirst("减免:", "")))); //扣款
						}
						catch (Exception e) {

						}
						event.setPersonId(getPersonId(event.getPersonName(), values[1])); //责任人ID
						//保存
						save(event);
					}
				}
				else if(colName.indexOf("列责情况")!=-1) {
					if(content==null || content.trim().isEmpty()) {
						break;
					}
					//列责情况范例：1、责任人:聂鑫，责任等级:主要，扣款:10.00，扣分:0.00,单位:福州南动地勤一班；1、责任人:周建，责任等级:主要，扣款:10.00，扣分:0.00,单位:福州南动地勤一班；1、责任人:程剑英，责任等级:重要，扣款:10.00，扣分:0.00,单位:福州南乘务队
					//			  1、责任人:王林，责任等级:全部，扣款:0.00，扣分:0.00,单位:福动所地勤二班；1、责任人:王林，责任等级:全部，扣款:0.00，扣分:0.00,单位:福动所地勤二班
					String[] values = content.split("；");
					for(int k=0; k<values.length; k++) {
						RailwayEvent event = new RailwayEvent();
						try {
							PropertyUtils.copyProperties(event, railwayEvent);
						} 
						catch (Exception e) {

						}
						event.setId(UUIDLongGenerator.generateId()); //ID
						int beginIndex = values[k].indexOf("责任人:") + "责任人:".length();
						int endIndex = values[k].indexOf("，", beginIndex);
						event.setPersonName(values[k].substring(beginIndex, endIndex)); //责任人
						beginIndex = values[k].indexOf("扣款:", endIndex) + "扣款:".length();
						endIndex = values[k].indexOf("，", beginIndex);
						try {
							event.setDeduct(Math.abs(Double.parseDouble(values[k].substring(beginIndex, endIndex)))); //扣款
						}
						catch (Exception e) {

						}
						beginIndex = values[k].indexOf(",单位:", endIndex) + ",单位:".length();
						event.setPersonId(getPersonId(event.getPersonName(), values[k].substring(beginIndex))); //责任人ID
						//保存
						save(event);
					}
				}
			}
		}
	}
	
	/**
	 * 获取责任人ID
	 * @param personName
	 * @param orgName
	 * @return
	 * @throws ServiceException
	 */
	private long getPersonId(String personName, String orgName) throws ServiceException {
		String hql = "from Person Person where Person.name='" + JdbcUtils.resetQuot(personName) + "'";
		List persons = getDatabaseService().findRecordsByHql(hql, 0, 5);
		if(persons==null || persons.isEmpty()) {
			return 0;
		}
		if(persons.size()==1) {
			return ((Person)persons.get(0)).getId();
		}
		for(Iterator iterator = persons.iterator(); iterator.hasNext();) {
			Person person = (Person)iterator.next();
			//检查部门是否匹配
			hql = "select Org.directoryName" +
				  " from Org Org, PersonSubjection PersonSubjection" +
				  " where Org.id=PersonSubjection.orgId" +
				  " and PersonSubjection.personId=" + person.getId() +
				  " order by PersonSubjection.id";
			String personOrgName = (String)getDatabaseService().findRecordByHql(hql);
			if(orgName.endsWith(personOrgName)) {
				return person.getId();
			}
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.railway.event.service.RailwayEventService#deleteImportedData(long)
	 */
	public void deleteImportedData(long importId) throws ServiceException {
		String hql = "from RailwayEvent RailwayEvent where RailwayEvent.importId=" + importId;
		getDatabaseService().deleteRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.railway.event.service.RailwayEventService#getEventCounts(long, int, int)
	 */
	public int[] getEventCounts(long personId, int year, int month) throws ServiceException {
		Date beginDate;
		try {
			beginDate = DateTimeUtils.parseDate(year + "-" + month + "-1", "yyyy-M-d");
		}
		catch (ParseException e) {
			throw new ServiceException();
		}
		Date endDate = DateTimeUtils.add(beginDate, Calendar.MONTH, 1);
		int[] counts = {0, 0, 0, 0};
		String hql = "select RailwayEvent.code" +
					 " from RailwayEvent RailwayEvent" +
					 " where RailwayEvent.personId=" + personId +
					 " and RailwayEvent.checkTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 " and RailwayEvent.checkTime<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")";
		List codes = getDatabaseService().findRecordsByHql(hql);
		if(codes==null || codes.isEmpty()) {
			return counts;
		}
		for(Iterator iterator = codes.iterator(); iterator.hasNext();) {
			String code = (String)iterator.next();
			if(code==null || code.isEmpty()) {
				continue;
			}
			char level = code.substring(0, 1).toLowerCase().charAt(0);
			if(level>='a' && level<='d') {
				counts[level-'a']++;
			}
		}
		return counts;
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
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}
}