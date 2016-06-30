<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<col align="right">
	<col width="50%">
	<col align="right">
	<col width="50%">
	<tr>
		<td nowrap="nowrap">广告客户：</td>
		<td colspan="3"><ext:field writeonly="true" property="customerName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">广告名称：</td>
		<td><ext:field writeonly="true" property="advertName"/></td>
		<td nowrap="nowrap">广告位：</td>
		<td><ext:field writeonly="true" property="spaceName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">投放时间：</td>
		<td colspan="3">
			<ext:field writeonly="true" property="beginTime"/>
			<ext:notEmpty property="endTime">
				&nbsp;至&nbsp;<ext:field writeonly="true" property="endTime"/>
			</ext:notEmpty>
		</td>
	</tr>
	<ext:notEmpty property="factEndTime">
		<tr>
			<td nowrap="nowrap">实际结束时间：</td>
			<td colspan="3"><ext:field writeonly="true" property="factEndTime"/></td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td nowrap="nowrap">价格(元)：</td>
		<td><ext:field writeonly="true" property="price"/></td>
		<td nowrap="nowrap">点击次数：</td>
		<td><ext:field writeonly="true" property="accessTimes"/></td>
	</tr>
</table>