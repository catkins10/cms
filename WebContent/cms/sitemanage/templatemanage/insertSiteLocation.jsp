<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertSiteLocation">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			var separator = "";
			if(obj && obj.id=="siteLocation") {
				var siteNames = obj.innerText;
				var properties = obj.getAttribute("urn");
				//解析是否只显示本站点/栏目
				document.getElementsByName("selfOnly")[0].checked = "true"==StringUtils.getPropertyValue(properties, "selfOnly");
				//解析分隔符格式
				separator = StringUtils.getPropertyValue(properties, "separator");
			}
			var doc = frames[0].document;
			doc.open();
			doc.write('<body contentEditable="true" onkeydown="return true;if(event.keyCode==13)return false;" style="border-style:none; overflow:hidden; margin:3px; font-size:12px">');
			doc.write(separator);
			doc.write('</body>');
			doc.close();
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
			//是否只显示自己
			var selfOnly = document.getElementsByName("selfOnly")[0].checked ? "true" : "false";
			obj.innerHTML = "<栏目位置" + (selfOnly=="true" ? ":当前栏目" : "") + ">";
			obj.id = "siteLocation";
			var doc = frames[0].document;
			obj.setAttribute("urn", "selfOnly=" + selfOnly + "&separator=" + StringUtils.encodePropertyValue(doc.body.innerHTML));
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">只显示本栏目：</td>
			<td><ext:field property="selfOnly"/></td>
		</tr>
		<tr>
			<td>分隔符：</td>
			<td><iframe id="separator" src="about:blank" class="frame" style="width:100%; height:30px;"></iframe></td>
		</tr>
	</table>
</ext:form>