package com.yuanluesoft.cms.evaluation.actions.evaluation.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.evaluation.forms.admin.Evaluation;
import com.yuanluesoft.cms.evaluation.model.EvaluationItemCategory;
import com.yuanluesoft.cms.evaluation.pojo.EvaluationItem;
import com.yuanluesoft.cms.evaluation.service.EvaluationService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonSubjection;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationAction extends FormAction {

	/**
	 * 获取测评主题
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic getEvaluationTopic(HttpServletRequest request) throws Exception {
		com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic topic = (com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic)request.getAttribute("TOPIC");
		if(topic!=null) {
			return topic;
		}
		EvaluationService evaluationService = (EvaluationService)getService("evaluationService");
    	topic = (com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic)evaluationService.load(com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic.class, RequestUtils.getParameterLongValue(request, "topicId"));
    	request.setAttribute("TOPIC", topic);
    	return topic;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("manageUnit_transact")) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(com.yuanluesoft.jeaf.form.ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		writeEvaluationForm(form, sessionInfo, request);
	}
	
	/**
	 * 输出测评表单
	 * @param form
	 * @param sessionInfo
	 * @param request
	 * @throws Exception
	 */
	protected void writeEvaluationForm(com.yuanluesoft.jeaf.form.ActionForm form, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Evaluation evaluationForm = (Evaluation)form;
		//获取测评主题
		evaluationForm.setTopic(getEvaluationTopic(request));
		//设置被测评用户姓名和部门名称
		PersonService personService = (PersonService)getService("personService");
		OrgService orgService = (OrgService)getService("orgService");
		Person person = personService.getPerson(evaluationForm.getTargetPersonId());
		evaluationForm.setTargetPersonName(person.getName());
		evaluationForm.setTargetPersonOrg(orgService.getDirectoryFullName(((PersonSubjection)person.getSubjections().iterator().next()).getOrgId(), "/", "unit,school"));
		//整理测评项目分类
		evaluationForm.setItemCategories(new ArrayList());
		EvaluationItemCategory itemCategory = null;
		int index = 1;
		for(Iterator iterator = evaluationForm.getTopic().getItems().iterator(); iterator.hasNext();) {
			EvaluationItem item = (EvaluationItem)iterator.next();
			if(itemCategory==null || !item.getCategory().equals(itemCategory.getCategory())) {
				itemCategory = new EvaluationItemCategory();
				itemCategory.setCategory(item.getCategory()); //分类名称
				itemCategory.setFirstItemIndex(index);
				itemCategory.setItems(new ArrayList());
				evaluationForm.getItemCategories().add(itemCategory);
			}
			itemCategory.getItems().add(item);
			index++;
		}
	}
}