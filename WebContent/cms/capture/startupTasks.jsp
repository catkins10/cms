<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doStartupTasks">
	<div style="border: #cccccc 1px solid; background-color: #ffffff; overflow: auto; width:100%; height: 220px;">
		<table style="table-layout:fixed" class="view" width="100%" border="0" cellpadding="0" cellspacing="1">
			<col width="36px" class="viewColEven">
			<col width="100%" class="viewColOdd">
			<tr height="23px">
				<td class="viewHeader" align="center">选择</td>
				<td class="viewHeader" align="center">任务描述</td>
			</tr>
			<ext:iterate id="task" property="startupTasks">
				<tr>
					<td class="viewData" align="center"><input type="checkbox" name="taskId" value="<ext:write name="task" property="id"/>" checked="checked"/></td>
					<td class="viewData" align="left"><ext:write name="task" property="description"/></td>
				</tr>
			</ext:iterate>
		</table>
	</div>
</ext:form>