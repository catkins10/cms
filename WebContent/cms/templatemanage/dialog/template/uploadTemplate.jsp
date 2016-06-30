<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/uploadTemplate">
	<script>
		window.onload = function() {
			var editor = DialogUtils.getDialogArguments().editor;
			var templateName = editor.document.getElementsByName('templateName')[0].value;
			if(templateName=="") {
				document.getElementById('trTemplateName').style.display = '';
			}
		};
		function doOk() {
			if(FormUtils.getAttachmentCount('templateUpload')==0) {
				alert('尚未完成模板文件上传');
				return;
			}
			var editor = DialogUtils.getDialogArguments().editor;
			if(editor.document.getElementsByName('templateName')[0].value=='') {
				var templateName = document.getElementsByName('templateName')[0].value;
				if(templateName=="") {
					alert('模板名称不能为空');
					return;
				}
				editor.document.getElementsByName('templateName')[0].value = templateName;
			}
			editor.window.setTimeout('FormUtils.doAction("saveTemplate", "templateAction=upload")', 10);
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr id="trTemplateName" style="display:none">
			<td align="right" nowrap="nowrap">模板名称：</td>
			<td width="100%"><ext:field property="templateName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">模板文件(*.zip)：</td>
			<td><ext:field property="templateUpload"/></td>
		</tr>
	</table>
</ext:form>