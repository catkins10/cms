//日期选择,terminalType:终端类型,computer/android/iphone/symbian
FormField.DatePicker = function(inputField, displayArea, alignLeft, terminalType) {
	this.inputField = inputField;
	var date = new Date(inputField.value.replace(new RegExp("-", "g"), "/"));
	if(!isNaN(date)) {
		window.datePickerLastValue = date;
	}
	else {
		date = window.datePickerLastValue ? window.datePickerLastValue : new Date();
   	}
   	this.year = date.getFullYear();
   	this.month = date.getMonth() + 1;
   	this.day = date.getDate();
   	FormField.Picker.call(this, inputField.title, this._generatePickerHTML, displayArea, 330, 200, !alignLeft, true, false, false, terminalType);
	if(!this.isTouchMode) {
		this._resetPicker(this.pickerFrame.document.body); //重置日期选择器
	}
	else {
		this._initTouchPicker();
	}
};
FormField.DatePicker.prototype = new FormField.Picker();
//获取关联的字段列表,由继承者实现
FormField.DatePicker.prototype.getRelationFields = function() {
	return [this.inputField];
};
FormField.DatePicker.prototype._generatePickerHTML = function() { //生成日历HTML
	var weekDays = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
	var html = ''
	html += '<div class="datePicker" style="clear: both;">' +
			' <div class="yearMonthPicker">' +
			'	<table border="0" cellspacing="0" cellpadding="0">' +
			'	 <tr>' +
			'	  <td><div id="yearPicker" class="yearPicker" style="overflow: hidden;"></div></td>' +
			'	  <td><div id="monthPicker" class="monthPicker" style="overflow: hidden;"></div></td>' +
			'	 </tr>' +
			'	</table>' +
			' </div>' +
			' <div id="dayPicker" style="clear: both;">' +
			'  <table id="dayTable" class="dayTable" border="1" cellspacing="0" cellpadding="0" width="100%">' +
			'	<tr>';
	for(var i=0; i<weekDays.length; i++) {
		html += '	<td align="center" class="week">' + weekDays[i] + '</td>';
	}
	html += '	</tr>';
	for(var i=0; i<6; i++) {
		html+= '<tr>';
		for(var j=0; j<7; j++) { 
			html += '<td align="center" class="day"></td>';
		}
		html += '</tr>';
	}
	html += '  </table>' +
			' </div>' +
			'</div>';
	return html;
};
FormField.DatePicker.prototype._initTouchPicker = function() { //初始化触摸屏日历
	this.created = false;
	var picker = this;
	this.doOK = function() {
		if(picker.day!=-1) {
			picker.inputField.value = picker.year + '-' + picker.month + '-' + picker.day;
			window.top.datePickerLastValue = new Date(picker.year + '/' + picker.month + '/' + picker.day);
			try {
				picker.inputField.onchange();
			}
			catch(e) {
			
			}
		}
	};
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/jeaf/common/js/scroller.js', 'scrollerScript', function() {
	   	var dayPicker = DomUtils.getElement(picker.pickerBody, 'div', 'dayPicker');
		dayPicker.scroller = new Scroller(dayPicker, false, false, false, false, false);
		dayPicker.scroller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
			if(Math.abs(y) < Math.abs(x)) {
				y = 0;
			}
			else {
				x = 0;
			}
			if(x>0 || y>0) {
				picker.month++;
				if(picker.month>12) {
					picker.month = 1;
					picker.year++;
				}
			}
			else if(x<0 || y<0) {
				picker.month--;
				if(picker.month<1) {
					picker.month = 12;
					picker.year--;
				}
			}
			picker.day = -1;
			picker._resetPicker(picker.pickerBody);
		};
		picker._resetPicker(picker.pickerBody);
		picker.created = true;
   	});
};
FormField.DatePicker.prototype._resetPicker = function(pickerBody) { //重置日期选择器
   	var picker = this;
  	//生成年选择器
   	FormField.NumberPicker.generateNumberPicker(DomUtils.getElement(pickerBody, 'div', 'yearPicker'), this.year - 10, this.year + 10, this.year, "年", function(numberValue) {
   		picker.year = numberValue;
   		picker.day = -1;
   		picker._resetPicker(pickerBody);
   	}, false, this.isTouchMode);
   	
   	//生成月选择器
   	FormField.NumberPicker.generateNumberPicker(DomUtils.getElement(pickerBody, 'div', 'monthPicker'), 1, 12, this.month, "月", function(numberValue) {
   		picker.month = numberValue;
   		picker.day = -1;
   		picker._resetPicker(pickerBody);
   	}, false, this.isTouchMode);
   	
   	//输出日期
  	this.dayCell = null;
	var table = DomUtils.getElement(pickerBody, 'table', 'dayTable');
	var today = new Date();
	var date = new Date(this.year, this.month - 1, 1);
	var week = date.getDay();
	var maxDay = 100;
	for(var i=0; i<42; i++) {
		var cell = table.rows[Math.floor(i/7)+1].cells[i%7];
		var value = "&nbsp;";
		var className = "day";
		if(i >= week && i < maxDay) {
			value = date.getDate();
			date.setDate(value + 1);
			if(date.getMonth() + 1 != this.month) {
				maxDay = value;
			}
			if(i%7==0 || i%7==6) {
				className += " weekend";
			}
			if(this.year==today.getFullYear() && this.month==today.getMonth() + 1 && value==today.getDate()) {
				className += " today";
			}
			if(value==this.day) {
				this.dayCell = cell;
				className += " selectedDay";
			}
		}
		cell.innerHTML = value;
		cell.className = className;
		if(cell.onclick) {
			continue;
		}
		cell.onclick = function() {
			var day = Number(this.innerHTML);
			if(isNaN(day)) {
				return;
			}
			picker.day = day;
			if(picker.dayCell) {
				picker.dayCell.className = picker.dayCell.className.replace(" selectedDay", '');
			}
			picker.dayCell = this;
			this.className = this.className + " selectedDay";
			if(picker.isTouchMode) {
				return;
			}
			picker.inputField.value = picker.year + '-' + picker.month + '-' + picker.day;
			window.top.datePickerLastValue = new Date(picker.year + '/' + picker.month + '/' + picker.day);
			try {
				picker.inputField.onchange();
			}
			catch(e) {
			
			}
			picker.destory();
		};
	}
};
FormField.DatePicker.prototype._generateYearList = function() { //生成年下拉列表
	var itemsText = '';
	for(var i = this.year - 50; i < this.year + 50; i++) {
		itemsText += (itemsText=='' ? '' : '\0 ') + i + '年';
	}
	return itemsText;
};