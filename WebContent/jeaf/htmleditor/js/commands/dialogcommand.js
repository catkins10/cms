//对话框命令
EditorDialogCommand = function(name, title, showTitle, iconIndex, iconUrl, dialogURL, dialogWidth, dialogHeight) {
	this.name = name;
	this.title = title;
	this.showTitle = showTitle;
	this.iconIndex = iconIndex;
	this.iconUrl = iconUrl;
	this.dialogURL = dialogURL;
	this.dialogWidth = dialogWidth;
	this.dialogHeight = dialogHeight;
};
EditorDialogCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorDialogCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	DialogUtils.openDialog(this.dialogURL, this.dialogWidth, this.dialogHeight, '', HtmlEditor.getDialogArguments());
};