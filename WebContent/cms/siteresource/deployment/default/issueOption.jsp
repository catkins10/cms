<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/issueOption">
	<script>
		function doOK() {
			DialogUtils.getDialogOpener().document.getElementsByName('issueEndTime')[0].value = document.getElementsByName('option')[0].checked ? "" : document.getElementsByName('endTime')[0].value;
			DialogUtils.getDialogOpener().setTimeout(document.getElementsByName('reissue')[0].value=="true" ? "FormUtils.doAction('reissueArticle')" : "FormUtils.doAction('issueArticle')", 1);
			DialogUtils.closeDialog();
		}
	</script>
	是否确定发布？
	<table border="0" width="100%" cellspacing="0" cellpadding="5px" style="display:none">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">发布选项：</td>
			<td><ext:field property="option" onclick="document.getElementById('endTimeLabel').style.visibility=document.getElementById('endTimeInput').style.visibility=(document.getElementsByName('option')[0].checked ? 'hidden' : 'visible');"/></td>
		</tr>
		<tr>
			<td id="endTimeLabel" style="visibility: hidden;" nowrap="nowrap">截止时间：</td>
			<td id="endTimeInput" style="visibility: hidden;"><ext:field property="endTime"/></td>
		</tr>
	</table>
</ext:form>