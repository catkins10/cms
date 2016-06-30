/*
 * Created on 2006-6-15
 *
 */
package com.yuanluesoft.j2oa.reimburse.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.j2oa.loan.pojo.Loan;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 借款核销(reimburse_loan)
 * @author linchuan
 *
 */
public class ReimburseLoan extends Record {
	private long reimburseId; //报销单ID
	private long loanId; //借款单ID
	private double reimburseMoney; //核销金额
	private char reimbursed = '0'; //是否已核销
	private Timestamp reimburseTime; //核销时间
	private Loan loan;
	private Reimburse reimburse;
    
    /**
     * @return Returns the reimburseId.
     */
    public long getReimburseId() {
        return reimburseId;
    }
    /**
     * @param reimburseId The reimburseId to set.
     */
    public void setReimburseId(long reimburseId) {
        this.reimburseId = reimburseId;
    }
    /**
     * @return Returns the loanId.
     */
    public long getLoanId() {
        return loanId;
    }
    /**
     * @param loanId The loanId to set.
     */
    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }
    /**
     * @return Returns the loan.
     */
    public Loan getLoan() {
        return loan;
    }
    /**
     * @param loan The loan to set.
     */
    public void setLoan(Loan loan) {
        this.loan = loan;
    }
    /**
     * @return Returns the reimburseMoney.
     */
    public double getReimburseMoney() {
        return reimburseMoney;
    }
    /**
     * @param reimburseMoney The reimburseMoney to set.
     */
    public void setReimburseMoney(double reimburseMoney) {
        this.reimburseMoney = reimburseMoney;
    }
    /**
     * @return Returns the reimbursed.
     */
    public char getReimbursed() {
        return reimbursed;
    }
    /**
     * @param reimbursed The reimbursed to set.
     */
    public void setReimbursed(char reimbursed) {
        this.reimbursed = reimbursed;
    }
    /**
     * @return Returns the reimburseTime.
     */
    public java.sql.Timestamp getReimburseTime() {
        return reimburseTime;
    }
    /**
     * @param reimburseTime The reimburseTime to set.
     */
    public void setReimburseTime(java.sql.Timestamp reimburseTime) {
        this.reimburseTime = reimburseTime;
    }
    /**
     * @return Returns the reimburse.
     */
    public com.yuanluesoft.j2oa.reimburse.pojo.Reimburse getReimburse() {
        return reimburse;
    }
    /**
     * @param reimburse The reimburse to set.
     */
    public void setReimburse(
            com.yuanluesoft.j2oa.reimburse.pojo.Reimburse reimburse) {
        this.reimburse = reimburse;
    }
}
