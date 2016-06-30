<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSetHomePage">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var url = "";
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj && obj.getAttribute("onclick")) {
				//解析地址
				url = "" + obj.getAttribute("onclick");
				var index = url.indexOf("this.setHomePage(");
				if(index==-1) {
					url = "";
				}
				else {
					index += "this.setHomePage(".length + 1;
					url = url.substring(index , url.indexOf(")", index) - 1);
				}
			}
			if(url=="") {
				url = location.href;
				url = url.substring(0, url.indexOf("/", url.indexOf("://") + 3));
			}
			document.getElementsByName("url")[0].value = url;
		}
		function doOk() {
			var url = document.getElementsByName("url")[0].value;
			if(url=="") {
				alert("地址不能为空");
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
				obj.innerHTML = "设为首页";
			}
			obj.href = "#";
			obj.setAttribute("onclick", "this.style.behavior='url(#default#homepage)';this.setHomePage('" + url + "');");
			obj.removeAttribute("id");
			obj.removeAttribute("urn");
			obj.removeAttribute("target");
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="360px" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">地址：</td>
			<td><ext:field property="url"/></td>
		</tr>
	</table>
</ext:form>