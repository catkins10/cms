<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">内容</td>
		<td class="tdcontent"><ext:field writeonly="true" property="content"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">业务分类</td>
		<td class="tdcontent"><ext:field writeonly="true" property="smsBusinessName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发送人号码</td>
		<td class="tdcontent"><ext:field property="senderNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">接收时间</td>
		<td class="tdcontent"><ext:field property="receiveTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">答复内容</td>
		<td class="tdcontent"><ext:field property="replyContent"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">答复时间</td>
		<td class="tdcontent"><ext:field property="replyTime"/></td>
	</tr>
</table>