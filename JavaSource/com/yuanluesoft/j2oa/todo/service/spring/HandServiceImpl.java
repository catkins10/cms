/*
 * Created on 2005-11-16
 *
 */
package com.yuanluesoft.j2oa.todo.service.spring;

import java.util.Iterator;
import java.util.Set;

import com.yuanluesoft.j2oa.todo.pojo.Todo;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 *
 * @author linchuan
 *
 */
public class HandServiceImpl extends BusinessServiceImpl {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		super.save(record);
		Todo todo = (Todo)record;
		if(todo.getIsHand()=='1') {
			saveHandPerson(todo.getHandPersons());
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		super.update(record);
		Todo todo = (Todo)record;
		if(todo.getIsHand()=='1') {
			getDatabaseService().deleteRecordsByHql("from HandPerson HandPerson where HandPerson.mainRecordId=" + todo.getId());
			getDatabaseService().flush();
			saveHandPerson(todo.getHandPersons());
		}
		return record;
	}
	
	/**
	 * 保存交办人
	 * @param memberSet
	 */
	private void saveHandPerson(Set handPersonSet) {
		if(handPersonSet==null) {
			return;
		}
		for(Iterator iterator=handPersonSet.iterator(); iterator.hasNext();) {
			Record record = (Record)iterator.next();
			getDatabaseService().saveRecord(record);
		}
	}
}