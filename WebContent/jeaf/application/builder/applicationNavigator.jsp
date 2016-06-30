<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveApplicationNavigator">
	<script>
		function formOnSubmit() {
			var types = document.getElementsByName('type');
			if(types[0].checked) {
				if(document.getElementsByName("navigator.viewName")[0].value=="") {
					alert("视图未选择");
					return false;
				}
				document.getElementsByName("navigator.url")[0].value = "";
			}
			else if(types[1].checked) {
				if(document.getElementsByName("navigator.url")[0].value=="") {
					alert("链接不能为空");
					document.getElementsByName("navigator.url")[0].focus();
					return false;
				}
				document.getElementsByName("navigator.viewId")[0].value = "";
				document.getElementsByName("navigator.viewName")[0].value = "";
			}
			return true;
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">名称：</td>
			<td><ext:field property="navigator.label"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">类型：</td>
			<td><ext:field property="type" onclick="var types=document.getElementsByName('type');document.getElementById('trView').style.display=(types[0].checked ? '' : 'none');document.getElementById('trLink').style.display=(types[1].checked ? '' : 'none');"/></td>
		</tr>
		<tr id="trView" style="<ext:notEqual value="viewLink" property="type">display:none</ext:notEqual>">
			<td nowrap="nowrap">视图：</td>
			<td><ext:field property="navigator.viewName" onchange="if(document.getElementsByName('navigator.label')[0].value=='')document.getElementsByName('navigator.label')[0].value=document.getElementsByName('navigator.viewName')[0].value;"/></td>
		</tr>
		<tr id="trLink" style="<ext:notEqual value="link" property="type">display:none</ext:notEqual>">
			<td nowrap="nowrap">链接：</td>
			<td><ext:field property="navigator.url"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">访问权限：</td>
			<td><ext:field property="navigator.accessPrivilege"/></td>
		</tr>
	</table>
</ext:form>