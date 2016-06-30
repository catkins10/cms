//列表选择,values格式:标题1|值1\0标题2|值2\0...\0标题n|值n,callback=function(selectedItemValue, selectedItemTitle);
FormField.ListPicker = function(values, valueField, titleField, displayArea, pickerWidth, alignRight, callback, terminalType) {
	var title = valueField ? valueField.title : (titleField ? titleField.title : null);
	FormField.Picker.call(this, title ? title : '选择', this._generatePickerHTML, displayArea, pickerWidth, 180, alignRight, false, false, false, terminalType);
	this.valueField = valueField;
	this.titleField = titleField;
	this.callback = callback;
	//生成条目列表
	if(values.substring(values.length - 1)=='\0') {
		values = values.substring(0, values.length - 1);
	}
	this.selectedIndex = -1;
	this.listItems = [];
	values = values.split("\0");
	for(var i = 0; i < values.length && values[i]!=""; i++) { 
		var index = values[i].indexOf("|");
		var listItem = {value: values[i].substring(index+1).trim(), title: (index==-1 ? values[i] : values[i].substring(0, index)).trim()};
		this.listItems.push(listItem);
		try {
			if(this.valueField.value==listItem.value) {
				this.selectedIndex = i;
			}
		}
		catch(e) {
		
		}
	}
	//初始化
	if(!this.isTouchMode) {
		this._initComputerPicker(); //初始化电脑版选择器
	}
	else {
		this._initTouchPicker(); //初始化触摸屏选择器
	}
};
FormField.ListPicker.prototype = new FormField.Picker();
//获取关联的字段列表,由继承者实现
FormField.ListPicker.prototype.getRelationFields = function() {
	return [this.valueField, this.titleField];
};
FormField.ListPicker.prototype._generatePickerHTML = function() { //生成HTML
	var html;
	if(this.isTouchMode) { //触摸屏
	 	html = '<div>' +
	 		   '	<table id="listPicker.listTable" border="0" cellspacing="0" cellpadding="0" width="100%">' +
			   '	</table>' +
			   '</div>';
	 }
	 else { //电脑版
		html = '<!DOCTYPE html>\n' +
			   '<html style="width:100%; height:100%">\n' +
			   '	<head>\n' +
			   '		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">\n' +
			   '	</head>\n' +
			   '	<body style="overflow:hidden; margin:0px; padding:0px; width:100%;  height:100%;">\n' +
			   '		<div class="listbar" style="overflow-y: auto; overflow-x: hidden;">\n' +
			   '			<table id="listPicker.listTable" border="0" cellpadding="0" cellspacing="0" width="100%" style="table-layout:fixed;">\n' +
			   '			</table>\n' +
			   '		</div>\n' +
			   '	</body>\n' +
			   '</html>';
	 }
	 return html;
};
FormField.ListPicker.prototype._initComputerPicker = function() { //初始化电脑版选择器
	var picker = this;
	this.listTable = this.pickerFrame.document.getElementById("listPicker.listTable");
	for(var i = 0; i < this.listItems.length; i++) { 
		var td = this.listTable.insertRow(-1).insertCell(-1);
		td.noWrap = true;
		td.className = "listnormal";
		td.id = this.listItems[i].value;
		td.innerHTML = this.listItems[i].title;
		td.onmouseover = function() {
			for(var i = 0; i < picker.listTable.rows.length; i++) {
				var cell =  picker.listTable.rows[i].cells[0];
				cell.className = cell==this ? "listover" : "listnormal";
			}
		}
		td.onmousedown = function() {
			picker.destory();
			picker._onOK(this.id, this.innerHTML);
		}
	}
	this.pickerHeight = Math.min(Math.max(this.listTable.offsetHeight + DomUtils.getBorderWidth(this.listTable.parentNode, 'top') + DomUtils.getBorderWidth(this.listTable.parentNode, 'bottom'), 10), 180);
};
FormField.ListPicker.prototype._initTouchPicker = function() { //初始化触摸屏选择器
	var picker = this;
	this.doOK = function() {
		var values = "", titles = ""; 
		var inputs = picker.pickerBody.getElementsByTagName('input');
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].checked) {
				values += (values=="" ? "" : ",") + inputs[i].value;
				titles += (titles=="" ? "" : ",") + inputs[i].parentNode.parentNode.cells[0].innerHTML;
			}
		}
		picker._onOK(values, titles);
	};
	this.listTable = DomUtils.getElement(this.pickerBody, 'table', 'listPicker.listTable');
	for(var i = 0; i < this.listItems.length; i++) { 
		var tr = this.listTable.insertRow(-1);
		tr.onclick = function() {
			var input = this.getElementsByTagName('input')[0];
			input.checked = this.multiSelect ? !input.checked : true;
		};
		//创建列表条目内容单元格
		var valueTd = tr.insertCell(-1);
		valueTd.width = "100%";
		valueTd.innerHTML = this.listItems[i].title;
		//创建选择框单元格
		var boxTd = tr.insertCell(-1);
		boxTd.align = "center";
		boxTd.nowrap = true;
		var radio = this.listTable.ownerDocument.createElement('input');
		radio.name = "listPicker.selectedItem";
		radio.type = this.multiSelect ? 'checkbox' : 'radio';
		radio.value = this.listItems[i].value;
		boxTd.appendChild(radio);
		//设置样式
		valueTd.className = boxTd.className = "listItem";
		if(i == this.listItems.length - 1) { //最后一个
			 valueTd.style.borderBottomStyle = boxTd.style.borderBottomStyle = "none";
		}
	}
	var clientHeight = DomUtils.getClientHeight(this.pickerContainer.ownerDocument);
	if(this.pickerContainer.offsetHeight <= clientHeight - 20) {
		return;
	}
	var div = this.listTable.parentNode;
	div.style.height = (div.offsetHeight - this.pickerContainer.offsetHeight + clientHeight - 20) + "px";
	div.style.overflow = "hidden";
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/jeaf/common/js/scroller.js', 'scrollerScript', function() {
		new Scroller(div, false, true, true, false, false);
	});
};
FormField.ListPicker.prototype._onOK = function(value, title) {
	try { this.titleField.value = title; this.titleField.focus(); } catch(e) {}
	try { this.valueField.value = value; this.valueField.focus(); } catch(e) {}
	try { this.titleField.onchange(); } catch(e) {}
	try { this.valueField.onchange(); } catch(e) {}
	if(!this.callback) {
		return;
	}
	try {
		this.callback.call(this, value, title);
	}
	catch(e) {
	
	}
};
//显示选择器
FormField.ListPicker.prototype.show = function(pickerLeft, pickerTop, displayOnly) {
	this.constructor.prototype.show.call(this, pickerLeft, pickerTop, displayOnly);
	if(this.selectedIndex==-1) {
		return;
	}
	if(!this.isTouchMode) {
		var cell = this.listTable.rows[this.selectedIndex].cells[0];
		cell.className = "listover";
		this.listTable.parentNode.scrollTop = cell.offsetTop - this.pickerHeight / 2 + cell.offsetHeight / 2;
	}
	else {
		var row = this.listTable.rows[this.selectedIndex];
		row.cells[1].childNodes[0].checked = true;
		this.listTable.parentNode.scrollTop = row.cells[0].offsetTop - this.listTable.parentNode.offsetHeight / 2 + row.cells[0].offsetHeight / 2;
	}
};