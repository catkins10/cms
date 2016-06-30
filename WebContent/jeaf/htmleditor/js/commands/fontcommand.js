//字体命令
EditorFontCommand = function(name, title, iconIndex) {
	EditorMenuCommand.call(this, name, title, iconIndex);
	this.width = 160; //菜单宽度
	this.height = 200; //菜单高度
	this.autoHeight = true; //是否自动高度
};
EditorFontCommand.prototype = new EditorMenuCommand;
EditorFontCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	var items = [];
	if(this.name=="fontName") {
		var value = CssUtils.getElementComputedStyle(selectedElement, "font-family");
		var fontNames = '宋体,黑体,微软雅黑,楷体_GB2312,仿宋_GB2312,隶书,幼圆,新宋体,细明体,Arial,Comic Sans MS,Courier New,Tahoma,Times New Roman,Verdana'.split(',');
		for(var i=0; i<fontNames.length; i++) {
			var html = '<span style="display:inline-block; padding-left: 22px; background-repeat:no-repeat; background-position: center left;' + (value==fontNames[i] ? ' background-image:url(' + RequestUtils.getContextPath() + '/jeaf/htmleditor/images/checked.gif)' : '') + '">' +
					   '	<font style="font-family:' + fontNames[i] + '">' + fontNames[i] + '</font>' +
					   '</span>';
			items.push({id:fontNames[i], html:html});
		}
	}
	else if(this.name=="fontSize") {
		var value = CssUtils.getElementComputedStyle(selectedElement, "font-size");
		var fontSizes = '8,10,11,12,13,14,16,18,20,24,28,32,36,48,64'.split(',');
		for(var i=0; i<fontSizes.length; i++) {
			var html = '<span style="display:inline-block; padding-left: 22px; background-repeat:no-repeat; background-position: center left;' + (value==fontSizes[i] + "px" ? ' background-image:url(' + RequestUtils.getContextPath() + '/jeaf/htmleditor/images/checked.gif)' : '') + '">' +
					   '	<font style="font-size:' + fontSizes[i] + 'px">' + fontSizes[i] + 'px</fonr>' +
					   '</span>';
			items.push({id:fontSizes[i], html:html});
		}
	}
	return items;
};
EditorFontCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	editor.saveUndoStep();
	if(this.name=="fontName") {
		editor.setFontStyle('fontFamily', itemId);
	}
	else if(this.name=="fontSize") {
		editor.setFontStyle('fontSize', itemId + 'px');
	}
};
EditorFontCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return "" + editorDocument.queryCommandEnabled(this.name)=="true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};