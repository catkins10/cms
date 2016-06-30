<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">开始时间</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">结束时间</td>
		<td class="tdtitle" nowrap="nowrap" width="33%">项目名称</td>
		<td class="tdtitle" nowrap="nowrap" width="33%">项目描述</td>
		<td class="tdtitle" nowrap="nowrap" width="33%">责任描述</td>
	</tr>
	<ext:iterate id="project" property="projects">
		<tr align="center">
			<td class="tdcontent"><ext:field writeonly="true" name="project" property="startDate"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="project" property="endDate"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="project" property="projectName"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="project" property="description"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="project" property="duty"/></td>
		</tr>
	</ext:iterate>
</table>