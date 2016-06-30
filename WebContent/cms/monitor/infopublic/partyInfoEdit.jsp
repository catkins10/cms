<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent" colspan="3"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">索引号</td>
		<td class="tdcontent"><ext:field property="infoIndex"/></td>
		<td class="tdtitle" nowrap="nowrap">发布机构</td>
		<td class="tdcontent"><ext:field property="infoFrom"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注/文号</td>
		<td class="tdcontent"><ext:field property="mark"/></td>
		<td class="tdtitle" nowrap="nowrap">生成日期</td>
		<td class="tdcontent"><ext:field property="generateDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属目录</td>
		<td class="tdcontent" colspan="3"><ext:field property="directoryName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field property="issueTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">内容概述</td>
		<td class="tdcontent" colspan="3"><ext:field property="summarize"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">内容</td>
		<td colspan="3" class="tdcontent">
			<span class="content"><ext:field property="body"/></span>
		</td>
	</tr>
</table>