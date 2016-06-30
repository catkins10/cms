//智能互动命令
RobotCommand = function() {
	EditorMenuCommand.call(this, 'robot', '智能互动', -1, RequestUtils.getContextPath() + "/cms/onlineservice/faq/templatemanage/icons/robot.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
RobotCommand.prototype = new EditorMenuCommand;
RobotCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
RobotCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'robotTalkRecord', title:'设为机器发言记录', iconIndex:-1},
			{id:'questionerTalkRecord', title:'设为用户发言记录', iconIndex:-1},
			{id:'talkContent', title:'插入发言内容', iconIndex:-1},
			{id:'talkTime', title:'插入发言时间', iconIndex:-1},
			{id:'promptArea', title:'设为提示信息区域', iconIndex:-1},
			{id:'greeting', title:'设置问候语', iconIndex:-1}];
};
RobotCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("robotTalkRecord,questionerTalkRecord".indexOf(itemId)!=-1) {
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "td,div");
		if(!obj) {
			alert("发言记录必须是TD、DIV，请重新选择。");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		obj.id = "设为机器发言记录"==itemId ? "robotTalkRecord" : "questionerTalkRecord";
		obj.title = itemId=="robotTalkRecord" ? "机器发言记录" : "用户发言记录";
		alert("设置完成");
	}
	if("promptArea"==itemId) {
		var obj = DomUtils.getParentNode(HtmlEditor.selectedElement, "table,div");
		if(!obj) {
			alert("发言记录必须是TABLE、DIV，请重新选择。");
			return;
		}
		HtmlEditor.editor.saveUndoStep();
		obj.id = itemId;
		obj.title = "提示信息区域";
		alert("设置完成");
	}
	else if("greeting"==itemId) {
		HtmlEditor.editor.saveUndoStep();
		var meta = DomUtils.getMeta(HtmlEditor.editorDocument, 'greeting', true);
		DialogUtils.openInputDialog('问候语', [{name:'greeting', title:'问候语', inputMode:'text', defaultValue:(meta ? StringUtils.utf8Decode(meta.content) : '')}], 430, 200, '', function(value) {
			meta.content = StringUtils.utf8Encode(value.greeting);
		});
	}
	else if("talkContent,talkTime".indexOf(itemId)!=-1) {
		HtmlEditor.editor.saveUndoStep();
		var a = DomUtils.getParentNode(HtmlEditor.selectedElement, "a");
		if(!a) {
			a = DomUtils.createElement(HtmlEditor.editorWindow, HtmlEditor.range, 'a') ;
			if(!a) {
				alert("请重新选择插入位置");
				return;
			}
		}
		a.innerHTML = "<" + (itemId=="talkContent" ? "发言内容" : "发言时间") + ">";
		a.id = itemId;
	}
};
HtmlEditor.registerCommand(new RobotCommand());