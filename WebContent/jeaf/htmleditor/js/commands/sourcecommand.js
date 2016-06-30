//源代码命令
EditorSourceCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.showTitle = true;
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
EditorSourceCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return sourceCodeMode ? HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE : HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorSourceCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	editor._switchEditMode(); //切换编辑方式
};