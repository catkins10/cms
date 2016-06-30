<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doCreateViews">
	<script type="text/javascript" src="<%=request.getContextPath()%>/jeaf/application/builder/js/builder.js"></script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">视图字段：</td>
			<td><ext:field property="view.viewFieldNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top: 8px">排序字段列表：</td>
			<td>
				<span id="spanSortFields"></span>
				<input type="button" class="button" onclick="setFields('view.sortField', '<ext:write property="id"/>', 'spanSortFields')" value="选择"/>
			</td>
		</tr>
	</table>
	<script>writeFields('view.sortField', 'spanSortFields');</script>
</ext:form>