<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertAgentLibLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj && obj.target) {
				setLinkOpenMode(obj.target); //解析打开方式
				var agentLib = StringUtils.getPropertyValue(obj.href, "lib");
				if(agentLib!="") {
					DropdownField.setValue('agentLib', StringUtils.utf8Decode(agentLib));
				}
			}
		};
		function doOk() {
			dialogArguments.editor.saveUndoStep();
			var agentLib = document.getElementsByName("agentLib")[0].value;
			if(agentLib=="") {
				alert("没有选择名录库");
				return;
			}
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
			}
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = agentLib;
			}
			resetLinkByOpenMode(obj, RequestUtils.getContextPath() + "/bidding/enterprise/listAgents.shtml?lib=" + StringUtils.utf8Encode(agentLib));
			obj.removeAttribute("id");
			obj.removeAttribute("urn");
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td valign="top" nowrap="nowrap">代理名录库：</td>
			<td><ext:field property="agentLib"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>