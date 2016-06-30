//字段命令
AdvertCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/advert/templatemanage/insertAdvertSpace.shtml";
	EditorDialogCommand.call(this, 'advert', '广告位', false, -1, RequestUtils.getContextPath() + "/cms/advert/icons/advert.gif", url, 500, 300);
	this.showTitle = true;
};
AdvertCommand.prototype = new EditorDialogCommand;
AdvertCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
HtmlEditor.registerCommand(new AdvertCommand());