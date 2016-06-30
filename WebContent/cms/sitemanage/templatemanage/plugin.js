//模板作用范围
var SiteTemplateRangeCommand = function() {
	this.name = 'siteTemplateRange';
	this.title = '模板作用范围';
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
SiteTemplateRangeCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
SiteTemplateRangeCommand.prototype.create = function(toolbarButtonElement) {
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/cms/sitemanage/js/site.js');
	var id = Math.random();
	toolbarButtonElement.innerHTML = '<table border="0" cellspacing="0" cellpadding="0" style="float:left; margin-right:2px;">' +
									 '	<tr>' +
									 '		<td style="padding-left:5px; padding-right:5px" nowrap>' + this.title + '</td>' +
									 '		<td id="' + id + '" width="120px"></td>' +
									 '		<td style="padding-left:2px;" nowrap><input onclick="document.getElementsByName(\'matchByName\')[0].value=(this.checked ? 1 : 0);" type="checkbox" class="checkbox" style="border-style:none;"' + (document.getElementsByName("matchByName")[0].value=="1" ? ' checked="true"' : '') + '">按栏目名称</td>' +
									 '	</tr>' +
									 '</table>';
	var input = '<input name="' + this.name + '" value="' + document.getElementsByName("columnNames")[0].value + '" readonly type="text">';
	var script = 'selectSite(600, 360, true, "columnIds{id},columnNames{name|栏目|100%},' + this.name + '{name|栏目|0}", "", "", ",", "column,viewReference", "' + document.getElementsByName("siteId")[0].value + '")';
	new SelectField(input, script, 'field', '', '', '', document.getElementById(id));
};
if(document.getElementsByName("columnIds")[0]) {
	HtmlEditor.registerCommand(new SiteTemplateRangeCommand());
}