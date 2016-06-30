//撤销、重做命令
EditorUndoRedoCommand = function(name, title, iconIndex) {
	this.name = name;
	this.title = title;
	this.iconIndex = iconIndex;
};
EditorUndoRedoCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	if(this.name=="undo") {
		return editor._isAllowUndo() ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
	}
	else {
		return editor._isAllowRedo() ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
	}
};
EditorUndoRedoCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	if(this.name=="undo") {
		editor.undo();
	}
	else {
		editor.redo();
	}
};