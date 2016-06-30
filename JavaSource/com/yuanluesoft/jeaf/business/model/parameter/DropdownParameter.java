package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数:下拉列表
 * @author linchuan
 *
 */
public class DropdownParameter extends RadioParameter {
	private String valueField; //对应的值字段,默认为字段自己(Field.name)
	private String titleField; //对应的标题字段,默认为字段自己(Field.name)
	private boolean selectOnly; //是否只能选择
	private String listPickerWidth; //列表宽度
	
	/**
	 * @return the listPickerWidth
	 */
	public String getListPickerWidth() {
		return listPickerWidth;
	}
	/**
	 * @param listPickerWidth the listPickerWidth to set
	 */
	public void setListPickerWidth(String listPickerWidth) {
		this.listPickerWidth = listPickerWidth;
	}
	/**
	 * @return the selectOnly
	 */
	public boolean isSelectOnly() {
		return selectOnly;
	}
	/**
	 * @param selectOnly the selectOnly to set
	 */
	public void setSelectOnly(boolean selectOnly) {
		this.selectOnly = selectOnly;
	}
	/**
	 * @return the titleField
	 */
	public String getTitleField() {
		return titleField;
	}
	/**
	 * @param titleField the titleField to set
	 */
	public void setTitleField(String titleField) {
		this.titleField = titleField;
	}
	/**
	 * @return the valueField
	 */
	public String getValueField() {
		return valueField;
	}
	/**
	 * @param valueField the valueField to set
	 */
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
}