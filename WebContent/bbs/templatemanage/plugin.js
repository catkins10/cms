//模板隶属目录
var BbsDirectoryBindCommand = function() {
	this.name = 'bbsDirectoryBind';
	this.title = '模板隶属目录';
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
BbsDirectoryBindCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
BbsDirectoryBindCommand.prototype.create = function(toolbarButtonElement) {
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/bbs/forum/js/forum.js');
	var id = Math.random();
	toolbarButtonElement.innerHTML = '<table border="0" cellspacing="0" cellpadding="0" style="float:left; margin-right:2px;">' +
									 '	<tr>' +
									 '		<td style="padding-left:5px; padding-right:5px" nowrap>模板隶属目录</td>' +
									 '		<td id="' + id + '" width="100px"></td>' +
									 '	</tr>' +
									 '</table>';
	var input = '<input name="selectBbsDirectory" value="' + document.getElementsByName("directoryName")[0].value + '" readonly type="text">';
	new SelectField(input, 'selectDirectory(600, 360, false, "directoryId{id},selectBbsDirectory{name},directoryName{name}")', 'field', '', '', '', document.getElementById(id));
};
if(document.getElementsByName("directoryName")[0]) {
	HtmlEditor.registerCommand(new BbsDirectoryBindCommand());
}