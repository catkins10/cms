//日期选择
DateField = function(inputElementHTML, styleClass, style, selectButtonStyleClass, selectButtonStyle, alignLeft, parentElement, terminalType) {
	FormField.call(this, inputElementHTML, 'text', style, styleClass);
	this.terminalType = terminalType;
	this.alignLeft = alignLeft;
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
	//绑定事件
	var datePickerButton = this.getInputElement().readOnly ?  this.getFieldElement() : this.document.getElementById("picker_" + this.id);
	var dateField = this;
	datePickerButton.onclick = function() {
		dateField.onSelectDate();
	}
};
DateField.prototype = new FormField();
DateField.prototype.onSelectDate = function() { // //事件:选择日期
	new FormField.DatePicker(this.getInputElement(), this.getFieldElement(), this.alignLeft, this.terminalType).show();
};