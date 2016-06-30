<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj) {
				document.getElementsByName("url")[0].value = obj.href;
				setLinkOpenMode(obj.target); //解析打开方式
			}
		};
		function doOk() {
			var href = document.getElementsByName("url")[0].value;
			if(href=="") {
				alert("链接不能为空");
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
				obj.innerHTML = '<%=request.getParameter("title")%>';
				if(obj.innerHTML=='' || obj.innerHTML=='null' || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
					obj.innerHTML = StringUtils.utf8Decode(href.substring(href.lastIndexOf("/") + 1));
				}
			}
			resetLinkByOpenMode(obj, href);
			obj.removeAttribute("id");
			obj.removeAttribute("urn");
			DialogUtils.closeDialog();
		}
		function selectUrl() {
			DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('attachments'), 640, 400);
		}
		function setUrl(url) {
			document.getElementsByName("url")[0].value = url;
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap">链接地址：</td>
			<td><ext:field property="url"/></td>
		</tr>
		<tr valign="bottom">
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>