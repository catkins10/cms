package com.yuanluesoft.j2oa.book.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 图书(book_book)
 * @author linchuan
 *
 */
public class Book extends Record {
	private String serialNumber; //书籍编号
	private String title; //书籍名称
	private String category; //书籍类别
	private String author; //作者姓名
	private String publishingHouse; //出版社名称
	private Date publicationDate; //出版日期
	private int pages; //书籍页数
	private String keyword; //关键词
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private char isBorrowing = '0'; //否是被借阅
	private String remark; //备注
	private Set borrows; //借阅记录
	
	/**
	 * 否是被借阅
	 * @return
	 */
	public String getIsBorrowingTitle() {
		return isBorrowing=='1' ? "√" : "";
	}
	
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the isBorrowing
	 */
	public char getIsBorrowing() {
		return isBorrowing;
	}
	/**
	 * @param isBorrowing the isBorrowing to set
	 */
	public void setIsBorrowing(char isBorrowing) {
		this.isBorrowing = isBorrowing;
	}
	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return the pages
	 */
	public int getPages() {
		return pages;
	}
	/**
	 * @param pages the pages to set
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}
	/**
	 * @return the publicationDate
	 */
	public Date getPublicationDate() {
		return publicationDate;
	}
	/**
	 * @param publicationDate the publicationDate to set
	 */
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	/**
	 * @return the publishingHouse
	 */
	public String getPublishingHouse() {
		return publishingHouse;
	}
	/**
	 * @param publishingHouse the publishingHouse to set
	 */
	public void setPublishingHouse(String publishingHouse) {
		this.publishingHouse = publishingHouse;
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
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the borrows
	 */
	public Set getBorrows() {
		return borrows;
	}
	/**
	 * @param borrows the borrows to set
	 */
	public void setBorrows(Set borrows) {
		this.borrows = borrows;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
}