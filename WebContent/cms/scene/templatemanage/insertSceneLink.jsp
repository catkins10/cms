<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSceneLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			if(location.href.indexOf('parentDirectoryId')==-1) {
				var directoryId = dialogArguments.editor.document.getElementsByName("directoryId")[0].value;
				if(directoryId!="" && directoryId!="0") {
					location.href = location.href + '&parentDirectoryId=' + directoryId;
					return;
				}
			}
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				return;
			}
			DropdownField.setValue("sceneName", obj.getAttribute("urn"));
			//解析打开方式
			setLinkOpenMode(obj.target);
		}
		function doOk() {
			var sceneId = document.getElementsByName('sceneId')[0].value;
			if(sceneId=="") {
				alert("请选择场景");
				return;
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
				obj.innerHTML = document.getElementsByName('sceneName')[0].value;
			}
			obj.setAttribute("urn", sceneId);
			obj.id = 'sceneLink';
			obj.target = getLinkOpenMode();
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap">场景：</td>
			<td><ext:field property="sceneName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>