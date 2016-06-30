//字段命令
FieldCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertField.shtml" +
			  "?applicationName=" + document.getElementsByName("applicationName")[0].value +
			  "&pageName=" + document.getElementsByName("pageName")[0].value;
	EditorDialogCommand.call(this, 'field', '字段', false, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_field.gif", url, 550, 360);
	this.showTitle = true;
};
FieldCommand.prototype = new EditorDialogCommand;
FieldCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
if(document.getElementsByName("pageName")[0]) {
	HtmlEditor.registerCommand(new FieldCommand());
}