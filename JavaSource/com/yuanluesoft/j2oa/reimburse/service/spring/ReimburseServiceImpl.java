/*
 * Created on 2006-6-20
 *
 */
package com.yuanluesoft.j2oa.reimburse.service.spring;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.loan.pojo.Loan;
import com.yuanluesoft.j2oa.loan.service.LoanService;
import com.yuanluesoft.j2oa.reimburse.pojo.Reimburse;
import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseCharge;
import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseConfig;
import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseLoan;
import com.yuanluesoft.j2oa.reimburse.service.ChargeStandardService;
import com.yuanluesoft.j2oa.reimburse.service.ReimburseService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 *
 * @author linchuan
 *
 */
public class ReimburseServiceImpl extends BusinessServiceImpl implements ReimburseService {
    private AttachmentService attachmentService; //附件管理服务
    private ChargeStandardService chargeStandardService; //费用标准服务
    private LoanService loanService; //借款服务
  
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		super.save(record);
		if(record instanceof ReimburseCharge) {
			ReimburseCharge charge = (ReimburseCharge)record;
			updateReimburseMoney(charge.getReimburseId(), null);
		}
		else if(record instanceof Reimburse) {
			Reimburse reimburse = (Reimburse)record;
			updateReimburseMoney(reimburse.getId(), reimburse);
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		super.update(record);
		if(record instanceof ReimburseCharge) {
			ReimburseCharge charge = (ReimburseCharge)record;
			updateReimburseMoney(charge.getReimburseId(), null);
		}
		return record;
	}
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(record instanceof ReimburseCharge) {
			ReimburseCharge charge = (ReimburseCharge)record;
			updateReimburseMoney(charge.getReimburseId(), null);
		}
	}
	
	/*
     *  (non-Javadoc)
     * @see com.yuanluesoft.j2oa.reimburse.service.ReimburseService#saveReimburseLoans(com.yuanluesoft.j2oa.reimburse.pojo.Reimburse, java.lang.String)
     */
    public void saveReimburseLoans(Reimburse reimburse, String reimburseLoanMoney) throws ServiceException {
        if(reimburseLoanMoney==null || reimburse.getPrepaid()=='1') { //没有核销列表或者已经支付
            return;
        }
        //删除原有核销记录
        if(reimburse.getReimburseLoans()!=null) {
            for(Iterator iterator = reimburse.getReimburseLoans().iterator(); iterator.hasNext();) {
            	Record record = (Record)iterator.next();
                getDatabaseService().deleteRecord(record);
            }
        }
        double moneyTotal = reimburse.getMoney(); //总金额
        Set reimburseLoans = new LinkedHashSet();
        if(!reimburseLoanMoney.equals("")) {
            String[] values = reimburseLoanMoney.split(",");
            double money;
            for(int i=0; i<values.length && moneyTotal>0.0; i+=2) {
                try {
                    money = Double.parseDouble(values[i+1]);
                }
                catch(NumberFormatException e) {
                    money = 0.0;
                }
                if(money>0.0) {
	                ReimburseLoan reimburseLoan = new ReimburseLoan();
	                long loanId = Long.parseLong(values[i]);
	                Loan loan = (Loan)getDatabaseService().findRecordById(Loan.class.getName(), loanId);
	                if(money > loan.getMoney() - loan.getReimburseMoney()) { //核销金额大于需要核销的金额
	                    money = loan.getMoney() - loan.getReimburseMoney();
	                }
	                if(money>moneyTotal) { //核销金额大于报销总金额
	                    money = moneyTotal; 
	                }
	                reimburseLoan.setId(UUIDLongGenerator.generateId());
	                reimburseLoan.setLoanId(loanId);
	                reimburseLoan.setLoan(loan);
	                reimburseLoan.setReimburseMoney(money);
	                reimburseLoan.setReimburseId(reimburse.getId());
	                getDatabaseService().saveRecord(reimburseLoan);
	                reimburseLoans.add(reimburseLoan);
	                moneyTotal -= money;
	            }
            }
        }
        //设置需要支付的金额
        reimburse.setPayMoney(moneyTotal);
        getDatabaseService().updateRecord(reimburse);
        reimburse.setReimburseLoans(reimburseLoans.isEmpty() ? null : reimburseLoans);
    }
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.j2oa.reimburse.service.ReimburseService#pay(com.yuanluesoft.j2oa.reimburse.pojo.Reimburse)
     */
    public void pay(Reimburse reimburse, SessionInfo sessionInfo) throws ServiceException {
        //设置为已付款状态
        reimburse.setPrepaid('1');
        reimburse.setPayDate(DateTimeUtils.date());
        getDatabaseService().updateRecord(reimburse);
        //借款核销
        if(reimburse.getReimburseLoans()!=null) {
            for(Iterator iterator = reimburse.getReimburseLoans().iterator(); iterator.hasNext();) {
                ReimburseLoan reimburseLoan = (ReimburseLoan)iterator.next();
                if(reimburseLoan.getReimbursed()!='1') { //未核销,则进行核销
                    double repayMoney = loanService.reimburseLoan(reimburseLoan.getLoan(), reimburseLoan.getReimburseMoney(), sessionInfo);
                    //重设核销金额
                    reimburseLoan.setReimburseMoney(repayMoney);
                    reimburseLoan.setReimbursed('1');
                    reimburseLoan.setReimburseTime(DateTimeUtils.now());
                    getDatabaseService().updateRecord(reimburseLoan);
                }
            }
        }
    }
    
    /**
     * 更新报销总金额
     * @param reimburseId
     * @throws ServiceException
     */
    private void updateReimburseMoney(long reimburseId, Reimburse reimburse) throws ServiceException {
    	if(reimburse==null) {
	    	reimburse = (Reimburse)getDatabaseService().findRecordById(Reimburse.class.getName(), reimburseId);
	    	if(reimburse==null) {
	    		return;
	    	}
    	}
    	Number money = (Number)getDatabaseService().findRecordByHql("select sum(ReimburseCharge.money) from ReimburseCharge ReimburseCharge where ReimburseCharge.reimburseId=" + reimburseId);
    	reimburse.setMoney(money==null ? 0 : money.doubleValue());
    	getDatabaseService().updateRecord(reimburse);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.reimburse.service.ReimburseService#getReimburseConfig()
	 */
	public ReimburseConfig getReimburseConfig() throws ServiceException {
		return (ReimburseConfig)getDatabaseService().findRecordByHql("from ReimburseConfig ReimburseConfig");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		ReimburseConfig reimburseConfig = getReimburseConfig();
		return ListUtils.generateList(reimburseConfig==null ? "差旅费,培训费,手机话费报销" : reimburseConfig.getTypes(), ",");
	}

	/**
     * @return Returns the attachmentService.
     */
    public AttachmentService getAttachmentService() {
        return attachmentService;
    }
    /**
     * @param attachmentService The attachmentService to set.
     */
    public void setAttachmentService(
            AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
    /**
     * @return Returns the chargeStandardService.
     */
    public ChargeStandardService getChargeStandardService() {
        return chargeStandardService;
    }
    /**
     * @param chargeStandardService The chargeStandardService to set.
     */
    public void setChargeStandardService(
            ChargeStandardService chargeStandardService) {
        this.chargeStandardService = chargeStandardService;
    }
    /**
     * @return Returns the loanService.
     */
    public LoanService getLoanService() {
        return loanService;
    }
    /**
     * @param loanService The loanService to set.
     */
    public void setLoanService(LoanService loanService) {
        this.loanService = loanService;
    }
}
