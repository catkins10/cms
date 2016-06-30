<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveView">
	<script type="text/javascript" src="<%=request.getContextPath()%>/jeaf/application/builder/js/builder.js"></script>
	<script>
		function formOnSubmit() {
			if(FieldValidator.validateStringField(document.getElementsByName("view.name")[0], "&,?,=,\",'", true, "名称")=="NaN") {
				return false;
			}
			var englishName = document.getElementsByName("view.englishName")[0].value;
			if(englishName=="") {
				alert("英文名称不能为空");
				return false;
			}
			if(englishName.replace(/[\dA-Za-z]/gi, "")!="") {
				alert("英文名称必须由数字和字母组成");
				return false;
			}
			if(englishName.charAt(0)>='0' && englishName.charAt(0)<='9') {
				alert("英文名称不能以数字开头");
				return false;
			}
			return true;
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">视图名称：</td>
			<td><ext:field property="view.name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">英文名称：</td>
			<td><ext:field property="view.englishName"/></td>
		</tr>
		<ext:equal value="true" property="workflowSupport">
			<tr>
				<td nowrap="nowrap">过滤方式：</td>
				<td><ext:field property="view.filterMode"/></td>
			</tr>
		</ext:equal>
		<tr>
			<td nowrap="nowrap">条件(WHERE)：</td>
			<td><ext:field property="view.hqlWhere"/></td>
		</tr>
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
		<tr>
			<td nowrap="nowrap">访问权限：</td>
			<td><ext:field property="view.accessPrivilege"/></td>
		</tr>
	</table>
	<script>writeFields('view.sortField', 'spanSortFields');</script>
</ext:form>