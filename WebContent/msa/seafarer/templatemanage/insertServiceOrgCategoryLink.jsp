<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertAssessLink">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj && obj.tagName=="A") {
				setLinkOpenMode(obj.target); //解析打开方式
				var category = StringUtils.getPropertyValue(obj.href, "category");
				if(category!="") {
					DropdownField.setValue('category', StringUtils.utf8Decode(category));
				}
			}
		}
		function doOk() {
			var category = document.getElementsByName("category")[0].value;
			if(category=="") {
				alert("没有指定分类");
				return;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj || obj.tagName!='A') {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					return;
				}
			}
			if(obj.innerHTML=="" || obj.innerHTML.toLowerCase()=="<br>" || obj.innerHTML.toLowerCase()=="<br/>") {
				obj.innerHTML = category;
			}
			var siteId = dialogArguments.editor.document.getElementsByName("siteId")[0].value;
			var url = RequestUtils.getContextPath() + "/cms/sitemanage/applicationIndex.shtml" +
					  "?applicationName=msa/seafarer" +
					  "&pageName=serviceOrgs" +
					  "&category=" + StringUtils.utf8Encode(category) +
					  (siteId=="" || siteId=="0" ? "" : "&siteId=" + siteId);
			resetLinkByOpenMode(obj, url);
			obj.removeAttribute("id");
			obj.removeAttribute("urn");
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td valign="top" nowrap="nowrap">服务机构分类：</td>
			<td><ext:field property="category"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">打开方式：</td>
			<td><jsp:include page="/cms/templatemanage/dialog/link/linkOpenMode.jsp" /></td>
		</tr>
	</table>
</ext:form>