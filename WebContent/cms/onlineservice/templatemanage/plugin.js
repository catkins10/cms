//网上办事位置命令
OnlineServiceLocationCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/onlineservice/templatemanage/insertLocation.shtml";
	EditorDialogCommand.call(this, 'onlineServiceLocation', '网上办事位置', false, -1, RequestUtils.getContextPath() + "/cms/onlineservice/templatemanage/icons/onlineServiceLocation.gif", url, 430, 180);
	this.showTitle = true;
};
OnlineServiceLocationCommand.prototype = new EditorDialogCommand;
OnlineServiceLocationCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
HtmlEditor.registerCommand(new OnlineServiceLocationCommand());

//模板作用范围
var OnlineServiceTemplateRangeCommand = function(name, title) {
	this.name = name;
	this.title = title;
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
OnlineServiceTemplateRangeCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
OnlineServiceTemplateRangeCommand.prototype.create = function(toolbarButtonElement) {
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/cms/onlineservice/js/onlineservice.js');
	var id = Math.random();
	toolbarButtonElement.innerHTML = '<table border="0" cellspacing="0" cellpadding="0" style="float:left; margin-right:2px;">' +
									 '	<tr>' +
									 '		<td style="padding-left:5px; padding-right:5px" nowrap>' + this.title + '</td>' +
									 '		<td id="' + id + '" width="120px"></td>' +
									 '	</tr>' +
									 '</table>';
	var input = '<input name="' + this.name + '" value="' + document.getElementsByName("onlineServiceSelectDirectory"==this.name ? "directoryName" : "itemTypes")[0].value + '"' + ('onlineServiceSelectDirectory'==this.name ? ' readonly' : '') + ' type="text">';
	var script;
	if("onlineServiceSelectDirectory"==this.name) {
		script = 'selectOnlineServiceDirectory(600, 360, false, "directoryId{id},directoryName{name},' + this.name + '{name}")';
	}
	else {
		script = 'selectItemType("事项类型", 600, 360, true, "itemTypes{title},itemTypes{title|类型|100%},' + this.name + '{title|类型|0}", "", ",")';
	}
	new SelectField(input, script, 'field', '', '', '', document.getElementById(id));
};
if(document.getElementsByName("directoryName")[0]) {
	HtmlEditor.registerCommand(new OnlineServiceTemplateRangeCommand('onlineServiceSelectDirectory', '模板作用范围'));
}
if(document.getElementsByName("itemTypes")[0]) {
	HtmlEditor.registerCommand(new OnlineServiceTemplateRangeCommand('onlineServiceSelectItemType', '事项类型'));
}