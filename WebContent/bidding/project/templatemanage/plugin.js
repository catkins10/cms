//设置模板作用范围命令
BidingTamplateRangeCommand = function() {
	var url = RequestUtils.getContextPath() + "/bidding/project/templatemanage/setTemplateRange.shtml";
	EditorDialogCommand.call(this, 'bidingTamplateRange', '模板作用范围', false, -1, RequestUtils.getContextPath() + "/bidding/project/templatemanage/icons/project_template_range.gif", url, 550, 280);
	this.showTitle = true;
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
BidingTamplateRangeCommand.prototype = new EditorDialogCommand;
BidingTamplateRangeCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
HtmlEditor.registerCommand(new BidingTamplateRangeCommand());