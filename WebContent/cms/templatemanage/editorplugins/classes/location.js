//站点/栏目位置命令
LocationCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/sitemanage/templatemanage/insertSiteLocation.shtml";
	EditorDialogCommand.call(this, 'location', '栏目位置', false, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_location.gif", url, 430, 180);
	this.showTitle = true;
};
LocationCommand.prototype = new EditorDialogCommand;
LocationCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
HtmlEditor.registerCommand(new LocationCommand());