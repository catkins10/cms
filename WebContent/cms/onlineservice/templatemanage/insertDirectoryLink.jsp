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
			//解析站点名称
			document.getElementsByName("directoryName")[0].value = StringUtils.getPropertyValue(properties, "directoryName");
			//解析站点ID
			document.getElementsByName("directoryId")[0].value = StringUtils.getPropertyValue(properties, "directoryId");
			//解析打开方式
			setLinkOpenMode(StringUtils.getPropertyValue(properties, "openMode"));
		}
		function doOk() {
			//目录名称
			var directoryName = document.getElementsByName("directoryName")[0].value;
			if(directoryName=='') {
				alert("目录不能为空");
				return;
			}
			//目录ID
			var directoryId = document.getElementsByName("directoryId")[0].value;
		
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
				if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
					obj.innerHTML = directoryName;
				}
			}
			obj.id = "onlineServiceDirectoryLink";
			obj.setAttribute("urn", "directoryName=" + directoryName + "&directoryId=" + directoryId + "&openMode=" + getLinkOpenMode());
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">目录选择：</td>
			<td><ext:field property="directoryName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>