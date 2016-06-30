<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">报送级别：</td>
		<td><ext:field writeonly="true" property="sendHigher.level"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">报送时间：</td>
		<td><ext:field writeonly="true" property="sendHigher.sendTime"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">报送人：</td>
		<td><ext:field writeonly="true" property="sendHigher.sender"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">采用情况：</td>
		<td><ext:field writeonly="true" property="used"/></td>
	</tr>
	<ext:notEmpty property="sendHigher.useTime">
		<tr>
			<td nowrap="nowrap">刊物名称：</td>
			<td><ext:field writeonly="true" property="sendHigher.useMagazine"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">采用时间：</td>
			<td><ext:field writeonly="true" property="sendHigher.useTime"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">登记人：</td>
			<td><ext:field writeonly="true" property="sendHigher.useRegister"/></td>
		</tr>
	</ext:notEmpty>
</table>