package com.yuanluesoft.jeaf.form.callback;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.formula.service.ConditionCheckCallback;
import com.yuanluesoft.jeaf.security.service.RecordControlService;

/**
 * 表单按钮隐藏条件判断回调
 * @author linchuan
 *
 */
public class HideConditionCheckCallback implements ConditionCheckCallback {
	private char accessLevel = '0';
	private boolean deleteEnable;
	private String openMode;
	private String subForm;
	
	public HideConditionCheckCallback(char accessLevel, boolean deleteEnable, String openMode, String subForm) {
		super();
		this.accessLevel = accessLevel;
		this.deleteEnable = deleteEnable;
		this.openMode = openMode;
		this.subForm = subForm;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.formula.service.ConditionCheckCallback#check(java.lang.String, java.lang.String[])
	 */
	public boolean check(String formulaName, String[] parameters) throws ServiceException {
		if(formulaName.equals("notSubform")) { //判断是否某个子表单
			return subForm==null || !subForm.equals(parameters[0]);
		}
		else if(formulaName.equals("isNew")) { //是否新记录
			return openMode==null || openMode.isEmpty() || FormAction.OPEN_MODE_CREATE.equals(openMode) || FormAction.OPEN_MODE_CREATE_COMPONENT.equals(openMode);
		}
		else if(formulaName.equals("isOld")) { //是否旧记录
			return openMode!=null && !openMode.isEmpty() && !FormAction.OPEN_MODE_CREATE.equals(openMode) && !FormAction.OPEN_MODE_CREATE_COMPONENT.equals(openMode);
		}
		else if(formulaName.equals("isRead")) { //是否处于只读状态
			return subForm.equals(FormAction.SUBFORM_READ);
		}
		else if(formulaName.equals("isEdit")) { //是否处于编辑状态
			return !subForm.equals(FormAction.SUBFORM_READ);
		}
		else if(formulaName.equals("deleteDisable")) { //是否允许删除
			return !deleteEnable;
		}
		else if(formulaName.equals("isEditable")) { //是否有编辑权
			return (accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE);
		}
		else if(formulaName.equals("notEditable")) { //是否无编辑权
			return (accessLevel<RecordControlService.ACCESS_LEVEL_EDITABLE);
		}
		return true; //不确定的条件,都当作隐藏条件成立
	}
}