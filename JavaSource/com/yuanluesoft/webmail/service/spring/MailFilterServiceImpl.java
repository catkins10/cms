/*
 * Created on 2006-5-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.service.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.webmail.pojo.Mail;
import com.yuanluesoft.webmail.pojo.MailFilter;
import com.yuanluesoft.webmail.service.MailFilterService;
import com.yuanluesoft.webmail.service.WebMailService;

/**
 *
 * @author linchuan
 *
 */
public class MailFilterServiceImpl extends BusinessServiceImpl implements MailFilterService {
    private ViewService viewService; //视图数据服务
    private ViewDefineService viewDefineService; //视图定义服务
    private WebMailService webMailService; //邮件服务
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.MailFilterService#doFilter(java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void doFilter(List mails, SessionInfo sessionInfo) throws ServiceException {
		if(mails==null || mails.isEmpty()) { //没有邮件需要过滤
            return;
        }
        //获取过滤规则
        String hql = "from MailFilter MailFilter where MailFilter.userId=" + sessionInfo.getUserId() + " and MailFilter.enable='1' order by MailFilter.priority";
        List rules = getDatabaseService().findRecordsByHql(hql);
        if(rules==null || rules.isEmpty()) {
            return;
        }
        //获取邮件ID列表
        String mailIds = null;
        for(Iterator iterator = mails.iterator(); iterator.hasNext();) {
            Mail mail = (Mail)iterator.next();
            mailIds = (mailIds==null ? "" : mailIds + ",") + mail.getId();
        }
        //获取收件箱视图
        View inboxView;
        try {
            inboxView = viewDefineService.getView("webmail", "inbox", sessionInfo);
        }
        catch (Exception e) {
            Logger.exception(e);
            throw new ServiceException();
        }
        inboxView.setPageRows(0);
        for(Iterator iterator = rules.iterator(); iterator.hasNext();) {
            MailFilter mailFilter = (MailFilter)iterator.next();
            String conditions = mailFilter.getConditions();
            if(conditions==null || conditions.equals("")) { //条件为空
                continue;
            }
            inboxView.setWhere("Mail.userId=" + sessionInfo.getUserId() + " and Mail.id in (" + JdbcUtils.validateInClauseNumbers(mailIds) + ")");
            conditions = conditions.substring(conditions.indexOf("\r\n\r\n") + 4);
            List matchMails;
            try {
            	ViewPackage viewPackage = new ViewPackage();
            	viewPackage.setView(inboxView);
            	viewPackage.setSearchConditions(conditions);
            	viewPackage.setSearchMode(true);
            	viewPackage.setViewMode(View.VIEW_DISPLAY_MODE_NORMAL);
                viewService.retrieveViewPackage(viewPackage, inboxView, 0, true, false, false, null, sessionInfo);
                if(viewPackage.getRecords()==null || viewPackage.getRecords().isEmpty()) { //没有匹配的邮件
                    continue;
                }
                matchMails = viewPackage.getRecords();
            }
            catch (Exception e) {
                Logger.exception(e);
                throw new ServiceException();
            }
            //处理和过滤条件匹配的邮件
            String action = mailFilter.getAction();
            if(action==null || action.equals("")) { //没有需要执行的操作
                continue;
            }
            String[] values = action.split("\\x7c");
            if(MailFilterService.ACTION_TYPE_DELETE.equals(values[1])) { //删除
                for(Iterator iteratorMail = matchMails.iterator(); iteratorMail.hasNext();) {
                    webMailService.deleteMail(((Mail)iteratorMail.next()).getId(), sessionInfo);
                }
            }
            else if(MailFilterService.ACTION_TYPE_MOVE.equals(values[1])) { //发送到邮箱
                long mailboxId = Long.parseLong(values[2]);
                for(Iterator iteratorMail = matchMails.iterator(); iteratorMail.hasNext();) {
                    webMailService.moveMail(((Mail)iteratorMail.next()).getId(), mailboxId, sessionInfo);
                }
            }
            else {
                continue;
            }
            //从邮件ID列表中删除已经被过滤调的邮件
            mailIds += ",";
            for(Iterator iteratorMail = matchMails.iterator(); iteratorMail.hasNext();) {
                long mailId = ((Mail)iteratorMail.next()).getId();
                mailIds = mailIds.replaceFirst(mailId + ",", "");
            }
            if(mailIds.equals("")) { //所有邮件都处理完毕
                break;
            }
            mailIds = mailIds.substring(0, mailIds.length() - 1);
        }
    }

    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.MailFilterService#adjustFilterPriority(java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void adjustFilterPriority(String ruleIds, boolean up, SessionInfo sessionInfo) throws ServiceException {
		if(ruleIds==null || ruleIds.equals("")) {
            return;
        }
        String hql = "from MailFilter MailFilter where MailFilter.userId=" + sessionInfo.getUserId();
        if(up) { //上调优先级
            List mailFilters = getDatabaseService().findRecordsByHql(hql + " and MailFilter.id in (" + JdbcUtils.validateInClauseNumbers(ruleIds) + ") order by MailFilter.priority");
            for(Iterator iterator = mailFilters.iterator(); iterator.hasNext();) {
                MailFilter mailFilter = (MailFilter)iterator.next();
                if(mailFilter.getPriority()==1) { //已经是最高优先级
                    break;
                }
                if(ListUtils.findObjectByProperty(mailFilters, "priority", new Long(mailFilter.getPriority()-1))!=null) {
                    //高一级规则已经在列表中
                    break;
                }
                //查找比当前过滤规则优先级高一级的规则
                MailFilter upMailFilter = (MailFilter)getDatabaseService().findRecordByHql(hql + " and MailFilter.priority=" + (mailFilter.getPriority()-1));
                upMailFilter.setPriority(upMailFilter.getPriority() + 1); //降低优先级
                mailFilter.setPriority(mailFilter.getPriority() - 1); //提高优先级
                getDatabaseService().updateRecord(mailFilter);
                getDatabaseService().updateRecord(upMailFilter);
            }
        }
        else { //下调优先级
            List mailFilters = getDatabaseService().findRecordsByHql(hql + " and MailFilter.id in (" + JdbcUtils.validateInClauseNumbers(ruleIds) + ") order by MailFilter.priority desc");
            for(Iterator iterator = mailFilters.iterator(); iterator.hasNext();) {
                MailFilter mailFilter = (MailFilter)iterator.next();
                if(ListUtils.findObjectByProperty(mailFilters, "priority", new Long(mailFilter.getPriority()+1))!=null) {
                    //低一级规则已经在列表中
                    break;
                }
                //查找比当前过滤规则优先级低一级的规则
                MailFilter downMailFilter = (MailFilter)getDatabaseService().findRecordByHql(hql + " and MailFilter.priority=" + (mailFilter.getPriority()+1));
                if(downMailFilter==null) { //已经是最低优先级
                    break;
                }
                downMailFilter.setPriority(downMailFilter.getPriority() - 1); //提高优先级
                mailFilter.setPriority(mailFilter.getPriority() + 1); //降低优先级
                getDatabaseService().updateRecord(mailFilter);
                getDatabaseService().updateRecord(downMailFilter);
            }
        }
    }
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
     */
    public void delete(Record record) throws ServiceException {
        MailFilter mailFilter = (MailFilter)record;
        int priority = mailFilter.getPriority();
        long userId = mailFilter.getUserId();
        super.delete(record);
        //更新优先级
        List mailFilters = getDatabaseService().findRecordsByHql("from MailFilter MailFilter where MailFilter.userId=" + userId + " and MailFilter.priority>" + priority);
        if(mailFilters!=null) {
            for(Iterator iterator = mailFilters.iterator(); iterator.hasNext();) {
                mailFilter = (MailFilter)iterator.next();
                mailFilter.setPriority(mailFilter.getPriority() - 1);
                getDatabaseService().updateRecord(mailFilter);
            }
        }
    }
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
     */
    public Record save(Record record) throws ServiceException {
        //设置过滤优先级
        MailFilter mailFilter = (MailFilter)record;
        Integer maxPriority = (Integer)getDatabaseService().findRecordByHql("select max(MailFilter.priority) from MailFilter MailFilter where MailFilter.userId=" + mailFilter.getUserId());
        if(maxPriority==null) {
            mailFilter.setPriority(1);
        }
        else {
            mailFilter.setPriority(maxPriority.intValue() + 1);
        }
        return super.save(record);
    }
    /**
     * @return Returns the viewDefineService.
     */
    public ViewDefineService getViewDefineService() {
        return viewDefineService;
    }
    /**
     * @param viewDefineService The viewDefineService to set.
     */
    public void setViewDefineService(ViewDefineService viewDefineService) {
        this.viewDefineService = viewDefineService;
    }
	/**
	 * @return the webMailService
	 */
	public WebMailService getWebMailService() {
		return webMailService;
	}

	/**
	 * @param webMailService the webMailService to set
	 */
	public void setWebMailService(WebMailService webMailService) {
		this.webMailService = webMailService;
	}

	/**
	 * @return the viewService
	 */
	public ViewService getViewService() {
		return viewService;
	}

	/**
	 * @param viewService the viewService to set
	 */
	public void setViewService(ViewService viewService) {
		this.viewService = viewService;
	}
}
