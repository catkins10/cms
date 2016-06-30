<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/readersModifyOption">
	<script>
		function doOk() {
			var url = RequestUtils.getContextPath() + '/cms/publicservice/admin/modifyReaders.shtml' +
					 '?modifyMode=' + document.getElementsByName('mode')[0].value + 
					 '&readerIds=' + document.getElementsByName('readers.visitorIds')[0].value +
					 '&selectedOnly=' + document.getElementsByName('selectedOnly')[0].checked;
			DialogUtils.getDialogOpener().setTimeout('FormUtils.doAction("' + url + '");', 1);
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">修改方式：</td>
			<td><ext:field property="mode"/></td>
		</tr>
		<tr id="trUser">
			<td nowrap="nowrap">用户：</td>
			<td><ext:field property="readers.visitorNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">修改范围：</td>
			<td><ext:field property="selectedOnly"/></td>
		</tr>
	</table>
</ext:form>