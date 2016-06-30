<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
window.onload = function() {
	if(document.getElementsByName('festival.beginTime')[0].value=='') {
		DateTimeField.setValue('festival.beginTime', 'hour', '0');
		DateTimeField.setValue('festival.beginTime', 'minute', '0');
		DateTimeField.setValue('festival.beginTime', 'second', '0');
	}
	if(document.getElementsByName('festival.endTime')[0].value=='') {
		DateTimeField.setValue('festival.endTime', 'hour', '23');
		DateTimeField.setValue('festival.endTime', 'minute', '59');
		DateTimeField.setValue('festival.endTime', 'second', '59');
	}
	if(document.getElementsByName('festival.legalBeginTime')[0].value=='') {
		DateTimeField.setValue('festival.legalBeginTime', 'hour', '0');
		DateTimeField.setValue('festival.legalBeginTime', 'minute', '0');
		DateTimeField.setValue('festival.legalBeginTime', 'second', '0');
	}
	if(document.getElementsByName('festival.legalEndTime')[0].value=='') {
		DateTimeField.setValue('festival.legalEndTime', 'hour', '23');
		DateTimeField.setValue('festival.legalEndTime', 'minute', '59');
		DateTimeField.setValue('festival.legalEndTime', 'second', '59');
	}
}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="90px" align="right">
	<col width="100%">
	<tr>
		<td>节日名称：</td>
		<td><ext:field property="festival.name"/></td>
	</tr>
	<tr>
		<td>放假时间：</td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><ext:field property="festival.beginTime"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td><ext:field property="festival.endTime"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>法定放假时间：</td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><ext:field property="festival.legalBeginTime"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td><ext:field property="festival.legalEndTime"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>调休时间：</td>
		<td>
			<ext:iterate id="adjust" indexId="adjustIndex" property="festival.adjusts"><ext:notEqual value="0" name="adjustIndex"> 、</ext:notEqual><a href="javascript:PageUtils.editrecord('jeaf/timetable', 'festivalAdjust', '<ext:write property="id"/>', 'width=430,height=120,mode=dialog', '', 'adjust.id=<ext:write name="adjust" property="id"/>')"><ext:field name="adjust" property="beginTime" writeonly="true"/> ~ <ext:field name="adjust" property="endTime" writeonly="true"/></a></ext:iterate>
		</td>
	</tr>
</table>