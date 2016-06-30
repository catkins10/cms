//应用模板作用范围
var ApplicationTemplateRangeCommand = function() {
	this.name = 'applicationTemplateRange';
	this.title = '模板作用范围';
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
ApplicationTemplateRangeCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
ApplicationTemplateRangeCommand.prototype.create = function(toolbarButtonElement) {
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/eai/js/eai.js');
	var id = Math.random();
	toolbarButtonElement.innerHTML = '<table border="0" cellspacing="0" cellpadding="0" style="float:left; margin-right:2px;">' +
									 '	<tr>' +
									 '		<td style="padding-left:5px; padding-right:5px" nowrap>模板作用范围</td>' +
									 '		<td id="' + id + '" width="120px"></td>' +
									 '	</tr>' +
									 '</table>';
	var input = '<input name="' + this.name + '" value="' + document.getElementsByName("applicationTitles")[0].value + '" readonly type="text">';
	new SelectField(input, 'EAISelect(600, 360, true, "applicationNames{id},' + this.name + '{name|应用名称|100%},applicationTitles{name|应用名称|0}", "", ";", "application")', 'field', '', '', '', document.getElementById(id));
};
if(document.getElementsByName("applicationNames")[0]) {
	HtmlEditor.registerCommand(new ApplicationTemplateRangeCommand());
}

//登录模版域名列表
var LoginTemplateHostsCommand = function() {
	this.name = 'loginTemplateHosts';
	this.title = '域名';
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
LoginTemplateHostsCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
LoginTemplateHostsCommand.prototype.create = function(toolbarButtonElement) {
	toolbarButtonElement.innerHTML = '<table border="0" cellspacing="0" cellpadding="0" style="float:left; margin-right:2px;">' +
									 '	<tr>' +
									 '		<td style="padding-left:5px; padding-right:5px" nowrap>域名</td>' +
									 '		<td width="120px"><input value="' + document.getElementsByName("hostNames")[0].value + '" onchange="document.getElementsByName(\'hostNames\')[0].value=this.value;" type="text"></td>' +
									 '	</tr>' +
									 '</table>';
};
if(document.getElementsByName("hostNames")[0]) {
	HtmlEditor.registerCommand(new LoginTemplateHostsCommand());
}