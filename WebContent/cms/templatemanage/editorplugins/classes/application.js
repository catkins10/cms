//插入应用名称命令
ApplicationTitleCommand = function() {
	this.name = 'applicationTitle';
	this.title = '应用名称';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_application_title.gif";
	this.showTitle = true;
};
ApplicationTitleCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
ApplicationTitleCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	HtmlEditor.editor.saveUndoStep();
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
	if(!obj) {
		obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
		if(!obj) {
			alert('请重新选择插入的位置');
			return;
		}
	}
	obj.innerHTML = "<应用名称>";
	obj.id = "applicationTitle";
};
HtmlEditor.registerCommand(new ApplicationTitleCommand());