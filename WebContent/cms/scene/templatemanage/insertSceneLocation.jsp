<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertLocation">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			var separator = "";
			if(obj && obj.id=="sceneLocation") {
				//解析分隔符格式
				separator = StringUtils.getPropertyValue(obj.getAttribute("urn"), "separator");
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
			obj.innerHTML = "<场景位置>";
			obj.id = "sceneLocation";
			var doc = frames[0].document;
			obj.setAttribute("urn", "separator=" + StringUtils.encodePropertyValue(doc.body.innerHTML));
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">分隔符：</td>
			<td><iframe id="separator" src="about:blank" class="frame" style="width:100%; height:30px;"></iframe></td>
		</tr>
	</table>
</ext:form>