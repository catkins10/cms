<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<col>
	<col width="33%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent" colspan="5"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发生时间</td>
		<td class="tdcontent"><ext:field property="time"/></td>
		<td class="tdtitle" nowrap="nowrap">发布人</td>
		<td class="tdcontent"><ext:field property="creatorName"/></td>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent" colspan="5"><ext:field property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">详细描述</td>
		<td colspan="5" class="tdcontent"><ext:field property="content"/></td>
	</tr>
</table>