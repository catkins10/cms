//日期命令
DateCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertDate.shtml";
	EditorDialogCommand.call(this, 'date', '日期', false, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_date.gif", url, 470, 200);
	this.showTitle = true;
};
DateCommand.prototype = new EditorDialogCommand;
DateCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
HtmlEditor.registerCommand(new DateCommand());