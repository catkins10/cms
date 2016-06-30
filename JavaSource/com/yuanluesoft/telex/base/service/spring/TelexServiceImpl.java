package com.yuanluesoft.telex.base.service.spring;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLPreElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.telex.base.model.Total;
import com.yuanluesoft.telex.base.model.TotalBySecurityLevel;
import com.yuanluesoft.telex.base.pojo.TelegramCategory;
import com.yuanluesoft.telex.base.pojo.TelegramLevel;
import com.yuanluesoft.telex.base.pojo.TelegramOpinionPerson;
import com.yuanluesoft.telex.base.pojo.TelegramSecurityLevel;
import com.yuanluesoft.telex.base.pojo.TelegramSn;
import com.yuanluesoft.telex.base.pojo.TelegramTransactionSheet;
import com.yuanluesoft.telex.base.service.TelexService;

/**
 * 
 * @author linchuan
 *
 */
public class TelexServiceImpl extends BusinessServiceImpl implements TelexService {
	private HTMLParser htmlParser;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#getNextSequence(boolean, boolean)
	 */
	public int getNextSequence(boolean isSend, boolean isCryptic, boolean readOnly) throws ServiceException {
		if(readOnly) {
			return getCurrentSequence(isSend, isCryptic) + 1;
		}
		int year = DateTimeUtils.getYear(DateTimeUtils.date());
		String hql = "from TelegramSn TelegramSn" +
					 " where TelegramSn.isSend" + (isSend ? "=" : "!=") +  "'1'" +
					 " and TelegramSn.isCryptic" + (isCryptic ? "=" : "!=") +  "'1'" +
					 " and TelegramSn.year=" + year;
		TelegramSn telegramSn = (TelegramSn)getDatabaseService().findRecordByHql(hql);
		int sequence = 1;
		if(telegramSn!=null) {
			sequence = telegramSn.getSn() + 1;
			telegramSn.setSn(sequence);
			getDatabaseService().updateRecord(telegramSn);
		}
		else {
			telegramSn = new TelegramSn();
			telegramSn.setId(UUIDLongGenerator.generateId()); //ID
			telegramSn.setIsCryptic(isCryptic ? '1' : '0');
			telegramSn.setIsSend(isSend ? '1' : '0');
			telegramSn.setYear(year);
			telegramSn.setSn(sequence);
			getDatabaseService().saveRecord(telegramSn);
		}
		return sequence;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#getCurrentSequence(boolean, boolean)
	 */
	public int getCurrentSequence(boolean isSend, boolean isCryptic) throws ServiceException {
		int year = DateTimeUtils.getYear(DateTimeUtils.date());
		String hql = "from TelegramSn TelegramSn" +
					 " where TelegramSn.isSend" + (isSend ? "=" : "!=") +  "'1'" +
					 " and TelegramSn.isCryptic" + (isCryptic ? "=" : "!=") +  "'1'" +
					 " and TelegramSn.year=" + year;
		TelegramSn telegramSn = (TelegramSn)getDatabaseService().findRecordByHql(hql);
		return telegramSn!=null ? telegramSn.getSn() : 0;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#saveCurrentSequence(boolean, boolean, int)
	 */
	public void saveCurrentSequence(boolean isSend, boolean isCryptic, int sn) throws ServiceException {
		int year = DateTimeUtils.getYear(DateTimeUtils.date());
		String hql = "from TelegramSn TelegramSn" +
					 " where TelegramSn.isSend" + (isSend ? "=" : "!=") +  "'1'" +
					 " and TelegramSn.isCryptic" + (isCryptic ? "=" : "!=") +  "'1'" +
					 " and TelegramSn.year=" + year;
		TelegramSn telegramSn = (TelegramSn)getDatabaseService().findRecordByHql(hql);
		if(telegramSn!=null) {
			telegramSn.setSn(sn);
			getDatabaseService().updateRecord(telegramSn);
		}
		else {
			telegramSn = new TelegramSn();
			telegramSn.setId(UUIDLongGenerator.generateId()); //ID
			telegramSn.setIsCryptic(isCryptic ? '1' : '0');
			telegramSn.setIsSend(isSend ? '1' : '0');
			telegramSn.setYear(year);
			telegramSn.setSn(sn);
			getDatabaseService().saveRecord(telegramSn);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#listTelegramCategories()
	 */
	public List listTelegramCategories() throws ServiceException {
		return getDatabaseService().findRecordsByHql("select TelegramCategory.category from TelegramCategory TelegramCategory order by TelegramCategory.id");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#listTelegramSecurityLevels()
	 */
	public List listTelegramSecurityLevels() throws ServiceException {
		return getDatabaseService().findRecordsByHql("select TelegramSecurityLevel.securityLevel from TelegramSecurityLevel TelegramSecurityLevel order by TelegramSecurityLevel.id");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#listTelegramLevels()
	 */
	public List listTelegramLevels() throws ServiceException {
		return getDatabaseService().findRecordsByHql("select TelegramLevel.level from TelegramLevel TelegramLevel order by TelegramLevel.id");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("telegramCategories".equals(itemsName)) {
			return getDatabaseService().findRecordsByHql("select TelegramCategory.category, TelegramCategory.category from TelegramCategory TelegramCategory order by TelegramCategory.id");
		}
		else if("telegramSecurityLevels".equals(itemsName)) {
			return getDatabaseService().findRecordsByHql("select TelegramSecurityLevel.securityLevel, TelegramSecurityLevel.securityLevel from TelegramSecurityLevel TelegramSecurityLevel order by TelegramSecurityLevel.id");
		}
		else if("telegramLevels".equals(itemsName)) {
			return getDatabaseService().findRecordsByHql("select TelegramLevel.level, TelegramLevel.level from TelegramLevel TelegramLevel order by TelegramLevel.id");
		}
		else if("opinionPersons".equals(itemsName)) {
			return getDatabaseService().findRecordsByHql("select TelegramOpinionPerson.personName,TelegramOpinionPerson.personName from TelegramOpinionPerson TelegramOpinionPerson order by TelegramOpinionPerson.personName");
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#saveTelegramCategories(java.lang.String)
	 */
	public void saveTelegramCategories(String categories) throws ServiceException {
		//删除原来的配置
		getDatabaseService().deleteRecordsByHql("from TelegramCategory TelegramCategory");
		//保存新配置
		if(categories==null || categories.equals("")) {
			return;
		}
		String[] values = categories.replaceAll("，", ",").split(",");
		for(int i=0; i<values.length; i++) {
			TelegramCategory category = new TelegramCategory();
			category.setId(UUIDLongGenerator.generateId()); //ID
			category.setCategory(values[i].trim());
			getDatabaseService().saveRecord(category);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#saveTelegramSecurityLevels(java.lang.String)
	 */
	public void saveTelegramSecurityLevels(String securityLevels) throws ServiceException {
		//删除原来的配置
		getDatabaseService().deleteRecordsByHql("from TelegramSecurityLevel TelegramSecurityLevel");
		//保存新配置
		if(securityLevels==null || securityLevels.equals("")) {
			return;
		}
		String[] values = securityLevels.replaceAll("，", ",").split(",");
		for(int i=0; i<values.length; i++) {
			TelegramSecurityLevel level = new TelegramSecurityLevel();
			level.setId(UUIDLongGenerator.generateId()); //ID
			level.setSecurityLevel(values[i].trim());
			getDatabaseService().saveRecord(level);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#saveTelegramLevels(java.lang.String)
	 */
	public void saveTelegramLevels(String levels) throws ServiceException {
		//删除原来的配置
		getDatabaseService().deleteRecordsByHql("from TelegramLevel TelegramLevel");
		//保存新配置
		if(levels==null || levels.equals("")) {
			return;
		}
		String[] values = levels.replaceAll("，", ",").split(",");
		for(int i=0; i<values.length; i++) {
			TelegramLevel level = new TelegramLevel();
			level.setId(UUIDLongGenerator.generateId()); //ID
			level.setLevel(values[i].trim());
			getDatabaseService().saveRecord(level);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.service.TelegramSignService#generateTransactionSheet(java.lang.Object, long)
	 */
	public String generateTransactionSheet(Object telegram, long sheetid) throws ServiceException {
		//获取办理单
		TelegramTransactionSheet sheet = (TelegramTransactionSheet)getDatabaseService().findRecordById(TelegramTransactionSheet.class.getName(), sheetid);
		HTMLDocument sheetDocument = htmlParser.parseHTMLString(sheet.getBody(), "utf-8");
		sheetDocument.getBody().setAttribute("contenteditable", "true");
		//插入打印脚本
		HTMLScriptElement script = (HTMLScriptElement)sheetDocument.createElement("script");
		String js = "window.onload = function(){window.print();}\r\n" +
					"window.onbeforeprint = function(){document.body.contentEditable=false;}\r\n" +
					"window.onafterprint = function(){document.body.contentEditable=true;}";
		htmlParser.setTextContent(script, js);
		htmlParser.getHTMLHeader(sheetDocument, true).appendChild(script);
		//替换字段内容
		NodeList fields = sheetDocument.getElementsByTagName("a");
		if(fields!=null && fields.getLength()>0) {
			for(int i=fields.getLength() - 1; i>=0; i--) {
				HTMLAnchorElement field = (HTMLAnchorElement)fields.item(i);
				String fieldName = field.getId();
				if(fieldName==null || fieldName.equals("")) {
					continue;
				}
				try {
					if("opinion".equals(fieldName)) { //办理意见
						Set opinions = (Set)PropertyUtils.getProperty(telegram, "opinions");
						if(opinions!=null && !opinions.isEmpty()) {
							String opinionType = htmlParser.getTextContent(field);
							opinionType = opinionType.substring(1, opinionType.length() - 3);
							for(Iterator iterator = opinions.iterator(); iterator.hasNext();) {
								Opinion opinion = (Opinion)iterator.next();
								if(!opinionType.equals(opinion.getOpinionType())) {
									continue;
								}
								//输出意见
								HTMLPreElement pre = (HTMLPreElement)sheetDocument.createElement("pre");
								pre.setAttribute("style", "word-wrap:break-word; margin:0px");
								htmlParser.setTextContent(pre, opinion.getOpinion());
								field.getParentNode().insertBefore(pre, field);
								//输出填写人和填写时间
								HTMLDivElement div = (HTMLDivElement)sheetDocument.createElement("div");
								div.setAttribute("style", "width:100%; padding-top:20px; padding-bottom:30px; padding-right:50px; text-align:right");
								htmlParser.setTextContent(div, opinion.getPersonName() + " " + new SimpleDateFormat("yyyy-MM-dd").format(opinion.getCreated()));
								field.getParentNode().insertBefore(div, field);
							}
						}
						field.getParentNode().removeChild(field);
					}
					else {
						String fieldValue = "";
						Object value = PropertyUtils.getProperty(telegram, fieldName);
						if(value!=null) {
							String fieldType = field.getAttribute("urn");
							if("timestamp".equals(fieldType)) { //时间
								fieldValue = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(value);
							}
							else if("date".equals(fieldType)) { //日期
								fieldValue = new SimpleDateFormat("yyyy-MM-dd").format(value);
							}
							else {
								fieldValue = value + "";
							}
						}
						field.getParentNode().replaceChild(sheetDocument.createTextNode(fieldValue), field);
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
			}
		}
		return htmlParser.getDocumentHTML(sheetDocument, "utf-8");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#saveOpinionPerson(java.lang.String)
	 */
	public void saveOpinionPerson(String personName) throws ServiceException {
		try {
			TelegramOpinionPerson opinionPerson = new TelegramOpinionPerson();
			opinionPerson.setId(UUIDLongGenerator.generateId()); //ID
			opinionPerson.setPersonName(personName.trim()); //用户名
			getDatabaseService().saveRecord(opinionPerson);
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.base.service.TelexService#totalTelegram(java.sql.Date, java.sql.Date, java.lang.String[], java.lang.String[], long)
	 */
	public List totalTelegram(Date beginDate, Date endDate, String[] telegramLevels, String[] securityLevels, long fromUnitId) throws ServiceException {
		List securityLevelList = listTelegramSecurityLevels();
		List telegramLevelList = listTelegramLevels();
		List totals = new ArrayList();
		TotalBySecurityLevel totalBySecurityLevel = new TotalBySecurityLevel();
		totalBySecurityLevel.setSecurityLevel("合计");
		totalBySecurityLevel.setTotals(doTotalTelegram(beginDate, endDate, telegramLevels, securityLevels, telegramLevelList, securityLevelList, fromUnitId));
		totals.add(totalBySecurityLevel);
		for(Iterator iterator = securityLevelList==null ? null : securityLevelList.iterator(); iterator!=null && iterator.hasNext();) {
			String securityLevel = (String)iterator.next();
			totalBySecurityLevel = new TotalBySecurityLevel();
			totalBySecurityLevel.setSecurityLevel(securityLevel);
			totalBySecurityLevel.setTotals(doTotalTelegram(beginDate, endDate, telegramLevels, new String[]{securityLevel}, telegramLevelList, securityLevelList, fromUnitId));
			totals.add(totalBySecurityLevel);
		}
		return totals;
	}
	
	/**
	 * 执行统计
	 * @param beginDate
	 * @param endDate
	 * @param telegramLevels
	 * @param securityLevels
	 * @param telegramLevelList
	 * @param securityLevelList
	 * @param fromUnitId
	 * @return
	 * @throws ServiceException
	 */
	private List doTotalTelegram(Date beginDate, Date endDate, String[] telegramLevels, String[] securityLevels, List telegramLevelList, List securityLevelList, long fromUnitId) throws ServiceException {
		//发报统计
		Total total = new Total("合计"); //合计
		Total crypticSendTotal = new Total("密文发报"); //密发
		Total plainSendTotal = new Total("明文发报"); //明发
		Total crypticReceiveTotal = new Total("密文收报"); //密收
		Total plainReceiveTotal = new Total("明文收报"); //明收
		String where = null;
		if(beginDate!=null) {
			where = "SendTelegram.sendTime>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")";
		}
		if(endDate!=null) {
			where = (where==null ? "" :  where + " and ") + "SendTelegram.sendTime<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, 1), null) + ")";
		}
		if(telegramLevels!=null && telegramLevels.length>0 && (telegramLevels.length!=1 || !telegramLevels[0].isEmpty())) {
			//获取电报级别列表
			if(telegramLevelList!=null && telegramLevels.length<telegramLevelList.size()) { //没有全部选中
				where = (where==null ? "" :  where + " and ") + "SendTelegram.telegramLevel in ('" + ListUtils.join(telegramLevels, "','", false) + "')";
			}
		}
		boolean hidePlain = false;
		if(securityLevels!=null && securityLevels.length>0 && (securityLevels.length!=1 || !securityLevels[0].isEmpty())) {
			//获取电报级别列表
			if(securityLevelList!=null && securityLevels.length<securityLevelList.size()) { //没有全部选中
				where = (where==null ? "" :  where + " and ") + "SendTelegram.securityLevel in ('" + ListUtils.join(securityLevels, "','", false) + "')";
				hidePlain = true;
			}
		}
		if(fromUnitId>0) {
			where = (where==null ? "" :  where + " and ") +
				    "(SendTelegram.fromUnitIds='" + fromUnitId + "'" +
				    "  or SendTelegram.fromUnitIds like '" + fromUnitId + ",%'" +
				    "  or SendTelegram.fromUnitIds like '%," + fromUnitId + "'" +
				    "  or SendTelegram.fromUnitIds like '%," + fromUnitId + ",%'" +
				    ")";
		}
		String hql = "select SendTelegram.toUnitIds, SendTelegram.isCryptic, SendTelegram.pages, SendTelegram.totalPages" +
					 " from SendTelegram SendTelegram" +
					 (where==null ? "" : " where " + where);
		for(int i=0; ; i+=200) {
			List telegrams = getDatabaseService().findRecordsByHql(hql, i, 200); //每次读200条记录
			if(telegrams==null || telegrams.isEmpty()) {
				break;
			}
			for(Iterator iterator = telegrams.iterator(); iterator.hasNext();) {
				Object[] values = (Object[])iterator.next();
				if(values[0]==null || values[0].equals("")) {
					continue;
				}
				int unitCount = ((String)values[0]).split(",").length;
				if('1'==((Character)values[1]).charValue()) {
					crypticSendTotal.setCount(crypticSendTotal.getCount() + 1); //电报数
					crypticSendTotal.setUnitCount(crypticSendTotal.getUnitCount() + unitCount); //单位数
					crypticSendTotal.setPages(crypticSendTotal.getPages() + ((Number)values[2]).intValue()); //页数
					crypticSendTotal.setUnitPages(crypticSendTotal.getUnitPages() + ((Number)values[3]).intValue()); //总页数
				}
				else {
					plainSendTotal.setCount(plainSendTotal.getCount() + 1); //电报数
					plainSendTotal.setUnitCount(plainSendTotal.getUnitCount() + unitCount); //单位数
					plainSendTotal.setPages(plainSendTotal.getPages() + ((Number)values[2]).intValue()); //页数
					plainSendTotal.setUnitPages(plainSendTotal.getUnitPages() + ((Number)values[3]).intValue()); //总页数
				}
			}
		}
		
		//收报统计
		where = (where==null ? null : where.replaceAll("SendTelegram.", "ReceiveTelegram.").replaceAll(".sendTime", ".receiveTime"));
		//密文收报
		hql = "select count(ReceiveTelegram.id), sum(ReceiveTelegram.pages)" +
			  " from ReceiveTelegram ReceiveTelegram" +
			  " where ReceiveTelegram.isCryptic='1'" +
			  (where==null ? "" : " and " + where);
		Object[] values = (Object[])getDatabaseService().findRecordByHql(hql);
		if(values!=null) {
			crypticReceiveTotal.setCount(values[0]==null ? 0 : ((Number)values[0]).intValue()); //电报数
			crypticReceiveTotal.setPages(values[1]==null ? 0 : ((Number)values[1]).intValue()); //页数
		}
		if(!hidePlain) {
			//明文收报
			hql = "select count(ReceiveTelegram.id), sum(ReceiveTelegram.pages)" +
				  " from ReceiveTelegram ReceiveTelegram" +
				  " where ReceiveTelegram.isCryptic!='1'" +
				  (where==null ? "" : " and " + where);
			values = (Object[])getDatabaseService().findRecordByHql(hql);
			if(values!=null) {
				plainReceiveTotal.setCount(values[0]==null ? 0 : ((Number)values[0]).intValue()); //电报数
				plainReceiveTotal.setPages(values[1]==null ? 0 : ((Number)values[1]).intValue()); //页数
			}
		}
		//密文签收
		hql = "select count(TelegramSign.id), sum(ReceiveTelegram.pages)" +
			  " from TelegramSign TelegramSign, ReceiveTelegram ReceiveTelegram" +
			  " where ReceiveTelegram.isCryptic='1'" +
			  " and TelegramSign.telegramId=ReceiveTelegram.id" +
			  " and not TelegramSign.signTime is null" +
			  (where==null ? "" : " and " + where);
		values = (Object[])getDatabaseService().findRecordByHql(hql);
		if(values!=null) {
			crypticReceiveTotal.setUnitCount(values[0]==null ? 0 : ((Number)values[0]).intValue()); //单位数
			crypticReceiveTotal.setUnitPages(values[1]==null ? 0 : ((Number)values[1]).intValue()); //总页数
		}
		if(!hidePlain) {
			//明文签收
			hql = "select count(TelegramSign.id), sum(ReceiveTelegram.pages)" +
				  " from TelegramSign TelegramSign, ReceiveTelegram ReceiveTelegram" +
				  " where ReceiveTelegram.isCryptic!='1'" +
				  " and TelegramSign.telegramId=ReceiveTelegram.id" +
				  " and not TelegramSign.signTime is null" +
				  (where==null ? "" : " and " + where);
			values = (Object[])getDatabaseService().findRecordByHql(hql);
			if(values!=null) {
				plainReceiveTotal.setUnitCount(values[0]==null ? 0 : ((Number)values[0]).intValue()); //单位数
				plainReceiveTotal.setUnitPages(values[1]==null ? 0 : ((Number)values[1]).intValue()); //总页数
			}
		}
		//合计
		total.setCount(crypticSendTotal.getCount() + plainSendTotal.getCount() + crypticReceiveTotal.getCount() + plainReceiveTotal.getCount());
		total.setUnitCount(crypticSendTotal.getUnitCount() + plainSendTotal.getUnitCount() + crypticReceiveTotal.getUnitCount() + plainReceiveTotal.getUnitCount());
		total.setPages(crypticSendTotal.getPages() + plainSendTotal.getPages() + crypticReceiveTotal.getPages() + plainReceiveTotal.getPages());
		total.setUnitPages(crypticSendTotal.getUnitPages() + plainSendTotal.getUnitPages() + crypticReceiveTotal.getUnitPages() + plainReceiveTotal.getUnitPages());
		
		//返回统计数据列表
		List totals = new ArrayList();
		totals.add(crypticSendTotal);
		if(!hidePlain) {
			totals.add(plainSendTotal);
		}
		totals.add(crypticReceiveTotal);
		if(!hidePlain) {
			totals.add(plainReceiveTotal);
		}
		totals.add(total);
		return totals;
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
