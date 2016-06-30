//时间选择
TimeField = function(inputElementHTML, secondEnabled, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, terminalType) {
	style = (!style || style=='null' ? '' : style + ";") + 'width:' + (secondEnabled ? 106 : 68) + 'px !important';
	FormField.call(this, inputElementHTML, 'hidden', style, styleClass);
	this.terminalType = terminalType;
	this.timeFocus = "Hour"; //当前时间字段
	this.secondEnabled = secondEnabled;
	var hourValue = '';
	var minuteValue = '';
	var secondValue = '';
	var fieldValue = this.getAttribute("value");
	if(fieldValue!='') {
		var values = fieldValue.split(":");
		hourValue = Number(values[0]);
		minuteValue = Number(values[1]);
		if(secondEnabled) {
			secondValue = Number(values[2]);
		}
	}
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'dropDownButton';
	}
	var title = this.getAttribute('title');
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%"' + (title ? ' title="' + title + '"' : '') + '>' +
			   '	<tr>' +
			   '		<td style="padding:0px !important; width:' + (secondEnabled ? 33 : 50) + '%"><input id="' + this.id + 'Hour" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + hourValue + '"/></td>' +
			   '		<td style="padding:0px !important; width:8px" align="center">:</td>' +
			   '		<td style="padding:0px !important; width:' + (secondEnabled ? 33 : 50) + '%"><input id="' + this.id + 'Minute" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + minuteValue + '"/></td>' +
			   (secondEnabled ? '		<td style="padding:0px !important; width:8px" align="center">:</td>' : '') +
			   (secondEnabled ? '		<td style="padding:0px !important; width:33%"><input id="' + this.id + 'Second" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + secondValue + '"/></td>' : '') +
			   '		<td nowrap="nowrap" id="timePicker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important;' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	var timeField = this;
	//事件处理:选择时间
	var timePickerButton = this.document.getElementById('timePicker_' + this.id);
	timePickerButton.onclick = function() {
		timeField.onSelectTime(timePickerButton);
	};
	//事件处理:onchange,onfocus
	var fieldNames = ["Hour", "Minute"];
	if(secondEnabled) {
		fieldNames.append("Second");
	}
	for(var i=0; i<fieldNames.length; i++) {
		this.document.getElementById(this.id + fieldNames[i]).onchange = function() {
			timeField._update();
		};
		this.document.getElementById(this.id + fieldNames[i]).onfocus = function() {
			timeField.timeFocus = this.id.substring(timeField.id.length);
		};
	}
	//根据字体大小,调整时间字段宽度
	var fontSize = CssUtils.getElementComputedStyle(this.document.getElementById(this.id + "Hour"), 'font-size');
	fontSize = fontSize ? Number(fontSize.replace('px', '')) : 12;
	if(fontSize > 12) {
		this.getFieldElement().style.width = Math.floor((secondEnabled ? 106 : 68) * fontSize / 12) + "px";
	}
};
TimeField.prototype = new FormField();
TimeField.prototype.onSelectTime = function(timePickerButton) { //事件:选择时间
	var numberFields = [{field:this.document.getElementById(this.id + 'Hour'), minNumber:0, maxNumber:23, focus:this.timeFocus=='Hour'},
						{field:this.document.getElementById(this.id + 'Minute'), minNumber:0, maxNumber:59, focus:this.timeFocus=='Minute'}];
	if(this.secondEnabled) {
		numberFields.push({field:this.document.getElementById(this.id + 'Second'), minNumber:0, maxNumber:59, focus:this.timeFocus=='Second'});
	}
	new FormField.NumberPicker(this.getAttribute('title'), numberFields, ':', timePickerButton, this.terminalType).show();
};
TimeField.prototype._update = function() {
	var hour = new Number(this.document.getElementById(this.id + "Hour").value);
	var value = (hour<10 ? "0" : "") + hour;
	var minute = new Number(this.document.getElementById(this.id + "Minute").value);
	value += ":" + (minute<10 ? "0" : "") + minute;
	if(this.secondEnabled) {
		var second = new Number(this.document.getElementById(this.id + "Second").value);
		value += ":" + (second<10 ? "0" : "") + second;
	}
	this.getInputElement().value = value;
	try {
		this.getInputElement().onchange();
	}
	catch(e) {
	
	}
};
TimeField.setValue = function(fieldName, timeField, timeValue) { //设置时间字段的值,timeField:hour/minute/second
	var field = FormField.getFormField(fieldName);
	var input = field.document.getElementById(field.id + timeField.substring(0, 1).toUpperCase() + timeField.substring(1));
	input.value = timeValue;
	input.onchange();
};