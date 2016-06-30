//PORTAL命令
PortalCommand = function() {
	EditorMenuCommand.call(this, 'portal', 'PORTAL', -1, RequestUtils.getContextPath() + "/portal/icons/portal.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
PortalCommand.prototype = new EditorMenuCommand;
PortalCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
PortalCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'portalPages', title:'插入页面列表', iconIndex:-1},
			{id:'portalPageBody', title:'设为内容区域', iconIndex:-1},
			{id:'addPortalPageButton', title:'设为“添加页面”按钮', iconIndex:-1},
			{id:'editPortalPageButton', title:'设为“页面定制”按钮', iconIndex:-1},
			{id:'addPortletButton', title:'设为“添加PORTLET”按钮', iconIndex:-1},
			{id:'portalStyleDefine', title:'样式配置', iconIndex:-1}];
};
PortalCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("portalPages"==itemId) {
		RecordListCommand.doInsertRecordList('portal', 'portalPages', 'PORTAL页面列表', false, false);
	}
	else if("portalStyleDefine"==itemId) {
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/styleDefine.shtml?templateId=" + document.getElementsByName("id")[0].value, 640, 480, "", HtmlEditor.getDialogArguments());
	}
	else if("portalPageBody"==itemId) {
		var obj = DomUtils.getParentNode(selectedElement, "td,div,span");
		if(!obj) {
			alert("内容区域必须是TD、DIV或者SPAN，请重新选择。");
			return;
		}
		obj.id = "portalPageBody";
		obj.title = "PORTAL内容区域";
		alert("设置完成");
	}
	else {
		var obj = DomUtils.getParentNode(selectedElement, "a,img,button,input,span,div,td");
		if(!obj) {
			alert("位置不正确，请重新选择。");
			return;
		}
		obj.id = itemId;
		if("addPortalPageButton"==itemId) {
			obj.title = "添加页面";
		}
		else  if("editPortalPageButton"==itemId) {
			obj.title = "页面定制";
		}
		else if("addPortletButton"==itemId) {
			obj.title = "添加PORTLET";
		}
		obj.setAttribute("name", obj.id, 0);
		alert("设置完成");
	}
};
HtmlEditor.registerCommand(new PortalCommand());

//PORTLET命令
PortletCommand = function() {
	EditorMenuCommand.call(this, 'portlet', 'PORTLET', -1, RequestUtils.getContextPath() + "/portal/icons/portlet.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
PortletCommand.prototype = new EditorMenuCommand;
PortletCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
PortletCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'portlet', title:'设为PORTLET', iconIndex:-1},
			{id:'portletTitleBar', title:'设为标题栏', iconIndex:-1},
			{id:'portletButtonBar', title:'设为操作按钮栏', iconIndex:-1},
			{id:'portletBody', title:'设为内容区域', iconIndex:-1},
			{id:'portletTitle', title:'插入标题', iconIndex:-1},
			{id:'portletMinimizeButton', title:'设为“最小化”按钮', iconIndex:-1},
			{id:'portletRestoreButton', title:'设为“还原”按钮', iconIndex:-1},
			{id:'portletEditButton', title:'设为“编辑”按钮', iconIndex:-1},
			{id:'portletRefreshButton', title:'设为“刷新”按钮', iconIndex:-1},
			{id:'portletRemoveButton', title:'设为“删除”按钮', iconIndex:-1}];
};
PortletCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if("portlet"==itemId) {
		var portalPageBody = editorDocument.getElementById('portalPageBody');
		if(!portalPageBody) {
			alert("未配置PORTAL内容区域。");
			return;
		}
		var obj = selectedElement;
		if(!obj) {
			alert("PORTLET位置不正确，请重新选择。");
			return;
		}
		while(obj.parentNode!=portalPageBody && obj.tagName!='BODY') {
			obj = obj.parentNode;
		}
		if(obj.parentNode!=portalPageBody) {
			alert("PORTLET位置不正确，PORTLET必须位于PORTAL内容区域，请重新选择。");
			return;
		}
		DialogUtils.openInputDialog('PORTLET名称', [{name:'name', title:'名称', inputMode:'text', defaultValue:(obj.title ? obj.title.substring(obj.title.indexOf(':') + 1) : '')}], 180, 50, '', function(value) {
			editor.saveUndoStep();
			obj.id = obj.name = 'portlet';
			obj.setAttribute("urn", value.name);
			obj.title = 'PORTLET:' + value.name;
		});
	}
	else if(',portletTitleBar,portletButtonBar,portletBody,'.indexOf(',' + itemId + ',')!=-1) {
		var title;
		if('portletTitleBar'==itemId) {
			title = "标题栏";
		}
		else if('portletButtonBar'==itemId) {
			title = "操作按钮栏";
		}
		else if('portletBody'==itemId) {
			title = "内容区域";
		}
		var obj = DomUtils.getParentNode(selectedElement, "td,div,span");
		if(!obj) {
			alert(title + "必须是TD、DIV或者SPAN，请重新选择。");
			return;
		}
		editor.saveUndoStep();
		obj.id = itemId;
		obj.title = "PORTLET" + title;
		alert("设置完成");
	}
	else if("portletTitle"==itemId) {
		var a = DomUtils.getParentNode(selectedElement, "a");
		if(!a) {
			a = DomUtils.createLink(editorWindow, range);
		}
		a.id = "portletTitle";
		a.innerHTML = "&lt;PORTLET标题&gt;";
	}
	else {
		var obj = DomUtils.getParentNode(selectedElement, "img,a,input,td,div,span");
		if(!obj) {
			alert("按钮位置不正确，请重新选择。");
			return;
		}
		obj.id = itemId;
		if("portletMinimizeButton"==itemId) {
			obj.title = "最小化";
		}
		else if("portletRestoreButton"==itemId) {
			obj.title = "还原";
		}
		else if("portletEditButton"==itemId) {
			obj.title = "编辑";
		}
		else if("portletRefreshButton"==itemId) {
			obj.title = "刷新";
		}
		else if("portletRemoveButton"==itemId) {
			obj.title = "删除";
		}
	}
};
HtmlEditor.registerCommand(new PortletCommand());