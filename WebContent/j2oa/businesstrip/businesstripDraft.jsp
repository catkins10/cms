<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<script>
window.onload = function() {
	if(document.getElementsByName('endTime')[0].value=='') {
		DateTimeField.setValue('endTime', 'hour', '23');
		DateTimeField.setValue('endTime', 'minute', '59');
		DateTimeField.setValue('endTime', 'second', '59');
	}
}
</script>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请人</td>
		<td class="tdcontent"><ext:field property="proposerName"/></td>
		<td class="tdtitle" nowrap="nowrap">申请部门</td>
		<td class="tdcontent"><ext:field property="departmentName"/></td>
		<td class="tdtitle" nowrap="nowrap">出差地点</td>
		<td class="tdcontent"><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">出差目的</td>
		<td colspan="5" class="tdcontent"><ext:field property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">出差时间</td>
		<td colspan="3" class="tdcontent">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed"><tr>
				<td width="50%"><ext:field property="beginTime"/></td>
				<td width="30px" align="center">至</td>
				<td width="50%"><ext:field property="endTime"/></td>
			</tr></table>
		</td>
		<td class="tdtitle" nowrap="nowrap">交通工具</td>
		<td class="tdcontent"><ext:field property="vehicle"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap" style="padding-top:6px">出差人</td>
		<td colspan="5" class="tdcontent"><ext:field property="tripPerson.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="5" class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>