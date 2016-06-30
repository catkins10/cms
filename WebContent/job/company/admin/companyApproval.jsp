<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveCompanyApproval">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">是否需要审核：</td>
			<td><ext:field property="approvalRequired"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">审核通过邮件标题：</td>
			<td><ext:field property="passMailSubject"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:28px">审核通过邮件模板：</td>
			<td>
				<div style="padding-bottom: 3px">
					<input type="button" class="button" value="插入企业名称" onclick="FormUtils.pasteText('passMailTemplate', '&lt;企业名称&gt;')">
					<input type="button" class="button" value="插入发送时间" onclick="FormUtils.pasteText('passMailTemplate', '&lt;发送时间&gt;')">
				</div>
				<ext:field property="passMailTemplate"/>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">审核未通过邮件标题：</td>
			<td><ext:field property="failedMailSubject"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:28px">审核未通过邮件模板：</td>
			<td>
				<div style="padding-bottom: 3px">
					<input type="button" class="button" value="插入企业名称" onclick="FormUtils.pasteText('failedMailTemplate', '&lt;企业名称&gt;')">
					<input type="button" class="button" value="插入发送时间" onclick="FormUtils.pasteText('failedMailTemplate', '&lt;发送时间&gt;')">
					<input type="button" class="button" value="插入未通过原因" onclick="FormUtils.pasteText('failedMailTemplate', '&lt;未通过原因&gt;')">
				</div>
				<ext:field property="failedMailTemplate"/>
			</td>
		</tr>
	</table>
</ext:form>