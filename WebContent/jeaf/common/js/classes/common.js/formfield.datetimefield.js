//日期时间选择
DateTimeField = function(inputElementHTML, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, clickShowPicker, terminalType) {
	FormField.call(this, inputElementHTML, 'hidden', style, styleClass);
	this.terminalType = terminalType;
	this.timeFocus = "Hour"; //当前时间字段
	var dateValue = '';
	var hourValue = '';
	var minuteValue = '';
	var secondValue = '';
	var fieldValue = this.getAttribute("value");
	if(fieldValue!='') {
		var values = fieldValue.split(' ');
		dateValue = values[0];
		if(values[1]) {
			values = values[1].split(":");
			hourValue = Number(values[0]);
			minuteValue = Number(values[1]);
			secondValue = Number(values[2]);
		}
	}
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'dropDownButton';
	}
	var alt = this.getAttribute('alt');
	var title = this.getAttribute('title');
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%"' + (title ? ' title="' + title + '"' : '') + '>' +
			   '	<tr>' +
			   '		<td style="padding:0px !important;"><input id="' + this.id + 'Date" dataType="date"' + (!alt || alt=='' ? '' : ' alt="' + alt + '" ' + 'onfocus="if(value==alt)value=\'\';" onblur="if(value==\'\')value=alt;"') + ' maxlength="10"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '" type="text" value="' + dateValue + '"' + (title ? ' title="' + title + '"' : '') + '/></td>' +
			   '		<td nowrap="nowrap" id="datePicker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important;' + selectButtonStyle + '"' : '') + '></td>' +
			   '		<td style="padding:0px !important; width:22px"><input id="' + this.id + 'Hour" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + hourValue + '"/></td>' +
			   '		<td style="padding:0px !important; width:8px" align="center">:</td>' +
			   '		<td style="padding:0px !important; width:22px"><input id="' + this.id + 'Minute" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + minuteValue + '"/></td>' +
			   '		<td style="padding:0px !important; width:8px" align="center">:</td>' +
			   '		<td style="padding:0px !important; width:22px"><input id="' + this.id + 'Second" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + secondValue + '"/></td>' +
			   '		<td nowrap="nowrap" id="timePicker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important; ' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	var dateTimeField = this;
	//事件处理:日期选择
	var datePickerButton = !clickShowPicker ? this.document.getElementById("datePicker_" + this.id) : this.document.getElementById('dateTable' + this.id);
	datePickerButton.onclick = function() {
		dateTimeField.onSelectDate(datePickerButton);
	}
	//事件处理:时间选择
	var timePickerButton = this.document.getElementById('timePicker_' + this.id);
	timePickerButton.onclick = function() {
		dateTimeField.onSelectTime(timePickerButton);
	};
	//事件处理:onchange,onfocus
	var fieldNames = ["Date", "Hour", "Minute", "Second"];
	for(var i=0; i<fieldNames.length; i++) {
		this.document.getElementById(this.id + fieldNames[i]).onchange = function() {
			dateTimeField._update();
		};
		if(i==0) {
			continue;
		}
		this.document.getElementById(this.id + fieldNames[i]).onfocus = function() {
			dateTimeField.timeFocus = this.id.substring(dateTimeField.id.length);
		};
	}
	//根据字体大小,调整时间字段宽度
	var fontSize = CssUtils.getElementComputedStyle(this.document.getElementById(this.id + "Date"), 'font-size');
	fontSize = fontSize ? Number(fontSize.replace('px', '')) : 12;
	for(var i=1; fontSize > 12 && i < fieldNames.length; i++) {
		this.document.getElementById(this.id + fieldNames[i]).style.width = Math.floor(22 * fontSize / 12) + 'px';
	}
};
DateTimeField.prototype = new FormField();
DateTimeField.prototype.onSelectDate = function(datePickerButton) { //事件:选择日期
	new FormField.DatePicker(this.document.getElementById(this.id + "Date"), datePickerButton, false, this.terminalType).show();
};
DateTimeField.prototype.onSelectTime = function(timePickerButton) { //事件:选择时间
	var numberFields = [{field:this.document.getElementById(this.id + 'Hour'), minNumber:0, maxNumber:23, focus:this.timeFocus=='Hour'},
						{field:this.document.getElementById(this.id + 'Minute'), minNumber:0, maxNumber:59, focus:this.timeFocus=='Minute'},
						{field:this.document.getElementById(this.id + 'Second'), minNumber:0, maxNumber:59, focus:this.timeFocus=='Second'}];
	new FormField.NumberPicker(this.getAttribute('title'), numberFields, ':', timePickerButton, this.terminalType).show();
};
DateTimeField.prototype._update = function() {
	var value = this.document.getElementById(this.id + "Date").value;
	if(value!='') {
		value += ' ' + new Number(this.document.getElementById(this.id + "Hour").value);
		value += ':' + new Number(this.document.getElementById(this.id + "Minute").value);
		value += ':' + new Number(this.document.getElementById(this.id + "Second").value);
	}
	this.getInputElement().value = value;
	try {
		this.getInputElement().onchange();
	}
	catch(e) {
	
	}
};
DateTimeField.setValue = function(fieldName, timeField, timeValue) { //设置时间字段的值,timeField:date/hour/minute/second
	var field = FormField.getFormField(fieldName);
	var input = field.document.getElementById(field.id + timeField.substring(0, 1).toUpperCase() + timeField.substring(1));
	input.value = timeValue;
	input.onchange();
};