<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				return;
			}
			var properties = obj.getAttribute("urn");
			document.getElementsByName("itemType")[0].value = StringUtils.getPropertyValue(properties, "itemType");
			setLinkOpenMode(StringUtils.getPropertyValue(properties, "openMode"));
		}
		function doOk() {
			var itemType = document.getElementsByName("itemType")[0].value;
			if(itemType=='') {
				alert("事项类型不能为空");
				return;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
				if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
					obj.innerHTML = itemType;
				}
			}
			obj.id = "onlineServiceAuthorityDirectoryLink";
			obj.setAttribute("urn", "itemType=" + itemType + "&openMode=" + getLinkOpenMode());
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">事项类型：</td>
			<td><ext:field property="itemType"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>