<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertEvaluationLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj) {
				setLinkOpenMode(obj.target); //解析打开方式
			}
		}
		function doOk() {
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
			}
			if(obj.innerHTML=="") {
				obj.innerHTML = '部门考核';
			}
			obj.target = getLinkOpenMode();
			obj.id = "departmentEvaluationLink";
			obj.removeAttribute("urn");
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="360px" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
		<tr valign="bottom">
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>