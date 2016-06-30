<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function publicPreapproval() {
	if('<ext:empty property="preapproval.body">true</ext:empty>'=='true') {
		alert('公告内容不能为空。');
		return;
	}
	FormUtils.doAction('publicPreapproval');
}
</script>
<div style="width:380px">
	<br>
	<div style="font-size:14px">
		&nbsp;&nbsp;是否确定发布预审公示?
	</div>
	<br>
</div>