<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertInteractiveForm">
	<script>
	var dialogArguments = DialogUtils.getDialogArguments();
	window.onload = function() {
		//从form.action解析目录
		var form = DomUtils.getParentNode(dialogArguments.selectedElement, 'form');
		if(form) {
			document.getElementsByName("directoryName")[0].value = StringUtils.getPropertyValue(form.action, "directoryName");
 			document.getElementsByName("directoryId")[0].value = StringUtils.getPropertyValue(form.action, "directoryId");
 		}
	};
	function doOk() {
	 	var directoryName = document.getElementsByName("directoryName")[0].value;
	 	var directoryId = document.getElementsByName("directoryId")[0].value;
		if(directoryName=='') {
			alert("目录不能为空");
			return false;
		}
		dialogArguments.editor.window.setTimeout("FormCommand.doInsertForm('<%=request.getParameter("applicationName")%>', '<%=request.getParameter("formName")%>', '<%=request.getParameter("formTitle")%>', 'directoryId=" + directoryId + "&directoryName=" + directoryName + "')", 10);
		DialogUtils.closeDialog();
	}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr valign="middle">
			<td nowrap="nowrap">事项目录：</td>
			<td><ext:field property="directoryName"/></td>
		</tr>
	</table>
</ext:form>