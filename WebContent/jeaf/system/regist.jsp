<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doRegist">
	<script>
		function doRegist() {
			if(document.getElementsByName("sn")[0].value=="") {
				alert('注册码未设置。');
				return;
			}
			document.forms[0].submit();
		}
	</script>
	<table width="360px" border="0" cellpadding="3" cellspacing="0">
		<col valign="middle" align="right">
		<col valign="middle" width="100%">
		<tr>
			<td nowrap="nowrap">系统编码：</td>
			<td><ext:write property="code"/></td>
		</tr>
		<tr>
			<td>注 册 码：</td>
			<td><html:text styleClass="field" property="sn"/></td>
		</tr>
	</table>
</ext:form>