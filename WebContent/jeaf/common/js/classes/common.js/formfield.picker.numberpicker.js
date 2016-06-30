//时间选择,numberFields:[{field:字段, minNumber:最小值, maxNumner:最大值, suffix:后缀(如:月,日), focus:是否获得焦点}, ...], terminalType:终端类型,computer/android/iphone/symbian
FormField.NumberPicker = function(pickerTitle, numberFields, separator, displayArea, terminalType) {
	this.numberFields = numberFields;
	this.separator = separator;
	FormField.Picker.call(this, pickerTitle, this._generatePickerHTML, displayArea, 330, 200, true, true, false, false, terminalType);
	if(this.isTouchMode) {
		this._initTouchPicker();
	}
};
FormField.NumberPicker.prototype = new FormField.Picker();
//获取关联的字段列表,由继承者实现
FormField.NumberPicker.prototype.getRelationFields = function() {
	var fields = [];
	for(var i = 0; i < this.numberFields.length; i++) {
		fields.push(this.numberFields[i].field);
	}
	return fields;
};
FormField.NumberPicker.prototype._generatePickerHTML = function() { //生成HTML
	if(!this.isTouchMode) {
		return null;
	}
	var html = '';
	html += '<table id="numberPickerTable" cellspacing="0" cellpadding="0" align="center">';
	html += '	<tr>';
	for(var i = 0; i < this.numberFields.length; i++) {
		html+= '	<td><div class="numberPicker" style="overflow: hidden;"></div></td>';
		if(i < this.numberFields.length - 1) {
			html +='<td class="numberPickerSeparator">' + this.separator + '</td>';
		}
	}
	html += '	</tr>';
	html += '</table>';
	return html;
};
FormField.NumberPicker.prototype._initTouchPicker = function() { //初始化触摸屏日历
	var picker = this;
	this.doOK = function() {
		for(var i = 0; i < picker.numberFields.length; i++) {
			picker.numberFields[i].field.value = picker.numberFields[i]._value;
			try {
				picker.numberFields[i].field.onchange();
			}
			catch(e) {
			
			}
		}
	};
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/jeaf/common/js/scroller.js', 'scrollerScript', function() {
	   	picker._resetPicker();
   	});
};
FormField.NumberPicker.prototype._resetPicker = function(pickerBody) { //重置日期选择器
   	var picker = this;
   	this._retrieveFieldValues();
	var numberPickerTable = DomUtils.getElement(this.pickerBody, 'table', 'numberPickerTable');
   	for(var i = 0; i < this.numberFields.length; i++) {
  		FormField.NumberPicker.generateNumberPicker(numberPickerTable.rows[0].cells[i * 2].childNodes[0], this.numberFields[i]._minNumber, this.numberFields[i]._maxNumber, this.numberFields[i]._value, this.numberFields[i].suffix, function(numberValue, parentElement) {
	  		picker.numberFields[parentElement.parentNode.cellIndex / 2]._value = numberValue;
	  		picker._resetPicker();
	  	}, true, this.isTouchMode);
	}
};
//显示选择器
FormField.NumberPicker.prototype.show = function(pickerLeft, pickerTop, displayOnly) {
	if(this.isTouchMode) {
		this.constructor.prototype.show.call(this, pickerLeft, pickerTop, displayOnly);
		return;
	}
	var focus = 0;
	for(; focus < this.numberFields.length && !this.numberFields[focus].focus; focus++);
	this._retrieveFieldValues();
	var itemsText = '';
	for(var i= this.numberFields[focus]._minNumber; i <= this.numberFields[focus]._maxNumber; i++) {
		itemsText += (itemsText=='' ? '' : '\0 ') + i;
	}
	var picker = this;
	new FormField.ListPicker(itemsText, this.numberFields[focus].field, null, this.displayArea, 50, true, function() {
		if(focus < picker.numberFields.length - 1) { //焦点自动移动到下一个字段
			window.setTimeout(function() {
				picker.numberFields[focus + 1].field.focus();
			}, 100);
		}
	}).show();
};
FormField.NumberPicker.prototype._retrieveFieldValues = function() {
	var values = [];
	//设置字段值
	for(var i = 0; i < this.numberFields.length; i++) {
		if(!this.numberFields[i]._value && this.numberFields[i]._value!=0) {
			var suffix = this.numberFields[i].suffix;
			this.numberFields[i]._value = Number(suffix ? this.numberFields[i].field.value.replace(suffix, '') : this.numberFields[i].field.value);
			if(isNaN(this.numberFields[i]._value)) {
				this.numberFields[i]._value = 0;
			}
		}
		values.push(this.numberFields[i]._value);
	}
	//获取字段的最大值和最小值,并检查当前值是否超出范围
	for(var i = 0; i < this.numberFields.length; i++) {
		this.numberFields[i]._minNumber = typeof this.numberFields[i].minNumber == 'function' ? this.numberFields[i].minNumber.call(this, values) : this.numberFields[i].minNumber;
		this.numberFields[i]._maxNumber = typeof this.numberFields[i].maxNumber == 'function' ? this.numberFields[i].maxNumber.call(this, values) : this.numberFields[i].maxNumber;
		this.numberFields[i]._value = Math.min(Math.max(this.numberFields[i]._value, this.numberFields[i]._minNumber), this.numberFields[i]._maxNumber);
	}
};
//数字选择
FormField.NumberPicker.generateNumberPicker = function(parentElement, minValue, maxValue, currentValue, suffix, onNumberPicked, verticalScroll, isTouchMode) {
	if(!suffix) {
		suffix = '';
	}
	parentElement.innerHTML = "";
	var document = parentElement.ownerDocument;
	var divNumbers = document.createElement("div");
	parentElement.appendChild(divNumbers);
	var selectedIndex;
	for(var i = minValue-1; i <= maxValue+1; i++) {
		var span = document.createElement("span");
		span.className = "pickerItem" + (currentValue==i ? " selectedPickerItem" : "");
		if(!verticalScroll) {
			span.style.display = 'inline-block';
		}
		span.innerHTML = i==minValue-1 || i==maxValue+1 ? '&nbsp;' : i + suffix + (isTouchMode || currentValue!=i ? '' : '<span class="dropDownButton">&nbsp;</span>');
		if(currentValue==i) {
			selectedIndex = divNumbers.childNodes.length;
		}
		else if(i!=minValue-1 && i!=maxValue+1) {
			span.onclick = function() {
				onNumberPicked.call(null, Number(suffix=='' ? this.innerHTML : this.innerHTML.replace(suffix, '')), parentElement);
			};
		}
		divNumbers.appendChild(span);
		if(isTouchMode || currentValue!=i) {
			continue;
		}
		span.onclick = function() {
			var itemsText = '';
			for(var j = minValue; j <= maxValue; j++) {
				itemsText += (itemsText=='' ? '' : '\0 ') + j + suffix;
			}
			var listPicker = new FormField.ListPicker(itemsText, null, null, this, this.offsetWidth + 8, true, function(selectedItemValue, selectedItemTitle) {
				onNumberPicked.call(null, Number(suffix=='' ? selectedItemValue : selectedItemValue.replace(suffix, '')), parentElement);
			});
			listPicker.selectedIndex = currentValue - minValue;
			listPicker.show();
		};
	}
	if(verticalScroll) { //垂直滚动
		parentElement.scrollTop = (selectedIndex - 1) * divNumbers.childNodes[0].offsetHeight;
	}
	else { //水平滚动
		divNumbers.style.width = (divNumbers.childNodes[0].offsetWidth * (maxValue - minValue + 3)) + "px";
		parentElement.scrollLeft = (selectedIndex - 1) * divNumbers.childNodes[0].offsetWidth;
	}
	if(!isTouchMode) {
		return;
	}
	//为触摸屏选择器创建滚动条
	parentElement.scroller = new Scroller(parentElement, !verticalScroll, verticalScroll, false, false, false);
	parentElement.scroller.onAfterScroll = function(x, y, isLeft, isRight, isTop, isBottom, touchEnd) {
		var index;
		if(verticalScroll) {
			index = Math.round(parentElement.scrollTop / divNumbers.childNodes[0].offsetHeight) + 1;
		}
		else {
			index = Math.round(parentElement.scrollLeft / divNumbers.childNodes[0].offsetWidth) + 1;
		}
		onNumberPicked.call(null, Number(suffix=='' ? divNumbers.childNodes[index].innerHTML : divNumbers.childNodes[index].innerHTML.replace(suffix, '')), parentElement);
	};
};