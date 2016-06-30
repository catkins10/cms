<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<script language="JavaScript" charset="utf-8" src="js/site.js"></script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">简称(英文)</td>
		<td class="tdcontent"><ext:field property="shortName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">LOGO</td>
		<td class="tdcontent"><ext:field property="logo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">描述</td>
		<td class="tdcontent"><ext:field property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上级站点/栏目</td>
		<td class="tdcontent">
			<ext:equal value="true" property="changeParentDirectoryDisabled">
				<ext:field writeonly="true" property="parentDirectoryName"/>
			</ext:equal>
			<ext:equal value="false" property="changeParentDirectoryDisabled">
				<ext:field property="parentDirectoryName"/>
			</ext:equal>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">同步到其他栏目</td>
		<td class="tdcontent"><ext:field property="synchToDirectoryNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">同步到本栏目的其他栏目</td>
		<td class="tdcontent"><ext:field property="synchFromDirectoryNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">文章发布流程</td>
		<td class="tdcontent"><ext:field property="workflowName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">站点编辑删除文章</td>
		<td class="tdcontent"><ext:field property="editorDeleteable"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">站点编辑撤销文章</td>
		<td class="tdcontent"><ext:field property="editorReissueable"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">匿名用户资源访问级别</td>
		<td class="tdcontent"><ext:field property="anonymousLevel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">同步的文章发布</td>
		<td class="tdcontent"><ext:field property="synchIssue"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否停用</td>
		<td class="tdcontent"><ext:field property="halt"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">重定向的URL</td>
		<td class="tdcontent"><ext:field property="redirectUrl"/></td>
	</tr>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>