//信息公开目录位置命令
InfoDirectoryLocationCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/infopublic/templatemanage/insertDirectoryLocation.shtml";
	EditorDialogCommand.call(this, 'infoDirectoryLocation', '目录位置', false, -1, RequestUtils.getContextPath() + "/cms/infopublic/templatemanage/icons/info_directory_location.gif", url, 430, 180);
	this.showTitle = true;
};
InfoDirectoryLocationCommand.prototype = new EditorDialogCommand;
InfoDirectoryLocationCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
HtmlEditor.registerCommand(new InfoDirectoryLocationCommand());