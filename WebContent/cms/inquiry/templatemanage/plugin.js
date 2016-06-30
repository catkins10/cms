//模板名称
var InquiryTemplateRangeCommand = function() {
	this.name = 'inquiryTemplateRange';
	this.title = '模板隶属目录';
	this.enabledInSourceCodeMode = true; //在源码模式下有效
};
InquiryTemplateRangeCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL;
};
InquiryTemplateRangeCommand.prototype.create = function(toolbarButtonElement) {
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + '/cms/inquiry/js/inquiry.js');
	var id = Math.random();
	toolbarButtonElement.innerHTML = '<table border="0" cellspacing="0" cellpadding="0" style="float:left; margin-right:2px;">' +
									 '	<tr>' +
									 '		<td style="padding-left:5px; padding-right:5px" nowrap>模板作用范围</td>' +
									 '		<td id="' + id + '" width="120px"></td>' +
									 '	</tr>' +
									 '</table>';
	var input = '<input name="selectSubject" value="' + document.getElementsByName("subject")[0].value + '" readonly type="text">';
	new SelectField(input, 'selectInquirySubject(600, 360, false, "subjectId{id},subject{name},selectSubject{name}")', 'field', '', '', '', document.getElementById(id));
};
if(document.getElementsByName("subjectId")[0]) {
	HtmlEditor.registerCommand(new InquiryTemplateRangeCommand());
}