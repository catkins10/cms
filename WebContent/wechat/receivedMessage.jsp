<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveReceivedMessage">
	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col valign="middle" width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap" valign="top">消息内容</td>
			<td class="tdcontent"><ext:field property="content"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">用户昵称</td>
			<td class="tdcontent"><ext:field property="fromUserNickname"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">创建时间</td>
			<td class="tdcontent"><ext:field property="createTime"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">消息类型</td>
			<td class="tdcontent"><ext:field writeonly="true" property="msgType"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">答复时间</td>
			<td class="tdcontent"><ext:field property="replyTime"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">答复人</td>
			<td class="tdcontent"><ext:field property="replier"/></td>
		</tr>
	</table>
</ext:form>