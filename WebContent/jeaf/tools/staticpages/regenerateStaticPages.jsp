<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doRegenerateStaticPages">
   	<script>
		function onRegenerateModeChange(mode) {
			document.getElementById("trRegenerateByUrl").style.display = (mode=="按URL重建" ? "" : "none");
			document.getElementById("trRegenerateByRecord").style.display = (mode=="按记录类名称" ? "" : "none");
			DialogUtils.adjustDialogSize();
		}
	</script>
   	<table border="0" cellpadding="3" cellspacing="3" width="360px">
   		<col align="right">
   		<col width="100%">
   		<tr>
   			<td nowrap="nowrap">更新方式：</td>
   			<td><ext:field property="regenerateMode" onchange="onRegenerateModeChange(value)"/></td>
   		</tr>
   		<tr id="trRegenerateByUrl" style="display:none">
   			<td nowrap="nowrap">动态页面URL(shtml)：</td>
   			<td><ext:field property="dynamicUrl"/></td>
   		</tr>
   		<tr id="trRegenerateByRecord" style="display:none">
   			<td nowrap="nowrap">类名称：</td>
   			<td><ext:field property="recordClassName"/></td>
   		</tr>
   	</table>
</ext:form>