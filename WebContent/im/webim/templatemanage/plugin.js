//WEBIM加载命令
WebimAddonCommand = function() {
	EditorMenuCommand.call(this, 'webimAddon', 'WEBIM', -1, RequestUtils.getContextPath() + "/im/icons/im.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
WebimAddonCommand.prototype = new EditorMenuCommand;
WebimAddonCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
WebimAddonCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'addWebim', title:'加载WEBIM', iconIndex:-1},
			{id:'removeWebim', title:'卸载WEBIM', iconIndex:-1},
			{id:'addWebcs', title:'加载WEB客服', iconIndex:-1},
			{id:'removeWebcs', title:'卸载WEB客服', iconIndex:-1}];
};
WebimAddonCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("addWebim"==itemId) {
		HtmlEditor.editor.saveUndoStep();
		ScriptUtils.appendJsFile(HtmlEditor.editorDocument, RequestUtils.getContextPath() + "/im/webim/js/webimLoader.js", 'scriptWebim');
		alert("WEBIM加载完成");
	}
	else if("removeWebim"==itemId || "removeWebcs"==itemId) {
		HtmlEditor.editor.saveUndoStep();
		var scriptWebim = HtmlEditor.editorDocument.getElementById('scriptWebim');
		if(scriptWebim) {
			scriptWebim.parentNode.removeChild(scriptWebim);
		}
		alert("卸载完成");
	}
	else if("addWebcs"==itemId) {
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/im/webim/templatemanage/webcsAddon.shtml", 520, 280, '', HtmlEditor.getDialogArguments());
	}
};
HtmlEditor.registerCommand(new WebimAddonCommand());

//插入WEBIM操作命令
WebimActionCommand = function(name, title) {
	EditorMenuCommand.call(this, name, title, -1, RequestUtils.getContextPath() + "/im/icons/im.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
WebimActionCommand.prototype = new EditorMenuCommand;
WebimActionCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
WebimActionCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	if(this.name=="webcs") {
		return [{id:'insertCloseWebcsButton', title:'插入关闭在线客服按钮', iconIndex:-1}];
	}
	else {
		return [{id:'chatButton', title:'设为对话按钮', iconIndex:-1},
				{id:'onlineUserButton', title:'设为在线用户按钮', iconIndex:-1},
				{id:'userTreeButton', title:'设为用户目录树按钮', iconIndex:-1},
				{id:'systemMessageButton', title:'设为系统消息按钮', iconIndex:-1},
				{id:'personalSettingButton', title:'设为个人设置按钮', iconIndex:-1},
				{id:'insertCloseWebimButton', title:'插入关闭WEBIM按钮', iconIndex:-1},
				{id:'insertCloseChatButton', title:'插入关闭对话按钮', iconIndex:-1}];
	}
};
WebimActionCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("insertCloseWebimButton"==itemId || 'insertCloseWebcsButton'==itemId || "insertCloseChatButton"==itemId) {
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, 'a,span,div,button,input,img,td');
		if(!obj) {
			obj = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'a') ;
			obj.innerHTML = "关闭";
		}
		obj.setAttribute("onclick", ("insertCloseChatButton"==itemId ? "window.webim.closeChat(this);" : "window.webim.closeWebim();"));
		alert("关闭按钮插入完成");
	}
	else {
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, 'div,td,span,li');
		if(!obj) {
			alert("位置不正确，请重新选择。");
			return;
		}
		var action;
		if("chatButton") {
			action = "对话";
		}
		else if("onlineUserButton") {
			action = "在线用户";
		}
		else if("userTreeButton") {
			action = "用户目录树";
		}
		else if("systemMessageButton") {
			action = "系统消息";
		}
		else if("personalSettingButton") {
			action = "个人设置";
		}
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/im/webim/templatemanage/insertWebimAction.shtml?action=" + StringUtils.utf8Encode(action), 562, 230, "", HtmlEditor.getDialogArguments());	
	}
};
HtmlEditor.registerCommand(new WebimActionCommand('webim', 'WEBIM'));
HtmlEditor.registerCommand(new WebimActionCommand('webcs', 'WEB客服'));

//插入WEBIM窗口操作
WebimDialogCommand = function() {
	EditorMenuCommand.call(this, 'webimDialog', 'WEBIM窗口', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_zoom_out_button.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
WebimDialogCommand.prototype = new EditorMenuCommand;
WebimDialogCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
WebimDialogCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'closeButton', title:'插入关闭按钮', iconIndex:-1},
			{id:'minimizeButton', title:'插入最小化按钮', iconIndex:-1},
			{id:'zoomOutButton', title:'插入窗口放大按钮', iconIndex:-1},
			{id:'okButton', title:'插入确定按钮', iconIndex:-1}];
};
WebimDialogCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "td,span,div,a,img,button,input");
	if(!obj) {
		alert("请重新选择插入位置。");
		return;
	}
	HtmlEditor.editor.saveUndoStep();
	var script;
	if("closeButton"==itemId) {
		obj.title = "关闭";
		script = "parent.frames['frameWebim'].webim.closeWebimDialog(false)";
	}
	else if("minimizeButton"==itemId) {
		obj.title = "最小化";
		script = "parent.frames['frameWebim'].webim.closeWebimDialog(true)";
	}
	else if("zoomOutButton"==itemId) {
		obj.title = "放大";
		script = "parent.frames['frameWebim'].webim.zoomOutWebimDialog(0.2)";
	}
	else if("okButton"==itemId) {
		obj.title = "OK";
		script = "doOK();";
	}
	obj.setAttribute("onclick", script);
	alert("操作完成");
};
HtmlEditor.registerCommand(new WebimDialogCommand());

//插入WEBIM对话操作
WebimChatCommand = function() {
	EditorMenuCommand.call(this, 'webimChat', 'WEBIM对话', -1, RequestUtils.getContextPath() + "/im/icons/im.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
WebimChatCommand.prototype = new EditorMenuCommand;
WebimChatCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
WebimChatCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'fontSettingAction', title:'设为设置字体按钮', iconIndex:-1},
			{id:'insertExpressionAction', title:'设为插入表情按钮', iconIndex:-1},
			{id:'sendFileAction', title:'设为发送文件按钮', iconIndex:-1},
			{id:'sendImageAction', title:'设为发送图片按钮', iconIndex:-1},
			{id:'addPersonAction', title:'设为添加用户按钮', iconIndex:-1},
			{id:'fontSettingBox', title:'设为字体配置框', iconIndex:-1},
			{id:'expressionBox', title:'设为表情列表框', iconIndex:-1},
			{id:'fileUploadingBox', title:'设为文件上传提示框', iconIndex:-1}];
};
WebimChatCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
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
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, itemId.indexOf('Box')!=-1 ? "td,div" : "td,span,div,a,img,button,input");
	if(!obj) {
		alert("请重新选择插入位置。");
		return;
	}
	obj.id = itemId;
	obj.title = title;
	alert("操作完成");
};
HtmlEditor.registerCommand(new WebimChatCommand());