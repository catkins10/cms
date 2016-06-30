<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/writeMonitoringReport">
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000" width="100%">
		<tr>
			<td nowrap>目录：</td>
			<td width="100%"><ext:field property="directoryName"/></td>
			<td nowrap>统计时间：</td>
			<td width="90px" nowrap><ext:field property="beginDate"/></td>
			<td nowrap>至</td>
			<td width="90px" nowrap><ext:field property="endDate"/></td>
			<td nowrap>&nbsp;<input onclick="FormUtils.submitForm()" type="button" class="button" value="确定"></td>
		</tr>
		<ext:notEqual value="-1" property="guideVisits">
			<table border="0" cellpadding="3" cellspacing="3" style="color:#000000" width="100%">
				<col align="right">
				<col width="100%">
				<tr>
					<td nowrap>政府信息公开指南：</td>
					<td nowrap width="100%"><ext:field property="guideVisits"/></td>
				</tr>
				<tr>
					<td nowrap>政府信息公开目录：</td>
					<td nowrap><ext:field property="publicDirectoryVisits"/></td>
				</tr>
				<tr>
					<td nowrap>依申请公开：</td>
					<td nowrap><ext:field property="publicRequestVisits"/></td>
				</tr>
				<tr>
					<td nowrap>政府信息公开年度报告：</td>
					<td nowrap><ext:field property="reportVisits"/></td>
				</tr>
				<tr>
					<td nowrap>政府信息公开制度规定：</td>
					<td nowrap><ext:field property="lawsVisits"/></td>
				</tr>
				<tr>
					<td nowrap>政府信息公开意见箱：</td>
					<td nowrap><ext:field property="publicOpinionVisits"/></td>
				</tr>
			</table>
		</ext:notEqual>
	</table>
</ext:form>