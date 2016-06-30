<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="../js/infopublic.js"></script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>

<table valign="middle" border="0" cellpadding="0" cellspacing="1">
	<col align="right">
	<col width="620px">
	<tr>
		<td class="tdtitle" valign="top">名称：</td>
		<td class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">文号：</td>
			<td class="tdcontent"><ext:field property="mark"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">发布机构：</td>
			<td class="tdcontent"><ext:field property="infoFrom"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">主题词：</td>
			<td class="tdcontent"><ext:field property="keywords"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">信息分类：</td>
		<td class="tdcontent"><ext:field property="directoryFullName"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">生成日期：</td>
			<td class="tdcontent"><ext:field property="generateDate" readonly="true"/></td>
		</tr>
	</ext:equal>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">所属其它目录：</td>
		<td class="tdcontent"><ext:field property="otherDirectoryFullNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">同步更新到网站栏目：</td>
		<td class="tdcontent"><ext:field property="issueSite" onclick="showSelectIssueSite()"/></td>
	</tr>
	<tr id="selectIssueSite" style="<ext:notEqual property="issueSite" value="1">display:none</ext:notEqual>">
		<td class="tdtitle" nowrap="nowrap">被同步的网站栏目：</td>
		<td class="tdcontent"><ext:field property="issueSiteNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间：</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间：</td>
		<td class="tdcontent"><ext:field property="issueTime"/></td>
	</tr>
	<ext:equal value="0" property="type">
		<tr>
			<td class="tdtitle" nowrap="nowrap">索引号：</td>
			<td class="tdcontent"><ext:field property="infoIndex"/></td>
		</tr>
		<tr>
			<td class="tdtitle" valign="top">内容概述：</td>
			<td class="tdcontent"><ext:field property="summarize"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">省办统计分类：</td>
		<td class="tdcontent"><ext:field property="category"/></tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent">
			<ext:iterate id="action" property="formActions">
				<input type="button" class="button" onclick="<ext:write name="action" property="execute"/>" value="<ext:write name="action" property="title"/>"/>
			</ext:iterate>
		</td>
	</tr>
	<tr style="height:360px">
		<td class="tdtitle" valign="top">内容：</td>
		<td class="tdcontent"><ext:field property="body"/></td>
	</tr>
</table>
<script>
function showSelectIssueSite() {
	document.getElementById("selectIssueSite").style.display = (document.getElementsByName("issueSite")[0].checked ? "" : "none");
}
</script>