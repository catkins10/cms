<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="50px" align="right">
	<col width="100%">
	<tr>
		<td>时间：</td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="100px"><ext:field property="offDay.offDayNameOfWeek"/></td>
					<td nowrap="nowrap">&nbsp;</td>
					<td><ext:field property="offDay.beginTime"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td><ext:field property="offDay.endTime"/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>