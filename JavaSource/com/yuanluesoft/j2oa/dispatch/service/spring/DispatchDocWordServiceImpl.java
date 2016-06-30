/*
 * Created on 2005-9-23
 *
 */
package com.yuanluesoft.j2oa.dispatch.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.dispatch.pojo.Dispatch;
import com.yuanluesoft.j2oa.dispatch.pojo.DispatchDocWordConfig;
import com.yuanluesoft.j2oa.dispatch.service.DispatchDocWordService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.numeration.service.NumerationCallback;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 *
 * @author linchuan
 *
 */
public class DispatchDocWordServiceImpl extends BusinessServiceImpl  implements DispatchDocWordService {
	private NumerationService numerationService; //编号服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.dispatch.service.DispatchService#listDocWords(java.lang.String)
	 */
	public List listDocWords() throws ServiceException {
        return getDatabaseService().findRecordsByHql("select DispatchDocWordConfig.docWord from DispatchDocWordConfig DispatchDocWordConfig order by DispatchDocWordConfig.docWord");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		return listDocWords();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DispatchDocWordService#generateDocWord(com.yuanluesoft.j2oa.dispatch.pojo.Dispatch, boolean)
	 */
	public void generateDocWord(final Dispatch dispatch, boolean isWorkflowTest) throws ServiceException {
		//没有机关代字,不编号
		if(dispatch.getDocMark()==null || dispatch.getDocMark().equals("")) {
			return;
		}
		DispatchDocWordConfig docWordConfig = (DispatchDocWordConfig)getDatabaseService().findRecordByHql("from DispatchDocWordConfig DispatchDocWordConfig where DispatchDocWordConfig.docWord='" + JdbcUtils.resetQuot(dispatch.getDocMark()) + "'");
	
		NumerationCallback numerationCallback = new NumerationCallback() {
			public Object getFieldValue(String fieldName, int fieldLength) {
				if("文件字".equals(fieldName)) {
					return dispatch.getDocMark();
				}
				else if("年度".equals(fieldName)) {
					return new Integer(dispatch.getMarkYear());
				}
				else if("序号".equals(fieldName)) {
					return dispatch.getMarkSequence()==0 ? null : new Integer(dispatch.getMarkSequence());
				}
				return null;
			}
		};
		int sequence = dispatch.getMarkSequence();
		if(sequence==0) { //未编号
			sequence = numerationService.getNextSequence("发文", "文件字", docWordConfig.getFormat(), isWorkflowTest, numerationCallback);
			if(!isWorkflowTest) {
				//更新联合编号的文件字顺序号
				List docWords = getDatabaseService().findRecordsByHql("from DispatchDocWordConfig DispatchDocWordConfig" +
																	  " where DispatchDocWordConfig.groupId=" + docWordConfig.getGroupId() +
																	  " and DispatchDocWordConfig.groupId!=0" +
																	  " and DispatchDocWordConfig.id!=" + docWordConfig.getId());
				synchGroupSequence(docWords, dispatch.getMarkYear(), sequence);
			}
			dispatch.setMarkSequence(sequence);
		}
		//设置文件字
		dispatch.setDocWord(numerationService.generateNumeration("发文", "文件字", docWordConfig.getFormat(), isWorkflowTest, numerationCallback));
		getDatabaseService().updateRecord(dispatch);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#validateDataIntegrity(java.lang.Object)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		List errors = new ArrayList();
		DispatchDocWordConfig docWordConfig = (DispatchDocWordConfig)record;
		DispatchDocWordConfig docWordConfigCheck = (DispatchDocWordConfig)getDatabaseService().findRecordByKey(DispatchDocWordConfig.class.getName(), "docWord", docWordConfig.getDocWord());
		if(docWordConfigCheck!=null && docWordConfigCheck.getId()!=docWordConfig.getId()) {
			errors.add("文件字“" + docWordConfig.getDocWord() + "”已存在，文件字名称不允许重复");
		}
		return errors.isEmpty() ? null : errors;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DocWordService#updateGroup(com.yuanluesoft.j2oa.dispatch.pojo.DocWordConfig, java.lang.String)
	 */
	public void updateGroup(final DispatchDocWordConfig docWordConfig, String unionDocWords) throws ServiceException {
		if(unionDocWords==null || "".equals(unionDocWords)) {
			docWordConfig.setGroupId(0); //设置组ID
			getDatabaseService().updateRecord(docWordConfig);
			return;
		}
		long groupId = UUIDLongGenerator.generateId();
		docWordConfig.setGroupId(groupId); //设置组ID
		//根据文件字名称查找文件字记录
		List unionDocWordConfigs = getDatabaseService().findRecordsByHql("from DispatchDocWordConfig DispatchDocWordConfig where DispatchDocWordConfig.docWord in ('" + JdbcUtils.resetQuot(unionDocWords).replaceAll(",", "','") + "')");
		if(unionDocWordConfigs==null || unionDocWordConfigs.isEmpty()) {
			return;
		}
		for(Iterator iterator = unionDocWordConfigs.iterator(); iterator.hasNext();) {
			DispatchDocWordConfig unionDocWord = (DispatchDocWordConfig)iterator.next();
			//更新组ID
			unionDocWord.setGroupId(groupId);
			if(unionDocWord.getId()!=docWordConfig.getId()) {
				getDatabaseService().updateRecord(unionDocWord);
			}
		}
		//同步更新数序号
		final int year = DateTimeUtils.getYear(DateTimeUtils.date());
		NumerationCallback numerationCallback = new NumerationCallback() {
			public Object getFieldValue(String fieldName, int fieldLength) {
				if("文件字".equals(fieldName)) {
					return docWordConfig.getDocWord();
				}
				else if("年度".equals(fieldName)) {
					return new Integer(year);
				}
				return null;
			}
		};
		int sequence = numerationService.getNextSequence("发文", "文件字", docWordConfig.getFormat(), true, numerationCallback);
		synchGroupSequence(unionDocWordConfigs, year, sequence - 1);
	}
	
	/**
	 * 同步更新联合编号的文件字
	 * @param unionDocWords
	 * @param markYear
	 * @param sequence
	 * @throws ServiceException
	 */
	private void synchGroupSequence(List unionDocWords, final int markYear, int sequence) throws ServiceException {
		if(unionDocWords==null || unionDocWords.isEmpty()) {
			return;
		}
		for(Iterator iterator = unionDocWords.iterator(); iterator.hasNext();) {
			final DispatchDocWordConfig unionDocWord = (DispatchDocWordConfig)iterator.next();
			NumerationCallback numerationSynchCallback = new NumerationCallback() {
				public Object getFieldValue(String fieldName, int fieldLength) {
					if("文件字".equals(fieldName)) {
						return unionDocWord.getDocWord();
					}
					else if("年度".equals(fieldName)) {
						return new Integer(markYear);
					}
					return null;
				}
			};
			numerationService.setSequence("发文", "文件字", unionDocWord.getFormat(), sequence, numerationSynchCallback);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.service.DocWordService#getUnionDocWords(com.yuanluesoft.j2oa.dispatch.pojo.DocWordConfig)
	 */
	public String getUnionDocWords(DispatchDocWordConfig docWordConfig) throws ServiceException {
		List docWords = getDatabaseService().findRecordsByHql("select DispatchDocWordConfig.docWord" +
															  " from DispatchDocWordConfig DispatchDocWordConfig" +
															  " where DispatchDocWordConfig.groupId=" + docWordConfig.getGroupId() +
															  " and DispatchDocWordConfig.groupId!=0" +
															  " and DispatchDocWordConfig.id!=" + docWordConfig.getId());
		return ListUtils.join(docWords, ",", false);
	}
	
	/**
	 * @return the numerationService
	 */
	public NumerationService getNumerationService() {
		return numerationService;
	}

	/**
	 * @param numerationService the numerationService to set
	 */
	public void setNumerationService(NumerationService numerationService) {
		this.numerationService = numerationService;
	}
}