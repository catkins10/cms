<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertResourceLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj || obj.id!="resourceLink") {
				return;
			}
			var properties = obj.getAttribute("urn");
			//解析文章标题
			document.getElementsByName("resourceName")[0].value = StringUtils.getPropertyValue(properties, "resourceName");
			//解析文章ID
			document.getElementsByName("resourceId")[0].value = StringUtils.getPropertyValue(properties, "resourceId");
			//解析打开方式
			setLinkOpenMode(StringUtils.getPropertyValue(properties, "openMode"));
		}
		function doOk() {
			//文章名称
			var resourceName = document.getElementsByName("resourceName")[0].value;
			if(resourceName=='') {
				alert("文章不能为空");
				return;
			}
			//文章ID
			var resourceId = document.getElementsByName("resourceId")[0].value;
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
			}
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = resourceName;
			}
			obj.id = "resourceLink";
			obj.setAttribute("urn", "resourceName=" + resourceName + "&resourceId=" + resourceId + "&openMode=" + getLinkOpenMode());
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap">文章选择：</td>
			<td width="100%"><ext:field property="resourceName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>