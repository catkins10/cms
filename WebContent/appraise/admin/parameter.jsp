<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveParameter">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">服务对象上报截止日期：</td>
			<td><ext:field property="recipientsUploadEnd"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">服务对象上报催办开始日期：</td>
			<td><ext:field property="recipientsUploadUrgeBegin"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">服务对象上报催办天数：</td>
			<td><ext:field property="recipientsUploadUrgeDays"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">服务对象上报催办时间：</td>
			<td><ext:field property="recipientsUploadUrgeTime"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top"><br/><br/>服务对象上报催办短信格式：</td>
			<td>
				<div style="padding-bottom: 3px;">
					<input type="button" class="button" value="插入单位名称" style="width:80px" onclick="FormUtils.pasteText('recipientsUploadUrgeSms', '&lt;单位名称&gt;')">
					<input type="button" class="button" value="插入年度" style="width:60px" onclick="FormUtils.pasteText('recipientsUploadUrgeSms', '&lt;年度&gt;')">
					<input type="button" class="button" value="插入月份" style="width:60px" onclick="FormUtils.pasteText('recipientsUploadUrgeSms', '&lt;月份&gt;')">
				</div>
				<ext:field property="recipientsUploadUrgeSms"/>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">服务对象上限：</td>
			<td><ext:field property="recipientsLimit"/></td>
		</tr>
	</table>
</ext:form>