<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/insertAdvertSpace">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj && obj.id=="advertSpace") {
				var urn = obj.getAttribute("urn");
				document.getElementsByName("advertSpaceId")[0].value = StringUtils.getPropertyValue(urn, "advertSpaceId");
				document.getElementsByName("advertSpaceName")[0].value = StringUtils.getPropertyValue(urn, "advertSpaceName");
				DropdownField.setValue("loadMode", StringUtils.getPropertyValue(urn, "loadMode"));
				DropdownField.setValue("hideMode", StringUtils.getPropertyValue(urn, "hideMode"));
				document.getElementsByName("speed")[0].value = StringUtils.getPropertyValue(urn, "speed");
				document.getElementsByName("displaySeconds")[0].value = StringUtils.getPropertyValue(urn, "displaySeconds");
			}
		}
		function doOk() {
			if(document.getElementsByName("advertSpaceName")[0].value=='') {
				alert("广告位不能为空");
				return;
			}
			if(document.getElementsByName("loadMode")[0].value=='') {
				alert("加载方式不能为空");
				return;
			}
			if(document.getElementsByName("hideMode")[0].value=='') {
				alert("关闭方式不能为空");
				return;
			}
			dialogArguments.editor.saveUndoStep();
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					alert('请重新选择插入的位置');
					return;
				}
			}
			obj.innerHTML = "&lt;广告位:" + document.getElementsByName("advertSpaceName")[0].value + "&gt;";
			obj.id = "advertSpace";
			var urn = "advertSpaceId=" + document.getElementsByName("advertSpaceId")[0].value +
					  "&advertSpaceName=" + StringUtils.encodePropertyValue(document.getElementsByName("advertSpaceName")[0].value) +
					  "&loadMode=" + document.getElementsByName("loadMode")[0].value +
					  "&hideMode=" + document.getElementsByName("hideMode")[0].value +
					  "&speed=" + document.getElementsByName("speed")[0].value +
					  "&displaySeconds=" + document.getElementsByName("displaySeconds")[0].value;
			obj.setAttribute("urn", urn);
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td>广告位：</td>
			<td><ext:field property="advertSpaceName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">加载方式：</td>
			<td><ext:field property="loadMode"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">速度(像数/毫秒)：</td>
			<td><ext:field property="speed"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">关闭方式：</td>
			<td><ext:field property="hideMode"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">显示时长(秒)：</td>
			<td><ext:field property="displaySeconds"/></td>
		</tr>
	</table>
</ext:form>