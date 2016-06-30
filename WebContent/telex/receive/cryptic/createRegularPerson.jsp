<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" cellpadding="2" cellspacing="0" align="center" width="300px">
	<col width="90px" align="right">
	<col width="100%">
	<tr height="30px">
		<td nowrap><b>姓　　名:</b></td>
		<td><ext:write property="name"/></td>
	</tr>
	<tr height="30px">
		<td nowrap><b>所在单位:</b></td>
		<td><ext:write property="orgName"/></td>
	</tr>
	<tr height="30px">
		<td nowrap><b>登录用户名:</b></td>
		<td><ext:field property="loginName"/></td>
	</tr>
	<tr height="30px">
		<td nowrap><b>登录密码:</b></td>
		<td><ext:field property="password"/></td>
	</tr>
</table>
<script>
	function formOnSubmit() { //提交校验样本数据
		if(document.getElementsByName("loginName")[0].value=="") {
			alert("登录用户名不能为空");
			return false;
		}
		if(document.getElementsByName("password")[0].value=="") {
			alert("登录密码不能为空");
			return false;
		}
		return true;
	}
</script>