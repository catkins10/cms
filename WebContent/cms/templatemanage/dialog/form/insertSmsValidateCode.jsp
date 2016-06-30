<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertSmsValidateCode">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = dialogArguments.selectedElement;
			if(obj && obj.id=="smsValidateCodeLink") {
				document.getElementsByName("sendLimit")[0].value = StringUtils.getPropertyValue(obj.getAttribute("urn"), "sendLimit");
				document.getElementsByName("timeInterval")[0].value = StringUtils.getPropertyValue(obj.getAttribute("urn"), "timeInterval");
			}
		};
		function doOk() {
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, 'a');
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return;
				}
			}
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = "发送短信验证码";
			}
			obj.id = "smsValidateCodeLink";
			obj.setAttribute("urn", "sendLimit=" + document.getElementsByName("sendLimit")[0].value + "&timeInterval=" + document.getElementsByName("timeInterval")[0].value);
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">发送次数限制：</td>
			<td><ext:field property="sendLimit"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">时间间隔(秒)：</td>
			<td><ext:field property="timeInterval"/></td>
		</tr>
	</table>
</ext:form>