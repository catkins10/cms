<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertApplicationLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj) {
				//解析打开方式
				setLinkOpenMode(obj.target);
				if(obj.id=='applicationLink') {
					var urn = obj.getAttribute("urn");
					document.getElementsByName("applicationTitle")[0].value = StringUtils.getPropertyValue(urn, "applicationTitle");
					document.getElementsByName("applicationName")[0].value = StringUtils.getPropertyValue(urn, "applicationName");
				}
			}
		}
		function doOk() {
			var applicationTitle = document.getElementsByName("applicationTitle")[0].value;
			if(applicationTitle=="") {
				alert("尚未选择应用");
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return;
				}
			}
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = applicationTitle;
			}
			//打开方式
			obj.target = getLinkOpenMode();
			obj.setAttribute("urn", "applicationTitle=" + applicationTitle + "&applicationName=" + document.getElementsByName("applicationName")[0].value);
			obj.id = 'applicationLink';
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">应用选择：</td>
			<td><ext:field property="applicationTitle"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>