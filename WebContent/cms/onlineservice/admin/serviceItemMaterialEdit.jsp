<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	var fileType;
	function selectFile(type) {
		fileType = type;
		DialogUtils.selectAttachment('/cms/onlineservice/admin/selectAttachment.shtml', '<ext:write property="id"/>', 'attachments', 600, 400, 'setFileURL("{URL}")');
	}
	function setFileURL(url) {
		document.getElementsByName("material." + fileType)[0].value = url;
		if(fileType=='tableURL' && document.getElementsByName("material.tableName")[0].value=="") {
			var name = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));
			document.getElementsByName("material.tableName")[0].value = StringUtils.utf8Decode(name);
		}
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="5px" style="table-layout:fixed">
	<tr>
		<td width="80px" align="right">材料名称：</td>
		<td><ext:field property="material.name"/></td>
	</tr>
	<tr>
		<td align="right">申报说明：</td>
		<td><ext:field property="material.description"/></td>
	</tr>
	<tr>
		<td width="80px" align="right">表格URL：</td>
		<td><ext:field property="material.tableURL"/></td>
	</tr>
	<tr>
		<td align="right">表格名称：</td>
		<td><ext:field property="material.tableName"/></td>
	</tr>
	<tr>
		<td align="right">样表URL：</td>
		<td><ext:field property="material.exampleURL"/></td>
	</tr>
	<tr>
		<td align="right">备注：</td>
		<td><ext:field property="material.remark"/></td>
	</tr>
</table>