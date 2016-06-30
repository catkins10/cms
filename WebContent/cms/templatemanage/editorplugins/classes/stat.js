//插入访问次数命令
CounterCommand = function() {
	this.name = 'counter';
	this.title = '访问次数';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_counter.gif";
	this.showTitle = true;
};
CounterCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
CounterCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	HtmlEditor.editor.saveUndoStep();
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
	if(!obj) {
		obj = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
		if(!obj) {
			alert('请重新选择插入的位置');
			return;
		}
	}
	obj.innerHTML = "<访问次数>";
	obj.id = "counter";
};
HtmlEditor.registerCommand(new CounterCommand());