<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertFormElement">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			if(!dialogArguments.selectedElement || ",INPUT,IMG,A,BUTTON,DIV,".indexOf(',' + dialogArguments.selectedElement.tagName + ',')==-1) {
				return;
			}
			document.getElementById("trStyle").style.display = "none";
			var buttons = '\0' + DropdownField.getListValues('button');
			if(buttons.indexOf('\0' + dialogArguments.selectedElement.title + '|')!=-1) {
				DropdownField.setValue("button", dialogArguments.selectedElement.title);
			}
		};
		function doOk() {
			var buttonTitle = document.getElementsByName("button")[0].value;
			if(buttonTitle=="") {
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = dialogArguments.selectedElement;
			if(!obj || ",INPUT,IMG,A,BUTTON,DIV,".indexOf(',' + obj.tagName + ',')==-1) {
				obj = DomUtils.createElement(dialogArguments.window, dialogArguments.range, 'button');
				obj.innerHTML = buttonTitle;
				var cssName = document.getElementsByName("cssName")[0].value;
				if(cssName!="") {
					obj.className = cssName;
				}
			}
			obj.id = "button";
			obj.title = buttonTitle;
			obj.tabIndex = (document.getElementsByName("formTarget")[0].checked ? 1 : 0);
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">表单按钮：</td>
			<td><ext:field property="button"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">窗口打开方式：</td>
			<td><ext:field property="formTarget"/></td>
		</tr>
		<tr id="trStyle">
			<td nowrap="nowrap">样式名称：</td>
			<td><ext:field property="cssName"/></td>
		</tr>
	</table>
</ext:form>