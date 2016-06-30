//预置页面命令
SubPageCommand = function() {
	var themeType = document.getElementsByName("themeType")[0];
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertSubPage.shtml" +
			  "?applicationName=" + document.getElementsByName("applicationName")[0].value +
			  "&pageName=" + document.getElementsByName("pageName")[0].value +
			  (themeType ? "&themeType=" + themeType.value : "");
	EditorDialogCommand.call(this, 'subPage', '预置页面', false, -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_sub_page.gif", url, 550, 360);
	this.showTitle = true;
};
SubPageCommand.prototype = new EditorDialogCommand;
SubPageCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
if(document.getElementsByName("pageName")[0]) {
	HtmlEditor.registerCommand(new SubPageCommand());
}