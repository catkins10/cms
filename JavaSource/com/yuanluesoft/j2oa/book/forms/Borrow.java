package com.yuanluesoft.j2oa.book.forms;

import com.yuanluesoft.j2oa.book.pojo.BookBorrow;

/**
 * 
 * @author linchuan
 *
 */
public class Borrow extends Book {
	private BookBorrow borrow = new BookBorrow();

	/**
	 * @return the borrow
	 */
	public BookBorrow getBorrow() {
		return borrow;
	}

	/**
	 * @param borrow the borrow to set
	 */
	public void setBorrow(BookBorrow borrow) {
		this.borrow = borrow;
	}
}