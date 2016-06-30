//导航栏命令
NavigatorCommand = function() {
	EditorMenuCommand.call(this, 'navigator', '导航栏', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_navigator.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
NavigatorCommand.prototype = new EditorMenuCommand;
NavigatorCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
NavigatorCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'insertNavigator', title:'创建导航栏', iconIndex:-1},
			{id:'insertNavigatorLinkItem', title:'设为导航栏链接', iconIndex:-1},
			{id:'insertNavigatorMenuItem', title:'设为导航栏菜单', iconIndex:-1},
			{id:'insertNavigatorItemList', title:'插入导航项目列表', iconIndex:-1}];
};
NavigatorCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	var themeType = document.getElementsByName("themeType")[0];
	if(itemId=="insertNavigator") { //创建导航栏
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,ul");
		if(element==null) {
			alert("导航栏位置不正确，请重新选择导航栏位置。");
		}
		else {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertNavigator.shtml", 705, 390, "", HtmlEditor.getDialogArguments());
		}
	}
	else if(itemId=="insertNavigatorLinkItem") { //设为导航栏链接
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,li");
		if(element==null || element.id=="navigator") {
			alert("链接位置不正确，请重新选择链接位置。");
		}
		else if(this.getNavigator(element)==null) {
			alert("链接位置不正确，请重新选择链接位置。");
		}
		else {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertNavigatorLinkItem.shtml", 562, 230, "", HtmlEditor.getDialogArguments());
		}
	}
	else if(itemId=="insertNavigatorMenuItem") { //设为导航栏菜单
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,li");
		if(element==null || element.id=="navigator") {
			alert("菜单位置不正确，请重新选择菜单位置。");
		}
		else if(this.getNavigator(element)==null) {
			alert("链接位置不正确，请重新选择链接位置。");
		}
		else {
			DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertNavigatorMenuItem.shtml", 660, 380, "", HtmlEditor.getDialogArguments());
		}
	}
	else if(itemId=="insertNavigatorItemList") { //插入导航项目列表
		var element = DomUtils.getParentNode(HtmlEditor.selectedElement, "div,td,span,ul");
		if(element==null) {
			alert("位置不正确，请重新选择列表插入位置。");
		}
		else if(element.id!="navigator" && this.getNavigator(element)==null) {
			alert("位置不正确，请重新选择列表插入位置。");
		}
		else {
			//选择有内嵌记录列表的记录列表
			var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectView.shtml";
			url += "?navigatorSupportOnly=true";
			url += "&currentApplicationName=" + document.getElementsByName("applicationName")[0].value;
			url += "&currentPageName=" + document.getElementsByName("pageName")[0].value;
			url += "&script=" + StringUtils.utf8Encode("NavigatorCommand.insertNavigatorItemList('{id}'.split('__')[0], '{id}'.split('__')[1], '{name}')");
			DialogUtils.openDialog(url, 550, 360, "", HtmlEditor.getDialogArguments());
		}
	}
};
NavigatorCommand.prototype.getNavigator = function(element) { //获取元素所在的导航栏
	while(element.tagName.toLowerCase()!="body") {
		if(element.id=="navigator") {
			return element;
		}
		element = element.parentElement;
	}
	return null;
};
NavigatorCommand.insertNavigatorItemList = function(applicationName, recordListName, title) {
	var themeType = document.getElementsByName("themeType")[0];
	var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertNavigatorItemList.shtml" +
					"?recordListName=" + recordListName +
					(applicationName!="" ? "&applicationName=" + applicationName : "") +
					"&themeType=" + (themeType ?  themeType.value : "");
	DialogUtils.openDialog(dialogUrl, 696, 300, "", HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new NavigatorCommand());