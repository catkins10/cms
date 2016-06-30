<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<div style="width:360px">
	<ext:empty name="batchSendScript">
		<br>
		<b>&nbsp;&nbsp;操作不能完成</b>
		<br><br>
	</ext:empty>
	<ext:notEmpty name="batchSendScript">
		<br>
		<b>&nbsp;&nbsp;操作未完成</b>
		<br><br>
		<script language="javascript">
			<ext:write name="batchSendScript" filter="false"/>
		</script>
	</ext:notEmpty>
</div>