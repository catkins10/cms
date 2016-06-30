//查找命令
EditorFindCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, false, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/find.shtml', 430, 200);
};
EditorFindCommand.prototype = new EditorDialogCommand;
EditorFindCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};