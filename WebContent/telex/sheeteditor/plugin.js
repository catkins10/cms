//插入字段
TelexFieldCommand = function() {
	EditorMenuCommand.call(this, 'telexField', '字段', -1, RequestUtils.getContextPath() + "/telex/images/field.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
TelexFieldCommand.prototype = new EditorMenuCommand;
TelexFieldCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
TelexFieldCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	this.fields = [['标题', 'subject'], ['内容摘要', 'summary'], ['原电号', 'sn'], ['流水号', 'sequence'], ['部委号', 'unitCode'],
			  ['发电类型', 'category'], ['密级', 'securityLevel'], ['等级', 'telegramLevel'], ['页数', 'pages'],
			  ['份数', 'telegramNumber'], ['总页数', 'totalPages'], ['发电时间', 'sendTime', 'date'],
			  ['发电单位', 'fromUnitNames'], ['来电台家', 'station'], ['收电单位', 'toUnitNames'], ['接收时间', 'receiveTime', 'date'],
			  ['接收人', 'receiverName'], ['备注', 'remark'], ['秘书长批示意见', 'opinion'], ['市委市府领导批示意见', 'opinion']];
	var items = [];
	for(var i=0; i<this.fields.length; i++) {
		items.push({id:i, title:this.fields[i][0], iconIndex:-1});
	}
	return items;
};
TelexFieldCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	editor.saveUndoStep();
	var a = DomUtils.getParentNode(selectedElement, 'a');
	if(!a) {
		a = DomUtils.createLink(editorWindow, range);
	}
	var field = this.fields[Number(itemId)];
	a.id = field[1];
	if(field.length>2) {
		a.setAttribute("urn", field[2]); //字段类型
	}
	a.innerHTML = "<" + field[0] + ">";
};
HtmlEditor.registerCommand(new TelexFieldCommand());