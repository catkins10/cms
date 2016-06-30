<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/saveCssFile">
	<script>
		function insertImage() {
			document.getElementsByName('cssText')[0].focus();
			var dialogUrl = RequestUtils.getContextPath() + '/cms/templatemanage/selectCssFileAttachment.shtml' +
						 	'?id=' + document.getElementsByName('id')[0].value +
						 	'&attachmentSelector.scriptRunAfterSelect=setUrl("{URL}")' +
						 	'&attachmentSelector.type=images';
			DialogUtils.openDialog(dialogUrl, 600, 400);
		}
		function setUrl(imageUrl) {
			FormUtils.pasteText('cssText', imageUrl);
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="2px" style="table-layout:fixed">
		<tr>
			<td align="right" width="60px">名　称：</td>
			<td><ext:field property="cssName"/></td>
		</tr>
		<tr>
			<td align="right" valign="top">样式表：</td>
			<td><ext:field property="cssText"/></td>
		</tr>
	</table>
</ext:form>