<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function publicCompletion() {
	<ext:empty property="completion.completeDate">
		alert('竣工时间未设置。');
		return;
	</ext:empty>
	FormUtils.doAction('publicCompletion');
}
</script>
<div style="width:380px">
	<br>
	<div style="font-size:14px">
		&nbsp;&nbsp;是否确定竣工?
	</div>
	<br>
</div>