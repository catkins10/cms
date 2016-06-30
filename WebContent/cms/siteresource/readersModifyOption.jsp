<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/readersModifyOption">
	<script>
		function doOk() {
			var parameter = 'modifyMode=' + document.getElementsByName('mode')[0].value + 
							'&deleteNotColumnVisitor=' + document.getElementsByName('deleteNotColumnVisitor')[0].checked +
							'&readerIds=' + document.getElementsByName('readers.visitorIds')[0].value +
							'&selectedResourceOnly=' + document.getElementsByName('selectedOnly')[0].checked;
			DialogUtils.getDialogOpener().setTimeout('FormUtils.doAction("modifyReaders", "' + parameter + '");', 1);
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">修改方式：</td>
			<td><ext:field property="mode" onchange="var value=document.getElementsByName('mode')[0].value;document.getElementById('trDeleteNotColumnVisitor').style.display=(value=='synchColumnVisitor' ? '' : 'none');document.getElementById('trUser').style.display=(value=='synchColumnVisitor' ? 'none' : '');"/></td>
		</tr>
		<tr id="trDeleteNotColumnVisitor">
			<td nowrap="nowrap">非栏目访问者：</td>
			<td><ext:field property="deleteNotColumnVisitor"/></td>
		</tr>
		<tr id="trUser" style="display: none">
			<td nowrap="nowrap">用户：</td>
			<td><ext:field property="readers.visitorNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">修改范围：</td>
			<td><ext:field property="selectedOnly"/></td>
		</tr>
	</table>
</ext:form>