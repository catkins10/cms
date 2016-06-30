<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="../js/infopublic.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" valign="top">名称</td>
		<td class="tdcontent" colspan="3"><ext:field property="subject"/></td>
	</tr>
	<ext:equal value="0" property="type">
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
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属目录</td>
		<td class="tdcontent" colspan="3"><ext:field property="directoryFullName"/></td>
	</tr>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">所属其它目录</td>
		<td class="tdcontent" colspan="3"><ext:field property="otherDirectoryFullNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">同步更新到网站栏目</td>
		<td class="tdcontent" colspan="3"><ext:field property="issueSite" onclick="showSelectIssueSite()"/></td>
	</tr>
	<tr id="selectIssueSite" style="<ext:notEqual property="issueSite" value="1">display:none</ext:notEqual>">
		<td class="tdtitle" nowrap="nowrap">选择被同步的网站栏目</td>
		<td class="tdcontent" colspan="3"><ext:field property="issueSiteNames"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">主题分类</td>
			<td class="tdcontent"><ext:field property="category"/></td>
			<td class="tdtitle" nowrap="nowrap">主题词</td>
			<td class="tdcontent"><ext:field property="keywords"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field property="issueTime"/></td>
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
			<td class="tdcontent" colspan="3"><ext:field property="summarize"/></td>
		</tr>
	</ext:equal>
	<tr style="height:280px">
		<td class="tdtitle" valign="top">内容</td>
		<td colspan="3" class="tdcontent"><ext:field property="body"/></td>
	</tr>
</table>
<script>
function showSelectIssueSite() {
	document.getElementById("selectIssueSite").style.display = (document.getElementsByName("issueSite")[0].checked ? "" : "none");
}
</script>