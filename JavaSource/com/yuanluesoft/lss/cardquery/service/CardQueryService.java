/**
 * 
 */
package com.yuanluesoft.lss.cardquery.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * @author kangshiwei
 *
 */
public interface CardQueryService {
 public void importData(ActionForm form,long recordId,SessionInfo sessionInfo,HttpServletRequest request, HttpServletResponse response) throws ServiceException;
}
