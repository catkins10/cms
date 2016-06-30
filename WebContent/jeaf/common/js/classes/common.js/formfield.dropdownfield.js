//下拉列表字段
DropdownField = function(inputElementHTML, listValues, valueField, titleField, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, listPickerWidth, terminalType) {
	FormField.call(this, inputElementHTML, 'text', style, styleClass);
	this.listValues = listValues;
	this.valueField = valueField;
	this.titleField = titleField;
	this.listPickerWidth = listPickerWidth;
	this.terminalType = terminalType;
	this.parentElement = parentElement;
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'dropDownButton';
	}
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">' +
			   '	<tr>' +
			   '		<td style="padding:0px !important;">' + this.resetInputElementHTML() + '</td>' +
			   '		<td nowrap="nowrap" id="picker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important; ' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	var dropdownField = this;
	var fieldElement = this.getFieldElement();
	var form = DomUtils.getParentNode(fieldElement, "form");
	this.form = form ? form : this.document.body;
	//事件处理
	var dropdownPickerButton = inputElementHTML.toLowerCase().indexOf(" readonly")==-1 ? this.document.getElementById('picker_' + this.id) : fieldElement;
	dropdownPickerButton.onclick = function() {
		dropdownField.onDropdown();
	};
	EventUtils.addEvent(window, "load", function() {
		try { dropdownField.getField(true).onchange(); } catch(e) {}
		try { dropdownField.getField(false).onchange(); } catch(e) {}
	});
};
DropdownField.prototype = new FormField();
DropdownField.prototype.onDropdown = function() { //下拉
	new FormField.ListPicker(this._getListValues(), this.getField(false), this.getField(true), this.getFieldElement(), this.listPickerWidth, false, null, this.terminalType).show();
};
DropdownField.prototype._getListValues = function() { //获取列表值
	if(typeof this.listValues == 'function') {
		return this.listValues.call(this);
	}
	else {
		return this.listValues;
	}
};
DropdownField.prototype.getField = function(isTitleInput) { //获取值/标题输入框
	var fieldName = isTitleInput ? this.titleField : this.valueField;
	if(!fieldName || fieldName=="") {
		return null;
	}
	var	input = this.inputElementHTML.indexOf('name="' + fieldName + '"')==-1 ? DomUtils.getElement(this.form, "input", fieldName) : this.getInputElement();
	if(input) {
		return input;
	}
	input = this.document.getElementsByName(fieldName)[0];
	if(input) {
		return input;
	}
	return this.getInputElement();
};
DropdownField.prototype.setValue = function(value) { //设置值,当值在选项列表中是返回true
	this.getField(false).value = value;
	//设置标题
	var items = this._getListValues().split('\0');
	for(var i=0; i<items.length; i++) {
		var index = items[i].indexOf("|");
		if(items[i].substring(index+1).trim()==value) {
			this.getField(true).value = (index==-1 ? items[i] : items[i].substring(0, index)).trim();
			return true;
		}
	}
	this.getField(true).value = value;
	return false;
};
DropdownField.setListValues = function(fieldName, listValues) { //修改下拉列表选项
	var dropdownField = DropdownField.getDropdownField(fieldName);
	if(dropdownField) {
		dropdownField.listValues = listValues;
	}
};
DropdownField.getListValues = function(fieldName) { //获取下拉列表选项
	var dropdownField = DropdownField.getDropdownField(fieldName);
	if(dropdownField) {
		return dropdownField._getListValues();
	}
};
DropdownField.getSelectedListValue = function(fieldName) { //获取下拉列表选项
	var list = DropdownField.getListValues(fieldName);
	return list.split('\0')[DropdownField.getSelectedIndex(fieldName)];
};
DropdownField.setValue = function(fieldName, value) { //设置值,当值在选项列表中是返回true
	if(!value) {
		return false;
	}
	var dropdownField = DropdownField.getDropdownField(fieldName);
	return dropdownField ? dropdownField.setValue(value) : false;
};
DropdownField.setValueByIndex = function(fieldName, valueIndex) { //设置值,当值在选项列表中是返回true
	var dropdownField = DropdownField.getDropdownField(fieldName);
	if(!dropdownField) {
		return;
	}
	var items = dropdownField._getListValues().split('\0');
	if(valueIndex>items.length-1) {
		return false;
	}
	var index = items[valueIndex].indexOf("|");
	dropdownField.getField(false).value = items[valueIndex].substring(index + 1).trim();
	dropdownField.getField(true).value = (index==-1 ? items[valueIndex] : items[valueIndex].substring(0, index)).trim();
	return true;
};
DropdownField.getSelectedIndex = function(fieldName) { //获取选中条目的序号
	var dropdownField = DropdownField.getDropdownField(fieldName);
	if(!dropdownField || !dropdownField.listValues || dropdownField.listValues=="") {
		return -1;
	}
	var title = dropdownField.getField(true).value.trim();
	var value = dropdownField.getField(false).value.trim();
	var items = dropdownField._getListValues().split('\0');
	for(var i=0; i<items.length; i++) {
		var index = items[i].indexOf("|");
		if(value==items[i].substring(index+1).trim()) {
			return i;
		}
		if(title==(index==-1 ? items[i] : items[i].substring(0, index)).trim()) {
			return i;
		}
	}
	return -1;	
};
DropdownField.getDropdownField = function(fieldName) { //获取下拉字段对象
	var dropdownField = FormField.getFormField(fieldName);
	return dropdownField ? dropdownField : FormField.getFormField(fieldName + "_title");
};