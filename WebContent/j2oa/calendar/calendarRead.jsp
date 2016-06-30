<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">内容</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">地点</td>
		<td class="tdcontent"><ext:field writeonly="true" property="address"/></td>
		<td class="tdtitle" nowrap="nowrap">重要性</td>
		<td class="tdcontent"><ext:field writeonly="true" property="important"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">参加人</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="leaders.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开始时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="beginTime"/></td>
		<td class="tdtitle" nowrap="nowrap">结束时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主办部门</td>
		<td class="tdcontent"><ext:field writeonly="true" property="department"/></td>
		<td class="tdtitle" nowrap="nowrap">经办人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creatorName"/></td>
	</tr>
</table>