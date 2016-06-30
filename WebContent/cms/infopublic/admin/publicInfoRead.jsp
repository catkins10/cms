<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="../js/infopublic.js"></script>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">索引号</td>
			<td class="tdcontent"><ext:field writeonly="true" property="infoIndex"/></td>
			<td class="tdtitle" nowrap="nowrap">发布机构</td>
			<td class="tdcontent"><ext:field writeonly="true" property="infoFrom"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">备注/文号</td>
			<td class="tdcontent"><ext:field writeonly="true" property="mark"/></td>
			<td class="tdtitle" nowrap="nowrap">生成日期</td>
			<td class="tdcontent"><ext:field writeonly="true" property="generateDate"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属目录</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="directoryFullName"/></td>
	</tr>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">所属其它目录</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="otherDirectoryFullNames"/></td>
	</tr>
	<ext:equal property="issueSite" value="1">
		<tr>
			<td class="tdtitle" nowrap="nowrap">被同步的网站栏目</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="issueSiteNames"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="issueTime"/></td>
	</tr>
	<ext:notEmpty property="readers.visitorNames">
		<tr>
			<td class="tdtitle">访问者</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="readers.visitorNames"/></td>
		</tr>
	</ext:notEmpty>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" valign="top">内容概述</td>
			<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="summarize"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" valign="top">内容</td>
		<td colspan="3" class="tdcontent">
			<span class="content"><ext:field writeonly="true" property="body"/></span>
		</td>
	</tr>
</table>