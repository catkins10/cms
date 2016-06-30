<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/cms/sitemanage/js/site.js"></script>
<script language="JavaScript" charset="utf-8" src="../js/infopublic.js"></script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">类目代码</td>
		<td class="tdcontent"><ext:field property="code"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">描述</td>
		<td class="tdcontent"><ext:field property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">上级目录</td>
		<td class="tdcontent" nowrap="nowrap">
			<ext:equal value="true" property="changeParentDirectoryDisabled">
				<ext:field writeonly="true" property="parentDirectoryName"/>
			</ext:equal>
			<ext:equal value="false" property="changeParentDirectoryDisabled">
				<ext:field property="parentDirectoryName"/>
			</ext:equal>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">同步到网站栏目</td>
		<td class="tdcontent"><ext:field property="synchSiteNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">信息发布流程</td>
		<td class="tdcontent"><ext:field property="workflowName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">信息编辑删除信息</td>
		<td class="tdcontent"><ext:field property="editorDeleteable"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">信息编辑撤销信息</td>
		<td class="tdcontent"><ext:field property="editorReissueable"/></td>
	</tr>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
</table>