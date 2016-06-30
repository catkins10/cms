//图片命令
EditorImageCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, true, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/image.shtml', 720, 600);
};
EditorImageCommand.prototype = new EditorDialogCommand;
EditorImageCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return "" + editorDocument.queryCommandEnabled("InsertImage") == "true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};