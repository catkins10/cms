//插入链接命令
var LinkCommand = function() {
	this.name = 'templateLink';
	this.title = '链接';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_link.gif";
	this.showTitle = true;
};
LinkCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
LinkCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectSiteLink.shtml";
	var applicationName = document.getElementsByName("applicationName")[0];
	var pageName = document.getElementsByName("pageName")[0];
	url += "?currentApplicationName=" + (applicationName ?  applicationName.value : "");
	url += "&currentPageName=" + (pageName ?  pageName.value : "");
	url += "&script=" + StringUtils.utf8Encode("LinkCommand.insertLink('{id}', '{name}', '{dialogURL}')");
	DialogUtils.openDialog(url, 550, 360);
};
LinkCommand.insertLink = function(id, title, dialogURL, url, applicationTitle) {
	var themeType = document.getElementsByName("themeType")[0];
	var url = RequestUtils.getContextPath() + dialogURL +
			 (dialogURL.indexOf('?')==-1 ? '?' : '&') + "themeType=" + (themeType ?  themeType.value : "");
	DialogUtils.openDialog(url, 500, 180, '', HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new LinkCommand());