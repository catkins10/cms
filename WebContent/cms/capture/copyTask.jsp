<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doCopyTask">
	<script>
		function copyTask() {
			if(document.getElementsByName("description")[0].value=="") {
				alert("任务描述不能为空");
				return;
			}
			if(document.getElementsByName("captureURL")[0].value=="") {
				alert("被抓取页面URL不能为空");
				return;
			}
			FormUtils.doAction("doCopyTask");
		}
	</script>
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<tr>
			<td nowrap="nowrap" align="right">任务描述：</td>
			<td width="100%"><ext:field property="description"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">被抓取页面URL：</td>
			<td width="100%"><ext:field property="captureURL"/></td>
		</tr>
	</table>
</ext:form>