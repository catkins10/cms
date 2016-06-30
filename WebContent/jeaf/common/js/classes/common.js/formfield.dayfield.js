//月日选择
DayField = function(inputElementHTML, styleClass, style, selectButtonStyleClass, selectButtonStyle, parentElement, terminalType) {
	style = (!style || style=='null' ? '' : style + ";") + 'width:68px !important';
	FormField.call(this, inputElementHTML, 'hidden', style, styleClass);
	this.terminalType = terminalType;
	this.dayFocus = "Month"; //当前字段
	var monthValue = '';
	var dayValue = '';
	var fieldValue = this.getAttribute("value");
	if(fieldValue!='') {
		var values = fieldValue.split("\-");
		monthValue = Number(values[0]);
		dayValue = Number(values[1]);
	}
	if(!selectButtonStyleClass || selectButtonStyleClass=='' || selectButtonStyleClass=='null') {
		selectButtonStyleClass = 'dropDownButton';
	}
	var title = this.getAttribute('title');
	var html = '<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%"' + (title ? ' title="' + title + '"' : '') + '>' +
			   '	<tr>' +
			   '		<td style="padding:0px !important; width:50%"><input id="' + this.id + 'Month" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + monthValue + '"/></td>' +
			   '		<td style="padding:0px !important; width:8px" align="center">-</td>' +
			   '		<td style="padding:0px !important; width:50%"><input id="' + this.id + 'Day" dataType="number" maxlength="2"' + (this.styleClass=='' ? '' : ' class="' + this.styleClass + '"') + ' style="' + this.getInputElementCssText() + '; text-align:center;" type="text" value="' + dayValue + '"/></td>' +
			   '		<td nowrap="nowrap" id="dayPicker_' + this.id + '" class="' + selectButtonStyleClass + '"' + (selectButtonStyle ? ' style="padding:0px !important; ' + selectButtonStyle + '"' : '') + '></td>' +
			   '	</tr>' +
			   '</table>';
	this.writeFieldElement(html, parentElement);
	var dayField = this;
	//事件处理:选择时间
	var dayPickerButton = this.document.getElementById('dayPicker_' + this.id);
	dayPickerButton.onclick = function() {
		dayField.onSelectDay(dayPickerButton);
	};
	//事件处理:onchange,onfocus
	var fieldNames = ["Month", "Day"];
	for(var i=0; i<fieldNames.length; i++) {
		this.document.getElementById(this.id + fieldNames[i]).onchange = function() {
			dayField._update();
		};
		this.document.getElementById(this.id + fieldNames[i]).onfocus = function() {
			dayField.dayFocus = this.id.substring(dayField.id.length);
		};
	}
	//根据字体大小,调整时间字段宽度
	var fontSize = CssUtils.getElementComputedStyle(this.document.getElementById(this.id + "Month"), 'font-size');
	fontSize = fontSize ? Number(fontSize.replace('px', '')) : 12;
	if(fontSize > 12) {
		this.getFieldElement().style.width = Math.floor(68 * fontSize / 12) + "px";
	}
};
DayField.prototype = new FormField();
DayField.prototype.onSelectDay = function(dayPickerButton) {
	var numberFields = [{field:this.document.getElementById(this.id + 'Month'), minNumber:1, maxNumber:12, focus:this.dayFocus=='Month'},
						{field:this.document.getElementById(this.id + 'Day'), minNumber:1, maxNumber:function(allFieldValues) {
							return allFieldValues[0]==2 ? 29 : (',1,3,5,7,8,10,12,'.indexOf(',' + allFieldValues[0] + ',')!=-1 ? 31 : 30);
						}, focus:this.dayFocus=='Day'}];
	new FormField.NumberPicker(this.getAttribute('title'), numberFields, '-', dayPickerButton, this.terminalType).show();
};
DayField.prototype._update = function() {
	var value = '';
	var month = new Number(this.document.getElementById(this.id + "Month").value);
	if(!isNaN(month) && month>=1 && month<=12) {
		var day = new Number(this.document.getElementById(this.id + "Day").value);
		if(!isNaN(month) && day>=1) {
			var limit = 30;
			if(month==2) {
				limit = 29;
			}
			else if(',1,3,5,7,8,10,12,'.indexOf(',' + month + ',')!=-1) {
				limit = 31;
			}
			day = (day>=limit ? limit : day);
			value = (month<10 ? "0" : "") + month + "-" + (day<10 ? "0" : "") + day;
		}
	}
	this.getInputElement().value = value;
	try {
		this.getInputElement().onchange();
	}
	catch(e) {
	
	}
};
DayField.setValue = function(fieldName, dayField, dayValue) { //设置月/日的值,dayField:month/day
	var field = FormField.getFormField(fieldName);
	var input = field.document.getElementById(field.id + dayField.substring(0, 1).toUpperCase() + dayField.substring(1));
	input.value = dayValue;
	input.onchange();
};