<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveTelegramConfigure">
 	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">电报密级：</td>
			<td><ext:field property="securityLevels"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">电报等级：</td>
			<td><ext:field property="levels"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">电报分类：</td>
			<td><ext:field property="categories"/></td>
		</tr>
		<tr class="readonlyrow">
			<td colspan="2" align="left"><b><br/>当前流水号配置</b></td>
		</tr>
		<tr>
			<td nowrap="nowrap">发送(明文)：</td>
			<td><ext:field property="currentSendSn"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">发送(密文)：</td>
			<td><ext:field property="currentCrypticSendSn"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">接收(明文)：</td>
			<td><ext:field property="currentReceiveSn"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">接收(密文)：</td>
			<td><ext:field property="currentCrypticReceiveSn"/></td>
		</tr>
	</table>
</ext:form>