/*
 * Created on 2006-5-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 邮件过滤服务
 * @author linchuan
 * 
 */
public interface MailFilterService extends BusinessService {
    public static final String ACTION_TYPE_DELETE = "delete";
    public static final String ACTION_TYPE_MOVE = "move";
    
    /**
     * 过滤邮件
     * @param mailSession
     * @param mails
     * @throws ServiceException
     */
    public void doFilter(List mails, SessionInfo sessionInfo) throws ServiceException;
    
    /**
     * 调整过滤规则优先级
     * @param mailSession
     * @param ruleIds
     * @param up
     * @throws ServiceException
     */
    public void adjustFilterPriority(String ruleIds, boolean up, SessionInfo sessionInfo) throws ServiceException;
}
