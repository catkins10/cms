//链接命令
EditorLinkCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, true, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/link.shtml', 430, 230);
};
EditorLinkCommand.prototype = new EditorDialogCommand;
EditorLinkCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	var ret = range && range.queryCommandEnabled ? range.queryCommandEnabled("createLink") : editorDocument.queryCommandEnabled("createLink");
	return "" + ret == "true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};