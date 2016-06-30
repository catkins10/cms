<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doCopyTask">
	<script>
		function loadNewTask() {
			top.location.href = "task.shtml?act=edit" + "&id=" + document.getElementsByName("id")[0].value;
		}
	</script>
	<font style="font-size:14px">复制完成！</font>
</ext:form>