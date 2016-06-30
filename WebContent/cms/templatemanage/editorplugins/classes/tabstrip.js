//选项卡命令
TabstripCommand = function() {
	EditorMenuCommand.call(this, 'tabstrip', '选项卡', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_tabstrip.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
TabstripCommand.prototype = new EditorMenuCommand;
TabstripCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
TabstripCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'insertTabstripBody', title:'插入标签页', iconIndex:-1},
			{id:'insertTabstripButton', title:'设置为选项', iconIndex:-1},
			{id:'insertTabstripButonList', title:'插入选项列表', iconIndex:-1},
			{id:'insertTabstripMoreLink', title:'插入"更多"链接', iconIndex:-1}];
};
TabstripCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	var themeType = document.getElementsByName("themeType")[0];
	if(itemId=="insertTabstripBody") { //插入标签页
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertTabstripBody.shtml", 470, 300, "", HtmlEditor.getDialogArguments());	
	}
	else if(itemId=="insertTabstripButton") { //设置为选项
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,li,a,img");
		if(element==null) {
			alert("TAB选项位置不正确，请重新选择TAB选项位置。");
		}
		else {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertTabstripButton.shtml", 600, 380, "", HtmlEditor.getDialogArguments());
		}
	}
	else if(itemId=="insertTabstripButonList") { //插入选项列表
		//选择有内嵌记录列表的记录列表
		var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectView.shtml" +
				  "?conatinsEmbedViewOnly=true" +
				  "&currentApplicationName=" + document.getElementsByName("applicationName")[0].value +
				  "&currentPageName=" + document.getElementsByName("pageName")[0].value +
				  "&script=" + StringUtils.utf8Encode("TabstripCommand.insertTabstripButonList('{id}'.split('__')[0], '{id}'.split('__')[1], '{name}')");
		DialogUtils.openDialog(url, 550, 360);
	}
	else if(itemId=="insertTabstripMoreLink") { //插入"更多"链接
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertTabstripMoreLink.shtml", 430, 180, "", HtmlEditor.getDialogArguments());
	}
};
TabstripCommand.insertTabstripButonList = function(applicationName, recordListName, title) {
	var themeType = document.getElementsByName("themeType")[0];
	var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertTabstripButtonList.shtml" +
					"?recordListName=" + recordListName +
					(applicationName!="" ? "&applicationName=" + applicationName : "") +
					"&themeType=" + (themeType ?  themeType.value : "");
	DialogUtils.openDialog(dialogUrl, 720, 400, "", HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new TabstripCommand());