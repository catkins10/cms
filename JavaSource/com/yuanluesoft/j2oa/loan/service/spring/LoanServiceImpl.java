/*
 * Created on 2006-6-22
 *
 */
package com.yuanluesoft.j2oa.loan.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.loan.pojo.Loan;
import com.yuanluesoft.j2oa.loan.pojo.LoanConfig;
import com.yuanluesoft.j2oa.loan.service.LoanService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;

/**
 *
 * @author linchuan
 *
 */
public class LoanServiceImpl extends BusinessServiceImpl implements LoanService {
    private WorkflowExploitService workflowExploitService; //工作流利用服务
    private SessionService sessionService;

    /* (non-Javadoc)
     * @see com.yuanluesoft.j2oa.loan.service.LoanService#listPersonalTorepayLoans(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public List listPersonalTorepayLoans(SessionInfo sessionInfo) throws ServiceException {
        String hql = "from Loan Loan " +
        			 " where Loan.loanPersonId=" + sessionInfo.getUserId() + 
        			 " and Loan.prepaid='1'" +
        			 " and Loan.repaid!='1'" +
        			 " and Loan.money>Loan.reimburseMoney" +
        			 " order by Loan.loanDate DESC";
        return getDatabaseService().findRecordsByHql(hql);
    }

    /* (non-Javadoc)
     * @see com.yuanluesoft.j2oa.loan.service.LoanService#reimburseLoan(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, long, double)
     */
    public double reimburseLoan(Loan loan, double money, SessionInfo sessionInfo) throws ServiceException {
        money = Math.min(loan.getMoney()-loan.getReimburseMoney(), money); //核销金额必须小于未核销金额
        loan.setReimburseMoney(loan.getReimburseMoney() + money); //设置已核销金额
        if(loan.getMoney()-loan.getReimburseMoney()==0.0) { //已全部还清,系统自动完成办理过程
        	workflowExploitService.getWorkItemAndSend(loan.getWorkflowInstanceId(), null, loan, null, null, sessionInfo);
        	loan.setRepayDate(DateTimeUtils.date());
        	loan.setRepaid('1');
        }
        getDatabaseService().updateRecord(loan); //保存借款单
        return money;
    }

    /* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.loan.service.LoanService#getLoanConfig()
	 */
	public LoanConfig getLoanConfig() throws ServiceException {
		return (LoanConfig)getDatabaseService().findRecordByHql("from LoanConfig LoanConfig");
	}
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//获取借款类别列表
        LoanConfig loanConfig = getLoanConfig();
       	return ListUtils.generateList(loanConfig==null ? "出差,培训,接待,设备采购,办公用品采购" : loanConfig.getTypes(), ",");
	}

	/**
     * @return Returns the sessionService.
     */
    public SessionService getSessionService() {
        return sessionService;
    }
    /**
     * @param sessionService The sessionService to set.
     */
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

	/**
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}
}
