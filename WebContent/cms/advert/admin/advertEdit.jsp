<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="2" cellspacing="0">
	<col align="right">
	<col width="50%">
	<col align="right">
	<col width="50%">
	<tr>
		<td nowrap="nowrap">名称：</td>
		<td><ext:field property="name"/></td>
		<td nowrap="nowrap">链接：</td>
		<td><ext:field property="href"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">广告位：</td>
		<td><ext:field property="spaceName"/></td>
		<td nowrap="nowrap">广告客户：</td>
		<td><ext:field property="customerName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">宽度：</td>
		<td><ext:field property="width"/></td>
		<td nowrap="nowrap">高度：</td>
		<td><ext:field property="height"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">最小化时宽度：</td>
		<td><ext:field property="minimizeWidth"/></td>
		<td nowrap="nowrap">最小化时高度：</td>
		<td><ext:field property="minimizeHeight"/></td>
	</tr>
	<ext:notEmpty property="puts">
		<tr>
			<td nowrap="nowrap">投放情况：</td>
			<td colspan="3">
				<ext:iterate id="advertPut" indexId="advertPutIndex" property="puts" length="3">
					<a href="javascript:DialogUtils.openDialog('<%=request.getContextPath()%>/cms/advert/admin/advertPut.shtml?id=<ext:field writeonly="true" name="advertPut" property="id"/>', 430, 300)">
						<ext:writeNumber name="advertPutIndex" plus="1"/>、<ext:field writeonly="true" name="advertPut" property="beginTime"/><ext:notEmpty name="advertPut" property="end">&nbsp;至&nbsp;<ext:field writeonly="true" name="advertPut" property="end"/></ext:notEmpty>&nbsp;
					</a>
				</ext:iterate>
			</td>
		</tr>
	</ext:notEmpty>
	<tr>
		<td nowrap="nowrap" valign="top">广告内容：</td>
		<td colspan="3" valign="top"><ext:field property="content" style="height:200px"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">最小化时内容：</td>
		<td colspan="3" valign="top"><ext:field property="minimizeContent" style="height:200px"/></td>
	</tr>
</table>