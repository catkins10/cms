<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertNewestImage">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(obj && obj.id=="newestImage") {
				var properties = obj.getAttribute("urn");
				document.getElementsByName("imageURL")[0].value = StringUtils.getPropertyValue(properties, "imageURL");
				document.getElementsByName("timeLimit")[0].value = StringUtils.getPropertyValue(properties, "timeLimit");
			}
		};
		function doOk() {
			var imageURL = document.getElementsByName("imageURL")[0].value;
			var timeLimit = document.getElementsByName("timeLimit")[0].value;
			var obj = DomUtils.getParentNode(dialogArguments.selectedElement, "a");
			if(!obj) {
				obj = DomUtils.createLink(dialogArguments.window, dialogArguments.range);
				if(!obj) {
					alert('请重新选择插入的位置');
				}
			}
			obj.innerHTML = '&lt;&quot;new&quot;图片&gt;';
			obj.id = "newestImage";
			var urn = "imageURL=" + imageURL + "&timeLimit=" + timeLimit;
			obj.setAttribute("urn", urn);
			DialogUtils.closeDialog();
		}
		function selectImage() {
			DialogUtils.openDialog(dialogArguments.editor.getAttachmentSelectorURL('images'), 640, 400);
		}
		function setUrl(imageURL) {
			document.getElementsByName("imageURL")[0].value = imageURL;
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">“new”图片：</td>
			<td><ext:field property="imageURL"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">有效期(小时)：</td>
			<td><ext:field property="timeLimit"/></td>
		</tr>
    </table>
</ext:form>