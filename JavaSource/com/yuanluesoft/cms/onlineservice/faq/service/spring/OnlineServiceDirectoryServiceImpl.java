package com.yuanluesoft.cms.onlineservice.faq.service.spring;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceDirectoryServiceImpl extends com.yuanluesoft.cms.onlineservice.service.spring.OnlineServiceDirectoryServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.service.spring.OnlineServiceDirectoryServiceImpl#getNavigatorDataUrl(long, java.lang.String)
	 */
	public String getNavigatorDataUrl(long directoryId, String directoryType) {
		return "/cms/onlineservice/faq/admin/faqView.shtml?directoryId=" + directoryId;
	}
}