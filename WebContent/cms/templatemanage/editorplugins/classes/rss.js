//RSS命令
RssCommand = function() {
	EditorMenuCommand.call(this, 'rss', 'RSS', -1, RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_insert_rss.gif");
	this.showDropButton = false;
	this.showTitle = true;
};
RssCommand.prototype = new EditorMenuCommand;
RssCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
RssCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	return [{id:'insertRssSubscribeLink', title:'插入RSS订阅链接', iconIndex:-1},
			{id:'insertRssChannel', title:'插入RSS频道', iconIndex:-1}];
};
RssCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	var themeType = document.getElementsByName("themeType")[0];
	if(itemId=="insertRssSubscribeLink") { //插入RSS订阅链接
		DialogUtils.openDialog(RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRssSubscribeLink.shtml?themeType=" + (themeType ?  themeType.value : ""), 455, 180, "", HtmlEditor.getDialogArguments());
	}
	else if(itemId=="insertRssChannel") { //插入RSS频道
		var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectView.shtml";
		url += "?rssChannelSupportOnly=true";
		url += "&script=" + StringUtils.utf8Encode("RssCommand.InsertRSSChannel('{id}'.split('__')[0], '{id}'.split('__')[1], '{templateExtendURL}', '{name}')");
		DialogUtils.openDialog(url, 550, 360);
	}
};
RssCommand.InsertRSSChannel = function(applicationName, channel, templateExtendURL, title) {
	var themeType = document.getElementsByName("themeType")[0];
	var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRssChannel.shtml" +
					"?channel=" + channel +
					"&applicationName=" + applicationName +
					"&title=" + title +
					"&themeType=" + (themeType ?  themeType.value : "") +
					(templateExtendURL!="" && templateExtendURL!="null" ? "&templateExtendURL=" + StringUtils.utf8Encode(templateExtendURL) : "");
	DialogUtils.openDialog(dialogUrl, 680, 220, "", HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new RssCommand());