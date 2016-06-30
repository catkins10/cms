/*
 * Created on 2004-12-20
 *
 */
package com.yuanluesoft.jeaf.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.form.model.AttachmentSelector;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.model.FormActionList;
import com.yuanluesoft.jeaf.form.model.TabList;

/**
 * 
 * @author linchuan
 * 
 */
public class ActionForm extends org.apache.struts.action.ActionForm implements Cloneable {
	private long id; //记录ID
	private Form formDefine = new Form(); //表单定义
	private FormActionList formActions = new FormActionList(); //表单操作
	private String act; //操作类型:create/open/edit
	private String displayMode; //显示方式:独立窗口/window,对话框/dialog
	private boolean internalForm; //是否内部表单
	private String reloadPageURL; //重新加载页面的URL
	private String formTitle; //表单窗口标题
	private String actionResult; //操作执行结果
	private String prompt; //系统提示信息
	private List errors; //系统错误信息
	private boolean directOpenComponent; //组成部分是否不经过主记录直接打开
	private boolean locked; //是否已锁定记录
	private String unlockUrl; //解锁URL
	private String queryString; //URL参数
	private String requestCode; //请求编码,防止恶意提交,匿名页面有效
	private String tabSelected; //当前选中的TAB
	
	private AttachmentSelector attachmentSelector = new AttachmentSelector(); //附件选择
	private TabList tabs = new TabList(); //页签列表
	
	private String subForm; //子表单
	private String innerDialog; //当前需要显示的内置对话框jsp文件
	
	private String refeshOpenerScript; //刷新主窗口的脚本
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.validator.ValidatorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		if(queryString==null) {
			queryString = request.getQueryString();
		}
	}
	
	/**
	 * 设置错误信息
	 * @param error
	 */
	public void setError(String error) {
		if(error==null || error.equals("")) {
			return;
		}
		if(errors==null) {
			errors = new ArrayList();
		}
		else if(errors.indexOf(error)!=-1) {
			return;
		}
		errors.add(error);
	}
	
	/**
	 * @return Returns the act.
	 */
	public String getAct() {
		return act;
	}
	/**
	 * @param act The act to set.
	 */
	public void setAct(String act) {
		this.act = act;
	}
	/**
	 * @return Returns the actionResult.
	 */
	public String getActionResult() {
		return actionResult;
	}
	/**
	 * @param actionResult The actionResult to set.
	 */
	public void setActionResult(String actionResult) {
		this.actionResult = actionResult;
	}
	/**
	 * @return Returns the formDefine.
	 */
	public Form getFormDefine() {
		return formDefine;
	}
	/**
	 * @param formDefine The formDefine to set.
	 */
	public void setFormDefine(Form formDefine) {
		this.formDefine = formDefine;
	}
	/**
	 * @return Returns the formTitle.
	 */
	public String getFormTitle() {
		if(formTitle==null && formDefine!=null) {
			formTitle = formDefine.getTitle();
		}
		return formTitle;
	}
	/**
	 * @param formTitle The formTitle to set.
	 */
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return Returns the subform.
	 */
	public String getSubForm() {
		return subForm;
	}
	/**
	 * @param subform The subform to set.
	 */
	public void setSubForm(String subForm) {
		this.subForm = subForm;
	}
	/**
	 * @return Returns the prompt.
	 */
	public String getPrompt() {
		return prompt;
	}
	/**
	 * @param prompt The prompt to set.
	 */
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	/**
	 * @return Returns the locked.
	 */
	public boolean isLocked() {
		return locked;
	}
	/**
	 * @param locked The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	/**
	 * @return the attachmentSelector
	 */
	public AttachmentSelector getAttachmentSelector() {
		return attachmentSelector;
	}
	/**
	 * @param attachmentSelector the attachmentSelector to set
	 */
	public void setAttachmentSelector(AttachmentSelector attachmentSelector) {
		this.attachmentSelector = attachmentSelector;
	}
	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}
	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	/**
	 * @return the tabSelected
	 */
	public String getTabSelected() {
		return tabSelected;
	}
	/**
	 * @param tabSelected the tabSelected to set
	 */
	public void setTabSelected(String tabSelected) {
		this.tabSelected = tabSelected;
	}

	/**
	 * @return the errors
	 */
	public List getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List errors) {
		this.errors = errors;
	}

	/**
	 * @return the tabs
	 */
	public TabList getTabs() {
		return tabs;
	}

	/**
	 * @param tabs the tabs to set
	 */
	public void setTabs(TabList tabs) {
		this.tabs = tabs;
	}

	/**
	 * @return the displayMode
	 */
	public String getDisplayMode() {
		return displayMode;
	}

	/**
	 * @param displayMode the displayMode to set
	 */
	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	/**
	 * @return the innerDialog
	 */
	public String getInnerDialog() {
		return innerDialog;
	}

	/**
	 * @param innerDialog the innerDialog to set
	 */
	public void setInnerDialog(String innerDialog) {
		this.innerDialog = innerDialog;
	}
	
	/**
	 * @return the refeshOpenerScript
	 */
	public String getRefeshOpenerScript() {
		return refeshOpenerScript;
	}

	/**
	 * @param refeshOpenerScript the refeshOpenerScript to set
	 */
	public void setRefeshOpenerScript(String refeshOpenerScript) {
		this.refeshOpenerScript = refeshOpenerScript;
	}

	/**
	 * @return the formActions
	 */
	public FormActionList getFormActions() {
		return formActions;
	}

	/**
	 * @param formActions the formActions to set
	 */
	public void setFormActions(FormActionList formActions) {
		this.formActions = formActions;
	}

	/**
	 * @return the requestCode
	 */
	public String getRequestCode() {
		return requestCode;
	}

	/**
	 * @param requestCode the requestCode to set
	 */
	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	/**
	 * @return the directOpenComponent
	 */
	public boolean isDirectOpenComponent() {
		return directOpenComponent;
	}

	/**
	 * @param directOpenComponent the directOpenComponent to set
	 */
	public void setDirectOpenComponent(boolean directOpenComponent) {
		this.directOpenComponent = directOpenComponent;
	}

	/**
	 * @return the unlockUrl
	 */
	public String getUnlockUrl() {
		return unlockUrl;
	}

	/**
	 * @param unlockUrl the unlockUrl to set
	 */
	public void setUnlockUrl(String unlockUrl) {
		this.unlockUrl = unlockUrl;
	}

	/**
	 * @return the internalForm
	 */
	public boolean isInternalForm() {
		return internalForm;
	}

	/**
	 * @param internalForm the internalForm to set
	 */
	public void setInternalForm(boolean internalForm) {
		this.internalForm = internalForm;
	}

	/**
	 * @return the reloadPageURL
	 */
	public String getReloadPageURL() {
		return reloadPageURL;
	}

	/**
	 * @param reloadPageURL the reloadPageURL to set
	 */
	public void setReloadPageURL(String reloadPageURL) {
		this.reloadPageURL = reloadPageURL;
	}
}