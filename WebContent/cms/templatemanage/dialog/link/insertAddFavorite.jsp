<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertAddFavorite">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var url = "", name = "", index;
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj && obj.href && (index=obj.href.indexOf("addFavorite("))!=-1) {
				//解析地址
				var href = obj.href;
				index += "addFavorite(".length + 1;
				var indexEnd = href.indexOf("'", index);
				url = href.substring(index , indexEnd);
				index = href.indexOf("'" , indexEnd + 2) + 1;
				indexEnd = href.indexOf("'", index);
				name = href.substring(index , indexEnd);
			}
			if(url=="") {
				url = location.href;
				url = url.substring(0, url.indexOf("/", url.indexOf("://") + 3));
				name = dialogArguments.document.title;
			}
			document.getElementsByName("url")[0].value = url;
			document.getElementsByName("name")[0].value = name;
		}
		function doOk() {
			var url = document.getElementsByName("url")[0].value;
			if(url=="") {
				alert("地址不能为空");
				return;
			}
			var name = document.getElementsByName("name")[0].value;
			if(name=="") {
				alert("名称不能为空");
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
				obj.innerHTML = "加入收藏";
			}
			obj.href = "javascript:window.external.addFavorite('" + url + "', '" + name + "')";
			obj.removeAttribute("onclick");
			obj.removeAttribute("id");
			obj.removeAttribute("urn");
			obj.removeAttribute("target");
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="360px" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap">地址：</td>
			<td width="100%"><ext:field property="url"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">名称：</td>
			<td><ext:field property="name"/></td>
		</tr>
	</table>
</ext:form>