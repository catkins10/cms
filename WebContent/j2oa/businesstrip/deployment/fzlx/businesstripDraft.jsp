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
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请人</td>
		<td class="tdcontent"><ext:field property="proposerName"/></td>
		<td class="tdtitle" nowrap="nowrap">出差地点</td>
		<td class="tdcontent"><ext:field property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">出差时间</td>
		<td class="tdcontent" colspan="3">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed"><tr>
				<td width="50%"><ext:field property="beginTime"/></td>
				<td width="30px" align="center">至</td>
				<td width="50%"><ext:field property="endTime"/></td>
			</tr></table>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" nowrap="nowrap">出差目的</td>
		<td class="tdcontent" colspan="3"><ext:field property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" style="padding-top:6px" nowrap="nowrap">出差人</td>
		<td class="tdcontent" colspan="3"><ext:field property="tripPerson.visitorNames"/></td>
	</tr>
</table>