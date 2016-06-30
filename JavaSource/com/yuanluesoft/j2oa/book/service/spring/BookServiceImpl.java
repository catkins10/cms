package com.yuanluesoft.j2oa.book.service.spring;

import com.yuanluesoft.j2oa.book.pojo.Book;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 图书管理服务
 * @author linchuan
 *
 */
public class BookServiceImpl extends BusinessServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		upateBorrowing(record);
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		upateBorrowing(record);
		return super.update(record);
	}
	
	/**
	 * 更新图书的借阅状态
	 * @param pojo
	 * @throws ServiceException
	 */
	private void upateBorrowing(Object pojo) throws ServiceException {
		if(!(pojo instanceof Book)) {
			return;
		}
		Book book = (Book)pojo;
		String hql = "select BookBorrow.id" +
					 " from BookBorrow BookBorrow" +
					 " where BookBorrow.bookId=" + book.getId() +
					 " and not BookBorrow.borrowTime is null" + //借出时间不为空
					 " and BookBorrow.isReturned!='1'"; //未归还
		book.setIsBorrowing(getDatabaseService().findRecordByHql(hql)==null ? '0' : '1');
	}
}