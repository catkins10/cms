<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="../js/forum.js"></script>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" class="tdtitle">名称</td>
		<td class="tdcontent"><ext:field property="directoryName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">描述</td>
		<td class="tdcontent"><ext:field property="description"/></td>
	</tr>
	<ext:notEqual value="0" property="id">
		<tr>
			<td nowrap="nowrap" class="tdtitle">上级</td>
			<td class="tdcontent">
				<ext:equal value="true" property="changeParentDirectoryDisabled">
					<ext:field writeonly="true" property="parentDirectoryName"/>
				</ext:equal>
				<ext:equal value="false" property="changeParentDirectoryDisabled">
					<ext:field property="parentDirectoryName"/>
				</ext:equal>
			</td>
		</tr>
	</ext:notEqual>
	<jsp:include page="popedomConfigEdit.jsp"/>
</table>