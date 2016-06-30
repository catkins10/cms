package com.yuanluesoft.cms.onlineservice.faq.service.spring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq;
import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaqItem;
import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaqRelation;
import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaqSubjection;
import com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.CnToSpell;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceFaqServiceImpl extends BusinessServiceImpl implements OnlineServiceFaqService {
	private PageService pageService; //页面服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof OnlineServiceFaq) {
			OnlineServiceFaq faq = (OnlineServiceFaq)record;
			setQuestionSpell(faq);
			if(faq.getStatus()==FAQ_STATUS_ISSUE) {
				pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重建静态页面
			}
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof OnlineServiceFaq) {
			OnlineServiceFaq faq = (OnlineServiceFaq)record;
			setQuestionSpell(faq);
			if(faq.getStatus()==FAQ_STATUS_ISSUE) {
				pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重建静态页面
			}
			else if(faq.getStatus()==FAQ_STATUS_DELETED + (FAQ_STATUS_ISSUE - '0')) { //逻辑删除
				pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_LOGICAL_DELETE); //重建静态页面
			}
		}
		return super.update(record);
	}
	
	/**
	 * 设置问题的拼音
	 * @param faq
	 */
	private void setQuestionSpell(OnlineServiceFaq faq) {
		String spell = CnToSpell.getFullSpell(faq.getQuestion(), false);
		faq.setQuestionSpell(StringUtils.slice(spell, 50, ""));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(record instanceof OnlineServiceFaq && (((OnlineServiceFaq)record).getStatus()==FAQ_STATUS_ISSUE)) {
			//重建静态页面
			pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService#issue(com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void issue(OnlineServiceFaq faq, SessionInfo sessionInfo) throws ServiceException {
		faq.setIssuePersonId(sessionInfo==null ? 100 : sessionInfo.getUserId());
		faq.setStatus(FAQ_STATUS_ISSUE);
		if(faq.getIssueTime()==null) {
			faq.setIssueTime(DateTimeUtils.now());
		}
		getDatabaseService().updateRecord(faq);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(faq, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService#unissue(com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq)
	 */
	public void unissue(OnlineServiceFaq faq) throws ServiceException {
		faq.setStatus(FAQ_STATUS_UNISSUE);
		getDatabaseService().updateRecord(faq);
		//重新生成静态页面
		pageService.rebuildStaticPageForModifiedObject(faq, StaticPageBuilder.OBJECT_MODIFY_ACTION_LOGICAL_DELETE);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService#updateFaqSubjectios(com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq, boolean, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void updateFaqSubjectios(OnlineServiceFaq faq, boolean isNew, String subjectionDirectoryIds, String subjectionItemIds, String subjectionItemNames) throws ServiceException {
		updateFaqDirectorySubjectios(faq, isNew, subjectionDirectoryIds);
		updateFaqSubjectioItems(faq, isNew, subjectionItemIds, subjectionItemNames);
	}
	
	/**
	 * 更新隶属目录
	 * @param faq
	 * @param isNew
	 * @param subjectionDirectoryIds
	 * @throws ServiceException
	 */
	private void updateFaqDirectorySubjectios(OnlineServiceFaq faq, boolean isNew, String subjectionDirectoryIds) throws ServiceException {
		if(subjectionDirectoryIds==null || subjectionDirectoryIds.equals("")) {
			return;
		}
		if(!isNew) {
			//检查隶属栏目是否发生变化
			if(subjectionDirectoryIds.equals(ListUtils.join(faq.getSubjections(), "directoryId", ",", false))) {
				return;
			}
			//删除旧的隶属关系
			for(Iterator iterator = faq.getSubjections()==null ? null : faq.getSubjections().iterator(); iterator!=null && iterator.hasNext();) {
				OnlineServiceFaqSubjection subjection = (OnlineServiceFaqSubjection)iterator.next();
				getDatabaseService().deleteRecord(subjection);
			}
		}
		//保存新的隶属关系
		faq.setSubjections(new HashSet());
		String[] ids = subjectionDirectoryIds.split(",");
		for(int i=0; i<ids.length; i++) {
			OnlineServiceFaqSubjection subjection = new OnlineServiceFaqSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setFaqId(faq.getId());
			subjection.setDirectoryId(Long.parseLong(ids[i]));
			getDatabaseService().saveRecord(subjection);
			faq.getSubjections().add(subjection);
		}
	}
	
	/**
	 * 更新隶属事项
	 * @param faq
	 * @param isNew
	 * @param subjectionItemIds
	 * @param subjectionItemNames
	 * @throws ServiceException
	 */
	private void updateFaqSubjectioItems(OnlineServiceFaq faq, boolean isNew, String subjectionItemIds, String subjectionItemNames) throws ServiceException {
		if(subjectionItemIds==null) {
			return;
		}
		if(!isNew) {
			//检查隶属事项是否发生变化
			if(subjectionItemIds.equals(ListUtils.join(faq.getSubjectionItems(), "itemId", ",", false))) {
				return;
			}
			//删除旧的隶属关系
			for(Iterator iterator = faq.getSubjectionItems()==null ? null : faq.getSubjectionItems().iterator(); iterator!=null && iterator.hasNext();) {
				OnlineServiceFaqItem subjectionItem = (OnlineServiceFaqItem)iterator.next();
				getDatabaseService().deleteRecord(subjectionItem);
			}
		}
		if(subjectionItemIds.isEmpty()) {
			return;
		}
		//保存新的隶属关系
		faq.setSubjectionItems(new HashSet());
		String[] ids = subjectionItemIds.split(",");
		String[] names = subjectionItemNames.split(",");
		for(int i=0; i<ids.length; i++) {
			OnlineServiceFaqItem subjectionItem = new OnlineServiceFaqItem();
			subjectionItem.setId(UUIDLongGenerator.generateId());
			subjectionItem.setFaqId(faq.getId());
			subjectionItem.setItemId(Long.parseLong(ids[i]));
			subjectionItem.setItemName(names[i]);
			getDatabaseService().saveRecord(subjectionItem);
			faq.getSubjectionItems().add(subjectionItem);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService#listRelationFaqs(long, boolean)
	 */
	public List listRelationFaqs(long faqId, boolean publicOnly) throws ServiceException {
		String hql = "from OnlineServiceFaq OnlineServiceFaq" +
					 " where (OnlineServiceFaq.id in (" +
					 " select OnlineServiceFaqRelation.relationFaqId from OnlineServiceFaqRelation OnlineServiceFaqRelation where OnlineServiceFaqRelation.faqId=" + faqId +
					 ")" +
					 "or OnlineServiceFaq.id in (" +
					 " select OnlineServiceFaqRelation.faqId from OnlineServiceFaqRelation OnlineServiceFaqRelation where OnlineServiceFaqRelation.relationFaqId=" + faqId +
					 "))" +
					 (publicOnly ? " and OnlineServiceFaq.status='" + FAQ_STATUS_ISSUE + "'" : "") +
					 "order by OnlineServiceFaq.question";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService#addRelationFaqs(com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq, java.lang.String)
	 */
	public void addRelationFaqs(OnlineServiceFaq faq, String newRelationFaqIds) throws ServiceException {
		if(newRelationFaqIds==null || newRelationFaqIds.isEmpty()) {
			return;
		}
		String[] faqIds = newRelationFaqIds.split(",");
		for(int i=0; i<faqIds.length; i++) {
			Long id = new Long(faqIds[i]);
			if(id.longValue()==faq.getId()) {
				continue;
			}
			if(ListUtils.findObjectByProperty(faq.getRelations(), "relationFaqId", id)!=null) {
				continue;
			}
			if(ListUtils.findObjectByProperty(faq.getBeRelations(), "faqId", id)!=null) {
				continue;
			}
			OnlineServiceFaqRelation relation = new OnlineServiceFaqRelation();
			relation.setId(UUIDLongGenerator.generateId());
			relation.setFaqId(faq.getId());
			relation.setRelationFaqId(id.longValue());
			getDatabaseService().saveRecord(relation);
			//重建静态页面
			OnlineServiceFaq relationFaq = (OnlineServiceFaq)load(OnlineServiceFaq.class, relation.getRelationFaqId());
			if(relationFaq.getStatus()==FAQ_STATUS_ISSUE) {
				pageService.rebuildStaticPageForModifiedObject(relationFaq, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重建静态页面
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService#removeRelationFaqs(com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq, java.lang.String[])
	 */
	public void removeRelationFaqs(OnlineServiceFaq faq, String[] selectedRelationFaqIds) throws ServiceException {
		if(selectedRelationFaqIds==null || selectedRelationFaqIds.length==0) {
			return;
		}
		for(int i=0; i<selectedRelationFaqIds.length; i++) {
			Long id = new Long(selectedRelationFaqIds[i]);
			OnlineServiceFaqRelation relation = (OnlineServiceFaqRelation)ListUtils.findObjectByProperty(faq.getRelations(), "relationFaqId", id);
			if(relation==null && (relation=(OnlineServiceFaqRelation)ListUtils.findObjectByProperty(faq.getBeRelations(), "faqId", id))==null) {
				continue;
			}
			getDatabaseService().deleteRecord(relation);
			//重建静态页面
			OnlineServiceFaq relationFaq = (OnlineServiceFaq)load(OnlineServiceFaq.class, id.longValue());
			if(relationFaq.getStatus()==FAQ_STATUS_ISSUE) {
				pageService.rebuildStaticPageForModifiedObject(relationFaq, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE); //重建静态页面
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService#listFaqs(long, boolean)
	 */
	public List listFaqs(long directoryId, boolean publicOnly) throws ServiceException {
		String hql = "select OnlineServiceFaq" +
					 " from OnlineServiceFaq OnlineServiceFaq, OnlineServiceFaqSubjection OnlineServiceFaqSubjection" +
					 " where OnlineServiceFaqSubjection.faqId=OnlineServiceFaq.id" +
					 " and OnlineServiceFaqSubjection.directoryId=" + directoryId +
					 (publicOnly ? " and OnlineServiceFaq.status='" + FAQ_STATUS_ISSUE + "'" : "") +
					 " order by OnlineServiceFaq.question";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
}