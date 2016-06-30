//全屏命令
EditorFitWindowCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
EditorFitWindowCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return editor.fullScreenMode ? HtmlEditor.TOOLBAR_BUTTON_STATE_ACTIVE : HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditorFitWindowCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	editor._switchFullScreen(); //切换全屏编辑方式
};