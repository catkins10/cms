<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/writeProjectReport" target="_blank" method="get">
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000" width="100%">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap>时间：</td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td width="50%"><ext:field property="beginDate"/></td>
						<td nowrap="nowrap">&nbsp;至&nbsp;</td>
						<td width="50%"><ext:field property="endDate"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td nowrap>地区/开发区：</td>
			<td><ext:field property="area"/></td>
		</tr>
	</table>
</ext:form>