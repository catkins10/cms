<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<ext:equal value="0" property="sourceDirectoryId">
		<tr>
			<td class="tdtitle" nowrap="nowrap">发电企业类型</td>
			<td class="tdcontent"><ext:field property="directoryName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">上级</td>
			<td class="tdcontent">
				<ext:equal value="true" property="changeParentDirectoryDisabled">
					<ext:field writeonly="true" property="parentDirectoryName"/>
				</ext:equal>
				<ext:equal value="false" property="changeParentDirectoryDisabled">
					<ext:field property="parentDirectoryName"/>
				</ext:equal>
			</td>
		</tr>
	</ext:equal>
	<ext:notEqual value="0" property="sourceDirectoryId">
		<tr>
			<td class="tdtitle" nowrap="nowrap">发电企业类型</td>
			<td class="tdcontent"><ext:field writeonly="true" property="directoryName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">上级</td>
			<td class="tdcontent"><ext:field writeonly="true" property="parentDirectoryName"/></td>
		</tr>
	</ext:notEqual>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
</table>