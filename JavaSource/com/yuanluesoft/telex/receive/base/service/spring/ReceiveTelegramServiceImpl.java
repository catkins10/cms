package com.yuanluesoft.telex.receive.base.service.spring;

import java.io.File;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.fingerprint.service.FingerprintService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.telex.base.pojo.TelegramSn;
import com.yuanluesoft.telex.receive.base.model.TelegramSignPerson;
import com.yuanluesoft.telex.receive.base.pojo.ReceiveTelegram;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSign;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSignAgent;
import com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService;

/**
 * 
 * @author linchuan
 *
 */
public class ReceiveTelegramServiceImpl extends BusinessServiceImpl implements ReceiveTelegramService {
	private OrgService orgService; //组织机构服务
	private PersonService personService; //用户服务
	private FingerprintService fingerprintService; //指纹服务

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof ReceiveTelegram) {
			ReceiveTelegram currentTelegram = (ReceiveTelegram)record;
			if(currentTelegram.getSequence()!=null && !currentTelegram.getSequence().equals("")) {
				//调整当前电报之后的其他电报的顺序号
				int year = DateTimeUtils.getYear(currentTelegram.getCreated());
				String hql = "from ReceiveTelegram ReceiveTelegram" +
							 " where ReceiveTelegram.id!=" + currentTelegram.getId() +
							 " and ReceiveTelegram.isCryptic" + (currentTelegram.getIsCryptic()=='1' ? "=" : "!=") + "'1'" +
							 " and ReceiveTelegram.created>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(currentTelegram.getCreated(), null) + ")" +
							 " and ReceiveTelegram.created<TIMESTAMP(" + (year + 1) + "-01-01 00:00:00)" + //同一年
							 " order by ReceiveTelegram.created";
				int sequence = Integer.parseInt(currentTelegram.getSequence());
				List telegrams = getDatabaseService().findRecordsByHql(hql);
				if(telegrams!=null && !telegrams.isEmpty()) {
					for(Iterator iterator = telegrams.iterator(); iterator.hasNext();) {
						ReceiveTelegram telegram = (ReceiveTelegram)iterator.next();
						telegram.setSequence((sequence++) + "");
						getDatabaseService().updateRecord(telegram);
					}
				}
				//修改参数配置中的当前顺序号
				hql = "from TelegramSn TelegramSn" +
					  " where TelegramSn.isSend!='1'" +
					  " and TelegramSn.isCryptic" + (currentTelegram.getIsCryptic()=='1' ? "=" : "!=") +  "'1'" +
					  " and TelegramSn.year=" + year;
				TelegramSn telegramSn = (TelegramSn)getDatabaseService().findRecordByHql(hql);
				telegramSn.setSn(sequence - 1);
				getDatabaseService().updateRecord(telegramSn);
			}
		}
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService#getReceiveTelegram(long)
	 */
	public ReceiveTelegram getReceiveTelegram(long id) throws ServiceException {
		return (ReceiveTelegram)getDatabaseService().findRecordById(ReceiveTelegram.class.getName(), id, ListUtils.generateList("signs,opinions", ","));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.sign.service.TelegramSignService#appendSignReceivers(long, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void appendSignReceivers(long telegramId, String receiverIds, String receiverNames, SessionInfo sessionInfo) throws ServiceException {
		if(receiverIds==null || "".equals(receiverIds)) {
			return;
		}
		String[] ids = receiverIds.split(",");
		String[] names = receiverNames.split(",");
		Timestamp now = DateTimeUtils.now();
		ReceiveTelegram telegram = getReceiveTelegram(telegramId);
		for(int i=0; i<ids.length; i++) {
			TelegramSign telegramSign = new TelegramSign();
			telegramSign.setId(UUIDLongGenerator.generateId()); //ID
			telegramSign.setTelegramId(telegramId); //电报ID
			telegramSign.setReceiverId(Long.parseLong(ids[i])); //接收人ID
			telegramSign.setReceiverName(names[i]); //接收人姓名
			telegramSign.setCreated(now); //创建时间
			telegramSign.setCreatorId(sessionInfo.getUserId()); //创建人ID
			telegramSign.setCreator(sessionInfo.getUserName()); //创建人用户名
			telegramSign.setNeedReturn(telegram.getNeedReturn()); //是否需要清退
			getDatabaseService().saveRecord(telegramSign);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService#deleteSignReceivers(java.lang.String, boolean)
	 */
	public void deleteSignReceivers(String telegramSignIds, boolean notSignOnly) throws ServiceException {
		String hql = "from TelegramSign TelegramSign" +
					 " where TelegramSign.id in (" + telegramSignIds + ")" +
					 (notSignOnly ? " and TelegramSign.signTime is null" : ""); //只能删除未签收过的
		getDatabaseService().deleteRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService#setSignReceiverReturn(java.lang.String, boolean)
	 */
	public void setReturnOption(String telegramSignIds, boolean needReturn) throws ServiceException {
		String hql = "from TelegramSign TelegramSign" +
		 			 " where TelegramSign.id in (" + telegramSignIds + ")" +
		 			 " and TelegramSign.returnTime is null" +
		 			 " and TelegramSign.needReturn" + (needReturn ? "!=" : "=") + "'1'";
		List telegramSigns = getDatabaseService().findRecordsByHql(hql);
		if(telegramSigns==null || telegramSigns.isEmpty()) {
			return;
		}
		for(Iterator iterator = telegramSigns.iterator(); iterator.hasNext();) {
			TelegramSign telegramSign = (TelegramSign)iterator.next();
			telegramSign.setNeedReturn(needReturn ? '1' : '0');
			getDatabaseService().updateRecord(telegramSign);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.sign.service.TelegramSignService#listTelegrams(java.lang.String)
	 */
	public List listTelegrams(String telegramSignIds) throws ServiceException {
		String hql = "from TelegramSign TelegramSign" +
					 " where TelegramSign.id in (" + telegramSignIds + ")";
		return ListUtils.sortByProperty(getDatabaseService().findRecordsByHql(hql), "id", telegramSignIds);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.sign.service.TelegramSignService#listToSignTelegrams(com.yuanluesoft.telex.sign.model.TelegramSignPerson, java.lang.String)
	 */
	public List listToSignTelegrams(TelegramSignPerson signPerson, String telegramIds) throws ServiceException {
		//获取待签收的电报列表
		String hql = "Select TelegramSign" +
					 " from TelegramSign TelegramSign" +
					 " left join TelegramSign.telegram ReceiveTelegram" +
					 " where ReceiveTelegram.archiveTime is null" + //未归档
					 " and ReceiveTelegram.isCryptic='1'" + //密文
					 " and TelegramSign.receiverId in (0," + signPerson.getPersonId() + "," + signPerson.getPersonOrgIds() + (signPerson.getAgentPersonOrOrgIds()==null || signPerson.getAgentPersonOrOrgIds().equals("") ? "" : "," + signPerson.getAgentPersonOrOrgIds()) + ")" +
					 " and TelegramSign.signTime is null" +
					 (telegramIds==null ? "" : " and ReceiveTelegram.id in (" + telegramIds + ")") +
					 " order by TelegramSign.created DESC";
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.sign.service.TelegramSignService#listToReturnTelegrams(com.yuanluesoft.telex.sign.model.TelegramSignPerson, java.lang.String)
	 */
	public List listToReturnTelegrams(TelegramSignPerson signPerson, String telegramIds) throws ServiceException {
		//获取待清退的电报列表
		String hql = "Select TelegramSign" +
					 " from TelegramSign TelegramSign" +
					 " left join TelegramSign.telegram ReceiveTelegram" +
					 " where ReceiveTelegram.archiveTime is null" + //未归档
					 " and TelegramSign.needReturn='1'" + //需要退报
					 " and ReceiveTelegram.isCryptic='1'" + //密文
					 " and TelegramSign.receiverId in (0," + signPerson.getPersonId() + "," + signPerson.getPersonOrgIds() + (signPerson.getAgentPersonOrOrgIds()==null || signPerson.getAgentPersonOrOrgIds().equals("") ? "" : "," + signPerson.getAgentPersonOrOrgIds()) + ")" +
					 " and not (TelegramSign.signTime is null)" +
					 " and TelegramSign.returnTime is null" +
					 (telegramIds==null ? "" : " and ReceiveTelegram.id in (" + telegramIds + ")") +
					 " order by TelegramSign.created DESC";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService#signTelegrams(com.yuanluesoft.telex.receive.base.model.TelegramSignPerson, java.lang.String, java.sql.Timestamp, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List signTelegrams(TelegramSignPerson signPerson, String telegramSignIds, Timestamp signTime, SessionInfo sessionInfo) throws ServiceException {
			if(telegramSignIds==null || telegramSignIds.equals("")) {
			return null;
		}
		String hql = "Select TelegramSign" +
					 " from TelegramSign TelegramSign" +
					 " where TelegramSign.signTime is null" +
					 (signPerson.getPersonOrgIds()==null ? "" : " and TelegramSign.receiverId in (0," + signPerson.getPersonId() + "," + signPerson.getPersonOrgIds() + (signPerson.getAgentPersonOrOrgIds()==null || signPerson.getAgentPersonOrOrgIds().equals("") ? "" : "," + signPerson.getAgentPersonOrOrgIds()) + ")") +
					 " and TelegramSign.id in (" + telegramSignIds + ")";
		List telegramSigns = getDatabaseService().findRecordsByHql(hql);
		if(telegramSigns==null) {
			return null;
		}
		for(Iterator iterator = telegramSigns.iterator(); iterator.hasNext();) {
			TelegramSign telegramSign = (TelegramSign)iterator.next();
			telegramSign.setSignPersonId(signPerson.getPersonId()); //签收人ID
			telegramSign.setSignPersonName(signPerson.getPersonName()); //签收人姓名
			telegramSign.setSignTime(signTime); //签收时间
			telegramSign.setSignOperatorId(sessionInfo.getUserId()); //经办人ID
			telegramSign.setSignOperatorName(sessionInfo.getUserName()); //经办人姓名
			telegramSign.setIsAgentSign(signPerson.isAgent() ? '1' : '0'); //是否代签收
			getDatabaseService().updateRecord(telegramSign);
		}
		return telegramSigns;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.sign.service.TelegramSignService#returnTelegrams(com.yuanluesoft.telex.sign.model.TelegramSignPerson, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List returnTelegrams(TelegramSignPerson signPerson, String telegramSignIds, SessionInfo sessionInfo) throws ServiceException {
		if(telegramSignIds==null || telegramSignIds.equals("")) {
			return null;
		}
		String hql = "Select TelegramSign" +
					 " from TelegramSign TelegramSign" +
					 " where not TelegramSign.signTime is null" +
					 " and TelegramSign.receiverId in (" + signPerson.getPersonId() + "," + signPerson.getPersonOrgIds() + (signPerson.getAgentPersonOrOrgIds()==null || signPerson.getAgentPersonOrOrgIds().equals("") ? "" : "," + signPerson.getAgentPersonOrOrgIds()) + ")" +
					 " and TelegramSign.needReturn='1'" +
					 " and TelegramSign.returnTime is null" +
					 " and TelegramSign.id in (" + telegramSignIds + ")";
		List telegramSigns = getDatabaseService().findRecordsByHql(hql);
		if(telegramSigns==null) {
			return null;
		}
		Timestamp now = DateTimeUtils.now();
		for(Iterator iterator = telegramSigns.iterator(); iterator.hasNext();) {
			TelegramSign telegramSign = (TelegramSign)iterator.next();
			telegramSign.setReturnPersonId(signPerson.getPersonId()); //清退人ID
			telegramSign.setReturnPersonName(signPerson.getPersonName()); //清退人姓名
			telegramSign.setReturnTime(now); //清退时间
			telegramSign.setReturnOperatorId(sessionInfo.getUserId()); //经办人ID
			telegramSign.setReturnOperatorName(sessionInfo.getUserName()); //经办人姓名
			telegramSign.setIsAgentReturn(signPerson.isAgent() ? '1' : '0'); //是否代清退
			getDatabaseService().updateRecord(telegramSign);
		}
		return telegramSigns;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.sign.service.TelegramSignService#getSignPerson(long)
	 */
	public TelegramSignPerson getSignPerson(long personId) throws ServiceException {
		TelegramSignPerson signPerson = new TelegramSignPerson();
		signPerson.setPersonId(personId); //用户ID
		
		Person person = personService.getPerson(personId);
        String orgIds;
        if(person==null) { //不是系统注册用户
        	//检查是否代理人
        	TelegramSignAgent signAgent = (TelegramSignAgent)getDatabaseService().findRecordById(TelegramSignAgent.class.getName(), personId);
        	if(signAgent==null) {
        		throw new ServiceException();
        	}
        	//设置用户信息
        	signPerson.setPersonName(signAgent.getName());
        	//设置所在单位全称
        	signPerson.setPersonOrgId(signAgent.getOrgId());
        	orgIds = signAgent.getOrgId() + "," + orgService.listParentOrgIds(signAgent.getOrgId());
        	signPerson.setAgent(true); //标记为代理人
        }
        else { //系统注册用户
        	//设置用户信息
        	signPerson.setPersonName(person.getName());
        	//设置所在单位ID
        	orgIds = orgService.listOrgIdsOfPerson("" +person.getId(), true);
        	signPerson.setPersonOrgId(Long.parseLong(orgIds.split(",")[0]));
        	//获取用户代理的领导ID列表
        	String hql = "select TelegramLeaderAgent.leaderId" +
        				 " from TelegramLeaderAgent TelegramLeaderAgent" +
        				 " where TelegramLeaderAgent.agentId in (" + personId + "," + orgIds + ")";
        	List leaderIds = getDatabaseService().findRecordsByHql(hql);
        	//向上继承代理关系
        	if(leaderIds!=null && !leaderIds.isEmpty()) {
        		int size = leaderIds.size();
        		for(int i=0; i<size; i++) {
        			long leaderId = ((Long)leaderIds.get(i)).longValue();
        			//按用户获取上级机构列表
        			String leaderOrgIds = orgService.listOrgIdsOfPerson("" + personId, true);
        			if(leaderOrgIds==null || leaderOrgIds.equals("")) {
        				//按组织机构获取上级上级机构列表
        				leaderOrgIds = orgService.listParentOrgIds(leaderId);
        				if(leaderOrgIds==null || leaderOrgIds.equals("")) {
        					continue;
        				}
        			}
        			//插入到id列表
        			String[] ids = leaderOrgIds.split(",");
        			for(int j=0; j<ids.length; j++) {
        				Long id = new Long(ids[j]);
        				if(leaderIds.indexOf(id)==-1) {
        					leaderIds.add(id);
        				}
        			}
        		}
        	}
        	signPerson.setAgentPersonOrOrgIds(ListUtils.join(leaderIds, ",", false));
        }
        signPerson.setPersonOrgIds(orgIds); //全部机构ID
    	signPerson.setPersonOrgFullName(orgService.getDirectoryFullName(signPerson.getPersonOrgId(), "/", "unit")); //所在单位全称
    	return signPerson;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.sign.service.TelegramSignService#saveSignAgent(java.lang.String, long, java.lang.String, java.lang.String, java.lang.String, char, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public TelegramSignAgent saveSignAgent(String name, long orgId, String orgName, String certificateName, String certificateCode, char sex, String fingerprintTemplate, SessionInfo sessionInfo) throws ServiceException {
		TelegramSignAgent signAgent = new TelegramSignAgent();
		signAgent.setId(UUIDLongGenerator.generateId()); //ID
		signAgent.setName(name); //姓名
		signAgent.setOrgId(orgId); //所在部门或单位
		signAgent.setOrgName(orgName); //所在部门或单位
		signAgent.setSex(sex); //性别
		signAgent.setCertificateCode(certificateCode); //证件号码
		signAgent.setCertificateName(certificateName); //证件名称
		signAgent.setCreated(DateTimeUtils.now()); //创建时间
		signAgent.setCreator(sessionInfo.getUserName()); //创建人
		signAgent.setCreatorId(sessionInfo.getUserId()); //创建人ID
		getDatabaseService().saveRecord(signAgent);
		//保存指纹
		fingerprintService.saveFingerprintTemplate(signAgent.getId(), signAgent.getName(), null, fingerprintTemplate);
		return signAgent;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.telex.receive.base.service.ReceiveTelegramService#exportListing(int, int, boolean, javax.servlet.http.HttpServletResponse)
	 */
	public void exportListing(int year, int month, boolean isCryptic, HttpServletResponse response) throws ServiceException {
		jxl.write.WritableWorkbook  wwb = null;
		jxl.Workbook rw = null;
		try {
			String title = year + "年" + month + "月份密码电报收报核对清单";
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "inline; filename=\"" + URLEncoder.encode(title + ".xls", "UTF-8") + "\"");
			//读入报表模板
			rw = jxl.Workbook.getWorkbook(new File(Environment.getWebinfPath() + "telex/receive/cryptic/template/telex.template.xls"));

			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(response.getOutputStream(), rw);
			jxl.write.WritableSheet ws = wwb.getSheet(0);
			
			//标题
			jxl.write.Label label = new jxl.write.Label(0, 0, title, ws.getCell(0, 0).getCellFormat());
			ws.addCell(label);
			
			final int TOP_ROWS = 2; //从第3行开始输出数据
			
			//获取数据
			Date beginDate = DateTimeUtils.parseDate(year + "-" + month + "-1", "yyyy-M-d");
			Date endDate = DateTimeUtils.add(beginDate, Calendar.MONTH, 1);
			String hql = "from ReceiveTelegram ReceiveTelegram" +
						 " where ReceiveTelegram.isCryptic" + (isCryptic ? "=" : "!=") + "'1'" +
						 " and ReceiveTelegram.created>=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")"  +
						 " and ReceiveTelegram.created<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")"  +
						 " order by ReceiveTelegram.created";
			int rowIndex = TOP_ROWS + 1;
			int totalNumber = 0; //份数累计
			int totalSigns = 0; //实际办理数累计
			int totalReturns = 0; //实际清退数累计
			int totalPages = 0; //页数累计
			for(int i=0; ;i+=100) {
				List telegrams = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("signs", ","), i, 100); //每次取100条记录
				if(telegrams==null || telegrams.isEmpty()) {
					break;
				}
				//输出记录
				for(Iterator iterator = telegrams.iterator(); iterator.hasNext();) {
					ReceiveTelegram telegram = (ReceiveTelegram)iterator.next();
					int number = telegram.getSigns()==null ? 0 : telegram.getSigns().size();
					int pages = number * telegram.getPages();
					totalNumber += number;
					totalPages += pages;
					
					//实际办理数、清退数
					int signCount = 0;
					int returnCount = 0;
					for(Iterator iteratorSign = telegram.getSigns()==null ? null : telegram.getSigns().iterator(); iteratorSign!=null && iteratorSign.hasNext();) {
						TelegramSign sign = (TelegramSign)iteratorSign.next();
						if(sign.getSignTime()!=null) {
							signCount++;
						}
						if(sign.getReturnTime()!=null) {
							returnCount++;
						}
					}
					totalSigns += signCount;
					totalReturns += returnCount;
					ws.insertRow(rowIndex);
					writeTelegram(ws, telegram, number, signCount, returnCount, pages, rowIndex++, TOP_ROWS);
				}
			}
			//删除第一行,模板数据
			ws.removeRow(TOP_ROWS);
			
			rowIndex--;
			//总份数
			jxl.write.Number labelNumber = new jxl.write.Number(4, rowIndex, totalNumber, ws.getCell(4, rowIndex).getCellFormat());
			ws.addCell(labelNumber);
			//总办理数
			labelNumber = new jxl.write.Number(5, rowIndex, totalSigns, ws.getCell(5, rowIndex).getCellFormat());
			ws.addCell(labelNumber);
			//总清退数
			labelNumber = new jxl.write.Number(6, rowIndex, totalReturns, ws.getCell(6, rowIndex).getCellFormat());
			ws.addCell(labelNumber);
			//总页数
			labelNumber = new jxl.write.Number(9, rowIndex, totalPages, ws.getCell(9, rowIndex).getCellFormat());
			ws.addCell(labelNumber);
			wwb.write();
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			//关闭Excel工作薄对象
			try {
				wwb.close();
			}
			catch(Exception e) {
				
			}
			//关闭只读的Excel对象
			rw.close();
		}
	}
	
	/**
	 * 输出一个电报
	 * @param ws
	 * @param telegram
	 * @param number
	 * @param signCount
	 * @param returnCount
	 * @param pages
	 * @param rowIndex
	 * @param firstRowIndex
	 * @throws Exception
	 */
	private void writeTelegram(jxl.write.WritableSheet ws, ReceiveTelegram telegram, int number, int signCount, int returnCount, int pages, int rowIndex, int firstRowIndex) throws Exception {
		//日期
		jxl.write.Label label = new jxl.write.Label(0, rowIndex, DateTimeUtils.getDay(telegram.getCreated()) + "", ws.getCell(0, firstRowIndex).getCellFormat());
		ws.addCell(label);
		
		//发电单位
		label = new jxl.write.Label(1, rowIndex, telegram.getFromUnitNames(), ws.getCell(1, firstRowIndex).getCellFormat());
		ws.addCell(label);
		
		//榕机密收序号
		label = new jxl.write.Label(2, rowIndex, telegram.getSequence(), ws.getCell(2, firstRowIndex).getCellFormat());
		ws.addCell(label);
		
		//标题
		label = new jxl.write.Label(3, rowIndex, telegram.getSubject(), ws.getCell(3, firstRowIndex).getCellFormat());
		ws.addCell(label);
	
		//份数
		jxl.write.Number labelNumber = new jxl.write.Number(4, rowIndex, number, ws.getCell(4, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		
		//实际办理数
		labelNumber = new jxl.write.Number(5, rowIndex, signCount, ws.getCell(5, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		
		//实际清退数
		labelNumber = new jxl.write.Number(6, rowIndex, returnCount, ws.getCell(6, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		
		//等级
		label = new jxl.write.Label(7, rowIndex, telegram.getTelegramLevel(), ws.getCell(7, firstRowIndex).getCellFormat());
		ws.addCell(label);
		
		//密级
		label = new jxl.write.Label(8, rowIndex, telegram.getSecurityLevel(), ws.getCell(8, firstRowIndex).getCellFormat());
		ws.addCell(label);
		
		//页数
		labelNumber = new jxl.write.Number(9, rowIndex, pages, ws.getCell(9, firstRowIndex).getCellFormat());
		ws.addCell(labelNumber);
		
		//备注
		label = new jxl.write.Label(10, rowIndex, telegram.getRemark(), ws.getCell(10, firstRowIndex).getCellFormat());
		ws.addCell(label);
	}

	/**
	 * @return the fingerprintService
	 */
	public FingerprintService getFingerprintService() {
		return fingerprintService;
	}

	/**
	 * @param fingerprintService the fingerprintService to set
	 */
	public void setFingerprintService(FingerprintService fingerprintService) {
		this.fingerprintService = fingerprintService;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}
