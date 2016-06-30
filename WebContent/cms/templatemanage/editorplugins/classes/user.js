//RSS命令
UserCommand = function() {
	EditorMenuCommand.call(this, 'user', '用户', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_login_user_action.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
UserCommand.prototype = new EditorMenuCommand;
UserCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
UserCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'insertUserName', title:'插入用户名', iconIndex:-1},
			{id:'insertPortrait', title:'插入头像', iconIndex:-1},
			{id:'insertPoint', title:'插入积分', iconIndex:-1},
			{id:'insertLoginName', title:'插入登录用户名', iconIndex:-1},
			{id:'insertDepartment', title:'插入用户所在部门', iconIndex:-1},
			{id:'insertLogoutUrl', title:'插入注销链接', iconIndex:-1},
			{id:'insertChangePasswordUrl', title:'插入修改密码链接', iconIndex:-1},
			{id:'insertPersonalDataUrl', title:'插入修改个人资料链接', iconIndex:-1},
			{id:'insertTemplateUrl', title:'插入模板配置链接', iconIndex:-1}];
};
UserCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	HtmlEditor.editor.saveUndoStep();
	var a = DomUtils.getParentNode(HtmlEditor.selectedElement, 'a');
	if(!a) {
		a = DomUtils.createLink(HtmlEditor.editorWindow, HtmlEditor.range);
	}
	a.id = "loginUserAction";
	if(itemId=="insertUserName") { //插入用户名
		a.setAttribute("urn", "用户名");
		a.innerHTML = "<用户名>";
	}
	else if(itemId=="insertPortrait") { //插入头像
		a.setAttribute("urn", "头像");
		if(a.innerHTML=="") {
			a.innerHTML = "<头像>";
		}
	}
	else if(itemId=="insertPoint") { //插入积分
		a.setAttribute("urn", "积分");
		a.innerHTML = "<积分>";
	}
	else if(itemId=="insertLoginName") { //插入登录用户名
		a.setAttribute("urn", "登录用户名");
		a.innerHTML = "<登录用户名>";
	}
	else if(itemId=="insertDepartment") { //插入用户所在部门
		a.setAttribute("urn", "用户所在部门");
		a.innerHTML = "<用户所在部门>";
	}
	else if(itemId=="insertLogoutUrl") { //插入注销链接
		a.setAttribute("urn", "注销");
	}
	else if(itemId=="insertChangePasswordUrl") { //插入修改密码链接
		a.setAttribute("urn", "修改密码");
	}
	else if(itemId=="insertPersonalDataUrl") { //插入修改个人资料链接
		a.setAttribute("urn", "修改个人资料");
	}
	else if(itemId=="insertTemplateUrl") { //插入模板配置链接
		a.setAttribute("urn", "模板配置");
	}
	if(a.innerHTML=='') {
		a.innerHTML = a.getAttribute("urn");
	}
};
HtmlEditor.registerCommand(new UserCommand());