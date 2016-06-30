//IM窗口命令
IMDialogCommand = function() {
	EditorMenuCommand.call(this, 'IMDialog', 'IM窗口', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_zoom_out_button.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
IMDialogCommand.prototype = new EditorMenuCommand;
IMDialogCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
IMDialogCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'closeButton', title:'插入窗口关闭按钮', iconIndex:-1},
			{id:'minimizeButton', title:'插入窗口最小化按钮', iconIndex:-1},
			{id:'maximizeButton', title:'插入窗口最大化按钮', iconIndex:-1},
			{id:'restoreButton', title:'插入窗口还原按钮', iconIndex:-1},
			{id:'border1Color', title:'设置窗口外边框颜色', iconIndex:-1},
			{id:'border2Color', title:'设置窗口内边框颜色', iconIndex:-1},
			{id:'okButton', title:'插入确定按钮', iconIndex:-1}];
};
IMDialogCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("border1Color"==itemId || "border2Color"==itemId) {
		var color = HtmlEditor.editorDocument.body.getAttribute(itemId);
		DialogUtils.openColorDialog(("border1Color"==itemId ? "外边框颜色" : "内边框颜色"), (color ? color : ""), "", function(colorValue) {
			HtmlEditor.editorDocument.body.setAttribute(itemId, colorValue);
		});
		return;
	}
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "td,span,div,a,img,button,input");
	if(!obj) {
		alert("请重新选择插入位置。");
		return;
	}
	HtmlEditor.editor.saveUndoStep();
	var script;
	if("closeButton"==itemId) {
		obj.title = "关闭";
		script = "doHtmlDialogAction('WINDOW_CLOSE')";
	}
	else if("minimizeButton"==itemId) {
		obj.title = "最小化";
		script = "doHtmlDialogAction('WINDOW_MINIMIZE')";
	}
	else if("maximizeButton"==itemId) {
		obj.title = "最大化";
		obj.id = "windowMaximizeButton";
		script = "document.getElementById('windowRestoreButton').style.display='';document.getElementById('windowMaximizeButton').style.display='none';doHtmlIMDialogAction('WINDOW_MAXIMIZE')";
	}
	else if("restoreButton"==itemId) {
		obj.title = "还原";
		obj.id = "windowRestoreButton";
		obj.style.display = "none";
		script = "document.getElementById('windowRestoreButton').style.display='none';document.getElementById('windowMaximizeButton').style.display='';doHtmlIMDialogAction('WINDOW_RESTORE')";
	}
	else if("okButton"==itemId) {
		obj.title = "OK";
		script = "doOK();";
	}
	obj.setAttribute("onclick", script);
	alert("操作完成");
};
HtmlEditor.registerCommand(new IMDialogCommand());

//插入IM对话操作
IMChatCommand = function() {
	EditorMenuCommand.call(this, 'IMChat', 'IM对话', -1, RequestUtils.getContextPath() + "/im/icons/im.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
IMChatCommand.prototype = new EditorMenuCommand;
IMChatCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
IMChatCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'fontSettingAction', title:'设为设置字体按钮', iconIndex:-1},
			{id:'insertExpressionAction', title:'设为插入表情按钮', iconIndex:-1},
			{id:'sendFileAction', title:'设为发送文件按钮', iconIndex:-1},
			{id:'sendImageAction', title:'设为发送图片按钮', iconIndex:-1},
			{id:'addPersonAction', title:'设为添加用户按钮', iconIndex:-1},
			{id:'fontSettingBox', title:'设为字体配置框', iconIndex:-1},
			{id:'expressionBox', title:'设为表情列表框', iconIndex:-1},
			{id:'fileUploadingBox', title:'设为文件上传提示框', iconIndex:-1}];
};
IMChatCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	var title;
	if("fontSettingAction"==itemId) {
		title = "字体";
	}
	else if("insertExpressionAction"==itemId) {
		title = "插入表情";
	}
	else if("sendFileAction"==itemId) {
		title = "发送文件";
	}
	else if("sendImageAction"==itemId) {
		title = "发送图片";
	}
	else if("addPersonAction"==itemId) {
		title = "添加用户";
	}
	else if("fontSettingBox"==itemId) {
		title = "字体";
	}
	else if("expressionBox"==itemId) {
		title = "表情";
	}
	else if("fileUploadingBox"==itemId) {
		title = "文件上传";
	}
	HtmlEditor.editor.saveUndoStep();
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "td,div");
	if(!obj) {
		alert("请重新选择插入位置。");
		return;
	}
	obj.id = itemId;
	obj.title = title;
	alert("操作完成");
};
HtmlEditor.registerCommand(new IMChatCommand());