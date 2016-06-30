//对话框命令
DialogCommand = function() {
	EditorMenuCommand.call(this, 'dialog', '对话框', -1, RequestUtils.getContextPath() + "/jeaf/dialog/templatemanage/icons/dialog.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
DialogCommand.prototype = new EditorMenuCommand;
DialogCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
DialogCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'dialogTitle', title:'插入对话框标题', iconIndex:-1},
			{id:'dialogCloseButton', title:'插入关闭按钮', iconIndex:-1},
			{id:'dialogBody', title:'插入对话框主体', iconIndex:-1}];
};
DialogCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("dialogTitle"==itemId || "dialogBody"==itemId) {
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
		if(!obj) {
			obj = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'a') ;
			if(!obj) {
				alert("请重新选择插入位置");
				return;
			}
		}
		obj.innerHTML = "dialogTitle"==itemId ? "<对话框标题>" : "<对话框主体>";
		obj.id = itemId;
	}
	else if("dialogCloseButton"==itemId) {
		HtmlEditor.editor.saveUndoStep();
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a,input,img,div,span,li,button");
		if(!obj) {
			obj = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'a') ;
			if(!obj) {
				alert("请重新选择插入位置");
				return;
			}
			obj.innerHTML = "关闭";
		}
		obj.title = "关闭";
		obj.id = "dialogCloseButton";
	}
};
HtmlEditor.registerCommand(new DialogCommand());