//选择字段
SelectField = function(inputElementHTML, onSelect, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement) {
	FormField.call(this, inputElementHTML, 'text', style, styleClass);
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'selectButton';
	}
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">' +
			   '	<tr>' +
			   '		<td style="padding:0px !important;">' + this.resetInputElementHTML() + '</td>' +
			   '		<td nowrap="nowrap" id="picker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important; ' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	//事件处理
	var selectButton = inputElementHTML.toLowerCase().indexOf(" readonly")==-1 ? this.document.getElementById('picker_' + this.id ) : this.getFieldElement();
	selectButton.onclick = function() {
		eval(onSelect);
	};
};
SelectField.prototype = new FormField();