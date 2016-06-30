<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/find">
<script>
	var found = false;
	window.onload = function() {
		var dialogArguments = DialogUtils.getDialogArguments();
		var text = "";
		if(dialogArguments.range && dialogArguments.range.findText) { //IE
			text = dialogArguments.range.text;
		}
		else if(dialogArguments.range && dialogArguments.range.toString) {
			text = dialogArguments.range.toString();
		}
		if(text=="" && dialogArguments.editor.findText) {
			text = dialogArguments.editor.findText;
		}
		document.getElementsByName("find")[0].value = text;
		dialogArguments.editor.findText = text;
	};
	function saveFindText() {
		var dialogArguments = DialogUtils.getDialogArguments();
		dialogArguments.editor.findText = document.getElementsByName("find")[0].value;
	};
	function findNext(alertDisable) {
		var text = document.getElementsByName("find")[0].value;
		if(text=="") {
			return;
		}
		var matchCase = document.getElementsByName("matchCase")[0].checked;
		var down = document.getElementsByName("direction")[1].checked;
		var dialogArguments = DialogUtils.getDialogArguments();
		try {
			dialogArguments.range.collapse(!down);
		}
		catch(e) {
		
		}
		DomSelection.selectRange(dialogArguments.window, dialogArguments.range);
		if(dialogArguments.range && dialogArguments.range.findText) { //IE
			found = dialogArguments.range.findText(text, down ? 1000000 : -1000000 , matchCase ? 4 : 0);
			if(found) {
				DomSelection.selectRange(dialogArguments.window, dialogArguments.range);
			}
		}
		else {
			found = dialogArguments.window.find(text, matchCase, !down);
			dialogArguments.range = dialogArguments.editor.getRange();
		}
		if(!found && !alertDisable) {
			alert("找不到“" + text + "”");
		}
	}
	function replaceText(saveUndoStepDisable) {
		if(!found) {
			return;
		}
		var dialogArguments = DialogUtils.getDialogArguments();
		if(!saveUndoStepDisable) {
			dialogArguments.editor.saveUndoStep();
		}
		var newText = document.getElementsByName('replace')[0].value;
		DomSelection.pasteHTML(dialogArguments.window, dialogArguments.range, StringUtils.generateHtmlContent(newText));
		dialogArguments.range = dialogArguments.editor.getRange();
		if(dialogArguments.range.moveStart) { //IE
			dialogArguments.range.moveStart('character', -newText.length);
		}
		else {
			dialogArguments.range.setStart(dialogArguments.range.startContainer, dialogArguments.range.startOffset - newText.length);
		}
		findNext(saveUndoStepDisable);
	}
	function replaceAll() {
		if(!found) {
			findNext(true);
		}
		if(!found) {
			return;
		}
		var dialogArguments = DialogUtils.getDialogArguments();
		dialogArguments.editor.saveUndoStep();
		while(found) {
			replaceText(true);
		}
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<tr>
		<td nowrap="nowrap" align="right">查找内容：</td>
		<td width="100%"><ext:field property="find" onchange="saveFindText()"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">替换为：</td>
		<td><ext:field property="replace"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">查找方向：</td>
		<td><ext:field property="direction"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right"></td>
		<td><ext:field property="matchCase"/></td>
	</tr>
</table>
</ext:form>