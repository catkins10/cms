<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<ext:equal value="0" property="receiveMessageId">
		<tr>
			<td class="tdtitle" nowrap="nowrap">发送范围</td>
			<td class="tdcontent"><ext:field property="rangeMode" onclick="document.getElementById('trGroups').style.display=document.getElementById('trUsers').style.display=document.getElementsByName('rangeMode')[0].checked ? 'none' : '';"/></td>
		</tr>
		<tr id="trGroups" style="<ext:equal value="0" property="rangeMode">display:none</ext:equal>">
			<td class="tdtitle" nowrap="nowrap">分组</td>
			<td class="tdcontent"><ext:field property="groupNames"/></td>
		</tr>
		<tr id="trUsers" style="<ext:equal value="0" property="rangeMode">display:none</ext:equal>">
			<td class="tdtitle" nowrap="nowrap">用户</td>
			<td class="tdcontent"><ext:field property="userNames"/></td>
		</tr>
	</ext:equal>
	<jsp:include page="messageContentEdit.jsp" flush="true"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<ext:notEmpty property="sendTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">发送时间</td>
			<td class="tdcontent"><ext:field property="sendTime"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">状态</td>
			<td class="tdcontent"><ext:field property="status"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">用户数</td>
			<td class="tdcontent"><ext:field property="totalCount"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">发送成功用户数</td>
			<td class="tdcontent"><ext:field property="sentCount"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">发送失败用户数</td>
			<td class="tdcontent"><ext:field property="errorCount"/></td>
		</tr>
	</ext:notEmpty>
</table>