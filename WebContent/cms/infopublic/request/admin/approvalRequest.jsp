<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function resetDialog() {
		var showNotPublicType = document.getElementsByName('approvalResult')[0].value=='不公开';
		var showNotPublicReason = document.getElementsByName('notPublicType')[0].value=='其他原因';
		document.getElementById('trNotPublicType').style.display = showNotPublicType ? '' : 'none';
		document.getElementById('trNotPublicReason').style.display = showNotPublicType && showNotPublicReason ? '' : 'none';
	}
	function doApprovalRequest() {
		if(document.getElementsByName('approvalResult')[0].value=='') {
			alert('审批结果不能为空');
			return;
		}
		if(document.getElementsByName('approvalResult')[0].value=='不公开' && document.getElementsByName('notPublicType')[0].value=='') {
			alert('不公开原因不能为空');
			return;
		}
		FormUtils.doAction('doApprovalRequest');
	}
	window.onload = resetDialog;
</script>
<table valign="middle" width="430px" border="0" cellpadding="5" cellspacing="0">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">是否公开：</td>
		<td><ext:field property="approvalResult" onchange="resetDialog()"/></td>
	</tr>
	<tr style="display:none" id="trNotPublicType">
		<td nowrap="nowrap">不公开原因：</td>
		<td><ext:field property="notPublicType" onchange="resetDialog()"/></td>
	</tr>
	<tr style="display:none" id="trNotPublicReason">
		<td nowrap="nowrap">其他原因说明：</td>
		<td><ext:field property="notPublicReason"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">是否发布到网站：</td>
		<td><ext:field property="publicPass"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">发布申请内容：</td>
		<td><ext:field property="publicBody"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">发布办理过程：</td>
		<td><ext:field property="publicWorkflow"/></td>
	</tr>
</table>