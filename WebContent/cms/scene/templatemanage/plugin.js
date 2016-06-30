//场景位置命令
SceneLocationCommand = function() {
	var url = RequestUtils.getContextPath() + "/cms/scene/templatemanage/insertSceneLocation.shtml";
	EditorDialogCommand.call(this, 'sceneLocation', '场景位置', false, -1, RequestUtils.getContextPath() + "/cms/scene/templatemanage/icons/sceneLocation.gif", url, 430, 180);
	this.showTitle = true;
};
SceneLocationCommand.prototype = new EditorDialogCommand;
SceneLocationCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
HtmlEditor.registerCommand(new SceneLocationCommand());

//模板作用范围
var SceneTemplateRangeCommand = function(name, title, selectNodeTypes) {
	this.name = name;
	this.title = title;
	this.selectNodeTypes = selectNodeTypes;
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
SceneTemplateRangeCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
SceneTemplateRangeCommand.prototype.create = function(toolbarButtonElement) {
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/cms/scene/js/scene.js');
	var id = Math.random();
	toolbarButtonElement.innerHTML = '<table border="0" cellspacing="0" cellpadding="0" style="float:left; margin-right:2px;">' +
									 '	<tr>' +
									 '		<td style="padding-left:5px; padding-right:5px" nowrap>' + this.title + '</td>' +
									 '		<td id="' + id + '" width="120px"></td>' +
									 '	</tr>' +
									 '</table>';
	var input = '<input name="' + this.name + '" value="' + document.getElementsByName("directoryName")[0].value + '" readonly type="text">';
	var script = 'selectScene(600, 360, false, "directoryId{id},directoryName{name},' + this.name + '{name}", "", "", "", "' + this.selectNodeTypes + '")';
	new SelectField(input, script, 'field', '', '', '', document.getElementById(id));
};
if(document.getElementsByName("directoryName")[0]) {
	HtmlEditor.registerCommand(new SceneTemplateRangeCommand('selectScene', '模板作用范围', "scene,service"));
	HtmlEditor.registerCommand(new SceneTemplateRangeCommand('selectSceneContent', '模板作用范围', "scene,service,content"));
}