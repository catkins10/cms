<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<col align="right">
	<col width="50%">
	<col align="right">
	<col width="50%">
	<tr>
		<td nowrap="nowrap">广告客户：</td>
		<td colspan="3"><ext:field property="customerName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">广告名称：</td>
		<td><ext:field property="advertName"/></td>
		<td nowrap="nowrap">广告位：</td>
		<td><ext:field property="spaceName"/></td>
	</tr>
	<ext:notEmpty property="currentAdvertPut">
		<tr>
			<td nowrap="nowrap">当前广告：</td>
			<td><ext:field writeonly="true" property="currentAdvertPut.advertName"/></td>
			<td nowrap="nowrap">截止时间：</td>
			<td><ext:field writeonly="true" property="currentAdvertPut.endTime"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">客户名称：</td>
			<td colspan="3"><ext:field writeonly="true" property="currentAdvertPut.customerName"/></td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td nowrap="nowrap">投放时间：</td>
		<td colspan="3">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="50%"><ext:field property="beginTime"/></td>
					<td>&nbsp;至&nbsp;</td>
					<td width="50%"><ext:field property="endTime"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap">报价说明：</td>
		<td colspan="3"><ext:field property="quotedPrice"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">价格(元)：</td>
		<td colspan="3"><ext:field property="price"/></td>
	</tr>
</table>