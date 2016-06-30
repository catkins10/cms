<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertAddFavorite">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		function doOk() {
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return false;
				}
			}
			var simpleChinese = document.getElementsByName("convertTo")[0].checked;
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = simpleChinese ? "简体中文" : "繁體中文";
			}
			obj.href = "";
			var expires = new Date();
			if(!simpleChinese) {
				expires.setYear(expires.getYear() + 100);
			}
			obj.removeAttribute("id");
			obj.removeAttribute("urn");
			obj.removeAttribute("onclick");
			obj.href = "#";
		　	obj.setAttribute("onclick", "document.cookie='traditionalChinese=" + (!simpleChinese) + ";expires=" + expires.toGMTString() + ";path=/';top.location.reload();");
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="360px" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap">转换为：</td>
			<td width="100%"><ext:field property="convertTo"/></td>
		</tr>
	</table>
</ext:form>