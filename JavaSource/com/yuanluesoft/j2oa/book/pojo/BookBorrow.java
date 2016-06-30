package com.yuanluesoft.j2oa.book.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 借阅信息(book_borrow)
 * @author linchuan
 *
 */
public class BookBorrow extends Record {
	private long bookId; //书籍ID
	private long personId; //借阅人ID
	private String personName; //借阅人
	private Timestamp borrowTime; //借阅时间
	private Timestamp returnTime; //归还时间
	private char isReturned = '0'; //是否已归还
	private String remark; //备注
	
	/**
	 * @return the bookId
	 */
	public long getBookId() {
		return bookId;
	}
	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
	/**
	 * @return the borrowTime
	 */
	public Timestamp getBorrowTime() {
		return borrowTime;
	}
	/**
	 * @param borrowTime the borrowTime to set
	 */
	public void setBorrowTime(Timestamp borrowTime) {
		this.borrowTime = borrowTime;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the returnTime
	 */
	public Timestamp getReturnTime() {
		return returnTime;
	}
	/**
	 * @param returnTime the returnTime to set
	 */
	public void setReturnTime(Timestamp returnTime) {
		this.returnTime = returnTime;
	}
	/**
	 * @return the isReturned
	 */
	public char getIsReturned() {
		return isReturned;
	}
	/**
	 * @param isReturned the isReturned to set
	 */
	public void setIsReturned(char isReturned) {
		this.isReturned = isReturned;
	}
}