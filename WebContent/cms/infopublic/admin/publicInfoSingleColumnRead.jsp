<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" border="0" cellpadding="0" cellspacing="1">
	<col align="right">
	<col width="620px">
	<tr>
		<td class="tdtitle" valign="top">名称：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="subject"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">文号：</td>
			<td class="tdcontent"><ext:field writeonly="true" property="mark"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">发布机构：</td>
			<td class="tdcontent"><ext:field writeonly="true" property="infoFrom"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">主题词：</td>
			<td class="tdcontent"><ext:field writeonly="true" property="keywords"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">信息分类：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="directoryFullName"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">生成日期：</td>
			<td class="tdcontent"><ext:field writeonly="true" property="generateDate"/></td>
		</tr>
	</ext:equal>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">所属其它目录：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="otherDirectoryFullNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">同步更新到网站栏目：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="issueSite"/></td>
	</tr>
	<tr id="selectIssueSite" style="<ext:notEqual property="issueSite" value="1">display:none</ext:notEqual>">
		<td class="tdtitle" nowrap="nowrap">被同步的网站栏目：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="issueSiteNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="issueTime"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">索引号：</td>
			<td class="tdcontent"><ext:field writeonly="true" property="infoIndex"/></td>
		</tr>
		<tr>
			<td class="tdtitle" valign="top">内容概述：</td>
			<td class="tdcontent"><ext:field writeonly="true" property="summarize"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">省办统计分类：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">内容：</td>
		<td class="tdcontent"><ext:field writeonly="true" property="body"/></td>
	</tr>
</table>