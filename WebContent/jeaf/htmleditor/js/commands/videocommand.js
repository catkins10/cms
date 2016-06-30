//视频命令
EditorVideoCommand = function(name, title, iconIndex) {
	EditorDialogCommand.call(this, name, title, true, iconIndex, '', RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/video.shtml', 760, 560);
};
EditorVideoCommand.prototype = new EditorDialogCommand;
EditorVideoCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};