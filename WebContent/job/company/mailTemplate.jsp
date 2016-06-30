<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveMailTemplate">
	<script>
		function insertField(inputFieldName, fieldNames, srcElement) {
			PopupMenu.popupMenu(fieldNames, function(menuItemId, menuItemTitle) {
				FormUtils.pasteText(inputFieldName, '<' + menuItemTitle + '>');
			}, srcElement);			
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">面试邀请函邮件标题：</td>
			<td><ext:field property="invitationMailSubject"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:28px">面试邀请函邮件模板：</td>
			<td>
				<div style="padding: 3px 0px 5px 0px">
					<span class="button" onclick="insertField('invitationMailTemplate', '求职人姓名\0职位\0面试时间\0企业名称\0地址\0发送时间', this)">&nbsp;插入字段&nbsp;</span>
				</div>
				<ext:field property="invitationMailTemplate"/>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">录用通知书邮件标题：</td>
			<td><ext:field property="offerMailSubject"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:28px">录用通知书邮件模板：</td>
			<td>
				<div style="padding: 3px 0px 5px 0px">
					<span class="button" onclick="insertField('offerMailTemplate', '求职人姓名\0职位\0入职时间\0企业名称\0地址\0发送时间', this)">&nbsp;插入字段&nbsp;</span>
				</div>
				<ext:field property="offerMailTemplate"/>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:28px">职位推送邮件标题：</td>
			<td>
				<div style="padding: 3px 0px 5px 0px">
					<span class="button" onclick="insertField('pushMailSubject', '企业名称\0职位', this)">&nbsp;插入字段&nbsp;</span>
				</div>
				<ext:field property="pushMailSubject"/>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top:28px">职位推送邮件模板：</td>
			<td>
				<div style="padding: 3px 0px 5px 0px">
					<span class="button" onclick="insertField('pushMailTemplate', '求职人姓名\0企业名称\0职位\0职位描述\0职位要求\0月薪\0学历要求\0工作年限要求\0发送时间\0职位链接\0求职链接\0取消订阅链接', this)">&nbsp;插入字段&nbsp;</span>
				</div>
				<ext:field property="pushMailTemplate"/>
			</td>
		</tr>
	</table>
</ext:form>