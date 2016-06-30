//编辑样式表
var EditCssCommand = function() {
	this.name = 'editCss';
	this.title = '编辑CSS';
	this.iconUrl = RequestUtils.getContextPath() + "/cms/templatemanage/icons/cms_edit_css.gif";
	this.showTitle = true;
};
EditCssCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
EditCssCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	HtmlEditor.editorDocument = editorDocument;
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/selectTemplateAttachment.shtml" +
			  "?id=" + document.getElementsByName("id")[0].value +
			  "&attachmentSelector.scriptRunAfterSelect=" + StringUtils.utf8Encode("EditCssCommand.editCss('{fileName}', '{URL}')") +
			  "&attachmentSelector.type=css";
	DialogUtils.openDialog(url, 600, 360);
};
EditCssCommand.editCss = function(fileName, cssUrl) {
	var url = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/templateCssFile.shtml" +
			  "?id=" + document.getElementsByName("id")[0].value +
			  "&fileName=" + StringUtils.utf8Encode(fileName) +
			  "&script=" + StringUtils.utf8Encode("EditCssCommand.afterCssModified('" + cssUrl + "')");
	DialogUtils.openDialog(url, 760, 480);
};
EditCssCommand.afterCssModified = function(cssUrl) {
	var links = HtmlEditor.editorDocument.getElementsByTagName("link");
	for(var i=0; i<(!links ? 0 : links.length); i++) {
		var href = links[i].getAttribute('href', 2);
		if(href && href.substring(0, cssUrl.length)==cssUrl) {
			links[i].href = cssUrl + "?seq=" + ("" + Math.random()).substring(2);
		}
	}
};
HtmlEditor.registerCommand(new EditCssCommand());