//菜单命令
EditorMenuCommand = function(name, title, iconIndex, iconUrl) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.iconUrl = iconUrl;
	this.showDropButton = true;
	this.width = 150; //菜单宽度
	this.height = 200; //菜单高度
	this.autoHeight = true; //是否自动高度
};
EditorMenuCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	
};
EditorMenuCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	
};
EditorMenuCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorMenuCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	DomSelection.selectRange(editorWindow, range);
	var items = this.createItems(editor, editorWindow, editorDocument, range, selectedElement);
	if(!items || items.length==0) {
		return;
	}
	var pickerHtml = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">' +
					 '<html>\n' +
					 '	<head>\n' +
					 '		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">\n' +
					 '	</head>\n' +
					 '	<body style="margin:0px; padding:0px; background-color:transparent;">\n' +
					 '		<div id="menuPicker" class="menubar" style="width: ' + (this.width-2) + 'px; position:static; overflow-y:auto; overflow-x:hidden;"/>' +
					 '	</body>\n' +
					 '</html>';
	this.picker = new FormField.Picker(this.title, pickerHtml, toolbarButton, this.width, this.height, false, this.autoHeight, false);
	var pickerDocument = this.picker.pickerFrame.document;
	var menuPicker = pickerDocument.getElementById("menuPicker");
	var command = this;
	//添加菜单项目
	for(var i=0; i<items.length; i++) {
		var item = pickerDocument.createElement('div');
		item.style.margin = "1px";
		item.className = "menunormal";
		item.id = items[i].id;
		if(items[i].html) {
			item.innerHTML = items[i].html;
		}
		else {
			var html = '<div style="word-break:keep-all; white-space:nowrap; height:16px; line-height:16px;">';
			if(items[i].iconIndex>=0 || items[i].iconUrl) {
				var icon = items[i].iconUrl;
				if(!icon) {
					icon = RequestUtils.getContextPath() + '/jeaf/htmleditor/images/icons.gif,0,' + (16 * items[i].iconIndex);
				}
				var values = icon.split(",");
				html += '<span style="display:inline-block; height:16px; width:20px; overflow:hidden; float:left;">' +
						' <img src="' + values[0] + '"' +
						'  style="' + (values.length<1 ? '' : 'margin-left:-' + values[1] + 'px; margin-top:-' + values[2] + 'px') + '"/>' +
						'</span>';
			}
			item.innerHTML = html + items[i].title + '</div>';
		}
		item.onmouseover = function() {
			this.className = "menuover";
		};
		item.onmouseout = function() {
			this.className = "menunormal";
		};
		item.onclick = function() {
			DomSelection.selectRange(editorWindow, range);
			command.onclick(this.id, editor, editorWindow, editorDocument, range, selectedElement);
			command.picker.destory();
		};
		menuPicker.appendChild(item);
	}
	//显示选择器
	this.picker.show();
	menuPicker.style.height = (DomUtils.getClientHeight(pickerDocument)-2) + "px";
};