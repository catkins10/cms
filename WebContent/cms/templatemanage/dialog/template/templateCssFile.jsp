<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/saveTemplateCssFile" onsubmit="formOnSubmit()">
	<script>
		function insertImage() {
			document.getElementsByName('cssContent')[0].focus();
			var dialogUrl = RequestUtils.getContextPath() + '/cms/templatemanage/selectTemplateAttachment.shtml' +
						 	'?id=' + document.getElementsByName('id')[0].value +
						 	'&attachmentSelector.scriptRunAfterSelect=SetUrl("{URL}")' +
						 	'&attachmentSelector.type=images';
			DialogUtils.openDialog(dialogUrl, 600, 400);
		}
		function SetUrl(imageUrl) {
			FormUtils.pasteText('cssContent', imageUrl);
		}
		function formOnSubmit() {
			var script = document.getElementsByName('script')[0].value;
			if(script!=null) {
				DialogUtils.getDialogOpener().setTimeout(script, 3000);
			}
			return true;
		}
	</script>
	<div style="padding-bottom: 3px;">样式表：</div>
	<div style="width:100%">
	<ext:field property="cssContent"/>
	</div>
</ext:form>