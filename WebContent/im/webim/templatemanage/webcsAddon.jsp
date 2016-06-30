<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/templatemanage/webcsAddon">
	<script>
		var dialogArguments = DialogUtils.getDialogArguments();
		window.onload = function() {
			var scriptWebcs = dialogArguments.document.getElementById("scriptWebim");
			if(!scriptWebcs) {
				return;
			}
			//站点信息
			document.getElementsByName("siteName")[0].value = StringUtils.utf8Decode(StringUtils.getPropertyValue(scriptWebcs.src, "siteName"));
			if(document.getElementsByName("siteName")[0].value=="") {
				return;
			}
			document.getElementsByName("siteId")[0].value = StringUtils.getPropertyValue(scriptWebcs.src, "siteId");
			//对齐方式	
			DropdownField.setValue("webcsAlign", StringUtils.getPropertyValue(scriptWebcs.src, "webimAlign"));
			//边距
			document.getElementsByName("horizontalMargin")[0].value = StringUtils.getPropertyValue(scriptWebcs.src, "horizontalMargin", "10");
			document.getElementsByName("verticalMargin")[0].value = StringUtils.getPropertyValue(scriptWebcs.src, "verticalMargin", "100");
			//对话窗口
			document.getElementsByName("chatDialogWidth")[0].value = StringUtils.getPropertyValue(scriptWebcs.src, "chatDialogWidth", "500");
			document.getElementsByName("chatDialogHeight")[0].value = StringUtils.getPropertyValue(scriptWebcs.src, "chatDialogHeight", "300");
			//留言窗口
			document.getElementsByName("messageDialogWidth")[0].value = StringUtils.getPropertyValue(scriptWebcs.src, "messageDialogWidth", "600");
			document.getElementsByName("messageDialogHeight")[0].value = StringUtils.getPropertyValue(scriptWebcs.src, "messageDialogHeight", "400");
			//评价窗口
			document.getElementsByName("evaluationDialogWidth")[0].value = StringUtils.getPropertyValue(scriptWebcs.src, "evaluationDialogWidth", "500");
			document.getElementsByName("evaluationDialogHeight")[0].value = StringUtils.getPropertyValue(scriptWebcs.src, "evaluationDialogHeight", "300");
		}
		function doOk() {
			if(document.getElementsByName("siteName")[0].value=="") {
				alert("隶属站点未选择");
				return false;
			}
			dialogArguments.editor.saveUndoStep();
			var js = RequestUtils.getContextPath() + "/im/webim/js/webimLoader.js" +
					 "?customerService=true" + 
					 "&siteId=" + document.getElementsByName("siteId")[0].value +
					 "&siteName=" + StringUtils.utf8Encode(document.getElementsByName("siteName")[0].value) +
					 "&webimAlign=" + document.getElementsByName("webcsAlign")[0].value +
					 "&horizontalMargin=" + document.getElementsByName("horizontalMargin")[0].value +
					 "&verticalMargin=" + document.getElementsByName("verticalMargin")[0].value +
					 "&chatDialogWidth=" + document.getElementsByName("chatDialogWidth")[0].value +
					 "&chatDialogHeight=" + document.getElementsByName("chatDialogHeight")[0].value +
					 "&messageDialogWidth=" + document.getElementsByName("messageDialogWidth")[0].value +
					 "&messageDialogHeight=" + document.getElementsByName("messageDialogHeight")[0].value +
					 "&evaluationDialogWidth=" + document.getElementsByName("evaluationDialogWidth")[0].value +
					 "&evaluationDialogHeight=" + document.getElementsByName("evaluationDialogHeight")[0].value;
			ScriptUtils.appendJsFile(dialogArguments.document, js, 'scriptWebim');
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="5" cellpadding="0px">
		<tr>
			<td nowrap="nowrap" align="right">隶属站点：</td>
			<td width="100%" colspan="7"><ext:field property="siteName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">客服窗口对齐方式：</td>
			<td width="50%"><ext:field property="webcsAlign"/></td>
			<td nowrap="nowrap" align="right">水平边距：</td>
			<td width="25%"><ext:field property="horizontalMargin"/></td>
			<td nowrap="nowrap" align="right">垂直边距：</td>
			<td width="25%"><ext:field property="verticalMargin"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">对话窗口宽度：</td>
			<td><ext:field property="chatDialogWidth"/></td>
			<td nowrap="nowrap" align="right">高度：</td>
			<td colspan="3"><ext:field property="chatDialogHeight"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">留言窗口宽度：</td>
			<td><ext:field property="messageDialogWidth"/></td>
			<td nowrap="nowrap" align="right">高度：</td>
			<td colspan="3"><ext:field property="messageDialogHeight"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">评价窗口宽度：</td>
			<td><ext:field property="evaluationDialogWidth"/></td>
			<td nowrap="nowrap" align="right">高度：</td>
			<td colspan="3"><ext:field property="evaluationDialogHeight"/></td>
		</tr>
	</table>
</ext:form>