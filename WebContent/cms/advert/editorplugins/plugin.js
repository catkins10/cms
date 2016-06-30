//广告命令
AdvertActionCommand = function() {
	EditorMenuCommand.call(this, 'advertAction', '广告', -1, RequestUtils.getContextPath() + "/cms/advert/icons/advert.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
AdvertActionCommand.prototype = new EditorMenuCommand;
AdvertActionCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
AdvertActionCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'insertCloseButton', title:'插入关闭广告按钮', iconIndex:-1},
			{id:'insertMinimizeButton', title:'插入最小化广告按钮', iconIndex:-1}];
};
AdvertActionCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "td,span,div,a,img");
	if(!obj) {
		alert('请重新选择插入的位置');
		return;
	}
	HtmlEditor.editor.saveUndoStep();
	if("insertCloseButton"==itemId) {
		obj.title = "关闭";
		obj.id = "closeAdvert";
	}
	else if("insertMinimizeButton"==itemId) {
		obj.title = "最小化";
		obj.id = "minimizeAdvert";
	}
	alert("操作完成");
};
HtmlEditor.registerCommand(new AdvertActionCommand());