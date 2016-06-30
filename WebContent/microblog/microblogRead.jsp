<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">微博帐号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="accountIdArray"/></td>
	</tr>
	<ext:equal value="0" property="privateMessageId">
		<tr style="display:none">
			<td class="tdtitle" nowrap="nowrap">发送范围</td>
			<td class="tdcontent"><ext:field writeonly="true" property="range"/></td>
		</tr>
		<tr id="trGroups" style="<ext:notEqual value="groups" property="range">display:none</ext:notEqual>">
			<td class="tdtitle" nowrap="nowrap">分组</td>
			<td class="tdcontent"><ext:field writeonly="true" property="groupNames"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">微博内容</td>
		<td class="tdcontent" style="font-size: 14px; line-height: 18px;"><ext:field writeonly="true" property="content"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">图片</td>
		<td class="tdcontent"><ext:field writeonly="true" property="image"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sendTime"/></td>
	</tr>
</table>