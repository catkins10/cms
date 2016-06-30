<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">发送范围</td>
		<td class="tdcontent"><ext:field writeonly="true" property="rangeMode"/></td>
	</tr>
	<ext:notEqual value="0" property="rangeMode">
		<tr>
			<td class="tdtitle" nowrap="nowrap">分组</td>
			<td class="tdcontent"><ext:field writeonly="true" property="groupNames"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">用户</td>
			<td class="tdcontent"><ext:field writeonly="true" property="userNames"/></td>
		</tr>
	</ext:notEqual>
	<jsp:include page="messageContentRead.jsp" flush="true"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<ext:notEmpty property="sendTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">发送时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="sendTime"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">状态</td>
			<td class="tdcontent"><ext:field writeonly="true" property="status"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">用户数</td>
			<td class="tdcontent"><ext:field writeonly="true" property="totalCount"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">发送成功用户数</td>
			<td class="tdcontent"><ext:field writeonly="true" property="sentCount"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">发送失败用户数</td>
			<td class="tdcontent"><ext:field writeonly="true" property="errorCount"/></td>
		</tr>
	</ext:notEmpty>
</table>