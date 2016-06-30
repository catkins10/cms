<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function countDays() {
	var beginTime = FormUtils.getTimeValue("beginTime");
	var endTime = FormUtils.getTimeValue("endTime");
	if(beginTime!="" && endTime!="") {
		var diff = Math.abs((endTime - beginTime)/24/3600000);
		var days = Math.floor(diff);
		var hours = diff - days;
		document.getElementsByName("dayCount")[0].value = (hours>(1/6) ? days + 1 : (hours==0 ? days : days + 0.5));
	}
}
window.onload = function() {
	if(document.getElementsByName('endTime')[0].value=='') {
		DateTimeField.setValue('endTime', 'hour', '23');
		DateTimeField.setValue('endTime', 'minute', '5');
		DateTimeField.setValue('endTime', 'second', '9');
	}
}
</script>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="33%" class="tdcontent">
	<col valign="middle">
	<col valign="middle" width="33%" class="tdcontent">
	<col valign="middle">
	<col valign="middle" width="33%" class="tdcontent">
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请人</td>
		<td class="tdcontent"><ext:field property="personName"/></td>
		<td class="tdtitle" nowrap="nowrap">所在部门</td>
		<td class="tdcontent"><ext:field property="departmentName"/>
		</td>
		<td class="tdtitle" nowrap="nowrap">请假类别</td>
		<td class="tdcontent"><ext:field property="type"/></tr>
	<tr>
		<td valign="top" style="padding-top:6px">请假原因</td>
		<td colspan="5" class="tdcontent"><ext:field property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">请假时间</td>
		<td colspan="3" class="tdcontent">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed"><tr>
				<td width="50%"><ext:field property="beginTime" onchange="countDays()"/></td>
				<td width="30px" align="center">至</td>
				<td width="50%"><ext:field property="endTime" onchange="countDays()"/></td>
			</tr></table>
		</td>
		<td class="tdtitle" nowrap="nowrap">请假天数</td>
		<td class="tdcontent"><ext:field property="dayCount"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top" style="padding-top:6px">工作代理人</td>
		<td colspan="5" class="tdcontent"><ext:field property="agents.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="5" class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>