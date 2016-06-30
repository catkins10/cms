//FLASH命令
EditorFlashCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, false, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/flash.shtml', 430, 350);
};
EditorFlashCommand.prototype = new EditorDialogCommand;
EditorFlashCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return "" + editorDocument.queryCommandEnabled("InsertImage") == "true" || (selectedElement && selectedElement.tagName=="EMBED") ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};