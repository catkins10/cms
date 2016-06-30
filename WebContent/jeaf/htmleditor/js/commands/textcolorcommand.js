//撤销、重做命令
EditorTextColorCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showDropButton = true;
};
EditorTextColorCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return "" + editorDocument.queryCommandEnabled(this.name)=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorTextColorCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	var colors = '000000,993300,333300,003300,003366,000080,333399,333333,800000,FF6600,808000,808080,008080,0000FF,666699,808080,FF0000,FF9900,99CC00,339966,33CCCC,3366FF,800080,999999,FF00FF,FFCC00,FFFF00,00FF00,00FFFF,00CCFF,993366,C0C0C0,FF99CC,FFCC99,FFFF99,CCFFCC,CCFFFF,99CCFF,CC99FF,FFFFFF'.split(',');
	var pickerHtml = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">' +
					 '<html>\n' +
					 '	<head>\n' +
					 '		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">\n' +
					 '	</head>\n' +
					 '	<body style="margin:0px; padding:0px; border-style:none;">\n' +
					 '		<div id="colorPicker" class="listbar" style="position:static;"/>' +
					 '	</body>\n' +
					 '</html>';
	this.picker = new FormField.Picker(this.title, pickerHtml, toolbarButton, 163, 180, false, true, false);
	var pickerDocument = this.picker.pickerFrame.document;
	var colorPicker = pickerDocument.getElementById("colorPicker");
	
	var command = this;
	//添加"自动"按钮
	this._addButton('自动', colorPicker).onclick = function() {
		command._setColor(command.name=="foreColor" ? "#000000" : "#ffffff", editor, editorWindow, editorDocument, range);
	};
	//添加颜色选取框
	for(var i=0; i<colors.length; i++) {
		if(i!=0 && i%8==0) {
			var br = pickerDocument.createElement('br');
			br.style.clear = 'both';
			colorPicker.appendChild(br);
		}
		var colorBox = pickerDocument.createElement('span');
		colorBox.style.width = '12px';
		colorBox.style.height = '12px';
		colorBox.style.display = 'inline-block';
		colorBox.style.margin = '1px';
		colorBox.style.padding = '2px';
		colorBox.style.border = '#fff 1px solid';
		colorBox.id = colors[i];
		colorBox.onmouseover = function() {
			this.style.borderColor = '#555555';
			this.style.backgroundColor = '#e0e0e8';
		};
		colorBox.onmouseout = function() {
			this.style.borderColor = '#fff';
			this.style.backgroundColor = 'transparent';
		};
		colorBox.onclick = function() {
			command._setColor("#" + this.id, editor, editorWindow, editorDocument, range);
		};
		colorPicker.appendChild(colorBox);

		var colorDiv = pickerDocument.createElement('span');
		colorDiv.style.width = '10px';
		colorDiv.style.height = '10px';
		colorDiv.style.display = 'inline-block';
		colorDiv.style.border = '#dddddd 1px solid';
		colorDiv.style.backgroundColor = '#' + colors[i];
		colorBox.appendChild(colorDiv);
	}
	//添加"其它颜色..."按钮
	var br = pickerDocument.createElement('br');
	br.style.clear = 'both';
	colorPicker.appendChild(br);
	this._addButton('其它颜色...', colorPicker).onclick = function() {
		DialogUtils.openColorDialog(command.title, '', '', function(colorValue) {
			command._setColor(colorValue, editor, editorWindow, editorDocument, range);
		});
	};
	//显示选择器	
	this.picker.show();
};
EditorTextColorCommand.prototype._setColor = function(color, editor, editorWindow, editorDocument, range) {
	this.picker.destory();
	DomSelection.selectRange(editorWindow, range);
	editor.saveUndoStep();
	editor.setFontStyle(this.name=='foreColor' ? 'color' : 'backgroundColor', color);
};
EditorTextColorCommand.prototype._addButton = function(name, colorPicker) {
	var button = colorPicker.ownerDocument.createElement('div');
	button.style.padding = "4px 0px 4px 0px";
	button.style.textAlign = "center";
	button.innerHTML = name;
	button.style.border = '#ffffff 1px solid';
	button.onmouseover = function() {
		this.style.border = '#555555 1px solid';
		this.style.backgroundColor = '#e0e0e8';
	};
	button.onmouseout = function() {
		this.style.border = '#ffffff 1px solid';
		this.style.backgroundColor = '#ffffff';
	};
	colorPicker.appendChild(button);
	return button;
};