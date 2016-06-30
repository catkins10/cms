//插入分页符命令
EditorPageBreakCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
};
EditorPageBreakCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return "" + editorDocument.queryCommandEnabled("InsertParagraph") == "true" ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorPageBreakCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	editor.saveUndoStep();
	DomSelection.pasteHTML(editorWindow, range, '<div style="page-break-after: always"></div>');
};