package com.yuanluesoft.cms.messageboard.service.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.messageboard.pojo.MessageBoardFaq;
import com.yuanluesoft.cms.messageboard.service.MessageBoardService;
import com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class MessageBoardServiceImpl extends PublicServiceImpl implements MessageBoardService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if((record instanceof MessageBoardFaq) ) {
			getExchangeClient().synchUpdate(record, null, 2000);
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof MessageBoardFaq) {
			getExchangeClient().synchUpdate(record, null, 2000);
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof MessageBoardFaq) {
			getExchangeClient().synchDelete(record, null, 2000);
		}
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.messageboard.service.MessageBoardService#findFaq(java.lang.String)
	 */
	public MessageBoardFaq findFaq(String subject) throws ServiceException {
		String hql = "from MessageBoardFaq MessageBoardFaq" +
					 " where '" + JdbcUtils.resetQuot(subject) + "'" +
					 " like concat(concat('%', MessageBoardFaq.firstKeyword), '%')";
		List faqs = getDatabaseService().findRecordsByHql(hql, 0, 100); 
		for(Iterator iterator = (faqs==null ? null : faqs.iterator()); iterator!=null && iterator.hasNext();) {
			MessageBoardFaq faq = (MessageBoardFaq)iterator.next();
			if(faq.getOtherKeywords()==null) {
				return faq;
			}
			String[] otherKeywords = faq.getOtherKeywords().split(" ");
			int i=0;
			for(; i<otherKeywords.length && (otherKeywords[i].isEmpty() || subject.indexOf(otherKeywords[i])!=-1); i++);
			if(i==otherKeywords.length) {
				return faq;
			}
		}
		return null;
	}
}