/*
 * Created on 2005-9-16
 *
 */
package com.yuanluesoft.j2oa.document.actions.keyword;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.document.pojo.Keyword;
import com.yuanluesoft.j2oa.document.pojo.KeywordCategory;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class KeywordAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) { //管理员
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		KeywordCategory pojoKeyword = (KeywordCategory)record;
		if(pojoKeyword.getKeywords()!=null) {
			String keywordList = null;
			for(Iterator iterator = pojoKeyword.getKeywords().iterator(); iterator.hasNext();) {
				Keyword keyword = (Keyword)iterator.next();
				keywordList = (keywordList==null ? "" : keywordList + " ") + keyword.getKeyword();
			}
			((com.yuanluesoft.j2oa.document.forms.Keyword)form).setKeywordList(keywordList);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#saveRecord(com.yuanluesoft.jeaf.core.forms.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		KeywordCategory category = (KeywordCategory)record;
		String strKeywords = ((com.yuanluesoft.j2oa.document.forms.Keyword)form).getKeywordList();
		String[] keywordList = strKeywords.replaceAll("\n", " ").split(" ");
		Set keywords = new HashSet();
		for(int i=0; i<keywordList.length; i++) {
			keywordList[i] = keywordList[i].trim();
			if(!keywordList[i].equals("")) {
				Keyword keyword = new Keyword();
				keyword.setId(UUIDLongGenerator.generateId()); //ID
				keyword.setCategoryId(category.getId()); //分类ID
				keyword.setKeyword(keywordList[i]); //主题词
				keyword.setKeywordIndex(i); //序号
				keywords.add(keyword);
			}
		}
		category.setKeywords(keywords);
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.actions.form.BaseAction#inheritProperties(com.yuanluesoft.erp.core.forms.ActionForm, com.yuanluesoft.erp.core.forms.ActionForm)
	 */
	public void inheritProperties(ActionForm newForm, ActionForm currentForm) {
		super.inheritProperties(newForm, currentForm);
		//继承区域类别
		((com.yuanluesoft.j2oa.document.forms.Keyword)newForm).setDistrict(((com.yuanluesoft.j2oa.document.forms.Keyword)currentForm).getDistrict());
	}
}