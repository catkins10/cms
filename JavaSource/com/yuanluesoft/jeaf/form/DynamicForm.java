package com.yuanluesoft.jeaf.form;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.form.model.DynamicFormField;

/**
 * 动态表单
 * @author linchuan
 *
 */
public class DynamicForm extends ActionForm {
	private List fields = new ArrayList(); //字段列表

	/**
	 * 添加字段
	 * @param field
	 * @param value
	 */
	public void addField(Field field, Object value) {
		fields.add(new DynamicFormField(field, value));
	}

	/**
	 * @return the fields
	 */
	public List getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List fields) {
		this.fields = fields;
	}
}