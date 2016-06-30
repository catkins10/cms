<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertNavigatorLink">
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
					return;
				}
			}
			//打开方式
			obj.target = getLinkOpenMode();
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = "<%=request.getParameter("title")%>";
			}
			obj.setAttribute("urn", "applicationName=<%=request.getParameter("applicationName")%>&title=<%=request.getParameter("title")%>");
			obj.id = 'navigatorLink';
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="360px" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>