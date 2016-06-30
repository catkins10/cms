<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSceneServiceLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj || obj.tagName!='A') {
				return;
			}
			//设置服务ID和名称
			var urn = obj.getAttribute("urn");
			document.getElementsByName("serviceId")[0].value = StringUtils.getPropertyValue(urn, "id");
			document.getElementsByName("serviceName")[0].value = StringUtils.getPropertyValue(urn, "name");
			//解析打开方式
			setLinkOpenMode(obj.target);
		}
		function doOk() {
			if(document.getElementsByName("serviceName")[0].value=='') {
				alert("尚未选择场景服务");
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
			}
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = document.getElementsByName("serviceName")[0].value;
			}
			obj.id = "sceneServiceLink";
			obj.target = getLinkOpenMode();
			var sceneTree = "<%=request.getParameter("sceneTree")%>";
			obj.setAttribute("urn", "id=" + document.getElementsByName("serviceId")[0].value + "&name=" + document.getElementsByName("serviceName")[0].value + (sceneTree=="true" ? "&sceneTree=true" : ""));
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">场景服务：</td>
			<td><ext:field property="serviceName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>